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
import java.util.Calendar;
import java.util.logging.Level;
import org.compiere.model.X_C_CalendarCOPESA;
import org.compiere.model.X_C_CalendarCOPESALine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
//import org.compiere.util.DB;


/**
 * 	Generate Calendar Line COPESA
 *	
 *  @author Italo Niñoles
 */
public class GenerateCalendarLine extends SvrProcess
{
	/** Product					*/
	private boolean			p_ISFullDay = false;
	/**	ID COPESA Calendar				*/
	private int 		id_Calendar;

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
			else if (para[i].getParameterName().equals("IsFullDay"))
				p_ISFullDay = para[i].getParameterAsBoolean();
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
		X_C_CalendarCOPESA calendar = new X_C_CalendarCOPESA(getCtx(), id_Calendar, get_TrxName());
		int flag = 0;
		int cant = 0;
		//borramos registros existentes
		//DB.executeUpdate("DELETE FROM C_CalendarCOPESALine WHERE C_CalendarCOPESA_ID = "+calendar.get_ID(),get_TrxName());
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(calendar.getStartDate().getTime());
		while (flag == 0)
		{	
			if(!p_ISFullDay)
			{
				X_C_CalendarCOPESALine calLine = new X_C_CalendarCOPESALine(getCtx(), 0, get_TrxName());
				calLine.setAD_Org_ID(calendar.getAD_Org_ID());
				calLine.setC_CalendarCOPESA_ID(calendar.get_ID());
				calLine.setIsActive(true);
				calLine.setIsShip(false);
				calLine.setDateTrx(new Timestamp(cal.getTimeInMillis()));
				calLine.setDayName("0"+cal.get(Calendar.DAY_OF_WEEK));
				calLine.set_CustomColumn("DayTime", "M");
				if(calLine.save())
					cant++;
				X_C_CalendarCOPESALine calLine2 = new X_C_CalendarCOPESALine(getCtx(), 0, get_TrxName());
				calLine2.setAD_Org_ID(calendar.getAD_Org_ID());
				calLine2.setC_CalendarCOPESA_ID(calendar.get_ID());
				calLine2.setIsActive(true);
				calLine2.setIsShip(false);
				calLine2.setDateTrx(new Timestamp(cal.getTimeInMillis()));
				calLine2.setDayName("0"+cal.get(Calendar.DAY_OF_WEEK));
				calLine2.set_CustomColumn("DayTime", "T");
				if(calLine2.save())
					cant++;
			}
			else
			{
				X_C_CalendarCOPESALine calLine = new X_C_CalendarCOPESALine(getCtx(), 0, get_TrxName());
				calLine.setAD_Org_ID(calendar.getAD_Org_ID());
				calLine.setC_CalendarCOPESA_ID(calendar.get_ID());
				calLine.setIsActive(true);
				calLine.setIsShip(false);
				calLine.setDateTrx(new Timestamp(cal.getTimeInMillis()));
				calLine.setDayName("0"+cal.get(Calendar.DAY_OF_WEEK));
				if(calLine.save())
					cant++;
			}
			cal.add(Calendar.DAY_OF_YEAR, 1);
			//preguntamos si la fecha de mañana es mayor a la fecha final y sobreescribimos flag			
			if(new Timestamp(cal.getTimeInMillis()).after(calendar.getEndDate()))
				flag = 1;
		}		
		return "@Created@=" + cant;
	}	//	doIt

}	//	BPGroupAcctCopy
