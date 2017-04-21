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
import java.util.Properties;
import org.compiere.util.Env;

/** Generated Model for GL_BudgetDetail
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0 - $Id$ */
public class X_GL_BudgetDetail extends PO implements I_GL_BudgetDetail, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170120L;

    /** Standard Constructor */
    public X_GL_BudgetDetail (Properties ctx, int GL_BudgetDetail_ID, String trxName)
    {
      super (ctx, GL_BudgetDetail_ID, trxName);
      /** if (GL_BudgetDetail_ID == 0)
        {
			setGL_BudgetDetail_ID (0);
        } */
    }

    /** Load Constructor */
    public X_GL_BudgetDetail (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_GL_BudgetDetail[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set BudgetAmt.
		@param BudgetAmt BudgetAmt	  */
	public void setBudgetAmt (BigDecimal BudgetAmt)
	{
		throw new IllegalArgumentException ("BudgetAmt is virtual column");	}

	/** Get BudgetAmt.
		@return BudgetAmt	  */
	public BigDecimal getBudgetAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_BudgetAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set BudgetStart.
		@param BudgetStart BudgetStart	  */
	public void setBudgetStart (BigDecimal BudgetStart)
	{
		set_Value (COLUMNNAME_BudgetStart, BudgetStart);
	}

	/** Get BudgetStart.
		@return BudgetStart	  */
	public BigDecimal getBudgetStart () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_BudgetStart);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set ChangeAmt.
		@param ChangeAmt ChangeAmt	  */
	public void setChangeAmt (BigDecimal ChangeAmt)
	{
		set_Value (COLUMNNAME_ChangeAmt, ChangeAmt);
	}

	/** Get ChangeAmt.
		@return ChangeAmt	  */
	public BigDecimal getChangeAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ChangeAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_C_ValidCombination getC_ValidCombination() throws RuntimeException
    {
		return (org.compiere.model.I_C_ValidCombination)MTable.get(getCtx(), org.compiere.model.I_C_ValidCombination.Table_Name)
			.getPO(getC_ValidCombination_ID(), get_TrxName());	}

	/** Set Combination.
		@param C_ValidCombination_ID 
		Valid Account Combination
	  */
	public void setC_ValidCombination_ID (int C_ValidCombination_ID)
	{
		if (C_ValidCombination_ID < 1) 
			set_Value (COLUMNNAME_C_ValidCombination_ID, null);
		else 
			set_Value (COLUMNNAME_C_ValidCombination_ID, Integer.valueOf(C_ValidCombination_ID));
	}

	/** Get Combination.
		@return Valid Account Combination
	  */
	public int getC_ValidCombination_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ValidCombination_ID);
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

	public org.compiere.model.I_GL_BudgetControl getGL_BudgetControl() throws RuntimeException
    {
		return (org.compiere.model.I_GL_BudgetControl)MTable.get(getCtx(), org.compiere.model.I_GL_BudgetControl.Table_Name)
			.getPO(getGL_BudgetControl_ID(), get_TrxName());	}

	/** Set Budget Control.
		@param GL_BudgetControl_ID 
		Budget Control
	  */
	public void setGL_BudgetControl_ID (int GL_BudgetControl_ID)
	{
		if (GL_BudgetControl_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_GL_BudgetControl_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_GL_BudgetControl_ID, Integer.valueOf(GL_BudgetControl_ID));
	}

	/** Get Budget Control.
		@return Budget Control
	  */
	public int getGL_BudgetControl_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_GL_BudgetControl_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set GL_BudgetDetail ID.
		@param GL_BudgetDetail_ID GL_BudgetDetail ID	  */
	public void setGL_BudgetDetail_ID (int GL_BudgetDetail_ID)
	{
		if (GL_BudgetDetail_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_GL_BudgetDetail_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_GL_BudgetDetail_ID, Integer.valueOf(GL_BudgetDetail_ID));
	}

	/** Get GL_BudgetDetail ID.
		@return GL_BudgetDetail ID	  */
	public int getGL_BudgetDetail_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_GL_BudgetDetail_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Line Total.
		@param LineTotalAmt 
		Total line amount incl. Tax
	  */
	public void setLineTotalAmt (BigDecimal LineTotalAmt)
	{
		throw new IllegalArgumentException ("LineTotalAmt is virtual column");	}

	/** Get Line Total.
		@return Total line amount incl. Tax
	  */
	public BigDecimal getLineTotalAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_LineTotalAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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
}