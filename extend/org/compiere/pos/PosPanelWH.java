/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                        *
 * Copyright (C) 1999-2006 Adempiere, Inc. All Rights Reserved.                *
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

package org.compiere.pos;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.*;

import javax.swing.*;

import org.compiere.swing.*;
import org.compiere.apps.*;
import org.compiere.apps.form.*;
import org.compiere.model.*;

import java.util.logging.*;
import org.compiere.util.*;

/**
 *	Point of Sales Main Window.
 *
 *  @author Comunidad de Desarrollo OpenXpertya 
 *         *Basado en Codigo Original Modificado, Revisado y Optimizado de:
 *         *Copyright (c) Jorg Janke
 *  @version $Id: PosPanel.java,v 1.10 2004/07/12 04:10:04 jjanke Exp $
 */
public class PosPanelWH extends PosPanel
{
	/**
	 * 	Constructor - see init 
	 */
	public PosPanelWH()
	{
		super ();
		/*originalKeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		m_focusMgr = new PosKeyboardFocusManager();
		KeyboardFocusManager.setCurrentKeyboardFocusManager(m_focusMgr);*/
	}	//	PosPanel

//	/**	Window No			*/
//	private int         	m_WindowNo = 0;
//	/**	FormFrame			*/
//	private FormFrame 		m_frame;
//	/**	Logger				*/
	private CLogger			log = CLogger.getCLogger(getClass());
//	/** Context				*/
	private Properties		m_ctx = Env.getCtx();
//	/** Sales Rep 			*/
//	private int				m_SalesRep_ID = 0;
//	/** POS Model			*/
//	protected MPOS			p_pos = null;
//	/** Keyoard Focus Manager		*/
//	private PosKeyboardFocusManager	m_focusMgr = null;
//
//	/**	Status Bar					*/
//	protected StatusBar			f_status = new StatusBar();
//	/** Customer Panel				*/
//	protected SubBPartner 		f_bpartner = null;
//	/** Sales Rep Panel				*/
//	protected SubSalesRep 		f_salesRep = null;
//	/** Current Line				*/
//	protected SubCurrentLine 	f_curLine = null;
//	/** Product	Selection			*/
//	protected SubProduct 		f_product = null;
//	/** All Lines					*/
//	protected SubLines 			f_lines = null;
//	/** Function Keys				*/
//	protected SubFunctionKeys 	f_functionKeys = null;
//	/** Checkout					*/
//	protected SubCheckout 		f_checkout = null;
//	/** Basic Keys					*/
////	protected SubBasicKeys 		f_basicKeys = null;
//	
//	/**	Product Query Window		*/
//	protected QueryProduct		f_queryProduct = null;
//	/**	BPartner Query Window		*/
//	protected QueryBPartner		f_queryBPartner = null;
//	/** Ticket Query Window			*/
//	protected QueryTicket 		f_queryTicket = null;
//	
//	protected CashSubFunctions 	f_cashfunctions;
//	
//	//	Today's (login) date		*/
	private Timestamp			m_today = Env.getContextAsDate(m_ctx, "#Date");
//	
//	private KeyboardFocusManager originalKeyboardFocusManager;
//	
	/**
	 *	Initialize Panel
	 *  @param WindowNo window
	 *  @param frame parent frame
	 */
	public void init (int WindowNo, FormFrame frame)
	{
		frame.setMaximize(true);
		setm_SalesRep_ID(Env.getAD_User_ID(Env.getCtx()));
		log.info("init - SalesRep_ID=" + getm_SalesRep_ID());
		setm_WindowNo(WindowNo);
		setm_frame(frame);
		//
		try
		{
			if (!dynInit())
			{
				dispose();
				frame.dispose();
				return;
			}
			frame.getContentPane().add(this, BorderLayout.CENTER);
			frame.getContentPane().add(f_status, BorderLayout.SOUTH);
		//	this.setPreferredSize(new Dimension (800-20,600-20));
		}
		catch(Exception e)
		{
			log.log(Level.SEVERE, "init", e);
		}
		log.config( "PosPanel.init - " + getPreferredSize());
		getm_focusMgr().start();
	}	//	init

//	/**
//	 * 	Dispose - Free Resources
//	 */
//	public void dispose()
//	{
//		if (m_focusMgr != null)
//			m_focusMgr.stop();
//		m_focusMgr = null;
//		KeyboardFocusManager.setCurrentKeyboardFocusManager(originalKeyboardFocusManager);
//		//
//		if (f_bpartner != null)
//			f_bpartner.dispose();
//		f_bpartner = null;
//		if (f_salesRep != null)
//			f_salesRep.dispose();
//		f_salesRep = null;
//		if (f_curLine != null)
//		{
//			f_curLine.deleteOrder();
//			f_curLine.dispose();
//		}
//		f_curLine = null;
//		if (f_product != null)
//			f_product.dispose();
//		f_product = null;
//		if (f_lines != null)
//			f_lines.dispose();
//		f_lines = null;
//		if (f_functionKeys != null)
//			f_functionKeys.dispose();
//		f_functionKeys = null;
//		if (f_checkout != null)
//			f_checkout.dispose();
//		f_checkout = null;
///*		if (f_basicKeys != null)
//			f_basicKeys.dispose();			removed by ConSerTi upon not appreciating its functionality
//		f_basicKeys = null;
//*/		//
//		if (f_queryProduct != null)
//			f_queryProduct.dispose();
//		f_queryProduct = null;
//		if (f_queryBPartner != null)
//			f_queryBPartner.dispose();
//		f_queryBPartner = null;
//		if (f_queryTicket != null)
//			f_queryTicket.dispose();
//		f_queryTicket = null;
//		//
//		if (f_cashfunctions != null)
//			f_cashfunctions.dispose();
//		f_cashfunctions = null;
//		if (m_frame != null)
//			m_frame.dispose();
//		m_frame = null;
//		m_ctx = null;
//	}	//	dispose

	
	/**************************************************************************
	 * 	Dynamic Init.
	 * 	PosPanel has a GridBagLayout.
	 * 	The Sub Panels return their position
	 */
	private boolean dynInit()
	{
		if (!this.setMPOS())
			return false;
		
		//	Create Sub Panels
		f_bpartner = new SubBPartner (this);
		add (f_bpartner, f_bpartner.getGridBagConstraints());
		//
		/*f_salesRep = new SubSalesRep (this);
		add (f_salesRep, f_salesRep.getGridBagConstraints());*/
		//
		f_curLine = new SubCurrentLine (this);
		add (f_curLine, f_curLine.getGridBagConstraints());
		//
		f_product = new SubProduct (this);
		add (f_product, f_product.getGridBagConstraints());
		//
		f_lines = new SubLines (this);
		add (f_lines, f_lines.getGridBagConstraints());
		//
		f_functionKeys = new SubFunctionKeys(this);
		add (f_functionKeys, f_functionKeys.getGridBagConstraints());
		//
		f_checkout = new SubCheckout (this);
		add (f_checkout, f_checkout.getGridBagConstraints());
		//
/*		f_basicKeys = new SubBasicKeys (this);
		add (f_basicKeys, f_basicKeys.getGridBagConstraints());  removed by ConSerTi upon not appreciating its functionality
*/		
		//	--	Query
		f_queryProduct = new QueryProduct (this);
		add (f_queryProduct, f_queryProduct.getGridBagConstraints());
		//
		f_queryBPartner = new QueryBPartner (this);
		add (f_queryBPartner, f_queryBPartner.getGridBagConstraints());
		//
		f_queryTicket = new QueryTicket(this);
		add (f_queryTicket, f_queryTicket.getGridBagConstraints());
		//
		f_cashfunctions = new CashSubFunctions(this);
		add (f_cashfunctions, f_cashfunctions.getGridBagConstraints());
		
		//faaguilar OFB bind keys begin	    
		CPanel content=(CPanel) f_functionKeys.getComponent(0);
		content.getComponentCount();
		MPOSKey[] 	m_keys;
		MPOSKeyLayout fKeys = MPOSKeyLayout.get(Env.getCtx(), p_pos.getC_POSKeyLayout_ID());
		m_keys = fKeys.getKeys(false);
		
		for (int i = 0; i < m_keys.length && i<12; i++)
		{
			CButton source=(CButton) content.getComponent(i);
			String myaction="F"+ (i+1);
			source.getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(myaction), myaction);
			source.getActionMap().put(myaction, new AbstractAction() {
		        public void actionPerformed(ActionEvent e) {
		        	CButton source = (CButton) e.getSource();
		            System.out.println("Activated: " + source.getText() + ":" + source.getActionCommand());
		            int C_POSKey_ID = Integer.parseInt(source.getActionCommand());	
						MPOSKey key = new MPOSKey (Env.getCtx(), C_POSKey_ID, null);
						
							f_product.setM_Product_ID(key.getM_Product_ID());
							f_product.setPrice();
							f_curLine.setQty(key.getQty());
							f_curLine.saveLine();
							updateInfo();
							return;
				}

		    });
		}
		//faaguilar OFB bind keys end
		/*
		//faaguilar hide information begin
		CPanel cash=(CPanel)f_checkout.getComponent(4);
		cash.setVisible(false);
		CPanel cash2=(CPanel)f_checkout.getComponent(5);
		cash2.setVisible(false);
		//faaguilar hide information end
		
		//faaguilar OFB new Button begin
		CButton nueva=(CButton) f_checkout.getComponent(1);
		//nueva.addActionListener(this);*/
		//faaguilar OFB new Button end
		
