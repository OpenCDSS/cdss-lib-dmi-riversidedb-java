//--------------------------------------------------------------------------
// RiversideDB_ProductGroup_JFrame -
//
//	Contains the code used to create the GUI for 
//	RiversideDB_ProductGroup objects.
//
//--------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
//--------------------------------------------------------------------------
// History:
//
// 2003-06-03	Morgan Sheedy, RTi	Initial Implementation
//
// 2003-06-08	AML, RTi		(Security)DBUser, DBGroup,
//					DBPermissions implemented.
//
// 2003-07-01	AML, RTi		code clean up
//
// 2003-12-08	AML, RTi		Pass objects around as a result
//					of using RTi's SimpleJTree 
//
// 2004-07-14	AML, RTi		General code cleaning.
// 2004-10-25 Luiz Teixeira, RTi	Upgraded by moving it from the 
//					main application (RiverTrak Assistant)
//					to the RiversideDB_DMI library.
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
// 2005-01-07 Luiz Teixeira, RTi	Added some temporary JOption messages
//					in the constructor until all the existing
//					issues there (see REVISIT) is resolved.
//--------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import  java.awt.Color;
import  java.awt.Component;
import  java.awt.Dimension;
import  java.awt.GridBagConstraints;
import  java.awt.GridBagLayout;
import  java.awt.Insets;
import  java.awt.event.ActionEvent;
import  java.awt.event.ActionListener;
import  java.awt.event.WindowEvent;
import  java.awt.event.WindowListener;
import  java.util.Vector;

import 	javax.swing.ImageIcon;
import 	javax.swing.JCheckBox;
import 	javax.swing.JFrame;
import 	javax.swing.JLabel;
import 	javax.swing.JOptionPane;
import 	javax.swing.JPanel;
import 	javax.swing.JTextField;

import  RTi.Util.GUI.JGUIUtil;
import  RTi.Util.GUI.ResponseJDialog;
import  RTi.Util.GUI.ResponseJDialog;
import  RTi.Util.GUI.SimpleJButton;
import  RTi.Util.GUI.SimpleJComboBox;
import  RTi.Util.GUI.SimpleJMenuItem;
import  RTi.Util.IO.IOUtil;
import  RTi.Util.IO.PropList;
import	RTi.Util.Message.Message;
import  RTi.Util.String.StringUtil;
import  RTi.Util.Time.DateTime;
import  RTi.Util.Time.TimeUtil;

import 	RTi.DMI.DMI;
import 	RTi.DMI.DMIUtil;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_DBUser;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_DMI;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_ProductGroup;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_ProductType;

