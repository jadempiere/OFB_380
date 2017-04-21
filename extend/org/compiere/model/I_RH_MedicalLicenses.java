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

/** Generated Interface for RH_MedicalLicenses
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0
 */
public interface I_RH_MedicalLicenses 
{

    /** TableName=RH_MedicalLicenses */
    public static final String Table_Name = "RH_MedicalLicenses";

    /** AD_Table_ID=2000008 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

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

    /** Column name Amount2 */
    public static final String COLUMNNAME_Amount2 = "Amount2";

	/** Set Amount2	  */
	public void setAmount2 (BigDecimal Amount2);

	/** Get Amount2	  */
	public BigDecimal getAmount2();

    /** Column name AmountRefunded */
    public static final String COLUMNNAME_AmountRefunded = "AmountRefunded";

	/** Set AmountRefunded	  */
	public void setAmountRefunded (BigDecimal AmountRefunded);

	/** Get AmountRefunded	  */
	public BigDecimal getAmountRefunded();

    /** Column name AmountTendered */
    public static final String COLUMNNAME_AmountTendered = "AmountTendered";

	/** Set AmountTendered	  */
	public void setAmountTendered (BigDecimal AmountTendered);

	/** Get AmountTendered	  */
	public BigDecimal getAmountTendered();

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

    /** Column name AppealCompin */
    public static final String COLUMNNAME_AppealCompin = "AppealCompin";

	/** Set AppealCompin	  */
	public void setAppealCompin (boolean AppealCompin);

	/** Get AppealCompin	  */
	public boolean isAppealCompin();

    /** Column name AppealIsapre */
    public static final String COLUMNNAME_AppealIsapre = "AppealIsapre";

	/** Set AppealIsapre	  */
	public void setAppealIsapre (boolean AppealIsapre);

	/** Get AppealIsapre	  */
	public boolean isAppealIsapre();

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

    /** Column name C_Payment_ID */
    public static final String COLUMNNAME_C_Payment_ID = "C_Payment_ID";

	/** Set Payment.
	  * Payment identifier
	  */
	public void setC_Payment_ID (int C_Payment_ID);

	/** Get Payment.
	  * Payment identifier
	  */
	public int getC_Payment_ID();

	public org.compiere.model.I_C_Payment getC_Payment() throws RuntimeException;

    /** Column name Characteristics */
    public static final String COLUMNNAME_Characteristics = "Characteristics";

	/** Set Characteristics	  */
	public void setCharacteristics (String Characteristics);

	/** Get Characteristics	  */
	public String getCharacteristics();

    /** Column name cobro */
    public static final String COLUMNNAME_cobro = "cobro";

	/** Set cobro	  */
	public void setcobro (String cobro);

	/** Get cobro	  */
	public String getcobro();

    /** Column name CollectionIsapre */
    public static final String COLUMNNAME_CollectionIsapre = "CollectionIsapre";

	/** Set CollectionIsapre	  */
	public void setCollectionIsapre (BigDecimal CollectionIsapre);

	/** Get CollectionIsapre	  */
	public BigDecimal getCollectionIsapre();

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

    /** Column name Date1 */
    public static final String COLUMNNAME_Date1 = "Date1";

	/** Set Date.
	  * Date when business is not conducted
	  */
	public void setDate1 (Timestamp Date1);

	/** Get Date.
	  * Date when business is not conducted
	  */
	public Timestamp getDate1();

    /** Column name date2 */
    public static final String COLUMNNAME_date2 = "date2";

	/** Set date2	  */
	public void setdate2 (Timestamp date2);

	/** Get date2	  */
	public Timestamp getdate2();

    /** Column name DateEnd */
    public static final String COLUMNNAME_DateEnd = "DateEnd";

	/** Set DateEnd	  */
	public void setDateEnd (Timestamp DateEnd);

	/** Get DateEnd	  */
	public Timestamp getDateEnd();

    /** Column name datestartrequest */
    public static final String COLUMNNAME_datestartrequest = "datestartrequest";

	/** Set datestartrequest	  */
	public void setdatestartrequest (Timestamp datestartrequest);

	/** Get datestartrequest	  */
	public Timestamp getdatestartrequest();

    /** Column name Days */
    public static final String COLUMNNAME_Days = "Days";

	/** Set Days	  */
	public void setDays (BigDecimal Days);

	/** Get Days	  */
	public BigDecimal getDays();

    /** Column name DescriptionAppeal */
    public static final String COLUMNNAME_DescriptionAppeal = "DescriptionAppeal";

	/** Set DescriptionAppeal	  */
	public void setDescriptionAppeal (String DescriptionAppeal);

