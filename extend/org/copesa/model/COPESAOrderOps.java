package org.copesa.model;

import java.math.BigDecimal;
import org.compiere.model.MOrder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;

import org.compiere.util.DB;
import org.compiere.util.Env;
import org.copesa.utils.DateUtils;
import org.compiere.model.MOrderLine;


public class COPESAOrderOps {
	
	public static BigDecimal getMonthlyPrice(MOrder _order) throws Exception 
	{
		int orderid = _order.getC_Order_ID();
		if (orderid <= 0 || _order.getDocStatus().compareTo("DR") == 0)
			return null;
	    String sql = "SELECT Round(total, 2) FROM   co_factcalendar_header fac " +
	                 "JOIN c_order co on co.c_order_id =  fac.c_order_id " +
                     "WHERE  co.c_order_id = ? " +
                     "  AND fac.ini <= Greatest(Now(), co.datepromised + '2 days'::interval) " +
                     "  AND fac.fin > Greatest(Now(), co.datepromised + '2 days'::interval)";
	    
	    PreparedStatement pstmt = DB.prepareStatement(sql, _order.get_TrxName());
	    pstmt.setInt(1, orderid);
	    BigDecimal amount = null;
	    ResultSet rs = pstmt.executeQuery();
	    if (rs.next() )
	    	amount = rs.getBigDecimal(1);
	    rs.close();
	    pstmt.close();
	    return amount;
	    
	}

	public static BigDecimal getCopayment(MOrder _order) throws Exception 
	{
		int orderid = _order.getC_Order_ID();
		if (orderid <= 0 || _order.getDocStatus().compareTo("DR") == 0 || _order.getDatePromised().compareTo(_order.getDateOrdered()) == 0)
			return null;
		
	    String sql = "SELECT Round(total, 2) " +
                     "FROM   co_factcalendar_header " +
                     "WHERE  c_order_id = ? AND periodo = 0";
	    
	    PreparedStatement pstmt = DB.prepareStatement(sql, _order.get_TrxName());
	    pstmt.setInt(1, orderid);
	    
	    ResultSet rs = pstmt.executeQuery();
	    BigDecimal amount = null;
	    if (rs.next() )
	    	amount = rs.getBigDecimal(1);
	    rs.close();
	    pstmt.close();
	    return amount;
	    
	}

	public static Timestamp getDateFirstInvoice(MOrder _order) throws Exception 
	{
		int orderid = _order.getC_Order_ID();
		if (orderid <= 0 || _order.getDocStatus().compareTo("DR") == 0 || _order.getDatePromised().compareTo(_order.getDateOrdered()) == 0)
			return null;

		String sql = "SELECT Min(fin) FROM co_factcalendar_header WHERE c_order_id = ?";
	    
	    PreparedStatement pstmt = DB.prepareStatement(sql, _order.get_TrxName());
	    pstmt.setInt(1, orderid);
	    
	    ResultSet rs = pstmt.executeQuery();
	    Timestamp dateFirstInvoice = null;
	    if (rs.next() )
	       dateFirstInvoice = rs.getTimestamp(1);
	    rs.close();
	    pstmt.close();
	    return dateFirstInvoice;
	    
	}
	
	public static void UpdatePrices(MOrder _order) throws Exception
	{
		int orderid = _order.getC_Order_ID();
		if (orderid <= 0)
			return;
		
		BigDecimal monthPrice = COPESAOrderOps.getMonthlyPrice(_order);
		BigDecimal copayment = COPESAOrderOps.getCopayment(_order);
		Timestamp dateFirstInvoice = COPESAOrderOps.getDateFirstInvoice(_order);

		String sql = "UPDATE C_Order set pricepat = ?, copayment = ?,  DateFirstInvoice = ? WHERE c_order_id = ?";
	    
	    PreparedStatement pstmt = DB.prepareStatement(sql, _order.get_TrxName());
	    pstmt.setBigDecimal(1, monthPrice);
	    pstmt.setBigDecimal(2, copayment);
	    pstmt.setTimestamp(3, dateFirstInvoice);
	    pstmt.setInt(4, orderid );
	    
	    pstmt.execute();
	    pstmt.close();
	    
	}
	
