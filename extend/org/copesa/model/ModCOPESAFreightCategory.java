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
package org.copesa.model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPriceList;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;


/**
 *	Validator for COPESA
 *
 *  @author Italo Ni�oles
 */
public class ModCOPESAFreightCategory implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCOPESAFreightCategory ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCOPESAFreightCategory.class);
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
		engine.addModelChange(MOrder.Table_Name, this);		
		engine.addModelChange(MOrderLine.Table_Name, this);
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     *	OFB Consulting Ltda. By Julio Far�as
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if((type == TYPE_AFTER_CHANGE || type == TYPE_AFTER_NEW) && po.get_Table_ID()==MOrder.Table_ID && po.is_ValueChanged("DocStatus")) 
		{	
			MOrder order = (MOrder)po;
			/*if (order.isSOTrx() && order.getDocStatus().compareToIgnoreCase("IP") == 0)
			{	
				//borrado de lineas ya calculadas 
				String sql = "DELETE FROM C_OrderLine WHERE C_OrderLine_ID IN " +
						" (SELECT C_OrderLine_ID FROM C_OrderLine WHERE C_Order_ID = " + order.get_ID()+
						" AND C_Charge_ID IN ( SELECT C_Charge_ID FROM C_Charge cc INNER JOIN C_ChargeType ct ON (cc.C_ChargeType_ID = ct.C_ChargeType_ID) " +
						" WHERE ct.value IN ('TCFU','TCFP')))";
				DB.executeUpdate(sql, po.get_TrxName());
				//calculo de geozona
				//MBPartnerLocation loc = new MBPartnerLocation(po.getCtx(), order.getC_BPartner_Location_ID(), po.get_TrxName());
				//int ID_Geozone = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(gc.C_Geozone_ID) " +
				//		"FROM C_GeozoneCities gc WHERE gc.C_City_ID = "+loc.get_ValueAsInt("C_City_ID")+" AND gc.IsActive = 'Y'");
				//flete por unidad
				//buscamos cantidad de productos por unidad
				//ininoles se cambia codigo para que sean varios tipos de despacho por cantidad
				//ininoles nuevocambio distintas direcciones de despacho
				//if(ID_Geozone > 0)
				//{
					BigDecimal totalAmtQty = Env.ZERO;
					String sqlQty = "SELECT SUM(qtyEntered) as qtyEntered, fc.value,loc.C_Geozone_ID  " +
							" FROM C_OrderLine col " +
							" INNER JOIN M_Product mp ON(col.M_Product_ID = mp.M_Product_ID) " +
							" INNER JOIN M_FreightCategory fc ON(mp.M_FreightCategory_ID = fc.M_FreightCategory_ID) " +
							" INNER JOIN C_BPartner_Location loc ON (loc.C_BPartner_Location_ID = col.C_BPartner_Location_ID)" +
							" WHERE fc.value LIKE 'CFPU%' AND C_Order_ID = ? GROUP BY fc.value, loc.C_Geozone_ID "; 
					
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try
					{
						pstmt = DB.prepareStatement(sqlQty, po.get_TrxName());
						pstmt.setInt(1,order.get_ID());
						rs = pstmt.executeQuery();
						while(rs.next())
						{
							BigDecimal amtWeight = DB.getSQLValueBD(po.get_TrxName(), "SELECT MAX(amt) " +
									" FROM M_FCategoryValues cv " +
									" INNER JOIN M_FreightCategory fc ON(cv.M_FreightCategory_ID = fc.M_FreightCategory_ID) " +
									" WHERE fc.value = '"+rs.getString("value")+"' AND ? BETWEEN qtymin AND qtymax AND C_Geozone_ID = "+rs.getInt("C_Geozone_ID"), rs.getBigDecimal("qtyEntered"));
							if(amtWeight != null)
								totalAmtQty = totalAmtQty.add(amtWeight);							
						}
						rs.close();
						pstmt.close();
						pstmt = null;
					}
					catch (SQLException e)
					{
						//throw new DBException(e, sql);
						log.config(e.toString());
					}
					finally
					{
						DB.close(rs, pstmt);
						rs = null; pstmt = null;
					}		
					if(totalAmtQty != null && totalAmtQty.compareTo(Env.ZERO) > 0)
					{
						//se genera linea con monto ya calculado
						//buscamos cargo por peso
						int ID_Charge = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(C_Charge_ID) " +
								" FROM C_Charge cc " +
								" INNER JOIN C_ChargeType ct ON (cc.C_ChargeType_ID = ct.C_ChargeType_ID) " +
								" WHERE ct.value = 'TCFU'");
						if(ID_Charge > 0)
						{
							//MOrderLine oLine = new MOrderLine(po.getCtx(), 0, po.get_TrxName());
							MOrderLine oLine = new MOrderLine(order);
							oLine.setAD_Org_ID(order.getAD_Org_ID());
							oLine.setC_BPartner_Location_ID(order.getC_BPartner_Location_ID());						
							oLine.setC_Charge_ID(ID_Charge);
							oLine.setQty(Env.ONE);
							oLine.setPrice(totalAmtQty);
							oLine.setDescription("Agregado Automatico. Flete por Cantidad");
							//oLine.setTax();
							oLine.save();
							oLine.setLineNetAmt();						
							oLine.save();
						}	
					}
					//flete por peso
					//buscamos peso de productos
					//ininoles se cambia forma para tener varios tipos de despacho				
					BigDecimal totalAmtWeight = Env.ZERO;
					String sqlWeight = "SELECT SUM(qtyEntered * mp.weight) as weight, fc.value, loc.C_Geozone_ID " +
							" FROM C_OrderLine col " +
							" INNER JOIN M_Product mp ON(col.M_Product_ID = mp.M_Product_ID) " +
							" INNER JOIN M_FreightCategory fc ON(mp.M_FreightCategory_ID = fc.M_FreightCategory_ID) " +
							" INNER JOIN C_BPartner_Location loc ON (loc.C_BPartner_Location_ID = col.C_BPartner_Location_ID) "+
							" WHERE fc.value LIKE 'CFPP%' AND C_Order_ID = ? GROUP BY fc.value,loc.C_Geozone_ID";
					PreparedStatement pstmtW = null;
					ResultSet rsW = null;
					try
					{
						pstmtW = DB.prepareStatement(sqlWeight, po.get_TrxName());
						pstmtW.setInt(1, order.get_ID());
						rsW = pstmtW.executeQuery();
						while (rsW.next())
						{
							BigDecimal amtWeight = DB.getSQLValueBD(po.get_TrxName(), "SELECT MAX(amt) " +
									" FROM M_FCategoryValues cv " +
									" INNER JOIN M_FreightCategory fc ON(cv.M_FreightCategory_ID = fc.M_FreightCategory_ID) " +
									" WHERE fc.value = '"+rsW.getString("value")+"' AND ? BETWEEN qtymin AND qtymax AND C_Geozone_ID = "+rsW.getInt("C_Geozone_ID"), rsW.getBigDecimal("weight"));
							if(amtWeight != null)
								totalAmtWeight = totalAmtWeight.add(amtWeight);							
						}
						rsW.close();
						pstmtW.close();
						pstmtW = null;
					}
					catch (SQLException e)
					{
						//throw new DBException(e, sql);
						log.config(e.toString());
					}
					finally
					{
						DB.close(rsW, pstmtW);
						rsW = null; pstmtW = null;
					}		
					if(totalAmtWeight != null && totalAmtWeight.compareTo(Env.ZERO) > 0)
					{
						//se genera linea con monto ya calculado
						//buscamos cargo por peso
						int ID_Chargew = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(C_Charge_ID) " +
								" FROM C_Charge cc " +
								" INNER JOIN C_ChargeType ct ON (cc.C_ChargeType_ID = ct.C_ChargeType_ID) " +
								" WHERE ct.value = 'TCFP'");
						if(ID_Chargew > 0)
						{
							//MOrderLine oLine = new MOrderLine(po.getCtx(), 0, po.get_TrxName());
							MOrderLine oLine = new MOrderLine(order);
							oLine.setAD_Org_ID(order.getAD_Org_ID());
							oLine.setC_BPartner_Location_ID(order.getC_BPartner_Location_ID());						
							oLine.setC_Charge_ID(ID_Chargew);
							oLine.setQty(Env.ONE);
							oLine.setPrice(totalAmtWeight);
							oLine.setDescription("Agregado Automatico. Flete por Peso");
							//oLine.setTax();
							oLine.save();
							oLine.setLineNetAmt();						
							oLine.save();
						}	
					}
				//}
			}*/
			//llamamos metodo para actualizar
			UpdateFreight(order);
		}		
		if((type == TYPE_AFTER_CHANGE || type == TYPE_AFTER_NEW) && po.get_Table_ID()==MOrderLine.Table_ID && (po.is_ValueChanged("C_BPartner_Location_ID") || po.is_ValueChanged("DeliveryViaRule"))) 
		{
			MOrderLine oLine = (MOrderLine)po;
			//MOrder order = new MOrder(po.getCtx(), oLine.getC_Order_ID(), po.get_TrxName());
			MOrder order = oLine.getParent();
			UpdateFreight(order);
		}
		if((type == TYPE_AFTER_CHANGE || type == TYPE_AFTER_NEW) && po.get_Table_ID()==MOrder.Table_ID && po.is_ValueChanged("C_BPartner_Location_ID") ) 
		{	
			MOrder order = (MOrder)po;
			UpdateFreight(order);
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
	
	public void UpdateFreight(MOrder order)
	{
		if (order.isSOTrx() && order.getDocStatus().compareToIgnoreCase("IP") == 0)
		{
			MPriceList pl = new MPriceList(order.getCtx(), order.getM_PriceList_ID(), order.get_TrxName());
			if(!pl.get_ValueAsBoolean("NoFreight"))
			{
				//borrado de lineas ya calculadas 
				String sql = "DELETE FROM C_OrderLine WHERE C_OrderLine_ID IN " +
						" (SELECT C_OrderLine_ID FROM C_OrderLine WHERE C_Order_ID = " + order.get_ID()+
						" AND C_Charge_ID IN ( SELECT C_Charge_ID FROM C_Charge cc INNER JOIN C_ChargeType ct ON (cc.C_ChargeType_ID = ct.C_ChargeType_ID) " +
						" WHERE ct.value IN ('TCFU','TCFP')))";
				DB.executeUpdate(sql, order.get_TrxName());
				
				DB.executeUpdate(" UPDATE C_Order co SET TotalLines = " +
						" (select ROUND(SUM(LineNetAmt)) from C_OrderLine where C_Order_ID=co.c_Order_ID) " +
						" WHERE C_Order_ID = "+order.get_ID(), order.get_TrxName());
				
				DB.executeUpdate(" UPDATE C_Order co SET GrandTotal = ROUND(TotalLines + " +
						" (select SUM(TAXAMT) from C_OrderTax where C_Order_ID=co.c_Order_ID)) " +
						" WHERE C_Order_ID = "+order.get_ID(), order.get_TrxName());
				//ininoles se actualizan montos en caso que no se generen lineas
				/*order.calculateTaxTotal();
				order.save();*/
				
				//calculo de geozona
				//MBPartnerLocation loc = new MBPartnerLocation(po.getCtx(), order.getC_BPartner_Location_ID(), po.get_TrxName());
				//int ID_Geozone = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(gc.C_Geozone_ID) " +
				//		"FROM C_GeozoneCities gc WHERE gc.C_City_ID = "+loc.get_ValueAsInt("C_City_ID")+" AND gc.IsActive = 'Y'");
				//flete por unidad
				//buscamos cantidad de productos por unidad
				//ininoles se cambia codigo para que sean varios tipos de despacho por cantidad
				//ininoles nuevocambio distintas direcciones de despacho
				//if(ID_Geozone > 0)
				//{
					//BigDecimal totalAmtQty = Env.ZERO;
					//BigDecimal totalAmtQtyPAT = Env.ZERO;
				    //ininoles se cambia de donde sale la geozona- Geozonas Editoriales
					String sqlQty = "SELECT SUM(qtyEntered) as qtyEntered, fc.value,cg.C_Geozone_ID,col.line,col.M_Product_ID, " +
							" col.C_BPartner_Location_ID,col.C_OrderLine_ID " +
							" FROM C_OrderLine col " +
							" INNER JOIN M_Product mp ON(col.M_Product_ID = mp.M_Product_ID) " +
							" INNER JOIN M_FreightCategory fc ON(mp.M_FreightCategory_ID = fc.M_FreightCategory_ID) " +
							" INNER JOIN C_BPartner_Location loc ON (loc.C_BPartner_Location_ID = col.C_BPartner_Location_ID)" +
							" INNER JOIN C_GeozoneCities cgc ON (loc.C_City_ID = cgc.C_City_ID)"+
							" INNER JOIN C_Geozone cg ON (cgc.C_Geozone_ID = cg.C_Geozone_ID AND type like 'E') "+
							" WHERE fc.value LIKE 'CFPU%' " +
							" AND (col.DeliveryViaRule NOT IN ('D','P') OR col.DeliveryViaRule IS NULL)" +
							" AND C_Order_ID = ? GROUP BY fc.value, cg.C_Geozone_ID,col.C_BPartner_Location_ID,col.M_Product_ID,col.line,col.C_OrderLine_ID "; 
					
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					try
					{
						pstmt = DB.prepareStatement(sqlQty, order.get_TrxName());
						pstmt.setInt(1,order.get_ID());
						rs = pstmt.executeQuery();
						while(rs.next())
						{
							//se genera linea para ser usada despues
							MOrderLine oLineShip = new MOrderLine(order.getCtx(), rs.getInt("C_OrderLine_ID"), order.get_TrxName());
							
							/*BigDecimal amtWeight = DB.getSQLValueBD(order.get_TrxName(), "SELECT MAX(amt) " +
									" FROM M_FCategoryValues cv " +
									" INNER JOIN M_FreightCategory fc ON(cv.M_FreightCategory_ID = fc.M_FreightCategory_ID) " +
									" WHERE fc.value = '"+rs.getString("value")+"' AND ? BETWEEN qtymin AND qtymax AND C_Geozone_ID = "+rs.getInt("C_Geozone_ID"), rs.getBigDecimal("qtyEntered"));*/
							BigDecimal amtWeight = DB.getSQLValueBD(order.get_TrxName(), "SELECT MAX(amt) " +
									" FROM M_FCategoryValues cv " +
									" INNER JOIN M_FreightCategory fc ON(cv.M_FreightCategory_ID = fc.M_FreightCategory_ID) " +
									" WHERE fc.value = '"+rs.getString("value")+"' AND C_Geozone_ID = "+rs.getInt("C_Geozone_ID"));
							BigDecimal amtWeightPAT = DB.getSQLValueBD(order.get_TrxName(), "SELECT MAX(MonthlyAmount) " +
									" FROM M_FCategoryValues cv " +
									" INNER JOIN M_FreightCategory fc ON(cv.M_FreightCategory_ID = fc.M_FreightCategory_ID) " +
									" WHERE fc.value = '"+rs.getString("value")+"' AND C_Geozone_ID = "+rs.getInt("C_Geozone_ID"));
							if(amtWeight != null)
							{
								//en vez de aumentar variable, se generan lineas 
								amtWeight = amtWeight.multiply(rs.getBigDecimal("qtyEntered"));
								//totalAmtQty = totalAmtQty.add(amtWeight);
								/*if(amtWeightPAT != null)
									totalAmtQtyPAT = totalAmtQtyPAT.add(amtWeightPAT);*/
								amtWeightPAT = amtWeightPAT.multiply(rs.getBigDecimal("qtyEntered"));
								
								int ID_Charge = DB.getSQLValue(order.get_TrxName(), "SELECT MAX(C_Charge_ID) " +
										" FROM C_Charge cc " +
										" INNER JOIN C_ChargeType ct ON (cc.C_ChargeType_ID = ct.C_ChargeType_ID) " +
										" WHERE ct.value = 'TCFU'");
								if(ID_Charge > 0)
								{
									//MOrderLine oLine = new MOrderLine(po.getCtx(), 0, po.get_TrxName());
									MOrderLine oLine = new MOrderLine(order);
									oLine.setAD_Org_ID(order.getAD_Org_ID());
									oLine.setC_BPartner_Location_ID(order.getC_BPartner_Location_ID());						
									oLine.setC_Charge_ID(ID_Charge);
									oLine.setQty(Env.ONE);
									oLine.setPrice(amtWeight);
									oLine.setDescription("Agregado Automatico. Flete por Cantidad. Linea: "+rs.getInt("Line"));
									//oLine.setTax();
									oLine.save();
									oLine.setLineNetAmt();
									oLine.set_CustomColumn("DatePromised2",order.getDateOrdered());
									oLine.set_CustomColumn("DatePromised3",order.getDateOrdered());
									oLine.set_CustomColumn("MonthlyAmount",amtWeightPAT);
									oLine.set_CustomColumn("m_productref_id",rs.getInt("M_Product_ID"));
									//ininoles 04-04 nuevos campos pedidos por humberto
									oLine.setC_BPartner_Location_ID(oLineShip.getC_BPartner_Location_ID());
									oLine.set_CustomColumn("C_Geozone_ID",oLineShip.get_ValueAsInt("C_Geozone_ID"));
									oLine.setC_BPartner_ID(oLineShip.getC_BPartner_ID());
									//ininoles end 04-04
									oLine.save();
								}	
							}
						}
						rs.close();
						pstmt.close();
						pstmt = null;
					}
					catch (SQLException e)
					{
						//throw new DBException(e, sql);
						log.config(e.toString());
					}
					finally
					{
						DB.close(rs, pstmt);
						rs = null; pstmt = null;
					}
					/*
					if(totalAmtQty != null && totalAmtQty.compareTo(Env.ZERO) > 0)
					{
						//se genera linea con monto ya calculado
						//buscamos cargo por peso
						int ID_Charge = DB.getSQLValue(order.get_TrxName(), "SELECT MAX(C_Charge_ID) " +
								" FROM C_Charge cc " +
								" INNER JOIN C_ChargeType ct ON (cc.C_ChargeType_ID = ct.C_ChargeType_ID) " +
								" WHERE ct.value = 'TCFU'");
						if(ID_Charge > 0)
						{
							//MOrderLine oLine = new MOrderLine(po.getCtx(), 0, po.get_TrxName());
							MOrderLine oLine = new MOrderLine(order);
							oLine.setAD_Org_ID(order.getAD_Org_ID());
							oLine.setC_BPartner_Location_ID(order.getC_BPartner_Location_ID());						
							oLine.setC_Charge_ID(ID_Charge);
							oLine.setQty(Env.ONE);
							oLine.setPrice(totalAmtQty);
							oLine.setDescription("Agregado Automatico. Flete por Cantidad");
							//oLine.setTax();
							oLine.save();
							oLine.setLineNetAmt();
							oLine.set_CustomColumn("DatePromised2",order.getDateOrdered());
							oLine.set_CustomColumn("DatePromised3",order.getDateOrdered());
							oLine.set_CustomColumn("MonthlyAmount",totalAmtQtyPAT);
							oLine.save();
						}	
					}*/
					//flete por peso
					//buscamos peso de productos
					//ininoles se cambia forma para tener varios tipos de despacho
					BigDecimal totalAmtWeight = Env.ZERO;
					BigDecimal totalAmtWeightPAT = Env.ZERO;
					//ininoles se cambia de donde sale la geozona - Geozonas ganchos
					String sqlWeight = "SELECT SUM(qtyEntered * mp.weight) as weight, fc.value, cg.C_Geozone_ID, MAX(col.M_Product_ID) as M_Product_ID" +
							" FROM C_OrderLine col " +
							" INNER JOIN M_Product mp ON(col.M_Product_ID = mp.M_Product_ID) " +
							" INNER JOIN M_FreightCategory fc ON(mp.M_FreightCategory_ID = fc.M_FreightCategory_ID) " +
							" INNER JOIN C_BPartner_Location loc ON (loc.C_BPartner_Location_ID = col.C_BPartner_Location_ID) "+
							" INNER JOIN C_GeozoneCities cgc ON (loc.C_City_ID = cgc.C_City_ID)"+
							" INNER JOIN C_Geozone cg ON (cgc.C_Geozone_ID = cg.C_Geozone_ID AND type like 'G') "+
							" WHERE fc.value LIKE 'CFPP%' " +
							" AND (col.DeliveryViaRule NOT IN ('D','P') OR col.DeliveryViaRule IS NULL) " +
							" AND C_Order_ID = ? GROUP BY fc.value,cg.C_Geozone_ID,col.C_BPartner_Location_ID ";
					int ID_Prod = 0;
					
					PreparedStatement pstmtW = null;
					ResultSet rsW = null;
					try
					{
						pstmtW = DB.prepareStatement(sqlWeight, order.get_TrxName());
						pstmtW.setInt(1, order.get_ID());
						rsW = pstmtW.executeQuery();
						while (rsW.next())
						{
							BigDecimal amtWeight = DB.getSQLValueBD(order.get_TrxName(), "SELECT MAX(amt) " +
									" FROM M_FCategoryValues cv " +
									" INNER JOIN M_FreightCategory fc ON(cv.M_FreightCategory_ID = fc.M_FreightCategory_ID) " +
									" WHERE fc.value = '"+rsW.getString("value")+"' AND ? BETWEEN qtymin AND qtymax AND C_Geozone_ID = "+rsW.getInt("C_Geozone_ID"), rsW.getBigDecimal("weight"));
							BigDecimal amtWeightPAT = DB.getSQLValueBD(order.get_TrxName(), "SELECT MAX(MonthlyAmount) " +
									" FROM M_FCategoryValues cv " +
									" INNER JOIN M_FreightCategory fc ON(cv.M_FreightCategory_ID = fc.M_FreightCategory_ID) " +
									" WHERE fc.value = '"+rsW.getString("value")+"' AND C_Geozone_ID = "+rsW.getInt("C_Geozone_ID"));
							if(amtWeight != null)
								totalAmtWeight = totalAmtWeight.add(amtWeight);			
							if(amtWeightPAT != null)
								totalAmtWeightPAT = totalAmtWeightPAT.add(amtWeightPAT);
							ID_Prod = rsW.getInt("M_Product_ID");
						}
						rsW.close();
						pstmtW.close();
						pstmtW = null;
					}
					catch (SQLException e)
					{
						//throw new DBException(e, sql);
						log.config(e.toString());
					}
					finally
					{
						DB.close(rsW, pstmtW);
						rsW = null; pstmtW = null;
					}		
					if(totalAmtWeight != null && totalAmtWeight.compareTo(Env.ZERO) > 0)
					{
						//se genera linea con monto ya calculado
						//buscamos cargo por peso
						int ID_Chargew = DB.getSQLValue(order.get_TrxName(), "SELECT MAX(C_Charge_ID) " +
								" FROM C_Charge cc " +
								" INNER JOIN C_ChargeType ct ON (cc.C_ChargeType_ID = ct.C_ChargeType_ID) " +
								" WHERE ct.value = 'TCFP'");
						if(ID_Chargew > 0)
						{
							//MOrderLine oLine = new MOrderLine(po.getCtx(), 0, po.get_TrxName());
							MOrderLine oLine = new MOrderLine(order);
							oLine.setAD_Org_ID(order.getAD_Org_ID());
							oLine.setC_BPartner_Location_ID(order.getC_BPartner_Location_ID());						
							oLine.setC_Charge_ID(ID_Chargew);
							oLine.setQty(Env.ONE);
							oLine.setPrice(totalAmtWeight);
							oLine.setDescription("Agregado Automatico. Flete por Peso");
							//oLine.setTax();
							oLine.save();
							oLine.setLineNetAmt();
							oLine.set_CustomColumn("DatePromised2",order.getDateOrdered());
							oLine.set_CustomColumn("DatePromised3",order.getDateOrdered());
							oLine.set_CustomColumn("MonthlyAmount",totalAmtWeightPAT);	
							oLine.set_CustomColumn("m_productref_id",ID_Prod);
							oLine.save();
						}	
					}
				//}
					//ininoles se actualizan montos en caso que no se generen lineas
					//order.calculateTaxTotal();
					//order.saveEx(order.get_TrxName());
			}
		}
	}
}	