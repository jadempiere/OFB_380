/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.ofb.process;

import java.sql.*;
import java.util.logging.*;

import org.compiere.model.*;
import org.compiere.process.SvrProcess;
import org.compiere.util.*;

/**
 *		
 *  @author Fabian Aguilar faaguilar
 *  @version $Id: CreateCombinations.java,v 1.2 2008/06/12 00:51:01  $
 */
public class CreateCombinations extends SvrProcess
{
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		
		
		String sql="Select C_ElementValue_ID from C_ElementValue where ISSUMMARY='N' and AD_Client_ID="+getAD_Client_ID();
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				MElementValue account= new MElementValue(getCtx(),rs.getInt(1),get_TrxName());
				account.CreateCombination();
				account.save();
				
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		DB.executeUpdate("UPDATE C_ValidCombination "+
		" set ad_org_id=0, "+
 		" alias=(select b.value from C_ELEMENTVALUE b where b.C_ELEMENTVALUE_ID = ACCOUNT_ID), "+
		" description=(select b.name from C_ELEMENTVALUE b where b.C_ELEMENTVALUE_ID = ACCOUNT_ID)",get_TrxName());
		
		DB.executeUpdate("update C_VALIDCOMBINATION "+
		"set combination=substr(alias ||'-'||description,1,59)",get_TrxName());
		
	   return "Combinaciones Creadas";
	}	//	doIt
}	//	CopyOrder
