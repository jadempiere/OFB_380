/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 Adempiere, Inc. All Rights Reserved.               *
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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.grid.ed.VDate;
import org.compiere.grid.ed.VLookup;
import org.compiere.grid.ed.VNumber;
import org.compiere.model.MCurrency;
import org.compiere.model.MDocType;
import org.compiere.model.MLookupFactory;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPriceList;
import org.compiere.model.MPriceListVersion;
import org.compiere.model.MUser;
import org.compiere.model.PO;
import org.compiere.print.ReportCtl;
import org.compiere.print.ReportEngine;
import org.compiere.process.DocAction;
import org.compiere.sqlj.BPartner;
import org.compiere.swing.CButton;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.swing.CPanel;
import org.compiere.swing.CTextField;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

/**
 *	Checkout Sub Panel
 *	
 *  @author Comunidad de Desarrollo OpenXpertya 
 *         *Basado en Codigo Original Modificado, Revisado y Optimizado de:
 *         *Copyright ï¿½ Jorg Janke
 *  @version $Id: SubCheckout.java,v 1.1 2004/07/12 04:10:04 jjanke Exp $
 */
public class SubCheckout extends PosSubPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6359287546081954105L;

	/**
	 * 	Constructor
	 *	@param posPanel POS Panel
	 */
	public SubCheckout (PosPanel posPanel)
	{
		super (posPanel);
	}	//	PosSubCheckout
	
	private CButton f_register = null;
	public CButton f_summary = null;
	private CButton f_process = null;
	//private CButton f_print = null;
	//
	//ininoles nuevos campos y botones
	public CComboBox		f_PriceList;
	private CButton f_RefreshPrice = null;
	//private CButton f_ConfirmPrice = null;
	public VLookup cOrderLookup;	
	public VLookup cOrderLookupToCopy;
	public int ID_lastOrder;
	public VDate dateField;
	
	//TODO: credit card
