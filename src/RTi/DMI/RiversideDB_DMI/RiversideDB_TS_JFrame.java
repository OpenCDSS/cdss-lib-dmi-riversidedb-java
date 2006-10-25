//--------------------------------------------------------------------------
// RiversideDB_TS_JFrame - 
//
//	Contains the code used to create the GUI for MeasTypes (Time Series)
//	used in the RiverTrakAssistant.  This class is called by
//	the RTAssistant_Main_JFrame.
//
//--------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
//--------------------------------------------------------------------------
// History:
//
// 2002-12-06	Morgan Sheedy, RTi	Initial Implementation
//
//
// 2002-11-06	AMS, RTi		Changed class name from:
//					RiversideDBAdministratorMeasLocJFrame 
//					to: RTiDBAdmin_MeasLoc_JFrame
//
// 2002-12-06 	AMS, RTi		Formatted numbers to be written
//					in the GUI with precision: %.6f and
//					for comparison, the numbers are
//					formatted to %.8f.
//
// 2002-15-06	AMS, RTi		Changed name from:
//					RTiDBAdmin_MeasLoc_JFrame 
//					to: RTiDBAdmin_TS_JFrame.
//					Is specific to the GUI for the
//					Time Series ( MeasTypes )
//
// 2002-18-06	AMS, RTi		Changed format.
//					Changing from tab to tab does not
//					call any methods any longer.
//					The verify_xx_tab() methods 
//					now do 3 main things:
//					-sets the 
//						"_selected_xx_string" global
//						variables.
//					-sets the .setDirty flag on the 
//						MeasType object.
//					-uses the ".set" methods to set the
//						new value in the MeasType 
//						object.
//
// 2002-26-09	Morgan Love, RTi	Changed Application name from:
//					RiversideDBAdmin to:
//					RiverTrakAssistant and Class from:
//					RTiDBAdmin_TS_JFrame to:
//					RiversideDB_TS_JFrame.
//
// 2003-04-10	AML, RTi		Update Missing and isMissing 
//					items from DMI class to DMIUtil.
//
// 2003-04-15	AML, RTi		Remove "create method" from 
//					tab and rename tab to "transmit
//					protocol".  Add Create method to
//					top interface as a non-editable
//					JTextField that will be edited by
//					code only when the TS are changed.
//	
//
// 2003-05-20	AML, RTi		Default transmitProtocol for new
//					time series: REGULAR
//
// 2003-06-03	AML, RTi		Update to use new TS classes.
//
// 2003-06-03	AML, RTi		*Prepare to add security checks:
//					DMI.canRead(), canWrite(), etc.
//
//					*Remove "New Table" button from
//					Data Storage tabs since with the
//					implementatin of security, only
//					root user will have permissions to
//					create a new Table.  The capability
//					will be added to the Tools menu for
//					root user only.
//
// 2003-06-11	AML, RTi		*MeasType.isEditable() field no
//					longer employed (replaced by 
//					the DBPermissions).  
//					*New field in MeasType: 
//					isVisible
//
// 2003-06-11	AML, RTi		*MeasType.isEditable() field is back
//					and set as an uneditable JCheckBox 
//					for Existing records.  If a new 
//					TS record is created, the JCheckBox
//					is active and defaulted to: 'Y'.
//
// 2003-06-30	AML, RTi		*code clean up.
//
// 2003-10-24	AML, RTi		DataType: FLOODMONITOR has "n/a"
//					as units
//
// 2004-07-14	AML, RTi		general code cleaning.
// 2004-10-29 Luiz Teixeira, RTi	Created the class
//					RiversideDB_BaseEditor_JFrame.java to 
// 					be used as base for all the editor
//					that needed to communicate back to the
//					calling classes. This is now done via
//					the RiversideDB_System_Listener which
//					is implemented in the base class
//					RiversideDB_BaseEditor_JFrame. The
//					base class also implements the 
//					addRiversideDBSystemListener(...)
//					and the private array member
//					RiversideDB_System_Listener []
//					_listeners to keep the assingned
//					listeners. This class now extends
// 					from RiversideDB_BaseEditor_JFrame.
// 2004-10-29 Luiz Teixeira, RTi	Removed the definition and all
//					reference to all __calling_class.
//					From now on using the 
//					RiversideDB_System_Listener implemen
//					ted in RiversideDB_BaseEditor_JFrame
// 2005-01-07 Luiz Teixeira, RTi	Upgraded to either use the pre-03.00.00
//					Editable field (getEditable() method) or
//					the the 03.00.00 IsEditable field
//					(getIsEditable() method)
// 2005-08-10	J. Thomas Sapienza, RTi	Added __originallyNewObject so that the
//					window manager would properly close the
//					GUI for new TS.
//--------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import  java.awt.AWTEvent;
import  java.awt.Color;
import  java.awt.Component;
import  java.awt.Dimension;
import  java.awt.Font;
import  java.awt.GridLayout;
import  java.awt.GridBagConstraints;
import  java.awt.GridBagLayout;
import  java.awt.Insets;
import  java.awt.Image;
import  java.awt.event.ActionEvent;
import  java.awt.event.ActionListener;
import  java.awt.event.ItemEvent;
import  java.awt.event.ItemListener;
import  java.awt.event.WindowEvent;
import  java.awt.event.WindowListener;
import  java.net.URL;
import  java.util.Vector;

import 	javax.swing.ImageIcon;
import 	javax.swing.JCheckBox;
import 	javax.swing.JFrame;
import 	javax.swing.JLabel;
import 	javax.swing.JList;
import 	javax.swing.JOptionPane;
import 	javax.swing.JPanel;
import 	javax.swing.JTabbedPane;
import 	javax.swing.JTextField;
import 	javax.swing.UIManager;

import  RTi.TS.TS;
import  RTi.TS.TSIdent;

import  RTi.Util.GUI.JGUIUtil;
import  RTi.Util.GUI.ResponseJDialog;
import  RTi.Util.GUI.ResponseJDialog;
import  RTi.Util.GUI.SimpleJButton;
import  RTi.Util.GUI.SimpleJComboBox;
import  RTi.Util.GUI.SimpleJTree;
import 	RTi.Util.IO.DataUnits;
import 	RTi.Util.IO.DataUnitsConversion;
import  RTi.Util.IO.IOUtil;
import  RTi.Util.IO.PropList;
import	RTi.Util.Message.Message;
import  RTi.Util.String.StringUtil;
import  RTi.Util.Time.TimeInterval;
import  RTi.Util.Time.TimeUtil;

import 	RTi.DMI.DMI;
import 	RTi.DMI.DMIUtil;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_DataSource;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_DataType;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_DataUnits;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_DBUser;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_DMI;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_MeasLoc;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_MeasTransProtocol;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_MeasType;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_Scenario;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_Tables;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_MeasLoc_JTree;

/**
RiversideDB_MeasType_JFrame.
This class is laid out similarly to the other RiverTrakAssistant classes.
The general format of the class is laid out below, with the major
methods listed with their key functions.  The main object types from 
RiversideDB that are manipulated by this class are:<br><ul>
<li>RiversideDB_MeasType (abbreviated MeasType)</li></ul><br>
<p><b>Constructor</b><br>
The constructor is called by an action in one of the JTrees of the 
main application.  The main application passes to this class a 
RiversideDB_MeasType.  This class is called in one of two cases: <br>
<ul><li>to view the properties of a current MeasType</li>
<li>to create a new MeasType</li></ul>
In the case of viewing a current MeasType, the MeasType object
passed to this class is already defined and all required properties 
already known.  In the case of creating a totally new MeasType, the 
MeasType passed in to this class is essentially an empty skeleton.  <br>
It is important to distinguish in the constructor, if we are dealing with
an existing MeasType and just changing some of its fields or if
we are creating a totally new MeasType.  If the fields used to define the 
TSIdent, such as the DataType field for the MeasType passed in is MISSING, 
then we know that we are creating a new MeasType object.  In the constructor 
then, set the <i>__bln_new_object = true </i> flag.  The <b><i>
__bln_new_object</i></b> flag is important because:<br> <ul>
<li>we can mark the object as dirty (using the <i>setDirty(true)</i> flag)
 since it is an entirely new object</li>
<li>we need to add the fields for <i>DBUser</i>, <i>DBGroup</i>, and 
<i>DBPermissions</i> based on the information already known by the DMI for 
this user.</li>
<li> we do not have to confirm changes made to a totally new object like we do 
if the user is changing an existing object</li>
<li>we need to know to add a new node to the JTree</li></ul>
At this point, whether we are creating a new MeasType or modifying an
existing one, we assign the MeasType to the variable known throughout
this class as: <b><i>__gui_RTi_MeasType</i></b>.  <br><br>
Finally, the constructor also:<ul><li> sets up Vectors of (static) reference data, 
read directly from the database thay will be used throughout the class</li>
<li>calls method: <i>init_layout_GUI()</i> which creates and sets up the 
GUI components</li></ul><br> 
</p>
<p><b>init_layout_gui</b><br>
This method is called from the constructor to create and layout the
GUI components.  It calls the method: <i>create_main_panel</i>, which
in turn calls methods named such as: <i>assemble_tab_QA_QC()</i>,
<i>assemble_tab_transmitProt</i>, etc.  These methods all create 
GUI components and put them together in a <i>GridBagLayout</i>. They
do not worry about setting correct values in the components' fields, but just 
gets the components set up.  The main components in the GUI consists of:
<ul><li>JPanel that contains general information pertinent to the MeasType 
object at the top of the GUI</li> <li>a series of tabs in a JTabbedPane 
with fields for the MeasType. Tab topics include: 
<ul><li>QA QC </li> <li>TransmitProtocal</li> <li>Status </li>
<li> timeseries 1</li><li>timeseries 2</li> </ul> </li>
<li>a panel added at the bottom that includes the standard buttons for: 
<ul><li>apply</li><li>close</li><li>cancel</li></ul></li></ul>
At the end of <i>init_layout_gui</i>, a 
call to <i>update_gui_fields</i> is made which fills in all the 
fields of the GUI according to the MeasType currently being worked with.
</p>
<p><b>update_gui_fields</b><br>
This method: <ul><li>determines if the current user, already known to the DMI,
has write permissions.  If the user does not have write permissions, the 
<b>apply</b> and <b>close</b> buttons are disabled, leaving only the 
<b>cancel</b> button enabled.</li><li>Fills in all the GUI fields according
to the data in the MeasType object.</li></ul>
</p><br>
At this point the GUI is essentially laid out and we wait for actions...  The 
major methods triggered by actions include: <ul>
<li><i>checkRequiredInput()</i></li>
<li><i>update_RiversideDB_objects()</i></li>
<li><i>update_database()</i></li></ul>
Each of these will be detailed below.

<p><b>checkRequiredInput</b><br>
This method simply goes through all the fields in the GUI and checks that 
each fields is: <ul> <li>filled in if it is a required field </li>
<li>contains valid values (for JTextFields, for example)</li>
</ul>If an invalid entry is encountered, the method displays a warning message,
indicating the fields with invalid values.
</ul>
</p>

<p><b>update_RiversideDB_objects</b><br>
This method:<ul>
<li><b>creates the <i>__gui</i> versions of the objects!</b> This includes: <ul><li>__gui_RTi_MeasType</li></ul> The 
<i>__gui</i> versions are created in this method by copying the <i>__db</i> 
versions (which were originally created and set up in the constructor)</li>
<li>calls the <i>verify_xxx</i> methods. These include:<ul>
<li><i>verify_top_fields()</i></li>
<li><i>verify_QA_QC_tab()</i></li>
<li><i>verify_transmitProt_tab()</i></li>
<li><i>verify_status_tab()</i></li>
<li><i>verify_TS1_tab()</i></li>
<li><i>verify_TS2_tab()</i></li>
The <i>verify_xxx</i> methods:<ul><li> fill in the <i>__gui</i> verions of the objects
in memory with the values that are currently set in the GUI</li>
<li>compares the values in the <i>__gui</i> version of the objects to the 
<i>__db</i> version of the objects.  If the <i>__gui</i> versions differ 
from the <i>__db</i> verions:<ul>
<li>the <i>__gui</i> versions are marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the 
<i>__gui</i> differs from the <i>__db</i> version (the <i>__db</i> version 
remember, represents how the object is in the database itself) </li></ul>
</li></ul></ul>
</p>
<p><b>update_database</b><br>
This method: <ul>
<li>makes a confirmation message to verify that the user wants to save the 
changes (and lists out all the changes from the <i>__dirty_vect</i>) <b>if</b>
a new MeasType was <b>not</b> created (if a new MeasType was created, 
we do not want to re-confirm every change)</li>
<li>writes the <i>__gui</i> version of the objects to the database</li>
<li>if the objects were successfully written to the database, the 
<i>__gui</i> objects are marked as <b>not</b> dirty 
( <i>setDirty(false)</i></li>
<li><i>__dirty_vect</i> is cleared out</li>
<li>if we created a new MeasType object (<i>__bln_new_object == true</i>),
add a new node on the JTree in the main application  - <b>or</b> -  update the 
existing MeasType node on the JTree with the new changes.</li>
</ul>
</p>

<br>
<p><b>ACTIONS and the events they trigger</b><br>
The main actions in the GUI are fired off when the user selects one of the
following buttons:<ul><li>cancel</li><li>close</li><li>apply</li></ul>
<ul><li><b>cancel</b><br>
Items that are checked before the GUI is closed:<ul>
<li>if the user was changing the properties of an existing MeasType, 
<ul><li><i>update_RiversideDB_objects()</i> is called to <b>create</b> and 
update the <i><b>__gui_RTi_MeasType</b></i> object in memory, mark it dirty, 
and add messages to the <i>__dirty_vect</i> Vector.</li>
<li>Print a confirmation message, confirming the user wants to cancel 
the changes (that are stored in the <i>__dirty_vect</i>) </li></ul> </li> </ul> </li>
<li><b>close</b> (close Button and "X" in application window bar)<br>
The method: <i>closeGUI()</i>is called which does the following:<ul>
<li>calls <i>checkRequiredInput</i> which makes sure all the required fields in the GUI are filled out and have valid entries</li>
<li>calls <i>update_RiversideDB_objects</i> which is called to 
<b>create</b> and update the <i><b>__gui_RTi_MeasType</b></i> object in 
memory, mark it dirty, and add messages to the <i>__dirty_vect</i> Vector.
</li>
<li>creates a confirmation message if the <i>__gui</i> versions are dirty, 
prompting the user to verify if they want to save their changes
(all the changes are listed out from the <i>__dirty_vect</i>)</li>
<li>updates the database by calling, <i>update_database</i></li>
<li>closes the GUI and destroys it</li>
</ul> </li>
<li><b>apply</b><br>
After the <b>apply</b> button is pressed, methods are called that follow a 
similar pattern as those called after the <b>close</b> button outlined above.
These methods are:<ul>
<li>checkRequired_input</li>
<li>update_RiversideDB_objects</li>
<li>update_database</li>
<li>Then, since the GUI is not being closed, the objects need to be updated
in memory to represent the new states.  Since the <i>__gui</i> objects have
been written to the database, the <i>__db</i> objects need to be updated to
represent the new database status.  To do so:<ul><li> the 
<i>__db_RTi_MeasType</i> object is re-created, using the copy 
constructor and passing in the <i>__gui_RTi_MeasType</i> object: <br>
<i>__db_RTi_MeasType = new RiversideDB_MeasType
( __gui_RTi_MeasType) </i> </li> </ul></li> </ul> </li> </ul>
*/
public class RiversideDB_TS_JFrame
	extends    RiversideDB_EditorBase_JFrame
	implements ActionListener,
		   ItemListener,
		   WindowListener
{

// Class name
private static String __class = "RiversideDB_TS_JFrame";

//variables passed in with constructor.
//RiversideDB_DMI object - already opened
private RiversideDB_DMI __dmi = null;

//System units to use: SI or ENGL
private String __system_units = null;

//Shared Layout parameters 
Insets __insets = null;
GridBagLayout __gridbag = null;

//indicated what the current action is- 
//ie, if a MeasType node is being added.  Will update tree if set to true.
private boolean __bln_new_object = false;

//Main Container components
private JPanel __main_JPanel = null;
private JPanel __top_TS_info_JPanel = null;
private JPanel __tabbedInfo_JPanel = null;
private JPanel __bottom_close_JPanel = null;

//Tabbed pane itself
private JTabbedPane __info_JTabbedPane = null;

//CLOSE PANEL
//components for close panel
private SimpleJButton __close_JButton = null;
private SimpleJButton __cancel_JButton = null;
private SimpleJButton __apply_JButton = null;

/////////////////////////////////////////////////////////
///  TIME SERIES / MEASUREMENT TYPES
/////////////////////////////////////////////////////////
//Holds name of Time Series that is selected in the JTree
private String __preselected_TSID_string = null;

//components for top panel for TS
private JLabel __TS_title_JLabel = null;
private JTextField __TS_location_JTextField = null;
private SimpleJComboBox __TS_dataType_JComboBox = null;
private JTextField __TS_subDataType_JTextField = null;
private SimpleJComboBox __TS_dataSource_JComboBox = null;
private SimpleJComboBox __TS_timeStepBase_JComboBox = null;
private SimpleJComboBox __TS_timeStepMultiplier_JComboBox = null;
private SimpleJComboBox __TS_scenario_JComboBox = null;
private JTextField __TS_tsid_JTextField = null;
private JTextField __TS_description_JTextField = null;
private SimpleJComboBox __TS_units_JComboBox = null;
private JTextField __TS_create_method_JTextField = null;

//vector of strings for MeasType.Time_step_base
private Vector __timestep_base_vect = new Vector();

//Strings for TIMESTEP_BASE options...
String TIME_STEPBASE_IRREGULAR = "IRREGULAR";
String TIME_STEPBASE_HOUR = "HOUR";
String TIME_STEPBASE_MINUTE = "MINUTE";
String TIME_STEPBASE_DAY = "DAY";
String TIME_STEPBASE_MONTH = "MONTH";
String TIME_STEPBASE_YEAR = "YEAR";

//////////////////////////////////////////////////////
// TIME SERIES TABS
//////////////////////////////////////////////////////

//TIME SERIES TAB: TIME SERIES 1
//Components
private SimpleJComboBox __TS1_tab_table_num1_JComboBox = null;
private SimpleJComboBox __TS1_tab_dbload1_JComboBox = null;

//TIME SERIES TAB: TIME SERIES 2
private SimpleJComboBox __TS2_tab_table_num2_JComboBox = null;
private SimpleJComboBox __TS2_tab_dbload2_JComboBox = null;

//DBLoad Methods for BOTH TimeSeries 1 and 2 
//can be: "1" or "2". 
// 1==Append and 2==Drop and Replace
static String DBLOAD_METHOD_1 = "1 - INSERT AND UPDATE";
static String DBLOAD_METHOD_2 = "2 - DROP AND REPLACE";

//Transmit Protocol TAB
private SimpleJComboBox __transProt_tab_transmit_JComboBox = null;

//STATUS TAB
//SimpleJComboBox __status_tab_status_JComboBox = null;
private JTextField __status_tab_status_JTextField = null;

//QA/QC TAB
private String __QA_QC_tab_string = "QA/QC";
//information label
private JLabel __QA_QC_tab_min_units_JLabel = null;
private JLabel __QA_QC_tab_max_units_JLabel = null;
private JTextField __QA_QC_tab_min_JTextField = null;
private JTextField __QA_QC_tab_max_JTextField = null;
private JCheckBox __QA_QC_tab_visible_JCheckBox = null;
private JCheckBox __QA_QC_tab_editable_JCheckBox = null;

//all the OBJECTS that are manipulated in this GUI.

//MeasType object that is currently being used...
RiversideDB_MeasType __db_RTi_MeasType = null;
//DataType
private Vector __RTi_DataType_vect = null;
//Tables
private Vector __RTi_Tables_vect = null;

//boolean to include extra confirmation dialogs before writing to the database
private boolean __cautious_mode = true;

//Object to be manipulated on
private RiversideDB_MeasType __gui_RTi_MeasType = null;

//Holds a Vector of status information-- each
//field that has been changed is recored in this vector.
private Vector __dirty_vect = new Vector();

//String for "NONE" selection in the SimpleJComboBox
String NONE_STRING = "NONE";

//unknown String
String UNKNOWN_STRING = "UNKNOWN";

//flag to indicate if current user has write permissions.
private boolean __canWriteMeasType = false; 

//Holds the DBUser object
private RiversideDB_DBUser __DBUser = null;

private boolean __originallyNewObject = false;

/**
RiversideDB_TS_JFrame constructor.  This GUI is based on the 
RiversideDB_MeasType table.
The constructor is called by an action in one of the JTrees of the 
main application.  The main application passes to this class a 
RiversideDB_MeasType.  This class is called in one of two cases: <br>
<ul><li>to view the properties of a current MeasType</li>
<li>to create a new MeasType</li></ul>
In the case of viewing a current MeasType, the MeasType object
passed to this class is already defined and all required properties 
already known.  In the case of creating a totally new MeasType, the 
MeasType passed in to this class is essentially an empty skeleton.  <br>
It is important to distinguish in the constructor, if we are dealing with
an existing MeasType and just changing some of its fields or if
we are creating a totally new MeasType.  If the fields used to create
the TSIdent for a MeasType are MISSING, such as the Data Type field,
for the MeasType passed in, then we know that we are
creating a new MeasType object.  In the constructor then, set
the <i>__bln_new_object = true </i> flag.  The <b><i>__bln_new_object</i></b>
flag is important because:<br>
<ul>
<li>we can mark the object as dirty (using the <i>setDirty(true)</i> flag)
 since it is an entirely new object</li>
<li>we need to add the fields for <i>DBUser</i>, <i>DBGroup</i>, and 
<i>DBPermissions</i> based on the information already known by the DMI for 
this user.</li>
<li> we do not have to confirm changes made to a totally new object like we do 
if the user is changing an existing object</li>
<li>we need to know to add a new node to the JTree</li></ul> <br><br> 
At this point, whether we are creating a new MeasType or modifying an
existing one, we assign the MeasType to the variable known throughout
this class as: <b><i>__gui_RTi_MeasType</i></b><br><br>
Finally, the constructor also:<ul><li> sets up Vectors of (static) reference 
data, read directly from the database thay will be used throughout the 
class</li> <li>calls method: <i>init_layout_GUI()</i> which creates and 
sets up the GUI components</li></ul><br> 
@param dmi Instance of RiversideDB_DMI that has already been opened.
@param windowManager RiversideDB_WindowManager to control the windows open.
@param title String for title of JFrame.
@param mt RiversideDB_MeasType object to display in this GUI. If a new 
MeasType is being created, the Data_Type field will be MISSING.
*/
public RiversideDB_TS_JFrame( 	RiversideDB_DMI dmi,
				RiversideDB_WindowManager windowManager,
				String title,
				RiversideDB_MeasType mt ) {
	super( title );
	String routine = __class + ".constructor";

	//add RTi icon
	JGUIUtil.setIcon( this, JGUIUtil.getIconImage() );

	// Set global variables
 	__dmi = dmi;
 	__db_RTi_MeasType = mt;
 	
 	// Set protected member in the base RiversideDB_EditorBase_JFrame class.
 	_windowManager = windowManager;

	//get current user
	try {
 		__DBUser = __dmi.getDBUser();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}

	//DataType objects
 	__RTi_DataType_vect = null;
	try { __RTi_DataType_vect = __dmi.readDataTypeList();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e);
		Message.printWarning( 2, routine, 
		"Unable to get list of Data Types." );

 		__RTi_DataType_vect = new Vector();
	}

	//set up the __RTi_Tables_vect Vector...
	try { __RTi_Tables_vect = __dmi.readTablesList();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		Message.printWarning( 2, routine, 
		"Error creating vector of " +
		"RiversideDB_Tables objects." );
 		__RTi_Tables_vect = new Vector();
	}

	//get Units
 	__system_units = IOUtil.getPropValue( "UNITS_SYSTEM" );
	if ( __system_units == null ) {
		Message.printWarning( 2, routine,
		"Unable to determine system units- will use \"ENGL\"." );
 		__system_units = "ENGL";
	}

	//get the particulare MEASType object that we are dealing with...
	//If we are creating a new MeasType, the fields used to make
	//the TSIDENT will be null.  These fields include the Data Type field.
	String type = null;
	type = __db_RTi_MeasType.getData_type();
	if ( ( type == null ) || ( type.length() <= 0 )) {
		//set Flag
 		__bln_new_object = true;
		__originallyNewObject = true;

		//mark new Object as Dirty
 		__db_RTi_MeasType.setDirty( true );

		//assign User permissions
 		__db_RTi_MeasType.
		setDBUser_num( __DBUser.getDBUser_num() );
 		__db_RTi_MeasType.
		setDBGroup_num( __DBUser.getPrimaryDBGroup_num());
 		__db_RTi_MeasType.setDBPermissions(
 		__DBUser.getDefault_DBPermissions() );

		//set permissions 
 		__db_RTi_MeasType.
		setTS_DBUser_num( __DBUser.getDBUser_num() );
 		__db_RTi_MeasType.
		setTS_DBGroup_num( __DBUser.getPrimaryDBGroup_num());
 		__db_RTi_MeasType.setTS_DBPermissions(
 		__DBUser.getDefault_DBPermissions() );

		//set Time Step multiplier to be 1 by default 
		//(otherwise defaults to -999 by database )
 		__db_RTi_MeasType.setTime_step_mult( 1 );

		//set create_method to unkown.
 		__db_RTi_MeasType.setCreate_method(  
		UNKNOWN_STRING );
		//set IsVisible to True
 		__db_RTi_MeasType.setIsVisible( "Y" );
		//set DataSource to be unknown
 		__db_RTi_MeasType.setSource_abbrev( UNKNOWN_STRING );
		//set TransmitProtocol to REGULAR
 		__db_RTi_MeasType.setTransmitProtocol( "REGULAR" );
		//set Max QA/QC to 99999
 		__db_RTi_MeasType.setMax_check( 99999 );
		//set Min QA/QC to -99999
 		__db_RTi_MeasType.setMin_check( -99999 );
		//set base string for the time step base
 		__db_RTi_MeasType.setTime_step_base( "DAY" );
	}

	//update the __preselected_TSID_string
	TSIdent tsid = null;
	try {
		tsid = __db_RTi_MeasType.toTSIdent();
	}
	catch ( Exception e) {
		Message.printWarning( 2, routine, e );
		tsid = null;
	}
	if ( tsid != null ) {
 		__preselected_TSID_string = tsid.toString();
	}
		
	tsid = null;

	//see if user has permissions to change objects.
	try {
 		__canWriteMeasType =
 		__dmi.canWrite( __db_RTi_MeasType.getDBUser_num(),
 		__db_RTi_MeasType.getDBGroup_num(),
 		__db_RTi_MeasType.getDBPermissions() );
	}
	catch ( Exception e ) {
		Message.printWarning ( 2, routine, e);
 		__canWriteMeasType = false;
	}

	// create/layout the GUI ...
	//Assign the shared variables used for layouts
	//layout manager is GridBagLayout
 	__gridbag = new GridBagLayout();

	//Insets order: top, left, bottom, right
 	__insets = new Insets( 2, 2, 2, 2);

	init_layout_GUI();

	//add window listener
	addWindowListener ( this );

	//setup frame to do nothing on close so that we can take over
	//control of window events.
	setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
	
	setResizable( false );

}