/**
RiversideDB_ProductGroup_JFrame.
This class is laid out similarly to the other RiverTrakAssistant classes.
The general format of the class is laid out below, with the major
methods listed with their key functions.  The main object type from 
RiversideDB that are manipulated by this class is:<br><ul>
<li>RiversideDB_ProductGroup_JFrame (abbreviated ProductGroup_JFrame)</li></ul>
</br> <p><b>Constructor</b><br>
The constructor is called by an action in one of the JTrees of the 
main application.  The main application passes to this class a 
RiversideDB_ProductGroup.  This class is called in one of two cases: <br>
<ul><li>to view the properties of a current ProductGroup</li>
<li>to create a new ProductGroup</li></ul>
In the case of viewing a current ProductGroup, the ProductGroup object
passed to this class is already defined and all required properties 
already known.  In the case of creating a totally new ProductGroup, the 
ProductGroup passed in to this class is essentially an empty skeleton.<br>
It is important to distinguish in the constructor, if we are dealing with
an existing ProductGroup and just changing some of its fields or if
we are creating a totally new ProductGroup.  If the Identifier
for the ProductGroup passed in is MISSING, then we know that we are
creating a new ProductGroup object.  In the constructor then, set
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
At this point, whether we are creating a new ProductGroup or modifying an
existing one, we assign the ProductGroup to the variable known throughout
this class as: <b><i>__gui_RTi_ProductGroup</i></b>.
Finally, the constructor also:<ul><li> sets up Vectors of (static) reference data, 
read directly from the database thay will be used throughout the class</li>
<li>calls method: <i>init_layout_GUI()</i> which creates and sets up the 
GUI components</li></ul><br> 
</p>
<p><b>init_layout_gui</b><br>
This method is called from the constructor to create and layout the
GUI components.  It calls the method: <i>create_main_panel</i>, which
in turn calls methods named such as: <i>assemble_tab_files()</i>,
<i>assemble_tab_timeseries</i>, etc.  These methods all create 
GUI components and put them together in a <i>GridBagLayout</i>. They
do not worry about setting correct values in the components' fields, but just 
gets the components set up.  The main components in the GUI consists of:
<ul><li>JPanel that contains general information pertinent to the ProductGroup 
</li> <li>a panel added at the bottom that includes the standard buttons for: 
<ul><li>apply</li><li>close</li><li>cancel</li></ul></li></ul>
At the end of <i>init_layout_gui</i>, a 
call to <i>update_gui_fields</i> is made which fills in all the 
fields of the GUI according to the ProductGroup object currently being worked with.
</p>
<p><b>update_gui_fields</b><br>
This method: <ul><li>determines if the current user, already known to the DMI,
has write permissions.  If the user does not have write permissions, the 
<b>apply</b> and <b>close</b> buttons are disabled, leaving only the 
<b>cancel</b> button enabled.</li><li>Fills in all the GUI fields according
to the data in the ProductGroup object.</li></ul>
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
<li><b>creates the <i>__gui</i> versions of the objects!</b> This includes: <ul><li>__gui_RTi_ProductGroup</li></ul> The 
<i>__gui</i> versions are created in this method by copying the <i>__db</i> 
versions (which were originally created and set up in the constructor)</li>
<li>calls the <i>verify_fields</i> method.
The <i>verify_fields</i> methods<ul><li> fill in the <i>__gui</i> verions
of the object in memory with the values that are currently set in the GUI</li>
<li>compares the values in the <i>__gui</i> version of the object to the 
<i>__db</i> version of the object.  If the <i>__gui</i> version differ 
from the <i>__db</i> verion:<ul>
<li>the <i>__gui</i> version are marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the 
<i>__gui</i> differs from the <i>__db</i> version (the <i>__db</i> version 
remember, represents how the object is in the database itself) </li></ul>
</li></ul></ul>
</p>
<p><b>update_database</b><br>
This method: <ul>
<li>makes a confirmation message to verify that the user wants to save the 
changes (and lists out all the changes from the <i>__dirty_vect</i>) <b>if</b>
a new ProductGroup was <b>not</b> created (if a new ProductGroup was created, 
we do not want to re-confirm every change)</li>
<li>writes the <i>__gui</i> version of the objects to the database</li>
<li>if the objects were successfully written to the database, the 
<i>__gui</i> objects are marked as <b>not</b> dirty 
( <i>setDirty(false)</i></li>
<li><i>__dirty_vect</i> is cleared out</li>
<li>if we created a new ProductGroup object (<i>__bln_new_object == true</i>),
add a new node on the JTree in the main application  - <b>or</b> -  update the 
existing ProductGroup node on the JTree with the new changes.</li>
</ul>
</p>

<br>
<p><b>ACTIONS and the events they trigger</b><br>
The main actions in the GUI are fired off when the user selects one of the
following buttons:<ul><li>cancel</li><li>close</li><li>apply</li></ul>
<ul><li><b>cancel</b><br>
Items that are checked before the GUI is closed:<ul>
<li>if the user was changing the properties of an existing ProductGroup, 
<ul><li><i>update_RiversideDB_objects()</i> is called to <b>create</b> and 
update the <i><b>__gui_RTi_ProductGroup</b></i> object in memory, mark it 
dirty, and add messages to the <i>__dirty_vect</i> Vector.</li>
<li>Print a confirmation 
message, confirming the user wants to cancel the changes (that are stored 
in the <i>__dirty_vect</i>) </li></ul> </li> </ul> </li>
<li><b>close</b> (close Button and "X" in application window bar)<br>
The method: <i>closeGUI()</i>is called which does the following:<ul>
<li>calls <i>checkRequiredInput</i> which makes sure all the required fields in the GUI are filled out and have valid entries</li>
<li>calls <i>update_RiversideDB_objects</i> which is called to 
<b>create</b> and update the <i><b>__gui_RTi_ProductGroup</b></i> 
object in memory, mark it dirty, 
and add messages to the <i>__dirty_vect</i> Vector.
</li>
<li>creates a confirmation message if the <i>__gui</i> version are dirty, 
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
<i>__db_RTi_ProductGroup</i> object is re-created, using the copy 
constructor and passing in the <i>__gui_RTi_ProductGroup</i> object: <br>
<i>__db_RTi_ProductGroup = new RiversideDB_ProductGroup
( __gui_RTi_ProductGroup) </i> </li></ul> </li>
</ul>
</li>
</ul>
*/
public class RiversideDB_ProductGroup_JFrame
	extends    RiversideDB_EditorBase_JFrame
	implements ActionListener,
		   WindowListener
{

// Class name
private static String __class = "RiversideDB_ProductGroup_JFrame";

//variables passed in with constructor.
//RiversideDB_DMI object - already opened
private RiversideDB_DMI __dmi = null;

//Shared Layout parameters
Insets __insets = null;
GridBagLayout __gridbag = null;

//indicated what the current action is-
//ie, if a new ProductGroup node is being added.
private boolean __bln_new_object = false;

//components for close panel
private SimpleJButton __close_JButton = null;
private SimpleJButton __cancel_JButton = null;
private SimpleJButton __apply_JButton = null;

//Holds name of Product Group Type - comes from JTree...For example, if they
//click on Add New Group from Import Tree, type is "import".
private String __preselected_type_str = null;

//GUI Components
private JTextField __id_JTextField = null;
private JCheckBox __enabled_JCheckBox = null;
private JTextField __name_JTextField = null;
private SimpleJComboBox __type_JComboBox = null;
private JTextField __comment_JTextField = null;

//ProductGroup object that is currently being used...
RiversideDB_ProductGroup __db_RTi_ProductGroup= null;
RiversideDB_ProductGroup __gui_RTi_ProductGroup = null;

//vector contain ProductGroup objects for a given ProductType.
private Vector __productGroup_vect = null;

//Product types vector - holds strings representing available ProductTypes
//Used for Drop-down list of Types (which is set to disabled currently).
private Vector __productType_vect = new Vector();

//Holds a Vector of status information-- each
//field that has been changed is recored in this vector.
private Vector __dirty_vect = new Vector();

//Flag to indicate if we are running in CAUTIOUS MODE---
//aka, if we prompt the user for confirmation of changes
private boolean __cautious_mode = true;

//flag to indicate if current user has write permissions.
private boolean __canWriteProductGroup = false; 

//Holds the DBUser object
private RiversideDB_DBUser __DBUser = null;

/**
The constructor is called by an action in one of the JTrees of the 
main application.  The main application passes to this class a 
RiversideDB_ProductGroup.  This class is called in one of two cases: <br>
<ul><li>to view the properties of a current ProductGroup</li>
<li>to create a new ProductGroup</li></ul>
In the case of viewing a current ProductGroup, the ProductGroup object
passed to this class is already defined and all required properties 
already known.  In the case of creating a totally new ProductGroup, the 
ProductGroup passed in to this class is essentially an empty skeleton.  <br>
It is important to distinguish in the constructor, if we are dealing with
an existing ProductGroup and just changing some of its fields or if
we are creating a totally new ProductGroup.  If the Identifier String
for the ProductGroup passed in is MISSING, then we know that we are
creating a new ProductGroup object.  In the constructor then, set
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
At this point, whether we are creating a new ProductGroup or modifying an
existing one, we assign the ProductGroup to the variable known throughout
this class as: <b><i>__gui_RTi_ProductGroup</i></b>.<br><br>
Finally, the constructor also:<ul><li> sets up Vectors of (static) reference data, 
read directly from the database thay will be used throughout the class</li>
<li>calls method: <i>init_layout_GUI()</i> which creates and sets up the 
GUI components</li></ul><br> 
@param dmi Instance of RiversideDB_DMI that has already been opened.
@param title String for title of JFrame.
@param ip RiversideDB_ProductGroup object to display in this GUI. If a new 
ProductGroup is being created, the Identifier (String) field will be MISSING.
*/
public RiversideDB_ProductGroup_JFrame(
				RiversideDB_DMI dmi,
				RiversideDB_WindowManager windowManager,
				String title,
				RiversideDB_ProductGroup pg ) {
	super( title );
	String routine = __class + ".constructor";

	//Add RTI icon
	JGUIUtil.setIcon( this, JGUIUtil.getIconImage() );

	// Set global variables
 	__dmi = dmi;
 	__preselected_type_str = pg.getProductType();
 	__db_RTi_ProductGroup = pg;
 	
 	// Set protected member in the base RiversideDB_EditorBase_JFrame class.
 	_windowManager = windowManager;

	// Get current user
	try {
 		__DBUser = __dmi.getDBUser();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		JOptionPane.showMessageDialog(
			this,  e, "Warning1", JOptionPane.WARNING_MESSAGE );
		return;
	}

	// Get vector of all product types
	Vector pt_vect = null;
	try {
		pt_vect = __dmi.readProductTypeList();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
 		__productType_vect = null;
 		// REVIST [LT] 2005-01-07 - ShowMessageDialog added here because
 		//         if the ProductType table is not in
		//         the RiversideDB, which is the case for the test case
		//	   (Polochic) nothing happing when trying to bring up
		//	   the Import Group Properties editor.
		JOptionPane.showMessageDialog(
			this,  e, "Warning", JOptionPane.WARNING_MESSAGE );
		return;  // Nothing to do.	
	}
	// Get Type names from the ProductType objects
	int size = 0;
	if ( pt_vect != null ) {
		size = pt_vect.size();
	}
	RiversideDB_ProductType pt = null;
	for ( int i=0; i< size; i++ ) {
		pt = (RiversideDB_ProductType) pt_vect.elementAt(i);
		if ( pt == null ) {
			continue;
		}
 		__productType_vect.addElement( pt.getProductType() );
	}

	// If Identifier is MISSING, we are creating a new ProductGroup
	if ( DMIUtil.isMissing( pg.getIdentifier() ) ) {
	// If (pg.getIdentifier().equalsIgnoreCase( DMIUtil.MISSING_STRING) ) {}
		//new ProductGroup object
 		__bln_new_object = true;
 		__db_RTi_ProductGroup.setDirty( true );

		//assign user permissions for new object
 		__db_RTi_ProductGroup.
		setDBUser_num( __DBUser.getDBUser_num() );
 		__db_RTi_ProductGroup.
		setDBGroup_num( __DBUser.getPrimaryDBGroup_num());
 		__db_RTi_ProductGroup.setDBPermissions(
 		__DBUser.getDefault_DBPermissions() );
	}
	else {
 		__db_RTi_ProductGroup = pg;
	}

	// See if user has permissions to change the ProductGroup
	try {
 		__canWriteProductGroup =
 		__dmi.canWrite( __db_RTi_ProductGroup.getDBUser_num(),
 		__db_RTi_ProductGroup.getDBGroup_num(),
 		__db_RTi_ProductGroup.getDBPermissions() );
	}
	catch ( Exception e ) {
		Message.printWarning ( 2, routine, e);
 		__canWriteProductGroup = false;
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
indicating the fields with invalid values. (Prints one error message at the end indicating any fields that need to
be filled in before a save can occur.)
</ul>
@exception Exception thrown if a required filled does not
have a value.
*/
protected void checkRequiredInput() throws Exception {
	String routine = __class + ".checkRequiredInput";

	StringBuffer buffer = new StringBuffer();

	//identifer
	String id_str = null;
	id_str = __id_JTextField.getText().trim();
	if (( id_str == null ) || ( id_str.length() <= 0 ) ) {
		buffer.append( "Unable to update database without " +
		"an Identifier.\n" ); 
	}
	//make sure no other ProductGroup has this same name
	if ( __bln_new_object ) {
		int size = 0;
		if ( __productGroup_vect != null ) {
			size = __productGroup_vect.size();
		}
		RiversideDB_ProductGroup pg = null;
		for ( int i=0; i<size; i++ ) {
			pg = (RiversideDB_ProductGroup) 
 			__productGroup_vect.elementAt(i);
			if ( pg == null ) {
				continue;
			}
 
			if ( pg.getIdentifier().equalsIgnoreCase( id_str ) ) {
				buffer.append( "Already a ProductGroup in " +
				"database with Identifier: \"" + 
				id_str + "\"." + 
				"Unable to update database.\n" );
			}
		}
	}

	//Name
	String name_str = null;
	name_str = __name_JTextField.getText().trim();
	if (( name_str == null ) || ( name_str.length() <= 0 ) ) {
		buffer.append( "Unable to update database without " +
		"a Name.\n" );
	}

	//Enabled - can't be null since is JCheckBox
	//Type - can't be null since it is JComboBox

	if ( buffer.length() >0 ) {
		buffer.append("Please specify all required fields or cancel." );
		JOptionPane.showMessageDialog( this, 
		buffer.toString(), "Warning", JOptionPane.WARNING_MESSAGE);

		throw new Exception ( 
		"Please specify all required fields or cancel." );
	}
	buffer = null;

}//end checkRequieredInput

/**
This method does the following:<ul>
<li>calls <i>checkRequiredInput</i> which makes sure all the required fields in the GUI are filled out and have valid entries</li>
<li>calls <i>update_RiversideDB_objects</i> which is called to 
<b>create</b> and update the <i><b>__gui_RTi_ProductGroup</b></i>
object in memory, marks it dirty, 
and add messages to the <i>__dirty_vect</i> Vector.
</li>
<li>creates a confirmation message if the <i>__gui</i> versions are dirty, 
prompting the user to verify if they want to save their changes
(all the changes are listed out from the <i>__dirty_vect</i>)</li>
<li>updates the database by calling, <i>update_database</i></li>
<li>closes the GUI and destroys it</li>
</ul>
*/
public void closeGUI() {
	String routine = __class + ".closeGUI";

	boolean blnUpdated=true;
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
		if( __gui_RTi_ProductGroup.isDirty() ) {
			//prompt user if they want to save their
			//changes to the database.
			int x = 0;
			x = new ResponseJDialog ( this, "Save Changes",
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
				windowManagerClose();
			}
			else if ( x == ResponseJDialog.NO ) {
				windowManagerClose();
			}
		}
		else { //no changes, so just close.
			windowManagerClose();
		}
	}
	/*
	else {
		if ( blnUpdated ) {
			windowManagerClose();
		}
	}
	*/

} //end closeGUI

/**
Finalizes and cleans up.
*/
protected void finalize() throws Throwable
{

 	__insets = null;
 	__dmi = null;
 	__close_JButton = null;
 	__cancel_JButton = null;
 	__apply_JButton = null;
 	
	// Finalize the base RiversideDB_EditorBase_JFrame class
	super.finalize();
}

/**
This method is called from the constructor to create and layout the
GUI components. It creates GUI components and put them together 
in a <i>GridBagLayout</i>. It does not worry about setting correct values 
in the components' fields, but just gets the components set up.  
The main components in the GUI consists of:
<ul><li>JPanel that contains general information pertinent to the ProductGroup 
object at the top of the GUI</li>
<li>a panel added at the bottom that includes the standard buttons for: 
<ul><li>apply</li><li>close</li><li>cancel</li></ul></li></ul>
At the end of <i>init_layout_gui</i>, a 
call to <i>update_gui_fields</i> is made which fills in all the 
fields of the GUI according to the ProductGroup object currently being 
worked with.
*/
private void init_layout_GUI( ) {
	String routine = __class + ".init_layout_GUI";

	//Create overall Main Panel
	JPanel main_panel = null;
	main_panel = new JPanel();
	main_panel.setLayout( new GridBagLayout() );

	JPanel top_panel = null;
	top_panel = new JPanel();
	top_panel.setLayout( new GridBagLayout() );

	//components
	//ID JTextField and JLabel
	JLabel id_JLabel = new JLabel ( "Identifier:" );
 	__id_JTextField = new JTextField( 15 );
	//Enabled JCheckBox
	String name = null;
	if ( __preselected_type_str.equalsIgnoreCase("IMPORT") ) {
		name = "Import";
	}
	else if ( __preselected_type_str.equalsIgnoreCase("EXPORT") ) {
		name = "Export";
	}
 	__enabled_JCheckBox = new JCheckBox( name + " " +
	"Product Group is Enabled" );

	//Name JTextField and JLabel
	JLabel name_JLabel = new JLabel ( "Name:");
 	__name_JTextField = new JTextField( 15 );
	//Type JComboBox and JLabel
	JLabel type_JLabel = new JLabel ( "Type:");
 	__type_JComboBox = new SimpleJComboBox( __productType_vect );
 	JGUIUtil.setEnabled(__type_JComboBox, false );

	//Comment JTextField and JLabel
	JLabel comment_JLabel = new JLabel ( "Comment:");
 	__comment_JTextField = new JTextField( 30 );

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

	//Layout GUI

	try {
		int x =0;
		int y =0;
		
		//ID JLabel
		JGUIUtil.addComponent(
		top_panel, id_JLabel,
		x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );
		//ID JTextField
		JGUIUtil.addComponent(
		top_panel, __id_JTextField,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

		//enabled JCheckBox
		JGUIUtil.addComponent(
		top_panel, __enabled_JCheckBox,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

		x=0;
		//Name JLabel
		JGUIUtil.addComponent(
		top_panel, name_JLabel,
		x, ++y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );
		//Name JTextField
		JGUIUtil.addComponent(
		top_panel, __name_JTextField,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

		x=0;
		//comment JLabel
		JGUIUtil.addComponent(
		top_panel, comment_JLabel,
		x, ++y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );
		//comment JTextField
		JGUIUtil.addComponent(
		top_panel, __comment_JTextField,
		++x, y, 2, 1, 0.0, 0.0, __insets,
		GridBagConstraints.HORIZONTAL,
		GridBagConstraints.WEST );

		//assemble overall panel
		x=0;
		y=0;
		//top panel
		JGUIUtil.addComponent(
		main_panel, top_panel,
		x, y, 1, 1, 1.0, 0.0, __insets,
		GridBagConstraints.EAST,
		GridBagConstraints.CENTER );

		//close panel
		JGUIUtil.addComponent(
		main_panel, close_JPanel,
		x, ++y, 1, 1, 1.0, 0.0, __insets,
		GridBagConstraints.EAST,
		GridBagConstraints.CENTER );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine,
			"Error laying out the Main portion of the "+
			"graphical display for Reduction Time Series.");
		Message.printWarning( 2, routine, e );
	}

	//NOW that the specific panels have
	//been created, fill in the values for all the FIELDS!
	update_GUI_fields();

	//pack and set visible
	getContentPane().add( "Center",	main_panel );
	pack();
	JGUIUtil.center (this );
	setVisible( true );

} //end init_layout_GUI

/**
This method: <ul>
<li>makes a confirmation message to verify that the user wants to save the 
changes (and lists out all the changes from the <i>__dirty_vect</i>) <b>if</b>
a new ProductGroup was <b>not</b> created (if a new ProductGroup was created, 
we do not want to re-confirm every change)</li>
<li>writes the <i>__gui</i> version of the objects to the database</li>
<li>if the objects were successfully written to the database, the 
<i>__gui</i> objects are marked as <b>not</b> dirty 
( <i>setDirty(false)</i></li>
<li><i>__dirty_vect</i> is cleared out</li>
<li>if we created a new ProductGroup object (<i>__bln_new_object == true</i>),
add a new node on the JTree in the main application  - <b>or</b> -  update the 
existing ProductGroup node on the JTree with the new changes.</li>
</ul>
@exception Exception thrown if error encountered.
*/
protected void update_database( ) throws Exception {
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
		if ( __gui_RTi_ProductGroup.isDirty() ) {
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

	//ProductGroup is dirty
	if ( __gui_RTi_ProductGroup.isDirty() ) {
		if ( Message.isDebugOn ) {
			Message.printDebug( 2, routine,
			"Writing ProductGroup object to the database." );
		}

		//update title for JFrame
		this.setTitle( "RiverTrak® Assistant - " +
		"Import Product Group - " + __gui_RTi_ProductGroup.
		getIdentifier() );

		int pg_num = -999;
		try {
			//pg_num = 
 			__dmi.writeProductGroup( __gui_RTi_ProductGroup );
 			__gui_RTi_ProductGroup.setDirty( false );
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e );
			Message.printWarning( 2, routine,
			"Unable to write ProductGroup object " +
			"to the database.", this );

 			__gui_RTi_ProductGroup.setDirty( true );
		}

		//if ProductGroup was dirty, meant that the
		//something in the object was changed or an
		//entirely new ProductGroup was added
		//-- update JTree to reflect this change.
		if ( __bln_new_object ) {
 			addProductGroupNode( __gui_RTi_ProductGroup );
		}
		else {
 			updateProductGroupNode( __gui_RTi_ProductGroup,
 						__db_RTi_ProductGroup.
 							getIdentifier() );
		}
	}
	else {
		Message.printStatus( 2, routine,
		"No changes were made to the ProductGroup object to update." );
	}


	//empty out dirty vector
 	__dirty_vect.clear();
} //end update_database

/**
This method:<ul>
<li><b>creates the <i>__gui</i> versions of the object!</b> This includes: 
<ul><li>__gui_RTi_ProductGroup</li></ul> The 
<i>__gui</i> version is created in this method by copying the <i>__db</i> 
version (which were originally created and set up in the constructor)</li>
<li>calls the <i>verify_fields</i> method.
The <i>verify_fields</i> method:<ul><li> fill in the <i>__gui</i> verion 
of the objects in memory with the values that are currently set in the GUI</li>
<li>compares the values in the <i>__gui</i> version of the objects to the 
<i>__db</i> version of the objects.  If the <i>__gui</i> versions differ 
from the <i>__db</i> verions:<ul>
<li>the <i>__gui</i> version are marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the 
<i>__gui</i> differs from the <i>__db</i> version (the <i>__db</i> version 
remember, represents how the object is in the database itself) </li></ul>
</li></ul></ul>
@param Exception thrown if error encountered.
*/
protected void update_RiversideDB_objects() throws Exception {
	String routine = __class + ".update_RiversideDB_objects";

 	__gui_RTi_ProductGroup = new RiversideDB_ProductGroup( 
 	__db_RTi_ProductGroup );

	//get all fields
	try {
		verify_fields();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e);
		Message.printWarning( 2, routine,
		"Unable to validate fields in ProductGroup GUI." );
		throw new Exception( 
		"Unable to validate fields in ProductGroup GUI." );
	}

} //end update_RiversideDB_objects

/**
This method: <ul><li>determines if the current user, already known to the DMI,
has write permissions.  If the user does not have write permissions, the 
<b>apply</b> and <b>close</b> buttons are disabled, leaving only the 
<b>cancel</b> button enabled.</li><li>Fills in all the GUI fields according
to the data in the ProductGroup object.</li></ul>

*/
protected void update_GUI_fields( ) {
	String routine = __class + ".update_GUI_fields";

	//check to see which buttons should be enabled
	if ( ! __canWriteProductGroup ) {
 		JGUIUtil.setEnabled(__apply_JButton, false );	
 		JGUIUtil.setEnabled(__close_JButton, false );	
	}

	//id
	String db_id = null;
	db_id = __db_RTi_ProductGroup.getIdentifier();
 	__id_JTextField.setText( db_id );

	//enabled -set default to True
	String db_enabled_str = null;
	boolean db_enabled = true;
	db_enabled_str = __db_RTi_ProductGroup.getIsEnabled();
	if ( db_enabled_str.equalsIgnoreCase( "N" ) ) {
		db_enabled = false;
	}
 	__enabled_JCheckBox.setSelected( db_enabled);

	//name
	String db_name = null;
	db_name = __db_RTi_ProductGroup.getName();
 	__name_JTextField.setText( db_name );

	//type
	//if we are creating a new ProductGroup, the ProductType
	//was passed in to the constructor (based on which tree it
	//was called from). If we are editing an existing ProductGroup,
	//the type string was defined when the ProductGroup object
	//was identified.
 	__type_JComboBox.setSelectedItem( __preselected_type_str );

	//comment
	String db_comment = null;
	db_comment = __db_RTi_ProductGroup.getComment();
 	__comment_JTextField.setText( db_comment );

} //end update_GUI_fields

/**
Verifies and stores all the information in the fields of the interface.
The method <ul><li> fills in the <i>__gui</i> verions of the object
in memory with the values that are currently set in the GUI</li>
<li>compares the values in the <i>__gui</i> version of the objects to the 
<i>__db</i> version of the objects.  If the <i>__gui</i> version differ 
from the <i>__db</i> verion:<ul>
<li>the <i>__gui</i> version is marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the 
<i>__gui</i> differs from the <i>__db</i> version (the <i>__db</i> version 
remember, represents how the object is in the database itself) </li></ul>
</li></ul></ul>
*/
public void verify_fields() throws Exception {
	String routine = __class + ".verify_fields";
	
	//ID
	String gui_id = null;
	String db_id = null;
	gui_id = __id_JTextField.getText().trim();
	db_id = __db_RTi_ProductGroup.getIdentifier();
	
	if ( ! db_id.equalsIgnoreCase( gui_id ) ) {
 		__gui_RTi_ProductGroup.setDirty( true );
 		__dirty_vect.addElement(
		"Change Identifier from \"" + db_id + 
		"\" to \"" + gui_id + "\"");

 		__gui_RTi_ProductGroup.setIdentifier( gui_id );
	}

	//Enabled
	boolean bln_gui_enabled = false;
	String gui_enabled_str = "N";
	String db_enabled_str = null;
	bln_gui_enabled = __enabled_JCheckBox.isSelected();
	if ( bln_gui_enabled ) {
		gui_enabled_str = "Y";
	}
	db_enabled_str = __db_RTi_ProductGroup.getIsEnabled();

	if ( ! db_enabled_str.equalsIgnoreCase( gui_enabled_str ) ) {
 		__gui_RTi_ProductGroup.setDirty( true );
 		__dirty_vect.addElement(
		"Change IsEnabled (Y/N) from \"" + db_enabled_str + 
		"\" to \"" + gui_enabled_str + "\"");

 		__gui_RTi_ProductGroup.setIsEnabled( gui_enabled_str );
	}

	//Name
	String gui_name = null;
	String db_name = null;
	gui_name = __name_JTextField.getText().trim();
	db_name = __db_RTi_ProductGroup.getName();
	
	if ( ! db_name.equalsIgnoreCase( gui_name ) ) {
 		__gui_RTi_ProductGroup.setDirty( true );
 		__dirty_vect.addElement(
		"Change Name from \"" + db_name + 
		"\" to \"" + gui_name + "\"");

 		__gui_RTi_ProductGroup.setName( gui_name );
	}

	//Comment
	String gui_comment = null;
	String db_comment = null;
	gui_comment = __comment_JTextField.getText().trim();
	db_comment = __db_RTi_ProductGroup.getComment();
	
	if ( ! db_comment.equalsIgnoreCase( gui_comment ) ) {
 		__gui_RTi_ProductGroup.setDirty( true );
 		__dirty_vect.addElement(
		"Change Comment from \"" + db_comment + 
		"\" to \"" + gui_comment + "\"");

 		__gui_RTi_ProductGroup.setComment( gui_comment );
	}

} //end verify_fields


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
				//update the __gui_ versions to be the 
				//the memory version again since update failed.
 				__gui_RTi_ProductGroup = null;
 				__gui_RTi_ProductGroup = new 
				RiversideDB_ProductGroup( __db_RTi_ProductGroup);

			}
		}

		if ( blnUpdated ) {
			//if we got this far, the database was updated,
			//so update objects in memory.  The __gui_ object 
			//was written to the database, so now the 
			//_db_ object should equal the __gui_ object
 			__db_RTi_ProductGroup = null;
 			__db_RTi_ProductGroup = new RiversideDB_ProductGroup( 
 			__gui_RTi_ProductGroup );
 			__db_RTi_ProductGroup.setDirty( false );
	
 			__gui_RTi_ProductGroup = null;
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

			}

			if ( ( __gui_RTi_ProductGroup.isDirty()) ) {
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
				"Are you sure you want to " +
				"Cancel the following changes?\n" +
				b.toString()+"?", 
				ResponseJDialog.YES | ResponseJDialog.NO ).
				response();
	
				if ( x == ResponseJDialog.YES ) {
					//write to log file
					Message.printStatus( 5, 
					routine, 
					"User canceled changes: " +
					b.toString() ); 

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
				windowManagerClose();
			}
		}
		else {
			//just do a normal cancel- close gui
			windowManagerClose();
		}

	}
	else if ( source.equals( __close_JButton ) ) {
		closeGUI();
	}
	

	source = null;
	} // End of try
	catch ( Exception e ) {
		if ( Message.isDebugOn ) {
			Message.printWarning ( 2, routine, e );
		}
	}
} //end actionPerformed

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
	if ( __canWriteProductGroup ) {
	 	closeGUI();
	}
	else {
		//just close as a cancel
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
	if (__bln_new_object) {
		_windowManager.closeWindowInstance(
			_windowManager.WINDOW_PRODUCTGROUP,
			"CREATING NEW OBJECT");	
	}
	else {
		_windowManager.closeWindowInstance(
			_windowManager.WINDOW_PRODUCTGROUP ,
			new Long(__db_RTi_ProductGroup.getProductGroup_num()) );
	}
}

/////////////////////////////////////////////////////////////////
/////////////////////// END ACTIONS /////////////////////////////
/////////////////////////////////////////////////////////////////

}// end RiversideDB_ProductGroup_JFrame class

