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
import java.util.logging.Level;

import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
/**
 * 	UpdatePriceOLine Line COPESA
 *	
 *  @author Italo Niñoles
 */
public class UpdatePriceOLine extends SvrProcess
{	
	private int 		id_OrderLine;
	private BigDecimal priceNew;
	private String		p_Password;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare ()
	{
		id_OrderLine = getRecord_ID();
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (para[i].getParameterName().equals("Price"))
				priceNew = para[i].getParameterAsBigDecimal();
			else if (para[i].getParameterName().equals("Password"))
				p_Password = para[i].getParameterAsString();
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
		MOrderLine oLine = new MOrderLine(getCtx(), id_OrderLine, get_TrxName());
		MOrder order = new MOrder(getCtx(), oLine.getC_Order_ID(), get_TrxName());
		MClient client = new MClient(getCtx(), oLine.getAD_Client_ID(), get_TrxName());
		String msg = "";
		if((order.getDocStatus().compareTo("DR") == 0 ||  
			order.getDocStatus().compareTo("IP") == 0 || 
			order.getDocStatus().compareTo("IN") == 0) && 
			client.get_ValueAsString("PricePassword").compareTo(p_Password) == 0)
		{
			oLine.setPrice(priceNew);
			oLine.setLineNetAmt();
			oLine.setTax();
			oLine.save();
			msg = "Precio Actualizado";
		}else
			msg = "Error: Password Incorrecta";
		return msg;
	}	//	doIt

}	//	BPGroupAcctCopy

