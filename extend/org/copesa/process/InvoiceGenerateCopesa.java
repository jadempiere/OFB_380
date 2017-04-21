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
import java.util.Calendar;
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
	private MInvoice 	m_invoice = null;
	/**	The current Shipment	*/
	/** Number of Invoices		*/
	private int			m_created = 0;
	/**	Line Number				*/
	private int			m_line = 0;
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
		log.info("DateInvoiced=" + p_DateInvoiced
			+ ",C_BPartner_ID=" + p_C_BPartner_ID
			+ ", C_Order_ID=" + p_C_Order_ID + ", DocAction=" + p_docAction);
		//
		//validacion fecha fin de orden debe ser igual o menor a fecha de facturación 
		if(p_DateOrdered_To != null && p_DateOrdered_To.compareTo(p_DateInvoiced) > 0)
			throw new AdempiereException("Error: Fecha nota de venta no puede ser mayor a fecha facturación");
		
		//Validación que fecha debe ser mayor a hoy
		Calendar calendar = Calendar.getInstance();
		Timestamp hoy = new Timestamp(calendar.getTimeInMillis());
		//dejamos solo la fecha
		hoy.setHours(0);
		hoy.setMinutes(0);
		hoy.setSeconds(0);
		hoy.setNanos(0);
		
		/*if(p_DateInvoiced.compareTo(hoy) <=0)
			throw new AdempiereException("Error: Fecha de Facturacion debe ser mayor a "+hoy);
			*/
		//ininoles end
		
		String sql = null;
		//generacion de facturas no PAT
		sql = "SELECT * FROM C_Order o "
			//+ "WHERE DocStatus IN('CO','CL') AND IsSOTrx='Y' AND PaymentRule <> 'D' ";
			+ "WHERE DocStatus IN('CO','CL') AND IsSOTrx='Y' AND PaymentRule NOT IN ('D','C') ";
		if (p_C_BPartner_ID != 0)
			sql += " AND C_BPartner_ID=?";
		if (p_C_Order_ID != 0)
			sql += " AND C_Order_ID=?";
		if (p_DateOrdered != null && p_DateOrdered_To != null)
			sql += " AND o.DateOrdered BETWEEN ? AND ? ";
		//
		sql += " AND EXISTS (SELECT * FROM C_OrderLine ol "
			+ "WHERE o.C_Order_ID=ol.C_Order_ID AND ol.QtyOrdered<>ol.QtyInvoiced) "
			+ "AND o.C_DocType_ID IN (SELECT C_DocType_ID FROM C_DocType "
			+ "WHERE DocBaseType='SOO' AND DocSubTypeSO NOT IN ('ON','OB','WR')) "
			//que no sea consolidado
			//el consolidado se hará en el reporte, son facturas normales
			/*+ " AND M_PriceList_ID IN (SELECT M_PriceList_ID FROM M_PriceList "
			+ " WHERE IsActive = 'Y' AND IsConsolidated <> 'Y') "*/
			//end
			+ "ORDER BY M_Warehouse_ID, C_BPartner_ID, Bill_Location_ID, C_Order_ID";
		
		PreparedStatement pstmt = null;
		try
		{
			//mfrojas
			log.config("sql select facturas "+sql);
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
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MOrder order = new MOrder (getCtx(), rs, get_TrxName());
				MOrderLine[] oLines = order.getLines(true, null);
				log.config("lineas "+oLines.length);
				for (int i = 0; i < oLines.length; i++)
				{
					MOrderLine oLine = oLines[i];
					BigDecimal toInvoice = oLine.getQtyOrdered().subtract(oLine.getQtyInvoiced());
					if (toInvoice.compareTo(Env.ZERO) == 0 && oLine.getM_Product_ID() != 0)
						continue;
					BigDecimal qtyEntered = toInvoice;
					createLine (order, oLine, toInvoice, qtyEntered);
					
				}
				completeInvoice();
				//m_created++;
			}	//	for all orders
			rs.close ();
			pstmt.close ();
			pstmt = null;		
			rs = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}		
		//generacipon de facturas PAT
		/*sql = "SELECT * FROM C_Order o "
			+ "WHERE DocStatus IN('CO','CL') AND IsSOTrx='Y' AND PaymentRule IN ('D','C') ";
		if (p_C_BPartner_ID != 0)
			sql += " AND C_BPartner_ID=?";
		if (p_C_Order_ID != 0)
			sql += " AND C_Order_ID=?";
		if (p_DateOrdered != null && p_DateOrdered_To != null)
		{
			sql += " AND o.DateOrdered BETWEEN ? AND ? ";
		}*/
		//
		/*sql += " AND o.C_DocType_ID IN (SELECT C_DocType_ID FROM C_DocType "
			+ "WHERE DocBaseType='SOO' AND DocSubTypeSO NOT IN ('ON','OB','WR')) "*/
			//	que no sea consolidado
			//el consolidado se hará en el reporte, son facturas normales
			/*+ " AND M_PriceList_ID IN (SELECT M_PriceList_ID FROM M_PriceList "
			+ " WHERE IsActive = 'Y' AND IsConsolidated <> 'Y') "*/
			//end
			//+ "ORDER BY M_Warehouse_ID, C_BPartner_ID, Bill_Location_ID, C_Order_ID";
		//PAT Facturara en base a nueva tabla C_OrderPayCalendar
		sql = "SELECT opc.C_Order_ID,opc.DateEnd,C_OrderPayCalendar_ID, opc.C_DocType_ID FROM C_OrderPayCalendar opc " +
			 " INNER JOIN C_Order co ON (opc.C_Order_ID = co.C_Order_ID) " +
			 " WHERE co.DocStatus IN('CO','CL') AND co.IsSOTrx='Y' AND co.PaymentRule IN ('D','C') AND opc.IsInvoiced <> 'Y' ";
		if (p_C_BPartner_ID != 0)
				sql += " AND co.C_BPartner_ID = ?";	 
		if (p_C_Order_ID != 0)
			sql += " AND co.C_Order_ID = ?";
		if (p_DateOrdered != null && p_DateOrdered_To != null)
		{
			sql += " AND opc.DateEnd BETWEEN ? AND ? ";
		}
		
		pstmt = null;
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
			while (rs.next ())
			{
				MOrder order = new MOrder (getCtx(), rs.getInt("C_Order_ID"), get_TrxName());
				if(order.get_ID() == 2001834)
					log.config("log");
				//Factura/Boleta sin gancho 
				//if(!tieneGancho(order) && facturarPAT(order))
				if(!tieneGancho(order))
				{					
					/* ininoles se generan lineas a partir de funcion nueva
					MOrderLine[] oLines = order.getLines(true, null);
					for (int i = 0; i < oLines.length; i++)
					{
						MOrderLine oLine = oLines[i];
						BigDecimal toInvoice = oLine.getQtyOrdered();
						BigDecimal qtyEntered = toInvoice;
						if(toInvoice.compareTo(Env.ZERO) > 0)
							createLinePAT(order, oLine, toInvoice, qtyEntered, rs.getTimestamp("DateEnd"));
					}*/
					// se actualiza programa de facturación
					createLinePATView(order, rs.getTimestamp("DateEnd"),rs.getInt("C_DocType_ID"));
					
					if(m_invoice != null)
					{
						DB.executeUpdate("UPDATE C_OrderPayCalendar SET IsInvoiced = 'Y', C_Invoice_ID = "+m_invoice.get_ID()+" " +
								" WHERE C_OrderPayCalendar_ID = "+rs.getInt("C_OrderPayCalendar_ID"), get_TrxName());
					}
					completeInvoice();
					//m_created++;
				}
				//else if(tieneGancho(order) && facturarPAT(order)) // facturas pat con gancho 
				else if(tieneGancho(order)) // facturas pat con gancho
				{
					//factura 1 de servicios mensuales
					/* linea se reara con nueva funcion de creado de lineas
					MOrderLine[] oLines = order.getLines(true, null);
					for (int i = 0; i < oLines.length; i++)
					{
						MOrderLine oLine = oLines[i];
						BigDecimal toInvoice = oLine.getQtyOrdered();
						BigDecimal qtyEntered = toInvoice;
						if(toInvoice.compareTo(Env.ZERO) > 0)
							createLinePAT(order, oLine, toInvoice, qtyEntered, rs.getTimestamp("DateEnd"));
					}*/
					// se actualiza programa de facturación
					createLinePATView(order, rs.getTimestamp("DateEnd"),rs.getInt("C_DocType_ID"));
					if(m_invoice != null)
					{
						DB.executeUpdate("UPDATE C_OrderPayCalendar SET IsInvoiced = 'Y', C_Invoice_ID = "+m_invoice.get_ID()+" " +
								" WHERE C_OrderPayCalendar_ID = "+rs.getInt("C_OrderPayCalendar_ID"), get_TrxName());
					}
					completeInvoice();
					//m_created++;
					//end
					//factura 2 ganchos y despacho de ganchos
					/*if(facturarGanchoPAT(order))
					{
						//cabecera
						MInvoice invGancho = new MInvoice (order, 0, rs.getTimestamp("DateEnd"));
						if (!invGancho.save())
							throw new IllegalStateException("Could not create Invoice (o)");
						//Lineas
						String sqlGancho = "SELECT C_OrderLine_ID FROM C_OrderLine ol " +
								" LEFT JOIN M_Product mp ON (ol.M_Product_ID = mp.M_Product_ID) " +
								" LEFT JOIN M_Product_Category pc ON (mp.M_Product_Category_ID = pc.M_Product_Category_ID) " +
								" LEFT JOIN C_Charge cc ON (ol.C_Charge_ID = ol.C_Charge_ID) " +
								" LEFT JOIN C_ChargeType ct ON (cc.C_ChargeType_ID = ct.C_ChargeType_ID) " +
								" WHERE ol.C_Order_ID = "+order.get_ID()+" AND ol.IsActive = 'Y' " +
								" AND (upper(pc.value) like '%NOEDITO%' OR upper(ct.value) = 'TCF%')";
						PreparedStatement pstmtGancho = DB.prepareStatement (sqlGancho, get_TrxName());
						ResultSet rsGancho = pstmtGancho.executeQuery ();
						while (rsGancho.next())
						{
							MOrderLine oLineG = new MOrderLine(getCtx(), rsGancho.getInt("C_OrderLine_ID"), get_TrxName());
							MInvoiceLine line = new MInvoiceLine(invGancho);
							line.setOrderLine(oLineG);
							line.setQtyInvoiced(oLineG.getQtyEntered());
							line.setQtyEntered(oLineG.getQtyEntered());
							if (!line.save())
								throw new IllegalStateException("Could not create Invoice Line (o)");
						}
						if (!invGancho.processIt(p_docAction))
						{
							log.warning("completeInvoice - failed: " + m_invoice);
						}
						invGancho.saveEx();
						//addLog(invGancho.getC_Invoice_ID(), invGancho.getDateInvoiced(), null, invGancho.getDocumentNo());
						m_created++;
					}*/
					//end
				}				
				//actualizamos cantidad de lineas con productos
				/*DB.executeUpdate("UPDATE C_OrderLine SET QtyInvoiced = 0 WHERE C_OrderLine_ID IN " +
						" (SELECT C_OrderLine_ID FROM C_OrderLine ol " +
						" INNER JOIN M_Product mp ON (ol.M_Product_ID = mp.M_Product_ID) " +
						" INNER JOIN M_Product_Category pc ON (mp.M_Product_Category_ID = pc.M_Product_Category_ID) " +
						" WHERE ol.C_Order_ID = "+order.get_ID()+" AND ol.IsActive = 'Y' AND upper(pc.value) like 'EDITO%')", get_TrxName());
					*/
			}	//	for all orders
			rs.close ();
			pstmt.close ();
			pstmt = null;			
			
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		return "Se han generado "+m_created+" facturas";
	}	//	doIt
		
	
	/**************************************************************************
	 * 	Create Invoice Line from Order Line
	 *	@param order order
	 *	@param orderLine line
	 *	@param qtyInvoiced qty
	 *	@param qtyEntered qty
	 */
	private void createLine (MOrder order, MOrderLine orderLine, 
		BigDecimal qtyInvoiced, BigDecimal qtyEntered)
	{
		if (m_invoice == null)
		{
			m_invoice = new MInvoice (order, 0, order.getDateOrdered());
			//ininoles se setea tipo de doc
			/*
			if(ID_DocType > 0)
			{
				m_invoice.setC_DocType_ID(ID_DocType);
				m_invoice.setC_DocTypeTarget_ID(ID_DocType);
			}
			*/
			if (!m_invoice.save())
				throw new IllegalStateException("Could not create Invoice (o)");
		}
		//	
		MInvoiceLine line = new MInvoiceLine (m_invoice);
		line.setOrderLine(orderLine);
		line.setQtyInvoiced(qtyInvoiced);
		line.setQtyEntered(qtyEntered);
		line.setLine(m_line + orderLine.getLine());
		if (!line.save())
			throw new IllegalStateException("Could not create Invoice Line (o)");
		log.fine(line.toString());
	}	//	createLine
	
	/**
	 * 	Complete Invoice
	 */
	private void completeInvoice()
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

			//addLog(m_invoice.getC_Invoice_ID(), m_invoice.getDateInvoiced(), null, m_invoice.getDocumentNo());
			m_created++;
		}
		m_invoice = null;
		m_line = 0;
	}	//	completeInvoice
	
	private boolean tieneGancho(MOrder order)
	{
		int cant = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM C_OrderLine ol " +
				" INNER JOIN M_Product mp ON (ol.M_Product_ID = mp.M_Product_ID) " +
				" INNER JOIN M_Product_Category pc ON (mp.M_Product_Category_ID = pc.M_Product_Category_ID) " +
				" WHERE C_Order_ID = "+order.get_ID()+
				" AND upper(pc.value) like '%NOEDI%' ");
		if(cant > 0)
			return true;
		else
			return false;
	}
	/*private int IDDocBoleta()
	{
		int id_boleta = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_DocType_ID) FROM C_DocType WHERE IsActive = 'Y' " +
				"AND upper(NAME) like '%BOLETA%' AND DocBaseType = 'ARI'");
		return id_boleta;
	}*/
	/*private int IDDocFactura()
	{
		int id_boleta = DB.getSQLValue(get_TrxName(), "SELECT MAX(C_DocType_ID) FROM C_DocType WHERE IsActive = 'Y' " +
				" AND DocBaseType = 'ARI' AND (upper(NAME) like '%INVOICE%' OR upper(NAME) like '%FACTURA%')");
		return id_boleta;
	}*/
	/*private boolean facturarPAT(MOrder order)
	{
		//validamos que mes de facturación sea mayor a mes de orden
		/*if(p_DateInvoiced.getMonth() <= order.getDateOrdered().getMonth())
			return false;*/
		//validacion de no generar 2 facturas el mismo mes
		/*int mesReal = p_DateInvoiced.getMonth()+1;
		int cantV1 = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM C_Invoice ci " +
				" WHERE DocStatus IN ('CO','DR','CL','IP','IN') AND C_Order_ID = "+order.get_ID() +
				" AND extract(month from dateinvoiced) = "+mesReal);
		if(cantV1 > 0)
			return false;*/
		//validacion de fecha de vencimiento de contrato
		/*Timestamp finContrato = DB.getSQLValueTS(get_TrxName(), "SELECT MAX(datepromised3) FROM C_OrderLine " +
				" WHERE datepromised3 IS NOT NULL AND IsActive = 'Y' AND C_Order_ID = "+order.get_ID());
		//le sumamos uno al mes para facturar ultimo periodo ya que facturacion es por mes vencido
		finContrato.setMonth(finContrato.getMonth()+1);
		/*Calendar calendar = Calendar.getInstance();
		Timestamp hoy = new Timestamp(calendar.getTimeInMillis());*/		
		/*return true;
	}*/
	/*private boolean facturarGanchoPAT(MOrder order)
	{
		int cant = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM C_Invoice ci " +
				" INNER JOIN C_InvoiceLine il ON (ci.C_Invoice_ID = il.C_Invoice_ID) " +
				" INNER JOIN M_Product mp ON (il.M_Product_ID = mp.M_Product_ID) " +
				" INNER JOIN M_Product_Category pc ON (mp.M_Product_Category_ID = pc.M_Product_Category_ID) " +
				" WHERE DocStatus IN ('CO','DR','CL','IP','IN') AND C_Order_ID = "+order.get_ID()+
				" AND il.IsActive = 'Y' AND upper(pc.value) like '%NOEDITO%'");
		if(cant > 0)
			return false;
		else
			return true;
	}*/
	/*private void createLinePAT(MOrder order, MOrderLine orderLine, 
			BigDecimal qtyInvoiced, BigDecimal qtyEntered, Timestamp dateInvoiced)
		{
			if (m_invoice == null)
			{
				m_invoice = new MInvoice (order, 0, dateInvoiced);
				if (!m_invoice.save())
					throw new IllegalStateException("Could not create Invoice (o)");
			}
			//
			//ininoles todas las validaciones estaran en la nueva función
			boolean generaLinea = true;
			
			//se valida que no sea producto ni flete gancho
			if(orderLine.getM_Product_ID() > 0
					&& orderLine.getM_Product().getM_Product_Category().getValue().toUpperCase().contains("NOEDI"))
				generaLinea = false;
			if(orderLine.getC_Charge_ID() > 0)
				generaLinea = false;
			//end
			//se valida que linea tenga vigencia
			
			//@mfrojas se ingresa cambios realizados por HC (copesa)
			
			Timestamp inicio = (Timestamp)orderLine.get_Value("datepromised2");
			Timestamp fin = (Timestamp)orderLine.get_Value("datepromised3");
			
			//cambio agregado
			
	        BigDecimal linenetamt = orderLine.getLineNetAmt();
	        
	        boolean esnoedi = orderLine.getM_Product_ID() > 0 && orderLine.getM_Product().getM_Product_Category().getValue().toUpperCase().contains("NOEDI");

	        //obtener productref
	        
	        MProduct pref = new MProduct(getCtx(),Integer.valueOf(orderLine.get_ValueAsString("M_ProductRef_ID")),get_TrxName());
	        esnoedi = esnoedi || Integer.valueOf(orderLine.get_ValueAsString("M_ProductRef_ID")) > 0 && pref.getM_Product_Category().getValue().toUpperCase().contains("NOEDI");
			
			
	        if(!esnoedi && inicio != null && inicio.compareTo(dateInvoiced) > 0 )
	            generaLinea = false;
	       if( esnoedi && linenetamt.compareTo(Env.ONE) <= 0) 
	            generaLinea = false;
	       
	       //fin agregar codigo
	       
	       
	        if(fin != null && fin.compareTo(dateInvoiced) < 0)
	            generaLinea = false;
	 
			log.config("inicio "+inicio);
			log.config("dateInvoiced  "+dateInvoiced);
			log.config("orden "+order.get_ID());
			if(inicio != null && inicio.compareTo(dateInvoiced) > 0 )
				generaLinea = false;
			if(fin != null && fin.compareTo(dateInvoiced) < 0)
				generaLinea = false;
			//end
			
			if(generaLinea)
			{
				MInvoiceLine line = new MInvoiceLine (m_invoice);
				line.setOrderLine(orderLine);
				line.setQtyInvoiced(qtyInvoiced);
				line.setQtyEntered(qtyEntered);
				line.setLine(m_line + orderLine.getLine());
				if (!line.save())
					throw new IllegalStateException("Could not create Invoice Line (o)");
				//@mfrojas se agrega en un nuevo if, la variable enviada por copesa
				//el codigo original era sin if / else, solo venía desde bigdecimal amtpat hacia adelante
				
	            if( esnoedi )
	            {
	                line.setPrice((BigDecimal)orderLine.get_Value("PriceEntered"));
	                line.setLineNetAmt();
	                line.setTaxAmt();
	                line.save();
	            }
	            else
	            {
				//reemplazo de monto por monto mensual
	            	BigDecimal amtPAT = (BigDecimal)orderLine.get_Value("MonthlyAmount");
	            	if(amtPAT != null)
	            	{
	            		line.setPrice(amtPAT);
	            		line.setLineNetAmt();
	            		line.setTaxAmt();
	            		line.save();
	            	}
	            }
				log.fine(line.toString());
			}
		}	//	createLine*/
	private void createLinePATView(MOrder order,  Timestamp dateInvoiced, int ID_DocType)
	{	
		if (m_invoice == null)
		{
			m_invoice = new MInvoice (order, 0, dateInvoiced);
			if(ID_DocType > 0)
			{
				m_invoice.setC_DocType_ID(ID_DocType);
				m_invoice.setC_DocTypeTarget_ID(ID_DocType);
			}
			if (!m_invoice.save())
				throw new IllegalStateException("Could not create Invoice (o)");
		}
		//
		String sqlDet = "SELECT * FROM co_factcalendar WHERE C_Order_ID = ? AND ? BETWEEN DateStart AND DateEnd";
		try 
		{
			PreparedStatement pstmtLine = DB.prepareStatement (sqlDet, get_TrxName());
			pstmtLine.setInt(1, order.get_ID());
			pstmtLine.setTimestamp(2, dateInvoiced);		
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
		}
				
	}	//	createLine
	
}	//	InvoiceGenerate
