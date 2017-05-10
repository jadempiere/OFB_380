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

import java.sql.Timestamp;
import java.util.Calendar;

import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_C_Channel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;


/**
 *	Validator for COPESA
 *
 *  @author Italo Niñoles
 */
public class ModCOPESAUpdateShipLine implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCOPESAUpdateShipLine ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCOPESAUpdateShipLine.class);
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
		engine.addModelChange(MOrderLine.Table_Name, this);		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By Julio Farías
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if((type == TYPE_AFTER_CHANGE || type == TYPE_AFTER_NEW) && po.get_Table_ID()==MOrder.Table_ID 
				&& (po.is_ValueChanged("DocStatus") || po.is_ValueChanged("C_Channel_ID"))) 
		{	
			MOrder order = (MOrder)po;
			if (order.isSOTrx() && order.getDocStatus().compareToIgnoreCase("IP") == 0)
			{
				MOrderLine[] lines = order.getLines(false, null);	//	Line is default
				for (int i = 0; i < lines.length; i++)
				{
					MOrderLine oLine = lines[i];
					if(oLine.getM_Product_ID() > 0)
					{
						//buscar metodo de envío
						MProduct prod = new MProduct(po.getCtx(), oLine.getM_Product_ID(), po.get_TrxName());
						String delivery = prod.get_ValueAsString("DeliveryViaRule");
						if(prod.get_ValueAsBoolean("IsChannelDelivery"))
						{
							if(order.get_ValueAsInt("C_Channel_ID") > 0)
							{
								X_C_Channel channel = new X_C_Channel(po.getCtx(), order.get_ValueAsInt("C_Channel_ID"),po.get_TrxName());
								if(channel.get_ValueAsString("DeliveryViaRule") != null && channel.get_ValueAsString("DeliveryViaRule") != ""
									&& channel.get_ValueAsString("DeliveryViaRule").length() > 0)
									delivery = channel.get_ValueAsString("DeliveryViaRule");
							}
						}
						//actualizar metodo de la linea
						//DB.executeUpdate("UPDATE C_OrderLine SET DeliveryViaRule = '"+delivery+"' WHERE C_OrderLine_ID = "+oLine.get_ID(), po.get_TrxName());
						String olddelivery = oLine.get_ValueAsString("DeliveryViaRule");
						if(delivery != null && delivery != "" && delivery.trim().length() > 0 && delivery.compareTo(olddelivery) != 0)
						{
							oLine.set_CustomColumn("DeliveryViaRule", delivery);
							oLine.save();
						}
					}
				}
			}
		}		
		if((type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW) && po.get_Table_ID()==MOrderLine.Table_ID 
				&& po.is_ValueChanged("DeliveryViaRule")) 
		{	
			MOrderLine oLine = (MOrderLine)po;
			int ID_Shipper = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(M_Shipper_ID) FROM C_GeozoneShipper " +
					" WHERE C_Geozone_ID = "+oLine.get_ValueAsInt("C_Geozone_ID")+"AND DeliveryViaRule = '"+oLine.get_ValueAsString("DeliveryViaRule")+"' ");
			if(ID_Shipper > 0)
				oLine.set_CustomColumn("M_Shipper_ID", ID_Shipper);
		}
		if(type == TYPE_BEFORE_NEW && po.get_Table_ID()==MOrderLine.Table_ID) 
		{	
			MOrderLine oLine = (MOrderLine)po;
			if(oLine.getM_Product_ID() > 0)
			{
				Calendar calendar = Calendar.getInstance();
				Timestamp today = new Timestamp(calendar.getTimeInMillis());
				Timestamp movDate = null;
				movDate = DB.getSQLValueTS(po.get_TrxName(), "SELECT MAX(MovementDate) " +
						" FROM M_ProductPrice pp " +
						" INNER JOIN M_PriceList_Version plv ON (pp.M_PriceList_Version_ID = plv.M_PriceList_Version_ID) " +
						" WHERE pp.IsActive = 'Y' AND pp.M_Product_ID = "+oLine.getM_Product_ID()+
						" AND M_PriceList_ID = "+ oLine.getParent().getM_PriceList_ID());    //oLine.getC_Order().getM_PriceList_ID());
				if(movDate != null)
				{
					//hacemos validacion primero
					if(movDate != null && movDate.compareTo(today) < 0)
						movDate = today;
					oLine.set_CustomColumn("MovementDate",movDate);
				}
				else
				{
					//MOrder order = new MOrder(po.getCtx(), oLine.getC_Order_ID(), po.get_TrxName());
					MOrder order = oLine.getParent();
					movDate = (Timestamp)order.get_Value("DateCompleted");
					//hacemos validacion primero
					if(movDate != null && movDate.compareTo(today) < 0)
						movDate = today;
					if(movDate != null)
						oLine.set_CustomColumn("MovementDate",movDate);
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