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

import org.compiere.model.MClient;
import org.compiere.model.MOrder;
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
public class ModCOPESAValidPTermOrder implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCOPESAValidPTermOrder ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCOPESAValidPTermOrder.class);
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
		engine.addDocValidate(MOrder.Table_Name, this);		
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By Julio Farías
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		return null;
	}	//	modelChange

	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);
		
		if(timing == TIMING_BEFORE_COMPLETE && po.get_Table_ID()==MOrder.Table_ID)
		{
			MOrder order = (MOrder)po;
			if(order.isSOTrx())
			{
				int cant = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(M_PriceList_ID) FROM M_PriceList WHERE M_PriceList_ID IN ( " +
						" SELECT pl.M_PriceList_ID FROM M_PriceList pl" +
						" INNER JOIN C_BPartnerPriceList bpl ON pl.M_PriceList_ID = bpl.M_PriceList_ID "+
						" WHERE pl.IsActive = 'Y' AND bpl.IsActive = 'Y' AND pl.IsSOPriceList = 'Y' "+
						" AND pl.IsInBPartner = 'Y' AND bpl.C_BPartner_ID = " + order.getC_BPartner_ID()+
						" UNION SELECT pl.M_PriceList_ID FROM M_PriceList pl " +
						" INNER JOIN AD_UserPriceList upl ON pl.M_PriceList_ID = upl.M_PriceList_ID" +
						" WHERE pl.IsActive = 'Y' AND upl.IsActive = 'Y' AND pl.IsSOPriceList = 'Y' " +
						" AND pl.IsInUser = 'Y' AND upl.AD_User_ID = "+Env.getAD_User_ID(po.getCtx())+
						" UNION " +
						" SELECT pl.M_PriceList_ID FROM M_PriceList pl " +
						" INNER JOIN AD_RolePriceList rpl ON pl.M_PriceList_ID = rpl.M_PriceList_ID " +
						" WHERE pl.IsActive = 'Y' AND rpl.IsActive = 'Y' AND pl.IsSOPriceList = 'Y' " +
						" AND ((pl.IsInChannel = 'Y' AND rpl.C_Channel_ID = "+order.get_ValueAsInt("C_Channel_ID")+" )" +
						" 	OR (pl.IsInRole = 'Y' AND rpl.AD_Role_ID = "+Env.getAD_Role_ID(po.getCtx())+" ) " +
						" 	OR (pl.IsInPTerm = 'Y' AND rpl.C_PaymentTerm_ID = "+order.getC_PaymentTerm_ID()+") " +
						"    ) " +
						" UNION SELECT pl.M_PriceList_ID FROM M_PriceList pl " +
						" WHERE pl.IsActive = 'Y' AND pl.IsSOPriceList = 'Y' AND pl.IsInUser = 'N' " +
						" AND pl.IsInBPartner = 'N' AND pl.IsInRole = 'N' " +
						" AND pl.IsInPTerm = 'N' AND pl.IsInChannel = 'N' " +
						" AND pl.AD_Client_ID = "+Env.getAD_Client_ID(po.getCtx())+")" +
						" AND (dateStart IS NULL OR dateStart < now()) AND (dateEnd IS NULL OR dateEnd > now())" +
						" AND M_PriceList_ID = "+order.getM_PriceList_ID());
				if(cant < 1)
						return "Error: Termino de pago no permitido para esta lista de precio";
			}			
		}
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