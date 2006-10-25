

//------------------------------------------------------------------------------
// RiversideDB_ReferenceTable_DataType_TableModel - Table model used for
// 	displaying thw data editor for the reference table DataType.
//	This class extends from RiversideDB_ReferenceTable_Abstract_TableModel.
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
// 2004-12-16 Luiz Teixeira		Split the original (JTS) 
//					  RiversideDB_ReferenceTable_JFrame class
//					into:
//					  RiversideDB_ReferenceTable_Abstract_
//					  TableModel
//					and
//					  RiversideDB_ReferenceTable_*_TableModel
//					where * is:
//						DataDimension
//						DataSource
//						DataType
//						DataUnits
//						ImportType
//						MeasCreateMethod
//						MeasQualityFlag
//						MeasReductionType
//						MeasTimeScale
//						MeasTransProtocol
//						MessageLog
//						Scenario
//						SHEFType
//						TableLayout
// 2004-12-17 Luiz Teixeira 	Removed all code that become unnecessary after
//				the implementation described above. Renamed 
//				variables (more descriptive) and redone some of 
//				the documentation.
// 2004-12-22 Luiz Teixeira	Upgraded the method checkTableDirty(), now
//				abstract in the base class RiversideDB_Reference
//				Table_Abstract_TableModel.
// 2005-01-05 Luiz Teixeira, RTi	Added the argument rdmi to the constructor
//					to be used when versioning is needed.
// 2005-01-05 Luiz Teixeira, RTi	Matched the window width in the JFrame
//					class with the Sum of the table Column
//					Widths here.
//------------------------------------------------------------------------------
package RTi.DMI.RiversideDB_DMI;

import java.util.Vector;

import RTi.Util.Message.Message;
import RTi.DMI.DMIUtil;

