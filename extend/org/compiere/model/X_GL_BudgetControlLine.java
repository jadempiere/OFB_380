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

/** Generated Model for GL_BudgetControlLine
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0 - $Id$ */
public class X_GL_BudgetControlLine extends PO implements I_GL_BudgetControlLine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20170120L;

    /** Standard Constructor */
    public X_GL_BudgetControlLine (Properties ctx, int GL_BudgetControlLine_ID, String trxName)
    {
      super (ctx, GL_BudgetControlLine_ID, trxName);
      /** if (GL_BudgetControlLine_ID == 0)
        {
			setGL_BudgetControl_ID (0);
			setGL_BudgetControlLine_ID (0);
        } */
    }

    /** Load Constructor */
    public X_GL_BudgetControlLine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_GL_BudgetControlLine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Amount.
		@param Amount 
		Amount in a defined currency
	  */
	public void setAmount (BigDecimal Amount)
	{
		set_Value (COLUMNNAME_Amount, Amount);
	}

	/** Get Amount.
		@return Amount in a defined currency
	  */
	public BigDecimal getAmount () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Amount);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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

	/** Set Amount3.
		@param Amount3 Amount3	  */
	public void setAmount3 (BigDecimal Amount3)
	{
		set_Value (COLUMNNAME_Amount3, Amount3);
	}

	/** Get Amount3.
		@return Amount3	  */
	public BigDecimal getAmount3 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Amount3);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public org.compiere.model.I_C_Period getC_Period() throws RuntimeException
    {
		return (org.compiere.model.I_C_Period)MTable.get(getCtx(), org.compiere.model.I_C_Period.Table_Name)
			.getPO(getC_Period_ID(), get_TrxName());	}

	/** Set Period.
		@param C_Period_ID 
		Period of the Calendar
	  */
	public void setC_Period_ID (int C_Period_ID)
	{
		if (C_Period_ID < 1) 
			set_Value (COLUMNNAME_C_Period_ID, null);
		else 
			set_Value (COLUMNNAME_C_Period_ID, Integer.valueOf(C_Period_ID));
	}

	/** Get Period.
		@return Period of the Calendar
	  */
	public int getC_Period_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Period_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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
			set_Value (COLUMNNAME_GL_BudgetControl_ID, null);
		else 
			set_Value (COLUMNNAME_GL_BudgetControl_ID, Integer.valueOf(GL_BudgetControl_ID));
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

	/** Set GL_BudgetControlLine ID.
		@param GL_BudgetControlLine_ID GL_BudgetControlLine ID	  */
	public void setGL_BudgetControlLine_ID (int GL_BudgetControlLine_ID)
	{
		if (GL_BudgetControlLine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_GL_BudgetControlLine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_GL_BudgetControlLine_ID, Integer.valueOf(GL_BudgetControlLine_ID));
	}

	/** Get GL_BudgetControlLine ID.
		@return GL_BudgetControlLine ID	  */
	public int getGL_BudgetControlLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_GL_BudgetControlLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_GL_BudgetDetail getGL_BudgetDetail() throws RuntimeException
    {
		return (I_GL_BudgetDetail)MTable.get(getCtx(), I_GL_BudgetDetail.Table_Name)
			.getPO(getGL_BudgetDetail_ID(), get_TrxName());	}

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

	/** Set Comment/Help.
		@param Help 
		Comment or Hint
	  */
	public void setHelp (String Help)
	{
		set_Value (COLUMNNAME_Help, Help);
	}

	/** Get Comment/Help.
		@return Comment or Hint
	  */
	public String getHelp () 
	{
		return (String)get_Value(COLUMNNAME_Help);
	}

	/** Set IsRequired.
		@param IsRequired IsRequired	  */
	public void setIsRequired (boolean IsRequired)
	{
		set_Value (COLUMNNAME_IsRequired, Boolean.valueOf(IsRequired));
	}

	/** Get IsRequired.
		@return IsRequired	  */
	public boolean isRequired () 
	{
		Object oo = get_Value(COLUMNNAME_IsRequired);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Mode AD_Reference_ID=2000041 */
	public static final int MODE_AD_Reference_ID=2000041;
	/** Convenio Marco = MAR */
	public static final String MODE_ConvenioMarco = "MAR";
	/** Trato Directo = DIR */
	public static final String MODE_TratoDirecto = "DIR";
	/** Compra menor a 100 UTM = MEN */
	public static final String MODE_CompraMenorA100UTM = "MEN";
	/** Compra mayor a 100 UTM = MAY */
	public static final String MODE_CompraMayorA100UTM = "MAY";
	/** Compra menor a 3 UTM = 3ME */
	public static final String MODE_CompraMenorA3UTM = "3ME";
	/** Licitaciones Privadas = LIC */
	public static final String MODE_LicitacionesPrivadas = "LIC";
	/** Licitaciones Públicas = LIB */
	public static final String MODE_LicitacionesPúblicas = "LIB";
	/** Contrato de Arrastre = CAR */
	public static final String MODE_ContratoDeArrastre = "CAR";
	/** Set Mode.
		@param Mode Mode	  */
	public void setMode (String Mode)
	{

		set_Value (COLUMNNAME_Mode, Mode);
	}

	/** Get Mode.
		@return Mode	  */
	public String getMode () 
	{
		return (String)get_Value(COLUMNNAME_Mode);
	}

	public org.compiere.model.I_M_Product getM_Product() throws RuntimeException
    {
		return (org.compiere.model.I_M_Product)MTable.get(getCtx(), org.compiere.model.I_M_Product.Table_Name)
			.getPO(getM_Product_ID(), get_TrxName());	}

	/** Set Product.
		@param M_Product_ID 
		Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID)
	{
		if (M_Product_ID < 1) 
			set_Value (COLUMNNAME_M_Product_ID, null);
		else 
			set_Value (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public int getM_Product_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Program AD_Reference_ID=2000053 */
	public static final int PROGRAM_AD_Reference_ID=2000053;
	/** 1 = 1 */
	public static final String PROGRAM_1 = "1";
	/** 2 = 2 */
	public static final String PROGRAM_2 = "2";
	/** Set Program.
		@param Program Program	  */
	public void setProgram (String Program)
	{

		set_Value (COLUMNNAME_Program, Program);
	}

	/** Get Program.
		@return Program	  */
	public String getProgram () 
	{
		return (String)get_Value(COLUMNNAME_Program);
	}

	/** Set Quantity.
		@param Qty 
		Quantity
	  */
	public void setQty (BigDecimal Qty)
	{
		set_Value (COLUMNNAME_Qty, Qty);
	}

	/** Get Quantity.
		@return Quantity
	  */
	public BigDecimal getQty () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Qty);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SQLAmountDiff.
		@param SQLAmountDiff SQLAmountDiff	  */
	public void setSQLAmountDiff (BigDecimal SQLAmountDiff)
	{
		throw new IllegalArgumentException ("SQLAmountDiff is virtual column");	}

	/** Get SQLAmountDiff.
		@return SQLAmountDiff	  */
	public BigDecimal getSQLAmountDiff () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SQLAmountDiff);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SQLAmountDiff2.
		@param SQLAmountDiff2 SQLAmountDiff2	  */
	public void setSQLAmountDiff2 (BigDecimal SQLAmountDiff2)
	{
		throw new IllegalArgumentException ("SQLAmountDiff2 is virtual column");	}

	/** Get SQLAmountDiff2.
		@return SQLAmountDiff2	  */
	public BigDecimal getSQLAmountDiff2 () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SQLAmountDiff2);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set SQLProductNameOrg.
		@param SQLProductNameOrg SQLProductNameOrg	  */
	public void setSQLProductNameOrg (String SQLProductNameOrg)
	{
		throw new IllegalArgumentException ("SQLProductNameOrg is virtual column");	}

	/** Get SQLProductNameOrg.
		@return SQLProductNameOrg	  */
	public String getSQLProductNameOrg () 
	{
		return (String)get_Value(COLUMNNAME_SQLProductNameOrg);
	}
}