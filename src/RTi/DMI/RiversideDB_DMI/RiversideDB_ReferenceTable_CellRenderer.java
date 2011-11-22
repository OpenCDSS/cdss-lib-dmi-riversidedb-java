// -----------------------------------------------------------------------------
// RiversideDB_ReferenceTable_CellRenderer - class to render cells for 
//	reference tables.
// -----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// -----------------------------------------------------------------------------
// History:
// 2004-01-26	J. Thomas Sapienza, RTi	Initial version.
// 2004-02-05	JTS, RTi		Moved from RTi.App.RiverTrakAssitant
//					to RTi.DMI.RiversideDB_DMI.
// 2004-12-16 Luiz Teixeira, RTi 	Replaced all references to the (JTS) 
//					RiversideDB_ReferenceTable_TableModel
//					to references to the new (base/derived)
//				RiversideDB_ReferenceTable_Abstract_TableModel.
// -----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.Util.GUI.JWorksheet_AbstractExcelCellRenderer;

/**
This class is used to render cells for reference tables.
*/
public class RiversideDB_ReferenceTable_CellRenderer
extends JWorksheet_AbstractExcelCellRenderer {

/**
Table model for which this class renders the cell.
*/
private RiversideDB_ReferenceTable_Abstract_TableModel __tableModel;

/**
Constructor.  Private so that it cannot be called.
*/
@SuppressWarnings("unused")
private RiversideDB_ReferenceTable_CellRenderer() {}

/**
Constructor.  
@param model the model for which this class will render cells
*/
public RiversideDB_ReferenceTable_CellRenderer(
	RiversideDB_ReferenceTable_Abstract_TableModel model)
{
	__tableModel = model;
}

/**
Returns the format for a given column.
@param column the colum for which to return the format.
@return the format (as used by StringUtil.format) for a column.
*/
public String getFormat(int column) {
	return __tableModel.getFormat(column);
}

/**
Returns the widths of the columns in the table.
@return an integer array of the widths of the columns in the table.
*/
public int[] getColumnWidths() {
	return __tableModel.getColumnWidths();
}

}
