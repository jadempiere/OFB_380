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

/** Generated Model for RH_MedicalLicenses
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0 - $Id$ */
public class X_RH_MedicalLicenses extends PO implements I_RH_MedicalLicenses, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20160512L;

    /** Standard Constructor */
    public X_RH_MedicalLicenses (Properties ctx, int RH_MedicalLicenses_ID, String trxName)
    {
      super (ctx, RH_MedicalLicenses_ID, trxName);
      /** if (RH_MedicalLicenses_ID == 0)
        {
			setAppealCompin (false);
			setAppealIsapre (false);
			setLicenseStatus (null);
			setRH_MedicalLicenses_ID (0);
        } */
    }

    /** Load Constructor */
    public X_RH_MedicalLicenses (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_RH_MedicalLicenses[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Amount2.
		@param Amount2 Amount2	  */
	public void setAmount2 (BigDecimal Amount2)
	{
		set_Value (COLUMNNAME_Amount2, Amount2);
	}

	/** Get Amount2.
		@return Amount2	  */
	public BigDecimal getAmount2 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Amount2);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set AmountRefunded.
		@param AmountRefunded AmountRefunded	  */
	public void setAmountRefunded (BigDecimal AmountRefunded)
	{
		set_Value (COLUMNNAME_AmountRefunded, AmountRefunded);
	}

	/** Get AmountRefunded.
		@return AmountRefunded	  */
	public BigDecimal getAmountRefunded () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmountRefunded);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set AmountTendered.
		@param AmountTendered AmountTendered	  */
	public void setAmountTendered (BigDecimal AmountTendered)
	{
		set_Value (COLUMNNAME_AmountTendered, AmountTendered);
	}

	/** Get AmountTendered.
		@return AmountTendered	  */
	public BigDecimal getAmountTendered () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_AmountTendered);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set AppealCompin.
		@param AppealCompin AppealCompin	  */
	public void setAppealCompin (boolean AppealCompin)
	{
		set_Value (COLUMNNAME_AppealCompin, Boolean.valueOf(AppealCompin));
	}

	/** Get AppealCompin.
		@return AppealCompin	  */
	public boolean isAppealCompin () 
	{
		Object oo = get_Value(COLUMNNAME_AppealCompin);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set AppealIsapre.
		@param AppealIsapre AppealIsapre	  */
	public void setAppealIsapre (boolean AppealIsapre)
	{
		set_Value (COLUMNNAME_AppealIsapre, Boolean.valueOf(AppealIsapre));
	}

	/** Get AppealIsapre.
		@return AppealIsapre	  */
	public boolean isAppealIsapre () 
	{
		Object oo = get_Value(COLUMNNAME_AppealIsapre);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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

	public org.compiere.model.I_C_Payment getC_Payment() throws RuntimeException
    {
		return (org.compiere.model.I_C_Payment)MTable.get(getCtx(), org.compiere.model.I_C_Payment.Table_Name)
			.getPO(getC_Payment_ID(), get_TrxName());	}

	/** Set Payment.
		@param C_Payment_ID 
		Payment identifier
	  */
	public void setC_Payment_ID (int C_Payment_ID)
	{
		if (C_Payment_ID < 1) 
			set_Value (COLUMNNAME_C_Payment_ID, null);
		else 
			set_Value (COLUMNNAME_C_Payment_ID, Integer.valueOf(C_Payment_ID));
	}

	/** Get Payment.
		@return Payment identifier
	  */
	public int getC_Payment_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Payment_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Characteristics AD_Reference_ID=2000025 */
	public static final int CHARACTERISTICS_AD_Reference_ID=2000025;
	/** Completo = 01 */
	public static final String CHARACTERISTICS_Completo = "01";
	/** Parcial = 02 */
	public static final String CHARACTERISTICS_Parcial = "02";
	/** Set Characteristics.
		@param Characteristics Characteristics	  */
	public void setCharacteristics (String Characteristics)
	{

		set_Value (COLUMNNAME_Characteristics, Characteristics);
	}

	/** Get Characteristics.
		@return Characteristics	  */
	public String getCharacteristics () 
	{
		return (String)get_Value(COLUMNNAME_Characteristics);
	}

	/** Set cobro.
		@param cobro cobro	  */
	public void setcobro (String cobro)
	{
		set_Value (COLUMNNAME_cobro, cobro);
	}

	/** Get cobro.
		@return cobro	  */
	public String getcobro () 
	{
		return (String)get_Value(COLUMNNAME_cobro);
	}

	/** Set CollectionIsapre.
		@param CollectionIsapre CollectionIsapre	  */
	public void setCollectionIsapre (BigDecimal CollectionIsapre)
	{
		set_Value (COLUMNNAME_CollectionIsapre, CollectionIsapre);
	}

	/** Get CollectionIsapre.
		@return CollectionIsapre	  */
	public BigDecimal getCollectionIsapre () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CollectionIsapre);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set date2.
		@param date2 date2	  */
	public void setdate2 (Timestamp date2)
	{
		set_Value (COLUMNNAME_date2, date2);
	}

	/** Get date2.
		@return date2	  */
	public Timestamp getdate2 () 
	{
		return (Timestamp)get_Value(COLUMNNAME_date2);
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

	/** Set datestartrequest.
		@param datestartrequest datestartrequest	  */
	public void setdatestartrequest (Timestamp datestartrequest)
	{
		set_Value (COLUMNNAME_datestartrequest, datestartrequest);
	}

	/** Get datestartrequest.
		@return datestartrequest	  */
	public Timestamp getdatestartrequest () 
	{
		return (Timestamp)get_Value(COLUMNNAME_datestartrequest);
	}

	/** Set Days.
		@param Days Days	  */
	public void setDays (BigDecimal Days)
	{
		set_Value (COLUMNNAME_Days, Days);
	}

	/** Get Days.
		@return Days	  */
	public BigDecimal getDays () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Days);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set DescriptionAppeal.
		@param DescriptionAppeal DescriptionAppeal	  */
	public void setDescriptionAppeal (String DescriptionAppeal)
	{
		set_Value (COLUMNNAME_DescriptionAppeal, DescriptionAppeal);
	}

	/** Get DescriptionAppeal.
		@return DescriptionAppeal	  */
	public String getDescriptionAppeal () 
	{
		return (String)get_Value(COLUMNNAME_DescriptionAppeal);
	}

	/** Set DescriptionAppealI.
		@param DescriptionAppealI DescriptionAppealI	  */
	public void setDescriptionAppealI (String DescriptionAppealI)
	{
		set_Value (COLUMNNAME_DescriptionAppealI, DescriptionAppealI);
	}

	/** Get DescriptionAppealI.
		@return DescriptionAppealI	  */
	public String getDescriptionAppealI () 
	{
		return (String)get_Value(COLUMNNAME_DescriptionAppealI);
	}

	/** Set devengado.
		@param devengado devengado	  */
	public void setdevengado (boolean devengado)
	{
		set_Value (COLUMNNAME_devengado, Boolean.valueOf(devengado));
	}

	/** Get devengado.
		@return devengado	  */
	public boolean isdevengado () 
	{
		Object oo = get_Value(COLUMNNAME_devengado);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set devengoaprobado.
		@param devengoaprobado devengoaprobado	  */
	public void setdevengoaprobado (String devengoaprobado)
	{
		set_Value (COLUMNNAME_devengoaprobado, devengoaprobado);
	}

	/** Get devengoaprobado.
		@return devengoaprobado	  */
	public String getdevengoaprobado () 
	{
		return (String)get_Value(COLUMNNAME_devengoaprobado);
	}

	/** Set dictamen.
		@param dictamen dictamen	  */
	public void setdictamen (String dictamen)
	{
		set_Value (COLUMNNAME_dictamen, dictamen);
	}

	/** Get dictamen.
		@return dictamen	  */
	public String getdictamen () 
	{
		return (String)get_Value(COLUMNNAME_dictamen);
	}

	/** Set Disease.
		@param Disease Disease	  */
	public void setDisease (String Disease)
	{
		set_Value (COLUMNNAME_Disease, Disease);
	}

	/** Get Disease.
		@return Disease	  */
	public String getDisease () 
	{
		return (String)get_Value(COLUMNNAME_Disease);
	}

	/** Set DM_Document.
		@param DM_Document_ID DM_Document	  */
	public void setDM_Document_ID (BigDecimal DM_Document_ID)
	{
		set_Value (COLUMNNAME_DM_Document_ID, DM_Document_ID);
	}

	/** Get DM_Document.
		@return DM_Document	  */
	public BigDecimal getDM_Document_ID () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_DM_Document_ID);
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

		set_ValueNoCheck (COLUMNNAME_DocStatus, DocStatus);
	}

	/** Get Document Status.
		@return The current status of the document
	  */
	public String getDocStatus () 
	{
		return (String)get_Value(COLUMNNAME_DocStatus);
	}

	/** Set Doctor.
		@param Doctor Doctor	  */
	public void setDoctor (String Doctor)
	{
		set_Value (COLUMNNAME_Doctor, Doctor);
	}

	/** Get Doctor.
		@return Doctor	  */
	public String getDoctor () 
	{
		return (String)get_Value(COLUMNNAME_Doctor);
	}

	/** Set Ds.
		@param Ds Ds	  */
	public void setDs (String Ds)
	{
		set_Value (COLUMNNAME_Ds, Ds);
	}

	/** Get Ds.
		@return Ds	  */
	public String getDs () 
	{
		return (String)get_Value(COLUMNNAME_Ds);
	}

	/** Set DsSubsidio.
		@param DsSubsidio DsSubsidio	  */
	public void setDsSubsidio (String DsSubsidio)
	{
		set_Value (COLUMNNAME_DsSubsidio, DsSubsidio);
	}

	/** Get DsSubsidio.
		@return DsSubsidio	  */
	public String getDsSubsidio () 
	{
		return (String)get_Value(COLUMNNAME_DsSubsidio);
	}

	/** Set egresoaprobado.
		@param egresoaprobado egresoaprobado	  */
	public void setegresoaprobado (String egresoaprobado)
	{
		set_Value (COLUMNNAME_egresoaprobado, egresoaprobado);
	}

	/** Get egresoaprobado.
		@return egresoaprobado	  */
	public String getegresoaprobado () 
	{
		return (String)get_Value(COLUMNNAME_egresoaprobado);
	}

	/** EntityRejection AD_Reference_ID=2000026 */
	public static final int ENTITYREJECTION_AD_Reference_ID=2000026;
	/** Compin = 01 */
	public static final String ENTITYREJECTION_Compin = "01";
	/** Isapre = 02 */
	public static final String ENTITYREJECTION_Isapre = "02";
	/** Suseso = 03 */
	public static final String ENTITYREJECTION_Suseso = "03";
	/** CGR = 04 */
	public static final String ENTITYREJECTION_CGR = "04";
	/** CGD = 05 */
	public static final String ENTITYREJECTION_CGD = "05";
	/** Set EntityRejection.
		@param EntityRejection EntityRejection	  */
	public void setEntityRejection (String EntityRejection)
	{

		set_Value (COLUMNNAME_EntityRejection, EntityRejection);
	}

	/** Get EntityRejection.
		@return EntityRejection	  */
	public String getEntityRejection () 
	{
		return (String)get_Value(COLUMNNAME_EntityRejection);
	}

	/** Set EspecialidadDoctor.
		@param EspecialidadDoctor EspecialidadDoctor	  */
	public void setEspecialidadDoctor (String EspecialidadDoctor)
	{
		set_Value (COLUMNNAME_EspecialidadDoctor, EspecialidadDoctor);
	}

	/** Get EspecialidadDoctor.
		@return EspecialidadDoctor	  */
	public String getEspecialidadDoctor () 
	{
		return (String)get_Value(COLUMNNAME_EspecialidadDoctor);
	}

	/** EstadoRecuperacion AD_Reference_ID=2000021 */
	public static final int ESTADORECUPERACION_AD_Reference_ID=2000021;
	/** F = f */
	public static final String ESTADORECUPERACION_F = "f";
	/** M = m */
	public static final String ESTADORECUPERACION_M = "m";
	/** Set EstadoRecuperacion.
		@param EstadoRecuperacion EstadoRecuperacion	  */
	public void setEstadoRecuperacion (String EstadoRecuperacion)
	{

		set_Value (COLUMNNAME_EstadoRecuperacion, EstadoRecuperacion);
	}

	/** Get EstadoRecuperacion.
		@return EstadoRecuperacion	  */
	public String getEstadoRecuperacion () 
	{
		return (String)get_Value(COLUMNNAME_EstadoRecuperacion);
	}

	/** Set fechadevengo.
		@param fechadevengo fechadevengo	  */
	public void setfechadevengo (Timestamp fechadevengo)
	{
		set_Value (COLUMNNAME_fechadevengo, fechadevengo);
	}

	/** Get fechadevengo.
		@return fechadevengo	  */
	public Timestamp getfechadevengo () 
	{
		return (Timestamp)get_Value(COLUMNNAME_fechadevengo);
	}

	/** Set fechaegreso.
		@param fechaegreso fechaegreso	  */
	public void setfechaegreso (Timestamp fechaegreso)
	{
		set_Value (COLUMNNAME_fechaegreso, fechaegreso);
	}

	/** Get fechaegreso.
		@return fechaegreso	  */
	public Timestamp getfechaegreso () 
	{
		return (Timestamp)get_Value(COLUMNNAME_fechaegreso);
	}

	/** Set Folio.
		@param Folio Folio	  */
	public void setFolio (String Folio)
	{
		set_Value (COLUMNNAME_Folio, Folio);
	}

	/** Get Folio.
		@return Folio	  */
	public String getFolio () 
	{
		return (String)get_Value(COLUMNNAME_Folio);
	}

	/** Set IsContinuity.
		@param IsContinuity IsContinuity	  */
	public void setIsContinuity (boolean IsContinuity)
	{
		set_Value (COLUMNNAME_IsContinuity, Boolean.valueOf(IsContinuity));
	}

	/** Get IsContinuity.
		@return IsContinuity	  */
	public boolean isContinuity () 
	{
		Object oo = get_Value(COLUMNNAME_IsContinuity);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set LicensedAmount.
		@param LicensedAmount LicensedAmount	  */
	public void setLicensedAmount (BigDecimal LicensedAmount)
	{
		set_Value (COLUMNNAME_LicensedAmount, LicensedAmount);
	}

	/** Get LicensedAmount.
		@return LicensedAmount	  */
	public BigDecimal getLicensedAmount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LicensedAmount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** LicenseStatus AD_Reference_ID=2000010 */
	public static final int LICENSESTATUS_AD_Reference_ID=2000010;
	/** Aceptada = ACT */
	public static final String LICENSESTATUS_Aceptada = "ACT";
	/** Segundo Rechazo = 2RE */
	public static final String LICENSESTATUS_SegundoRechazo = "2RE";
	/** Tercer Rechazo = 3RE */
	public static final String LICENSESTATUS_TercerRechazo = "3RE";
	/** Cuarto Rechazo = 4RE */
	public static final String LICENSESTATUS_CuartoRechazo = "4RE";
	/** Quinto Rechazo = 5RE */
	public static final String LICENSESTATUS_QuintoRechazo = "5RE";
	/** Set LicenseStatus.
		@param LicenseStatus LicenseStatus	  */
	public void setLicenseStatus (String LicenseStatus)
	{

		set_Value (COLUMNNAME_LicenseStatus, LicenseStatus);
	}

	/** Get LicenseStatus.
		@return LicenseStatus	  */
	public String getLicenseStatus () 
	{
		return (String)get_Value(COLUMNNAME_LicenseStatus);
	}

	/** LicenseType AD_Reference_ID=2000012 */
	public static final int LICENSETYPE_AD_Reference_ID=2000012;
	/** 1: Enfermedad o Accidente ComÃºn = 1E */
	public static final String LICENSETYPE_1EnfermedadOAccidenteComÃºn = "1E";
	/** 2: Medicina Preventiva = 2M */
	public static final String LICENSETYPE_2MedicinaPreventiva = "2M";
	/** 3: Pre y Post Natal = 3P */
	public static final String LICENSETYPE_3PreYPostNatal = "3P";
	/** 4: Enfermedad Grave del NiÃ±o Menor del AÃ±o = 4E */
	public static final String LICENSETYPE_4EnfermedadGraveDelNiÃOMenorDelAÃO = "4E";
	/** 5: Accidente del Trabajo o del Trayecto = 5A */
	public static final String LICENSETYPE_5AccidenteDelTrabajoODelTrayecto = "5A";
	/** 6: Enfermedad Profesional = 6E */
	public static final String LICENSETYPE_6EnfermedadProfesional = "6E";
	/** 7: PatologÃ­as del Embarazo = 7P */
	public static final String LICENSETYPE_7PatologÃ­asDelEmbarazo = "7P";
	/** Maternal = MT */
	public static final String LICENSETYPE_Maternal = "MT";
	/** Simple = SP */
	public static final String LICENSETYPE_Simple = "SP";
	/** Set LicenseType.
		@param LicenseType LicenseType	  */
	public void setLicenseType (String LicenseType)
	{

		set_Value (COLUMNNAME_LicenseType, LicenseType);
	}

	/** Get LicenseType.
		@return LicenseType	  */
	public String getLicenseType () 
	{
		return (String)get_Value(COLUMNNAME_LicenseType);
	}

	/** month AD_Reference_ID=2000013 */
	public static final int MONTH_AD_Reference_ID=2000013;
	/** Abril = ABR */
	public static final String MONTH_Abril = "ABR";
	/** Agosto = AGO */
	public static final String MONTH_Agosto = "AGO";
	/** Diciembre = DIC */
	public static final String MONTH_Diciembre = "DIC";
	/** Enero = ENE */
	public static final String MONTH_Enero = "ENE";
	/** Febrero = FEB */
	public static final String MONTH_Febrero = "FEB";
	/** Julio = JUL */
	public static final String MONTH_Julio = "JUL";
	/** Junio = JUN */
	public static final String MONTH_Junio = "JUN";
	/** Marzo = MAR */
	public static final String MONTH_Marzo = "MAR";
	/** Mayo = MAY */
	public static final String MONTH_Mayo = "MAY";
	/** Noviembre = NOV */
	public static final String MONTH_Noviembre = "NOV";
	/** Octubre = OCT */
	public static final String MONTH_Octubre = "OCT";
	/** Septiembre = SEP */
	public static final String MONTH_Septiembre = "SEP";
	/** Set month.
		@param month month	  */
	public void setmonth (String month)
	{

		set_Value (COLUMNNAME_month, month);
	}

	/** Get month.
		@return month	  */
	public String getmonth () 
	{
		return (String)get_Value(COLUMNNAME_month);
	}

	/** Set MontoAFP.
		@param MontoAFP MontoAFP	  */
	public void setMontoAFP (BigDecimal MontoAFP)
	{
		set_Value (COLUMNNAME_MontoAFP, MontoAFP);
	}

	/** Get MontoAFP.
		@return MontoAFP	  */
	public BigDecimal getMontoAFP () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MontoAFP);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set MontoSalud.
		@param MontoSalud MontoSalud	  */
	public void setMontoSalud (BigDecimal MontoSalud)
	{
		set_Value (COLUMNNAME_MontoSalud, MontoSalud);
	}

	/** Get MontoSalud.
		@return MontoSalud	  */
	public BigDecimal getMontoSalud () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_MontoSalud);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** PaymentStatus AD_Reference_ID=2000009 */
	public static final int PAYMENTSTATUS_AD_Reference_ID=2000009;
	/** RecepciÃ³n Conforme = RPC */
	public static final String PAYMENTSTATUS_RecepciÃNConforme = "RPC";
	/** Solicitud Cobra a ISAPRE = SCI */
	public static final String PAYMENTSTATUS_SolicitudCobraAISAPRE = "SCI";
	/** Sin cobro = SCO */
	public static final String PAYMENTSTATUS_SinCobro = "SCO";
	/** Set PaymentStatus.
		@param PaymentStatus PaymentStatus	  */
	public void setPaymentStatus (String PaymentStatus)
	{

		set_Value (COLUMNNAME_PaymentStatus, PaymentStatus);
	}

	/** Get PaymentStatus.
		@return PaymentStatus	  */
	public String getPaymentStatus () 
	{
		return (String)get_Value(COLUMNNAME_PaymentStatus);
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

	/*public I_C_ValidCombination getProcess() throws RuntimeException
    {
		return (I_C_ValidCombination)MTable.get(getCtx(), I_C_ValidCombination.Table_Name)
			.getPO(getProcessing(), get_TrxName());	}
	*/
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

	/** Set respuesta.
		@param respuesta respuesta	  */
	public void setrespuesta (String respuesta)
	{
		set_Value (COLUMNNAME_respuesta, respuesta);
	}

	/** Get respuesta.
		@return respuesta	  */
	public String getrespuesta () 
	{
		return (String)get_Value(COLUMNNAME_respuesta);
	}

	/** Set RH_MedicalLicenses ID.
		@param RH_MedicalLicenses_ID RH_MedicalLicenses ID	  */
	public void setRH_MedicalLicenses_ID (int RH_MedicalLicenses_ID)
	{
		if (RH_MedicalLicenses_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_RH_MedicalLicenses_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_RH_MedicalLicenses_ID, Integer.valueOf(RH_MedicalLicenses_ID));
	}

	/** Get RH_MedicalLicenses ID.
		@return RH_MedicalLicenses ID	  */
	public int getRH_MedicalLicenses_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RH_MedicalLicenses_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set RutDoctor.
		@param RutDoctor RutDoctor	  */
	public void setRutDoctor (String RutDoctor)
	{
		set_Value (COLUMNNAME_RutDoctor, RutDoctor);
	}

	/** Get RutDoctor.
		@return RutDoctor	  */
	public String getRutDoctor () 
	{
		return (String)get_Value(COLUMNNAME_RutDoctor);
	}

	/** Set subsidioderecho.
		@param subsidioderecho subsidioderecho	  */
	public void setsubsidioderecho (boolean subsidioderecho)
	{
		set_Value (COLUMNNAME_subsidioderecho, Boolean.valueOf(subsidioderecho));
	}

	/** Get subsidioderecho.
		@return subsidioderecho	  */
	public boolean issubsidioderecho () 
	{
		Object oo = get_Value(COLUMNNAME_subsidioderecho);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	public org.compiere.model.I_C_BPartner getSuplen() throws RuntimeException
    {
		return (org.compiere.model.I_C_BPartner)MTable.get(getCtx(), org.compiere.model.I_C_BPartner.Table_Name)
			.getPO(getSuplencia(), get_TrxName());	}

	/** Set Suplencia.
		@param Suplencia Suplencia	  */
	public void setSuplencia (int Suplencia)
	{
		set_Value (COLUMNNAME_Suplencia, Integer.valueOf(Suplencia));
	}

	/** Get Suplencia.
		@return Suplencia	  */
	public int getSuplencia () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Suplencia);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** TimeOfTheDay AD_Reference_ID=2000014 */
	public static final int TIMEOFTHEDAY_AD_Reference_ID=2000014;
	/** Mañana = MAN */
	public static final String TIMEOFTHEDAY_Ma�ana = "MAN";
	/** Noche = NOC */
	public static final String TIMEOFTHEDAY_Noche = "NOC";
	/** Tarde = TAR */
	public static final String TIMEOFTHEDAY_Tarde = "TAR";
	/** Set TimeOfTheDay.
		@param TimeOfTheDay TimeOfTheDay	  */
	public void setTimeOfTheDay (String TimeOfTheDay)
	{

		set_Value (COLUMNNAME_TimeOfTheDay, TimeOfTheDay);
	}

	/** Get TimeOfTheDay.
		@return TimeOfTheDay	  */
	public String getTimeOfTheDay () 
	{
		return (String)get_Value(COLUMNNAME_TimeOfTheDay);
	}

	/** Set TotalRecuperar.
		@param TotalRecuperar TotalRecuperar	  */
	public void setTotalRecuperar (BigDecimal TotalRecuperar)
	{
		set_Value (COLUMNNAME_TotalRecuperar, TotalRecuperar);
	}

	/** Get TotalRecuperar.
		@return TotalRecuperar	  */
	public BigDecimal getTotalRecuperar () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TotalRecuperar);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Type AD_Reference_ID=101 */
	public static final int TYPE_AD_Reference_ID=101;
	/** SQL = S */
	public static final String TYPE_SQL = "S";
	/** Java Language = J */
	public static final String TYPE_JavaLanguage = "J";
	/** Java Script = E */
	public static final String TYPE_JavaScript = "E";
	/** Set Type.
		@param Type 
		Type of Validation (SQL, Java Script, Java Language)
	  */
	public void setType (String Type)
	{

		set_Value (COLUMNNAME_Type, Type);
	}

	/** Get Type.
		@return Type of Validation (SQL, Java Script, Java Language)
	  */
	public String getType () 
	{
		return (String)get_Value(COLUMNNAME_Type);
	}
}