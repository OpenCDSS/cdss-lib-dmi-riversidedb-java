

//------------------------------------------------------------------------------
// RiversideDB_ReferenceTable_Abstract_TableModel - Base class for all Reference
// Table editors (RiversideDB_ReferenceTable_*_TableModel.java).

//------------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
//------------------------------------------------------------------------------
// History:
// 2004-01-26	J. Thomas Sapienza, RTi	Initial version.
// 2004-01-29	JTS, RTi		Added column tool tips.
// 2004-02-05	JTS, RTi		Moved from RTi.App.RiverTrakAssitant
//					to RTi.DMI.RiversideDB_DMI.
// 2004-12-13 Luiz Teixeira		Notice: Althought this class was moved
//					   from RTi.App.RiverTrakAssistant ( see
//					   entry above), it was yet referencing
//					RiverTrakAssistant_ReferenceTable_JFrame
//					   in RTi.App.RiverTrakAssistant package
//					Now, because the class
//					RiverTrakAssistant_ReferenceTable_JFrame
//					   was difinitivelly moved from the
//					   RTi.App.RiverTrakAssistant package to
//					   RTi.DMI.RiversideDB_DMI and renamed
//					RiversideDB_ReferenceTable_JFrame
//					   all references to the
//					   RTi.App.
//		   			RiverTrakAssistant_ReferenceTable_JFrame
//					   were replaced by
//					   RTi.DMI.RiversideDB_DMI.
//		   			RiversideDB_ReferenceTable_JFrame
// 2004-12-16 Luiz Teixeira 		Split the original (JTS) class
//					   RiversideDB_ReferenceTable_TableModel
//					into:
//				  RiversideDB_ReferenceTable_Abstract_TableModel
//					and
//				  RiversideDB_ReferenceTable_????????_TableModel
//					where ???????? is the table name:
//						DataDimension
//						DataType
//						DataUnits
//						DataUnits
//						ImportType
//						MeasCreateMethod
//						MeasQualityFlag
//						MeasReductionType
//						MeasTransProtocol
//						MessageLog
//						Scenario
//						SHEFType
//						TableLayout
//						Props
// 2004-12-17 Luiz Teixeira 	Removed all code that become unnecessary after
//				the implementation described above. Renamed
//				variables (more descriptive) and redone some of
//				the documentation.
//------------------------------------------------------------------------------
package RTi.DMI.RiversideDB_DMI;

import java.util.Vector;

import RTi.DMI.DMIUtil;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.String.StringUtil;

