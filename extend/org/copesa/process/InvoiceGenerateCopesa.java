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

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.process.DocAction;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Generate Invoices
 *	
 *  @author Jorg Janke
 *  @version $Id: InvoiceGenerate.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class InvoiceGenerateCopesa extends SvrProcess
{
	/**	Date Invoiced			*/
	private Timestamp	p_DateInvoiced = null;
	private Timestamp	p_DateOrdered = null;
	private Timestamp	p_DateOrdered_To = null;
	/** BPartner				*/
	private int			p_C_BPartner_ID = 0;
	/** Order					*/
	private int			p_C_Order_ID = 0;
	/** Invoice Document Action	*/
	private String		p_docAction = DocAction.ACTION_Complete;
	
	/**	The current Invoice	*/
	//private MInvoice 	m_invoice = null;
	/**	The current Shipment	*/
	/** Number of Invoices		*/
	private int			m_created = 0;
	/**	Line Number				*/
	//private int			m_line = 0;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("DateInvoiced"))
				p_DateInvoiced = (Timestamp)para[i].getParameter();
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = para[i].getParameterAsInt();
			else if (name.equals("C_Order_ID"))
				p_C_Order_ID = para[i].getParameterAsInt();
			else if (name.equals("DocAction"))
				p_docAction = (String)para[i].getParameter();
			else if (name.equals("DateOrdered"))
			{
				p_DateOrdered = para[i].getParameterAsTimestamp();
				p_DateOrdered_To = para[i].getParameterToAsTimestamp();
			}
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}

		//	Login Date
		if (p_DateInvoiced == null)
			p_DateInvoiced = Env.getContextAsDate(getCtx(), "#Date");
		if (p_DateInvoiced == null)
			p_DateInvoiced = new Timestamp(System.currentTimeMillis());

		//	DocAction check
		if (!DocAction.ACTION_Complete.equals(p_docAction))
			p_docAction = DocAction.ACTION_Prepare;
	}	//	prepare

	/**
	 * 	Generate Invoices
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		log.info("DateInvoiced=" + p_DateInvoiced + ",C_BPartner_ID=" + p_C_BPartner_ID	+ ", C_Order_ID=" + p_C_Order_ID + ", DocAction=" + p_docAction);
		//
		//validacion fecha fin de orden debe ser igual o menor a fecha de facturación 
		if(p_DateOrdered_To != null && p_DateOrdered_To.compareTo(p_DateInvoiced) > 0)
			throw new AdempiereException("Error: Fecha nota de venta no puede ser mayor a fecha facturación");
		
		String sql = null;
		PreparedStatement pstmt = null;

		sql = "SELECT opc.C_Order_ID,opc.DateEnd,C_OrderPayCalendar_ID, opc.C_DocType_ID as F_DocType_ID, opc.periodno, co.* FROM C_OrderPayCalendar opc " +
			 " INNER JOIN C_Order co ON (opc.C_Order_ID = co.C_Order_ID) " +
			 " WHERE co.DocStatus IN('CO','CL') AND co.IsSOTrx='Y' AND opc.IsInvoiced <> 'Y' ";
		if (p_C_BPartner_ID != 0)
				sql += " AND co.C_BPartner_ID = ?";	 
		if (p_C_Order_ID != 0)
			sql += " AND co.C_Order_ID = ?";
		if (p_DateOrdered != null && p_DateOrdered_To != null)
		{
			sql += " AND opc.DateEnd BETWEEN ? AND ? ";
		}
		
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			int index = 1;
			if (p_C_BPartner_ID != 0)
				pstmt.setInt(index++, p_C_BPartner_ID);
			if (p_C_Order_ID != 0)
				pstmt.setInt(index++, p_C_Order_ID);
			if (p_DateOrdered != null && p_DateOrdered_To != null)
			{
				pstmt.setTimestamp(index++, p_DateOrdered);
				pstmt.setTimestamp(index++, p_DateOrdered_To);
			}
			ResultSet rs = pstmt.executeQuery();
			long firstInvoice = 0;
			long lastInvoice = 0;
			while (rs.next ())
			{
				MOrder order = new MOrder(getCtx(), rs, get_TrxName() );
				MInvoice inv = createInvoice(order, rs.getTimestamp("DateEnd"),rs.getInt("F_DocType_ID"), rs.getInt("C_OrderPayCalendar_ID"), rs.getInt("periodno"));
				if( inv != null )
				{
					lastInvoice = inv.getC_Invoice_ID();
					if (firstInvoice == 0)
					    firstInvoice = lastInvoice;
					completeInvoice(inv);
				}
			}	//	for all orders
			rs.close ();
			pstmt.close ();
			pstmt = null;			
			UpdateOrderPayCalendar(firstInvoice, lastInvoice);
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		return "Se han generado "+m_created+" facturas";
	}	//	doIt
		
	
	/**
	 * 	Complete Invoice
	 */
	private void completeInvoice(MInvoice m_invoice)
	{
		if (m_invoice != null)
		{
			if(m_invoice.getC_BPartner().getSOCreditStatus().compareTo("W") == 0)
			{
				m_invoice.setDocStatus("IN");
			}
			else if (!m_invoice.processIt(p_docAction))
			{
				log.warning("completeInvoice - failed: " + m_invoice);
				addLog("completeInvoice - failed: " + m_invoice); // Elaine 2008/11/25
			}
			m_invoice.saveEx();
			m_created++;
		}
		m_invoice = null;
	}	//	completeInvoice
	
	private MInvoice createInvoice(MOrder order,  Timestamp dateInvoiced, int ID_DocType, int C_OrderPayCalendar_ID, int _periodno)
	{
		MInvoice m_invoice = new MInvoice (order, 0, dateInvoiced);
		if(ID_DocType > 0)
		{
			m_invoice.setC_DocType_ID(ID_DocType);
			m_invoice.setC_DocTypeTarget_ID(ID_DocType);
			m_invoice.set_CustomColumn("C_OrderPayCalendar_ID", C_OrderPayCalendar_ID);
			m_invoice.set_CustomColumn("IsHold", order.get_Value("IsHold"));
		}
		if (!m_invoice.save())
			throw new IllegalStateException("Could not create Invoice (o)");
		String sqlDet = "SELECT * FROM co_factcalendar WHERE C_Order_ID = ? AND periodno = ?";
		try 
		{
			PreparedStatement pstmtLine = DB.prepareStatement (sqlDet, get_TrxName());
			pstmtLine.setInt(1, order.get_ID());
			pstmtLine.setInt(2, _periodno);		
			ResultSet rsLine = pstmtLine.executeQuery();
			while (rsLine.next())
			{
				MOrderLine oLine = new MOrderLine(getCtx(), rsLine.getInt("C_OrderLine_ID"), get_TrxName());			
		   		MInvoiceLine line = new MInvoiceLine (m_invoice);
				line.setOrderLine(oLine);
				line.setQtyInvoiced(oLine.getQtyEntered());
				line.setQtyEntered(oLine.getQtyEntered());
				line.setPrice(rsLine.getBigDecimal("linenetamt"));
				line.setLineNetAmt();
	            line.setTaxAmt();
				if (!line.save())
					throw new IllegalStateException("Could not create Invoice Line (o)");
			}
		} 
		catch (Exception e) {
			log.config("Error al generar linea:"+e.toString());
			return null;
		}
		return m_invoice;
				
	}	//	createLine

	private void UpdateOrderPayCalendar(long _firstInvoice, long _lastInvoice)
	{
		String sql = "UPDATE C_OrderPayCalendar as opc set C_Invoice_ID = inv.C_Invoice_ID, IsInvoiced = 'Y' ";
		sql += "FROM C_Invoice inv where opc.C_OrderPayCalendar_ID = inv.C_OrderPayCalendar_ID ";
		sql += " AND inv.C_Invoice_ID BETWEEN " + _firstInvoice + " AND " + _lastInvoice;
		
		DB.executeUpdate(sql, get_TrxName());
	}
	
}	//	InvoiceGenerate