/*	private CLabel f_lcreditCardNumber = null;
	private CTextField f_creditCardNumber = null;
	private CLabel f_lcreditCardExp = null;
	private CTextField f_creditCardExp = null;
	private CLabel f_lcreditCardVV = null;
	private CTextField f_creditCardVV = null;
	private CButton f_creditPayment = null;
*/
	private CLabel f_lDocumentNo = null;
	private CTextField f_DocumentNo;
	private CLabel f_lcashGiven = null;
	private VNumber f_cashGiven;
	private CLabel f_lcashReturn = null;
	private VNumber f_cashReturn;
	private CButton f_cashPayment = null;
	
	private CButton f_cashRegisterFunctions;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(SubCheckout.class);
	
	/**
	 * 	Initialize
	 */
	public void init()
	{
		//	Content
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = INSETS2;
		
		//	BOX	1 - CASH 
		gbc.gridx = 0;
		gbc.gridheight = 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = .1;
		CPanel cash = new CPanel(new GridBagLayout());
		cash.setBackground(java.awt.Color.lightGray);
		cash.setBorder(new TitledBorder(Msg.getMsg(Env.getCtx(), "Checkout")));
		gbc.gridy = 0;
		add (cash, gbc);
		GridBagConstraints gbc0 = new GridBagConstraints();
		gbc0.insets = INSETS2;
//		gbc0.anchor = GridBagConstraints.EAST;
		//
		f_lDocumentNo = new CLabel(Msg.getMsg(Env.getCtx(),"DocumentNo"));
		cash.add (f_lDocumentNo, gbc0);
		f_DocumentNo = new CTextField("");
		f_DocumentNo.setName("DocumentNo");
		cash.add (f_DocumentNo, gbc0);
		f_lcashGiven = new CLabel(Msg.getMsg(Env.getCtx(),"CashGiven"));
		cash.add (f_lcashGiven, gbc0);
		f_cashGiven = new VNumber("CashGiven", false, false, true, DisplayType.Amount, Msg.translate(Env.getCtx(), "CashGiven"));
		f_cashGiven.setColumns(12, 25);
		cash.add (f_cashGiven, gbc0);
		f_cashGiven.setValue(Env.ZERO);
		f_cashGiven.addActionListener(this);  //to update the change with the money
		//ininoles se ocultan campos
		f_lcashGiven.setVisible(false);
		f_cashGiven.setVisible(false);
		//
		f_lcashReturn = new CLabel(Msg.getMsg(Env.getCtx(),"CashReturn"));
		cash.add (f_lcashReturn, gbc0);
		f_cashReturn = new VNumber("CashReturn", false, true, false, DisplayType.Amount, "CashReturn");
		f_cashReturn.setColumns(8, 25);
		cash.add (f_cashReturn, gbc0);
		f_cashReturn.setValue(Env.ZERO);
		//ininoles se ocultan campos
		f_lcashReturn.setVisible(false);
		f_cashReturn.setVisible(false);
		
		f_cashPayment = createButtonAction("Payment", null);
		f_cashPayment.setActionCommand("Cash");
		gbc0.weightx = 0.1;		
		cash.add (f_cashPayment, gbc0);
		//ininoles se ocultan campos
		f_cashPayment.setVisible(false);
		
		//	BOX 2 - UTILS
		CPanel utils = new CPanel(new GridBagLayout());
		utils.setBorder(new TitledBorder(Msg.getMsg(Env.getCtx(), "Utils")));
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = .1;
		add (utils, gbc);
		GridBagConstraints gbcU = new GridBagConstraints();
		gbcU.insets = INSETS2;
		gbcU.anchor = GridBagConstraints.EAST;
		//CASH FUNCTIONS
		f_cashRegisterFunctions = createButtonAction("CashRegisterFunction", null);
		f_cashRegisterFunctions.setText("Cash Functions");
	    f_cashRegisterFunctions.setPreferredSize(new Dimension(130,37));
	    f_cashRegisterFunctions.setMaximumSize(new Dimension(130,37));
		f_cashRegisterFunctions.setMinimumSize(new Dimension(130,37));
		//utils.add(f_cashRegisterFunctions, gbcU);
		//antes del boton registrar nuevo campo de orden a copiar
		cOrderLookupToCopy = new VLookup("C_Order_ID", false, true, false,
				MLookupFactory.get (Env.getCtx(), 0, 0, 2161, DisplayType.Search));
		//cOrderLookupToCopy.setValue(p_posPanel.f_curLine.getOrder().get_ID());
		cOrderLookupToCopy.setVisible(true);
		cOrderLookupToCopy.setReadWrite(true);
		cOrderLookupToCopy.addActionListener(this);
		utils.add (cOrderLookupToCopy, gbcU);
		//REGISTER
		f_register = createButtonAction("Register", null);
 		utils.add (f_register, gbcU);
		//SUMMARY
		f_summary = createButtonAction("Summary", null);
		//f_summary.setEnabled(false);
		f_summary.setVisible(false);
		//f_summary.setReadWrite(false);
 		utils.add (f_summary, gbcU);
		//PROCESS
		f_process = createButtonAction("Process", null);
 		utils.add (f_process, gbcU);
		//PRINT
		/*f_print = createButtonAction("Print", null);
 		utils.add (f_print, gbcU);*/
 		//ininoles agregamos lista de precios
 		f_PriceList = new CComboBox();
 		utils.add (f_PriceList, gbcU); 		
 		KeyNamePair pListDefault = new KeyNamePair(p_pos.getM_PriceList().getM_PriceList_ID(), p_pos.getM_PriceList().getName()); 		
 		f_PriceList.addItem(pListDefault);
 		//f_PriceList.addActionListener(this);
 		//aplicar lista de precios
 		f_RefreshPrice = createButtonAction("Aplicar LP", null);
 		f_RefreshPrice.setPreferredSize(new Dimension(70,40));
 		utils.add (f_RefreshPrice, gbcU);
 		//campo de fecha para cambiar fecha de la orden
 		dateField = new VDate("Fecha", false, false, true, DisplayType.Date, "Fecha");
 		dateField.setValue(Env.getContextAsDate(Env.getCtx(), "#Date"));
		dateField.setVisible(true);
		dateField.setReadWrite(true);
		utils.add (dateField, gbcU);	
 		//confirmar promoción
 		/*f_ConfirmPrice = createButtonAction("Confirmar", null);
 		f_ConfirmPrice.setPreferredSize(new Dimension(70,40));
 		utils.add (f_ConfirmPrice, gbcU);*/
 		//agregamos campos de orden
		cOrderLookup = new VLookup("C_Order_ID", false, true, false,
				MLookupFactory.get (Env.getCtx(), 0, 0, 2161, DisplayType.Search));
		cOrderLookup.setValue(p_posPanel.f_curLine.getOrder().get_ID());
		//f_DocumentNo.setText(p_posPanel.f_curLine.getOrder().getDocumentNo());
		cOrderLookup.setVisible(false);
		utils.add (cOrderLookup, gbcU);	
		fillCombos(); 		

		
	
//TODO: Credit card
/*  Panel para la introducciï¿½n de los datos de CreditCard para el pago quitada por ConSerTi al no considerar
 *  que sea ï¿½til de momento
 	
		//	--	1 -- Creditcard 
		CPanel creditcard = new CPanel(new GridBagLayout());
		creditcard.setBorder(new TitledBorder(Msg.translate(Env.getCtx(), "CreditCardType")));
		gbc.gridy = 2;
		add (creditcard, gbc);
		GridBagConstraints gbc1 = new GridBagConstraints();
		gbc1.insets = INSETS2;
		gbc1.anchor = GridBagConstraints.WEST;
		
		gbc1.gridx = 0;
		gbc1.gridy = 0;
		f_lcreditCardNumber = new CLabel(Msg.translate(Env.getCtx(), "CreditCardNumber"));
		creditcard.add (f_lcreditCardNumber, gbc1);
		gbc1.gridy = 1;
		f_creditCardNumber = new CTextField(18); 
		creditcard.add (f_creditCardNumber, gbc1);
		gbc1.gridx = 1;
		gbc1.gridy = 0;
		f_lcreditCardExp = new CLabel(Msg.translate(Env.getCtx(),"CreditCardExp"));
		creditcard.add (f_lcreditCardExp, gbc1);
		gbc1.gridy = 1;
		f_creditCardExp = new CTextField(5); 
		creditcard.add (f_creditCardExp, gbc1);
		gbc1.gridx = 2;
		gbc1.gridy = 0;
		f_lcreditCardVV = new CLabel(Msg.translate(Env.getCtx(), "CreditCardVV"));
		creditcard.add (f_lcreditCardVV, gbc1);
		gbc1.gridy = 1;
		f_creditCardVV = new CTextField(5); 
		creditcard.add (f_creditCardVV, gbc1);
		//
		gbc1.gridx = 3;
		gbc1.gridy = 0;
		gbc1.gridheight = 2;
		f_creditPayment = createButtonAction("Payment", null);
		f_creditPayment.setActionCommand("CreditCard");
		gbc1.anchor = GridBagConstraints.EAST;
		gbc1.weightx = 0.1;
		creditcard.add (f_creditPayment, gbc1);
		
		**/ //fin del comentario para quitar la parte del CreditCard
	}	//	init

	/**
	 * 	Get Panel Position
	 */
	public GridBagConstraints getGridBagConstraints()
	{
		GridBagConstraints gbc = super.getGridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		return gbc;
	}	//	getGridBagConstraints
	
	/**
	 * 	Dispose - Free Resources
	 */
	public void dispose()
	{
		super.dispose();
	}	//	dispose


	/**
	 * 	Action Listener
	 *	@param e event
	 */
	public void actionPerformed (ActionEvent e)
	{
		String action = e.getActionCommand();		
		if (action == null || action.length() == 0)
			return;
		log.info( "PosSubCheckout - actionPerformed: " + action);
		
		//	Register
		if (action.equals("Register"))
		{
			//ininoles se usara para traer lineas en vez de orden
			/*p_posPanel.f_queryTicket.reset();
			p_posPanel.openQuery(p_posPanel.f_queryTicket);*/
			MOrder orderTo = p_posPanel.f_curLine.getOrder();
			if(orderTo.getLines().length <= 0)
			{
				MOrder orderFrom = new MOrder(Env.getCtx(),Integer.parseInt(cOrderLookupToCopy.getValue().toString()), null);
				int cantLinesCopy = copyLinesFrom(orderTo,orderFrom, true, false);
				orderTo.set_CustomColumn("OrderType2", "REN");
				orderTo.set_CustomColumn("C_OrderRef_ID", orderFrom.get_ID());
				orderTo.setC_BPartner_ID(orderFrom.getC_BPartner_ID());
				orderTo.setC_BPartner_Location_ID(orderFrom.getC_BPartner_Location_ID());
				orderTo.save();
				updatePriceOLines(orderTo,  p_posPanel.f_bpartner.getM_PriceList_Version_ID(), -1);
				
				//actualizamos BP de pos
				p_posPanel.f_bpartner.f_name.setValue(orderFrom.getC_BPartner().getName());
				p_posPanel.f_bpartner.setC_BPartner_ID(orderFrom.getC_BPartner_ID());
				p_posPanel.f_bpartner.fillCombos();
				KeyNamePair pBPartnert = new KeyNamePair(orderFrom.getC_BPartner_Location_ID(), orderFrom.getC_BPartner_Location().getName());
				//p_posPanel.f_bpartner.f_location.add(pBPartnert);
				p_posPanel.f_bpartner.f_location.setValue(pBPartnert);
				p_posPanel.f_bpartner.f_Bpartner.setValue(null);
				p_posPanel.f_bpartner.f_locationShip.setValue(null);
				//p_posPanel.f_bpartner.dispose();
				p_posPanel.f_bpartner.f_bSearch.setReadWrite(false);				
				//end
				//SubFunctionKeys.ID_Level = 2;
				//SubFunctionKeys.ID_LevelPrice = 2;
				//SubFunctionKeys.ID_BotonHide = "0";
				//ininoles se setea nivel a cantLinesCopy
				SubFunctionKeys.ID_Level = cantLinesCopy+1;
				SubFunctionKeys.ID_LevelPrice = cantLinesCopy+1;
				p_posPanel.f_functionKeys.removeAll();
				p_posPanel.f_functionKeys.init();
			}
		}
		//	Summary
		else if (action.equals("Summary"))
		{
			displaySummary();			
			//f_summary.setReadWrite(true);
		}
		else if (action.equals("Process"))
		{
			//if (isOrderFullyPay())
			//{
			//ininoles validacion de stock
			MOrder order = p_posPanel.f_curLine.getOrder();						
			
			if(!validStock(order, order.get_TrxName()))
			{
				p_posPanel.f_status.setStatusLine("ERROR: Problemas de Stock");
				JOptionPane.showMessageDialog(null,"ERROR: Problemas de Stock");
			}
			else if(validCantLinePL(order, order.get_TrxName()) > 0)
			{
				p_posPanel.f_status.setStatusLine("ERROR: Pedido debe incluir al menos "+validCantLinePL(order, order.get_TrxName())+
						" líneas por restricción de la lista de precios");
				JOptionPane.showMessageDialog(null,"ERROR: Pedido debe incluir al menos "+validCantLinePL(order, order.get_TrxName())+
						" líneas por restricción de la lista de precios");
			}
			else
			{
				DB.executeUpdate("UPDATE C_OrderLine SET IsConfirmed = 'Y' WHERE C_Order_ID = "+order.get_ID(), order.get_TrxName());
				displaySummary();
				//Check if order is completed, if so, print and open drawer, create an empty order and set cashGiven to zero
				if(processOrder())
				{
					//printTicket();
					openCashDrawer();
					//ininoles guardamos ultimo ID para ser seteado al procesar
					ID_lastOrder = p_posPanel.f_curLine.getOrder().get_ID();
					p_posPanel.newOrder();
					f_cashGiven.setValue(Env.ZERO);
					p_posPanel.f_status.setStatusLine("ORDEN INGRESADA");	
					p_posPanel.f_bpartner.f_bSearch.setReadWrite(true);
				}		
				//ininoles recargamos panel de botonera
				p_posPanel.f_functionKeys.removeAll();
				SubFunctionKeys.ID_Level=1;
				SubFunctionKeys.ID_LevelPrice=1;
				//SubFunctionKeys.ID_BotonShow = "";
				SubFunctionKeys.ID_BotonHide = "0";
				p_posPanel.f_functionKeys.init();
				//reseteamos cantidad
				p_posPanel.f_salesRep.setQty(Env.ONE);
				//ininoles end
				f_PriceList.setEditable(true);
				f_PriceList.setReadWrite(true);
				cOrderLookup.setVisible(true);
				cOrderLookup.setValue(ID_lastOrder);
				dateField.setReadWrite(true);
				//ininoles nuevos campos solo lectura
				p_posPanel.f_bpartner.f_name.setReadWrite(true);
				p_posPanel.f_bpartner.f_name.setEditable(true);
				p_posPanel.f_bpartner.f_bSearch.setReadWrite(true);
				p_posPanel.f_bpartner.f_location.setReadWrite(true);
				p_posPanel.f_bpartner.f_location.setEditable(true);
				/*}
				else
				{
					p_posPanel.f_status.setStatusLine("PAYMENT NOT FULL.");
				}*/
			}
		}
		//	Print
		else if (action.equals("Print"))
		{
			if (isOrderFullyPay())
			{
				displaySummary();
				printTicket();
				openCashDrawer();
			}
			else
			{
				p_posPanel.f_status.setStatusLine("Order not fully paid.");
			}
		}
		//	Cash (Payment)
		else if (action.equals("Cash"))
		{
			displayReturn();			
			openCashDrawer();
		}
		else if (action.equals("CashRegisterFunction"))
		{
			p_posPanel.openQuery(p_posPanel.f_cashfunctions);
		}
		else if (e.getSource() == f_cashGiven)
			displayReturn();
		else if (action.equals("Aplicar LP"))
		{
			p_posPanel.f_status.setStatusLine("");	
			MOrder order = p_posPanel.f_curLine.getOrder();
			KeyNamePair pl = (KeyNamePair)f_PriceList.getSelectedItem();
			MPriceList pListNew = new MPriceList(Env.getCtx(), pl.getKey(), null);
			int levePListOld = DB.getSQLValue(null, 
					"SELECT LevelNo FROM M_PriceList WHERE M_PriceList_ID="+order.getM_PriceList_ID());
			if(pl !=null)
			{
				//validacion solo si ya hay lineas se hace validacion
				if(order.getLines().length > 0)
				{
					if(pListNew.get_ValueAsInt("LevelNo") < levePListOld)
					{
						p_posPanel.f_status.setStatusLine("ERROR: Incorrect Price List");					
						KeyNamePair plOld = new KeyNamePair(order.getM_PriceList_ID(), order.getM_PriceList().getName());
						f_PriceList.setSelectedItem(plOld);
						JOptionPane.showMessageDialog(null,"ERROR: Incorrect Price List");
					}
					else
					{
						order.setM_PriceList_ID(pl.getKey());
						order.save();
						p_pos.setM_PriceList_ID(pl.getKey());
						p_posPanel.f_bpartner.setM_PriceList_Version_ID(pl.getKey());
						//updatePriceOLines(order, p_posPanel.f_bpartner.getM_PriceList_Version_ID(), SubFunctionKeys.ID_Level);
						//updatePriceOLines(order, p_posPanel.f_bpartner.getM_PriceList_Version_ID(), SubFunctionKeys.ID_LevelPrice);
						//ininoles ahora cuando se aplique la LP se calculara el nivel denuevo
						updatePriceOLines(order, p_posPanel.f_bpartner.getM_PriceList_Version_ID(), -1);
					}
				}
				else
				{
					order.setM_PriceList_ID(pl.getKey());
					order.save();
					p_pos.setM_PriceList_ID(pl.getKey());
					p_posPanel.f_bpartner.setM_PriceList_Version_ID(pl.getKey());
					//updatePriceOLines(order, p_posPanel.f_bpartner.getM_PriceList_Version_ID(), SubFunctionKeys.ID_Level);
					SubFunctionKeys.ID_Level=1;
					SubFunctionKeys.ID_LevelPrice=1;
					//SubFunctionKeys.ID_BotonShow = "";
					SubFunctionKeys.ID_BotonHide = "0";
					p_posPanel.f_functionKeys.removeAll();
					p_posPanel.f_functionKeys.init();
				}				
			}		
			displaySummary();			
		}
		else if (action.equals("Confirmar"))
		{
			//ininoles se setea cantidad, se actualizan las lineas a confirmadas y se deja PL no modificable.
			p_posPanel.f_checkout.f_PriceList.setReadWrite(false);
			p_posPanel.f_checkout.f_PriceList.setEditable(false);
			
			MOrderLine[] oLines = p_posPanel.f_curLine.getOrder().getLines(" AND IsConfirmed = 'N' ", "line");			
			for (int i = 0; i < oLines.length; i++)
			{
				MOrderLine line = oLines[i];
				line.setQty((BigDecimal) p_posPanel.f_salesRep.f_quantity.getValue());
				line.setLineNetAmt();
				line.setTax();
				line.set_CustomColumn("IsConfirmed", true);
				line.save();
			}
			p_posPanel.updateInfo();
			p_posPanel.f_salesRep.setQty(Env.ONE);
			//recargamos botonera			
			SubFunctionKeys.ID_Level=1;
			SubFunctionKeys.ID_LevelPrice=1;
			//SubFunctionKeys.ID_BotonShow = "";
			SubFunctionKeys.ID_BotonHide = "0";
			p_posPanel.f_functionKeys.removeAll();
			p_posPanel.f_functionKeys.init();			
			
		}
		else if (e.getSource() == cOrderLookupToCopy)
			cOrderLookupToCopy.setValue(cOrderLookupToCopy.getValue());
		/*else if (e.getSource() == dateField)
		{
			dateField.setValue(dateField.getValue());
			MOrder order = p_posPanel.f_curLine.getOrder();
			order.setDateOrdered((Timestamp)dateField.getValue());
			order.save();			
		}*/
/*		//	CreditCard (Payment)
		else if (action.equals("CreditCard"))
		{
			Log.print("CreditCard");
		}  fin del comentario para la Credit Card*/
		
		p_posPanel.updateInfo();
	}	//	actionPerformed

	public void displaySummary() {
		p_posPanel.f_status.setStatusLine(p_posPanel.f_curLine.getOrder().getSummary());
		displayReturn();
	}

	/**
	 * 	Process Order
	 *  @author Comunidad de Desarrollo OpenXpertya 
	 *         *Basado en Codigo Original Modificado, Revisado y Optimizado de:
	 *         *Copyright ï¿½ ConSerTi
	 */
	public boolean processOrder()
	{		
		//Returning orderCompleted to check for order completness
		boolean orderCompleted = false;
		p_posPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		MOrder order = p_posPanel.f_curLine.getOrder();
		if (order != null)
			// check if order completed OK
			if (order.getDocStatus().equals("DR") )
			{ 
				order.setC_BPartner_Location_ID(p_posPanel.f_bpartner.getC_BPartner_Location_ID());
				order.setDocAction(DocAction.ACTION_Complete);
				order.setDocStatus("IP");
				order.save();
				//orden no se completará				
				/*try
				{
					if (order.processIt(DocAction.ACTION_Complete) )
					{
						order.save();
					}
					else
					{
						log.info( "SubCheckout - processOrder FAILED");	
						p_posPanel.f_status.setStatusLine("Order can not be completed, verifique el libro");				
					}
				}
				catch (Exception e)
				{
					log.severe("Order can not be completed - " + e.getMessage());
					p_posPanel.f_status.setStatusLine("Error when processing order, verifique el libro" );
				}*/
				//finally
				//{ // When order failed convert it back to draft so it can be processed
				  /*if( order.getDocStatus().equals("IN") )
				  {
					order.setDocStatus("DR");
				  }
				  else if( order.getDocStatus().equals("CO") )
				  {
					order = null;
					orderCompleted = true;
					//p_posPanel.newOrder();
					//f_cashGiven.setValue(Env.ZERO);
					log.info( "SubCheckout - processOrder OK");
					p_posPanel.f_status.setStatusLine("Order completed.");
					//ininoles recargamos panel de botonera
					p_posPanel.f_functionKeys.removeAll();
					p_posPanel.f_functionKeys.ID_BotonHide = "";
					p_posPanel.f_functionKeys.ID_Level=1;
					p_posPanel.f_functionKeys.ID_BotonShow = "";
					p_posPanel.f_functionKeys.init();
					//ininoles end
				  }			
				  else
				  {
					log.info( "SubCheckout - processOrder - unrecognized DocStatus");
					p_posPanel.f_status.setStatusLine("Orden was not completed correctly.");	 
				  }*/
				order = null;
				orderCompleted = true;
				//p_posPanel.newOrder();
				//f_cashGiven.setValue(Env.ZERO);
				log.info( "SubCheckout - processOrder OK");
				p_posPanel.f_status.setStatusLine("Order completed.");				
				//} // try-finally
				  
			}
		p_posPanel.setCursor(Cursor.getDefaultCursor());
		return orderCompleted;
	}	// processOrder
	
	/**
	 * 	Print Ticket
	 *  @author Comunidad de Desarrollo OpenXpertya 
	 *  *Basado en Codigo Original Modificado, Revisado y Optimizado de:
	 *  *Copyright ï¿½ ConSerTi
	 */
	public void printTicket()
	{
		MOrder order = p_posPanel.f_curLine.getOrder();
		//int windowNo = p_posPanel.getWindowNo();
		//Properties m_ctx = p_posPanel.getPropiedades();
		
		if (order != null)
		{
			try 
			{
				//TODO: to incorporate work from Posterita
				/*
				if (p_pos.getAD_PrintLabel_ID() != 0)
					PrintLabel.printLabelTicket(order.getC_Order_ID(), p_pos.getAD_PrintLabel_ID());
				*/ 
				//print standard document
				//ReportCtl.startDocumentPrint(ReportEngine.ORDER, order.getC_Order_ID(), null, Env.getWindowNo(this), true);// original
				ReportCtl.startDocumentPrint(ReportEngine.POS, order.getC_Order_ID(), null, Env.getWindowNo(this), true);//faaguilar
				
			}
			catch (Exception e) 
			{
				log.severe("PrintTicket - Error Printing Ticket");
			}
		}	  
	}	

	/**
	 * 	Display cash return
	 *  Display the difference between tender amount and bill amount
	 *  @author Comunidad de Desarrollo OpenXpertya 
 *         *Basado en Codigo Original Modificado, Revisado y Optimizado de:
 *         *Copyright ï¿½ ConSerTi
	 */
	public void displayReturn()
	{
		BigDecimal given = new BigDecimal(f_cashGiven.getValue().toString());
 		if (p_posPanel != null && p_posPanel.f_curLine != null)
		{
			MOrder order = p_posPanel.f_curLine.getOrder();
			BigDecimal total = new BigDecimal(0);
			if (order != null)
				{
  				f_DocumentNo.setText(order.getDocumentNo());
				total = order.getGrandTotal();
				}				
			double cashReturn = given.doubleValue() - total.doubleValue();
			f_cashReturn.setValue(new BigDecimal(cashReturn));
			//cOrderLookup.setValue(p_posPanel.f_curLine.getOrder().get_ID());
		}
	}	

	/**
	 * Is order fully pay ?
	 * Calculates if the given money is sufficient to pay the order
	 * 
	 * @author Comunidad de Desarrollo OpenXpertya 
 *         *Basado en Codigo Original Modificado, Revisado y Optimizado de:
 *         *Copyright ï¿½ ConSerTi
	 */
	public boolean isOrderFullyPay()
	{
		BigDecimal given = new BigDecimal(f_cashGiven.getValue().toString());
		boolean paid = false;
		if (p_posPanel != null && p_posPanel.f_curLine != null)
		{
			MOrder order = p_posPanel.f_curLine.getOrder();
			//faaguilar OFB for new windows without payment begin
			MDocType dt = MDocType.get(Env.getCtx(), order.getC_DocTypeTarget_ID());
			if(dt.getDocSubTypeSO().equals("WP") || dt.getDocSubTypeSO().equals("RM"))
				return true;
			//faaguilar OFB for new windows without payment end
			BigDecimal total = new BigDecimal(0);
			if (order != null)
				total = order.getGrandTotal();
			paid = given.doubleValue() >= total.doubleValue();
		}
		return paid;
	}
	
	
	/**
	 * 	Abrir caja
	 *  Abre la caja registradora
	 *  @author Comunidad de Desarrollo OpenXpertya 
 *         *Basado en Codigo Original Modificado, Revisado y Optimizado de:
 *         *Copyright ï¿½ ConSerTi
	 */
	public void openCashDrawer()
	{
		//String puerto = null;
		//TODO - to incorporate work from Posterita
		/*
		try
		{
			String sql = "SELECT p.Port"
					+ " FROM AD_PrintLabel l"
					+ " INNER JOIN AD_LabelPrinter p ON (l.AD_LabelPrinter_ID=p.AD_LabelPrinter_ID)"
					+ " WHERE l.AD_PrintLabel_ID=?";
			puerto = DB.getSQLValueString(null, sql, p_pos.getAD_PrintLabel_ID());
		}
		catch(Exception e)
		{
			log.severe("AbrirCaja - Puerto no encontrado");
		}*/
		
		/*
		if (puerto == null)
			log.severe("Port is mandatory for cash drawner");
		
		try
		{
			byte data[] = new byte[5];
			data[0] = 27;
			data[1] = 112;
			data[2] = 0;
			data[3] = 50;
			data[4] = 50;
			FileOutputStream fos = new FileOutputStream(puerto);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			bos.write(data, 0, data.length);
			bos.close();
			fos.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}*/
	}	
	
	public void fillCombos()
	{
		Vector<KeyNamePair> PListVector = new Vector<KeyNamePair>();
		if (f_PriceList != null)
		{
			//ininoles nuevo select de listas de precios
			String sqlPList = null; 
			MUser userC = new MUser(p_ctx, Env.getAD_User_ID(Env.getCtx()), null);
			/*sqlPList = "SELECT M_PriceList_ID FROM C_BPartnerPriceList WHERE IsActive = 'Y'" +
			" AND C_BPartner_ID = "+p_posPanel.f_bpartner.getC_BPartner_ID()+" AND AD_Client_ID = "+Env.getAD_Client_ID(Env.getCtx());*/
			sqlPList = "SELECT M_PriceList_ID FROM M_PriceList WHERE M_PriceList_ID IN ( " +
					" SELECT pl.M_PriceList_ID FROM M_PriceList pl " + //select listas de precio en socio de negocio
					" INNER JOIN C_BPartnerPriceList bpl ON pl.M_PriceList_ID = bpl.M_PriceList_ID" +
					" WHERE pl.IsActive = 'Y' AND bpl.IsActive = 'Y' AND pl.IsSOPriceList = 'Y' " +
					" AND pl.IsInBPartner = 'Y' AND bpl.C_BPartner_ID = "+p_posPanel.f_bpartner.getC_BPartner_ID()+
					
					" UNION SELECT pl.M_PriceList_ID FROM M_PriceList pl " + //select listas de precio en usuario
					" INNER JOIN AD_UserPriceList upl ON pl.M_PriceList_ID = upl.M_PriceList_ID " +
					" WHERE pl.IsActive = 'Y' AND upl.IsActive = 'Y' AND pl.IsSOPriceList = 'Y' " +
					" AND pl.IsInUser = 'Y' AND upl.AD_User_ID = "+Env.getAD_User_ID(Env.getCtx())+
					
					/*" UNION SELECT pl.M_PriceList_ID FROM M_PriceList pl " + //select listas de precio en rol
					" INNER JOIN AD_RolePriceList rpl ON pl.M_PriceList_ID = rpl.M_PriceList_ID " +
					" WHERE pl.IsActive = 'Y' AND rpl.IsActive = 'Y' AND pl.IsSOPriceList = 'Y' " +
					" AND pl.IsInRole = 'Y' AND rpl.AD_Role_ID = "+Env.getAD_Role_ID(Env.getCtx())+*/
					
					" UNION SELECT pl.M_PriceList_ID FROM M_PriceList pl " +
					" INNER JOIN AD_RolePriceList rpl ON pl.M_PriceList_ID = rpl.M_PriceList_ID " +
					" WHERE pl.IsActive = 'Y' AND rpl.IsActive = 'Y' AND pl.IsSOPriceList = 'Y'  " +
					" AND ((pl.IsInChannel = 'Y' AND rpl.C_Channel_ID = "+userC.get_ValueAsInt("C_Channel_ID")+") " +
					" 	OR (pl.IsInRole = 'Y' AND rpl.AD_Role_ID ="+Env.getAD_Role_ID(Env.getCtx())+") " +
					//"	OR (pl.IsInPTerm = 'Y' AND rpl.C_PaymentTerm_ID = "+p_posPanel.f_curLine.getOrder().getC_PaymentTerm_ID()+"))" +
					//ininoles ahora aparecen todas las listas de precios asociadas a terminos de. Ahora se valida al completar NV
					"	OR (pl.IsInPTerm = 'Y' ))" +
					
					" UNION SELECT pl.M_PriceList_ID FROM M_PriceList pl " + //select listas de precio no BP no USER
					" WHERE pl.IsActive = 'Y' AND pl.IsSOPriceList = 'Y' AND pl.IsInUser = 'N' " +
					" AND pl.IsInBPartner = 'N' AND pl.IsInRole = 'N' " +
					" AND pl.IsInPTerm = 'N' AND pl.IsInChannel = 'N' " +
					" AND pl.AD_Client_ID = "+Env.getAD_Client_ID(Env.getCtx())+")" +
					" AND (dateStart IS NULL OR dateStart < now()) AND (dateEnd IS NULL OR dateEnd > now())"+
					" ORDER BY name";					
			
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sqlPList, null);							
				rs = pstmt.executeQuery();
				//MPriceList[] PriceLists = new MPriceList[10];
				while (rs.next())
				{
					MPriceList pList = new MPriceList(Env.getCtx(), rs.getInt("M_PriceList_ID"), null);
					PListVector.add(pList.getKeyNamePair());
				}	
				rs.close();
				pstmt.close();
				pstmt = null;
			}
			catch (SQLException e3)
			{
				log.config("error"+e3+" SQL: "+sqlPList);
			}
			finally
			{
				DB.close(rs, pstmt);
				rs = null; pstmt = null;
			}
		}
		DefaultComboBoxModel pListModel = new DefaultComboBoxModel(PListVector); 
		f_PriceList.setModel(pListModel);
		KeyNamePair pListDefault = new KeyNamePair(p_pos.getM_PriceList().getM_PriceList_ID(), p_pos.getM_PriceList().getName());
		f_PriceList.setValue(pListDefault);
		MOrder order = p_posPanel.f_curLine.getOrder();
		order.setM_PriceList_ID(pListDefault.getKey());
		order.save();		
		p_posPanel.f_bpartner.setM_PriceList_Version_ID(pListDefault.getKey());
	}	//	fillCombos
	public void updatePriceOLines(MOrder order, int ID_pListVersion, int id_level)
	{
		if(id_level < 0)//recalculo de nivel
		{
			id_level = DB.getSQLValue(order.get_TrxName(), "SELECT COUNT(1) from m_productprice " +
					" where m_pricelist_version_id = "+getM_PriceList_Version_ID(order)+" and levels = 1 " +
					" and addlevel = 'Y' and m_product_id in " +
					"  ( select m_product_id from c_orderline where c_order_id = "+order.get_ID()+")");
		}
		if(id_level < 1)
			id_level = 1;
		MOrderLine[] oLines = order.getLines(" AND IsConfirmed = 'N' ", "line");	
		//id_level = id_level-1;
		for (int i = 0; i < oLines.length; i++)
		{
			MOrderLine line = oLines[i];
			//calculamos precio nuevo
			BigDecimal amt = null;
			String sqlAmt = "";
			if(id_level == 1)
			{
				try 
				{							
					 /*sqlAmt = "SELECT Price FROM M_ProductPriceRef WHERE IsActive = 'Y'" +
						" AND M_Product_ID = "+ID_ProductL1+" AND Levels = '0"+ID_Level+"' " +
						" AND M_ProductRef_ID = "+key.getM_Product_ID()+" AND M_ProductRef2_ID = "+ID_LastProduct+
						" AND M_PriceList_Version_ID = "+p_posPanel.f_bpartner.getM_PriceList_Version_ID();*/
					 sqlAmt = "SELECT PriceList FROM M_ProductPrice WHERE IsActive = 'Y' " +
					 	" AND M_Product_ID = "+line.getM_Product_ID()+" AND Levels = "+id_level+
					 	" AND M_PriceList_Version_ID = "+ID_pListVersion;
					 amt = DB.getSQLValueBD(null, sqlAmt);
				}catch (Exception e2) 
				{
					log.config("error"+e2+" SQL: "+sqlAmt);
					amt = Env.ZERO;
				}
			}
			else if(id_level > 1)
			{
				try 
				{	
					sqlAmt = "SELECT Price FROM M_ProductPriceRef WHERE IsActive = 'Y' " +
					 	" AND M_Product_ID = "+line.getM_Product_ID()+" AND Levels = "+id_level+
					 	" AND M_PriceList_Version_ID = "+ID_pListVersion;
					 amt = DB.getSQLValueBD(null, sqlAmt);
				}catch (Exception e2) 
				{
					log.config("error"+e2+" SQL: "+sqlAmt);
					amt = Env.ZERO;
				}
			}
			if(amt != null)
			{
				line.setPrice(amt);
				line.setLineNetAmt();
				line.setTax();
				line.save();
			}
		}
	}
	//ininoles metodos de copiar lineas nuevos
	public int copyLinesFrom (MOrder toOrder, MOrder otherOrder, boolean counter, boolean copyASI)
	{
		if (toOrder.isProcessed() || toOrder.isPosted() || toOrder == null)
			return 0;
		//MOrderLine[] fromLines = otherOrder.getLines(" AND IsFree = 'N' AND M_Product_ID > 0 " +
		MOrderLine[] fromLines = otherOrder.getLines(" AND IsFree = 'N' AND M_Product_ID > 0 " +
				" AND M_Product_ID NOT IN " +
				" (SELECT M_Product_ID FROM M_Product mp " +
				" INNER JOIN M_Product_Category mpc ON mpc.M_Product_Category_ID = mp.M_Product_Category_ID " +
				" WHERE mpc.description like 'NOEDITORIAL')", "line");
		int count = 0;
		for (int i = 0; i < fromLines.length; i++)
		{
			MOrderLine line = new MOrderLine(otherOrder);
			copyValues(fromLines[i], line,toOrder.getAD_Org_ID());
			line.setC_Order_ID(toOrder.getC_Order_ID());
			//
			line.setQtyDelivered(Env.ZERO);
			line.setQtyInvoiced(Env.ZERO);
			line.setQtyReserved(Env.ZERO);
			line.setDateDelivered(null);
			line.setDateInvoiced(null);
			//
			line.setOrder(toOrder);
			int I_ZERO = new Integer(0);
			line.set_ValueNoCheck ("C_OrderLine_ID", I_ZERO);	//	new
			//	References
			if (!copyASI)
			{
				line.setM_AttributeSetInstance_ID(0);
				line.setS_ResourceAssignment_ID(0);
			}
			if (counter)
				line.setRef_OrderLine_ID(fromLines[i].getC_OrderLine_ID());
			else
				line.setRef_OrderLine_ID(0);

			// don't copy linked lines
			line.setLink_OrderLine_ID(0);
			//	Tax
			if (toOrder.getC_BPartner_ID() != otherOrder.getC_BPartner_ID())
				line.setTax();		//	recalculate
			//
			//
			line.set_CustomColumn("C_BpartnerRef_ID", fromLines[i].get_ValueAsInt("C_BpartnerRef_ID"));
			line.setC_BPartner_Location_ID(fromLines[i].getC_BPartner_Location_ID());
			line.setProcessed(false);
			line.set_CustomColumn("DatePromised2", null);
			line.set_CustomColumn("DatePromised3", null);
			
			//lineas se confirman
			//ininoles ahora las lineas no son confirmadas
			//line.set_CustomColumn("IsConfirmed", true);
			line.set_CustomColumn("IsConfirmed", false);

			if (line.save(toOrder.get_TrxName()))
				count++;
			//	Cross Link
			if (counter)
			{
				fromLines[i].setRef_OrderLine_ID(line.getC_OrderLine_ID());
				fromLines[i].save(toOrder.get_TrxName());
			}
			//se actualiza variable que oculta productos
			SubFunctionKeys.ID_BotonHide= SubFunctionKeys.ID_BotonHide+","+line.getM_Product_ID();
		}
		if (fromLines.length != count)
			log.log(Level.SEVERE, "Line difference - From=" + fromLines.length + " <> Saved=" + count);
		return count;
	}	//	copyLinesFrom	
	public static void copyValues (PO from, PO to,int AD_Org_ID)
	{
		PO.copyValues (from, to);
		to.setAD_Org_ID(AD_Org_ID);
	}	//	copyValues	
	public static boolean validStock (MOrder order, String Trx)
	{
		MOrderLine[] oLines2 = order.getLines(false, null);
		for (int i = 0; i < oLines2.length; i++)
		{
			MOrderLine line = oLines2[i];
			if(line.getM_Product_ID() > 0)
			{
				if(line.getM_Product().getProductType().compareToIgnoreCase("I")==0)
				{
					BigDecimal qty = DB.getSQLValueBD(Trx,"select bomqtyavailablecopesa("+line.getM_Product_ID()+","+order.getM_Warehouse_ID()+","+line.get_ValueAsInt("M_Locator_ID")+")");
					if(qty.compareTo(Env.ZERO)<0)
						return false;
				}
			}
		}
		return true;
	}	//	copyValues
	public static int validCantLinePL (MOrder order, String Trx)
	{
		int cant = DB.getSQLValue(Trx, "select count(1) from c_orderline col" +
				" join c_order co on (co.c_order_id = col.c_order_id) " +
				" join m_product mp on (mp.m_product_id = col.m_product_id) " +
				" where mp.description not in ('FULLSD') " +
				" and col.isactive = 'Y' and c_orderlineref_id is null " +
				" and co.c_order_id = "+order.get_ID());
		MPriceList pList = new MPriceList(Env.getCtx(), order.getM_PriceList_ID(), Trx);
		int cantLinePL = pList.get_ValueAsInt("minLevel");
		if(cant < cantLinePL)
			return cantLinePL;
		else
			return 0;
	}	
	public int getM_PriceList_Version_ID(MOrder order)
	{
		int M_PriceList_ID = order.getM_PriceList_ID();
		MPriceList pl = MPriceList.get(p_ctx, M_PriceList_ID, null);		
		MPriceListVersion plv = pl.getPriceListVersion (p_posPanel.getToday());
		if (plv != null && plv.getM_PriceList_Version_ID() != 0)
			return plv.getM_PriceList_Version_ID();
		else 
			return -1;
	}	//	getM_PriceList_Version_ID
	
}	//	PosSubCheckout