	/** Get DescriptionAppeal	  */
	public String getDescriptionAppeal();

    /** Column name DescriptionAppealI */
    public static final String COLUMNNAME_DescriptionAppealI = "DescriptionAppealI";

	/** Set DescriptionAppealI	  */
	public void setDescriptionAppealI (String DescriptionAppealI);

	/** Get DescriptionAppealI	  */
	public String getDescriptionAppealI();

    /** Column name devengado */
    public static final String COLUMNNAME_devengado = "devengado";

	/** Set devengado	  */
	public void setdevengado (boolean devengado);

	/** Get devengado	  */
	public boolean isdevengado();

    /** Column name devengoaprobado */
    public static final String COLUMNNAME_devengoaprobado = "devengoaprobado";

	/** Set devengoaprobado	  */
	public void setdevengoaprobado (String devengoaprobado);

	/** Get devengoaprobado	  */
	public String getdevengoaprobado();

    /** Column name dictamen */
    public static final String COLUMNNAME_dictamen = "dictamen";

	/** Set dictamen	  */
	public void setdictamen (String dictamen);

	/** Get dictamen	  */
	public String getdictamen();

    /** Column name Disease */
    public static final String COLUMNNAME_Disease = "Disease";

	/** Set Disease	  */
	public void setDisease (String Disease);

	/** Get Disease	  */
	public String getDisease();

    /** Column name DM_Document_ID */
    public static final String COLUMNNAME_DM_Document_ID = "DM_Document_ID";

	/** Set DM_Document	  */
	public void setDM_Document_ID (BigDecimal DM_Document_ID);

	/** Get DM_Document	  */
	public BigDecimal getDM_Document_ID();

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

    /** Column name Doctor */
    public static final String COLUMNNAME_Doctor = "Doctor";

	/** Set Doctor	  */
	public void setDoctor (String Doctor);

	/** Get Doctor	  */
	public String getDoctor();

    /** Column name Ds */
    public static final String COLUMNNAME_Ds = "Ds";

	/** Set Ds	  */
	public void setDs (String Ds);

	/** Get Ds	  */
	public String getDs();

    /** Column name DsSubsidio */
    public static final String COLUMNNAME_DsSubsidio = "DsSubsidio";

	/** Set DsSubsidio	  */
	public void setDsSubsidio (String DsSubsidio);

	/** Get DsSubsidio	  */
	public String getDsSubsidio();

    /** Column name egresoaprobado */
    public static final String COLUMNNAME_egresoaprobado = "egresoaprobado";

	/** Set egresoaprobado	  */
	public void setegresoaprobado (String egresoaprobado);

	/** Get egresoaprobado	  */
	public String getegresoaprobado();

    /** Column name EntityRejection */
    public static final String COLUMNNAME_EntityRejection = "EntityRejection";

	/** Set EntityRejection	  */
	public void setEntityRejection (String EntityRejection);

	/** Get EntityRejection	  */
	public String getEntityRejection();

    /** Column name EspecialidadDoctor */
    public static final String COLUMNNAME_EspecialidadDoctor = "EspecialidadDoctor";

	/** Set EspecialidadDoctor	  */
	public void setEspecialidadDoctor (String EspecialidadDoctor);

	/** Get EspecialidadDoctor	  */
	public String getEspecialidadDoctor();

    /** Column name EstadoRecuperacion */
    public static final String COLUMNNAME_EstadoRecuperacion = "EstadoRecuperacion";

	/** Set EstadoRecuperacion	  */
	public void setEstadoRecuperacion (String EstadoRecuperacion);

	/** Get EstadoRecuperacion	  */
	public String getEstadoRecuperacion();

    /** Column name fechadevengo */
    public static final String COLUMNNAME_fechadevengo = "fechadevengo";

	/** Set fechadevengo	  */
	public void setfechadevengo (Timestamp fechadevengo);

	/** Get fechadevengo	  */
	public Timestamp getfechadevengo();

    /** Column name fechaegreso */
    public static final String COLUMNNAME_fechaegreso = "fechaegreso";

	/** Set fechaegreso	  */
	public void setfechaegreso (Timestamp fechaegreso);

	/** Get fechaegreso	  */
	public Timestamp getfechaegreso();

    /** Column name Folio */
    public static final String COLUMNNAME_Folio = "Folio";

	/** Set Folio	  */
	public void setFolio (String Folio);

	/** Get Folio	  */
	public String getFolio();

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

    /** Column name IsContinuity */
    public static final String COLUMNNAME_IsContinuity = "IsContinuity";

	/** Set IsContinuity	  */
	public void setIsContinuity (boolean IsContinuity);

