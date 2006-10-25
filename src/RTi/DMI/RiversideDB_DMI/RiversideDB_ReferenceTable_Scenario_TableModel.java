

//------------------------------------------------------------------------------
// RiversideDB_ReferenceTable_Scenario_TableModel - Table model used for
// 	displaying thw data editor for the reference table Scenario.
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
// 2005-01-10 Luiz Teixeira, RTi	Upgraded to either use the pre-03.00.00
//					Active field(setActive() and getActive()
//					methods) or the 03.00.00 IsActive field
//					(setIsActive() and getIsActive() method)
//------------------------------------------------------------------------------
package RTi.DMI.RiversideDB_DMI;

import java.util.Vector;

import RTi.Util.Message.Message;
import RTi.DMI.DMIUtil;

/**
Table model used for displaying thw data editor for the table Scenario.
This class extends from RiversideDB_ReferenceTable_Abstract_TableModel.
*/
public class RiversideDB_ReferenceTable_Scenario_TableModel
	extends RiversideDB_ReferenceTable_Abstract_TableModel
{

/**
References to columns.
*/
public final static int
	COL_SCENARIO_SCENARIO_NUM = 0,
	COL_SCENARIO_ACTIVE       = 1,
	COL_SCENARIO_DESCRIPTION  = 2,
	COL_SCENARIO_IDENTIFIER   = 3,
	NUMBER_OF_COLUMNS         = 4;

/**
Default constructor.
@param rdmi the RiversideDB_dmi to use.
@param results the results to show in the table.
@param editable whether the data is editable or not.
*/
public RiversideDB_ReferenceTable_Scenario_TableModel (
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
		RiversideDB_Scenario s1 =
			(RiversideDB_Scenario)_data.elementAt(i);
		s1.setOriginal(s1.cloneSelf());
		s1.setDirty(false);
	}
}

/**
Checks if the key field in the table already has a certain value, and if so,
generates a unique value.
@param integerKey the integer to check for in the key field.
@param row the row in which the given value is to be placed.
@return a unique integer (might be the one passed in) that can validly be
placed in the key field.
*/
protected int checkTableField( int integerKey, int row )
{
	RiversideDB_Scenario d;
	for (int i = 0; i < _rows; i++) {
		if (i != row) {
			d = (RiversideDB_Scenario)_data.elementAt(i);
			if ( d.getScenario_num() == integerKey ) {
				return checkTableField( integerKey + 1, row );
			}
		}
	}
	return integerKey;
}

/**
Checks if the table object is dirty.
@param currentObject the current RiversideDB_<TableName> object
@param originalObject the original RiversideDB_<TableName> object
@return true if the object has changed, false otherwise.
*/
protected void checkTableDirty( Object currentObject, Object originalObject )
{
	RiversideDB_Scenario current =
		(RiversideDB_Scenario) currentObject;
	RiversideDB_Scenario original =
		(RiversideDB_Scenario) originalObject;

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
		case COL_SCENARIO_SCENARIO_NUM:	return Integer.class;
		case COL_SCENARIO_ACTIVE:	return Integer.class;
		case COL_SCENARIO_DESCRIPTION:	return String.class;
		case COL_SCENARIO_IDENTIFIER:	return String.class;
		default:			return String.class;
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
		case COL_SCENARIO_SCENARIO_NUM: return "Scenario\nNum";
		case COL_SCENARIO_ACTIVE:       return "\nActive";
		case COL_SCENARIO_DESCRIPTION:  return "\nDescription";
		case COL_SCENARIO_IDENTIFIER:   return "\nIdentifier";
		default:                        return " ";
	}
}

