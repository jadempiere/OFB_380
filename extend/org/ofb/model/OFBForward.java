package org.ofb.model;

import org.compiere.util.DB;

public class OFBForward {
	
	
	public static boolean OrderUNAB()
	{
		String OrderUNAB = "N";
		try 
		{
			OrderUNAB = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_UNAB_ValidatorOrder'");
			if(OrderUNAB == null)	
				OrderUNAB = "N";
		}
		catch (Exception e)
		{
			OrderUNAB = "N";
		}
		return OrderUNAB.equals("Y");
	}	
	public static boolean GenerateXMLMinOut()
	{
		String GenerateXML = "N";
		try 
		{
			GenerateXML = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_GenerateXMLMinOut'");
			if(GenerateXML == null)	
				GenerateXML = "N";
		}
		catch (Exception e)
		{
			GenerateXML = "N";
		}
		return GenerateXML.equals("Y");
	}
	public static boolean GenerateXMLMinOutFel()
	{
		String GenerateXML = "N";
		try 
		{
			GenerateXML = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_GenerateXMLMinOutFEL'");
			if(GenerateXML == null)	
				GenerateXML = "N";
		}
		catch (Exception e)
		{
			GenerateXML = "N";
		}
		return GenerateXML.equals("Y");
	}
	public static boolean GenerateXMLMinOutCGProvectis()
	{
		String GenerateXML = "N";
		try 
		{
			GenerateXML = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_GenerateXMLMinOutCGProvectis'");
			if(GenerateXML == null)	
				GenerateXML = "N";
		}
		catch (Exception e)
		{
			GenerateXML = "N";
		}
		return GenerateXML.equals("Y");
	}
	public static String RutEmpresaFEL()
	{
		String rutEmp = "1";
		try 
		{
			rutEmp = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_RutEmpresaFEL'");
			if(rutEmp == null)	
				rutEmp = "1";
		}
		catch (Exception e)
		{
			rutEmp = "1";
		}
		return rutEmp;
	}
	public static String RutEmpresaFELOrg(int ID_Org)
	{
		String rutEmp = "0";
		try 
		{
			rutEmp = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_RutEmpresaFEL' AND AD_Org_ID = "+ID_Org);
			if(rutEmp == null)	
				rutEmp = "0";
		}
		catch (Exception e)
		{
			rutEmp = "0";
		}
		return rutEmp;
	}
	public static String RutUsuarioFEL()
	{
		String rutEmp = "1";
		try 
		{
			rutEmp = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_RutUsuarioFEL'");
			if(rutEmp == null)	
				rutEmp = "1";
		}
		catch (Exception e)
		{
			rutEmp = "1";
		}
		return rutEmp;
	}
	public static String RutUsuarioFELOrg(int ID_Org)
	{
		String rutEmp = "0";
		try 
		{
			rutEmp = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_RutUsuarioFEL' AND AD_Org_ID = "+ID_Org);
			if(rutEmp == null)	
				rutEmp = "0";
		}
		catch (Exception e)
		{
			rutEmp = "0";
		}
		return rutEmp;
	}
	public static String ContrasenaFEL()
	{
		String rutEmp = "1";
		try 
		{
			rutEmp = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_ContraseñaFEL'");
			if(rutEmp == null)	
				rutEmp = "1";
		}
		catch (Exception e)
		{
			rutEmp = "1";
		}
		return rutEmp;
	}
	public static String ContrasenaFELOrg(int ID_Org)
	{
		String rutEmp = "0";
		try 
		{
			rutEmp = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_ContraseñaFEL' AND AD_Org_ID = "+ID_Org);
			if(rutEmp == null)	
				rutEmp = "0";
		}
		catch (Exception e)
		{
			rutEmp = "0";
		}
		return rutEmp;
	}
	public static boolean ValidatorRequisitionTSM()
	{
		String flag = "N";
		try 
		{
			flag = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_TSM_ValidatorRequisition'");
			if(flag == null)	
				flag = "N";
		}
		catch (Exception e)
		{
			flag = "N";
		}
		return flag.equals("Y");
	}	
	public static String PathBatIMacro()
	{
		String ruta = "";
		try 
		{
			ruta = DB.getSQLValueString(null, "Select MAX(Value) from AD_SysConfig where name='OFB_RutaBatImacro' ");
			if(ruta == null)	
				ruta = "";
		}
		catch (Exception e)
		{
			ruta = "";
		}
		return ruta;
	}
	public static String PathDataIMacro()
	{
		String ruta = "";
		try 
		{
			ruta = DB.getSQLValueString(null, "Select MAX(Value) from AD_SysConfig where name='OFB_RutaDataImacro' ");
			if(ruta == null)	
				ruta = "";
		}
		catch (Exception e)
		{
			ruta = "";
		}
		return ruta;
	}
	public static String PathDeleteDataIMacro()
	{
		String ruta = "";
		try 
		{
			ruta = DB.getSQLValueString(null, "Select MAX(Value) from AD_SysConfig where name='OFB_RutaDeleteDataIMacro' ");
			if(ruta == null)	
				ruta = "";
		}
		catch (Exception e)
		{
			ruta = "";
		}
		return ruta;
	}
	public static boolean NoExplodeBOMOrder()
	{
		String ExplodeBOM = "N";
		try 
		{
			ExplodeBOM = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NoExplodeBOM_Order'");
			if(ExplodeBOM == null)	
				ExplodeBOM = "N";
		}
		catch (Exception e)
		{
			ExplodeBOM = "N";
		}
		return ExplodeBOM.equals("Y");
	}
	public static boolean NoValidationLineOrderRep()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NoValidationLineOrder'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean NoValidationPriceOrderLineZero()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NoValidationPriceOrderLineZero'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean NoValidationPriceInvoiceLineZero()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NoValidationPriceInvoiceLineZero'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean NewSQLBtnHistory()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NewSQLBtnHistory'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}	
	public static boolean NewSQLBtnHistoryProduct()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NewSQLBtnHistoryProduct'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}	
	public static boolean DateSalesDocumentFromOrderPA()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_DateSalesDocumentFromOrder'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}	
	public static boolean NewDescriptionInvoiceGenPA()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NewDescriptionInvoiceGenPA'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean NewUpdateMantainceDetailTSM()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NewUpdateMantainceDetailTSM'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean NewUpdateMaintainceParent()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NewUpdateMaintainceParent'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean NoCopyLocationLineOrder()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NoCopyLocationLineOrder'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static String PassEncriptCOPESA1()
	{
		String rutEmp = "";
		try 
		{
			rutEmp = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_PassEncriptCOPESA1'");
			if(rutEmp == null)	
				rutEmp = "";
		}
		catch (Exception e)
		{
			rutEmp = "";
		}
		return rutEmp;
	}
	public static String PassEncriptCOPESA2()
	{
		String rutEmp = "";
		try 
		{
			rutEmp = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_PassEncriptCOPESA2'");
			if(rutEmp == null)	
				rutEmp = "";
		}
		catch (Exception e)
		{
			rutEmp = "";
		}
		return rutEmp;
	}
	public static String Pauta_PathCOPESA()
	{
		String rutEmp = "";
		try 
		{
			rutEmp = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_Pauta_PathCOPESA'");
			if(rutEmp == null)	
				rutEmp = "";
		}
		catch (Exception e)
		{
			rutEmp = "";
		}
		return rutEmp;
	}
	public static String Pauta_PrefixCOPESA()
	{
		String rutEmp = "";
		try 
		{
			rutEmp = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_Pauta_PrefixCOPESA'");
			if(rutEmp == null)	
				rutEmp = "";
		}
		catch (Exception e)
		{
			rutEmp = "";
		}
		return rutEmp;
	}
	public static String NOPauta_PathCOPESA()
	{
		String rutEmp = "";
		try 
		{
			rutEmp = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NOPauta_PathCOPESA'");
			if(rutEmp == null)	
				rutEmp = "";
		}
		catch (Exception e)
		{
			rutEmp = "";
		}
		return rutEmp;
	}
	public static String NOPauta_PrefixCOPESA()
	{
		String rutEmp = "";
		try 
		{
			rutEmp = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NOPauta_PrefixCOPESA'");
			if(rutEmp == null)	
				rutEmp = "";
		}
		catch (Exception e)
		{
			rutEmp = "";
		}
		return rutEmp;
	}
	public static String TableNameWinFind()
	{
		String rutEmp = "";
		try 
		{
			rutEmp = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_TableNameWinFind'");
			if(rutEmp == null)	
				rutEmp = "";
		}
		catch (Exception e)
		{
			rutEmp = "";
		}
		return rutEmp;
	}
	public static boolean NoUseDesactiveOrderLines()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_NoUseDesactiveOrderLines'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
	public static boolean UseOLLocatorToReserved()
	{
		String validLine = "N";
		try 
		{
			validLine = DB.getSQLValueString(null, "Select Value from AD_SysConfig where name='OFB_UseOLLocatorToReserved'");
			if(validLine == null)	
				validLine = "N";
		}
		catch (Exception e)
		{
			validLine = "N";
		}
		return validLine.equals("Y");
	}
}
