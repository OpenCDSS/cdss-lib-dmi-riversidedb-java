
//------------------------------------------------------------------------------
// RiversideDB_ReferenceTable_Abstract_JFrame - Base class for all Reference
//                                                Table Editors.
//------------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
//------------------------------------------------------------------------------
// History:
// 2004-01-26	J. Thomas Sapienza, RTi	Initial version.
// 2004-02-02	JTS, RTi		Did work on checking each kind of
//					database write to make sure that if
//					users overwrite/delete data that has
//					changed in the database, it is handled
//					gracefully and they are notified.
// 2004-02-03	JTS, RTi		Added import code.
// 2004-02-05	JTS, RTi		* Table model moved into
//					  RTi.DMI.RiversideDB_DMI.
//					* Removed reference to parent JFrame
//					  as it was not being used.
// 					* Added window manager code.
// 2004-12-02 Luiz Teixeira, RTi	Update the construction of the title
//					now adding the application name for
//					windows only if the name is defined in
//					the JGUIUtil class.
// 2004-12-13 Luiz Teixeira		Split the JTS original RiversideDB_Reren
//					        ceTable_JFrame class into:
//					RiversideDB_ReferenceTable_EditorBase_
//					        JFrame and
//					RiversideDB_ReferenceTable_*_JFrame
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
// 2004-12-15 Luiz Teixeira		Added the Props Table to the list of
//					tables available for display.
//					The derived class processing this table
//					will be :
//					RiversideDB_ReferenceTable_Props_JFrame
// 2004-12-16 Luiz Teixeira, RTi 	Replaced all references to the (JTS)
//					RiversideDB_ReferenceTable_TableModel
//					to references to the new (base/derived)
//				RiversideDB_ReferenceTable_Abstract_TableModel.
// 2004-12-27 Luiz Teixeira, RTi	Change name from
//					RiversideDB_ReferenceTable_EditorBase_
//					JFrame to RiversideDB_ReferenceTable_Abs
//					tract_JFrame to be consistent with the
//					TableModel classes (see previous entry)
// 2004-12-20 Luiz Teixeira, RTi 	Remove the table constants useless now
//					that the class
//					RiversideDB_ReferenceTable_TableModel
//					was split into base and derived classes
//					i.e. RiversideDB_ReferenceTable_Abstrat_
//					TableModel and RiversideDB_Reference
//					Table_????_TableModel.
//					Removed call to trimColumns also because
//					with the new base/derived approach the
//					table model contains only the columns of
//					the table we are dealing with,
// 2004-12-20 Luiz Teixeira, RTi 	Added the method updateGUIState().
// 2004-12-22 Luiz Teixeira, RTi	Made isDataDirt method public to be used
//					possibly by the WindowManager.
// 2005-01-10 Luiz Teixeira, RTi	Update the message information in the
//					method saveClicked to better inform the
//					user about the errors during the delete
//					and write processes.
//------------------------------------------------------------------------------
package RTi.DMI.RiversideDB_DMI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import RTi.DMI.DMIDataObject;
import RTi.Util.GUI.JFileChooserFactory;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.JScrollWorksheet;
import RTi.Util.GUI.JWorksheet;
import RTi.Util.GUI.JWorksheet_CellAttributes;
import RTi.Util.GUI.JWorksheet_TableModelListener;
import RTi.Util.GUI.ResponseJDialog;
import RTi.Util.GUI.SimpleFileFilter;
import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;
import RTi.Util.String.StringUtil;

