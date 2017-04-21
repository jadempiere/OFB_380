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
import org.compiere.model.MRequisition;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
import org.compiere.model.MBPartner;
import org.compiere.util.DB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;



/**
 *	
 *	
 *  @author mfrojas
 *  @version $Id: SeparateInvoices.java $
 */
public class ProcessPurchaseRequisition extends SvrProcess
{
	private String			p_DocStatus = null;
	private int				p_Requisition = 0; 
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
			p_Requisition=getRecord_ID();
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
		if (p_Requisition > 0)
		{
			MRequisition req = new MRequisition(getCtx(),0, get_TrxName());
			
	/*		//validaciones por rol
			if(req.getDocStatus().compareTo("DR")==0 && role_ID != 2000017)
				throw new AdempiereException("Rol No Puede Procesar Documento");
			if(req.getDocStatus().compareTo("VA")==0 && role_ID != 2000018)
				throw new AdempiereException("Rol No Puede Procesar Documento");
			if(p_DocStatus.compareToIgnoreCase("EA")==0 && role_ID != 2000017)
				throw new AdempiereException("Rol No Puede Procesar Documento");
			//if(p_DocStatus.compareToIgnoreCase("CO")==0 && role_ID != 2000018)
			//	throw new AdempiereException("Rol No Puede Procesar Documento");
			if(req.getDocStatus().compareTo("RE")==0 && role_ID != 2000017)
				throw new AdempiereException("Rol No Puede Procesar Documento");			
*/			
			//@mfrojas cambio de estado
			
			if(req.getDocStatus().compareTo("DR")==0)
			{				
				req.setDocStatus("VA");
			}
			else if(req.getDocStatus().compareTo("VA")==0)
			{
				
				req.setDocStatus("RE");
			}
			else if(req.getDocStatus().compareTo("RE")==0)
			{
				req.setDocStatus(p_DocStatus);
				if(p_DocStatus.compareToIgnoreCase("CO")==0)
				{

				}
			}
			else if(req.getDocStatus().compareTo("EA")==0)
				req.setDocStatus("RE");
			req.save();
		}
	   return "Procesado "+logM;
	   
	}	//	doIt

}
