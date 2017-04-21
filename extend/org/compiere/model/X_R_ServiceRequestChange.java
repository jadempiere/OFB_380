/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for R_ServiceRequestChange
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0 - $Id$ */
public class X_R_ServiceRequestChange extends PO implements I_R_ServiceRequestChange, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170130L;

    /** Standard Constructor */
    public X_R_ServiceRequestChange (Properties ctx, int R_ServiceRequestChange_ID, String trxName)
    {
      super (ctx, R_ServiceRequestChange_ID, trxName);
      /** if (R_ServiceRequestChange_ID == 0)
        {
			setR_ServiceRequestChange_ID (0);
        } */
    }

    /** Load Constructor */
    public X_R_ServiceRequestChange (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_R_ServiceRequestChange[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.compiere.model.I_AD_Role getAD_Role() throws RuntimeException
    {
		return (org.compiere.model.I_AD_Role)MTable.get(getCtx(), org.compiere.model.I_AD_Role.Table_Name)
			.getPO(getAD_Role_ID(), get_TrxName());	}

	/** Set Role.
		@param AD_Role_ID 
		Responsibility Role
	  */
	public void setAD_Role_ID (int AD_Role_ID)
	{
		if (AD_Role_ID < 0) 
			set_Value (COLUMNNAME_AD_Role_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Role_ID, Integer.valueOf(AD_Role_ID));
	}

	/** Get Role.
		@return Responsibility Role
	  */
	public int getAD_Role_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Role_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_AD_User getAD_User() throws RuntimeException
    {
		return (org.compiere.model.I_AD_User)MTable.get(getCtx(), org.compiere.model.I_AD_User.Table_Name)
			.getPO(getAD_User_ID(), get_TrxName());	}

	/** Set User/Contact.
		@param AD_User_ID 
		User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID)
	{
		if (AD_User_ID < 1) 
			set_Value (COLUMNNAME_AD_User_ID, null);
		else 
			set_Value (COLUMNNAME_AD_User_ID, Integer.valueOf(AD_User_ID));
	}

	/** Get User/Contact.
		@return User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_User_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_AD_User getAD_UserRef() throws RuntimeException
    {
		return (org.compiere.model.I_AD_User)MTable.get(getCtx(), org.compiere.model.I_AD_User.Table_Name)
			.getPO(getAD_UserRef_ID(), get_TrxName());	}

	/** Set AD_UserRef_ID.
		@param AD_UserRef_ID AD_UserRef_ID	  */
	public void setAD_UserRef_ID (int AD_UserRef_ID)
	{
		if (AD_UserRef_ID < 1) 
			set_Value (COLUMNNAME_AD_UserRef_ID, null);
		else 
			set_Value (COLUMNNAME_AD_UserRef_ID, Integer.valueOf(AD_UserRef_ID));
	}

	/** Get AD_UserRef_ID.
		@return AD_UserRef_ID	  */
	public int getAD_UserRef_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_UserRef_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_AD_WF_Node getAD_WF_Node() throws RuntimeException
    {
		return (org.compiere.model.I_AD_WF_Node)MTable.get(getCtx(), org.compiere.model.I_AD_WF_Node.Table_Name)
			.getPO(getAD_WF_Node_ID(), get_TrxName());	}

	/** Set Node.
		@param AD_WF_Node_ID 
		Workflow Node (activity), step or process
	  */
	public void setAD_WF_Node_ID (int AD_WF_Node_ID)
	{
		if (AD_WF_Node_ID < 1) 
			set_Value (COLUMNNAME_AD_WF_Node_ID, null);
		else 
			set_Value (COLUMNNAME_AD_WF_Node_ID, Integer.valueOf(AD_WF_Node_ID));
	}

	/** Get Node.
		@return Workflow Node (activity), step or process
	  */
	public int getAD_WF_Node_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_WF_Node_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_AD_WF_Node getAD_WF_OldNode() throws RuntimeException
    {
		return (org.compiere.model.I_AD_WF_Node)MTable.get(getCtx(), org.compiere.model.I_AD_WF_Node.Table_Name)
			.getPO(getAD_WF_OldNode_ID(), get_TrxName());	}

	/** Set AD_WF_OldNode_ID.
		@param AD_WF_OldNode_ID AD_WF_OldNode_ID	  */
	public void setAD_WF_OldNode_ID (int AD_WF_OldNode_ID)
	{
		if (AD_WF_OldNode_ID < 1) 
			set_Value (COLUMNNAME_AD_WF_OldNode_ID, null);
		else 
			set_Value (COLUMNNAME_AD_WF_OldNode_ID, Integer.valueOf(AD_WF_OldNode_ID));
	}

	/** Get AD_WF_OldNode_ID.
		@return AD_WF_OldNode_ID	  */
	public int getAD_WF_OldNode_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_WF_OldNode_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_AD_Workflow getAD_Workflow() throws RuntimeException
    {
		return (org.compiere.model.I_AD_Workflow)MTable.get(getCtx(), org.compiere.model.I_AD_Workflow.Table_Name)
			.getPO(getAD_Workflow_ID(), get_TrxName());	}

	/** Set Workflow.
		@param AD_Workflow_ID 
		Workflow or combination of tasks
	  */
	public void setAD_Workflow_ID (int AD_Workflow_ID)
	{
		if (AD_Workflow_ID < 1) 
			set_Value (COLUMNNAME_AD_Workflow_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Workflow_ID, Integer.valueOf(AD_Workflow_ID));
	}

	/** Get Workflow.
		@return Workflow or combination of tasks
	  */
	public int getAD_Workflow_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Workflow_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Close Date.
		@param CloseDate 
		Close Date
	  */
	public void setCloseDate (Timestamp CloseDate)
	{
		set_Value (COLUMNNAME_CloseDate, CloseDate);
	}

	/** Get Close Date.
		@return Close Date
	  */
	public Timestamp getCloseDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_CloseDate);
	}

	/** Set Date.
		@param Date1 
		Date when business is not conducted
	  */
	public void setDate1 (Timestamp Date1)
	{
		set_Value (COLUMNNAME_Date1, Date1);
	}

	/** Get Date.
		@return Date when business is not conducted
	  */
	public Timestamp getDate1 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Date1);
	}

	/** Set DifferenceStatus.
		@param DifferenceStatus 
		DifferenceStatus
	  */
	public void setDifferenceStatus (BigDecimal DifferenceStatus)
	{
		set_Value (COLUMNNAME_DifferenceStatus, DifferenceStatus);
	}

	/** Get DifferenceStatus.
		@return DifferenceStatus
	  */
	public BigDecimal getDifferenceStatus () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DifferenceStatus);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** DocStatus AD_Reference_ID=131 */
	public static final int DOCSTATUS_AD_Reference_ID=131;
	/** Drafted = DR */
	public static final String DOCSTATUS_Drafted = "DR";
	/** Completed = CO */
	public static final String DOCSTATUS_Completed = "CO";
	/** Approved = AP */
	public static final String DOCSTATUS_Approved = "AP";
	/** Not Approved = NA */
	public static final String DOCSTATUS_NotApproved = "NA";
	/** Voided = VO */
	public static final String DOCSTATUS_Voided = "VO";
	/** Invalid = IN */
	public static final String DOCSTATUS_Invalid = "IN";
	/** Reversed = RE */
	public static final String DOCSTATUS_Reversed = "RE";
	/** Closed = CL */
	public static final String DOCSTATUS_Closed = "CL";
	/** Unknown = ?? */
	public static final String DOCSTATUS_Unknown = "??";
	/** In Progress = IP */
	public static final String DOCSTATUS_InProgress = "IP";
	/** Waiting Payment = WP */
	public static final String DOCSTATUS_WaitingPayment = "WP";
	/** Waiting Confirmation = WC */
	public static final String DOCSTATUS_WaitingConfirmation = "WC";
	/** Set Document Status.
		@param DocStatus 
		The current status of the document
	  */
	public void setDocStatus (String DocStatus)
	{

		set_Value (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	public String getDocStatus () 
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
	}

	/** Set Hours1.
		@param Hours1 Hours1	  */
	public void setHours1 (Timestamp Hours1)
	{
		set_Value (COLUMNNAME_Hours1, Hours1);
	}

	/** Get Hours1.
		@return Hours1	  */
	public Timestamp getHours1 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_Hours1);
	}

	public I_R_ServiceRequest getR_ServiceRequest() throws RuntimeException
    {
		return (I_R_ServiceRequest)MTable.get(getCtx(), I_R_ServiceRequest.Table_Name)
			.getPO(getR_ServiceRequest_ID(), get_TrxName());	}

	/** Set ServiceRequest ID.
		@param R_ServiceRequest_ID ServiceRequest ID	  */
	public void setR_ServiceRequest_ID (int R_ServiceRequest_ID)
	{
		if (R_ServiceRequest_ID < 1) 
			set_Value (COLUMNNAME_R_ServiceRequest_ID, null);
		else 
			set_Value (COLUMNNAME_R_ServiceRequest_ID, Integer.valueOf(R_ServiceRequest_ID));
	}

	/** Get ServiceRequest ID.
		@return ServiceRequest ID	  */
	public int getR_ServiceRequest_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_R_ServiceRequest_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set R_ServiceRequestChange ID.
		@param R_ServiceRequestChange_ID R_ServiceRequestChange ID	  */
	public void setR_ServiceRequestChange_ID (int R_ServiceRequestChange_ID)
	{
		if (R_ServiceRequestChange_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_R_ServiceRequestChange_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_R_ServiceRequestChange_ID, Integer.valueOf(R_ServiceRequestChange_ID));
	}

	/** Get R_ServiceRequestChange ID.
		@return R_ServiceRequestChange ID	  */
	public int getR_ServiceRequestChange_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_R_ServiceRequestChange_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_AD_User getSalesRep() throws RuntimeException
    {
		return (org.compiere.model.I_AD_User)MTable.get(getCtx(), org.compiere.model.I_AD_User.Table_Name)
			.getPO(getSalesRep_ID(), get_TrxName());	}

	/** Set Sales Representative.
		@param SalesRep_ID 
		Sales Representative or Company Agent
	  */
	public void setSalesRep_ID (int SalesRep_ID)
	{
		if (SalesRep_ID < 1) 
			set_Value (COLUMNNAME_SalesRep_ID, null);
		else 
			set_Value (COLUMNNAME_SalesRep_ID, Integer.valueOf(SalesRep_ID));
	}

	/** Get Sales Representative.
		@return Sales Representative or Company Agent
	  */
	public int getSalesRep_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SalesRep_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Sector.
		@param Sector Sector	  */
	public void setSector (int Sector)
	{
		set_Value (COLUMNNAME_Sector, Integer.valueOf(Sector));
	}

	/** Get Sector.
		@return Sector	  */
	public int getSector () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Sector);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SR_AccountType.
		@param SR_AccountType SR_AccountType	  */
	public void setSR_AccountType (String SR_AccountType)
	{
		set_Value (COLUMNNAME_SR_AccountType, SR_AccountType);
	}

	/** Get SR_AccountType.
		@return SR_AccountType	  */
	public String getSR_AccountType () 
	{
		return (String)get_Value(COLUMNNAME_SR_AccountType);
	}

	/** Set SR_Amt.
		@param SR_Amt SR_Amt	  */
	public void setSR_Amt (BigDecimal SR_Amt)
	{
		set_Value (COLUMNNAME_SR_Amt, SR_Amt);
	}

	/** Get SR_Amt.
		@return SR_Amt	  */
	public BigDecimal getSR_Amt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SR_Amt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_C_BPartner_Location getSR_BPartner_Loaction() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner_Location)MTable.get(getCtx(), org.compiere.model.I_C_BPartner_Location.Table_Name)
			.getPO(getSR_BPartner_Loaction_ID(), get_TrxName());	}

	/** Set SR_BPartner_Loaction_ID.
		@param SR_BPartner_Loaction_ID SR_BPartner_Loaction_ID	  */
	public void setSR_BPartner_Loaction_ID (int SR_BPartner_Loaction_ID)
	{
		if (SR_BPartner_Loaction_ID < 1) 
			set_Value (COLUMNNAME_SR_BPartner_Loaction_ID, null);
		else 
			set_Value (COLUMNNAME_SR_BPartner_Loaction_ID, Integer.valueOf(SR_BPartner_Loaction_ID));
	}

	/** Get SR_BPartner_Loaction_ID.
		@return SR_BPartner_Loaction_ID	  */
	public int getSR_BPartner_Loaction_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SR_BPartner_Loaction_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set SR_ChargeDate.
		@param SR_ChargeDate SR_ChargeDate	  */
	public void setSR_ChargeDate (Timestamp SR_ChargeDate)
	{
		set_Value (COLUMNNAME_SR_ChargeDate, SR_ChargeDate);
	}

	/** Get SR_ChargeDate.
		@return SR_ChargeDate	  */
	public Timestamp getSR_ChargeDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_SR_ChargeDate);
	}

	/** Set SR_Comments.
		@param SR_Comments SR_Comments	  */
	public void setSR_Comments (String SR_Comments)
	{
		set_Value (COLUMNNAME_SR_Comments, SR_Comments);
	}

	/** Get SR_Comments.
		@return SR_Comments	  */
	public String getSR_Comments () 
	{
		return (String)get_Value(COLUMNNAME_SR_Comments);
	}

	/** Set SR_CorrectValue.
		@param SR_CorrectValue SR_CorrectValue	  */
	public void setSR_CorrectValue (String SR_CorrectValue)
	{
		set_Value (COLUMNNAME_SR_CorrectValue, SR_CorrectValue);
	}

	/** Get SR_CorrectValue.
		@return SR_CorrectValue	  */
	public String getSR_CorrectValue () 
	{
		return (String)get_Value(COLUMNNAME_SR_CorrectValue);
	}

	/** Set SR_CreditCardDueDate.
		@param SR_CreditCardDueDate SR_CreditCardDueDate	  */
	public void setSR_CreditCardDueDate (String SR_CreditCardDueDate)
	{
		set_Value (COLUMNNAME_SR_CreditCardDueDate, SR_CreditCardDueDate);
	}

	/** Get SR_CreditCardDueDate.
		@return SR_CreditCardDueDate	  */
	public String getSR_CreditCardDueDate () 
	{
		return (String)get_Value(COLUMNNAME_SR_CreditCardDueDate);
	}

	/** Set SR_CreditCardNo.
		@param SR_CreditCardNo SR_CreditCardNo	  */
	public void setSR_CreditCardNo (String SR_CreditCardNo)
	{
		set_Value (COLUMNNAME_SR_CreditCardNo, SR_CreditCardNo);
	}

	/** Get SR_CreditCardNo.
		@return SR_CreditCardNo	  */
	public String getSR_CreditCardNo () 
	{
		return (String)get_Value(COLUMNNAME_SR_CreditCardNo);
	}

	/** Set SR_Date.
		@param SR_Date SR_Date	  */
	public void setSR_Date (Timestamp SR_Date)
	{
		set_Value (COLUMNNAME_SR_Date, SR_Date);
	}

	/** Get SR_Date.
		@return SR_Date	  */
	public Timestamp getSR_Date () 
	{
		return (Timestamp)get_Value(COLUMNNAME_SR_Date);
	}

	/** Set SR_Description.
		@param SR_Description SR_Description	  */
	public void setSR_Description (String SR_Description)
	{
		set_Value (COLUMNNAME_SR_Description, SR_Description);
	}

	/** Get SR_Description.
		@return SR_Description	  */
	public String getSR_Description () 
	{
		return (String)get_Value(COLUMNNAME_SR_Description);
	}

	/** Set SR_DueDate.
		@param SR_DueDate SR_DueDate	  */
	public void setSR_DueDate (Timestamp SR_DueDate)
	{
		set_Value (COLUMNNAME_SR_DueDate, SR_DueDate);
	}

	/** Get SR_DueDate.
		@return SR_DueDate	  */
	public Timestamp getSR_DueDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_SR_DueDate);
	}

	/** Set SR_EditionNo.
		@param SR_EditionNo SR_EditionNo	  */
	public void setSR_EditionNo (String SR_EditionNo)
	{
		set_Value (COLUMNNAME_SR_EditionNo, SR_EditionNo);
	}

	/** Get SR_EditionNo.
		@return SR_EditionNo	  */
	public String getSR_EditionNo () 
	{
		return (String)get_Value(COLUMNNAME_SR_EditionNo);
	}

	/** Set SR_EMail.
		@param SR_EMail SR_EMail	  */
	public void setSR_EMail (String SR_EMail)
	{
		set_Value (COLUMNNAME_SR_EMail, SR_EMail);
	}

	/** Get SR_EMail.
		@return SR_EMail	  */
	public String getSR_EMail () 
	{
		return (String)get_Value(COLUMNNAME_SR_EMail);
	}

	/** Set SR_InvoiceNo.
		@param SR_InvoiceNo SR_InvoiceNo	  */
	public void setSR_InvoiceNo (String SR_InvoiceNo)
	{
		set_Value (COLUMNNAME_SR_InvoiceNo, SR_InvoiceNo);
	}

	/** Get SR_InvoiceNo.
		@return SR_InvoiceNo	  */
	public String getSR_InvoiceNo () 
	{
		return (String)get_Value(COLUMNNAME_SR_InvoiceNo);
	}

	/** Set SR_Last4Digits.
		@param SR_Last4Digits SR_Last4Digits	  */
	public void setSR_Last4Digits (String SR_Last4Digits)
	{
		set_Value (COLUMNNAME_SR_Last4Digits, SR_Last4Digits);
	}

	/** Get SR_Last4Digits.
		@return SR_Last4Digits	  */
	public String getSR_Last4Digits () 
	{
		return (String)get_Value(COLUMNNAME_SR_Last4Digits);
	}

	/** Set SR_Name.
		@param SR_Name SR_Name	  */
	public void setSR_Name (String SR_Name)
	{
		set_Value (COLUMNNAME_SR_Name, SR_Name);
	}

	/** Get SR_Name.
		@return SR_Name	  */
	public String getSR_Name () 
	{
		return (String)get_Value(COLUMNNAME_SR_Name);
	}

	/** Set SR_OperationNo.
		@param SR_OperationNo SR_OperationNo	  */
	public void setSR_OperationNo (String SR_OperationNo)
	{
		set_Value (COLUMNNAME_SR_OperationNo, SR_OperationNo);
	}

	/** Get SR_OperationNo.
		@return SR_OperationNo	  */
	public String getSR_OperationNo () 
	{
		return (String)get_Value(COLUMNNAME_SR_OperationNo);
	}

	/** SR_PartialOrTotal AD_Reference_ID=2000054 */
	public static final int SR_PARTIALORTOTAL_AD_Reference_ID=2000054;
	/** Parcial = P */
	public static final String SR_PARTIALORTOTAL_Parcial = "P";
	/** Total = T */
	public static final String SR_PARTIALORTOTAL_Total = "T";
	/** Set SR_PartialOrTotal.
		@param SR_PartialOrTotal SR_PartialOrTotal	  */
	public void setSR_PartialOrTotal (String SR_PartialOrTotal)
	{

		set_Value (COLUMNNAME_SR_PartialOrTotal, SR_PartialOrTotal);
	}

	/** Get SR_PartialOrTotal.
		@return SR_PartialOrTotal	  */
	public String getSR_PartialOrTotal () 
	{
		return (String)get_Value(COLUMNNAME_SR_PartialOrTotal);
	}

	/** Set SR_Phone.
		@param SR_Phone SR_Phone	  */
	public void setSR_Phone (String SR_Phone)
	{
		set_Value (COLUMNNAME_SR_Phone, SR_Phone);
	}

	/** Get SR_Phone.
		@return SR_Phone	  */
	public String getSR_Phone () 
	{
		return (String)get_Value(COLUMNNAME_SR_Phone);
	}

	/** SR_Reason AD_Reference_ID=2000053 */
	public static final int SR_REASON_AD_Reference_ID=2000053;
	/** M1 = 01 */
	public static final String SR_REASON_M1 = "01";
	/** M2 = 02 */
	public static final String SR_REASON_M2 = "02";
	/** Set SR_Reason.
		@param SR_Reason SR_Reason	  */
	public void setSR_Reason (String SR_Reason)
	{

		set_Value (COLUMNNAME_SR_Reason, SR_Reason);
	}

	/** Get SR_Reason.
		@return SR_Reason	  */
	public String getSR_Reason () 
	{
		return (String)get_Value(COLUMNNAME_SR_Reason);
	}

	/** Set SR_Reference.
		@param SR_Reference SR_Reference	  */
	public void setSR_Reference (String SR_Reference)
	{
		set_Value (COLUMNNAME_SR_Reference, SR_Reference);
	}

	/** Get SR_Reference.
		@return SR_Reference	  */
	public String getSR_Reference () 
	{
		return (String)get_Value(COLUMNNAME_SR_Reference);
	}

	public org.compiere.model.I_AD_User getSR_User() throws RuntimeException
    {
		return (org.compiere.model.I_AD_User)MTable.get(getCtx(), org.compiere.model.I_AD_User.Table_Name)
			.getPO(getSR_User_ID(), get_TrxName());	}

	/** Set SR_User_ID.
		@param SR_User_ID SR_User_ID	  */
	public void setSR_User_ID (int SR_User_ID)
	{
		if (SR_User_ID < 1) 
			set_Value (COLUMNNAME_SR_User_ID, null);
		else 
			set_Value (COLUMNNAME_SR_User_ID, Integer.valueOf(SR_User_ID));
	}

	/** Get SR_User_ID.
		@return SR_User_ID	  */
	public int getSR_User_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SR_User_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Zone.
		@param Zone Zone	  */
	public void setZone (String Zone)
	{
		set_Value (COLUMNNAME_Zone, Zone);
	}

	/** Get Zone.
		@return Zone	  */
	public String getZone () 
	{
		return (String)get_Value(COLUMNNAME_Zone);
	}
}