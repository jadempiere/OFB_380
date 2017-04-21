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
 *  @author Italo Ni�oles
 */
public class ModCOPESAUpdateCalendarOLine implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCOPESAUpdateCalendarOLine ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCOPESAUpdateCalendarOLine.class);
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
		//engine.addDocValidate(MOrder.Table_Name, this);		
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By Julio Far�as
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
					int cant = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) FROM C_OrderLine ol" +
							" INNER JOIN M_Product mp ON (ol.M_Product_ID = mp.M_Product_ID) " +
							" WHERE mp.IsPrimaryCalendar = 'Y' AND C_Order_ID = "+order.get_ID());
					
					/*int cantSec = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) FROM C_OrderLine ol" +
							" INNER JOIN M_Product mp ON (ol.M_Product_ID = mp.M_Product_ID) " +
							" WHERE mp.C_CalendarCOPESARef_ID IS NOT NULL AND mp.IsPrimaryCalendar <> 'Y' " +
							" AND C_Order_ID = "+order.get_ID());*/
					
					/*int ID_Location = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(C_BPartner_Location_ID) FROM C_OrderLine ol" +
							" INNER JOIN M_Product mp ON (ol.M_Product_ID = mp.M_Product_ID) " +
							" WHERE mp.IsPrimaryCalendar = 'Y' AND C_Order_ID = "+order.get_ID());
					*/
					/*int ID_CalP = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(ol.C_CalendarCOPESA_ID) FROM C_OrderLine ol" +
							" INNER JOIN M_Product mp ON (ol.M_Product_ID = mp.M_Product_ID) " +
							" WHERE ol.IsActive = 'Y' AND mp.IsPrimaryCalendar = 'Y' AND C_Order_ID = "+order.get_ID());
					*/
					//Se saca validaci�n
					/*if(cantSec > 0 && cant <= 0)
						return "No Existe producto con calendario primario";
					*/
					if(cant > 0)
					{
						//se actualizan calendarios secundarios
						MOrderLine[] lines = order.getLines(true, null);	//	Line is default
						for (int i = 0; i < lines.length; i++)
						{
							MOrderLine line = lines[i];
							if(line.getM_Product_ID() > 0)
							{
								MProduct prod = new MProduct(po.getCtx(), line.getM_Product_ID(), po.get_TrxName());
								/*if(prod.get_ValueAsBoolean("IsPrimaryCalendar") == false 
										&& line.getC_BPartner_Location_ID() == ID_Location && ID_CalP > 0 && line.get_ValueAsInt("C_CalendarCOPESA_ID") > 0)
								{
									//tambien validamos en base a funcion nueva bitand
									String flag = DB.getSQLValueString(po.get_TrxName()," SELECT cal_contains("+ID_CalP+","+line.get_ValueAsInt("C_CalendarCOPESA_ID")+") " +
											" FROM C_CalendarCOPESA WHERE C_CalendarCOPESA_ID = "+line.get_ValueAsInt("C_CalendarCOPESA_ID"));
									if(prod.get_ValueAsInt("C_CalendarCOPESARef_ID") > 0 && flag.toUpperCase().compareTo("F") == 0)								
									{
										DB.executeUpdate("UPDATE C_OrderLine SET C_CalendarCOPESA_ID = "+prod.get_ValueAsInt("C_CalendarCOPESARef_ID")
											+" WHERE C_OrderLine_ID = "+line.get_ID(),po.get_TrxName());
									}
								}*/
								if(prod.get_ValueAsBoolean("IsPrimaryCalendar") == false)
								{
									int cantContain = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) FROM C_OrderLine ol" +
											" INNER JOIN M_Product mp ON (ol.M_Product_ID = mp.M_Product_ID) " +
											" WHERE C_Order_ID = "+order.get_ID()+" AND mp.IsPrimaryCalendar = 'Y' " +
											" AND ol.IsActive = 'Y' AND ol.C_CalendarCOPESA_ID IS NOT NULL " +
											" AND cal_contains(ol.C_CalendarCOPESA_ID,"+line.get_ValueAsInt("C_CalendarCOPESA_ID")+") = 't'");
									if(cantContain <= 0 && prod.get_ValueAsInt("C_CalendarCOPESARef_ID") > 0 )
									{	
										DB.executeUpdate("UPDATE C_OrderLine SET C_CalendarCOPESA_ID = "+prod.get_ValueAsInt("C_CalendarCOPESARef_ID")
												+" WHERE C_OrderLine_ID = "+line.get_ID(),po.get_TrxName());
									}
								}								
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
		
		/*if(timing == TIMING_BEFORE_COMPLETE && po.get_Table_ID()==MOrder.Table_ID)
		{
			MOrder order = (MOrder)po;
			int cant = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) FROM C_OrderLine ol" +
					" INNER JOIN M_Product mp ON (ol.M_Product_ID = mp.M_Product_ID) " +
					" WHERE mp.IsPrimaryCalendar = 'Y' AND C_Order_ID = "+order.get_ID());
			
			int cantSec = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) FROM C_OrderLine ol" +
					" INNER JOIN M_Product mp ON (ol.M_Product_ID = mp.M_Product_ID) " +
					" WHERE mp.C_CalendarCOPESARef_ID IS NOT NULL AND mp.IsPrimaryCalendar <> 'Y' " +
					" AND C_Order_ID = "+order.get_ID());
			
			int ID_Location = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(C_BPartner_Location_ID) FROM C_OrderLine ol" +
					" INNER JOIN M_Product mp ON (ol.M_Product_ID = mp.M_Product_ID) " +
					" WHERE mp.IsPrimaryCalendar = 'Y' AND C_Order_ID = "+order.get_ID());
			
			int ID_CalP = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(C_CalendarCOPESA_ID) FROM C_OrderLine ol" +
					" INNER JOIN M_Product mp ON (ol.M_Product_ID = mp.M_Product_ID) " +
					" WHERE ol.IsActive = 'Y' AND mp.IsPrimaryCalendar = 'Y' AND C_Order_ID = "+order.get_ID());
			
			//int ID_Product = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(M_Product_ID) FROM C_OrderLine ol" +
			//		" WHERE ol.C_Order_ID = "+order.get_ID()+" AND C_BPartner_Location_ID = "+ID_Location);
			
			if(cantSec > 0 && cant <= 0)
				return "No Existe producto con calendario primario";
			
			if(cant > 0)
			{
				//se actualizan calendarios secundarios
				MOrderLine[] lines = order.getLines(true, null);	//	Line is default
				for (int i = 0; i < lines.length; i++)
				{
					MOrderLine line = lines[i];
					if(line.getM_Product_ID() > 0)
					{
						MProduct prod = new MProduct(po.getCtx(), line.getM_Product_ID(), po.get_TrxName());
						if(prod.get_ValueAsBoolean("IsPrimaryCalendar") == false 
								&& line.getC_BPartner_Location_ID() == ID_Location && ID_CalP > 0 && line.get_ValueAsInt("C_CalendarCOPESA_ID") > 0)
						{
							//tambien validamos en base a funcion nueva bitand
							String flag = DB.getSQLValueString(po.get_TrxName()," SELECT cal_contains("+ID_CalP+","+line.get_ValueAsInt("C_CalendarCOPESA_ID")+") " +
									" FROM C_CalendarCOPESA WHERE C_CalendarCOPESA_ID = "+line.get_ValueAsInt("C_CalendarCOPESA_ID"));
							if(prod.get_ValueAsInt("C_CalendarCOPESARef_ID") > 0 && flag.toUpperCase().compareTo("F") == 0)								
							{
								DB.executeUpdate("UPDATE C_OrderLine SET C_CalendarCOPESA_ID = "+prod.get_ValueAsInt("C_CalendarCOPESARef_ID")
									+" WHERE C_OrderLine_ID = "+line.get_ID(),po.get_TrxName());
							}
						}
					}
				}
			}			
		}*/
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

	public boolean reqApproval(MOrder order)
	{
		if(order.getPaymentRule().compareTo("D") == 0)
		{
			BigDecimal amt = DB.getSQLValueBD(null,"SELECT SUM(LineNetAmt) " +
					" FROM C_OrderLine WHERE C_Charge_ID > 0 AND C_Order_ID = ? ",order.get_ID());
			if(amt.compareTo(Env.ONEHUNDRED) < 0)
				return false;
		}	
		return true;
	}
	

}	