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
package org.ofb.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

import org.compiere.model.MAllocationHdr;
import org.compiere.model.MClient;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPayment;
import org.compiere.model.MPaymentAllocate;
import org.compiere.model.MRequisitionLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Validator default OFB
 *
 *  @author Italo Niñoles
 */
public class ModelOFB implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModelOFB ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModelOFB.class);
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
			log.info("Initializing global validator: "+this.toString());
		}

		//	Tables to be monitored		
		engine.addModelChange(MPayment.Table_Name, this);
		engine.addModelChange(MInvoice.Table_Name, this);		
		engine.addModelChange(MOrderLine.Table_Name, this);
		engine.addModelChange(MPaymentAllocate.Table_Name, this);
		//	Documents to be monitored

	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	
     */
	public static final String DOCSTATUS_Drafted = "DR";
	public static final String DOCSTATUS_Completed = "CO";
	public static final String DOCSTATUS_InProgress = "IP";
	public static final String DOCSTATUS_Voided = "VO";
	
	
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if (type == TYPE_AFTER_CHANGE && po.get_Table_ID()==MPayment.Table_ID)
		{
			MPayment pay = (MPayment) po;
			//MPayment Mpay = new MPayment(po.getCtx(), pay.get_ID(), po.get_TrxName());
			
			PreparedStatement pstmt = null;
			String mySql = "select distinct C_AllocationHdr_ID FROM C_AllocationLine WHERE C_Payment_ID = ?";
			
			if (pay.getDocStatus() == DOCSTATUS_Completed)
			{
				try 
				{
					pstmt = DB.prepareStatement(mySql, po.get_TrxName());
					pstmt.setInt(1, pay.get_ID());											
					ResultSet rs = pstmt.executeQuery();
				
					if (rs.next())
					{
						MAllocationHdr hdr = new MAllocationHdr(po.getCtx(), rs.getInt("C_AllocationHdr_ID"), po.get_TrxName());
						if (hdr.getDocStatus().compareTo(DOCSTATUS_Drafted) == 0 || hdr.getDocStatus().compareTo(DOCSTATUS_InProgress) == 0)
						{							
							hdr.setDocStatus(hdr.completeIt());
							hdr.save();
							log.info("Allocation: "+hdr.get_ID()+" Completed");
							
						}
					}
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, e.getMessage(), e);
				}
			}			
			if (pay.getDocStatus().compareTo(DOCSTATUS_Voided) == 0)
			{
				try 
				{
					pstmt = DB.prepareStatement(mySql, po.get_TrxName());
					pstmt.setInt(1, pay.get_ID());											
					ResultSet rs = pstmt.executeQuery();
					if (rs.next())
					{
						MAllocationHdr hdr = new MAllocationHdr(po.getCtx(), rs.getInt("C_AllocationHdr_ID"), po.get_TrxName());
						if (hdr.getDocStatus().compareTo(DOCSTATUS_Completed) == 0)
						{							
							if (hdr.voidIt())
							{
								hdr.save();
								log.info("Allocation: "+hdr.get_ID()+ " Voided");
							}														
						}
					}
				}
				catch (Exception e)
				{
					log.log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}		
		if ((type == TYPE_BEFORE_CHANGE || type== TYPE_BEFORE_NEW)&& po.get_Table_ID()==MInvoice.Table_ID)
		{
			MInvoice inv = (MInvoice) po;
			if(inv.isSOTrx())
			{
				int cant = 0;
				cant = DB.getSQLValue(po.get_TrxName(), "SELECT COUNT(1) FROM C_Invoice WHERE C_DocTypeTarget_ID = "+inv.getC_DocTypeTarget_ID()+" " +
						"AND DocumentNo = '"+inv.getDocumentNo()+"' AND C_Invoice_ID <> "+inv.get_ID());
				if(cant > 0)
					return "Ya existe una factura con mismo Numero y Tipo de Documento";
			}
		}
		if ((type == TYPE_AFTER_NEW ||  type == TYPE_AFTER_DELETE) && po.get_Table_ID()==MOrderLine.Table_ID)
		{
			MOrderLine oLine = (MOrderLine) po;
			if(oLine.get_ValueAsInt("M_RequisitionLine_ID")>0)
			{
				MRequisitionLine line=new MRequisitionLine(po.getCtx(),oLine.get_ValueAsInt("M_RequisitionLine_ID"), po.get_TrxName());
				String sqlUpRL = "SELECT SUM(QTYENTERED) FROM C_OrderLine col " +
						"INNER JOIN C_Order co ON (col.C_Order_ID = co.C_Order_ID) " +
						" WHERE M_RequisitionLine_ID = "+oLine.get_ValueAsInt("M_RequisitionLine_ID")+" AND co.DocStatus IN ('DR','IP','CO','CL')";
				BigDecimal amt = DB.getSQLValueBD(po.get_TrxName(), sqlUpRL);
				if (amt != null && amt.compareTo(Env.ZERO) > 0)
				{
					line.set_CustomColumn("QtyUsed", amt);
					line.save();	
				}
			}
		}		
		if ((type == TYPE_AFTER_NEW || type == TYPE_AFTER_CHANGE || type == TYPE_AFTER_DELETE)&& po.get_Table_ID()==MPaymentAllocate.Table_ID)
		{
			MPaymentAllocate pAll = (MPaymentAllocate) po;
			MPayment pay = new MPayment(po.getCtx(), pAll.getC_Payment_ID(), po.get_TrxName());
			BigDecimal Amount = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(Amount) FROM C_PaymentAllocate WHERE IsActive = 'Y' AND C_Payment_ID = "+pay.get_ID());
			//BigDecimal DAmt = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(DiscountAmt) FROM C_PaymentAllocate WHERE IsActive = 'Y' AND C_Payment_ID = "+pay.get_ID());
			//BigDecimal WOffAmt = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(WriteOffAmt) FROM C_PaymentAllocate WHERE IsActive = 'Y' AND C_Payment_ID = "+pay.get_ID());
			//BigDecimal OUAmt = DB.getSQLValueBD(po.get_TrxName(), "SELECT SUM(OverUnderAmt) FROM C_PaymentAllocate WHERE IsActive = 'Y' AND C_Payment_ID = "+pay.get_ID());
			pay.setPayAmt(Amount);
			pay.save();
		}
					
	return null;
	}	//	modelChange

	/**
	 *	Validate Document.
	 *	Called as first step of DocAction.prepareIt
     *	when you called addDocValidate for the table.
     *	Note that totals, etc. may not be correct.
	 *	@param po persistent object
	 *	@param timing see TIMING_ constants
     *	@return error message or null
	 */
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
		StringBuffer sb = new StringBuffer ("QSS_Validator");
		return sb.toString ();
	}	//	toString


	

}	