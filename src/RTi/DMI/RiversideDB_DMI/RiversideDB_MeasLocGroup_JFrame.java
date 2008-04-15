

//--------------------------------------------------------------------------
// RiversideDB_MeasLocGroup_JFrame -
//
//	Contains the code used to create the GUI that contains
//	MeasLocGroup and DBUserMeasLocGroupRelation information.
//
//--------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
//--------------------------------------------------------------------------
// History:
//
// 2003-06-17	A. Morgan Sheedy, RTi	Initial Implementation
// 2003-06-23	A. Morgan Sheedy, RTi	Added DBUserMeasLocGroupRelation
//					objects to this GUI.
// 2003-06-27	Anne Morgan Love, RTi	code clean up.
//
// 2003-06-27	AML, RTi		general code cleaning.
// 2004-10-25 Luiz Teixeira, RTi	Upgraded by moving it from the
//					main application (RiverTrak Assistant)
//					to the RiversideDB_DMI library.
// 2004_10_29 Luiz Teixeira, RTi	Created the class
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
// 2004_10_29 Luiz Teixeira, RTi	Removed the definition and all
//					reference to all __calling_class.
//					From now on using the
//					RiversideDB_System_Listener implemen
//					ted in RiversideDB_BaseEditor_JFrame
// 2004-12-07 Luiz Teixeira, RTi	Because I moved the following methods
//						addVectors,
//						findAdditions,
//						findRemovals and
//					        removeDuplicateStringsFromVector
//					from the RiversideDB_Util to this class
//					base (RiversideDB_EditorBase) all the
//					preceeding "RiversideDB_Util." reference
//					to these methods were deleted.
// 2005-01-06 Luiz Teixeira, RTi 	Upgraded adding code for the optable
//						field.
//					Some cleanup and documentation.
//					Resized some of the text fields to allow
//						better presentation of the
//						information.
//					Added the browser button and related
//						code to allow the interctive
//						selection of the optable file.
//------------------------------------------------------------------------------
package RTi.DMI.RiversideDB_DMI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import RTi.Util.GUI.JFileChooserFactory;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.ResponseJDialog;
import RTi.Util.GUI.SimpleFileFilter;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.IO.IOUtil;
import RTi.Util.Message.Message;

