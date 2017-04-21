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
package org.copesa.process;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.logging.Level;

import org.compiere.model.MInOut;
import org.compiere.model.MInOutLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.process.ProcessInfoParameter;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;

/**
 */
public class ShipGenerateCopesa extends SvrProcess
{
	/**	Date Invoiced			*/
	private Timestamp	p_DateShip = null;
	private Timestamp	p_DateShip_To = null;
	private Timestamp	p_DateTrx_To = null;
	private int			p_C_BPartner_ID = 0;
	private int			p_C_Order_ID = 0;
	/** Number of Invoices		*/
	private int			m_created = 0;
	/**	Line Number				*/
	private int			m_line = 0;
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			if (para[i].getParameter() == null)
				;
			else if (name.equals("MovementDate"))
			{
				p_DateShip = para[i].getParameterAsTimestamp();
				p_DateShip_To = para[i].getParameterToAsTimestamp();
			}
			else if (name.equals("C_BPartner_ID"))
				p_C_BPartner_ID = para[i].getParameterAsInt();
			else if (name.equals("C_Order_ID"))
				p_C_Order_ID = para[i].getParameterAsInt();		
			else if (name.equals("DateTrx"))
				p_DateTrx_To = para[i].getParameterAsTimestamp();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}		
	}	//	prepare

	/**
	 * 	Generate Invoices
	 *	@return info
	 *	@throws Exception
	 */
	protected String doIt () throws Exception
	{	
		m_created = 0;
		m_line = 0;
		//generacion de guias
		String sqlGancho = "SELECT C_OrderLine_ID " +
				" FROM C_OrderLine ol" +
				" INNER JOIN C_Order co ON (ol.C_Order_ID = co.C_Order_ID) " +
				" LEFT JOIN M_Product mp ON (ol.M_Product_ID = mp.M_Product_ID)" +
				" LEFT JOIN M_Product_Category pc ON (mp.M_Product_Category_ID = pc.M_Product_Category_ID)" +
				" WHERE co.DocStatus IN ('CO','CL') AND ol.IsActive = 'Y' AND upper(pc.value) like '%NOEDITO%'" +
				" AND ol.C_OrderLine_ID NOT IN " +
				" (SELECT mio.C_OrderLine_ID FROM M_InOutLine mio WHERE mio.C_OrderLine_ID IS NOT NULL AND mio.IsActive = 'Y')";
		if(p_C_Order_ID > 0)
			sqlGancho = sqlGancho + " AND ol.C_Order_ID = "+p_C_Order_ID;
		if(p_C_BPartner_ID > 0)
			sqlGancho = sqlGancho + " AND co.C_BPartner_ID = "+p_C_BPartner_ID;
		if(p_DateShip != null && p_DateShip_To != null)
			sqlGancho = sqlGancho + " AND ol.MovementDate BETWEEN ? AND ? ";
		sqlGancho = sqlGancho + " ORDER BY co.C_Order_ID, ol.M_Shipper_ID, ol.C_BpartnerRef_ID, " +
				" ol.C_BPartner_Location_ID, ol.MovementDate ";	
		
		PreparedStatement pstmtGancho = DB.prepareStatement (sqlGancho, get_TrxName());
		if(p_DateShip != null && p_DateShip_To != null)
		{
			pstmtGancho.setTimestamp(1, p_DateShip);
			pstmtGancho.setTimestamp(2, p_DateShip_To);
		}
		try
		{
			ResultSet rsGancho = pstmtGancho.executeQuery ();
			int LastOrder_ID = 0;
			int LastShipper_ID = 0;
			int LastBPartner_ID = 0;
			int LastBPartnertLoc_ID = 0;
			Timestamp LastmovDate = null;
			MInOut ship = null;
			boolean createShip = true;
			while (rsGancho.next())
			{
				MOrderLine oLine = new MOrderLine(getCtx(), rsGancho.getInt("C_OrderLine_ID"), get_TrxName());
				MOrder order = new MOrder(getCtx(), oLine.getC_Order_ID(), get_TrxName());				
				//creamos guia si es nula
				if(LastShipper_ID != oLine.get_ValueAsInt("M_Shipper_ID") || 
						LastOrder_ID != oLine.getC_Order_ID() ||
						LastBPartner_ID != oLine.get_ValueAsInt("C_BpartnerRef_ID") ||
						LastBPartnertLoc_ID != oLine.get_ValueAsInt("C_Bpartner_Location_ID") ||
						LastmovDate != (Timestamp)oLine.get_Value("MovementDate")
						)
				{
					LastShipper_ID = oLine.get_ValueAsInt("M_Shipper_ID");
					LastOrder_ID = oLine.getC_Order_ID();
					LastBPartner_ID = oLine.get_ValueAsInt("C_BpartnerRef_ID");
					LastBPartnertLoc_ID = oLine.get_ValueAsInt("C_Bpartner_Location_ID");
					LastmovDate = (Timestamp)oLine.get_Value("MovementDate");					
					createShip = true;
				}			
				if(createShip)
				{
					//ship = new MInOut(order, 0, (Timestamp)oLine.get_Value("MovementDate"));
					if(p_DateTrx_To != null)
						ship = new MInOut(order, 0, p_DateTrx_To);
					else
						ship = new MInOut(order, 0, (Timestamp)oLine.get_Value("MovementDate"));
					ship.setDeliveryViaRule(oLine.get_ValueAsString("DeliveryViaRule"));
					ship.setM_Shipper_ID(oLine.get_ValueAsInt("M_Shipper_ID"));
					ship.save();
					createShip = false;
					m_created++;
				}
				//se crea linea en base a oLine
				MInOutLine ioLine = new MInOutLine(ship);
				ioLine.setM_Product_ID(oLine.getM_Product_ID());
				ioLine.setQty(oLine.getQtyEntered());
				ioLine.setC_OrderLine_ID(oLine.get_ID());
				int ID_Locator = DB.getSQLValue(get_TrxName(), "SELECT MAX(M_Locator_ID) FROM M_Locator WHERE IsActive = 'Y' AND M_Warehouse_ID = "+ship.getM_Warehouse_ID());
				if(ID_Locator  > 0)
					ioLine.setM_Locator_ID(ID_Locator);
				else
					ioLine.setM_Locator_ID(2000002);
				ioLine.save();
				m_line++;
			}
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sqlGancho, e);
		}
		return "Se han generado "+m_created+" guias y "+m_line+" Lineas";
	}	//	doIt
}	//	ShipGenerate
