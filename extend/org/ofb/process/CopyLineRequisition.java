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
import java.util.logging.Level;

import org.compiere.model.MRequisition;
import org.compiere.model.MRequisitionLine;
import org.compiere.process.*;

/**
 *  Copy Order Lines
 *
 *	@author Jorg Janke
 *	@version $Id: CopyFromOrder.java,v 1.2 2006/07/30 00:51:02 jjanke Exp $
 */
public class CopyLineRequisition extends SvrProcess
{
	/**	The Order				*/
	private int		p_M_RequisitionFrom_ID;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("M_Requisition"))
				p_M_RequisitionFrom_ID = ((BigDecimal)para[i].getParameter()).intValue();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}
	}	//	prepare

	/**
	 *  Perform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		int M_RequisitionTo_ID = getRecord_ID();
		if (p_M_RequisitionFrom_ID == 0)
			throw new IllegalArgumentException("Source M_Requisition_ID == 0");
		if (M_RequisitionTo_ID == 0)
			throw new IllegalArgumentException("Target M_Requisition_ID == 0");
		MRequisition from = new MRequisition (getCtx(), p_M_RequisitionFrom_ID, get_TrxName());
		MRequisition to = new MRequisition (getCtx(), M_RequisitionTo_ID, get_TrxName());
		//
		int no = copyLinesFrom (from,to);		//	no Attributes
		//
		return "@Copied@=" + no;
	}	//	doIt
	
	public int copyLinesFrom (MRequisition sourceReq, MRequisition targetReq)
	{
		if (sourceReq == null || targetReq == null)
			return 0;
		MRequisitionLine[] fromLines = sourceReq.getLines();
		int count = 0;
		for (int i = 0; i < fromLines.length; i++)
		{
			MRequisitionLine newLine = new MRequisitionLine(targetReq);
			newLine.setM_Product_ID(fromLines[i].getM_Product_ID());
			newLine.setC_UOM_ID(fromLines[i].getC_UOM_ID());
			newLine.setQty(fromLines[i].getQty());
			newLine.setPriceActual(fromLines[i].getPriceActual());
			newLine.setDescription(fromLines[i].getDescription());
			newLine.setM_Product_ID(fromLines[i].getM_Product_ID());
			newLine.setC_BPartner_ID(fromLines[i].getC_BPartner_ID());
			newLine.setC_Charge_ID(fromLines[i].getC_Charge_ID());
			newLine.setIsActive(fromLines[i].isActive());
			if(newLine.save(get_TrxName()))
				count++;
			try {
				newLine.set_CustomColumn("M_RequisitionLineRef_ID",fromLines[i].get_ID());
			} catch (Exception e) {
				log.config(e.toString());
			}
		}
		if (fromLines.length != count)
			log.log(Level.SEVERE, "Line difference - From=" + fromLines.length + " <> Saved=" + count);
		return count;
	}	//	copyLinesFrom

}	//	CopyFromOrder
