

//------------------------------------------------------------------------------
// RiversideDB_ReferenceTable_MeasTransProtocol_JFrame - Frame extending from
//    RiversideDB_ReferenceTable_Abstract_JFrame and containing the editor for
//    the RiversideDB table MeasTransProtocol.
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
import RTi.DMI.DMIDataObject;
import RTi.Util.Message.Message;

/**
Frame extending from RiversideDB_ReferenceTable_Abstract_JFrame and containing
the editor for the RiversideDB table MeasTransProtocol.
*/
public class RiversideDB_ReferenceTable_MeasTransProtocol_JFrame extends
	RiversideDB_ReferenceTable_Abstract_JFrame {

/**
Class name.
*/
public final String CLASS = "RiversideDB_ReferenceTable_MeasTransProtocol_JFrame";

/**
Constructor.
@param dmi the dmi to use.
@param windowManager the window manager to use.
@param type the kind of reference table to display.
@param editable whether the data is editable or not.
*/
public RiversideDB_ReferenceTable_MeasTransProtocol_JFrame(
		RiversideDB_DMI dmi,
		RiversideDB_WindowManager windowManager,
		boolean editable ) {

	// 1) The base class will initialize the instance members and add the
	//    application name for windows, if any, to the _title.
	super( dmi, windowManager, editable );

	// 2) Query out the data from the database
	readData();

	// 3) Update the _title by adding this table name to it.
	_title = _title + "Meas Trans Protocol";

	// 4) Build and display the GUI
	setupGUI();
}

/**
Responds when the add button is pressed.
*/
protected void addClicked()
{
// Creates a blank record object for the MeasTransProtocol table, marks it
	// as a new, and inserts it into the tabl.
	RiversideDB_MeasTransProtocol mtp = new RiversideDB_MeasTransProtocol();
	mtp.setOriginal   ( null );
	_worksheet.addRow (  mtp );

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

	RiversideDB_MeasTransProtocol mtp = (RiversideDB_MeasTransProtocol)
					ddo;
	RiversideDB_MeasTransProtocol mtp2 = (RiversideDB_MeasTransProtocol)
					mtp.getOriginal();
	mtp2.setOriginal(mtp2.cloneSelf());
	if (mtp.getProtocol().trim().equals(mtp2.getProtocol().trim())) {
		return null;
	}
	obs[0] = mtp;
	obs[1] = mtp2;

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

		eq = (	(RiversideDB_MeasTransProtocol) d1 ).equals(
			(RiversideDB_MeasTransProtocol) d2 );

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
	RiversideDB_MeasTransProtocol mtp  = (RiversideDB_MeasTransProtocol)
					ddo;
	RiversideDB_MeasTransProtocol mtp0 = (RiversideDB_MeasTransProtocol)
					mtp.getOriginal();
	RiversideDB_MeasTransProtocol mtpr =
		_dmi.readMeasTransProtocolForProtocol( mtp0.getProtocol() );

	if ( shouldDelete(
		mtp, mtp0, mtpr, "Protocol", mtp.getProtocol() ) ) {
		_dmi.deleteMeasTransProtocolForProtocol( mtp0.getProtocol() );
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
	_worksheet.addRow(new RiversideDB_MeasTransProtocol());

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
		_windowManager.WINDOW_REF_MEAS_TRANS_PROTOCOL );
}

/**
Reads the proper data to display in the table.
*/
protected void readData()
{
	String routine = CLASS + ".readData";

	try {
		_results = _dmi.readMeasTransProtocolList();
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
	setSize( 600, 600 );
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
			RiversideDB_ReferenceTable_MeasTransProtocol_TableModel (
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
Writes a record to the database.
@param recordNum the number of the record in the Vector to be written.
@param ddo the DMIDataObject storing the record to be written.
@param pairs Vector that ties the record to be written to a row in the
worksheet.
*/
protected void writeRecord( int recordNum, DMIDataObject ddo, Vector pairs )
throws Exception
{
	RiversideDB_MeasTransProtocol mtp  = (RiversideDB_MeasTransProtocol)
					ddo;
	RiversideDB_MeasTransProtocol mtp0 = (RiversideDB_MeasTransProtocol)
					mtp.getOriginal();
	RiversideDB_MeasTransProtocol mtpr =
		_dmi.readMeasTransProtocolForProtocol( mtp.getProtocol() );

	if ( shouldWrite(
		mtp, mtp0, mtpr, "Protocol", mtp.getProtocol() ) ) {
		_dmi.writeMeasTransProtocol( mtp );
	}
}

}
//------------------------------------------------------------------------------
