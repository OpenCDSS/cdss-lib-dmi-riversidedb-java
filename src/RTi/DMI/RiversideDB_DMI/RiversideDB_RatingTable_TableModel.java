// ----------------------------------------------------------------------------
// RiversideDB_RatingTable_TableModel - 
// Table model for displaying RiversideDB_RatingTable_Table information.
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
// 2003-09-09	A. Morgan Love, RTi		Initial version based on
//						JTS's ERDiagram_TableModel.java 
// 2005-01-05 Luiz Teixeira, RTi		Added the __dmi member used when
//						needed for versioning. 
// 2005-04-11	J. Thomas Sapienza, RTi		Updated to no longer use the 0th
//						column as a row number counter.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.Util.GUI.JWorksheet;

import java.util.Vector;

import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;

/**
This class is a table model for displaying data from RiversideDB_RatingTable_Table objects.
*/
public class RiversideDB_RatingTable_TableModel 
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
private int __columns = 3;

/**
Reference to table worksheet this tablemodel is for.
*/
private JWorksheet __worksheet;

/**
The dmi used to query out database data.
*/
protected RiversideDB_DMI __rdmi = null;

/**
Constructor.  This builds the Model for displaying the given 
RiversideDB_RatingTable_Table objects' information.
@param dmi the dmi to use.
@param data Vector of RiversideDB_RatingTable_Table objects for 
which to display data.
@throws Exception if an invalid data or dmi was passed in.
*/
public RiversideDB_RatingTable_TableModel( RiversideDB_DMI rdmi,
					   Vector data ) 
throws Exception {
	
	// _data is a Vector inherited from JWorksheet_AbstractRowTableModel.
	// It stores the Vector of data that you'll show in the table.
	if ( data == null ) {
		throw new Exception ("Null data Vector passed to " 
			+ "RiversideDB_RatingTable_TableModel constructor.");
	}
	_data = data;
	
	// Connection to database
	if ( rdmi == null ) {
		throw new Exception ("Null data Vector passed to " 
			+ "RiversideDB_RatingTable_TableModel constructor.");
	}
	__rdmi = rdmi;	

	//  _rows is an integer that contains the number of rows of
	// data to be displayed in the table.  Make sure to set the 
	// value 
	_rows = _data.size();
}

/**
Cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
public void finalize() throws Throwable
{
	__rdmi = null;
	super.finalize();
}

/**
Returns the class of the data stored in a given column.  This method is 
inherited from the very base-most java TableModel class and is required.
@param columnIndex the column for which to return the data class.

 this method is used so that the class that renders table cells knows
what kind of data type it is dealing with.  what we use it for is in the
cell renderer base class, so that we know if a cell should be right-justified
(numbers) or left-justified (strings and dates). 

Also, make sure the class type returned from this method matches the kind of
object you return in the call to getValueAt(), otherwise an exception will 
get thrown.
*/
public Class getColumnClass (int columnIndex) {
	switch (columnIndex) {
		case  0: return Double.class;	// val1
		case  1: return Double.class;	// val2
		case  2: return Double.class;	// shift
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

 steve decided that the 0th column (the one that holds the row number)
shouldn't have a column name, so that's what I've been doing on other tables.
But you can put "ROW #" or whatever you want in there.

If you ever try to change a table name on the fly (say you start displaying a 
table, then a user changes a setting and you want to change a column name
from one value to another), it's not as easy as simply returning a new value
in this method.  I know I've dealt with it before, and it's a pain in the butt. 
It's easier to just rebuild the table entirely ... but you only need to worry
about this if you ever need to do that.
*/
public String getColumnName(int columnIndex) {
	switch (columnIndex) {
		case  0:	return "Stage";
		case  1:	return "Discharge";
		case  2:	return "Shift";
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


you'll see that in all these methods that use a switch() statement to switch
between columns that I have a 'default:' tag.  Really, strictly-speaking,
this isn't completely necessary, since if you set the __columns variable 
correctly up top then the switch() will never be out of bounds ... but it's
just a good idea ... 
*/
public String getFormat(int column) {
	switch (column) {
		case  0:	return "%8.3f";	// val1
		case  1:	return "%8.3f"; // val2
		case  2:	return "%8.3f"; //shift
		default:	return "%1s";
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
	RiversideDB_RatingTable table = 
	(RiversideDB_RatingTable ) _data.elementAt(row);

	double num;
	switch (col) {
		case  0:	num = table.getValue1();
				return new Double( num );

		case  1:	num = table.getValue2();
				return new Double( num );

		case  2:	num = table.getShift1();
				return new Double( num );

		default:	return "";
	}
}

/**
Returns an array containing the widths (in number of characters) that the 
fields in the table should be sized to.
@return an integer array containing the widths for each field.


the widths are specified in terms of the number of characters that should
fit across in the field.  Here's how the table actually determines the real
size ... (in the following, W is the width you specify in the array)

1) it gets the font setting for the table header and writes out W "X" characters
to a string.  So a W of 6 would make "XXXXXX" ... it then sees how many pixels
in the header font it would take to fit this string.

2) it gets the font setting for the cell fonts and writes out W "X" characters
and sees how many pixels would be required to fit this string.

3) whichever one is LARGER, it pads it out with a few more pixels (about 10, I
think) and that's the width of the column in pixels
*/
public int[] getColumnWidths() {
	int[] widths = new int[__columns];
	int i = 0;
	widths[i++] = 5;
	widths[i++] = 5;	
	widths[i++] = 5;	
	return widths;
}
/**
Returns whether the cell at the given position is editable or not.  
@param rowIndex unused
@param columnIndex the index of the column to check for whether it is editable.
@return whether the cell at the given position is editable.
*/
public boolean isCellEditable(int rowIndex, int columnIndex) {
	//all columns are editable
	return true;
}

/**
Sets the value at the specified position to the specified
value. 
@param value the value to set the cell to. 
@param row the row of the cell for which to set the value. 
@param col the col of the cell for which to set the value. 
*/ 
public void setValueAt(Object value, int row, int col) {

	if ( __worksheet != null ) {	
		__worksheet.setDirty( true );
	}

	double dval;
	RiversideDB_RatingTable table = null;
	switch (col) {
 		case  0: 
			dval = ((Double)value).doubleValue();
			//update object
			table = (RiversideDB_RatingTable) 
			_data.elementAt( row );
			table.setValue1( dval);
			break;
 		case  1: dval = ((Double)value).doubleValue();
			//update object
			table = 
			(RiversideDB_RatingTable) _data.elementAt( row );
			table.setValue2( dval);
			break;
 		case  2: dval = ((Double)value).doubleValue();
			//update object
			table = 
			(RiversideDB_RatingTable) _data.elementAt( row );
			table.setShift1( dval);
			break;
	}
	super.setValueAt( value, row, col);
}

/**
Reference to the worksheet in which this table model
manages the data. 
*/ 
public void setWorksheet(JWorksheet worksheet) {
	__worksheet = worksheet;
}


}