/**
Table model used for displaying thw data editor for the table DataType.
This class extends from RiversideDB_ReferenceTable_Abstract_TableModel.
*/
public class RiversideDB_ReferenceTable_DataType_TableModel
	extends RiversideDB_ReferenceTable_Abstract_TableModel
{

/**
References to columns.
*/
public final static int
	COL_DATA_TYPE_DATATYPE           =  0,
	COL_DATA_TYPE_DEFAULT_ENGL_MAX   =  1,
	COL_DATA_TYPE_DEFAULT_ENGL_MIN   =  2,
	COL_DATA_TYPE_DEFAULT_ENGL_UNITS =  3,
	COL_DATA_TYPE_DEFAULT_SI_MAX     =  4,
	COL_DATA_TYPE_DEFAULT_SI_MIN     =  5,
	COL_DATA_TYPE_DEFAULT_SI_UNITS   =  6,
	COL_DATA_TYPE_DESCRIPTION        =  7,
	COL_DATA_TYPE_DIMENSION          =  8,
	COL_DATA_TYPE_MEAS_LOC_TYPE      =  9,
	COL_DATA_TYPE_MEAS_TIME_SCALE    = 10,
	COL_DATA_TYPE_RECORD_TYPE        = 11,
	COL_DATA_TYPE_SHEF_PE            = 12,
	NUMBER_OF_COLUMNS                = 13;

/**
Default constructor.
@param rdmi the RiversideDB_dmi to use.
@param results the results to show in the table.
@param editable whether the data is editable or not.
*/
public RiversideDB_ReferenceTable_DataType_TableModel (
	RiversideDB_DMI rdmi, Vector results, boolean editable )
throws Exception {
	super ( rdmi, results, editable );
	_numberOfColumns = NUMBER_OF_COLUMNS;	
}

/**
Stores a backup of each object so that the original values prior to any
editing can be chcked later to find out if anything is different.
*/
public void backupData()
{
	for (int i = 0; i < _rows; i++) {
		RiversideDB_DataType d3 =
			(RiversideDB_DataType)_data.elementAt(i);
		d3.setOriginal(d3.cloneSelf());
		d3.setDirty(false);
	}
}

/**
Checks if the key field in the table already has a certain value, and if so,
generates a unique value.
@param s the String to check for in the key field.
@param row the row in which the given value is to be placed.
@return a unique String (might be the one passed in) that can validly be
placed in the key field.
*/
protected String checkTableField( String s, int row )
{
	s = replaceSpaces(s);
	RiversideDB_DataType d;
	for (int i = 0; i < _rows; i++) {
		if (i != row) {
			d = (RiversideDB_DataType)_data.elementAt(i);
			if (d.getDataType().trim().equals(s)) {
				return checkTableField("X" + s, row);
			}
		}
	}
	return s;
}

/**
Checks if the table object is dirty.
@param currentObject the current RiversideDB_<TableName> object
@param originalObject the original RiversideDB_<TableName> object
@return true if the object has changed, false otherwise.
*/
protected void checkTableDirty( Object currentObject, Object originalObject )
{
	RiversideDB_DataType current =
		(RiversideDB_DataType) currentObject;
	RiversideDB_DataType original =
		(RiversideDB_DataType) originalObject;

	if ( original == null ) {
		current.setDirty( true );
	} else {
		if ( !current.equals( original ) ) {
			current.setDirty ( true );
		} else {
			current.setDirty ( false );
		}
	}
}

/**
Returns the class of data stored in the specified column.
@param column the column to return the class of.
@return the class of data stored in the specified column.
*/
public Class getColumnClass ( int column )
{
	switch (column) {	
		case COL_DATA_TYPE_DATATYPE:	       return String.class;
		case COL_DATA_TYPE_DEFAULT_ENGL_MAX:   return Double.class;
		case COL_DATA_TYPE_DEFAULT_ENGL_MIN:   return Double.class;
		case COL_DATA_TYPE_DEFAULT_ENGL_UNITS: return String.class;
		case COL_DATA_TYPE_DEFAULT_SI_MAX:     return Double.class;
		case COL_DATA_TYPE_DEFAULT_SI_MIN:     return Double.class;
		case COL_DATA_TYPE_DEFAULT_SI_UNITS:   return String.class;
		case COL_DATA_TYPE_DESCRIPTION:	       return String.class;
		case COL_DATA_TYPE_DIMENSION:	       return String.class;
		case COL_DATA_TYPE_MEAS_LOC_TYPE:      return String.class;
		case COL_DATA_TYPE_MEAS_TIME_SCALE:    return String.class;
		case COL_DATA_TYPE_RECORD_TYPE:	       return String.class;
		case COL_DATA_TYPE_SHEF_PE:	       return String.class;	
		default:			       return String.class;
	}
}

/**
Returns the name of the specified column.
@param column the column for which to return the name.
@return the name of the specified column.
*/
public String getColumnName( int column )
{
	switch (column) {
		case COL_DATA_TYPE_DATATYPE:           return "\nDatatype";
		case COL_DATA_TYPE_DEFAULT_ENGL_MAX:   return "Default\nEngl Max";
		case COL_DATA_TYPE_DEFAULT_ENGL_MIN:   return "Default\nEngl Min";
		case COL_DATA_TYPE_DEFAULT_ENGL_UNITS: return "Default\nEnglish Units";
		case COL_DATA_TYPE_DEFAULT_SI_MAX:     return "Default\nSI Max";
		case COL_DATA_TYPE_DEFAULT_SI_MIN:     return "Default\nSI Min";
		case COL_DATA_TYPE_DEFAULT_SI_UNITS:   return "Default\nSI Units";
		case COL_DATA_TYPE_DESCRIPTION:        return "\nDescription";
		case COL_DATA_TYPE_DIMENSION:          return "\nDimension";
		case COL_DATA_TYPE_MEAS_LOC_TYPE:      return "MeasLoc\nType";
		case COL_DATA_TYPE_MEAS_TIME_SCALE:    return "Meas\nTime Scale";
		case COL_DATA_TYPE_RECORD_TYPE:        return "Record\nType";
		case COL_DATA_TYPE_SHEF_PE:            return "\nSHEF PE";
		default:                               return " ";
	}
}

/**
Returns the tool tips for the columns.
@return the tool tips for the columns.
*/
public String[] getColumnToolTips()
{
	String[] tips = new String[_numberOfColumns];                                 
	tips[COL_DATA_TYPE_DATATYPE]           = "DataType.DataType";
	tips[COL_DATA_TYPE_DEFAULT_ENGL_MAX]   = "DataType.Default_engl_max";
	tips[COL_DATA_TYPE_DEFAULT_ENGL_MIN]   = "DataType.Default_engl_min";
	tips[COL_DATA_TYPE_DEFAULT_ENGL_UNITS] = "DataType.Default_engl_units";
	tips[COL_DATA_TYPE_DEFAULT_SI_MAX]     = "DataType.Default_si_max";
	tips[COL_DATA_TYPE_DEFAULT_SI_MIN]     = "Datatype.Default_si_min";
	tips[COL_DATA_TYPE_DEFAULT_SI_UNITS]   = "DataType.Default_si_units";
	tips[COL_DATA_TYPE_DESCRIPTION]        = "DataType.Description";
	tips[COL_DATA_TYPE_DIMENSION]          = "DataType.Dimension";
	tips[COL_DATA_TYPE_MEAS_LOC_TYPE]      = "DataType.Meas_loc_type";
	tips[COL_DATA_TYPE_MEAS_TIME_SCALE]    = "DataType.Meas_time_scale";
	tips[COL_DATA_TYPE_RECORD_TYPE]        = "DataType.Record_type";
	tips[COL_DATA_TYPE_SHEF_PE]            = "DataType.SHEF_pe";                                            	
	return tips;
}

/**
Returns the widths of all the table columns.
@return the widths of all the table columns.
*/
public int[] getColumnWidths()
{
	int[] widths = new int[_numberOfColumns];
	// Window width(check JFrame caller) is +/- ( sum of table widths * 10 )
	widths[COL_DATA_TYPE_DATATYPE]           =  7;
	widths[COL_DATA_TYPE_DEFAULT_ENGL_MAX]   =  6;
	widths[COL_DATA_TYPE_DEFAULT_ENGL_MIN]   =  6;
	widths[COL_DATA_TYPE_DEFAULT_ENGL_UNITS] =  8;
	widths[COL_DATA_TYPE_DEFAULT_SI_MAX]     =  6;
	widths[COL_DATA_TYPE_DEFAULT_SI_MIN]     =  6;
	widths[COL_DATA_TYPE_DEFAULT_SI_UNITS]   =  8;
	widths[COL_DATA_TYPE_DESCRIPTION]        = 30;
	widths[COL_DATA_TYPE_DIMENSION]          = 14;
	widths[COL_DATA_TYPE_MEAS_LOC_TYPE]      =  7;
	widths[COL_DATA_TYPE_MEAS_TIME_SCALE]    =  8;
	widths[COL_DATA_TYPE_RECORD_TYPE]        =  5;
	widths[COL_DATA_TYPE_SHEF_PE]            =  7;
	return widths;
}

/**
Returns the format of the specified column.
@param column the column to return the format for.
@return the format of the specified column.
*/
public String getFormat( int column )
{
	switch ( column ) {	
		case COL_DATA_TYPE_DATATYPE:		return "%-20s";
		case COL_DATA_TYPE_DEFAULT_ENGL_MAX:	return "%10.2f";
		case COL_DATA_TYPE_DEFAULT_ENGL_MIN:	return "%10.2f";
		case COL_DATA_TYPE_DEFAULT_ENGL_UNITS:	return "%-20s";
		case COL_DATA_TYPE_DEFAULT_SI_MAX:	return "%10.2f";
		case COL_DATA_TYPE_DEFAULT_SI_MIN:	return "%10.2f";
		case COL_DATA_TYPE_DEFAULT_SI_UNITS:	return "%-20s";
		case COL_DATA_TYPE_DESCRIPTION:		return "%-20s";
		case COL_DATA_TYPE_DIMENSION:		return "%-20s";
		case COL_DATA_TYPE_MEAS_LOC_TYPE:	return "%-20s";
		case COL_DATA_TYPE_MEAS_TIME_SCALE:	return "%-20s";
		case COL_DATA_TYPE_RECORD_TYPE:		return "%-20s";
		case COL_DATA_TYPE_SHEF_PE:		return "%-20s";
		default:				return "%-20s";
	}
}

/**
Returns the value stored at the specified cell ( row, column )
@param row the row from which to return the value.
@param column the column from which to return the value.
@return the value stored at the specified cell ( row, column )
*/
protected Object getTableValueAt( int row, int column )
{
	RiversideDB_DataType d =
		(RiversideDB_DataType)_data.elementAt(row);
		
	switch (column) {
		case COL_DATA_TYPE_DATATYPE:
			return d.getDataType();
		case COL_DATA_TYPE_DEFAULT_ENGL_MAX:
			return new Double(d.getDefault_engl_max());
		case COL_DATA_TYPE_DEFAULT_ENGL_MIN:
			return new Double(d.getDefault_engl_min());
		case COL_DATA_TYPE_DEFAULT_ENGL_UNITS:
			return d.getDefault_engl_units();
		case COL_DATA_TYPE_DEFAULT_SI_MAX:
			return new Double(d.getDefault_si_max());
		case COL_DATA_TYPE_DEFAULT_SI_MIN:
			return new Double(d.getDefault_si_min());
		case COL_DATA_TYPE_DEFAULT_SI_UNITS:
			return d.getDefault_si_units();
		case COL_DATA_TYPE_DESCRIPTION:
			return d.getDescription();
		case COL_DATA_TYPE_DIMENSION:
			return d.getDimension();
		case COL_DATA_TYPE_MEAS_LOC_TYPE:
			return d.getMeas_loc_type();
		case COL_DATA_TYPE_MEAS_TIME_SCALE:
			return d.getMeas_time_scale();
		case COL_DATA_TYPE_RECORD_TYPE:
			return d.getRecord_type();
		case COL_DATA_TYPE_SHEF_PE:
			return d.getSHEF_pe();
		default:
			return " ";
	}
}

/**
Sets the table value at the specified cell ( row, column ).
@param value the value to put in the location.
@param row the row at which to set the value.
@param column the column at which to set the value.
*/
protected Object setTableValueAt( Object value, int row, int column )
{
	RiversideDB_DataType dt =
		(RiversideDB_DataType)_data.elementAt(row);
	RiversideDB_DataType dt0 =
		(RiversideDB_DataType)dt.getOriginal();
	String s;
	double d;
	switch (column) {
		case COL_DATA_TYPE_DATATYPE:
			s = readString(value, 50);
			value = s;
			s = checkTableField(s, row);
			if (!equal(s, dt.getDataType())) {
				dt.setDataType(s);
				checkTableDirty(dt, dt0);
				valueChanged(row, column, value);
			}
			break;
		case COL_DATA_TYPE_DEFAULT_ENGL_MAX:
			d = readDouble(value);
			if (d != dt.getDefault_engl_max()) {
				dt.setDefault_engl_max(d);
				checkTableDirty(dt, dt0);
				valueChanged(row, column, value);
			}
			break;
		case COL_DATA_TYPE_DEFAULT_ENGL_MIN:
			d = readDouble(value);
			if (d != dt.getDefault_engl_min()) {
				dt.setDefault_engl_min(d);
				checkTableDirty(dt, dt0);
				valueChanged(row, column, value);
			}
			break;
		case COL_DATA_TYPE_DEFAULT_ENGL_UNITS:
			s = readString(value, 10);
			value = s;
			if (!equal(s, dt.getDefault_engl_units())) {
				dt.setDefault_engl_units(s);
				checkTableDirty(dt, dt0);
				valueChanged(row, column, value);
			}
			break;
		case COL_DATA_TYPE_DEFAULT_SI_MAX:
			d = readDouble(value);
			if (d != dt.getDefault_si_max()) {
				dt.setDefault_si_max(d);
				checkTableDirty(dt, dt0);
				valueChanged(row, column, value);
			}
			break;
		case COL_DATA_TYPE_DEFAULT_SI_MIN:
			d = readDouble(value);
			if (d != dt.getDefault_si_min()) {
				dt.setDefault_si_min(d);
				checkTableDirty(dt, dt0);
				valueChanged(row, column, value);
			}
			break;
		case COL_DATA_TYPE_DEFAULT_SI_UNITS:
			s = readString(value, 10);
			value = s;
			if (!equal(s, dt.getDefault_si_units())) {
				dt.setDefault_si_units(s);
				checkTableDirty(dt, dt0);
				valueChanged(row, column, value);
			}
			break;
		case COL_DATA_TYPE_DESCRIPTION:
			s = readString(value, 50);
			value = s;
			if (!equal(s, dt.getDescription())) {
				dt.setDescription(s);
				checkTableDirty(dt, dt0);
				valueChanged(row, column, value);
			}
			break;
		case COL_DATA_TYPE_DIMENSION:
			s = trimString((String)value, " - ");
			s = readString(s, 20);
			value = s;
			if (!equal(s, dt.getDimension())) {
				dt.setDimension(s);
				checkTableDirty(dt, dt0);
				valueChanged(row, column, value);
			}
			break;
		case COL_DATA_TYPE_MEAS_LOC_TYPE:
			s = trimString((String)value, " - ");
			s = readString(s, 10);
			value = s;
			if (!equal(s, dt.getMeas_loc_type())) {
				dt.setMeas_loc_type(s);
				checkTableDirty(dt, dt0);
				valueChanged(row, column, value);
			}
			break;
		case COL_DATA_TYPE_MEAS_TIME_SCALE:
			s = trimString((String)value, " - ");
			s = readString(s, 10);
			value = s;
			if (!equal(s, dt.getMeas_time_scale())) {
				dt.setMeas_time_scale(s);
				checkTableDirty(dt, dt0);
				valueChanged(row, column, value);
			}
			break;
		case COL_DATA_TYPE_RECORD_TYPE:
			s = readString(value, 10);
			value = s;
			if (!equal(s, dt.getRecord_type())) {
				dt.setRecord_type(s);
				checkTableDirty(dt, dt0);
				valueChanged(row, column, value);
			}
			break;
		case COL_DATA_TYPE_SHEF_PE:
			s = readString(value, 6);
			value = s;
			if (!equal(s, dt.getSHEF_pe())) {
				dt.setSHEF_pe(s);
				checkTableDirty(dt, dt0);
				valueChanged(row, column, value);
			}
			break;
		default:
			break;
	}
	return value;
}

}
//------------------------------------------------------------------------------