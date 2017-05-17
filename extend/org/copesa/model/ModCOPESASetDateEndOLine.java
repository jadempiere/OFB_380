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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;

import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.copesa.utils.DateUtils;

/**
 *	Validator for COPESA
 *
 *  @author Italo Niñoles
 */
public class ModCOPESASetDateEndOLine implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCOPESASetDateEndOLine ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCOPESASetDateEndOLine.class);
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
		engine.addModelChange(MOrderLine.Table_Name, this);		
		
	}	//	initialize

    /**
     *	Model Change of a monitored Table.
     */
	public String modelChange (PO po, int type) throws Exception
	{
		log.info(po.get_TableName() + " Type: "+type);
		
		if((type == TYPE_BEFORE_NEW || type == TYPE_BEFORE_CHANGE)&& po.get_Table_ID()==MOrderLine.Table_ID) 
		{	
			MOrderLine oLine = (MOrderLine)po;
			//MOrder order = new MOrder(po.getCtx(), oLine.getC_Order_ID(), po.get_TrxName());
			MOrder order = oLine.getParent();
			if (order.isSOTrx() && order.getDocStatus().compareToIgnoreCase("CO") != 0
					&& !oLine.get_ValueAsBoolean("IsFree") && oLine.getM_Product_ID() > 0)
			{
				Timestamp dateStart = (Timestamp)oLine.get_Value("DatePromised2");
				Timestamp dateEnd = (Timestamp)oLine.get_Value("DatePromised3");
				if(dateStart == null)
				{
					//dateStart = order.getDateOrdered();
					dateStart = order.getDatePromised();
			        oLine.set_CustomColumn("DatePromised2", dateStart);
				}
				
				if(dateStart != null && dateEnd == null)
				{
					int cant = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(DurationDays) as DurationDays " +
							" FROM M_ProductPrice pp " +
							" INNER JOIN M_PriceList_Version plv ON pp.M_PriceList_Version_ID = plv.M_PriceList_Version_ID " +
							" INNER JOIN M_PriceList pl ON plv.M_PriceList_ID = pl.M_PriceList_ID " +
							" WHERE pp.IsActive = 'Y' AND M_product_ID = "+oLine.getM_Product_ID()+
							" AND pl.M_priceList_ID = "+order.getM_PriceList_ID());
					if(cant > 0)
					{
						Calendar calCalendario = Calendar.getInstance();
				        calCalendario.setTimeInMillis(dateStart.getTime());
				        calCalendario.add(Calendar.DATE, cant-1);
				        Timestamp newDate = new Timestamp(calCalendario.getTimeInMillis());
				        oLine.set_CustomColumn("DatePromised3", newDate) ;
					} 
			
				}
			}else if(order.isSOTrx() && order.getDocStatus().compareToIgnoreCase("CO") != 0
					&& oLine.get_ValueAsBoolean("IsFree") && oLine.getM_Product_ID() > 0)
			{
				
				
				Timestamp dateStart = (Timestamp)oLine.get_Value("DatePromised2");
				Timestamp dateEnd = (Timestamp)oLine.get_Value("DatePromised3");
				if(dateStart == null)
				{
					dateStart = order.getDatePromised();
			        oLine.set_CustomColumn("DatePromised2", dateStart);
				}
				if(dateStart != null && dateEnd == null)
				{
					String sql = "SELECT FreeDays, durationdays " +
							" FROM M_ProductPrice pp " +
							" INNER JOIN M_PriceList_Version plv ON pp.M_PriceList_Version_ID = plv.M_PriceList_Version_ID " +
							" INNER JOIN M_PriceList pl ON plv.M_PriceList_ID = pl.M_PriceList_ID " +
							" WHERE pp.IsActive = 'Y' AND M_product_ID = "+oLine.getM_Product_ID()+
							" AND pl.M_priceList_ID = "+order.getM_PriceList_ID();
							
					PreparedStatement pstmt = DB.prepareStatement(sql, order.get_TrxName());
				    ResultSet rs = pstmt.executeQuery();
				    int duration = -1;
				    int freedays = -1;
					if(rs.next())
					{
						freedays = rs.getInt(1);
						duration = rs.getInt(2);
						if (freedays <= 0 )
							freedays = 1;
						Calendar calCalendario = Calendar.getInstance();
				        calCalendario.setTimeInMillis(dateStart.getTime());
				        calCalendario.add(Calendar.DATE, freedays - 1);
				        Timestamp newDate = new Timestamp(calCalendario.getTimeInMillis());
				        oLine.set_CustomColumn("DatePromised3", newDate) ;
				        //buscamos linea relacionada para actualizar fecha de inicio
				        int ID_LineRef = DB.getSQLValue(po.get_TrxName(), "SELECT MAX(C_OrderLine_ID) " +
				        		"FROM C_OrderLine WHERE C_OrderLineRef_ID = "+oLine.get_ID()) ;
				        if(ID_LineRef > 0)
				        {
				        	MOrderLine oLineRef = new MOrderLine(po.getCtx(), ID_LineRef, po.get_TrxName());
				        	//ininoles nueva validacion y cambios para fecha fin
							Calendar calendar = Calendar.getInstance();
							calendar.setTimeInMillis(dateStart.getTime());
							calendar.add(Calendar.DATE, freedays);
							oLineRef.set_CustomColumn("DatePromised2",new Timestamp(calendar.getTimeInMillis()));
							//------
							dateEnd = null;
							if (order.getPaymentRule().compareTo("D") == 0 )
								dateEnd = DateUtils.veryDistantDate();
							else
							{
								if (duration == 0 )
									duration = 1;
								
								if (duration > 0 )
								{	
									calendar.add(Calendar.DATE, duration - 1);
									dateEnd = new Timestamp(calendar.getTimeInMillis());
								}
							}
							oLineRef.set_CustomColumn("DatePromised3", dateEnd);									
							//------------------
							//oLineRef.set_CustomColumn("DatePromised3",null);
							oLineRef.save();
				        }
					}
				}
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