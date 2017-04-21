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

/** Generated Model for R_RequestLineAction
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0 - $Id$ */
public class X_R_RequestLineAction extends PO implements I_R_RequestLineAction, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20161117L;

    /** Standard Constructor */
    public X_R_RequestLineAction (Properties ctx, int R_RequestLineAction_ID, String trxName)
    {
      super (ctx, R_RequestLineAction_ID, trxName);
      /** if (R_RequestLineAction_ID == 0)
        {
			setR_RequestLineAction_ID (0);
        } */
    }

    /** Load Constructor */
    public X_R_RequestLineAction (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 7 - System - Client - Org 
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
      StringBuffer sb = new StringBuffer ("X_R_RequestLineAction[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_OrderShipCalendar getC_OrderShipCalendar() throws RuntimeException
    {
		return (I_C_OrderShipCalendar)MTable.get(getCtx(), I_C_OrderShipCalendar.Table_Name)
			.getPO(getC_OrderShipCalendar_ID(), get_TrxName());	}

	/** Set C_OrderShipCalendar ID.
		@param C_OrderShipCalendar_ID C_OrderShipCalendar ID	  */
	public void setC_OrderShipCalendar_ID (int C_OrderShipCalendar_ID)
	{
		if (C_OrderShipCalendar_ID < 1) 
			set_Value (COLUMNNAME_C_OrderShipCalendar_ID, null);
		else 
			set_Value (COLUMNNAME_C_OrderShipCalendar_ID, Integer.valueOf(C_OrderShipCalendar_ID));
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

	public org.compiere.model.I_R_Request getR_Request() throws RuntimeException
    {
		return (org.compiere.model.I_R_Request)MTable.get(getCtx(), org.compiere.model.I_R_Request.Table_Name)
			.getPO(getR_Request_ID(), get_TrxName());	}

	/** Set Request.
		@param R_Request_ID 
		Request from a Business Partner or Prospect
	  */
	public void setR_Request_ID (int R_Request_ID)
	{
		if (R_Request_ID < 1) 
			set_Value (COLUMNNAME_R_Request_ID, null);
		else 
			set_Value (COLUMNNAME_R_Request_ID, Integer.valueOf(R_Request_ID));
	}

	/** Get Request.
		@return Request from a Business Partner or Prospect
	  */
	public int getR_Request_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_R_Request_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_R_RequestLine getR_RequestLine() throws RuntimeException
    {
		return (I_R_RequestLine)MTable.get(getCtx(), I_R_RequestLine.Table_Name)
			.getPO(getR_RequestLine_ID(), get_TrxName());	}

	/** Set Request ID.
		@param R_RequestLine_ID Request ID	  */
	public void setR_RequestLine_ID (int R_RequestLine_ID)
	{
		if (R_RequestLine_ID < 1) 
			set_Value (COLUMNNAME_R_RequestLine_ID, null);
		else 
			set_Value (COLUMNNAME_R_RequestLine_ID, Integer.valueOf(R_RequestLine_ID));
	}

	/** Get Request ID.
		@return Request ID	  */
	public int getR_RequestLine_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_R_RequestLine_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set R_RequestLineAction ID.
		@param R_RequestLineAction_ID R_RequestLineAction ID	  */
	public void setR_RequestLineAction_ID (int R_RequestLineAction_ID)
	{
		if (R_RequestLineAction_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_R_RequestLineAction_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_R_RequestLineAction_ID, Integer.valueOf(R_RequestLineAction_ID));
	}

	/** Get R_RequestLineAction ID.
		@return R_RequestLineAction ID	  */
	public int getR_RequestLineAction_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_R_RequestLineAction_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set TypeAcction.
		@param TypeAcction TypeAcction	  */
	public void setTypeAcction (int TypeAcction)
	{
		set_Value (COLUMNNAME_TypeAcction, Integer.valueOf(TypeAcction));
	}

	/** Get TypeAcction.
		@return TypeAcction	  */
	public int getTypeAcction () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_TypeAcction);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}