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

import org.compiere.model.MBPartner;
import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.MPriceList;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

/**
 *	Validator for COPESA
 *
 *  @author Italo Niñoles
 */
public class ModCOPESAOrderCopyDocType implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCOPESAOrderCopyDocType ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCOPESAOrderCopyDocType.class);
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
		if((type == TYPE_AFTER_CHANGE || type == TYPE_AFTER_NEW) && po.get_Table_ID()==MOrder.Table_ID )
		{
			MOrder order = (MOrder)po;
			if(order.isSOTrx() && (order.getDocStatus().compareToIgnoreCase("DR") == 0
					|| order.getDocStatus().compareToIgnoreCase("IP") == 0
					|| order.getDocStatus().compareToIgnoreCase("AP") == 0
					|| order.getDocStatus().compareToIgnoreCase("IN") == 0))
			{	
				//primero se reemplaza por el doc boleta si es persona natural
				MBPartner bPart = new MBPartner(po.getCtx(), order.getC_BPartner_ID(), po.get_TrxName());
				if(bPart.get_ValueAsString("PartnerType").compareTo("N") == 0)
				{
					if(IDDocBoleta(po) > 0)
					{
						DB.executeUpdate("UPDATE C_Order SET C_DocType_ID = "+IDDocBoleta(po)
								+", C_DocTypeTarget_ID = "+IDDocBoleta(po)+" WHERE C_Order_ID = "+order.get_ID(), po.get_TrxName());
					}
				}//si no es persona natural tratar de reemplazar por lista de precios
				else
				{
					MPriceList pList = new MPriceList(po.getCtx(), order.getM_PriceList_ID(), po.get_TrxName());
					if(pList.get_ValueAsInt("C_DocType_ID") > 0)
					{
						DB.executeUpdate("UPDATE C_Order SET C_DocType_ID = "+pList.get_ValueAsInt("C_DocType_ID")
								+", C_DocTypeTarget_ID = "+pList.get_ValueAsInt("C_DocType_ID")+" WHERE C_Order_ID = "+order.get_ID(), po.get_TrxName());
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

	private int IDDocBoleta(PO po)
	{
		int id_boleta = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(C_DocType_ID) FROM C_DocType WHERE IsActive = 'Y' " +
				"AND upper(NAME) like '%BOLETA%' AND DocBaseType = 'SOO'");
		return id_boleta;
	}
	

}	