/**
RiversideDB_MeasLocGroup_JFrame.
This class is laid out similarly to the other RiverTrakAssistant classes.
The general format of the class is laid out below, with the major
methods listed with their key functions.  The main object types from
RiversideDB that are manipulated by this class are:<br><ul>
<li>RiversideDB_MeasLocGroup_JFrame (abbreviated MeasLocGroup_JFrame)</li>
</ul><br>
<p><b>Constructor</b><br>
The constructor is called by an action in one of the JTrees of the
main application.  The main application passes to this class a
RiversideDB_MeasLocGroup.  This class is called in one of two cases: <br>
<ul><li>to view the properties of a current MeasLocGroup</li>
<li>to create a new MeasLocGroup</li></ul>
In the case of viewing a current MeasLocGroup, the MeasLocGroup object
passed to this class is already defined and all required properties
already known.  In the case of creating a totally new MeasLocGroup, the
MeasLocGroup passed in to this class is essentially an empty skeleton. <br>
It is important to distinguish in the constructor, if we are dealing with
an existing MeasLocGroup and just changing some of its fields or if
we are creating a totally new MeasLocGroup.  If the Identifier String
for the MeasLocGroup passed in is MISSING, then we know that we are
creating a new MeasLocGroup object.  In the constructor then, set
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
<li>we need to know to add a new node to the JTree</li></ul>
At this point, whether we are creating a new MeasLocGroup or modifying an
existing one, we assign the MeasLocGroup to the variable known throughout
this class as: <b><i>__gui_RTi_MeasLocGroup</i></b>.  We also set up
the variable known throughout this class as: <b><i>
__db_RTi_DBUserMeasLocGroupRelation_vect</i></b> which is a Vector of
DBUserMeasLocGroupRelations objects that
are related to the <i>__gui_RTi_MeasLocGroup</i>.  If we have created
a totally new MeasLocGroup, the <i>__gui_RTi_DBUserMeasLocGroup_Relation_vect
</i> is an empty Vector; otherwise, it is a Vector of
DBUserMeasLocGroupRelation objects read directly
from the database, based on the already existing MeasLocGroup.  <br><br>
Finally, the constructor also:<ul><li> sets up Vectors of (static) reference data,
read directly from the database thay will be used throughout the class</li>
<li>calls method: <i>init_layout_GUI()</i> which creates and sets up the
GUI components</li></ul><br>
</p>
<p><b>init_layout_gui</b><br>
This method is called from the constructor to create and layout the
GUI components.  It calls the method: <i>create_main_panel</i>
which creates the GUI components and put them together in a
<i>GridBagLayout</i>. They do not worry about setting correct values
in the components' fields, but just gets the components set up.
The main components in the GUI consists of:
<ul><li>JPanel that contains general information pertinent to the MeasLocGroup
object at the top of the GUI</li>
<li>a panel added at the bottom that includes the standard buttons for:
<ul><li>apply</li><li>close</li><li>cancel</li></ul></li></ul>
At the end of <i>init_layout_gui</i>, a
call to <i>update_gui_fields</i> is made which fills in all the
fields of the GUI according to the MeasLocGroup and DBUserMeasLocGroupRelation
objects currently being worked with.
</p>
<p><b>update_gui_fields</b><br>
This method: <ul><li>determines if the current user, already known to the DMI,
has write permissions.  If the user does not have write permissions, the
<b>apply</b> and <b>close</b> buttons are disabled, leaving only the
<b>cancel</b> button enabled.</li><li>Fills in all the GUI fields according
to the data in the MeasLocGroup object.</li></ul>
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
<li><b>creates the <i>__gui</i> versions of the objects!</b> This includes: <ul><li>__gui_RTi_MeasLocGroup</li><li>__gui_RTi_DBUserMeasLocGroupRelation</li></ul> The
<i>__gui</i> versions are created in this method by copying the <i>__db</i>
versions (which were originally created and set up in the constructor)</li>
<li>calls the <i>verify_fields</i> method.
The <i>verify_fields</i> method:<ul><li> fill in the <i>__gui</i> verions of the objects
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
a new MeasLocGroup was <b>not</b> created (if a new MeasLocGroup was created,
we do not want to re-confirm every change)</li>
<li>writes the <i>__gui</i> version of the objects to the database</li>
<li>if the objects were successfully written to the database, the
<i>__gui</i> objects are marked as <b>not</b> dirty
( <i>setDirty(false)</i></li>
<li><i>__dirty_vect</i> is cleared out</li>
<li>if we created a new MeasLocGroup object (<i>__bln_new_object == true</i>),
add a new node on the JTree in the main application  - <b>or</b> -  update the
existing MeasLocGroup node on the JTree with the new changes.</li>
</ul>
</p>

<br>
<p><b>ACTIONS and the events they trigger</b><br>
The main actions in the GUI are fired off when the user selects one of the
following buttons:<ul><li>cancel</li><li>close</li><li>apply</li></ul>
<ul><li><b>cancel</b><br>
Items that are checked before the GUI is closed:<ul>
<li>if the user was changing the properties of an existing MeasLocGroup,
<ul><li><i>update_RiversideDB_objects()</i> is called to <b>create</b> and
update the <i><b>__gui_RTi_MeasLocGroup</b></i> and <i>
<b>__gui_RTi_DBUserMeasLocGroupRelation</b></i> objects in memory,
mark them dirty, and add messages to the <i>__dirty_vect</i> Vector.</li><li>
Print a confirmation
message, confirming the user wants to cancel the changes (that are stored
in the <i>__dirty_vect</i>) </li></ul> </li> </ul> </li>
<li><b>close</b> (close Button and "X" in application window bar)<br>
The method: <i>closeGUI()</i>is called which does the following:<ul>
<li>calls <i>checkRequiredInput</i> which makes sure all the required fields in the GUI are filled out and have valid entries</li>
<li>calls <i>update_RiversideDB_objects</i> which is called to
<b>create</b> and update the <i><b>__gui_RTi_MeasLoc</b></i> and
<i> <b>__gui_RTi_DBUserMeasLocGroupRelation_vect</b></i> objects in memory,
mark them dirty, and add messages to the <i>__dirty_vect</i> Vector.
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
<i>__db_RTi_MeasLocGroup</i> object is re-created, using the copy
constructor and passing in the <i>__gui_RTi_MeasLocGroup</i> object: <br>
<i>__db_RTi_MeasLocGroup = new RiversideDB_MeasLocGroup
( __gui_RTi_MeasLocGroup) </i> </li><li>the <i>__db_RTi_DBUserMeasLocGroupRelation_vect</i>
is cleared out and then re-filled with the DBUserMeasLocGroupRelation objects from the
<i>__gui_RTi_DBUserMeasLocGroupRelation_vect</i></li></ul> </li>
</ul>
</li>
</ul>
*/
public class RiversideDB_MeasLocGroup_JFrame
	extends    RiversideDB_EditorBase_JFrame
	implements ActionListener,
		   WindowListener
{

// Claas name
private static String __class = "RiversideDB_MeasLocGroup_JFrame";

//variables passed in with constructor.
//RiversideDB_DMI object - already opened
private RiversideDB_DMI __dmi = null;

//Shared Layout parameters
private Insets __insets = null;

//indicated what the current action is-
//ie, if a new MeasLocGroup node is being added.
private boolean __bln_new_object = false;

//CLOSE PANEL

// Components for close panel
private SimpleJButton  __close_JButton = null;
private SimpleJButton __cancel_JButton = null;
private SimpleJButton  __apply_JButton = null;

// Current MeasLocGroup identifiers
int __preselected_MeasLocGroup_num = -999;

// GUI Components
private JTextField      __id_JTextField = null;
private JTextField    __name_JTextField = null;
private JTextField    __desc_JTextField = null;
private JTextField __optable_JTextField = null;
private SimpleJButton __browser_JButton = null;

private JList                     __all_users_JList = null;
private JList                __selected_users_JList = null;
private DefaultListModel      __all_users_ListModel = null;
private DefaultListModel __selected_users_ListModel = null;
private SimpleJButton          __move_right_JButton = null;
private SimpleJButton      __clear_selected_JButton = null;

// MeasLocGroup object that is currently being used...
private RiversideDB_MeasLocGroup  __db_RTi_MeasLocGroup = null;
private RiversideDB_MeasLocGroup __gui_RTi_MeasLocGroup = null;

// List of all DBUsers
private Vector __RTi_DBUser_vect = null;

// Vector contain DBUserMeasLocGroupRelation objects for a given MeasLocGroup.
private Vector __db_RTi_DBUserMeasLocGroupRelation_vect = null;
private Vector __gui_RTi_DBUserMeasLocGroupRelation_vect = null;

// Flag to indicate vector of DBUserMeasLocGroupRelation objects has changed.
private boolean __blnDBUserMeasLocGroupRelation_setDirty = false;

// MeasLocGroup vector
private Vector __RTi_MeasLocGroup_vect = null;

// Flag to indicate if we are running in CAUTIOUS MODE---
// aka, if we prompt the user for confirmation of changes
private boolean __cautious_mode = true;

// Flag to indicate if current user has write permissions.
private boolean __canWriteMeasLocGroup = false;

// Holds a Vector of status information-- each
//field that has been changed is recored in this vector.
private Vector __dirty_vect = new Vector();

// Holds the DBUser object
private RiversideDB_DBUser __DBUser = null;

/**
The constructor is called by an action in one of the JTrees of the
main application.  The main application passes to this class a
RiversideDB_ImportProduct.  This class is called in one of two cases: <br>
<ul><li>to view the properties of a current ImportProduct</li>
<li>to create a new ImportProduct</li></ul>
In the case of viewing a current ImportProduct, the ImportProduct object
passed to this class is already defined and all required properties
already known.  In the case of creating a totally new ImportProduct, the
ImportProduct passed in to this class is essentially an empty skeleton,
with only the ProductGroup (parent of the ImportProduct in the JTree)
filled in. <br>
It is important to distinguish in the constructor, if we are dealing with
an existing ImportProduct and just changing some of its fields or if
we are creating a totally new ImportProduct.  If the ImportProduct_num
for the ImportProduct passed in is MISSING, then we know that we are
creating a new ImportProduct object.  In the constructor then, set
the <i>__bln_new_object = true </i> flag.  The <b><i>__bln_new_object</i></b>
flag is important because:<br>
<ul>
<li>we need to set up a dialog to ask the user what <b>type</b> of
ImportProduct they are creating (using <i>RiversideDB_ImportType</i>)
<li>we need to get an ImportProduct_num for the new ImportProduct and in
doing so, we need to actually write the ImportProduct to the database.
Once it is written to the database and the user "Cancels" out of the GUI
later without saving changes, we need to know to go back and delete the
ImportProduct</li>
<li>we can mark the object as dirty (using the <i>setDirty(true)</i> flag)
 since it is an entirely new object</li>
<li>we need to add the fields for <i>DBUser</i>, <i>DBGroup</i>, and
<i>DBPermissions</i> based on the information already known by the DMI for
this user.</li>
<li> we do not have to confirm changes made to a totally new object like we do
if the user is changing an existing object</li>
<li>we need to know to add a new node to the JTree</li></ul>
When creating a <b>new</b> ImportProduct, the most important field in
to fill in is the (autonum) ImportProduct_num field mentioned above since
we can not write any updates to the database for this ImportProduct or for
its related ImportConf objects without knowing the ImportProduct_num.  By
writing the object to the database and retrieving it we can get its
ImportProduct_num. <br><br>
At this point, whether we are creating a new ImportProduct or modifying an
existing one, we assign the ImportProduct to the variable known throughout
this class as: <b><i>__gui_RTi_ImportProduct</i></b>.  We also set up
the variable known throughout this class as: <b><i>
__gui_RTi_ImportConf_vect</i></b> which is a Vector of ImportConf objects that
are related to the <i>__gui_RTi_ImportProduct</i>.  If we have created
a totally new ImportProduct, the <i>__gui_RTi_ImportConf_vect</i> is an
empty Vector; otherwise, it is a Vector of ImportConf objects read directly
from the database, based on the ImportProduct_num of the (already existing)
ImportProduct.<br><br>
Finally, the constructor also:<ul><li> sets up Vectors of (static) reference data,
read directly from the database thay will be used throughout the class</li>
<li>calls method: <i>init_layout_GUI()</i> which creates and sets up the
GUI components</li></ul><br>
@param dmi Instance of RiversideDB_DMI that has already been opened.
@param title String for title of JFrame.
@param measlocgrp RiversideDB_MeasLocGroup object for GUI to display in
this GUI. If a new MeasLocGroup is being created, the identifier field will
be MISSING.
*/
public RiversideDB_MeasLocGroup_JFrame(
				RiversideDB_DMI dmi,
				RiversideDB_WindowManager windowManager,
				String title,
				RiversideDB_MeasLocGroup measlocgrp )
{
	super( title );
	String routine = __class + ".constructor";

	// Add RTI icon
	JGUIUtil.setIcon( this, JGUIUtil.getIconImage() );


	// Set global variables
 	__dmi = dmi;
 	__db_RTi_MeasLocGroup = measlocgrp;
 	__preselected_MeasLocGroup_num = measlocgrp.getMeasLocGroup_num();

 	// Set protected member in the base RiversideDB_EditorBase_JFrame class.
 	_windowManager = windowManager;

	//
	String mlg_id = null;
	mlg_id = measlocgrp.getIdentifier();
	if ( ( mlg_id == null ) || ( mlg_id.length() <= 0 ) ) {
		// must be creating a totally new MeasLocGroup object
 		__bln_new_object = true;
 		__db_RTi_MeasLocGroup.setDirty ( true );
	}

	// Get current user
	try {
 		__DBUser = __dmi.getDBUser();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}
	
	// Get all DBUsers
	try {
 		__RTi_DBUser_vect = __dmi.readDBUserList();
	}
	catch ( Exception e) {
		Message.printWarning( 2, routine, e );
 		__RTi_DBUser_vect = new Vector();
	}

	// Get MeasLocGroup vector
	try {
 		__RTi_MeasLocGroup_vect = __dmi.readMeasLocGroupList( );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
 		__RTi_MeasLocGroup_vect = new Vector();
	}

	// Get Vector of DBUserMeasLocGroupRelation objects based
	// on the current MeasLocGroup selected...
	int numb_users = 0;
	if ( __bln_new_object ) {
		//no DBUserMeasLocGroupRelation objects associated yet.
 		__db_RTi_DBUserMeasLocGroupRelation_vect = new Vector();
		//add root user- root should always belong to every MeasLocGrp
		RiversideDB_DBUserMeasLocGroupRelation dbumlgr = new
		RiversideDB_DBUserMeasLocGroupRelation();

		numb_users = __RTi_DBUser_vect.size();
		RiversideDB_DBUser du = null;
		for ( int i=0; i<numb_users; i++ ) {
			du = (RiversideDB_DBUser) __RTi_DBUser_vect.elementAt(i);
			if ( du == null ) {
				continue;
			}
			if ( du.getLogin().equalsIgnoreCase("root" ) ) {
				dbumlgr.setDBUser_num(du.getDBUser_num() );
				dbumlgr.setDBUser_num(
 				__preselected_MeasLocGroup_num );
				//add it to the vector
 				__db_RTi_DBUserMeasLocGroupRelation_vect.
				addElement( dbumlgr );
				break;
			}
			du = null;
		}
	}
	else {
		try {
 			__db_RTi_DBUserMeasLocGroupRelation_vect = __dmi.
			readDBUserMeasLocGroupRelationListForMeasLocGroup_num (
 			__preselected_MeasLocGroup_num );
		}
		catch ( Exception e ){
			Message.printWarning( 2, routine, e );
		}
	}

	if ( __bln_new_object ) { //if we are creating a New MeasLocGroup object
		//assign user permissions for new object
 		__db_RTi_MeasLocGroup.
		setDBUser_num( __DBUser.getDBUser_num() );
 		__db_RTi_MeasLocGroup.
		setDBGroup_num( __DBUser.getPrimaryDBGroup_num());
 		__db_RTi_MeasLocGroup.setDBPermissions(
 		__DBUser.getDefault_DBPermissions() );

		//Set optable to "" until we know what to do with this!
 		__db_RTi_MeasLocGroup.setOptable( "" );
	}

	// See if user has permissions to change the MeasLocGroup
	try {
 		__canWriteMeasLocGroup =
 		__dmi.canWrite( __db_RTi_MeasLocGroup.getDBUser_num(),
 		__db_RTi_MeasLocGroup.getDBGroup_num(),
 		__db_RTi_MeasLocGroup.getDBPermissions() );
	}
	catch ( Exception e ) {
		Message.printWarning ( 2, routine, e);
 		__canWriteMeasLocGroup = false;
	}

	// Insets order: top, left, bottom, right
 	__insets = new Insets( 2, 2, 5, 2);

	// Create/layout the GUI ...
	init_layout_GUI();

	// Add window listener
	addWindowListener ( this );

	// Setup frame to do nothing on close so that we can take over
	// control of window events.
	setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );

	setResizable( false );

}

