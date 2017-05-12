package org.copesa.model;

import java.math.BigDecimal;
import org.compiere.model.MOrder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import org.compiere.util.DB;



public class COPESAOrderOps {
	
	public static BigDecimal getMonthlyPrice(MOrder _order) throws Exception 
	{
		int orderid = _order.getC_Order_ID();
		if (orderid <= 0)
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
		if (orderid <= 0)
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
		if (orderid <= 0)
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
	
	
	public static void SetDatesForPAT(MOrder _order)
	{
		int orderid = _order.getC_Order_ID();
		if (orderid <= 0)
			return;

		String sql = "UPDATE C_OrderLine set datepromised3 = to_date('01-01-3022', 'DD-MM-YYYY'), isactive = 'Y' WHERE C_Order_ID = " + orderid + " AND isfree = 'N'";
		DB.executeUpdate(sql, _order.get_TrxName());
	}
	
}
