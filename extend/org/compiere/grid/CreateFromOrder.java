/******************************************************************************
 * Copyright (C) 2009 Low Heng Sin                                            *
 * Copyright (C) 2009 Idalica Corporation                                     *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 *****************************************************************************/
package org.compiere.grid;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;

import org.compiere.minigrid.IMiniTable;
import org.compiere.model.GridTab;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.model.MRequisitionLine;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

/**
 *
 *  @author Fabian Aguilar
 *  @version  $Id: CreateFromOrder.java,v 1.4 2006/07/30 00:51:28 jjanke Exp $
 *
 * @author Teo Sarca, SC ARHIPAC SERVICE SRL
 * 			<li>BF [ 1896947 ] Generate invoice from Order error
 * 			<li>BF [ 2007837 ] VCreateFrom.save() should run in trx
 */
public class CreateFromOrder extends CreateFrom
{
	/**
	 *  Protected Constructor
	 *  @param mTab MTab
	 */
	public CreateFromOrder(GridTab mTab)
	{
		super(mTab);
		log.info(mTab.toString());
	}   //  VCreateFromInvoice

	/**
	 *  Dynamic Init
	 *  @return true if initialized
	 */
	public boolean dynInit() throws Exception
	{
		log.config("");
		setTitle(Msg.getElement(Env.getCtx(), "C_Invoice_ID", false) + " .. " + Msg.translate(Env.getCtx(), "CreateFrom"));

		return true;
	}   //  dynInit


