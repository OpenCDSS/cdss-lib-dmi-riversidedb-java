// ----------------------------------------------------------------------------
// RiversideDB_State_TableModel - table model for displaying state data
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
// 2003-10-28	J. Thomas Sapienza, RTi	Initial version.
// 2004-09-29	JTS, RTi		Added support for the new state table
//					design in version 03.00.00.
// 2005-04-11	J. Thomas Sapienza, RTi	Updated to no longer use the 0th
//					column as a row number counter.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import java.util.Vector;

import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;
import RTi.Util.GUI.JWorksheet_TableModelListener;

/**
This table model displays state data.  
*/
public class RiversideDB_State_TableModel
extends JWorksheet_AbstractRowTableModel {

/**
Number of columns in the table model.
*/
private int __COLUMNS = 5;

/**
Reference to the column numbers for a version 02 table.
*/
private final int
	__COL_MODULE = 		0,
	__COL_VARIABLE = 	1,
	__COL_VAL = 		2,
	__COL_SEQ = 		3,
	__COL_STATEGROUPNUM02 = 4;

/**
Reference to the column numbers for a version 03 table.
*/
private final int
	__COL_STATEGROUPNUM03 = 		0,
	__COL_OPERATIONSTATERELATIONNUM = 	1,
	__COL_SEQUENCE = 			2,
	__COL_STATENAME = 			3,
	__COL_VALUESTR = 			4;

/**
Whether the data can be edited or not.
*/
private boolean __editable = false;

/**
Whether the database table is of version 02.XX.XX.
*/
private boolean __isVersion02 = true;

/**
Constructor.  
@param values the values to be displayed in the table model
@param editable whether the data can be edited
@throws Exception if invalid data were passed in.
*/
public RiversideDB_State_TableModel(Vector values, boolean editable)
throws Exception {
	if (values == null) {
		throw new Exception ("Invalid data passed to " 
			+ "RiversideDB_State_TableModel constructor.");
	}
	_data = values;
	_rows = _data.size();

	__editable = editable;
}

/**
Constructor.  
@param values the values to be displayed in the table model
@param editable whether the data can be edited
@throws Exception if invalid data were passed in.
*/
public RiversideDB_State_TableModel(RiversideDB_DMI dmi, Vector values, 
boolean editable)
throws Exception {
	if (values == null) {
		throw new Exception ("Invalid data passed to " 
			+ "RiversideDB_State_TableModel constructor.");
	}

	String[] version = dmi.getDatabaseVersionArray();
	if (version != null && !version[0].equals("02")) {
		__isVersion02 = false;
	}
	_data = values;
	_rows = _data.size();

	__editable = editable;
}

/**
From AbstractTableModel; returns the class of the data stored in a given
column.
@param columnIndex the column for which to return the data class.
*/
public Class getColumnClass (int columnIndex) {
	if (__isVersion02) {
		switch (columnIndex) {
			case __COL_MODULE:		return String.class;
			case __COL_VARIABLE: 		return String.class;
			case __COL_VAL:			return String.class;
			case __COL_SEQ:			return Integer.class;
			case __COL_STATEGROUPNUM02:	return Integer.class;
		}
	}
	else {
		switch (columnIndex) {
			case __COL_STATEGROUPNUM03:	return Integer.class;
			case __COL_OPERATIONSTATERELATIONNUM: 
							return Integer.class;
			case __COL_SEQUENCE:		return Integer.class;
			case __COL_STATENAME:		return String.class;
			case __COL_VALUESTR:		return String.class;
		}
	}
	return String.class;
}

/**
From AbstractTableModel; returns the number of columns of data.
@return the number of columns of data.
*/
public int getColumnCount() {
	return __COLUMNS;
}

/**
From AbstractTableModel; returns the name of the column at the given position.
@param columnIndex the position of the column for which to return the name.
@return the name of the column at the given position.
*/
public String getColumnName(int columnIndex) {
	if (__isVersion02) {
		switch (columnIndex) {
			case __COL_MODULE:		return "MODULE";
			case __COL_VARIABLE: 		return "VARIABLE";
			case __COL_VAL:			return "VALUE";
			case __COL_SEQ:			return "SEQUENCE";
			case __COL_STATEGROUPNUM02:	return "STATEGROUP_NUM";
		}
	}
	else {
		switch (columnIndex) {
			case __COL_STATEGROUPNUM03:	return "STATEGROUP_NUM";
			case __COL_OPERATIONSTATERELATIONNUM: 
					return "OPERATIONSTATERELATION_NUM";
			case __COL_SEQUENCE:		return "SEQUENCE";
			case __COL_STATENAME:		return "STATE_NAME";
			case __COL_VALUESTR:		return "VALUE";
		}
	}
	return " ";
}

/**
Returns the format that the specified column should be displayed in when
the table is being displayed in the given table format. 
@param column column for which to return the format.
@return the format (as used by StringUtil.formatString() in which to display the
column.
*/
public String getFormat(int column) {
	if (__isVersion02) {
		switch (column) {
			case __COL_MODULE:		return "%-40s";
			case __COL_VARIABLE: 		return "%-40s";
			case __COL_VAL:			return "%-40s";
			case __COL_SEQ:			return "%8d";
			case __COL_STATEGROUPNUM02:	return "%8d";
		}
	}
	else {
		switch (column) {
			case __COL_STATEGROUPNUM03:	return "%8d";
			case __COL_OPERATIONSTATERELATIONNUM: return "%8d";
			case __COL_SEQUENCE:		return "%8d";
			case __COL_STATENAME:		return "%-40s";
			case __COL_VALUESTR:		return "%-40s";
		}
	}	
	return "%8s";
}

/**
From AbstractTableModel; returns the number of rows of data in the table.
@return the number of rows of data in the table.
*/
public int getRowCount() {
	return _rows;
}

/**
From AbstractTableModel; returns the data that should be placed in the JTable
at the given row and column.
@param row the row for which to return data.
@param col the column for which to return data.
@return the data that should be placed in the JTable at the given row and col.
*/
public Object getValueAt(int row, int col) {
	if (_sortOrder != null) {
		row = _sortOrder[row];
	}

	RiversideDB_State s = (RiversideDB_State)_data.elementAt(row);

	if (__isVersion02) {
		switch (col) {
			case __COL_MODULE:	return s.getModule();
			case __COL_VARIABLE: return s.getVariable();
			case __COL_VAL:	return s.getVal();
			case __COL_SEQ:	return new Integer(s.getSeq());
			case __COL_STATEGROUPNUM02:	
				return new Integer((int)s.getStateGroup_num());
		}
	}
	else {
		switch (col) {
			case __COL_STATEGROUPNUM03:	
				return new Integer((int)s.getStateGroup_num());
			case __COL_OPERATIONSTATERELATIONNUM: 
				return new Integer(
					s.getOperationStateRelation_num());
			case __COL_SEQUENCE:		
				return new Integer(s.getSequence());
			case __COL_STATENAME:
				return s.getStateName();
			case __COL_VALUESTR:		
				return s.getValueStr();
		}
	}
	
	return " ";
}

/**
Returns an array containing the widths (in number of characters) that the 
fields in the table should be sized to.
@return an integer array containing the widths for each field.
*/
public int[] getColumnWidths() {
	int[] widths = new int[__COLUMNS];
	for (int i = 0; i < __COLUMNS; i++) {
		widths[i] = 0;
	}
	int i = 0;
	widths[i++] = 20;	// 
	widths[i++] = 20;	// 
	widths[i++] = 20;	// 
	widths[i++] = 20;	// 
	widths[i++] = 20;	// 

	return widths;
}

/**
Returns whether the cell at the given position is editable or not.  
@param rowIndex unused
@param columnIndex the index of the column to check for whether it is editable.
@return whether the cell at the given position is editable.
*/
public boolean isCellEditable(int rowIndex, int columnIndex) {
	if (__isVersion02 && columnIndex == __COL_VAL && __editable) {
		return true;
	}
	else if (columnIndex == __COL_VALUESTR && __editable) {
		return true;
	}
	return false;
}

/**
Sets the value at the specified position to the specified value.
@param value the value to set the cell to.
@param row the row of the cell for which to set the value.
@param col the col of the cell for which to set the value.
*/
public void setValueAt(Object value, int row, int col) {
	RiversideDB_State s = (RiversideDB_State)_data.elementAt(row);
	String val;
	int i;
	if (__isVersion02) {
		switch (col) {
			case __COL_MODULE:	
				val = (String)value;			
				if (!s.getModule().equals(val)) {
					s.setModule(val);
					valueChanged(row, col, val);
				}
				break;
			case __COL_VARIABLE: 
				val = (String)value;
				if (!s.getVariable().equals(val)) {
					s.setVariable(val);
					valueChanged(row, col, val);
				}
				break;
			case __COL_VAL:	
				val = (String)value;
				if (!s.getVal().equals(val)) {
					s.setVal(val);
					valueChanged(row, col, val);
				}
				break;
			case __COL_SEQ:	
				i = ((Integer)value).intValue();
				if (s.getSeq() != i) {
					s.setSeq(i);
					valueChanged(row, col, value);
				}
				break;
			case __COL_STATEGROUPNUM02:	
				i = ((Integer)value).intValue();
				if (s.getStateGroup_num() != i) {
					s.setStateGroup_num(i);
					valueChanged(row, col, value);
				}
				break;
		}
	}
	else {
		switch (col) {
			case __COL_STATEGROUPNUM03:	
				i = ((Integer)value).intValue();
				if (s.getStateGroup_num() != i) {
					s.setStateGroup_num(i);
					valueChanged(row, col, value);
				}
				break;
			case __COL_OPERATIONSTATERELATIONNUM: 
				i = ((Integer)value).intValue();
				if (s.getOperationStateRelation_num() != i) {
					s.setOperationStateRelation_num(i);
					valueChanged(row, col, value);
				}
				break;			
			case __COL_SEQUENCE:		
				i = ((Integer)value).intValue();
				if (s.getSequence() != i) {
					s.setSequence(i);
					valueChanged(row, col, value);
				}
				break;			
			case __COL_STATENAME:
				val = (String)value;
				if (!s.getValueStr().equals(val)) {
					s.setStateName(val);
					valueChanged(row, col, val);
				}
				break;
			case __COL_VALUESTR:		
				val = (String)value;
				if (!s.getValueStr().equals(val)) {
					s.setValueStr(val);
					valueChanged(row, col, val);
				}
				break;
		}

	}
	super.setValueAt(value, row, col);	
}

/**
This is necessary for the RiverTrakCalibrator, as it lets the GUI in which
this Table model is used know whenever a value is changed.
*/
public void valueChanged(int row, int col, Object value) {
	int size = _listeners.size();
	JWorksheet_TableModelListener tml = null;
	for (int i = 0; i < size; i++) {
		tml = (JWorksheet_TableModelListener)_listeners.elementAt(i);
		tml.tableModelValueChanged(row, col, value);
	}
}

}
