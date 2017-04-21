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

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.compiere.model.*;

/**
 *	COPESA Callouts valida credit card.
 *	
 *  @author Italo Niñoles
 *  @version $Id: CalloutCOPESACreditCard.java,v 1.5 2016/04/25 
 */
public class CalloutCOPESASalesOrder extends CalloutEngine
{
	/**
	 *
	 */
	public String validCreditCard (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null || value=="")
			return "";
		if(value != null && value.toString().length()<1)		
			return "";
		if(validateCC2(value.toString()))
		{
			//antes de mandar mensaje setemos
			String nameCC = "";
			nameCC = getNameCreditCard(value.toString());
			if(nameCC != null && nameCC.trim() != "")
			{
				if(nameCC.compareToIgnoreCase("mastercard") == 0)
					mTab.setValue("CreditCardType", "M");
				if(nameCC.compareToIgnoreCase("visa") == 0)
					mTab.setValue("CreditCardType", "V");
				if(nameCC.compareToIgnoreCase("american express") == 0)
					mTab.setValue("CreditCardType", "A");
				if(nameCC.compareToIgnoreCase("diners club") == 0)
					mTab.setValue("CreditCardType", "D");
				if(nameCC.compareToIgnoreCase("discover") == 0)
					mTab.setValue("CreditCardType", "N");
			}			
			return "Tarjeta Válida - "+getNameCreditCard(value.toString());
		}
		else
		{
			mTab.setValue(mField.getColumnName(),"");
			return "Tarjeta NO Válida";
		}
    }		
	public String validMail (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null || value=="")
			return "";
		if(value != null && value.toString().length()<1)		
			return "";
		if(isEmail(value.toString()))
			return "Correo Válido";
		else
		{
			mTab.setValue(mField.getColumnName(),"");
			return "Correo NO Válido ";
		}
    }
	public String calculateNewPrice (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null || value=="")
			return "";
		if(value != null && value.toString().length()<1)		
			return "";
				
		return "";
    }
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
	     } else if (tarjeta == 6011) {
	    	 	nameTarjeta = "Discover";
	     } else if (tarjeta == 6520) {
	    	 nameTarjeta = "Palacio del Hierro";
	     }
		return nameTarjeta;
	}
	public static boolean validateCC2(String numero){
        
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
	public boolean isEmail(String correo) 
	{
		Pattern pat = null;
        Matcher mat = null;        
        pat = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        mat = pat.matcher(correo);
        if (mat.find()) {
            System.out.println("[" + mat.group() + "]");
            return true;
        }else{
            return false;
        }        
    }
	
}	//	CalloutOrder