/**
Method add all the components to a JPanel for the QA/QC tab.
*/
public JPanel assemble_tab_QA_QC() {
	String routine = __class + ".assemble_tab_QA_QC";

	JPanel QA_QC_JPanel = new JPanel();
	QA_QC_JPanel.setLayout( __gridbag );

	//components
	//information JLabel 
	JLabel QA_QC_tab_info_JLabel = new JLabel(
	"Specify range limits for Data" );

	//Min allowed JTextFIeld - NOT REQUIRED
	JLabel QA_QC_tab_min_JLabel = new JLabel( "Min Allowed: " );
	QA_QC_tab_min_JLabel.setToolTipText("Minimum allowed value allowed when importing data (required)" );

 	__QA_QC_tab_min_JTextField = new JTextField( 10 ); 

	//get units for current selection
 	__QA_QC_tab_min_units_JLabel = new JLabel( "Units: " );

	//Max allowed JTextField - NOT REQUIRED
	JLabel QA_QC_tab_max_JLabel = new JLabel( "Max Allowed: " );
	QA_QC_tab_max_JLabel.setToolTipText("Maximum allowed value allowed when importing data (required)" );

 	__QA_QC_tab_max_JTextField = new JTextField( 10 );

	//get units for current selection
 	__QA_QC_tab_max_units_JLabel = new JLabel("");

	//visible JCheckBox with Label
 	__QA_QC_tab_visible_JCheckBox = new JCheckBox(
	"Time Series is visible in RiverTrak" ); 
 	__QA_QC_tab_visible_JCheckBox.setToolTipText("Inidicates if Time Series is visible in viewing tools (required)");

	//visible JCheckBox with Label
 	__QA_QC_tab_editable_JCheckBox = new JCheckBox(
	"Time Series is editable in RiverTrak" );
 	__QA_QC_tab_editable_JCheckBox.setToolTipText("Indicates whether time series are editable in the viewing tools- uneditable once set");

	//assemble Panel
	try {
		//information label
		int y =0;
		int x =0;
		//spacer
		JGUIUtil.addComponent( 
			QA_QC_JPanel, 
			new JLabel("                                "),
			x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

		x=1;
		JGUIUtil.addComponent( 
			QA_QC_JPanel, QA_QC_tab_info_JLabel,
			x, y, 3, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

		
		//min
		JGUIUtil.addComponent( 
			QA_QC_JPanel, QA_QC_tab_min_JLabel,
			x, ++y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );
		JGUIUtil.addComponent( 
			QA_QC_JPanel, __QA_QC_tab_min_JTextField,
			++x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );
		JGUIUtil.addComponent( 
			QA_QC_JPanel, __QA_QC_tab_min_units_JLabel,
			++x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );
		//max
		x=1;
		JGUIUtil.addComponent( 
			QA_QC_JPanel, QA_QC_tab_max_JLabel,
			x, ++y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );
		JGUIUtil.addComponent( 
			QA_QC_JPanel, __QA_QC_tab_max_JTextField,
			++x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );
		JGUIUtil.addComponent( 
			QA_QC_JPanel, __QA_QC_tab_max_units_JLabel,
			++x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );


		//visible JCheckBox
		x=1;
		JGUIUtil.addComponent( 
			QA_QC_JPanel, __QA_QC_tab_visible_JCheckBox,
			x, ++y, 3, 1, 1, 0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

		//editable JCheckBox
		JGUIUtil.addComponent( 
			QA_QC_JPanel, __QA_QC_tab_editable_JCheckBox,
			x, ++y, 3, 1, 1, 0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );
	}
	
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		Message.printWarning( 2, routine, 
		"Unable to lay out QA/QC tab" );
	}

	return QA_QC_JPanel;
} //end assemble_tab_QA_QC

/**
Method adds all the components to a JPanel for the Transmit Protocal Data tab. 
@return JPanel with all the components.
*/
public JPanel assemble_tab_transmitProt() {
	String routine = __class + ".assemble_tab_transmitProt";

	JPanel transProt_JPanel = new JPanel();
	transProt_JPanel.setLayout( __gridbag );

	//components
	//description labels
	JLabel transProt_tab_desc1_JLabel = new JLabel( 
	"Indicate how data are transmitted, which determines how missing data are handled." ); 
	JLabel transProt_tab_desc2_JLabel = new JLabel( "For example: ");
	JLabel transProt_tab_desc3_JLabel = new JLabel( 
	"Missing data for REGULAR transmit private result in missing values." );
	JLabel transProt_tab_desc4_JLabel = new JLabel( 
	"Missing data in ALERT transmit private result in known values being carried forward." );

	//Label for TransmitProtocol - NOT REQUIRED
	JLabel transProt_tab_transmit_JLabel = new JLabel( "Transmit Protocol: " );
	transProt_tab_transmit_JLabel.setToolTipText("Data Transmission method (see manual for more details) (reuqired)");

	//MeasTransProtocol
	Vector RTi_MeasTransProtocol_vect = null;
	try {	RTi_MeasTransProtocol_vect = __dmi.readMeasTransProtocolList();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		Message.printWarning( 2, routine, 
		"Error creating vector of " +
		"RiversideDB_MeasTransProtocol objects." );

		RTi_MeasTransProtocol_vect = new Vector();
	}

	//use the RTi_MeasTransProtocol_vect to fill the comboBox
	//of transmit protocol methods.
	Vector prot_vect = new Vector();
	String prot_str = null;
	RiversideDB_MeasTransProtocol mtp = null;
	for ( int i=0; i<RTi_MeasTransProtocol_vect.size(); i++ ) {
		mtp = (RiversideDB_MeasTransProtocol) 
			RTi_MeasTransProtocol_vect.elementAt(i);
		if ( mtp == null ) {
			continue;
		}
		prot_str = mtp.getProtocol() + " - " + mtp.getDescription();
		if ( prot_str != null ) {
			if ( prot_str.length() > 45 ) {
				prot_str = prot_str.substring( 0, 45 ) + "...";
			}
			prot_vect.addElement( prot_str );
		}
		mtp = null;
		prot_str = null;
	}
	RTi_MeasTransProtocol_vect = null;

	//now make the COMBOBOX - NOT REQUIRED ( "none" option )
 	__transProt_tab_transmit_JComboBox = 
	new SimpleJComboBox( prot_vect );

	//assemble
	try {
		//Info
		int y =0;
		//add descriptions
		JGUIUtil.addComponent( 
			transProt_JPanel, transProt_tab_desc1_JLabel,
			0, y, 3, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

		JGUIUtil.addComponent( 
			transProt_JPanel, transProt_tab_desc2_JLabel,
			0, ++y, 3, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

		JGUIUtil.addComponent( 
			transProt_JPanel, transProt_tab_desc3_JLabel,
			0, ++y, 3, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

		JGUIUtil.addComponent( 
			transProt_JPanel, transProt_tab_desc4_JLabel,
			0, ++y, 3, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

		//TransmitProtocol METHODS
		JGUIUtil.addComponent( 
			transProt_JPanel, transProt_tab_transmit_JLabel,
			0, ++y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

		JGUIUtil.addComponent( 
			transProt_JPanel, __transProt_tab_transmit_JComboBox,
			1, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		Message.printWarning( 2, routine, 
			"Error laying out \"transProt\" tab." );
	}

	return transProt_JPanel;

} //end assemble_tab_transmitProt

/**
Method adds all the components to a JPanel for the STATUS Data tab.
@return JPanel with all the components.
*/
public JPanel assemble_tab_status() {
	String routine = __class + ".assemble_tab_status";

	JPanel status_JPanel = new JPanel();
	status_JPanel.setLayout( __gridbag );

	//components
	//info label
	JLabel status_tab_info_JLabel = new JLabel( "Status feature is not implemented" );

	//jLabel for textfield - NOT REQUIRED
	JLabel status_tab_status_JLabel = new JLabel( "Status:" );
	 status_tab_status_JLabel.setToolTipText("Currently not implemented.");
	
	//not used right now, so make it UNEDITABLE AND 
	//put NOT AVAILABLE in the JTextField
 	__status_tab_status_JTextField = new JTextField( "NOT AVAILABLE" );
 	__status_tab_status_JTextField.setEditable( false );
 	__status_tab_status_JTextField.setBackground( Color.lightGray );
 	

	//assemble
	try {
		//Info
		int y =0;
		JGUIUtil.addComponent( 
			status_JPanel, status_tab_info_JLabel,
			0, y, 2, 1, 1, 0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.CENTER );

		//JLabel for Status JTextField
		JGUIUtil.addComponent( 
			status_JPanel, status_tab_status_JLabel,
			0, ++y, 1, 1, 1, 0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );


		//JTextField
		JGUIUtil.addComponent( 
			status_JPanel, 
 			__status_tab_status_JTextField,
			1, y, 1, 1, 1, 0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		Message.printWarning( 2, routine, 
			"Error laying out \"Status\" tab." );
	}

	return status_JPanel;

} //end assemble_tab_status

/**
Method adds all the components to a JPanel for the
TIME SERIES (1) Data tab.  This information is from the
database table: MeasType.Table_num1 and MeasType.Dbload_method1.
@return JPanel with all the components.
*/
public JPanel assemble_tab_TS1( ) {
	String routine = __class + ".assemble_tab_TS1";

	JPanel ts1_JPanel = new JPanel();
	ts1_JPanel.setLayout( __gridbag );

	//components
	//Informative label
	JLabel ts1_tab_info_JLabel = new JLabel( 
	"Specify the database table to store the time series data and the method to load the data" );

	//Label for Dbload_1 method.
	JLabel ts1_tab_dbload1_JLabel = new JLabel( "Load Method:" );
	ts1_tab_dbload1_JLabel.setToolTipText("Indicates how time series should be loaded into database (required)");

	//DBLoad Vector is composed of 2 choices
	//"1 - Insert and Update" and "2 - Drop and Replace"
	Vector dbload_vect = new Vector();
	dbload_vect.addElement( DBLOAD_METHOD_1 );
	dbload_vect.addElement( DBLOAD_METHOD_2 );

	//combobox for dbload methods
 	__TS1_tab_dbload1_JComboBox = new SimpleJComboBox( dbload_vect );

	//Label for Table_num1 drop-down list
	JLabel ts1_tab_table_num1_JLabel = new JLabel(
	"Time Series Data Table:" );
	ts1_tab_table_num1_JLabel.setToolTipText("Database table name that stores the time series data (required)");

	//Table_nums vect should come from the __RTi_Tables_vect already created.
	Vector v = null;
	v = setup_tables_vect();
	//now make the ComboBox for Table nums!  
 	__TS1_tab_table_num1_JComboBox = new SimpleJComboBox( v );

	//assemble
	try {
		//TITLE
		int y =0;
		int x =0;
		JGUIUtil.addComponent( 
			ts1_JPanel, ts1_tab_info_JLabel,
			x, y, 2, 1, 1, 0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.CENTER );

		//TABLE NUMBER1 COMBO BOX
		JGUIUtil.addComponent( 
			ts1_JPanel, ts1_tab_table_num1_JLabel,
			x, ++y, 1, 1, 1, 0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );
			//GridBagConstraints.WEST );

		JGUIUtil.addComponent( 
			ts1_JPanel, __TS1_tab_table_num1_JComboBox,
			++x, y, 1, 1, 1, 0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

		//DBLOAD1 METHODS
		x=0;
		JGUIUtil.addComponent( 
			ts1_JPanel, ts1_tab_dbload1_JLabel,
			x, ++y, 1, 1, 1, 0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );
			//GridBagConstraints.WEST );

		JGUIUtil.addComponent( 
			ts1_JPanel, __TS1_tab_dbload1_JComboBox,
			++x, y, 1, 1, 1, 0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		Message.printWarning( 2, routine, 
			"Error laying out \"Time Series (1)\" tab." );
	}

	
	return ts1_JPanel;

} //end assemble_tab_TS1

/**
Method adds all the components to a JPanel for the
TIME SERIES (2) Data tab.  This information is from the
database table: MeasType.Table_num2 and MeasType.Dbload_method2.
@return JPanel with all the components.
*/
public JPanel assemble_tab_TS2( ) {
	String routine = __class + ".assemble_tab_TS2";

	JPanel TS2_JPanel = new JPanel();
	TS2_JPanel.setLayout( __gridbag );

	//components
	//title
	JLabel TS2_tab_info_JLabel = new JLabel( 
	"Specify the second database table to store the time series data and the method to load the data" );
	
	//Label for Dbload_2 method.
	JLabel TS2_tab_dbload2_JLabel = new JLabel( "Load Method:" );
	TS2_tab_dbload2_JLabel.setToolTipText("Indicates how second copy of time series should be loaded into database (optional)");
	//DBLoad Vector is composed of 2 choices
	//"1 - Insert and Update" and "2 - Drop and Replace"
	Vector dbload_vect = new Vector();
	dbload_vect.addElement( DBLOAD_METHOD_1 );
	dbload_vect.addElement( DBLOAD_METHOD_2 );

	//combobox for dbload methods
 	__TS2_tab_dbload2_JComboBox = new SimpleJComboBox( dbload_vect );

	//Label for Table_num2 drop-down list.....
	JLabel TS2_tab_table_num2_JLabel = new JLabel( 
	"Time Series Data Table:" );
	TS2_tab_table_num2_JLabel.setToolTipText("Database table name that stores copied time series data (optional)");

	Vector v =  null;
	v = setup_tables_vect();
	v.insertElementAt(NONE_STRING, 0 );
	//now make the ComboBox for Table nums!  - 
 	__TS2_tab_table_num2_JComboBox = new SimpleJComboBox( v );

	//assemble
	try {
		int y = 0;
		int x = 0;
		//TITLE
		JGUIUtil.addComponent( 
			TS2_JPanel, TS2_tab_info_JLabel,
			x, y, 2, 1, 1, 0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.CENTER );

		//TABLE NUMBER1 COMBO BOX
		JGUIUtil.addComponent( 
			TS2_JPanel, TS2_tab_table_num2_JLabel,
			x, ++y, 1, 1, 1, 0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );
			//GridBagConstraints.WEST );

		JGUIUtil.addComponent( 
			TS2_JPanel, __TS2_tab_table_num2_JComboBox,
			++x, y, 1, 1, 1, 0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

		//DBLOAD2 METHODS
		x=0;
		JGUIUtil.addComponent( 
			TS2_JPanel, TS2_tab_dbload2_JLabel,
			x, ++y, 1, 1, 1, 0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );
			//GridBagConstraints.WEST );

		JGUIUtil.addComponent( 
			TS2_JPanel, __TS2_tab_dbload2_JComboBox,
			++x, y, 1, 1, 1, 0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		Message.printWarning( 2, routine, 
			"Error laying out \"Time Series (2)\" tab." );
	}

	return TS2_JPanel;

} //end assemble_tab_TS2

/**
This method does the following:<ul>
<li>calls <i>checkRequiredInput</i> which makes sure all the required fields in the GUI are filled out and have valid entries</li>
<li>calls <i>update_RiversideDB_objects</i> which is called to 
<b>create</b> and update the <i><b>__gui_RTi_MeasType</b></i>
object in memory, mark it dirty, and add messages to the
<i>__dirty_vect</i> Vector. </li>
<li>creates a confirmation message if the <i>__gui</i> versions are dirty, 
prompting the user to verify if they want to save their changes
(all the changes are listed out from the <i>__dirty_vect</i>)</li>
<li>updates the database by calling, <i>update_database</i></li>
<li>closes the GUI and destroys it</li>
</ul>
*/
public void closeGUI() {

	String routine = __class + ".closeGUI";
	boolean blnUpdated = true;
	//required fields
	try {
		checkRequiredInput();
	}
	catch ( Exception e ) {
		//then there was an error so do not 
		//update object in memory or in database.
		Message.printWarning( 2, routine, e );

		blnUpdated= false;
	}

	//update the object in memory
	if ( blnUpdated ) {
		try {
			update_RiversideDB_objects();
		}
		catch( Exception e ) {
			Message.printWarning( 2, routine, e );
			blnUpdated=false;
		}
	}

	boolean blnClose = true;
	if( blnUpdated)  {
		if( __gui_RTi_MeasType.isDirty() ) {

			//prompt user if they want to save their
			//changes to the database.
			int x = 0;
			x = new ResponseJDialog ( this,
				"Save Changes",
				"Save your changes before closing?", 
				ResponseJDialog.YES | ResponseJDialog.NO
				| ResponseJDialog.CANCEL).response();
	
			if ( x == ResponseJDialog.YES ) {
				//update database itself
				try {
					//update database itself
					update_database();
				}
				catch( Exception e ) {
					Message.printWarning( 2, routine, e );
					blnUpdated=false;
				}

				//setVisible(false);
				//dispose();
				windowManagerClose();
			}
			else if ( x == ResponseJDialog.NO ) {
 				__dirty_vect.clear();
				windowManagerClose();
				//setVisible(false);
				//dispose();
			}
		}
		else { //no changes, so just close.
			windowManagerClose();
			//setVisible(false);
			//dispose();
		}
	}
	else {
		if ( blnUpdated ) {
			setVisible(false);
			dispose();
		}
		// windowManagerClose();
	}
} //end closeGUI

/**
This method simply goes through all the fields in the GUI and checks that 
each fields is: <ul> <li>filled in if it is a required field </li>
<li>contains valid values (for JTextFields, for example)</li>
</ul>If an invalid entry is encountered, the method displays a warning message,
indicating the fields with invalid values. (Prints one error message at the end indicating any fields that need to
be filled in before a save can occur.)
</ul>
@exception Exception thrown if a required filled does not
have a value.
*/
protected void checkRequiredInput() throws Exception {
	String routine = __class + ".checkRequiredInput";

	StringBuffer buffer = new StringBuffer();

	//location - noneditable, can't be null.
	//DataType - JComboBox - can't be null
	//DataSource - JComboBox- can't be null
	//TimeStepBase - JComboBox -can't be null
	//Time Step Multiplier - JComboBox - can't be null;
	//Make sure if TimeStepBase is "IRREGULAR" that Multiplier is "1"
	
	//Description
	String gui_desc = null;
	gui_desc = __TS_description_JTextField.getText().trim();
	if (( gui_desc == null ) || ( gui_desc.length()<=0 )) {
		buffer.append( "Unable to save without " +
		"\"Description\" \n" );
	}

	//units - should not be null, but sometimes the combobox is
	//created w/o any items
	String gui_units = null;
	gui_units = ((String) __TS_units_JComboBox.getSelected()).trim();
	if (( gui_units == null ) || ( gui_units.length()<=0 )) {
		buffer.append( "Unable to save without " +
		"\"Units\" \n" );
	}
	
	//QA/QC MIN
	String gui_min = null;
	gui_min = __QA_QC_tab_min_JTextField.getText().trim();
	if (( gui_min == null ) || ( gui_min.length()<=0 )) {
		buffer.append( "Unable to save without "+
		"\"QA/QC Minimum Value\" \n" );
	}
	else {
		if (!StringUtil.isDouble( gui_min ) ) {
			buffer.append( "Unable to save. " +
			"\"QA/QC Minimum Value\" must be a double \n" );
		}
	}
	//QA/QC Max
	String gui_max = null;
	gui_max = __QA_QC_tab_max_JTextField.getText().trim();
	if (( gui_max == null ) || ( gui_max.length()<=0 )) {
		buffer.append( "Unable to save without " +
		"\"QA/QC Maximum Value\" \n" );
	}
	else {
		if (!StringUtil.isDouble( gui_max ) ) {
			buffer.append( "Unable to save. " +
			"\"QA/QC Maximum Value\" must be a double \n" );
		}
	}
	
	//QA/QC Visible flag -can't be null since it is a JCheckBox
	//Transmit Protocol - JComboBox - can't be null.
	//Data Storage: Data Table (TS1) -JComboBox - can't be null
	//Data Storage: Load Method (TS1) -JComboBox - can't be null
	
	//check length of StringBuffer.
	//If it is longer than 0, then add final statement
	//and printWarning
	if ( buffer.length() >0 ) {
		buffer.append( "Please specify all required fields or cancel." );
		JOptionPane.showMessageDialog( this, 
		buffer.toString(), "Warning", JOptionPane.WARNING_MESSAGE);

		throw new Exception ( 
		"Please specify all required fields or cancel." );
	}
	buffer = null;

}//end checkRequieredInput

/**
Creates the __tabbedInfo_JPanel panel that holds the JTabbedPanes that
contain information regarding the Time Series for the MeasType Identifier 
selected in the JTree.  The tabs consist of the following topics: 
Time Series, Time Series 2, Transmit Protocol, Status, QA/QC.
*/
private void create_tabbed_panel( ) {
	String routine = __class + ".create_tabbed_panel";

	//create the overall panel to hold things.
 	__tabbedInfo_JPanel = new JPanel();
 	__tabbedInfo_JPanel.setLayout( new GridBagLayout() );

	//create the tabbed panes
 	__info_JTabbedPane = new JTabbedPane();

	//don't add change listener until GUI constructed.

	//CASE 0 - 1st tab
	//QA/QC
	JPanel QA_QC_JPanel = null;
	QA_QC_JPanel = assemble_tab_QA_QC( );
	if ( QA_QC_JPanel != null ) {
		//add files panel to tab
 		__info_JTabbedPane.addTab( 
 		__QA_QC_tab_string, QA_QC_JPanel );
 		__info_JTabbedPane.setToolTipTextAt( 0, "Specify valid data range");
	}

	//CASE 1 - 2nd tab
	//Transmit Protocol
	JPanel transProt_JPanel = null;
	transProt_JPanel = assemble_tab_transmitProt();
	if ( transProt_JPanel != null ) {
		//add files panel to tab
 		__info_JTabbedPane.addTab( "Transmit Protocol", transProt_JPanel);
 		__info_JTabbedPane.setToolTipTextAt( 1, "Specify data trasmission method");
	}

	//CASE 2 - 3rd tab
	//Status
	JPanel status_JPanel = null;
	status_JPanel = assemble_tab_status();
	if ( status_JPanel != null ) {
		//add files panel to tab
 		__info_JTabbedPane.addTab( 
 		"Status", status_JPanel );
 		__info_JTabbedPane.setToolTipTextAt( 2, "Specify status unavialable");

	}

	//CASE 3 - 4th tab
	//Time Series Tab
	//Creates a panel to hold all the info for the Time Series 1 data tab 
	JPanel ts1_JPanel = null;
	ts1_JPanel = assemble_tab_TS1( );
	if ( ts1_JPanel != null ) {
		//add TS1 panel to tab
 		__info_JTabbedPane.addTab( "Data Storage 1", ts1_JPanel );
 		__info_JTabbedPane.setToolTipTextAt( 3, "Specify time series load method and storage table");
	}

	//CASE 4 - 5th tab
	//Time Series 2 Tab
	JPanel TS2_JPanel = null;
	TS2_JPanel = assemble_tab_TS2( );
	if ( TS2_JPanel != null ) {
		//add TS2 panel to tab
 		__info_JTabbedPane.addTab( "Data Storage 2", TS2_JPanel );
 		__info_JTabbedPane.setToolTipTextAt( 4, "Specify copied time series load method and storage table");

	}

	//ASSEMBLE this portion of GUI
	//top, left, bottom, right
	try {
		//add tabbed pane to main panel
		JGUIUtil.addComponent( 
 		__tabbedInfo_JPanel, __info_JTabbedPane,
			0, 0, 2, 1, 1, 0, __insets,
			GridBagConstraints.BOTH,
			GridBagConstraints.CENTER );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e);
		Message.printWarning( 2, routine, 
		"Error laying out information panel with tabbed panes." );
	}

} //end create_tabbed_panel

/**
Creates the topmost panel of the MeasType Configuration GUI 
for MeasType Time Series.  The specific data is filled in the 
update_GUI_fields() method. The Fields include:
<P><TABLE> <TD>TITLE</TD> <TD>Data Type</TD> <TD>Subdata Type</TD> 
<TD>Data Source</TD> <TD>Time Step Base</TD> <TD>Time Step Multiplier</TD> 
<TD>Scenario</TD> <TD>Description</TD> <TD>Units</TD> </TABLE></P>
*/
private void create_top_panel( ) {
	String routine = __class + ".create_top_panel";

 	__top_TS_info_JPanel = new JPanel();
 	__top_TS_info_JPanel.setLayout( __gridbag );

	//components are:
	//Title,Location, Data Type, SubDataType, Data Source, 
	//Time Step Base, Time Step Multiplier, Scenario, tsid
	//Description, Units
	
	//TItle
// REVISIT [LT] 205-01-26 - The __preselected_TSID_string is only initialize during
//			    construction, and never updated. It is used in many places
//			    principally for comments and it was used here to display
//			    this title, which seems to be redundant, since we have the 
//			    Time Series Identifiers which is properly updated as the 
//			    selection in the dialog box changes. If proved unnecessary
//			    delete for good.	
//	__TS_title_JLabel = new JLabel( __preselected_TSID_string );

	//Location 
	JLabel TS_location_JLabel = new JLabel( "Location: " );
	TS_location_JLabel.setToolTipText("<HTML>Location matches that of the measurement location that is<BR>associated with the time series and cannot be changed</HTML>");

 	__TS_location_JTextField = new JTextField( 35 );
	//Uneditble field 
 	__TS_location_JTextField.setEditable( false );
 	__TS_location_JTextField.setBackground( Color.lightGray );

	//We Already have vector of data type objects.
	//get list of all datatypes with description

	Vector datatype_vect =  new Vector();
	RiversideDB_DataType dt = null;
	String datatype_str = null;
	for ( int i=0; i< __RTi_DataType_vect.size(); i++ ) {
		dt = (RiversideDB_DataType) __RTi_DataType_vect.elementAt(i);
		if ( dt == null ) {
			continue;
		}
		//get data type with description
		datatype_str = dt.getDataType() + " - " + dt.getDescription();

		if  ( datatype_str != null ) {
			if ( datatype_str.length() > 45 ) {
				datatype_str = 
				datatype_str.substring(0, 45) + "...";
			}
			//add them to the vector
			datatype_vect.addElement( datatype_str );
		}
		dt = null;
		datatype_str = null;
	}

	//Data TYPE  - REQUIRED
	JLabel TS_dataType_JLabel = new JLabel( "Data Type:" );
	TS_dataType_JLabel.setToolTipText("Data type for the time series (required)");

	//make DataType Combobox - required field ( no "none" choice )
 	__TS_dataType_JComboBox = new SimpleJComboBox( datatype_vect );
 	__TS_dataType_JComboBox.addItemListener(this);

	//SUB DATA TYPE - NOT REQUIRED
	JLabel TS_subDataType_JLabel = new JLabel( "Sub Data Type:" );
	TS_subDataType_JLabel.setToolTipText("Sub data type used in cases where the data type does not uniquely identify a time series (optional)");

 	__TS_subDataType_JTextField = new JTextField( 20 );
 	__TS_subDataType_JTextField.addActionListener( this );

	//DATA SOURCE - NOT REQUIRED
	JLabel TS_dataSource_JLabel = new JLabel( "Data Source:" );
	TS_dataSource_JLabel.setToolTipText("Agency or creator of the data (required)");

	Vector RTi_DataSource_vect = null;
	try {	
		RTi_DataSource_vect = __dmi.readDataSourceList();
	}
	catch( Exception e ) {
		Message.printWarning( 2, routine, e );
		Message.printWarning( 2, routine,
		"Unable to get list of data sources. ");
		RTi_DataSource_vect = new Vector();
	} 
	
	//We already have the non-null vector of DataSource objects
	//to use to make a list of Source choices.
	RiversideDB_DataSource ds = null;
	String source_str = null;
	Vector source_vect = new Vector();
	for ( int i=0; i<RTi_DataSource_vect.size(); i++ ) {
		ds = (RiversideDB_DataSource) RTi_DataSource_vect.elementAt(i);
		if ( ds == null ) {
			continue;
		}
		source_str = ds.getSource_abbrev() + " - " + 
			ds.getSource_name();
		if ( source_str != null ) {
			if ( source_str.length() > 45 ) {
				source_str = 
				source_str.substring( 0, 45 ) + "...";
			}
			source_vect.addElement( source_str );
		}

		source_str = null;
		ds = null;
	}
	RTi_DataSource_vect =null;

	//make ComboBox - required field ( no "none" choice )
 	__TS_dataSource_JComboBox = new SimpleJComboBox( source_vect );
 	__TS_dataSource_JComboBox.addItemListener( this );

	//TIME STEP BASE - REQUIRED
	JLabel TS_timeStepBase_JLabel = new JLabel( "Time Step Base:" );
	TS_timeStepBase_JLabel.setToolTipText("<HTML>Used with the Time Step Multiplier (below)<BR>to indicate time interval between data values (required)</HTML>");

	if ( __timestep_base_vect != null ) {
 		__timestep_base_vect = new Vector();
	}

 	__timestep_base_vect.addElement( TIME_STEPBASE_DAY );
 	__timestep_base_vect.addElement( TIME_STEPBASE_HOUR );
 	__timestep_base_vect.addElement( TIME_STEPBASE_IRREGULAR );
 	__timestep_base_vect.addElement( TIME_STEPBASE_MINUTE );
 	__timestep_base_vect.addElement( TIME_STEPBASE_MONTH );
 	__timestep_base_vect.addElement( TIME_STEPBASE_YEAR );

	//make combobox - required ( no "none" option )
 	__TS_timeStepBase_JComboBox = new SimpleJComboBox( __timestep_base_vect );
 	__TS_timeStepBase_JComboBox.addItemListener(this);
	
	//get array of units for the first item in the __timestep_base_vect
	//since that is the one that will be selected by default, before
	//the GUI fields are updated.
	int [] arrMults = null;
	arrMults = TimeInterval.multipliersForIntervalBase(
	TIME_STEPBASE_DAY, true, false );

	//TIME STEP MULTIPLIER - REQUIRED
	JLabel TS_timeStepMultiplier_JLabel = new JLabel( "Time Step Multiplier:" );
	TS_timeStepMultiplier_JLabel.setToolTipText("<HTML>Used in conjunction with the Time Step Base (above)<BR>to indicate time interval between datat values (required)</HTML>");

 	__TS_timeStepMultiplier_JComboBox = new SimpleJComboBox( );
	for ( int i=0; i<arrMults.length; i++ ) {
 		__TS_timeStepMultiplier_JComboBox.addItem( String.valueOf(
		arrMults[i]) );
	}
 	__TS_timeStepMultiplier_JComboBox.addItemListener( this );

	//SCENARIO - NOT required
	JLabel TS_scenario_JLabel = new JLabel( "Scenario:" );
	TS_scenario_JLabel.setToolTipText("Indicates a variation on base data (see manual) (optional)" );

	//make a vector with just the Scenario ID and description-
	//Scenario
	Vector RTi_Scenario_vect = null;
	try {	RTi_Scenario_vect = __dmi.readScenarioList();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		Message.printWarning( 2, routine, 
		"Error creating vector of "+
		"RiversideDB_Scenario objects." );

		RTi_Scenario_vect = new Vector();
	}

	//removing any duplicates using the Vector of Scenario Objects.
	//Also, add an empty string as the LAST element since
	//the scenario can be blank.
	RiversideDB_Scenario ms = null;
	Vector scenario_vect = new Vector();
	String scen_str = null;
	for ( int i=0; i<RTi_Scenario_vect.size(); i++ ) {
		ms = (RiversideDB_Scenario) RTi_Scenario_vect.elementAt(i);
		if ( ms != null ) {
			scen_str = ms.getIdentifier() + " - " +
				ms.getDescription();
			if ( scen_str != null ) {
				if ( scen_str.length() > 45 ) {
					scen_str = 
					scen_str.substring( 0, 45 ) + "...";
				}

				scenario_vect.addElement( scen_str );
			}
		}
		ms = null;
		scen_str = null;
	}
	RTi_Scenario_vect = null;
	//now add the NONE string to the END of the vector
	scenario_vect.addElement( NONE_STRING );

	//now  make the combobox - NOT required ( "none" string )
 	__TS_scenario_JComboBox = new SimpleJComboBox( scenario_vect );
 	__TS_scenario_JComboBox.addItemListener( this );

	//TSID
	JLabel TS_tsid_JLabel = new JLabel( "Time Series Identifier:" );
	TS_tsid_JLabel.setToolTipText(
		"Non-editable time series identifier formed from the above fields" );
 	__TS_tsid_JTextField = new JTextField( 40 );
 	__TS_tsid_JTextField.setEditable( false );
 	__TS_tsid_JTextField.setBackground( Color.lightGray );

	//DESCRIPTION - NOT REQUIRED
	JLabel TS_description_JLabel = new JLabel( "Description:" );
	TS_description_JLabel.setToolTipText(
		"Full description of the time series location and data type (required)");
 	__TS_description_JTextField = new JTextField( 40 );

	//UNITS - REQUIRED
	JLabel TS_units_JLabel = new JLabel( "Units" );  // REVISIT LT
	TS_units_JLabel.setToolTipText("Units for the data type (required)");

	//Get Units to go in JComboBox
	//get dimension for the datatype selected.
	int sel_dataType_int = -999;
	sel_dataType_int = __TS_dataType_JComboBox.getSelectedIndex();

	dt = null;
	dt = (RiversideDB_DataType) __RTi_DataType_vect.elementAt(sel_dataType_int);

	// now use that to get the Dimesnion
	String dim = null;
	dim = dt.getDimension();

	//use dimension to get list of units for 
	Vector units_vect = new Vector();
	Vector v = DataUnits.lookupUnitsForDimension( __system_units, dim );

	int size = 0;
	if ( v != null ) {
		size = v.size();
	}

	DataUnits du = null;
	for ( int i=0; i< size; i++ ) {
		du = (DataUnits)v.elementAt(i);
		units_vect.addElement( du.getAbbreviation() + 
		" - " + du.getLongName() );
	}
	if ( size == 0 ) {
		units_vect.addElement( "      " );
	}
	//now sort the units 
	Vector sorted_units_vect = null;
	sorted_units_vect = StringUtil.sortStringList( units_vect,
		StringUtil.SORT_ASCENDING, null, false, true );
	units_vect = null;
	if ( sorted_units_vect == null ) {
		sorted_units_vect = new Vector();
	}
	String dt_str = (String) __TS_dataType_JComboBox.getSelected();
	if ( dt_str.startsWith("FLOODMONITOR") ) {
		sorted_units_vect.removeAllElements();
		sorted_units_vect.add("MULTIPLE");
	}

	// REQUIRED - no "none" options
	// REVISIT [LT] 2005-01-26 If the units is not defined for the startup 
	// 		timeseries the sorted_units_vect will be empty and  
	//		nothing will show up in the selection box. This problems
	//		was observed for Polochic when the startup was
	//		CONDUCTIVIT and NA was the unit defined in RiversideDB
	// 		Solution - Te RivesideDB must be set properly (MT)
 	__TS_units_JComboBox = new SimpleJComboBox( sorted_units_vect );
 	__TS_units_JComboBox.addItemListener( this );

	//Label for Create_method drop-down list.....  REQUIRED
	JLabel TS_create_method_JLabel = new JLabel( "Create Method:" );
	TS_create_method_JLabel.setToolTipText("Non-editable field indicates how the data is generated");

	//create method - Uneditable JLabel
 	__TS_create_method_JTextField = new JTextField( 25 );
 	__TS_create_method_JTextField.setEditable( false );
	//set to unkown to start
 	__TS_create_method_JTextField.setText( UNKNOWN_STRING );

	//Layout GUI
	try {
		//ASSEMBLE GUI PANEL
		//now add all pieces to main container panel
		
		int y = 0;
		int x = 0;
// REVISIT [LT] 205-01-26 - The __preselected_TSID_string is only initialize during
//			    construction, and never updated. It is used in many places
//			    principally for comments and it was used here to display
//			    this title, which seems to be redundant, since we have the 
//			    Time Series Identifiers which is properly updated as the 
//			    selection in the dialog box changes. If proved unnecessary
//			    delete for good.		
		//TITLE
/*		JGUIUtil.addComponent( 
 		__top_TS_info_JPanel, __TS_title_JLabel,
		x, y, 4, 1, 1, 0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );
*/

		//LOCATION
		JGUIUtil.addComponent( 
 		__top_TS_info_JPanel, TS_location_JLabel,
		x, ++y, 1, 1, 1, 0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

		JGUIUtil.addComponent( 
 		__top_TS_info_JPanel, __TS_location_JTextField,
		++x, y, 1, 1, 1, 0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

		//DATA TYPE
		x=0;
		JGUIUtil.addComponent( 
 		__top_TS_info_JPanel, TS_dataType_JLabel,
		x, ++y, 1, 1, 1, 0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

		JGUIUtil.addComponent( 
 		__top_TS_info_JPanel, __TS_dataType_JComboBox,
		++x, y, 1, 1, 1, 0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

		//SUB DATA TYPE
		x=0;
		JGUIUtil.addComponent( 
 		__top_TS_info_JPanel, TS_subDataType_JLabel,
		x, ++y, 1, 1, 1, 0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

		JGUIUtil.addComponent( 
 		__top_TS_info_JPanel, __TS_subDataType_JTextField,
		++x, y, 1, 1, 1, 0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

		//DATA SOURCE
		x=0;
		JGUIUtil.addComponent( 
 		__top_TS_info_JPanel, TS_dataSource_JLabel,
		x, ++y, 1, 1, 1, 0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

		JGUIUtil.addComponent( 
 		__top_TS_info_JPanel, __TS_dataSource_JComboBox,
		++x, y, 1, 1, 1, 0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );
		
		//TIME STEP BASE
		x=0;
		JGUIUtil.addComponent( 
 		__top_TS_info_JPanel, TS_timeStepBase_JLabel,
		x, ++y, 1, 1, 1, 0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

		JGUIUtil.addComponent( 
 		__top_TS_info_JPanel, __TS_timeStepBase_JComboBox,
		++x, y, 1, 1, 1, 0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

		//TIME STEP MULTIPLIER
		x=0;
		JGUIUtil.addComponent( 
 		__top_TS_info_JPanel, TS_timeStepMultiplier_JLabel,
		x, ++y, 1, 1, 1, 0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

		JGUIUtil.addComponent( 
 		__top_TS_info_JPanel, __TS_timeStepMultiplier_JComboBox,
		++x, y, 1, 1, 1, 0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

		//SCENARIO
		x=0;
		JGUIUtil.addComponent( 
 		__top_TS_info_JPanel, TS_scenario_JLabel,
		x, ++y, 1, 1, 1, 0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

		JGUIUtil.addComponent( 
 		__top_TS_info_JPanel, __TS_scenario_JComboBox,
		++x, y, 1, 1, 1, 0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

		//TSID
		x=0;
		JGUIUtil.addComponent( 
 		__top_TS_info_JPanel, TS_tsid_JLabel,
		x, ++y, 1, 1, 1, 0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

		JGUIUtil.addComponent( 
 		__top_TS_info_JPanel, __TS_tsid_JTextField,
		++x, y, 1, 1, 1, 0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

		//DESCRIPTION
		x=0;
		JGUIUtil.addComponent( 
 		__top_TS_info_JPanel, TS_description_JLabel,
		x, ++y, 1, 1, 1, 0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

		JGUIUtil.addComponent( 
 		__top_TS_info_JPanel, __TS_description_JTextField,
		++x, y, 1, 1, 1, 0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

		//UNITS
		x=0;
		JGUIUtil.addComponent( 
 		__top_TS_info_JPanel, TS_units_JLabel,
		x, ++y, 1, 1, 1, 0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

		JGUIUtil.addComponent( 
 		__top_TS_info_JPanel, __TS_units_JComboBox,
		++x, y, 1, 1, 1, 0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

		//Create Method
		x=0;
		JGUIUtil.addComponent( 
 		__top_TS_info_JPanel, TS_create_method_JLabel,
		x, ++y, 1, 1, 1, 0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

		JGUIUtil.addComponent( 
 		__top_TS_info_JPanel, __TS_create_method_JTextField,
		++x, y, 1, 1, 1, 0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, 
			"Error laying out the top portion of the GUI " +
			"with the MeasType Measurement Type (Time Series) " +
			"information." );
		Message.printWarning( 2, routine, e );
	}

}

/**
Finalizes and cleans up.
*/
protected void finalize() throws Throwable
{
 	__insets = null;
 	__gridbag = null;
 	__RTi_DataType_vect = null;
 	__dmi = null;
 	__main_JPanel = null;
 	__top_TS_info_JPanel = null;
 	__tabbedInfo_JPanel = null;
 	__bottom_close_JPanel = null;
 	__info_JTabbedPane = null;
 	__close_JButton = null;
 	__cancel_JButton = null;
 	__apply_JButton = null;
 	__TS_title_JLabel = null;
 	__TS_location_JTextField = null;
 	__TS_dataType_JComboBox = null;
 	__TS_subDataType_JTextField = null;
 	__TS_dataSource_JComboBox = null;
 	__TS_timeStepBase_JComboBox = null;
 	__TS_timeStepMultiplier_JComboBox = null;
 	__TS_scenario_JComboBox = null;
 	__TS_tsid_JTextField = null;
 	__TS_description_JTextField = null;
 	__TS_units_JComboBox = null;
 	__TS_create_method_JTextField = null;
 	__timestep_base_vect = new Vector();
 	__TS2_tab_table_num2_JComboBox = null;
 	__TS2_tab_dbload2_JComboBox = null;
 	__transProt_tab_transmit_JComboBox = null;
 	__status_tab_status_JTextField = null;
 	__QA_QC_tab_min_units_JLabel = null;
 	__QA_QC_tab_max_units_JLabel = null;
 	__QA_QC_tab_min_JTextField = null;
 	__QA_QC_tab_max_JTextField = null;
 	__QA_QC_tab_visible_JCheckBox = null;
 	__QA_QC_tab_editable_JCheckBox = null;
 	__db_RTi_MeasType = null;
 	__RTi_DataType_vect = null;
 	__RTi_Tables_vect = null;
 	
 	// Finalize the base RiversideDB_EditorBase_JFrame class
	super.finalize();
}

/**
This method is called from the constructor to create and layout the
GUI components.  It calls the method: <i>create_main_panel</i>, which
in turn calls methods named such as: <i>assemble_tab_QA_QC()</i>,
<i>assemble_tab_transmitProt</i>, etc.  These methods all create 
GUI components and put them together in a <i>GridBagLayout</i>. They
do not worry about setting correct values in the components' fields, but just 
gets the components set up.  The main components in the GUI consists of:
<ul><li>JPanel that contains general information pertinent to the MeasType 
object at the top of the GUI</li>
<li>a series of tabs in a JTabbedPane with fields for the MeasType.
Tab topics include: <ul><li>QA QC </li> <li>Transmit Protocal </li> 
<li>Status </li><li> time series 1</li><li>time series 2</li> </ul> </li>
<li>a panel added at the bottom that includes the standard buttons for: 
<ul><li>apply</li><li>close</li><li>cancel</li></ul></li></ul>
At the end of <i>init_layout_gui</i>, a 
call to <i>update_gui_fields</i> is made which fills in all the 
fields of the GUI according to the MeasType object currently being worked with.
*/
private void init_layout_GUI( ) {
	String routine = __class + ".init_layout_GUI";
	try {
	
		//Create overall Main Panel
 		__main_JPanel = new JPanel();
 		__main_JPanel.setLayout( __gridbag );

		//create Top Panel
		create_top_panel( );

		//create the panel that holds the tabbed panes with 
		//additional info about the selected product.
		//Creates the JPanel: __tabbedInfo_JPanel
		create_tabbed_panel( );

		//create close Buttons
		//close panel
		JPanel close_JPanel = new JPanel();
		close_JPanel.setLayout( new GridBagLayout() );

 		__close_JButton = new SimpleJButton( "Close", this );
 		__cancel_JButton = new SimpleJButton( "Cancel", this );
 		__apply_JButton = new SimpleJButton( "Apply", this );
	
		JGUIUtil.addComponent( 
			close_JPanel, __apply_JButton,
			0, 0, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );
	
		JGUIUtil.addComponent( 
			close_JPanel, __close_JButton,
			1, 0, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.CENTER );
		JGUIUtil.addComponent( 
			close_JPanel, __cancel_JButton,
			2, 0, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );
	
		//NOW that the specific panels have 
		//been created, fill in the values
		//for all the FIELDS!
		update_GUI_fields();

		//now assemble GUI
		int vcnt = 0;

		if ( __top_TS_info_JPanel != null ) {
			JGUIUtil.addComponent( 
 			__main_JPanel, __top_TS_info_JPanel,
			0, vcnt, 1, 1, 1, 0, __insets,
			GridBagConstraints.BOTH,
			GridBagConstraints.CENTER );

			vcnt++;
		}

		if ( __tabbedInfo_JPanel != null ) {
			JGUIUtil.addComponent( 
 			__main_JPanel, __tabbedInfo_JPanel,
			0, vcnt, 1, 1, 1, 0, __insets,
			GridBagConstraints.BOTH,
			GridBagConstraints.CENTER );

			vcnt++;
		}

		JGUIUtil.addComponent( 
 		__main_JPanel, close_JPanel,
			0, vcnt, 1, 1, 1, 0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.CENTER );

			vcnt++;
		
		//pack and set visible
		getContentPane().add( "Center", __main_JPanel );	
		pack();
		JGUIUtil.center( this );
		setVisible( true );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine,
		"Error laying out GUI." );
		Message.printWarning( 2, routine, e );
	}
} //end init_layout_GUI

/**
Method creates a vector of Tables, the contents of which depends 
on the database version.  The tables are have isTSTemplate set
to true ("Y") and have names that start with "TS" are included.
@return Vector with applicable tables.
*/
protected Vector setup_tables_vect() {
	String routine = __class + ".setup_tables_vet";

	//earlier versions of DB, did not have the isTSTemplate field
	boolean with_template = false;
	try {
		with_template = __dmi.isDatabaseVersionAtLeast( 20800 );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}

	//just read in all tables.
	Vector v = new Vector();
	int size =0;
	if ( __RTi_Tables_vect != null ) {
		size = __RTi_Tables_vect.size();
	}

	//get the tables to put into the __tables_vect
	//which is used for the JComboBox
	RiversideDB_Tables rt = null;
	String desc = null;
	String name = null;
	for ( int i=0;i<size;i++ ) {
		rt = (RiversideDB_Tables) __RTi_Tables_vect.elementAt(i);
		if ( rt== null ) {
			continue;
		}
		name = rt.getTable_name();
		desc = rt.getDescription();
		String name_desc = null;
		if ( desc != null ) {
			name_desc = name + " - "  + desc;
		}
		else {
			name_desc = name;
		}
		if ( name_desc.length() > 40 )  {
			name_desc = name_desc.substring(0,40) + "... ";
		}
		String tstemplate = "";
		tstemplate=rt.getIsTSTemplate();

		if ( with_template ) {

			//SHOULD BE::: 
			//if "Y", then add, not if"N"
			if ( rt.getIsTSTemplate().equalsIgnoreCase("N") ) {
				if ( name.startsWith( "TS" ) ) {
					v.addElement( 
					rt.getTable_num() + " - " +
					name_desc  );
				}
			}
		}
		else { //working with older DB that did not have
			//isTSTemplate field.
			v.addElement( name_desc );
		}
	}//end loop
	return v; 
} //end setup_tables_vect

/**
This method: <ul>
<li>makes a confirmation message to verify that the user wants to save the 
changes (and lists out all the changes from the <i>__dirty_vect</i>) <b>if</b>
a new MeasType was <b>not</b> created (if a new MeasType was created, 
we do not want to re-confirm every change)</li>
<li>writes the <i>__gui</i> version of the objects to the database</li>
<li>if the objects were successfully written to the database, the 
<i>__gui</i> objects are marked as <b>not</b> dirty 
( <i>setDirty(false)</i></li>
<li><i>__dirty_vect</i> is cleared out</li>
<li>if we created a new MeasType object (<i>__bln_new_object == true</i>),
add a new node on the JTree in the main application  - <b>or</b> -  update the 
existing MeasType node on the JTree with the new changes.</li>
</ul>
@exception Exception thrown if error encountered.
*/
protected void update_database( ) throws Exception
{
	String routine = __class + ".update_database", mssg;

	//holds messages from __dirty_vect
	StringBuffer b = new StringBuffer();
	for ( int i=0; i< __dirty_vect.size(); i++ ) {
		if ( i == ( __dirty_vect.size())-1 ) {
			b.append( (String) __dirty_vect.elementAt(i)  );
		}
		else {
			b.append( (String) __dirty_vect.elementAt(i) + "\n" );
		}
	}

	//if we are running in cautious mode and if we 
	//are changing an already existing object ( not a completely new one),
	//then prompt the user before writing to the database
	if( ( __cautious_mode ) && ( ! __bln_new_object ) ) {
		if ( __gui_RTi_MeasType.isDirty()) {
			//write out a confirmation message.
			int x = new ResponseJDialog( 
			this, "Confirm Changes to be saved to database", 
			"Confirm Changes:\n" +b.toString(),
			ResponseJDialog.YES | ResponseJDialog.NO ).response();
	
			if ( x == ResponseJDialog.YES ) {
				Message.printStatus( 1, routine, 
				"Saving changes to the database. ");
	
				//write to log file
				Message.printStatus( 5, routine, 
				"User confirmed changes: " +b.toString() ); 
	
			}	
			else {
				//write to log file
				Message.printStatus( 5, routine, 
				"User denied changes: " +b.toString() ); 
				//empty out dirty vector
 				__dirty_vect.clear();
				throw new Exception (
				"User choose not to write changes "+
				"back to database." );
			}
		}
	}
	RiversideDB_MeasType mt = null;
	if ( __gui_RTi_MeasType.isDirty() ) {
		if ( Message.isDebugOn ) {
			Message.printDebug( 5, routine,
			"Writing MeasType object to the database." );
		}
			
		try {
			//write object to database
			
			mt = __dmi.writeMeasType( __gui_RTi_MeasType );

			//object no longer dirty
 			__gui_RTi_MeasType.setDirty( false );
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e );
			Message.printWarning( 1, routine, 
			"The write method failed for this MeasType object. " +
			"Unable to update the database.\nSee log file " +
			"for details.", this );
			//leave object as dirty
 			__gui_RTi_MeasType.setDirty( true );

			throw new Exception (
			"The write method failed for this MeasType object. " +
			"Unable to update the database.\nSee log file " +
			"for details.");
		}
	}
	else {
		Message.printStatus( 2, routine,
		"No changes were made to the MeasType object to update." );
	}

	//update title on JFrame
	//This applies if we have added a completely new Time Series and
	//if we have changed the TSID of an already existing Time Series
	this.setTitle( "RiverTrak® Assistant - Time Series " +
	"Properties - " + __TS_tsid_JTextField.getText().trim() );

	//now see if we are adding a completely new node - if so,
	//need to update the JTree!
	if ( __bln_new_object ) {
		//update MeasType number
 		__gui_RTi_MeasType.setMeasType_num( mt.getMeasType_num() );
		mt = null;

		addMeasTypeNode( __gui_RTi_MeasType );
	}
	else { //update existing node on tree

		//update Node on tree to reflect any changes in name
		String gui_name = null;
 		__TS_tsid_JTextField.setText( update_tsid_field() );

		//update tree
 		updateMeasTypeNode( __gui_RTi_MeasType,
 				    __db_RTi_MeasType );
	}

	//empty out dirty vector
 	__dirty_vect.clear();

	//update the flag to indicate we are not working on a new TS now.
 	__bln_new_object = false;
	
} //end update_database

/**
This method:<ul>
<li><b>creates the <i>__gui</i> versions of the objects!</b> This includes:
<ul><li>__gui_RTi_MeasType</li></ul> The <i>__gui</i> version is created in 
this method by copying the <i>__db</i> version (which was originally created
and set up in the constructor)</li>
<li>calls the <i>verify_xxx</i> methods. These include:<ul>
<li><i>verify_top_fields()</i></li>
<li><i>verify_QA_QC_tab()</i></li>
<li><i>verify_transmitProt_tab()</i></li>
<li><i>verify_status_tab()</i></li>
<li><i>verify_timeseries1_tab()</i></li>
<li><i>verify_timeseries2_tab()</i></li> </ul>
The <i>verify_xxx</i> methods:<ul><li> fill in the <i>__gui</i> version of 
the object in memory with the values that are currently set in the GUI</li>
<li>compares the values in the <i>__gui</i> version of the objects to the 
<i>__db</i> version of the object.  If the <i>__gui</i> version differs 
from the <i>__db</i> verion:<ul>
<li>the <i>__gui</i> version is marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the 
<i>__gui</i> differs from the <i>__db</i> version (the <i>__db</i> version 
remember, represents how the object is in the database itself) </li></ul>
</li></ul></ul>
@exception Exception thrown if error occurs
*/
protected void update_RiversideDB_objects( ) throws Exception {
	update_RiversideDB_objects( false );
}

/**
This method:<ul>
<li><b>creates the <i>__gui</i> versions of the objects!</b> This includes:
<ul><li>__gui_RTi_MeasType</li></ul> The <i>__gui</i> version is created in 
this method by copying the <i>__db</i> version (which was originally created
and set up in the constructor)</li>
<li>calls the <i>verify_xxx</i> methods. These include:<ul>
<li><i>verify_top_fields()</i></li>
<li><i>verify_QA_QC_tab()</i></li>
<li><i>verify_transmitProt_tab()</i></li>
<li><i>verify_status_tab()</i></li>
<li><i>verify_timeseries1_tab()</i></li>
<li><i>verify_timeseries2_tab()</i></li> </ul>
The <i>verify_xxx</i> methods:<ul><li> fill in the <i>__gui</i> version of 
the object in memory with the values that are currently set in the GUI</li>
<li>compares the values in the <i>__gui</i> version of the objects to the 
<i>__db</i> version of the object.  If the <i>__gui</i> version differs 
from the <i>__db</i> verion:<ul>
<li>the <i>__gui</i> version is marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the 
<i>__gui</i> differs from the <i>__db</i> version (the <i>__db</i> version 
remember, represents how the object is in the database itself) </li></ul>
</li></ul></ul>
@param suppress_warnings False if no warnings are to be displayed to the
user (used by the cancel button methods).
@exception Exception thrown if error occurs
*/
protected void update_RiversideDB_objects( boolean suppress_warnings) 
throws Exception {
	String routine = __class + ".update_RiversideDB_objects";

	//We have to go through the entire GUI to gather, verify, and set
	//its information
	boolean blnContinue = true;
	
	//make copy of the object in memory to work on.
 	__gui_RTi_MeasType = new RiversideDB_MeasType( __db_RTi_MeasType );

	try {
		verify_top_fields();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e);
		blnContinue = false;
	}

	//Also, verify that there are not already MeasTypes with this 
	//same name 
	if( __gui_RTi_MeasType.isDirty() && ( __bln_new_object ) ) {
		String tsid = null;
 		__TS_tsid_JTextField.setText( update_tsid_field() );
		tsid = __TS_tsid_JTextField.getText().trim();

		Vector v = null;
		try {
			//query using the TSIdent value
			v = __dmi.readMeasTypeListForTSIdent( tsid );
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e);
		}
		int size=0;
		if ( v != null ) {
			size = v.size();
		}

		//should be size 1 or 0 
		RiversideDB_MeasType mt = null;
		if ( size == 0 ) {
			//then we have no MeasType with that tsid
		}
		else if ( size == 1 ) {
			mt = ( RiversideDB_MeasType ) v.elementAt(0);
			if (mt.getMeasType_num() != 
 			__gui_RTi_MeasType.getMeasType_num() ){
	
				if ( !suppress_warnings) {
					Message.printWarning( 1, routine,
					"Cannot define a time series with a " +
					"Time Series Identifier\n" +
					"that matches an existing time "+
					"series.\nPlease change the "+
					"definition of this time series or " +
					"cancel.", this );
		
					//clear out dirty vector 
 					__dirty_vect.clear();
	
					throw new Exception (
					"Cannot define a time series with a " +
					"Time Series Identifier\n(\"" + tsid + 
					"\" that matches an existing time "+
					"series.\nPlease change the "+
					"definition of this time series " +
					"or cancel." );
				}
			}
		}
		v = null;
	}
	
	if ( blnContinue ) {
		//Verify ALL the TABS
		int numb_tabs = 0;
		if ( __info_JTabbedPane != null ) {
			numb_tabs = __info_JTabbedPane.getTabCount();
		}
		for ( int i=0; i<numb_tabs; i++ ) {
			//Send in each tab to be verified
			if ( blnContinue ) {
				try {
					verify_tab_info( i );
				}
				catch ( Exception e ) {
					Message.printWarning( 2, routine, e);
					blnContinue = false;
				}
			}
		}
	}
	
} //end update_RiversideDB_objects

/**
This method: <ul><li>determines if the current user, already known to the DMI,
has write permissions.  If the user does not have write permissions, the 
<b>apply</b> and <b>close</b> buttons are disabled, leaving only the 
<b>cancel</b> button enabled.</li><li>Fills in all the GUI fields according
to the data in the MeasType object.</li></ul>
*/
protected void update_GUI_fields( ) {
	String routine = __class + ".update_GUI_fields";

	if ( ! __canWriteMeasType ) {
 		JGUIUtil.setEnabled(__apply_JButton, false );	
 		JGUIUtil.setEnabled(__close_JButton, false );	
	}

// REVISIT [LT] 205-01-26 - The __preselected_TSID_string is only initialize during
//			    construction, and never updated. It is used in many places
//			    principally for comments and it was used here to display
//			    this title, which seems to be redundant, since we have the 
//			    Time Series Identifiers which is properly updated as the 
//			    selection in the dialog box changes. If proved unnecessary
//			    delete for good.
/*	if ( __TS_title_JLabel != null ) {
 		__TS_title_JLabel.setText( __preselected_TSID_string );
	} */

	//////////////////////////////////
	///////// TOP Panel /////////////
	//////////////////////////////////
	//use the information from the MeasType object to fill in the
	//top fields.  The TSID, we already have, since it was 
	//preselected in the Tree.
	
	//if we are creating a new TS, __db_RTi_MeasType
	//maybe null.

	//LOCATION
	//use the measloc id to get the MeasLoc object
	//associated with this MeasType-- get MeasLoc
	//ID and name.
	RiversideDB_MeasLoc ml = null;
	long ml_num = -999;
	ml_num = __db_RTi_MeasType.getMeasLoc_num();
	//with ml_num, get MeasLoc object
	try {
		ml = __dmi.readMeasLocForMeasLoc_num( ml_num );
	}
	catch (Exception e ) {
		Message.printWarning( 2, routine, e);
		ml = null;
	}

	String name= null;
	name = __db_RTi_MeasType.getMeasLoc_name();
	if ( name != null ) {
 		__TS_location_JTextField.setText( 
		ml.getIdentifier() + " - " + name );
	}
	else {
 		__TS_location_JTextField.setText( ml.getIdentifier() );
	}
	ml=null;

	//DATA TYPE
	String type_string = null;
	type_string = __db_RTi_MeasType.getData_type();
	if ( Message.isDebugOn ) {
		Message.printDebug( 3, routine,
		"Data Type for \"" + __preselected_TSID_string + 
		"\" is \"" + type_string + "\"." );
	}

	//now need to select the item in the JComboBox
	try {
		JGUIUtil.selectTokenMatches( __TS_dataType_JComboBox, 
		true, " - ", 0, 0, type_string, "" );
	}
	catch ( Exception e ) {
		Message.printWarning( 50, routine, e);
	}

	//Data Sub Type - not required
	String subtype_string = null;
	subtype_string = __db_RTi_MeasType.getSub_type();

	if ( Message.isDebugOn ) {
		Message.printDebug( 3, routine,
		"Data Sub Type for \"" + __preselected_TSID_string + 
		"\" is \"" + subtype_string + "\"." );
	}
	if ( subtype_string != null ) {
 	__TS_subDataType_JTextField.  setText(subtype_string);
	}
			
	//Data Source
	String source_string = null;
	source_string = __db_RTi_MeasType.getSource_abbrev();

	if ( Message.isDebugOn ) {
		Message.printDebug( 
		3, routine, "Data Source for \"" + 
 		__preselected_TSID_string + "\" is \"" + source_string + "\"." );
	}
	
	//now need to select the item in the JComboBox
	try { 
		JGUIUtil.selectTokenMatches( __TS_dataSource_JComboBox, 
		true, " - ", 0, 0, source_string, NONE_STRING );
	}
	catch ( Exception e ) {
		Message.printWarning( 50, routine, e);
	}

	//Time Step Base
	String base_string = null;
	base_string = __db_RTi_MeasType.getTime_step_base();

	if ( Message.isDebugOn ) {
		Message.printDebug( 3, routine,
		"Time Step Base for \"" + __preselected_TSID_string + 
		"\" is \"" + base_string + "\"." );
	}
	
	//select item in JComboBox
 	__TS_timeStepBase_JComboBox.setSelectedItem( base_string );

	//update the list of multipliers valid for this time step base.
	int [] arrMults = null;
	arrMults = TimeInterval.multipliersForIntervalBase( base_string,
	true, false );

	if ( arrMults == null ) {
		arrMults = new int[1];
		arrMults[0]=0;
	}

 	__TS_timeStepMultiplier_JComboBox.removeAllItems();
	for ( int i=0; i<arrMults.length; i++ ) {
 		__TS_timeStepMultiplier_JComboBox.addItem(
		String.valueOf( arrMults[i] ) );
	}

	//Time Step Multiplier
	long mult_long = -999;
	mult_long = __db_RTi_MeasType.getTime_step_mult();
	if ( Message.isDebugOn ) {
		Message.printDebug( 3, routine,
		"Time Step Multiplier for \"" + 
 		__preselected_TSID_string + 
		"\" is \"" + mult_long +"\"." );
	}
	
	//if ( mult_long != DMIUtil.MISSING_LONG ) {}
	if ( !DMIUtil.isMissing( mult_long ) ) {
 		__TS_timeStepMultiplier_JComboBox.setSelectedItem( 
		String.valueOf( mult_long ) );
	}
	else { //isMissing, so let it default to 1 
 		__TS_timeStepMultiplier_JComboBox.setSelectedItem( "1" );
	}

	//Scenario
	String scen_string = null;
	scen_string = __db_RTi_MeasType.getScenario();
	if ( Message.isDebugOn ) {
		Message.printDebug( 
		2, routine, "Scenario for \"" + 
 		__preselected_TSID_string + "\" is \"" + scen_string + "\"." );
	}
	//if ( scen_string == DMIUtil.MISSING_STRING )  {}
	if ( DMIUtil.isMissing( scen_string ) ) {
		scen_string = "NONE";
	}
	//now need to select the item in the JComboBox
	try {
		JGUIUtil.selectTokenMatches( __TS_scenario_JComboBox, true, 
		" - ", 0, 0, scen_string, "" );
	}
	catch ( Exception e ) {
		Message.printWarning( 50, routine, e);
	}

	//Tsid - field that compiled by adding all the above fields
	//to get the TSID
	String tsid_str = null;
	tsid_str = update_tsid_field();
 	__TS_tsid_JTextField.setText(tsid_str);

	//Description
	String desc_string = null;
	desc_string = __db_RTi_MeasType.getDescription();
	if ( Message.isDebugOn ) {
		Message.printDebug( 3, routine,
		"Description for \"" + 
 		__preselected_TSID_string + "\" is \"" + desc_string + "\"." );
	}
 	__TS_description_JTextField.setText(desc_string);

	//Units- get Units for actual object
	String units_string = null;
	units_string = __db_RTi_MeasType.getUnits_abbrev();
	if ( Message.isDebugOn ) {
		Message.printDebug( 3, routine, "Units for \"" + 
 		__preselected_TSID_string + "\" is \"" + units_string + "\"." );
	}
	
	//try to find the units in the JComboBox list and select them.
	try {
		JGUIUtil.selectTokenMatches( __TS_units_JComboBox, true, 
		" - ", 0, 0, units_string, "" );
	}
	catch ( Exception e ) {
		Message.printWarning( 50, routine, e);
	}

	//now get create method
	String cm_string = null;
	cm_string = __db_RTi_MeasType.getCreate_method();
	if ( Message.isDebugOn ) {
		Message.printDebug( 3, routine,
		"Create method for \"" + __preselected_TSID_string + 
		"\" is \"" + cm_string + "\"." );
	}
	if (( cm_string == null ) || ( cm_string.length() <= 0 )) {
		//select UNKOWN
 		__TS_create_method_JTextField.setText( UNKNOWN_STRING );
	}
	else {
 		__TS_create_method_JTextField.setText(cm_string);
	}

	//NOW work on TABs
	///////// TS TAB 1 and TS Tab 2 //////////
	boolean with_template = false;
	try {
		with_template = __dmi.isDatabaseVersionAtLeast( 20800 );
	}
	catch ( Exception e ) { Message.printWarning( 2, routine, e );
	}	

	///////// TS TAB 1 //////////
	//TABLE_NUM1
	long table_num1 = -999;
	table_num1 = __db_RTi_MeasType.getTable_num1();
	/*
	if ( with_template ) {
		//enable "New Table" button
		//JGUIUtil.setEnabled(_TS1_tab_newTable_JButton, true );
	}
	else {
		//disable "New Table" button
		//JGUIUtil.setEnabled_TS1_tab_newTable_JButton, false );
	}
	*/
	if ( Message.isDebugOn ) {
		Message.printDebug( 3, routine,
		"Table_num1 for \"" + __preselected_TSID_string + 
		"\" is \"" + table_num1 + "\"." );
	}

	try {
		JGUIUtil.selectTokenMatches(
 		__TS1_tab_table_num1_JComboBox, true, " - ", 0, 0, 
		String.valueOf(table_num1), "" );
	}
	catch ( Exception e ) {
		Message.printWarning( 50, routine, e);
	}

	//DBLOAD_METHOD1
	long load_meth1= -999;
	load_meth1 = __db_RTi_MeasType.getDbload_method1();

	if ( Message.isDebugOn ) {
		Message.printDebug( 3, routine,
		"Dbload_method1 for \"" + __preselected_TSID_string + 
		"\" is \"" + load_meth1 + "\"." );
	}
	try {
		JGUIUtil.selectTokenMatches(
 		__TS1_tab_dbload1_JComboBox, 
		true, " - ", 0, 0, String.valueOf(load_meth1), "" );
	}
	catch ( Exception e ) {
		Message.printWarning( 50, routine, e);
	}

	///////// TS 2 TAB //////////
	//TABLE_NUM2 - can be NONE
	/*
	if ( with_template ) {
		//enable "New Table" button
		//JGUIUtil.setEnabled(_TS2_tab_newTable_JButton, true );
	}
	else {
		//disable "New Table" button
		//JGUIUtil.setEnabled(_TS2_tab_newTable_JButton, false );
	}
	*/

	long table_num2= -999;
	table_num2 = __db_RTi_MeasType.getTable_num2();
	if ( Message.isDebugOn ) {
		Message.printDebug( 3, routine,
		"Table_num2 for \"" + __preselected_TSID_string + 
		"\" is \"" + table_num2+ "\"." );
	}
	try {
		JGUIUtil.selectTokenMatches(
 		__TS2_tab_table_num2_JComboBox, true, " - ", 0, 0, 
		String.valueOf(table_num2), NONE_STRING );
	}
	catch ( Exception e ) {
		Message.printWarning( 50, routine, e);
	}

	//DBLOAD_METHOD2 - optional
	long load_meth2 = -999;
	load_meth2 = __db_RTi_MeasType.getDbload_method2();
	if ( Message.isDebugOn ) {
		Message.printDebug( 3, routine,
		"Dbload_method2 for \"" + __preselected_TSID_string + 
		"\" is \"" + load_meth2 + "\"." );
	}
	//try and select value
	try {
		JGUIUtil.selectTokenMatches(
 		__TS2_tab_dbload2_JComboBox, true, " - ", 0, 0, 
		String.valueOf(load_meth2), NONE_STRING );
	}
	catch ( Exception e ) {
		Message.printWarning( 50, routine, e);
	}

	///////// TransProt TAB //////////
	//"Transmit Protocol"
	String prot_string = null;
	prot_string = __db_RTi_MeasType.getTransmitProtocol();

	//select Item in the Combobox
	try {
		JGUIUtil.selectTokenMatches(
 		__transProt_tab_transmit_JComboBox, true,
		" - ", 0, 0, prot_string,"" );
	}
	catch ( Exception e ) {
		Message.printWarning( 50, routine, e);
	}

	///////// STATuS TAB //////////
	//Status Tab is currently not being used.
	/*
	if ( __status_tab_status_JTextField != null ) {
 		__status_tab_status_JTextField.setText("");
	}
	*/

	//////////////////////////////////
	///////// QA/QC TAB //////////
	//////////////////////////////////
	//MIN ALLOWED
	double min_check = -999;
	min_check = __db_RTi_MeasType.getMin_check();

	//if ( min_check != DMIUtil.MISSING_DOUBLE ) {}
	if ( !DMIUtil.isMissing( min_check )) {
		if ( Message.isDebugOn ) {
			Message.printDebug( 3, routine,
			"Min_check for \"" + __preselected_TSID_string + 
			"\" is \"" + min_check+ "\"." );
		}
		String min_str = null;
		min_str = String.valueOf(min_check);
		//set Precision
		min_str = StringUtil.formatString( min_check, "%.2f" );
 		__QA_QC_tab_min_JTextField.setText( min_str );
	}
	else { //is missing, so set to Missing value
 		__QA_QC_tab_min_JTextField.setText( String.valueOf(
		DMIUtil.MISSING_DOUBLE) );
	}

	//now update the Units
	String u = null;
	u =(String) __TS_units_JComboBox.getSelected();
	if ( u == null ) {
		u= "";
	}
	int ind = -999;
	ind = u.indexOf(" -");
	if ( ind > 0 ) {
		u = (u.substring(0, ind)).trim();
	}
 	__QA_QC_tab_min_units_JLabel.setText(u);

	//MAX ALLOWED
	double max_check = 999;
	max_check = __db_RTi_MeasType.getMax_check();
	//if ( max_check != DMIUtil.MISSING_DOUBLE ) {}
	if ( !DMIUtil.isMissing( max_check ) ) {
		if ( Message.isDebugOn ) {
			Message.printDebug( 3, routine,
			"Max_check for \"" + __preselected_TSID_string + 
			"\" is \"" + max_check + "\"." );
		}
		//format string
		String max_str = null;
		max_str = String.valueOf( max_check);
		max_str = StringUtil.formatString( max_check, "%.2f" );
	
		//set Text in GUI 
 		__QA_QC_tab_max_JTextField.setText( max_str );
	}
	else {  //is Missing, so set text to missing
 		__QA_QC_tab_max_JTextField.setText( String.valueOf(
		DMIUtil.MISSING_DOUBLE )) ;
	}

	//"u" already defined above for min units
 	__QA_QC_tab_max_units_JLabel.setText(u);

	//Visible JCheckBox
	//String should be "N" for NO and "Y" for YES -make "Y" the default
	String visible = null;
	visible = __db_RTi_MeasType.getIsVisible();

	if ( Message.isDebugOn ) {
		Message.printDebug( 3, routine,
		"IsVisible for \"" + __preselected_TSID_string + "\" is \"" + 
		visible + "\"." );
	}
	if ( visible.equalsIgnoreCase("N") ) {	
		//uncheck the box
 		__QA_QC_tab_visible_JCheckBox.setSelected( false );
	}
	else {
		//check the box
 		__QA_QC_tab_visible_JCheckBox.setSelected( true );
	}

	String isEditable = null;
	if ( __dmi.isDatabaseVersionAtLeast(__dmi._VERSION_030000_20041001) ) {
		isEditable = __db_RTi_MeasType.getIsEditable();
	} else {
		isEditable = __db_RTi_MeasType.getEditable();			
	}

	if ( Message.isDebugOn ) {
		Message.printDebug( 3, routine,
		"IsEditable for \"" + __preselected_TSID_string + "\" is \"" + 
		isEditable + "\"." );
	}
	if ( isEditable.equalsIgnoreCase("N") ) {	
 		__QA_QC_tab_editable_JCheckBox.setSelected( false );
	}
	else {
 		__QA_QC_tab_editable_JCheckBox.setSelected( true );
	}

	//if we are not adding a new record, disable this JCheckBox.
	if ( ! __bln_new_object ) {
 		JGUIUtil.setEnabled(__QA_QC_tab_editable_JCheckBox, false );
	}
		

} //end update_GUI_fields

/**
Updates the tsid JTextField by concatentating the entries
in the JTextFields and JComboBoxes in the GUI.
@return String with tsid.
*/
protected String update_tsid_field() {
	String routine = __class + ".update_tsid_field";

	String full_tsid= null;

	int ind = -999;
	String location = null;
	location = __TS_location_JTextField.getText();
	ind = location.indexOf(" -");
	if ( ind > 0 ) {
		location = location.substring( 0, ind );
	}

	ind = -999;
	String data_type = null;
	data_type = (String) __TS_dataType_JComboBox.getSelected();
	ind = data_type.indexOf(" -");
	if ( ind > 0 ) {
		data_type = data_type.substring( 0, ind );
	}

	String sub_data_type = null;
	sub_data_type = __TS_subDataType_JTextField.getText();
	if (( sub_data_type == null ) || ( sub_data_type.length() <=0 ) ) {
		sub_data_type = null;
	}

	ind = -999;
	String data_source = null;
	data_source = (String) __TS_dataSource_JComboBox.getSelected();
	ind = data_source.indexOf(" -");
	if ( ind > 0 ) {
		data_source = data_source.substring( 0, ind );
	}

	ind = -999;
	String ts_base = null;
	ts_base = (String) __TS_timeStepBase_JComboBox.getSelected();
	ind = ts_base.indexOf(" -");
	if ( ind > 0 ) {
		ts_base = ts_base.substring( 0, ind );
	}

	ind = -999;
	String ts_mult = null;
	ts_mult = (String) __TS_timeStepMultiplier_JComboBox.getSelected();
	ind = ts_mult.indexOf(" -");
	if ( ind > 0 ) {
		ts_mult = ts_mult.substring( 0, ind );
	}

	ind = -999;
	String scen = null;
	scen = (String) __TS_scenario_JComboBox.getSelected();
	ind = scen.indexOf(" -");
	if ( ind > 0 ) {
		scen = scen.substring( 0, ind );
	}
	if ( scen.equalsIgnoreCase( NONE_STRING ) ) {
		scen = null;
	}

	//assemble:
	if ( scen != null ) {
		if ( sub_data_type != null ) {
			full_tsid = 	location + "." +
					data_source + "." +
					data_type + "-" +
					sub_data_type + "." +
					ts_mult + ts_base + "." +
					scen;
		}
		else {
			full_tsid = 	location + "." +
					data_source + "." +
					data_type + "." +
					ts_mult + ts_base + "." +
					scen;
		}
	} 
	else { //scenario is null
		if ( sub_data_type != null ) {
			full_tsid = 	location + "." +
					data_source + "." +
					data_type + "-" +
					sub_data_type + "." +
					ts_mult + ts_base;
		}
		else {
			full_tsid = 	location + "." +
					data_source + "." +
					data_type + "." +
					ts_mult + ts_base;
		}
	}
	return full_tsid;
} //end update_tsid_field

/**
Verifies and stores all the information on the QA/QC tab.
The method <ul><li> fills in the <i>__gui</i> verion of the object
in memory with the values that are currently set in the GUI</li>
<li>compares the values in the <i>__gui</i> version of the object to the 
<i>__db</i> version of the object.  If the <i>__gui</i> version differs 
from the <i>__db</i> verion:<ul>
<li>the <i>__gui</i> version is marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the 
<i>__gui</i> version differs from the <i>__db</i> version 
(the <i>__db</i> version remember, represents how the object is 
in the database itself) </li></ul> </li></ul></ul>
@exception Exception thrown if error occurs
*/
public void verify_QA_QC_tab() throws Exception {
	String routine = __class + ".verify_QA_QC_tab";

	//get Min allowed - REQUIRED 
	String gui_min_str = null;
	double gui_min = -999;
	String db_min_str = null;
	double db_min = -999;

	//gui values
	gui_min_str = ( __QA_QC_tab_min_JTextField.getText()).trim();
	gui_min= StringUtil.atod( gui_min_str );
	//format the string to %.2f precision to compare to original value.
	gui_min_str = StringUtil.formatString( gui_min, "%.2f" );

	//memory values
	db_min = __db_RTi_MeasType.getMin_check();
	//format it so we can compare
	db_min_str = StringUtil.formatString( db_min,"%.2f");
		
	if ( ! db_min_str.equalsIgnoreCase( gui_min_str ) ) {
		//set Dirty
 		__gui_RTi_MeasType.setDirty( true );
 		__dirty_vect.addElement(
		"Change Minimum Check from \"" + db_min_str + "\" to \"" +
		gui_min_str + "\"");
		//set in object
 		__gui_RTi_MeasType.setMin_check( gui_min );
	}

	//get Max allowed - REQUIRED 
	String gui_max_str = null;
	double gui_max = -999;
	String db_max_str = null;
	double db_max = -999;

	//gui values
	gui_max_str = ( __QA_QC_tab_max_JTextField.getText()).trim();
	gui_max= StringUtil.atod( gui_max_str );
	//format the string to %.2f precision to compare to original value.
	gui_max_str = StringUtil.formatString( gui_max, "%.2f" );

	//memory values
	db_max = __db_RTi_MeasType.getMax_check();
	//format it so we can compare
	db_max_str = StringUtil.formatString( db_max,"%.2f");
		
	if ( ! db_max_str.equalsIgnoreCase( gui_max_str ) ) {
		//set Dirty
 		__gui_RTi_MeasType.setDirty( true );
 		__dirty_vect.addElement(
		"Change Maximum Check from \"" + db_max_str + "\" to \"" +
		gui_max_str + "\"");
		//set in object
 		__gui_RTi_MeasType.setMax_check( gui_max );
	}

	//visible - REQUIRED
	String gui_visible = "N";
	String db_visible = "N";

	boolean gui_isVisible = false;
	gui_isVisible = __QA_QC_tab_visible_JCheckBox.isSelected();
	if ( gui_isVisible ) {
		gui_visible = "Y";
	}
	else { 
		gui_visible = "N";
	}

	db_visible = __db_RTi_MeasType.getIsVisible();

	//now compare the value to that in the DB
	//curr_visible is:"Y" or "N"
	if ( !db_visible.equalsIgnoreCase( gui_visible )) {
 		__gui_RTi_MeasType.setDirty( true );
 		__dirty_vect.addElement(
		"Change Is Visible value (Y/N) (QA/QC tab) from \"" + 
		db_visible + "\" to \"" + gui_visible + "\"");
		//set in object
 		__gui_RTi_MeasType.setIsVisible( gui_visible );
	}

	//editable - will go away in the future.
	String gui_editable = "N";
	

	boolean gui_isEditable = false;
	gui_isEditable = __QA_QC_tab_editable_JCheckBox.isSelected();
	if ( gui_isEditable ) {
		gui_editable = "Y";
	}
	else { 
		gui_editable = "N";
	}

	String db_editable = "N";
	if ( __dmi.isDatabaseVersionAtLeast(__dmi._VERSION_030000_20041001) ) {
		db_editable = __db_RTi_MeasType.getIsEditable();
	} else {
		db_editable = __db_RTi_MeasType.getEditable();			
	}

	//now compare the value to that in the DB
	//curr_editable is:"Y" or "N"
	if ( !db_editable.equalsIgnoreCase( gui_editable )) {
 		__gui_RTi_MeasType.setDirty( true );
 		__dirty_vect.addElement(
			"Change Is Editable value (Y/N) (QA/QC tab) from \""
			+ db_editable
			+ "\" to \""
			+ gui_editable
			+ "\"");
		// Set in object
		if ( __dmi.isDatabaseVersionAtLeast(__dmi._VERSION_030000_20041001) ) {
 			__gui_RTi_MeasType.setIsEditable( gui_editable );
 		} else {
 			__gui_RTi_MeasType.setEditable( gui_editable );
 		}
	}

} //end verify_QA_QC_tab

/**
Verifies and stores all the information on the Transmit Protocol tab.
The method <ul><li> fills in the <i>__gui</i> verion of the object
in memory with the values that are currently set in the GUI</li>
<li>compares the values in the <i>__gui</i> version of the object to the 
<i>__db</i> version of the object.  If the <i>__gui</i> version differs 
from the <i>__db</i> verion:<ul>
<li>the <i>__gui</i> version is marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the 
<i>__gui</i> version differs from the <i>__db</i> version 
(the <i>__db</i> version remember, represents how the object is 
in the database itself) </li></ul> </li></ul></ul>
@exception Exception thrown if error occurs
*/
public void verify_transProt_tab() throws Exception {
	String routine = __class + ".verify_transProt_tab";

	String gui_transmit = null;
	String db_transmit = null;
	db_transmit = __db_RTi_MeasType.getTransmitProtocol().toUpperCase();
	gui_transmit = (String) __transProt_tab_transmit_JComboBox.
	getSelected();
	int ind = -999;
	ind = gui_transmit.indexOf(" -");
	if ( ind > 0 ) {
		gui_transmit = gui_transmit.substring( 0, ind );
	}
	if ( !db_transmit.equalsIgnoreCase( gui_transmit )) {
		//Set setDirty flag
 		__gui_RTi_MeasType.setDirty( true );
 		__dirty_vect.addElement(
		"Change Transmit Protocol from \"" + db_transmit + "\" to \"" +
		gui_transmit + "\"");
		//set in object
 		__gui_RTi_MeasType.setTransmitProtocol( gui_transmit );
	}

} //end verify_transProt_tab

/**
STATUS Tab not being used currently
The method <ul><li> fills in the <i>__gui</i> verion of the object
in memory with the values that are currently set in the GUI</li>
<li>compares the values in the <i>__gui</i> version of the object to the 
<i>__db</i> version of the object.  If the <i>__gui</i> version differs 
from the <i>__db</i> verion:<ul>
<li>the <i>__gui</i> version is marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the 
<i>__gui</i> version differs from the <i>__db</i> version 
(the <i>__db</i> version remember, represents how the object is 
in the database itself) </li></ul> </li></ul></ul>
@exception Exception thrown if error occurs
*/
public void verify_status_tab() throws Exception {
	String routine = __class + ".verify_status_tab";
//REVISIT - Under access this field is set to not accept null. Since this is currently 
//          not used we need either to set to something or change the DB restriction to 
//          accept null field.
	__gui_RTi_MeasType.setStatus("-");

} //end verify_status_tab


/**
Verifies all the information in the top portion of the GUI.
The method <ul><li> fills in the <i>__gui</i> verion of the object
in memory with the values that are currently set in the GUI</li>
<li>compares the values in the <i>__gui</i> version of the object to the 
<i>__db</i> version of the object.  If the <i>__gui</i> version differs 
from the <i>__db</i> verion:<ul>
<li>the <i>__gui</i> version is marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the 
<i>__gui</i> version differs from the <i>__db</i> version 
(the <i>__db</i> version remember, represents how the object is 
in the database itself) </li></ul> </li></ul></ul>
@exception Exception thrown if error occurs
*/
public void verify_top_fields( ) throws Exception {
	String routine = __class + ".verify_top_fields";

	String db_name = null;
	String db_desc = null;
	TSIdent tsid = null;
	try {
		tsid = __db_RTi_MeasType.toTSIdent();
	}
	catch ( Exception e) {
		Message.printWarning( 2, routine, e );
		tsid = null;
	}
	if ( tsid != null ) {
		db_name = tsid.toString();
	}
	tsid = null;
	db_desc = __db_RTi_MeasType.getDescription().toUpperCase();

	//Data Type - REQUIRED
	String gui_dataType = null;
	gui_dataType = (String) __TS_dataType_JComboBox.getSelected();
	String db_dataType = null;
	db_dataType = __db_RTi_MeasType.getData_type();
	//need to trim String since there is a " - " followed by description 
	int ind = -999;
	ind = gui_dataType.indexOf( " -" );
	if ( ind > 0 ) {
		gui_dataType = (gui_dataType.substring( 0, ind )).trim();
	}

	if ( ! db_dataType.equalsIgnoreCase( gui_dataType ) ){
		//set setDirty Flag
 		__gui_RTi_MeasType.setDirty( true );
 		__dirty_vect.addElement(
		"Change Data Type from \"" + db_dataType + "\" to \"" +
		gui_dataType + "\"");
		//set the DataType Value in the Object in memory
 		__gui_RTi_MeasType.setData_type( gui_dataType );
	}

	//SubData Type - NOT REQUIREd
	String gui_subDataType = null;
	String db_subDataType = null;
	db_subDataType = __db_RTi_MeasType.getSub_type();
	gui_subDataType = ( __TS_subDataType_JTextField.getText()).
	trim().toUpperCase() ;

	if ( ! db_subDataType.equalsIgnoreCase( gui_subDataType ) ){
		//set the setDirty flag
 		__gui_RTi_MeasType.setDirty( true );
 		__dirty_vect.addElement(
		"Change Subdata Type from \"" + db_subDataType + "\" to \"" +
		gui_subDataType + "\"");
		//set the SubDataType value in object
 		__gui_RTi_MeasType.setSub_type( gui_subDataType );
	}

	//Data Source - REQUIRED 
	String gui_source = null;
	gui_source = (String) __TS_dataSource_JComboBox.getSelected();
	ind = -999;
	ind = gui_source.indexOf( " -" );
	if ( ind > 0 ) {
		gui_source = (gui_source.substring( 0, ind )).trim();
	}
	String db_source = null;
	db_source = __db_RTi_MeasType.getSource_abbrev();
	if ( ! db_source.equalsIgnoreCase( gui_source ) ) {
		//set setDirty
 		__gui_RTi_MeasType.setDirty( true );
 		__dirty_vect.addElement(
		"Change Source from \"" + db_source + "\" to \"" +
		gui_source + "\"");
		//set the value in object object
 		__gui_RTi_MeasType.setSource_abbrev( gui_source);
	}

	//Time Step Base - REQUIRED
	String gui_step = null;
	gui_step = (String) __TS_timeStepBase_JComboBox.getSelected();
	String db_step = null;
	db_step = __db_RTi_MeasType.getTime_step_base();
	ind = -999;
	ind = gui_step.indexOf( " -" );
	if ( ind > 0 ) {
		gui_step = (gui_step.substring( 0, ind )).trim();
	}

	if ( !db_step.equalsIgnoreCase( gui_step ) ) {
		//set the setDirty flag
 		__gui_RTi_MeasType.setDirty( true );
 		__dirty_vect.addElement(
		"Change Time Step Base from \"" + db_step + "\" to \"" +
		gui_step + "\"");
		//set the SubDataType value in memory
 		__gui_RTi_MeasType.setTime_step_base( gui_step );
	}

	//Time Step Multiplier - REQUIRED
	long db_mult = 1;
	db_mult = __db_RTi_MeasType.getTime_step_mult();
	if ( DMIUtil.isMissing( db_mult ) ) {
	//if ( db_mult == DMIUtil.MISSING_LONG ) {}
		db_mult = 1;
	}
	long gui_mult = 1;
	String gui_mult_str = null;
	gui_mult_str = (String)
 	__TS_timeStepMultiplier_JComboBox.getSelected();
	//we know it is an int
	gui_mult = StringUtil.atol( gui_mult_str );
	if ( db_mult != gui_mult ) {
		//mark object as dirty 
 		__gui_RTi_MeasType.setDirty( true );
 		__dirty_vect.addElement(
		"Change Time Step Multiplier from \"" + db_mult + "\" to \"" +
		gui_mult + "\"");
		//set new value in object

 		__gui_RTi_MeasType.setTime_step_mult( gui_mult );
	}

	//Scenario - NOT REQUIRED
	String db_scen = null;
	db_scen = __db_RTi_MeasType.getScenario();
	String gui_scen = null;
	gui_scen = (String) __TS_scenario_JComboBox.getSelected();
	ind = -999;
	ind = gui_scen.indexOf( " -" );
	if ( ind > 0 ) {
		gui_scen = (gui_scen.substring( 0, ind )).trim();
	}

	//check for "none" condition
	if ( gui_scen.equalsIgnoreCase( NONE_STRING ) ) {
		//set the __selected global variable
		gui_scen = DMIUtil.MISSING_STRING;
	}
	//now check to see if value in DB is missing
	if ( !db_scen.equalsIgnoreCase( gui_scen ) ) {
		//then the selection in the
		//GUI is "none", but it was not none/missing in the 
		//original object. Set the setDirty flag
 		__gui_RTi_MeasType.setDirty( true );
 		__dirty_vect.addElement(
		"Change Scenario from \"" + db_scen + "\" to \"" +
		gui_scen + "\"");
		//set the Scenario value in memory
 		__gui_RTi_MeasType.setScenario( gui_scen );
	}

	//don't need to get the TSID JTextField

	//Description - REQUIRED
	//get value from GUI
	String gui_desc = null;
	//We got db_desc above db_desc = __db_RTi_MeasType.getDescription();
	gui_desc = ( __TS_description_JTextField.getText()).trim().toUpperCase();

	if ( !db_desc.equalsIgnoreCase(gui_desc ) ) {
		//set the setDirty flag
 		__gui_RTi_MeasType.setDirty( true );
 		__dirty_vect.addElement(
		"Change Description from \"" + db_desc + "\" to \"" +
		gui_desc + "\"");
		//set the SubDataType value in memory
 		__gui_RTi_MeasType.setDescription( gui_desc );
	}

	//Units - REQUIRED
	String db_units = null;
	db_units = __db_RTi_MeasType.getUnits_abbrev();
	String gui_units = null;
	gui_units =(String) __TS_units_JComboBox.getSelected();
	ind = -999;
	ind = gui_units.indexOf( " -" );
	if ( ind > 0 ) {
		gui_units = (gui_units.substring( 0, ind )).trim();
	}

	if ( ! db_units.equalsIgnoreCase( gui_units ) ) {
		//set the setDirty flag
 		__gui_RTi_MeasType.setDirty( true );
 		__dirty_vect.addElement(
		"Change Units from \"" + db_units + "\" to \"" +
		gui_units + "\"");
		//set the SubDataType value in memory
 		__gui_RTi_MeasType.setUnits_abbrev( gui_units );
	} 
	
	//Create method
	String gui_create = null;
	String db_create = null;
	db_create = __db_RTi_MeasType.getCreate_method();
	gui_create = __TS_create_method_JTextField.getText();
	ind = -999;
	ind = gui_create.indexOf( " -" );
	if ( ind > 0 ) {
		gui_create = (gui_create.substring( 0, ind )).trim();
	}
	if ( !db_create.equalsIgnoreCase( gui_create ) ){
		//set the setDirty flag
 		__gui_RTi_MeasType.setDirty( true );
 		__dirty_vect.addElement(
		"Change Create Method from \"" + db_create + "\" to \"" +
		gui_create + "\"");
		//set the createmethod value in memory
 		__gui_RTi_MeasType.setCreate_method( gui_create );
	} 

} //end verify_top_fields


/**
Verifies all the information on the tab with the index passed in.
The method <ul><li> fills in the <i>__gui</i> verion of the object
in memory with the values that are currently set in the GUI</li>
<li>compares the values in the <i>__gui</i> version of the object to the 
<i>__db</i> version of the object.  If the <i>__gui</i> version differs 
from the <i>__db</i> verion:<ul>
<li>the <i>__gui</i> version is marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the 
<i>__gui</i> version differs from the <i>__db</i> version 
(the <i>__db</i> version remember, represents how the object is 
in the database itself) </li></ul> </li></ul></ul>
@param tab_index  Index of tab to verify.
@exception Exception thrown if error occurs
*/
public void verify_tab_info( int tab_index ) throws Exception {
	String routine = __class + ".verify_tab_info";

	switch ( tab_index ) {
		case 0:
			//QA/QC Tab
			try {
				verify_QA_QC_tab();
			}
			catch ( Exception e ){
				Message.printWarning( 2, routine,e);
			}
			break;
		case 1:
			//Transmit Protocol Tab
			try {
				verify_transProt_tab();
			}
			catch ( Exception e ){
				Message.printWarning( 2, routine,e);
			}
			break;

		case 2: 
			//Status Tab
			try {
				verify_status_tab();
			}
			catch ( Exception e ){
				Message.printWarning( 2, routine,e);
			}
			break;
		case 3: 
			//Time Series 1
			try {
				verify_TS1_tab();
			}
			catch ( Exception e ){
				Message.printWarning( 2, routine,e);
			}
			break;
		case 4:
			//Time Series 2
			try {
				verify_TS2_tab();
			}
			catch ( Exception e ){
				Message.printWarning( 2, routine,e);
			}

			break;

		default:
			break;
			
	}//end switch
} //end verify_tab_info

/**
Verifies and stores all the information on the TIME SERIES 1 tab.
The method <ul><li> fills in the <i>__gui</i> verion of the object
in memory with the values that are currently set in the GUI</li>
<li>compares the values in the <i>__gui</i> version of the object to the 
<i>__db</i> version of the object.  If the <i>__gui</i> version differs 
from the <i>__db</i> verion:<ul>
<li>the <i>__gui</i> version is marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the 
<i>__gui</i> version differs from the <i>__db</i> version 
(the <i>__db</i> version remember, represents how the object is 
in the database itself) </li></ul> </li></ul></ul>
@exception Exception thrown if error occurs
*/
public void verify_TS1_tab() throws Exception {
	String routine = __class + ".verify_TS1_tab";

	String gui_table_num1_str = null;
	long gui_table_num1 = -999;
	long db_table_num1 = -999;
	gui_table_num1_str = (String) __TS1_tab_table_num1_JComboBox.
	getSelected();
	int ind = -999;
	ind = gui_table_num1_str.indexOf( " -" );
	if ( ind > 0 ) {
		gui_table_num1_str = 
		(gui_table_num1_str.substring( 0, ind )).trim();
	}
	gui_table_num1 = StringUtil.atol( gui_table_num1_str );

	db_table_num1 = __db_RTi_MeasType.getTable_num1();

	if ( db_table_num1 != gui_table_num1 ) {
		//then it is a change so set dirty
 		__gui_RTi_MeasType.setDirty( true );
 		__dirty_vect.addElement(
		"Change Table Number1 (Data Storage tab)from \"" + 
		db_table_num1 + "\" to \"" +
		gui_table_num1 + "\"");
		//now set it in memory
 		__gui_RTi_MeasType.setTable_num1( gui_table_num1 );
	}

	//Dbload_method1 - Required
	String gui_dbload1_str =null;
	long gui_dbload1 = -999;
	long db_dbload1 = -999;
	gui_dbload1_str = (String) __TS1_tab_dbload1_JComboBox.
	getSelected();
	ind = -999;
	ind = gui_dbload1_str.indexOf( " -" );
	if ( ind > 0 ) {
		gui_dbload1_str = (gui_dbload1_str.substring( 0, ind )).trim();
	}

	gui_dbload1 = StringUtil.atol( gui_dbload1_str );
	db_dbload1 = __db_RTi_MeasType.getDbload_method1();

	if ( db_dbload1 != gui_dbload1 ) {
		//Set setDirty flag
 		__gui_RTi_MeasType.setDirty( true );
 		__dirty_vect.addElement(
		"Change Load Method1 (Data Storage tab) from \"" + 
		db_dbload1 + "\" to \"" + gui_dbload1 + "\"");
		//set in memory
 		__gui_RTi_MeasType.setDbload_method1( gui_dbload1 );
	}

} //end verify_TS1_tab

/**
Verifies and stores all the information on the TIME SERIES 2 tab.
The method <ul><li> fills in the <i>__gui</i> verion of the object
in memory with the values that are currently set in the GUI</li>
<li>compares the values in the <i>__gui</i> version of the object to the 
<i>__db</i> version of the object.  If the <i>__gui</i> version differs 
from the <i>__db</i> verion:<ul>
<li>the <i>__gui</i> version is marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the 
<i>__gui</i> version differs from the <i>__db</i> version 
(the <i>__db</i> version remember, represents how the object is 
in the database itself) </li></ul> </li></ul></ul>
@exception Exception thrown if error occurs
*/
public void verify_TS2_tab() throws Exception {
	String routine = __class + ".verify_TS2_tab";

	//can be none!
	String gui_table_num2_str = null;
	long gui_table_num2 = -999;
	long db_table_num2 = -999;
	gui_table_num2_str = (String) __TS2_tab_table_num2_JComboBox.
	getSelected();
	int ind = -999;
	ind = gui_table_num2_str.indexOf( " -" );
	if ( ind > 0 ) {
		gui_table_num2_str = 
		(gui_table_num2_str.substring( 0, ind )).trim();
	}
	if ( gui_table_num2_str.equalsIgnoreCase( NONE_STRING ) ) {
		gui_table_num2 = DMIUtil.MISSING_LONG;
	}
	else {
		gui_table_num2 = StringUtil.atol( gui_table_num2_str );
	}

	db_table_num2 = __db_RTi_MeasType.getTable_num2();

	if ( db_table_num2 != gui_table_num2 ) {
		//then it is a change so set dirty
 		__gui_RTi_MeasType.setDirty( true );
 		__dirty_vect.addElement(
		"Change Table Number2 from \"" + db_table_num2 + "\" to \"" +
		gui_table_num2 + "\"");
		//now set it in memory
 		__gui_RTi_MeasType.setTable_num2( gui_table_num2 );
	}

	String gui_dbload2_str =null;
	long gui_dbload2 = -999;
	long db_dbload2 = -999;
	//do not bother checking dbload method if table_num2 is NONE.
	//if ( gui_table_num2 == DMIUtil.MISSING_LONG ) {}
	if ( DMIUtil.isMissing( gui_table_num2 ) ) {
		gui_dbload2= DMIUtil.MISSING_LONG;
 		__gui_RTi_MeasType.setDbload_method2( gui_dbload2 );
	}
	else { //( gui_table_num2 != DMIUtil.MISSING_LONG ) 
		//Dbload_method2 
		gui_dbload2_str = (String) __TS2_tab_dbload2_JComboBox.
		getSelected();
		ind = -999;
		ind = gui_dbload2_str.indexOf( " -" );
		if ( ind > 0 ) {
			gui_dbload2_str = 
			(gui_dbload2_str.substring( 0, ind )).trim();
		}
	
		gui_dbload2 = StringUtil.atol( gui_dbload2_str );
		db_dbload2 = __db_RTi_MeasType.getDbload_method2();
	
		if ( db_dbload2 != gui_dbload2 ) {
			//Set setDirty flag
 			__gui_RTi_MeasType.setDirty( true );
 			__dirty_vect.addElement(
			"Change DBLoad2 from \"" + 
			db_dbload2 + "\" to \"" + gui_dbload2 + "\"");
			//set in memory
 			__gui_RTi_MeasType.setDbload_method2( gui_dbload2 );
		}
	}
} //end verify_TS2_tab


/////////////////////////////////////////////////////////////////
////////////////////////  ACTIONS ///////////////////////////////
/////////////////////////////////////////////////////////////////
/**
The event handler manages action events. 
@param event Event to handle.
*/
public void actionPerformed (ActionEvent event) {
	String routine = __class + ".actionPerformed";

	try {
	String command = event.getActionCommand();
	Object source = event.getSource();
	if ( source.equals( __apply_JButton ) ) {
		boolean blnUpdated = true;

		//REQUIRED Fields
		try {
			checkRequiredInput();
		}
		catch ( Exception e ) {
			//then there was an error so do not 
			//update object in memory or in database.
			Message.printWarning( 2, routine, e );
			blnUpdated= false;
		}
	
		//update the "GUI" object in memory
		if ( blnUpdated ) {
			try {
				update_RiversideDB_objects();
			}
			catch( Exception e ) {
				Message.printWarning( 2, routine, e );
				blnUpdated=false;
			}
		}
		if ( blnUpdated ) {
			try {
				//update database itself
				update_database();
			}
			catch( Exception e ) {
				Message.printWarning( 2, routine, e );
				blnUpdated=false;
				//keep __db_RTi_MeasType in memory
				//update the __gui_ version to be the 
				//the memory version again since update failed.
 				__gui_RTi_MeasType = null;
 				__gui_RTi_MeasType = 
				new RiversideDB_MeasType( __db_RTi_MeasType );
			}
		}

		if ( blnUpdated ) {
			//if we got this far, the database was updated,
			//so update objects in memory.  The __gui_ object 
			//was written to the database, so now the 
			//_db_ object should equal the __gui_ object
 			__db_RTi_MeasType = null;
 			__db_RTi_MeasType = 
			new RiversideDB_MeasType( __gui_RTi_MeasType );
 			__db_RTi_MeasType.setDirty( false );
			
 			__gui_RTi_MeasType = null;
		}
	}//end apply
	else if ( source.equals( __cancel_JButton ) ) {
		//if(( __cautious_mode ) && ( __dirty_vect.size() > 0 ) ) {}
		if( __cautious_mode ) {
			boolean blnContinue = true;
			//create and update __gui_ objects in memory
			//The reason to do this is to be able to provide the
			//user with a Confirm Cancel message.
			try {
				//true means suppress warning messages.
				update_RiversideDB_objects( true );
			}
			catch( Exception e ) {
				Message.printWarning( 2, routine, e);
				windowManagerClose();
				//blnContinue = false;
				//get rid of gui
				//setVisible(false);
				//dispose();
			}

			if ( (blnContinue) && ( __gui_RTi_MeasType.isDirty()) &&
			( ! __bln_new_object ) ){
				//holds messages from __dirty_vect
				StringBuffer b = new StringBuffer();
				for ( int i=0; i< __dirty_vect.size(); i++ ) {
					b.append( (String) 
 					__dirty_vect.elementAt(i) + "\n" );
				}

				//write out a confirmation message.
				int x = new ResponseJDialog(
				this, "Cancel Changes",
				"Are you sure you want to " +
				"Cancel the following changes?\n" +
				b.toString(), 
				ResponseJDialog.YES | ResponseJDialog.NO ).
				response();
		
				if ( x == ResponseJDialog.YES ) {
					//write to log file
					Message.printStatus( 5, routine, 
					"User canceled changes: " +
					b.toString() ); 
	
					//setVisible(false);
					//dispose();
					windowManagerClose();
		
					//empty out dirty vector
 					__dirty_vect.clear();
		
				}	
				else {
					//do nothing.. leave GUI open
 					__dirty_vect.clear();
				}
				b = null;
			}
			else {
				//Nothing has been changed, so
				//just do a normal cancel- close gui
				//setVisible(false);
				//dispose();
				windowManagerClose();
			}
		}
		else {
			//just do a normal cancel- close gui
			windowManagerClose();
			//setVisible(false);
			//dispose();
		}
	}
	else if ( source.equals( __close_JButton ) ) {

		//close implies the window should be 
		//closed -- w/o automatically saving changes.
		//If there have been changes, prompt user.
		closeGUI();
	}
	
	else if ( source == __TS_subDataType_JTextField ) {
		String ts= update_tsid_field();
 		__TS_tsid_JTextField.setText( ts );
	}

	source = null;
	} // End of try
	catch ( Exception e ) {
		if ( Message.isDebugOn ) {
			Message.printWarning ( 2, routine, e );
		}
	}
} //end actionPerformed

/**
Respond to ItemEvents.
*/
public void itemStateChanged ( ItemEvent event ) {
	String routine = __class + ".itemStateChanged";

	Object source = event.getItemSelectable();

	if ( event.getStateChange() == ItemEvent.SELECTED ) {
		if ( source == __TS_dataSource_JComboBox ) {
			//also update TSID field
			String ts= update_tsid_field();
 			__TS_tsid_JTextField.setText( ts );
		}
		else if ( source == __TS_dataType_JComboBox ) {
			// get dimension for the datatype selected.
			int sel_dataType_int = -999;
			sel_dataType_int = 
 			__TS_dataType_JComboBox.getSelectedIndex();
	
			RiversideDB_DataType dt = null;
			dt = (RiversideDB_DataType) 
 			__RTi_DataType_vect.elementAt(sel_dataType_int);
		
			// now use that to get the Dimesnion
			String dim = null;
			dim = dt.getDimension();
	
			// use dimension to get list of units for 
			Vector units_vect = new Vector();
			Vector v = 
				DataUnits.lookupUnitsForDimension( __system_units, dim );
			int size = 0;
			if ( v != null ) {
				size = v.size();
			}
			DataUnits du = null;
			for ( int i=0; i< size; i++ ) {
				du = (DataUnits)v.elementAt(i);
				units_vect.addElement( du.getAbbreviation() + 
				" - " + du.getLongName() );
			}
			if ( size == 0 ) {
				units_vect.addElement( "      " );
			}
			//now sort the units 
			Vector sorted_units_vect = null;
			sorted_units_vect = StringUtil.sortStringList( units_vect,
				StringUtil.SORT_ASCENDING, null, false, true );
			units_vect = null;
			if ( sorted_units_vect == null ) {
				sorted_units_vect = new Vector();
			}
			//if datatype is FLOODMONITOR- ignore all
			//the units and just add "N/A"
			String dt_str = (String)
 			__TS_dataType_JComboBox.getSelected();
			if ( dt_str.startsWith("FLOODMONITOR" ) ) {
				sorted_units_vect.removeAllElements();
				sorted_units_vect.add("MULTIPLE");
			}
			//now update the jcombobox with the units
 			__TS_units_JComboBox.removeAllItems();
			for ( int i=0;i<sorted_units_vect.size(); i++ ) {
 				__TS_units_JComboBox.addItem(
				sorted_units_vect.elementAt(i));
			}
	
			//update the units strings in the QA/QC tab
			//to match the units now selected 
			//in the units JcomboBox
			String u = null;
			u =(String) __TS_units_JComboBox.getSelected();
			if ( u == null ){
				u = "";
			}
			int ind = -999;
			ind = u.indexOf( " -" );
			if ( ind > 0 ) {
				u = u.substring(0,ind);
			}
			//update units field in QA/QC tab
 			__QA_QC_tab_max_units_JLabel.setText(u);
 			__QA_QC_tab_min_units_JLabel.setText(u);

			//also update TSID field
			String ts= update_tsid_field();
 			__TS_tsid_JTextField.setText( ts );
		}
		else if ( source == __TS_scenario_JComboBox ) {
			//also update TSID field
			String ts= update_tsid_field();
 			__TS_tsid_JTextField.setText( ts );
		}
		else if ( source == __TS_timeStepBase_JComboBox ) {
			String tsb = (String) 
 			__TS_timeStepBase_JComboBox.getSelected();
			//update the list of TIme Step Multiplier
			//choices for the specific Base choosen
			int [] arrMults = null;
			arrMults = TimeInterval.multipliersForIntervalBase(
			tsb, true, false );

 			__TS_timeStepMultiplier_JComboBox.removeAllItems();
			for ( int i=0; i<arrMults.length; i++ ) {
 				__TS_timeStepMultiplier_JComboBox.addItem(
				String.valueOf( arrMults[i] ) );
			}

			//also update TSID field
			String ts= update_tsid_field();
 			__TS_tsid_JTextField.setText( ts );

		}
		else if ( source == __TS_timeStepMultiplier_JComboBox ) {
			String ts= update_tsid_field();
 			__TS_tsid_JTextField.setText( ts );
		}
		else if ( source == __TS_units_JComboBox ) {
			//get units string
			String u = null;
			u =(String) __TS_units_JComboBox.getSelected();
			if ( u == null ){
				u = "";
			}
			int ind = -999;
			ind = u.indexOf( " -" );
			if ( ind > 0 ) {
				u = u.substring(0,ind);
			}
			//update units field in QA/QC tab
 			__QA_QC_tab_max_units_JLabel.setText(u);
 			__QA_QC_tab_min_units_JLabel.setText(u);

			//also update TSID field
			String ts= update_tsid_field();
 			__TS_tsid_JTextField.setText( ts );
		} 

	}
	if ( event.getStateChange() == ItemEvent.DESELECTED ) {
	}

} //end itemStateChanged


//WINDOW EVENTS
public void windowActivated ( WindowEvent e )
{
}

/**
This class is listening for GeoViewGUI closing so it can gracefully handle.
*/
public void windowClosed ( WindowEvent e )
{	
}

/**
We don't want user to be able to close the GUI with the "x".
*/
public void windowClosing ( WindowEvent e ) {
	if ( __canWriteMeasType ) {
	 	closeGUI();
	}
	else {
		//just close as a cancel
		//setVisible(false);
		//dispose();
		windowManagerClose();
	}
}

public void windowDeactivated ( WindowEvent e )
{
}

public void windowDeiconified ( WindowEvent e )
{
}

public void windowIconified ( WindowEvent e )
{
}

public void windowOpened ( WindowEvent e )
{
}
private void windowManagerClose() {
	Message.printStatus(6, "", "WindowManager Close");
	if (__originallyNewObject) {
		//new MeasTypes are added with ID of MeasLoc_name()
		_windowManager.closeWindowInstance(
			_windowManager.WINDOW_MEASTYPE,
			new String(__db_RTi_MeasType.getMeasLoc_name() ) );
	}
	else {
		//existing MeasTypes are added with ID of MeasType_num()
		Long id = new Long(__db_RTi_MeasType.getMeasType_num());
		_windowManager.closeWindowInstance(
			_windowManager.WINDOW_MEASTYPE,
			"" + id );
	}
}

/////////////////////////////////////////////////////////////////
/////////////////////// END ACTIONS /////////////////////////////
/////////////////////////////////////////////////////////////////

}// end RiversideDB_TS_JFrame class
