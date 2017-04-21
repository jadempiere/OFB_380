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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import org.compiere.model.X_C_OrderShipCalendar;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;


/**
 * 	Generate Calendar Line COPESA
 *	
 *  @author Italo Niñoles
 */
public class GenerateOrderShip extends SvrProcess
{
	int ID_Order;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("C_Order_ID"))
				ID_Order = para[i].getParameterAsInt();
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
		String sql = "SELECT C_Order_ID FROM C_Order co " +
				" WHERE DocStatus IN ('CO') AND (SELECT MAX(DateTrx) FROM C_OrderShipCalendar WHERE C_Order_ID = co.C_Order_ID) < (now()+(12 * '1 month'::interval))";
		if(ID_Order > 0)
			sql = sql + " AND C_Order_ID = "+ID_Order;
		sql = sql + " ORDER BY C_Order_ID DESC";		
			
		PreparedStatement pstmt = DB.prepareStatement (sql, get_TrxName());		
		try
		{
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next())
			{
				String sqlOrder = "SELECT *,(SELECT AD_Org_ID FROM C_Order WHERE C_Order_ID = "+rs.getInt("C_Order_ID")+" )as AD_Org_ID " +
						" FROM RVOFB_OrderShipFull co WHERE C_Order_ID = "+rs.getInt("C_Order_ID")+
						" AND DateTrx > (SELECT MAX(DateTrx) FROM C_OrderShipCalendar WHERE C_Order_ID = co.C_Order_ID AND IsActive= 'Y') " +
						" AND DateTrx <= now()+(12 * '1 month'::interval)";
				try 
				{
					PreparedStatement psOrder = DB.prepareStatement(sqlOrder,get_TrxName());
					ResultSet rsOrder = psOrder.executeQuery();
					while (rsOrder.next()) 
					{
						X_C_OrderShipCalendar sCal = new X_C_OrderShipCalendar(getCtx(), 0,get_TrxName());
						sCal.setC_Order_ID(rsOrder.getInt("C_Order_ID"));
						sCal.setAD_Org_ID(rsOrder.getInt("AD_Org_ID"));
						sCal.setIsActive(true);
						sCal.setC_OrderLine_ID(rsOrder.getInt("C_OrderLine_ID"));
						sCal.setC_CalendarCOPESA_ID(rsOrder.getInt("C_CalendarCOPESA_ID"));
						sCal.setC_CalendarCOPESALine_ID(rsOrder.getInt("C_CalendarCOPESALine_ID"));
						sCal.setDateTrx(rsOrder.getTimestamp("DateTrx"));
						sCal.set_CustomColumn("M_Product_ID",rsOrder.getInt("M_Product_ID"));
						sCal.save();
						cant++;
					}						
				}
				catch (Exception e) 
				{
					log.config(e.toString());
				}
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		return "@Created@=" + cant;
	}	//	doIt

}	//	BPGroupAcctCopy
