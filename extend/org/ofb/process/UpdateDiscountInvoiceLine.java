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

import java.math.BigDecimal;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;

/**
 *	
 *	
 *  @author Italo Niñoles ininoles
 *  @version $Id: SeparateInvoices.java $
 */
public class UpdateDiscountInvoiceLine extends SvrProcess
{	
	private int			ID_Invoice = 0;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{
		 ID_Invoice = getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		MInvoice inv = new MInvoice(getCtx(), ID_Invoice, get_TrxName());
		MInvoiceLine[] lines = inv.getLines(false);
		BigDecimal discount = (BigDecimal)inv.get_Value("Discount");
		int cant = 0;
		if(discount != null && discount.compareTo(Env.ZERO) != 0)
		{	
			for (int i = 0; i < lines.length; i++)
			{
				MInvoiceLine line = lines[i];
				BigDecimal fPrice = (BigDecimal)line.get_Value("ForeignPrice");
				BigDecimal amtDis = Env.ZERO;
				BigDecimal newPrice  = Env.ZERO;
				if(fPrice != null && fPrice.compareTo(Env.ZERO) != 0)
				{
					amtDis = fPrice.divide(Env.ONEHUNDRED);
					newPrice = fPrice;
				}
				else 
				{
					line.set_CustomColumn("ForeignPrice", line.getPriceEntered());
					amtDis = line.getPriceEntered().divide(Env.ONEHUNDRED);
					newPrice = line.getPriceEntered();
				}
				amtDis = amtDis.multiply(discount);
				line.setPrice(newPrice.subtract(amtDis));
				line.setLineNetAmt();
				line.setTaxAmt();
				line.save();
				cant++;
			}
		}
	    return "Se han actualizado "+cant+ " Lineas";
	}	//	doIt
}
