/******************************************************************************
 * The contents of this file are subject to the   Compiere License  Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * You may obtain a copy of the License at http://www.compiere.org/license.html
 * Software distributed under the License is distributed on an  "AS IS"  basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * The Original Code is Compiere ERP & CRM Smart Business Solution. The Initial
 * Developer of the Original Code is Jorg Janke. Portions created by Jorg Janke
 * are Copyright (C) 1999-2005 Jorg Janke.
 * All parts are Copyright (C) 1999-2005 ComPiere, Inc.  All Rights Reserved.
 * Contributor(s): ______________________________________.
 *****************************************************************************/
package org.compiere.model;
import java.util.*;
import org.compiere.util.*;

/**
 *	Order Callouts.
 *	
 *  @author Fabian Aguilar OFB faaguilar
 *  @version $Id: CalloutOrder.java,v 1.34 2006/11/25 21:57:24  Exp $
 */
public class CalloutBP extends CalloutEngine
{
	/**	Debug Steps			*/
	//private boolean steps = false;


	 
	public String rut (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		
		String Digito =(String) mTab.getValue("Digito");
	    
	    
	    if(Digito==null)
			return "";
		if(Digito.equals("") )
		    return "";
			
	    if(value==null)
		return "";
		
			
		Digito=Digito.trim();
		
				
		
		int M=0,S=1,T;
		
		
		String myRut=(String)mTab.getValue("Value");
		
		if(myRut==null)
		{
			mTab.fireDataStatusEEvent ("Validacion de Rut ", "No Valido ", true);
			mTab.setValue("Digito",null);
			//mTab.setValue("Value",null);
		
		return "";
		}
		if(myRut.equals(""))
		{
			mTab.fireDataStatusEEvent ("Validacion de Rut ", "No Valido ", true);
			mTab.setValue("Digito",null);
			//mTab.setValue("Value",null);
		return "";
		}
		if(myRut.length()==0)
		{
			mTab.fireDataStatusEEvent ("Validacion de Rut ", "No Valido ", true);
			mTab.setValue("Digito",null);
			//mTab.setValue("Value",null);
		return "";
		}
		
		if(myRut.equals("-"))
		{
			mTab.fireDataStatusEEvent ("Validacion de Rut ", "No Valido ", true);
			mTab.setValue("Digito",null);
			//mTab.setValue("Value",null);
		return "";
		}
		
		if(myRut.equals("%"))
		{
			mTab.fireDataStatusEEvent ("Validacion de Rut ", "No Valido ", true);
			mTab.setValue("Digito",null);
			//mTab.setValue("Value",null);
		return "";
		}
		
		if(myRut.substring(0, 1).equals("0"))
		{
			mTab.fireDataStatusEEvent ("Validacion de Rut ", "No puede Partir con 0 ", true);
			mTab.setValue("Digito",null);
			//mTab.setValue("Value",null);
		return "";
		}
		
		try
		{
		
		T=Integer.parseInt((String)mTab.getValue("Value"));
		}
		catch (Exception e)
		{
			mTab.fireDataStatusEEvent ("Validacion de Rut ", "No Valido ", true);
			mTab.setValue("Digito",null);
			//mTab.setValue("Value",null);
			
			return "";
		}
		
		for(;T!=0;T/=10)
		S=(S+T%10*(9-M++%6))%11;
		
		char dig=(char)(S!=0?S+47:75);
		
		if(Digito.charAt(0)!=dig)
		{
			mTab.setValue("Digito",null);
			//mTab.setValue("Value",null);
			mTab.fireDataStatusEEvent ("Validacion de Rut ", "No Valido ", true);
		}
		else // es valido
		{
			//ver si existe //ininoles para el mismo cliente  
			int existe=DB.getSQLValue("C_BPartner","select count(1) from C_BPartner where value='" + myRut + "' and AD_Client_ID="+ Env.getAD_Client_ID(ctx));
			if(existe!=0)
			{
				mTab.setValue("Digito",null);
				//mTab.setValue("Value",null);
				mTab.fireDataStatusEEvent ("Validacion de Rut ", "El Rut ya existe, por favor verifiquelo ", true);
				
			}
			
		}
		
		
		
		return "";
	}	//	charge

	public String rutcontact (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		
		String Digito =(String) mTab.getValue("Digito");
	    
	    
	    if(Digito==null)
			return "";
		if(Digito.equals("") )
		    return "";
		
		if(!(Digito.equals("1") || Digito.equals("2") || Digito.equals("3") || Digito.equals("4") || Digito.equals("5") || Digito.equals("6") || Digito.equals("7") || Digito.equals("8") || Digito.equals("9") || Digito.equals("0") || Digito.equals("K") || Digito.equals("k")))
	    {
		mTab.fireDataStatusEEvent ("Validacion de Rut ", "No Valido ", true);
		mTab.setValue("Digito",null);
		//mTab.setValue("Value",null);
		return "";
		}
			
			
	    if(value==null)
		return "";
		
			
		Digito=Digito.trim();
		
				
	
		int M=0,S=1,T;
		
		
		String myRut=(String)mTab.getValue("Rut");
		
		if(myRut==null)
		{
		mTab.fireDataStatusEEvent ("Validacion de Rut ", "No Valido ", true);
		mTab.setValue("Digito",null);
		mTab.setValue("Rut",null);
		return "";
		}
		if(myRut.equals(""))
		{
		mTab.fireDataStatusEEvent ("Validacion de Rut ", "No Valido ", true);
		mTab.setValue("Digito",null);
		mTab.setValue("Rut",null);
		return "";
		}
		if(myRut.length()==0)
		{
		mTab.fireDataStatusEEvent ("Validacion de Rut ", "No Valido ", true);
		mTab.setValue("Digito",null);
		mTab.setValue("Rut",null);
		return "";
		}
		
		if(myRut.equals("-"))
		{
		mTab.fireDataStatusEEvent ("Validacion de Rut ", "No Valido ", true);
		mTab.setValue("Digito",null);
		mTab.setValue("Rut",null);
		return "";
		}
		
		if(myRut.equals("%"))
		{
		mTab.fireDataStatusEEvent ("Validacion de Rut ", "No Valido ", true);
		mTab.setValue("Digito",null);
		mTab.setValue("Rut",null);
		return "";
		}
		
		if(myRut.substring(0, 1).equals("0"))
		{
		mTab.fireDataStatusEEvent ("Validacion de Rut ", "No puede Partir con 0 ", true);
		mTab.setValue("Digito",null);
		mTab.setValue("Rut",null);
		return "";
		}
		
		try
		{
		
		T=Integer.parseInt((String)mTab.getValue("Rut"));
		}
		catch (Exception e)
		{
			mTab.fireDataStatusEEvent ("Validacion de Rut ", "No Valido ", true);
			mTab.setValue("Digito",null);
			mTab.setValue("Rut",null);
			return "";
		}
			
			
		
		
				
		for(;T!=0;T/=10)
		S=(S+T%10*(9-M++%6))%11;
		
		char dig=(char)(S!=0?S+47:75);
		
		if(Digito.charAt(0)!=dig)
		{
		mTab.fireDataStatusEEvent ("Validacion de Rut ", "No Valido ", true);
		mTab.setValue("Digito",null);
		mTab.setValue("Rut",null);
		}
		
		return "";
	}	//	charge

}	//	CalloutOrder

