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

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;

//import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

import org.compiere.apps.AEnv;
import org.compiere.grid.ed.VLookup;
import org.compiere.model.GridTab;
import org.compiere.model.MColumn;
//import org.compiere.model.MDocType;
import org.compiere.model.MLookup;
import org.compiere.model.MLookupFactory;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

public class VCreateFromOrderUI extends CreateFromOrder implements ActionListener, VetoableChangeListener
{
	private static final long serialVersionUID = 1L;
	
	private VCreateFromDialog dialog;

	public VCreateFromOrderUI(GridTab mTab)
	{
		super(mTab);
		log.info(getGridTab().toString());
		
		dialog = new VCreateFromDialog(this, getGridTab().getWindowNo(), true);
		
		p_WindowNo = getGridTab().getWindowNo();

		try
		{
			if (!dynInit())
				return;
			jbInit();

			setInitOK(true);
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "", e);
			setInitOK(false);
		}
		AEnv.positionCenterWindow(Env.getWindow(p_WindowNo), dialog);
	}   //  VCreateFrom
	
	/** Window No               */
	private int p_WindowNo;

	/**	Logger			*/
	private CLogger log = CLogger.getCLogger(getClass());
	
	//
	//private int Order_ID;
	private int Org_ID;	
	private int AD_UserID=0;
	private int SubProject_ID=0;
	private VLookup userSearch;
	private VLookup SpjSearch;
	//private GridTab p_mTab;
	private JLabel userLabel=new JLabel();
	private JLabel spjLabel=new JLabel();
	private JLabel organizationLabel = new JLabel();
	private VLookup organizationPick = null;
	
	/**
	 *  Dynamic Init
	 *  @throws Exception if Lookups cannot be initialized
	 *  @return true if initialized
	 */
	public boolean dynInit() throws Exception
	{
		log.config("");
		
		super.dynInit();
		
		dialog.setTitle("Requisitions Lines");
   
				
		int AD_Column_ID = MColumn.getColumn_ID("M_Requisition", "AD_User_ID");        //  m_requisition.ad_user_id
		MLookup lookupUser = MLookupFactory.get (Env.getCtx(), 0, 0, AD_Column_ID, DisplayType.Search);
		userSearch = new VLookup("AD_User_ID", true, false, true, lookupUser);
		userSearch.addVetoableChangeListener(this);
		
		AD_Column_ID = MColumn.getColumn_ID("M_Requisition", "C_SubProjectOFB_ID");        //  m_requisition.C_SubProjectOFB_ID
		if(AD_Column_ID>0){
			MLookup lookupSpj = MLookupFactory.get (Env.getCtx(), 0, 0, AD_Column_ID, DisplayType.Search);
			SpjSearch = new VLookup("C_SubProjectOFB_ID", true, false, true, lookupSpj);
			SpjSearch.addVetoableChangeListener(this);
		}
		
		AD_Column_ID = 839;
		MLookup lookupOrg = MLookupFactory.get(Env.getCtx(), 0, 0, AD_Column_ID, DisplayType.TableDir);
		organizationPick = new VLookup("AD_Org_ID", true, false, true, lookupOrg);
		organizationPick.addVetoableChangeListener(this);
			
		//BPartner_ID=Env.getContextAsInt(Env.getCtx(), p_WindowNo, "C_BPartner_ID");
		//Order_ID=Env.getContextAsInt(Env.getCtx(), p_WindowNo, "C_Order_ID");
		Org_ID=Env.getContextAsInt(Env.getCtx(), p_WindowNo, "AD_Org_ID");
		
		
		loadLines(0,0);
		return true;
	}   //  dynInit
    
	/**
	 *  Static Init.
	 *  <pre>
	 *  parameterPanel
	 *      parameterBankPanel
	 *      parameterStdPanel
	 *          bPartner/order/invoice/shopment/licator Label/Field
	 *  dataPane
	 *  southPanel
	 *      confirmPanel
	 *      statusBar
	 *  </pre>
	 *  @throws Exception
	 */
    private void jbInit() throws Exception
    {
    	userLabel.setText(Msg.getElement(Env.getCtx(), "AD_User_ID"));
    	spjLabel.setText("Proyecto");
    	organizationLabel.setText(Msg.translate(Env.getCtx(), "AD_Org_ID"));
    	CPanel parameterPanel = dialog.getParameterPanel();
    	parameterPanel.setLayout(new BorderLayout());
    	
    	CPanel parameterStdPanel = new CPanel(new GridBagLayout());
    	parameterPanel.add(parameterStdPanel, BorderLayout.CENTER);
    	
    	parameterStdPanel.add(userLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
    			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
   
    		parameterStdPanel.add(userSearch, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
    				,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    	
    		if(SpjSearch!=null){
	        	parameterStdPanel.add(spjLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
	        			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
	        	parameterStdPanel.add(SpjSearch,  new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
	        			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
	        	parameterStdPanel.add(organizationLabel, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
	        			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
	        	parameterStdPanel.add(organizationPick,  new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
	        			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    		}
    		else
    		{
    			parameterStdPanel.add(organizationLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
	        			,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
	        	parameterStdPanel.add(organizationPick,  new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
	        			,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    		}
    }   //  jbInit

	/*************************************************************************/

	private boolean 	m_actionActive = false;
	
	/**
	 *  Action Listener
	 *  @param e event
	 */
	public void actionPerformed(ActionEvent e)
	{
		log.config("Action=" + e.getActionCommand());
		
		if (m_actionActive)
			return;
		m_actionActive = true;
		log.config("Action=" + e.getActionCommand());
		
		m_actionActive = false;
	}   //  actionPerformed

	/**
	 *  Change Listener
	 *  @param e event
	 */
	public void vetoableChange (PropertyChangeEvent e)
	{
		log.config(e.getPropertyName() + "=" + e.getNewValue());

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
		else if (e.getPropertyName().equals("AD_Org_ID"))
		{
			if(e.getNewValue()== null)
				//Org_ID = 0;
				;
			else
				Org_ID = ((Integer) e.getNewValue()).intValue();
			loadLines (AD_UserID,SubProject_ID);
		}
		dialog.tableChanged(null);
	}   //  vetoableChange
	
	/**************************************************************************
	 *  Load BPartner Field
	 *  @param forInvoice true if Invoices are to be created, false receipts
	 *  @throws Exception if Lookups cannot be initialized
	 */
	protected void initBPartner (boolean forInvoice) throws Exception
	{
		
	}   //  initBPartner

	private void loadLines (int AD_User, int SubPro)
	{
		boolean IsSOTrx = "Y".equals(Env.getContext(Env.getCtx(), p_WindowNo, "IsSOTrx"));
		int bPartner_ID=Env.getContextAsInt(Env.getCtx(), p_WindowNo, "C_BPartner_ID");
		
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		
		String sql="";
		
		if(DB.isPostgreSQL())
		{
			sql = "Select A.DocumentNo,A.DATEREQUIRED,A.AD_USER_ID,A.M_Requisition_ID,coalesce(C.M_PRODUCT_ID,0),(C.QTY-coalesce(C.QtyUsed,0)),C.M_Requisitionline_id,B.name,"
					+" coalesce(P.name,'') as Productname,C.PRICEACTUAL,C.C_Project_ID,pj.name as Projectname,C.description,coalesce(h.name,''),coalesce(h.C_Charge_ID,0) "
					+" from M_Requisition A "
					+" inner join M_RequisitionLine C on (A.M_Requisition_ID=C.M_Requisition_ID)"
					+" left outer join M_Product P on (P.M_Product_ID=C.M_Product_ID)"
					+" left outer join C_Charge h on (h.C_Charge_ID=C.C_Charge_ID)"
					+" inner join C_DocType doc on (A.C_DocType_ID=doc.C_DocType_ID)"
					+" left outer join AD_User B on (A.AD_USER_ID=B.AD_USER_ID)"
					+" left outer join C_Project pj on (C.C_Project_ID=pj.C_Project_ID)"
					+" where A.DocStatus='CO' and A.isactive='Y' and C.isactive='Y' and  doc.docbasetype not in ('RRC','PLR','PRT')  and A.AD_Client_ID="+Env.getAD_Client_ID(Env.getCtx());
			//ininoles validacion org * muestre todas las solicitudes			
			if (Org_ID  > 0)
				sql = sql + " and A.AD_Org_ID="+Org_ID;
			else if (Org_ID == 0)
				sql = sql + " and A.AD_Org_ID > 0 ";
			else 
				sql = sql + " and A.AD_Org_ID < 0 ";
		}	
		else
		{
			sql = "Select A.DocumentNo,A.DATEREQUIRED,A.AD_USER_ID,A.M_Requisition_ID,coalesce(C.M_PRODUCT_ID,0),(C.QTY-coalesce(C.QtyUsed,0)),C.M_Requisitionline_id,B.name,"
				+" coalesce(P.name,cast(' ' as nvarchar2(255)  )) as Productname,C.PRICEACTUAL,C.C_Project_ID,pj.name as Projectname,C.description,coalesce(h.name,cast(' ' as nvarchar2(60)  )),coalesce(h.C_Charge_ID,0) "
					+" from M_Requisition A "
					+" inner join M_RequisitionLine C on (A.M_Requisition_ID=C.M_Requisition_ID)"
					+" left outer join M_Product P on (P.M_Product_ID=C.M_Product_ID)"
					+" left outer join C_Charge h on (h.C_Charge_ID=C.C_Charge_ID)"
					+" inner join C_DocType doc on (A.C_DocType_ID=doc.C_DocType_ID)"
					+" left outer join AD_User B on (A.AD_USER_ID=B.AD_USER_ID)"
					+" left outer join C_Project pj on (C.C_Project_ID=pj.C_Project_ID)"
					+" where A.DocStatus='CO' and A.isactive='Y' and C.isactive='Y' and  doc.docbasetype not in ('RRC','PLR','PRT')  and A.AD_Client_ID="+Env.getAD_Client_ID(Env.getCtx());
		
			//ininoles validacion org * muestre todas las solicitudes			
			if (Org_ID  > 0)
				sql = sql + " and A.AD_Org_ID="+Org_ID;
			else if (Org_ID == 0)
				sql = sql + " and A.AD_Org_ID > 0 ";
			else 
				sql = sql + " and A.AD_Org_ID < 0 ";
		}
		sql+=" and (C.QTY-coalesce(C.QtyUsed,0)) > 0";
				
		if(IsSOTrx)
			sql+=" and doc.issotrx='Y' and C.C_BPartner_ID="+bPartner_ID;
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
				Vector<Object> line = new Vector<Object>(10);
				
				line.add(new Boolean(false));       //  0-Selection
				line.add(rs.getTimestamp(2));    //  1--Datetrx
				line.add(rs.getString(8));      //  2--BPartner
				line.add(rs.getString(1));     //3- Documentno
				line.add(rs.getString(12)); 		//4-project
				
				KeyNamePair pp = new KeyNamePair(rs.getInt(5), rs.getString(9));
				line.add(pp);      //  5-product
				
				KeyNamePair pp2 = new KeyNamePair(rs.getInt(15), rs.getString(14));
				line.add(pp2);      //  6-cargo
				
				line.add(rs.getString(13));     // 7 description
				line.add(rs.getBigDecimal(6));       //  8-qty
				line.add(rs.getBigDecimal(10));   //9 price
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
		
		loadTableOIS(data);
	}
	
	
	/**
	 *  Load Order/Invoice/Shipment data into Table
	 *  @param data data
	 */
	protected void loadTableOIS (Vector<?> data)
	{
		//  Remove previous listeners
		dialog.getMiniTable().getModel().removeTableModelListener(dialog);
		//  Set Model
		DefaultTableModel model = new DefaultTableModel(data, getOISColumnNames());
		model.addTableModelListener(dialog);
		dialog.getMiniTable().setModel(model);
		// 
		
		configureMiniTable(dialog.getMiniTable());
	}   //  loadOrder
	
	public void showWindow()
	{
		dialog.setVisible(true);
	}
	
	public void closeWindow()
	{
		dialog.dispose();
	}
}
