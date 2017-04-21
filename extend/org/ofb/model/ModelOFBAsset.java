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

//import org.compiere.FA.CreateAssetForecast;
import org.compiere.FA.CreateAssetForecast;
import org.compiere.model.MAsset;
import org.compiere.model.MAssetAcct;
import org.compiere.model.MAssetChange;
import org.compiere.model.MAssetGroupAcct;
import org.compiere.model.MClient;
//import org.compiere.model.MDepreciationWorkfile;
import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrderLine;
import org.compiere.model.MRefList;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_A_Asset_Addition;
import org.compiere.model.X_A_Asset_Use;
import org.compiere.model.X_A_Depreciation_Workfile;
import org.compiere.model.X_C_InvoiceLine;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Validator default OFB
 *
 *  @author Italo Niñoles
 */
public class ModelOFBAsset implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModelOFBAsset ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModelOFBAsset.class);
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
		engine.addModelChange(MInOutLine.Table_Name, this);
		engine.addModelChange(MInvoiceLine.Table_Name, this);
		//	Documents to be monitored
		engine.addDocValidate(MInOut.Table_Name, this);
		engine.addDocValidate(MInvoice.Table_Name, this);

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
		if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE) && po.get_Table_ID()==MInvoiceLine.Table_ID)
		{
			MInvoiceLine iLine = (MInvoiceLine) po;
			MInvoice inv = new MInvoice(po.getCtx(), iLine.getC_Invoice_ID(), po.get_TrxName());
			if(inv.getDocStatus().compareTo("VO") != 0 && iLine.getM_InOutLine_ID() > 0)
			{
				MInOutLine ioLine = new MInOutLine(po.getCtx(), iLine.getM_InOutLine_ID(), po.get_TrxName());
				if(ioLine.get_ValueAsInt("A_Asset_ID") > 0)
					iLine.setA_Asset_ID(ioLine.get_ValueAsInt("A_Asset_ID"));
				if(ioLine.get_ValueAsInt("A_Asset_Group_ID") > 0)
					iLine.setA_Asset_Group_ID(ioLine.get_ValueAsInt("A_Asset_Group_ID"));
				if(iLine.get_ValueAsString("A_CapvsExp") != null && iLine.get_ValueAsString("A_CapvsExp").trim() != ""
					&& iLine.get_ValueAsString("A_CapvsExp").trim() != " ")
					iLine.setA_CapvsExp(ioLine.get_ValueAsString("A_CapvsExp"));
			}
		}
		if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE) && po.get_Table_ID()==MInOutLine.Table_ID)
		{
			MInOutLine ioLine = (MInOutLine) po;
			MInOut receipt = new MInOut(po.getCtx(), ioLine.getM_InOut_ID(), po.get_TrxName());
			if(receipt.getDocStatus().compareTo("VO") != 0 && ioLine.getC_OrderLine_ID() > 0)
			{
				MOrderLine oLine = new MOrderLine(po.getCtx(), ioLine.getC_OrderLine_ID(), po.get_TrxName());
				if(oLine.get_ValueAsInt("A_Asset_ID") > 0)
					ioLine.set_CustomColumn("A_Asset_ID", oLine.get_ValueAsInt("A_Asset_ID"));
				if(oLine.get_ValueAsInt("A_Asset_Group_ID") > 0)
					ioLine.set_CustomColumn("A_Asset_Group_ID",oLine.get_ValueAsInt("A_Asset_Group_ID"));
				if(oLine.get_ValueAsString("A_CapvsExp") != null && oLine.get_ValueAsString("A_CapvsExp").trim() != ""
					&& oLine.get_ValueAsString("A_CapvsExp").trim() != " ")
					ioLine.set_CustomColumn("A_CapvsExp",oLine.get_ValueAsString("A_CapvsExp"));
			}
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
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MInOut.Table_ID)
		{
			MInOut receipt = (MInOut) po;
			if(receipt.isSOTrx() == false)
			{
				createAsset(receipt);
			}
		}
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MInvoice.Table_ID)
		{
			MInvoice invoice = (MInvoice) po;
			if(invoice.isSOTrx() == false)
			{
				updateAsset(invoice);
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
		StringBuffer sb = new StringBuffer ("QSS_Validator");
		return sb.toString ();
	}	//	toString

	public void createAsset(MInOut receipt)	
	{	
		MInOutLine[] lines = receipt.getLines(false);
		int group_ID = 0;
		for (int lineIndex = 0; lineIndex < lines.length; lineIndex++)
		{
			MInOutLine sLine = lines[lineIndex];
			if(isAsset(sLine))
			{
				if(sLine.get_ValueAsString("A_CapvsExp").equals(X_C_InvoiceLine.A_CAPVSEXP_Capital)  )//crear activo
				{				
					group_ID = sLine.get_ValueAsInt("A_Asset_Group_ID");					
					if(group_ID <= 0)
					{
						group_ID = DB.getSQLValue(receipt.get_TrxName(), "select a_asset_group_id from a_asset_group where isdefault='Y'");
					    if(group_ID <= 0)
					    	throw new IllegalStateException("no default asset group");
					}
					BigDecimal qtyAsset = sLine.getQtyEntered();
					MAsset asset = new MAsset(receipt.getCtx(),  0 ,receipt.get_TrxName());
					asset.setName(sLine.getDescription());
					asset.setAD_Org_ID(sLine.getAD_Org_ID());
					asset.setAssetServiceDate(receipt.getMovementDate());
					asset.setQty(qtyAsset); //se ocupa variable manejable
					asset.setA_Asset_Group_ID(group_ID);					
					if(sLine.get_ValueAsInt("AD_User_ID") > 0)
						asset.setAD_User_ID(sLine.get_ValueAsInt("AD_user_ID"));
					asset.saveEx();
											
					sLine.set_CustomColumn("A_Asset_ID", asset.getA_Asset_ID());
					sLine.save();
						
					if(sLine.getC_OrderLine_ID() > 0)
					{
						MOrderLine oLine = new MOrderLine (receipt.getCtx() ,sLine.getC_OrderLine_ID(),receipt.get_TrxName());
						oLine.set_CustomColumn("A_Asset_ID", asset.getA_Asset_ID());
						oLine.save();
					}	
					
					X_A_Asset_Use  assetuse = new X_A_Asset_Use(receipt.getCtx(),  0 ,receipt.get_TrxName());
					assetuse.setA_Asset_ID(asset.getA_Asset_ID());
					assetuse.setUseDate(receipt.getMovementDate());
					assetuse.setUseUnits(1);
					if(sLine.get_ValueAsInt("AD_User_ID")>0)
					{
						assetuse.set_CustomColumn("AD_User_ID", sLine.get_ValueAsInt("AD_User_ID"));
						assetuse.save();
					}
					if(sLine.get_ValueAsInt("S_Resource_ID")>0)
					{
						assetuse.set_CustomColumn("S_Resource_ID", sLine.get_ValueAsInt("S_Resource_ID"));
						assetuse.save();
					}
				}
			}
		}
	}//endcreateAsset()
	private boolean isAsset(MInOutLine sLine)
	{
		if(sLine.getC_Charge_ID()>0)
		{
			  if(sLine.getC_Charge().getC_ChargeType_ID()>0)
				if(sLine.getC_Charge().getC_ChargeType().getValue().equals("TCAF"))
						return true;
		}
		if(sLine.getM_Product_ID()>0)
		{
			if(sLine.getM_Product().getM_Product_Category().getA_Asset_Group_ID()>0 )
				return true;
		}
		return false;
	}
	public void updateAsset(MInvoice inv)
	{		
		MInvoiceLine[] lines = inv.getLines(false);
		for (int lineIndex = 0; lineIndex < lines.length; lineIndex++)
		{
			MInvoiceLine sLine = lines[lineIndex];
			boolean createdUpdated = false;
			if(isAsset(sLine))
			{
				if(sLine.get_ValueAsInt("A_Asset_Group_ID")>0 && sLine.get_ValueAsString("A_CapvsExp").equals(X_C_InvoiceLine.A_CAPVSEXP_Capital))
				{
					int gacct_id = DB.getSQLValue(inv.get_TrxName(), "select a_asset_group_acct_id from a_asset_group_acct where a_asset_group_id="+sLine.get_ValueAsInt("A_Asset_Group_ID"));
					MAssetGroupAcct assetgrpacct = new MAssetGroupAcct (inv.getCtx(), gacct_id, inv.get_TrxName());
					
					MAsset asset = new MAsset(inv.getCtx(),sLine.getA_Asset_ID(),inv.get_TrxName());
					asset.setA_Asset_Group_ID(sLine.get_ValueAsInt("A_Asset_Group_ID"));
					asset.setUseLifeYears(assetgrpacct.getUseLifeYears());
					asset.setUseLifeMonths(assetgrpacct.getUseLifeMonths());
					asset.setIsDepreciated(true);
					asset.setIsOwned(true);
					//ininoles nuevo campo
					asset.saveEx();
				
					int acct_id = DB.getSQLValue(inv.get_TrxName(), "select A_Asset_Acct_ID from A_Asset_Acct where A_Asset_ID="+asset.getA_Asset_ID());
					MAssetAcct assetacct = new MAssetAcct (inv.getCtx(), acct_id, inv.get_TrxName());
					assetacct.setPostingType(assetgrpacct.getPostingType());
					assetacct.setA_Split_Percent(assetgrpacct.getA_Split_Percent());
					assetacct.setA_Depreciation_Conv_ID(assetgrpacct.getConventionType());
					//assetacct.setA_Depreciation_ID(assetgrpacct.getDepreciationType());
					assetacct.setA_Depreciation_ID(assetgrpacct.getA_Depreciation_ID());
					assetacct.setA_Depreciation_F_ID(assetgrpacct.getA_Depreciation_F_ID());
					
					assetacct.setA_Asset_Spread_ID(assetgrpacct.getA_Asset_Spread_Type());
					assetacct.setA_Period_Start(1);
					assetacct.setA_Period_End(assetgrpacct.getUseLifeMonths());
					assetacct.setA_Depreciation_Method_ID(assetgrpacct.getA_Depreciation_Calc_Type());
					assetacct.setA_Asset_Acct(assetgrpacct.getA_Asset_Acct());
					assetacct.setC_AcctSchema_ID(assetgrpacct.getC_AcctSchema_ID());
					assetacct.setA_Accumdepreciation_Acct(assetgrpacct.getA_Accumdepreciation_Acct());
					assetacct.setA_Depreciation_Acct(assetgrpacct.getA_Depreciation_Acct());
					//assetacct.setA_Disposal_Revenue(assetgrpacct.getA_Disposal_Revenue());
					//assetacct.setA_Disposal_Loss(assetgrpacct.getA_Disposal_Loss());
					assetacct.setA_Reval_Accumdep_Offset_Cur(assetgrpacct.getA_Reval_Accumdep_Offset_Cur());
					assetacct.setA_Reval_Accumdep_Offset_Prior(assetgrpacct.getA_Reval_Accumdep_Offset_Prior());
					assetacct.setA_Reval_Cost_Offset(assetgrpacct.getA_Reval_Cost_Offset());
					assetacct.setA_Reval_Cost_Offset_Prior(assetgrpacct.getA_Reval_Cost_Offset_Prior());
					assetacct.setA_Reval_Depexp_Offset(assetgrpacct.getA_Reval_Depexp_Offset());
					assetacct.setA_Depreciation_Manual_Amount(assetgrpacct.getA_Depreciation_Manual_Amount());
					assetacct.setA_Depreciation_Manual_Period(assetgrpacct.getA_Depreciation_Manual_Period());
					assetacct.setA_Depreciation_Table_Header_ID(assetgrpacct.getA_Depreciation_Table_Header_ID());
					assetacct.setA_Depreciation_Variable_Perc(assetgrpacct.getA_Depreciation_Variable_Perc());
					assetacct.setProcessing(false);
					//assetacct.set_Value("A_Salvage_Value",assetgrpacct.get_ValueAsInt("A_Salvage_Value")); 
					/*
					assetacct.set_Value("A_Salvage_Value",assetgrpacct.get_Value("A_Salvage_Value"));//ininoles camnio a bigdecimal para evitar error
					assetacct.set_Value("A_Disposal_RevenueD_Acct",assetgrpacct.get_ValueAsInt("A_Disposal_RevenueD_Acct"));
					assetacct.set_Value("A_Disposal_RevenueC_Acct",assetgrpacct.get_ValueAsInt("A_Disposal_RevenueC_Acct"));
					assetacct.set_Value("A_Disposal_Loss_Acct",assetgrpacct.get_ValueAsInt("A_Disposal_Loss_Acct"));
					assetacct.set_Value("A_Disposal_Gain_Acct",assetgrpacct.get_ValueAsInt("A_Disposal_Gain_Acct"));
					assetacct.set_Value("A_AssetComplement_Acct",assetgrpacct.get_ValueAsInt("A_AssetComplement_Acct"));
					assetacct.save();
					*/
					log.config("MAssetChange change");
					int change_id = DB.getSQLValue(inv.get_TrxName(), "select A_Asset_Change_ID from A_Asset_Change where A_Asset_ID="+asset.getA_Asset_ID());
					MAssetChange change = new MAssetChange (inv.getCtx(), change_id, inv.get_TrxName());
					//ininoles se setea activo
					change.setA_Asset_ID(asset.getA_Asset_ID());
					change.setPostingType(assetacct.getPostingType());
					change.setA_Split_Percent(assetacct.getA_Split_Percent());
					change.setConventionType(assetacct.getA_Depreciation_Conv_ID());
					//change.setA_Asset_ID(asset.getA_Asset_ID());								
					change.setDepreciationType(assetacct.getA_Depreciation_ID());
					change.setA_Asset_Spread_Type(assetacct.getA_Asset_Spread_ID());
					change.setA_Period_Start(assetacct.getA_Period_Start());
					change.setA_Period_End(assetacct.getA_Period_End());
					change.setIsInPosession(asset.isOwned());
					change.setIsDisposed(asset.isDisposed());
					change.setIsDepreciated(asset.isDepreciated());
					change.setIsFullyDepreciated(asset.isFullyDepreciated());					
					change.setA_Depreciation_Calc_Type(assetacct.getA_Depreciation_Method_ID());
					change.setA_Asset_Acct(assetacct.getA_Asset_Acct());
					change.setC_AcctSchema_ID(assetacct.getC_AcctSchema_ID());
					change.setA_Accumdepreciation_Acct(assetacct.getA_Accumdepreciation_Acct());
					change.setA_Depreciation_Acct(assetacct.getA_Depreciation_Acct());
					change.setA_Disposal_Revenue(assetacct.getA_Disposal_Revenue());
					change.setA_Disposal_Loss(assetacct.getA_Disposal_Loss());
					change.setA_Reval_Accumdep_Offset_Cur(assetacct.getA_Reval_Accumdep_Offset_Cur());
					change.setA_Reval_Accumdep_Offset_Prior(assetacct.getA_Reval_Accumdep_Offset_Prior());
					change.setA_Reval_Cal_Method(assetacct.getA_Reval_Cal_Method());
					change.setA_Reval_Cost_Offset(assetacct.getA_Reval_Cost_Offset());
					change.setA_Reval_Cost_Offset_Prior(assetacct.getA_Reval_Cost_Offset_Prior());
					change.setA_Reval_Depexp_Offset(assetacct.getA_Reval_Depexp_Offset());
					change.setA_Depreciation_Manual_Amount(assetacct.getA_Depreciation_Manual_Amount());
					change.setA_Depreciation_Manual_Period(assetacct.getA_Depreciation_Manual_Period());
					change.setA_Depreciation_Table_Header_ID(assetacct.getA_Depreciation_Table_Header_ID());
					change.setA_Depreciation_Variable_Perc(assetacct.getA_Depreciation_Variable_Perc());
					change.setA_Parent_Asset_ID(asset.getA_Parent_Asset_ID());
				    change.setChangeType("CRT");	
					change.setTextDetails(MRefList.getListDescription (inv.getCtx(),"A_Update_Type" , "CRT"));		    
				    change.setIsInPosession(asset.isOwned());
					change.setIsDisposed(asset.isDisposed());
					change.setIsDepreciated(asset.isDepreciated());
					change.setIsFullyDepreciated(asset.isFullyDepreciated());
					change.setLot(asset.getLot());
					change.setSerNo(asset.getSerNo());
					change.setVersionNo(asset.getVersionNo());
				    change.setUseLifeMonths(asset.getUseLifeMonths());
				    change.setUseLifeYears(asset.getUseLifeYears());
				    change.setLifeUseUnits(asset.getLifeUseUnits());
				    change.setAssetDisposalDate(asset.getAssetDisposalDate());
				    change.setAssetServiceDate(asset.getAssetServiceDate());
				    change.setC_BPartner_Location_ID(asset.getC_BPartner_Location_ID());
				    change.setC_BPartner_ID(asset.getC_BPartner_ID());
				    change.setAssetValueAmt(sLine.getLineTotalAmt());
				    change.setA_QTY_Current(sLine.getQtyEntered());
				    change.setA_QTY_Original(sLine.getQtyEntered());
				    change.setA_Asset_CreateDate(asset.getA_Asset_CreateDate());
				    change.setAD_User_ID(asset.getAD_User_ID());
				    change.setC_Location_ID(asset.getC_Location_ID());
				    //faaguilar OFB begin
				    change.setAssetValueAmt(sLine.getLineNetAmt().divide(sLine.getQtyEntered(), BigDecimal.ROUND_HALF_EVEN));
				    change.setA_QTY_Current(Env.ONE);
				    change.setA_QTY_Original(Env.ONE);
				    //faaguilar OFB begin
				    change.save();
				    
				    log.config("X_A_Depreciation_Workfile");
				    int Workfile_id = DB.getSQLValue(inv.get_TrxName(), "select A_Depreciation_Workfile_ID from A_Depreciation_Workfile where A_Asset_ID="+asset.getA_Asset_ID());
					X_A_Depreciation_Workfile assetwk = new X_A_Depreciation_Workfile (inv.getCtx(), Workfile_id, inv.get_TrxName());
					//assetwk.setA_Asset_ID(asset.getA_Asset_ID());		
					assetwk.setA_Life_Period(assetgrpacct.getUseLifeMonths());
					assetwk.setA_Asset_Life_Years(assetgrpacct.getUseLifeYears());
					assetwk.setA_Asset_Cost(assetwk.getA_Asset_Cost().add(sLine.getLineTotalAmt()));							
					assetwk.setA_QTY_Current(sLine.getQtyEntered());
					assetwk.setIsDepreciated(assetgrpacct.isProcessing());
					assetwk.setPostingType(assetgrpacct.getPostingType());
					assetwk.setA_Accumulated_Depr(new BigDecimal (0.0));
					assetwk.setA_Period_Posted(0);
					assetwk.setA_Asset_Life_Current_Year(new BigDecimal (0.0));
					assetwk.setA_Curr_Dep_Exp(new BigDecimal (0.0));
					//faaguilar OFB begin
					assetwk.setA_Asset_Cost(sLine.getLineNetAmt().divide(sLine.getQtyEntered(), BigDecimal.ROUND_HALF_EVEN));							
					assetwk.setA_QTY_Current(Env.ONE);
					//faaguilar OFB end
					assetwk.save();
					
					log.config("X_A_Asset_Addition");
					int Addition_id = DB.getSQLValue(inv.get_TrxName(), "select A_Asset_Addition_ID from A_Asset_Addition where A_Asset_ID="+asset.getA_Asset_ID());
					X_A_Asset_Addition assetadd = new X_A_Asset_Addition (inv.getCtx(), Addition_id, inv.get_TrxName());
					assetadd.setA_Asset_ID(asset.getA_Asset_ID());
					assetadd.setAssetValueAmt(sLine.getLineTotalAmt());
					assetadd.setA_SourceType("INV");
					assetadd.setA_CapvsExp("Cap");
					//assetadd.setM_InOutLine_ID(rs.getInt("C_Invoice_ID"));				
					assetadd.setC_Invoice_ID(inv.getC_Invoice_ID());
					assetadd.setDocumentNo(inv.getDocumentNo());
					assetadd.setLine(sLine.getLine());
					assetadd.setDescription(sLine.getDescription());
					assetadd.setA_QTY_Current(sLine.getQtyEntered());
					assetadd.setPostingType(assetwk.getPostingType());
					//faaguilar OFB begin
					assetadd.setA_QTY_Current(Env.ONE);
					assetadd.setAssetValueAmt(sLine.getLineNetAmt().divide(sLine.getQtyEntered(), BigDecimal.ROUND_HALF_EVEN));
					assetadd.setIsApproved(false);
					assetadd.setC_Currency_ID(inv.getC_Currency_ID());
					//faaguilar OFB end
					assetadd.save();
					DB.executeUpdate("Delete from A_Asset_Forecast where Processed='N' and Corrected='N' and A_Asset_ID="+asset.getA_Asset_ID(), inv.get_TrxName());
					CreateAssetForecast.createForecast(asset, change, assetacct, inv.get_TrxName());
				
					createdUpdated = true;
				}
				if(sLine.getA_Asset_ID()>0 && sLine.get_ValueAsString("A_CapvsExp").equals(X_C_InvoiceLine.A_CAPVSEXP_Expense))//gasto relacionado con un activo
				{
					MAsset asset = new MAsset(inv.getCtx(),  sLine.getA_Asset_ID() ,inv.get_TrxName());
					BigDecimal monto = sLine.getLineNetAmt();
					
					int change_id = DB.getSQLValue(inv.get_TrxName(), "select A_Asset_Change_ID from A_Asset_Change where A_Asset_ID="+asset.getA_Asset_ID());
					MAssetChange change = new MAssetChange (inv.getCtx(), change_id, inv.get_TrxName());
					change.setAssetValueAmt(change.getAssetValueAmt().add(monto) );
					change.save();
					
					int Workfile_id = DB.getSQLValue(inv.get_TrxName(), "select A_Depreciation_Workfile_ID from A_Depreciation_Workfile where A_Asset_ID="+asset.getA_Asset_ID());
					X_A_Depreciation_Workfile assetwk = new X_A_Depreciation_Workfile (inv.getCtx(), Workfile_id, inv.get_TrxName());
					assetwk.setA_Asset_Cost(assetwk.getA_Asset_Cost().add(monto));
					assetwk.save();
					
					X_A_Asset_Addition assetadd = new X_A_Asset_Addition (inv.getCtx(), 0, inv.get_TrxName());
					assetadd.setA_Asset_ID(asset.getA_Asset_ID());
					assetadd.setAssetValueAmt(sLine.getLineTotalAmt());
					assetadd.setA_SourceType("INV");
					assetadd.setA_CapvsExp("Exp");			
					assetadd.setC_Invoice_ID(inv.getC_Invoice_ID());
					assetadd.setDocumentNo(inv.getDocumentNo());
					assetadd.setLine(sLine.getLine());
					assetadd.setDescription(sLine.getDescription());
					assetadd.setA_QTY_Current(sLine.getQtyEntered());
					assetadd.setPostingType(assetwk.getPostingType());
					assetadd.setA_QTY_Current(sLine.getQtyInvoiced());
					assetadd.setAssetValueAmt(sLine.getLineNetAmt());
					assetadd.save();
					
					int acct_id = DB.getSQLValue(inv.get_TrxName(), "select A_Asset_Acct_ID from A_Asset_Acct where A_Asset_ID="+asset.getA_Asset_ID());
					MAssetAcct assetacct = new MAssetAcct (inv.getCtx(), acct_id, inv.get_TrxName());
					DB.executeUpdate("Delete from A_Asset_Forecast where Processed='N' and Corrected='N' and A_Asset_ID="+asset.getA_Asset_ID(), inv.get_TrxName());
					CreateAssetForecast.createForecast(asset, change, assetacct, inv.get_TrxName());
					
					createdUpdated = true;
				}
				if(createdUpdated)
				{
					sLine.setA_Processed(true);
					sLine.save();
				}	
			}
		}//fin for
	}//updateAssetAsset()
		
	private boolean isAsset(MInvoiceLine sLine)
	{
		if(sLine.getC_Charge_ID()>0)
		{
			  if(sLine.getC_Charge().getC_ChargeType_ID()>0)
				if( (sLine.getC_Charge().getC_ChargeType().getValue().equals("TCAF")) )
						return true;
		}
		if(sLine.getM_Product_ID()>0)
		{
			if(sLine.getM_Product().getM_Product_Category().getA_Asset_Group_ID()>0 )
				return true;
		}
		return false;
	}
}	