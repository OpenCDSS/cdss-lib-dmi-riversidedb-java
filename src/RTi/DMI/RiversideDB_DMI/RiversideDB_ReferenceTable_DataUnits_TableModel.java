

//------------------------------------------------------------------------------
// RiversideDB_ReferenceTable_DataUnits_TableModel - Table model used for
// 	displaying thw data editor for the reference table DataUnits.
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
// 2004-12-16 Luiz Teixeira 		Split the original (JTS) 
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

/**
Table model used for displaying the data editor for the table DataUnits.
This class extends from RiversideDB_ReferenceTable_Abstract_TableModel.
*/
public class RiversideDB_ReferenceTable_DataUnits_TableModel
	extends RiversideDB_ReferenceTable_Abstract_TableModel
{

/**
References to columns.
*/
public final static int
	COL_DATA_UNITS_UNITS_ABBREV      = 0,
	COL_DATA_UNITS_ADD_FACTOR        = 1,
	COL_DATA_UNITS_BASE_UNIT         = 2,
	COL_DATA_UNITS_DIMENSION         = 3,
	COL_DATA_UNITS_MULT_FACTOR       = 4,
	COL_DATA_UNITS_OUTPUT_PRECISION  = 5,
	COL_DATA_UNITS_UNITS_DESCRIPTION = 6,
	COL_DATA_UNITS_UNITS_SYSTEM      = 7,
	NUMBER_OF_COLUMNS                = 8;

/**
Default constructor.
@param rdmi the RiversideDB_dmi to use.
@param results the results to show in the table.
@param editable whether the data is editable or not.
*/
public RiversideDB_ReferenceTable_DataUnits_TableModel (
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
		RiversideDB_DataUnits d4 =
			(RiversideDB_DataUnits)_data.elementAt(i);
		d4.setOriginal(d4.cloneSelf());
		d4.setDirty(false);
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
	RiversideDB_DataUnits d;
	for (int i = 0; i < _rows; i++) {
		if (i != row) {
			d = (RiversideDB_DataUnits)_data.elementAt(i);
			if (d.getUnits_abbrev().trim().equals(s)) {
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
	RiversideDB_DataUnits current =
		(RiversideDB_DataUnits) currentObject;
	RiversideDB_DataUnits original =
		(RiversideDB_DataUnits) originalObject;

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
		case COL_DATA_UNITS_UNITS_ABBREV:      return String.class;
		case COL_DATA_UNITS_ADD_FACTOR:	       return Double.class;
		case COL_DATA_UNITS_BASE_UNIT:	       return String.class;
		case COL_DATA_UNITS_DIMENSION:	       return String.class;
		case COL_DATA_UNITS_MULT_FACTOR:       return Double.class;
		case COL_DATA_UNITS_OUTPUT_PRECISION:  return Integer.class;
		case COL_DATA_UNITS_UNITS_DESCRIPTION: return String.class;
		case COL_DATA_UNITS_UNITS_SYSTEM:      return String.class;
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
	switch ( column ) {	                                         
		case COL_DATA_UNITS_UNITS_ABBREV:      return "Units\nAbbrev";
		case COL_DATA_UNITS_ADD_FACTOR:        return "Units\nAdd Factor";
		case COL_DATA_UNITS_BASE_UNIT:         return "\nBase Unit";
		case COL_DATA_UNITS_DIMENSION:         return "\nDimension";
		case COL_DATA_UNITS_MULT_FACTOR:       return "Mult\nFactor";
		case COL_DATA_UNITS_OUTPUT_PRECISION:  return "Output\nPrecision";
		case COL_DATA_UNITS_UNITS_DESCRIPTION: return "Units\nDescription";
		case COL_DATA_UNITS_UNITS_SYSTEM:      return "Units\nSystem";
		default: return " ";
	}
}

/**
Returns the tool tips for the columns.
@return the tool tips for the columns.
*/
public String[] getColumnToolTips()
{
	String[] tips = new String[_numberOfColumns];                                           
	tips[COL_DATA_UNITS_UNITS_ABBREV]      = "DataUnits.Units_abbrev";
	tips[COL_DATA_UNITS_ADD_FACTOR]        = "DataUnits.Add_factor";
	tips[COL_DATA_UNITS_BASE_UNIT]         = "DataUnits.Base_unit";
	tips[COL_DATA_UNITS_DIMENSION]         = "DataUnits.Dimension";
	tips[COL_DATA_UNITS_MULT_FACTOR]       = "DataUnits.Mult_factor";
	tips[COL_DATA_UNITS_OUTPUT_PRECISION]  = "DataUnits.Output_precision";
	tips[COL_DATA_UNITS_UNITS_DESCRIPTION] = "DataUnits.Units_description";
	tips[COL_DATA_UNITS_UNITS_SYSTEM]      = "DataUnits.Units_system";	
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
	widths[COL_DATA_UNITS_UNITS_ABBREV]      =  5;
	widths[COL_DATA_UNITS_ADD_FACTOR]        =  7;
	widths[COL_DATA_UNITS_BASE_UNIT]         =  6;
	widths[COL_DATA_UNITS_DIMENSION]         =  8;
	widths[COL_DATA_UNITS_MULT_FACTOR]       =  6;
	widths[COL_DATA_UNITS_OUTPUT_PRECISION]  =  6;
	widths[COL_DATA_UNITS_UNITS_DESCRIPTION] = 17;
	widths[COL_DATA_UNITS_UNITS_SYSTEM]      =  5;
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
		case COL_DATA_UNITS_UNITS_ABBREV:      return "%-20s";
		case COL_DATA_UNITS_ADD_FACTOR:	       return "%10.2f";
		case COL_DATA_UNITS_BASE_UNIT:	       return "%-20s";
		case COL_DATA_UNITS_DIMENSION:	       return "%-20s";
		case COL_DATA_UNITS_MULT_FACTOR:       return "%10.2f";
		case COL_DATA_UNITS_OUTPUT_PRECISION:  return "%8d";
		case COL_DATA_UNITS_UNITS_DESCRIPTION: return "%-20s";
		case COL_DATA_UNITS_UNITS_SYSTEM:      return "%-20s";
		default:			       return "%-20s";
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
	RiversideDB_DataUnits d =
		(RiversideDB_DataUnits)_data.elementAt(row);
		
	switch (column) {
		case COL_DATA_UNITS_UNITS_ABBREV:
			return d.getUnits_abbrev();
		case COL_DATA_UNITS_ADD_FACTOR:
			return new Double(d.getAdd_factor());
		case COL_DATA_UNITS_BASE_UNIT:
			return d.getBase_unit();
		case COL_DATA_UNITS_DIMENSION:
			return d.getDimension();
		case COL_DATA_UNITS_MULT_FACTOR:
			return new Double(d.getMult_factor());
		case COL_DATA_UNITS_OUTPUT_PRECISION:
			return new Integer(d.getOutput_precision());
		case COL_DATA_UNITS_UNITS_DESCRIPTION:
			return d.getUnits_description();
		case COL_DATA_UNITS_UNITS_SYSTEM:
			return d.getUnits_system();
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
	RiversideDB_DataUnits du =
		(RiversideDB_DataUnits)_data.elementAt(row);
	RiversideDB_DataUnits du0 =
		(RiversideDB_DataUnits)du.getOriginal();
	double d;
	int i;
	String s;
	switch (column) {
		case COL_DATA_UNITS_UNITS_ABBREV:
			s = readString(value, 10);
			value = s;
			s = checkTableField(s, row);
			if (!equal(s, du.getUnits_abbrev())) {
				du.setUnits_abbrev(s);
				checkTableDirty(du, du0);
				valueChanged(row, column, value);
			}
			break;
		case COL_DATA_UNITS_ADD_FACTOR:
			d = readDouble(value);
			if (d != du.getAdd_factor()) {
				du.setAdd_factor(d);
				checkTableDirty(du, du0);
				valueChanged(row, column, value);
			}
			break;
		case COL_DATA_UNITS_BASE_UNIT:
			s = readString(value, 1);
			value = s;
			if (!equal(s, du.getBase_unit())) {
				du.setBase_unit(s);
				checkTableDirty(du, du0);
				valueChanged(row, column, value);
			}
			break;
		case COL_DATA_UNITS_DIMENSION:
			s = trimString((String)value, " - ");
			s = readString(s, 20);
			value = s;
			if (!equal(s, du.getDimension())) {
				du.setDimension(s);
				checkTableDirty(du, du0);
				valueChanged(row, column, value);
			}
			break;
		case COL_DATA_UNITS_MULT_FACTOR:
			d = readDouble(value);
			if (d != du.getMult_factor()) {
				du.setMult_factor(d);
				checkTableDirty(du, du0);
				valueChanged(row, column, value);
			}
			break;
		case COL_DATA_UNITS_OUTPUT_PRECISION:
			i = readInt(value);
			if (i != du.getOutput_precision()) {
				du.setOutput_precision(i);
				checkTableDirty(du, du0);
				valueChanged(row, column, value);
			}
			break;
		case COL_DATA_UNITS_UNITS_DESCRIPTION:
			s = readString(value, 50);
			value = s;
			if (!equal(s, du.getUnits_description())) {
				du.setUnits_description(s);
				checkTableDirty(du, du0);
				valueChanged(row, column, value);
			}
			break;
		case COL_DATA_UNITS_UNITS_SYSTEM:
			s = readString(value, 4);
			value = s;
			if (!equal(s, du.getUnits_system())) {
				du.setUnits_system(s);
				checkTableDirty(du, du0);
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