	public static void AddFreightLines(int _orderid, int _userid, String _trxName) throws Exception
	{
		if (_orderid <= 0)
			return;
		
		if (_userid <= 0)
			return;
		
		String sql = "SELECT COPESA_UpdateFreight( ?, ?)";
	    
	    PreparedStatement pstmt = DB.prepareStatement(sql, _trxName);
	    pstmt.setInt(1, _orderid);
	    pstmt.setInt(2, _userid);
	    
	    pstmt.execute();
	    pstmt.close();
	    
	}

	
	public static void SetMovDates(int _orderid, String _trxName) throws Exception
	{
		if (_orderid <= 0)
			return;
		
		String sql = "SELECT COPESA_setmovdates( ? )";
	    
	    PreparedStatement pstmt = DB.prepareStatement(sql, _trxName);
	    pstmt.setInt(1, _orderid);
	    pstmt.execute();
	    pstmt.close();
	    
	}
	/*
	public static void SetLinesDates(int _orderid, String _trxName) throws Exception
	{
		if (_orderid <= 0)
			return;
		
		String sql = "select copesa_setlinesdates(?)";
	    
	    PreparedStatement pstmt = DB.prepareStatement(sql, _trxName);
	    pstmt.setInt(1, _orderid);
	    pstmt.execute();
	    pstmt.close();
	    
	}
	
	*/
	public static void setOrderLinesDates(int _orderid, String _trxName) throws Exception
	{
		
		if (_orderid <= 0 )
			return;

		String sql = "select copesa_setorderlinesdates(?)";
	    
	    PreparedStatement pstmt = DB.prepareStatement(sql, _trxName);
	    pstmt.setInt(1, _orderid);
	    pstmt.execute();
	    pstmt.close();
	}

	public static void SetDatesForNoPAT(MOrder _order)
	{
		int orderid = _order.getC_Order_ID();
		if (orderid <= 0)
			return;

		String sql = "UPDATE C_OrderLine set datepromised3 = to_date('01-01-3022', 'DD-MM-YYYY'), isactive = 'Y' WHERE C_Order_ID = " + orderid + " AND isfree = 'N'";
		DB.executeUpdate(sql, _order.get_TrxName());
	}

	
	
	public static int getLineGeozone(MOrderLine _orderline) throws Exception 
	{
		int geozoneid = -1;
		
		int orderlineid = _orderline.getC_OrderLine_ID();
		if (orderlineid <= 0 )
			return -1;
		
		int mprodid = _orderline.getM_Product_ID();
		if (mprodid <= 0)
			mprodid = _orderline.get_ValueAsInt("M_ProductRef_ID");
		
		if( mprodid <= 0 )
			return -1;
		
		
		int locid = _orderline.getC_BPartner_Location_ID();
		if (locid <= 0)
			return -1;
		
		String sql = "select case when cat.description ~ 'NOEDITORIAL' then 'G' ELSE 'E' end " +
		             "from m_product mp " +
		             "join m_product_category cat on (cat.m_product_category_id = mp.m_product_category_id )" +
			         "where mp.m_product_id = ? ";

		PreparedStatement pstmt = DB.prepareStatement(sql, _orderline.get_TrxName());
	    pstmt.setInt(1, mprodid);
	    ResultSet rs = pstmt.executeQuery();
	    String cat = "E";
	    if (rs.next() )
	    	cat = rs.getString(1);
	    rs.close();
	    pstmt.close();
			  
		
	    sql = "select MAX(gzc.C_Geozone_ID) " +
	          "FROM C_GeozoneCities gzc " + 
	          "JOIN C_Geozone gz ON (gzc.C_Geozone_ID = gz.C_Geozone_ID) " + 
	          "JOIN C_BPartner_Location cbp on cbp.c_city_id = gzc.c_city_id " +  
	          "where cbp.c_bpartner_location_id = ? " +
	          "  and gzc.IsActive = 'Y' " +
	          "  and gz.type = ?";
	    
	    pstmt = DB.prepareStatement(sql, _orderline.get_TrxName());
	    pstmt.setInt(1, locid);
	    pstmt.setString(2, cat);
	    rs = pstmt.executeQuery();
	    if (rs.next() )
	    	geozoneid = rs.getInt(1);
	    rs.close();
	    pstmt.close();
	    return geozoneid;
	    
	}
	
