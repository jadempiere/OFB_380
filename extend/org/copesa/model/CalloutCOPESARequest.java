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
import org.compiere.model.CalloutEngine;
import org.compiere.model.GridField;
import org.compiere.model.GridTab;
import org.compiere.model.MResolution;

/**
 *	COPESA Callouts valida credit card.
 *	
 *  @author Italo Niñoles
 *  @version $Id: CalloutCOPESACreditCard.java,v 1.5 2016/04/25 
 */
public class CalloutCOPESARequest extends CalloutEngine
{
	/**
	 *
	 */
	public String setAction (Properties ctx, int WindowNo, GridTab mTab, GridField mField, Object value)
	{
		if(value==null || value=="")
			return "";
		if(value != null && value.toString().length()<1)		
			return "";		
		int ID_resolution = (Integer)mTab.getValue("R_Resolution_ID");
		if(ID_resolution > 0)
		{
			MResolution mRes = new MResolution(ctx, ID_resolution, null);
			if(mRes.get_ValueAsString("RequestAcction") != null 
					&& mRes.get_ValueAsString("RequestAcction").trim().length() > 0)
				mTab.setValue("RequestAcction", mRes.get_ValueAsString("RequestAcction").trim());
		}
		return "";
    }
}	//	CalloutOrder

