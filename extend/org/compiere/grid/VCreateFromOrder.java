/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
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
package org.compiere.grid;

import java.beans.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import javax.swing.JLabel;
import javax.swing.table.*;
import org.compiere.apps.*;
import org.compiere.grid.ed.*;
import org.compiere.minigrid.IMiniTable;
import org.compiere.model.*;
import org.compiere.swing.CPanel;
import org.compiere.util.*;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *  Create Lines for PO from Requisitions
 *
 *  @author Fabian Aguilar
 *  @version  $Id: VCreateFromOrder.java,v 1.30 2007/05/29 02:10:58 faaguilar Exp $
 */
public class VCreateFromOrder extends CreateFrom implements ActionListener, VetoableChangeListener
{
	/**
	 *  Protected Constructor
	 *  @param mTab MTab
	 */
	private int Order_ID;
	private int Org_ID;
	private int AD_UserID=0;
	private int SubProject_ID=0;
	private int p_WindowNo;
	private VLookup userSearch;
	private VLookup SpjSearch;
	private GridTab p_mTab;
	private VCreateFromDialog dialog;
	private JLabel userLabel;
	private JLabel spjLabel;
	
	VCreateFromOrder(GridTab mTab)
	{
		super(mTab);
		log.info(mTab.toString());
		log.info("");
		
		p_WindowNo = getGridTab().getWindowNo();
		p_mTab= mTab;
		dialog = new VCreateFromDialog(this, getGridTab().getWindowNo(), true);
		
		AEnv.positionCenterWindow(Env.getWindow(p_WindowNo), dialog);
	}   //  VCreateFromOrder

	
	private void jbInit() throws Exception
    {
    	
    	userLabel.setText(Msg.getElement(Env.getCtx(), "AD_User_ID"));
    	spjLabel.setText("Proyecto");
    	

    	CPanel parameterPanel = dialog.getParameterPanel();
    	parameterPanel.setLayout(new BorderLayout());
    	CPanel parameterStdPanel = new CPanel(new GridBagLayout());
    	parameterPanel.add(parameterStdPanel, BorderLayout.CENTER);

    	parameterStdPanel.add(userLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
   
    		parameterStdPanel.add(userSearch, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
    				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    	
        	parameterStdPanel.add(spjLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
        			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        	parameterStdPanel.add(SpjSearch,  new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
        			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

    }   //  jbInit
	
	/**
	 *  Dynamic Init
	 *  @throws Exception if Lookups cannot be initialized
	 *  @return true if initialized
	 */
	public boolean dynInit() throws Exception
	{
		if (p_mTab.getValue("C_Order_ID") == null)
		{
			return false;
		}

		setTitle("Requisitions Lines");
		
		//  BPartner
		
		int AD_Column_ID = MColumn.getColumn_ID("M_Requisition", "AD_User_ID");        //  m_requisition.ad_user_id
		MLookup lookupUser = MLookupFactory.get (Env.getCtx(), 0, 0, AD_Column_ID, DisplayType.Search);
		userSearch = new VLookup("AD_User_ID", true, false, true, lookupUser);
		userSearch.addVetoableChangeListener(this);
		
		AD_Column_ID = MColumn.getColumn_ID("M_Requisition", "C_SubProjectOFB_ID");        //  m_requisition.C_SubProjectOFB_ID
		MLookup lookupSpj = MLookupFactory.get (Env.getCtx(), 0, 0, AD_Column_ID, DisplayType.Search);
		SpjSearch = new VLookup("C_SubProjectOFB_ID", true, false, true, lookupSpj);
		SpjSearch.addVetoableChangeListener(this);
		
			
		//BPartner_ID=Env.getContextAsInt(Env.getCtx(), p_WindowNo, "C_BPartner_ID");
		Order_ID=Env.getContextAsInt(Env.getCtx(), p_WindowNo, "C_Order_ID");
		Org_ID=Env.getContextAsInt(Env.getCtx(), p_WindowNo, "AD_Org_ID");
		
		loadLines(0,0);

		return true;
	}   //  dynInit

	/**
	 *  Init Details (never called)
	 *  @param C_BPartner_ID BPartner
	 */
	protected void initBPDetails(int C_BPartner_ID)
	{
	}   //  initDetails

	/**
	 *  Change Listener
	 *  @param e event
	 */
	public void vetoableChange (PropertyChangeEvent e)
	{
		log.config(e.getPropertyName() + "=" + e.getNewValue());

		//  BankAccount
		if (e.getPropertyName() == "AD_User_ID")
		{
			if(e.getNewValue()!=null)
			{
			    AD_UserID = ((Integer)e.getNewValue()).intValue();
				loadLines (AD_UserID,SubProject_ID);
			}
		}
		else if (e.getPropertyName() == "C_SubProjectOFB_ID")
		{
			if(e.getNewValue()!=null)
			{
			    SubProject_ID = ((Integer)e.getNewValue()).intValue();
				loadLines (AD_UserID,SubProject_ID);
			}
		}
		dialog.tableChanged(null);
	}   //  vetoableChange

	/**
	 *  Load Data - Bank Account
	 *  @param C_BankAccount_ID Bank Account
	 */
	private void loadLines (int AD_User, int SubPro)
	{
		
		/**
		 *  Selected        - 0
		 *  Documentno            - 1
		 *  Datetrx    - 2
		 *  Description     - 3
		 *  ID             - 4
		 */
		
		boolean IsSOTrx = "Y".equals(Env.getContext(Env.getCtx(), p_WindowNo, "IsSOTrx"));
		int bPartner_ID=Env.getContextAsInt(Env.getCtx(), p_WindowNo, "C_BPartner_ID");
		
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		
		String sql="";
		if(DB.isPostgreSQL())
		sql = "Select A.DocumentNo,A.DATEREQUIRED,A.AD_USER_ID,A.M_Requisition_ID,C.M_PRODUCT_ID,(C.QTY-coalesce(C.QtyUsed,0)),C.M_Requisitionline_id,B.name,"
		+" P.name as Productname,C.PRICEACTUAL,C.C_SubProjectOFB_ID,pj.name as Projectname,C.description, C.IsRfQ "
			+" from M_Requisition A "
			+" inner join M_RequisitionLine C on (A.M_Requisition_ID=C.M_Requisition_ID)"
			+" inner join M_Product P on (P.M_Product_ID=C.M_Product_ID)"
			+" inner join C_DocType doc on (A.C_DocType_ID=doc.C_DocType_ID)"
			+" left outer join AD_User B on (A.AD_USER_ID=B.AD_USER_ID)"
			+" left outer join C_SubProjectOFB pj on (C.C_SubProjectOFB_ID=pj.C_SubProjectOFB_ID)"
			+" where A.DocStatus='CO' and A.isactive='Y' and C.isactive='Y' and  doc.docbasetype not in ('RRC','PLR','PRT')  and A.AD_Org_ID="+Org_ID; //'PRT' purchase requisition temp
		else
			sql = "Select A.DocumentNo,A.DATEREQUIRED,A.AD_USER_ID,A.M_Requisition_ID,C.M_PRODUCT_ID,(C.QTY-coalesce(C.QtyUsed,0)),C.M_Requisitionline_id,B.name,"
				+" P.name as Productname,C.PRICEACTUAL,C.C_SubProjectOFB_ID,pj.name as Projectname,C.description, C.IsRfQ "
					+" from M_Requisition A "
					+" inner join M_RequisitionLine C on (A.M_Requisition_ID=C.M_Requisition_ID)"
					+" inner join M_Product P on (P.M_Product_ID=C.M_Product_ID)"
					+" inner join C_DocType doc on (A.C_DocType_ID=doc.C_DocType_ID)"
					+" left outer join AD_User B on (A.AD_USER_ID=B.AD_USER_ID)"
					+" left outer join C_SubProjectOFB pj on (C.C_SubProjectOFB_ID=pj.C_SubProjectOFB_ID)"
					+" where A.DocStatus='CO' and A.isactive='Y' and C.isactive='Y' and doc.docbasetype not in ('RRC','PLR','PRT') and A.AD_Org_ID="+Org_ID;
		
		if(!IsSOTrx)
			sql+=" and not EXISTS (select * from C_OrderLine ol where ol.M_Requisitionline_id=C.M_Requisitionline_id) ";
		else
			sql+=" and (C.QTY-coalesce(C.QtyUsed,0)) > 0";
				
		if(IsSOTrx)
			sql+=" and doc.issotrx='Y' and A.C_BPartner_ID="+bPartner_ID;
		else
			sql+=" and doc.issotrx='N'";
		
		if(AD_User>0)
			sql=sql+" and A.AD_User_ID="+AD_User;
		
		if(SubPro>0)
			sql=sql+" and pj.C_SubProjectOFB_ID="+SubPro;
		
		log.config ("**"+sql);
		
		try
		{
			PreparedStatement pstmt = DB.prepareStatement(sql.toString(), null);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				if(rs.getString("IsRfQ").equals("Y"))
					continue;
				
				Vector<Object> line = new Vector<Object>(10);
				
				line.add(new Boolean(false));       //  0-Selection
				line.add(rs.getTimestamp(2));    //  1--Datetrx
				line.add(rs.getString(8));      //  2--BPartner
				line.add(rs.getString(1));     //3- Documentno
				line.add(rs.getString(12)); 		//4-project
				line.add(rs.getString(9));      //  5-product
				line.add(rs.getString(13));     // 6 description
				line.add(rs.getBigDecimal(6));       //  7-qty
				line.add(rs.getBigDecimal(10));   //8 price
				line.add(rs.getInt(5)); //9 product ID
				line.add(rs.getInt(7)); //10 R line ID
				line.add(rs.getInt(11)); //11 SUB ID
				data.add(line);
			}
			rs.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		//  Header Info
		Vector<String> columnNames = new Vector<String>(10);
		columnNames.add(Msg.getMsg(Env.getCtx(), "Select"));
		columnNames.add(Msg.getElement(Env.getCtx(), "DateRequired"));
		columnNames.add("Usuario");
		columnNames.add("N� Solicitud");
		columnNames.add("Proyecto/OT");
		columnNames.add(Msg.translate(Env.getCtx(), "ProductName"));
		columnNames.add(Msg.translate(Env.getCtx(), "Description"));
		columnNames.add(Msg.translate(Env.getCtx(), "Quantity"));
		columnNames.add(Msg.translate(Env.getCtx(), "Price"));
		columnNames.add("Control");
		columnNames.add("Seguimiento");
		columnNames.add("Traza");

		
		//  Remove previous listeners
		dialog.getMiniTable().getModel().removeTableModelListener(dialog);
		//  Set Model
		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		model.addTableModelListener(dialog);
		dialog.getMiniTable().setModel(model);
		//
		dialog.getMiniTable().setColumnClass(0, Boolean.class, false);      //  0-Selection
		dialog.getMiniTable().setColumnClass(1, Timestamp.class, true);        //  1--Datetrx
		dialog.getMiniTable().setColumnClass(2, String.class, true);    //  2-BPartner
		dialog.getMiniTable().setColumnClass(3, String.class, true);		//  3- Documentno
		dialog.getMiniTable().setColumnClass(4, String.class, true);        // 4-project
		dialog.getMiniTable().setColumnClass(5, String.class, true);    //  5-product
		dialog.getMiniTable().setColumnClass(6, String.class, true);    //  6-description
		dialog.getMiniTable().setColumnClass(7, BigDecimal.class, true);     //  7-qty
		dialog.getMiniTable().setColumnClass(8, BigDecimal.class, true);     //  8-price
		
		dialog.getMiniTable().setColumnClass(9, Integer.class, true);     //  9-ID
		dialog.getMiniTable().setColumnClass(10, Integer.class, true);     //  10-ID
		dialog.getMiniTable().setColumnClass(11, Integer.class, true);     //  11-ID
		//  Table UI
		dialog.getMiniTable().autoSize();
	}   //  loadBankAccount

	/**
	 *  List number of rows selected
	 */
	public void info()
	{

	} 

	/**
	 *  Save Statement - Insert Data
	 *  @return true if saved
	 */
	public boolean save(IMiniTable miniTable, String trxName)
	{
		log.config("");
		MOrder newOrder = new MOrder (Env.getCtx(), Order_ID, null);
		log.config("**doctype "+ newOrder.getC_DocTypeTarget_ID());
  
		//  Lines
		for (int i = 0; i < miniTable.getRowCount(); i++)
		{
			if (((Boolean)miniTable.getValueAt(i, 0)).booleanValue())
			{
				MOrderLine line = new MOrderLine(newOrder);
				//log.config("valor:"+(String)model.getValueAt(i, 6));
				BigDecimal valor=(BigDecimal)miniTable.getValueAt(i, 8);
				log.config("valor:"+valor);
				line.setPrice(valor);
				Integer product=(Integer)miniTable.getValueAt(i, 9);
				line.setProduct(new MProduct(Env.getCtx(),product.intValue(),null));
				
				line.setQtyOrdered((BigDecimal)miniTable.getValueAt(i, 7));
				line.setQtyEntered((BigDecimal)miniTable.getValueAt(i, 7));
				Integer subproject=(Integer)miniTable.getValueAt(i, 11);
				line.set_ValueOfColumn("C_SubProjectOFB_ID", subproject.intValue());
				
				Integer requiline=(Integer)miniTable.getValueAt(i, 10);
				line.set_ValueOfColumn("M_RequisitionLine_ID",requiline.intValue());
				
				line.setDescription((String)miniTable.getValueAt(i, 6));
				line.setAD_Org_ID( new MRequisitionLine (Env.getCtx(),requiline.intValue(),null).getAD_Org_ID() );
				
			    boolean OK = line.save();
				if (OK)
				DB.executeUpdate("update M_RequisitionLine set C_ORDERLINE_ID="+ line.getC_OrderLine_ID()+" where M_RequisitionLine_ID="+(Integer)miniTable.getValueAt(i, 10),null);
			}   //   if selected
		}   //  for all rows
		
		return true;
	}   //  save
	
	public void showWindow()
	{
		dialog.setVisible(true);
	}
	
	public void closeWindow()
	{
		dialog.dispose();
	}
	
	/**
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		log.config("Action=" + e.getActionCommand());
	}
}   //  VCreateFromStatement