	/**
	 *  Load PBartner dependent Order/Invoice/Shipment Field.
	 *  @param C_BPartner_ID BPartner
	 */
	protected ArrayList<KeyNamePair> loadRMAData(int C_BPartner_ID) {
		ArrayList<KeyNamePair> list = new ArrayList<KeyNamePair>();

		String sqlStmt = "SELECT r.M_RMA_ID, r.DocumentNo || '-' || r.Amt from M_RMA r "
				+ "WHERE ISSOTRX='N' AND r.DocStatus in ('CO', 'CL') "
				+ "AND r.C_BPartner_ID=? "
				+ "AND NOT EXISTS (SELECT * FROM C_Invoice inv "
				+ "WHERE inv.M_RMA_ID=r.M_RMA_ID AND inv.DocStatus IN ('CO', 'CL'))";

		PreparedStatement pstmt = null;
		try {
			pstmt = DB.prepareStatement(sqlStmt, null);
			pstmt.setInt(1, C_BPartner_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(new KeyNamePair(rs.getInt(1), rs.getString(2)));
			}
			rs.close();
		} catch (SQLException e) {
			log.log(Level.SEVERE, sqlStmt.toString(), e);
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception ex) {
					log.severe("Could not close prepared statement");
				}
			}
		}

		return list;
	}

	/**
	 *  List number of rows selected
	 */
	public void info()
	{

	}   //  infoInvoice

	protected void configureMiniTable (IMiniTable miniTable)
	{
		miniTable.setColumnClass(0, Boolean.class, false);      //  0-Selection
		miniTable.setColumnClass(1, Timestamp.class, true);        //  1--Datetrx
		miniTable.setColumnClass(2, String.class, true);    //  2-BPartner
		miniTable.setColumnClass(3, String.class, true);		//  3- Documentno
		miniTable.setColumnClass(4, String.class, true);        // 4-project
		miniTable.setColumnClass(5, String.class, true);    //  5-product
		miniTable.setColumnClass(6, String.class, true);    //  6-charge
		miniTable.setColumnClass(7, String.class, true);    //  7-description
		miniTable.setColumnClass(8, BigDecimal.class, true);     //  8-qty
		miniTable.setColumnClass(9, BigDecimal.class, true);     //  9-price	
		miniTable.setColumnClass(10, Integer.class, true);     //  10-ID
		miniTable.setColumnClass(11, Integer.class, true);     //  11-ID
		
		//  Table UI
		miniTable.autoSize();
	}

	/**
	 *  Save - Create Invoice Lines
	 *  @return true if saved
	 */
	public boolean save(IMiniTable miniTable, String trxName)
	{
		log.config("");
		int Order_ID=((Integer)getGridTab().getValue("C_Order_ID")).intValue();
		MOrder newOrder = new MOrder (Env.getCtx(), Order_ID, null);
		log.config("**doctype "+ newOrder.getC_DocTypeTarget_ID());
  
		//  Lines
		for (int i = 0; i < miniTable.getRowCount(); i++)
		{
			if (((Boolean)miniTable.getValueAt(i, 0)).booleanValue())
			{
				MOrderLine line = new MOrderLine(newOrder);
				
				BigDecimal valor=(BigDecimal)miniTable.getValueAt(i, 9);
				log.config("valor:"+valor);
				line.setPrice(valor);
				
				KeyNamePair product_id = (KeyNamePair)miniTable.getValueAt(i, 5);
				
				if(product_id.getKey()>0)
				line.setProduct(new MProduct(Env.getCtx(),product_id.getKey(),null));
				
				KeyNamePair charge_id = (KeyNamePair)miniTable.getValueAt(i, 6);
				if(charge_id.getKey()>0)
				line.setC_Charge_ID(charge_id.getKey());
				
				//@mfrojas Validación MINJU. Si no hay producto ni cargo, se agrega producto estandar
				if(product_id.getKey()<=0 && charge_id.getKey()<=0)
				{
					int prod = DB.getSQLValue(trxName, "SELECT m_product_id from m_product where value like 'EST'");
					
					line.setM_Product_ID(prod);
				}
				
				line.setQtyOrdered((BigDecimal)miniTable.getValueAt(i, 8));
				line.setQtyEntered((BigDecimal)miniTable.getValueAt(i, 8));
				Integer project=(Integer)miniTable.getValueAt(i, 11);
				if(project > 0)
					line.set_ValueOfColumn("C_Project_ID", project.intValue());
				
				Integer requiline=(Integer)miniTable.getValueAt(i, 10);
				line.set_ValueOfColumn("M_RequisitionLine_ID",requiline.intValue());
				
				line.setDescription((String)miniTable.getValueAt(i, 7));
				
				line.setAD_Org_ID( new MRequisitionLine (Env.getCtx(),requiline.intValue(),null).getAD_Org_ID() );
				
				MRequisitionLine rLine= new MRequisitionLine( Env.getCtx(), requiline.intValue() ,null);
				if(!rLine.get_ValueAsString("Help").equals(""))
					line.set_CustomColumn("Help", rLine.get_ValueAsString("Help"));
				
			    boolean OK = line.save();
				if (OK)
				DB.executeUpdate("update M_RequisitionLine set C_ORDERLINE_ID="+ line.getC_OrderLine_ID()+" where M_RequisitionLine_ID="+(Integer)miniTable.getValueAt(i, 10),null);
			}   //   if selected
		}   //  for all rows
		
		return true;
	}   //  saveInvoice

	protected Vector<String> getOISColumnNames()
	{
		//  Header Info
		Vector<String> columnNames = new Vector<String>(10);
		columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
		columnNames.add(Msg.getElement(Env.getCtx(), "DateRequired"));
		columnNames.add("Usuario");
		columnNames.add("NoSolicitud");
		columnNames.add("Proyecto/OT");
		columnNames.add(Msg.translate(Env.getCtx(), "ProductName"));
		columnNames.add("Cargo");
		columnNames.add(Msg.translate(Env.getCtx(), "Description"));
		columnNames.add(Msg.translate(Env.getCtx(), "Quantity"));
		columnNames.add(Msg.translate(Env.getCtx(), "Price"));
		columnNames.add("Control");
		columnNames.add("Seguimiento");
		

	    return columnNames;
	}

}
