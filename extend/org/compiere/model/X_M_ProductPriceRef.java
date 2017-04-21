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

/** Generated Model for M_ProductPriceRef
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0 - $Id$ */
public class X_M_ProductPriceRef extends PO implements I_M_ProductPriceRef, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20160422L;

    /** Standard Constructor */
    public X_M_ProductPriceRef (Properties ctx, int M_ProductPriceRef_ID, String trxName)
    {
      super (ctx, M_ProductPriceRef_ID, trxName);
      /** if (M_ProductPriceRef_ID == 0)
        {
			setM_PriceList_Version_ID (0);
			setM_Product_ID (0);
			setM_ProductPriceRef_ID (0);
			setm_productref_id (0);
			setPrice (Env.ZERO);
        } */
    }

    /** Load Constructor */
    public X_M_ProductPriceRef (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_M_ProductPriceRef[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Levels AD_Reference_ID=2000000 */
	public static final int LEVELS_AD_Reference_ID=2000000;
	/** Nivel 2 = 02 */
	public static final String LEVELS_Nivel2 = "02";
	/** Nivel 3 = 03 */
	public static final String LEVELS_Nivel3 = "03";
	/** Nivel 4 = 04 */
	public static final String LEVELS_Nivel4 = "04";
	/** Nivel 5 = 05 */
	public static final String LEVELS_Nivel5 = "05";
	/** Set Levels.
		@param Levels Levels	  */
	public void setLevels (String Levels)
	{

		set_Value (COLUMNNAME_Levels, Levels);
	}

	/** Get Levels.
		@return Levels	  */
	public String getLevels () 
	{
		return (String)get_Value(COLUMNNAME_Levels);
	}

	public org.compiere.model.I_M_PriceList_Version getM_PriceList_Version() throws RuntimeException
    {
		return (org.compiere.model.I_M_PriceList_Version)MTable.get(getCtx(), org.compiere.model.I_M_PriceList_Version.Table_Name)
			.getPO(getM_PriceList_Version_ID(), get_TrxName());	}

	/** Set Price List Version.
		@param M_PriceList_Version_ID 
		Identifies a unique instance of a Price List
	  */
	public void setM_PriceList_Version_ID (int M_PriceList_Version_ID)
	{
		if (M_PriceList_Version_ID < 1) 
			set_Value (COLUMNNAME_M_PriceList_Version_ID, null);
		else 
			set_Value (COLUMNNAME_M_PriceList_Version_ID, Integer.valueOf(M_PriceList_Version_ID));
	}

	/** Get Price List Version.
		@return Identifies a unique instance of a Price List
	  */
	public int getM_PriceList_Version_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_PriceList_Version_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set M_ProductPriceRef ID.
		@param M_ProductPriceRef_ID M_ProductPriceRef ID	  */
	public void setM_ProductPriceRef_ID (int M_ProductPriceRef_ID)
	{
		if (M_ProductPriceRef_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_ProductPriceRef_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_ProductPriceRef_ID, Integer.valueOf(M_ProductPriceRef_ID));
	}

	/** Get M_ProductPriceRef ID.
		@return M_ProductPriceRef ID	  */
	public int getM_ProductPriceRef_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_ProductPriceRef_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_M_Product getm_productref() throws RuntimeException
    {
		return (org.compiere.model.I_M_Product)MTable.get(getCtx(), org.compiere.model.I_M_Product.Table_Name)
			.getPO(getm_productref_id(), get_TrxName());	}

	/** Set m_productref_id.
		@param m_productref_id m_productref_id	  */
	public void setm_productref_id (int m_productref_id)
	{
		set_Value (COLUMNNAME_m_productref_id, Integer.valueOf(m_productref_id));
	}

	/** Get m_productref_id.
		@return m_productref_id	  */
	public int getm_productref_id () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_m_productref_id);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.compiere.model.I_M_Product getm_productref2() throws RuntimeException
    {
		return (org.compiere.model.I_M_Product)MTable.get(getCtx(), org.compiere.model.I_M_Product.Table_Name)
			.getPO(getm_productref2_id(), get_TrxName());	}

	/** Set m_productref2_id.
		@param m_productref2_id m_productref2_id	  */
	public void setm_productref2_id (int m_productref2_id)
	{
		set_Value (COLUMNNAME_m_productref2_id, Integer.valueOf(m_productref2_id));
	}

	/** Get m_productref2_id.
		@return m_productref2_id	  */
	public int getm_productref2_id () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_m_productref2_id);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Price.
		@param Price 
		Price
	  */
	public void setPrice (BigDecimal Price)
	{
		set_Value (COLUMNNAME_Price, Price);
	}

	/** Get Price.
		@return Price
	  */
	public BigDecimal getPrice () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Price);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}
}