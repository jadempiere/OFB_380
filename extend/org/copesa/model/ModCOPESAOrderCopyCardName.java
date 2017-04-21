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
/**
 *	Validator for COPESA
 *
 *  @author Italo Niñoles
 */
public class ModCOPESAOrderCopyCardName implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCOPESAOrderCopyCardName ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCOPESAOrderCopyCardName.class);
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
		if((type == TYPE_BEFORE_CHANGE || type == TYPE_BEFORE_NEW) && po.get_Table_ID()==MOrder.Table_ID )
		{
			MOrder order = (MOrder)po;
			if(order.isSOTrx() && (order.getDocStatus().compareToIgnoreCase("DR") == 0
					|| order.getDocStatus().compareToIgnoreCase("IP") == 0
					|| order.getDocStatus().compareToIgnoreCase("AP") == 0
					|| order.getDocStatus().compareToIgnoreCase("IN") == 0))
			{
				String nameCC = "";
				if(order.get_ValueAsString("CreditCardNumber") != null &&
						order.get_ValueAsString("CreditCardNumber").trim() != "")
				{
					if(validateCC2(order.get_ValueAsString("CreditCardNumber")))
					{
						nameCC = getNameCreditCard(order.get_ValueAsString("CreditCardNumber"));
						if(nameCC != null && nameCC.trim() != "")
						{
							if(nameCC.compareToIgnoreCase("mastercard") == 0)
								order.set_CustomColumn("CreditCardType","M");
							if(nameCC.compareToIgnoreCase("visa") == 0)
								order.set_CustomColumn("CreditCardType","V");
							if(nameCC.compareToIgnoreCase("american express") == 0)
								order.set_CustomColumn("CreditCardType","A");
							if(nameCC.compareToIgnoreCase("diners club") == 0)
								order.set_CustomColumn("CreditCardType","D");
							if(nameCC.compareToIgnoreCase("discover") == 0)
								order.set_CustomColumn("CreditCardType","N");
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

	public String getNameCreditCard(String sTarjeta)
	{
		 double tarjeta = Integer.parseInt(sTarjeta.substring(0, 3));
		String nameTarjeta = "";
		if (tarjeta >= 510 && tarjeta <= 559) {
			 nameTarjeta = "Mastercard";
	     }else if (tarjeta >= 400 && tarjeta <= 499) {
	        	nameTarjeta = "Visa";
	     } else if (tarjeta >= 340 && tarjeta <= 379) {
	        	nameTarjeta = "American Express";
	     } else if (tarjeta >= 300 && tarjeta <= 305) {
	        	nameTarjeta = "Diners Club";
	     } else if (tarjeta >= 400 && tarjeta <= 499) {
	        	nameTarjeta = "Visa";
	     } else if (tarjeta == 601) {
	    	 	nameTarjeta = "Discover";
	     } else if (tarjeta == 652) {
	    	 nameTarjeta = "Palacio del Hierro";
	     }
		return nameTarjeta;
	}
	public static boolean validateCC2(String numero)
	{        
        int s1 = 0, s2 = 0;
        String reversa = new StringBuffer(numero).reverse().toString();
        for(int i = 0 ;i < reversa.length();i++){
            int digito = Character.digit(reversa.charAt(i), 10);
            if(i % 2 == 0){//this is for odd digits, they are 1-indexed in the algorithm
                s1 += digito;
            }else{//add 2 * digit for 0-4, add 2 * digit - 9 for 5-9
                s2 += 2 * digito;
                if(digito >= 5){
                    s2 -= 9;
                }
            }
        }
        System.out.println("La tarjeta es:");
        return (s1 + s2) % 10 == 0;
	}

}	