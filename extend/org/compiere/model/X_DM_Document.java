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
import org.compiere.util.KeyNamePair;

/** Generated Model for DM_Document
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0 - $Id$ */
public class X_DM_Document extends PO implements I_DM_Document, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20160616L;

    /** Standard Constructor */
    public X_DM_Document (Properties ctx, int DM_Document_ID, String trxName)
    {
      super (ctx, DM_Document_ID, trxName);
      /** if (DM_Document_ID == 0)
        {
			setAmt (Env.ZERO);
			setDM_Document_ID (0);
			setDocStatus (null);
// DR
			setDocumentNo (null);
			setProcessed (false);
// N
        } */
    }

    /** Load Constructor */
    public X_DM_Document (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_DM_Document[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set acumanticipo.
		@param acumanticipo acumanticipo	  */
	public void setacumanticipo (BigDecimal acumanticipo)
	{
		set_Value (COLUMNNAME_acumanticipo, acumanticipo);
	}

	/** Get acumanticipo.
		@return acumanticipo	  */
	public BigDecimal getacumanticipo () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_acumanticipo);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set acumdeva.
		@param acumdeva acumdeva	  */
	public void setacumdeva (BigDecimal acumdeva)
	{
		set_Value (COLUMNNAME_acumdeva, acumdeva);
	}

	/** Get acumdeva.
		@return acumdeva	  */
	public BigDecimal getacumdeva () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_acumdeva);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set acumdevotrr.
		@param acumdevotrr acumdevotrr	  */
	public void setacumdevotrr (BigDecimal acumdevotrr)
	{
		set_Value (COLUMNNAME_acumdevotrr, acumdevotrr);
	}

	/** Get acumdevotrr.
		@return acumdevotrr	  */
	public BigDecimal getacumdevotrr () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_acumdevotrr);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set acumdevr.
		@param acumdevr acumdevr	  */
	public void setacumdevr (BigDecimal acumdevr)
	{
		set_Value (COLUMNNAME_acumdevr, acumdevr);
	}

	/** Get acumdevr.
		@return acumdevr	  */
	public BigDecimal getacumdevr () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_acumdevr);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set acummultas.
		@param acummultas acummultas	  */
	public void setacummultas (BigDecimal acummultas)
	{
		set_Value (COLUMNNAME_acummultas, acummultas);
	}

	/** Get acummultas.
		@return acummultas	  */
	public BigDecimal getacummultas () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_acummultas);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set acumotrasr.
		@param acumotrasr acumotrasr	  */
	public void setacumotrasr (BigDecimal acumotrasr)
	{
		set_Value (COLUMNNAME_acumotrasr, acumotrasr);
	}

	/** Get acumotrasr.
		@return acumotrasr	  */
	public BigDecimal getacumotrasr () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_acumotrasr);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set acumreajustea.
		@param acumreajustea acumreajustea	  */
	public void setacumreajustea (BigDecimal acumreajustea)
	{
		set_Value (COLUMNNAME_acumreajustea, acumreajustea);
	}

	/** Get acumreajustea.
		@return acumreajustea	  */
	public BigDecimal getacumreajustea () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_acumreajustea);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set acumreajusteo.
		@param acumreajusteo acumreajusteo	  */
	public void setacumreajusteo (BigDecimal acumreajusteo)
	{
		set_Value (COLUMNNAME_acumreajusteo, acumreajusteo);
	}

	/** Get acumreajusteo.
		@return acumreajusteo	  */
	public BigDecimal getacumreajusteo () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_acumreajusteo);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set acumretencion.
		@param acumretencion acumretencion	  */
	public void setacumretencion (BigDecimal acumretencion)
	{
		set_Value (COLUMNNAME_acumretencion, acumretencion);
	}

	/** Get acumretencion.
		@return acumretencion	  */
	public BigDecimal getacumretencion () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_acumretencion);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set Amount.
		@param Amt 
		Amount
	  */
	public void setAmt (BigDecimal Amt)
	{
		set_Value (COLUMNNAME_Amt, Amt);
	}

	/** Get Amount.
		@return Amount
	  */
	public BigDecimal getAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Amt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set amtdate.
		@param amtdate amtdate	  */
	public void setamtdate (BigDecimal amtdate)
	{
		set_Value (COLUMNNAME_amtdate, amtdate);
	}

	/** Get amtdate.
		@return amtdate	  */
	public BigDecimal getamtdate () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_amtdate);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set banco.
		@param banco banco	  */
	public void setbanco (String banco)
	{
		set_Value (COLUMNNAME_banco, banco);
	}

	/** Get banco.
		@return banco	  */
	public String getbanco () 
	{
		return (String)get_Value(COLUMNNAME_banco);
	}

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Business Partner .
		@param C_BPartner_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_BPartner getc_bpartnerp() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_Name)
			.getPO(getc_bpartnerp_ID(), get_TrxName());	}

	/** Set c_bpartnerp_ID.
		@param c_bpartnerp_ID c_bpartnerp_ID	  */
	public void setc_bpartnerp_ID (int c_bpartnerp_ID)
	{
		if (c_bpartnerp_ID < 1) 
			set_Value (COLUMNNAME_c_bpartnerp_ID, null);
		else 
			set_Value (COLUMNNAME_c_bpartnerp_ID, Integer.valueOf(c_bpartnerp_ID));
	}

	/** Get c_bpartnerp_ID.
		@return c_bpartnerp_ID	  */
	public int getc_bpartnerp_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_c_bpartnerp_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Charge getC_Charge() throws RuntimeException
    {
		return (org.compiere.model.I_C_Charge)MTable.get(getCtx(), org.compiere.model.I_C_Charge.Table_Name)
			.getPO(getC_Charge_ID(), get_TrxName());	}

	/** Set Charge.
		@param C_Charge_ID 
		Additional document charges
	  */
	public void setC_Charge_ID (int C_Charge_ID)
	{
		if (C_Charge_ID < 1) 
			set_Value (COLUMNNAME_C_Charge_ID, null);
		else 
			set_Value (COLUMNNAME_C_Charge_ID, Integer.valueOf(C_Charge_ID));
	}

	/** Get Charge.
		@return Additional document charges
	  */
	public int getC_Charge_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Charge_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Currency getC_Currency() throws RuntimeException
    {
		return (org.compiere.model.I_C_Currency)MTable.get(getCtx(), org.compiere.model.I_C_Currency.Table_Name)
			.getPO(getC_Currency_ID(), get_TrxName());	}

	/** Set Currency.
		@param C_Currency_ID 
		The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID)
	{
		if (C_Currency_ID < 1) 
			set_Value (COLUMNNAME_C_Currency_ID, null);
		else 
			set_Value (COLUMNNAME_C_Currency_ID, Integer.valueOf(C_Currency_ID));
	}

	/** Get Currency.
		@return The Currency for this record
	  */
	public int getC_Currency_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Currency_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException
    {
		return (org.compiere.model.I_C_DocType)MTable.get(getCtx(), org.compiere.model.I_C_DocType.Table_Name)
			.getPO(getC_DocType_ID(), get_TrxName());	}

	/** Set Document Type.
		@param C_DocType_ID 
		Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0) 
			set_Value (COLUMNNAME_C_DocType_ID, null);
		else 
			set_Value (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set codigodeltrabajo.
		@param codigodeltrabajo codigodeltrabajo	  */
	public void setcodigodeltrabajo (String codigodeltrabajo)
	{
		set_Value (COLUMNNAME_codigodeltrabajo, codigodeltrabajo);
	}

	/** Get codigodeltrabajo.
		@return codigodeltrabajo	  */
	public String getcodigodeltrabajo () 
	{
		return (String)get_Value(COLUMNNAME_codigodeltrabajo);
	}

	/** Set Comments.
		@param Comments 
		Comments or additional information
	  */
	public void setComments (String Comments)
	{
		set_Value (COLUMNNAME_Comments, Comments);
	}

	/** Get Comments.
		@return Comments or additional information
	  */
	public String getComments () 
	{
		return (String)get_Value(COLUMNNAME_Comments);
	}

	/** Set Committed Amount.
		@param CommittedAmt 
		The (legal) commitment amount
	  */
	public void setCommittedAmt (BigDecimal CommittedAmt)
	{
		set_Value (COLUMNNAME_CommittedAmt, CommittedAmt);
	}

	/** Get Committed Amount.
		@return The (legal) commitment amount
	  */
	public BigDecimal getCommittedAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CommittedAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set contractamt.
		@param contractamt contractamt	  */
	public void setcontractamt (BigDecimal contractamt)
	{
		set_Value (COLUMNNAME_contractamt, contractamt);
	}

	/** Get contractamt.
		@return contractamt	  */
	public BigDecimal getcontractamt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_contractamt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_C_Order getC_Order() throws RuntimeException
    {
		return (org.compiere.model.I_C_Order)MTable.get(getCtx(), org.compiere.model.I_C_Order.Table_Name)
			.getPO(getC_Order_ID(), get_TrxName());	}

	/** Set Order.
		@param C_Order_ID 
		Order
	  */
	public void setC_Order_ID (int C_Order_ID)
	{
		if (C_Order_ID < 1) 
			set_Value (COLUMNNAME_C_Order_ID, null);
		else 
			set_Value (COLUMNNAME_C_Order_ID, Integer.valueOf(C_Order_ID));
	}

	/** Get Order.
		@return Order
	  */
	public int getC_Order_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Order_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_Project getC_Project() throws RuntimeException
    {
		return (org.compiere.model.I_C_Project)MTable.get(getCtx(), org.compiere.model.I_C_Project.Table_Name)
			.getPO(getC_Project_ID(), get_TrxName());	}

	/** Set Project.
		@param C_Project_ID 
		Financial Project
	  */
	public void setC_Project_ID (int C_Project_ID)
	{
		if (C_Project_ID < 1) 
			set_Value (COLUMNNAME_C_Project_ID, null);
		else 
			set_Value (COLUMNNAME_C_Project_ID, Integer.valueOf(C_Project_ID));
	}

	/** Get Project.
		@return Financial Project
	  */
	public int getC_Project_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Project_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), String.valueOf(getC_Project_ID()));
    }

	public org.compiere.model.I_C_ProjectLine getC_ProjectLine() throws RuntimeException
    {
		return (org.compiere.model.I_C_ProjectLine)MTable.get(getCtx(), org.compiere.model.I_C_ProjectLine.Table_Name)
			.getPO(getC_ProjectLine_ID(), get_TrxName());	}

	/** Set Project Line.
		@param C_ProjectLine_ID 
		Task or step in a project
	  */
	public void setC_ProjectLine_ID (int C_ProjectLine_ID)
	{
		if (C_ProjectLine_ID < 1) 
			set_Value (COLUMNNAME_C_ProjectLine_ID, null);
		else 
			set_Value (COLUMNNAME_C_ProjectLine_ID, Integer.valueOf(C_ProjectLine_ID));
	}

	/** Get Project Line.
		@return Task or step in a project
	  */
	public int getC_ProjectLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ProjectLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_C_RfQ getC_RfQ() throws RuntimeException
    {
		return (org.compiere.model.I_C_RfQ)MTable.get(getCtx(), org.compiere.model.I_C_RfQ.Table_Name)
			.getPO(getC_RfQ_ID(), get_TrxName());	}

	/** Set RfQ.
		@param C_RfQ_ID 
		Request for Quotation
	  */
	public void setC_RfQ_ID (int C_RfQ_ID)
	{
		if (C_RfQ_ID < 1) 
			set_Value (COLUMNNAME_C_RfQ_ID, null);
		else 
			set_Value (COLUMNNAME_C_RfQ_ID, Integer.valueOf(C_RfQ_ID));
	}

	/** Get RfQ.
		@return Request for Quotation
	  */
	public int getC_RfQ_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_RfQ_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set DateEnd.
		@param DateEnd DateEnd	  */
	public void setDateEnd (Timestamp DateEnd)
	{
		set_Value (COLUMNNAME_DateEnd, DateEnd);
	}

	/** Get DateEnd.
		@return DateEnd	  */
	public Timestamp getDateEnd () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateEnd);
	}

	/** Set dateexpiration.
		@param dateexpiration dateexpiration	  */
	public void setdateexpiration (Timestamp dateexpiration)
	{
		set_Value (COLUMNNAME_dateexpiration, dateexpiration);
	}

	/** Get dateexpiration.
		@return dateexpiration	  */
	public Timestamp getdateexpiration () 
	{
		return (Timestamp)get_Value(COLUMNNAME_dateexpiration);
	}

	/** Set datereception.
		@param datereception datereception	  */
	public void setdatereception (Timestamp datereception)
	{
		set_Value (COLUMNNAME_datereception, datereception);
	}

	/** Get datereception.
		@return datereception	  */
	public Timestamp getdatereception () 
	{
		return (Timestamp)get_Value(COLUMNNAME_datereception);
	}

	/** Set Date Start.
		@param DateStart 
		Date Start for this Order
	  */
	public void setDateStart (Timestamp DateStart)
	{
		set_Value (COLUMNNAME_DateStart, DateStart);
	}

	/** Get Date Start.
		@return Date Start for this Order
	  */
	public Timestamp getDateStart () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateStart);
	}

	/** Set Transaction Date.
		@param DateTrx 
		Transaction Date
	  */
	public void setDateTrx (Timestamp DateTrx)
	{
		set_Value (COLUMNNAME_DateTrx, DateTrx);
	}

	/** Get Transaction Date.
		@return Transaction Date
	  */
	public Timestamp getDateTrx () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateTrx);
	}

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
	}

	/** Set Description2.
		@param Description2 Description2	  */
	public void setDescription2 (String Description2)
	{
		set_Value (COLUMNNAME_Description2, Description2);
	}

	/** Get Description2.
		@return Description2	  */
	public String getDescription2 () 
	{
		return (String)get_Value(COLUMNNAME_Description2);
	}

	/** Set dm_ac_acuerdo.
		@param dm_ac_acuerdo dm_ac_acuerdo	  */
	public void setdm_ac_acuerdo (String dm_ac_acuerdo)
	{
		set_Value (COLUMNNAME_dm_ac_acuerdo, dm_ac_acuerdo);
	}

	/** Get dm_ac_acuerdo.
		@return dm_ac_acuerdo	  */
	public String getdm_ac_acuerdo () 
	{
		return (String)get_Value(COLUMNNAME_dm_ac_acuerdo);
	}

	/** Set dm_ac_certificatedate.
		@param dm_ac_certificatedate dm_ac_certificatedate	  */
	public void setdm_ac_certificatedate (String dm_ac_certificatedate)
	{
		set_Value (COLUMNNAME_dm_ac_certificatedate, dm_ac_certificatedate);
	}

	/** Get dm_ac_certificatedate.
		@return dm_ac_certificatedate	  */
	public String getdm_ac_certificatedate () 
	{
		return (String)get_Value(COLUMNNAME_dm_ac_certificatedate);
	}

	/** Set dm_ac_session.
		@param dm_ac_session dm_ac_session	  */
	public void setdm_ac_session (String dm_ac_session)
	{
		set_Value (COLUMNNAME_dm_ac_session, dm_ac_session);
	}

	/** Get dm_ac_session.
		@return dm_ac_session	  */
	public String getdm_ac_session () 
	{
		return (String)get_Value(COLUMNNAME_dm_ac_session);
	}

	/** Set dm_ac_sessiondate.
		@param dm_ac_sessiondate dm_ac_sessiondate	  */
	public void setdm_ac_sessiondate (String dm_ac_sessiondate)
	{
		set_Value (COLUMNNAME_dm_ac_sessiondate, dm_ac_sessiondate);
	}

	/** Get dm_ac_sessiondate.
		@return dm_ac_sessiondate	  */
	public String getdm_ac_sessiondate () 
	{
		return (String)get_Value(COLUMNNAME_dm_ac_sessiondate);
	}

	/** Set dm_ac_sessiontype.
		@param dm_ac_sessiontype dm_ac_sessiontype	  */
	public void setdm_ac_sessiontype (String dm_ac_sessiontype)
	{
		set_Value (COLUMNNAME_dm_ac_sessiontype, dm_ac_sessiontype);
	}

	/** Get dm_ac_sessiontype.
		@return dm_ac_sessiontype	  */
	public String getdm_ac_sessiontype () 
	{
		return (String)get_Value(COLUMNNAME_dm_ac_sessiontype);
	}

	/** Set DM_Document.
		@param DM_Document_ID DM_Document	  */
	public void setDM_Document_ID (int DM_Document_ID)
	{
		if (DM_Document_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_DM_Document_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_DM_Document_ID, Integer.valueOf(DM_Document_ID));
	}

	/** Get DM_Document.
		@return DM_Document	  */
	public int getDM_Document_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DM_Document_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_DM_Document getdm_documentparent() throws RuntimeException
    {
		return (I_DM_Document)MTable.get(getCtx(), I_DM_Document.Table_Name)
			.getPO(getdm_documentparent_ID(), get_TrxName());	}

	/** Set dm_documentparent_ID.
		@param dm_documentparent_ID dm_documentparent_ID	  */
	public void setdm_documentparent_ID (int dm_documentparent_ID)
	{
		if (dm_documentparent_ID < 1) 
			set_Value (COLUMNNAME_dm_documentparent_ID, null);
		else 
			set_Value (COLUMNNAME_dm_documentparent_ID, Integer.valueOf(dm_documentparent_ID));
	}

	/** Get dm_documentparent_ID.
		@return dm_documentparent_ID	  */
	public int getdm_documentparent_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_dm_documentparent_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set dm_documenttype.
		@param dm_documenttype dm_documenttype	  */
	public void setdm_documenttype (String dm_documenttype)
	{
		set_Value (COLUMNNAME_dm_documenttype, dm_documenttype);
	}

	/** Get dm_documenttype.
		@return dm_documenttype	  */
	public String getdm_documenttype () 
	{
		return (String)get_Value(COLUMNNAME_dm_documenttype);
	}

	/** Set dm_document_type.
		@param dm_document_type dm_document_type	  */
	public void setdm_document_type (String dm_document_type)
	{
		set_Value (COLUMNNAME_dm_document_type, dm_document_type);
	}

	/** Get dm_document_type.
		@return dm_document_type	  */
	public String getdm_document_type () 
	{
		return (String)get_Value(COLUMNNAME_dm_document_type);
	}

	public I_DM_Document getdm_mandateagreement() throws RuntimeException
    {
		return (I_DM_Document)MTable.get(getCtx(), I_DM_Document.Table_Name)
			.getPO(getdm_mandateagreement_ID(), get_TrxName());	}

	/** Set dm_mandateagreement_ID.
		@param dm_mandateagreement_ID dm_mandateagreement_ID	  */
	public void setdm_mandateagreement_ID (int dm_mandateagreement_ID)
	{
		if (dm_mandateagreement_ID < 1) 
			set_Value (COLUMNNAME_dm_mandateagreement_ID, null);
		else 
			set_Value (COLUMNNAME_dm_mandateagreement_ID, Integer.valueOf(dm_mandateagreement_ID));
	}

	/** Get dm_mandateagreement_ID.
		@return dm_mandateagreement_ID	  */
	public int getdm_mandateagreement_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_dm_mandateagreement_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_DM_Document getdm_rs() throws RuntimeException
    {
		return (I_DM_Document)MTable.get(getCtx(), I_DM_Document.Table_Name)
			.getPO(getdm_rs_id(), get_TrxName());	}

	/** Set dm_rs_id.
		@param dm_rs_id dm_rs_id	  */
	public void setdm_rs_id (int dm_rs_id)
	{
		set_Value (COLUMNNAME_dm_rs_id, Integer.valueOf(dm_rs_id));
	}

	/** Get dm_rs_id.
		@return dm_rs_id	  */
	public int getdm_rs_id () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_dm_rs_id);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set DocDistribution.
		@param DocDistribution DocDistribution	  */
	public void setDocDistribution (String DocDistribution)
	{
		set_Value (COLUMNNAME_DocDistribution, DocDistribution);
	}

	/** Get DocDistribution.
		@return DocDistribution	  */
	public String getDocDistribution () 
	{
		return (String)get_Value(COLUMNNAME_DocDistribution);
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
	/** Valorización = VA */
	public static final String DOCSTATUS_Valorizacion = "VA";
	/** En Apelación = EA */
	public static final String DOCSTATUS_EnApelacion = "EA";
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

	/** Set Document No.
		@param DocumentNo 
		Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo)
	{
		set_Value (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo () 
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

	/** Set DocumentTitle.
		@param DocumentTitle DocumentTitle	  */
	public void setDocumentTitle (String DocumentTitle)
	{
		set_Value (COLUMNNAME_DocumentTitle, DocumentTitle);
	}

	/** Get DocumentTitle.
		@return DocumentTitle	  */
	public String getDocumentTitle () 
	{
		return (String)get_Value(COLUMNNAME_DocumentTitle);
	}

	/** Set estamento.
		@param estamento estamento	  */
	public void setestamento (String estamento)
	{
		set_Value (COLUMNNAME_estamento, estamento);
	}

	/** Get estamento.
		@return estamento	  */
	public String getestamento () 
	{
		return (String)get_Value(COLUMNNAME_estamento);
	}

	/** Set Fix amount.
		@param FixAmt 
		Fix amounted amount to be levied or paid
	  */
	public void setFixAmt (BigDecimal FixAmt)
	{
		set_Value (COLUMNNAME_FixAmt, FixAmt);
	}

	/** Get Fix amount.
		@return Fix amounted amount to be levied or paid
	  */
	public BigDecimal getFixAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_FixAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_GL_Journal getGL_Journal() throws RuntimeException
    {
		return (org.compiere.model.I_GL_Journal)MTable.get(getCtx(), org.compiere.model.I_GL_Journal.Table_Name)
			.getPO(getGL_Journal_ID(), get_TrxName());	}

	/** Set Journal.
		@param GL_Journal_ID 
		General Ledger Journal
	  */
	public void setGL_Journal_ID (int GL_Journal_ID)
	{
		if (GL_Journal_ID < 1) 
			set_Value (COLUMNNAME_GL_Journal_ID, null);
		else 
			set_Value (COLUMNNAME_GL_Journal_ID, Integer.valueOf(GL_Journal_ID));
	}

	/** Get Journal.
		@return General Ledger Journal
	  */
	public int getGL_Journal_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_GL_Journal_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set grade.
		@param grade grade	  */
	public void setgrade (String grade)
	{
		set_Value (COLUMNNAME_grade, grade);
	}

	/** Get grade.
		@return grade	  */
	public String getgrade () 
	{
		return (String)get_Value(COLUMNNAME_grade);
	}

	/** ISAPRE AD_Reference_ID=2000024 */
	public static final int ISAPRE_AD_Reference_ID=2000024;
	/** BANMEDICA = 01 */
	public static final String ISAPRE_BANMEDICA = "01";
	/** CONSALUD = 02 */
	public static final String ISAPRE_CONSALUD = "02";
	/** MAS VIDA = 03 */
	public static final String ISAPRE_MASVIDA = "03";
	/** COLMENA = 04 */
	public static final String ISAPRE_COLMENA = "04";
	/** CRUZ BLANCA = 05 */
	public static final String ISAPRE_CRUZBLANCA = "05";
	/** VIDA TRES = 06 */
	public static final String ISAPRE_VIDATRES = "06";
	/** FONASA = 07 */
	public static final String ISAPRE_FONASA = "07";
	/** Set ISAPRE.
		@param ISAPRE ISAPRE	  */
	public void setISAPRE (String ISAPRE)
	{

		set_Value (COLUMNNAME_ISAPRE, ISAPRE);
	}

	/** Get ISAPRE.
		@return ISAPRE	  */
	public String getISAPRE () 
	{
		return (String)get_Value(COLUMNNAME_ISAPRE);
	}

	/** Set iscustomacct.
		@param iscustomacct iscustomacct	  */
	public void setiscustomacct (boolean iscustomacct)
	{
		set_Value (COLUMNNAME_iscustomacct, Boolean.valueOf(iscustomacct));
	}

	/** Get iscustomacct.
		@return iscustomacct	  */
	public boolean iscustomacct () 
	{
		Object oo = get_Value(COLUMNNAME_iscustomacct);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set issigfe.
		@param issigfe issigfe	  */
	public void setissigfe (boolean issigfe)
	{
		set_Value (COLUMNNAME_issigfe, Boolean.valueOf(issigfe));
	}

	/** Get issigfe.
		@return issigfe	  */
	public boolean issigfe () 
	{
		Object oo = get_Value(COLUMNNAME_issigfe);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set LogoRef.
		@param LogoRef LogoRef	  */
	public void setLogoRef (String LogoRef)
	{
		set_Value (COLUMNNAME_LogoRef, LogoRef);
	}

	/** Get LogoRef.
		@return LogoRef	  */
	public String getLogoRef () 
	{
		return (String)get_Value(COLUMNNAME_LogoRef);
	}

	/** Set numberdays.
		@param numberdays numberdays	  */
	public void setnumberdays (BigDecimal numberdays)
	{
		set_Value (COLUMNNAME_numberdays, numberdays);
	}

	/** Get numberdays.
		@return numberdays	  */
	public BigDecimal getnumberdays () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_numberdays);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set ofbbutton.
		@param ofbbutton ofbbutton	  */
	public void setofbbutton (String ofbbutton)
	{
		set_Value (COLUMNNAME_ofbbutton, ofbbutton);
	}

	/** Get ofbbutton.
		@return ofbbutton	  */
	public String getofbbutton () 
	{
		return (String)get_Value(COLUMNNAME_ofbbutton);
	}

	/** Set ofbbutton2.
		@param ofbbutton2 ofbbutton2	  */
	public void setofbbutton2 (String ofbbutton2)
	{
		set_Value (COLUMNNAME_ofbbutton2, ofbbutton2);
	}

	/** Get ofbbutton2.
		@return ofbbutton2	  */
	public String getofbbutton2 () 
	{
		return (String)get_Value(COLUMNNAME_ofbbutton2);
	}

	/** Set ofbbutton3.
		@param ofbbutton3 ofbbutton3	  */
	public void setofbbutton3 (String ofbbutton3)
	{
		set_Value (COLUMNNAME_ofbbutton3, ofbbutton3);
	}

	/** Get ofbbutton3.
		@return ofbbutton3	  */
	public String getofbbutton3 () 
	{
		return (String)get_Value(COLUMNNAME_ofbbutton3);
	}

	/** Set ofbbutton4.
		@param ofbbutton4 ofbbutton4	  */
	public void setofbbutton4 (String ofbbutton4)
	{
		set_Value (COLUMNNAME_ofbbutton4, ofbbutton4);
	}

	/** Get ofbbutton4.
		@return ofbbutton4	  */
	public String getofbbutton4 () 
	{
		return (String)get_Value(COLUMNNAME_ofbbutton4);
	}

	/** Set performancebondstatus.
		@param performancebondstatus performancebondstatus	  */
	public void setperformancebondstatus (String performancebondstatus)
	{
		set_Value (COLUMNNAME_performancebondstatus, performancebondstatus);
	}

	/** Get performancebondstatus.
		@return performancebondstatus	  */
	public String getperformancebondstatus () 
	{
		return (String)get_Value(COLUMNNAME_performancebondstatus);
	}

	/** Set Posted.
		@param Posted 
		Posting status
	  */
	public void setPosted (boolean Posted)
	{
		set_Value (COLUMNNAME_Posted, Boolean.valueOf(Posted));
	}

	/** Get Posted.
		@return Posting status
	  */
	public boolean isPosted () 
	{
		Object oo = get_Value(COLUMNNAME_Posted);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Process Now.
		@param Processing Process Now	  */
	public void setProcessing (boolean Processing)
	{
		set_Value (COLUMNNAME_Processing, Boolean.valueOf(Processing));
	}

	/** Get Process Now.
		@return Process Now	  */
	public boolean isProcessing () 
	{
		Object oo = get_Value(COLUMNNAME_Processing);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set resolutionnumber.
		@param resolutionnumber resolutionnumber	  */
	public void setresolutionnumber (String resolutionnumber)
	{
		set_Value (COLUMNNAME_resolutionnumber, resolutionnumber);
	}

	/** Get resolutionnumber.
		@return resolutionnumber	  */
	public String getresolutionnumber () 
	{
		return (String)get_Value(COLUMNNAME_resolutionnumber);
	}

	/** Set send.
		@param send send	  */
	public void setsend (boolean send)
	{
		set_Value (COLUMNNAME_send, Boolean.valueOf(send));
	}

	/** Get send.
		@return send	  */
	public boolean issend () 
	{
		Object oo = get_Value(COLUMNNAME_send);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set type1.
		@param type1 type1	  */
	public void settype1 (String type1)
	{
		set_Value (COLUMNNAME_type1, type1);
	}

	/** Get type1.
		@return type1	  */
	public String gettype1 () 
	{
		return (String)get_Value(COLUMNNAME_type1);
	}

	/** Set type2.
		@param type2 type2	  */
	public void settype2 (String type2)
	{
		set_Value (COLUMNNAME_type2, type2);
	}

	/** Get type2.
		@return type2	  */
	public String gettype2 () 
	{
		return (String)get_Value(COLUMNNAME_type2);
	}

	/** Set type3.
		@param type3 type3	  */
	public void settype3 (String type3)
	{
		set_Value (COLUMNNAME_type3, type3);
	}

	/** Get type3.
		@return type3	  */
	public String gettype3 () 
	{
		return (String)get_Value(COLUMNNAME_type3);
	}

	/** Set typecontract.
		@param typecontract typecontract	  */
	public void settypecontract (String typecontract)
	{
		set_Value (COLUMNNAME_typecontract, typecontract);
	}

	/** Get typecontract.
		@return typecontract	  */
	public String gettypecontract () 
	{
		return (String)get_Value(COLUMNNAME_typecontract);
	}
}