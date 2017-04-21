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
package org.minju.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MBPartner;
import org.compiere.util.Env;

/**
 *	Order Callouts.
 *	
 *  @author Italo Niñoles OFB ininoles.
 *  @version $Id: CalloutRHAdministrativeRequests.java,v 2.0 2012/12/03  Exp $
 */
public class CalloutMedLicenses extends CalloutEngine
{		
	public String EndDate (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) throws ParseException
	{
		if(value==null)
			return "";

		Date fi = (java.util.Date)mTab.getValue("datestartrequest");		
		BigDecimal qtyDays= (BigDecimal)mTab.getValue("Days");
		int qty = Integer.valueOf(qtyDays.intValue());
		if(fi != null || qtyDays != null)
		{
			Date ff = calculateEndDate(fi, qty);		
			Timestamp cff = new Timestamp(ff.getTime());			
			mTab.setValue("DateEnd", cff);	
		}
		return "";
	}
	public String SetOrg (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) throws ParseException
	{
		if(value==null)
			return "";
		int ID_BPartner = (Integer)value;
		if(ID_BPartner > 0)
		{
			MBPartner bPart = new MBPartner(ctx, ID_BPartner, null);
			if(bPart.getAD_Org_ID() > 0)
				mTab.setValue("AD_Org_ID",bPart.get_ValueAsInt("AD_OrgRef_ID"));
		}
		return "";
	}
	
	public static java.util.Date calculateEndDate(Date startDate, int duration)
	{		
	  Calendar startCal = Calendar.getInstance();
	 
	  startCal.setTime(startDate);
			
	  for (int i = 1; i < duration; i++)
	  {
	    startCal.add(Calendar.DAY_OF_MONTH, 1);
	  }
	  return startCal.getTime();
	}
	public String noNegativeValue (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value) throws ParseException
	{
		if(value==null)
			return "";
		BigDecimal num = (BigDecimal)value;
		if(num.compareTo(Env.ZERO) <= 0)
			num = num.negate();
		mTab.setValue(mField.getColumnName(),num);		
		return "";
	}
}


