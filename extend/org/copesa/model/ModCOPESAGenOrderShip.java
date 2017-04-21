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

import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.X_C_OrderShipCalendar;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
/**
 *	Validator for COPESA
 *
 *  @author Italo Niñoles
 */
public class ModCOPESAGenOrderShip implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCOPESAGenOrderShip ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCOPESAGenOrderShip.class);
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
     *	OFB Consulting Ltda. By Julio Farías
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		return null;
	}	//	modelChange

	public String docValidate (PO po, int timing)
	{
		log.info(po.get_TableName() + " Timing: "+timing);
		
		if(timing == TIMING_AFTER_COMPLETE && po.get_Table_ID()==MOrder.Table_ID)
		{
			MOrder order = (MOrder)po;
			if(order.isSOTrx())
			{
				/*String sql = "SELECT ol.C_OrderLine_ID, ol.C_CalendarCOPESA_ID, cl.C_CalendarCOPESALine_ID, cl.DateTrx, ol.M_Product_ID " +
						" FROM C_Order co" +
						" INNER JOIN C_OrderLine ol ON (co.C_Order_ID = ol.C_Order_ID)" +
						" INNER JOIN C_CalendarCOPESA cc ON (ol.C_CalendarCOPESA_ID = cc.C_CalendarCOPESA_ID)" +
						" INNER JOIN C_CalendarCOPESALine cl ON (cc.C_CalendarCOPESA_ID = cl.C_CalendarCOPESA_ID) " +
						" WHERE ol.C_Order_ID = "+order.get_ID()+" AND cl.IsShip = 'Y' AND co.PaymentRule IN ('D','C','I')";
				if(order.getPaymentRule().compareTo("D") == 0 || order.getPaymentRule().compareTo("D") == 0)
					sql = sql + " AND cl.datetrx BETWEEN ol.DatePromised2 AND ol.DatePromised2+(2 * '1 year'::interval) ";
				else
				{
					//siempre que sea a 2 años
					//sql = sql + " AND cl.datetrx BETWEEN ol.DatePromised2 AND ol.DatePromised3 ";
					sql = sql + " AND cl.datetrx BETWEEN ol.DatePromised2 AND ol.DatePromised2+(2 * '1 year'::interval) ";
				}
				sql = sql + " ORDER BY cl.datetrx ASC";*/
				/*String sql = "SELECT ol.C_OrderLine_ID, ol.C_CalendarCOPESA_ID, cl.C_CalendarCOPESALine_ID, cl.DateTrx, ol.M_Product_ID " +
						" FROM C_Order co " +
						" INNER JOIN C_OrderLine ol ON (co.C_Order_ID = ol.C_Order_ID) " +
						" INNER JOIN C_CalendarCOPESA cc ON (ol.C_CalendarCOPESA_ID = cc.C_CalendarCOPESA_ID) " +
						" INNER JOIN C_CalendarCOPESALine cl ON (cc.C_CalendarCOPESA_ID = cl.C_CalendarCOPESA_ID) " +
						" INNER JOIN M_Product mp ON (ol.M_Product_ID = mp.M_Product_ID) " +
						" INNER JOIN M_Product_Category mpc ON (mp.M_Product_Category_ID = mpc.M_Product_Category_ID) " +
						" WHERE ol.C_Order_ID = "+order.get_ID()+" AND cl.IsShip = 'Y' " +
								//"AND co.PaymentRule IN ('D','C','I') " +
						" AND ol.IsActive = 'Y' AND upper(mpc.description) like 'EDITORIAL' AND ol.IsFree = 'N' " +
						" AND cl.datetrx BETWEEN ol.DatePromised2 AND ol.DatePromised2+(2 * '1 month'::interval)" +
						" UNION " +
						" SELECT ol.C_OrderLine_ID, ol.C_CalendarCOPESA_ID, cl.C_CalendarCOPESALine_ID, cl.DateTrx, ol.M_Product_ID " +
						" FROM C_Order co" +
						" INNER JOIN C_OrderLine ol ON (co.C_Order_ID = ol.C_Order_ID)" +
						" INNER JOIN C_CalendarCOPESA cc ON (ol.C_CalendarCOPESA_ID = cc.C_CalendarCOPESA_ID)" +
						" INNER JOIN C_CalendarCOPESALine cl ON (cc.C_CalendarCOPESA_ID = cl.C_CalendarCOPESA_ID)" +
						" INNER JOIN M_Product mp ON (ol.M_Product_ID = mp.M_Product_ID)" +
						" INNER JOIN M_Product_Category mpc ON (mp.M_Product_Category_ID = mpc.M_Product_Category_ID) " +
						" WHERE ol.C_Order_ID = "+order.get_ID()+" AND cl.IsShip = 'Y' " +
								//"AND co.PaymentRule IN ('D','C','I') " +
						" AND ol.IsActive = 'Y' AND upper(mpc.description) like 'EDITORIAL' AND ol.IsFree = 'Y' " +
						" AND cl.datetrx BETWEEN ol.DatePromised2 AND ol.DatePromised3 " +
						" ORDER BY datetrx ASC ";*/
				String sql = "SELECT * FROM RVOFB_OrderShip WHERE C_Order_ID = "+order.get_ID();
				
				try 
				{
					PreparedStatement ps = DB.prepareStatement(sql, po.get_TrxName());
					ResultSet rs = ps.executeQuery();				
					while (rs.next()) 
					{
						X_C_OrderShipCalendar sCal = new X_C_OrderShipCalendar(po.getCtx(), 0, po.get_TrxName());
						sCal.setC_Order_ID(order.get_ID());
						sCal.setAD_Org_ID(order.getAD_Org_ID());
						sCal.setIsActive(true);
						sCal.setC_OrderLine_ID(rs.getInt("C_OrderLine_ID"));
						sCal.setC_CalendarCOPESA_ID(rs.getInt("C_CalendarCOPESA_ID"));
						sCal.setC_CalendarCOPESALine_ID(rs.getInt("C_CalendarCOPESALine_ID"));
						sCal.setDateTrx(rs.getTimestamp("DateTrx"));
						sCal.set_CustomColumn("M_Product_ID",rs.getInt("M_Product_ID"));
						//ininoles forzamos el ID para que no de error
						int ID_ShipCalendar = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(currentnext) FROM ad_sequence " +
								" WHERE name = 'C_OrderShipCalendar'");
						//sCal.setC_OrderShipCalendar_ID(ID_ShipCalendar);
						sCal.save();
						ID_ShipCalendar++;
						//DB.executeUpdateEx("UPDATE AD_Sequence SET currentnext = "+ID_ShipCalendar+" WHERE name = 'C_OrderShipCalendar' ", po.get_TrxName());
					}					
				}
				catch (Exception e) 
				{
					log.config(e.toString());
				}
			}
		}
		if((timing == TIMING_AFTER_VOID || timing == TIMING_AFTER_REACTIVATE) && po.get_Table_ID()==MOrder.Table_ID)
		{	
			MOrder order = (MOrder)po;
			if(order.isSOTrx())
			{
				DB.executeUpdate("DELETE FROM C_OrderShipCalendar WHERE C_Order_ID = "+order.get_ID(), po.get_TrxName());
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

	public boolean reqApproval(MOrder order)
	{
		if(order.getPaymentRule().compareTo("D") == 0)
		{
			BigDecimal amt = DB.getSQLValueBD(null,"SELECT SUM(LineNetAmt) " +
					" FROM C_OrderLine WHERE C_Charge_ID > 0 AND C_Order_ID = ? ",order.get_ID());
			if(amt.compareTo(Env.ONEHUNDRED) < 0)
				return false;
		}	
		return true;
	}
	

}	