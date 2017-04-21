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

/** Generated Model for C_OrderShipCalendar
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0 - $Id$ */
public class X_C_OrderShipCalendar extends PO implements I_C_OrderShipCalendar, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20160929L;

    /** Standard Constructor */
    public X_C_OrderShipCalendar (Properties ctx, int C_OrderShipCalendar_ID, String trxName)
    {
      super (ctx, C_OrderShipCalendar_ID, trxName);
      /** if (C_OrderShipCalendar_ID == 0)
        {
			setC_OrderShipCalendar_ID (0);
        } */
    }

    /** Load Constructor */
    public X_C_OrderShipCalendar (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_C_OrderShipCalendar[")
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

	public I_C_CalendarCOPESALine getC_CalendarCOPESALine() throws RuntimeException
    {
		return (I_C_CalendarCOPESALine)MTable.get(getCtx(), I_C_CalendarCOPESALine.Table_Name)
			.getPO(getC_CalendarCOPESALine_ID(), get_TrxName());	}

	/** Set C_CalendarCOPESALine ID.
		@param C_CalendarCOPESALine_ID C_CalendarCOPESALine ID	  */
	public void setC_CalendarCOPESALine_ID (int C_CalendarCOPESALine_ID)
	{
		if (C_CalendarCOPESALine_ID < 1) 
			set_Value (COLUMNNAME_C_CalendarCOPESALine_ID, null);
		else 
			set_Value (COLUMNNAME_C_CalendarCOPESALine_ID, Integer.valueOf(C_CalendarCOPESALine_ID));
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

	public org.compiere.model.I_C_OrderLine getC_OrderLine() throws RuntimeException
    {
		return (org.compiere.model.I_C_OrderLine)MTable.get(getCtx(), org.compiere.model.I_C_OrderLine.Table_Name)
			.getPO(getC_OrderLine_ID(), get_TrxName());	}

	/** Set Sales Order Line.
		@param C_OrderLine_ID 
		Sales Order Line
	  */
	public void setC_OrderLine_ID (int C_OrderLine_ID)
	{
		if (C_OrderLine_ID < 1) 
			set_Value (COLUMNNAME_C_OrderLine_ID, null);
		else 
			set_Value (COLUMNNAME_C_OrderLine_ID, Integer.valueOf(C_OrderLine_ID));
	}

	/** Get Sales Order Line.
		@return Sales Order Line
	  */
	public int getC_OrderLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_OrderLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_OrderShipCalendar ID.
		@param C_OrderShipCalendar_ID C_OrderShipCalendar ID	  */
	public void setC_OrderShipCalendar_ID (int C_OrderShipCalendar_ID)
	{
		if (C_OrderShipCalendar_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_OrderShipCalendar_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_OrderShipCalendar_ID, Integer.valueOf(C_OrderShipCalendar_ID));
	}

	/** Get C_OrderShipCalendar ID.
		@return C_OrderShipCalendar ID	  */
	public int getC_OrderShipCalendar_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_OrderShipCalendar_ID);
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

	/** Set IsShipped.
		@param IsShipped IsShipped	  */
	public void setIsShipped (boolean IsShipped)
	{
		set_Value (COLUMNNAME_IsShipped, Boolean.valueOf(IsShipped));
	}

	/** Get IsShipped.
		@return IsShipped	  */
	public boolean isShipped () 
	{
		Object oo = get_Value(COLUMNNAME_IsShipped);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}
}