/**
This method simply goes through all the fields in the GUI and checks that
each fields is: <ul> <li>filled in if it is a required field </li>
<li>contains valid values (for JTextFields, for example)</li>
</ul>If an invalid entry is encountered, the method displays a warning message,
indicating the fields with invalid values. (Prints one error message at the end
indicating any fields that need to
be filled in before a save can occur.)
</ul>
@exception Exception thrown if a required filled does not
have a value.
*/
protected void checkRequiredInput() throws Exception
{   String mssg;
	StringBuffer buffer = new StringBuffer();

	// Identifier
	String id_str = null;
	id_str = __id_JTextField.getText().trim();
	if (( id_str == null ) || ( id_str.length() <= 0 ) ) {
		mssg = "Unable to update database without a Identifier.\n";
		buffer.append( mssg );
	}
	if ( __bln_new_object ) {
		// Make sure no other MeasLocGroup has this same name
		int size = 0;
		if ( __RTi_MeasLocGroup_vect != null ) {
			size = __RTi_MeasLocGroup_vect.size();
		}
		RiversideDB_MeasLocGroup mlg = null;
		for ( int i=0; i<size; i++ ) {
			mlg = (RiversideDB_MeasLocGroup )
 				__RTi_MeasLocGroup_vect.elementAt(i);
			if ( mlg == null ) {
				continue;
			}

			if ( mlg.getIdentifier().equalsIgnoreCase( id_str ) ) {
				mssg = "There is already a MeasLocGroup in the"
					+ " database with Identifier: \""
					+ id_str
					+ "\". Unable to update database.\n";
				buffer.append( mssg );
			}
		}
	}

	// Name
	String name_str = null;
	name_str = __name_JTextField.getText().trim();
	if (( name_str == null ) || ( name_str.length() <= 0 ) ) {
		mssg = "Unable to update database without a Name.\n";
		buffer.append( mssg );
	}

	// Description
	String desc_str = null;
	desc_str = __desc_JTextField.getText().trim();
	if (( desc_str == null ) || ( desc_str.length() <= 0 ) ) {
		mssg  = "Unable to update database without a Description.\n"; 
		buffer.append( mssg );
	}

	// Optable (optional) Nothing to check.

	// Make sure there is at least 1 DBUserMeasLocGroupRelation item
	// (right-hand list)
	if ( __selected_users_ListModel.isEmpty() ) {
		mssg = "Unable to update database without at least one entry in"
			+ " the right-hand DBUserMeasLocGroupRelation list.\n";
		buffer.append( mssg );
	}

	if ( buffer.length() > 0 ) {
		mssg = "Please specify all required fields or cancel.";
		buffer.append( mssg );
		JOptionPane.showMessageDialog( this, buffer.toString(),
			"Warning", JOptionPane.WARNING_MESSAGE );
		throw new Exception ( mssg );
	}
	buffer = null;
}

