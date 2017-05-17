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
import org.compiere.model.MOrderLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	Validator for COPESA
 *
 *  @author Italo Niñoles
 */
public class ModCOPESAOLineFree implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCOPESAOLineFree ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCOPESAOLineFree.class);
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
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     */
	public String modelChange (PO po, int type) throws Exception
	{
	    BigDecimal newAmt;
	    BigDecimal newAmtPAT;
	    int freedays;
	    int durationdays;
	    int lineid;
	    MOrderLine oLine;
		log.info(po.get_TableName() + " Type: "+type);
		
		if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE)&& po.get_Table_ID()==MOrder.Table_ID) 
		{	
			MOrder order = (MOrder)po;
			if (order.isSOTrx() && order.getDocStatus().compareToIgnoreCase("IP") == 0)
			{

				MOrderLine[] lines = order.getLines();
				// El query busca todas aquellas líneas de productos de gracia de la orden que aún no son referenciadas por otra línea,
				// es decir, aquellas líneas que les falta crear su línea de producto de gracia.
				String sql = "SELECT c_orderline_id, FreeDays, FreeAmt, PricePATFree, durationdays " +
							 "FROM M_ProductPrice pp " +
							 "INNER JOIN M_PriceList_Version plv ON pp.M_PriceList_Version_ID = plv.M_PriceList_Version_ID " + 
							 "INNER JOIN M_PriceList pl ON plv.M_PriceList_ID = pl.M_PriceList_ID " + 
							 "INNER JOIN c_orderline col on col.m_product_id = pp.m_product_id " +
							 "WHERE pp.IsActive = 'Y' " + 
							 "AND pl.M_priceList_ID = ? " +
							 "AND col.c_order_id = ? " +
							 "AND pp.freedays >= 0 " +
							 "AND not exists (select * from c_orderline where c_orderlineref_id = col.c_orderline_id) ";

				PreparedStatement pstmt = DB.prepareStatement(sql, order.get_TrxName());
			    pstmt.setInt(1, order.getM_PriceList_ID());  
			    pstmt.setInt(2, order.getC_Order_ID());  
			    ResultSet rs = pstmt.executeQuery();
			    
			    while (rs.next() )
			    {	
			    	lineid = rs.getInt(1);
			    	
			    	if(lineid <= 0)
			    		continue;
			    	oLine = null;
					for (int i = 0; i < lines.length; i++)
					{
						if( lines[i].get_ID() == lineid )
							oLine = lines[i]; 
					}
			    	
					if( oLine == null )
						continue;
					
			    	freedays = rs.getInt(2);
			    	newAmt = rs.getBigDecimal(3);
			    	newAmtPAT = rs.getBigDecimal(4);
			    	durationdays = rs.getInt(5);
                    
			    	if(newAmt == null || newAmt.compareTo(Env.ZERO) <= 0)
						newAmt = Env.ONE;
					
					if(newAmtPAT == null || newAmtPAT.compareTo(Env.ZERO) <= 0)
						newAmtPAT = Env.ONE;
			    	
			    	if( freedays >= 0 )
			    		COPESAOrderOps.createFreeLine(order, oLine, freedays, newAmt, newAmtPAT, durationdays);
			    	
			    	freedays = -1;
			    	durationdays = -1;
			    	newAmt = null;
			    	newAmtPAT = null;
			    }	
			    rs.close();
			    pstmt.close();
				
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
