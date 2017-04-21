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
import org.compiere.model.X_GL_BudgetControlLine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Validator for MINJU
 *
 *  @author Italo Niñoles
 */
public class ModMINJUCopyInfoBudget implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModMINJUCopyInfoBudget ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModMINJUCopyInfoBudget.class);
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
		engine.addModelChange(MRequisitionLine.Table_Name, this);		
		engine.addModelChange(MOrderLine.Table_Name, this);
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE) && po.get_Table_ID()==MRequisitionLine.Table_ID) 
		{	
			MRequisitionLine reqLine = (MRequisitionLine)po;
			if(reqLine.get_ValueAsInt("GL_BudgetControlLine_ID") > 0)
			{
				X_GL_BudgetControlLine bcLine = new X_GL_BudgetControlLine(po.getCtx(), reqLine.get_ValueAsInt("GL_BudgetControlLine_ID"),po.get_TrxName());
				//antes de copiar campos realizamos validación
				/*BigDecimal amtUsed = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(LineNetAmt) FROM M_RequisitionLine rLine " +
						" INNER JOIN M_Requisition r ON (rLine.M_Requisition_ID = r.M_Requisition_ID) " +
						" WHERE rLine.IsActive = 'Y' AND DocStatus IN ('CO','DR','IP','IN') " +
						" AND rline.GL_BudgetControlLine_ID = ? ",bcLine.get_ID());
				BigDecimal amtOpen = bcLine.getAmount().subtract(amtUsed).subtract(reqLine.getLineNetAmt());
				if(amtOpen.compareTo(Env.ZERO) < 0)
					return "Error: Monto sobrepasa monto presupuestado";*/
				reqLine.setAD_Org_ID(bcLine.getAD_Org_ID());
				//ininoles ahora se copiara categori en vez de producto
				if(bcLine.getM_Product_ID() > 0)
					reqLine.setM_Product_ID(bcLine.getM_Product_ID());	
				if(bcLine.get_ValueAsInt("M_Product_Category_ID") > 0)
					reqLine.set_CustomColumn("M_Product_Category_ID", bcLine.get_ValueAsInt("M_Product_Category_ID"));
				reqLine.setDescription(bcLine.getDescription());
				//BigDecimal qtyNew = (BigDecimal)bcLine.get_Value("Qty");
				/*BigDecimal amtUsed = (BigDecimal)bcLine.get_Value("Amount2");//comprometido
				if(amtUsed == null)
					amtUsed = Env.ZERO;
				//calculamos cantidad usada
				BigDecimal qtyUsed = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(qty) FROM M_RequisitionLine rLine " +
						" INNER JOIN M_Requisition r ON (rLine.M_Requisition_ID = r.M_Requisition_ID) " +
						" WHERE DocStatus IN ('CO','DR','IP','IN') " +
						" AND rline.GL_BudgetControlLine_ID = ? ",bcLine.get_ID());
				BigDecimal qtyOpen = (BigDecimal)bcLine.get_Value("Qty");
				if(qtyOpen == null)
					qtyOpen = Env.ZERO;
				if(qtyUsed == null)
					qtyUsed = Env.ZERO;
				qtyOpen = qtyOpen.subtract(qtyUsed);
				if(qtyOpen == null || qtyOpen.compareTo(Env.ZERO) == 0)
					qtyOpen = Env.ONE;				
				BigDecimal amtOpen = bcLine.getAmount().subtract(amtUsed);
				if(amtOpen != null && amtOpen.compareTo(Env.ZERO) > 0)
				{
					reqLine.setQty(qtyOpen);
					//BigDecimal unitPrice = bcLine.getAmount().divide(qtyNew, RoundingMode.HALF_EVEN);
					BigDecimal unitPrice = amtOpen.divide(qtyOpen, RoundingMode.HALF_EVEN);
					if(unitPrice != null && unitPrice.compareTo(Env.ZERO) >= 0)
					{
						reqLine.setPriceActual(unitPrice);
						reqLine.setLineNetAmt();
					}
				}
				reqLine.setLineNetAmt();*/
			}
		}
		/*if(type == TYPE_BEFORE_CHANGE && po.get_Table_ID()==MRequisitionLine.Table_ID) 
		{	
			MRequisitionLine reqLine = (MRequisitionLine)po;
			if(reqLine.get_ValueAsInt("GL_BudgetControlLine_ID") > 0)
			{
				X_GL_BudgetControlLine bcLine = new X_GL_BudgetControlLine(po.getCtx(), reqLine.get_ValueAsInt("GL_BudgetControlLine_ID"),po.get_TrxName());
				reqLine.setAD_Org_ID(bcLine.getAD_Org_ID());
				if(bcLine.getM_Product_ID() > 0)
					reqLine.setM_Product_ID(bcLine.getM_Product_ID());
				//ininoles ahora se copiara categoria de producto en vez de producto
				if(bcLine.get_ValueAsInt("M_Product_Category_ID") > 0)
					reqLine.set_CustomColumn("M_Product_Category_ID", bcLine.get_ValueAsInt("M_Product_Category_ID"));
				reqLine.setDescription(bcLine.getDescription());
				/*BigDecimal qtyNew = (BigDecimal)bcLine.get_Value("Qty");
				BigDecimal amtUsed = (BigDecimal)bcLine.get_Value("Amount2");//comprometido
				if(amtUsed == null)
					amtUsed = Env.ZERO;
				BigDecimal amtOpen = bcLine.getAmount().subtract(amtUsed);
				if(reqLine.getQty() != null && reqLine.getQty().compareTo(Env.ZERO) > 0)
				{
					reqLine.setQty(reqLine.getQty());
					BigDecimal unitPrice = bcLine.getAmount().divide((BigDecimal)bcLine.get_Value("Qty"), RoundingMode.HALF_EVEN);
					//BigDecimal unitPrice = amtOpen.divide((BigDecimal)bcLine.get_Value("Qty"), RoundingMode.HALF_EVEN);
					if(unitPrice != null && unitPrice.compareTo(Env.ZERO) >= 0)
					{
						reqLine.setPriceActual(unitPrice);
						reqLine.setLineNetAmt();
					}
				}
				else if(qtyNew != null && qtyNew.compareTo(Env.ZERO) > 0)
				{
					reqLine.setQty(qtyNew);
					//BigDecimal unitPrice = bcLine.getAmount().divide(qtyNew, RoundingMode.HALF_EVEN);
					BigDecimal unitPrice = amtOpen.divide((BigDecimal)bcLine.get_Value("Qty"), RoundingMode.HALF_EVEN);
					if(unitPrice != null && unitPrice.compareTo(Env.ZERO) >= 0)
					{
						reqLine.setPriceActual(unitPrice);
						reqLine.setLineNetAmt();
					}
				}
				reqLine.setLineNetAmt();*/
			/*}
		}*/
		if((type == TYPE_AFTER_CHANGE || type == TYPE_AFTER_NEW) && po.get_Table_ID()==MOrderLine.Table_ID) 
		{	
			MOrderLine oLine = (MOrderLine)po;
			MOrder order = new MOrder(po.getCtx(), oLine.getC_Order_ID(), po.get_TrxName());
			if(order.getDocStatus().compareTo("CO") != 0 && order.getDocStatus().compareTo("VO") != 0 )
			if(oLine.get_ValueAsInt("M_RequisitionLine_ID") > 0)
			{
				MRequisitionLine req = new MRequisitionLine(po.getCtx(), oLine.get_ValueAsInt("M_RequisitionLine_ID"), po.get_TrxName());
				if(req.get_ValueAsInt("M_Product_Category_ID") > 0)
					DB.executeUpdate("UPDATE C_OrderLine SET M_Product_Category_ID = "+req.get_Value("M_Product_Category_ID")+
							" WHERE C_OrderLine_ID = "+oLine.get_ID(), po.get_TrxName());
				if(req.getDescription() != null && req.getDescription().trim().length() > 0)
					DB.executeUpdate("UPDATE C_OrderLine SET Description = '"+req.getDescription()+"'"+
						" WHERE C_OrderLine_ID = "+oLine.get_ID(), po.get_TrxName());
			}
		}
		if((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE) && po.get_Table_ID()==MRequisitionLine.Table_ID) 
		{	
			MRequisitionLine reqLine = (MRequisitionLine)po;
			if(reqLine.get_ValueAsInt("GL_BudgetControlLine_ID") > 0)
			{
				X_GL_BudgetControlLine bcLine = new X_GL_BudgetControlLine(po.getCtx(), reqLine.get_ValueAsInt("GL_BudgetControlLine_ID"),po.get_TrxName());
				//antes de copiar campos realizamos validación
				BigDecimal amtUsed = DB.getSQLValueBD(po.get_TrxName(), "SELECT coalesce(SUM(LineNetAmt),0) FROM M_RequisitionLine rLine " +
						" INNER JOIN M_Requisition r ON (rLine.M_Requisition_ID = r.M_Requisition_ID) " +
						" WHERE rLine.IsActive = 'Y' AND DocStatus IN ('CO','DR','IP') " +
						" AND rline.GL_BudgetControlLine_ID = ? ",bcLine.get_ID());
				BigDecimal amtOpen = bcLine.getAmount().subtract(amtUsed);
				log.config("lala "+reqLine.get_ValueAsInt("GL_BudgetControlLine_ID"));
				if(amtOpen.compareTo(Env.ZERO) < 0)
					return "Error: Monto sobrepasa monto presupuestado";
				
			}
		}
		return null;
	}	//	modelChange

	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);
		
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