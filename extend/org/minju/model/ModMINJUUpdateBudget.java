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
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPayment;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_GL_BudgetControlLine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
/**
 *	Validator for MINJU
 *
 *  @author Italo Niñoles
 */
public class ModMINJUUpdateBudget implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModMINJUUpdateBudget ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModMINJUUpdateBudget.class);
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
		//engine.addDocValidate(MInvoice.Table_Name, this);
		engine.addDocValidate(MPayment.Table_Name, this);
		
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
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID() == MOrder.Table_ID)
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
					if(rLine.get_ValueAsInt("GL_BudgetControlLine_ID") > 0)
					{
						X_GL_BudgetControlLine bcLine = new X_GL_BudgetControlLine(po.getCtx(), rLine.get_ValueAsInt("GL_BudgetControlLine_ID"), po.get_TrxName());
						BigDecimal usedAmt = (BigDecimal)bcLine.get_Value("Amount2");
						if(usedAmt == null)
							usedAmt = Env.ZERO;
						BigDecimal taxAmt = null; 
						if(line.getC_Tax().getRate().compareTo(Env.ZERO) > 0)
						{
							taxAmt = line.getLineNetAmt().multiply(line.getC_Tax().getRate());
							taxAmt = taxAmt.divide(Env.ONEHUNDRED);
						}
						if(taxAmt != null)
							usedAmt = usedAmt.add(taxAmt);
						usedAmt = usedAmt.add(line.getLineNetAmt());
						bcLine.set_CustomColumn("Amount2", usedAmt);
						bcLine.save();
					}
				}
			}
		}
		//restamos usado al anular orden
		if(timing == TIMING_BEFORE_VOID && po.get_Table_ID() == MOrder.Table_ID)
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
					if(rLine.get_ValueAsInt("GL_BudgetControlLine_ID") > 0)
					{
						X_GL_BudgetControlLine bcLine = new X_GL_BudgetControlLine(po.getCtx(), rLine.get_ValueAsInt("GL_BudgetControlLine_ID"), po.get_TrxName());
						BigDecimal usedAmt = (BigDecimal)bcLine.get_Value("Amount2");
						if(usedAmt == null)
							usedAmt = Env.ZERO;
						BigDecimal taxAmt = null; 
						if(line.getC_Tax().getRate().compareTo(Env.ZERO) > 0)
						{
							taxAmt = line.getLineNetAmt().multiply(line.getC_Tax().getRate());
							taxAmt = taxAmt.divide(Env.ONEHUNDRED);
						}
						if(taxAmt != null)
							usedAmt = usedAmt.add(taxAmt);
						usedAmt = usedAmt.subtract(line.getLineNetAmt());
						bcLine.set_CustomColumn("Amount2", usedAmt);
						bcLine.save();
					}
				}
			}
		}
		//restamos usado al reactivar order
		if(timing == TIMING_BEFORE_REACTIVATE && po.get_Table_ID() == MOrder.Table_ID)
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
					if(rLine.get_ValueAsInt("GL_BudgetControlLine_ID") > 0)
					{
						X_GL_BudgetControlLine bcLine = new X_GL_BudgetControlLine(po.getCtx(), rLine.get_ValueAsInt("GL_BudgetControlLine_ID"), po.get_TrxName());
						BigDecimal usedAmt = (BigDecimal)bcLine.get_Value("Amount2");
						if(usedAmt == null)
							usedAmt = Env.ZERO;
						BigDecimal taxAmt = null; 
						if(line.getC_Tax().getRate().compareTo(Env.ZERO) > 0)
						{
							taxAmt = line.getLineNetAmt().multiply(line.getC_Tax().getRate());
							taxAmt = taxAmt.divide(Env.ONEHUNDRED);
						}
						if(taxAmt != null)
							usedAmt = usedAmt.add(taxAmt);
						usedAmt = usedAmt.subtract(line.getLineNetAmt());
						bcLine.set_CustomColumn("Amount2", usedAmt);
						bcLine.save();
					}
				}
			}
		}		
		//sumamos ejecutado al completar factura		
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID() == MInvoice.Table_ID)
		{
			MInvoice inv = (MInvoice)po;
			MInvoiceLine[] lines = inv.getLines();	//	Line is default
			for (int i = 0; i < lines.length; i++)
			{
				MInvoiceLine line = lines[i];
				//buscamos si tiene linea de solicitud asociada
				int ID_ReqLine = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(M_RequisitionLine_ID) " +
						" FROM M_RequisitionLine rl " +
						" INNER JOIN M_Requisition mr on (rl.M_Requisition_ID = mr.M_Requisition_ID) " +
						" WHERE rl.C_OrderLine_ID = "+line.getC_OrderLine_ID()+" AND rl.IsActive = 'Y' " +
						" AND mr.IsActive = 'Y' AND mr.DocStatus NOT IN ('VO')");
				if(ID_ReqLine > 0)
				{
					MRequisitionLine rLine = new MRequisitionLine(po.getCtx(), ID_ReqLine, po.get_TrxName());
					if(rLine.get_ValueAsInt("GL_BudgetControlLine_ID") > 0)
					{
						X_GL_BudgetControlLine bcLine = new X_GL_BudgetControlLine(po.getCtx(), rLine.get_ValueAsInt("GL_BudgetControlLine_ID"), po.get_TrxName());
						BigDecimal usedAmt = (BigDecimal)bcLine.get_Value("Amount3");
						if(usedAmt == null)
							usedAmt = Env.ZERO;
						usedAmt = usedAmt.add(line.getLineTotalAmt());
						bcLine.set_CustomColumn("Amount3", usedAmt);
						bcLine.save();
					}
				}
			}
		}
		//se resta usado al anular factura
		if(timing == TIMING_AFTER_VOID && po.get_Table_ID() == MInvoice.Table_ID)
		{
			MInvoice inv = (MInvoice)po;
			MInvoiceLine[] lines = inv.getLines();	//	Line is default
			for (int i = 0; i < lines.length; i++)
			{
				MInvoiceLine line = lines[i];
				//buscamos si tiene linea de solicitud asociada
				int ID_ReqLine = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(M_RequisitionLine_ID) " +
						" FROM M_RequisitionLine rl " +
						" INNER JOIN M_Requisition mr on (rl.M_Requisition_ID = mr.M_Requisition_ID) " +
						" WHERE rl.C_OrderLine_ID = "+line.getC_OrderLine_ID()+" AND rl.IsActive = 'Y' " +
						" AND mr.IsActive = 'Y' AND mr.DocStatus NOT IN ('VO')");
				if(ID_ReqLine > 0)
				{
					MRequisitionLine rLine = new MRequisitionLine(po.getCtx(), ID_ReqLine, po.get_TrxName());
					if(rLine.get_ValueAsInt("GL_BudgetControlLine_ID") > 0)
					{
						X_GL_BudgetControlLine bcLine = new X_GL_BudgetControlLine(po.getCtx(), rLine.get_ValueAsInt("GL_BudgetControlLine_ID"), po.get_TrxName());
						BigDecimal usedAmt = (BigDecimal)bcLine.get_Value("Amount3");
						if(usedAmt == null)
							usedAmt = Env.ZERO;
						usedAmt = usedAmt.subtract(line.getLineTotalAmt());
						bcLine.set_CustomColumn("Amount3", usedAmt);
						bcLine.save();
					}
				}
			}
		}
		//se suma usado al completar pago
		/*if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID() == MPayment.Table_ID)
		{
			MPayment pay = (MPayment)po;
			if(pay.getC_Invoice_ID() > 0)
			{
				MInvoice inv = new MInvoice(po.getCtx(), pay.getC_Invoice_ID(), po.get_TrxName());
				MInvoiceLine[] lines = inv.getLines();	//	Line is default
				for (int i = 0; i < lines.length; i++)
				{
					MInvoiceLine line = lines[i];
					if(line.getC_OrderLine_ID() > 0)
					{
						//buscamos si tiene linea de solicitud asociada
						int ID_ReqLine = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(M_RequisitionLine_ID) " +
								" FROM M_RequisitionLine rl " +
								" INNER JOIN M_Requisition mr on (rl.M_Requisition_ID = mr.M_Requisition_ID) " +
								" WHERE rl.C_OrderLine_ID = "+line.getC_OrderLine_ID()+" AND rl.IsActive = 'Y' " +
								" AND mr.IsActive = 'Y' AND mr.DocStatus NOT IN ('VO')");
						if(ID_ReqLine > 0)	
						{
							MRequisitionLine rLine = new MRequisitionLine(po.getCtx(), ID_ReqLine, po.get_TrxName());
							if(rLine.get_ValueAsInt("GL_BudgetControlLine_ID") > 0)
							{
								X_GL_BudgetControlLine bcLine = new X_GL_BudgetControlLine(po.getCtx(), rLine.get_ValueAsInt("GL_BudgetControlLine_ID"), po.get_TrxName());
								BigDecimal usedAmt = (BigDecimal)bcLine.get_Value("Amount3");
								if(usedAmt == null)
									usedAmt = Env.ZERO;
								usedAmt = usedAmt.add(line.getLineTotalAmt());
								bcLine.set_CustomColumn("Amount3", usedAmt);
								bcLine.save();
							}
						}
					}
					else if(line.get_ValueAsInt("GL_BudgetControlLine_ID") > 0)
					{
						X_GL_BudgetControlLine bcLine = new X_GL_BudgetControlLine(po.getCtx(), line.get_ValueAsInt("GL_BudgetControlLine_ID"), po.get_TrxName());
						BigDecimal usedAmt = (BigDecimal)bcLine.get_Value("Amount3");
						if(usedAmt == null)
							usedAmt = Env.ZERO;
						usedAmt = usedAmt.add(line.getLineTotalAmt());
						bcLine.set_CustomColumn("Amount3", usedAmt);
						bcLine.save();
					}
				}
			}
			if(pay.get_ValueAsInt("GL_BudgetControlLine_ID") > 0)
			{
				X_GL_BudgetControlLine bcLine = new X_GL_BudgetControlLine(po.getCtx(), pay.get_ValueAsInt("GL_BudgetControlLine_ID"), po.get_TrxName());
				BigDecimal usedAmt = (BigDecimal)bcLine.get_Value("Amount3");
				if(usedAmt == null)
					usedAmt = Env.ZERO;
				usedAmt = usedAmt.add(pay.getPayAmt());
				bcLine.set_CustomColumn("Amount3", usedAmt);
				bcLine.save();
			}
		}*/
		//se resta usado al anular pago
		/*if(timing == TIMING_BEFORE_VOID && po.get_Table_ID() == MPayment.Table_ID)
		{
			MPayment pay = (MPayment)po;
			if(pay.getC_Invoice_ID() > 0)
			{
				MInvoice inv = new MInvoice(po.getCtx(), pay.getC_Invoice_ID(), po.get_TrxName());
				MInvoiceLine[] lines = inv.getLines();	//	Line is default
				for (int i = 0; i < lines.length; i++)
				{
					MInvoiceLine line = lines[i];
					if(line.getC_OrderLine_ID() > 0)
					{
					//buscamos si tiene linea de solicitud asociada
						int ID_ReqLine = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(M_RequisitionLine_ID) " +
								" FROM M_RequisitionLine rl " +
								" INNER JOIN M_Requisition mr on (rl.M_Requisition_ID = mr.M_Requisition_ID) " +
								" WHERE rl.C_OrderLine_ID = "+line.getC_OrderLine_ID()+" AND rl.IsActive = 'Y' " +
								" AND mr.IsActive = 'Y' AND mr.DocStatus NOT IN ('VO')");					
						if(ID_ReqLine > 0)
						{
							MRequisitionLine rLine = new MRequisitionLine(po.getCtx(), ID_ReqLine, po.get_TrxName());
							if(rLine.get_ValueAsInt("GL_BudgetControlLine_ID") > 0)
							{
								X_GL_BudgetControlLine bcLine = new X_GL_BudgetControlLine(po.getCtx(), rLine.get_ValueAsInt("GL_BudgetControlLine_ID"), po.get_TrxName());
								BigDecimal usedAmt = (BigDecimal)bcLine.get_Value("Amount3");
								if(usedAmt == null)
									usedAmt = Env.ZERO;
								usedAmt = usedAmt.subtract(line.getLineTotalAmt());
								bcLine.set_CustomColumn("Amount3", usedAmt);
								bcLine.save();
							}
						}
					}else if(line.get_ValueAsInt("GL_BudgetControlLine_ID") > 0)
					{
						X_GL_BudgetControlLine bcLine = new X_GL_BudgetControlLine(po.getCtx(), line.get_ValueAsInt("GL_BudgetControlLine_ID"), po.get_TrxName());
						BigDecimal usedAmt = (BigDecimal)bcLine.get_Value("Amount3");
						if(usedAmt == null)
							usedAmt = Env.ZERO;
						usedAmt = usedAmt.subtract(line.getLineTotalAmt());
						bcLine.set_CustomColumn("Amount3", usedAmt);
						bcLine.save();
					}
				}
			}
			if(pay.get_ValueAsInt("GL_BudgetControlLine_ID") > 0)
			{
				X_GL_BudgetControlLine bcLine = new X_GL_BudgetControlLine(po.getCtx(), pay.get_ValueAsInt("GL_BudgetControlLine_ID"), po.get_TrxName());
				BigDecimal usedAmt = (BigDecimal)bcLine.get_Value("Amount3");
				if(usedAmt == null)
					usedAmt = Env.ZERO;
				usedAmt = usedAmt.subtract(pay.getPayAmt());
				bcLine.set_CustomColumn("Amount3", usedAmt);
				bcLine.save();
			}
		}*/
		
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