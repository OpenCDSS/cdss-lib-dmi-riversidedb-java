

//------------------------------------------------------------------------------
// RiversideDB_ReferenceTable_MeasReductionType_TableModel - Table model used for
// 	displaying thw data editor for the reference table MeasReductionType.
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
Table model used for displaying the data editor for the table MeasReductionType.
This class extends from RiversideDB_ReferenceTable_Abstract_TableModel.
*/
public class RiversideDB_ReferenceTable_MeasReductionType_TableModel
	extends RiversideDB_ReferenceTable_Abstract_TableModel
{

/**
References to columns.
*/
public final static int
	COL_MEAS_REDUCTION_TYPE_TYPE        = 0,
	COL_MEAS_REDUCTION_TYPE_DESCRIPTION = 1,
	NUMBER_OF_COLUMNS                   = 2;

/**
Default constructor.
@param rdmi the RiversideDB_dmi to use.
@param results the results to show in the table.
@param editable whether the data is editable or not.
*/
public RiversideDB_ReferenceTable_MeasReductionType_TableModel (
	RiversideDB_DMI rdmi, List results, boolean editable )
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
		RiversideDB_MeasReductionType m3 = (RiversideDB_MeasReductionType)_data.get(i);
		m3.setOriginal(m3.cloneSelf());
		m3.setDirty(false);
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
	RiversideDB_MeasReductionType d;
	for (int i = 0; i < _rows; i++) {
		if (i != row) {
			d = (RiversideDB_MeasReductionType)_data.get(i);
			if (d.getType().trim().equals(s)) {
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
	RiversideDB_MeasReductionType current =
		(RiversideDB_MeasReductionType) currentObject;
	RiversideDB_MeasReductionType original =
		(RiversideDB_MeasReductionType) originalObject;

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
		case COL_MEAS_REDUCTION_TYPE_TYPE:	 return String.class;
		case COL_MEAS_REDUCTION_TYPE_DESCRIPTION:return String.class;
		default:				 return String.class;
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
		case COL_MEAS_REDUCTION_TYPE_TYPE:       return "Type";
		case COL_MEAS_REDUCTION_TYPE_DESCRIPTION:return "Description";
		default:                                 return " ";
	}
}

/**
Returns the tool tips for the columns.
@return the tool tips for the columns.
*/
public String[] getColumnToolTips()
{
	String[] tips = new String[_numberOfColumns];
        tips[COL_MEAS_REDUCTION_TYPE_TYPE]        = "MeasReductionType.Type";
	tips[COL_MEAS_REDUCTION_TYPE_DESCRIPTION] = "MeasReductionType.Description";                                      
    
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
	widths[COL_MEAS_REDUCTION_TYPE_TYPE] =		12;
	widths[COL_MEAS_REDUCTION_TYPE_DESCRIPTION] =	48;

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
		case COL_MEAS_REDUCTION_TYPE_TYPE:	 return "%-20s";
		case COL_MEAS_REDUCTION_TYPE_DESCRIPTION:return "%-20s";
		default:				 return "%-20s";
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
	RiversideDB_MeasReductionType m = (RiversideDB_MeasReductionType)_data.get(row);
	switch (column) {
		case COL_MEAS_REDUCTION_TYPE_TYPE:
			return m.getType();
		case COL_MEAS_REDUCTION_TYPE_DESCRIPTION:
			return m.getDescription();
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
	RiversideDB_MeasReductionType m = (RiversideDB_MeasReductionType)_data.get(row);
	RiversideDB_MeasReductionType m0 = (RiversideDB_MeasReductionType)m.getOriginal();
	String s;
	switch (column) {
		case COL_MEAS_REDUCTION_TYPE_TYPE:
			s = readString(value, 20);
			value = s;
			s = checkTableField(s, row);
			if (!equal(s, m.getType())) {
				m.setType(s);
				checkTableDirty(m, m0);
				valueChanged(row, column, value);
			}
			break;
		case COL_MEAS_REDUCTION_TYPE_DESCRIPTION:
			s = readString(value, 255);
			value = s;
			if (!equal(s, m.getDescription())) {
				m.setDescription(s);
				checkTableDirty(m, m0);
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