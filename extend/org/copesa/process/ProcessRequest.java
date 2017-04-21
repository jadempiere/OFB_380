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
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MOrderLine;
import org.compiere.model.MRequest;
import org.compiere.model.MRequestType;
import org.compiere.model.MResolution;
import org.compiere.model.X_C_OrderShipCalendar;
import org.compiere.model.X_R_RequestLine;
import org.compiere.model.X_R_RequestLineAction;
import org.compiere.model.X_R_ServiceRequest;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;


/**
 * 	Generate Calendar Line COPESA
 *	
 *  @author Italo Niñoles
 */
public class ProcessRequest extends SvrProcess
{
	/** Product					*/
	//private String DocAction;
	/**	ID COPESA Calendar				*/
	private int 		ID_Request;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare ()
	{
		ID_Request = getRecord_ID();
	}	//	prepare

	
	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		MRequest req = new MRequest(getCtx(), ID_Request, get_TrxName());
		String strMsg = "Ticket Procesado";
		if(req.get_ValueAsString("DocStatus").compareTo("DR") == 0)
		{
			
			//validación al procesar
			if(req.get_ValueAsInt("C_BPartnerContact_ID") > 0)
			{
				boolean flag = false;
				if(req.get_ValueAsInt("C_BPartnerContact_ID") == req.getC_Order().getC_BPartner_ID())
					flag = true;
				else
				{
					int cant = DB.getSQLValue(get_TrxName(), "select count(1) FROM C_OrderLine WHERE C_Order_ID = "+req.getC_Order_ID()+" AND C_BpartnerRef_ID = "+req.get_ValueAsInt("C_BPartnerContact_ID"));
					if(cant > 0)
						flag = true;
				}
				if(!flag)
					throw new AdempiereException("Error: El socio de contacto debe estar relacionada con la orden");			
			}
			
			String sql = "SELECT ol.Line, ol.C_BPartner_ID, ol.C_BPartnerRef_ID,ol.M_Product_ID," +
					" ol.DatePromised2, ol.DatePromised3, ol.C_BPartner_Location_ID,ol.QtyEntered, ol.C_OrderLine_ID " +
					" FROM C_OrderLine ol " +
					" INNER JOIN M_Product mp ON (ol.M_Product_ID = mp.M_Product_ID)" +
					" INNER JOIN M_Product_Category pc ON (mp.M_Product_Category_ID = pc.M_Product_Category_ID) " +
					" WHERE pc.description like 'EDITORIAL' " +
					" AND ol.IsActive = 'Y' AND ol.M_Product_ID > 0" +
					" AND ol.C_Order_ID = "+req.get_ValueAsInt("C_Order_ID");
			if(req.get_ValueAsInt("C_BPartner_ID") > 0)
				sql = sql + " AND ol.C_BPartnerRef_ID = " + req.get_ValueAsInt("C_BPartner_ID");
			
			if(req.get_ValueAsInt("C_BPartner_Location_ID") > 0)
				sql = sql + " AND ol.C_BPartner_Location_ID = " + req.get_ValueAsInt("C_BPartner_Location_ID");
			
			if(req.get_ValueAsInt("C_OrderLine_ID") > 0)
				sql = sql + " AND ol.C_OrderLine_ID = " + req.get_ValueAsInt("C_OrderLine_ID");
			sql = sql + " ORDER BY ol.Line";
				
			PreparedStatement pstmt = DB.prepareStatement (sql, get_TrxName());		
			try
			{
				ResultSet rs = pstmt.executeQuery ();
				while (rs.next())
				{
					X_R_RequestLine rLine = new X_R_RequestLine(getCtx(), 0, get_TrxName());
					rLine.setR_Request_ID(req.get_ID());
					rLine.setAD_Org_ID(req.getAD_Org_ID());
					rLine.setLine(rs.getInt("Line"));
					rLine.setC_BPartner_ID(rs.getInt("C_BPartner_ID"));
					rLine.setC_BPartnerRef_ID(rs.getInt("C_BPartnerRef_ID"));
					rLine.setM_Product_ID(rs.getInt("M_Product_ID"));
					rLine.setDatePromised2(rs.getTimestamp("DatePromised2"));
					rLine.setDatePromised3(rs.getTimestamp("DatePromised3"));
					rLine.setC_BPartner_Location_ID(rs.getInt("C_BPartner_Location_ID"));
					rLine.setQtyEntered(rs.getBigDecimal("QtyEntered"));
					rLine.set_CustomColumn("C_OrderLine_ID", rs.getInt("C_OrderLine_ID"));
					Timestamp inicio = (Timestamp)req.get_Value("StartDate");
					Timestamp fin = (Timestamp)req.get_Value("EndDate");
					if(req.get_ValueAsString("RequestAcction") != null && req.get_ValueAsString("RequestAcction").length() > 0)
						rLine.setRequestAcction(req.get_ValueAsString("RequestAcction"));
					if(inicio != null)
						rLine.setDatePromised2Ref(inicio);
					if(fin != null)
						rLine.setDatePromised3Ref(fin);
					if(req.get_ValueAsInt("C_BPartner_LocationRef_ID") > 0)
						rLine.set_CustomColumn("C_BPartner_LocationRef_ID", req.get_ValueAsInt("C_BPartner_LocationRef_ID"));
					rLine.save();
				}
			}catch (Exception e)
			{
				log.log(Level.SEVERE, sql, e);
			}
			req.set_CustomColumn("DocStatus", "IP");
		}
		else if(req.get_ValueAsString("DocStatus").compareTo("IP") == 0)
		{
			//validación al procesar
			if(req.get_ValueAsInt("C_BPartnerContact_ID") > 0)
			{
				boolean flag = false;
				if(req.get_ValueAsInt("C_BPartnerContact_ID") == req.getC_Order().getC_BPartner_ID())
					flag = true;
				else
				{
					int cant = DB.getSQLValue(get_TrxName(), "select count(1) FROM C_OrderLine WHERE C_Order_ID = "+req.getC_Order_ID()+" AND C_BpartnerRef_ID = "+req.get_ValueAsInt("C_BPartnerContact_ID"));
					if(cant > 0)
						flag = true;
				}
				if(!flag)
					throw new AdempiereException("Error: El socio de contacto debe estar relacionada con la orden");			
			}
			if(req.get_ValueAsString("RequestAcction").compareTo("G") == 0)
			{
				//generacion de servicerequest
				X_R_ServiceRequest sReq = new X_R_ServiceRequest(getCtx(), 0, get_TrxName());
				sReq.setAD_Org_ID(0);
				sReq.setIsActive(true);				
				sReq.setC_Order_ID(req.getC_Order_ID());
				sReq.setC_OrderLine_ID(req.get_ValueAsInt("C_OrderLine_ID"));				
				sReq.setR_Request_ID(req.get_ID());
				//ininoles se setea workflow y nodo inicial
				if(req.getR_Resolution_ID() > 0)
				{
					MResolution rType = new MResolution(getCtx(), req.getR_Resolution_ID(), get_TrxName());
					if(rType.get_ValueAsInt("AD_Workflow_ID") > 0)
						sReq.setAD_Workflow_ID(rType.get_ValueAsInt("AD_Workflow_ID"));
					if(rType.get_ValueAsInt("AD_WF_Node_ID") > 0)
					{
						sReq.set_CustomColumn("AD_WF_Node_ID",rType.get_ValueAsInt("AD_WF_Node_ID"));
						//seteo de usuario por defecto
						int cantUser = DB.getSQLValue(get_TrxName(),"SELECT COUNT(1) FROM AD_WF_Node_AccessCOPESA " +
								" WHERE AD_WF_Node_ID = "+rType.get_ValueAsInt("AD_WF_Node_ID")+" AND IsActive = 'Y'");
						if(cantUser >= 1)//seteo por defecto
						{
							int ID_User = DB.getSQLValue(get_TrxName(),"SELECT MIN(AD_User_ID) FROM AD_WF_Node_AccessCOPESA " +
									" WHERE AD_WF_Node_ID = "+rType.get_ValueAsInt("AD_WF_Node_ID")+" AND IsActive = 'Y'");
							sReq.setAD_User_ID(ID_User);
						}
					}
				}
				//se copian nuevos campos de sr
				sReq.setSR_AccountType(req.get_ValueAsString("SR_AccountType"));
				sReq.setSR_Amt((BigDecimal)req.get_Value("SR_Amt"));
				sReq.setSR_BPartner_Loaction_ID(req.get_ValueAsInt("SR_BPartner_Loaction_ID"));
				sReq.setSR_ChargeDate((Timestamp)req.get_Value("SR_ChargeDate"));
				sReq.setSR_Comments(req.get_ValueAsString("SR_Comments"));
				sReq.setSR_CorrectValue(req.get_ValueAsString("SR_CorrectValue"));
				sReq.setSR_CreditCardDueDate(req.get_ValueAsString("SR_CreditCardDueDate"));
				sReq.setSR_CreditCardNo(req.get_ValueAsString("SR_CreditCardNo"));
				sReq.setSR_Date((Timestamp)req.get_Value("SR_Date"));
				sReq.setSR_Description(req.get_ValueAsString("SR_Description"));
				sReq.setSR_DueDate((Timestamp)req.get_Value("SR_DueDate"));
				sReq.setSR_EditionNo(req.get_ValueAsString("SR_EditionNo"));
				sReq.setSR_EMail(req.get_ValueAsString("SR_EMail"));
				sReq.setSR_InvoiceNo(req.get_ValueAsString("SR_InvoiceNo"));
				sReq.setSR_Last4Digits(req.get_ValueAsString("SR_Last4Digits"));
				sReq.setSR_Name(req.get_ValueAsString("SR_Name"));
				sReq.setSR_OperationNo(req.get_ValueAsString("SR_OperationNo"));
				if(req.get_ValueAsString("SR_PartialOrTotal") != null && req.get_ValueAsString("SR_PartialOrTotal").trim().length() > 0)
					sReq.setSR_PartialOrTotal(req.get_ValueAsString("SR_PartialOrTotal"));
				sReq.setSR_Phone(req.get_ValueAsString("SR_Phone"));
				if(req.get_ValueAsString("SR_Reason") != null && req.get_ValueAsString("SR_Reason").trim().length() > 0)
					sReq.setSR_Reason(req.get_ValueAsString("SR_Reason"));
				sReq.setSR_Reference(req.get_ValueAsString("SR_Reference"));
				sReq.setSR_User_ID(req.get_ValueAsInt("SR_User_ID"));
				sReq.set_CustomColumn("SR_PhoneUser_ID",req.get_ValueAsInt("SR_PhoneUser_ID"));
				sReq.set_CustomColumn("CreditCardType",req.get_ValueAsInt("CreditCardType"));				
				sReq.save();
				req.set_CustomColumn("DocStatus", "IS");
				req.setProcessed(true);
			}
			else
			{
			
				//validación accion de las lineas
				int cantFlag = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM R_RequestLine " +
						
						" WHERE R_Request_ID = "+req.get_ID()+" AND (RequestAcction IS NOT NULL AND RequestAcction <> '"+req.get_ValueAsString("RequestAcction")+"')");
				if(cantFlag > 0)
					throw new AdempiereException("Acción en las lineas no concordante");			
				
				String sql = " SELECT R_RequestLine_ID "+
				" FROM R_RequestLine WHERE IsActive = 'Y' " +
				" AND R_Request_ID = "+req.get_ID();
			
				PreparedStatement pstmt = DB.prepareStatement (sql, get_TrxName());		
				try
				{
					ResultSet rs = pstmt.executeQuery ();
					while (rs.next())
					{
						X_R_RequestLine rLine = new X_R_RequestLine(getCtx(), rs.getInt("R_RequestLine_ID"), get_TrxName());
						if(rLine.getRequestAcction() != null && rLine.getRequestAcction().compareTo("Z") == 0)
						{
							//desactivamos las lineas suspendidas y las agregamos a detale
							String sqlZoom = " SELECT C_OrderShipCalendar_ID "+
							" FROM C_OrderShipCalendar WHERE IsActive = 'Y' " +
							" AND C_OrderLine_ID = "+rLine.get_ValueAsInt("C_OrderLine_ID")+
							" AND dateTrx >= ? AND dateTrx <= ? ";
						
							PreparedStatement pstmtZoom = DB.prepareStatement (sqlZoom, get_TrxName());		
							try
							{
								pstmtZoom.setTimestamp(1,rLine.getDatePromised2Ref());
								pstmtZoom.setTimestamp(2,rLine.getDatePromised3Ref());
								ResultSet rsZoom = pstmtZoom.executeQuery ();
								while (rsZoom.next())
								{
									X_C_OrderShipCalendar oLineShip = new X_C_OrderShipCalendar(getCtx(),rsZoom.getInt("C_OrderShipCalendar_ID"),get_TrxName());
									oLineShip.set_CustomColumn("R_Request_ID",rLine.getR_Request_ID());
									oLineShip.setIsActive(false);								
									oLineShip.save();
									X_R_RequestLineAction rLAction = new X_R_RequestLineAction(getCtx(), 0, get_TrxName());
									rLAction.setR_Request_ID(rLine.getR_Request_ID());
									rLAction.setR_RequestLine_ID(rLine.get_ID());
									rLAction.setAD_Org_ID(rLine.getAD_Org_ID());
									rLAction.setTypeAcction(0);
									rLAction.setC_OrderShipCalendar_ID(oLineShip.get_ID());
									rLAction.save();
								}
							}catch (Exception e)
							{
								log.log(Level.SEVERE, sql, e);
							}
							//creamos las lineas nuevas de despacho
							String sqlNLine = "SELECT *,(SELECT AD_Org_ID FROM C_Order WHERE C_Order_ID = "+req.getC_Order_ID()+" )as AD_Org_ID " +
							" FROM RVOFB_OrderShipFull co WHERE C_Order_ID = "+req.getC_Order_ID()+
							" AND C_OrderLine_ID = "+rLine.get_ValueAsInt("C_OrderLine_ID")+
							" AND DateTrx >= ? AND DateTrx <= ?";
							//calculamos nuevas fechas de pauta
							MOrderLine oLine = new MOrderLine(getCtx(), rLine.get_ValueAsInt("C_OrderLine_ID"), get_TrxName());
							Timestamp DatePromise2New = (Timestamp)oLine.get_Value("DatePromised3");
							long timeDif = rLine.getDatePromised3Ref().getTime() - rLine.getDatePromised2Ref().getTime();
							Timestamp DatePromise3New = new Timestamp(DatePromise2New.getTime()+timeDif);
							if(DatePromise2New != null && DatePromise3New != null)
							{
								try 
								{
									PreparedStatement pstmtNLine = DB.prepareStatement(sqlNLine,get_TrxName());
									pstmtNLine.setTimestamp(1,DatePromise2New);
									pstmtNLine.setTimestamp(2,DatePromise3New);
									ResultSet rsNLine = pstmtNLine.executeQuery();
									while (rsNLine.next()) 
									{
										X_C_OrderShipCalendar sCal = new X_C_OrderShipCalendar(getCtx(), 0,get_TrxName());
										sCal.setC_Order_ID(rsNLine.getInt("C_Order_ID"));
										sCal.setAD_Org_ID(rsNLine.getInt("AD_Org_ID"));
										sCal.setIsActive(true);
										sCal.setC_OrderLine_ID(rsNLine.getInt("C_OrderLine_ID"));
										sCal.setC_CalendarCOPESA_ID(rsNLine.getInt("C_CalendarCOPESA_ID"));
										sCal.setC_CalendarCOPESALine_ID(rsNLine.getInt("C_CalendarCOPESALine_ID"));
										sCal.setDateTrx(rsNLine.getTimestamp("DateTrx"));
										sCal.set_CustomColumn("M_Product_ID",rsNLine.getInt("M_Product_ID"));
										sCal.set_CustomColumn("R_Request_ID",rLine.getR_Request_ID());
										sCal.save();
									}						
								}
								catch (Exception e) 
								{
									log.config(e.toString());
								}
							}
							//actualizamos fecha fin orden con update directo para que no modifique precios ni nada
							//if(rLine.getDatePromised3New() != null)
							if(DatePromise3New != null)
							{
								String sqlUpdate = "UPDATE C_OrderLine SET DatePromised3 = ? WHERE C_OrderLine_ID = "+rLine.get_ValueAsInt("C_OrderLine_ID");
								PreparedStatement pstmtUpdate = DB.prepareStatement(sqlUpdate,get_TrxName());
								pstmtUpdate.setTimestamp(1,DatePromise3New);
								pstmtUpdate.executeUpdate();
							}
						}
						if(rLine.getRequestAcction() != null && rLine.getRequestAcction().compareTo("A") == 0)
						{
							//activamos las lineas suspendidas y las agregamos a detale como baja
							String sqlZoom = " SELECT C_OrderShipCalendar_ID "+
							" FROM C_OrderShipCalendar WHERE IsActive = 'N' " +
							" AND C_OrderLine_ID = "+rLine.get_ValueAsInt("C_OrderLine_ID")+
							" AND dateTrx >= ? AND dateTrx <= ? AND R_Request_ID > 0";
						
							PreparedStatement pstmtZoom = DB.prepareStatement (sqlZoom, get_TrxName());		
							try
							{
								pstmtZoom.setTimestamp(1,rLine.getDatePromised2Ref());
								pstmtZoom.setTimestamp(2,rLine.getDatePromised3Ref());
								ResultSet rsZoom = pstmtZoom.executeQuery ();
								while (rsZoom.next())
								{
									X_C_OrderShipCalendar oLineShip = new X_C_OrderShipCalendar(getCtx(),rsZoom.getInt("C_OrderShipCalendar_ID"),get_TrxName());
									oLineShip.set_CustomColumn("R_Request_ID",rLine.getR_Request_ID());
									oLineShip.setIsActive(true);								
									oLineShip.save();
									X_R_RequestLineAction rLAction = new X_R_RequestLineAction(getCtx(), 0, get_TrxName());
									rLAction.setR_Request_ID(rLine.getR_Request_ID());
									rLAction.setR_RequestLine_ID(rLine.get_ID());
									rLAction.setAD_Org_ID(rLine.getAD_Org_ID());
									rLAction.setTypeAcction(1);
									rLAction.setC_OrderShipCalendar_ID(oLineShip.get_ID());
									rLAction.save();
								}
							}catch (Exception e)
							{
								log.log(Level.SEVERE, sql, e);
							}
							//calculamos nuevas fechas 
							MOrderLine oLine = new MOrderLine(getCtx(), rLine.get_ValueAsInt("C_OrderLine_ID"), get_TrxName());
							Timestamp DatePromise2New = (Timestamp)oLine.get_Value("DatePromised3");
							long timeDif = rLine.getDatePromised3Ref().getTime() - rLine.getDatePromised2Ref().getTime();
							Timestamp DatePromise3New = new Timestamp(DatePromise2New.getTime()-timeDif);
							
							//se desactivan lineas de solicitud antigua
							/*String sqlUp = " UPDATE C_OrderShipCalendar SET IsActive = 'N' " +
									" WHERE C_OrderShipCalendar_ID IN (SELECT C_OrderShipCalendar_ID "+
									" FROM C_OrderShipCalendar WHERE IsActive = 'Y' " +
									" AND C_OrderLine_ID = "+rLine.get_ValueAsInt("C_OrderLine_ID")+
									" AND dateTrx >= ? AND dateTrx <= ? " +
									" AND R_Request_ID <> "+rLine.getR_Request_ID()+")";*/
							String sqlUp = " UPDATE C_OrderShipCalendar SET IsActive = 'N' " +
							" WHERE C_OrderShipCalendar_ID IN (SELECT C_OrderShipCalendar_ID "+
							" FROM C_OrderShipCalendar WHERE IsActive = 'Y' " +
							" AND C_OrderLine_ID = "+rLine.get_ValueAsInt("C_OrderLine_ID")+
							" AND dateTrx > ? )";
						
							PreparedStatement pstmtUp = DB.prepareStatement (sqlUp, get_TrxName());		
							try
							{
								pstmtUp.setTimestamp(1,DatePromise3New);
								//pstmtUp.setTimestamp(2,rLine.getDatePromised3Ref());
								pstmtUp.executeUpdate();
							}catch (Exception e)
							{
								log.log(Level.SEVERE, sql, e);
							}
												
							//actualizamos fecha fin orden con update directo para que no modifique precios ni nada
							//extencion de contrato
							//if(rLine.getDatePromised3New() != null)
							if(DatePromise3New != null)
							{
								String sqlUpdate = "UPDATE C_OrderLine SET DatePromised3 = ? WHERE C_OrderLine_ID = "+rLine.get_ValueAsInt("C_OrderLine_ID");
								PreparedStatement pstmtUpdate = DB.prepareStatement(sqlUpdate,get_TrxName());
								pstmtUpdate.setTimestamp(1,DatePromise3New);
								pstmtUpdate.executeUpdate();
							}
						}
						if(rLine.getRequestAcction() != null && rLine.getRequestAcction().compareTo("P") == 0)
						{
							//actualizamos direccion de la linea de orden
							DB.executeUpdate("UPDATE C_OrderLine SET C_Bpartner_Location_ID = "+rLine.getC_BPartner_LocationRef_ID()+" WHERE C_OrderLine_ID = "+rLine.get_ValueAsInt("C_OrderLine_ID"), get_TrxName());
							
							//desactivamos las lineas suspendidas y las agregamos a detale
							String sqlZoom = " SELECT C_OrderShipCalendar_ID "+
							" FROM C_OrderShipCalendar WHERE IsActive = 'Y' " +
							" AND C_OrderLine_ID = "+rLine.get_ValueAsInt("C_OrderLine_ID")+
							" AND dateTrx >= ? ";
						
							PreparedStatement pstmtZoom = DB.prepareStatement (sqlZoom, get_TrxName());		
							try
							{
								pstmtZoom.setTimestamp(1,rLine.getDatePromised2Ref());							
								ResultSet rsZoom = pstmtZoom.executeQuery ();
								while (rsZoom.next())
								{
									X_C_OrderShipCalendar oLineShip = new X_C_OrderShipCalendar(getCtx(),rsZoom.getInt("C_OrderShipCalendar_ID"),get_TrxName());
									oLineShip.set_CustomColumn("R_Request_ID",rLine.getR_Request_ID());
									oLineShip.setIsActive(false);								
									oLineShip.save();
									X_R_RequestLineAction rLAction = new X_R_RequestLineAction(getCtx(), 0, get_TrxName());
									rLAction.setR_Request_ID(rLine.getR_Request_ID());
									rLAction.setR_RequestLine_ID(rLine.get_ID());
									rLAction.setAD_Org_ID(rLine.getAD_Org_ID());
									rLAction.setTypeAcction(0);
									rLAction.setC_OrderShipCalendar_ID(oLineShip.get_ID());
									rLAction.save();
								}
							}catch (Exception e)
							{
								log.log(Level.SEVERE, sql, e);
							}
							//creamos las lineas nuevas de despacho
							String sqlNLine = "SELECT *,(SELECT AD_Org_ID FROM C_Order WHERE C_Order_ID = "+req.getC_Order_ID()+" )as AD_Org_ID " +
							" FROM RVOFB_OrderShipFull co WHERE C_Order_ID = "+req.getC_Order_ID()+
							" AND C_OrderLine_ID = "+rLine.get_ValueAsInt("C_OrderLine_ID")+
							" AND DateTrx >= ? AND DateTrx <= ?";
							//calculo de fecha fin
							Calendar calendar = Calendar.getInstance();
							calendar.setTimeInMillis(rLine.getDatePromised2Ref().getTime());
							calendar.add(Calendar.MONTH, 2);
							Timestamp dateFin = new Timestamp(calendar.getTimeInMillis());
							
							//calculamos nuevas fechas de pauta						
							if(rLine.getDatePromised2Ref() != null)
							{
								try 
								{
									PreparedStatement pstmtNLine = DB.prepareStatement(sqlNLine,get_TrxName());
									pstmtNLine.setTimestamp(1,rLine.getDatePromised2Ref());
									pstmtNLine.setTimestamp(2,dateFin);
									ResultSet rsNLine = pstmtNLine.executeQuery();
									while (rsNLine.next()) 
									{
										X_C_OrderShipCalendar sCal = new X_C_OrderShipCalendar(getCtx(), 0,get_TrxName());
										sCal.setC_Order_ID(rsNLine.getInt("C_Order_ID"));
										sCal.setAD_Org_ID(rsNLine.getInt("AD_Org_ID"));
										sCal.setIsActive(true);
										sCal.setC_OrderLine_ID(rsNLine.getInt("C_OrderLine_ID"));
										sCal.setC_CalendarCOPESA_ID(rsNLine.getInt("C_CalendarCOPESA_ID"));
										sCal.setC_CalendarCOPESALine_ID(rsNLine.getInt("C_CalendarCOPESALine_ID"));
										sCal.setDateTrx(rsNLine.getTimestamp("DateTrx"));
										sCal.set_CustomColumn("M_Product_ID",rsNLine.getInt("M_Product_ID"));
										sCal.set_CustomColumn("R_Request_ID",rLine.getR_Request_ID());
										sCal.set_CustomColumn("C_BPartner_Location_ID ",rLine.getC_BPartner_LocationRef_ID());
										sCal.save();
									}						
								}
								catch (Exception e) 
								{
									log.config(e.toString());
								}
							}
						}
						if(rLine.getRequestAcction() != null && rLine.getRequestAcction().compareTo("T") == 0)
						{
							//desactivamos las lineas suspendidas y las agregamos a detale
							String sqlZoom = " SELECT C_OrderShipCalendar_ID "+
							" FROM C_OrderShipCalendar WHERE IsActive = 'Y' " +
							" AND C_OrderLine_ID = "+rLine.get_ValueAsInt("C_OrderLine_ID")+
							" AND dateTrx >= ? AND DateTrx <= ?";
						
							PreparedStatement pstmtZoom = DB.prepareStatement (sqlZoom, get_TrxName());		
							try
							{
								pstmtZoom.setTimestamp(1,rLine.getDatePromised2Ref());							
								pstmtZoom.setTimestamp(2,rLine.getDatePromised3Ref());
								ResultSet rsZoom = pstmtZoom.executeQuery ();
								while (rsZoom.next())
								{
									X_C_OrderShipCalendar oLineShip = new X_C_OrderShipCalendar(getCtx(),rsZoom.getInt("C_OrderShipCalendar_ID"),get_TrxName());
									oLineShip.set_CustomColumn("R_Request_ID",rLine.getR_Request_ID());
									oLineShip.setIsActive(false);								
									oLineShip.save();
									X_R_RequestLineAction rLAction = new X_R_RequestLineAction(getCtx(), 0, get_TrxName());
									rLAction.setR_Request_ID(rLine.getR_Request_ID());
									rLAction.setR_RequestLine_ID(rLine.get_ID());
									rLAction.setAD_Org_ID(rLine.getAD_Org_ID());
									rLAction.setTypeAcction(0);
									rLAction.setC_OrderShipCalendar_ID(oLineShip.get_ID());
									rLAction.save();
								}
							}catch (Exception e)
							{
								log.log(Level.SEVERE, sql, e);
							}
							//creamos las lineas nuevas de despacho
							String sqlNLine = "SELECT *,(SELECT AD_Org_ID FROM C_Order WHERE C_Order_ID = "+req.getC_Order_ID()+" )as AD_Org_ID " +
							" FROM RVOFB_OrderShipFull co WHERE C_Order_ID = "+req.getC_Order_ID()+
							" AND C_OrderLine_ID = "+rLine.get_ValueAsInt("C_OrderLine_ID")+
							" AND DateTrx >= ? AND DateTrx <= ?";
							//calculamos nuevas fechas de pauta						
							if(rLine.getDatePromised2Ref() != null)
							{
								try 
								{
									PreparedStatement pstmtNLine = DB.prepareStatement(sqlNLine,get_TrxName());
									pstmtNLine.setTimestamp(1,rLine.getDatePromised2Ref());
									pstmtNLine.setTimestamp(2,rLine.getDatePromised3Ref());
									ResultSet rsNLine = pstmtNLine.executeQuery();
									while (rsNLine.next()) 
									{
										X_C_OrderShipCalendar sCal = new X_C_OrderShipCalendar(getCtx(), 0,get_TrxName());
										sCal.setC_Order_ID(rsNLine.getInt("C_Order_ID"));
										sCal.setAD_Org_ID(rsNLine.getInt("AD_Org_ID"));
										sCal.setIsActive(true);
										sCal.setC_OrderLine_ID(rsNLine.getInt("C_OrderLine_ID"));
										sCal.setC_CalendarCOPESA_ID(rsNLine.getInt("C_CalendarCOPESA_ID"));
										sCal.setC_CalendarCOPESALine_ID(rsNLine.getInt("C_CalendarCOPESALine_ID"));
										sCal.setDateTrx(rsNLine.getTimestamp("DateTrx"));
										sCal.set_CustomColumn("M_Product_ID",rsNLine.getInt("M_Product_ID"));
										sCal.set_CustomColumn("R_Request_ID",rLine.getR_Request_ID());
										sCal.set_CustomColumn("C_BPartner_Location_ID ",rLine.getC_BPartner_LocationRef_ID());
										sCal.save();
									}						
								}
								catch (Exception e) 
								{
									log.config(e.toString());
								}
							}
						}
						if(rLine.getRequestAcction() != null && rLine.getRequestAcction().compareTo("E") == 0)
						{
							//desactivamos la linea de la orden
							DB.executeUpdate("UPDATE C_OrderLine SET IsActive = 'N' WHERE C_OrderLine_ID = "+rLine.get_ValueAsInt("C_OrderLine_ID"), get_TrxName());
							try
							{
								//borrado de cuotas de facturacion despues de la fecha
								String sqlPay = " DELETE FROM C_OrderPayCalendar WHERE C_Order_ID = " +rLine.getR_Request().getC_Order_ID()+
										" AND DateEnd >= ?";
								PreparedStatement pstmtPay = DB.prepareStatement (sqlPay, get_TrxName());
								pstmtPay.setTimestamp(1,rLine.getDatePromised2Ref());							
								pstmtPay.executeUpdate();
								//borrado de pauta despues de la fecha
								String sqlShip = " DELETE FROM C_OrderShipCalendar WHERE C_Order_ID = " +rLine.getR_Request().getC_Order_ID()+
										" AND DateTrx >= ?";
								PreparedStatement pstmtShip = DB.prepareStatement (sqlShip, get_TrxName());
								pstmtShip.setTimestamp(1,rLine.getDatePromised2Ref());							
								pstmtShip.executeUpdate();
							}
							catch (Exception e)
							{
								log.log(Level.SEVERE, sql, e);
							}
						}
						if(rLine.getRequestAcction() != null && rLine.getRequestAcction().compareTo("R") == 0)
						{
							//desactivamos las lineas suspendidas y las agregamos a detale
							String sqlZoom = " SELECT C_OrderShipCalendar_ID "+
							" FROM C_OrderShipCalendar WHERE IsActive = 'Y' " +
							" AND C_OrderLine_ID = "+rLine.get_ValueAsInt("C_OrderLine_ID")+
							" AND dateTrx = ? ";
						
							PreparedStatement pstmtZoom = DB.prepareStatement (sqlZoom, get_TrxName());		
							try
							{
								pstmtZoom.setTimestamp(1,rLine.getDatePromised2Ref());
								ResultSet rsZoom = pstmtZoom.executeQuery ();
								while (rsZoom.next())
								{
									X_C_OrderShipCalendar oLineShip = new X_C_OrderShipCalendar(getCtx(),rsZoom.getInt("C_OrderShipCalendar_ID"),get_TrxName());
									oLineShip.set_CustomColumn("R_Request_ID",rLine.getR_Request_ID());
									oLineShip.setIsActive(false);								
									oLineShip.save();
									X_R_RequestLineAction rLAction = new X_R_RequestLineAction(getCtx(), 0, get_TrxName());
									rLAction.setR_Request_ID(rLine.getR_Request_ID());
									rLAction.setR_RequestLine_ID(rLine.get_ID());
									rLAction.setAD_Org_ID(rLine.getAD_Org_ID());
									rLAction.setTypeAcction(3);
									rLAction.setC_OrderShipCalendar_ID(oLineShip.get_ID());
									rLAction.save();
								}
							}catch (Exception e)
							{
								log.log(Level.SEVERE, sql, e);
							}
							//creamos las lineas nuevas de despacho
							String sqlNLine = "SELECT *,(SELECT AD_Org_ID FROM C_Order WHERE C_Order_ID = "+req.getC_Order_ID()+" )as AD_Org_ID " +
							" FROM RVOFB_OrderShipFull co WHERE C_Order_ID = "+req.getC_Order_ID()+
							" AND C_OrderLine_ID = "+rLine.get_ValueAsInt("C_OrderLine_ID")+
							" AND DateTrx = ? ";
							//calculamos nuevas fechas de pauta						
							try 
							{
								PreparedStatement pstmtNLine = DB.prepareStatement(sqlNLine,get_TrxName());
								pstmtNLine.setTimestamp(1,rLine.getDatePromised2Ref());
								ResultSet rsNLine = pstmtNLine.executeQuery();
								while (rsNLine.next()) 
								{
									X_C_OrderShipCalendar sCal = new X_C_OrderShipCalendar(getCtx(), 0,get_TrxName());
									sCal.setC_Order_ID(rsNLine.getInt("C_Order_ID"));
									sCal.setAD_Org_ID(rsNLine.getInt("AD_Org_ID"));
									sCal.setIsActive(true);
									sCal.setC_OrderLine_ID(rsNLine.getInt("C_OrderLine_ID"));
									sCal.setC_CalendarCOPESA_ID(rsNLine.getInt("C_CalendarCOPESA_ID"));
									sCal.setC_CalendarCOPESALine_ID(rsNLine.getInt("C_CalendarCOPESALine_ID"));
									sCal.setDateTrx(rsNLine.getTimestamp("DateTrx"));
									sCal.set_CustomColumn("M_Product_ID",rsNLine.getInt("M_Product_ID"));
									sCal.set_CustomColumn("R_Request_ID",rLine.getR_Request_ID());
									sCal.save();
								}						
							}
							catch (Exception e) 
							{
								log.config(e.toString());
							}
						}
					}
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, sql, e);
				}
				req.set_CustomColumn("DocStatus", "CO");
				req.setProcessed(true);
			}
		}else if (req.get_ValueAsString("DocStatus").compareTo("ST") == 0)
		{
			//ininoles se actialozan campo de zona y sector en direccion de lineas de NV
			String sql = " SELECT R_RequestLine_ID "+
			" FROM R_RequestLine WHERE IsActive = 'Y' AND C_OrderLine_ID > 0" +
			" AND R_Request_ID = "+req.get_ID();
		
			PreparedStatement pstmt = DB.prepareStatement (sql, get_TrxName());		
			ResultSet rs = null;
			try
			{
				rs = pstmt.executeQuery ();
				while (rs.next())
				{
					X_R_RequestLine rLine = new X_R_RequestLine(getCtx(), rs.getInt("R_RequestLine_ID"), get_TrxName());					
					MOrderLine oLine = new MOrderLine(getCtx(), rLine.get_ValueAsInt("C_OrderLine_ID"), get_TrxName());
					MBPartnerLocation bparL = new MBPartnerLocation(getCtx(),oLine.getC_BPartner_Location_ID(),get_TrxName());
					bparL.set_CustomColumn("Zone",rLine.get_ValueAsString("Zone"));
					bparL.set_CustomColumn("Sector",rLine.get_ValueAsInt("Sector"));
					bparL.save();
				}
			}
			catch (Exception e)
			{
				log.log(Level.SEVERE, sql, e);
			}
			finally
			{
				pstmt.close();rs.close();
				pstmt = null; rs = null;
			}
			req.set_CustomColumn("DocStatus", "CO");
			req.setProcessed(true);
			strMsg = "Direcciones Actualizadas. Debe ir a la NV y Completarla";
		}
		req.save();
		return strMsg;
	}	//	doIt

}	//	BPGroupAcctCopy