    public static void createFreeLine(MOrder _order, MOrderLine _line, int _freeDays, BigDecimal _newAmt, BigDecimal _newAmtPAT, int _duration) throws Exception
    {
			MOrderLine oLineNew = new MOrderLine(_order);
			oLineNew.setAD_Org_ID(_line.getAD_Org_ID());
			oLineNew.setC_BPartner_Location_ID(_line.getC_BPartner_Location_ID());
			oLineNew.set_CustomColumn("C_BPartnerRef_ID", _line.get_ValueAsInt("C_BPartnerRef_ID"));
			oLineNew.setM_Product_ID(_line.getM_Product_ID());
			oLineNew.setQty(_line.getQtyEntered());
			oLineNew.set_CustomColumn("C_CalendarCOPESA_ID", _line.get_ValueAsInt("C_CalendarCOPESA_ID"));
			//ininoles seteamos nuevo monto
			//oLineNew.setPrice(Env.ONE);
			oLineNew.setPrice(_newAmt);
			oLineNew.set_CustomColumn("C_OrderLineRef_ID", _line.get_ID());
			oLineNew.set_CustomColumn("IsFree", true);
			//oLineNew.set_CustomColumn("DatePromised2", order.getDateOrdered());
			oLineNew.set_CustomColumn("DatePromised2", _order.getDatePromised());
			oLineNew.set_CustomColumn("M_Locator_ID", _line.get_ValueAsInt("M_Locator_ID"));
			
			//se suman dias a fecha fin
			//ininoles nueva validacion y cambios para fecha fin
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(_order.getDateOrdered().getTime());
			if(_freeDays > 0)
			{
				calendar.add(Calendar.DATE, _freeDays);
				Timestamp datEnd = new Timestamp(calendar.getTimeInMillis());					
				oLineNew.set_CustomColumn("DatePromised3", datEnd);
			}else if(_freeDays == 0){									
				oLineNew.set_CustomColumn("DatePromised3", _order.getDateOrdered());
			}						
			//campo gracia
			if(_newAmtPAT != null && _newAmtPAT.compareTo(Env.ZERO) > 0)
			{
				oLineNew.set_CustomColumn("MonthlyAmount",_newAmtPAT);
			}
			int geozoneid = _line.get_ValueAsInt("C_Geozone_ID");
			if (geozoneid <= 0)
			    geozoneid = COPESAOrderOps.getLineGeozone(_line);
			if (geozoneid > 0)
			{	
			    oLineNew.set_CustomColumn("C_Geozone_ID", geozoneid);
			    _line.set_CustomColumn("C_Geozone_ID", geozoneid);
			}    
			oLineNew.save();
			//actualizamos fecha de inicio de linea base
			calendar.add(Calendar.DATE, 1);
			_line.set_CustomColumn("DatePromised2",new Timestamp(calendar.getTimeInMillis()));
			
			Timestamp dateEnd = null;
			if (_order.getPaymentRule().compareTo("D") == 0 )
				dateEnd = DateUtils.veryDistantDate();
			else
			{
				if (_duration == 0 )
					_duration = 1;
				
				if (_duration > 0 )
				{	
					calendar.add(Calendar.DATE, _duration - 1);
					dateEnd = new Timestamp(calendar.getTimeInMillis());
				}
			}
			_line.set_CustomColumn("DatePromised3", dateEnd);		

			_line.set_CustomColumn("C_OrderLineRef_ID", oLineNew.get_ID());												
			_line.save();																						

    }

	
}
