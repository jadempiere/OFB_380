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

import java.math.BigDecimal;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
/**
 * 	UpdatePriceOLine Line COPESA
 *	
 *  @author Italo Niñoles
 */
public class UpdatePriceOrderFromCopy extends SvrProcess
{	
	private int 		ID_Order;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare ()
	{
		ID_Order = getRecord_ID();
	}	//	prepare

	
	/**
	 * 	Process
	 *	@return message
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{
		MOrder order = new MOrder(getCtx(),ID_Order, get_TrxName());
		if(order.get_ValueAsInt("C_OrderRef_ID") > 0)
		{
			MOrderLine[] lines = order.getLines(true, null);	
			for (int i = 0; i < lines.length; i++)
			{
				MOrderLine line = lines[i];
				//actualizamos los productos editoriales
				if(line.getM_Product_ID() > 0 && line.getM_Product().getProductType().compareTo("S")==0
						&& !line.get_ValueAsBoolean("IsFree"))
				{
					int ID_oLineOld= DB.getSQLValue(get_TrxName(), "SELECT MAX(C_OrderLine_ID) FROM C_OrderLine " +
							" WHERE C_Order_ID = "+order.get_ValueAsInt("C_OrderRef_ID")+" AND M_Product_ID = "+line.getM_Product_ID()+
							" AND IsFree = 'N' ");					
					if(ID_oLineOld > 0)
					{
						MOrderLine oLineOld = new MOrderLine(getCtx(), ID_oLineOld, get_TrxName());
						line.setPrice(oLineOld.getPriceEntered());
						line.set_CustomColumn("MonthlyAmount", (BigDecimal)oLineOld.get_Value("MonthlyAmount"));
						line.save();
					}
				}
			}
		}		
		return "Precios Actualizados";
	}	//	doIt

}	//	BPGroupAcctCopy

