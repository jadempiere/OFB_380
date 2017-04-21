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
import java.sql.Timestamp;
import java.util.Properties;

/** Generated Model for C_CalendarCOPESALine
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0 - $Id$ */
public class X_C_CalendarCOPESALine extends PO implements I_C_CalendarCOPESALine, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20160422L;

    /** Standard Constructor */
    public X_C_CalendarCOPESALine (Properties ctx, int C_CalendarCOPESALine_ID, String trxName)
    {
      super (ctx, C_CalendarCOPESALine_ID, trxName);
      /** if (C_CalendarCOPESALine_ID == 0)
        {
			setC_CalendarCOPESA_ID (0);
			setC_CalendarCOPESALine_ID (0);
        } */
    }

    /** Load Constructor */
    public X_C_CalendarCOPESALine (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_C_CalendarCOPESALine[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_CalendarCOPESA getC_CalendarCOPESA() throws RuntimeException
    {
		return (I_C_CalendarCOPESA)MTable.get(getCtx(), I_C_CalendarCOPESA.Table_Name)
			.getPO(getC_CalendarCOPESA_ID(), get_TrxName());	}

	/** Set C_CalendarCOPESA ID.
		@param C_CalendarCOPESA_ID C_CalendarCOPESA ID	  */
	public void setC_CalendarCOPESA_ID (int C_CalendarCOPESA_ID)
	{
		if (C_CalendarCOPESA_ID < 1) 
			set_Value (COLUMNNAME_C_CalendarCOPESA_ID, null);
		else 
			set_Value (COLUMNNAME_C_CalendarCOPESA_ID, Integer.valueOf(C_CalendarCOPESA_ID));
	}

	/** Get C_CalendarCOPESA ID.
		@return C_CalendarCOPESA ID	  */
	public int getC_CalendarCOPESA_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_CalendarCOPESA_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_CalendarCOPESALine ID.
		@param C_CalendarCOPESALine_ID C_CalendarCOPESALine ID	  */
	public void setC_CalendarCOPESALine_ID (int C_CalendarCOPESALine_ID)
	{
		if (C_CalendarCOPESALine_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_CalendarCOPESALine_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_CalendarCOPESALine_ID, Integer.valueOf(C_CalendarCOPESALine_ID));
	}

	/** Get C_CalendarCOPESALine ID.
		@return C_CalendarCOPESALine ID	  */
	public int getC_CalendarCOPESALine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_CalendarCOPESALine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** DayName AD_Reference_ID=2000002 */
	public static final int DAYNAME_AD_Reference_ID=2000002;
	/** Lunes = 01 */
	public static final String DAYNAME_Lunes = "01";
	/** Martes = 02 */
	public static final String DAYNAME_Martes = "02";
	/** Miercoles = 03 */
	public static final String DAYNAME_Miercoles = "03";
	/** Jueves = 04 */
	public static final String DAYNAME_Jueves = "04";
	/** Viernes = 05 */
	public static final String DAYNAME_Viernes = "05";
	/** Sabado = 06 */
	public static final String DAYNAME_Sabado = "06";
	/** Domingo = 07 */
	public static final String DAYNAME_Domingo = "07";
	/** Set DayName.
		@param DayName DayName	  */
	public void setDayName (String DayName)
	{

		set_Value (COLUMNNAME_DayName, DayName);
	}

	/** Get DayName.
		@return DayName	  */
	public String getDayName () 
	{
		return (String)get_Value(COLUMNNAME_DayName);
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

	/** Set IsShip.
		@param IsShip IsShip	  */
	public void setIsShip (boolean IsShip)
	{
		set_Value (COLUMNNAME_IsShip, Boolean.valueOf(IsShip));
	}

	/** Get IsShip.
		@return IsShip	  */
	public boolean isShip () 
	{
		Object oo = get_Value(COLUMNNAME_IsShip);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}
}