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
import java.sql.Timestamp;
import java.util.Calendar;

import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Validator for COPESA
 *
 *  @author Italo Ni�oles
 */
public class ModCOPESAOLineFree implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCOPESAOLineFree ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCOPESAOLineFree.class);
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
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE)&& po.get_Table_ID()==MOrder.Table_ID) 
		{	
			MOrder order = (MOrder)po;
			if (order.isSOTrx() && order.getDocStatus().compareToIgnoreCase("IP") == 0)
			{
				if (order.isSOTrx() && order.getDocStatus().compareToIgnoreCase("CO") != 0)
				{
					MOrderLine[] lines = order.getLines();
					for (int i = 0; i < lines.length; i++)
					{
						MOrderLine oLine = lines[i];
						int cantRef = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) FROM C_OrderLine " +
								" WHERE C_OrderLineRef_ID = "+oLine.get_ID());
						if(!oLine.get_ValueAsBoolean("IsFree") && cantRef < 1 && oLine.getM_Product_ID() > 0)
						{
							int cant = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(FreeDays) as FreeDays " +
									" FROM M_ProductPrice pp " +
									" INNER JOIN M_PriceList_Version plv ON pp.M_PriceList_Version_ID = plv.M_PriceList_Version_ID " +
									" INNER JOIN M_PriceList pl ON plv.M_PriceList_ID = pl.M_PriceList_ID " +
									" WHERE pp.IsActive = 'Y' AND M_product_ID = "+oLine.getM_Product_ID()+
									" AND pl.M_priceList_ID = "+order.getM_PriceList_ID());
							//ininoles consultamos monto
							BigDecimal newAmt = DB.getSQLValueBD(po.get_TrxName(), "SELECT MAX(FreeAmt) as FreeAmt " +
									" FROM M_ProductPrice pp " +
									" INNER JOIN M_PriceList_Version plv ON pp.M_PriceList_Version_ID = plv.M_PriceList_Version_ID " +
									" INNER JOIN M_PriceList pl ON plv.M_PriceList_ID = pl.M_PriceList_ID " +
									" WHERE pp.IsActive = 'Y' AND M_product_ID = "+oLine.getM_Product_ID()+
									" AND pl.M_priceList_ID = "+order.getM_PriceList_ID());
							if(newAmt == null )
								newAmt = Env.ONE;
							if(newAmt != null && newAmt.compareTo(Env.ZERO) <= 0)
								newAmt = Env.ONE;
							//precio gracia mensual PAT
							//ininoles consultamos monto
							BigDecimal newAmtPAT = DB.getSQLValueBD(po.get_TrxName(), "SELECT MAX(PricePATFree) as FreeAmt " +
									" FROM M_ProductPrice pp " +
									" INNER JOIN M_PriceList_Version plv ON pp.M_PriceList_Version_ID = plv.M_PriceList_Version_ID " +
									" INNER JOIN M_PriceList pl ON plv.M_PriceList_ID = pl.M_PriceList_ID " +
									" WHERE pp.IsActive = 'Y' AND M_product_ID = "+oLine.getM_Product_ID()+
									" AND pl.M_priceList_ID = "+order.getM_PriceList_ID());
							if(newAmtPAT == null )
								newAmtPAT = Env.ZERO;
							if(newAmtPAT != null && newAmt.compareTo(Env.ZERO) <= 0)
								newAmtPAT = Env.ZERO;
							
							if(cant >= 0)
							{
								MOrderLine oLineNew = new MOrderLine(order);
								oLineNew.setAD_Org_ID(oLine.getAD_Org_ID());
								oLineNew.setC_BPartner_Location_ID(oLine.getC_BPartner_Location_ID());
								oLineNew.set_CustomColumn("C_BPartnerRef_ID", oLine.get_ValueAsInt("C_BPartnerRef_ID"));
								oLineNew.setM_Product_ID(oLine.getM_Product_ID());
								oLineNew.setQty(oLine.getQtyEntered());
								oLineNew.set_CustomColumn("C_CalendarCOPESA_ID", oLine.get_ValueAsInt("C_CalendarCOPESA_ID"));
								//ininoles seteamos nuevo monto
								//oLineNew.setPrice(Env.ONE);
								oLineNew.setPrice(newAmt);
								oLineNew.set_CustomColumn("C_OrderLineRef_ID", oLine.get_ID());
								oLineNew.set_CustomColumn("IsFree", true);
								//oLineNew.set_CustomColumn("DatePromised2", order.getDateOrdered());
								oLineNew.set_CustomColumn("DatePromised2", order.getDatePromised());
								//se suman dias a fecha fin
								//ininoles nueva validacion y cambios para fecha fin
								Calendar calendar = Calendar.getInstance();
								calendar.setTimeInMillis(order.getDateOrdered().getTime());
								if(cant > 0)
								{
									calendar.add(Calendar.DATE, cant);
									Timestamp datEnd = new Timestamp(calendar.getTimeInMillis());					
									oLineNew.set_CustomColumn("DatePromised3", datEnd);
								}else if(cant == 0){									
									oLineNew.set_CustomColumn("DatePromised3", order.getDateOrdered());
								}						
								oLineNew.save();
								//campo gracia
								if(newAmtPAT != null && newAmtPAT.compareTo(Env.ZERO) > 0)
								{
									oLineNew.set_CustomColumn("MonthlyAmount",newAmtPAT);
									oLineNew.save();
								}
								//actualizamos fecha de inicio de linea base
								calendar.add(Calendar.DATE, 1);
								oLine.set_CustomColumn("DatePromised2",new Timestamp(calendar.getTimeInMillis()));
								oLine.set_CustomColumn("DatePromised3",null);		
								oLine.set_CustomColumn("C_OrderLineRef_ID", oLineNew.get_ID());												
								oLine.save();																						
							}
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
