/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.copesa.model;

import java.math.BigDecimal;

import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 *	Validator for COPESA
 *
 *  @author Italo Niñoles
 */
public class ModCOPESADivideFull implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCOPESADivideFull ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCOPESADivideFull.class);
	/** Client			*/
	private int		m_AD_Client_ID = -1;
	

	/**
	 *	Initialize Validation
	 *	@param engine validation engine
	 *	@param client client
	 */
	public void initialize (ModelValidationEngine engine, MClient client)
	{
		//client = null for global validator
		if (client != null) {
			m_AD_Client_ID = client.getAD_Client_ID();
			log.info(client.toString());
		}
		else  {
			log.info("Initializing Model Price Validator: "+this.toString());
		}

		//	Tables to be monitored
		engine.addModelChange(MOrder.Table_Name, this);		
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By Julio Farías
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if((type == TYPE_AFTER_CHANGE || type == TYPE_AFTER_NEW) && po.get_Table_ID()==MOrder.Table_ID && po.is_ValueChanged("DocStatus")) 
		{	
			MOrder order = (MOrder)po;
			if (order.isSOTrx() && order.getDocStatus().compareToIgnoreCase("IP") == 0)
			{
				//buscamos ID linea de producto full
				int ID_LineProdFull = 0;
				ID_LineProdFull = DB.getSQLValue(po.get_TrxName(),"SELECT MAX(C_OrderLine_ID) FROM C_OrderLine " +
						" WHERE IsActive = 'Y' AND C_Order_ID = "+order.get_ID()+" AND M_Product_ID = 2000003");
				if(ID_LineProdFull > 0)
				{
					int ID_LV = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(M_Product_ID) FROM M_Product WHERE upper(Description) like '%FULLLV%'");
					MProduct prodLV = new MProduct(po.getCtx(), ID_LV, po.get_TrxName());
					int ID_SD = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(M_Product_ID) FROM M_Product WHERE upper(Description) like '%FULLSD%'");
					MProduct prodSD = new MProduct(po.getCtx(), ID_SD, po.get_TrxName());
					if(ID_LV > 0 && ID_SD > 0)
					{
						//actualizamos la linea existente con producto L-V
						if(prodLV.get_ValueAsInt("C_CalendarCOPESA_ID") > 0)
							DB.executeUpdate("UPDATE C_OrderLine SET M_Product_ID = "+ID_LV+", C_CalendarCOPESA_ID = "+prodLV.get_ValueAsInt("C_CalendarCOPESA_ID")+" WHERE C_OrderLine_ID = "+ID_LineProdFull, po.get_TrxName());
						else
							DB.executeUpdate("UPDATE C_OrderLine SET M_Product_ID = "+ID_LV+" WHERE C_OrderLine_ID = "+ID_LineProdFull, po.get_TrxName());
						//se actualiza precio mensual L-V
						MOrderLine oLineOld = new MOrderLine(po.getCtx(), ID_LineProdFull, po.get_TrxName());
						int ID_Level = oLineOld.get_ValueAsInt("LevelNo");
						BigDecimal newAmtPAT = null;
						if(ID_Level == 1)
						{
							newAmtPAT= DB.getSQLValueBD(po.get_TrxName(), "SELECT MAX(PricePAT) as PricePAT " +
									" FROM M_ProductPrice pp " +
									" INNER JOIN M_PriceList_Version plv ON pp.M_PriceList_Version_ID = plv.M_PriceList_Version_ID " +
									" INNER JOIN M_PriceList pl ON plv.M_PriceList_ID = pl.M_PriceList_ID " +
									" WHERE pp.IsActive = 'Y' AND M_product_ID = "+ID_LV+
									" AND Levels = "+ID_Level+" AND pl.M_priceList_ID = "+order.getM_PriceList_ID());
						}else if (ID_Level > 1)
						{
							newAmtPAT = DB.getSQLValueBD(po.get_TrxName(), "SELECT MAX(PricePAT) as PricePAT " +
									" FROM M_ProductPriceRef pp " +
									" INNER JOIN M_PriceList_Version plv ON pp.M_PriceList_Version_ID = plv.M_PriceList_Version_ID " +
									" INNER JOIN M_PriceList pl ON plv.M_PriceList_ID = pl.M_PriceList_ID " +
									" WHERE pp.IsActive = 'Y' AND M_product_ID = "+ID_LV+
									" AND Levels = "+ID_Level+" AND pl.M_priceList_ID = "+order.getM_PriceList_ID());
						}
						if(newAmtPAT != null)
							DB.executeUpdate("UPDATE C_OrderLine SET monthlyAmount = "+newAmtPAT+" WHERE C_OrderLine_ID = "+ID_LineProdFull, po.get_TrxName());
							
						//creamos nueva linea con producto S-D Precio 0
						MOrderLine oLineBase = new MOrderLine(po.getCtx(),ID_LineProdFull,po.get_TrxName());
						MOrderLine oLine = new MOrderLine(order);
						oLine.setAD_Org_ID(order.getAD_Org_ID());
						oLine.setC_BPartner_Location_ID(oLineBase.getC_BPartner_Location_ID());
						//oLine.set_CustomColumn("AD_User_ID", oLineBase.get_ValueAsInt("AD_User_ID"));
						oLine.set_CustomColumn("C_BPartnerRef_ID", oLineBase.get_ValueAsInt("C_BPartnerRef_ID"));
						oLine.setM_Product_ID(ID_SD);
						oLine.setQty(Env.ONE);
						oLine.setPrice(Env.ZERO);
						//se acualiza precio mensual S-D
						BigDecimal newAmtPAT2 = null;
						if(ID_Level == 1)
						{
							newAmtPAT2= DB.getSQLValueBD(po.get_TrxName(), "SELECT MAX(PricePAT) as PricePAT " +
									" FROM M_ProductPrice pp " +
									" INNER JOIN M_PriceList_Version plv ON pp.M_PriceList_Version_ID = plv.M_PriceList_Version_ID " +
									" INNER JOIN M_PriceList pl ON plv.M_PriceList_ID = pl.M_PriceList_ID " +
									" WHERE pp.IsActive = 'Y' AND M_product_ID = "+ID_SD+
									" AND Levels = "+ID_Level+" AND pl.M_priceList_ID = "+order.getM_PriceList_ID());
						}else if (ID_Level > 1)
						{
							newAmtPAT2 = DB.getSQLValueBD(po.get_TrxName(), "SELECT MAX(PricePAT) as PricePAT " +
									" FROM M_ProductPriceRef pp " +
									" INNER JOIN M_PriceList_Version plv ON pp.M_PriceList_Version_ID = plv.M_PriceList_Version_ID " +
									" INNER JOIN M_PriceList pl ON plv.M_PriceList_ID = pl.M_PriceList_ID " +
									" WHERE pp.IsActive = 'Y' AND M_product_ID = "+ID_SD+
									" AND Levels = "+ID_Level+" AND pl.M_priceList_ID = "+order.getM_PriceList_ID());
						}
						if(newAmtPAT2 != null)
							oLine.set_CustomColumn("MonthlyAmount",newAmtPAT2);							
						oLine.save();
						if(prodSD.get_ValueAsInt("C_CalendarCOPESA_ID") > 0)
						{
							oLine.set_CustomColumn("C_CalendarCOPESA_ID", prodSD.get_ValueAsInt("C_CalendarCOPESA_ID"));
							oLine.save();
						}
					}	
				}
			}
		}		
	return null;
	}	//	modelChange

	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);
		
		return null;
	}	//	docValidate
	
	/**
	 *	User Login.
	 *	Called when preferences are set
	 *	@param AD_Org_ID org
	 *	@param AD_Role_ID role
	 *	@param AD_User_ID user
	 *	@return error message or null
	 */
	public String login (int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		log.info("AD_User_ID=" + AD_User_ID);

		return null;
	}	//	login


	/**
	 *	Get Client to be monitored
	 *	@return AD_Client_ID client
	 */
	public int getAD_Client_ID()
	{
		return m_AD_Client_ID;
	}	//	getAD_Client_ID


	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("ModelPrice");
		return sb.toString ();
	}	//	toString


	

}	