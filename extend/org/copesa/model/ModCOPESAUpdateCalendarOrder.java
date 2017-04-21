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

//import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MClient;
import org.compiere.model.MOrder;
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
public class ModCOPESAUpdateCalendarOrder implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCOPESAUpdateCalendarOrder ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCOPESAUpdateCalendarOrder.class);
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
		
		if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE) && po.get_Table_ID()==MOrder.Table_ID) 
		{	
			MOrder order = (MOrder)po;
			if(order.get_ValueAsInt("C_BPartner_Location2_ID") > 0)
			{
				//MBPartnerLocation bloc = new MBPartnerLocation(po.getCtx(), order.get_ValueAsInt("C_BPartner_Location2_ID"), po.get_TrxName());
				//@mfrojas se cambia S por I
				if(order.getPaymentRule().compareTo("I") == 0)//si es cheque
				{
					//ininoles geozona se sacará de la orden
					//if(bloc.get_ValueAsInt("C_Geozone_ID") > 0)
					if(order.get_ValueAsInt("C_Geozone_ID") > 0)
					{
						/*int ID_Calendar = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(C_Calendarcopesa_ID) " +
								" FROM C_CalendarCopesa WHERE IsCalculated = 'Y' AND C_Geozone_ID = "+bloc.get_ValueAsInt("C_Geozone_ID"));*/
						int ID_Calendar = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(C_Calendarcopesa_ID) " +
								" FROM C_CalendarCopesa WHERE CalendarType = 'R' AND C_Geozone_ID = "+order.get_ValueAsInt("C_Geozone_ID"));
						int oldCalendarID = order.get_ValueAsInt("C_CalendarCOPESA_ID");						
						if(ID_Calendar > 0)
						{
							order.set_CustomColumn("C_CalendarCOPESA_ID", ID_Calendar);							
						}
						if(oldCalendarID != ID_Calendar)
							order.set_CustomColumn("C_CalendarCOPESALine_ID", null);
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