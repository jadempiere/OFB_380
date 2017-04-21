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
package org.copesa.process;

import java.sql.Timestamp;
import java.util.logging.Level;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
//import org.compiere.util.DB;


/**
 * 	Generate Calendar Line COPESA
 *	
 *  @author Italo Niñoles
 */
public class UpdateCalendarLine extends SvrProcess
{
	/** Tipo dia					*/
	private String		p_DayTime;
	/**	ID COPESA Calendar				*/
	private int 		id_Calendar;
	/**	fecha 				*/
	private Timestamp	p_DateTrx;
	private Timestamp	p_DateTrxTo;
	/**	fecha 				*/
	private String		p_DayName;
	private boolean		p_15Days = false;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare ()
	{
		id_Calendar = getRecord_ID();
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (para[i].getParameterName().equals("DayTime"))
				p_DayTime = para[i].getParameterAsString();
			else if (para[i].getParameterName().equals("DateTrx"))
			{
				p_DateTrx = para[i].getParameterAsTimestamp();
				p_DateTrxTo = para[i].getParameterToAsTimestamp();
			}
			else if (para[i].getParameterName().equals("DayName"))
				p_DayName = para[i].getParameterAsString();
			else if (para[i].getParameterName().equals("15Days"))
				p_15Days = para[i].getParameterAsBoolean();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	
	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		int cant = 0;
		if(p_15Days)
		{
			if(p_DayName != null && p_DateTrx != null)
			{				
				String sql = "UPDATE C_CalendarCOPESALine SET IsShip = 'Y' " +
				" WHERE C_CalendarCOPESALine_ID IN (" +
				" select C_CalendarCOPESALine_ID from C_CalendarCOPESALine " +
				" where c_calendarcopesa_id = "+id_Calendar+" and dayname = '"+p_DayName+"' "+
				" and CAST(extract( week from datetrx) - extract(week from date '"+p_DateTrx+"') AS integer ) % 2 = 0 " +		
				" )";
				cant = DB.executeUpdate(sql, get_TrxName());
			}
		}
		else
		{			
			String sql = "UPDATE C_CalendarCOPESALine SET IsShip = 'Y' " +
					" WHERE C_CalendarCOPESA_ID = "+id_Calendar;
			if(p_DayTime != null)
				sql = sql + " AND DayTime = '"+p_DayTime+"'";
			if(p_DateTrx != null && p_DateTrxTo != null)
				sql = sql + " AND DateTrx BETWEEN ? AND ? ";
			if(p_DayName != null)
				sql = sql + " AND DayName = '"+p_DayName+"'";
								
			if(p_DateTrx != null && p_DateTrxTo != null)
			{
				Timestamp[] dates = new Timestamp[2];
				dates[0] = p_DateTrx;
				dates[1] = p_DateTrxTo;
				cant = DB.executeUpdate(sql, dates, false, get_TrxName());
			}else
			{
				cant = DB.executeUpdate(sql,get_TrxName());
			}
		}
		return "@Updated@=" + cant;
	}	//	doIt

}	//	BPGroupAcctCopy