/**
Returns the tool tips for the columns.
@return the tool tips for the columns.
*/
public String[] getColumnToolTips()
{
	String[] tips = new String[_numberOfColumns];
	tips[COL_SCENARIO_SCENARIO_NUM] = "Scenario.Scenario_num";
	tips[COL_SCENARIO_ACTIVE]       = "Scenario.Active";
	tips[COL_SCENARIO_DESCRIPTION]  = "Scenario.Description";
	tips[COL_SCENARIO_IDENTIFIER]   = "Scenario.Identifier";
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
	widths[COL_SCENARIO_SCENARIO_NUM] =  6;
	widths[COL_SCENARIO_ACTIVE]       =  4;
	widths[COL_SCENARIO_DESCRIPTION]  = 30;
	widths[COL_SCENARIO_IDENTIFIER]   = 10;
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
		case COL_SCENARIO_SCENARIO_NUM:	return "%8d";
		case COL_SCENARIO_ACTIVE:	return "%8d";
		case COL_SCENARIO_DESCRIPTION:	return "%-20s";
		case COL_SCENARIO_IDENTIFIER:	return "%-20s";
		default:			return "%-20s";
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
	RiversideDB_Scenario s = (RiversideDB_Scenario)_data.elementAt(row);

	switch (column) {
		case COL_SCENARIO_SCENARIO_NUM:
			return new Integer((int)s.getScenario_num());
		case COL_SCENARIO_ACTIVE:
			if ( _rdmi.isDatabaseVersionAtLeast(_rdmi._VERSION_030000_20041001) ) {
 				return new Integer(s.getIsActive());
			} else {
				return new Integer(s.getActive());
			}
		case COL_SCENARIO_DESCRIPTION:
			return s.getDescription();
		case COL_SCENARIO_IDENTIFIER:
			return s.getIdentifier();
		default:
			return " ";
	}
}

/**
Returns whether the cell at the given position is editable or not.  In this
table model all columns above #2 are editable.
@param rowIndex unused
@param column the index of the column to check for whether it is editable.
@return whether the cell at the given position is editable.
This method may be implemented in some of the derived class
   (i.e. RiversideDB_ReferenceTable_Scenario_TableModel.java)
*/
public boolean isCellEditable(int rowIndex, int column)
{
	// Colunm Scenario_num is AutoNum. Should not be editable.
	if ( column == COL_SCENARIO_SCENARIO_NUM ) {
		return false;
	}
	
	// The others are editable if the table is editable.
	if ( _editable ) return true;
	return false;
}

/**
Sets the table value at the specified cell ( row, column ).
@param value the value to put in the location.
@param row the row at which to set the value.
@param column the column at which to set the value.
*/
protected Object setTableValueAt( Object value, int row, int column )
{
	RiversideDB_Scenario rs =
		(RiversideDB_Scenario)_data.elementAt(row);
	RiversideDB_Scenario rs0 =
		(RiversideDB_Scenario)rs.getOriginal();
	int i;
	String s;
	switch (column) {
		case COL_SCENARIO_SCENARIO_NUM:
			i = readInt(value);
			i = checkTableField(i, row);
			if (i != rs.getScenario_num()) {
				rs.setScenario_num(i);
				checkTableDirty(rs, rs0);
				valueChanged(row, column, value);
			}
			break;
		case COL_SCENARIO_ACTIVE:
			i = readInt(value);
			if ( _rdmi.isDatabaseVersionAtLeast(_rdmi._VERSION_030000_20041001) ) {
 				if (i != rs.getIsActive()) {
					rs.setIsActive(i);
					checkTableDirty(rs, rs0);
					valueChanged(row, column, value);
				}
			} else {
				if (i != rs.getActive()) {
					rs.setActive(i);
					checkTableDirty(rs, rs0);
					valueChanged(row, column, value);
				}
			}
			break;
		case COL_SCENARIO_DESCRIPTION:
			s = readString(value, 255);
			value = s;
			if (!equal(s, rs.getDescription())) {
				rs.setDescription(s);
				checkTableDirty(rs, rs0);
				valueChanged(row, column, value);
			}
			break;
		case COL_SCENARIO_IDENTIFIER:
			s = readString(value, 50);
			value = s;
			if (!equal(s, rs.getIdentifier())) {
				rs.setIdentifier(s);
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
//------------------------------------------------------------------------------