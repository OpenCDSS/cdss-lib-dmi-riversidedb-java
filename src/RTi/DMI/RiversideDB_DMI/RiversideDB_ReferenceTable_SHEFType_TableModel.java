

//------------------------------------------------------------------------------
// RiversideDB_ReferenceTable_SHEFType_TableModel - Table model used for
// 	displaying thw data editor for the reference table SHEFType.
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

import java.util.List;

/**
Table model used for displaying the data editor for the reference table 
SHEFType.
This class extends from RiversideDB_ReferenceTable_Abstract_TableModel.
*/
public class RiversideDB_ReferenceTable_SHEFType_TableModel
	extends RiversideDB_ReferenceTable_Abstract_TableModel
{

/**
References to columns.
*/
public final static int
	COL_SHEF_TYPE_SHEF_PE      = 0,
	COL_SHEF_TYPE_DEFAULT_BASE = 1,
	COL_SHEF_TYPE_DEFAULT_MULT = 2,
	COL_SHEF_TYPE_TIME_SCALE   = 3,
	COL_SHEF_TYPE_UNITS_ENGL   = 4,
	COL_SHEF_TYPE_UNITS_SI     = 5,
	NUMBER_OF_COLUMNS          = 6;

/**
Default constructor.
@param rdmi the RiversideDB_dmi to use.
This constructor should simple run the base class constructor.
@param results the results to show in the table.
@param editable whether the data is editable or not.
*/
public RiversideDB_ReferenceTable_SHEFType_TableModel ( RiversideDB_DMI rdmi, List results, boolean editable )
throws Exception {
	super ( rdmi, results, editable );
	_numberOfColumns = NUMBER_OF_COLUMNS;	
}

/**
Stores a backup of each object so that the original values prior to any
editing can be checked later to find out if anything is different.
*/
public void backupData()
{
	for (int i = 0; i < _rows; i++) {
		RiversideDB_SHEFType s1 = (RiversideDB_SHEFType)_data.get(i);
		s1.setOriginal(s1.cloneSelf());
		s1.setDirty(false);
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
	RiversideDB_SHEFType d;
	for (int i = 0; i < _rows; i++) {
		if (i != row) {
			d = (RiversideDB_SHEFType)_data.get(i);
			if (d.getSHEF_pe().trim().equals(s)) {
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
	RiversideDB_SHEFType current =
		(RiversideDB_SHEFType) currentObject;
	RiversideDB_SHEFType original =
		(RiversideDB_SHEFType) originalObject;

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
	switch ( column ) {
		case COL_SHEF_TYPE_SHEF_PE:	 return String.class;
		case COL_SHEF_TYPE_DEFAULT_BASE: return String.class;
		case COL_SHEF_TYPE_DEFAULT_MULT: return Integer.class;
		case COL_SHEF_TYPE_TIME_SCALE:	 return String.class;
		case COL_SHEF_TYPE_UNITS_ENGL:	 return String.class;
		case COL_SHEF_TYPE_UNITS_SI:	 return String.class;
		default:			 return String.class;
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
		case COL_SHEF_TYPE_SHEF_PE:      return "\nSHEF PE";
		case COL_SHEF_TYPE_DEFAULT_BASE: return "Default\nBase";
		case COL_SHEF_TYPE_DEFAULT_MULT: return "Default\nMult";
		case COL_SHEF_TYPE_TIME_SCALE:   return "Time\nScale";
		case COL_SHEF_TYPE_UNITS_ENGL:   return "\nUnits Engl";
		case COL_SHEF_TYPE_UNITS_SI:     return "\nUnits SI";
		default:                         return " ";
	}
}

/**
Returns the tool tips for the columns.
@return the tool tips for the columns.
*/
public String[] getColumnToolTips()
{
	String[] tips = new String[_numberOfColumns];
	tips[COL_SHEF_TYPE_SHEF_PE]      = "SHEFType.SHEF_pe";
	tips[COL_SHEF_TYPE_DEFAULT_BASE] = "SHEFType.Default_base";
	tips[COL_SHEF_TYPE_DEFAULT_MULT] = "SHEFType.Default_mult";
	tips[COL_SHEF_TYPE_TIME_SCALE]   = "SHEFType.Time_scale";
	tips[COL_SHEF_TYPE_UNITS_ENGL]   = "SHEFType.Units_engl";
	tips[COL_SHEF_TYPE_UNITS_SI]     = "SHEFType.Units_si";	
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
	widths[COL_SHEF_TYPE_SHEF_PE]      = 8;
	widths[COL_SHEF_TYPE_DEFAULT_BASE] = 8;
	widths[COL_SHEF_TYPE_DEFAULT_MULT] = 8;
	widths[COL_SHEF_TYPE_TIME_SCALE]   = 8;
	widths[COL_SHEF_TYPE_UNITS_ENGL]   = 8;
	widths[COL_SHEF_TYPE_UNITS_SI]     = 8;
	return widths;
}

/**
Returns the format of the specified column.
@param column the column to return the format for.
@return the format of the specified column.
*/
public String getFormat( int column )
{
	switch (column ) {			
		case COL_SHEF_TYPE_SHEF_PE:	 return "%-20s";
		case COL_SHEF_TYPE_DEFAULT_BASE: return "%-20s";
		case COL_SHEF_TYPE_DEFAULT_MULT: return "%8d";
		case COL_SHEF_TYPE_TIME_SCALE:	 return "%-20s";
		case COL_SHEF_TYPE_UNITS_ENGL:	 return "%-20s";
		case COL_SHEF_TYPE_UNITS_SI:	 return "%-20s";
		default:			 return "%-20s";
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
	RiversideDB_SHEFType s = (RiversideDB_SHEFType)_data.get(row);

	switch (column) {
		case COL_SHEF_TYPE_SHEF_PE:
			return s.getSHEF_pe();
		case COL_SHEF_TYPE_DEFAULT_BASE:
			return s.getDefault_base();
		case COL_SHEF_TYPE_DEFAULT_MULT:
			return new Integer((int)s.getDefault_mult());
		case COL_SHEF_TYPE_TIME_SCALE:
			return s.getTime_scale();
		case COL_SHEF_TYPE_UNITS_ENGL:
			return s.getUnits_engl();
		case COL_SHEF_TYPE_UNITS_SI:
			return s.getUnits_si();
		default:
			return " ";
	}
}

/**
Creates and populates the _containedColumns array, which contains the numbers
of the columns shown by the current GUI.
*//*
protected void initialize()
{
	_numberOfColumns = 6;
	
	Vector v = new Vector();
	v.add(new Integer(COL_SHEF_TYPE_SHEF_PE));
	v.add(new Integer(COL_SHEF_TYPE_DEFAULT_BASE));
	v.add(new Integer(COL_SHEF_TYPE_DEFAULT_MULT));
	v.add(new Integer(COL_SHEF_TYPE_TIME_SCALE));
	v.add(new Integer(COL_SHEF_TYPE_UNITS_ENGL));
	v.add(new Integer(COL_SHEF_TYPE_UNITS_SI));
	
	int size = v.size();
	_containedColumns = new int[size];
	Integer I;
	for (int i = 0; i < size; i++) {
		I = ((Integer)v.elementAt(i));
		_containedColumns[i] = I.intValue();
	}
}*/

/**
Sets the table value at the specified cell ( row, column ).
@param value the value to put in the location.
@param row the row at which to set the value.
@param column the column at which to set the value.
*/
protected Object setTableValueAt( Object value, int row, int column )
{
	RiversideDB_SHEFType rs = (RiversideDB_SHEFType)_data.get(row);
	RiversideDB_SHEFType rs0 = (RiversideDB_SHEFType)rs.getOriginal();
	int i;
	String s;
	switch (column) {
		case COL_SHEF_TYPE_SHEF_PE:
			s = readString(value, 6);
			value = s;
			s = checkTableField(s, row);
			if (!equal(s, rs.getSHEF_pe())) {
				rs.setSHEF_pe(s);
				checkTableDirty(rs, rs0);
				valueChanged(row, column, value);
			}
			break;
		case COL_SHEF_TYPE_DEFAULT_BASE:
			s = readString(value, 10);
			value = s;
			if (!equal(s, rs.getDefault_base())) {
				rs.setDefault_base(s);
				checkTableDirty(rs, rs0);
				valueChanged(row, column, value);
			}
			break;
		case COL_SHEF_TYPE_DEFAULT_MULT:
			i = readInt(value);
			if (i != rs.getDefault_mult()) {
				rs.setDefault_mult(i);
				checkTableDirty(rs, rs0);
				valueChanged(row, column, value);
			}
			break;
		case COL_SHEF_TYPE_TIME_SCALE:
			s = readString(value, 10);
			value = s;
			if (!equal(s, rs.getTime_scale())) {
				rs.setTime_scale(s);
				checkTableDirty(rs, rs0);
				valueChanged(row, column, value);
			}
			break;
		case COL_SHEF_TYPE_UNITS_ENGL:
			s = readString(value, 10);
			value = s;
			if (!equal(s, rs.getUnits_engl())) {
				rs.setUnits_engl(s);
				checkTableDirty(rs, rs0);
				valueChanged(row, column, value);
			}
			break;
		case COL_SHEF_TYPE_UNITS_SI:
			s = readString(value, 10);
			value = s;
			if (!equal(s, rs.getUnits_si())) {
				rs.setUnits_si(s);
				checkTableDirty(rs, rs0);
				valueChanged(row, column, value);
			}
			break;
		default:
			break;
	}
	return value;
}

}