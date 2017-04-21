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
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.util.KeyNamePair;

/** Generated Interface for DM_Document
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0
 */
public interface I_DM_Document 
{

    /** TableName=DM_Document */
    public static final String Table_Name = "DM_Document";

    /** AD_Table_ID=2000010 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name acumanticipo */
    public static final String COLUMNNAME_acumanticipo = "acumanticipo";

	/** Set acumanticipo	  */
	public void setacumanticipo (BigDecimal acumanticipo);

	/** Get acumanticipo	  */
	public BigDecimal getacumanticipo();

    /** Column name acumdeva */
    public static final String COLUMNNAME_acumdeva = "acumdeva";

	/** Set acumdeva	  */
	public void setacumdeva (BigDecimal acumdeva);

	/** Get acumdeva	  */
	public BigDecimal getacumdeva();

    /** Column name acumdevotrr */
    public static final String COLUMNNAME_acumdevotrr = "acumdevotrr";

	/** Set acumdevotrr	  */
	public void setacumdevotrr (BigDecimal acumdevotrr);

	/** Get acumdevotrr	  */
	public BigDecimal getacumdevotrr();

    /** Column name acumdevr */
    public static final String COLUMNNAME_acumdevr = "acumdevr";

	/** Set acumdevr	  */
	public void setacumdevr (BigDecimal acumdevr);

	/** Get acumdevr	  */
	public BigDecimal getacumdevr();

    /** Column name acummultas */
    public static final String COLUMNNAME_acummultas = "acummultas";

	/** Set acummultas	  */
	public void setacummultas (BigDecimal acummultas);

	/** Get acummultas	  */
	public BigDecimal getacummultas();

    /** Column name acumotrasr */
    public static final String COLUMNNAME_acumotrasr = "acumotrasr";

	/** Set acumotrasr	  */
	public void setacumotrasr (BigDecimal acumotrasr);

	/** Get acumotrasr	  */
	public BigDecimal getacumotrasr();

    /** Column name acumreajustea */
    public static final String COLUMNNAME_acumreajustea = "acumreajustea";

	/** Set acumreajustea	  */
	public void setacumreajustea (BigDecimal acumreajustea);

	/** Get acumreajustea	  */
	public BigDecimal getacumreajustea();

    /** Column name acumreajusteo */
    public static final String COLUMNNAME_acumreajusteo = "acumreajusteo";

	/** Set acumreajusteo	  */
	public void setacumreajusteo (BigDecimal acumreajusteo);

	/** Get acumreajusteo	  */
	public BigDecimal getacumreajusteo();

    /** Column name acumretencion */
    public static final String COLUMNNAME_acumretencion = "acumretencion";

	/** Set acumretencion	  */
	public void setacumretencion (BigDecimal acumretencion);

	/** Get acumretencion	  */
	public BigDecimal getacumretencion();

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name AD_User_ID */
    public static final String COLUMNNAME_AD_User_ID = "AD_User_ID";

	/** Set User/Contact.
	  * User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID);

	/** Get User/Contact.
	  * User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID();

	public org.compiere.model.I_AD_User getAD_User() throws RuntimeException;

    /** Column name Amt */
    public static final String COLUMNNAME_Amt = "Amt";

	/** Set Amount.
	  * Amount
	  */
	public void setAmt (BigDecimal Amt);

	/** Get Amount.
	  * Amount
	  */
	public BigDecimal getAmt();

    /** Column name amtdate */
    public static final String COLUMNNAME_amtdate = "amtdate";

	/** Set amtdate	  */
	public void setamtdate (BigDecimal amtdate);

	/** Get amtdate	  */
	public BigDecimal getamtdate();

    /** Column name banco */
    public static final String COLUMNNAME_banco = "banco";

	/** Set banco	  */
	public void setbanco (String banco);

	/** Get banco	  */
	public String getbanco();

    /** Column name C_BPartner_ID */
    public static final String COLUMNNAME_C_BPartner_ID = "C_BPartner_ID";

	/** Set Business Partner .
	  * Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID);

	/** Get Business Partner .
	  * Identifies a Business Partner
	  */
	public int getC_BPartner_ID();

	public org.compiere.model.I_C_BPartner getC_BPartner() throws RuntimeException;