	/** Get IsContinuity	  */
	public boolean isContinuity();

    /** Column name LicensedAmount */
    public static final String COLUMNNAME_LicensedAmount = "LicensedAmount";

	/** Set LicensedAmount	  */
	public void setLicensedAmount (BigDecimal LicensedAmount);

	/** Get LicensedAmount	  */
	public BigDecimal getLicensedAmount();

    /** Column name LicenseStatus */
    public static final String COLUMNNAME_LicenseStatus = "LicenseStatus";

	/** Set LicenseStatus	  */
	public void setLicenseStatus (String LicenseStatus);

	/** Get LicenseStatus	  */
	public String getLicenseStatus();

    /** Column name LicenseType */
    public static final String COLUMNNAME_LicenseType = "LicenseType";

	/** Set LicenseType	  */
	public void setLicenseType (String LicenseType);

	/** Get LicenseType	  */
	public String getLicenseType();

    /** Column name month */
    public static final String COLUMNNAME_month = "month";

	/** Set month	  */
	public void setmonth (String month);

	/** Get month	  */
	public String getmonth();

    /** Column name MontoAFP */
    public static final String COLUMNNAME_MontoAFP = "MontoAFP";

	/** Set MontoAFP	  */
	public void setMontoAFP (BigDecimal MontoAFP);

	/** Get MontoAFP	  */
	public BigDecimal getMontoAFP();

    /** Column name MontoSalud */
    public static final String COLUMNNAME_MontoSalud = "MontoSalud";

	/** Set MontoSalud	  */
	public void setMontoSalud (BigDecimal MontoSalud);

	/** Get MontoSalud	  */
	public BigDecimal getMontoSalud();

    /** Column name PaymentStatus */
    public static final String COLUMNNAME_PaymentStatus = "PaymentStatus";

	/** Set PaymentStatus	  */
	public void setPaymentStatus (String PaymentStatus);

	/** Get PaymentStatus	  */
	public String getPaymentStatus();

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

	//public I_C_ValidCombination getProcess() throws RuntimeException;

    /** Column name respuesta */
    public static final String COLUMNNAME_respuesta = "respuesta";

	/** Set respuesta	  */
	public void setrespuesta (String respuesta);

	/** Get respuesta	  */
	public String getrespuesta();

    /** Column name RH_MedicalLicenses_ID */
    public static final String COLUMNNAME_RH_MedicalLicenses_ID = "RH_MedicalLicenses_ID";

	/** Set RH_MedicalLicenses ID	  */
	public void setRH_MedicalLicenses_ID (int RH_MedicalLicenses_ID);

	/** Get RH_MedicalLicenses ID	  */
	public int getRH_MedicalLicenses_ID();

    /** Column name RutDoctor */
    public static final String COLUMNNAME_RutDoctor = "RutDoctor";

	/** Set RutDoctor	  */
	public void setRutDoctor (String RutDoctor);

	/** Get RutDoctor	  */
	public String getRutDoctor();

    /** Column name subsidioderecho */
    public static final String COLUMNNAME_subsidioderecho = "subsidioderecho";

	/** Set subsidioderecho	  */
	public void setsubsidioderecho (boolean subsidioderecho);

	/** Get subsidioderecho	  */
	public boolean issubsidioderecho();

    /** Column name Suplencia */
    public static final String COLUMNNAME_Suplencia = "Suplencia";

	/** Set Suplencia	  */
	public void setSuplencia (int Suplencia);

	/** Get Suplencia	  */
	public int getSuplencia();

	public org.compiere.model.I_C_BPartner getSuplen() throws RuntimeException;

    /** Column name TimeOfTheDay */
    public static final String COLUMNNAME_TimeOfTheDay = "TimeOfTheDay";

	/** Set TimeOfTheDay	  */
	public void setTimeOfTheDay (String TimeOfTheDay);

	/** Get TimeOfTheDay	  */
	public String getTimeOfTheDay();

    /** Column name TotalRecuperar */
    public static final String COLUMNNAME_TotalRecuperar = "TotalRecuperar";

	/** Set TotalRecuperar	  */
	public void setTotalRecuperar (BigDecimal TotalRecuperar);

	/** Get TotalRecuperar	  */
	public BigDecimal getTotalRecuperar();

    /** Column name Type */
    public static final String COLUMNNAME_Type = "Type";

	/** Set Type.
	  * Type of Validation (SQL, Java Script, Java Language)
	  */
	public void setType (String Type);

	/** Get Type.
	  * Type of Validation (SQL, Java Script, Java Language)
	  */
	public String getType();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();
}