		newOrder();
		return true;
	}	//	dynInit

//	/**
//	 * 	Set MPOS
//	 *	@return true if found/set
//	 */
	private boolean setMPOS()
	{
		MPOS[] poss = null;
		
			poss = this.getPOSs (0);

		//
		if (poss.length == 0)
		{
			ADialog.error(getWindowNo(), getm_frame(), "NoPOSForUser");
			return false;
		}
		else if (poss.length == 1)
		{
			p_pos = poss[0];
			return true;
		}
		//faaguilar OFB begin
		Object[] SelectionPos= new Object[poss.length];
		for(int x=0;x<poss.length;x++){
			SelectionPos[x]=poss[x].getDisplayName();
		}
		//faaguilar OFB end
		//	Select POS
		String msg = Msg.getMsg(Env.getCtx(), "SelectPOS");
		String title = Env.getHeader(Env.getCtx(), getWindowNo());
		Object selection = JOptionPane.showInputDialog(getm_frame(), msg, title, 
				JOptionPane.QUESTION_MESSAGE, null, SelectionPos, SelectionPos[0]);//faaguilar OFB
		if (selection != null)
		{
			p_pos = poss[0].getPosbyDisplay((String)selection); //faaguilar OFB
			return true;
		}
		return false;
	}	//	setMPOS