    /** Column name c_bpartnerp_ID */
    public static final String COLUMNNAME_c_bpartnerp_ID = "c_bpartnerp_ID";

	/** Set c_bpartnerp_ID	  */
	public void setc_bpartnerp_ID (int c_bpartnerp_ID);

	/** Get c_bpartnerp_ID	  */
	public int getc_bpartnerp_ID();

	public org.compiere.model.I_C_BPartner getc_bpartnerp() throws RuntimeException;

    /** Column name C_Charge_ID */
    public static final String COLUMNNAME_C_Charge_ID = "C_Charge_ID";

	/** Set Charge.
	  * Additional document charges
	  */
	public void setC_Charge_ID (int C_Charge_ID);

	/** Get Charge.
	  * Additional document charges
	  */
	public int getC_Charge_ID();

	public org.compiere.model.I_C_Charge getC_Charge() throws RuntimeException;

    /** Column name C_Currency_ID */
    public static final String COLUMNNAME_C_Currency_ID = "C_Currency_ID";

	/** Set Currency.
	  * The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID);

	/** Get Currency.
	  * The Currency for this record
	  */
	public int getC_Currency_ID();

	public org.compiere.model.I_C_Currency getC_Currency() throws RuntimeException;

    /** Column name C_DocType_ID */
    public static final String COLUMNNAME_C_DocType_ID = "C_DocType_ID";

	/** Set Document Type.
	  * Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID);

	/** Get Document Type.
	  * Document type or rules
	  */
	public int getC_DocType_ID();

	public org.compiere.model.I_C_DocType getC_DocType() throws RuntimeException;

    /** Column name codigodeltrabajo */
    public static final String COLUMNNAME_codigodeltrabajo = "codigodeltrabajo";

	/** Set codigodeltrabajo	  */
	public void setcodigodeltrabajo (String codigodeltrabajo);

	/** Get codigodeltrabajo	  */
	public String getcodigodeltrabajo();

    /** Column name Comments */
    public static final String COLUMNNAME_Comments = "Comments";

	/** Set Comments.
	  * Comments or additional information
	  */
	public void setComments (String Comments);

	/** Get Comments.
	  * Comments or additional information
	  */
	public String getComments();

    /** Column name CommittedAmt */
    public static final String COLUMNNAME_CommittedAmt = "CommittedAmt";

	/** Set Committed Amount.
	  * The (legal) commitment amount
	  */
	public void setCommittedAmt (BigDecimal CommittedAmt);

	/** Get Committed Amount.
	  * The (legal) commitment amount
	  */
	public BigDecimal getCommittedAmt();

    /** Column name contractamt */
    public static final String COLUMNNAME_contractamt = "contractamt";

	/** Set contractamt	  */
	public void setcontractamt (BigDecimal contractamt);

	/** Get contractamt	  */
	public BigDecimal getcontractamt();

    /** Column name C_Order_ID */
    public static final String COLUMNNAME_C_Order_ID = "C_Order_ID";

	/** Set Order.
	  * Order
	  */
	public void setC_Order_ID (int C_Order_ID);

	/** Get Order.
	  * Order
	  */
	public int getC_Order_ID();

	public org.compiere.model.I_C_Order getC_Order() throws RuntimeException;

    /** Column name C_Project_ID */
    public static final String COLUMNNAME_C_Project_ID = "C_Project_ID";

	/** Set Project.
	  * Financial Project
	  */
	public void setC_Project_ID (int C_Project_ID);

	/** Get Project.
	  * Financial Project
	  */
	public int getC_Project_ID();

	public org.compiere.model.I_C_Project getC_Project() throws RuntimeException;

    /** Column name C_ProjectLine_ID */
    public static final String COLUMNNAME_C_ProjectLine_ID = "C_ProjectLine_ID";

	/** Set Project Line.
	  * Task or step in a project
	  */
	public void setC_ProjectLine_ID (int C_ProjectLine_ID);

	/** Get Project Line.
	  * Task or step in a project
	  */
	public int getC_ProjectLine_ID();

	public org.compiere.model.I_C_ProjectLine getC_ProjectLine() throws RuntimeException;

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name C_RfQ_ID */
    public static final String COLUMNNAME_C_RfQ_ID = "C_RfQ_ID";

	/** Set RfQ.
	  * Request for Quotation
	  */
	public void setC_RfQ_ID (int C_RfQ_ID);

