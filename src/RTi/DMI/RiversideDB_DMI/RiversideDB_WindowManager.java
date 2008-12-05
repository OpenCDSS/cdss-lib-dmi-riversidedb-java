//------------------------------------------------------------------------------
// RiversideDB_WindowManager - Manage the RiversideDB_System windows
//------------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
//------------------------------------------------------------------------------
// History:
// 2004-02-05	J. Thomas Sapienza, RTi	Initial version.
// 2004-02-17	JTS, RTi		Simplified the design of the
//					displayWindow method, in particular
//					with regard to opening windows for
//					creating new objects.
// 2004-11-23 Luiz Teixeira, RTi	Moved from RiverTrak Assistant.
//					Renamed from
//					   RTAssistant_WindowManager to
//					   RiversideDB_System_WindowManager
// 2004-12-07 Luiz Teixeira, RTi	Renamed from
//					   RiversideDB_System_WindowManager
//					to RiversideDB_WindowManager.
//					Some cleanup and documentation.
// 2004-12-14 Luiz Teixeira, RTi	Replace all the calls to the
//					   RiversideDB_ReferenceTable_JFrame
// 					to the new base/derived versions.
//					Now there are individual classes for
//					each reference table, the parameter
//					type is not needed anymore and the
//					individual derived classes should be
//					easier to maintain and the design 
//					follows the general object oriented
//					concept of the 03.00.00 upgrade.
// 2004-12-15 Luiz Teixeira		Added the Props Table to the list of
//					tables available for display.
//					The class processing this table will be:
//					RiversideDB_ReferenceTable_Props_JFrame
// 2004-12-22 Luiz Teixeira		Added 	areWindowsOpenWithUnsavedData()
//						and two overloads of the 
//						isDataDirty(...) method.
//------------------------------------------------------------------------------
package RTi.DMI.RiversideDB_DMI;

import java.awt.Frame;
import javax.swing.JFrame;
import java.util.List;

import RTi.Util.GUI.WindowManager;
import RTi.Util.GUI.WindowManagerData;
import RTi.Util.Message.Message;

/**
This class manages windows for the RiversideDB_System.  As most of the
actual management work is handled in the underlying WindowManager class, this
class mainly opens windows as they are requested.
*/
public class RiversideDB_WindowManager extends WindowManager
{

/**
Class name
*/
private static String __class = "RiversideDB_WindowManager";

/**
Index of the windows this class can manage.
*/
public final int
	WINDOW_MAIN = 			  0,

	// After WINDOWS_REF_, the names are matching the table names
	// in the RiversideDB, with words separated by underscores.
	WINDOW_REF_DATA_DIMENSION =	  1,
	WINDOW_REF_DATA_SOURCE =	  2,
	WINDOW_REF_DATA_TYPE =		  3,
	WINDOW_REF_DATA_UNITS =		  4,
	WINDOW_REF_IMPORT_TYPE =	  5,
	WINDOW_REF_MEAS_CREATE_METHOD =	  6,
	WINDOW_REF_MEAS_QUALITY_FLAG =	  7,
	WINDOW_REF_MEAS_REDUCTION_TYPE =  8,
	WINDOW_REF_MEAS_TIME_SCALE =	  9,
	WINDOW_REF_MEAS_TRANS_PROTOCOL = 10,
	WINDOW_REF_MESSAGE_LOG =	 11,
	WINDOW_REF_SCENARIO =		 12,
	WINDOW_REF_SHEF_TYPE =		 13,
	WINDOW_REF_TABLE_LAYOUT =  	 14,
	WINDOW_REF_PROPS =               15,

