/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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
package org.artilec.process;

import java.util.Calendar;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.compiere.model.MClient;
import org.compiere.util.Env;
import org.compiere.util.EMail;
import org.compiere.model.MUser;




/**
 *	
 *	
 *  @author mfrojas
 *  @version $Id: ProcessPurchaseOrder.java $
 *  Procesar OC ARTILEC
 */

public class ProcessPurchaseOrder extends SvrProcess
{
	//private String			p_DocStatus = null;
	private int				p_Order_ID = 0; 
	private String				p_Action = "PR";
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			
			if (name.equals("Action"))
				p_Action = para[i].getParameterAsString();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
		p_Order_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		if (p_Order_ID > 0)
		{
			MOrder ord = new MOrder(getCtx(), p_Order_ID, get_TrxName());
			//seteo de nuevo estado al procesar
			String newStatus = "DR";
			String newAction = "DR";	
			log.config("paction "+p_Action);
			
			if(ord.getDocStatus().compareTo("DR") == 0 && p_Action.compareTo("CO") == 0)
			{
				newStatus = "IP";
				newAction = "IP";
			}
			else if(ord.getDocStatus().compareTo("IP") == 0 && p_Action.compareTo("CO") == 0)
			{
				newStatus = "CO";
				newAction = "CO";
			}
			else if(ord.getDocStatus().compareTo("IN") == 0 && p_Action.compareTo("CO") == 0)
			{
				newStatus = "IP";
				newAction = "IP";
			}
			//seteo de nuevo estado al rechazar
			else if(ord.getDocStatus().compareTo("DR") == 0 && p_Action.compareTo("VO") == 0)
			{
				newStatus = "DR";
				newAction = "DR";
			}
			else if(ord.getDocStatus().compareTo("IP") == 0 && p_Action.compareTo("VO") == 0)
			{
				newStatus = "DR";
				newAction = "DR";
			}
		
			//validacion de rol
			int cant = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM AD_Document_Action_Access daa" +
					" INNER JOIN AD_Ref_List rl ON (daa.AD_Ref_List_ID = rl.AD_Ref_List_ID) " +
					" WHERE value = '"+newAction+"' AND AD_Role_ID = "+Env.getAD_Role_ID(getCtx())+
					" AND C_DocType_ID = "+ord.getC_DocTypeTarget_ID());
			//procesar
			log.config("cantidad"+cant);
			if(cant > 0)
			{
				if(newAction.compareTo("CO") != 0)
				{
					ord.setDocStatus(newStatus);
					ord.save();
					String ln = System.getProperty("line.separator");
					StringBuilder str = new StringBuilder();
					str.append("Estimados");
					str.append(ln);
					str.append(ln);
					str.append("Se comunica que la orden "+ord.getDocumentNo());
					str.append(ln);
					str.append(" se encuentra en estado 'En Proceso' ");
					str.append(ln);
					
					MClient client = new MClient(ord.getCtx(),ord.getAD_Client_ID(),ord.get_TrxName());
					String direccionenvio = "bcalderon@artilec.net";
					EMail mail = new EMail(client, client.getRequestEMail(), direccionenvio, "Orden de Compra",str.toString());
					mail.createAuthenticator(client.getRequestUser(), client.getRequestUserPW());
					mail.send();
					log.config("Correo Enviado a "+direccionenvio);
					log.config("Errores Correo: "+mail.getSentMsg());
				}
				else if(newAction.compareTo("CO") == 0)
				{
					
					if(ord.getC_DocTypeTarget_ID()==2000138)
					{

						//validar adjuntos
						String sql = "select ad_attachment_id from ad_attachment where ad_table_id = 259 and record_id = "+ord.get_ID();
						PreparedStatement pstmt = DB.prepareStatement(sql, get_TrxName());
						ResultSet rs = pstmt.executeQuery();
						
						int att = 0;
					
						while (rs.next())
							att++;

						if(att <= 0)
						{	
							throw new AdempiereException ("No se ha encontrado adjuntos");
							//return "No se ha encontrado adjuntos";
						}
					}
					
					//validar que el usuario que está completando, tenga suficiente monto de aprobación.
					
					//int user = Env.getAD_User_ID(getCtx());
					
					MUser usuario = new MUser(getCtx(),Env.getAD_User_ID(getCtx()),get_TrxName());
					
					log.config("usuario "+usuario.getName());
					int montoapr = usuario.get_ValueAsInt("ApprovalAmt");
					
					String sqlmonto = "SELECT ApprovalAmt from ad_user where ad_user_id = "+Env.getAD_User_ID(getCtx());
					PreparedStatement pstmt2 = DB.prepareStatement(sqlmonto, get_TrxName());
					ResultSet rs2 = pstmt2.executeQuery();

					BigDecimal montoaprobacion = Env.ZERO;
					if(rs2.next())
					{
						montoaprobacion = rs2.getBigDecimal("ApprovalAmt");
					}

							
 					log.config("monto aprob "+montoaprobacion);
					//@mfrojas primero se revisa en caso de que la OC tenga la moneda CLP. 
					if(ord.getCurrencyISO().compareToIgnoreCase("CLP")==0)
					{
						if(montoaprobacion.compareTo(ord.getGrandTotal()) < 0)
						{
							throw new AdempiereException ("Monto de aprobación insuficiente");

							//return "Monto de aprobación insuficiente";
						}
					}
					//@mfrojas se revisa en caso de que la OC sea de moneda distinta a CLP
					else if (ord.getCurrencyISO().compareToIgnoreCase("CLP")!=0)
					{
						String sqlmoneda = "SELECT coalesce(DivideRate,0) from C_Conversion_Rate where isactive='Y' AND ValidFrom <= '"+ord.getDateOrdered()+"' AND ValidTo >= '"+ord.getDateOrdered()+"' AND C_Currency_ID = 228 AND C_Currency_ID_To =  "+ord.getC_Currency_ID();
						log.config("sql moneda "+sqlmoneda);
						int dividerate = DB.getSQLValue(get_TrxName(), sqlmoneda);
						log.config("dividerate es "+dividerate);
						if(dividerate <= 0)
						{
							throw new AdempiereException ("No hay tasa de cambio");

							//return "No hay tasa de cambio";
						}
						
						BigDecimal multiplicando = BigDecimal.valueOf(dividerate).multiply(ord.getGrandTotal());
						log.config("monto oc "+multiplicando);
						log.config("total oc  "+ord.getGrandTotal());
						if(montoaprobacion.compareTo(multiplicando) < 0)
						{	//return "Monto de aprobación insuficiente";
							throw new AdempiereException ("Monto de aprobación insuficiente");

						}
					}
							
	
					ord.setDocStatus("IP");
						ord.processIt("CO");
						ord.save();
					
					
						
				}
				
				//Se agrega envío de correos. 
				


			


			}
			else
				throw new AdempiereException("Error: Permisos de rol insuficientes");
		}
	   return "Procesado";
	}	//	doIt
	

}
