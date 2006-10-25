

//------------------------------------------------------------------------------
// RiversideDB_ReferenceTable_DataType_JFrame - Frame extending from
//    RiversideDB_ReferenceTable_Abstract_JFrame and containing the editor for
//    the RiversideDB table DataType.
//------------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
//------------------------------------------------------------------------------
// History:
//
// See previous history in RiversideDB_ReferenceTable_EditorBase_JFrame.java.
// 2004-12-13 Luiz Teixeira, RTi	Split this class from the (JTS) original
//					RiversideDB_ReferenceTable_JFrame.java
//					class, which is now the base class
//		             (RiversideDB_ReferenceTable_EditorBase_JFrame.java)
//					for all reference table editors.
// 2004-12-27 Luiz Teixeira, RTi	Changed class extension from
//					RiversideDB_ReferenceTable_EditorBase_
//					JFrame to RiversideDB_ReferenceTable_Abs
//					tract_JFrame to be consistent with the
//					TableModel classes.
// 2004-12-20 Luiz Teixeira, RTi 	Update the method addClicked(), now
//					calling the base method updateGUIState
//					to properly update the interface.
// 2005-01-05 Luiz Teixeira, RTi	Added the argument _dmi to the calls to
//						RiversideDB_..._TableModel
// 2005-01-05 Luiz Teixeira, RTi	Matched the window width with the Sum of
//					the table Column Widths in the TableModel
//					class.
//------------------------------------------------------------------------------
package RTi.DMI.RiversideDB_DMI;

import java.util.Vector;
import javax.swing.JPanel;
import RTi.DMI.DMIDataObject;
import RTi.Util.Message.Message;

/**
Frame extending from RiversideDB_ReferenceTable_Abstract_JFrame and containing
the editor for the RiversideDB table DataUnits.
*/
public class RiversideDB_ReferenceTable_DataType_JFrame extends
	RiversideDB_ReferenceTable_Abstract_JFrame {

/**
Class name.
*/
public final String CLASS = "RiversideDB_ReferenceTable_DataType_JFrame";

/**
Constructor.
@param dmi the dmi to use.
@param windowManager the window manager to use.
@param type the kind of reference table to display.
@param editable whether the data is editable or not.
*/
public RiversideDB_ReferenceTable_DataType_JFrame(
		RiversideDB_DMI dmi,
		RiversideDB_WindowManager windowManager,
		boolean editable ) {

	// 1) The base class will initialize the instance members and add the
	//    application name for windows, if any, to the _title.
	super( dmi, windowManager, editable );

	// 2) Query out the data from the database
	readData();

	// 3) Update the _title by adding this table name to it.
	_title = _title + "Data Type";

	// 4) Build and display the GUI
	setupGUI();
}

/**
Responds when the add button is pressed.
*/
protected void addClicked()
{
	// Creates a blank record object for the DataType table, marks it
	// as a new, and inserts it into the tabl.
	RiversideDB_DataType dt = new RiversideDB_DataType();
	dt.setOriginal   ( null );
	_worksheet.addRow(   dt );

	// Update GUI state.
	updateGUIState();
}

/**
Checks to see if the key value in a data object has changed.  The actual
key depends on the kind of table being displayed.  If there is
no backup copy of the original key record, then the record is new.  Otherwise
it pulls out the backup copy of the record and checks the key value.
@param ddo the record object to check.
@return null if the record is new (no data present in
DMIDataObject.getOriginal()), or if the key hasn't changed.
Otherwise, returns a 2-element array where the first element is the
object that was passed in and the second element is its backup copy.  The first
element is what is written to the database and the second element is the
record deleted from the database.
*/
protected DMIDataObject[] checkForKeyChange(DMIDataObject ddo)
{
	if (ddo.getOriginal() == null) {
		return null;
	}
	DMIDataObject[] obs = new DMIDataObject[2];

	RiversideDB_DataType dt = (RiversideDB_DataType)
					ddo;
	RiversideDB_DataType dt2 = (RiversideDB_DataType)
					dt.getOriginal();
	dt2.setOriginal(dt2.cloneSelf());
	if (dt.getDataType().trim().equals(dt2.getDataType().trim())) {
		return null;
	}
	obs[0] = dt;
	obs[1] = dt2;

	return obs;
}

/**
Checks to see if two data objects are equal.
@param d1 the first data object.
@param d2 the second data object.
@return true if they are equal, false if not.
*/
protected boolean dataObjectsEqual( DMIDataObject d1, DMIDataObject d2 )
{
	boolean eq = false;

	eq = (	(RiversideDB_DataType) d1 ).equals(
		(RiversideDB_DataType) d2 );

	return eq;
}

/**
Deletes a record from the database.  Does checks for whether the record may
have been changed by another user in the interim.
@param ddo the DMIDataObject representing the record to delete.
*/
protected void deleteRecord( DMIDataObject ddo )
throws Exception
{
	// Guaranteed that ddo.getOriginal() != null
	RiversideDB_DataType dt  = (RiversideDB_DataType)
					ddo;
	RiversideDB_DataType dt0 = (RiversideDB_DataType)
					dt.getOriginal();
	RiversideDB_DataType dtr =
		_dmi.readDataTypeForDataType( dt0.getDataType() );

	if ( shouldDelete(
		dt, dt0, dtr, "Data Type", dt.getDataType() ) ) {
		_dmi.deleteDataTypeForDataType( dt0.getDataType() );
	}
}

/**
Adds a new record to the worksheet with values read in from an import file.
@param values the values to be placed in the record.
@param fieldNums the visible fields into which to place the values.
@param classes the classes for the fields.
@return true if successful, false if not.
*/
protected boolean importNewRecord(
	Vector values, int[] fieldNums, Class[] classes )
{
	_worksheet.addRow(new RiversideDB_DataType());

	// Derived classes ( except Scenario ) should call super.importNewRecord
	// ( values, fieldNums, classes ) before returning.
	// Scenario should implement the alternative code here.
	return ( super.importNewRecord ( values, fieldNums, classes ) );
}

/**
Inform the RiversideDB_WindowManager object that this window is now closed.
*/
protected void informWindowManager()
{
	_windowManager.closeWindow(
		_windowManager.WINDOW_REF_DATA_TYPE );
}

/**
Reads the proper data to display in the table.
*/
protected void readData()
{
	String routine = CLASS + ".readData";

	try {
		_results = _dmi.readDataTypeList();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		_results = new Vector();
	}
}

/**
Sets the size of the GUI appropriately for the kind of table being displayed.
*/
protected void setSize()
{
	// Match the TableModel values. Width is +/- 10*tableColumnWidthsSum.
	setSize( 1000, 600 );
}

/**
Sets up the GUI.
*/
protected void setupGUI()
{
	String routine = CLASS + "setupGUI", mssg;
		
	// 1) Get the table model for this table.
	try {
		_model = new 
			RiversideDB_ReferenceTable_DataType_TableModel (
				_dmi, _results, _editable );
	} catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		mssg =  "Error creating the TableModel";
		Message.printWarning( 2, routine, mssg );
	}
	
	// 2) Run the base setUpGUI() method. It does all the actual work.
	super.setupGUI();
}

