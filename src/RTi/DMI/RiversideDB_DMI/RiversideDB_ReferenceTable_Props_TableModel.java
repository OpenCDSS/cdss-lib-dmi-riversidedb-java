

//------------------------------------------------------------------------------
// RiversideDB_ReferenceTable_Props_TableModel - Table model used for
// 	displaying thw data editor for the reference table Props.
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
// 2005-01-05 Luiz Teixeira, RTi	Added the COL_PROPS_DBUSER_NUM
//				        ( previously commented out )
// 2005-01-05 Luiz Teixeira, RTi	Matched the window width in the JFrame
//					class with the Sum of the table Column
//					Widths here.
// 2005-01-12 Luiz Teixeira, RTi	Override isCellEditable() method to
//					prevent column Prop_num to be edited.
//------------------------------------------------------------------------------
package RTi.DMI.RiversideDB_DMI;

import java.util.Vector;

import RTi.Util.Message.Message;
import RTi.DMI.DMIUtil;

/**
Table model used for displaying thw data editor for the table Props
This class extends from RiversideDB_ReferenceTable_Abstract_Props.
*/
public class RiversideDB_ReferenceTable_Props_TableModel
	extends RiversideDB_ReferenceTable_Abstract_TableModel
{

/**
References to columns.
*/
public final static int
	COL_PROPS_PROP_NUM    = 0,
	COL_PROPS_VARIABLE    = 1,
	COL_PROPS_VAL         = 2,
	COL_PROPS_SEQ         = 3,
	COL_PROPS_DESCRIPTION = 4,
	COL_PROPS_DBUSER_NUM  = 5,
	NUMBER_OF_COLUMNS     = 6;

/**
Default constructor.
@param rdmi the RiversideDB_dmi to use.
@param results the results to show in the table.
@param editable whether the data is editable or not.
*/
public RiversideDB_ReferenceTable_Props_TableModel (
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
		RiversideDB_Props props =
			(RiversideDB_Props)_data.elementAt(i);
		props.setOriginal(props.cloneSelf());
		props.setDirty(false);
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
	RiversideDB_Props p;
	for ( int i = 0; i < _rows; i++ ) {
		if ( i != row ) {
			p = (RiversideDB_Props) _data.elementAt(i);
			if ( p.getVariable().trim().equals(s) ) {
				return checkTableField( "X" + s, row );
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
	RiversideDB_Props current =
		(RiversideDB_Props) currentObject;
	RiversideDB_Props original =
		(RiversideDB_Props) originalObject;

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
		case COL_PROPS_PROP_NUM:    return Integer.class;
		case COL_PROPS_VARIABLE:    return String .class;
		case COL_PROPS_VAL:         return String .class;
		case COL_PROPS_SEQ:         return Integer.class;
		case COL_PROPS_DESCRIPTION: return String .class;
		case COL_PROPS_DBUSER_NUM:  return Integer.class;
		default:		    return String .class;
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
		case COL_PROPS_PROP_NUM:    return "Prop Num";
		case COL_PROPS_VARIABLE:    return "Variable";
		case COL_PROPS_VAL:         return "Val";
		case COL_PROPS_SEQ:         return "Seq";
		case COL_PROPS_DESCRIPTION: return "Description";
		case COL_PROPS_DBUSER_NUM:  return "DBUser Num";
		default:                    return " ";
	}
}

/**
Returns the tool tips for the columns.
@return the tool tips for the columns.
*/
public String[] getColumnToolTips()
{
	String[] tips = new String[_numberOfColumns];                                                                               
	tips[COL_PROPS_PROP_NUM]    = "Props.Prop_Num";
	tips[COL_PROPS_VARIABLE]    = "Props.Variable";
	tips[COL_PROPS_VAL]         = "Props.Val";
	tips[COL_PROPS_SEQ]         = "Props.Seq";
	tips[COL_PROPS_DESCRIPTION] = "Props.Description";
	tips[COL_PROPS_DBUSER_NUM]  = "Props.User_Num";
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
	widths[COL_PROPS_PROP_NUM]    =  6;
	widths[COL_PROPS_VARIABLE]    = 30;
	widths[COL_PROPS_VAL]         = 15;
	widths[COL_PROPS_SEQ]         =  3;
	widths[COL_PROPS_DESCRIPTION] = 18;
	widths[COL_PROPS_DBUSER_NUM]  =  8;

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
		case COL_PROPS_PROP_NUM:    return   "%8d";
		case COL_PROPS_VARIABLE:    return "%-20s";
		case COL_PROPS_VAL:	    return "%-20s";
		case COL_PROPS_SEQ:	    return   "%8d";
		case COL_PROPS_DESCRIPTION: return "%-20s";
		case COL_PROPS_DBUSER_NUM:  return   "%8d";
		default:		    return "%-20s";
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
	RiversideDB_Props d = (RiversideDB_Props) _data.elementAt(row);
	
	switch (column) {	
		case COL_PROPS_PROP_NUM:    return new Integer((int)d.getProp_num());
		case COL_PROPS_VARIABLE:    return d.getVariable();
		case COL_PROPS_VAL:         return d.getVal();
		case COL_PROPS_SEQ:         return new Integer((int)d.getSeq());
		case COL_PROPS_DESCRIPTION: return d.getDescription();
		case COL_PROPS_DBUSER_NUM:  return new Integer((int)d.getDBUser_num());				
		default:                    return " ";
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
	// Colunm Prop_num is AutoNum. Should not be editable.
	if ( column == COL_PROPS_PROP_NUM ) {
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
	RiversideDB_Props t  = (RiversideDB_Props) _data.elementAt(row);
	RiversideDB_Props t0 = (RiversideDB_Props) t.getOriginal();
	
	String s;
	int i;
	switch (column) {

		case COL_PROPS_PROP_NUM:
			i = readInt(value);
			if (i != t.getProp_num()) {
				t.setProp_num(i);
				checkTableDirty(t, t0);
				valueChanged(row, column, value);
			}
			break;
		case COL_PROPS_VARIABLE:
			s = readString(value, 255);
			s = checkTableField(s, row);
			value = s;
			if (!equal(s, t.getVariable())) {
				t.setVariable(s);
				checkTableDirty(t, t0);
				valueChanged(row, column, value);
			}
			break;
		case COL_PROPS_VAL:
			s = readString(value, 50);
			value = s;
			if (!equal(s, t.getVal())) {
				t.setVal(s);
				checkTableDirty(t, t0);
				valueChanged(row, column, value);
			}
			break;
		case COL_PROPS_SEQ:
			i = readInt(value);
		
			if (i != t.getSeq()) {
				t.setSeq(i);
				checkTableDirty(t, t0);
				valueChanged(row, column, value);
			}
			break;
		case COL_PROPS_DESCRIPTION:
			s = readString(value, 50);
			value = s;
			if (!equal(s, t.getDescription())) {
				t.setDescription(s);
				checkTableDirty(t, t0);
				valueChanged(row, column, value);
			}
			break;
		case COL_PROPS_DBUSER_NUM:
			i = readInt(value);
			if (i != t.getDBUser_num()) {
				t.setDBUser_num(i);
				checkTableDirty(t, t0);
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