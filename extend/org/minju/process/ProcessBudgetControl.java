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
package org.minju.process;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.X_GL_BudgetControl;
import org.compiere.process.SvrProcess;
import org.compiere.util.DB;
import org.compiere.util.Env;

/**
 *	
 *	
 *  @author Italo Niñoles ininoles 
 *  @version $Id: ProcessSalesOrder.java $
 */
//public class ProcessBudgetControl extends SvrProcess
public class ProcessBudgetControl extends SvrProcess
{
	//private String			p_DocStatus = null;
	private int				p_budgetC_ID = 0; 
	//private String				p_Action = "PR";
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	 protected void prepare()
	{
		/*ProcessInfoParameter[] para = getParameter();
		for (int i = 0; i < para.length; i++)
		{
			String name = para[i].getParameterName();
			
			if (name.equals("Action"))
				p_Action = para[i].getParameterAsString();
			else
				log.log(Level.SEVERE, "Unknown Parameter: " + name);
		}*/
		 p_budgetC_ID=getRecord_ID();
	}	//	prepare

	/**
	 *  Perrform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception
	{
		if (p_budgetC_ID > 0)
		{
			X_GL_BudgetControl budget = new X_GL_BudgetControl(getCtx(), p_budgetC_ID, get_TrxName());
			//seteo de nuevo estado
			String newStatus = "DR";
			String newAction = "DR";				
			if(budget.get_ValueAsString("DocStatus").compareTo("DR") == 0)
			{
				newStatus = "E1";
				newAction = "E1";
			}
			else if(budget.get_ValueAsString("DocStatus").compareTo("E1") == 0)
			{
				newStatus = "E2";
				newAction = "E2";
			}
			else if(budget.get_ValueAsString("DocStatus").compareTo("E2") == 0)
			{
				newStatus = "CO";
				newAction = "CO";
			}			
			else if(budget.get_ValueAsString("DocStatus").compareTo("CO") == 0)
			{
				newStatus = "E2";
				newAction = "VO";
			}
			//validacion de rol
			int cant = DB.getSQLValue(get_TrxName(), "SELECT COUNT(1) FROM AD_Document_Action_Access daa" +
					" INNER JOIN AD_Ref_List rl ON (daa.AD_Ref_List_ID = rl.AD_Ref_List_ID) " +
					" WHERE value = '"+newAction+"' AND AD_Role_ID = "+Env.getAD_Role_ID(getCtx())+
					" AND C_DocType_ID = "+budget.get_ValueAsInt("C_DocType_ID"));
			//procesar
			if(cant > 0)
			{
				if(newAction.compareTo("CO") != 0 && newAction.compareTo("VO") != 0)
				{
					budget.set_CustomColumn("DocStatus", newStatus);
					budget.save();
				}
				else if(newAction.compareTo("CO") == 0)
				{
					budget.set_CustomColumn("DocStatus", newStatus);
					budget.set_CustomColumn("Processed", true);
					DB.executeUpdate("UPDATE GL_BudgetControlLine SET Processed = 'Y' " +
							" WHERE GL_BudgetControl_ID = "+budget.get_ID(),get_TrxName());
					budget.save();
				}else if(newAction.compareTo("VO") == 0)
				{					
					budget.set_CustomColumn("Processed", false);
					budget.set_CustomColumn("DocStatus", newStatus);
					DB.executeUpdate("UPDATE GL_BudgetControlLine SET Processed = 'N' " +
							" WHERE GL_BudgetControl_ID = "+budget.get_ID(),get_TrxName());
					budget.save();
				}				
			}
			else
				throw new AdempiereException("Error: Permisos de rol insuficientes");
		}
	   return "Procesado";
	}	//	doIt
}