	/** Get RfQ.
	  * Request for Quotation
	  */
	public int getC_RfQ_ID();

	public org.compiere.model.I_C_RfQ getC_RfQ() throws RuntimeException;

    /** Column name DateEnd */
    public static final String COLUMNNAME_DateEnd = "DateEnd";

	/** Set DateEnd	  */
	public void setDateEnd (Timestamp DateEnd);

	/** Get DateEnd	  */
	public Timestamp getDateEnd();

    /** Column name dateexpiration */
    public static final String COLUMNNAME_dateexpiration = "dateexpiration";

	/** Set dateexpiration	  */
	public void setdateexpiration (Timestamp dateexpiration);

	/** Get dateexpiration	  */
	public Timestamp getdateexpiration();

    /** Column name datereception */
    public static final String COLUMNNAME_datereception = "datereception";

	/** Set datereception	  */
	public void setdatereception (Timestamp datereception);

	/** Get datereception	  */
	public Timestamp getdatereception();

    /** Column name DateStart */
    public static final String COLUMNNAME_DateStart = "DateStart";

	/** Set Date Start.
	  * Date Start for this Order
	  */
	public void setDateStart (Timestamp DateStart);

	/** Get Date Start.
	  * Date Start for this Order
	  */
	public Timestamp getDateStart();

    /** Column name DateTrx */
    public static final String COLUMNNAME_DateTrx = "DateTrx";

	/** Set Transaction Date.
	  * Transaction Date
	  */
	public void setDateTrx (Timestamp DateTrx);

	/** Get Transaction Date.
	  * Transaction Date
	  */
	public Timestamp getDateTrx();

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name Description2 */
    public static final String COLUMNNAME_Description2 = "Description2";

	/** Set Description2	  */
	public void setDescription2 (String Description2);

	/** Get Description2	  */
	public String getDescription2();

    /** Column name dm_ac_acuerdo */
    public static final String COLUMNNAME_dm_ac_acuerdo = "dm_ac_acuerdo";

	/** Set dm_ac_acuerdo	  */
	public void setdm_ac_acuerdo (String dm_ac_acuerdo);

	/** Get dm_ac_acuerdo	  */
	public String getdm_ac_acuerdo();

    /** Column name dm_ac_certificatedate */
    public static final String COLUMNNAME_dm_ac_certificatedate = "dm_ac_certificatedate";

	/** Set dm_ac_certificatedate	  */
	public void setdm_ac_certificatedate (String dm_ac_certificatedate);

	/** Get dm_ac_certificatedate	  */
	public String getdm_ac_certificatedate();

    /** Column name dm_ac_session */
    public static final String COLUMNNAME_dm_ac_session = "dm_ac_session";

	/** Set dm_ac_session	  */
	public void setdm_ac_session (String dm_ac_session);

	/** Get dm_ac_session	  */
	public String getdm_ac_session();

    /** Column name dm_ac_sessiondate */
    public static final String COLUMNNAME_dm_ac_sessiondate = "dm_ac_sessiondate";

	/** Set dm_ac_sessiondate	  */
	public void setdm_ac_sessiondate (String dm_ac_sessiondate);

	/** Get dm_ac_sessiondate	  */
	public String getdm_ac_sessiondate();

    /** Column name dm_ac_sessiontype */
    public static final String COLUMNNAME_dm_ac_sessiontype = "dm_ac_sessiontype";

	/** Set dm_ac_sessiontype	  */
	public void setdm_ac_sessiontype (String dm_ac_sessiontype);

	/** Get dm_ac_sessiontype	  */
	public String getdm_ac_sessiontype();

    /** Column name DM_Document_ID */
    public static final String COLUMNNAME_DM_Document_ID = "DM_Document_ID";

	/** Set DM_Document	  */
	public void setDM_Document_ID (int DM_Document_ID);

	/** Get DM_Document	  */
	public int getDM_Document_ID();

    /** Column name dm_documentparent_ID */
    public static final String COLUMNNAME_dm_documentparent_ID = "dm_documentparent_ID";

	/** Set dm_documentparent_ID	  */
	public void setdm_documentparent_ID (int dm_documentparent_ID);

	/** Get dm_documentparent_ID	  */
	public int getdm_documentparent_ID();

