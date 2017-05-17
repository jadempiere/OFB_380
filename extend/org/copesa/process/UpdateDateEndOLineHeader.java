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
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.Env;
/**
 * 	UpdatePriceOLine Line COPESA
 *	
 *  @author Italo Niñoles
 */
public class UpdateDateEndOLineHeader extends SvrProcess
{	
	private int 		id_Order;
	private Timestamp   p_DatePromised;

	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare ()
	{
		id_Order = getRecord_ID();
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (para[i].getParameterName().equals("DatePromised"))
				p_DatePromised = para[i].getParameterAsTimestamp();
			
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
		MOrder order = new MOrder(getCtx(), id_Order, get_TrxName());		
		String msg = "";
		if(order.getDocStatus().compareTo("DR") == 0 ||  
			order.getDocStatus().compareTo("IP") == 0 || 
			order.getDocStatus().compareTo("IN") == 0)
		{
			MOrderLine[] lines = order.getLines(true, null);
			for (int i = 0; i < lines.length; i++)
			{
				MOrderLine oLine = lines[i];
				if(oLine.get_ValueAsBoolean("IsFree") == false && oLine.getM_Product_ID() > 0
						&& oLine.getPriceEntered().compareTo(Env.ONE) > 0)
				{
					//ininoles solo si fecha es mayor a fecha fecha inicio de la linea
					Timestamp dateStart = (Timestamp)oLine.get_Value("DatePromised2");
					if(dateStart != null && p_DatePromised.compareTo(dateStart) > 0)
					{
						//validacion periodos(meses) completos
						//sacamos dia de ambos y comparamos que dia final sea igual a dia inicial +1
						int dayInicial = dateStart.getDate();
						int dayNew = p_DatePromised.getDate();
						int CorrectDay = dayInicial-1; 
						//si fecha nueva es menor a 1 validamosque sea mayor a 28 
						if(CorrectDay <= 0)
						{
							if(dayNew < 28)
								throw new AdempiereException("Error: Dia de termino incorrecto.");
						}
						else if(dayNew != dayInicial-1)
							throw new AdempiereException("Error: Dia debe ser "+CorrectDay);
						BigDecimal newAmt = null;
						Timestamp dateEnd = (Timestamp)oLine.get_Value("DatePromised3");
						// Se suma 1 porque las fechas de término están truncadas a 00:00:00.000, cuando en realidad deberían ser 23:59:59.999
						BigDecimal dif1 = new BigDecimal(dateEnd.getTime()-dateStart.getTime() + 24*3600*1000);  
							
						//dif1 = dif1*(24 * 60 * 60 * 1000);
						BigDecimal dif2 = new BigDecimal(p_DatePromised.getTime()-dateStart.getTime() + 24*3600*1000);
						
						//dif2 = dif2*(24 * 60 * 60 * 1000);
						BigDecimal factor = dif2.divide(dif1, 8, BigDecimal.ROUND_HALF_UP);;

						newAmt = oLine.getPriceEntered().multiply(factor);
						newAmt = newAmt.setScale(2,  BigDecimal.ROUND_HALF_UP);
						
						
						//newAmt = oLine.getPriceEntered().multiply(new BigDecimal(dif2));
						//newAmt = newAmt.divide(new BigDecimal(dif1), 0, RoundingMode.HALF_UP);				
						oLine.setPrice(newAmt);
						oLine.setLineNetAmt();
						oLine.setTax();
						oLine.set_CustomColumn("DatePromised3",p_DatePromised);
						oLine.save();
						msg = "Precio Actualizado";
					}
				}
			}
		}
		return msg;
	}	//	doIt

}	//	BPGroupAcctCopy

