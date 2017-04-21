/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2007 ComPiere, Inc. All Rights Reserved.                *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.util.KeyNamePair;

/** Generated Interface for GL_BudgetControlLine
 *  @author Adempiere (generated) 
 *  @version Release 3.8.0
 */
public interface I_GL_BudgetControlLine 
{

    /** TableName=GL_BudgetControlLine */
    public static final String Table_Name = "GL_BudgetControlLine";

    /** AD_Table_ID=2000019 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name Amount */
    public static final String COLUMNNAME_Amount = "Amount";

	/** Set Amount.
	  * Amount in a defined currency
	  */
	public void setAmount (BigDecimal Amount);

	/** Get Amount.
	  * Amount in a defined currency
	  */
	public BigDecimal getAmount();

    /** Column name Amount2 */
    public static final String COLUMNNAME_Amount2 = "Amount2";

	/** Set Amount2	  */
	public void setAmount2 (BigDecimal Amount2);

	/** Get Amount2	  */
	public BigDecimal getAmount2();

    /** Column name Amount3 */
    public static final String COLUMNNAME_Amount3 = "Amount3";

	/** Set Amount3	  */
	public void setAmount3 (BigDecimal Amount3);

	/** Get Amount3	  */
	public BigDecimal getAmount3();

    /** Column name C_Period_ID */
    public static final String COLUMNNAME_C_Period_ID = "C_Period_ID";

	/** Set Period.
	  * Period of the Calendar
	  */
	public void setC_Period_ID (int C_Period_ID);

	/** Get Period.
	  * Period of the Calendar
	  */
	public int getC_Period_ID();

	public org.compiere.model.I_C_Period getC_Period() throws RuntimeException;

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name C_ValidCombination_ID */
    public static final String COLUMNNAME_C_ValidCombination_ID = "C_ValidCombination_ID";

	/** Set Combination.
	  * Valid Account Combination
	  */
	public void setC_ValidCombination_ID (int C_ValidCombination_ID);

	/** Get Combination.
	  * Valid Account Combination
	  */
	public int getC_ValidCombination_ID();

	public org.compiere.model.I_C_ValidCombination getC_ValidCombination() throws RuntimeException;

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name GL_BudgetControl_ID */
    public static final String COLUMNNAME_GL_BudgetControl_ID = "GL_BudgetControl_ID";

	/** Set Budget Control.
	  * Budget Control
	  */
	public void setGL_BudgetControl_ID (int GL_BudgetControl_ID);

	/** Get Budget Control.
	  * Budget Control
	  */
	public int getGL_BudgetControl_ID();

	public org.compiere.model.I_GL_BudgetControl getGL_BudgetControl() throws RuntimeException;

    /** Column name GL_BudgetControlLine_ID */
    public static final String COLUMNNAME_GL_BudgetControlLine_ID = "GL_BudgetControlLine_ID";

	/** Set GL_BudgetControlLine ID	  */
	public void setGL_BudgetControlLine_ID (int GL_BudgetControlLine_ID);

	/** Get GL_BudgetControlLine ID	  */
	public int getGL_BudgetControlLine_ID();

    /** Column name GL_BudgetDetail_ID */
    public static final String COLUMNNAME_GL_BudgetDetail_ID = "GL_BudgetDetail_ID";

	/** Set GL_BudgetDetail ID	  */
	public void setGL_BudgetDetail_ID (int GL_BudgetDetail_ID);

	/** Get GL_BudgetDetail ID	  */
	public int getGL_BudgetDetail_ID();

	public I_GL_BudgetDetail getGL_BudgetDetail() throws RuntimeException;

    /** Column name Help */
    public static final String COLUMNNAME_Help = "Help";

	/** Set Comment/Help.
	  * Comment or Hint
	  */
	public void setHelp (String Help);

	/** Get Comment/Help.
	  * Comment or Hint
	  */
	public String getHelp();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name IsRequired */
    public static final String COLUMNNAME_IsRequired = "IsRequired";

	/** Set IsRequired	  */
	public void setIsRequired (boolean IsRequired);

	/** Get IsRequired	  */
	public boolean isRequired();

    /** Column name Mode */
    public static final String COLUMNNAME_Mode = "Mode";

	/** Set Mode	  */
	public void setMode (String Mode);

	/** Get Mode	  */
	public String getMode();

    /** Column name M_Product_ID */
    public static final String COLUMNNAME_M_Product_ID = "M_Product_ID";

	/** Set Product.
	  * Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID);

	/** Get Product.
	  * Product, Service, Item
	  */
	public int getM_Product_ID();

	public org.compiere.model.I_M_Product getM_Product() throws RuntimeException;

    /** Column name Processed */
    public static final String COLUMNNAME_Processed = "Processed";

	/** Set Processed.
	  * The document has been processed
	  */
	public void setProcessed (boolean Processed);

	/** Get Processed.
	  * The document has been processed
	  */
	public boolean isProcessed();

    /** Column name Program */
    public static final String COLUMNNAME_Program = "Program";

	/** Set Program	  */
	public void setProgram (String Program);

	/** Get Program	  */
	public String getProgram();

    /** Column name Qty */
    public static final String COLUMNNAME_Qty = "Qty";

	/** Set Quantity.
	  * Quantity
	  */
	public void setQty (BigDecimal Qty);

	/** Get Quantity.
	  * Quantity
	  */
	public BigDecimal getQty();

    /** Column name SQLAmountDiff */
    public static final String COLUMNNAME_SQLAmountDiff = "SQLAmountDiff";

	/** Set SQLAmountDiff	  */
	public void setSQLAmountDiff (BigDecimal SQLAmountDiff);

	/** Get SQLAmountDiff	  */
	public BigDecimal getSQLAmountDiff();

    /** Column name SQLAmountDiff2 */
    public static final String COLUMNNAME_SQLAmountDiff2 = "SQLAmountDiff2";

	/** Set SQLAmountDiff2	  */
	public void setSQLAmountDiff2 (BigDecimal SQLAmountDiff2);

	/** Get SQLAmountDiff2	  */
	public BigDecimal getSQLAmountDiff2();

    /** Column name SQLProductNameOrg */
    public static final String COLUMNNAME_SQLProductNameOrg = "SQLProductNameOrg";

	/** Set SQLProductNameOrg	  */
	public void setSQLProductNameOrg (String SQLProductNameOrg);

	/** Get SQLProductNameOrg	  */
	public String getSQLProductNameOrg();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();
}
