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

import java.sql.ResultSet;
import java.util.Properties;
import org.compiere.util.KeyNamePair;

/** Generated Model for C_BPartnerBenefits
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0 - $Id$ */
public class X_C_BPartnerBenefits extends PO implements I_C_BPartnerBenefits, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20160504L;

    /** Standard Constructor */
    public X_C_BPartnerBenefits (Properties ctx, int C_BPartnerBenefits_ID, String trxName)
    {
      super (ctx, C_BPartnerBenefits_ID, trxName);
      /** if (C_BPartnerBenefits_ID == 0)
        {
			setC_BPartner_ID (0);
			setC_BPartnerBenefits_ID (0);
        } */
    }

    /** Load Constructor */
    public X_C_BPartnerBenefits (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_C_BPartnerBenefits[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	/** Benefits AD_Reference_ID=2000005 */
	public static final int BENEFITS_AD_Reference_ID=2000005;
	/** Club La Tercera = TER */
	public static final String BENEFITS_ClubLaTercera = "TER";
	/** Club Paula = PAU */
	public static final String BENEFITS_ClubPaula = "PAU";
	/** Set Benefits.
		@param Benefits 
		List of benefits
	  */
	public void setBenefits (String Benefits)
	{

		set_Value (COLUMNNAME_Benefits, Benefits);
	}

	/** Get Benefits.
		@return List of benefits
	  */
	public String getBenefits () 
	{
		return (String)get_Value(COLUMNNAME_Benefits);
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

	/** Set C_BPartnerBenefits ID.
		@param C_BPartnerBenefits_ID C_BPartnerBenefits ID	  */
	public void setC_BPartnerBenefits_ID (int C_BPartnerBenefits_ID)
	{
		if (C_BPartnerBenefits_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_BPartnerBenefits_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_BPartnerBenefits_ID, Integer.valueOf(C_BPartnerBenefits_ID));
	}

	/** Get C_BPartnerBenefits ID.
		@return C_BPartnerBenefits ID	  */
	public int getC_BPartnerBenefits_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartnerBenefits_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
    }
}