	// After WINDOWS_, the names are matching the table names in the
	// RiversideDB, in some cases with words separated by underscores.
	WINDOW_IMPORT_PRODUCT =	   	16,
	WINDOW_EXPORT_PRODUCT =     	17,
	WINDOW_MEASLOC = 		18,
	WINDOW_MEASTYPE = 		19,
	WINDOW_MEASLOCGROUP = 		20,
	WINDOW_PRODUCTGROUP = 		21,
	WINDOW_REDUCTION = 		22;

/**
The number of windows this class can manage.
*/
private static final int __NUM_WINDOWS = 23;

/**
Reference to the DMI passed in by the main application.
*/
private RiversideDB_DMI __dmi = null;

/**
Constructor.
@param dmi the DMI being used by the Assistant.
@param main the JFrame of the Main assistant window.
*/
public RiversideDB_WindowManager( RiversideDB_DMI dmi, JFrame main )
{
	super( __NUM_WINDOWS );

	// Initialize instance members.
	__dmi  = dmi;

	// Initialize the windows indexes instance members
	initialize();
}

/**
Check if any of the opened windows contains unsaved data (is dirty).
@return true if the window is dirty, false otherwise.
*/
public boolean areWindowsOpenWithUnsavedData()
{	
	// If any is found opened and 
	for ( int i = 0; i < _windowStatus.length; i++ ) {
		if ( _windowStatus[i] == STATUS_OPEN ) {
			if  ( isDataDirty( i ) ) {
				// Found one dirt. Return true.
				return true;
			}
		}
	}

	List v;
	WindowManagerData data;
	int size = 0;
	for ( int i = 0; i < _windowInstanceInformation.length; i++ ) {
		if ( _windowInstanceInformation[i] != null ) {
			v = _windowInstanceInformation[i];
			size = v.size();
			for (int j = 0; j < size; j++) {
				data = (WindowManagerData) v.get(j);
				if ( data.getStatus() == STATUS_OPEN ) {
					if  ( isDataDirty( i, data.getID() ) ) {
						// Found one dirt. Return true.
						return true;
					}
				}
			}
		}
	}
	
	// Either all windows are closed or the opened ones are not dirty.
	return false;
}

/**
Display windows that can only be opened once.
@param winIndex the index of the window to display
@param editable whether the window data is editable
*/
public JFrame displayWindow( int winIndex, boolean editable )
{
	return displayWindow( winIndex, editable, null, null, null );
}

/**
Display windows that can only be opened once.
@param winIndex the index of the window to display
@param editable whether the window data is editable
@param title the title of the window
*/
public JFrame displayWindow( int winIndex, boolean editable, String title )
{
	return displayWindow( winIndex, editable, title, null, null );
}

/**
Display windows that can only be opened once and are populated from a single
given object.
@param winIndex the index of the window to display
@param edtable whether the window data is editable
@param title the title of the window
@param object object to populate the window with (e.g. an ImportProduct object)
*/
public JFrame displayWindow(
	int winIndex, boolean editable, String title, Object object)
{
	return displayWindow( winIndex, editable, title, object, null );
}

/**
Display windows that can be opened multiple times and are populated from a
single given object.
@param winIndex the index of the window to display
@param edtable whether the window data is editable
@param title the title of the window
@param object object to populate the window with (e.g. an ImportProduct object)
@param id the unique identifier of the window instance to display
*/
public JFrame displayWindow( int winIndex, boolean editable, String title,
	Object object, Object id )
{
	return displayWindow(
		winIndex, editable, title, object, (Object)null, id );
}

/**
Display windows that can be opened multiple times and are populated from a
single given object.
@param winIndex the index of the window to display
@param edtable whether the window data is editable
@param title the title of the window
@param object object to populate the window with (e.g. an ImportProduct object)
@param object2 a second object used to populate the window with (e.g. an ImportConf
object).
@param id the unique identifier of the window instance to display.
*/
public JFrame displayWindow( int winIndex, boolean editable, String title,
	Object object1, Object object2, Object id )
{
	String routine = __class + ".displayWindow";

	JFrame win = null;
	if ( Message.isDebugOn ) {
		Message.printDebug( 4, routine,
			" Title: '" + title + "', Index: '" + winIndex
			+ "', Id: '" + id + "'" );
	}
	if ( allowsMultipleWindowInstances(winIndex) ) {

		// Multiple instances allowed.
		if ( Message.isDebugOn ) {
			Message.printDebug( 4, routine,
				" Multiple instances allowed.");
		}
		if (id == null) {
			id = new String( "CREATING NEW OBJECT" );
			if ( Message.isDebugOn ) {
				Message.printDebug( 4, routine,
					" id == null, new object.");
			}
		}
		try {
			if (getWindowInstanceStatus(winIndex,id)==STATUS_OPEN) {
				if ( Message.isDebugOn ) {
					Message.printDebug( 4, routine,
						" Window already opened." );
				}
				win = getWindowInstanceWindow(winIndex, id);
				win.setState(Frame.NORMAL);
				win.toFront();
				return win;
			}
			if ( Message.isDebugOn ) {
				Message.printDebug( 4, routine,
					" Instance not opened, opening ...");
			}
			switch (winIndex) {
				
// REVISIT [LT] Why pass the title to the these flames from the manager. They
//              can be built by the frame objects thenselves, as is done for the
//              reference windows. (2004-12-14)

				case WINDOW_EXPORT_PRODUCT:
					win = new RiversideDB_Export_JFrame(
						__dmi,
						this,
						title,
						(RiversideDB_ExportProduct)
							object1 );
					setWindowInstanceOpen(winIndex, win, id);
					return win;
				case WINDOW_IMPORT_PRODUCT:
					win = new RiversideDB_Import_JFrame(
						__dmi,
						this,
						title,
						(RiversideDB_ImportProduct)
							object1 );
					setWindowInstanceOpen(winIndex, win, id);
					return win;
				case WINDOW_MEASLOC:
					win = new RiversideDB_Location_JFrame(
						__dmi,
						this,
						title,
						(RiversideDB_MeasLoc)
							object1 );
					setWindowInstanceOpen(winIndex, win, id);
					return win;
				case WINDOW_MEASLOCGROUP:
					win =
					    new RiversideDB_MeasLocGroup_JFrame(
						__dmi,
						this,
						title,
						(RiversideDB_MeasLocGroup )
							object1 );
					setWindowInstanceOpen(winIndex, win, id);
					return win;
				case WINDOW_PRODUCTGROUP:
					win =
					    new RiversideDB_ProductGroup_JFrame(
						__dmi,
						this,
						title,
						(RiversideDB_ProductGroup)
							object1 );
					setWindowInstanceOpen(winIndex, win, id);
					return win;
				case WINDOW_REDUCTION:
					win = new RiversideDB_Reduction_JFrame(
						__dmi,
						this,
						title,
						(RiversideDB_MeasType)
							object1,
						(RiversideDB_MeasReduction)
							object2 );
					setWindowInstanceOpen(winIndex, win, id);
					return win;
				case WINDOW_MEASTYPE:
					win = new RiversideDB_TS_JFrame(
						__dmi,
						this,
						title,
						(RiversideDB_MeasType)
							object1 );
					setWindowInstanceOpen(winIndex, win, id);
					return win;
			}
		}
		catch (Exception e) {
			Message.printWarning(2, routine, e);
			Message.printWarning(1, routine,
				" Error displaying window!");

		}
	}
	else {
		// Multiple instances not allowed.
		if ( Message.isDebugOn ) {
			Message.printDebug( 4, routine,
				" Multiple instances not allowed.");
		}
		// Single instance windows
		if ( getWindowStatus(winIndex) == STATUS_OPEN ) {
			win = getWindow(winIndex);
			win.setState(Frame.NORMAL);
			win.toFront();
			Message.printStatus(2, routine, " Window " + winIndex + " already opened." );
			return win;
		}

		switch (winIndex) {
			case WINDOW_REF_DATA_DIMENSION:
				win = new
				RiversideDB_ReferenceTable_DataDimension_JFrame(
					__dmi, this, editable);
				break;
			case WINDOW_REF_DATA_SOURCE:
				win = new
				RiversideDB_ReferenceTable_DataSource_JFrame(
					__dmi, this, editable);
				break;
			case WINDOW_REF_DATA_TYPE:
				win = new
				RiversideDB_ReferenceTable_DataType_JFrame(
					__dmi, this, editable);
				break;
			case WINDOW_REF_DATA_UNITS:
				win = new
				RiversideDB_ReferenceTable_DataUnits_JFrame(
					__dmi, this, editable);
				break;
			case WINDOW_REF_IMPORT_TYPE:
				win = new
				RiversideDB_ReferenceTable_ImportType_JFrame(
					__dmi, this, editable);
				break;
			case WINDOW_REF_MEAS_CREATE_METHOD:
				win = new
				RiversideDB_ReferenceTable_MeasCreateMethod_JFrame(
					__dmi, this, editable);
				break;
			case WINDOW_REF_MEAS_QUALITY_FLAG:
				win = new
				RiversideDB_ReferenceTable_MeasQualityFlag_JFrame(
					__dmi, this, editable);
				break;
			case WINDOW_REF_MEAS_REDUCTION_TYPE:
				win = new
				RiversideDB_ReferenceTable_MeasReductionType_JFrame(
					__dmi, this, editable);
				break;
			case WINDOW_REF_MEAS_TIME_SCALE:
				win = new
				RiversideDB_ReferenceTable_MeasTimeScale_JFrame(
					__dmi, this, editable);
				break;
			case WINDOW_REF_MEAS_TRANS_PROTOCOL:
				win = new
				RiversideDB_ReferenceTable_MeasTransProtocol_JFrame(
					__dmi, this, editable);
				break;
			case WINDOW_REF_MESSAGE_LOG:
				win = new
				RiversideDB_ReferenceTable_MessageLog_JFrame(
					__dmi, this, editable);
				break;
			case WINDOW_REF_SCENARIO:
				win = new
				RiversideDB_ReferenceTable_Scenario_JFrame(
					__dmi, this, editable);
				break;
			case WINDOW_REF_SHEF_TYPE:
				win = new
				RiversideDB_ReferenceTable_SHEFType_JFrame(
					__dmi, this, editable);
				break;
			case WINDOW_REF_TABLE_LAYOUT:
				win = new
				RiversideDB_ReferenceTable_TableLayout_JFrame(
					__dmi, this, editable);
				break;
			case WINDOW_REF_PROPS:
				win = new
				RiversideDB_ReferenceTable_Props_JFrame(
					__dmi, this, editable);
				break;
			default:
				return null;
		}

		setWindowOpen(winIndex, win);
	}
	
	return win;
}

/**
Finalize.  Free memory for garbage collection.
@exception Throwable if there is an error.
*/
public void finalize()
throws Throwable
{
	__dmi  = null;
	super.finalize();
}

/**
Initializes data members. Sets up which windows can allow mutiple instances.
With the exception of the Reference Table Editor windows all the other windows
are currently set to allow multiple instances.
<pre>	WINDOW_IMPORT_PRODUCT
	WINDOW_EXPORT_PRODUCT
	WINDOW_MEASLOC
	WINDOW_MEASTYPE
	WINDOW_MEASLOCGROUP
	WINDOW_PRODUCTGROUP
	WINDOW_REDUCTION
<pre>
*/
protected void initialize()
{
	for (int i = 16; i < __NUM_WINDOWS; i++) {
		setAllowMultipleWindowInstances(i, true);
	}
}

/**
Check if the window has unsaved data ( is dirt ).
@param windowType the number of the window.
@return true if the window is dirty, false otherwise.
*/
public boolean isDataDirty ( int windowType )
{
	boolean isDirty = false;
		
	// Get the window...
	JFrame window = getWindow( windowType );
	
	// Check if it is dirty...
	switch ( windowType ) {
		case WINDOW_REF_DATA_DIMENSION:
			isDirty = ((RiversideDB_ReferenceTable_DataDimension_JFrame)
				window).isDataDirty();
			break;
		case WINDOW_REF_DATA_SOURCE:
			isDirty = ((RiversideDB_ReferenceTable_DataSource_JFrame)
				window).isDataDirty(); 
			break;
		case WINDOW_REF_DATA_TYPE:
			isDirty = ((RiversideDB_ReferenceTable_DataType_JFrame)
				window).isDataDirty(); 
			break;
		case WINDOW_REF_DATA_UNITS:
			isDirty = ((RiversideDB_ReferenceTable_DataUnits_JFrame)
				window).isDataDirty(); 
			break;
		case WINDOW_REF_IMPORT_TYPE:
			isDirty = ((RiversideDB_ReferenceTable_ImportType_JFrame)
				window).isDataDirty();
			break;
		case WINDOW_REF_MEAS_CREATE_METHOD:
			isDirty = ((RiversideDB_ReferenceTable_MeasCreateMethod_JFrame)
				window).isDataDirty(); 
			break;
		case WINDOW_REF_MEAS_QUALITY_FLAG:
			isDirty = ((RiversideDB_ReferenceTable_MeasQualityFlag_JFrame)
				window).isDataDirty(); 
			break;
		case WINDOW_REF_MEAS_REDUCTION_TYPE:
			isDirty = ((RiversideDB_ReferenceTable_MeasReductionType_JFrame)
				window).isDataDirty(); 
			break;
		case WINDOW_REF_MEAS_TIME_SCALE:
			isDirty = ((RiversideDB_ReferenceTable_MeasTimeScale_JFrame)
				window).isDataDirty(); 
			break;
		case WINDOW_REF_MEAS_TRANS_PROTOCOL:
			isDirty = ((RiversideDB_ReferenceTable_MeasTransProtocol_JFrame)
				window).isDataDirty(); 
			break;
		case WINDOW_REF_MESSAGE_LOG:
			isDirty = ((RiversideDB_ReferenceTable_MessageLog_JFrame)
				window).isDataDirty(); 
			break;
		case WINDOW_REF_SCENARIO:
			isDirty = ((RiversideDB_ReferenceTable_Scenario_JFrame)
				window).isDataDirty(); 
			break;
		case WINDOW_REF_SHEF_TYPE:
			isDirty = ((RiversideDB_ReferenceTable_SHEFType_JFrame)
				window).isDataDirty(); 
			break;
		case WINDOW_REF_TABLE_LAYOUT:
			isDirty = ((RiversideDB_ReferenceTable_TableLayout_JFrame)
				window).isDataDirty();
			break;
		case WINDOW_REF_PROPS:
			isDirty = ((RiversideDB_ReferenceTable_Props_JFrame)
				window).isDataDirty(); 
			break;
		default:
			isDirty = false;
			break;
	}
	
	return isDirty;
}

/**
Check if the window instance has unsaved data ( is dirt ).
@param windowType the index of the window type.
@param id the unique identifier of the windows instance to close.
@return true if the window is dirty, false otherwise.
*/
public boolean isDataDirty ( int windowType, Object id )
{
	boolean isDirty = false;
	
	// Get the window...
	JFrame window = getWindowInstanceWindow( windowType, id );
	
	// Check if it is dirty...
	switch ( windowType ) {
		case WINDOW_EXPORT_PRODUCT:
			isDirty = ((RiversideDB_Export_JFrame) window).
				isDataDirty();
			break;
		case WINDOW_IMPORT_PRODUCT:
			isDirty = ((RiversideDB_Import_JFrame) window).
				isDataDirty();	
			break;
		case WINDOW_MEASLOC:
			isDirty = ((RiversideDB_Location_JFrame) window).
				isDataDirty();	
			break;
		case WINDOW_MEASLOCGROUP:
			isDirty = ((RiversideDB_MeasLocGroup_JFrame ) window).
				isDataDirty();
			break;
		case WINDOW_PRODUCTGROUP:
			isDirty = ((RiversideDB_ProductGroup_JFrame) window).
				isDataDirty();	
			break;
		case WINDOW_REDUCTION:
			isDirty = ((RiversideDB_Reduction_JFrame) window).
				isDataDirty();	
			break;
		case WINDOW_MEASTYPE:
			isDirty = ((RiversideDB_TS_JFrame) window).
				isDataDirty();
			break;	
		default:
			isDirty = false;
			break;
	}
	
	return isDirty;
}

/**
Sets the DMI being used by the application.
@param dmi the DMI being used by the application.
*/
public void setDMI(RiversideDB_DMI dmi)
{
	__dmi = dmi;
}

}
//------------------------------------------------------------------------------
