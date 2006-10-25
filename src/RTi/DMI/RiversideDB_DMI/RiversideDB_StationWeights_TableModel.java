// ----------------------------------------------------------------------------
// RiversideDB_StationWeights_TableModel - 
// Table model for displaying TSID and Station Weight information
// used for Reductions. Data from tables: MeasType and MeasReducRelation
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
// 2003-09-09	A. Morgan Love, RTi		Initial version based on
//						JTS's ERDiagram_TableModel.java
// 2005-01-05 Luiz Teixeira, RTi		Added the __dmi member used when
//						needed for versioning. 
// 2005-04-11	J. Thomas Sapienza, RTi	Updated to no longer use the 0th
//					column as a row number counter.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMIUtil;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.Message.Message;

import java.util.Date;
import java.util.Vector;

import RTi.TS.TS;

import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.String.StringUtil;

import RTi.Util.Time.DateTime;
import RTi.Util.Time.TimeInterval;

/**
This class is a table model for displaying data for station weights.
*/
public class RiversideDB_StationWeights_TableModel 
extends JWorksheet_AbstractRowTableModel {

/**
The number of columns in the table model. 

This should be changed to represent the number of columns you will have in
your tables.  Here's something, though:
- the columns count should be +1 the actual number of columns your table will
have, because the 0th column (the left-most one) actually contains the
row number in it. You can turn this column off if you want, but you still
need to provide room for it in the table model
*/
private int __columns = 2;

/**
The dmi used to query out database data.
*/
protected RiversideDB_DMI _rdmi = null;

/**
Constructor.  This builds the Model for displaying the TSID and weight 
information.
@param rdmi the RiversideDB_dmi to use.
@param data Vector of arrays (of Strings), containing data from MeasType and 
MeasReducRelation tables. 
<PRE> <TABLE>
<TR><TH>array index</TH><TH>data value</TH></TR>
<TR><TD>0</TD><TD>tsident</TD></TR>
<TR><TD>1</TD><TD>weight</TD></TR>
</TABLE> </PRE>
@throws Exception if an invalid data or dmi was passed in.
*/
public RiversideDB_StationWeights_TableModel(
	RiversideDB_DMI rdmi, Vector data )
throws Exception {
	
	// _data is a Vector inherited from JWorksheet_AbstractRowTableModel.
	// It stores the Vector of data that you'll show in the table.
	if (data == null) {
		throw new Exception ("Null data Vector passed to " 
			+ "RiversideDB_StationWeights_TableModel constructor.");
	}
	_data = data;
	
	// Connection to database
	if (rdmi == null) {
		throw new Exception ("Null data dmi passed to " 
			+ "RiversideDB_StationWeights_TableModel constructor.");
	}
	_rdmi = rdmi;	

	//  _rows is an integer that contains the number of rows of
	// data to be displayed in the table.  Make sure to set the 
	// value 
	_rows = _data.size();

}

/**
Returns the class of the data stored in a given column.  This method is 
inherited from the very base-most java TableModel class and is required.
@param columnIndex the column for which to return the data class.
*/
public Class getColumnClass (int columnIndex) {
	switch (columnIndex) {
		case  0: return String.class;	// tsident
		case  1: return String.class;	// weight
		default: return String.class;	// 
	}
}

/**
Returns the number of columns of data.  This method is inherited from
the very base-most Java TableModel class and is required.
@return the number of columns of data.
*/
public int getColumnCount() {
	return __columns;
}

/**
Returns the name of the column at the given position.  This method is inherited
from the very base-most Java TableModel class and is required.
@return the name of the column at the given position.
*/
public String getColumnName(int columnIndex) {
	switch (columnIndex) {
		case  0:	return "TSIdent";
		case  1:	return "Weight";
		default:	return "";
	}
}

/**
Returns the format that the specified column should be displayed in when
the table is being displayed in the given table format.  This method is
required for JWorksheet table models.
@param column column for which to return the format.
@return the format (as used by StringUtil.formatString() in which to display the
column.
*/
public String getFormat(int column) {
	switch (column) {
		case  0:	return "%-15s";	// tsident 
		case  1:	return "%-5s"; // weight
		default:	return "%-8s"; 
	}
}

/**
Returns the number of rows of data in the table.  This method is inherited from
the very base-most Java TableModel class and is required.
@return the number of rows of data in the table.
*/
public int getRowCount() {
	return _rows;
}

/**
Returns the data that should be placed in the JTable at the given row and 
column.  This method is inherited from the very base-most Java TableModel
class and is required.
@param row the row for which to return data.
@param col the column for which to return data.
@return the data that should be placed in the JTable at the given row and col.
*/
public Object getValueAt(int row, int col) {
	/////////////////////////
	//  
	// since the JWorksheets support sorting column values (just right-click
	// on a column header and you get the sort menu), we gotta make sure
	// that the row numbers never sort.  That's all this first section
	// does, and you can pretty much copy and paste this part directly

	//  _sortOrder is an inherited int array that maps sorted row 
	// numbers
	// to regular row numbers (or elements within the _data Vector).  
	// If it's null, then that means the table currently isn't sorted.
	if (_sortOrder != null) {
		row = _sortOrder[row];
	}
	
	//  this is where the most important part of the table model is.
	// It gets the object out of the array and returns the right values
	// for each column.  You can do a lot in here, I've done some really
	// complicated stuff for some tables.  But for the most part,
	// something simple like below will work
	Vector v= null;
	String [] arr = (String[])_data.elementAt(row);
	switch (col) {
		case  0:	return arr[0];

		case  1:	return arr[1];

		default:	return "";
	}

}

/**
Returns an array containing the widths (in number of characters) that the 
fields in the table should be sized to.
@return an integer array containing the widths for each field.
*/
public int[] getColumnWidths() {
	int[] widths = new int[__columns];
	int i = 0;
	widths[i++] = 18;	//1
	widths[i++] = 5;	
	return widths;
}

/**
Returns whether the cell at the given position is editable
or not.  In this table model column 2  (weight) is editable. 
@param rowIndex unused 
@param columnIndex the index of the column to check for whether it is editable.
@return whether the cell at the given position is editable.
*/ 
public boolean isCellEditable(int rowIndex, int columnIndex) {
	if ( columnIndex ==  1) {
 		return true;
	}
	return false;
}

/**
Sets the value at the specified position to the specified
value. 
@param value the value to set the cell to. 
@param row the row of the cell for which to set the value. 
@param col the col of the cell for which to set the value. 
*/ 
public void setValueAt(Object value, int row, int col) {
	String s;
	double dval;
	int ival;
	switch (col) {
 		case  1: 
			s = new String( (String) value );
			//set Precision
			//StringUtil.formatString(s, "%3.2f");
			if ( s.startsWith(".") ) {
				s = "0" + s;
			}
			//else if ( s.startsWith("-." ) ) {}
			String arr[] = (String[]) _data.elementAt(row);
			arr[1] = s;
			_data.setElementAt(arr, row);
			break;
	}
	super.setValueAt(value, row, col);
}

}