	public I_DM_Document getdm_documentparent() throws RuntimeException;

    /** Column name dm_documenttype */
    public static final String COLUMNNAME_dm_documenttype = "dm_documenttype";

	/** Set dm_documenttype	  */
	public void setdm_documenttype (String dm_documenttype);

	/** Get dm_documenttype	  */
	public String getdm_documenttype();

    /** Column name dm_document_type */
    public static final String COLUMNNAME_dm_document_type = "dm_document_type";

	/** Set dm_document_type	  */
	public void setdm_document_type (String dm_document_type);

	/** Get dm_document_type	  */
	public String getdm_document_type();

    /** Column name dm_mandateagreement_ID */
    public static final String COLUMNNAME_dm_mandateagreement_ID = "dm_mandateagreement_ID";

	/** Set dm_mandateagreement_ID	  */
	public void setdm_mandateagreement_ID (int dm_mandateagreement_ID);

	/** Get dm_mandateagreement_ID	  */
	public int getdm_mandateagreement_ID();

	public I_DM_Document getdm_mandateagreement() throws RuntimeException;

    /** Column name dm_rs_id */
    public static final String COLUMNNAME_dm_rs_id = "dm_rs_id";

	/** Set dm_rs_id	  */
	public void setdm_rs_id (int dm_rs_id);

	/** Get dm_rs_id	  */
	public int getdm_rs_id();

	public I_DM_Document getdm_rs() throws RuntimeException;

    /** Column name DocDistribution */
    public static final String COLUMNNAME_DocDistribution = "DocDistribution";

	/** Set DocDistribution	  */
	public void setDocDistribution (String DocDistribution);

	/** Get DocDistribution	  */
	public String getDocDistribution();

    /** Column name DocStatus */
    public static final String COLUMNNAME_DocStatus = "DocStatus";

	/** Set Document Status.
	  * The current status of the document
	  */
	public void setDocStatus (String DocStatus);

	/** Get Document Status.
	  * The current status of the document
	  */
	public String getDocStatus();

    /** Column name DocumentNo */
    public static final String COLUMNNAME_DocumentNo = "DocumentNo";

	/** Set Document No.
	  * Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo);

	/** Get Document No.
	  * Document sequence number of the document
	  */
	public String getDocumentNo();

    /** Column name DocumentTitle */
    public static final String COLUMNNAME_DocumentTitle = "DocumentTitle";

	/** Set DocumentTitle	  */
	public void setDocumentTitle (String DocumentTitle);

	/** Get DocumentTitle	  */
	public String getDocumentTitle();

    /** Column name estamento */
    public static final String COLUMNNAME_estamento = "estamento";

	/** Set estamento	  */
	public void setestamento (String estamento);

	/** Get estamento	  */
	public String getestamento();

    /** Column name FixAmt */
    public static final String COLUMNNAME_FixAmt = "FixAmt";

	/** Set Fix amount.
	  * Fix amounted amount to be levied or paid
	  */
	public void setFixAmt (BigDecimal FixAmt);

	/** Get Fix amount.
	  * Fix amounted amount to be levied or paid
	  */
	public BigDecimal getFixAmt();

    /** Column name GL_Journal_ID */
    public static final String COLUMNNAME_GL_Journal_ID = "GL_Journal_ID";

	/** Set Journal.
	  * General Ledger Journal
	  */
	public void setGL_Journal_ID (int GL_Journal_ID);

	/** Get Journal.
	  * General Ledger Journal
	  */
	public int getGL_Journal_ID();

	public org.compiere.model.I_GL_Journal getGL_Journal() throws RuntimeException;

    /** Column name grade */
    public static final String COLUMNNAME_grade = "grade";

	/** Set grade	  */
	public void setgrade (String grade);

	/** Get grade	  */
	public String getgrade();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name ISAPRE */
    public static final String COLUMNNAME_ISAPRE = "ISAPRE";

	/** Set ISAPRE	  */
	public void setISAPRE (String ISAPRE);

	/** Get ISAPRE	  */
	public String getISAPRE();

    /** Column name iscustomacct */
    public static final String COLUMNNAME_iscustomacct = "iscustomacct";

	/** Set iscustomacct	  */
	public void setiscustomacct (boolean iscustomacct);

