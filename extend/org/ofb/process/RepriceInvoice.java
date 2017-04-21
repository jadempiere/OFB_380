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
package org.ofb.process;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.compiere.model.MConversionRate;
import org.compiere.model.MCurrency;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.process.SvrProcess;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	
 *  @author Italo Niñoles
 *  @version $Id: RepriceInvoice.java $
 */
public class RepriceInvoice extends SvrProcess
{
	private int		p_Invoice_ID = 0;
	
	/**
	 * 	Prepare
	 */
	protected void prepare ()
	{	
		p_Invoice_ID = getRecord_ID();
	}	//	prepare

	/**
	 * 	Process
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		MInvoice inv = new MInvoice(getCtx(), p_Invoice_ID, get_TrxName());
		
		int ID_CurrencyBase = 228;
		if(inv.getC_Currency_ID() == ID_CurrencyBase)
		{
			throw new AdempiereUserError("Factura ya convertida", "Conversion Monetaria");
		}
		MCurrency curreTo = new MCurrency(getCtx(), 228, get_TrxName());		
		BigDecimal MultiplyRate = (BigDecimal)inv.get_Value("MultiplyRate");
		if(MultiplyRate==null || MultiplyRate.signum()==0)
			
		MultiplyRate=MConversionRate.getRate(inv.getC_Currency_ID(),curreTo.get_ID(),inv.getDateInvoiced(), inv.getC_ConversionType_ID(), 
				inv.getAD_Client_ID(), inv.getAD_Org_ID());
		log.config("currency = "+inv.getC_Currency_ID()+" - To_Currency="+curreTo.get_ID()+"- Date="+inv.getDateInvoiced()+"-Conversion="+inv.getC_ConversionType_ID());
		
		if(MultiplyRate==null || MultiplyRate.compareTo(Env.ZERO)<=0)
		{
			throw new AdempiereUserError("Por favor definir el tipo de cambio", "Conversion Monetaria");		  		
		}
		else
		{				
			log.config("C_Invoice_ID:"+inv.get_ID()+"-MultiplyRate:"+MultiplyRate);
			
			inv.set_CustomColumn("MultiplyRate", MultiplyRate);
			BigDecimal ForeignPrice = (BigDecimal)inv.get_Value("ForeignPrice");
			if(ForeignPrice == null || ForeignPrice.signum()==0)
			{
				inv.set_CustomColumn("ForeignPrice", inv.getGrandTotal());
				inv.setC_Currency_ID(ID_CurrencyBase);
				inv.save();
			}
			//actualizamos las lineas
			String sql = "SELECT invl.C_InvoiceLine_ID "
                +" FROM C_InvoiceLine invl WHERE invl.C_Invoice_ID = "+inv.get_ID();
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement (sql, get_TrxName());
				rs = pstmt.executeQuery();
				while (rs.next())
				{	
					MInvoiceLine line = new MInvoiceLine(Env.getCtx(),rs.getInt(1),get_TrxName());
					//validacion de ejecucion anterior
					if(line.get_Value("ForeignPrice") != null && ((BigDecimal)line.get_Value("ForeignPrice")).compareTo(Env.ZERO) > 0)
					{
						 rs  = null; pstmt  = null;
						 throw new AdempiereUserError("La conversion monetaria ya fue ejecutada con anterioridad para esta factura", "Conversion Monetaria");
					}
					//fin validacion
					line.set_CustomColumn("ForeignPrice", line.getPriceEntered());
					log.config("setForeignPrice:"+line.getPriceActual().setScale(curreTo.getStdPrecision(),BigDecimal.ROUND_HALF_EVEN));
					line.setPriceList(line.getPriceList().multiply(MultiplyRate).setScale(curreTo.getStdPrecision(),BigDecimal.ROUND_HALF_EVEN));				
					log.config("setPriceList:"+line.getPriceList().multiply(MultiplyRate));
					line.setPrice(line.getPriceEntered().multiply(MultiplyRate).setScale(curreTo.getStdPrecision(),BigDecimal.ROUND_HALF_EVEN));
					//line.setTaxAmt(line.getTaxAmt().multiply(MultiplyRate).setScale(curreTo.getStdPrecision(),BigDecimal.ROUND_HALF_EVEN));
					line.setLineNetAmt();
					line.save();
					line.setTaxAmt();
					line.save();
				}
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, sql, e);
				rs = null; pstmt = null;
			}finally
			{
				rs.close(); pstmt.close();
				rs = null; pstmt = null;
			}
		}		
		return "Conversión realizada";
	}	//	doIt
}	//RepriceInvoice