//	
	/**
	 * 	Get POSs for specific Sales Rep or all
	 *	@param SalesRep_ID
	 *	@return array of POS
	 */
	private MPOS[] getPOSs (int SalesRep_ID)
	{
		ArrayList<MPOS> list = new ArrayList<MPOS>();
		String sql = "SELECT p.* FROM C_POS p " +
				"Inner join C_DocType d on (p.c_doctype_id=d.c_doctype_id)" +
				" WHERE p.SalesRep_ID=? and d.docsubtypeso='WP'";
		if (SalesRep_ID == 0)
			sql = "SELECT p.* FROM C_POS p " +
			"Inner join C_DocType d on (p.c_doctype_id=d.c_doctype_id)" +
					" WHERE p.AD_Client_ID=? and d.docsubtypeso='WP'";
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			if (SalesRep_ID != 0)
				pstmt.setInt (1, getm_SalesRep_ID());
			else
				pstmt.setInt (1, Env.getAD_Client_ID(Env.getCtx()));
			ResultSet rs = pstmt.executeQuery ();
			while (rs.next ())
				list.add(new MPOS(Env.getCtx(), rs, null));
			rs.close ();
			pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		try
		{
			if (pstmt != null)
				pstmt.close ();
			pstmt = null;
		}
		catch (Exception e)
		{
			pstmt = null;
		}
		MPOS[] retValue = new MPOS[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getPOSs
//
//	
//	/**
//	 * 	Set Visible
//	 *	@param aFlag visible
//	 */
//	public void setVisible (boolean aFlag)
//	{
//		super.setVisible (aFlag);
//		f_product.f_name.requestFocus();
//	}	//	setVisible
//
//	
//	/**
//	 * 	Open Query Window
//	 *	@param panel
//	 */
//	public void openQuery (CPanel panel)
//	{
//		if (panel.equals(f_cashfunctions))
//		{
//			f_bpartner.setVisible(false);
//			f_salesRep.setVisible(false);
//			f_curLine.setVisible(false);
//			f_product.setVisible(false);
//		}
//		f_checkout.setVisible(false);
////		f_basicKeys.setVisible(false);  removed by ConSerTi upon not appreciating its functionality
//		f_lines.setVisible(false);
//		f_functionKeys.setVisible(false);
//		panel.setVisible(true);
//		
//	}	//	closeQuery
//
//	/**
//	 * 	Close Query Window
//	 *	@param panel
//	 */
//	public void closeQuery (CPanel panel)
//	{
//		panel.setVisible(false);
//		f_bpartner.setVisible(true);
//		f_salesRep.setVisible(true);
//		f_curLine.setVisible(true);
//		f_product.setVisible(true);
////		f_basicKeys.setVisible(true);   removed by ConSerTi upon not appreciating its functionality
//		f_lines.setVisible(true);
//		f_functionKeys.setVisible(true);
//		f_checkout.setVisible(true);
//	}	//	closeQuery
//
//	/**************************************************************************
//	 * 	Get Today's date
//	 *	@return date
//	 */
//	public Timestamp getToday()
//	{
//		return m_today;
//	}	//	getToday
//	
	/**
	 * 	New Order
	 *   
	 */
	public void newOrder()
	{
		log.info( "PosPabel.newOrder");
		f_bpartner.setC_BPartner_ID(0);		
		f_curLine.newOrder();
		f_curLine.newLine();
		f_product.f_name.requestFocus();
		
		
		updateInfo();
	}	//	newOrder
//	
//	/**
//	 * Get the number of the window for the function calls that it needs 
//	 * 
//	 * @return the window number
//	 */
//	public int getWindowNo()
//	{
//		return m_WindowNo;
//	}
//	
//	/**
//	 * Get the properties for the process calls that it needs
//	 * 
//	 * @return las Propiedades m_ctx
//	 */
//	public Properties getPropiedades()
//	{
//		return m_ctx;
//	}
//	
//	public void updateInfo()
//	{
//		if (f_lines != null)
//			f_lines.updateTable(f_curLine.getOrder());
//		if (f_checkout != null)
//			f_checkout.displayReturn();
//	}	
//	
//	 public void actionPerformed(ActionEvent e) {
//     	CButton source = (CButton) e.getSource();
//         System.out.println("Activated: " + source.getText());
//	 }
}	//	PosPanel

