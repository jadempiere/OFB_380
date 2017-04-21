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

//import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;

import org.compiere.grid.ed.VNumber;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MPriceList;
import org.compiere.swing.CButton;
import org.compiere.swing.CLabel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;


/**
 *	Sales Rep Sub Panel
 *	
 *  @author Comunidad de Desarrollo OpenXpertya 
 *         *Basado en Codigo Original Modificado, Revisado y Optimizado de:
 *         *Copyright (c) Jorg Janke
 *  @version $Id: SubSalesRep.java,v 1.1 2004/07/12 04:10:04 jjanke Exp $
 */
public class SubSalesRep extends PosSubPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 840666209988831145L;

	/**
	 * 	Constructor
	 *	@param posPanel POS Panel
	 */
	public SubSalesRep (PosPanel posPanel)
	{
		super (posPanel);
	}	//	PosSubSalesRep
	
	private CLabel f_label = null;
	private CButton f_button = null;
	//private CButton DiscountBtn; //faaguilar OFB
	//private VNumber f_discount;  //faaguilar OFB 
	//private BigDecimal discount;  //faaguilar OFB 

	private CButton f_plus;
	private CButton f_minus;
	public VNumber f_quantity;
	
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(SubSalesRep.class);
	
	/**
	 * 	Initialize
	 */
	public void init()
	{
		//	Title
		TitledBorder border = new TitledBorder(Msg.translate(Env.getCtx(), "QtyPOS"));
		setBorder(border);
		
		//	Content
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = INSETS2;
		//	--
		f_label = new CLabel("Cantidad Producto  ", CLabel.LEADING);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.BOTH;
		add (f_label, gbc);
		//
		//ininoles nuevos campos de cantidad
		f_minus = createButtonAction("Minus", null);		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		add(f_minus, gbc);		
		//cantidad
		f_quantity = new VNumber("QtyOrdered", false, false, true,
				DisplayType.Quantity, Msg.translate(Env.getCtx(), "QtyOrdered"));		
		f_quantity.addActionListener(this);
		f_quantity.setPreferredSize(new Dimension(50,25));
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		add(f_quantity, gbc);
		setQty(Env.ONE);
		//
		f_plus = createButtonAction("Plus", null);
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.NONE;
		add(f_plus, gbc);
		
		//faaguilar OFB discount begin
		/*DiscountBtn = new CButton("Descontar");
		DiscountBtn.addActionListener(this);
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.BOTH;
		add (DiscountBtn, gbc);*/
		
		/*f_discount = new VNumber("PriceActual", false, false, true,
				DisplayType.Amount, Msg.translate(Env.getCtx(), "PriceActual"));
		f_discount.setColumns(8, 20);
		f_discount.setValue(Env.ZERO);
		f_discount.addActionListener(this);
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.BOTH;
		add (f_discount, gbc);*/
		//faaguilar OFB discount end
		
		f_button = new CButton ("Confirmar");
		//f_button.setActionCommand("Confirmar");
		f_button = createButtonAction("Confirmar", null);
		f_button.setFocusable(false);
		f_button.addActionListener(this);
		f_button.setPreferredSize(new Dimension(70,40));
		gbc.gridx = 4; //faaguilar OFB original value 1
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.NONE;
		add (f_button, gbc);
	}	//	init

	/**
	 * 	Get Panel Position
	 */
	public GridBagConstraints getGridBagConstraints()
	{
		GridBagConstraints gbc = super.getGridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
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
		log.info( "PosSubSalesRep - actionPerformed: " + action);
		//faaguilar OFB begin
		log.info( "PosSubSalesRep - actionPerformed: " + action);
		if(action.equals("Confirmar"))
		{/*
			JPasswordField pf = new JPasswordField();
			Object[] message = new Object[] {"Ingrese Password", pf};
			Object[] options = new String[] {"OK", "Cancel"};
			JOptionPane op = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, options);
			JDialog dialog = op.createDialog(null, "Password");
			dialog.setVisible(true);

			String seleccion = String.valueOf(pf.getPassword()); 
			
			if(p_pos.getPassword().equals(seleccion) &&  discount.signum()>0){
				BigDecimal appliedDiscount=discount.divide(new BigDecimal("1.19"),0, BigDecimal.ROUND_HALF_EVEN);
				MOrderLine discountLine= new MOrderLine(p_posPanel.f_curLine.getOrder());
				discountLine.setC_Charge_ID(DB.getSQLValue(null, "select C_Charge_ID from C_charge where name='DESCUENTO'"));
				discountLine.setQty(Env.ONE);
				discountLine.setPrice(appliedDiscount.negate());
				discountLine.save();
				p_posPanel.updateInfo();
				f_discount.setValue(Env.ZERO);
			}
			else
				JOptionPane.showMessageDialog(
						   this,
						   "Password invalida");
		 	*/
			//Antes de setar actualizamos fecha de la cabecera
			MOrder order = p_posPanel.f_curLine.getOrder();
			//ininoles validacion de stock
			if(!validStockBeforeConfirm(order, null,(BigDecimal) f_quantity.getValue()))
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

				order.setDatePromised((Timestamp)p_posPanel.f_checkout.dateField.getValue());
				//se setean fechas con fecha de hoy
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);			
			    Timestamp timeNow = new Timestamp(cal.getTimeInMillis());			
				//order.setDateOrdered((Timestamp)p_posPanel.f_checkout.dateField.getValue());			
				//order.set_CustomColumn("DateCompleted", (Timestamp)p_posPanel.f_checkout.dateField.getValue());
			    order.setDateOrdered(timeNow);			
				order.set_CustomColumn("DateCompleted", timeNow);
				order.save();
				p_posPanel.f_checkout.dateField.setReadWrite(false);
				
				//ininoles se setea cantidad, se actualizan las lineas a confirmadas y se deja PL no modificable.
				p_posPanel.f_checkout.f_PriceList.setReadWrite(false);
				p_posPanel.f_checkout.f_PriceList.setEditable(false);
				
				//ininoles se saca solo lectura boton sumary			
				//p_posPanel.f_checkout.f_summary.setEnabled(true);
				//p_posPanel.f_checkout.f_summary.setReadWrite(true);
				//p_posPanel.f_checkout.f_summary.setVisible(true);
				
				MOrderLine[] oLines = p_posPanel.f_curLine.getOrder().getLines(" AND IsConfirmed = 'N' ", "line");			
				for (int i = 0; i < oLines.length; i++)
				{
					MOrderLine line = oLines[i];
					line.setQty((BigDecimal) f_quantity.getValue());
					line.setLineNetAmt();
					line.setTax();
					line.set_CustomColumn("IsConfirmed", true);
					line.save();
				}
				p_posPanel.updateInfo();
				setQty(Env.ONE);
				//ininoles se deja no modificable socio de facturación
				p_posPanel.f_bpartner.f_name.setReadWrite(false);
				p_posPanel.f_bpartner.f_bSearch.setReadWrite(false);
				p_posPanel.f_bpartner.f_location.setReadWrite(false);
				//recargamos botonera			
				SubFunctionKeys.ID_Level=1;
				SubFunctionKeys.ID_LevelPrice=1;
				//SubFunctionKeys.ID_BotonShow = "";
				SubFunctionKeys.ID_BotonHide = "0";
				/*p_posPanel.f_functionKeys.removeAll();
				p_posPanel.f_functionKeys.init();*/
				p_posPanel.updateBtn();
				//p_posPanel.f_checkout.displayReturn();
				p_posPanel.f_checkout.displaySummary();
				//p_posPanel.dispose();
				//p_posPanel.setCursor(Cursor.getDefaultCursor());
				//ininoles logica para que solo se prete el boton 1 vez
				//f_button.setVisible(false);
				//f_button.setReadWrite(false);
				//f_button.setEnabled(false);
			}			
		}
		/*else if (action.equals("VNumber")){
			f_discount.setValue(f_discount.getValue());
			discount=(BigDecimal)f_discount.getValue();
		}*/
		else if (action.equals("Plus"))
		{
			f_quantity.plus();
			p_posPanel.updateInfo();
		}
		//	Minus
		else if (action.equals("Minus"))
		{
			f_quantity.minus(1);
			p_posPanel.updateInfo();
		}
		else if (e.getSource() == f_quantity)
		{
			f_quantity.setValue(f_quantity.getValue());
			p_posPanel.updateInfo();
		}
		/*else if (e.getSource() == f_button)//	Logout
		//faaguilar OFB end
		//	Logout
		p_posPanel.dispose();*/
		//p_posPanel.updateInfo();
	}	//	actinPerformed
	public void setQty(BigDecimal qty) {
		f_quantity.setValue(qty);
	} //	setQty
	public static boolean validStockBeforeConfirm (MOrder order, String Trx, BigDecimal qtyToAdd)
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
					qty = qty.subtract(qtyToAdd);
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
	
}	//	PosSubSalesRep