/**
Base class for the JFrame in which the table containing reference table data is
displayed.
*/
abstract public class RiversideDB_ReferenceTable_Abstract_JFrame extends JFrame
	implements ActionListener,
		   JWorksheet_TableModelListener,
		   MouseListener,
		   WindowListener {

/**
Class name.
*/
public final String CLASS = "RiversideDB_ReferenceTable_Abstract_JFrame";

/**
Button labels.
*/
protected final String 	_BUTTON_ADD    = "Add Record",
			_BUTTON_CLOSE  = "Close",
			_BUTTON_CANCEL = "Cancel",
			_BUTTON_IMPORT = "Import Data",
			_BUTTON_DELETE = "Delete Record(s)",
			_BUTTON_SAVE   = "Save";

/**
Whether the data in the reference table is editable or not.
*/
protected boolean _editable = false;

/**
GUI buttons.
*/
protected JButton _addButton,
		  _cancelButton,
		  _closeButton,
		  _deleteButton,
		  _importButton,
		  _saveButton;

/**
The worksheet displayed in the gui.
*/
protected JWorksheet _worksheet = null;

/**
The cell attributes used for uneditable columns.
*/
protected JWorksheet_CellAttributes _ca = null;

/**
The dmi used to query out database data.
*/
protected RiversideDB_DMI _dmi = null;

/**
The table model used for the table.
*/
protected RiversideDB_ReferenceTable_Abstract_TableModel _model = null;

/**
The window manager being used for the application.
*/
protected RiversideDB_WindowManager _windowManager = null;

/**
A Vector of records that were deleted from the GUI and which may need deleted
from the database.
*/
protected Vector _deletedRecords = null;

/**
The results of the query that will be displayed in the table.
*/
protected Vector _results = null;

/*
Window title.
*/
protected String _title = "";

/**
Constructor.
@param dmi the dmi to use.
@param windowManager the window manager to use.
@param editable whether the data is editable or not.
*/
public RiversideDB_ReferenceTable_Abstract_JFrame(
		RiversideDB_DMI dmi,
		RiversideDB_WindowManager windowManager,
		boolean editable )
{
	super ("XXX");

	// 1) Initialize the instance members
	_dmi            = dmi;
	_windowManager  = windowManager;
	_editable       = editable;
	_title          = "";
	_deletedRecords = new Vector();

	// 2) Add the Application name for windows, if any.
	String app_title = JGUIUtil.getAppNameForWindows();
	if(  app_title != null &&  app_title.length() > 0 ) {
		_title = _title + app_title + " - ";
	}

	// 3) The derived class must still run the readData() method, update
	//    the title to include the table name and run the derived setupGUI()
	//    method which instantiates the proper
	//    RiversideDB_ReferenceTable_?_TableModel and call back this class
	//    setupGUI().
}

/**
Responds to GUI actions.
@param event the ActionEvent that happened.
*/
public void actionPerformed(ActionEvent event)
{
	String command = event.getActionCommand();

	// if any editing is going on, try to save the changes.  If that
	// can't happen, cancel them

	if (!_worksheet.stopEditing()) {
		_worksheet.cancelEditing();
	}

	if      ( command.equals ( _BUTTON_ADD    ) ) {
		addClicked();
		setButtonsChanges();
	}
	else if ( command.equals ( _BUTTON_CANCEL ) ) {
		cancelClicked();
	}
	else if ( command.equals ( _BUTTON_CLOSE  ) ) {
		closeClicked();
	}
	else if ( command.equals ( _BUTTON_DELETE ) ) {
		deleteClicked();
		setButtonsNoRowSelected();
		checkRecordsForChanges();
	}
	else if ( command.equals ( _BUTTON_IMPORT ) ) {
		importClicked();
	}
	else if ( command.equals ( _BUTTON_SAVE   ) ) {
		saveClicked();
		setButtonsNoChanges();
	}
}

/**
Responds when the add button is pressed.
The derived classes must implement this method and call the "updateGUIState" as
the last command to update the GUI.
*/
abstract protected void addClicked();

/**
Update the state of the GUI..
*/
protected void updateGUIState()
{
	_worksheet.scrollToLastRow();
	_worksheet.selectLastRow  ();
	setButtonsRowSelected     ();
}

/**
Responds to the cancel button being pressed.  Closes the window without
saving.
*/
protected void cancelClicked()
{
	closeWindow();
}

/**
Check if any data was changed, if so allow the user to make a decision to save
it or not.
*/
protected void checkDataChanges()
{
	if ( isDataDirty() ) {
		int x = new ResponseJDialog(this, "Data have changed!",
			"The GUI data have been changed.  Save changes?",
			ResponseJDialog.YES | ResponseJDialog.NO).response();
		if ( x == ResponseJDialog.YES ) {
			saveClicked();
		}
	}
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
	DMIDataObject[] obs = new DMIDataObject[2];
	return obs;
}

/**
Checks through all the records to see if any are new or dirty, and then
enables or disables the buttons appropriately.
*/
protected void checkRecordsForChanges()
{
	int rows = _worksheet.getRowCount();
	DMIDataObject ddo = null;
	for (int i = 0; i < rows; i++) {
		ddo = (DMIDataObject)_worksheet.getRowData(i);
		if (ddo.getOriginal() == null || ddo.isDirty()) {
			setButtonsChanges();
			return;
		}
	}

	int size = _deletedRecords.size();
	for (int i = 0; i < size; i++) {
		ddo = (DMIDataObject)_deletedRecords.elementAt(i);
		if (ddo.getOriginal () != null) {
			setButtonsChanges();
			return;
		}
	}

	setButtonsNoChanges();
}

/**
Responds when the close button is pressed.  Saves any changes and closes the
window.
*/
protected void closeClicked()
{
	checkDataChanges();
	closeWindow     ();
}

/**
Closes the GUI.
*/
protected void closeWindow()
{
	informWindowManager();
}

/**
Checks to see if two data objects are equal.
@param d1 the first data object.
@param d2 the second data object.
@return true if they are equal, false if not.
*/
abstract protected boolean dataObjectsEqual( DMIDataObject d1, DMIDataObject d2 );

/**
Responds when the delete button is pressed.  Deletes all the selected rows and
stores them in the _deletedRecords array.
*/
protected void deleteClicked()
{
	int[] rows = _worksheet.getSelectedRows();

	Vector v = _worksheet.getRowData(rows);
	_worksheet.deleteRows(rows);

	for (int i = 0; i < v.size(); i++) {
		_deletedRecords.add(v.elementAt(i));
	}
}

/**
Deletes a record from the database.  Does checks for whether the record may
have been changed by another user in the interim.
@param ddo the DMIDataObject representing the record to delete.
*/
abstract protected void deleteRecord(DMIDataObject ddo)
throws Exception;

/**
Responds when the import data button is clicked.  Opens a file chooser for
choosing the file from which to import.
*/
protected void importClicked()
{
	JGUIUtil.setWaitCursor(this, true);

	String directory = JGUIUtil.getLastFileDialogDirectory();

	JFileChooser jfc = null;
	if (directory == null) {
		jfc = JFileChooserFactory.createJFileChooser();
	}
	else {
		jfc = JFileChooserFactory.createJFileChooser(directory);
	}
	jfc.setDialogTitle("Select Input File");
	SimpleFileFilter csv = new SimpleFileFilter("csv",
		"Comma-delimited files");
	SimpleFileFilter csvf = new SimpleFileFilter("csv",
		"Comma-delimited files with field names");
	jfc.addChoosableFileFilter(csv);
	jfc.addChoosableFileFilter(csvf);
	jfc.setAcceptAllFileFilterUsed(false);
	jfc.setFileFilter(csv);
	jfc.setDialogType(JFileChooser.OPEN_DIALOG);

	JGUIUtil.setWaitCursor(this, false);
	int retVal = jfc.showOpenDialog(this);
	if (retVal != JFileChooser.APPROVE_OPTION) {
		return;
	}

	String currDir = (jfc.getCurrentDirectory()).toString();

	if (!currDir.equalsIgnoreCase(directory)) {
		JGUIUtil.setLastFileDialogDirectory(currDir);
	}
	String filename = jfc.getSelectedFile().getName();

	SimpleFileFilter ff = (SimpleFileFilter)jfc.getFileFilter();
	if (ff == csv) {
		importFile(currDir + File.separator + filename, false);
	}
	else {
		importFile(currDir + File.separator + filename, true);
	}
}

/**
Inform the RiversideDB_WindowManager object that this window is now closed.
*/
abstract protected void informWindowManager();

/**
Imports the data in a file into the current worksheet.
@param filename the file to read data from.
@param fieldNames if true, then the first line of the file contains the
names of the fields for which data is contained in the file.
*/
protected void importFile(String filename, boolean fieldNames) {
	String routine = CLASS + ".importFile";

	BufferedReader reader = null;
	try {
		reader = new BufferedReader(new FileReader(filename));
	}
	catch (Exception e) {
		e.printStackTrace();
		Message.printWarning(2, routine, e);
		Message.printWarning(1, routine, "Error trying read from "
			+ "file.");
		return;
	}

	String line = null;
	int[] fieldNums = null;

	if (fieldNames) {
		try {
			line = reader.readLine();
		}
		catch (Exception e) {
			e.printStackTrace();
			Message.printWarning(2, routine, e);
			Message.printWarning(1, routine, "Error reading from "
				+ "file.");
			return;
		}

		if (line == null) {
			Message.printWarning(1, routine, "File contains no "
				+ "data.");
			return;
		}

		Vector fields = StringUtil.breakStringList(line, ",",
			StringUtil.DELIM_ALLOW_STRINGS);
		fieldNums = new int[fields.size()];
		for (int i = 0; i < fields.size(); i++) {
			fieldNums[i] = _model.getFieldColumnNumber(
				(String)fields.elementAt(i));
		}
	}
	else {
		int nColumns = _model.getColumnCount();
		fieldNums = new int[nColumns];
		for (int i = 0; i < nColumns; i++) {
			fieldNums[i] = _worksheet.getVisibleColumn(i);
		}

	}

	Class[] classes = new Class[fieldNums.length];
	for (int i = 0; i < fieldNums.length; i++) {
		classes[i] = _worksheet.getColumnClass(
			_worksheet.getAbsoluteColumn(fieldNums[i]));
	}

	try {
		line = reader.readLine();
	}
	catch (Exception e) {
		e.printStackTrace();
		Message.printWarning(2, routine, e);
		Message.printWarning(1, routine, "Error reading from file.");
		return;
	}

	Vector v = null;
	int lineNum = 2;
	boolean skip = false;
	while (line != null) {
		line = line.trim();
		if (line.equals("")) {}
		else if (line.startsWith("#")) {}
		else {
			skip = false;
			v = StringUtil.breakStringList(line, ",",
				StringUtil.DELIM_ALLOW_STRINGS);

			if (v.size() != fieldNums.length) {
				Message.printWarning(1, routine, "Number of "
					+ "data fields in line " + lineNum
					+ " does not match the required "
					+ "number of data fields.");
				skip = true;
			}

			if (!skip) {
				if (!importNewRecord(v, fieldNums, classes)) {
					Message.printWarning(1, routine,
						"An error was encountered "
						+ "while importing line #"
						+ lineNum + " from the import "
						+ "file.  Check the log.");
				}
			}
		}

		try {
			line = reader.readLine();
			lineNum++;
		}
		catch (Exception e) {
			e.printStackTrace();
			Message.printWarning(2, routine, e);
			Message.printWarning(1, routine, "Error reading from "
				+ "file.");
			return;
		}
	}

	try {
		reader.close();
	}
	catch (Exception e) {
		e.printStackTrace();
		Message.printWarning(2, routine, e);
		Message.printWarning(1, routine, "Error closing file.");
		return;
	}
}

/**
Adds a new record to the worksheet with values read in from an import file.
@param values the values to be placed in the record.
@param fieldNums the visible fields into which to place the values.
@param classes the classes for the fields.
@return true if successful, false if not.
*/
protected boolean importNewRecord( Vector values, int[] fieldNums,
	Class[] classes)
{
	String routine = CLASS + ".importNewRecord";

	// Derived classes ( except Scenario ) should call super.importNewRecord
	// ( values, fieldNums, classes ) before returning to process this
	// common code.
	// Scenario should implement the alternative to this code locally.
	int row = _worksheet.getRowCount() - 1;

	boolean error = false;
	String s      = null;
	for ( int i = 0; i < values.size(); i++ ) {

		s = (String)values.elementAt(i);
		s = s.trim();

		try {
			if ( classes[i] == String.class ) {
				_worksheet.setValueAt(  s, row, fieldNums[i] );
			}
			else if ( classes[i] == Double.class ) {
				Double dd = new Double(s);
				_worksheet.setValueAt( dd, row, fieldNums[i] );
			}
			else if ( classes[i] == Integer.class ) {
				Integer ii = new Integer(s);
				_worksheet.setValueAt( ii, row, fieldNums[i] );
			}
		}
		catch ( Exception e ) {
			error = true;
			e.printStackTrace();
			Message.printWarning( 2, routine, e );
		}
	}
	return (!error);
}

/**
Checks through all the records to see if any are new or dirty.
@return true if any are dirty.
*/
public boolean isDataDirty()
{
	if (!_editable) {
		return false;
	}

	int rows = _worksheet.getRowCount();
	DMIDataObject ddo = null;
	for (int i = 0; i < rows; i++) {
		ddo = (DMIDataObject)_worksheet.getRowData(i);
		if (ddo.getOriginal() == null || ddo.isDirty()) {
			return true;
		}
	}

	int size = _deletedRecords.size();
	for (int i = 0; i < size; i++) {
		ddo = (DMIDataObject)_deletedRecords.elementAt(i);
		if (ddo.getOriginal () != null) {
			return true;
		}
	}

	return false;
}

/**
Does nothing.
*/
public void mouseClicked(MouseEvent event) {}

/**
Does nothing.
*/
public void mouseEntered(MouseEvent event) {}

/**
Does nothing.
*/
public void mouseExited(MouseEvent event) {}

/**
Does nothing.
*/
public void mousePressed(MouseEvent event) {}

/**
Determines whether any records are selected and enables/disables buttons
appropriately.
*/
public void mouseReleased(MouseEvent event)
{
	if (_worksheet.getSelectedRowCount() > 0) {
		setButtonsRowSelected();
	}
	else {
		setButtonsNoRowSelected();
	}
}

/**
Prompts the user to see if they still want to delete a record although another
user has changed its data in the database.
@param field the key field for the table in which the record will be deleted.
@param value the value in the key field.
*/
protected boolean promptForDelete(String field, String value)
{
	int x = new ResponseJDialog(this, "Data have changed!",
		"Database data have been changed since this GUI was opened.\n"
		+ "Record with " + field + " value '" + value
		+ "' has different values in the database now.  "
		+ "Delete anyways?",
		ResponseJDialog.YES | ResponseJDialog.NO).response();
	if (x == ResponseJDialog.YES) {
		return true;
	}
	return false;
}

/**
Prompts the user to see if they want to still insert a new record although
another user has already inserted that record in the database and the other
user's data will be overwritten.
@param field the key field for the table in which the record will be inserted.
@param value the value in the key field.
*/
protected boolean promptForInsert(String field, String value) {
	int x = new ResponseJDialog(this, "Data have changed!",
		"Database data have been changed since this GUI was opened.\n"
		+ "A record with " + field + " key value '" + value
		+ "' exists in the database now.  "
		+ "Overwrite it?",
		ResponseJDialog.YES | ResponseJDialog.NO).response();
	if (x == ResponseJDialog.YES) {
		return true;
	}
	return false;
}

/**
Prompts the user to see if they want to still change data in a record although
another user has already changed the data and that user's data will be
overwritten.
@param field the key field for the table in which data will be updated.
@param value the value in the key field.
*/
protected boolean promptForUpdate(String field, String value) {
	int x = new ResponseJDialog(this, "Data have changed!",
		"Database data have been changed since this GUI was opened.\n"
		+ "Record with " + field + " value '" + value
		+ "' has different values in the database now.  "
		+ "Overwrite changes?",
		ResponseJDialog.YES | ResponseJDialog.NO).response();
	if (x == ResponseJDialog.YES) {
		return true;
	}
	return false;
}

/**
Reads the proper data to display in the table.
*/
abstract protected void readData();

/**
Responds when the save button is pressed.  Saves changes to the database
and resets the GUI.
*/
protected void saveClicked()
{
	String routine = CLASS + ".saveClicked", mssg;
	String nl = System.getProperty( "line.separator" );

	int rows = _worksheet.getRowCount();
	Vector writeRecords = new Vector();
	Vector pairs = new Vector();

	// First go through all the records in the table and see if any
	// have been changed.  This is done by checking the isDirty flag().
	// These records are checked to see if their key values changed.  If
	// so, then their original data are added to the _deletedRecords
	// Vector to be deleted and the new data are added to a Vector of
	// records to be written.  If no key values have changed, the record
	// is added to the Vector of records to be written.

	// This is only done if the key value has changed.  The record with
	// the old key value is deleted from the database and then the
	// record with the new key value is written to the database.  This is
	// done because it is much easier (as an overall issue through the
	// code) than changing the key value in the database with the update.
	DMIDataObject ddo = null;
	DMIDataObject[] obs = null;
	for (int i = 0; i < rows; i++) {
		ddo = (DMIDataObject)_worksheet.getRowData(i);
		if (ddo.isDirty()) {
			obs = checkForKeyChange(ddo);
			if (obs != null) {
				writeRecords.add(obs[0]);
				_deletedRecords.add(obs[1]);
			}
			else {
				writeRecords.add(ddo);
			}
			pairs.add(new Integer(i));
		}
	}
	
	_dmi.setDumpSQLOnExecution( true );

	String errorList = "";
	
	// Now go through all the records to be deleted and delete them
	// from the database.  Records that are new
	// i.e., those that were added to the table in the GUI and then
	// were deleted, are not deleted from the database.
	int size = _deletedRecords.size();
	int deleteErrorCount = 0;
	for (int i = 0; i < size; i++) {
		ddo = (DMIDataObject)_deletedRecords.elementAt(i);
		if (ddo.getOriginal() != null) {
			try {
				deleteRecord(ddo);
			}
			catch (Exception e) {
				deleteErrorCount++;
				errorList = errorList + nl + " (Delete) "
					+deleteErrorCount + ") " + e.toString();
				e.printStackTrace();
				Message.printWarning( 2, routine, e );
				_results = new Vector();
			}
		}
	}

	// Now go through the writeRecords Vector and write all the records in
	// it to the database.
	// Notice: Scenario is a special case because its key value is an 
	//	   autonum.
	//         See RiversideDB_ReferenceTable_Scenario_JFrame.writeRecord()
	int writeErrorCount = 0;
	size = writeRecords.size();
	for (int i = 0; i < size; i++) {
		try {
			ddo = (DMIDataObject)writeRecords.elementAt(i);
			writeRecord(i, ddo, pairs);
		}
		catch (Exception e) {
			writeErrorCount++;
			errorList = errorList + nl + "  (Write) "
					+writeErrorCount + ") " + e.toString();
			Message.printWarning( 2, routine, e );	
		}

	}

	// Print out error information if any...
	if ( deleteErrorCount > 0 || writeErrorCount > 0 ) {
		int total = writeErrorCount + deleteErrorCount;
		mssg = "While saving changes to the database, " + total;
		if ( total == 1 ) { mssg = mssg + " error was encountered. "  ; }
		else              { mssg = mssg + " errors were encountered. "; }
		mssg = mssg + "Please check the log file for more information.";
		new ResponseJDialog( this, "Error!", mssg, ResponseJDialog.OK );
		mssg = mssg.trim() + errorList;
		Message.printWarning ( 2, routine, mssg );
	}

	// Refresh the worksheet 
	_worksheet.refresh();

	_dmi.setDumpSQLOnExecution(false);

	// Clear out the Vector of deleted records to represent a clean GUI state
	_deletedRecords = new Vector();

	// Go through the records in the table model and make a backup copy of
	// them in their current state so that change comparisons can be done
	// for determing dirtiness.
	_model.backupData();
}

/**
Sets the button states in a way representing that changes have been made to
table data.
*/
protected void setButtonsChanges()
{
	_cancelButton.setEnabled(true);
	_saveButton  .setEnabled(true);
}

/**
Sets the button states in a way representing that no changes have been made to
table data.
*/
protected void setButtonsNoChanges()
{
	_cancelButton.setEnabled(false);
	_saveButton  .setEnabled(false);
}

/**
Sets the button states in a way representing that no records are selected.
*/
protected void setButtonsNoRowSelected()
{
	_deleteButton.setEnabled(false);
}

/**
Sets the button states in a way representing that some records are selected.
*/
protected void setButtonsRowSelected()
{
	_deleteButton.setEnabled(true);
}

/**
Sets the size of the GUI appropriately for the kind of table being displayed.
*/
abstract protected void setSize();

/**
Sets up the GUI.
*/
protected void setupGUI()
{
	addWindowListener(this);

 	JPanel panel = new JPanel();
 	panel.setLayout(new GridBagLayout());
	JPanel bottomPanel = new JPanel();
	bottomPanel.setLayout(new GridBagLayout());
	int x = 5;

	_importButton = new JButton(_BUTTON_IMPORT);
	_importButton.addActionListener(this);
	if (_editable) {
		JGUIUtil.addComponent(bottomPanel, _importButton,
			x++, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.NONE, GridBagConstraints.EAST);
	}

	_addButton = new JButton(_BUTTON_ADD);
	_addButton.addActionListener(this);
	if (_editable) {
		JGUIUtil.addComponent(bottomPanel, _addButton,
			x++, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.NONE, GridBagConstraints.EAST);
	}

	_deleteButton = new JButton(_BUTTON_DELETE);
	_deleteButton.addActionListener(this);
	if (_editable) {
		JGUIUtil.addComponent(bottomPanel, _deleteButton,
			x++, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.NONE, GridBagConstraints.EAST);
	}

	_saveButton = new JButton(_BUTTON_SAVE);
	_saveButton.addActionListener(this);
	if (_editable) {
		JGUIUtil.addComponent(bottomPanel, _saveButton,
			x++, 1, 1, 1, 0.0, 0.0,
			GridBagConstraints.NONE, GridBagConstraints.EAST);
	}

	_closeButton = new JButton(_BUTTON_CLOSE);
	_closeButton.addActionListener(this);
	JGUIUtil.addComponent(bottomPanel, _closeButton,
		x++, 1, 1, 1, 0.0, 0.0,
		GridBagConstraints.NONE, GridBagConstraints.EAST);

	_cancelButton = new JButton(_BUTTON_CANCEL);
	_cancelButton.addActionListener(this);
	if (_editable) {
		JGUIUtil.addComponent(bottomPanel, _cancelButton,
			x++, 1, 1, 1, 0.0, 0.0,
		GridBagConstraints.NONE, GridBagConstraints.EAST);
	}
	PropList p = new PropList("JWorksheet");
	p.add("JWorksheet.ShowRowHeader=true");
	p.add("JWorksheet.ShowPopupMenu=true");
	p.add("JWorksheet.SelectionMode=ExcelSelection");
	p.add("JWorksheet.OneClickColumnSelection=false");
	p.add("JWorksheet.AllowCopy=true");
	if (_editable) {
		p.add("JWorksheet.AllowPaste=true");
	}
	else {
		p.add("JWorksheet.AllowPaste=false");
	}

	_model.addTableModelListener(this);
	RiversideDB_ReferenceTable_CellRenderer renderer =
		new RiversideDB_ReferenceTable_CellRenderer(_model);
	int[] widths = renderer.getColumnWidths();
	JScrollWorksheet jsw = new JScrollWorksheet(renderer, _model, p);
	_worksheet = jsw.getJWorksheet();
	_worksheet.addMouseListener(this);
	_worksheet.testing(true);
	_worksheet.setPreferredScrollableViewportSize(null);

	setupCellAttributes();
	setupComboBoxes();

	JGUIUtil.addComponent(panel, jsw,
		0, 0, x, 1, 1.0, 1.0,
		GridBagConstraints.BOTH, GridBagConstraints.CENTER);

	// Assemble
	getContentPane().add(panel);
	getContentPane().add("South", bottomPanel);
	pack();
	setSize();
	JGUIUtil.center(this);
	JGUIUtil.setIcon(this, JGUIUtil.getIconImage());
	setButtonsNoChanges();
	setButtonsNoRowSelected();
	setVisible(true);
	if (widths != null) {
		_worksheet.setColumnWidths(widths);
	}
 	_saveButton.requestFocus();

	// Set GUI title.
	setTitle( _title );
}

/**
Sets up cell attributes for tables, turning uneditable fields grey.
Currently only used for the SCENARIO table.
Overwrite in the derived class only when necessary.
*/
protected void setupCellAttributes()
{
}

/**
Sets up the combo boxes for the tables in which field values can be selected
from a combo box.
Currently only used for the DATA_UNITS and DATA_TYPE tables.
Overwrite in the derived class only when necessary.
*/
protected void setupComboBoxes()
{
}

/**
Determines if a record should be deleted from the database or not.
@param worksheet the DMIDataObject to be deleted, as stored in the worksheet.
@param original the original version of the worksheet DMIDataObject.
@param dmiObject the version of the DMIDataObject with the same key as the
original that is in the database.
@param field the key field for the current table.
@param value the value in the key field.
@return true if the record should be deleted, false if not.
*/
protected boolean shouldDelete(	DMIDataObject worksheet,
				DMIDataObject original,
				DMIDataObject dmiObject,
				String field,
				String value)
{
	DMIDataObject[] ddo = checkForKeyChange(worksheet);
	boolean changed = false;

	if (dmiObject != null && !dataObjectsEqual(dmiObject, original)) {
		changed = true;
	}

	// ddo: whether the key value of the gui record has been changed
	// 	from the original value. if null, then the key hasn't changed.
	// changed: if the database version of the record has changed since
	//	this GUI has opened (ie, another user has worked on it)

	if (changed && ddo == null) {
		return promptForDelete(field, value);
	}
	else if (changed && ddo != null) {
		return false;
	}
	else {
		return true;
	}
}

/**
Determines if a record should be written to the database or not.
@param worksheet the DMIDataObject to be written, as stored in the worksheet.
@param original the original version of the worksheet DMIDataObject.
@param dmiObject the version of the DMIDataObject with the same key as the
original that is in the database.
@param field the key field for the current table.
@param value the value in the key field.
@return true if the record should be written, false if not.
*/
protected boolean shouldWrite( DMIDataObject worksheet,
			       DMIDataObject original,
			       DMIDataObject dmiObject,
			       String field, String value )
{
	if (original == null) {
		// this is a new record (has no original value).  This includes
		// records where the key was changed, as they are really handled
		// as deletes of the original key, followed by a write of the
		// new key.
		if (dmiObject == null) {
			// no record with the same key exists in the database
			return true;
		}
		else {
			// a record with the same key already is in the database
			return promptForInsert(field, value);
		}
	}
	else {
			// a record with the same key already is in the database
		if (dmiObject == null) {
			// no record with the same key exists in the database
			return true;
		}
		// this record already existed.   Data values will be updated.
		if (!dataObjectsEqual(dmiObject, original)) {
			// the data has been changed in the database since
			// the gui was opened.
			return promptForUpdate(field, value);
		}
		else {
			// write that record
			return true;
		}
	}
}

/**
Called when a data value is changed in the table model; checks to see if
any records are changed from their original value and updates button states
appropriately.
@param rowNumber the number of the row in which data were changed
@param colNumber the number of the column in which data were changed
*/
public void tableModelValueChanged(int rowNumber, int colNumber, Object value) {
	checkRecordsForChanges();
}

/**
Does nothing.
*/
public void windowActivated(WindowEvent event) {}

/**
Does nothing.
*/
public void windowClosed(WindowEvent event) {}

/**
Saves changes to the database.
@param event the WindowEvent that happened.
*/
public void windowClosing(WindowEvent event)
{
	if (!_worksheet.stopEditing()) {
		_worksheet.cancelEditing();
	}
	checkDataChanges();
	informWindowManager();
}

/**
Does nothing.
*/
public void windowDeactivated(WindowEvent event) {}

/**
Does nothing.
*/
public void windowDeiconified(WindowEvent event) {}

/**
Does nothing.
*/
public void windowIconified(WindowEvent event) {}

/**
Does nothing.
*/
public void windowOpened(WindowEvent event) {}

/**
Writes a record to the database.
@param recordNum the number of the record in the Vector of records to be
written.
@param ddo the DMIDataObject storing the record to be written.
@param pairs Vector that ties the record to be written to a row in the
worksheet.
*/
protected void writeRecord(int recordNum, DMIDataObject ddo, Vector pairs)
throws Exception
{
}

}
//------------------------------------------------------------------------------
