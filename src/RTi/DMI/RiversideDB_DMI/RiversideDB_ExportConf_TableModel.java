// ----------------------------------------------------------------------------
// RiversideDB_ExportConf_TableModel - 
// Table model for displaying RiversideDB_ExportConf and
// RiversideDB_MeasType (for the TSID) information.
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
// 2003-09-09	A. Morgan Love, RTi		Initial version based on
//						JTS's ERDiagram_TableModel.java 
// 2005-04-11	J. Thomas Sapienza, RTi		Updated to no longer use the 0th
//						column as a row number counter.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.Util.GUI.JWorksheet;

import java.util.List;
import java.util.Vector;

import RTi.TS.TSIdent;

import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;

import RTi.Util.IO.DataUnits;
import RTi.Util.IO.IOUtil;
import RTi.Util.Message.Message;

//import RTi.DMI.RiversideDB_DataType;

/**
This class is a table model for displaying data from RiversideDB_ExportConf and MeasType objects.
*/
public class RiversideDB_ExportConf_TableModel 
extends JWorksheet_AbstractRowTableModel {

/**
The number of columns in the table model. 
*/
private int __columns = 5;

/**
Reference to table worksheet this tablemodel is for.
*/
private JWorksheet __worksheet;

/**
List of MeasTypes ordered corresponding to the
order of the ExportConf objects passed in.
*/
List __measType_vect = null;

/**
Array of Strings to use for the labels of the worksheet pertaining to
the ExportConf objects (all columns except the first 2 which are from
the MeasType object).
*/
String[] __arrLabels = null;

/**
Array of Strings to use for the tooltips of the worksheet columns
*/
String[] __arrToolTips = null;

//Vector containing values for field - this is a JComboBox
//(all the choices for field will be the same for the entire column)
List _field_vect = null;

//Connection to database to query 
RiversideDB_DMI __rdmi;

//To get JComboBox for units (which will contain different items 
//depending on the data type of the selected product), need to
//query the vector of DataTypes
List __dataType_vect = null;

//ROWs
private final static int __tsid = 0;
private final static int __ts_desc = 1;
private final static int __ts_active = 2;
private final static int __exp_id = 3;
private final static int __exp_units = 4;

/**
Constructor.  This builds the Model for displaying the given 
RiversideDB_ExportConf objects' information.
@param v List of Vectors.  First Vector is Vector of 
RiversideDB_ExportConf objects for which to display data.  
The ExportConf object itself knows about the related 
RiversideDB_MeasType object. The second Vector is
a Vector of RiversideDB_MeasTypes objects ordered
in the same order as the ExportConf objects passed in.
@param arrLabels Array of labels to use for the worksheet column names for
the ExportConf objects. Labels in the array are for the following
ExportConf object fields:
[0] Export ID
[1] Export Units
[2] IsActive
@param dmi Connection to open DMI.
@param dataType_vect List of DataType objects used to create unit choices 
in units JComboBox
@throws Exception if an invalid data or dmi was passed in.
*/
public RiversideDB_ExportConf_TableModel(List v, 
String [] arrLabels, String [] arrToolTips, 
RiversideDB_DMI dmi, List dataType_vect ) throws Exception {
	if (v.size() != 2 ) {
		throw new Exception ("Need to pass in a Vector of size " +
		"2 containing, 1) a Vector of ExportConf objects and " +
		"2), a Vector of MeasType objects" );
	}
	// ( _data is a Vector inherited from
	// JWorksheet_AbstractRowTableModel.  It stores the Vector of data that you'll show in the table. )
	_data = (List)v.get(0);
	__measType_vect = (List)v.get(1);

	if (_data == null) {
		throw new Exception ("Null data Vector passed to " 
		+ "RiversideDB_ExportConf_TableModel constructor.");
	}	
	if ( _data.size() != __measType_vect.size() ) {
		throw new Exception ("Vector of Riverside_MeasTypes must " +
		"be same size as Vecotr of RiversideDB_ExportConf objects.");
	}
	__arrLabels = arrLabels;
	__arrToolTips = arrToolTips;

	//  _rows is an integer that contains the number of rows of
	// data to be displayed in the table.  Make sure to set the 
	// value 
	_rows = _data.size();

	//connection to database
	__rdmi = dmi;

	//vector of RiversideDB_DataType objects 
	__dataType_vect = dataType_vect;
}//end constructor

/**
Adds a MeasType object to the Vector of MeasTypes.  Must be
called before "addRow"
*/
public void addMeasType( RiversideDB_MeasType mt ) {
	if ( __measType_vect == null ) {
		__measType_vect = new Vector();
		__measType_vect.add(mt);
	}
	else {
		__measType_vect.add(mt);
	}
}

/**
Removes the measType a TSIdent String matching the one
passed in from the Vector of MeasTypes: __measType_vect.
*/
public void deleteMeasType ( String tsid_str ) {
	int num_mt = __measType_vect.size();
	RiversideDB_MeasType mt = null;
	String s = null;
	for ( int i=0; i< num_mt; i++ ) {
		mt =(RiversideDB_MeasType) __measType_vect.get(i);
		if ( mt == null ) {
			continue;
		}
		try {
			s = (mt.toTSIdent()).toString();
		}
		catch ( Exception e ) {
			Message.printWarning( 2, 
			"RiversideDB_ExportConf_TableModel." +
			"deleteMeasType", e );
			s = "";
		}
		if ( s.equals( tsid_str ) ) {
			__measType_vect.remove(i);
		}
	}
}

/**
Returns the class of the data stored in a given column.  This method is 
inherited from the very base-most java TableModel class and is required.
@param columnIndex the column for which to return the data class.
*/
public Class getColumnClass (int columnIndex) {
	switch (columnIndex) {
		case  __tsid: return String.class;	// tsid
		case  __ts_desc: return String.class;	// description
		case  __ts_active: return String.class;	// isActive
		case  __exp_id: return String.class;	// id
		case  __exp_units: return String.class;	// units
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
		case  __tsid:	return "Time Series ID";
		case  __ts_desc:	return "Description";
		case  __ts_active:	return __arrLabels[0]; //"Time Series Enabled";
		case  __exp_id:	return __arrLabels[1]; //"Export ID";
		case  __exp_units:	return __arrLabels[2];//"Export Units";
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
		case  __tsid:	return "%-15s";	// tsid
		case  __ts_desc:	return "%-15s"; // desc
		case  __ts_active:	return "%-15s"; //enabled
		case  __exp_id:	return "%-15s"; //export id
		case  __exp_units:	return "%-5s"; //units
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
column.  This method is inherited from the very base-most Java TableModel class and is required.
@param row the row for which to return data.
@param col the column for which to return data.
@return the data that should be placed in the JTable at the given row and col.
*/
public Object getValueAt(int row, int col) {
	//  _sortOrder is an inherited int array that maps sorted row 
	// numbers
	// to regular row numbers (or elements within the _data Vector).  
	// If it's null, then that means the table currently isn't sorted.
	if (_sortOrder != null) {
		row = _sortOrder[row];
	}
	
	//  this is where the most Exportant part of the table model is.
	// It gets the object out of the array and returns the right values
	// for each column.  You can do a lot in here, I've done some really
	// complicated stuff for some tables.  But for the most part,
	// something simple like below will work
	RiversideDB_ExportConf table = (RiversideDB_ExportConf )_data.get(row);

	String s ="";
	RiversideDB_MeasType mt = null;
	mt = ( RiversideDB_MeasType ) __measType_vect.get(row);
	switch (col) {
		case  __tsid:	//tsid	
				if ( mt == null ) { 
					return "";
				}
				TSIdent tsid = null;
				try {
					tsid = mt.toTSIdent();
				}
				catch ( Exception e ) {
					Message.printWarning( 2, 
					"RiversideDB_ExportConf_TableModel. " +
					"getValueAt()", e);
					return "";
				}
				if ( tsid == null ) {
					return "";
				}
				return tsid.toString();

		case  __ts_desc:	//description
				if ( mt == null ) {
					return "";
				}
				return mt.getDescription();

		case  __ts_active:	//isActive
				s = table.getIsActive();
				if (( s == null ) || (s.length() <=0 )) {
					s = "N";
				}
				return s;
			
		case  __exp_id:	//export id
				s = table.getExport_id();
				if (( s== null ) || (s.length() <=0 )) {
					s = "";
				}
				return s;

		case  __exp_units:	//units
				s = table.getExport_units();
				if (( s== null ) || (s.length() <=0 )) {
					s = "SAME";
				}
				return s;

		default:	return "";
	}
}

/**
Returns the text to be assigned to worksheet tooltips.
@return a String array of tooltips text.
*/
public String [] getColumnToolTips() {
  	String[] tips = new String[__columns];
	tips[0] =  __arrToolTips[0];
	tips[1] =  __arrToolTips[1];
	tips[2] =  __arrToolTips[2];
	tips[3] =  __arrToolTips[3];
	tips[4] =  __arrToolTips[4];
        return tips;
	
}

/**
Returns an array containing the widths (in number of characters) that the 
fields in the table should be sized to.
@return an integer array containing the widths for each field.
*/
public int[] getColumnWidths() {
	int[] widths = new int[__columns];
	int i = 0;
	widths[i++] = 20;	//tsid
	widths[i++] = 22;	//desc	
	widths[i++] = 5;	//isActive
	widths[i++] = 10;	//export id
	widths[i++] = 6;	//units
	return widths;
}
/**
Returns whether the cell at the given position is editable or not.  
The TSIdent and Descriptions are not editable, but the fields from
the ExportConf objects are ( data file, data column, table id, units, active).
are.
@param rowIndex unused
@param columnIndex the index of the column to check for whether it is editable.
@return whether the cell at the given position is editable.
*/
public boolean isCellEditable(int rowIndex, int columnIndex) {
	//All columns but the first 2 are editable
	if( ( columnIndex == 0 ) || ( columnIndex == 1 ) ) {
		return false;
	}
	return true;
}

/**
Sets the ToolTips
*/
public void setColumnToopTip( int column, String tip ) {
	__arrToolTips[ column ] = tip;
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
	String s= "";
	RiversideDB_ExportConf table = null;
	switch (col) {
		case  __tsid: //can't change TSID
			break;
 		case  __ts_desc: //can't change TS description
			break;
 		case  __ts_active: //enabled
			s = (String) value;
			table = (RiversideDB_ExportConf) _data.get( row );
			table.setIsActive( s.toUpperCase() );
			break;
 		case  __exp_id: //export id
			s = (String) value;
			table = (RiversideDB_ExportConf) _data.get( row );
			table.setExport_id( s.toUpperCase() );
			break;
 		case  __exp_units: //units
			s = (String) value;
			if ( s.equals("SAME") ) {
				s = "";
			}
			table = (RiversideDB_ExportConf) _data.get( row );
			table.setExport_units( s.toUpperCase() );
			break;
	}
	updateWorksheet();

	super.setValueAt( value, row, col);
}

/**
Reference to the worksheet in which this table model manages the data. 
*/ 
public void setWorksheet(JWorksheet worksheet) {
	__worksheet = worksheet;

	updateWorksheet();
}

/**
This method  creates the JComoBox
the Export_units column, the content of which varies 
depending on the MeasType object, and the IsActive
JComboBox which contains: Y/N options.
*/
public void updateWorksheet( ) {
	//make comboBox for isActive YES or NO
	List active_vect = new Vector();
	active_vect.add( "Y" );
	active_vect.add( "N" );

	//set column for to use this (choices do not change )
	__worksheet.setColumnJComboBoxValues( __ts_active, active_vect );

	//make comboboxes for the units

	//tell worksheet using cell specific jcombobox for units
	//since units choices change depending on MeasType data type.
	__worksheet.setCellSpecificJComboBoxColumn( __exp_units, true );

	//go thru each item in worksheet and make specific JCombobox for
	//units depending on MeasType object data type.
	RiversideDB_MeasType mt =null;
	TSIdent tsid = null;
	String type = null;
	int size_dt = __dataType_vect.size();
	for ( int i=0; i<_rows; i++ ) {
		List units_vect= new Vector();
		mt = (RiversideDB_MeasType) __measType_vect.get(i);
		if ( mt == null ) {	
			continue;
		}
		try {
			tsid = mt.toTSIdent();
		}
		catch ( Exception e) {
			Message.printWarning( 2, "", e );
			tsid =null;
		}
		type = tsid.getType();
		RiversideDB_DataType dt =null;
		String dim = "";
		List du_vect = null;
		//use type to get DataType object
		for ( int j=0; j<size_dt; j++ ) {
			dt = (RiversideDB_DataType)__dataType_vect.get(j);
			if ( dt == null ) {	
				continue;
			}	
			if ( (type != null ) && (dt.getDataType().equals( type ) )) {
				//use this to get the Dimension
				dim = dt.getDimension();
				break;
			}
		}
		dt= null;

		//use Dimension to find units
		du_vect = DataUnits.lookupUnitsForDimension( IOUtil.getPropValue("UNITS_SYSTEM"), dim );

		if ( du_vect == null ) {	
			continue;
		}
		int du_size = du_vect.size();
		DataUnits du = null;
		for ( int k=0; k<du_size; k++ ) {
			du =(DataUnits) du_vect.get(k);
			if ( du == null ) {	
				continue;
			}
			units_vect.add( du.getAbbreviation() );
			du = null;
		//set this to be the items in the cell specific JComboBox
	
			if ( k == du_size-1 ) {
				units_vect.add( "SAME" );
			}

			__worksheet.
			setCellSpecificJComboBoxValues( i, __exp_units, units_vect );
		}

		mt = null;
		tsid= null;
		type = null;
	}
}

}
