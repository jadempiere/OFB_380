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
package org.minju.process;

import java.util.Calendar;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.X_RH_MedicalLicenses;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.model.MBPartner;
import org.compiere.util.DB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.compiere.model.MClient;
import java.text.SimpleDateFormat;
import java.sql.CallableStatement;

/**
 *	
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: SeparateInvoices.java $
 */
public class ProcessMedLicenses extends SvrProcess
{
	private String			p_DocStatus = null;
	private int				p_RH_MLicenses = 0; 
	private int				role_ID = 0;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{
		 ProcessInfoParameter[] para = getParameter();
			for (int i = 0; i < para.length; i++)
			{
				String name = para[i].getParameterName();
				
				if (name.equals("DocStatus"))
					p_DocStatus = (String) para[i].getParameter();
				else
					log.log(Level.SEVERE, "Unknown Parameter: " + name);
			}
			p_RH_MLicenses=getRecord_ID();
			role_ID = Env.getAD_Role_ID(getCtx());
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		String logM = "";
		if (p_RH_MLicenses > 0)
		{
			X_RH_MedicalLicenses med = new X_RH_MedicalLicenses(getCtx(),p_RH_MLicenses, get_TrxName());
			MBPartner bp = new MBPartner(getCtx(), med.getC_BPartner_ID(), get_TrxName());
			if((med.get_ValueAsBoolean("IsCloseDocument") 
					&& med.get_ValueAsString("textdetails")!= null 
					&& med.get_ValueAsString("textdetails").trim().length() > 0) ||
					bp.get_ValueAsString("Suplencia").compareTo("04") == 0 || bp.get_ValueAsString("Suplencia").compareTo("06") == 0)
			{
				med.setDocStatus("CL");
				med.setProcessed(true);
				med.save();
				//mfrojas si la licencia es de honorarios o comisión se debe mandar correo.
				
				if(bp.get_ValueAsString("Suplencia").compareTo("04")==0 || bp.get_ValueAsString("Suplencia").compareTo("06") == 0)
				{	//mandacorreo
					CallableStatement cst = DB.prepareCall("{call pa_envia_mail_auto2(?,?,?,?)}");
					log.config("id "+med.get_ID());
					cst.setInt(1, Env.getAD_Client_ID(getCtx()));
					cst.setInt(2, 5);
					cst.setString(3, "-");
					cst.setInt(4, med.get_ID());
					cst.execute();

				}
					return "Procesado "+logM;
			}
			
			//validaciones por rol
			if(med.getDocStatus().compareTo("DR")==0 && role_ID != 2000017)
				throw new AdempiereException("Rol No Puede Procesar Documento");
			if(med.getDocStatus().compareTo("VA")==0 && role_ID != 2000018)
				throw new AdempiereException("Rol No Puede Procesar Documento");
			if(p_DocStatus.compareToIgnoreCase("EA")==0 && role_ID != 2000017)
				throw new AdempiereException("Rol No Puede Procesar Documento");
			//if(p_DocStatus.compareToIgnoreCase("CO")==0 && role_ID != 2000018)
			//	throw new AdempiereException("Rol No Puede Procesar Documento");
			if(med.getDocStatus().compareTo("RE")==0 && role_ID != 2000017)
				throw new AdempiereException("Rol No Puede Procesar Documento");			
			
			//@mfrojas se agrega envío de correo a cada IF.
			if(med.getDocStatus().compareTo("DR")==0)
			{
				//validacion licencia fuera de plazo ininoles
				int dif = calculateEndDate(med.getdatestartrequest(), med.getDate1()) ;
				if(dif > 3)
				{
					logM = " Error Licencia fuera de plazo";
					//@mfrojas se agrega escritura en un campo para validar que la licencia efectivamente está fuera de plazo
					DB.executeUpdate("UPDATE RH_MedicalLicenses SET OutOfTime = 'Y' where  rh_medicallicenses_id = "+med.get_ID(),get_TrxName());

					CallableStatement cst = DB.prepareCall("{call pa_envia_mail_auto2(?,?,?,?)}");
					log.config("id "+med.get_ID());
					cst.setInt(1, Env.getAD_Client_ID(getCtx()));
					cst.setInt(2, 4);
					cst.setString(3, "-");
					cst.setInt(4, med.get_ID());
					cst.execute();
				}
				
				//seleccionar nombre de funcionario
				//MBPartner bp = new MBPartner(med.getCtx(),med.getC_BPartner_ID(),med.get_TrxName());
				//SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
				//seleccionar correo del usuario de la licencia médica
				int bpartner = med.getC_BPartner_ID();

				String sqlmail = "Select email from ad_user where c_bpartner_id ="+bpartner;
				PreparedStatement pstmt = null;
				pstmt = DB.prepareStatement(sqlmail, get_TrxName());
				ResultSet rs = pstmt.executeQuery();
				String mail_user = "";
				while(rs.next())
					 mail_user = rs.getString("email");
				log.config("correo "+mail_user);
				rs.close();
				//seleccionar correo de supervisor de usuario
				sqlmail = "SELECT email from ad_user where ad_user_id in (select supervisor_id from ad_user where c_bpartner_id = "+bpartner+")";
				pstmt = DB.prepareStatement(sqlmail, get_TrxName());
				rs =pstmt.executeQuery();
				
				String mailsupervisor = "";
				while(rs.next())
					mailsupervisor = rs.getString("email");
				log.config(mailsupervisor);
				rs.close();
				
				//seleccionar correo de rol Jefe RRHH
				//mfrojas cambiar por campo email_to y en el while ver que el getstring no sea vacío. 
				
				//sqlmail = "select email from ad_user where ad_user_id in (select ad_user_id from ad_user_roles where isactive='Y' and ad_role_id = 2000029 and ad_user_id != 100)";
				sqlmail = "select email_to from ad_user where ad_user_id in (select ad_user_id from ad_user_roles where isactive='Y' and ad_role_id = 2000029 and ad_user_id != 100)";
				pstmt = DB.prepareStatement(sqlmail, get_TrxName());
				rs = pstmt.executeQuery();
				
				String mailjeferrhh = "";
				while(rs.next())
					if(rs.getString("email_to") != null)
						mailjeferrhh = rs.getString("email_to");
				
				log.config("mail jefe rrhh "+mailjeferrhh);
				rs.close();
				
				//seleccionar correo de jefe de presupuesto
				sqlmail = "select email from ad_user where ad_user_id in (select ad_user_id from ad_user_roles where isactive='Y' and ad_role_id = 2000030 and ad_user_id != 100)";
				pstmt = DB.prepareStatement(sqlmail, get_TrxName());
				rs = pstmt.executeQuery();
				
				String mailjefepres = "";
				while(rs.next())
					mailjefepres = rs.getString("email");
				
				log.config("mail jefe presupuesto "+mailjefepres);

				rs.close();
				
				String sqlreposo = "SELECT name from ad_Ref_list where value like '"+med.getCharacteristics()+"' and ad_reference_id = 2000025";
				pstmt = DB.prepareStatement(sqlreposo,get_TrxName());
				rs = pstmt.executeQuery();
				//String reposo = "";
				/*while (rs.next())
					reposo = rs.getString("name");*/
				rs.close();
				//@mfrojas obtener doctor
				String sqldoctor = "select name from c_bpartner where c_bpartner_id = "+med.get_ValueAsInt("C_BPartneraa_ID");
				
				pstmt = DB.prepareStatement(sqldoctor, get_TrxName());
				
				rs = pstmt.executeQuery();
				/*String doctor = "";
				while (rs.next())
					doctor = rs.getString("name");*/
				
	/*			String ln = System.getProperty("line.separator");
				StringBuilder str = new StringBuilder();
				str.append("Estimados");
				str.append(ln);
				str.append(ln);
				str.append("Se comunica que con fecha "+sd.format(med.getDate1())+" se diagnosticó al paciente "+bp.getName());
				str.append(ln);
				str.append(" por el especialista "+doctor+", indicándose reposo "+reposo+" ");
				str.append(ln);
				str.append("por el período de "+sd.format(med.getdatestartrequest())+" hasta "+sd.format(med.getDateEnd())+"");
				
	*/			
				MClient client = new MClient(med.getCtx(),med.getAD_Client_ID(),med.get_TrxName());

				String correos = "";
				//Procedimiento para enviar correos a través de BD
				
				if(mailjeferrhh != null)
				{
					correos = mailjeferrhh;
					if(mailsupervisor!=null)
						correos = correos + ", " + mailsupervisor;
					if(mail_user != null)
						correos = correos + ", "+ mail_user;
					if(mailjefepres != null)
						correos = correos + ", "+mailjefepres;
					log.config(correos);
					
					correos = "mfrojas@gmail.com";
					CallableStatement cst = DB.prepareCall("{call pa_envia_mail_auto2(?,?,?,?)}");
					
					cst.setInt(1, client.get_ID());
					cst.setInt(2, 1);
					cst.setString(3, correos);
					cst.setInt(4, med.get_ID());
					cst.execute();
				}
				
				/*if(mailjeferrhh != null)
				{

					
					//@mfrojas crear mail
					EMail mail = new EMail(client, client.getRequestEMail(), mailjeferrhh, "LICENCIA MEDICA ADEMPIERE",str.toString());
					mail.createAuthenticator(client.getRequestUser(), client.getRequestUserPW());
					mail.send();
					log.config("Correo Enviado a "+mailjeferrhh);
					log.config("Errores Correo: "+mail.getSentMsg());
					
					if(mailsupervisor != null)
					{
						mail = new EMail(client, client.getRequestEMail(), mailsupervisor, "LICENCIA MEDICA ADEMPIERE",str.toString());
						mail.createAuthenticator(client.getRequestUser(), client.getRequestUserPW());
						mail.send();
						log.config("Correo Enviado a "+mailsupervisor);
						log.config("Errores Correo: "+mail.getSentMsg());
					}
					
					if(mail_user != null)
					{
						mail = new EMail(client, client.getRequestEMail(), mail_user, "LICENCIA MEDICA ADEMPIERE",str.toString());
						mail.createAuthenticator(client.getRequestUser(), client.getRequestUserPW());
						mail.send();
						log.config("Correo Enviado a "+mail_user);
						log.config("Errores Correo: "+mail.getSentMsg());

					}
					if(mailjefepres != null)
					{
						mail = new EMail(client, client.getRequestEMail(), mailjefepres, "LICENCIA MEDICA ADEMPIERE",str.toString());
						mail.createAuthenticator(client.getRequestUser(), client.getRequestUserPW());
						mail.send();
						log.config("Correo Enviado a "+mailjefepres);
						log.config("Errores Correo: "+mail.getSentMsg());

					}
	
				}
				*/
				
				//MOrg org = new MOrg(med.getCtx(),med.getAD_Org_ID(),med.get_TrxName());
				//int ID_User = org.get_ValueAsInt("SalesRep_ID");
				
				med.setDocStatus("VA");
			}
			else if(med.getDocStatus().compareTo("VA")==0)
			{
				//seleccionar nombre de funcionario
				//MBPartner bp = new MBPartner(med.getCtx(),med.getC_BPartner_ID(),med.get_TrxName());
				//SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");

				//seleccionar correo de rol Jefe RRHH
				
				//String sqlmail = "select email from ad_user where ad_user_id in (select ad_user_id from ad_user_roles where isactive='Y' and ad_role_id = 2000029 and ad_user_id != 100)";
				String sqlmail = "select email_to from ad_user where ad_user_id in (select ad_user_id from ad_user_roles where isactive='Y' and ad_role_id = 2000029 and ad_user_id != 100)";
				PreparedStatement pstmt = DB.prepareStatement(sqlmail, get_TrxName());
				ResultSet rs = pstmt.executeQuery();
				
				String mailjeferrhh = "";
				while(rs.next())
					if(rs.getString("email_to") != "")
						mailjeferrhh = rs.getString("email");
				
				log.config("mail jefe rrhh "+mailjeferrhh);
				rs.close();
				
				//seleccionar correo de jefe de presupuesto
				sqlmail = "select email from ad_user where ad_user_id in (select ad_user_id from ad_user_roles where isactive='Y' and ad_role_id = 2000030 and ad_user_id != 100)";
				pstmt = DB.prepareStatement(sqlmail, get_TrxName());
				rs = pstmt.executeQuery();
				
				String mailjefepres = "";
				while(rs.next())
					mailjefepres = rs.getString("email");
				
				log.config("mail jefe presupuesto "+mailjefepres);

				rs.close();

				
				//envío de correo
				
	/*			String ln = System.getProperty("line.separator");
				StringBuilder str = new StringBuilder();
				str.append("Estimados");
				str.append(ln);
				str.append(ln);
				str.append("Se informa que Licencia de fecha "+sd.format(med.getDate1())+", funcionario "+bp.getName());
				str.append(ln);
				str.append(" se encuentra en período de Recuperación");
				str.append(ln);
	*/			
				MClient client = new MClient(med.getCtx(),med.getAD_Client_ID(),med.get_TrxName());

				//Procedimiento para enviar correos a través de BD
				
				if(mailjeferrhh != null)
				{
					CallableStatement cst = DB.prepareCall("{call pa_envia_mail_auto2(?,?,?,?)}");
					
					cst.setInt(1, client.get_ID());
					cst.setInt(2, 2);
					cst.setString(3, "a");
					cst.setInt(4, med.get_ID());
					cst.execute();
				}
							
				/*if(mailjeferrhh != null)
				{

					
					//@mfrojas crear mail
					EMail mail = new EMail(client, client.getRequestEMail(), mailjeferrhh, "LICENCIA MEDICA ADEMPIERE",str.toString());
					mail.createAuthenticator(client.getRequestUser(), client.getRequestUserPW());
					mail.send();
					log.config("Correo Enviado a "+mailjeferrhh);
					log.config("Errores Correo: "+mail.getSentMsg());
					if(mailjefepres != null)
					{
						mail = new EMail(client, client.getRequestEMail(), mailjefepres, "LICENCIA MEDICA ADEMPIERE",str.toString());
						mail.createAuthenticator(client.getRequestUser(), client.getRequestUserPW());
						mail.send();
						log.config("Correo Enviado a "+mailjefepres);
						log.config("Errores Correo: "+mail.getSentMsg());

					}
	
				}
						
*/
				
				med.setDocStatus("RE");
			}
			else if(med.getDocStatus().compareTo("RE")==0)
			{
				med.setDocStatus(p_DocStatus);
				if(p_DocStatus.compareToIgnoreCase("CO")==0)
				{
					//@mfrojas sql de validación saldo 0
					String sql1 = "select coalesce(sum(aa.amount2)) from rh_medicallicensesline aa " +
					"where aa.paymenttype like 'DE' and aa.rh_medicallicenses_Id ="+med.getRH_MedicalLicenses_ID()+" ";
					
					String sql2 = "select coalesce(sum(aa.amount2),0) from rh_medicallicensesline aa where aa.paymenttype like 'IN'"+
					"and aa.rh_medicallicenses_Id = "+med.getRH_MedicalLicenses_ID()+"";
					
					log.config("sql1 "+sql1);
					log.config("sql2 "+sql2);

					
					int saldo1 = DB.getSQLValue(med.get_TrxName(), sql1);
					int saldo2 = DB.getSQLValue(med.get_TrxName(), sql2);
					log.config("saldo1 "+saldo1);
					log.config("saldo2 "+saldo2);
					
					if(saldo1 - saldo2 != 0)
						throw new AdempiereException("Saldo no es cero. Licencia no puede completarse.");
					med.setProcessed(true);
					//seleccionar nombre de funcionario
					//MBPartner bp = new MBPartner(med.getCtx(),med.getC_BPartner_ID(),med.get_TrxName());
					SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");

					//seleccionar correo de rol Jefe RRHH
					
					//String sqlmail = "select email from ad_user where ad_user_id in (select ad_user_id from ad_user_roles where isactive='Y' and ad_role_id = 2000029 and ad_user_id != 100)";
					String sqlmail = "select email_to from ad_user where ad_user_id in (select ad_user_id from ad_user_roles where isactive='Y' and ad_role_id = 2000029 and ad_user_id != 100)";
					PreparedStatement pstmt = DB.prepareStatement(sqlmail, get_TrxName());
					ResultSet rs = pstmt.executeQuery();
					
					String mailjeferrhh = "";
					while(rs.next())
						if(rs.getString("email_to") != "")
							mailjeferrhh = rs.getString("email");
					
					log.config("mail jefe rrhh "+mailjeferrhh);
					rs.close();
					
					//seleccionar correo de jefe de presupuesto
					sqlmail = "select email from ad_user where ad_user_id in (select ad_user_id from ad_user_roles where isactive='Y' and ad_role_id = 2000030 and ad_user_id != 100)";
					pstmt = DB.prepareStatement(sqlmail, get_TrxName());
					rs = pstmt.executeQuery();
					
					String mailjefepres = "";
					while(rs.next())
						mailjefepres = rs.getString("email");
					
					log.config("mail jefe presupuesto "+mailjefepres);

					rs.close();

					
					//envío de correo
					
					String ln = System.getProperty("line.separator");
					StringBuilder str = new StringBuilder();
					str.append("Estimados");
					str.append(ln);
					str.append(ln);
					str.append("Se informa que Licencia de fecha "+sd.format(med.getDate1())+", funcionario "+bp.getName());
					str.append(ln);
					str.append(" se encuentra archivada y cobrada en su totalidad");
					str.append(ln);
					
					MClient client = new MClient(med.getCtx(),med.getAD_Client_ID(),med.get_TrxName());
					
					if(mailjeferrhh != null)
					{
						CallableStatement cst = DB.prepareCall("{call pa_envia_mail_auto2(?,?,?,?)}");
						
						cst.setInt(1, client.get_ID());
						cst.setInt(2, 3);
						cst.setString(3, "a");
						cst.setInt(4, med.get_ID());
						cst.execute();
					}

								
	/*				if(mailjeferrhh != null)
					{

						
						//@mfrojas crear mail
						EMail mail = new EMail(client, client.getRequestEMail(), mailjeferrhh, "LICENCIA MEDICA ADEMPIERE",str.toString());
						mail.createAuthenticator(client.getRequestUser(), client.getRequestUserPW());
						mail.send();
						log.config("Correo Enviado a "+mailjeferrhh);
						log.config("Errores Correo: "+mail.getSentMsg());
						if(mailjefepres != null)
						{
							mail = new EMail(client, client.getRequestEMail(), mailjefepres, "LICENCIA MEDICA ADEMPIERE",str.toString());
							mail.createAuthenticator(client.getRequestUser(), client.getRequestUserPW());
							mail.send();
							log.config("Correo Enviado a "+mailjefepres);
							log.config("Errores Correo: "+mail.getSentMsg());

						}
		
					}
							
*/
				}
			}
			else if(med.getDocStatus().compareTo("EA")==0)
				med.setDocStatus("RE");
			med.save();
		}
	   return "Procesado "+logM;
	   
	}	//	doIt
	public static int calculateEndDate(Timestamp startDate, Timestamp endDate)
	{		
	  Calendar startCal = Calendar.getInstance();	 
	  startCal.setTime(startDate);
	  	  
	  Calendar endCal = Calendar.getInstance();	 
	  endCal.setTime(endDate);
	  int cantDays = 0;
	  if(startCal.compareTo(endCal) >= 0)
		  return 0;
	  else
	  {		  
		  while (startCal.compareTo(endCal) <= 0)
		  {
			  startCal.add(Calendar.DAY_OF_MONTH, 1);
			  if(startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY 
					  && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
				  cantDays++;
		  }
	  }	 
	  return cantDays;
	}
}
