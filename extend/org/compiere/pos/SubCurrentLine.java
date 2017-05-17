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

import java.awt.Event;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;

import org.compiere.grid.ed.VNumber;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MLocator;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MOrderTax;
import org.compiere.model.MProduct;
import org.compiere.model.MRole;
import org.compiere.model.MUser;
import org.compiere.model.MWarehouse;
import org.compiere.swing.CButton;
import org.compiere.swing.CComboBox;
import org.compiere.swing.CLabel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;
import org.compiere.util.Msg;

/**
 * Current Line Sub Panel
 * 
 * @author Comunidad de Desarrollo OpenXpertya 
 *         *Basado en Codigo Original Modificado, Revisado y Optimizado de:
 *         *Copyright ï¿½ Jorg Janke
 * @version $Id: SubCurrentLine.java,v 1.3 2004/07/24 04:31:52 jjanke Exp $
 * red1 - [2093355 ] Small bugs in OpenXpertya POS
 */
public class SubCurrentLine extends PosSubPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4023538043556457231L;

	/**
	 * Constructor
	 * 
	 * @param posPanel
	 *            POS Panel
	 */
	public SubCurrentLine(PosPanel posPanel) {
		super(posPanel);
	} //	PosSubCurrentLine
	
	private CButton f_new;

	private CButton f_reset;

	private CButton f_plus;

	private CButton f_minus;

	private CLabel f_currency;

	private VNumber f_price;

	private CLabel f_uom;

	private VNumber f_quantity;

	private MOrder m_order = null;
	//ininoles nuevo campo bodega en la linea
	/*private CComboBox	f_Warehouse;
	private CLabel f_lWarehouse = null;*/
	private CComboBox	f_Locator;
	private CLabel f_lLocator = null;
	
	//ininoles nuevo campo de monto mensual
	//private BigDecimal monthlyAmount = null;
	//ininoles nuevo campo de nivel
	private int level = 0;
	/**	Logger			*/
	private static CLogger log = CLogger.getCLogger(SubCurrentLine.class);
	
	/**
	 * Initialize
	 */ 
	public void init() {
		//	Title
		TitledBorder border = new TitledBorder(Msg.getMsg(Env.getCtx(),
				"CurrentLine"));
		setBorder(border);

		//	Content
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = INSETS2;
		gbc.gridy = 0;
		//	--		
		//ininoles se cambian orden de botones
		/*f_new = createButtonAction("Save", KeyStroke.getKeyStroke(
				KeyEvent.VK_INSERT, Event.SHIFT_MASK));
		gbc.gridx = 0;
		add(f_new, gbc);*/
		//ininoles 
		f_uom = new CLabel("--");
		gbc.gridx = 0;
		add(f_uom, gbc);
		//ininoles end
		
		//
		f_reset = createButtonAction("Reset", null);
		gbc.gridx = GridBagConstraints.RELATIVE;
		add(f_reset, gbc);
		//ininoles se ocultan campos
		f_reset.setVisible(false);		
		//
		f_currency = new CLabel("---");
		gbc.anchor = GridBagConstraints.EAST;
		gbc.weightx = .1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(f_currency, gbc);
		//ininoles se ocultan campos
		f_currency.setVisible(false);
		//
		f_price = new VNumber("PriceActual", false, false, true,
				DisplayType.Amount, Msg.translate(Env.getCtx(), "PriceActual"));
		f_price.addActionListener(this);
		f_price.setColumns(10, 25);
		//ininoles se ocultan campos
		f_price.setVisible(false);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		add(f_price, gbc);
		setPrice(Env.ZERO);
		//	--
		/*f_uom = new CLabel("--");
		gbc.anchor = GridBagConstraints.EAST;
		gbc.weightx = .1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(f_uom, gbc);*/
		//
		//nuevo campo de bodega
		f_lLocator = new CLabel("Ubicación");
		gbc.anchor = GridBagConstraints.EAST;
		gbc.weightx = .1;
		gbc.fill = GridBagConstraints.EAST;
		add(f_lLocator, gbc);
		
		f_Locator = new CComboBox();
		gbc.anchor = GridBagConstraints.WEST; 
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add (f_Locator, gbc);
		//fillWarehouse();
		fillLocator();
		/*KeyNamePair wareHouseDefault = new KeyNamePair(p_pos.getM_Warehouse_ID(), p_pos.getM_Warehouse().getName());
		f_Locator.setValue(wareHouseDefault);*/
		MRole rol = new MRole(Env.getCtx(), Env.getAD_Role_ID(Env.getCtx()),null);
		if(rol.get_ValueAsInt("M_Locator_ID") > 0)
		{
			MLocator loc = new MLocator(Env.getCtx(), rol.get_ValueAsInt("M_Locator_ID"), null);
			KeyNamePair wareHouseDefault = new KeyNamePair(loc.get_ID(), loc.getValue());
			f_Locator.setValue(wareHouseDefault);
			//actualizamos almacém
			if(m_order != null)
			{
				m_order.setM_Warehouse_ID(loc.getM_Warehouse_ID());
				m_order.saveEx();
			}
		}
		
		f_uom = new CLabel("--");
		gbc.anchor = GridBagConstraints.EAST;
		gbc.weightx = .1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(f_uom, gbc);
		//ininoles
		f_uom.setVisible(false);
		
		f_minus = createButtonAction("Minus", null);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		add(f_minus, gbc);
		//ininoles se ocultan campos
		f_minus.setVisible(false);
		//
		f_quantity = new VNumber("QtyOrdered", false, false, true,
				DisplayType.Quantity, Msg.translate(Env.getCtx(), "QtyOrdered"));
		f_quantity.addActionListener(this);
		f_quantity.setColumns(5, 25);
		add(f_quantity, gbc);
		setQty(Env.ONE);
		//ininoles se ocultan campos
		f_quantity.setVisible(false);
		//
		f_plus = createButtonAction("Plus", null);
		add(f_plus, gbc);
		//ininoles se ocultan campos
		f_plus.setVisible(false);		
		//ininoles
		f_new = createButtonAction("Save", KeyStroke.getKeyStroke(
				KeyEvent.VK_INSERT, Event.SHIFT_MASK));
		add(f_new, gbc);
		//ininoles end		
	} //	init

	/**
	 * Get Panel Position
	 */
	public GridBagConstraints getGridBagConstraints() {
		GridBagConstraints gbc = super.getGridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		return gbc;
	} //	getGridBagConstraints

	/**
	 * Dispose - Free Resources
	 */
	public void dispose() {
		super.dispose();
	} //	dispose

	/**
	 * Action Listener
	 * 
	 * @param e event
	 */
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action == null || action.length() == 0)
			return;
		log.info( "SubCurrentLine - actionPerformed: " + action);
		//	New / Reset
		if (action.equals("Save"))
		{
			if(saveLine())
			{
				p_posPanel.f_functionKeys.removeAll();
				p_posPanel.f_functionKeys.init();
			}
		}
		else if (action.equals("Reset"))
			newLine();
		//	Plus
		else if (action.equals("Plus"))
			f_quantity.plus();
		//	Minus
		else if (action.equals("Minus"))
			f_quantity.minus(1);
		//	VNumber
		else if (e.getSource() == f_price)
			f_price.setValue(f_price.getValue());
		else if (e.getSource() == f_quantity)
			f_quantity.setValue(f_quantity.getValue());
		p_posPanel.updateInfo();
		//p_posPanel.updateBtn();
	} //	actionPerformed

	/***************************************************************************
	 * Set Currency
	 * 
	 * @param currency
	 *            currency
	 */
	public void setCurrency(String currency) {
		if (currency == null)
			f_currency.setText("---");
		else
			f_currency.setText(currency);
	} //	setCurrency

	/**
	 * Set UOM
	 * 
	 * @param UOM
	 */
	public void setUOM(String UOM) {
		if (UOM == null)
			f_uom.setText("--");
		else
			f_uom.setText(UOM);
	} //	setUOM

	/**
	 * Set Price
	 * 
	 * @param price
	 */
	public void setPrice(BigDecimal price) {
		if (price == null)
			price = Env.ZERO;
		f_price.setValue(price);
		boolean rw = Env.ZERO.compareTo(price) == 0 || p_pos.isModifyPrice();
		f_price.setReadWrite(rw);
	} //	setPrice

	/**
	 * Get Price
	 * 
	 * @return price
	 */
	public BigDecimal getPrice() {
		return (BigDecimal) f_price.getValue();
	} //	getPrice

	/**
	 * Set Qty
	 * 
	 * @param qty
	 */
	public void setQty(BigDecimal qty) {
		f_quantity.setValue(qty);
	} //	setQty

	/**
	 * Get Qty
	 * 
	 * @return qty
	 */
	public BigDecimal getQty() {
		return (BigDecimal) f_quantity.getValue();
	} //	getQty

	/***************************************************************************
	 * New Line
	 */
	public void newLine() {
		p_posPanel.f_product.setM_Product_ID(0);
		setQty(Env.ONE);
		setPrice(Env.ZERO);
	} //	newLine

	/**
	 * Save Line
	 * 
	 * @return true if saved
	 */
	public boolean saveLine() {
		MProduct product = p_posPanel.f_product.getProduct();
		if (product == null)
			return false;				
		
		
		if(p_posPanel.f_bpartner.getC_BPartner_ID() <= 0 || p_posPanel.f_bpartner.getC_BPartner_Location_ID() <= 0
				|| p_posPanel.f_bpartner.getC_BpartnertRef_ID() <= 0 || p_posPanel.f_bpartner.getC_BPartner_LocationShip_ID() <= 0)
		{
			JOptionPane.showMessageDialog(null,"ERROR: LLenar todos los campos");
			p_posPanel.f_checkout.displayReturn();
			return true;
		}
		else
		{
			BigDecimal QtyOrdered = (BigDecimal) f_quantity.getValue();
			//ininoles seteo de precios se hara siempre antes de guardar 
			//independiente de como se ingrese el producto en el pos
			//obtenemos monto nuevo
			BigDecimal amt = null;
			BigDecimal amtMonthly = null;
			String sqlAmt = "";
			String sqlAmtMonthly = "";
			//ininoles validacion de stock
			BigDecimal qtyStock = DB.getSQLValueBD(null, "SELECT bomqtyavailablecopesa(M_Product_ID,"+m_order.getM_Warehouse_ID()+","+getM_Locator_ID()+")"+
					" FROM M_Product WHERE M_Product_ID = "+product.get_ID());
			if(qtyStock.compareTo(Env.ZERO) <= 0)
			{
				JOptionPane.showMessageDialog(null,"ERROR: Sin Stock de Producto");
				//p_posPanel.f_checkout.displayReturn();
				//p_posPanel.updateBtn();
				return false;
			}

			String PPriceFlag = "Y";
			PPriceFlag = DB.getSQLValueString(null, "SELECT AddLevel FROM M_ProductPrice WHERE IsActive = 'Y' " +
			 	" AND M_Product_ID = "+product.get_ID()+" AND Levels = 1"+
			 	" AND M_PriceList_Version_ID = "+p_posPanel.f_bpartner.getM_PriceList_Version_ID());
			if(PPriceFlag.equals("Y"))
				SubFunctionKeys.ID_LevelPrice = Math.min(SubFunctionKeys.ID_LevelPrice + 1, SubFunctionKeys.ID_Level);
			
			if(SubFunctionKeys.ID_LevelPrice == 1)
			{
				try 
				{	 
					 sqlAmt = "SELECT PriceStd FROM M_ProductPrice WHERE IsActive = 'Y' " +
					 	" AND M_Product_ID = "+product.get_ID()+" AND Levels = "+SubFunctionKeys.ID_LevelPrice+
					 	" AND M_PriceList_Version_ID = "+p_posPanel.f_bpartner.getM_PriceList_Version_ID();
					 amt = DB.getSQLValueBD(null, sqlAmt);
					 //precio mensual
					 sqlAmtMonthly = "SELECT PricePat FROM M_ProductPrice WHERE IsActive = 'Y' " +
					 	" AND M_Product_ID = "+product.get_ID()+" AND Levels = "+SubFunctionKeys.ID_LevelPrice+
					 	" AND M_PriceList_Version_ID = "+p_posPanel.f_bpartner.getM_PriceList_Version_ID();
					 amtMonthly = DB.getSQLValueBD(null, sqlAmtMonthly);
					 //end				 
				}catch (Exception e2) 
				{
					log.config("error"+e2+" SQL: "+sqlAmt);
					amt = Env.ZERO;
				}
			}
			else if(SubFunctionKeys.ID_LevelPrice > 1)
			{
				try 
				{	
					sqlAmt = "SELECT Price FROM M_ProductPriceRef WHERE IsActive = 'Y' " +
					 	" AND M_Product_ID = "+product.get_ID()+" AND Levels = "+SubFunctionKeys.ID_LevelPrice+
					 	" AND M_PriceList_Version_ID = "+p_posPanel.f_bpartner.getM_PriceList_Version_ID();
					 amt = DB.getSQLValueBD(null, sqlAmt);
					 //precio mensual
					 sqlAmtMonthly = "SELECT PricePAT FROM M_ProductPriceRef WHERE IsActive = 'Y' " +
					 	" AND M_Product_ID = "+product.get_ID()+" AND Levels = "+SubFunctionKeys.ID_LevelPrice+
					 	" AND M_PriceList_Version_ID = "+p_posPanel.f_bpartner.getM_PriceList_Version_ID();
					 amtMonthly = DB.getSQLValueBD(null, sqlAmtMonthly);
					 //end
				}catch (Exception e2) 
				{
					log.config("error"+e2+" SQL: "+sqlAmt);
					amt = Env.ZERO;
				}
			}
			//ininoles end		
			
			//BigDecimal PriceActual = ((BigDecimal) f_price.getValue()).divide(new BigDecimal("1.19"), 0); //faaguilar OFB sustract tax
			BigDecimal PriceActual = amt;
					
			//ininoles end	
			MOrderLine line = createLine(product, QtyOrdered, PriceActual);
			if (line == null)
				return false;
			line.setC_BPartner_Location_ID(p_posPanel.f_bpartner.getC_BPartner_LocationShip_ID());
			//ininoles seteamos campos adcionales		
			line.set_CustomColumn("C_BPartnerRef_ID", p_posPanel.f_bpartner.getC_BpartnertRef_ID());		
			line.set_CustomColumn("QtyOriginal",QtyOrdered);
			line.set_CustomColumn("C_CalendarCOPESA_ID",product.get_Value("C_CalendarCOPESA_ID"));
			//ininoles ahora lista de precios se bloquera luego de la primera confirmacion de promocion
			p_posPanel.f_checkout.cOrderLookup.setVisible(false);
			//ininoles nuevo campo precio mensual
			//ininoles se usan variables locales para precio mensual
			if(amtMonthly != null && amtMonthly.compareTo(Env.ZERO) > 0)
				line.set_CustomColumn("MonthlyAmount",amtMonthly);
			if(level > 0)
				line.set_CustomColumn("LevelNo",level);
			//seteo de bodega
			/*line.setM_Warehouse_ID(getM_Warehouse_ID());*/
			line.set_CustomColumn("M_Locator_ID",getM_Locator_ID());
			if (!line.save())
				return false;
			//
			newLine();
			//antes del return actualizamos los precios de las ordenes
			updateOrderLines(m_order);
			//ininoles se actualiza botonera y busqueda de producto siempre
			p_posPanel.f_product.setPrice(PriceActual);
			
			//se deja en solo lectura los campos de socio de negocio y direccion de despacho
			p_posPanel.f_bpartner.f_name.setReadWrite(false);
			p_posPanel.f_bpartner.f_name.setEditable(false);
			p_posPanel.f_bpartner.f_location.setReadWrite(false);
			p_posPanel.f_bpartner.f_location.setEditable(false);
			p_posPanel.f_bpartner.f_bSearch.setReadWrite(false);			
			p_posPanel.updateInfo();
			SubFunctionKeys.ID_Level = SubFunctionKeys.ID_Level +1;
/*HCH20170420: El AddLevel debe tomarlo justo antes de obtener los precios, no después.
			String PPriceFlag = "Y";
			PPriceFlag = DB.getSQLValueString(null, "SELECT AddLevel FROM M_ProductPrice WHERE IsActive = 'Y' " +
			 	" AND M_Product_ID = "+product.get_ID()+" AND Levels = 1"+
			 	" AND M_PriceList_Version_ID = "+p_posPanel.f_bpartner.getM_PriceList_Version_ID());
			if(PPriceFlag.equals("Y"))
				SubFunctionKeys.ID_LevelPrice = SubFunctionKeys.ID_LevelPrice +1;
*/				
			SubFunctionKeys.ID_BotonHide= SubFunctionKeys.ID_BotonHide+","+product.get_ID();
		}
		
		return true;
	} //	saveLine

	/** 
	 * to erase the lines from order
	 * @return true if deleted
	 */
	public void deleteLine (int row) {
		if (m_order != null && row != -1 )
		{
			MOrderLine[] lineas = m_order.getLines(true,"line asc");//faaguilar OFB add "line asc"
			
			int numLineas = lineas.length;
			if (numLineas > row)
			{
				//delete line from order - true only when DRAFT is not PREPARE-IT()
				lineas[row].setQtyDelivered(Env.ZERO);//faaguilar OFB Patch
				lineas[row].setQtyReserved(Env.ZERO);//faaguilar OFB Patch
				//ininoles guardamos ID antes de borrar || seteamos variables
				//p_posPanel.f_functionKeys.ID_BotonHide = p_posPanel.f_functionKeys.ID_BotonHide.replace(Integer.toString(lineas[row].getM_Product_ID())+";","");
				//p_posPanel.f_functionKeys.ID_BotonShow = p_posPanel.f_functionKeys.ID_BotonShow+lineas[row].getM_Product_ID()+";";
				SubFunctionKeys.ID_Level = SubFunctionKeys.ID_Level - 1;	
				SubFunctionKeys.ID_LevelPrice = SubFunctionKeys.ID_LevelPrice - 1;				
				lineas[row].delete(true);
				for (int i = row; i < (numLineas - 1); i++)
					lineas[i] = lineas[i + 1];
				lineas[numLineas - 1] = null;				
				//ininoles se saca ID de producto para recagar botonera
				p_posPanel.f_functionKeys.removeAll();				
				p_posPanel.f_functionKeys.init();
				//ininoles end
			}
		}
	} //	deleteLine
	
	/**
	 * Delete order from database
	 * 
	 * @author Comunidad de Desarrollo OpenXpertya 
 *         *Basado en Codigo Original Modificado, Revisado y Optimizado de:
 *         *Copyright ï¿½ ConSerTi
	 */		
	public void deleteOrder () {
		if (m_order != null)
		{
			if (m_order.getDocStatus().equals("DR") )//&& m_order.getLines().length < 1)
			{
				MOrderLine[] lineas = m_order.getLines();
				if (lineas != null)
				{
					int numLineas = lineas.length;
					if (numLineas > 0)
						for (int i = numLineas - 1; i >= 0; i--)
						{
							if (lineas[i] != null)
								deleteLine(i);
						}
				}
				
				MOrderTax[] taxs = m_order.getTaxes(true);
				if (taxs != null)
				{
					int numTax = taxs.length;
					if (numTax > 0)
						for (int i = taxs.length - 1; i >= 0; i--)
						{
							if (taxs[i] != null)
								taxs[i].delete(true);
							taxs[i] = null;
						}
				}
				
				m_order.delete(true);
				m_order = null;
			}
		}
	} //	deleteOrder
	
	/**
	 * Create new order
	 * 
	 * @author Comunidad de Desarrollo OpenXpertya 
 *         *Basado en Codigo Original Modificado, Revisado y Optimizado de:
 *         *Copyright ï¿½ ConSerTi
	 */
	public void newOrder()
	{
		m_order = null;
		m_order = getOrder();	
	}

	/**
	 * Get/create Header
	 * 
	 * @return header or null
	 */
	public MOrder getOrder() {
		if (m_order == null) {
			m_order = new MOrder(Env.getCtx(), 0, null);
			m_order.setAD_Org_ID(p_pos.getAD_Org_ID());
			m_order.setIsSOTrx(true);
			if (p_pos.getC_DocType_ID() != 0)
				m_order.setC_DocTypeTarget_ID(p_pos.getC_DocType_ID());
			else
				m_order.setC_DocTypeTarget_ID(MOrder.DocSubTypeSO_POS);
			MBPartner partner = p_posPanel.f_bpartner.getBPartner();
			if (partner == null || partner.get_ID() == 0)
				partner = p_pos.getBPartner();
			if (partner == null || partner.get_ID() == 0) {
				log.log(Level.SEVERE, "SubCurrentLine.getOrder - no BPartner");

				return null;
			}
			log.info( "SubCurrentLine.getOrder -" + partner);
			m_order.setBPartner(partner);
			p_posPanel.f_bpartner.setC_BPartner_ID(partner.getC_BPartner_ID());
			int id = p_posPanel.f_bpartner.getC_BPartner_Location_ID();
			if (id != 0)
				m_order.setC_BPartner_Location_ID(id);
			//ininoles se cambia usuario del campo usuario del pos por usuario de entorno
			id = p_posPanel.f_bpartner.getAD_User_ID();
			//id = Env.getAD_User_ID(p_ctx);
			if (id != 0)
				m_order.setAD_User_ID(id);
			//
			m_order.setM_PriceList_ID(p_pos.getM_PriceList_ID());
			m_order.setM_Warehouse_ID(p_pos.getM_Warehouse_ID());
			//m_order.setSalesRep_ID(p_pos.getSalesRep_ID());
			m_order.setSalesRep_ID(Env.getAD_User_ID(p_ctx));
			//ininoles seteo de canal en base a usuario logeado
			MUser userCtx = new MUser(p_ctx, Env.getAD_User_ID(p_ctx), null);
			if(userCtx.get_ValueAsInt("C_Channel_ID") > 0)
				m_order.set_CustomColumn("C_Channel_ID", userCtx.get_ValueAsInt("C_Channel_ID"));			
			//m_order.setPaymentRule(MOrder.PAYMENTRULE_Cash);
			m_order.setPaymentRule("D");
			//ininoles se setea canal de venta
			MUser user = new MUser(Env.getCtx(), Env.getAD_User_ID(Env.getCtx()), null);
			if(user.get_ValueAsInt("C_Channel_ID") > 0)
				m_order.set_CustomColumn("C_Channel_ID", user.get_ValueAsInt("C_Channel_ID"));
			//inino;les toma fecha ingresada y no la del PC
			m_order.setDateOrdered(Env.getContextAsDate(Env.getCtx(), "#Date"));
			m_order.setDateAcct(Env.getContextAsDate(Env.getCtx(), "#Date"));			
			//ininoles end
			if (!m_order.save())
			{
				m_order = null;
				log.severe("Unable create Order.");
			}
		}
		return m_order;
	} //	getHeader
	//ininoles nuevo metodo que solo devuelve la orden y si es nulo no crea una nueva
	public MOrder getOrderWhitOutCreate() 
	{
		if (m_order == null)
			return null;
		else
			return m_order; 
	}

	/**
	 * @author Comunidad de Desarrollo OpenXpertya 
	 *         *Basado en Codigo Original Modificado, Revisado y Optimizado de:
	 *         *Copyright ï¿½ ConSerTi
	 */
	public void setBPartner()
	{
		if (m_order != null)
			if (m_order.getDocStatus().equals("DR"))
			{
				MBPartner partner = p_posPanel.f_bpartner.getBPartner();
				//get default from mpos if no selection make
				if (partner == null || partner.get_ID() == 0)
					partner = p_pos.getBPartner();
				if (partner == null || partner.get_ID() == 0) {
					log.warning("SubCurrentLine.getOrder - no BPartner");
				}
				else
				{
					log.info("SubCurrentLine.getOrder -" + partner);
					m_order.setBPartner(partner);
					MOrderLine[] lineas = m_order.getLines();
					for (int i = 0; i < lineas.length; i++)
					{
						lineas[i].setC_BPartner_ID(partner.getC_BPartner_ID());
						lineas[i].setTax();
						lineas[i].save();
					}
					m_order.save();
				}
			}
	}

	/**
	 * Create new Line
	 * 
	 * @return line or null
	 */
	public MOrderLine createLine(MProduct product, BigDecimal QtyOrdered,
			BigDecimal PriceActual) {
		MOrder order = getOrder();
		if (order == null)
			return null;
		if (!order.getDocStatus().equals("DR"))
			return null;

        //create new line
		MOrderLine line = new MOrderLine(order);
		line.setProduct(product);
		line.setQty(QtyOrdered);
			
		line.setPrice(); //	sets List/limit
		line.setPrice(PriceActual);
		//line.save();
		return line;
			
	} //	createLine

	/**
	 * @param m_c_order_id
	 */
	public void setOldOrder(int m_c_order_id) 
	{
		deleteOrder();
		m_order = new MOrder(p_ctx , m_c_order_id, null);
		p_posPanel.updateInfo();
	}
	
	/**
	 * @param m_c_order_id
	 */
	public void setOrder(int m_c_order_id) 
	{
		m_order = new MOrder(p_ctx , m_c_order_id, null);
	}
	
	public void updateOrderLines(MOrder order) 
	{
		if(order != null)
		{
			MOrderLine[] lines = order.getLines(true, null);	
			for (int i = 0; i < lines.length; i++)
			{
				MOrderLine line = lines[i];
				//obtenemos monto nuevo
				if(!line.get_ValueAsBoolean("IsConfirmed"))
				{
					BigDecimal amt = null;
					BigDecimal amtPP = null;
					String sqlAmt = "";
					String sqlAmtPP = "";
					try 
					{	
						if(SubFunctionKeys.ID_LevelPrice == 1)
						{
							sqlAmt = "SELECT PriceStd FROM M_ProductPrice WHERE IsActive = 'Y' " +
							 	" AND M_Product_ID = "+line.getM_Product_ID()+" AND Levels = "+SubFunctionKeys.ID_LevelPrice+
							 	" AND M_PriceList_Version_ID = "+p_posPanel.f_bpartner.getM_PriceList_Version_ID();
							 amt = DB.getSQLValueBD(null, sqlAmt);
							 //precio pat
							 sqlAmtPP = "SELECT PricePat FROM M_ProductPrice WHERE IsActive = 'Y' " +
							 	" AND M_Product_ID = "+line.getM_Product_ID()+" AND Levels = "+SubFunctionKeys.ID_LevelPrice+
							 	" AND M_PriceList_Version_ID = "+p_posPanel.f_bpartner.getM_PriceList_Version_ID();
							 amtPP = DB.getSQLValueBD(null, sqlAmtPP);
						}
						else if(SubFunctionKeys.ID_LevelPrice > 1)
						{
							sqlAmt = "SELECT Price FROM M_ProductPriceRef WHERE IsActive = 'Y' " +
						 		" AND M_Product_ID = "+line.getM_Product_ID()+" AND Levels = "+SubFunctionKeys.ID_LevelPrice+
						 		" AND M_PriceList_Version_ID = "+p_posPanel.f_bpartner.getM_PriceList_Version_ID();
							amt = DB.getSQLValueBD(null, sqlAmt);
							//precio pat
							sqlAmtPP = "SELECT PricePat FROM M_ProductPriceRef WHERE IsActive = 'Y' " +
						 		" AND M_Product_ID = "+line.getM_Product_ID()+" AND Levels = "+SubFunctionKeys.ID_LevelPrice+
						 		" AND M_PriceList_Version_ID = "+p_posPanel.f_bpartner.getM_PriceList_Version_ID();
							amtPP = DB.getSQLValueBD(null, sqlAmtPP);
						}
					}catch (Exception e2) 
					{
						log.config("error"+e2+" SQL: "+sqlAmt);
						amt = Env.ZERO;
					}
					if(amt != null)
						line.setPrice(amt);
					if(amtPP != null)
						line.set_CustomColumn("MonthlyAmount", amtPP);
					if(SubFunctionKeys.ID_LevelPrice > 0)
						line.set_CustomColumn("LevelNo", SubFunctionKeys.ID_LevelPrice);
					line.save();
				}
			}
		}
	} //	update price
	/*public void setMonthlyAmount(BigDecimal amount) 
	{
		if (amount == null)
			amount = Env.ZERO;
		monthlyAmount = amount;
	} //	setPrice*/
	public void setLevelNo(int levelNo) 
	{
		level = levelNo;
	} //	setPrice
	/*public int getM_Warehouse_ID()
	{
		if (f_Warehouse.getSelectedItem() != null)
		{
			KeyNamePair pp = (KeyNamePair)f_Warehouse.getSelectedItem();
			if (pp != null)
				return pp.getKey();
		}
		return 0;
	}*/
	public int getM_Locator_ID()
	{
		if (f_Locator.getSelectedItem() != null)
		{
			KeyNamePair pp = (KeyNamePair)f_Locator.getSelectedItem();
			if (pp != null)
				return pp.getKey();
		}
		return 0;
	}
	/*private void fillWarehouse()
	{
		Vector<KeyNamePair> warehouseVector = new Vector<KeyNamePair>();
		if(f_Warehouse != null)
		{	
			String sqlPList = "SELECT M_Warehouse_ID FROM M_Warehouse WHERE IsActive = 'Y'" +
			" AND AD_Client_ID = "+Env.getAD_Client_ID(Env.getCtx());
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sqlPList, null);							
				rs = pstmt.executeQuery();
				while (rs.next())
				{
					MWarehouse wHouse = new MWarehouse(Env.getCtx(), rs.getInt("M_Warehouse_ID"), null);
					warehouseVector.add(wHouse.getKeyNamePair());
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
		DefaultComboBoxModel warehousehip = new DefaultComboBoxModel(warehouseVector); 
		f_Warehouse.setModel(warehousehip);
		KeyNamePair wareHouseDefault = new KeyNamePair(p_pos.getM_Warehouse_ID(), p_pos.getM_Warehouse().getName());
		f_Warehouse.setValue(wareHouseDefault);
	}	//	*/
	private void fillLocator()
	{
		Vector<KeyNamePair> warehouseVector = new Vector<KeyNamePair>();
		if(f_Locator != null)
		{	
			String sqlPList = "SELECT M_Locator_ID FROM M_Locator WHERE IsActive = 'Y'" +
			" AND M_Warehouse_ID = "+p_pos.getM_Warehouse_ID()+" AND AD_Client_ID = "+Env.getAD_Client_ID(Env.getCtx());
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sqlPList, null);							
				rs = pstmt.executeQuery();
				while (rs.next())
				{
					MLocator wHouse = new MLocator(Env.getCtx(), rs.getInt("M_Locator_ID"), null);
					warehouseVector.add(wHouse.getKeyNamePair());
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
		DefaultComboBoxModel warehousehip = new DefaultComboBoxModel(warehouseVector); 
		f_Locator.setModel(warehousehip);
		/*KeyNamePair wareHouseDefault = new KeyNamePair(p_pos.getM_Warehouse_ID(), p_pos.getM_Warehouse().getName());
		f_Locator.setValue(wareHouseDefault);*/
	}	//
	
} //	PosSubCurrentLine
