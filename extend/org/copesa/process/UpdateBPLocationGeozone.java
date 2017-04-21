/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
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
package org.copesa.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.model.MBPartnerLocation;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;
//import org.compiere.util.DB;


/**
 * 	Generate Calendar Line COPESA
 *	
 *  @author Italo Niñoles
 */
public class UpdateBPLocationGeozone extends SvrProcess
{
	/**	ID socio de negocio*/
	private int 		p_ID_Bpartner;
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare ()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (para[i].getParameterName().equals("C_BPartner_ID"))
				p_ID_Bpartner = para[i].getParameterAsInt();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	
	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		int cant = 0;
			
		String sql = "SELECT C_BPartner_Location_ID FROM C_BPartner_Location " +
				" WHERE IsActive = 'Y' AND C_City_ID IS NOT NULL AND AD_Client_ID = "+Env.getAD_Client_ID(getCtx());
		if(p_ID_Bpartner > 0)
			sql += " AND C_BPartner_ID = "+p_ID_Bpartner;
		
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, get_TrxName());
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				MBPartnerLocation loc = new MBPartnerLocation(getCtx(), rs.getInt("C_BPartner_Location_ID"), get_TrxName());
				int ID_Geozona = DB.getSQLValue(get_TrxName(), "SELECT MAX(gzc.C_Geozone_ID) " +
						" FROM C_GeozoneCities gzc WHERE gzc.IsActive = 'Y' " +
						" AND gzc.C_City_ID = "+loc.get_ValueAsInt("C_City_ID"));			
				if(ID_Geozona > 0)
				{
					loc.set_CustomColumn("C_Geozone_ID", ID_Geozona);
					loc.save();
					cant++;
				}
			}
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "", e);
		}
		return "@Updated@=" + cant;
	}	//	doIt

}	//	BPGroupAcctCopy