/**
This method does the following:<ul>
<li>calls <i>checkRequiredInput</i> which makes sure all the required fields in the GUI are filled out and have valid entries</li>
<li>calls <i>update_RiversideDB_objects</i> which is called to
<b>create</b> and update the <i><b>__gui_RTi_MeasLocGroup</b></i> and
<i> <b>__gui_RTi_DBUserMeasLocGroupRelation_vect</b></i> objects in memory,
mark them dirty, and add messages to the <i>__dirty_vect</i> Vector.
</li>
<li>creates a confirmation message if the <i>__gui</i> versions are dirty,
prompting the user to verify if they want to save their changes
(all the changes are listed out from the <i>__dirty_vect</i>)</li>
<li>updates the database by calling, <i>update_database</i></li>
<li>closes the GUI and destroys it</li>
</ul>
*/
public void closeGUI()
{
	String routine = __class + ".closeGUI";

	boolean blnUpdated = true;
	
	// Required fields
	try {
		checkRequiredInput();
	} catch ( Exception e ) {
		// Do not Update object in memory or in database.
		Message.printWarning( 2, routine, e );
		blnUpdated = false;
	}

	// Update the object in memory
	if ( blnUpdated ) {
		try {
			update_RiversideDB_objects();
		} catch( Exception e ) {
			Message.printWarning( 2, routine, e );
			blnUpdated = false;
		}
	}
	
	if( blnUpdated )  {
		if ( ( __gui_RTi_MeasLocGroup.isDirty()         ) ||
		     ( __blnDBUserMeasLocGroupRelation_setDirty ) ) {
			// Prompt user to save their changes to the database.
			int x = 0;
			x = new ResponseJDialog ( this, "Save Changes",
				"Save your changes before closing?",
				ResponseJDialog.YES | ResponseJDialog.NO
				| ResponseJDialog.CANCEL).response();

			if ( x == ResponseJDialog.YES ) {
				// Update database itself
				try {
					// Update database itself
					update_database();
				} catch( Exception e ) {
					Message.printWarning( 2, routine, e );
					blnUpdated=false;
				}

				// SetVisible(false) and dispose()
				windowManagerClose();
			}
			else if ( x == ResponseJDialog.NO ) {
				// SetVisible(false) and dispose()
				windowManagerClose();
			}
		}
		else {
			// No changes, so close (setVisible(false) and dispose())
			windowManagerClose();
		}
	}
	/*
	else {
		if ( blnUpdated ) {
			//setVisible(false);
			//dispose();
			windowManagerClose();
		}
	}
	*/
}

