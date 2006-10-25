

//------------------------------------------------------------------------------
// RiversideDB_ReferenceTable_MessageLog_TableModel - Table model used for
// 	displaying thw data editor for the reference table MessageLog.
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

import java.util.Date;
import java.util.Vector;

import RTi.Util.Message.Message;
import RTi.Util.Time.DateTime;
import RTi.DMI.DMIUtil;

/**
Table model used for displaying thw data editor for the table MessageLog.
This class extends from RiversideDB_ReferenceTable_Abstract_TableModel.
*/
public class RiversideDB_ReferenceTable_MessageLog_TableModel
	extends RiversideDB_ReferenceTable_Abstract_TableModel
{

/**
References to columns.
*/
public final static int
	COL_MESSAGE_LOG_MESSAGE_NUM   = 0,
	COL_MESSAGE_LOG_DATE_TIME     = 1,
	COL_MESSAGE_LOG_MESSAGE       = 2,
	COL_MESSAGE_LOG_MESSAGE_LEVEL = 3,
	COL_MESSAGE_LOG_MESSAGE_TYPE  = 4,
	COL_MESSAGE_LOG_ROUTINE       = 5,
	NUMBER_OF_COLUMNS             = 6;

/**
Default constructor.
@param rdmi the RiversideDB_dmi to use.
@param results the results to show in the table.
@param editable whether the data is editable or not.
*/
public RiversideDB_ReferenceTable_MessageLog_TableModel (
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
		RiversideDB_MessageLog m6 =
			(RiversideDB_MessageLog)_data.elementAt(i);
		m6.setOriginal(m6.cloneSelf());
		m6.setDirty(false);
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
	RiversideDB_MessageLog d;
	for (int i = 0; i < _rows; i++) {
		if (i != row) {
			d = (RiversideDB_MessageLog)_data.elementAt(i);
			if ( d.getMessage_num() == integerKey ) {
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
	RiversideDB_MessageLog current =
		(RiversideDB_MessageLog) currentObject;
	RiversideDB_MessageLog original =
		(RiversideDB_MessageLog) originalObject;

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
		case COL_MESSAGE_LOG_MESSAGE_NUM:   return Integer.class;
		case COL_MESSAGE_LOG_DATE_TIME:	    return String.class;
		case COL_MESSAGE_LOG_MESSAGE:	    return String.class;
		case COL_MESSAGE_LOG_MESSAGE_LEVEL: return Integer.class;
		case COL_MESSAGE_LOG_MESSAGE_TYPE:  return String.class;
		case COL_MESSAGE_LOG_ROUTINE:	    return String.class;
		default:			    return String.class;
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
		case COL_MESSAGE_LOG_MESSAGE_NUM:   return "Message\nNum";
		case COL_MESSAGE_LOG_DATE_TIME:     return "\nDate Time";
		case COL_MESSAGE_LOG_MESSAGE:       return "\nMessage";
		case COL_MESSAGE_LOG_MESSAGE_LEVEL: return "Message\nLevel";
		case COL_MESSAGE_LOG_MESSAGE_TYPE:  return "\nMessage Type";
		case COL_MESSAGE_LOG_ROUTINE:       return "\nRoutine";
		default:                            return " ";
	}
}

/**
Returns the tool tips for the columns.
@return the tool tips for the columns.
*/
public String[] getColumnToolTips()
{
	String[] tips = new String[_numberOfColumns];                                                                                  
	tips[COL_MESSAGE_LOG_MESSAGE_NUM]   = "MessageLog.Message_num";
	tips[COL_MESSAGE_LOG_DATE_TIME]     = "MessageLog.Date_Time";
	tips[COL_MESSAGE_LOG_MESSAGE]       = "MessageLog.Message";
	tips[COL_MESSAGE_LOG_MESSAGE_LEVEL] = "MessageLog.Message_Level";
	tips[COL_MESSAGE_LOG_MESSAGE_TYPE]  = "MessageLog.Message_Type";
	tips[COL_MESSAGE_LOG_ROUTINE]       = "MessageLog.Routine";	
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
	widths[COL_MESSAGE_LOG_MESSAGE_NUM]   =  5;
	widths[COL_MESSAGE_LOG_DATE_TIME]     =	10;
	widths[COL_MESSAGE_LOG_MESSAGE]       =	30;
	widths[COL_MESSAGE_LOG_MESSAGE_LEVEL] =  5;
	widths[COL_MESSAGE_LOG_MESSAGE_TYPE]  =	15;
	widths[COL_MESSAGE_LOG_ROUTINE]       =	15;
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
		case COL_MESSAGE_LOG_MESSAGE_NUM:   return "%8d";
		case COL_MESSAGE_LOG_DATE_TIME:	    return "%-20s";
		case COL_MESSAGE_LOG_MESSAGE:	    return "%-20s";
		case COL_MESSAGE_LOG_MESSAGE_LEVEL: return "%8d";
		case COL_MESSAGE_LOG_MESSAGE_TYPE:  return "%-20s";
		case COL_MESSAGE_LOG_ROUTINE:	    return "%-20s";
		default:			    return "%-20s";
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
	RiversideDB_MessageLog m =
		(RiversideDB_MessageLog)_data.elementAt(row);
		
	switch (column) {
		case COL_MESSAGE_LOG_MESSAGE_NUM:
			return new Integer((int)m.getMessage_num());
		case COL_MESSAGE_LOG_DATE_TIME:
			return new DateTime(m.getDate_Time()).toString(
				DateTime.FORMAT_YYYY_MM_DD_HH_mm);
		case COL_MESSAGE_LOG_MESSAGE:
			return m.getMessage();
		case COL_MESSAGE_LOG_MESSAGE_LEVEL:
			return new Integer((int)m.getMessage_Level());
		case COL_MESSAGE_LOG_MESSAGE_TYPE:
			return m.getMessage_Type();
		case COL_MESSAGE_LOG_ROUTINE:
			return m.getRoutine();
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
	RiversideDB_MessageLog m =
		(RiversideDB_MessageLog)_data.elementAt(row);
	RiversideDB_MessageLog m0 =
		(RiversideDB_MessageLog)m.getOriginal();
	String s;
	int i;
	Date d;
	switch (column) {
		case COL_MESSAGE_LOG_MESSAGE_NUM:
			i = readInt(value);
			i = checkTableField(i, row);
			if (i != m.getMessage_num()) {
				m.setMessage_num(i);
				checkTableDirty(m, m0);
				valueChanged(row, column, value);
			}
			break;
		case COL_MESSAGE_LOG_DATE_TIME:
			s = readString(value, 20);
			value = s;
			try {
				DateTime dt = DateTime.parse(s);
				if (!dt.getDate().equals(m.getDate_Time())) {
					m.setDate_Time(dt.getDate());
					checkTableDirty(m, m0);
					valueChanged(row, column, value);
				}
			}
			catch (Exception e) {
				m.setDate_Time(DMIUtil.MISSING_DATE);
			}
			break;
		case COL_MESSAGE_LOG_MESSAGE:
			s = readString(value, 255);
			value = s;
			if (!equal(s, m.getMessage())) {
				m.setMessage(s);
				checkTableDirty(m, m0);
				valueChanged(row, column, value);
			}
			break;
		case COL_MESSAGE_LOG_MESSAGE_LEVEL:
			i = readInt(value);
			if (i != m.getMessage_Level()) {
				m.setMessage_Level(i);
				checkTableDirty(m, m0);
				valueChanged(row, column, value);
			}
			break;
		case COL_MESSAGE_LOG_MESSAGE_TYPE:
			s = readString(value, 50);
			value = s;
			if (!equal(s, m.getMessage_Type())) {
				m.setMessage_Type(s);
				checkTableDirty(m, m0);
				valueChanged(row, column, value);
			}
			break;
		case COL_MESSAGE_LOG_ROUTINE:
			s = readString(value, 100);
			value = s;
			if (!equal(s, m.getRoutine())) {
				m.setRoutine(s);
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