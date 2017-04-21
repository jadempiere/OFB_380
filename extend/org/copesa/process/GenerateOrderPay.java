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
import org.compiere.model.X_C_OrderPayCalendar;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;


/**
 * 	Generate Calendar Line COPESA
 *	
 *  @author Italo Niñoles
 */
public class GenerateOrderPay extends SvrProcess
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
		String sql = "SELECT C_Order_ID FROM C_Order co " +
				" WHERE (SELECT COUNT(1) FROM C_OrderPayCalendar WHERE C_Order_ID = co.C_Order_ID AND IsActive = 'Y' AND IsInvoiced <> 'Y') < 24" +
				" AND PaymentRule IN ('C','D') AND DocStatus IN ('CO') ";
		if(ID_Order > 0)
			sql = sql + " AND C_Order_ID = "+ID_Order;
		sql = sql + " ORDER BY C_Order_ID DESC";		
			
		PreparedStatement pstmt = DB.prepareStatement (sql, get_TrxName());		
		try
		{
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next())
			{
				String sqlOrder = "SELECT *,(SELECT AD_Org_ID FROM C_Order WHERE C_Order_ID = "+rs.getInt("C_Order_ID")+") as AD_Org_ID " +
						" FROM fact_calendar("+rs.getInt("C_Order_ID")+") WHERE periodo > (SELECT COALESCE(MAX(periodNo),0) FROM C_OrderPayCalendar " +
								" 	WHERE C_Order_ID = "+rs.getInt("C_Order_ID")+" AND IsInvoiced <> 'Y') " +
								" AND periodo <= (SELECT COALESCE(MAX(periodNo),0) FROM C_OrderPayCalendar " +
								" 	WHERE C_Order_ID = "+rs.getInt("C_Order_ID")+" AND IsInvoiced <> 'Y') + " +
								" 	(SELECT 24 - COUNT(1) FROM C_OrderPayCalendar WHERE C_Order_ID = "+rs.getInt("C_Order_ID")+" AND IsInvoiced <> 'Y')";
				try 
				{
					PreparedStatement psOrder = DB.prepareStatement(sqlOrder,get_TrxName());
					ResultSet rsOrder = psOrder.executeQuery();				
					while (rsOrder.next()) 
					{
						X_C_OrderPayCalendar pCal = new X_C_OrderPayCalendar(getCtx(), 0,get_TrxName());
						pCal.setC_Order_ID(rs.getInt("C_Order_ID"));
						pCal.setAD_Org_ID(rsOrder.getInt("AD_Org_ID"));
						pCal.setIsActive(true);
						pCal.setPeriodNo(rsOrder.getInt("periodo"));
						pCal.setDateStart(rsOrder.getTimestamp("ini"));
						pCal.setDateEnd(rsOrder.getTimestamp("fin"));
						pCal.setLineNetAmt(rsOrder.getBigDecimal("neto"));				
						pCal.setLineTotalAmt(rsOrder.getBigDecimal("total"));
						pCal.set_CustomColumn("C_DocType_ID", rs.getInt("doctype"));
						pCal.save();
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
		return "@Created@=" + 0;
	}	//	doIt

}	//	BPGroupAcctCopy