/**
Creates a JPanel that contains the GUI components needed
for editing the RiversideDB_DBUserMeasLocGroupRelation table. The
GUI components consists of 2 JLists, one with all the DBUsers and
one with the selected DBUsers to relate to the current MeasLocGroup.
Other componentes include buttons to add/remove items from the JLists.
@return JPanel with all components.
*/
protected JPanel create_dbmlgr_panel ()
 {
	String routine = __class + ".create_dbmlgr_panel";
	
	JPanel panel = new JPanel();
	panel.setLayout( new GridBagLayout() );

	/////////////////////////////////////
	// LEFT List - LIST OF ALL DBUSERS
	// List of ALL DBUSERS
	/////////////////////////////////////
	
	JLabel all_users_JLabel = new JLabel ( "Users:" );
 	__all_users_ListModel   = new DefaultListModel();

	// Add all DBUsers to the ListModel
	int numb_users = __RTi_DBUser_vect.size();
	RiversideDB_DBUser du = null;
	for ( int i=0; i<numb_users; i++ ) {
		du = (RiversideDB_DBUser) __RTi_DBUser_vect.elementAt(i);
		if ( du == null ) {
			continue;
		}
 		__all_users_ListModel.addElement(
			// String.valueOf( du.getDBUser_num() ) + " - " +
			du.getLogin() + " - " + du.getDescription() );
		du = null;
	}

	// JList of DBUsers
 	__all_users_JList = new JList( __all_users_ListModel );
	// Allow multiple selection
 	__all_users_JList.setSelectionMode( 
 		ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
 	__all_users_JList.setVisibleRowCount( 10 );
 	__all_users_JList.setSelectedIndex  (  0 );

	// Add to scroll pane
	JScrollPane all_users_JScrollPane= new JScrollPane( __all_users_JList );
 	__all_users_JList.setToolTipText(
		"<HTML>Move users to right-hand list to add them<BR>as members to the current Location Group</HTML>" );
	// Dimension( width, height) of JScrollPane
	all_users_JScrollPane.setPreferredSize( new Dimension ( 150, 100 ) );
	all_users_JScrollPane.  setMinimumSize( new Dimension ( 150, 100 ) );
	all_users_JScrollPane.  setMaximumSize( new Dimension ( 150, 100 ) );

	/////////////////////////////////////////////
	//RIGHT LIST -- list of DBUserMeasLocRelation
	/////////////////////////////////////////////
	
	JLabel selected_users_JLabel = new JLabel(
		"Relation of Users to Current Location Group:" );
 	__selected_users_ListModel = new DefaultListModel();

	// Get list from DBUserMeasLocGroupRelation table for current MeasLocGrp
	Vector dbmlgr_vect = null;
	try {
		// _preselectedMeasLocGroup_num will be -999 if
		// creating a new MeasLocGroup.
		dbmlgr_vect = __dmi.
		readDBUserMeasLocGroupRelationListForMeasLocGroup_num (
 		__preselected_MeasLocGroup_num );
	}
	catch ( Exception e ){
		Message.printWarning( 2, routine, e );
		dbmlgr_vect = null;
	}

	int numb_dbmlgr = 0;
	if ( dbmlgr_vect != null ) {
		numb_dbmlgr = dbmlgr_vect.size();
	}
	RiversideDB_DBUserMeasLocGroupRelation dbmlgr = null;
	RiversideDB_DBUser dbuser = null;
	for ( int i=0; i<numb_dbmlgr; i++ ) {
		dbmlgr = (RiversideDB_DBUserMeasLocGroupRelation)
		dbmlgr_vect.elementAt(i);
		if ( dbmlgr == null ) {
			continue;
		}
		int dbuser_num = -999;
		dbuser_num = dbmlgr.getDBUser_num();
		//use that to get DBUser name
		try {
			dbuser = __dmi.readDBUserForDBUser_num( dbuser_num );
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e );
			dbuser = null;
		}
		if ( dbuser == null ) {
			continue;
		}
 		__selected_users_ListModel.addElement( dbuser.getLogin() );
		dbuser = null;
		dbmlgr = null;
	}

	// JList of DBUserMeasLocGroupRelation objects
 	__selected_users_JList = new JList( __selected_users_ListModel );

	// Single selection only
 	__all_users_JList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
 	__all_users_JList.setVisibleRowCount( 10 );
 	__all_users_JList.setSelectedIndex  (  0 );

	// Add list to scroll pane
	JScrollPane selected_users_JScrollPane = 
		new JScrollPane( __selected_users_JList );
	// Set size of list Dimension( w, h )
	selected_users_JScrollPane.setPreferredSize( new Dimension ( 100, 100 ) );
	selected_users_JScrollPane.  setMinimumSize( new Dimension ( 100, 100 ) );
	selected_users_JScrollPane.  setMaximumSize( new Dimension ( 100, 100 ) );
 	__selected_users_JList.setToolTipText(
		"Members of the current Location Group" );

	// JButtons to move items from one list to the other
 	__clear_selected_JButton = new SimpleJButton( "Clear Selected", this);
 	__clear_selected_JButton.setToolTipText(
 		"Remove the user selected in the right list from the Location Group." );

 	__move_right_JButton = new SimpleJButton( ">", this );
 	__move_right_JButton.setToolTipText( "<HTML>Move user selected in list " 
 		+ "of all users (left list)<BR>to list of users in the current "
 		+ "location group (right list)</HTML>" );

		// Add Left JLabel and JList
		int x=0;
		int y=0;

		JGUIUtil.addComponent(
			panel, all_users_JLabel,
			x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

		JGUIUtil.addComponent(
			panel, all_users_JScrollPane,
			x, ++y, 1, 3, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );

		++y;
		// Move right arrow button
		JGUIUtil.addComponent(
			panel, __move_right_JButton,
			++x, y+1, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

		// Add RIGHT JLabel and JList
		--y;
		JGUIUtil.addComponent(
			panel, selected_users_JLabel,
			++x, --y, 2, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

		JGUIUtil.addComponent(
			panel, selected_users_JScrollPane,
			x, ++y, 1, 3, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.NORTHWEST );

		++y;
		// Add clear selected button
		JGUIUtil.addComponent(
			panel, __clear_selected_JButton,
			++x, y+1, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

	return panel;
}

/**
Finalizes and cleans up.
*/
protected void finalize() throws Throwable
{
 	__insets          = null;
 	__dmi             = null;
 	__browser_JButton = null;
 	__close_JButton   = null;
 	__cancel_JButton  = null;
 	__apply_JButton   = null;

	// Finalize the base RiversideDB_EditorBase_JFrame class
	super.finalize();
}


/**
This method is called from the constructor to create and layout the
GUI components.  It calls the method: <i>create_main_panel</i>, which
in turn calls methods named such as: <i>assemble_tab_files()</i>,
<i>assemble_tab_timeseries</i>, etc.  These methods all create
GUI components and put them together in a <i>GridBagLayout</i>. They
do not worry about setting correct values in the components' fields, but just
gets the components set up.  The main components in the GUI consists of:
<ul><li>JPanel that contains general information pertinent to the MeasLocGroup
object at the top of the GUI</li>
<li>a series of tabs in a JTabbedPane with fields for the MeasLocGroup and
fields for the related DBUserMeasLocGroupRelation objects.
</ul> </li>
<li>a panel added at the bottom that includes the standard buttons for:
<ul><li>apply</li><li>close</li><li>cancel</li></ul></li></ul>
At the end of <i>init_layout_gui</i>, a
call to <i>update_gui_fields</i> is made which fills in all the
fields of the GUI according to the MeasLocGroup and DBUserMeasLocGroupRelation
objects currently being worked with.
*/
private void init_layout_GUI( ) {
	String routine = __class + ".init_layout_GUI";

	// Create overall Main Panel
	JPanel main_panel = null;
	main_panel        = new JPanel();
	main_panel.setLayout( new GridBagLayout() );

	JPanel top_panel = null;
	top_panel        = new JPanel();
	top_panel.setLayout( new GridBagLayout() );

	// Components
	// Identifier JTextField and JLabel
	JLabel id_JLabel = new JLabel ( "Identifier:" );
 	__id_JTextField  = new JTextField( 20 );
	id_JLabel.setToolTipText(
		"Identifier (required)");

	// Name JTextField and JLabel
	JLabel name_JLabel = new JLabel ( "Name:"  );
 	__name_JTextField  = new JTextField( 40 );
	name_JLabel.setToolTipText(
		"Descriptive Name (required)" );

	// Description JTextField and JLabel
	JLabel desc_JLabel = new JLabel ( "Description:" );
 	__desc_JTextField  = new JTextField( 40 );
	desc_JLabel.setToolTipText(
		"Location Description (required)" );

	// Optable JTextField and JLabel
	JLabel optable_JLabel = new JLabel ( "Optable:"  );
	__optable_JTextField  = new JTextField( 30 );
	optable_JLabel.setToolTipText(
		"Absolute path to the Optable file (optional)" );
	
	// Optable browse button	
	__browser_JButton  = new SimpleJButton( "Browse", this );	

	// measlocgrouprelation panel
	JPanel mlgr_JPanel = null;
	mlgr_JPanel = create_dbmlgr_panel();

	// Close panel
	JPanel close_JPanel = new JPanel();
	close_JPanel.setLayout( new GridBagLayout() );

 	__close_JButton  = new SimpleJButton( "Close",  this );
 	__cancel_JButton = new SimpleJButton( "Cancel", this );
 	__apply_JButton  = new SimpleJButton( "Apply",  this );

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


	// Layout GUI

	try {
		int x =0;
		int y =0;

		// Identifier JLabel
		JGUIUtil.addComponent(
			top_panel, id_JLabel,
			x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );
		// Identifier JTextField
		JGUIUtil.addComponent(
			top_panel, __id_JTextField,
			++x, y, 5, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

		x=0;
		// Name JLabel
		JGUIUtil.addComponent(
			top_panel, name_JLabel,
			x, ++y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );
		// Name JTextField
		JGUIUtil.addComponent(
			top_panel, __name_JTextField,
			++x, y, 5, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

		x=0;
		// Description JLabel
		JGUIUtil.addComponent(
			top_panel, desc_JLabel,
			x, ++y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );
		// Description JTextField
		JGUIUtil.addComponent(
			top_panel, __desc_JTextField,
			++x, y, 5, 1, 0.0, 0.0, __insets,
			GridBagConstraints.HORIZONTAL,
			GridBagConstraints.WEST );

		x=0;
		// Optable JLabel
		JGUIUtil.addComponent(
			top_panel, optable_JLabel,
			x, ++y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );
		// Optable JTextField
		JGUIUtil.addComponent(
			top_panel, __optable_JTextField,
			++x, y, 4, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.CENTER );
		// Optable browser JButton
		x++; x++; x++;
		JGUIUtil.addComponent(
			top_panel, __browser_JButton,
			++x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );	

		// Assemble overall panel
		x=0;
		y=0;
		// Top panel
		JGUIUtil.addComponent(
			main_panel, top_panel,
			x, y, 1, 1, 1.0, 0.0, __insets,
			GridBagConstraints.EAST,
			GridBagConstraints.CENTER );

		// DBUserMeasLocGroupRelation panel
		JGUIUtil.addComponent(
			main_panel, mlgr_JPanel,
			x, ++y, 1, 1, 1.0, 0.0, __insets,
			GridBagConstraints.EAST,
			GridBagConstraints.CENTER );

		// Close panel
		JGUIUtil.addComponent(
			main_panel, close_JPanel,
			x, ++y, 1, 1, 1.0, 0.0, __insets,
			GridBagConstraints.EAST,
			GridBagConstraints.CENTER );

	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine,
			"Error laying out the Main portion of the "+
			"graphical display for Time Series.");
		Message.printWarning( 2, routine, e );
	}

	// NOW that the specific panels have been created, fill in the values
	// for all the FIELDS!
	update_GUI_fields();

	// Pack and set visible
	getContentPane().add( "Center",	main_panel );
	pack();
	JGUIUtil.center (this );
	setVisible( true );
}

/**
This method: <ul>
<li>makes a confirmation message to verify that the user wants to save the
changes (and lists out all the changes from the <i>__dirty_vect</i>) <b>if</b>
a new MeasLocGroup was <b>not</b> created (if a new MeasLocGroup was created,
we do not want to re-confirm every change)</li>
<li>writes the <i>__gui</i> version of the objects to the database</li>
<li>if the objects were successfully written to the database, the
<i>__gui</i> objects are marked as <b>not</b> dirty
( <i>setDirty(false)</i></li>
<li><i>__dirty_vect</i> is cleared out</li>
<li>if we created a new MeasLocGroup object (<i>__bln_new_object == true</i>),
add a new node on the JTree in the main application  - <b>or</b> -  update the
existing MeasLocGroup node on the JTree with the new changes.</li>
</ul>
@exception Exception thrown if error encountered.
*/
protected void update_database() throws Exception {
	String routine = __class + ".update_database";

	//holds messages from __dirty_vect
	StringBuffer b = new StringBuffer();
	for ( int i=0; i< __dirty_vect.size(); i++ ) {
		if ( i == ( __dirty_vect.size()-1) ) {
			b.append( (String) __dirty_vect.elementAt(i) );
		}
		else {
			b.append( (String) __dirty_vect.elementAt(i) + "\n" );
		}
	}

	//if we are running in cautious mode and if we
	//are changing an already existing object ( not a completely new one),
	//then prompt the user before writing to the database
	if( ( __cautious_mode ) && ( ! __bln_new_object ) ) {
		if ( ( __gui_RTi_MeasLocGroup.isDirty() ) ||
		( __blnDBUserMeasLocGroupRelation_setDirty ) ){
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

	//MeasLocGroup is dirty
	RiversideDB_MeasLocGroup new_MeasLocGroup = null;
	if ( __gui_RTi_MeasLocGroup.isDirty() ) {
		if ( Message.isDebugOn ) {
			Message.printDebug( 2, routine,
			"Writing MeasLocGroup object to the database." );
		}

		//name may have changed for current MeasLocGroup or
		//if we created a new one, the title needs updating from
		//"New MeasLocGroup" to the real name
		this.setTitle( "RiverTrak® Assistant - " +
		"Location Group - " + __gui_RTi_MeasLocGroup.getIdentifier()
		);
		try {
			new_MeasLocGroup =
 			__dmi.writeMeasLocGroup( __gui_RTi_MeasLocGroup );
 			__gui_RTi_MeasLocGroup.setDirty( false );
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e );
			Message.printWarning( 1, routine,
			"Error writing Locations Group (MeasLocGroup object) " +
			"to the database.", this );

 			__gui_RTi_MeasLocGroup.setDirty( true );
		}


		//if MeasLocGroup was dirty, meant that the
		//something in the object was changed or an
		//entirely new MeasLocGroup was added
		//-- update JTree to reflect this change.
		if ( __bln_new_object ) {
			addMeasLocGroupNode   ( __gui_RTi_MeasLocGroup );
		} else {
			updateMeasLocGroupNode( __gui_RTi_MeasLocGroup,
						__db_RTi_MeasLocGroup );
		}
	}
	else {
		Message.printStatus( 2, routine,
		"No changes were made to the MeasLocGroup object to update." );
	}

	// if new_MeasLocGroup is null, then we did not write a
	//MeasLocGroup object to the database - we should then have the
	//MeasLocGroup_num already stored in variable:
	//_preselected_MeasLocGroup_num
	int mlg_num = -999;
	if ( new_MeasLocGroup == null ) {
		mlg_num = __preselected_MeasLocGroup_num;
	}
	else {
		mlg_num = new_MeasLocGroup.getMeasLocGroup_num();
	}
	new_MeasLocGroup = null;

	//update the DBUserMeasLocGroupRelation table too if needed.
	//if we adding a totally new MeasLocGroup, we know
	//that all the DBUserMeasLocGroupRelation objects are
	//new and the db needs updated.
	if ( __blnDBUserMeasLocGroupRelation_setDirty )  {
		if ( ! __bln_new_object ) {
			//write to database as is... should already have
			//measlocgroup_num

			//write DBUserMeasLocGroupRelation() method
			//first deletes all Current DBUserMeasLocGroupRelation
			//items for the current MeasLocGroup and then adds
			//all the new ones passed in
			//in a Vector with the write method.
			try {
 				__dmi.writeDBUserMeasLocGroupRelation(
 				__gui_RTi_DBUserMeasLocGroupRelation_vect );
			}
			catch ( Exception e ) {
				Message.printWarning( 2, routine, e );
				Message.printWarning( 1, routine,
				"Error writing the " +
				"DBUserMeasLocGroupRelation objects "+
				"to the database.", this );
			}
		}
		else { //we are adding a completely new MeasLocGroup object
			//we know we need to update the database with the
			//DBUserMeasLocGroupRelation objects in the
			//_gui_DB... vector.  Before we do that though,
			//we need to update each of the
			//DBUserMeasLocGroupRelation objects in that Vector
			//with the correct MeasLocGroup_num (which we now have
			//since we write the MeasLocGroup object to the
			//database).
			int numb_dbumlgs = 0;
			if ( __gui_RTi_DBUserMeasLocGroupRelation_vect != null) {
				numb_dbumlgs =
 				__gui_RTi_DBUserMeasLocGroupRelation_vect.size();
			}
			RiversideDB_DBUserMeasLocGroupRelation dbumlg = null;
			for ( int i=0; i<numb_dbumlgs; i++ ) {
				dbumlg =
				(RiversideDB_DBUserMeasLocGroupRelation)
 				__gui_RTi_DBUserMeasLocGroupRelation_vect.
				elementAt(i);
				if ( dbumlg == null ) {
					continue;
				}
				dbumlg.setMeasLocGroup_num( mlg_num );

				//now update the vector with updated object
 				__gui_RTi_DBUserMeasLocGroupRelation_vect.set(
				i, dbumlg );
			}

			//now update database
			//write new DBUserMeasLocGroupRelation Vector
			//This write method first deletes all Current
			//DBUserMeasLocGroupRelation items for the current
			//MeasLocGroup and then adds all the new ones passed in
			//in a Vector with the write method.
			try {
 				__dmi.writeDBUserMeasLocGroupRelation(
 				__gui_RTi_DBUserMeasLocGroupRelation_vect );
			}
			catch ( Exception e ) {
				Message.printWarning( 2, routine, e );
				Message.printWarning( 1, routine,
				"Error writing the " +
				"DBUserMeasLocGroupRelation objects "+
				"to the database.", this );
			}
		}
	}
	//empty out dirty vector
 	__dirty_vect.clear();
}


/**
This method:<ul>
<li><b>creates the <i>__gui</i> versions of the objects!</b> This includes:
<ul><li>__gui_RTi_MeasLocGroup</li>
<li>__gui_RTi_DBUserMeasLocGroupRelation_vect</li></ul>
The <i>__gui</i> versions are created in this method by copying the <i>__db</i>
versions (which were originally created and set up in the constructor)</li>
<li>calls the <i>verify_fields</i> method.
The <i>verify_fields</i> method:<ul><li> fills in the <i>__gui</i> verions of the objects
in memory with the values that are currently set in the GUI</li>
<li>compares the values in the <i>__gui</i> version of the objects to the
<i>__db</i> version of the objects.  If the <i>__gui</i> versions differ
from the <i>__db</i> verions:<ul>
<li>the <i>__gui</i> versions are marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the
<i>__gui</i> differs from the <i>__db</i> version (the <i>__db</i> version
remember, represents how the object is in the database itself) </li></ul>
</li></ul></ul>
@param Exception thrown if error encountered.
*/
protected void update_RiversideDB_objects() throws Exception
{
	String routine = __class + ".update_RiversideDB_objects", mssg;

 	__gui_RTi_MeasLocGroup =
 		new RiversideDB_MeasLocGroup( __db_RTi_MeasLocGroup );

	// Get all fields
	try {
		verify_fields();
	} catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		mssg = "Unable to validate fields in MeasLocGroup GUI.";
		Message.printWarning( 2, routine, mssg );
		throw new Exception( mssg );
	}

}

/**
This method: <ul><li>determines if the current user, already known to the DMI,
has write permissions.  If the user does not have write permissions, the
<b>apply</b> and <b>close</b> buttons are disabled, leaving only the
<b>cancel</b> button enabled.</li><li>Fills in all the GUI fields according
to the data in the MeasLocGroup and related DBUserMeasLocGroupRelation objects.
</li></ul>
*/
protected void update_GUI_fields() {

	// Check which buttons should be enabled
	if ( ! __canWriteMeasLocGroup ) {
 		JGUIUtil.setEnabled( __apply_JButton, false );
 		JGUIUtil.setEnabled( __close_JButton, false );
	}

	// Set text for the Identifier, Name, Description and Optable fields
 	__id_JTextField.setText     ( __db_RTi_MeasLocGroup.getIdentifier () );
 	__name_JTextField.setText   ( __db_RTi_MeasLocGroup.getName       () );
 	__desc_JTextField.setText   ( __db_RTi_MeasLocGroup.getDescription() );
	__optable_JTextField.setText( __db_RTi_MeasLocGroup.getOptable    () );

	// JLists are filled in at construction time, so do not need updating
}

/**
The method <ul><li> fills in the <i>__gui</i> verions of the objects
in memory with the values that are currently set in the GUI</li>
<li>compares the values in the <i>__gui</i> version of the objects to the
<i>__db</i> version of the objects.  If the <i>__gui</i> versions differ
from the <i>__db</i> verions:<ul>
<li>the <i>__gui</i> versions are marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the
<i>__gui</i> differs from the <i>__db</i> version (the <i>__db</i> version
remember, represents how the object is in the database itself) </li></ul>
</li></ul></ul> The object are: __gui_RTi_MeasLocGroup and
__gui_RTi_DBUserMeasLocGroupRelation_vect.
*/
public void verify_fields() throws Exception {
	String routine = __class + ".verify_fields";

	// Identifier
	String gui_id = null;
	String db_id  = null;
	gui_id = __id_JTextField.getText().trim();
	db_id  = __db_RTi_MeasLocGroup.getIdentifier();

	if ( ! db_id.equalsIgnoreCase( gui_id ) ) {
 		__gui_RTi_MeasLocGroup.setDirty( true );
 		__dirty_vect.addElement(
		"Change Identifier from \"" + db_id +
		"\" to \"" + gui_id + "\"");

 		__gui_RTi_MeasLocGroup.setIdentifier( gui_id );
	}

	// Name
	String gui_name = null;
	String db_name  = null;
	gui_name = __name_JTextField.getText().trim();
	db_name  = __db_RTi_MeasLocGroup.getName();

	if ( ! db_name.equalsIgnoreCase( gui_name ) ) {
 		__gui_RTi_MeasLocGroup.setDirty( true );
 		__dirty_vect.addElement(
		"Change Name from \"" + db_name +
		"\" to \"" + gui_name + "\"");
 		__gui_RTi_MeasLocGroup.setName( gui_name );
	}

	// Description
	String gui_desc = null;
	String db_desc  = null;
	gui_desc = __desc_JTextField.getText().trim();
	db_desc = __db_RTi_MeasLocGroup.getDescription();

	if ( ! db_desc.equalsIgnoreCase( gui_desc ) ) {
 		__gui_RTi_MeasLocGroup.setDirty( true );
 		__dirty_vect.addElement(
		"Change Description from \"" + db_desc +
		"\" to \"" + gui_desc + "\"");
 		__gui_RTi_MeasLocGroup.setDescription( gui_desc );
	}
	
	// Optable
	String gui_optable = null;
	String db_optable  = null;
	gui_optable = __optable_JTextField.getText().trim();
	db_optable  = __db_RTi_MeasLocGroup.getOptable();

	if ( ! db_optable.equalsIgnoreCase( gui_optable ) ) {
 		__gui_RTi_MeasLocGroup.setDirty( true );
 		__dirty_vect.addElement(
			"Change Optable from \"" + db_optable +
			"\" to \"" + gui_optable + "\"");
 		__gui_RTi_MeasLocGroup.setOptable( gui_optable );
	}

	//see if the vector of MeasLocGroupRelation objects has changed
	//We know there is at least 1 entry since we checked for that
	//in checkRequiredInput()

	// Get items in right-hand list
	int numb_relations = __selected_users_ListModel.size();

	// Make new __gui_DBUserMeasLocGroupRelation Vector
 	__gui_RTi_DBUserMeasLocGroupRelation_vect = new Vector();

	RiversideDB_DBUser user = null;
	RiversideDB_DBUserMeasLocGroupRelation dbumlg = null;
	for ( int i=0; i<numb_relations; i++ ) {
		//for each User selected, get the DBUser object so we can
		//get DBUser_num
		try {
			user = __dmi.readDBUserForLogin( (String)
 			__selected_users_ListModel.elementAt(i) );
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e);
		}
		if ( user == null ) {
			continue;
		}
		//make a new DBUserMeasLocGroupRelation
		dbumlg = new RiversideDB_DBUserMeasLocGroupRelation();
		dbumlg.setDBUser_num( user.getDBUser_num() );
		//if we are creating a totally new MeasLocGroup, the
		//_preselected_MeasLocGroup_num will be -999 and will
		//need to be updated AFTER dmi.writeMeasLocGroup() is
		//called since that will return a new MeasLocGroup object
		//that contains the new MeasLocGroup_num.
		dbumlg.setMeasLocGroup_num( __preselected_MeasLocGroup_num );

		//add to Vector
 		__gui_RTi_DBUserMeasLocGroupRelation_vect.
		addElement( dbumlg );

		user = null;
		dbumlg = null;
	}
	
	//See if the __gui_ version of the Vector of objects differs
	// from the __db_ version - convert DBUserMeasLocGroupRelation
	// objects to simple strings in format: MeasLocGroup_num,DBUser_num
	// For example: 4, 1 , so that we can compare using our generic
	// utility in __Util class.
	Vector v_add = null;
	Vector v_rem = null;

	// Make Vector containing simple String objects instead of
	// DBUserMeasLocGroupRelation objects
	Vector db_string_vect = new Vector();
	Vector gui_string_vect = new Vector();
	int db_vect_size = __db_RTi_DBUserMeasLocGroupRelation_vect.size();
	int gui_vect_size = __gui_RTi_DBUserMeasLocGroupRelation_vect.size();
	RiversideDB_DBUserMeasLocGroupRelation m = null;
	for ( int i=0; i<db_vect_size; i++ ) {
		m = (RiversideDB_DBUserMeasLocGroupRelation )
 		__db_RTi_DBUserMeasLocGroupRelation_vect.elementAt( i );
		if ( m == null ) {
			continue;
		}
		db_string_vect.addElement(
		m.getDBUser_num() + "," + m.getMeasLocGroup_num() );
		m = null;
	}
	for ( int i=0; i<gui_vect_size; i++ ) {
		m = (RiversideDB_DBUserMeasLocGroupRelation )
 		__gui_RTi_DBUserMeasLocGroupRelation_vect.elementAt( i );
		if ( m == null ) {
			continue;
		}
		gui_string_vect.addElement(
		m.getDBUser_num() + "," + m.getMeasLocGroup_num() );
		m = null;
	}

	// Now compare original vector to new vector
	// to see if there are any differences
	v_add = findAdditions ( db_string_vect, gui_string_vect );
	v_rem = findRemovals  ( db_string_vect, gui_string_vect );
	if ( ( v_add.size() == 0 ) && ( v_rem.size() == 0 )) {
		// no changes between the original and current list of objects.
	} else {
		// need to update.
 		__blnDBUserMeasLocGroupRelation_setDirty = true;
 		__dirty_vect.addElement(
			"Change list of DBUsers related to this MeasLocGroup "
			+ "table?" );
	}
	v_add = null;
	v_rem = null;
}


/////////////////////////////////////////////////////////////////
////////////////////////  ACTIONS ///////////////////////////////
/////////////////////////////////////////////////////////////////
/**
The event handler manages action events.
@param event Event to handle.
*/
public void actionPerformed (ActionEvent event)
{
	String routine = __class + ".actionPerformed", mssg;

	try {
	Object source  = event.getSource();

	if ( source.equals( __browser_JButton ) ) {
		
		// Prompt for the RiverTrak optable file...
		// Open a file chooser in the standard "Projects" directory on the
		// current drive: ?:\Program Files\RTi\Projects
		String curr_dir = IOUtil.getProgramWorkingDir();

		// REVISIT [LT] 2005-01-05 SAM's comment 
		//   "Check IOUtil for method or File class" What to look for?
		String dir = null;
		if ( curr_dir.length() > 0 ) {
			// Just need the drive here, which is the first letter(s)...
			int ind = curr_dir.indexOf( ":" );
			// Get the drive letter, followed by.  For example: "I:"
			dir = curr_dir.substring( 0, ind + 1 )
				+ "\\Program Files\\RTi\\Projects";
			if ( ! IOUtil.fileExists( dir ) ) {
				// Set it to root
				dir = curr_dir.substring( 0, ind+1 );
			}
		}
		if ( Message.isDebugOn ) {
			mssg = "Setting File Chooser directory to: '" + dir + "'";
			Message.printDebug( 3, routine, mssg );
		}
 	
		JFileChooser chooser = JFileChooserFactory.createJFileChooser();
		String openFileTitle = "Please select the Optable File";
		chooser.setDialogTitle( openFileTitle );
		File file = new File( dir );
        	
		// If "file" is null, then it will open the file chooser in the user's
		// default directory
		// ( usually "My Documents" on Windows OS, and $HOME on Unix )
		chooser.setCurrentDirectory( file );
		SimpleFileFilter filter = new SimpleFileFilter(
				"optable", "RiverTrak optable file" );
		chooser.addChoosableFileFilter(filter);
		chooser.setFileFilter( filter);
		int returnVal = chooser.showOpenDialog( this );
        	
		if ( returnVal == JFileChooser.APPROVE_OPTION ) {
			__optable_JTextField.setText(
				chooser.getSelectedFile().getPath() );
		}	
	}
	
	else if ( source.equals( __apply_JButton ) ) {
		boolean blnUpdated = true;

		// REQUIRED Fields
		try {
			checkRequiredInput();
		}
		catch ( Exception e ) {
			//then there was an error so do not
			//update object in memory or in database.
			Message.printWarning( 2, routine, e );
			blnUpdated= false;
		}

		// Update the "GUI" object in memory
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
				//update the __gui_ versions to be the
				//the memory version again since update failed.
 				__gui_RTi_MeasLocGroup = null;
 				__gui_RTi_MeasLocGroup = new
				RiversideDB_MeasLocGroup( __db_RTi_MeasLocGroup);
			}
		}

		if ( blnUpdated ) {
			//if we got this far, the database was updated,
			//so update objects in memory.  The __gui_ object
			//was written to the database, so now the
			//_db_ object should equal the __gui_ object
 			__db_RTi_MeasLocGroup = null;
 			__db_RTi_MeasLocGroup = new RiversideDB_MeasLocGroup(
 			__gui_RTi_MeasLocGroup );
 			__db_RTi_MeasLocGroup.setDirty( false );

 			__gui_RTi_MeasLocGroup = null;

		}
	}
	
	else if ( source.equals( __cancel_JButton ) ) {
		if( ( __cautious_mode ) && ( ! __bln_new_object ) ) {
			//create and update __gui_ objects in memory
			//The only reason to do this is to provide the
			//user with a Confirm Cancel message.
			try {
				update_RiversideDB_objects();
			}
			catch( Exception e ) {
				Message.printWarning( 2, routine, e);

				windowManagerClose();
				//setVisible(false);
				//dispose();
			}

			if ( ( __gui_RTi_MeasLocGroup.isDirty()) ) {
				//holds messages from __dirty_vect
				StringBuffer b = new StringBuffer();
				for (int i=0;i< __dirty_vect.size(); i++ ) {
					b.append( (String)
 					__dirty_vect.elementAt(i) +
					"\n" );
				}
				//write out a confirmation message.
				int x = new ResponseJDialog(
				this, "Cancel Changes",
				"Cancel the following changes?\n" +
				"Cancel changes?\n" +
				b.toString()+"?",
				ResponseJDialog.YES | ResponseJDialog.NO ).
				response();

				if ( x == ResponseJDialog.YES ) {
					//write to log file
					Message.printStatus( 5,
					routine,
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
	
	else if ( source.equals( __clear_selected_JButton ) ) {
		//get item selected in right-hand list
		String login_toDel = null;
		login_toDel = (String) __selected_users_JList.getSelectedValue();

		//user can not remove root from list.
		if ( login_toDel.equalsIgnoreCase( "root" ) ) {
			Message.printWarning( 1, routine,
			"Unable to remove user root from " +
			"DBUserMeasLocGroupRelation list for MeasLocGroup. ",
			this );
		}
		else {
			//remove item selected.
 			__selected_users_ListModel.removeElement( login_toDel );

			//set first item in list selected
			if ( __selected_users_ListModel.size() >= 1 ) {
 				__selected_users_JList.setSelectedIndex(0);
			}
		}

	} // end clear_selected
	
	else if ( source.equals( __close_JButton ) ) {
		closeGUI();
	}

	else if ( source.equals( __move_right_JButton ) ) {
		//Left hand list (all DBUsers) formated as:
		//"Hydro - Patti,Denis"
		//Right hand list (selected DBUsers) formated as:
		//"Hydro"
		//get list of items selected in Left list
		Object [] selNames_to_move =
 		__all_users_JList.getSelectedValues();

		boolean blnMove = true;
		//go through items to make sure they already aren't in the
		//right-hand list
		String fullname = null;
		for ( int i=0; i<selNames_to_move.length; i++ ) {
			//format of String:
			//" 1 - Hydro - Patti,Denis"
			fullname = (String) selNames_to_move[i];

			if ( fullname == null ) {
				continue;
			}
			String login = null;
			int ind = -999;
				ind = fullname.indexOf( " -" );
			if ( ind > 0 ) {
				login = fullname.substring(0, ind);
			}
			if ( login == null ) {
				continue;
			}
			//now make sure login does not exist
			//on right list.
			if ( __selected_users_ListModel.indexOf(login) >= 0 ) {
				//do nothing, because already
				//have that item in the right list
				Message.printWarning( 2, routine,
				"DBUser: \"" +login + "\" already " +
				"in list of selected DBUsers." );

				blnMove= false;
			}
			if ( blnMove ) {
				//actually move login name over...
 				__selected_users_ListModel.addElement( login );
				//set it selected
 				__selected_users_JList.
				setSelectedValue( login, true );
			}

			fullname = null;
			login = null;
		}
	}//end move_right

	source = null;
	} // End of try
	catch ( Exception e ) {
		if ( Message.isDebugOn ) {
			Message.printWarning ( 2, routine, e );
		}
	}
}

// WINDOW EVENTS

/**
*/
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
	if ( __canWriteMeasLocGroup ) {
	 	closeGUI();
	}
	else {
		//just close as a cancel ( setVisible(false) and dispose() )
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

	Message.printStatus( 6, "", "WindowManager Close" );
	if (__bln_new_object) {
		_windowManager.closeWindowInstance(
			_windowManager.WINDOW_MEASLOCGROUP,
			"CREATING NEW OBJECT");
	}
	else {
		// The measloc_num is the unique ID for this window.
		_windowManager.closeWindowInstance(
			_windowManager.WINDOW_MEASLOCGROUP,
			new Long(__db_RTi_MeasLocGroup.getMeasLocGroup_num() ));
	}
}


/////////////////////////////////////////////////////////////////
/////////////////////// END ACTIONS /////////////////////////////
/////////////////////////////////////////////////////////////////

}// end RiversideDB_MeasLocGroup_JFrame class