/**
Sets up the combo boxes for the tables in which field values can be selected
from a combo box.
*/
protected void setupComboBoxes()
{
	String routine = CLASS + ".setupComboBoxes";

	try {
		Vector v = _dmi.readDataDimensionList();
		int size = v.size();
		Vector values = new Vector();
		RiversideDB_DataDimension dd = null;
		for ( int i = 0; i < size; i++ ) {
			dd = (RiversideDB_DataDimension)v.elementAt(i);
			values.add(dd.getDimension  ().trim() + " - "
				 + dd.getDescription().trim());
		}
		_worksheet.setColumnJComboBoxValues(
			RiversideDB_ReferenceTable_DataType_TableModel.COL_DATA_TYPE_DIMENSION, values );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}

	try {
		Vector v = _dmi.readMeasTimeScaleList();
		int size = v.size();
		Vector values = new Vector();
		RiversideDB_MeasTimeScale mts = null;
		for ( int i = 0; i < size; i++ ) {
			mts = (RiversideDB_MeasTimeScale)v.elementAt(i);
			values.add(mts.getScale      ().trim() + " - "
				 + mts.getDescription().trim());
		}
		_worksheet.setColumnJComboBoxValues(
			RiversideDB_ReferenceTable_DataType_TableModel.COL_DATA_TYPE_MEAS_TIME_SCALE, values );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}

	try {
		Vector v = _dmi.readMeasLocTypeList();
		int size = v.size();
		Vector values = new Vector();
		RiversideDB_MeasLocType mlt = null;
		for ( int i = 0; i < size; i++ ) {
			mlt = (RiversideDB_MeasLocType)v.elementAt(i);
			values.add(mlt.getType()       .trim() + " - "
				 + mlt.getDescription().trim());
		}
		_worksheet.setColumnJComboBoxValues(
			RiversideDB_ReferenceTable_DataType_TableModel.COL_DATA_TYPE_MEAS_LOC_TYPE, values );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}

	try {
		Vector v = _dmi.readSHEFTypeList();
		int size = v.size();
		Vector values = new Vector();
		RiversideDB_SHEFType st = null;
		for ( int i = 0; i < size; i++ ) {
			st = (RiversideDB_SHEFType)v.elementAt(i);
			values.add(st.getSHEF_pe().trim());
		}
		values.add( "" );
		_worksheet.setColumnJComboBoxValues(
			RiversideDB_ReferenceTable_DataType_TableModel.COL_DATA_TYPE_SHEF_PE, values );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}
}

/**
Writes a record to the database.
@param recordNum the number of the record in the Vector to be written.
@param ddo the DMIDataObject storing the record to be written.
@param pairs Vector that ties the record to be written to a row in the
worksheet.
*/
protected void writeRecord( int recordNum, DMIDataObject ddo, Vector pairs )
throws Exception
{
	RiversideDB_DataType dt  = (RiversideDB_DataType)
					ddo;
	RiversideDB_DataType dt0 = (RiversideDB_DataType)
					dt.getOriginal();
	RiversideDB_DataType dtr =
		_dmi.readDataTypeForDataType( dt.getDataType() );

	if ( shouldWrite(
		dt, dt0, dtr, "Data Type", dt.getDataType() ) ) {
		_dmi.writeDataType( dt );
	}
}

}
//------------------------------------------------------------------------------





























































