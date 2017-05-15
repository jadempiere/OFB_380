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

import java.sql.Timestamp;
import java.util.Calendar;

import org.compiere.model.MClient;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;

/**
 *	Validator for COPESA
 *
 *  @author Italo Niñoles
 */
public class ModCOPESADateOrder implements ModelValidator
{
	/**
	 *	Constructor.
	 *	The class is instantiated when logging in and client is selected/known
	 */
	public ModCOPESADateOrder ()
	{
		super ();
	}	//	MyValidator

	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(ModCOPESADateOrder.class);
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
		// REFACTORIZACION: Este model no hace falta
		//engine.addDocValidate(MOrder.Table_Name, this);		
		
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
		
		if(timing == TIMING_BEFORE_COMPLETE && po.get_Table_ID()==MOrder.Table_ID)
		{
			MOrder order = (MOrder)po;
			if(order.isSOTrx())
			{
				MOrderLine[] oLines = order.getLines(false, null);				
				for (int i = 0; i < oLines.length; i++)
				{
					MOrderLine line = oLines[i];
					Timestamp dateStart = (Timestamp)line.get_Value("DatePromised2");
					Timestamp dateEnd = (Timestamp)line.get_Value("DatePromised3");
					//validacion de fecha de cabecera y lineas 
					if(dateStart != null)
					{
//						if(dateStart.compareTo(order.getDateOrdered()) < 0)
//							return "Error : Fecha de Producto "+line.getM_Product().getName()+" incorrecta";
					}					
					//validacion de fechas relacionadas
					if(line.get_ValueAsInt("C_OrderLineRef_ID") > 0 && line.get_ValueAsBoolean("IsFree"))
					{
						MOrderLine lineAfter = new MOrderLine(po.getCtx(), line.get_ValueAsInt("C_OrderLineRef_ID"), po.get_TrxName());
						Timestamp dateStartAfter = (Timestamp)lineAfter.get_Value("DatePromised2");
						//se suman dias a fecha fin
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeInMillis(dateEnd.getTime());
						calendar.add(Calendar.DATE, 1);							
						Timestamp newDate = new Timestamp(calendar.getTimeInMillis());
						//dejamos solo la fecha
						dateEnd.setHours(0);
						dateEnd.setMinutes(0);
						dateEnd.setSeconds(0);
						newDate.setHours(0);
						newDate.setMinutes(0);
						newDate.setSeconds(0);
						if(dateStartAfter.compareTo(newDate) != 0)
							return "Error: Fecha de Linea "+lineAfter.getLine()+" incorrecta";
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