package org.compiere.FA;

import java.sql.*;
//import java.util.Properties;
import java.util.Calendar;
import java.util.logging.Level;

import org.compiere.model.MAssetAcct;
import org.compiere.model.MAssetChange;	
import org.compiere.model.MDepreciationWorkfile;
import org.compiere.model.X_A_Asset_Forecast;
import org.compiere.model.X_A_Asset;
import org.compiere.process.*;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;


import java.math.BigDecimal;
public class CreateAssetForecast extends SvrProcess{

	int PGroup_ID = 0;
	String PPosession = "Y";
	int asset_ID = 0;
	int Org_ID=0;
	protected void prepare()
	{
		ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++)
        {
            String name = para[i].getParameterName();
            if (para[i].getParameter() == null)
                ;
            else if (name.equals("A_Asset_Group_ID"))
            	PGroup_ID = para[i].getParameterAsInt();
            else if (name.equals("IsInPosession"))
            	PPosession = (String)para[i].getParameter();
            else if (name.equals("A_Asset_ID"))
            	asset_ID = para[i].getParameterAsInt();
            else if (name.equals("AD_Org_ID"))
            	Org_ID = para[i].getParameterAsInt();
            else
                log.log(Level.SEVERE, "Unknown Parameter: " + name);
        }
	}	//	prepare
	
	protected String doIt() throws java.lang.Exception
	{
		String sql="Select A.A_Asset_ID, C.A_Asset_Acct_ID "
			+" From A_Asset A "
			+" Inner join A_Asset_Acct C on (A.A_Asset_ID=C.A_Asset_ID)"
			+" where 1=1";
			
		    if(asset_ID>0)
		    	sql+=" and A.A_Asset_ID=?";
		    else
		    	sql+=" and A.A_Asset_Group_ID=?";
		    
			sql+=" and A.IsInPosession=?";
			
			if(Org_ID>0)
				sql+=" and A.AD_Org_ID=?";
		
		int count=0;
		PreparedStatement pstmt = null;
		try
		{
			pstmt = DB.prepareStatement(sql, get_TrxName());
			if(asset_ID>0)
				 pstmt.setInt(1, asset_ID);
			 else
				 pstmt.setInt(1, PGroup_ID);
			pstmt.setString(2, PPosession);
			if(Org_ID>0)
				pstmt.setInt(3, Org_ID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				//DB.executeUpdate("Delete from A_Asset_Forecast where Processed='N' and A_Asset_ID="+rs.getInt(1), get_TrxName());
				X_A_Asset asset= new X_A_Asset(getCtx(), rs.getInt(1), get_TrxName());
				MDepreciationWorkfile workfile = getWorkFile(rs.getInt(1));
				MAssetAcct acct= new MAssetAcct(getCtx(), rs.getInt(2), get_TrxName());
				if(workfile==null || workfile.getA_Period_Posted()==acct.getA_Period_End())
					continue;
				
				replanningForecast(asset, workfile.getA_Asset_Cost().subtract(workfile.getA_Accumulated_Depr()),acct, 
						workfile.getA_Period_Posted(),
						DB.getSQLValueTS(get_TrxName(), "select max(datedoc) from a_asset_forecast where corrected='Y' and A_Asset_ID="+rs.getInt(1)));
				count++;
			}
			rs.close();
			pstmt.close();
			pstmt = null;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, sql, e);
		}
		
		return "Activo Replanificados :"+count;
	}
	
	public static void createForecast(X_A_Asset asset,MAssetChange change,MAssetAcct  acct, String trxName)
	{
		//BigDecimal SalvageAmt =acct.getA_Salvage_Value().divide(Env.ONEHUNDRED);				 
		//SalvageAmt=change.getAssetValueAmt().multiply(SalvageAmt);
		//ininoles se cambia que campo A_Salvage_Value no sea porcentaje sino monto
		BigDecimal SalvageAmt =acct.getA_Salvage_Value();
		BigDecimal Currentamt=change.getAssetValueAmt();
		//Currentamt=Currentamt.subtract(Env.ONE);ininoles
		
		if(!acct.get_ValueAsBoolean("IsByYear")) //faaguilar por mes
			for(int i=1;i<(acct.getA_Period_End()+1) ;i++){
				X_A_Asset_Forecast fore=new X_A_Asset_Forecast (Env.getCtx(),0,trxName);
				fore.setA_Asset_ID(asset.getA_Asset_ID());
				fore.setAD_Org_ID(asset.getAD_Org_ID());
				Calendar cal=Calendar.getInstance();
				cal.setTimeInMillis(asset.getAssetServiceDate().getTime());
				cal.add(Calendar.MONTH, i);
				fore.setDateDoc(new Timestamp(cal.getTimeInMillis()));
				fore.setPeriodNo(i);
				//fore.setAmount(Currentamt.divide(SalvageAmt.intValue()<=0?new BigDecimal(acct.getA_Period_End()):new BigDecimal(acct.getA_Period_End()-1),2, BigDecimal.ROUND_DOWN));
				BigDecimal amtAcct = Currentamt.divide(new BigDecimal(acct.getA_Period_End()),2, BigDecimal.ROUND_DOWN);
				if(i == acct.getA_Period_End())
				{
					amtAcct = amtAcct.subtract(SalvageAmt);				
				}
				fore.setAmount(amtAcct.setScale(2, BigDecimal.ROUND_DOWN));			
				fore.getAmount();
				fore.save();
			}
		else//faaguilar por año
			for(int i=0;i<(asset.getUseLifeYears()) ;i++){
				X_A_Asset_Forecast fore=new X_A_Asset_Forecast (Env.getCtx(),0,trxName);
				fore.setA_Asset_ID(asset.getA_Asset_ID());
				fore.setAD_Org_ID(asset.getAD_Org_ID());
				Calendar cal=Calendar.getInstance();
				cal.setTimeInMillis(asset.getAssetServiceDate().getTime());
				cal.set(Calendar.MONTH, Calendar.DECEMBER);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.add(Calendar.YEAR, i);
				fore.setDateDoc(new Timestamp(cal.getTimeInMillis()));
				fore.setPeriodNo(i+1);
				acct.save();
				//fore.setAmount(Currentamt.divide(SalvageAmt.intValue()<=0?new BigDecimal(acct.getA_Period_End()):new BigDecimal(acct.getA_Period_End()-1),2, BigDecimal.ROUND_DOWN));
				BigDecimal divisor = new BigDecimal("0.0");
				if (acct.get_ValueAsBoolean("IsByYear"))
				{
					divisor = new BigDecimal(asset.getUseLifeYears());
				}else
				{
					divisor = new BigDecimal(acct.getA_Period_End());
				}
				
				BigDecimal amtAcct = Currentamt.divide(divisor,2, BigDecimal.ROUND_DOWN);
				if(i+1 == acct.getA_Period_End())
				{
					amtAcct = amtAcct.subtract(SalvageAmt);				
				}
				fore.setAmount(amtAcct.setScale(2, BigDecimal.ROUND_DOWN));			
				fore.getAmount();
				fore.save();
			}
			
		//ininoles las planificaciones se generan siempre en el ciclo 
		/*if(SalvageAmt.intValue()>0){
		X_A_Asset_Forecast forelast=new X_A_Asset_Forecast (Env.getCtx(),0,trxName);
		forelast.setA_Asset_ID(asset.getA_Asset_ID());
		forelast.setAD_Org_ID(asset.getAD_Org_ID());
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(asset.getAssetServiceDate().getTime());
		cal.add(Calendar.MONTH, acct.getA_Period_End());
		forelast.setDateDoc(new Timestamp(cal.getTimeInMillis()));
		forelast.setPeriodNo(acct.getA_Period_End());
		forelast.setAmount(SalvageAmt);
		forelast.save();
		}*/
	}
	//replanning forecast 
	public void replanningForecast(X_A_Asset asset,BigDecimal Amount,MAssetAcct  acct, int Period, Timestamp lastdate)
	{
		
		//ininoles modificaciones para nueva forma de calculo de planificacion
		//BigDecimal SalvageAmt =acct.getA_Salvage_Value().divide(Env.ONEHUNDRED);
		//SalvageAmt=Amount.multiply(SalvageAmt);
		BigDecimal SalvageAmt =acct.getA_Salvage_Value();
		BigDecimal Currentamt=Amount;
		//Currentamt=Currentamt.subtract(Env.ONE);
		//if(SalvageAmt.intValue()<=0)
		Currentamt=Currentamt.divide(new BigDecimal(acct.getA_Period_End()-Period),2, BigDecimal.ROUND_DOWN);
		//else
			//Currentamt=Currentamt.divide(Period==(acct.getA_Period_End()-1)? Env.ONE : new BigDecimal(acct.getA_Period_End()-1-Period),2, BigDecimal.ROUND_DOWN);
		
		DB.executeUpdate("Update A_Asset_Forecast set amount="+ Currentamt +" Where Processed='N' AND A_Asset_ID="+asset.getA_Asset_ID(),get_TrxName() );
		if(asset.isInPosession()){
			DB.executeUpdate("Delete from A_Asset_Forecast where Processed='N' and Corrected='N' and A_Asset_ID="+asset.getA_Asset_ID(), get_TrxName());
			Period=DB.getSQLValue(get_TrxName(), "select max(PeriodNo) from a_asset_forecast where corrected='Y' and A_Asset_ID="+asset.getA_Asset_ID());
		}
		else
			DB.executeUpdate("Delete from A_Asset_Forecast where Processed='N' and A_Asset_ID="+asset.getA_Asset_ID(), get_TrxName());	
		
		int mes=1;
		if(!acct.get_ValueAsBoolean("IsByYear")) //faaguilar por mes
			for(int i=Period+1;i<(acct.getA_Period_End()+1) ;i++){
				X_A_Asset_Forecast fore=new X_A_Asset_Forecast (Env.getCtx(),0,null);
				fore.setA_Asset_ID(asset.getA_Asset_ID());
				fore.setAD_Org_ID(asset.getAD_Org_ID());
				Calendar cal=Calendar.getInstance();
				if(lastdate==null)
					cal.setTimeInMillis(asset.getAssetServiceDate().getTime());
				else
					cal.setTimeInMillis(lastdate.getTime());
				cal.add(Calendar.MONTH, mes);
				fore.setDateDoc(new Timestamp(cal.getTimeInMillis()));
				fore.setPeriodNo(i);
				//fore.setAmount(Currentamt);
				BigDecimal amtAcct = Currentamt;
				if(i == acct.getA_Period_End())
				{
					amtAcct = amtAcct.subtract(SalvageAmt);				
				}
				fore.setAmount(amtAcct.setScale(2, BigDecimal.ROUND_DOWN));		
				fore.getAmount(); 
				fore.save();
				mes++;
			}
		else// faaguilar by year
			for(int i=Period+1;i<(asset.getUseLifeYears()+1) ;i++){
				X_A_Asset_Forecast fore=new X_A_Asset_Forecast (Env.getCtx(),0,null);
				fore.setA_Asset_ID(asset.getA_Asset_ID());
				fore.setAD_Org_ID(asset.getAD_Org_ID());
				Calendar cal=Calendar.getInstance();
				if(lastdate==null)
					cal.setTimeInMillis(asset.getAssetServiceDate().getTime());
				else
					cal.setTimeInMillis(lastdate.getTime());
				cal.add(Calendar.YEAR, mes);
				cal.set(Calendar.MONTH, Calendar.DECEMBER);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				fore.setDateDoc(new Timestamp(cal.getTimeInMillis()));
				fore.setPeriodNo(i);
				//fore.setAmount(Currentamt);
				BigDecimal amtAcct = Currentamt;
				if(i == acct.getA_Period_End())
				{
					amtAcct = amtAcct.subtract(SalvageAmt);				
				}
				fore.setAmount(amtAcct.setScale(2, BigDecimal.ROUND_DOWN));		
				fore.getAmount(); 
				fore.save();
				mes++;
			}
		/*if(SalvageAmt.intValue()>0){
		X_A_Asset_Forecast forelast=new X_A_Asset_Forecast (Env.getCtx(),0,null);
		forelast.setA_Asset_ID(asset.getA_Asset_ID());
		forelast.setAD_Org_ID(asset.getAD_Org_ID());
		Calendar cal=Calendar.getInstance();
		cal.setTimeInMillis(asset.getAssetServiceDate().getTime());
		cal.add(Calendar.MONTH, acct.getA_Period_End());
		forelast.setDateDoc(new Timestamp(cal.getTimeInMillis()));
		forelast.setPeriodNo(acct.getA_Period_End());
		forelast.setAmount(SalvageAmt);
		forelast.save();
		}*/
	}
	public static MDepreciationWorkfile getWorkFile(int Asset_ID)
	{
		MDepreciationWorkfile workfile=null;
		CLogger	log = CLogger.getCLogger("MAsset");
		String sql = "SELECT * FROM A_Depreciation_Workfile WHERE A_Asset_ID=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, null);
			pstmt.setInt(1, Asset_ID);
			rs = pstmt.executeQuery();
			if (rs.next())
				workfile = new MDepreciationWorkfile(Env.getCtx(), rs, null);
		}
		catch (Exception e)
		{
			
			log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		//
		return workfile;
	}	//	getWorkFile
}
