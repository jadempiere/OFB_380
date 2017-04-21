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
package org.minju.model;

import java.math.BigDecimal;

import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
/**
 *	Validator for MINJU
 *
 *  @author Italo Niñoles
 */
public class ModMINJUValidCompleteOrderBugdet implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModMINJUValidCompleteOrderBugdet ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModMINJUValidCompleteOrderBugdet.class);
	/** Client			*/
	private int		m_AD_Client_ID = -1;
	

	/**
	 *	Initialize Validation
	 *	@param engine validation engine
	 *	@param client client
	 */
	public void initialize (ModelValidationEngine engine, MClient client)
	{
		//client = null for global validator
		if (client != null) {
			m_AD_Client_ID = client.getAD_Client_ID();
			log.info(client.toString());
		}
		else  {
			log.info("Initializing Model Price Validator: "+this.toString());
		}

		//	Tables to be monitored				
		engine.addDocValidate(MOrder.Table_Name, this);
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
			
		return null;
	}	//	modelChange

	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);
		if(timing == TIMING_BEFORE_COMPLETE && po.get_Table_ID() == MOrder.Table_ID)
		{
			MOrder order = (MOrder)po;
			MOrderLine[] lines = order.getLines(true, null);	//	Line is default
			for (int i = 0; i < lines.length; i++)
			{
				MOrderLine line = lines[i];
				//buscamos si tiene linea de solicitud asociada
				int ID_ReqLine = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(M_RequisitionLine_ID) " +
						" FROM M_RequisitionLine rl " +
						" INNER JOIN M_Requisition mr on (rl.M_Requisition_ID = mr.M_Requisition_ID) " +
						" WHERE rl.C_OrderLine_ID = "+line.get_ID()+" AND rl.IsActive = 'Y' " +
						" AND mr.IsActive = 'Y' AND mr.DocStatus NOT IN ('VO')");
				if(ID_ReqLine > 0)
				{
					MRequisitionLine rLine = new MRequisitionLine(po.getCtx(), ID_ReqLine, po.get_TrxName());
					BigDecimal totalAmt = null; 
					if(line.getC_Tax().getRate().compareTo(Env.ZERO) > 0)
					{
						totalAmt = line.getLineNetAmt().multiply(line.getC_Tax().getRate());
						totalAmt = totalAmt.divide(Env.ONEHUNDRED);
					}
					if(totalAmt != null)
						totalAmt = totalAmt.add(line.getLineNetAmt());
					else
						totalAmt = line.getLineNetAmt();
					if(totalAmt.compareTo(rLine.getLineNetAmt()) > 0)
					{
						//return "Error de presupuesto en linea "+line.getLine();
						order.setDescription(order.getDescription()+" Error de presupuesto en linea "+line.getLine()+"-");
					}
				}
			}
		}
		return null;
	}	//	docValidate
	
	/**
	 *	User Login.
	 *	Called when preferences are set
	 *	@param AD_Org_ID org
	 *	@param AD_Role_ID role
	 *	@param AD_User_ID user
	 *	@return error message or null
	 */
	public String login (int AD_Org_ID, int AD_Role_ID, int AD_User_ID)
	{
		log.info("AD_User_ID=" + AD_User_ID);

		return null;
	}	//	login


	/**
	 *	Get Client to be monitored
	 *	@return AD_Client_ID client
	 */
	public int getAD_Client_ID()
	{
		return m_AD_Client_ID;
	}	//	getAD_Client_ID


	/**
	 * 	String Representation
	 *	@return info
	 */
	public String toString ()
	{
		StringBuffer sb = new StringBuffer ("ModelPrice");
		return sb.toString ();
	}	//	toString


	

}	