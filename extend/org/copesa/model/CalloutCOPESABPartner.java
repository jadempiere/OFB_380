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

import java.util.Calendar;
import java.util.Properties;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;

/**
 *	COPESA Callouts valida credit card.
 *	
 *  @author Italo Niñoles
 *  @version $Id: CalloutCOPESACreditCard.java,v 1.5 2016/04/25 
 */
public class CalloutCOPESABPartner extends CalloutEngine
{
	/**
	 *
	 */
	public String validExpDate (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null || value=="")
			return "";
		if(value != null && value.toString().length() != 5)
		{
			mTab.setValue(mField.getColumnName(),"");
			return "Largo Incorrecto";
		}
		String ExpDate = value.toString();
		int month = Integer.parseInt(ExpDate.substring(0,2));
		if(month > 12)
		{
			mTab.setValue(mField.getColumnName(),"");
			return "Mes No válido";
		}
		int year = Integer.parseInt(ExpDate.substring(3,5));
		year = year +2000;
		
		Calendar dateCard = Calendar.getInstance();
		dateCard.set(Calendar.MONTH, month-1);
		dateCard.set(Calendar.YEAR, year);
		dateCard.set(Calendar.DAY_OF_MONTH, 1);
			
		Calendar until = Calendar.getInstance();
		until.add(Calendar.MONTH,3);
		until.set(Calendar.DAY_OF_MONTH, 1);
		
		if(dateCard.compareTo(until) < 0)
		{
			mTab.setValue(mField.getColumnName(),"");
			return "Fecha de término Ingresada no es válida";			
		}
		else
			return "";
    }
}	//	CalloutOrder

