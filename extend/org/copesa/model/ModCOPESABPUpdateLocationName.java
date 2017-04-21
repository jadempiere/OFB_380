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

import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MClient;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_C_City;
import org.compiere.model.X_C_Province;
import org.compiere.model.X_C_Region;
import org.compiere.model.X_C_Street;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
/**
 *	Validator for COPESA
 *
 *  @author Italo Niñoles
 */
public class ModCOPESABPUpdateLocationName implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCOPESABPUpdateLocationName ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCOPESABPUpdateLocationName.class);
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
		engine.addModelChange(MBPartnerLocation.Table_Name, this);		
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE) && po.get_Table_ID()==MBPartnerLocation.Table_ID) 
		{	
			MBPartnerLocation bpLoc = (MBPartnerLocation)po;
			String street = "";
			//String newName = "";
			String typeStreet = "";
			typeStreet = DB.getSQLValueString(po.get_TrxName(), 
					"SELECT NAME FROM AD_Ref_List WHERE AD_Reference_ID = 2000027 AND value = '"+bpLoc.get_ValueAsString("StreetType")+"'");
			if(bpLoc.get_ValueAsInt("C_Street_ID") > 0)
			{
				X_C_Street calle = new X_C_Street(po.getCtx(), bpLoc.get_ValueAsInt("C_Street_ID"), po.get_TrxName());				
				street = calle.getName();
			}else
				street = bpLoc.get_ValueAsString("StreetName");
			if(street == null || street == "" || street.trim().length() < 2)
				return "Debe Ingresar Calle";			
			else
			{
				//primero actualizamos los valores no esta optimizado. Revisar despues 
				if(bpLoc.get_ValueAsInt("C_City_ID") > 0)
				{
					X_C_City city = new X_C_City(po.getCtx(), bpLoc.get_ValueAsInt("C_City_ID"), po.get_TrxName());
					if(city.get_ValueAsInt("C_Province_ID") > 0)
					{
						bpLoc.set_CustomColumn("C_Province_ID", city.get_ValueAsInt("C_Province_ID"));
						X_C_Province prov = new X_C_Province(po.getCtx(), city.get_ValueAsInt("C_Province_ID"), po.get_TrxName());
						if(prov.getC_Region_ID() > 0)
						{
							bpLoc.set_CustomColumn("C_Region_ID", prov.getC_Region_ID());
						}
					}
				}	
				//ininoles end				
				String newName = typeStreet+" "+street+" "+ bpLoc.get_ValueAsString("AddressNumber")+" "+
				bpLoc.get_ValueAsString("HomeNumber")+" "+bpLoc.get_ValueAsString("Block")+" "+
				bpLoc.get_ValueAsString("Villa");
				if(bpLoc.get_ValueAsInt("C_City_ID") > 0)		
				{
					X_C_City city = new X_C_City(po.getCtx(), bpLoc.get_ValueAsInt("C_City_ID"), po.get_TrxName());
					newName = newName +" "+city.getName();
				}
				if(bpLoc.get_ValueAsInt("C_Province_ID") > 0)		
				{
					X_C_Province prov = new X_C_Province(po.getCtx(), bpLoc.get_ValueAsInt("C_Province_ID"), po.get_TrxName());
					newName = newName +" "+prov.getName();
				}
				if(bpLoc.get_ValueAsInt("C_Region_ID") > 0)		
				{
					X_C_Region region = new X_C_Region(po.getCtx(), bpLoc.get_ValueAsInt("C_Region_ID"), po.get_TrxName());
					newName = newName +" "+region.getName();
				}
				bpLoc.setName(newName);
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