/**
Base class for all ReferenceTable editors
  (RiversideDB_ReferenceTable_*_TableModel.java).
*/
public abstract class RiversideDB_ReferenceTable_Abstract_TableModel
	extends JWorksheet_AbstractRowTableModel {

/**
Number of columns in the table model.
*/
protected int _numberOfColumns = 0;

/**
Whether the data can be edited or not.
*/
protected boolean _editable = false;

/**
The dmi used to query out database data.
*/
protected RiversideDB_DMI _rdmi = null;

/**
Constructor.
@param rdmi the RiversideDB_dmi to use.
@param results the results ( rows of data ) to show in the table.
@param type the type of GUI (see RiversideDB_ReferenceTable_JFrame.*) in which
this table model will be opened.
@param editable whether the data is editable or not.
*/
public RiversideDB_ReferenceTable_Abstract_TableModel (
	RiversideDB_DMI rdmi, Vector results, boolean editable )
throws Exception {
	
	// _data is a Vector inherited from JWorksheet_AbstractRowTableModel.
	// It stores the Vector of data that you'll show in the table.
	if ( results == null ) {
		results = new Vector();
	}
	_data     = results;
	
	// Connection to database
	if ( rdmi == null ) {
		throw new Exception ( "Null data dmi passed to " +
		"RiversideDB_ReferenceTable_Abstract_TableModel constructor." );
	}
	_rdmi = rdmi;
	
	// Number of rows in the table.
	_rows     = results.size();
	_editable = editable;

	// Make a backup of the data if editable.
	if ( _editable ) {
		backupData();
	}
}

/**
Stores a backup of each object so that the original values prior to any
editing can be chcked later to find out if anything is different.
Must be implemented in the derived classes.
*/
abstract public void backupData();

/**
Checks if the key field in the table already has a certain value, and if so,
generates a unique value.
@param stringKey the String to check for in the key field.
@param row the row in which the given value is to be placed.
@return a unique String (might be the one passed in) that can validly be
placed in the key field.
Implemented this method in the derived class if needed.
*/
protected String checkTableField ( String stringKey, int row ) {
	return stringKey;
}

/**
Checks if the key field in the table already has a certain value, and if so,
generates a unique value.
@param integerKey the integer to check for in the key field.
@param row the row in which the given value is to be placed.
@return a unique integer (might be the one passed in) that can validly be
placed in the key field.
Implemented this method in the derived class if needed.
*/
protected int checkTableField( int integerKey, int row ) {
	return integerKey;
}

/**
Checks if the table object is dirty.
@param current the current RiversideDB_<TableName> object
@param original the original RiversideDB_<TableName> object
@return true if the object has changed, false otherwise.
*/
abstract protected void checkTableDirty ( Object current, Object original );

/**
Checks to see if two strings (either or both of which may be null) are the same.
@param s1 the first String to compare.
@param s2 the second String to compare.
@return true if they are the same (even if both are null), or false if not.
*/
protected boolean equal( String s1, String s2 )
{
	if (s1 == null && s2 == null) {
		return true;
	}
	if (s1 == null || s2 == null) {
		return false;
	}
	if (s1.trim().equals(s2.trim())) {
		return true;
	}
	return false;
}

/**
Cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
public void finalize() throws Throwable
{
	_rdmi = null;
	_data = null;
	super.finalize();
}

/**
Returns the class of data stored in the specified column.
@param column the column to return the class of.
@return the class of data stored in the specified column.
Must be implemented in the derived classes.
*/
abstract public Class getColumnClass ( int column );

/**
Returns the number of columns of data.
@return the number of columns of data.
*/
public int getColumnCount()
{
	return _numberOfColumns;
}

/**
Returns the name of the specified column.
@param column the column for which to return the name.
@return the name of the specified column.
Must be implemented in the derived classes.
*/
abstract public String getColumnName( int column );

/**
Returns the tool tips for the columns.
@return the tool tips for the columns.
Must be implemented in the derived classes.
*/
abstract public String[] getColumnToolTips();

/**
Returns the widths of all the table columns.
@return the widths of all the table columns.
Must be implemented in the derived classes.
*/
abstract public int[] getColumnWidths();

/**
Returns the visible column number for the field with the specified name, or -1
if the field with the name could not be found.  Field names are compared to the
field names in getColumnNames() with <b>NO</b> case sensitivity, with the
following changes:<br>
- newlines are turned into spaces<br>
- more than one space next to another anywhere in the column name is turned
into one space<br>
- the column name is trimmed<p>
Thus, '&nbsp&nbsp&nbsphi\n&nbsp&nbspthere&nbsp&nbsp&nbsp!&nbsp' will become
'hi there !'
@param fieldName the field name to look for
@return the number of the visible column of the field with the field name, or -1
if it could not be found.
*/
public int getFieldColumnNumber( String fieldName )
{
	String s = fieldName.trim();
	for ( int i = 0; i < _numberOfColumns; i++ ) {
		if ( JWorksheet.convertColumnName(getColumnName(i)).
			equalsIgnoreCase( s ) ) {
			return i;
		}
	}
	return -1;
}

/**
Returns the format of the specified column.
@param column the column to return the format for.
@return the format of the specified column.
Must be implemented in the derived classes.
*/
abstract public String getFormat( int column );

/**
Returns the number of rows of data in the table.
@return the number of rows of data in the table.
*/
public int getRowCount()
{
	return _rows;
}

/**
Returns the value stored at the specified cell ( row, column )
@param row the row from which to return the value.
@param column the column from which to return the value.
@return the value stored at the specified cell ( row, column )
Must be implemented in the derived classes.
*/
abstract protected Object getTableValueAt( int row, int column );

/**
Returns the value stored at the specified cell ( row, column )
@param row the row from which to return the value.
@param column the column from which to return the value.
@return the value stored at the specified cell ( row, column )
*/
public Object getValueAt ( int row, int column )
{
	if ( _sortOrder != null ) {
		row = _sortOrder[ row ];
	}
	return getTableValueAt( row, column );
}

/**
Checks to see if a value is in an array of values.
@param val the value to check for.
@param array the array to check.
@return true if the value is in the array, false if not.
*/
protected boolean inArray( int val, int[] array )
{
	for (int i = 0; i < array.length; i++) {
		if (array[i] == val) {
			return true;
		}
	}
	return false;
}

/**
Returns whether the cell at the given position is editable or not.
@param row the index of the row to check for whether it is editable.
@param column the index of the column to check for whether it is editable.
@return whether the cell at the given position is editable.
This method may be overwritten in the derived classes to use the row
and colunm parameter and determine the editability at the column
(i.e. RiversideDB_ReferenceTable_Scenario_TableModel.java) or cell level.
REVISIT [LT] Rewrite this JavaDoc ASA better explaining the use.
In this table model all columns above #2 are editable.
*/
public boolean isCellEditable( int row, int column )
{
	if ( _editable ) return true;
	return false;
}

/**
Turns an Object passed to setValueAt() into a double.
@param value the Object/Double value.
@return the double representation of the Object.
*/
protected double readDouble( Object value )
{
	if ( value == null ) return DMIUtil.MISSING_FLOAT;

	return ( (Double)value ).doubleValue();
}

/**
Turns an Object passed to setValueAt() into an int.
@param value the Object/Integer value.
@return the int representation of the Object.
*/
protected int readInt( Object value )
{
	if ( value == null ) return DMIUtil.MISSING_INT;

	return ( (Integer)value ).intValue();
}

/**
Turns an Object passed to setValueAt() into a String.
@param value the Object/String value.
@param size the maximum size of the data that can be stored in the field.
Must positive and the returned String is guaranteed to be no longer than this.
@return the String representation of the Object.
*/
protected String readString( Object value, int size )
{
	if ( value == null ) return DMIUtil.MISSING_STRING;

	String s = ( (String)value ).trim();
	if ( s.length() > size ) {
		s = s.substring( 0, size );
	}
	return s;
}

/**
Replaces the spaces in a string with underscores.
@param s the string in which to do replacement.
@return the string with spaces replaced by underscores.
*/
protected String replaceSpaces( String s )
{
	int index = s.indexOf(' ');
	if (index > 0) {
		return StringUtil.replaceString(s, " ", "_");
	}
	return s;
}

/**
Sets the table value at the specified cell ( row, column ).
@param value the value to put in the location.
@param row the row at which to set the value.
@param column the column at which to set the value.
Must be implemented in the derived classes.
*/
abstract protected Object setTableValueAt( Object value, int row, int column );

/**
Sets the table value at the specified cell ( row, column ).
@param value the value to put in the location.
@param row the row at which to set the value.
@param column the column at which to set the value.
*/
public void setValueAt( Object value, int row, int column )
{
	// REVISIT [LT] 2004-12-23 - This block of code is needed according to
	//                          JTS, but was not in any of the TableModel
	//			    classes in the RTAssistant. Discuss with
	//			    him and if really needed add to ALL
	//			    TableModel classes in this package.
	//----------------------------------------------------------------------
	if ( _sortOrder != null ) {
		row = _sortOrder[ row ];
	}
	//----------------------------------------------------------------------
	value = setTableValueAt( value, row, column );
	super.setValueAt       ( value, row, column );
};

/**
Trims a String of everything after and including the first instance of a
specified delimiter.
@param s the String to trim.
@param delimiter the delimiter of characters after and including which will be
trimmed from the String.
*/
protected String trimString( String s, String delimiter )
{
	int index = s.indexOf(delimiter);
	if (index == -1) {
		return s;
	}
	s = s.substring(0, index);
	return s;
}

}
//------------------------------------------------------------------------------