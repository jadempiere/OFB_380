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
import java.util.Date;

import org.compiere.model.MRequest;
import org.compiere.model.X_R_ServiceRequest;
import org.compiere.model.X_R_ServiceRequestChange;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.wf.MWFNode;

/**
 * 	COPESA
 *	
 *  @author Italo Niñoles
 */
public class ProcessServiceRequest extends SvrProcess
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
		X_R_ServiceRequest sReq = new X_R_ServiceRequest(getCtx(), ID_Request, get_TrxName());
		//antes de procesar se genera historial
		X_R_ServiceRequestChange srChange = new X_R_ServiceRequestChange(getCtx(), 0, get_TrxName());
		srChange.setR_ServiceRequest_ID(sReq.get_ID());
		srChange.setSR_AccountType(sReq.getSR_AccountType());
		srChange.setSR_Amt(sReq.getSR_Amt());
		srChange.setSR_BPartner_Loaction_ID(sReq.getSR_BPartner_Loaction_ID());
		srChange.setSR_ChargeDate(sReq.getSR_ChargeDate());
		srChange.setSR_Comments(sReq.getSR_Comments());
		srChange.setSR_CorrectValue(sReq.getSR_CorrectValue());
		srChange.setSR_CreditCardDueDate(sReq.getSR_CreditCardDueDate());
		srChange.setSR_CreditCardNo(sReq.getSR_CreditCardNo());
		srChange.setSR_Date(sReq.getSR_Date());		
		srChange.setSR_Description(sReq.getSR_Description());
		srChange.setSR_DueDate(sReq.getSR_DueDate());
		srChange.setSR_EditionNo(sReq.getSR_EditionNo());
		srChange.setSR_EMail(sReq.getSR_EMail());
		srChange.setSR_InvoiceNo(sReq.getSR_InvoiceNo());
		srChange.setSR_Last4Digits(sReq.getSR_Last4Digits());
		srChange.setSR_Name(sReq.getSR_Name());
		srChange.setSR_OperationNo(sReq.getSR_OperationNo());
		srChange.setSR_PartialOrTotal(sReq.getSR_PartialOrTotal());
		srChange.setSR_Phone(sReq.getSR_Phone());
		srChange.setSR_Reason(sReq.getSR_Reason());
		srChange.setSR_Reference(sReq.getSR_Reference());
		srChange.setSR_User_ID(sReq.getSR_User_ID());		
		srChange.setZone(sReq.getZone());
		srChange.setSector(sReq.getSector());
		//campos especiales
		srChange.setDate1(new Timestamp(new Date().getTime()));
		srChange.setHours1(new Timestamp(new Date().getTime()));
		srChange.setAD_UserRef_ID(sReq.getAD_User_ID());
		srChange.setDocStatus(sReq.getDocStatus());
		//seteo de rol
		MWFNode nodeOld = new MWFNode(getCtx(), sReq.get_ValueAsInt("AD_WF_Node_ID"), get_TrxName());
		int ID_Role = DB.getSQLValue(get_TrxName(),"SELECT MAX(AD_Role_ID) FROM AD_WF_Node_AccessCOPESA " +
				" WHERE AD_WF_Node_ID = "+nodeOld.get_ID()+" AND IsActive = 'Y'");
		if(ID_Role > 0)
			srChange.setAD_Role_ID(ID_Role);
		srChange.setAD_User_ID(Env.getAD_User_ID(getCtx()));
		srChange.setAD_WF_Node_ID(sReq.getAD_WF_NextNode_ID());
		srChange.setAD_WF_OldNode_ID(sReq.getAD_WF_Node_ID());
		Timestamp lastDate = DB.getSQLValueTS(get_TrxName(), "SELECT MAX(created) FROM R_ServiceRequestChange WHERE R_ServiceRequest_ID = ? AND IsActive = 'Y'",sReq.get_ID());
		if(lastDate != null)//calculo diferencia si existe 
		{
			long dif =  new Timestamp(new Date().getTime()).getTime()-lastDate.getTime();
			//dif = dif / (24 * 60 * 60 * 1000);
			dif = dif / (60 * 1000);
			srChange.setDifferenceStatus(new BigDecimal(dif));
		}		
		//ininoles end
		
		MWFNode node = new MWFNode(getCtx(), sReq.get_ValueAsInt("AD_WF_NextNode_ID"), get_TrxName());
		sReq.set_CustomColumn("AD_WF_Node_ID", sReq.get_ValueAsInt("AD_WF_NextNode_ID"));		
		sReq.set_CustomColumn("AD_WF_NextNode_ID", null);
		if(node.get_ValueAsString("Status") != null && node.get_ValueAsString("Status").trim().length() > 0)//nodo final
		{
			sReq.setDocStatus(node.get_ValueAsString("Status"));
			sReq.setProcessed(true);
			srChange.setCloseDate(new Timestamp(new Date().getTime()));
			if(sReq.getR_Request_ID() > 0)
			{
				MRequest req = new MRequest(getCtx(), sReq.getR_Request_ID(), get_TrxName());
				req.set_CustomColumn("DocStatus", "ST");
				req.setProcessed(false);
				/*if(sReq.getZone() != null && sReq.getZone().trim().length() > 0)
					req.set_CustomColumn("Zone", sReq.getZone());
				if(sReq.getSector() >= 0)
					req.set_CustomColumn("Sector", sReq.getSector());*/
				req.save();
			}
		}
		//seteo de usuario por defecto
		int cantUser = DB.getSQLValue(get_TrxName(),"SELECT COUNT(1) FROM AD_WF_Node_AccessCOPESA " +
				" WHERE AD_WF_Node_ID = "+node.get_ID()+" AND IsActive = 'Y'");
		if(cantUser >= 1)//seteo por defecto
		{
			int ID_User = DB.getSQLValue(get_TrxName(),"SELECT MAX(AD_User_ID) FROM AD_WF_Node_AccessCOPESA " +
					" WHERE AD_WF_Node_ID = "+node.get_ID()+" AND IsActive = 'Y'");
			sReq.setAD_User_ID(ID_User);
		}
		srChange.save();
		sReq.save();
		return "Procesado";
	}	//	doIt

}	//	BPGroupAcctCopy
