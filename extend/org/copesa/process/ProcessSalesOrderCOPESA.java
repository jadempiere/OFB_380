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
import java.sql.Timestamp;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPriceList;
import org.compiere.model.MRole;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.copesa.model.COPESAOrderOps;
import org.copesa.utils.DateUtils;


/**
 * 	Generate Calendar Line COPESA
 *	
 *  @author Italo Niñoles
 */
public class ProcessSalesOrderCOPESA extends SvrProcess
{
	private int 		id_SOrder;
	private String 		p_DocStatus;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare ()
	{
		id_SOrder = getRecord_ID();
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			
			if (para[i].getParameter() == null)
				;
			else if (para[i].getParameterName().equals("DocStatus"))
				p_DocStatus = para[i].getParameterAsString();
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
		// Partimos por agregar las líneas de flete
		//COPESAOrderOps.AddFreightLines((int)id_SOrder, (int) (Env.getAD_User_ID(Env.getCtx())), get_TrxName());
		
		MOrder order = new MOrder(getCtx(), id_SOrder, get_TrxName());
		Timestamp hoy = DateUtils.today();
		Timestamp instant = DateUtils.now();
		
		if(order.getDocStatus().compareTo("CO") != 0 
				&& order.getDocStatus().compareTo("VO") != 0)
		{
			//ininoles validaciones de campo 
			validFields(order);
			order.set_CustomColumn("DateCompleted", instant);
			order.setDateOrdered(instant);
			//ininoles end
			MRole rol = new MRole(getCtx(), Env.getAD_Role_ID(getCtx()), get_TrxName());
			if(order.isSOTrx())
			{
				if(p_DocStatus.compareTo("CO") == 0)
				{
					//ininoles antes de la validacion agregaremos las validaciones de los model
					//ModCOPESADateOrder
					MOrderLine[] oLines = order.getLines(false, null);				
					for (int i = 0; i < oLines.length; i++)
					{
						MOrderLine line = oLines[i];
						Timestamp dateStart = (Timestamp)line.get_Value("DatePromised2");
						Timestamp dateEnd = (Timestamp)line.get_Value("DatePromised3");
						//validacion de fecha de cabecera y lineas 
						if(dateStart != null)
						{
//							if(dateStart.compareTo(order.getDatePromised()) < 0)
//								throw new AdempiereException("Error : Fecha de Producto "+line.getM_Product().getName()+" incorrecta");
						}					
						//validacion de fechas relacionadas
						if(line.get_ValueAsInt("C_OrderLineRef_ID") > 0 && line.get_ValueAsBoolean("IsFree"))
						{
							MOrderLine lineAfter = new MOrderLine(getCtx(), line.get_ValueAsInt("C_OrderLineRef_ID"),get_TrxName());
							Timestamp dateStartAfter = (Timestamp)lineAfter.get_Value("DatePromised2");
							Timestamp newDate = DateUtils.nextDay( dateEnd );
							if(dateStartAfter.compareTo(newDate) != 0)
								throw new AdempiereException("Error: Fecha de Linea "+lineAfter.getLine()+" incorrecta");
						}
					}
					//ModCOPESAOrderPRuleInvoice
					if(order.getPaymentRule().compareToIgnoreCase("I") == 0 
							&& order.get_ValueAsString("TypePaymentRule").compareTo("E") != 0)
					{
						String ref = null;
						ref = order.get_ValueAsString("POReference2");
						if((order.getPOReference() == null || order.getPOReference().trim() == "") 
							&& (ref == null || ref.trim() == ""))
						{
							throw new AdempiereException("Error: Termino de pago necesita HES/OC");
						}
					}

					MPriceList pList = new MPriceList(getCtx(), order.getM_PriceList_ID(),get_TrxName());
					boolean ISSameBPLocator = pList.get_ValueAsBoolean("IsSameBPLocator");
					if(ISSameBPLocator)
					{
						int cant = DB.getSQLValue(get_TrxName(), "SELECT COUNT(DISTINCT(C_BPartner_Location_ID)) " +
								" FROM C_OrderLine WHERE C_Order_ID = "+order.get_ID());				
						if(cant > 1)
							throw new AdempiereException("Error: Direcciones no concordantes");
					}
					//ModCOPESAValidOrderCalendarLine
					int cant = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM C_OrderLine col" +
							" INNER JOIN M_Product mp ON (col.M_Product_ID = mp.M_Product_ID) " +
							" INNER JOIN M_Product_Category mpc ON (mp.M_Product_Category_ID = mpc.M_Product_Category_ID) " +
							" WHERE C_Order_ID = "+order.get_ID()+" AND col.C_CalendarCOPESA_ID IS NULL " +
							" AND upper(mpc.description) like '%EDITORIAL%'");
					if(cant > 0)
						throw new AdempiereException("Error: Calendario faltante en las lineas");
					//ModCOPESAValidPaymentRule
					if(pList.get_ValueAsString("PaymentRule") != null && pList.get_ValueAsString("PaymentRule").length() > 0
							&& pList.get_ValueAsString("PaymentRule") != "" && pList.get_ValueAsString("PaymentRule") != " ")
					{
						String PaymentRule2 = order.get_ValueAsString("TypePaymentRule");
						/*if(order.getPaymentRule() != null && 
								order.getPaymentRule().compareToIgnoreCase(pList.get_ValueAsString("PaymentRule")) != 0)*/
						if(PaymentRule2 != null && 
								PaymentRule2.compareToIgnoreCase(pList.get_ValueAsString("PaymentRule")) != 0)
						{
							String namePRule = DB.getSQLValueString(get_TrxName(), "SELECT rlt.NAME " +
									" FROM AD_Ref_List rl " +
									" INNER JOIN AD_Ref_List_Trl rlt ON(rlt.AD_Ref_List_ID = rl.AD_Ref_List_ID AND AD_Language='es_MX') " +
									" WHERE AD_Reference_ID = 195 AND rl.value = '"+pList.get_ValueAsString("PaymentRule")+"'");
							throw new AdempiereException("Error: Regla de pago debe ser "+namePRule);
						}
					}
					//ininoles fin validaciones
					//ininoles nueva validación1  16-08 bp de lineas deben existir en bp
					int cantV2 = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM C_OrderLine " +
							" WHERE C_Order_ID = "+order.get_ID()+"AND C_BPartnerRef_ID IS NOT NULL " +
							" AND C_BPartnerRef_ID NOT IN ( " +
							" SELECT C_BPartnerRef_ID FROM C_BPartnerRelated " +
							" WHERE C_BPartner_ID = "+order.getC_BPartner_ID()+" AND IsActive = 'Y' AND C_BPartnerRef_ID IS NOT NULL)");
					if(cantV2 > 0)
						throw new AdempiereException("Error: Socio de negocio de linea no pertenece a cliente de facturación");
					//ininoles end
					//ininoles nueva validación 21-09 no se puede completar pat sin cuota mensual 
					//ModCOPESAValidMAmtLine
					if(order.getPaymentRule().compareTo("D") == 0)
					{
						int cantMounthly = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM C_OrderLine ol " +
								" INNER JOIN M_Product mp ON (ol.M_Product_ID = mp.M_Product_ID) " +
								" INNER JOIN M_Product_Category pc ON (mp.M_Product_Category_ID = pc.M_Product_Category_ID) " +
								" WHERE ol.IsActive = 'Y' AND MonthlyAmount IS NULL AND upper(pc.value) LIKE 'EDITO%' " +
								" AND C_Order_ID = "+order.get_ID());
						if(cantMounthly > 0)
							throw new AdempiereException("Error: No Existen Precios PAT. Selecione otra Regla de Pago ");
					}					
					//ininoles end
					//ininoles nueva validación 25-01-2017 no se puede si no existe contacto telefonico activo en el BP 
					//ModCOPESAValidMAmtLine
					int cantContactPhone = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM AD_User" +
							" WHERE C_Bpartner_ID = "+order.getC_BPartner_ID()+" AND Type = '01' " +
							" AND IsActive = 'Y' ");
					if(cantContactPhone < 1)
						throw new AdempiereException("Error: Se debe crear un telefono de contacto al socio de negocio contratante");
					//ininoles end
					//ininoles nueva validación 31-01-2017 no se puede completar si hay una direccion sin zona o sector 
					//ModCOPESAValidMAmtLine
					int cantNoZoneSec = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) " +
							" FROM C_OrderLine ol " +
							" INNER JOIN C_BPartner_Location bpl ON (ol.C_BPartner_Location_ID = bpl.C_BPartner_Location_ID) " +
							" WHERE (ZONE IS NULL OR Sector IS NULL) " +
							" AND ol.IsActive = 'Y' AND C_order_ID = "+order.get_ID());
					if(cantNoZoneSec > 0)
					{
						order.set_CustomColumn("RequiresApprovalList", "EZ");	
						//order.setProcessed(true);
						order.save();
						commitEx();
						//return "Se Necesita Aprobación";
						throw new AdempiereException("Error: Direccion sin Zona/Sector. Favor Genere un Ticket");
					}
					//ininoles end
					//validaciones listas de precios valida
					//ModCOPESAValidPTermOrder.java
					int cantLPV = DB.getSQLValue(get_TrxName(), "SELECT COUNT(M_PriceList_ID) FROM M_PriceList WHERE M_PriceList_ID IN ( " +
							" SELECT pl.M_PriceList_ID FROM M_PriceList pl" +
							" INNER JOIN C_BPartnerPriceList bpl ON pl.M_PriceList_ID = bpl.M_PriceList_ID "+
							" WHERE pl.IsActive = 'Y' AND bpl.IsActive = 'Y' AND pl.IsSOPriceList = 'Y' "+
							" AND pl.IsInBPartner = 'Y' AND bpl.C_BPartner_ID = " + order.getC_BPartner_ID()+
							" UNION SELECT pl.M_PriceList_ID FROM M_PriceList pl " +
							" INNER JOIN AD_UserPriceList upl ON pl.M_PriceList_ID = upl.M_PriceList_ID" +
							" WHERE pl.IsActive = 'Y' AND upl.IsActive = 'Y' AND pl.IsSOPriceList = 'Y' " +
							" AND pl.IsInUser = 'Y' AND upl.AD_User_ID = "+Env.getAD_User_ID(getCtx())+
							" UNION " +
							" SELECT pl.M_PriceList_ID FROM M_PriceList pl " +
							" INNER JOIN AD_RolePriceList rpl ON pl.M_PriceList_ID = rpl.M_PriceList_ID " +
							" WHERE pl.IsActive = 'Y' AND rpl.IsActive = 'Y' AND pl.IsSOPriceList = 'Y' " +
							" AND ((pl.IsInChannel = 'Y' AND rpl.C_Channel_ID = "+order.get_ValueAsInt("C_Channel_ID")+" )" +
							" 	OR (pl.IsInRole = 'Y' AND rpl.AD_Role_ID = "+Env.getAD_Role_ID(getCtx())+" ) " +
							" 	OR (pl.IsInPTerm = 'Y' AND rpl.C_PaymentTerm_ID = "+order.getC_PaymentTerm_ID()+") " +
							"    ) " +
							" UNION SELECT pl.M_PriceList_ID FROM M_PriceList pl " +
							" WHERE pl.IsActive = 'Y' AND pl.IsSOPriceList = 'Y' AND pl.IsInUser = 'N' " +
							" AND pl.IsInBPartner = 'N' AND pl.IsInRole = 'N' " +
							" AND pl.IsInPTerm = 'N' AND pl.IsInChannel = 'N' " +
							" AND pl.AD_Client_ID = "+Env.getAD_Client_ID(getCtx())+")" +
							" AND (dateStart IS NULL OR dateStart < now()) AND (dateEnd IS NULL OR dateEnd > now())" +
							" AND M_PriceList_ID = "+order.getM_PriceList_ID());
					if(cantLPV < 1)
							return "Error: Termino de pago no permitido para esta lista de precio";
					
					//validaciones nivel listas de precios 
					//ModCOPESAValidPTermOrder.java
					if(validCantLinePL(order, order.get_TrxName()) > 0)
					{
						return "ERROR: Pedido debe incluir al menos "+validCantLinePL(order, order.get_TrxName())+
						" líneas por restricción de la lista de precios";
					}
					
					// HCH20170420: Validación de fechas, y se comienzan a usar funciones utilitarias
					validDates(order, hoy);
					//--
					
					//FIN VALIDACIONES
					// Ahora pasa todo a validación de backoffice, a menos que sea un rol que inmediatamente deja aprobado al completar
					if( rol.get_ValueAsBoolean("OrderApprobal") )
					{
						order.setDocStatus("IP");						
						//fecha se actualiza solo antes de completar
						order.set_CustomColumn("DateCompleted", hoy);
						order.set_CustomColumn("RequiresApprovalList", null);
						order.setDocAction("CO");
						//order.save();
						if ( !order.processIt("CO") )
							throw new AdempiereException( order.getProcessMsg() );
						if( !order.save() )
							throw new AdempiereException(log.retrieveErrorString("ERROR: No se pudo guardar"));
					}					
					else 
					{
						order.set_CustomColumn("RequiresApprovalList", "WA");	
						order.setProcessed(true);
						if( !order.save() )
							throw new AdempiereException(log.retrieveErrorString("ERROR: No se pudo guardar"));
						commitEx();
						//return "Se Necesita Aprobación";
						throw new AdempiereException("Se Necesita Aprobación");
					}
				}
				else if(p_DocStatus.compareTo("VO") == 0)
				{
					order.setDocStatus("IP");
					order.save();
					order.set_CustomColumn("RequiresApprovalList", null);
					order.save();
					order.processIt("VO");
					order.save();	
				}
				else if(p_DocStatus.compareTo("IN") == 0)
				{
					order.setProcessed(false);
					order.setDocStatus("IN");
					//order.set_CustomColumn("ISRequiresApproval", false);		
					order.set_CustomColumn("RequiresApprovalList", "RE");	
					order.save();		
				}
			}
		}
		return "Procesado";
	}	//	doIt
	public boolean reqApproval(MOrder order)
	{
		if(order.get_ValueAsString("TypePaymentRule").compareTo("C") == 0)
			return true;
		if(order.getPaymentRule().compareTo("D") == 0)
		{
			/*BigDecimal amt = DB.getSQLValueBD(null,"SELECT SUM(LineNetAmt)  " +
					" FROM C_OrderLine WHERE (C_Charge_ID > 0 OR IsFree = 'Y') " +
					" AND C_Order_ID = ? ",order.get_ID());*/
			BigDecimal amt = DB.getSQLValueBD(null,"SELECT SUM(LineNetAmt)  " +
					" FROM C_OrderLine col" +
					" INNER JOIN M_Product mp ON (col.M_Product_ID = mp.M_Product_ID)" +
					" INNER JOIN M_Product_Category mpc ON (mp.M_Product_Category_ID = mpc.M_Product_Category_ID)" +
					" WHERE col.IsActive = 'Y' AND upper(mpc.description) NOT LIKE 'EDITORIAL' " +
					" AND C_Order_ID = ? ",order.get_ID());			
			if(amt == null)
				amt = Env.ZERO;
			if(amt.compareTo(Env.ONEHUNDRED) < 0)
				return false;
		}	
		return true;
	}
	public boolean validFields(MOrder order)
	{
		if(order.getPaymentRule().compareTo("S") == 0)
			if(order.get_ValueAsInt("C_CalendarCOPESALine_ID") <=0 )
				throw new AdempiereException("Llenar campos obligatorios");
		if(order.get_ValueAsInt("C_Channel_ID") <=0 )
			throw new AdempiereException("Llenar campos obligatorios");
		if(order.get_ValueAsString("Type") == null || order.get_ValueAsString("Type") == ""
			|| order.get_ValueAsString("Type") == " ")
			throw new AdempiereException("Llenar campos obligatorios");
		if(order.get_ValueAsString("OrderType2") == null || order.get_ValueAsString("Type").trim() == "OrderType2"
			|| order.get_ValueAsString("OrderType2").trim() == " ")
			throw new AdempiereException("Llenar campos obligatorios");
		return true;
	}
	public static int validCantLinePL (MOrder order, String Trx)
	{
		int cant = DB.getSQLValue(Trx, "select count(1) from c_orderline col" +
				" join c_order co on (co.c_order_id = col.c_order_id) " +
				" join m_product mp on (mp.m_product_id = col.m_product_id) " +
				" where mp.description not in ('FULLSD') " +
				" and col.isactive = 'Y' and c_orderlineref_id is null " +
				" and co.c_order_id = "+order.get_ID());
		MPriceList pList = new MPriceList(Env.getCtx(), order.getM_PriceList_ID(), Trx);
		int cantLinePL = pList.get_ValueAsInt("minLevel");
		if(cant < cantLinePL)
			return cantLinePL;
		else
			return 0;
	}	

	public boolean validDates(MOrder _order, Timestamp _instant)
	{
		//validación no se puede completar si alguna linea es menor a hoy
		int cantVLine = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM C_OrderLine " +
				" WHERE IsActive = 'Y' AND C_Order_ID = "+ _order.get_ID()+" AND M_Product_ID != null AND DatePromised2 <= ?", _instant);
		if( cantVLine > 0 )
			throw new AdempiereException("Error: Fecha de Linea debe ser mayor a fecha de hoy");
		if( _order.getDatePromised().compareTo(_instant) <= 0 )
			throw new AdempiereException("Error: Fecha cabecera anterior a fecha de facturación");
		if(_order.getDateOrdered().compareTo(_instant) < 0)
			throw new AdempiereException("Error: Fecha cabecera anterior a fecha de facturación");
		int value = _order.getDateOrdered().compareTo(_order.getDatePromised());
		log.config("value "+value);
		if(_order.getDateOrdered().compareTo( _order.getDatePromised())>=0 )
			throw new AdempiereException("Error: Fecha de facturación debe ser menor a fecha de inicio");
		return true;
	}

}	//	BPGroupAcctCopy
