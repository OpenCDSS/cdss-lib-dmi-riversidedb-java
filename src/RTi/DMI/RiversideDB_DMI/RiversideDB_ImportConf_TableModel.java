// ----------------------------------------------------------------------------
// RiversideDB_ImportConf_TableModel - 
// Table model for displaying RiversideDB_ImportConf and
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

import java.util.Vector;

import RTi.TS.TSIdent;

import RTi.Util.GUI.JWorksheet_AbstractRowTableModel;

import RTi.Util.IO.DataUnits;
import RTi.Util.IO.IOUtil;
import RTi.Util.Message.Message;

//import RTi.DMI.RiversideDB_DataType;

/**
This class is a table model for displaying data from 
RiversideDB_ImportConf and MeasType objects.
*/
public class RiversideDB_ImportConf_TableModel 
extends JWorksheet_AbstractRowTableModel {

/**
The number of columns in the table model. 
*/
private int __columns = 7;

/**
Reference to table worksheet this tablemodel is for.
*/
private JWorksheet __worksheet;

/**
Vector of MeasTypes ordered corresponding to the
order of the ImportConf objects passed in.
*/
Vector __measType_vect = null;

/**
Array of Strings to use for the labels of the worksheet pertaining to
the ImportConf objects (all columns except the first 2 which are from
the MeasType object).
*/
String[] __arrLabels = null;

/**
Array of Strings to use for the tooltips of the worksheet pertaining to
the ImportConf objects.
*/
String[] __arrToolTips = null;

// indicates if we are removing the External Table Field from the
//worksheet like for ImportConf objects where the ImportType is SHEF.
String __importType_str = "";

//Vector containing values for field - this is a JComboBox
//(all the choices for field will be the same for the entire column)
Vector _field_vect = null;

//Connection to database to query 
RiversideDB_DMI __rdmi;

//To get JComboBox for units (which will contain different items 
//depending on the data type of the selected product), need to
//query the vector of DataTypes
Vector __dataType_vect = null;

//ROWS
private final static int __tsid = 0;
private final static int __ts_desc = 1;
private final static int __ts_active = 2;
private final static int __ext_id = 3;
private final static int __ext_field = 4;
private final static int __ext_table = 5;
private final static int __ext_units = 6;

/**
Constructor.  This builds the Model for displaying the given 
RiversideDB_ImportConf objects' information.
@param v Vector of Vectors.  First Vector is Vector of 
RiversideDB_ImportConf objects for which to display data.  
The ImportConf object itself knows about the related 
RiversideDB_MeasType object. The second Vector is
a Vector of RiversideDB_MeasTypes objects ordered
in the same order as the ImportConf objects passed in.
@param arrLabels Array of labels to use for the worksheet column names for
the ImportConf objects. Labels in the array are for the following
ImportConf of any type but SHEF object fields:
[0] Row Num
[1] TSID
[2] TS Desc
[3] IsActive
[4] External ID (data file)
[5] External Field (data column)
[6] External Table 
[7] External Units
Labels in the array for the ImportConf of type SHEF are:
[0] Row Num
[1] TSID
[2] TS Desc
[3] IsActive
[4] External ID (SHEF ID)
[5] External Field (SHEF PE)
[6] External Units
@param arrToolTips Array of Strings to use as the column tooltips.
@param importType_str String indicating type of import product since
different import products will have different number of related
ImportConf fields.
@param dmi Open connection to DMI
@param dataType_vect Vector of RiversideDB_DataType objects used to 
created choices in units JComoBox
@throws Exception if an invalid data or dmi was passed in.
*/
public RiversideDB_ImportConf_TableModel(Vector v, 
String [] arrLabels, String [] arrToolTips, 
String importType_str, RiversideDB_DMI dmi,
Vector dataType_vect ) 
throws Exception {
	if (v.size() != 2 ) {
		throw new Exception ("Need to pass in a Vector of size " +
		"2 containing, 1) a Vector of ImportConf objects and " +
		"2), a Vector of MeasType objects" );
	}
	// ( _data is a Vector inherited from
	// JWorksheet_AbstractRowTableModel.  It stores the Vector of data
	// that you'll show in the table. )
	_data = (Vector)v.elementAt(0);
	__measType_vect = (Vector)v.elementAt(1);

	if (_data == null) {
		throw new Exception ("Null data Vector passed to " 
		+ "RiversideDB_ImportConf_TableModel constructor.");
	}	
	if ( _data.size() != __measType_vect.size() ) {
		throw new Exception ("Vector of Riverside_MeasTypes must " +
		"be same size as Vecotr of RiversideDB_ImportConf objects.");
	}
	__arrLabels = arrLabels;
	__arrToolTips = arrToolTips;

	//  _rows is an integer that contains the number of rows of
	// data to be displayed in the table.  Make sure to set the 
	// value 
	_rows = _data.size();

	//If type of ImportProduct is SHEF, will have ImportConf 4 columns
	//for: SHEF PE, SHEF ID, Units, and Enabled 
	//For CAMPBELLSCI type, ImportConf will have data for:
	//5 columns: Table ID, Data Col, Data File, Units, and Enabled
	__importType_str = importType_str;

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
		mt =( RiversideDB_MeasType) __measType_vect.elementAt(i);
		if ( mt == null ) {
			continue;
		}
		try {
			s = (mt.toTSIdent()).toString();
		}
		catch ( Exception e ) {
			Message.printWarning( 2, 
			"RiversideDB_ImportConf_TableModel." +
			"deleteMeasType", e );
			s = "";
		}
		if ( s.equals( tsid_str ) ) {
			__measType_vect.removeElementAt(i);
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
		case  __tsid: return String.class;	//1 tsid 
		case  __ts_desc: return String.class;	//2 description
		case  __ts_active: return String.class;	//3 isActive
		case  __ext_id: return String.class;	//4 data file/shef ID
		case  __ext_field: return String.class;	//5 Data column/SHEF PE
		case  __ext_table: return String.class;	//6 Table ID
		case  __ext_units: return String.class;	//7 units
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
		case __tsid:	return __arrLabels[1];
		case __ts_desc:	return __arrLabels[2];

		case __ts_active:	return __arrLabels[3]; //"Time Series Enabled";
		case __ext_id:	return __arrLabels[4]; //"Data File" / SHEF ID
		case __ext_field:	return __arrLabels[5];//"Data Column";/SHEF PE
		case __ext_table:	return __arrLabels[6];//"Table ID";
		case __ext_units:	return __arrLabels[7];//"External Units";
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
	tips[5] =  __arrToolTips[5];
	tips[6] =  __arrToolTips[6];
        return tips;
	
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
		case __tsid:	return "%-15s";	// tsid
		case __ts_desc:	return "%-15s"; // desc
		case __ts_active:	return "%-2s"; //enabled
		case __ext_id:	return "%-15s"; //data file /shef ID
		case __ext_field:	return "%-5s"; //data col/SHEF PE
		case __ext_table:	return "%-15s"; //table id
		case __ext_units:	return "%-5s"; //units
		default:	return "%1s";
	}
}

/**
Returns the MeasType object corresponding to the TSIdent String passed in
@param tsid_str TSIdent String
@return RiversideDB_MeasType object
*/
public RiversideDB_MeasType getMeasTypeForTSIdent( String tsid_str ) {
	int num_mt = __measType_vect.size();
	RiversideDB_MeasType mt = null;
	String s = null;
	for ( int i=0; i< num_mt; i++ ) {
		mt =( RiversideDB_MeasType) __measType_vect.elementAt(i);
		if ( mt == null ) {
			continue;
		}
		try {
			s = (mt.toTSIdent()).toString();
		}
		catch ( Exception e ) {
			Message.printWarning( 2, 
			"RiversideDB_ImportConf_TableModel." +
			"deleteMeasType", e );
			s = "";
		}
		if ( s.equals( tsid_str ) ) {
			return mt;
		}
		mt = null;
	}
	return (RiversideDB_MeasType)null;
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
	RiversideDB_ImportConf table = 
	(RiversideDB_ImportConf ) _data.elementAt(row);

	String s ="";
	RiversideDB_MeasType mt = null;
	mt = ( RiversideDB_MeasType ) __measType_vect.elementAt(row);
	switch (col) {
		case __tsid:	//tsid	
				if ( mt == null ) { 
					return "";
				}
				TSIdent tsid = null;
				try {
					tsid = mt.toTSIdent();
				}
				catch ( Exception e ) {
					Message.printWarning( 2, 
					"RiversideDB_ImportConf_TableModel. " +
					"getValueAt()", e);
					return "";
				}
				if ( tsid == null ) {
					return "";
				}
				return tsid.toString();

		case __ts_desc:	//description
				if ( mt == null ) {
					return "";
				}
				return mt.getDescription();

		case __ts_active:	//isActive
				s = table.getIsActive();
				if (( s== null ) || (s.length() <=0 )) {
					s = "N";
				}
				return s;
			
		case __ext_id:	//data file	/  SHEF ID
				s = table.getExternal_id();
				if (( s== null ) || (s.length() <=0 )) {
					s = "";
				}
				return s;

		case __ext_field:	//data col	/ SHEF PE
				s = table.getExternal_field();
				if (( s== null ) || (s.length() <=0 )) {
					s = "";
				}
				return s;

		case __ext_table:	//table id 	
				s = table.getExternal_table();
				if (( s== null ) || (s.length() <=0 )) {
					s = "";
				}
				return s;
			
		case __ext_units:	//units
				s = table.getExternal_units();
				if (( s== null ) || (s.length() <=0 )) {
					s = "SAME";
				}
				return s;


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
	widths[i++] = 20;	// 0: tsid
	widths[i++] = 22;	// 1: desc	
	widths[i++] = 5;	// 2: active
	if ( __importType_str.indexOf( "SHEF" ) >=0  ) {
		widths[i++] = 5;	//3: SHEF ID
	}
	else {
		widths[i++] = 8;	//3: DATA FILE
	}
	widths[i++] = 5;	//4: data column/shef PE
	widths[i++] = 6;	//5: table ID
	widths[i++] = 6;	//6: units
	return widths;
}
/**
Returns whether the cell at the given position is editable or not.  
The TSIdent and Descriptions are not editable, but the fields from
the ImportConf objects are ( data file, data column, table id, units, active).
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
	RiversideDB_ImportConf table = null;
	switch (col) {
		case __tsid: //can't change TSID
			break;
 		case __ts_desc: //can't change TS description
			break;
 		case __ts_active: //enabled
			s = (String) value;
			table = 
			(RiversideDB_ImportConf) _data.elementAt( row );
			table.setIsActive( s.toUpperCase() );
			break;
 		case __ext_id: //data file/ shef id
			s = (String) value;
			table = 
			(RiversideDB_ImportConf) _data.elementAt( row );
			table.setExternal_id( s.toUpperCase() );
			break;
 		case __ext_field: //data col/SHEF PE
			s = (String) value;
			table = 
			(RiversideDB_ImportConf) _data.elementAt( row );
			table.setExternal_field( s.toUpperCase() );
			break;
 		case __ext_table: //table file 
			s = (String) value;
			table = 
			(RiversideDB_ImportConf) _data.elementAt( row );
			table.setExternal_table( s.toUpperCase() );
			break;
 		case __ext_units: //units
			s = (String) value;
			if ( s.equals("SAME") ) {
				s = "";
			}
			table = 
			(RiversideDB_ImportConf) _data.elementAt( row );
			table.setExternal_units( s.toUpperCase() );
			break;
	}
	updateWorksheet();

	super.setValueAt( value, row, col);
}

/**
Reference to the worksheet in which this table model
manages the data.  This method also removes any columns
of data that are not pertinent to the particular Type of 
ImportProduct object this ImportConf object is associated with.
*/ 
public void setWorksheet(JWorksheet worksheet) {
	__worksheet = worksheet;

	updateWorksheet();

	//if depending on type of importproduct, may need to
	//remove rows... For shef, we don't need column: external table
	if ( __importType_str.indexOf( "SHEF" ) >=0  ) {
		__worksheet.removeColumn( __ext_table ); 
	}
}

/**
Method creates JComboBoxes for the following 3 columns:
<table>
<tr><th>Field</th><th>Contents of JComboBox</th></tr>
<tr><td>IsActive</td><td>Y or N</td></tr>
<tr><td>External_field</td><td>Field options</td></tr>
<tr>External_units<td></td>Units, which vary depending on MeasType object<td></td></tr>
</table>
*/
public void updateWorksheet( ) {
	//make comboBox for isActive YES or NO. 
	Vector active_vect = new Vector();
	active_vect.addElement( "Y" );
	active_vect.addElement( "N" );

	//set column for to use this (choices do not change )
	__worksheet.setColumnJComboBoxValues( __ts_active, active_vect );

	//make comboboxes for the field and units
	if ( _field_vect == null ) {
		_field_vect= new Vector();
		if ( __importType_str.indexOf( "SHEF" ) >= 0 ){
			//data from SHEF_TYPE table
			Vector v= null;
			try {
				v = __rdmi.readSHEFTypeList();
			}
			catch ( Exception e ) {
				Message.printWarning( 2, 
				"RiversideDB_ImportConf_TableModel." +
				"setWorksheet", e );
				v = new Vector();
			}
			int s = v.size();
			RiversideDB_SHEFType st = null;
			for ( int i=0; i< s; i++ ){
				st = ( RiversideDB_SHEFType )v.elementAt(i);
				if ( st == null ) {
					continue;
				}
				_field_vect.addElement( st.getSHEF_pe() );
			}
			v = null;
		}
		else {
			//numbers 1-50
			for ( int i=1; i<=50; i++ ) {
				_field_vect.addElement( String.valueOf(i) );
			}
		
		}
	}
	//set column for to use this (choices do not change )
	__worksheet.setColumnJComboBoxValues( __ext_field, _field_vect );

	//tell worksheet using cell specific jcombobox for units
	//since units choices change depending on MeasType data type.
	__worksheet.setCellSpecificJComboBoxColumn( __ext_units, true );

	//go thru each item in worksheet and make specific JCombobox for
	//units depending on MeasType object data type.
	RiversideDB_MeasType mt =null;
	TSIdent tsid = null;
	String type = null;
	int size_dt = __dataType_vect.size();
	for ( int i=0; i<_rows; i++ ) {
		Vector units_vect= new Vector();
		mt = (RiversideDB_MeasType) __measType_vect.elementAt(i);
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
		Vector du_vect = null;
		//use type to get DataType object
		for ( int j=0; j<size_dt; j++ ) {
			dt = (RiversideDB_DataType) __dataType_vect.
			elementAt(j);
			if ( dt == null ) {	
				continue;
			}	
			if ( (type != null ) && 
			(dt.getDataType().equals( type ) )) {
				//use this to get the Dimension
				dim = dt.getDimension();
				break;
			}
		}
		dt= null;

		//use Dimension to find units
		du_vect = DataUnits.lookupUnitsForDimension(
		IOUtil.getPropValue("UNITS_SYSTEM"), dim );

		if ( du_vect == null ) {	
			continue;
		}
		int du_size = du_vect.size();
		DataUnits du = null;
		for ( int k=0; k<du_size; k++ ) {
			du =(DataUnits) du_vect.elementAt(k);
			if ( du == null ) {	
				continue;
			}
			units_vect.addElement( du.getAbbreviation() );
			du = null;
		//set this to be the items in the cell specific JComboBox
	
			if ( k == du_size-1 ) {
				units_vect.addElement( "SAME" );
			}

			__worksheet.
			setCellSpecificJComboBoxValues( 
			i, __ext_units, units_vect );
		}

		mt = null;
		tsid= null;
		type = null;
	}
}

}