	/** Get iscustomacct	  */
	public boolean iscustomacct();

    /** Column name issigfe */
    public static final String COLUMNNAME_issigfe = "issigfe";

	/** Set issigfe	  */
	public void setissigfe (boolean issigfe);

	/** Get issigfe	  */
	public boolean issigfe();

    /** Column name LogoRef */
    public static final String COLUMNNAME_LogoRef = "LogoRef";

	/** Set LogoRef	  */
	public void setLogoRef (String LogoRef);

	/** Get LogoRef	  */
	public String getLogoRef();

    /** Column name numberdays */
    public static final String COLUMNNAME_numberdays = "numberdays";

	/** Set numberdays	  */
	public void setnumberdays (BigDecimal numberdays);

	/** Get numberdays	  */
	public BigDecimal getnumberdays();

    /** Column name ofbbutton */
    public static final String COLUMNNAME_ofbbutton = "ofbbutton";

	/** Set ofbbutton	  */
	public void setofbbutton (String ofbbutton);

	/** Get ofbbutton	  */
	public String getofbbutton();

    /** Column name ofbbutton2 */
    public static final String COLUMNNAME_ofbbutton2 = "ofbbutton2";

	/** Set ofbbutton2	  */
	public void setofbbutton2 (String ofbbutton2);

	/** Get ofbbutton2	  */
	public String getofbbutton2();

    /** Column name ofbbutton3 */
    public static final String COLUMNNAME_ofbbutton3 = "ofbbutton3";

	/** Set ofbbutton3	  */
	public void setofbbutton3 (String ofbbutton3);

	/** Get ofbbutton3	  */
	public String getofbbutton3();

    /** Column name ofbbutton4 */
    public static final String COLUMNNAME_ofbbutton4 = "ofbbutton4";

	/** Set ofbbutton4	  */
	public void setofbbutton4 (String ofbbutton4);

	/** Get ofbbutton4	  */
	public String getofbbutton4();

    /** Column name performancebondstatus */
    public static final String COLUMNNAME_performancebondstatus = "performancebondstatus";

	/** Set performancebondstatus	  */
	public void setperformancebondstatus (String performancebondstatus);

	/** Get performancebondstatus	  */
	public String getperformancebondstatus();

    /** Column name Posted */
    public static final String COLUMNNAME_Posted = "Posted";

	/** Set Posted.
	  * Posting status
	  */
	public void setPosted (boolean Posted);

	/** Get Posted.
	  * Posting status
	  */
	public boolean isPosted();

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

	/** Set Processed.
	  * The document has been processed
	  */
	public void setProcessed (boolean Processed);

	/** Get Processed.
	  * The document has been processed
	  */
	public boolean isProcessed();

    /** Column name Processing */
    public static final String COLUMNNAME_Processing = "Processing";

	/** Set Process Now	  */
	public void setProcessing (boolean Processing);

	/** Get Process Now	  */
	public boolean isProcessing();

    /** Column name resolutionnumber */
    public static final String COLUMNNAME_resolutionnumber = "resolutionnumber";

	/** Set resolutionnumber	  */
	public void setresolutionnumber (String resolutionnumber);

	/** Get resolutionnumber	  */
	public String getresolutionnumber();

    /** Column name send */
    public static final String COLUMNNAME_send = "send";

	/** Set send	  */
	public void setsend (boolean send);

	/** Get send	  */
	public boolean issend();

    /** Column name type1 */
    public static final String COLUMNNAME_type1 = "type1";

	/** Set type1	  */
	public void settype1 (String type1);

	/** Get type1	  */
	public String gettype1();

    /** Column name type2 */
    public static final String COLUMNNAME_type2 = "type2";

	/** Set type2	  */
	public void settype2 (String type2);

	/** Get type2	  */
	public String gettype2();

    /** Column name type3 */
    public static final String COLUMNNAME_type3 = "type3";

	/** Set type3	  */
	public void settype3 (String type3);

	/** Get type3	  */
	public String gettype3();

    /** Column name typecontract */
    public static final String COLUMNNAME_typecontract = "typecontract";

	/** Set typecontract	  */
	public void settypecontract (String typecontract);

	/** Get typecontract	  */
	public String gettypecontract();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get UpdatedBy.
	  * User who updated this records
	  */
	public int getUpdatedBy();
}
