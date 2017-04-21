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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.adempiere.exceptions.DBException;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
//import org.compiere.util.DB;
import org.ofb.model.OFBForward;


/**
 * 	Generate Calendar Line COPESA
 *	
 *  @author Italo Niñoles
 */
public class ShipOrderNotProcessed extends SvrProcess
{
	/**	fecha 				*/
	private Timestamp	p_DateTrx;

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
			else if (para[i].getParameterName().equals("DateTrx"))
				p_DateTrx = para[i].getParameterAsTimestamp();
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
		if(p_DateTrx != null)
		{
			/*String Path = OFBForward.NOPauta_PathCOPESA();
			String prefix = OFBForward.NOPauta_PrefixCOPESA();*/
			//generacion nombre de archivo 
			/*DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	        
	        date1.setTime(p_DateTrx.getTime());
	        String fechaStrName = dateFormat.format(date1);*/		
			//String nameFile = Path+prefix+fechaStrName+".txt";
			//
			//generacion de parametro
			/*dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
			String fechaStr = dateFormat.format(date1);*/
			
			/*String sql = "Copy (Select CLIRUT,CLISUC,SUSFOL,SUCFOL,LINFOL,REPRUT,REPSUC,REPTIL,REPRAZ,REPAPA,REPAMA,REPSEX,REPDIR1,REPDIR2,REPNUM,REPDEP,REPBLOCK,REPVILLA,REPCOM,REPCIU,REPSECZON, " +
					" REPREG,REPPAIS,REPIDDIR,REPFON,REPFAX,REPOBS,SUSPRO,NOMPRO,EDIPRO,TIPCON,SUSEJE,SUSINI,SUSTER,TABVAL,C_ORDERREF_ID,CANAL_VENTA,TIPO_CAMBIO " +
					" from RVOFB_OrderShipCalendarProcess where IsShipped = 'N' AND DateTrx = '"+fechaStr+"')To '"+nameFile+"' With CSV DELIMITER ';';";
			DB.executeUpdate(sql,get_TrxName());*/
			
			//ininoles nueva forma de generacion de novedades con funciones de DB
			//generacion de parametro
			Date date1 = new Date();
			date1.setTime(p_DateTrx.getTime());
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String fechaStr = dateFormat.format(date1);			
			String sql = "select copesa_dump_novedades('"+fechaStr+"'::timestamp)";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, get_TrxName());				
				rs = pstmt.executeQuery();				
			}
			catch (SQLException e)
			{
				throw new DBException(e, sql);
			}
			finally
			{
				DB.close(rs, pstmt);
				rs = null; pstmt = null;
			}
		}
		return "generado ";
	}	//	doIt

}	//	BPGroupAcctCopy

