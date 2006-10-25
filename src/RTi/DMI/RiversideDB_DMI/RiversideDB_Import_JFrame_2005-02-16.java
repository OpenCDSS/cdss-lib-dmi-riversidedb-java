// RiversideDB_Import_JFrame - 
//	Creates the GUI with information for RiversideDB_ImportProduct 
//	and RiversideDB_ImportConf objects for the RiverTrakAssistant.  
//	This class is called by	the RTAssistant_Main_JFrame.
//	
//	Only ImportProducts of type CampbellScientific are currently
//	supported (2003_06_30) (If !IOUtil.Testing() only add Campbell Sci)
//--------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
//--------------------------------------------------------------------------
// History:
//
// 2002-23-05	Morgan Sheedy, RTi	Initial Implementation
//
// 2002-30-05	AMS			Instead of passing the RiversideDB_DMI
//					instance around from method to 
//					method, used the default "copy 
//					constructor" and just made a global
//					instance.
// 2002-06-27	SAM, RTi		Change setDirty() calls to setDirty().
//
// 2002-08-07	AMS			Commented out code that provides
//					a list of all products in 
//					import Group and different Group
///					names since it is just a repeat of
//					what is presented in JTree and since
//					we are accessing this Only through the
//					Jtree and no longer via the a menu
//					on the MenuBar.
//
//
// 2002-11-06	AMS, RTi		Changed class name from:
//					RiversideDBAdministratorImportJFrame 
//					to: RTiDBAdmin_Import_JFrame
//
// 2002-15-08	AMS,RTi			Pass in an instance of the 
//					RTiDBAdmin_Main_JFrame so that
//					this class can call the:
//					addImportProductNode( xxx ) method
//					in the Main class that will update
//					the Import JTree by adding a new node.
//
//					If a New ImportProduct is being created,
//					added a new call to update_database
//					in the constructor so that a 
//					generic new Import Product is
//					added to the database upfront so 
//					that we can get the ImportProduct_num
//					(an autonum) for the new product
//					right away.  Without the ImportProd_num
//					ImportConf objects can not be 
//					tied to the ImportProducts.
//					
// 20 Aug, 2002		AMS		Added JCheckBox to the ImportConf
//					(timeseries) tab in accordance to
//					change to Database.
//
// 2002-26-09	Morgan Love, RTi	Changed Application name from:
//					RiversideDBAdmin to:
//					RiverTrakAssistant and Class from:
//					RTiDBAdmin_Import_JFrame to:
//					RTAssistant_Import_JFrame.
//
// 2003-04-10	AML, RTi		Updated from DMI to DMIUtil methods
//					for MISSING.
//
//					Weekday_JComboBox on automation
//					tab is disabled because we are not
//					utilizing it at this point. Uncomment
//					out blocks of code to enable it.
//					see: __automation_tab_weekday_JComboBox.
//					setEnabled( true/false );
//
// 2003-05-19	AML, RTi		If "Cancel" after creating a NEW
//					ImportProduct, the new ImportProduct
//					is deleted from the database.
//
// 2003-06-03	AML, RTi		Update to use new TS classes.
//
// 2003-06-08	AML, RTi		Security measures using fields:
//					DBUser, DBGroup, DBPermissions 
//					implemented.
//
// 2003-06-17	AML, RTi		Added MeasLocGroup_num.
//
// 2003-06-30	AML, RTi		code clean up.
//
// 2003-10-20	AML, RTi		Added SHEF to import Types that
//					are visible in non-testing mode.
//
// 2004-01-14	AML, RTi		Pass around RiversideDB_ImportConf
//					and RiversideDB_ImportProduct 
//					objects to/from main class instead
//					of simple String representations 
//					of them.
//					
//					Updated Time Series tab in GUI
//					to display selected Time Series 
//					(RiversideDB_ImportConf objects)
//					in a JWorksheet.
//					
// 2004-02-05	J. Thomas Sapienza, RTi	Added window manager code.
//
// 2004-07-13	AML, RTi		For SHEF data types, it is noted
//					that on  older systems, the SHEF 
//					string is "SHEF_A" and
//					on newer systems, the SHEF string 
//					is "SHEF.A".  So if doing a String
//					comparison to determine SHEF import
//					products, just use: "SHEF" to
//					match part of the String.
//
// 2004_07_14	AML, RTi		*.setEnabled() and setBackgroundColor()
//					replaced by JGUIUtil.setEnabled().
//					*general code cleaning
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
// 2005-01-07 Luiz Teixeira, RTi 	Replaced the JWorksheet property names
//					by the new ones.
// 2005-02-01 Luiz Teixeira, RTi 	Security and Archive tabs now always
//					available. Old restriction commented
//					out    
// 2005-02-03 Luiz Teixeira, RTi	Changed from dItemListener (this) to 
//					AddActionListener (this) for the 
//					following JComboBox(s)
//					__archive_tab_wild_JComboBox 
//					__files_tab_source_wild_JComboBox 
//					__files_tab_dest_wild_JComboBox
// 					Because what we want from this control
//					is to use the item clicked, even if it
//					is alread selected.
//--------------------------------------------------------------------------
package RTi.DMI.RiversideDB_DMI;

import  java.awt.Color;
import  java.awt.Component;
import  java.awt.Dimension;
import  java.awt.Font;
import  java.awt.GridBagConstraints;
import  java.awt.GridBagLayout;
import  java.awt.GridLayout;
import  java.awt.Image;
import  java.awt.Insets;
import  java.awt.event.ActionEvent;
import  java.awt.event.ActionListener;
import  java.awt.event.ItemEvent;
import  java.awt.event.ItemListener;
import  java.awt.event.WindowEvent;
import  java.awt.event.WindowListener;
import  java.io.File;
import  java.util.Date;
import  java.util.Vector;
import 	javax.swing.BorderFactory;
import 	javax.swing.border.Border;
import 	javax.swing.border.TitledBorder;
import 	javax.swing.ButtonGroup;
import 	javax.swing.DefaultListModel;
import 	javax.swing.event.ListSelectionEvent;
import 	javax.swing.event.ListSelectionListener;
import 	javax.swing.ImageIcon;
import 	javax.swing.JCheckBox;
import 	javax.swing.JComboBox;
import 	javax.swing.JFileChooser;
import 	javax.swing.JFrame;
import 	javax.swing.JLabel;
import 	javax.swing.JList;
import 	javax.swing.JOptionPane;
import 	javax.swing.JPanel;
import 	javax.swing.JRadioButton;
import 	javax.swing.JScrollPane;
import 	javax.swing.JTabbedPane;
import 	javax.swing.JTextField;
import 	javax.swing.ListModel;
import 	javax.swing.ListSelectionModel;
import 	javax.swing.UIManager;

import 	RTi.Util.GUI.JFileChooserFactory;
import 	RTi.GIS.GeoView.GeographicProjection;
import 	RTi.GIS.GeoView.GeoLayer;
import 	RTi.GIS.GeoView.GeoProjection;
import 	RTi.GR.GRArc;
import 	RTi.GR.GRLegend;
import 	RTi.GR.GRColor;
import 	RTi.GR.GRPoint;
import 	RTi.GR.GRSymbol;

import  RTi.TS.TS;
import  RTi.TS.TSIdent;

import  RTi.Util.GUI.JGUIUtil;
import  RTi.Util.GUI.JWorksheet;
import  RTi.Util.GUI.ResponseJDialog;
import  RTi.Util.GUI.ResponseJDialog;
import  RTi.Util.GUI.SimpleJButton;
import  RTi.Util.GUI.SimpleJComboBox;
import	RTi.Util.IO.DataUnits;
import	RTi.Util.IO.DataUnitsConversion;
import  RTi.Util.IO.IOUtil;
import  RTi.Util.IO.PropList;
import	RTi.Util.Message.Message;
import  RTi.Util.String.StringUtil;
import  RTi.Util.Time.DateTime;
import  RTi.Util.Time.TimeInterval;
import  RTi.Util.Time.TimeUtil;

import 	RTi.DMI.DMI;
import 	RTi.DMI.DMIUtil;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_DMI;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_DataDimension;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_DataType;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_DataUnits;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_DBUser;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_DBUserMeasLocGroupRelation;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_ImportConf;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_ImportConf_CellRenderer;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_ImportConf_TableModel;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_ImportProduct;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_ImportType;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_MeasLocGroup;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_ProductGroup;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_MeasType;
import 	RTi.DMI.RiversideDB_DMI.RiversideDB_SHEFType;

/**
<html><body>
RiversideDB_Import_JFrame.
This class is laid out similarly to the other RiverTrakAssistant classes.
The general format of the class is laid out below, with the major
methods listed with their key functions.  The main object types from 
RiversideDB that are manipulated by this class are:<br><ul>
<li>RiversideDB_ImportProduct (abbreviated ImportProduct)</li>
<li>RiversideDB_ImportConf (abbreviated ImportConf)</li></ul><br>
<p><b>Constructor</b><br>
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
</p>
<p><b>init_layout_gui</b><br>
This method is called from the constructor to create and layout the
GUI components.  It calls the method: <i>create_main_panel</i>, which
in turn calls methods named such as: <i>assemble_tab_files()</i>,
<i>assemble_tab_timeseries</i>, etc.  These methods all create 
GUI components and put them together in a <i>GridBagLayout</i>. They
do not worry about setting correct values in the components' fields, but just 
gets the components set up.  The main components in the GUI consists of:
<ul><li>JPanel that contains general information pertinent to the ImportProduct 
object at the top of the GUI</li>
<li>a series of tabs in a JTabbedPane with fields for the ImportProduct and a
tab (the "timeseries" tab) for the related ImportConf objects, seperated by 
general topic. Tab topics include: <ul><li>automation </li> <li>file </li> 
<li>security </li><li> timeseries</li><li>properties</li><li>archive</li>
</ul> </li>
<li>a panel added at the bottom that includes the standard buttons for: 
<ul><li>apply</li><li>close</li><li>cancel</li></ul></li></ul>
At the end of <i>init_layout_gui</i>, a 
call to <i>update_gui_fields</i> is made which fills in all the 
fields of the GUI according to the ImportProduct and ImportConf objects
currently being worked with.
</p>
<p><b>update_gui_fields</b><br>
This method: <ul><li>determines if the current user, already known to the DMI,
has write permissions.  If the user does not have write permissions, the 
<b>apply</b> and <b>close</b> buttons are disabled, leaving only the 
<b>cancel</b> button enabled.</li><li>Fills in all the GUI fields according
to the data in the ImportProduct and related ImportConf objects.</li></ul>
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
<li><b>creates the <i>__gui</i> versions of the objects!</b> This includes: <ul><li>__gui_RTi_ImportProduct</li><li>__gui_RTi_ImportConf_vect</li></ul> The 
<i>__gui</i> versions are created in this method by copying the <i>__db</i> 
versions (which were originally created and set up in the constructor)</li>
<li>calls the <i>verify_xxx</i> methods. These include:<ul>
<li><i>verify_top_fields()</i></li>
<li><i>verify_archive_tab()</i></li>
<li><i>verify_automation_tab()</i></li>
<li><i>verify_files_tab()</i></li>
<li><i>verify_properties_tab()</i></li>
<li><i>verify_timeseries_tab()</i></li> </ul>
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
a new ImportProduct was <b>not</b> created (if a new ImportProduct was created, 
we do not want to re-confirm every change)</li>
<li>writes the <i>__gui</i> version of the objects to the database</li>
<li>if the objects were successfully written to the database, the 
<i>__gui</i> objects are marked as <b>not</b> dirty 
( <i>setDirty(false)</i></li>
<li><i>__dirty_vect</i> is cleared out</li>
<li>if we created a new ImportProduct object (<i>__bln_new_object == true</i>),
add a new node on the JTree in the main application  - <b>or</b> -  update the 
existing ImportProduct node on the JTree with the new changes.</li>
</ul>
</p>

<br>
<p><b>ACTIONS and the events they trigger</b><br>
The main actions in the GUI are fired off when the user selects one of the
following buttons:<ul><li>cancel</li><li>close</li><li>apply</li></ul>
<ul><li><b>cancel</b><br>
Items that are checked before the GUI is closed:<ul>
<li>if the user was creating a new ImportProduct ( <i>__bln_new_object == true </i> ), the ImportProduct is deleted from the database</li>
<li>if the user was changing the properties of an existing ImportProduct, 

<ul><li><i>update_RiversideDB_objects()</i> is called to <b>create</b> and 
update the <i><b>__gui_RTi_ImportProudct</b></i> and <i>
<b>__gui_RTi_ImportConf_vect</b></i> objects in memory, mark them dirty, and 
add messages to the <i>__dirty_vect</i> Vector.</li><li>Print a confirmation 
message, confirming the user wants to cancel the changes (that are stored 
in the <i>__dirty_vect</i>) </li></ul> </li> </ul> </li>
<li><b>close</b> (close Button and "X" in application window bar)<br>
The method: <i>closeGUI()</i>is called which does the following:<ul>
<li>calls <i>checkRequiredInput</i> which makes sure all the required fields in the GUI are filled out and have valid entries</li>
<li>calls <i>update_RiversideDB_objects</i> which is called to 
<b>create</b> and update the <i><b>__gui_RTi_ImportProudct</b></i> and 
<i> <b>__gui_RTi_ImportConf_vect</b></i> objects in memory, mark them dirty, 
and add messages to the <i>__dirty_vect</i> Vector.
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
<i>__db_RTi_ImportProudct</i> object is re-created, using the copy 
constructor and passing in the <i>__gui_RTi_ImportProduct</i> object: <br>
<i>__db_RTi_ImportProduct = new RiversideDB_ImportProduct
( __gui_RTi_ImportProduct) </i> </li><li>the <i>__db_RTi_ImportConf_vect</i> 
is cleared out and then re-filled with the ImportConf objects from the
<i>__gui_RTi_ImportConf_vect</i></li></ul> </li>
</ul>
</li>
</ul>
</body></html>
*/
public class RiversideDB_Import_JFrame
	extends    RiversideDB_EditorBase_JFrame
	implements ActionListener,
		   ItemListener,
		   WindowListener
{

// Class name
private static String __class = "RiversideDB_Import_JFrame";

//RiversideDB_DMI object - already opened
private RiversideDB_DMI __dmi = null;

//layout components
private  GridBagLayout __gridbag = null;
private  Insets __insets = null;

//indicated what the action on the currently selected 
//ImportProduct is- ie, if it is being added or just altered.
private  boolean __bln_new_object = false;

//components
private  JPanel __main_JPanel = null;
private  JPanel __top_group_JPanel = null;
private  JPanel __product_info_JPanel = null;
private  JPanel __close_JPanel = null;

private  SimpleJButton __close_JButton = null;
private  SimpleJButton __cancel_JButton = null;
private  SimpleJButton __apply_JButton = null;

private  DefaultListModel __products_avail_listModel = null;
private  SimpleJComboBox __group_name_JComboBox = null;

private JTextField __product_info_id_JTextField = null;
private  SimpleJComboBox __product_info_group_JComboBox = null;
private  SimpleJComboBox __product_info_type_JComboBox = null;
private  JCheckBox __product_info_active_JCheckBox = null;
private SimpleJComboBox __product_info_measlocgroup_JComboBox = null;

//Tabbed pane itself
private  JTabbedPane __info_JTabbedPane = null;

//FILE TAB
//private String for tab - has to be short in length
private String __files_tab_str = "Files";
private String __files_tab_db_str = "Database";

private  JTextField __files_tab_source_dir_JTextField = null;
private  SimpleJButton __files_tab_source_browse_JButton = null;
private  JTextField __files_tab_source_file_JTextField = null;
private  SimpleJComboBox __files_tab_source_wild_JComboBox = null;

private  JTextField __files_tab_dest_dir_JTextField = null;
private  SimpleJButton __files_tab_dest_browse_JButton = null;
private  JTextField __files_tab_dest_file_JTextField = null;
private  SimpleJComboBox __files_tab_dest_wild_JComboBox = null;

//PROPERTY TAB
private String __props_tab_str = "Properties";

private  SimpleJComboBox __props_tab_import_order_JComboBox = null;
private  SimpleJComboBox __props_tab_import_window_unit_JComboBox = null;
private  SimpleJComboBox __props_tab_import_window_int_JComboBox = null;
private  JTextField __props_tab_next_import_date_JTextField = null;
private  JTextField __props_tab_last_import_date_JTextField = null;
private  JTextField __props_tab_retries_JTextField = null;
private  JTextField __props_tab_generic_props_JTextField = null;

//Make Properties ComboBoxes- currently we only have
//properties for CampbellSci
private String __props_1_campbellsci_JLabel_str = "JULIANDAY";
private String __props_1_campbellsci_desc_JLabel_str = "File column number for Julian day";
private String __props_2_campbellsci_JLabel_str = "MISSING_VALUE";
private String __props_2_campbellsci_desc_JLabel_str = "Missing value indicator (should agree with CSI datalogger)";
private String __props_3_campbellsci_JLabel_str = "TABLEID";
private String __props_3_campbellsci_desc_JLabel_str = "File column number for Table ID";
private String __props_4_campbellsci_JLabel_str = "TIME";
private String __props_4_campbellsci_desc_JLabel_str = "File column number for time";
private String __props_5_campbellsci_JLabel_str = "TIMEZONE";
private String __props_5_campbellsci_desc_JLabel_str = "File column number for timezone";
private String __props_6_campbellsci_JLabel_str = "YEAR";
private String __props_6_campbellsci_desc_JLabel_str = "File column number for year";

private  SimpleJComboBox __props_1_campbellsci_JComboBox = null;
private  JTextField __props_2_campbellsci_JTextField = null;
private  SimpleJComboBox __props_3_campbellsci_JComboBox = null;
private  SimpleJComboBox __props_4_campbellsci_JComboBox = null;
private  SimpleJComboBox __props_5_campbellsci_JComboBox = null;
private  SimpleJComboBox __props_6_campbellsci_JComboBox = null;

//Automation TAB
private String __automation_tab_str = "Automation";
private  JCheckBox __automation_tab_automated_JCheckBox = null;
private  JRadioButton __automation_tab_every_interval_JRadioButton = null;
private  JRadioButton __automation_tab_at_time_JRadioButton = null;
private  SimpleJComboBox __automation_tab_year_JComboBox = null;
private  SimpleJComboBox __automation_tab_month_JComboBox = null;
private  SimpleJComboBox __automation_tab_day_JComboBox = null;
private  SimpleJComboBox __automation_tab_hour_JComboBox = null;
private  SimpleJComboBox __automation_tab_minute_JComboBox = null;
private  SimpleJComboBox __automation_tab_second_JComboBox = null;
private  SimpleJComboBox __automation_tab_weekday_JComboBox = null;
private  SimpleJComboBox __automation_tab_delay_int_JComboBox = null;
private  SimpleJComboBox __automation_tab_delay_unit_JComboBox = null;

//Archive tab
//used for tab label itself
private String __archive_tab_str = "Archive";
private  JCheckBox __archive_tab_perm_JCheckBox = null;
private  JTextField __archive_tab_dir_JTextField = null;
private  JTextField __archive_tab_file_JTextField = null;
private  SimpleJButton __archive_tab_browse_JButton = null;
private  SimpleJComboBox __archive_tab_wild_JComboBox = null;

//Security tab
//label for tab
private String __security_tab_str = "Security";

private  JTextField __security_tab_user_login_JTextField = null;
private  JTextField __security_tab_user_passwd_JTextField = null;
private  JTextField __security_tab_firewall_login_JTextField = null;
private  JTextField __security_tab_firewall_passwd_JTextField = null;

//Additional Files Tab
//private String __additFiles_tab_str = "Additional Files";
//private String __additFiles_tab_dir_str = "Directory";
//private String __additFiles_tab_browse_JButton_str = "Browse";
//private String __additFiles_tab_browse_title_str = 
//"Select Directory for Additional Files";
//private String __additFiles_tab_file_str = "File";
//JTextField __additFiles_tab_dir_JTextField = null;
//JTextField __additFiles_tab_file_JTextField = null;
//SimpleJButton __additFiles_tab_browse_JButton = null;

//Time Series Tab - created with ImportConf object.
private String __timeseries_tab_str = "Time Series";

private String __timeseries_tab_external_table_JLabel_str = "External \nTable";
private String __timeseries_tab_external_table_JLabel_camp_str = "Table \nIdentifier";
private String __timeseries_tab_external_field_JLabel_camp_str = "Data \nColumn";

private String __timeseries_tab_external_id_JLabel_camp_str = "Data \nFile";
//Units
private String __timeseries_tab_external_units_JLabel_str = "External\nUnits";
private String __timeseries_tab_active_JCheckBox_str = "Import \nEnabled";

private  JList __timeseries_tab_allTS_JList = null;
private  DefaultListModel __timeseries_tab_allTS_listModel = null;
private JWorksheet __timeseries_tab_selTS_JWorksheet = null;
private RiversideDB_ImportConf_TableModel __table_model = null;
private PropList __worksheet_PropList = null;
private Dimension __worksheet_dim = new Dimension( 675, 250 );
private String [] __arrWorksheet_labels = null;
private String [] __arrWorksheet_tooltips = null;

private  SimpleJButton __timeseries_tab_clear_selected_JButton = null;
private  SimpleJButton __timeseries_tab_move_right_JButton = null;

//New Import Product Tab
private String __new_importProd_tab_str = "New Import";
private  JTextField __new_importProd_tab_name_JTextField = null;

//New Group Tab
private String __new_group_tab_str = "New Group";
private  JTextField __new_group_tab_name_JTextField = null;

//Holds RIVERSIDEDB OBJECTS and private Vectors of the objects
//Holds all the ImportProduct Objects
private Vector __RTi_ImportProduct_vect = null;

//Holds the name of the Current ImportProduct Object being worked with!
private RiversideDB_ImportProduct __db_RTi_ImportProduct = null;

//HOlds the ImportProduct_num of the CURRENT importProduct
private long __db_ImportProduct_num = -999;

//Holds all the ImportConf objects
private Vector __db_RTi_ImportConf_vect = null;
private Vector __worksheet_RTi_ImportConf_vect = null;
private RiversideDB_ImportConf __db_RTi_ImportConf = null;

//Holds all the MeasTypes
private Vector __all_MeasType_vect = null;

//Holds all the MeasTypes WITH CREATE METHOD Import or Unknown
private Vector __leftList_MeasType_vect = new Vector();

//private Vector of ImportTypes 
private Vector __RTi_ImportType_vect = null;

//holds text name of node being operated on 
private String __db_tree_node_str = null;

//private Strings used to identify import product types
private String __type_campbellsci_str= "CAMPBELLSCI";
private String __type_tslookup_str= "TSLOOKUP";
private String __type_diadvisor_str= "DIADVISOR";
private String __type_shef_str= "SHEF_A";
//Disactivated private String __type_stormwatch_str= "STORMWATCH";
private String __type_nexrad_str= "NEXRAD";
private  JPanel __files_tab_JPanel = null;
private  JPanel __timeseries_tab_JPanel = null;
private  JPanel __properties_tab_JPanel = null;
private  JPanel __security_tab_JPanel = null;
private  JPanel __automation_tab_JPanel = null;
private  JPanel __archiving_tab_JPanel = null;

//DataType
private Vector __RTi_DataType_vect = null;

//Measlocgroup vector
private Vector __RTi_MeasLocGroup_vect = null;

//Holds objects with current, but uncommitted changes 
RiversideDB_ImportProduct __gui_RTi_ImportProduct = null;
private Vector __gui_RTi_ImportConf_vect  = null;

//Holds a private Vector of status information-- each
//field that has been changed is recored in this private Vector.
private Vector __dirty_vect = new Vector();

//Flag to indicate if we are running in CAUTIOUS MODE---
//aka, if we prompt the user for confirmation of changes
private boolean __cautious_mode = true;

//Holds the import type used for the object in the GUI
private String __db_ImportProduct_type = null;

//flag to indicate if current user has write permissions.
private boolean __canWriteImportProduct = false; 

//Holds the DBUser object
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
@param windowManager the window manager to use.
@param title String for title of JFrame.
@param ip RiversideDB_ImportProduct object to display in
this GUI. If a new ImportProduct is being created, the 
ImportProduct_num field will be MISSING.
*/
public RiversideDB_Import_JFrame(
				RiversideDB_DMI dmi,
				RiversideDB_WindowManager windowManager,
				String title,
				RiversideDB_ImportProduct ip )
{		
	super( title );
	
	String routine = __class + ".constructor";

	//add RTi icon
	JGUIUtil.setIcon( this, JGUIUtil.getIconImage() );

	// Make global
 	__dmi = dmi;
 	__db_RTi_ImportProduct = ip;
 	
 	// Set protected member in the base RiversideDB_EditorBase_JFrame class.
 	_windowManager = windowManager;

	//get current user
	try {
 	__DBUser = __dmi.getDBUser();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}
	
	//_RTi_importProduct_vect
	//get vector of all ImportProduct objects...
	try {	
 		__RTi_ImportProduct_vect = __dmi.readImportProductList();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		//create an empty vector
 		__RTi_ImportProduct_vect = new Vector();
	}

	//_RTi_importType_vect
	try {
 		__RTi_ImportType_vect = __dmi.readImportTypeList();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		Message.printWarning( 2, routine, 
		"Unable to get a Vector of ImportType objects" );

 		__RTi_ImportType_vect = new Vector();
	}

	//if we are creating a totally new ImportProduct, the IP_num will
	//be missing.
 	__db_ImportProduct_num = __db_RTi_ImportProduct.getImportProduct_num();
	//if( __db_ImportProduct_num != DMIUtil.MISSING_LONG )  {}
	if( ! DMIUtil.isMissing(__db_ImportProduct_num ) ) {
		//get ImportType
 		__db_ImportProduct_type =
 		__db_RTi_ImportProduct.getProduct_type();
		
		//get list of ImportConf objects for this -save
		//in both the GUI and the DB vectors.
		try {
 			__db_RTi_ImportConf_vect = __dmi.
			readImportConfListForImportProduct_numByLocation( 
			(int) __db_ImportProduct_num );

 			__worksheet_RTi_ImportConf_vect = __dmi.
			readImportConfListForImportProduct_numByLocation( 
			(int) __db_ImportProduct_num );

 			__gui_RTi_ImportConf_vect = __dmi.
			readImportConfListForImportProduct_numByLocation( 
			(int) __db_ImportProduct_num );
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e);
			Message.printWarning( 2, routine,
			"Unable to get list of ImportConf Objects");
		}
	}
	else { //we are creating a new ImportProduct
		// we are creating a NEW ImportProduct Object and
		//Create a JOptionPane with a list of choices for import
		//product TYPE... Once we have the Type, should be 
		//able to assemble the GUI

		//Mark Flag to indicate we are Adding a new node.
 		__bln_new_object = true;
	
		//ComboBox for Types ( ImportProduct.Product_type )
		Object options[]=null;
		if (!IOUtil.testing() ) {
			//add campbell sci
			Vector types_vect = new Vector();
			types_vect.addElement("CAMPBELLSCI " +
			"- Campbell Scientific Datalogger Comma De...");

			//add shef
			types_vect.addElement("SHEF_A " +
			"- Standard Hydrologic Exchange Format...");

			options= types_vect.toArray();
			types_vect = null;
		}
		else {
			int size= 0;
			size = __RTi_ImportType_vect.size();
			RiversideDB_ImportType it = null;
			Vector types_vect = new Vector();
			String type = null;
			String desc = null;
			for ( int i=0; i<size;i++ ) { 
				it = ( RiversideDB_ImportType ) 
 				__RTi_ImportType_vect.elementAt(i);
				if ( it ==null ) {
					continue;
				}
				type = it.getName();
				desc = it.getComment();
				if ( (type + desc).length() > 50 ) {
					desc = desc.substring( 
					0, 50-(type.length())) + "...";
				}
				types_vect.addElement( type + " - " + desc );
			}
	
			options= types_vect.toArray();
			types_vect = null;
		}

		Object selectedValue = JOptionPane.showInputDialog(
		this, "Choose the Import Product Type for new Import Product",
		"Import Product Type", JOptionPane.INFORMATION_MESSAGE,
		null, options, options[0] );

		Message.printStatus( 50,routine,
		"selected value = "+selectedValue );
		if( (selectedValue == null ) || 
		((selectedValue.toString()).equalsIgnoreCase("null")) ) {
			//cancel was pressed...
			windowManagerClose();
		}

		//else -get value and continue to make gui
		String new_type = ( String )selectedValue;
		//remove the description
		int index = -999;
		index = new_type.indexOf("-");
		if ( index > 0 ) {
 			__db_ImportProduct_type = new_type.
			substring(0,index).trim();	
		}

		//remove comment field.
		int ind = -999;
		ind = __db_ImportProduct_type.indexOf( " -");
		if ( ind > 0 ) {
 			__db_ImportProduct_type = __db_ImportProduct_type.
			substring( 0, ind ).trim();
		}

		if ( Message.isDebugOn ) {	
			Message.printDebug( 3, routine,
			"_db_ImportProduct_type selected: " +
			__db_ImportProduct_type );
		}

		//set new object to dirty so it will definitely be
		//written out to the database.
 		__db_RTi_ImportProduct.setDirty( true );

		//Make sure to set the NAME in the Object
 		__db_RTi_ImportProduct.setProduct_name ("");

		//fill in object with default choices and leave
		//product dirty so that we can write this object to
		//the database as is and get its ImportProduct number
 		__db_RTi_ImportProduct.setProduct_type( __db_ImportProduct_type);
 		__db_RTi_ImportProduct.setProduct_group( "GENERAL" );
 		__db_RTi_ImportProduct.setIsActive( "N" );
 		__db_RTi_ImportProduct.setIsInterval( "N" );
 		__db_RTi_ImportProduct.setIsAutomated( "N" );
 		__db_RTi_ImportProduct.setImport_order( 2 );
		Date today = new Date();
 		__db_RTi_ImportProduct.setLast_import_date( today );
 		__db_RTi_ImportProduct.setNext_import_date( today );

		//set User permissions since this is a new object.
 		__db_RTi_ImportProduct.setDBUser_num( 
		__DBUser.getDBUser_num() );
 		__db_RTi_ImportProduct.setDBGroup_num(
		__DBUser.getPrimaryDBGroup_num());
 		__db_RTi_ImportProduct.setDBPermissions( 
		__DBUser.getDefault_DBPermissions() );

		//make new (empty) vector of ImportConf objects
 		__db_RTi_ImportConf_vect = new Vector();
 		__worksheet_RTi_ImportConf_vect = new Vector();
 		__gui_RTi_ImportConf_vect = new Vector();

		//In order to be able to create new ImportConf objects,
		//we need an ImportProduct number, a MeasType number, 
		//and a MeasLocGroup_num
		//The MeasType num will come from the TimeSeries when
		//they are selected on the TimeSeries tab.
		//Since we have created a new ImportProduct, we need
		//to write it to the database before we can get its (autonum)
		//ImportProd_num
		//need to assign a (at least temporary) MeasLocGroup_num
		//to the ImportProduct, so do now, using the first 
		//entry
		if ( __RTi_MeasLocGroup_vect == null ) {
			try {	
				__RTi_MeasLocGroup_vect = 
				__dmi.readMeasLocGroupList();
			}
			catch ( Exception e ) {
				Message.printWarning( 2, routine, e );
				//create an empty vector
				__RTi_MeasLocGroup_vect = new Vector();
			}
		}
		int num = 0;
		if ( __RTi_MeasLocGroup_vect != null ) {
			num = __RTi_MeasLocGroup_vect.size();
		}
		int mlg_num = -999;
		RiversideDB_MeasLocGroup mlg = null;
		if ( num > 0 ) {
			mlg = 	(RiversideDB_MeasLocGroup)
			__RTi_MeasLocGroup_vect.elementAt(0);
			__db_RTi_ImportProduct.setMeasLocGroup_num( 
			mlg.getMeasLocGroup_num() );
		}
		mlg = null;

		if ( Message.isDebugOn ) {
			Message.printDebug( 3, routine, 
			"Creating a new ImportProduct.  Will write an " +
			"almost empty ImportProduct to the database so can " +
			"get its autonum for (ImportProduct_num)." );
		}
		try {
			//__dmi.setDumpSQLOnExecution( true );
			//write product (returns an ImportProduct object)
 			__db_RTi_ImportProduct = 
 			__dmi.writeImportProduct( __db_RTi_ImportProduct );
			Message.printStatus( 15, routine,
			"ImportProduct written to database." );
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e );
		}
 		__db_ImportProduct_num = __db_RTi_ImportProduct.
		getImportProduct_num();
	}

	//see if user has permissions to change the ImportProduct
	try {
 		__canWriteImportProduct =
 		__dmi.canWrite( __db_RTi_ImportProduct.getDBUser_num(),
 		__db_RTi_ImportProduct.getDBGroup_num(),
 		__db_RTi_ImportProduct.getDBPermissions() );
	}
	catch ( Exception e ) {
		Message.printWarning ( 2, routine, e);
 		__canWriteImportProduct = false;
	}
	//make shared layout paramaters
 	__gridbag =  new GridBagLayout();
 	__insets = new Insets( 2, 2, 2, 2 );

	// create/layout the GUI ...
	init_layout_GUI( );

	//add window listener
	addWindowListener ( this );

	//setup frame to do nothing on close so that we can take over
	//control of window events.
	setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );

	setResizable( false );
}

/**
Method adds all the components to a JPanel for the Archiving tab.
@return JPanel with all the components.
*/
public JPanel assemble_tab_archiving( ) {
	String routine = __class + ".assemble_tab_archiving";

	JPanel archive_JPanel = new JPanel();
	archive_JPanel.setLayout( __gridbag );

	//Permanent archive data files JCheckBox
 	__archive_tab_perm_JCheckBox = new JCheckBox( 
	"Permanently archive data files" );
 	__archive_tab_perm_JCheckBox.setToolTipText( "<HTML>" +
	"Select to permanently archive Import Product data." +
	"<BR>If selected, the Archive directory and file are required.</HTML>");

	//ALways good to give a default value
 	__archive_tab_perm_JCheckBox.setSelected( false );

	//add listener for when button is clicked/unclicked
 	__archive_tab_perm_JCheckBox.addActionListener( this );

	//label for Archive directory
	JLabel archive_tab_dir_JLabel = new JLabel (
 	"Archive Directory:" );

 	archive_tab_dir_JLabel.setToolTipText( "<HTML>" +
	"Required if permanently archiving data: " +
	"<BR>Type in directory (without file name) where Import " +
	"Product will be stored or <BR> use the Browse button " +
	"to select the directory.</HTML>" );

	//JTextField for directory
 	__archive_tab_dir_JTextField = new JTextField( 15  );

	//Button for browsing for directory
 	__archive_tab_browse_JButton = new SimpleJButton( "Browse", this );
 	__archive_tab_browse_JButton.setToolTipText("Browse for directory");

	//label for File Name
	JLabel archive_tab_file_JLabel = new JLabel (
 	"Archive File:" );
 	archive_tab_file_JLabel.setToolTipText( "<HTML>" +
	"Required if permanently archiving data: " +
	"<BR>Type in file name, using wildcards if desired.</HTML>");

	//JTextField for file name
 	__archive_tab_file_JTextField = new JTextField(15);

	//Label for wildcard combobox
	JLabel archive_tab_wild_JLabel = new JLabel( "Wildcards:" );
	archive_tab_wild_JLabel.setToolTipText("Select wildcards to " +
	"append to Import Product file name" );

	//ComboBox to select wildcards
	Vector v = new Vector();
//DELETE	v.addElement("");
	v.addElement("*   - Match all");
	v.addElement(".*  - Match Extension");
	v.addElement("*.  - Match Beginning");
 	__archive_tab_wild_JComboBox = new SimpleJComboBox( v );
	__archive_tab_wild_JComboBox.addActionListener( this );

	try {
	int y =0;
	int x =0;
	//add CheckBox for permanently archiving (includes label)
	JGUIUtil.addComponent( 
		archive_JPanel, __archive_tab_perm_JCheckBox,
		x, y, 2, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//add Label for selecting archive directory
	JGUIUtil.addComponent( 
		archive_JPanel, archive_tab_dir_JLabel,
		x, ++y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//add textfield for directory
	JGUIUtil.addComponent( 
		archive_JPanel, __archive_tab_dir_JTextField,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//add button to browse for directory
	JGUIUtil.addComponent( 
		archive_JPanel, __archive_tab_browse_JButton,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//add label for archive file name
	x=0;
	JGUIUtil.addComponent( 
		archive_JPanel, archive_tab_file_JLabel,
		x, ++y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//add textfield to enter filename
	JGUIUtil.addComponent( 
		archive_JPanel, __archive_tab_file_JTextField,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//add label for wildcard selection
	JGUIUtil.addComponent( 
		archive_JPanel, archive_tab_wild_JLabel,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//add combobox to select wildcards
	JGUIUtil.addComponent( 
		archive_JPanel, __archive_tab_wild_JComboBox,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );
	}
	catch ( Exception e) {
		Message.printWarning( 2, routine, e );
		Message.printWarning( 2, routine,
		"Error laying out archive tab." );
	}


	return archive_JPanel;

} //end assemble_tab_archiving

/**
Method adds all the components to a JPanel for the AUTOMATION tab.
@return JPanel with all the components.
*/
public JPanel assemble_tab_automation( ) {
	String routine = __class + ".assemble_tab_automation";

	JPanel auto_JPanel = new JPanel();
	auto_JPanel.setLayout( __gridbag );

	//isAutomated CheckBox WITH LABEL "Automated"
 	__automation_tab_automated_JCheckBox = new JCheckBox( "Import Is Automated" );
 	__automation_tab_automated_JCheckBox.setToolTipText(
	"Required: Check if Import Product is automated" );

	//give it a listener to listen for when it is clicked and unclicked
 	__automation_tab_automated_JCheckBox.addActionListener( this );

	//import label "Import"
	JLabel automation_tab_import_JLabel = new JLabel( "Import:");

	//checkbox WITH Label "every interval"
 	__automation_tab_every_interval_JRadioButton = new JRadioButton(
	"Import When Interval Matches" );
 	__automation_tab_every_interval_JRadioButton.addItemListener( this );
 	__automation_tab_every_interval_JRadioButton.setToolTipText(
	"Required: Select to set automation interval");

	//checkbox with LABEL "at time"
 	__automation_tab_at_time_JRadioButton = new JRadioButton(
	"Import At Time");
 	__automation_tab_at_time_JRadioButton.addItemListener( this );
 	__automation_tab_at_time_JRadioButton.setToolTipText(
	"Required: Select to set automation time");

	//make button group to add these check boxes to so that
	//only 1 can be checked at a time
	ButtonGroup group = new ButtonGroup();
	group.add( __automation_tab_every_interval_JRadioButton );
	group.add( __automation_tab_at_time_JRadioButton );

	//year label "Year"
	JLabel automation_tab_year_JLabel = new JLabel( "Year:" );
	automation_tab_year_JLabel.setToolTipText("Required if automated: Select year");

	//year Combobox
	Vector yr_vect = new Vector();
	yr_vect.addElement( "* - All" );
 	__automation_tab_year_JComboBox = new SimpleJComboBox( yr_vect );

	//month label "month"
	JLabel automation_tab_month_JLabel = new JLabel( "Month: " );
	automation_tab_month_JLabel.setToolTipText("Required if automated: Select month");

	//month JComboBox
	Vector mo_vect = new Vector();
	mo_vect.addElement( "* - All" );
	for ( int i=1; i<=12; i++ ) {
		mo_vect.addElement( String.valueOf(i) + " - " +
		TimeUtil.monthAbbreviation( i ) );
	}
 	__automation_tab_month_JComboBox = new SimpleJComboBox( mo_vect );
 	__automation_tab_month_JComboBox.addItemListener( this );

	//day label "Day"
	JLabel automation_tab_day_JLabel = new JLabel( "Day: " );
	automation_tab_day_JLabel.setToolTipText("Required if automated: Select day");

	//day JComboBox
	Vector day_vect = new Vector();
	day_vect.addElement("* - All" );
	//default, fill with 31
	for ( int i=1; i<=31; i++ ) {
		day_vect.addElement( String.valueOf(i) );
	}
 	__automation_tab_day_JComboBox = new SimpleJComboBox( day_vect );

	//hour label "hour"
	JLabel automation_tab_hour_JLabel = new JLabel( "Hour: " );
	automation_tab_hour_JLabel.setToolTipText("Required if automated: Select hour");

	//hour JComboBox 
	Vector hour_vect = new Vector();
	hour_vect.addElement("* - All" );
	for ( int i=0; i<=23; i++ ) {
		hour_vect.addElement( String.valueOf(i) );
	}
 	__automation_tab_hour_JComboBox = new SimpleJComboBox( hour_vect );

	//minute label "minute"
	JLabel automation_tab_minute_JLabel = new JLabel( "Minute: " );
	automation_tab_minute_JLabel.setToolTipText("Required if automated: Select minute");

	//minute JCombobox
	Vector min_vect = new Vector();
	min_vect.addElement("* - All" );
	for ( int i=0; i<=59; i++ ) {
		min_vect.addElement( String.valueOf(i) );
	}
 	__automation_tab_minute_JComboBox = new SimpleJComboBox( min_vect );

	//second label "second"
	JLabel automation_tab_second_JLabel = new JLabel( "Second: " );
	automation_tab_second_JLabel.setToolTipText("Required if automated: Select second");

	//second JComboBox
	Vector sec_vect = new Vector();
	sec_vect.addElement("* - All" );
	for ( int i=0; i<=59; i++ ) {
		sec_vect.addElement( String.valueOf(i) );
	}
 	__automation_tab_second_JComboBox = new SimpleJComboBox( sec_vect );

	//weekday label "weekday"
	JLabel automation_tab_weekday_JLabel = new JLabel( "Weekday: " );
	automation_tab_weekday_JLabel.setToolTipText("Required if automated: Select weekday");

	//weekday combo box (days of week)
	String [] arr_days = null;
	arr_days = TimeUtil.DAY_NAMES;
	Vector days_vect = new Vector();
	days_vect.addElement( "* - All" );
	for ( int i=0; i<arr_days.length; i++ ) {
		days_vect.addElement( String.valueOf(i) + " - " + arr_days[i] );
	}
 	__automation_tab_weekday_JComboBox = new SimpleJComboBox( days_vect );
 	JGUIUtil.setEnabled(__automation_tab_weekday_JComboBox, false );

	//delay label "Delay"
	JLabel automation_tab_delay_JLabel = new JLabel( "Delay: " );
	automation_tab_delay_JLabel.setToolTipText("Required if automated: Select delay");

	Vector delay_unit_vect = new Vector();
	delay_unit_vect.addElement( "DAY" );
	delay_unit_vect.addElement( "HOUR" );
	delay_unit_vect.addElement( "MINUTE" );
	delay_unit_vect.addElement( "MONTH" );
	delay_unit_vect.addElement( "SECOND" );
	delay_unit_vect.addElement( "YEAR" );

 	__automation_tab_delay_unit_JComboBox = new SimpleJComboBox( 
	delay_unit_vect );
 	__automation_tab_delay_unit_JComboBox.addItemListener( this );

	//make int values based on first item in unit_vect-
	//this will be updated in update_gui_fields
	int [] arrMults = null;
	//allow all multiplier intervals, not just evenly divisible ones
	//and include zero
	arrMults = TimeInterval.multipliersForIntervalBase(
	"DAY", false, true );
 	__automation_tab_delay_int_JComboBox = new SimpleJComboBox();
	for ( int i=0; i<arrMults.length; i++ ) {
 	__automation_tab_delay_int_JComboBox.addItem(
		String.valueOf( arrMults[i] ) );
	}

	//Assemble
	try {
	//ADD isAutomated CheckBox with label "Automated"
	//top, left, bottom, right
	int y = 0;
	int x = 0;
	JGUIUtil.addComponent( 
		auto_JPanel, __automation_tab_automated_JCheckBox,
		x, y, 3, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );
			
	//ADD Import checkbox With Label "when interval matches"
	JGUIUtil.addComponent( 
		auto_JPanel, __automation_tab_every_interval_JRadioButton,
		x, ++y, 3, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//ADD at time checkbox with Label "at time"
	++x;
	++x;
	JGUIUtil.addComponent( 
		auto_JPanel, __automation_tab_at_time_JRadioButton,
		++x, y, 2, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//ADD year label "Year"
	x=0;
	JGUIUtil.addComponent( 
		auto_JPanel, automation_tab_year_JLabel,
		x, ++y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//ADD year textfield
	JGUIUtil.addComponent( 
		auto_JPanel, __automation_tab_year_JComboBox,
		++x, y, 2, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//ADD month label "month"
	--x;
	JGUIUtil.addComponent( 
		auto_JPanel, automation_tab_month_JLabel,
		x, ++y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//ADD month textfield
	JGUIUtil.addComponent( 
		auto_JPanel, __automation_tab_month_JComboBox,
		++x, y, 2, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	--x;
	//ADD day label "Day"
	JGUIUtil.addComponent( 
		auto_JPanel, automation_tab_day_JLabel,
		x, ++y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//ADD day textfield
	JGUIUtil.addComponent( 
		auto_JPanel, __automation_tab_day_JComboBox,
		++x, y, 2, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	--x;
	//ADD hour label "hour"
	JGUIUtil.addComponent( 
		auto_JPanel, automation_tab_hour_JLabel,
		x, ++y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//ADD hour textfield
	JGUIUtil.addComponent( 
		auto_JPanel, __automation_tab_hour_JComboBox,
		++x, y, 2, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	--x;
	//ADD minute label "minute"
	JGUIUtil.addComponent( 
		auto_JPanel, automation_tab_minute_JLabel,
		x, ++y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//ADD minute textfield
	JGUIUtil.addComponent( 
		auto_JPanel, __automation_tab_minute_JComboBox,
		++x, y, 2, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	--x;
	//ADD second label "second"
	JGUIUtil.addComponent( 
		auto_JPanel, automation_tab_second_JLabel,
		x, ++y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//ADD second textfield
	JGUIUtil.addComponent( 
		auto_JPanel, __automation_tab_second_JComboBox,
		++x, y, 2, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	--x;
	//ADD weedkay label "weekday"
	JGUIUtil.addComponent( 
		auto_JPanel, automation_tab_weekday_JLabel,
		x, ++y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//ADD weekday combo box (days of week)
	JGUIUtil.addComponent( 
		auto_JPanel, __automation_tab_weekday_JComboBox,
		++x, y, 2, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	--x;
	//ADD delay label "Delay"
	JGUIUtil.addComponent( 
		auto_JPanel, automation_tab_delay_JLabel,
		x, ++y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//delay int JComboBox
	JGUIUtil.addComponent( 
		auto_JPanel, __automation_tab_delay_int_JComboBox,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//delay unit JComboBox
	JGUIUtil.addComponent( 
		auto_JPanel, __automation_tab_delay_unit_JComboBox,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e);
		Message.printWarning( 2, routine,
		"Unable to create automation tab components." );
	}

	return auto_JPanel;
} //end assemble_tab_automation

/**
Method adds all the components to a JPanel for the Files tab.
@return JPanel with all the components.
*/
public JPanel assemble_tab_files( ) {
	String routine = __class + ".assemble_tab_files";

	JPanel files_JPanel = new JPanel();
	files_JPanel.setLayout( __gridbag );

	//Select Source directory
	JLabel files_tab_source_dir_JLabel = new JLabel( "Source Directory:");
	files_tab_source_dir_JLabel.setToolTipText(
	"Required: Type in or browse for Import Product's source " +
	"directory (without file name)" );

	//type in directory
 	__files_tab_source_dir_JTextField = new JTextField( 15 );

	//button to browse to directory
 	__files_tab_source_browse_JButton = new SimpleJButton( "Browse", this );
 	__files_tab_source_browse_JButton.setToolTipText("Browse for directory");

	//Select source FILE iteself
	JLabel files_tab_source_file_JLabel = new JLabel( "Source File(s):" );
	files_tab_source_file_JLabel.setToolTipText(
	"Required: Type in file name for Import Product, using wildcards if needed.");

	//Type in  source file name
 	__files_tab_source_file_JTextField = new JTextField( 15  );

	//Use wildcard to get source file names
	JLabel files_tab_source_wild_JLabel = new JLabel( "Wildcards" );
	files_tab_source_wild_JLabel.setToolTipText("<HTML>Optional: "+
	"use wildcards to define imported file name. <BR>" +
	"Wildcards will be appended to file name. </html>");

	//make combo box with wildcard choices
	Vector v = new Vector();
//DELETE	v.addElement("");
	v.addElement("*   - Match all");
	v.addElement(".*  - Match Extension");
	v.addElement("*.  - Match Beginning");
 	__files_tab_source_wild_JComboBox = new SimpleJComboBox( v );
 	JGUIUtil.setEnabled(__files_tab_source_wild_JComboBox, true );
	__files_tab_source_wild_JComboBox.addActionListener( this );
	v = null;

	//make label for destination dir
	JLabel files_tab_dest_dir_JLabel = new JLabel( "Destination Directory:" );
	files_tab_dest_dir_JLabel.setToolTipText(
	"Required: Type in or browse for Import Product's destination " +
	"directory (without file name)" );

	//make textfield for entering destination dir
 	__files_tab_dest_dir_JTextField = new JTextField( 15  );

	//make BUtton for browsing to destination dir
 	__files_tab_dest_browse_JButton = new SimpleJButton(
 	"Browse", this );
 	__files_tab_dest_browse_JButton.setToolTipText(
	"Browse for directory" );

	//make label for destination file 
	JLabel files_tab_dest_file_JLabel = new JLabel( "Destination File(s):" );
	files_tab_dest_file_JLabel.setToolTipText(
	"Required: Type in file name for Import Product, using wildcards if needed.");

	//make textfield for entering destination file 
 	__files_tab_dest_file_JTextField = new JTextField( 15  );

	//Use wildcard to get destination file names
	JLabel files_tab_dest_wild_JLabel = new JLabel( "Wildcards" );
	files_tab_dest_wild_JLabel.setToolTipText("<HTML>Optional: "+
	"use wildcards to define file name. <BR>" +
	"Wildcards will be appended to file name. </html>");

	//but make new SimpleJComboBox
	Vector wild_vect = new Vector();
//DELETE	wild_vect.addElement("");
	wild_vect.addElement("%F - Substitute file name, no extension, from Source File(s)");
	wild_vect.addElement("%E - Substitute extension given in Source File(s)");
	wild_vect.addElement("%Y - Substitute current year (4 digits)" );
	wild_vect.addElement("%2Y - Substitute current year (2 digits)" );
	wild_vect.addElement("%M - Substitute current month (2 digits" );
	wild_vect.addElement("%D - Substitute current day (2 digits)" );
	wild_vect.addElement("%H - Substitute current hour (2 digits)" );
	wild_vect.addElement("%N - Substitute current minute (2 digits)" );
	wild_vect.addElement("%S - Substitute current second (2 digits)" );

 	__files_tab_dest_wild_JComboBox = new SimpleJComboBox( wild_vect );
 	JGUIUtil.setEnabled(__files_tab_dest_wild_JComboBox, true );
	__files_tab_dest_wild_JComboBox.addActionListener( this );

	//now assemble panel 
	int y =0;
	//label for source directory
	JGUIUtil.addComponent( 
		files_JPanel, files_tab_source_dir_JLabel,
		0, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//text field to enter name of source directory
	JGUIUtil.addComponent( 
		files_JPanel, __files_tab_source_dir_JTextField,
		1, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//Button to browse for source directory name
	JGUIUtil.addComponent( 
		files_JPanel, __files_tab_source_browse_JButton,
		2, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//label for source file
	JGUIUtil.addComponent( 
		files_JPanel, files_tab_source_file_JLabel,
		0, ++y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//text field to enter name of source file
	JGUIUtil.addComponent( 
		files_JPanel, __files_tab_source_file_JTextField,
		1, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//LABEL for ComboBox to select wildcard for source file
	JGUIUtil.addComponent( 
		files_JPanel, files_tab_source_wild_JLabel,
		2, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//ComboBox to select wildcard for source file
	JGUIUtil.addComponent( 
		files_JPanel, __files_tab_source_wild_JComboBox,
		3, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//label for destination directory
	JGUIUtil.addComponent( 
		files_JPanel, files_tab_dest_dir_JLabel,
		0, ++y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//text field to enter name of destination directory
	JGUIUtil.addComponent( 
		files_JPanel, __files_tab_dest_dir_JTextField,
		1, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//Button to browse for destination directory name
	JGUIUtil.addComponent( 
		files_JPanel, __files_tab_dest_browse_JButton,
		2, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//label for destination file
	JGUIUtil.addComponent( 
		files_JPanel, files_tab_dest_file_JLabel,
		0, ++y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//text field to enter name of destination file
	JGUIUtil.addComponent( 
		files_JPanel, __files_tab_dest_file_JTextField,
		1, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//LABEL for ComboBox to select wildcard for destination file
	JGUIUtil.addComponent( 
		files_JPanel, files_tab_dest_wild_JLabel,
		2, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//ComboBox to select wildcard for destination file
	JGUIUtil.addComponent( 
		files_JPanel, __files_tab_dest_wild_JComboBox,
		3, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	return files_JPanel;

} //end assemble_tab_files

/**
Method adds all the components to a JPanel for the New GROUP tab.
@return JPanel with all the components.
*/
public JPanel assemble_tab_new_group( ) {
	String routine = __class + ".assemble_tab_new_group";

	//Make panel to hold everything.  This will be returned.
	JPanel newGroup_JPanel = new JPanel();
	newGroup_JPanel.setLayout( __gridbag );

	//Label for name of new product
	JLabel new_group_tab_name_JLabel = new JLabel ( "Name of New Group:" );
	new_group_tab_name_JLabel.setToolTipText(
	"Required: name of new group" );

	//TextField to enter actual name
 	__new_group_tab_name_JTextField = new JTextField( 20 );

	//assemble components
	//label
	JGUIUtil.addComponent(
		newGroup_JPanel, new_group_tab_name_JLabel,
		0, 0, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );
		//GridBagConstraints.WEST );

	//text field
	JGUIUtil.addComponent(
		newGroup_JPanel, __new_group_tab_name_JTextField,
		1, 0, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	return newGroup_JPanel;

} //end assemble_tab_new_group

/**
Method adds all the components to a JPanel for the Properties tab.
@return JPanel with all the components.
*/
public JPanel assemble_tab_properties( ) {
	String routine = __class + ".assemble_tab_properties";

	//Make panel to hold everything.  This will be returned.
	JPanel props_JPanel = new JPanel();
	props_JPanel.setLayout( __gridbag );

	//now start with the components

	//Label for Import Order
	JLabel props_tab_import_order_JLabel = new JLabel( "Import Order:" );
	props_tab_import_order_JLabel.setToolTipText(
	"Required: select import order (2 is suggested)" );

	//Description for Import Order
	JLabel props_tab_import_order_desc_JLabel = new JLabel( "2 is recommended" ) ;

	//TextField for Import Order
	Vector order_vect = new Vector();
	//Michael originally said to start at 2, but there is 
	//data in the db that starts with 1...
	for ( int i=1;i<15;i++ ) {
		order_vect.addElement( String.valueOf(i) );
	}
	order_vect.addElement("99");
 	__props_tab_import_order_JComboBox = new SimpleJComboBox( order_vect );

	//Label for Import Window
	JLabel props_tab_import_window_JLabel = new JLabel( "Import Window:" );
	props_tab_import_window_JLabel.setToolTipText(
	"Required: select import window interval" );

	//Description for Import Window
	JLabel props_tab_import_window_desc_JLabel = new JLabel(
	"Recent period to process" );

	//TextField for Import Window
	Vector window_unit_vect = new Vector();
	window_unit_vect.addElement( "DAY" );
	window_unit_vect.addElement( "HOUR" );
	window_unit_vect.addElement( "MINUTE" );
	window_unit_vect.addElement( "MONTH" );
	window_unit_vect.addElement( "SECOND" );
	window_unit_vect.addElement( "YEAR" );

 	__props_tab_import_window_unit_JComboBox = new SimpleJComboBox( 
	window_unit_vect );
 	__props_tab_import_window_unit_JComboBox.addItemListener( this );

	//make choices of Multipliers based on first item in unit vector
	int [] arrMults = null;
	arrMults = TimeInterval.multipliersForIntervalBase(
	"DAY", true, true );
 	__props_tab_import_window_int_JComboBox = new SimpleJComboBox();
	for ( int i=0; i<arrMults.length; i++ ) {
 		__props_tab_import_window_int_JComboBox.addItem(
		String.valueOf( arrMults[i] ) );
	}

	//Label for Last Import Date
	JLabel props_tab_last_import_date_JLabel = new JLabel(
	"Last Import Date/Time:" );
	props_tab_last_import_date_JLabel.setToolTipText(
	"Uneditable: last import date and time (set by RiverTrak)");

	//Description/Format label for Last Import Date
	JLabel props_tab_last_import_date_desc_JLabel = new JLabel(
	"(YYYY-MM-DD HH:mm:ss) Set by RiverTrak® Forecaster/Watcher:" );

	//Label for Next Import Date
	JLabel props_tab_next_import_date_JLabel = new JLabel(
	"Next Import Date/Time:" );
	props_tab_next_import_date_JLabel.setToolTipText(
	"Required: set next import date and time");

	//Description/Format label for Next Import Date
	JLabel props_tab_next_import_date_desc_JLabel = new JLabel(
	"(YYYY-MM-DD HH:mm:ss) " + "Used/set by RiverTrak® Forecaster/Watcher");

	//TextField for Import Date
	//non - editable for LAST date
 	__props_tab_last_import_date_JTextField = new JTextField( 15 );
 	JGUIUtil.setEnabled(__props_tab_last_import_date_JTextField, false );
 	__props_tab_last_import_date_JTextField.setBackground( Color.lightGray );

	//editable for next
 	__props_tab_next_import_date_JTextField = new JTextField( 15 );

	//Label for Retries
	JLabel props_tab_retries_JLabel = new JLabel( "Retries:" );
	props_tab_retries_JLabel.setToolTipText(
	"Uneditble: number of retries (set by RiverTrak)" );

	//Description for Retries
	JLabel props_tab_retries_desc_JLabel = new JLabel(
	"Set by RiverTrak® Forecaster/Watcher" );

	//JTextField for Retries - noneditable
 	__props_tab_retries_JTextField = new JTextField( 3 );
 	JGUIUtil.setEnabled(__props_tab_retries_JTextField, false );
 	__props_tab_retries_JTextField.setBackground( Color.lightGray );

	//PROPERTIES...  the properties differ depending on the type
	//of ImportProduct.  Create the components, for all, but only 
	//add the ones that are needed for the current type.
	JPanel properties_JPanel = null;

	//FOR CAMPBELLSCIENTIFIC
	//1 "JULIANDAY" JLabel and JComboBox
	JLabel props_tab_props_1_campbellsci_JLabel = new JLabel(
 	__props_1_campbellsci_JLabel_str + ":" );
	props_tab_props_1_campbellsci_JLabel.setToolTipText("Required: " +
	"File column number for Julian day" );

	//description
	JLabel props_tab_props_1_campbellsci_desc_JLabel = new JLabel(
 	__props_1_campbellsci_desc_JLabel_str );

	Vector prop1_v = new Vector();
	for ( int i=1; i<=50; i++ ) {
		prop1_v.addElement( String.valueOf(i) );
	}
 	__props_1_campbellsci_JComboBox = new SimpleJComboBox( prop1_v );

	//2 "MISSING_VALUE" JLabel and JTextField;
	JLabel props_tab_props_2_campbellsci_JLabel = new JLabel(
 	__props_2_campbellsci_JLabel_str + ":" ); 
	props_tab_props_2_campbellsci_JLabel.setToolTipText("Required: " +
	"Missing value indicator (should agree with CSI datalogger)" );

	//description
	JLabel props_tab_props_2_campbellsci_desc_JLabel = new JLabel(
 	__props_2_campbellsci_desc_JLabel_str ); 

	//set value in textfield as default: -6999
 	__props_2_campbellsci_JTextField = new JTextField( "-6999", 4 );

	//3 "TABLEID" JLabel;
//	JLabel props_tab_props_3_diadvisor_JLabel = new JLabel(
// 	__props_3_diadvisor_JLabel_str + ":" );
//	props_tab_props_3_diadvisor_JLabel.setToolTipText("Required: " +
//	"File column number for Table ID" );


	//3 "TABLEID" JLabel and JComboBox;
	JLabel props_tab_props_3_campbellsci_JLabel = new JLabel(
 	__props_3_campbellsci_JLabel_str + ":" );
	props_tab_props_3_campbellsci_JLabel.setToolTipText("Required: " +
	"File column number for Table ID" );

	//description
	JLabel props_tab_props_3_campbellsci_desc_JLabel = new JLabel(
 	__props_3_campbellsci_desc_JLabel_str );

	Vector prop3_v = new Vector();
	for ( int i=1; i<=50; i++ ) {
		prop3_v.addElement( String.valueOf(i) );
	}
 	__props_3_campbellsci_JComboBox = new SimpleJComboBox( prop3_v );
	
	//4 "TIME" JLabel and JComboBox;
	JLabel props_tab_props_4_campbellsci_JLabel = new JLabel(
 	__props_4_campbellsci_JLabel_str + ":" ); 
	props_tab_props_4_campbellsci_JLabel.setToolTipText("Required: " +
	"File column number for time" );

	//description
	JLabel props_tab_props_4_campbellsci_desc_JLabel = new JLabel(
 	__props_4_campbellsci_desc_JLabel_str ); 

	Vector prop4_v = new Vector();
	for ( int i=1; i<=50; i++ ) {
		prop4_v.addElement( String.valueOf(i) );
	}
 	__props_4_campbellsci_JComboBox = new SimpleJComboBox( prop4_v );

	//5 "TIMEZONE" JLabel and JComboBox;
	JLabel props_tab_props_5_campbellsci_JLabel = new JLabel(
 	__props_5_campbellsci_JLabel_str + ":" );
	props_tab_props_5_campbellsci_JLabel.setToolTipText("Required: " +
	"File column number for timezone" );

	//description
	JLabel props_tab_props_5_campbellsci_desc_JLabel = new JLabel(
 	__props_5_campbellsci_desc_JLabel_str );

	Vector prop5_v = new Vector();
	prop5_v.addElement( "NONE" );
	for ( int i=0; i<=50; i++ ) {
		prop5_v.addElement( String.valueOf(i) );
	}
 	__props_5_campbellsci_JComboBox = new SimpleJComboBox( prop5_v );
 	JGUIUtil.setEnabled(__props_5_campbellsci_JComboBox, false );

	//6 "YEAR" JLabel and JComboBox;
	JLabel props_tab_props_6_campbellsci_JLabel = new JLabel(
 	__props_6_campbellsci_JLabel_str + ":" );
	props_tab_props_6_campbellsci_JLabel.setToolTipText("Required: " +
	"File column number for year" );
	//description
	JLabel props_tab_props_6_campbellsci_desc_JLabel = new JLabel(
 	__props_6_campbellsci_desc_JLabel_str );

	Vector prop6_v = new Vector();
	for ( int i=1; i<=50; i++ ) {
		prop6_v.addElement( String.valueOf(i) );
	}
 	__props_6_campbellsci_JComboBox = new SimpleJComboBox( prop6_v );
	
	//FOR GENERIC (non-campbellscientific types)
	//JLabel "Properties"
	JLabel props_tab_props_JLabel = new JLabel( "Properties:" );
	props_tab_props_JLabel.setToolTipText("<HTML> Add properties, using " +
	"syntax like: <BR> \"property key= value\"  <BR>" +
	"Seperate property pairs by semi-colons \";\"" );

	//TextField for Properties
 	__props_tab_generic_props_JTextField = new JTextField( 40 );

	//Assemble panel
	//Add Label for Import Window
	int y =0;
	int x =0;
	//Label for Import Order
	JGUIUtil.addComponent(
		props_JPanel, props_tab_import_order_JLabel,
		x, y, 1, 1, 0.0, 0.0, 
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//Add TextField for Import Order
	JGUIUtil.addComponent(
		props_JPanel, __props_tab_import_order_JComboBox,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//Add Description for Import Order
	JGUIUtil.addComponent(
		props_JPanel, props_tab_import_order_desc_JLabel,
		++x, y, 2, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//Label for Import Window
	x=0;
	JGUIUtil.addComponent(
		props_JPanel, props_tab_import_window_JLabel,
		x, ++y, 1, 1, 0.0, 0.0, 
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	JGUIUtil.addComponent(
		props_JPanel, __props_tab_import_window_int_JComboBox,
		++x, y, 1, 1, 0.0, 0.0, 
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	JGUIUtil.addComponent(
		props_JPanel, __props_tab_import_window_unit_JComboBox,
		++x, y, 1, 1, 0.0, 0.0, 
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//Add Description for Import Window
	JGUIUtil.addComponent(
		props_JPanel, props_tab_import_window_desc_JLabel,
		++x, y, 3, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	x=0;
	//Add Label for Retries
	JGUIUtil.addComponent(
		props_JPanel, props_tab_retries_JLabel,
		x, ++y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//Add TextField for Retries
	JGUIUtil.addComponent(
		props_JPanel, __props_tab_retries_JTextField,
		++x, y, 2, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//Description for retries
	JGUIUtil.addComponent(
		props_JPanel, props_tab_retries_desc_JLabel,
		++x, y, 3, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//Add Label for Last Import Date
	x=0;
	JGUIUtil.addComponent(
		props_JPanel, props_tab_last_import_date_JLabel,
		x, ++y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//Add TextField for Last Import Date 
	JGUIUtil.addComponent(
		props_JPanel, __props_tab_last_import_date_JTextField,
		++x, y, 3, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//Add description (with date format) 
	++x;
	++x;
	JGUIUtil.addComponent(
		props_JPanel, props_tab_last_import_date_desc_JLabel,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );


	//Add Label for NEXT Import Date
	x=0;
	JGUIUtil.addComponent(
		props_JPanel, props_tab_next_import_date_JLabel,
		x, ++y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//Add TextField for NEXT Import Date
	JGUIUtil.addComponent(
		props_JPanel, __props_tab_next_import_date_JTextField,
		++x, y, 3, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//Add description (with date format) 
	++x;
	++x;
	JGUIUtil.addComponent(
		props_JPanel, props_tab_next_import_date_desc_JLabel,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	x=0;
	
	// Add properties expecific to the Diavisor product.
	if( __db_ImportProduct_type.equalsIgnoreCase( __type_diadvisor_str) ) {
		
/*		x=0;
		++y;
		// Source URL Base
		JGUIUtil.addComponent( 
			props_JPanel, props_tab_props_2_diadvisor_JLabel,
			x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );
	
		JGUIUtil.addComponent( 
			props_JPanel, __props_2_diadvisor_JTextField,
			++x, y, 2, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );
	

		x=0;
		++y;
		// Source URL File
		JGUIUtil.addComponent( 
			props_JPanel, props_tab_props_3_diadvisor_JLabel,
			x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );
	
		JGUIUtil.addComponent( 
			props_JPanel, __props_3_diadvisor_JTextField,
			++x, y, 2, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );		*/
	}
	
	//add properties- specific to Product Type
	if( __db_ImportProduct_type.equalsIgnoreCase( __type_campbellsci_str) ) {
		x=0;
		++y;
		//TABLEID
		JGUIUtil.addComponent( 
			props_JPanel, props_tab_props_3_campbellsci_JLabel,
			x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );
	
		JGUIUtil.addComponent( 
			props_JPanel, __props_3_campbellsci_JComboBox,
			++x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );
	
		JGUIUtil.addComponent( 
			props_JPanel, props_tab_props_3_campbellsci_desc_JLabel,
			++x, y, 3, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );
		x=0;
		++y;
		//MISSING_VALUE
		JGUIUtil.addComponent( 
			props_JPanel, props_tab_props_2_campbellsci_JLabel,
			x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );
	
		JGUIUtil.addComponent( 
			props_JPanel, __props_2_campbellsci_JTextField,
			++x, y, 2, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );
	
		JGUIUtil.addComponent( 
			props_JPanel, props_tab_props_2_campbellsci_desc_JLabel,
			++x, y, 3, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );
		x=0;
		++y;
		//TIME
		JGUIUtil.addComponent( 
			props_JPanel, props_tab_props_4_campbellsci_JLabel,
			x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );
	
		JGUIUtil.addComponent( 
			props_JPanel, __props_4_campbellsci_JComboBox,
			++x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );
	
		JGUIUtil.addComponent( 
			props_JPanel, props_tab_props_4_campbellsci_desc_JLabel,
			++x, y, 3, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

		x=0;
		++y;
		//TIMEZONE
		JGUIUtil.addComponent( 
			props_JPanel, props_tab_props_5_campbellsci_JLabel,
			x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );
	
		JGUIUtil.addComponent( 
			props_JPanel, __props_5_campbellsci_JComboBox,
			++x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );
	
		JGUIUtil.addComponent( 
			props_JPanel, props_tab_props_5_campbellsci_desc_JLabel,
			++x, y, 3, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );
		x=0;
		++y;
		//JULIANDAY
		JGUIUtil.addComponent( 
			props_JPanel, props_tab_props_1_campbellsci_JLabel,
			x, ++y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );
		JGUIUtil.addComponent( 
			props_JPanel, __props_1_campbellsci_JComboBox,
			++x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );
	
		JGUIUtil.addComponent( 
			props_JPanel, props_tab_props_1_campbellsci_desc_JLabel,
			++x, y, 3, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );
		x=0;
		++y;
		//YEAR
		JGUIUtil.addComponent( 
			props_JPanel, props_tab_props_6_campbellsci_JLabel,
			x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );
	
		JGUIUtil.addComponent( 
			props_JPanel, __props_6_campbellsci_JComboBox,
			++x, y, 2, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );
	
		JGUIUtil.addComponent( 
			props_JPanel, props_tab_props_6_campbellsci_desc_JLabel,
			++x, y, 3, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );
	}
	else {
		JGUIUtil.addComponent( 
			props_JPanel, props_tab_props_JLabel,
			x, ++y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );

		JGUIUtil.addComponent( 
			props_JPanel, __props_tab_generic_props_JTextField,
			++x, y, 4, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

	}

	return props_JPanel;

} //end assemble_tab_properties

//Can use methods like below once we have implemented support for more
//types of ImportProducts...
/**
Method assembles a sub-panel for the Properties tab that contains
the configurable properties for an ImportProduct of type CAMPBELLSCIENTIFIC.
@return JPanel containing all the components needed for setting 
the properties of ImportProduct type CAMPBELLSCIENTIFIC.
*/
/*
protected JPanel assemble_tab_properties_campbellsci_props() {
	String routine = __class + ".assemble_tab_properties_campbellsci_props";
	JPanel panel = new JPanel();
	panel.setLayout( new GridBagLayout());

	//1 "JULIANDAY" JLabel and JComboBox
	JLabel props_tab_props_1_campbellsci_JLabel = new JLabel(
 	__props_1_campbellsci_JLabel_str + ":" );

	//description
	JLabel props_tab_props_1_campbellsci_desc_JLabel = new JLabel(
 	__props_1_campbellsci_desc_JLabel_str );

	Vector prop1_v = new Vector();
	for ( int i=1; i<=50; i++ ) {
		prop1_v.addElement( String.valueOf(i) );
	}
 	__props_1_campbellsci_JComboBox = new SimpleJComboBox( prop1_v );

	//2 "MISSING_VALUE" JLabel and JTextField;
	JLabel props_tab_props_2_campbellsci_JLabel = new JLabel(
 		__props_2_campbellsci_JLabel_str + ":" ); 

	//description
	JLabel props_tab_props_2_campbellsci_desc_JLabel = new JLabel(
 	__props_2_campbellsci_desc_JLabel_str ); 

	//set value in textfield as default: -6999
 	__props_2_campbellsci_JTextField = new JTextField( "-6999", 5 );

	//3 "TABLEID" JLabel and JComboBox;
	JLabel props_tab_props_3_campbellsci_JLabel = new JLabel(
 	__props_3_campbellsci_JLabel_str + ":" );

	//description
	JLabel props_tab_props_3_campbellsci_desc_JLabel = new JLabel(
 	__props_3_campbellsci_desc_JLabel_str );

	Vector prop3_v = new Vector();
	for ( int i=1; i<=50; i++ ) {
		prop3_v.addElement( String.valueOf(i) );
	}
 	__props_3_campbellsci_JComboBox = new SimpleJComboBox( prop3_v );
	
	//4 "TIME" JLabel and JComboBox;
	JLabel props_tab_props_4_campbellsci_JLabel = new JLabel(
 	__props_4_campbellsci_JLabel_str + ":" ); 

	//description
	JLabel props_tab_props_4_campbellsci_desc_JLabel = new JLabel(
 	__props_4_campbellsci_desc_JLabel_str ); 

	Vector prop4_v = new Vector();
	for ( int i=1; i<=50; i++ ) {
		prop4_v.addElement( String.valueOf(i) );
	}
 	__props_4_campbellsci_JComboBox = new SimpleJComboBox( prop4_v );

	//5 "TIMEZONE" JLabel and JComboBox;
	JLabel props_tab_props_5_campbellsci_JLabel = new JLabel(
 	__props_5_campbellsci_JLabel_str + ":" );

	//description
	JLabel props_tab_props_5_campbellsci_desc_JLabel = new JLabel(
 	__props_5_campbellsci_desc_JLabel_str );

	Vector prop5_v = new Vector();
	prop5_v.addElement( "NONE" );
	for ( int i=0; i<=50; i++ ) {
		prop5_v.addElement( String.valueOf(i) );
	}
 	__props_5_campbellsci_JComboBox = new SimpleJComboBox( prop5_v );
 	JGUIUtil.setEnabled(__props_5_campbellsci_JComboBox, false );

	//6 "YEAR" JLabel and JComboBox;
	JLabel props_tab_props_6_campbellsci_JLabel = new JLabel(
 	__props_6_campbellsci_JLabel_str + ":" );

	//description
	JLabel props_tab_props_6_campbellsci_desc_JLabel = new JLabel(
 	__props_6_campbellsci_desc_JLabel_str );

	Vector prop6_v = new Vector();
	for ( int i=1; i<=50; i++ ) {
		prop6_v.addElement( String.valueOf(i) );
	}
 	__props_6_campbellsci_JComboBox = new SimpleJComboBox( prop6_v );

	//assemble
	int x=0;
	int y=0;

	JGUIUtil.addComponent( 
		panel, props_tab_props_1_campbellsci_JLabel,
		x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	JGUIUtil.addComponent( 
		panel, __props_1_campbellsci_JComboBox,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	JGUIUtil.addComponent( 
		panel, props_tab_props_1_campbellsci_desc_JLabel,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );
	--x;
	--x;
	++y;
	JGUIUtil.addComponent( 
		panel, props_tab_props_2_campbellsci_JLabel,
		x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	JGUIUtil.addComponent( 
		panel, __props_2_campbellsci_JTextField,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	JGUIUtil.addComponent( 
		panel, props_tab_props_2_campbellsci_desc_JLabel,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );
	--x;
	--x;
	++y;
	JGUIUtil.addComponent( 
		panel, props_tab_props_3_campbellsci_JLabel,
		x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	JGUIUtil.addComponent( 
		panel, __props_3_campbellsci_JComboBox,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	JGUIUtil.addComponent( 
		panel, props_tab_props_3_campbellsci_desc_JLabel,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );
	--x;
	--x;
	++y;
	JGUIUtil.addComponent( 
		panel, props_tab_props_4_campbellsci_JLabel,
		x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	JGUIUtil.addComponent( 
		panel, __props_4_campbellsci_JComboBox,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	JGUIUtil.addComponent( 
		panel, props_tab_props_4_campbellsci_desc_JLabel,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );
	--x;
	--x;
	++y;
	JGUIUtil.addComponent( 
		panel, props_tab_props_5_campbellsci_JLabel,
		x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	JGUIUtil.addComponent( 
		panel, __props_5_campbellsci_JComboBox,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	JGUIUtil.addComponent( 
		panel, props_tab_props_5_campbellsci_desc_JLabel,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );
	--x;
	--x;
	++y;
	JGUIUtil.addComponent( 
		panel, props_tab_props_6_campbellsci_JLabel,
		x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	JGUIUtil.addComponent( 
		panel, __props_6_campbellsci_JComboBox,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	JGUIUtil.addComponent( 
		panel, props_tab_props_6_campbellsci_desc_JLabel,
		++x, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//clean up
	prop1_v = null;
	prop3_v = null;
	prop4_v = null;
	prop5_v = null;
	prop6_v = null;

	return panel;
}
*/

/**
Method assembles a sub-panel for the Properties tab that contains
the configurabel properties for an ImportProduct of any type other 
than CAMPBELLSCIENTIFIC.
@return JPanel containing all the components needed for setting 
the properties of ImportProduct any type other than CAMPBELLSCIENTIFIC.
*/
/*
protected JPanel assemble_tab_properties_generic_props() {
	String routine = __class + ".assemble_tab_properties_generic_props";
	JPanel panel = new JPanel();
	panel.setLayout( new GridBagLayout());

	//TextField for Properties
 	__props_tab_generic_props_JTextField = new JTextField( 40 );
	
	JGUIUtil.addComponent( 
		panel, __props_tab_generic_props_JTextField,
		0, 0, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	return panel;
}
*/

/**
Method adds all the components to a JPanel for the Security tab.
@return JPanel with all the components.
*/
public JPanel assemble_tab_security( ) {
	String routine = __class + ".assemble_tab_security";

	//Make panel to hold everything.  This will be returned.
	JPanel security_JPanel = new JPanel();
	security_JPanel.setLayout( __gridbag );

	//make subpanels- one to hold user info and the other
	//to hold firewall info
	JPanel user_JPanel = new JPanel();
	user_JPanel.setLayout( new GridBagLayout() );

	JPanel firewall_JPanel = new JPanel();
	firewall_JPanel.setLayout( new GridBagLayout() );

	//make titled borders to hold: user and to hold: firewall
	Border border = BorderFactory.createLineBorder( Color.black );
	Font title_font = new Font( "Arial", Font.BOLD, 12 );
	TitledBorder user_border = BorderFactory.createTitledBorder(
		border, "User", TitledBorder.TOP, 
		TitledBorder.CENTER, title_font );

	TitledBorder firewall_border = BorderFactory.createTitledBorder(
		border, "Firewall", TitledBorder.TOP, 
		TitledBorder.CENTER, title_font );

	//add borders to panels.
	user_JPanel.setBorder( user_border );
	firewall_JPanel.setBorder( firewall_border );

	//now make text fields: each sub panel gets 
	//a login and a password textField.
	//USER PANEL
	JLabel user_login_JLabel = new JLabel( "Login:" );
	user_login_JLabel.setToolTipText("Optional: add user login" );

 	__security_tab_user_login_JTextField = new JTextField( 10 );

	JLabel user_passwd_JLabel = new JLabel( "Password: " );
	user_passwd_JLabel.setToolTipText("Optional: add user password" );
	
 	__security_tab_user_passwd_JTextField = new JTextField( 10 );

	//add items 
	int y =0;
	//label for user login
	JGUIUtil.addComponent( 
		user_JPanel, user_login_JLabel,
		0, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//textfield for user login
	JGUIUtil.addComponent( 
		user_JPanel, __security_tab_user_login_JTextField,
		1, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//label for user password
	JGUIUtil.addComponent( 
		user_JPanel, user_passwd_JLabel,
		0, ++y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//textfield for user password
	JGUIUtil.addComponent( 
		user_JPanel, __security_tab_user_passwd_JTextField,
		1, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//now work on firewall sub panel
	JLabel firewall_login_JLabel = new JLabel( "Login:" );
	firewall_login_JLabel.setToolTipText("Optional: add firewall login" );

 	__security_tab_firewall_login_JTextField = new JTextField( 10 );

	JLabel firewall_passwd_JLabel = new JLabel( "Password:" );
	firewall_passwd_JLabel.setToolTipText("Optional: add firewall password" );
	
 	__security_tab_firewall_passwd_JTextField = new JTextField( 10 );

	//add items 
	y = 0;
	//label for firewall login
	JGUIUtil.addComponent( 
		firewall_JPanel, firewall_login_JLabel,
		0, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//textfield for firewall login
	JGUIUtil.addComponent( 
		firewall_JPanel, __security_tab_firewall_login_JTextField,
		1, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//label for firewall password
	JGUIUtil.addComponent( 
		firewall_JPanel, firewall_passwd_JLabel,
		0, ++y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	//textfield for firewall password
	JGUIUtil.addComponent( 
		firewall_JPanel, __security_tab_firewall_passwd_JTextField,
		1, y, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//now assemble overall panel by adding 
	//the 2 subpanels
	//top, left, bottom, right
	Insets ins_l = new Insets( 2, 2, 2, 5 );
	Insets ins_r = new Insets( 2, 5, 2, 2 );
	JGUIUtil.addComponent( 
		security_JPanel, user_JPanel,
		0, 0, 1, 1, 1, 0, ins_l,
		GridBagConstraints.NONE,
		GridBagConstraints.EAST );

	JGUIUtil.addComponent( 
		security_JPanel, firewall_JPanel,
		1, 0, 1, 1, 1, 0, ins_r,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );
		

	ins_l = null;
	ins_r = null;
	title_font = null;
	border = null;

	return security_JPanel;

} //end assemble_tab_security

/**
Method adds all the components to a JPanel for the Time Series tab. 
The time series tab consists of a JList of ALL possible import time
series and a JWorksheet of all selected time series for this ImportProduct.
The selected Time Series information itself, displayed in the JWorksheet,
represent the RiversideDB_ImportConf objects associated with this 
ImportProduct.  The JList of all time series to select from consist 
of all MeasType objects that have a create method of: 
<ul><li>IMPORT</li><li>UNKNOWN</li></ul>.  If MeasType with create method 
<i>UNKNOWN</i> is moved into the JWorksheet, that MeasType object is 
updated to have create method: <i>IMPORT</i>.
@return JPanel with all the components.
*/
public JPanel assemble_tab_timeSeries() {
	String routine = __class + ".assemble_tab_timeSeries";

	//Make panel to hold everything.  This will be returned.
	JPanel ts_JPanel = new JPanel();
	ts_JPanel.setLayout( __gridbag );

	//label for left list
	JLabel timeseries_tab_allTS_JLabel = new JLabel (
	"Time Series Defined as Imported:" );
	timeseries_tab_allTS_JLabel.setToolTipText("<HTML>"+
	"Select time series to include in this Import Product." +
	"<BR>Move time series to the right-hand worksheet using the \">\" button</HTML>");

	//label for right list
	JLabel timeseries_tab_selTS_JLabel = new JLabel (
	"Time Series to Import from File:" );  
	timeseries_tab_selTS_JLabel.setToolTipText(
	"<HTML>Required: Worksheet to display time series included in " +
	"Import Product with data related to the time series. </HTML>");

	//LEFT LIST - 
	//get list of all time series that are "UNKNOWN" or
	//are "IMPORT"
	
	// get the items to put in the listmodel
	try {
 		__all_MeasType_vect = 
 		__dmi.readMeasTypeListForTSIdentByLocation( "...." );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}

	//now have a vector of ALL the measTypes
	int numb_all_MeasType = 0;
	if ( __all_MeasType_vect != null ) {
		numb_all_MeasType = __all_MeasType_vect.size();
	}
	if ( Message.isDebugOn ) {
		Message.printDebug( 25, routine,
		"Total number of MeasTypes to choose from: " +
		numb_all_MeasType );
	}

	//Create the listModel for the JList.
 	__timeseries_tab_allTS_listModel = new DefaultListModel();

	//now go through the vector and add the types to the list model
	RiversideDB_MeasType rti_mt = null;
	TSIdent tsid = null;
	String create_str=null;
	for ( int i=0; i< numb_all_MeasType; i++ ) {
		rti_mt =  (RiversideDB_MeasType)
 		__all_MeasType_vect.elementAt(i);
		if ( rti_mt == null ) {
			continue;
		}
		create_str= rti_mt.getCreate_method();
		//exclude those that don't have IMPORT or UNKNOWN 
		//as their create_method
		if (( create_str.equalsIgnoreCase("IMPORT") ) ||
		( create_str.equalsIgnoreCase("UNKNOWN") ) ) {
			//get the IDENTIFIER from the MeasType object
			//and add it to the LISTMODEL for Display
			try {
				tsid = rti_mt.toTSIdent();
			}
			catch ( Exception e) {
				Message.printWarning( 2, routine, e );
				tsid = null;
			}
			if ( tsid != null ) {
 				__timeseries_tab_allTS_listModel.addElement( 
				tsid.toString() + " - " + 
				rti_mt.getDescription() );
			}
			//add this to the __all_import_MeasTypes_vect!
 			__leftList_MeasType_vect.addElement( rti_mt );
		}
	}

	rti_mt = null;

	//make the Left list - which is the  List of ALL measTypes
	//and add the listmodel to it!
 	__timeseries_tab_allTS_JList = new JList( 
 	__timeseries_tab_allTS_listModel );

	//Multiple Selection allowed on LEFT list ( single on right )
 	__timeseries_tab_allTS_JList.setSelectionMode( 
		ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );
 	__timeseries_tab_allTS_JList.setVisibleRowCount( 15 );
 	__timeseries_tab_allTS_JList.setSelectedIndex(0);

	//add list to scroll pane
	JScrollPane timeseries_tab_allTS_JScrollPane = new JScrollPane( 
 	__timeseries_tab_allTS_JList );
	//Dimension( width, height) of JScrollPane
	timeseries_tab_allTS_JScrollPane.setPreferredSize( 
		new Dimension ( 200, 200 ) );
 		timeseries_tab_allTS_JScrollPane.setMinimumSize( 
		new Dimension ( 200, 200 ) );
	timeseries_tab_allTS_JScrollPane.setMaximumSize( 
		new Dimension ( 200, 200 ) );

	//The list of all the Time Series does not need a listener.
	//The buttons trigger any action with items from this list
	//add ListSelectionListener to listen for changes
	//_timeseries_tab_allTS_JList.addListSelectionListener( this );

	//RIGHT LIST - 
	//Make the second List, the right list, that contains only the 
	//MeasTypes for the currently selected ImportProduct. This list is
	//displayed in a JWorksheet.

	//data for list
	//need vector of importconf objects and meastype objects ,
	//labels for worksheet and importproduct type
	Vector mt_vect = new Vector();

	//from the Vector of ImportConf objects, get the
	//the MeasType_nums for those objects.
	//Then, use the number to get the MeasType Object
	int numb_ic = 0;
	numb_ic = __db_RTi_ImportConf_vect.size();

	if ( ( numb_ic == 0 ) && ( ! __bln_new_object ))  {
		Message.printWarning( 1, routine, 
		"No ImportConf Time Series defined for: \"\n" +
 		__db_RTi_ImportProduct.getProduct_name() + "\".", this );
	}
	long mt_int = -999;
	RiversideDB_ImportConf rti_ic = null;
	rti_mt = null;
	for ( int i=0; i< numb_ic; i++ ) {
		rti_ic = ( RiversideDB_ImportConf )
 		__db_RTi_ImportConf_vect.elementAt( i );

		//now get the MeasType from the object...
		mt_int = rti_ic.getMeasType_num();
		//now we MeasType_num, use it to look up MeasType!
		//For
		if ( mt_int < 0 ) {
			continue;
		}
		//get the MeasTypeObject.
		try {
			rti_mt = (RiversideDB_MeasType) 
 			__dmi.readMeasTypeForMeasType_num( mt_int );
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e );
			rti_mt = null;
		}
		if ( rti_mt == null ) {
			continue;
		}

		mt_vect.addElement( rti_mt );

		rti_mt = null;
		rti_ic = null;
	}

	//Labels for worksheet
	if ( __arrWorksheet_labels == null ) {
 		__arrWorksheet_labels = new String[8];
		if ( Message.isDebugOn ) {
			Message.printDebug( 2, routine, 
			"_db_ImportProduct_type = \"" + 
			__db_ImportProduct_type + "\"" );
		}

		if ( __db_ImportProduct_type.indexOf("CAMPBELLSCI" ) >= 0 ) {
 			__arrWorksheet_labels[0] = "row \nnumber";
			//"Data File" for Import Conf field: external id
 			__arrWorksheet_labels[1] = "Time Series ID";
 			__arrWorksheet_labels[2] = "Description";
			//"TS Import Enabled"
 			__arrWorksheet_labels[3] =
 			__timeseries_tab_active_JCheckBox_str;
 			__arrWorksheet_labels[4] = 
 			__timeseries_tab_external_id_JLabel_camp_str;
			//"Data Column" for Import Conf field: external field
 			__arrWorksheet_labels[5] =
 			__timeseries_tab_external_field_JLabel_camp_str;
			//"Table Identifier" for Import Conf field: external table
 			__arrWorksheet_labels[6] =
 			__timeseries_tab_external_table_JLabel_camp_str;
			//"Units"
 			__arrWorksheet_labels[7] =
 			__timeseries_tab_external_units_JLabel_str;
		}
		else if ( __db_ImportProduct_type.indexOf("SHEF" ) >= 0 ) {
 			__arrWorksheet_labels[0] = "row \nnumber";
 			__arrWorksheet_labels[1] = "Time Series ID";
 			__arrWorksheet_labels[2] = "Description";
			//"TS Import Enabled"
 			__arrWorksheet_labels[3] =
 			__timeseries_tab_active_JCheckBox_str;
			//"SHEF ID" for Import Conf field: external table
 			__arrWorksheet_labels[4] = "SHEF \nID";
			//"Shef PE" for Import Conf field: external field
 			__arrWorksheet_labels[5] =
			"SHEF \nPE";
			//This column removed for shef 
			//(Import Conf field: external table)
 			__arrWorksheet_labels[6] =" ";
			//"Units"
 			__arrWorksheet_labels[7] =
 			__timeseries_tab_external_units_JLabel_str;
		}
		else { //fill it like CAMPBELSCI for now...
			//"Data File" for Import Conf field: external id
 			__arrWorksheet_labels[0] = "row \nnumber";
 			__arrWorksheet_labels[1] = "Time Series ID";
 			__arrWorksheet_labels[2] = "Description";
			//"TS Import Enabled"
 			__arrWorksheet_labels[3] =
 			__timeseries_tab_active_JCheckBox_str;
 			__arrWorksheet_labels[4] = 
 			__timeseries_tab_external_id_JLabel_camp_str;
			//"Data Column" for Import Conf field: external field
 			__arrWorksheet_labels[5] =
 			__timeseries_tab_external_field_JLabel_camp_str;
			//"Table Identifier" for Import Conf field: external table
 			__arrWorksheet_labels[6] =
 			__timeseries_tab_external_table_JLabel_camp_str;
			//"Units"
 			__arrWorksheet_labels[7] =
 			__timeseries_tab_external_units_JLabel_str;
		}
	}
	if ( __arrWorksheet_tooltips == null ) {
 		__arrWorksheet_tooltips = new String[7];
		if ( __db_ImportProduct_type.indexOf("CAMPBELLSCI" ) >= 0 ) {
 			__arrWorksheet_tooltips[0] = "Uneditable: Time " +
			"series identifier";
 			__arrWorksheet_tooltips[1] = "Uneditable: Time " +
			"series description";
 			__arrWorksheet_tooltips[2] = "Required: " +
			"Is time series import enabled: Yes, N: No";
			//= "File only - no directory";
 			__arrWorksheet_tooltips[3] = "Required: Data File";
 			__arrWorksheet_tooltips[4] = "Required: Data Column";
 			__arrWorksheet_tooltips[5] = "Required: Table ID";
 			__arrWorksheet_tooltips[6] = "Required: Units";
				
		}
		else if ( __db_ImportProduct_type.indexOf("SHEF" ) >= 0 ) {
 			__arrWorksheet_tooltips[0] = "Uneditable: Time " +
			"series identifier";
 			__arrWorksheet_tooltips[1] = "Uneditable: Time " +
			"series description";
 			__arrWorksheet_tooltips[2] = "Required: " +
			"Is time series import enabled: Yes, N: No";
			//= "File only - no directory";
 			__arrWorksheet_tooltips[3] = "Required: SHEF ID";
 			__arrWorksheet_tooltips[4] = "Required: SHEF PE";
 			__arrWorksheet_tooltips[5] = "";
 			__arrWorksheet_tooltips[6] = "Required: Units";
		}
		else { //fill like CAMPBELSCI
 			__arrWorksheet_tooltips[0] = "Uneditable: Time " +
			"series identifier";
 			__arrWorksheet_tooltips[1] = "Uneditable: Time " +
			"series description";
 			__arrWorksheet_tooltips[2] = "Required: " +
			"Is time series import enabled: Yes, N: No";
			//= "File only - no directory";
 			__arrWorksheet_tooltips[2] = "Required: Data File";
 			__arrWorksheet_tooltips[3] = "Required: Data Column";
 			__arrWorksheet_tooltips[4] = "Required: Table ID";
 			__arrWorksheet_tooltips[6] = "Required: Units";
		}
	}

	if ( __RTi_DataType_vect == null ) {
		try {
 			__RTi_DataType_vect = 
 			__dmi.readDataTypeList();

		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e);
 			__RTi_DataType_vect = new Vector();
		}
	}	

	//make Table Model
	//RiversideDB_ImportConf_TableModel tmic = null;
	try {
		//pass in Vector of ImportConf objects, 
		//Vector of MEasType objects, Arr of labels,
		//import type
		Vector v = new Vector();
	
		//NOTE:: if you pass in the actual vector: 
		//_db_RTi_ImportConf_vect, it will get added to deleted
		//from from within the TableModel, which we do not want!
		v.addElement( __worksheet_RTi_ImportConf_vect );
		v.addElement( mt_vect);

 		__table_model = new RiversideDB_ImportConf_TableModel( 
		v, __arrWorksheet_labels, __arrWorksheet_tooltips,
 		__db_ImportProduct_type, __dmi, __RTi_DataType_vect );

		RiversideDB_ImportConf_CellRenderer crr = 
		new RiversideDB_ImportConf_CellRenderer( __table_model);
	
 		__timeseries_tab_selTS_JWorksheet = new JWorksheet(
		crr, __table_model, __worksheet_PropList);
	}
	catch (Exception e) {
		//else make empty worksheet
		Message.printWarning(2, routine, e);
 		__timeseries_tab_selTS_JWorksheet = new JWorksheet(
		0, 0, __worksheet_PropList );
	}

	//set worksheet so it is known to TableModel and can
	//set it dirty is a value is changed!
 	__table_model.setWorksheet( __timeseries_tab_selTS_JWorksheet );
 	//__timeseries_tab_selTS_JWorksheet.setToolTipText("Add values for " +
	//"selected time series (required).");

	//add worksheet to scroll pane
 	JScrollPane timeseries_tab_selTS_JScrollPane = new JScrollPane( 
 	__timeseries_tab_selTS_JWorksheet );
	//Set size of list
 	timeseries_tab_selTS_JScrollPane.setPreferredSize( __worksheet_dim);
	
 	timeseries_tab_selTS_JScrollPane.setMinimumSize( __worksheet_dim );
 	timeseries_tab_selTS_JScrollPane.setMaximumSize( __worksheet_dim );


	//2 buttuns go in between the 2 Jlists- for
	//moving items back and forth
 	__timeseries_tab_clear_selected_JButton = new SimpleJButton(
 	"Clear Selected", this );
 	__timeseries_tab_clear_selected_JButton.setToolTipText( 
	"Remove entire row of data for selected worksheet cell(s)" );

 	__timeseries_tab_move_right_JButton = new SimpleJButton( ">", this );
 	__timeseries_tab_move_right_JButton.setToolTipText(
	"Move time series to list of selected time series for Import Product");

	//Label for left list
	JGUIUtil.addComponent( 
		ts_JPanel, timeseries_tab_allTS_JLabel,
		0, 0, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//add Left list with full list of TimeSeries
	JGUIUtil.addComponent( 
		ts_JPanel, timeseries_tab_allTS_JScrollPane,
		0, 1, 1, 7, 0.0, 0.0, __insets,
		//GridBagConstraints.NONE,
		GridBagConstraints.BOTH,
		GridBagConstraints.WEST );

	//add button: move right arrow
	JGUIUtil.addComponent( 
		ts_JPanel, __timeseries_tab_move_right_JButton,
		1, 3, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//Label for Right list
	JGUIUtil.addComponent( 
		ts_JPanel, timeseries_tab_selTS_JLabel,
		2, 0, 3, 1, 1, 0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	//add Right list
	JGUIUtil.addComponent( 
		ts_JPanel, timeseries_tab_selTS_JScrollPane,
		2, 1, 3, 3, 0.0, 0.0, __insets,
		//GridBagConstraints.NONE,
		GridBagConstraints.BOTH,
		GridBagConstraints.WEST );

	//Add clear selected button below the worksheet
	JGUIUtil.addComponent( 
		ts_JPanel, __timeseries_tab_clear_selected_JButton,
		4, 5, 1, 1, 0.0, 0.0, __insets,
		GridBagConstraints.NONE,
		GridBagConstraints.WEST );

	return ts_JPanel;

} //end assemble_tab_timeSeries

/**
This method does the following:<ul>
<li>calls <i>checkRequiredInput</i> which makes sure all the required fields in the GUI are filled out and have valid entries</li>
<li>calls <i>update_RiversideDB_objects</i> which is called to 
<b>create</b> and update the <i><b>__gui_RTi_ImportProudct</b></i> and 
<i> <b>__gui_RTi_ImportConf_vect</b></i> objects in memory, mark them dirty, 
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
 		__dirty_vect.clear();

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

	//see if __gui_RTi_ImportConf_vect has any dirty elements
	boolean dirty_importconf = false;
	int size = 0;
	if ( __gui_RTi_ImportConf_vect != null ) {
		size = __gui_RTi_ImportConf_vect.size();
	}
	RiversideDB_ImportConf ic = null;
	for ( int i=0; i<size; i++ ) {
		ic = ( RiversideDB_ImportConf ) 
 		__gui_RTi_ImportConf_vect.elementAt(i);
		if ( ic == null ) {
			continue;
		}
		if ( ic.isDirty() ) {
			dirty_importconf = true;
			break;
		}
		ic = null;
	}

	boolean blnClose = true;
	if( blnUpdated)  {
		if(( __gui_RTi_ImportProduct.isDirty()) || (dirty_importconf)) {
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
	else {
		if ( blnUpdated ) {
			windowManagerClose();
		}
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
	//nothing can be null except the "Selected" input time series
	//since everything else is in a drop-down list.
	
	//Product Identifier
	String prodID= null;
	prodID= __product_info_id_JTextField.getText().trim();
	if( ( prodID == null ) || ( prodID.length() <= 0 ) ) {
		buffer.append( "Unable to update database " + 
		"without a Product Indentifier.\n" );
	}

	//make sure it is not a repeat Product Identifier if we
	//are adding a new one.
	if ( __bln_new_object ) {
		int size=0;
		if ( __RTi_ImportProduct_vect != null ){
			size = __RTi_ImportProduct_vect.size();
		}
		RiversideDB_ImportProduct ip = null;
		for (int i=0; i<size; i++ ) {
			ip = (RiversideDB_ImportProduct) 
 			__RTi_ImportProduct_vect.elementAt(i);
			if ( (ip.getProduct_name()).equalsIgnoreCase( prodID ) ) {
				//we found a product with that name already
				buffer.append( "ImportProduct named: \""
				+ prodID + "\" already exists. Unable to " +
				"update database.\n");
				break;
			}
		}
	}

	//Product Group - can't be null - is uneditable
	//Product Type - drop down list, so can't be null

	//get input Time Series.
	//Files Source Directory
	String source_dir = null;
	source_dir = __files_tab_source_dir_JTextField.getText().trim();
	if( ( source_dir == null ) || ( source_dir.length() <= 0 ) ) {
		buffer.append( "Unable to update database " + 
		"without a Source Directory (Files tab).\n" );
	}

	//Files Source File(s)
	String file= null;
	file= __files_tab_source_file_JTextField.getText().trim();
	if( ( file == null ) || ( file.length() <= 0 ) ) {
		buffer.append( "Unable to update database " + 
		"without Source File(s) (Files tab).\n" );
	}

	//Destination Directory
	String dest_dir = null;
	dest_dir = __files_tab_dest_dir_JTextField.getText().trim();
	if( ( dest_dir == null ) || ( dest_dir.length() <= 0 ) ) {
		buffer.append( "Unable to update database " + 
		"without a Destination Directory (Files tab).\n" );
	}

	//Import Order - can't be null since is a JComboBox
	//Import Window - can't be null since are in a JComboBoxes
	//ARchive Permanently
	boolean blnArch = false;
	blnArch = __archive_tab_perm_JCheckBox.isSelected( );
	if ( blnArch ) {
		//If Archive Permanently is selected: Archive Directory</LI>
		//If Archive Permanently is selected: Archive File</LI>
		String arch_dir = null;
		arch_dir = __archive_tab_dir_JTextField.getText().trim();
		if( ( arch_dir == null ) || ( arch_dir.length() <= 0 ) ) {
			buffer.append( "Unable to update database " + 
			"without an Archive Directory (Archive tab) " +
			"since Pemanently Archive is selected.\n" );
		}
		String arch_file = null;
		arch_file = __archive_tab_file_JTextField.getText().trim();
		if( ( arch_file == null ) || ( arch_file.length() <= 0 ) ) {
			buffer.append( "Unable to update database " + 
			"without an Archive File (Archive tab) " +
			"since Pemanently Archive is selected.\n" );
		}
	}

	//next import date
	String gui_next_date_str= null;
	gui_next_date_str= ( __props_tab_next_import_date_JTextField.
	getText()).trim();
	boolean blnCheckDate = true;
	//make sure it is a valid date.
	if (( gui_next_date_str== null) || (gui_next_date_str.length() <= 0 )){
		//NEXT date can be null if it was null originally- ie,
		//a new ImportProduct is being defined.  It it had a date 
		//originally, it can not be null.
		Date db_date = null;
		db_date = __db_RTi_ImportProduct.getNext_import_date();
		if ( db_date == null ) {
			//ok
			blnCheckDate=false;
		}
		else {
			buffer.append( 
			"Unable to update database without a valid date for \""+
			"Next Import Date\" (Property Tab)\n" );
			blnCheckDate=false;
		}
	}
	//if the date entry is not null, make sure it is a valid date
	DateTime gui_next_date = null;
	if ( blnCheckDate ) {
		try {
			gui_next_date = DateTime.parse( gui_next_date_str);
		}
		catch (Exception e ) {	
			Message.printWarning( 2, routine, e);
			buffer.append(
			"Unable to update database without a valid date for \""+
			"Next Import Date\" (Property Tab)\n" );
		}
	}
	/*
	//check Properties on the properties tab.
	if ( __db_ImportProduct_type.equalsIgnoreCase( __type_campbellsci_str)) {
		//returns a Vector of Strings.
		//add each string to the error buffer is not null.

		//HOWEVER, we don't need to do this 
		//currently because all the required fields
		//are in JComboBoxes and can't be null!
		//Vector v = null;
		//v = checkRequired_campbellsci_props();
	}
	else {
		//verify_generic_props not required at this point
	}
	*/

	/*
	//Should not need to check now that list of ints for the
	//multiplier is created based on the unit selected.
	//automation tab
	//make sure interval for "DELAY" is valid if "Import Automated" is True

	if ( __automation_tab_automated_JCheckBox.isSelected() ) {
		String gui_delay_int_str= null;
		String gui_delay_unit_str= null;
		gui_delay_int_str=(String)
 		__automation_tab_delay_int_JComboBox.getSelected();
	
		gui_delay_unit_str=(String)
 		__automation_tab_delay_unit_JComboBox.getSelected();
	
		//concatenate the two
		String gui_delay_str= null;
		gui_delay_str= gui_delay_int_str+ gui_delay_unit_str;
		//if no interval
		if ( gui_delay_int_str.equalsIgnoreCase("0") ) {
			gui_delay_str= "";
		}
		TimeInterval timeInt = null;
		if (!gui_delay_str.equalsIgnoreCase("")) {
			//check interval
			try {
				timeInt = TimeInterval.parseInterval(
				gui_delay_str);
			}
			catch ( Exception e) {
				Message.printWarning( 2, routine, e);
				timeInt = null;
			}
		}
		if ( timeInt == null ) {
			//then the interval is invalid
			buffer.append( "Unable to update database " + 
			"without a valid Delay Interval. (Automation tab).\n" );
		}
		timeInt = null;
	}
	*/

	//Time Series Defined as Import-list to select from- should not be null
	//Make sure there are ImportConf time series defined and 
	//that every field in the worksheet is filled
	int table_size = __timeseries_tab_selTS_JWorksheet.getRowCount();
	if ( table_size <= 0 ) {
		buffer.append( "Unable to update database " + 
		"without any Time Series to Import from File defined " +
		"(Time Series tab).\n" );
	}
	else {
		//get the ImportConf Objects out of the worksheet
		Vector table_data = __timeseries_tab_selTS_JWorksheet.
		getAllData();
		//should be the same size as above
		table_size = table_data.size();

		RiversideDB_ImportConf ic = null;
		RiversideDB_MeasType mt = null;
		long mt_num = -999;
		for ( int i=0; i< table_size; i++ ) {
			ic = (RiversideDB_ImportConf) 
			table_data.elementAt( i );
			if ( ic == null ) {
				continue;
			}

			mt_num = ic.getMeasType_num();
			int mt_size = 0;
			if ( __all_MeasType_vect != null ) {
				mt_size = __all_MeasType_vect.size();
			}
			//find MeasType with matching MeasType_num
			String tsid_str= getTSIDForMeasType_num( mt_num );

			//if ( ic.getExternal_id().equalsIgnoreCase(
			//DMIUtil.MISSING_STRING ) ) {}
			if ( DMIUtil.isMissing( ic.getExternal_id() ) ) {
				//if ( __db_ImportProduct_type.
				//equalsIgnoreCase(__type_shef_str) ) {
				if ( __db_ImportProduct_type.indexOf( 
 				"SHEF" ) >= 0  ) {
					buffer.append( 
					"Unable to update database " + 
					"without a SHEF ID "+
					"for time series:\n\"" + tsid_str+ 
					"\" (Time Series tab).\n" );
				}
				else {
					buffer.append( 
					"Unable to update database " + 
					"without a Data File "+
					"for time series:\n\"" + tsid_str+ 
					"\" (Time Series tab).\n" );
				}
			}
			//on older systems, the SHEF string is "SHEF_A" and
			//on newer systems, the SHEF string is "SHEF.A"
			//so just look for part of the word- "SHEF"
			//if ( ( ! __db_ImportProduct_type.equalsIgnoreCase( 
 			//__type_shef_str) ) &&
			//( ic.getExternal_table().equalsIgnoreCase(
			//DMIUtil.MISSING_STRING ) ) ) {
			if ( (  __db_ImportProduct_type.indexOf( 
 			"SHEF" ) <0 ) && ( DMIUtil.isMissing( 
			ic.getExternal_table() ) ) ) {

			//( ic.getExternal_table().equalsIgnoreCase(
			//DMIUtil.MISSING_STRING ) ) ) {}
				buffer.append( "Unable to update database " + 
				"without an External Table/Table Identifier " + 
				"for time series:\n\"" + tsid_str+
				" (Time Series tab).\n" );
			}
			if ( DMIUtil.isMissing( ic.getExternal_field() ) ) {
				//if ( __db_ImportProduct_type.equalsIgnoreCase( 
 				//__type_shef_str) ) {
				if ( __db_ImportProduct_type.indexOf( 
 				"SHEF" ) >= 0  ) {
					buffer.append( 
					"Unable to update database " + 
					"without a SHEF PE  "+
					"for time series:\n\"" + tsid_str+ 
					"\" (Time Series tab).\n" );
				}
				else {
					buffer.append( 
					"Unable to update database " + 
					"without a Data Column  "+
					"for time series:\n\"" + tsid_str+ 
					"\" (Time Series tab).\n" );
				}
			}
			ic = null;
		}
	}

	if ( buffer.length() >0 ) {
		buffer.append( 
		"Please specify all required fields or cancel.\n" );
		JOptionPane.showMessageDialog( this, 
		buffer.toString(), "Warning", JOptionPane.WARNING_MESSAGE);

		throw new Exception ( 
		"Please specify all required fields or cancel." );
	}
	buffer = null;

}//end checkRequieredInput

/**
Creates the overall GUI panels including the JTabbedPanes which
contain information regarding the product selected. The tabs 
consist of the following topics: 
Files, Properties, Automation, Archiving, Security, Additonal Files.
*/
private void create_main_panel( ) {
	String routine = __class + ".create_main_product_info_panel";

	//create the overall panel to hold things.
 	__product_info_JPanel = new JPanel();
 	__product_info_JPanel.setLayout( new GridBagLayout() );

	//label for ID
	JLabel product_info_id_JLabel = new JLabel( "Product Identifier: " );
	product_info_id_JLabel.setToolTipText("Required: set Import Product "+
	"Identifier" );

	//JTextField for id
 	__product_info_id_JTextField = new JTextField(15);

	//label for Group
	JLabel product_info_group_JLabel = new JLabel( "Product Group: ");
	product_info_group_JLabel.setToolTipText(
	"Required: set Product Group for Import Product" );

	//items for Group JComboBox
	Vector prodGrp_vect = null;
	try {
		prodGrp_vect = 
 		__dmi.readProductGroupListForProductType( "IMPORT" );
	}
	catch (Exception e ) {
		Message.printWarning( 2, routine, e );
	}
	Vector grp_names_vect = new Vector();
	RiversideDB_ProductGroup pg = null;
	//go through vector and get list of just the Groups
	int size = 0;
	if ( prodGrp_vect != null ) {
		size = prodGrp_vect.size();
	}
	for ( int i=0; i<size; i++ ) {
		pg= (RiversideDB_ProductGroup) prodGrp_vect.elementAt(i);
		if ( pg == null ) {
			continue;
		}
		grp_names_vect.addElement( pg.getProductGroup_num() + " - " +
		pg.getName() );
		//grp_names_vect.addElement( pg.getProductGroup_num() + " - " +
		//pg.getIdentifier() );
	}

	//make JComboBox
 	__product_info_group_JComboBox = new SimpleJComboBox(grp_names_vect );
 	__product_info_group_JComboBox.setMaximumRowCount( 5 );

	//now create label for the JComboBox: "product type"
	JLabel product_info_type_JLabel = new JLabel( "Product Type:" );
	product_info_type_JLabel.setToolTipText(
	"Uneditable: set Import Product Type" );

	//create JComboBox
	//get vector of type names
	Vector type_vect = new Vector();
	RiversideDB_ImportType it = null;
	String type = null;
	for ( int i=0; i< __RTi_ImportType_vect.size(); i++ ) {
		it = (RiversideDB_ImportType) 
 		__RTi_ImportType_vect.elementAt(i);
		if ( it == null ) {
			continue;
		}
		type = it.getName() + " - " + it.getComment();
		if ( type != null ) {
			if ( type.length() > 80 ) {
				type = 
				(type.substring( 0, 80 ) + " ...").trim();
			}
			//add it to vector for drop-down list
			type_vect.addElement( type );
		}
		type = null;
		it = null;
	}
	//make ComboBox for Product Group
 	__product_info_type_JComboBox = new SimpleJComboBox( type_vect );
 	JGUIUtil.setEnabled(__product_info_type_JComboBox, false );

	//MeasLocGroup JLabel and Description
	JLabel product_info_measlocgroup_JLabel = new JLabel( "Location Group:" );
	product_info_measlocgroup_JLabel.setToolTipText(
	"Required: set Location Group for Import Product" );

	JLabel product_info_measlocgroup_desc_JLabel = new JLabel( 
	"Location Group Import Product belongs to" );

	//Vector of MeasLocGroup objects that this user has access to

	//__dmi.setDumpSQLOnExecution( true );
	Vector dbmlg_vect = null;
	try {
		dbmlg_vect = __dmi.
		readDBUserMeasLocGroupRelationListForDBUser_num(
 		__DBUser.getDBUser_num() );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		dbmlg_vect = new Vector();
	}
	//__dmi.setDumpSQLOnExecution( false );

	//vector of MeasLocGroup objects
	if ( __RTi_MeasLocGroup_vect == null ) {
		try {	
			__RTi_MeasLocGroup_vect = 
			__dmi.readMeasLocGroupList();
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e );
			//create an empty vector
			__RTi_MeasLocGroup_vect = new Vector();
		}
	}
	int size_dbmlg = dbmlg_vect.size();
	int size_mlg = __RTi_MeasLocGroup_vect.size();
	//go through vector to get MeasLocGroup objects that this user can access 
	RiversideDB_DBUserMeasLocGroupRelation dbmlg = null;
	RiversideDB_MeasLocGroup mlg = null;
	int mlg_num = -999;
	//vector to hold items that go into comboBox - will
	//have "MeasLocGroupNum - MeasLocGroup_ID - MeasLocGroup_Name"
	Vector measlocgroup_vect = new Vector();
	for ( int i=0; i<size_dbmlg; i++ ) {
		dbmlg = (RiversideDB_DBUserMeasLocGroupRelation)
		dbmlg_vect.elementAt(i);
		if ( dbmlg == null ) {
			continue;
		}
		//get MeasLocGroup num 
		mlg_num = dbmlg.getMeasLocGroup_num();
		//now use that to get MeasLocGroup object
		for ( int j=0; j<size_mlg;j++ ) {
			mlg = (RiversideDB_MeasLocGroup) 
			__RTi_MeasLocGroup_vect.elementAt(j);
			if ( mlg == null ) {
				continue;
			}
			if ( mlg.getMeasLocGroup_num() == mlg_num ) {
				//found one
				measlocgroup_vect.addElement(
				mlg.getMeasLocGroup_num() + " - " +
				mlg.getIdentifier() + " - " + 
				mlg.getName() );
				mlg= null;
				break;
			}
			mlg = null;
		}

		mlg= null;
		mlg_num=-999;
	}
	
	//make combo box for MeasLocGroup
 	__product_info_measlocgroup_JComboBox = new SimpleJComboBox( 
	measlocgroup_vect );

	//now make the check box  "active"
 	__product_info_active_JCheckBox = new JCheckBox( 
	"Import Product is Enabled" );
 	__product_info_active_JCheckBox.setToolTipText("Select " +
	"to enable Import Product" );

	//create the tabbed panes
 	__info_JTabbedPane = new JTabbedPane();


	//FILES TAB - 1st tab (case 0)
	//Creates a panel to hold all the info for the files tab 
 	__files_tab_JPanel = assemble_tab_files( );
	//add files panel to tab
 	__info_JTabbedPane.addTab( __files_tab_str, __files_tab_JPanel );

	//Time Series Tab - 2nd tab  (case 1)
	//based on the ImportConf object
	//creates panel to hold information about Time Series available
	//for Importing
 	__timeseries_tab_JPanel = assemble_tab_timeSeries( );
 	__info_JTabbedPane.addTab( __timeseries_tab_str, __timeseries_tab_JPanel );

	//PROPERTIES TAB - 3rd tab ( case 2)
	//creates a panel to hold information for the properties tab
	//properties_JPanel = assemble_tab_properties( dmi );
 	__properties_tab_JPanel = assemble_tab_properties( );
 	__info_JTabbedPane.addTab( __props_tab_str, __properties_tab_JPanel );

	//add security tab if Import Product Type is DIADVISOR or TSLOOKUP
	//Security Tab - 4th tab (case 3)
	//creates panel to hold information about Archiving
 	__security_tab_JPanel = assemble_tab_security( );
 	__info_JTabbedPane.addTab( __security_tab_str, __security_tab_JPanel );
	
	//creates panel to hold information about automation
 	__automation_tab_JPanel = assemble_tab_automation( );
 	__info_JTabbedPane.addTab( __automation_tab_str, __automation_tab_JPanel );

	//archive tab only needed for SHEF, TSLOOKUP, and CAMPBELLSCI types
	//Archiving TAB - 6th tab (case 5)
	//creates panel to hold information about Archiving
 	__archiving_tab_JPanel = assemble_tab_archiving( );
 	__info_JTabbedPane.addTab( __archive_tab_str, 
 	__archiving_tab_JPanel );

	//close panel
	JPanel close_JPanel = new JPanel();
	close_JPanel.setLayout( new GridBagLayout() );

 	__close_JButton = new SimpleJButton( 
	"Close", this );
 	__cancel_JButton = new SimpleJButton(
	"Cancel", this );
 	__apply_JButton = new SimpleJButton( 
	"Apply", this );

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

	//ASSEMBLE this portion of GUI
	try {
		int x=0;
		int y=0;

		JPanel p = new JPanel();
		p.setLayout( new GridBagLayout() );
		

		//JLabel for: "Product Type"
		JGUIUtil.addComponent(
			p, product_info_type_JLabel,
			x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );

		//JComboBox with Types - 
		JGUIUtil.addComponent(
			p, __product_info_type_JComboBox,
			++x, y, 2, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );
		
		x=0;
		//JLabel for ID
		JGUIUtil.addComponent(
			p, product_info_id_JLabel,
			x, ++y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );

		//JTextField for ID
		JGUIUtil.addComponent(
			p, __product_info_id_JTextField,
			++x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

		//JCheckBox - "is Active" or not
		JGUIUtil.addComponent(
			p, __product_info_active_JCheckBox,
			++x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );
		x=0;
		
		//JLabel for group
		JGUIUtil.addComponent(
			p, product_info_group_JLabel,
			x, ++y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );

		//JComboBox for group
		JGUIUtil.addComponent(
			p, __product_info_group_JComboBox,
			++x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

		

		x=0;
		//JLabel for: "MeasLocGroup"
		JGUIUtil.addComponent(
			p, product_info_measlocgroup_JLabel,
			x, ++y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );

		//JComboBox with MeasLocGroups 
		JGUIUtil.addComponent(
			p, __product_info_measlocgroup_JComboBox,
			++x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

		//Description for MeasLocGroups 
		JGUIUtil.addComponent(
			p, product_info_measlocgroup_desc_JLabel,
			++x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.WEST );

		//now add p to main panel and add tabbed panes
		y=0;
		JGUIUtil.addComponent( 
			 __product_info_JPanel, p,
			x, y, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.HORIZONTAL,
			GridBagConstraints.WEST );

		//add tabbed pane to main panel
		JGUIUtil.addComponent( 
 			__product_info_JPanel, __info_JTabbedPane,
			x, ++y, 1, 1, 0.0, 0.0, __insets,
			//GridBagConstraints.NONE,
			GridBagConstraints.BOTH,
			GridBagConstraints.EAST );

		//add close panel
		JGUIUtil.addComponent( 
 		__product_info_JPanel, close_JPanel,
			x, ++y, 1, 1, 1.0, 0.0, __insets,
			GridBagConstraints.HORIZONTAL,
			GridBagConstraints.CENTER );

	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e);
		Message.printWarning( 2, routine, 
		"Error laying out main product information panel." );
	}

} //end create_main_panel

/**
Finalizes and cleans up.
*/
protected void finalize() throws Throwable
{
	if ( __RTi_ImportProduct_vect != null ) {
 		__RTi_ImportProduct_vect = null;
	}
	if ( __all_MeasType_vect != null ) {
 		__all_MeasType_vect = null;
	}
	if ( __insets != null ) {
 		__insets = null;
	}
	if ( __gridbag != null ) {
 		__gridbag = null;
	}
	
	// Finalize the base RiversideDB_EditorBase_JFrame class
	super.finalize();
}

/**
Searches through Vector of MeasType objects known to this class 
(__leftList_MeasType_vect) to return the MeasType object matching 
the MeasType_num passed in.
@param mt_num MeasType_num used to find RiversideDB_MeasType object.
@param return RiversideDB_MeasType object that matches the measType_num
passed in.
*/
protected RiversideDB_MeasType getMeasTypeForMeasType_num ( long mt_num ) {
	String routine = __class + ".getMeasTypeForMeasType_num";

	int mt_size = __leftList_MeasType_vect.size();
	RiversideDB_MeasType mt = null;
	for (int k=0; k<mt_size; k++ ) {
		mt = ( RiversideDB_MeasType) __leftList_MeasType_vect.
		elementAt(k);
		if ( mt == null ) {
			continue;
		}
		if ( mt.getMeasType_num() == mt_num ) {
			return mt;
		}
	}
	return (RiversideDB_MeasType)null;
}

/**
Searches through Vector of MeasType objects known to this class 
(__leftList_MeasType_vect) to return the TSIdent, as a String, for 
MeasType object matching the MeasType_num passed in.
@param mt_num MeasType_num used to find RiversideDB_MeasType object.
@param return String representation of TSID for MeasType object that
matches the MeasType_num passed in.
*/
protected String getTSIDForMeasType_num ( long mt_num ) {
	String routine = __class + ".getMeasTypeForMeasType_num";

	int mt_size = __leftList_MeasType_vect.size();
	RiversideDB_MeasType mt = null;
	for (int k=0; k<mt_size; k++ ) {
		mt = ( RiversideDB_MeasType) __leftList_MeasType_vect.
		elementAt(k);
		if ( mt == null ) {
			continue;
		}

		if ( mt.getMeasType_num() == mt_num ) {
			break;
		}
		mt = null;
	}
	TSIdent tsid = null;
	if ( mt == null ) {
		return "";
	}
	try {
		tsid = mt.toTSIdent();
	}
	catch ( Exception e) {
		Message.printWarning( 2, routine, e );
		tsid = null;
	}
	String tsid_str= null;
	if ( tsid == null ) {
		return "";
	}
	return tsid.toString();
}

/**
Method reads the properties for the ImportProduct object passed 
in and returns the properties in a Vector of Vectors in the
format of: item at (0)="Property Name" and at (1)="Property Value".
@param RiversideDB_ImportProduct object to get properties from.
@return Vector of vectors containing the properties for the
RiversideDB_ImportProduct object passed in. Format of Vector:
item at (0)="Property Name" and at (1)="Property Value".
*/
protected Vector getVectorOfProperties( RiversideDB_ImportProduct ip ) {
	String routine = __class + ".getVectorOfProperties";

	//get the properties string for the MeasReduction object
	//for comparison to the CURRENT values set in the GUI
	String tmp_props_str= null;
	tmp_props_str= ip.getProperties();
	if ( Message.isDebugOn ){
		Message.printDebug( 4, routine, 
		"Property string for selected ImportProduct object is: \"" +
		tmp_props_str+ "\"." );
	}
	//the Properties string from the database is a list of properties, 
	//seperated by ";" and consisting of PropertyName=PropertyValue pairs:
	//For ex: "Prop1=Value1; Prop2=Value2; Prop3=Value3"

	//make Vector of Vectors to hold the properties for 
	//the ImportProduct object -each item in the Vector is another
	//vector with PropertyName as element 0 and PropertyValue as element 1
	Vector all_props_vect = new Vector();
	Vector tmp_props_vect = null;
	if ( tmp_props_str!= null ) {
		//break up string based on ";"s
		if ( tmp_props_str.indexOf( ";" ) > 0 ) {
			//break it up based on ";"
			tmp_props_vect = StringUtil.breakStringList(
			tmp_props_str, ";", StringUtil.DELIM_SKIP_BLANKS );
		}
		else  {

			//we have just 1 property-- add it to vector as is.
			tmp_props_vect = new Vector();
			tmp_props_vect.addElement( tmp_props_str);
		}
	}
	else {
		//there are no properties set, so add
		//an empty vector 
		tmp_props_vect.addElement( new Vector() );
	}

	//we have a vector containing Strings - each string in 
	//format : "Propertyname=PropertyValue"
	//Break this up further so that each string in turn is
	//converted to its own vector where
	//vector.elementAt(0) = property name and
	//vector.elmentAt(i) = property value.
	int tmp_num = 0;
	if ( tmp_props_vect != null ) {
		tmp_num = tmp_props_vect.size();
	}
	String str_with_eq = null;
	String str_name= null;
	String str_val= null;
	for ( int i=0; i<tmp_num; i++ )  {
		str_with_eq = ( String) tmp_props_vect.elementAt(i);
		//break this string up based on the equal sign
		int eq_ind = -999;
		eq_ind = str_with_eq.indexOf("=");
		if ( eq_ind > 0 ) {
			str_name = (str_with_eq.substring(0, eq_ind)).trim();
			str_val = (str_with_eq.substring(eq_ind+1)).trim();
		}
		//now make this a new vector
		Vector brokenup_vect = new Vector();
		brokenup_vect.addElement( str_name );
		brokenup_vect.addElement( str_val );
		//now add this vector to the all_props_vet
		all_props_vect.addElement( brokenup_vect );
	}
	return all_props_vect;
} //end getVectorOfProperties

/**
This method is called from the constructor to create and layout the
GUI components.  It calls the method: <i>create_main_panel</i>, which
in turn calls methods named such as: <i>assemble_tab_files()</i>,
<i>assemble_tab_timeseries</i>, etc.  These methods all create 
GUI components and put them together in a <i>GridBagLayout</i>. They
do not worry about setting correct values in the components' fields, but just 
gets the components set up.  The main components in the GUI consists of:
<ul><li>JPanel that contains general information pertinent to the ImportProduct 
object at the top of the GUI</li>
<li>a series of tabs in a JTabbedPane with fields for the ImportProduct and a
tab (the "timeseries" tab) for the related ImportConf objects, seperated by 
general topic. Tab topics include: <ul><li>automation </li> <li>file </li> 
<li>security </li><li> timeseries</li><li>properties</li><li>archive</li>
</ul> </li>
<li>a panel added at the bottom that includes the standard buttons for: 
<ul><li>apply</li><li>close</li><li>cancel</li></ul></li></ul>
At the end of <i>init_layout_gui</i>, a 
call to <i>update_gui_fields</i> is made which fills in all the 
fields of the GUI according to the ImportProduct and ImportConf objects
currently being worked with.
*/
private void init_layout_GUI( ) {
	String routine = __class + ".init_layout_GUI";
	//set up global worksheet Proplist
 	__worksheet_PropList = new PropList(
	"RTAdminAssistant.JWorksheet");
 	__worksheet_PropList.add("JWorksheet.CellFontName=Courier");
 	__worksheet_PropList.add("JWorksheet.CellFontStyle=Plain");
 	__worksheet_PropList.add("JWorksheet.CellFontSize=11");
 	__worksheet_PropList.add("JWorksheet.ColumnHeaderFontName=Arial");
 	__worksheet_PropList.add("JWorksheet.ColumnHeaderFontStyle=Plain");
 	__worksheet_PropList.add("JWorksheet.ColumnHeaderFontSize=11");
 	__worksheet_PropList.add("JWorksheet.ColumnHeaderBackground=LightGray");
 	__worksheet_PropList.add("JWorksheet.RowColumnPresent=false");
 	__worksheet_PropList.add("JWorksheet.ShowPopupMenu=true");
 	__worksheet_PropList.add("JWorksheet.SelectionMode=ExcelSelection");

	try {
 		__main_JPanel = new JPanel();
 		__main_JPanel.setLayout( __gridbag );

		//create the main gui that includes the tabbed panes with 
		//additional info about the selected product
		create_main_panel( );

		//fill in the values for all the FIELDS!
		update_GUI_fields();

		//now assemble GUI
		int vcnt = 0;

		if ( __top_group_JPanel != null ) {
			JGUIUtil.addComponent( 
 			__main_JPanel, __top_group_JPanel,
			0, vcnt, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.BOTH,
			GridBagConstraints.CENTER );

			vcnt++;
		}

		if ( __product_info_JPanel != null ) {
			JGUIUtil.addComponent( 
 			__main_JPanel, __product_info_JPanel,
			0, vcnt, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.BOTH,
			GridBagConstraints.CENTER );

			vcnt++;
		}

		if ( __close_JPanel != null ) {
			JGUIUtil.addComponent( 
 			__main_JPanel, __close_JPanel,
			0, vcnt, 1, 1, 0.0, 0.0, __insets,
			GridBagConstraints.NONE,
			GridBagConstraints.CENTER );

			vcnt++;
		}
		
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

	//set Worksheet row size
	int widths[] = null;
	if ( __timeseries_tab_selTS_JWorksheet != null ) {
		widths = ( __timeseries_tab_selTS_JWorksheet.getCellRenderer()).
		getColumnWidths();
	}
	if ( widths != null  ) {
 		__timeseries_tab_selTS_JWorksheet.setColumnWidths( widths );
	}

} //end init_layout_GUI

/**
Method updates the days listed in the day JComboBox in the 
automation tab, depending on the month selected in the month JComboBox.
*/
protected void update_automation_days() {
	String routine = __class + ".update_automation_days";
	String mo_str= null;
	String yr_str= null;
	mo_str= (String ) __automation_tab_month_JComboBox.getSelected();
	yr_str= (String ) __automation_tab_year_JComboBox.getSelected();
	int yr_int = 2000;
	int mo_int = 1;
	int num_days = 31;

	if ( mo_str.startsWith( "*" ) ){
		//have 31 days in day combo
	}
	else {
		int ind = -999;
		ind = mo_str.indexOf(" -" );
		if ( ind > 0 ) {
			mo_str= mo_str.substring( 0, ind );
		}
		//year to integer
		if ( StringUtil.isInteger( yr_str) ) {
			yr_int = StringUtil.atoi( yr_str); 
		}
		if ( StringUtil.isInteger( mo_str) ) {
			mo_int = StringUtil.atoi( mo_str); 
		}
		//get number of days for month selected
		num_days = TimeUtil.numDaysInMonth( mo_int, yr_int );
	}

	//now update the day JComboBox
 	__automation_tab_day_JComboBox.removeAllItems();
 	__automation_tab_day_JComboBox.addItem( "* - All" );
	for ( int i=1; i<=num_days; i++ ) {
 		__automation_tab_day_JComboBox.addItem( String.valueOf(i) );
		
	}
}//end update_automation_days

/**
Method updates the MeasType object found in the global vector:
_leftLeft_MeasType_vect with the MeasType object passed in, which
should be 1) marked dirty and 2) updates the object in the
global Vector of MeasType objects: __leftList_MeasType_vect.
This occurs when a MeasType with create method: <i>UNKNOWN</i> is
used to create a new ImportConf object for this ImportProduct (by
adding it to the JWorksheet of "selected" time series.  The create method
needs to be updated to <i>IMPORT</i>.  Likewise, it can be called when
the reverse scenario occurs and a time series is removed from the
list of selected time series and the MeasType object needs to be updated
to have create method set to <i>UNKNOWN</i>
@param mt RiversideDB_MeasType object to update in the Vector.
*/
protected void updateCreateMethodForMeasType( RiversideDB_MeasType mt ) {
	//find this MeasType in the Vector.
	long mt_num = -999;
	mt_num = mt.getMeasType_num();
	int size = 0;
	size = __leftList_MeasType_vect.size();
	RiversideDB_MeasType orig_mt = null;
	for ( int i=0; i<size; i++ ) {
		orig_mt = (RiversideDB_MeasType)
 		__leftList_MeasType_vect.elementAt(i);
		if (orig_mt == null ) {
			continue;
		}
		if ( orig_mt.getMeasType_num() == mt_num ) {
			//found matching MeasType.
			//replace this one with the new MeasType passed in
 			__leftList_MeasType_vect.set( i, mt );
			break;
		}
	}
}


/**
This method: <ul>
<li>makes a confirmation message to verify that the user wants to save the 
changes (and lists out all the changes from the <i>__dirty_vect</i>) <b>if</b>
a new ImportProduct was <b>not</b> created (if a new ImportProduct was created, 
we do not want to re-confirm every change)</li>
<li>writes the <i>__gui</i> version of the objects to the database</li>
<li>if the objects were successfully written to the database, the 
<i>__gui</i> objects are marked as <b>not</b> dirty 
( <i>setDirty(false)</i></li>
<li><i>__dirty_vect</i> is cleared out</li>
<li>if we created a new ImportProduct object (<i>__bln_new_object == true</i>),
add a new node on the JTree in the main application  - <b>or</b> -  update the 
existing ImportProduct node on the JTree with the new changes.</li>
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

	//see if __gui_RTi_ImportConf_vect has any dirty elements
	int ic_size = 0;
	boolean dirty_importconf = false;
	RiversideDB_ImportConf ic = null;
	if ( __gui_RTi_ImportConf_vect != null ) {
		ic_size = __gui_RTi_ImportConf_vect.size();
	}
	for ( int i=0; i<ic_size; i++ ) {
		ic = ( RiversideDB_ImportConf ) 
 		__gui_RTi_ImportConf_vect.elementAt(i);
		if ( ic == null ) {
			continue;
		}
		if ( ic.isDirty() ) {
			dirty_importconf = true;
			break;
		}
		ic = null;
	}

	boolean dirty_meastype = false; // this can't be true if
					//dirty_importconf isn't true too
	int mt_size = 0;
	if ( dirty_importconf ) {
		//see if __leftList_MeasType_vect has any dirty elements
		//This would happen if a MeasType was moved into the
		//list of selected Time Series that originally had 
		//create method of UNKNOWN
		RiversideDB_MeasType mt = null;
		if ( __leftList_MeasType_vect != null ) {
			mt_size = __leftList_MeasType_vect.size();
		}
		for ( int m=0; m<mt_size;m++ ) {
			mt = (RiversideDB_MeasType) 
 			__leftList_MeasType_vect.elementAt(m);
			if ( mt == null ) {
				continue;
			}
			if ( mt.isDirty() ) {
				dirty_meastype = true;
				mt = null;
				break;
			}
			mt = null;
		}
	}

	//if we are running in cautious mode and if we 
	//are changing an already existing object ( not a completely new one),
	//then prompt the user before writing to the database
	if( ( __cautious_mode ) && ( ! __bln_new_object ) ) {
		if (( __gui_RTi_ImportProduct.isDirty()) ||
		( dirty_importconf) ) {
			//write out a confirmation message.

			int x = new ResponseJDialog( this, 
			"Confirm Changes to be saved to database", 
			"Confirm Changes:\n" +b.toString() ,
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

	//first write any dirty MeasType objects since if we have
	//any that are dirty, it means we changed their create method 
	//from UNKNOWN to IMPORT (or vice versa).
	if ( dirty_meastype ) {
		RiversideDB_MeasType mt = null;
		//go through and write any dirty MeasType objects
		//back to the database.
		for ( int m=0; m<mt_size; m++ ) {
			mt = (RiversideDB_MeasType) 
 			__leftList_MeasType_vect.
			elementAt(m);
			if ( mt == null ) {	
				continue;
			}
			if ( mt.isDirty() ) {
				try {
 					__dmi.writeMeasType( mt );
				}
				catch ( Exception e ){
					Message.printWarning( 2, routine, e);
					mt.setDirty( true );
				}

				//update MeasType node on JTree
				//call with same MeasType object since
				//it uses the tsid from the first one
				//to locate the node to change and the
				//data from the second to add the data.
 				updateMeasTypeNode( mt, mt );

				mt.setDirty( false );
			}
			mt = null;
		}
	}

	if ( __gui_RTi_ImportProduct.isDirty() ) {
		//update title - identifier may have changed for 
		//a current ImportProduct Or, if we are adding a new
		//ImportProduct, need to update title from "New xx" to
		//use the identifier.
		this.setTitle( "RiverTrak® Assistant - " +
		"Import Product - " +
 		__product_info_id_JTextField.getText().trim() );

		if ( Message.isDebugOn ) {
			Message.printDebug( 2, routine,
			"Trying to write ImportProduct with DBUser = \"" + 
 			__gui_RTi_ImportProduct.getDBUser_num() + "\"," +
			"DBGroup= \"" + __gui_RTi_ImportProduct.getDBGroup_num()+
			"\", and DBPermissions= \"" + 
 			__gui_RTi_ImportProduct.getDBPermissions() + "\"." );
		}
		try {
			//write product
 			__dmi.writeImportProduct( __gui_RTi_ImportProduct );
			Message.printStatus( 15, routine,
			"ImportProduct written to database." );
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e );
			Message.printWarning( 1, routine,
			"Error writing ImportProduct to database!", this );

 			__gui_RTi_ImportProduct.setDirty( true );

			throw new Exception( 
			"Error writing ImportProduct to database" );
		}

		//update node on tree to reflect any changes
		String gui_name = null;
		gui_name = __product_info_id_JTextField.getText().trim();

		String gui_grp = null;
		gui_grp = (String) __product_info_group_JComboBox.  
		getSelected();
 
		//format: "1 - Imports" - just need the 
		//Identifier(no number)
		int ind = -999;
		ind = gui_grp.indexOf( " -");
		if ( ind > 0 ) {
			gui_grp = gui_grp.substring( ind +2 ).trim();
		}

		if ( __bln_new_object ) {
			//now if we are ADDING a new ImportProduct,
			//tell the main application so a new node can be 
			//added to the Import JTree
 			addImportProductNode( 	gui_grp,
 						gui_name,
 						__gui_RTi_ImportProduct );
		
			//set Flag to false so hitting "Apply" repeatedly 
			//won't keep on adding nodes to the JTree
 			__bln_new_object = false;
		}
		else { //we are updating a already present node
 			updateImportProductNode( __gui_RTi_ImportProduct,
 						 __db_tree_node_str);
		}
		
		//since database has been updated, update dirty flag
 		__gui_RTi_ImportProduct.setDirty( false );
	}
	//also need to update ImportConf objects.  
	//See if there has been an alteration to the original 
	//vector of ImportConf objects. Do not try and delete though if we 
	//are adding a completely new importProduct
	if ( dirty_importconf ) {
		if ( ! __bln_new_object ) {
			//delete all the ImportConf items in the Database
			//using the current product number:_db_ImportProduct_num
			Message.printDebug( 5, routine, 
			"ImportConf objects will be deleted from "+
			"the database for ImportProduct_num: " + 
 			__db_ImportProduct_num );
			try {
 				__dmi.deleteImportConfForImportProduct_num(
				(int) __db_ImportProduct_num );
			}
			catch ( Exception e ) {
				Message.printWarning( 2, routine, e );
				Message.printWarning( 2, routine,
				"Unable to delete the current ImportConf " +
				"objects from the database." );
			}
		}

		ic = null;
		for ( int i=0; i<ic_size; i++ ) {
			ic = ( RiversideDB_ImportConf ) 
 			__gui_RTi_ImportConf_vect.elementAt(i);
			if ( ic == null ) {
				continue;
			}

			//write to database
			Message.printStatus( 15, routine,
			"Updating the ImportConf object with " +
			"with ImportProduct_num = " + __db_ImportProduct_num + 
			" and MeasType_num: " + ic.getMeasType_num() + 
			" to the database. " );
			try {
 				__dmi.writeImportConf( ic );
				ic.setDirty( false );
			}
			catch ( Exception e ) {
				Message.printWarning( 2, routine, e );
				Message.printWarning( 2, routine,
				"Unable to write the updated ImportConf " +
				"object with MeasType_num " +
				ic.getMeasType_num() + " to the database. " );

				ic.setDirty( true );
			}
		}//end loop
	} 

	//empty out dirty vector
 	__dirty_vect.clear();
	
} //end update_database

/**
This method:<ul>
<li><b>creates the <i>__gui</i> versions of the objects!</b> This includes: <ul><li>__gui_RTi_ImportProduct</li><li>__gui_RTi_ImportConf_vect</li></ul> The 
<i>__gui</i> versions are created in this method by copying the <i>__db</i> 
versions (which were originally created and set up in the constructor)</li>
<li>calls the <i>verify_xxx</i> methods. These include:<ul>
<li><i>verify_top_fields()</i></li>
<li><i>verify_archive_tab()</i></li>
<li><i>verify_automation_tab()</i></li>
<li><i>verify_files_tab()</i></li>
<li><i>verify_properties_tab()</i></li>
<li><i>verify_timeseries_tab()</i></li> </ul>
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
@param Exception thrown if error encountered.
*/
protected void update_RiversideDB_objects( ) throws Exception {
	String routine = __class + ".update_RiversideDB_objects";

	//create the "_gui" version of the objects to manipulate
 	__gui_RTi_ImportProduct = new RiversideDB_ImportProduct( 
 	__db_RTi_ImportProduct );

	int s = 0;
	if ( __db_RTi_ImportConf_vect != null ){
		s = __db_RTi_ImportConf_vect.size();
	}

	//clear out the GUI version of the Vector and refill it.
 	__gui_RTi_ImportConf_vect.clear();
	for ( int i=0; i<s; i++ ) {
 		__gui_RTi_ImportConf_vect.addElement(
 		__db_RTi_ImportConf_vect.elementAt(i) );
	}

	boolean blnContinue = true;

	//get the Top values and then verify the tabs
	try {
		verify_top_fields();
	}
	catch (Exception e) {
		Message.printWarning( 2, routine, e);
		blnContinue = false;
	}
	if ( blnContinue ) {
		verify_tab_info();
		//this will get the time series tab too.
	}

} //end update_RiversideDB_objects

/**
This method: <ul><li>determines if the current user, already known to the DMI,
has write permissions.  If the user does not have write permissions, the 
<b>apply</b> and <b>close</b> buttons are disabled, leaving only the 
<b>cancel</b> button enabled.</li><li>Fills in all the GUI fields according
to the data in the ImportProduct and related ImportConf objects.</li></ul>
*/
protected void update_GUI_fields( ) {
	String routine = __class + ".update_GUI_fields";

	//create rest of GUI using specific info
	//from the available product chosen.
	if ( Message.isDebugOn ) {
		Message.printDebug( 25, routine,
		"Selected product from the list of available " +
		"products: \"" + __product_info_id_JTextField.getText() );
	}

	//check to see which buttons should be enabled
	if ( ! __canWriteImportProduct ) {
 		JGUIUtil.setEnabled(__apply_JButton, false );	
 		JGUIUtil.setEnabled(__close_JButton, false );	
	}

	//change title in titled border to reflect new product selected
	//_titled_border.setTitle( __product_info_id_JTextField.getText() );
 	__product_info_JPanel.repaint( 200 );
	///////// ID //////////////////
	String db_id = null;
	db_id = __db_RTi_ImportProduct.getProduct_name();
 	__product_info_id_JTextField.setText( db_id );

	///////// GROUP //////////////////
	//String db_grp = null;
	//db_grp = __db_RTi_ImportProduct.getProduct_group();
	int db_grp_num = -999;
	db_grp_num = __db_RTi_ImportProduct.getProductGroup_num();
	if ( Message.isDebugOn ) {
		Message.printDebug( 4, routine,
		"ProductGroup_num for ImportProduct is:\"" +db_grp_num+ "\"." );
	}
	try {
		JGUIUtil.selectTokenMatches(
 		__product_info_group_JComboBox, true, " - ", 0, 0,
		String.valueOf( db_grp_num), "" );
	}
	catch ( Exception e ) {
		Message.printWarning( 20, routine, e);
	}

	///////// TYPE //////////////////
	//The Type Combo box has the type, followed by a brief
	//description.  For instance, one item might be:
	//"TSLOOKUP - File contianing time series data", where
	//the Type itself is the "TSLOOKUP"

	//Find the RiversideDB_ImportType object that matches...
	//Go through list of ImportType objects
	//No "none" option for type
	String db_type = null;
	db_type = __db_ImportProduct_type;
	if ( Message.isDebugOn ) {
		Message.printDebug( 4, routine,
		"Type for ImportProduct is:\"" + db_type + "\"." );
	}
	if ( db_type != null ) {
		//try and select the specific type from the list of types...
		try {
			JGUIUtil.selectTokenMatches(
 			__product_info_type_JComboBox, true,
			" - ", 0, 0, db_type, "" );
		}
		catch (	Exception e ) {
			Message.printWarning( 25, routine, e );
		}
	}

	//STORMWATCH and DIADVISOR
	//Change tab name: "files" to "database" and Remove tab: archive tab
	// LT 2005-02-01 Do not remove tabs anymore. From verion 03.00.00 all
	// the tabs should be present according to MT.
//	if( ( db_type.equalsIgnoreCase( __type_stormwatch_str) )  ||
//	( db_type.equalsIgnoreCase( __type_diadvisor_str) ) ) {
		//then change tab name: "files" to "database"
 		// __info_JTabbedPane.setTitleAt( 0, __files_tab_db_str );
		//remove archive tab
// 		__info_JTabbedPane.remove( __archiving_tab_JPanel );
//		validate();
//		pack();
//	}

	//CAMBELLSCI
	/*
	//should be already taken care of in worksheet
	if ( db_type.equalsIgnoreCase( __type_campbellsci_str)) {
		//On Time Series tab:
		//-change label: "External Table" to "Table Identifier"
		//-change label: "External Field" to "Data Column"
		//-change label: "External ID" to "Data File"
	}
	*/
		
	//remove security tab unless type: DIADVISOR or TSLOOKUP
	// LT 2005-02-01 Do not remove tabs anymore. From verion 03.00.00 all
	// the tabs should be present according to MT.
//	if ( ( db_type.equalsIgnoreCase( __type_diadvisor_str) ) ||
//	( db_type.equalsIgnoreCase( __type_tslookup_str) ) ) { 
//		//leave security tab in place
//	}
//	else {//remove it
//		__info_JTabbedPane.remove( __security_tab_JPanel );
//		validate();
//		pack();
//	}

	////////MeasLocGroup
	int measlocgrp_num = -999;
	measlocgrp_num = __db_RTi_ImportProduct.getMeasLocGroup_num();
	//might be -999 if creating a new ImportProduct
	if ( Message.isDebugOn ) {
		Message.printDebug( 4, routine,
		"MeasLocGroup_num for ImportProduct is:\"" + measlocgrp_num + 
		"\"." );
	}
	if ( measlocgrp_num > 0 ) {
		//try and select the specific type from the list of types...
		try {
			JGUIUtil.selectTokenMatches(
 			__product_info_measlocgroup_JComboBox, true,
			" - ", 0, 0, String.valueOf(measlocgrp_num), "" );
		}
		catch (	Exception e ) {
			Message.printWarning( 25, routine, e );
		}
	}	
	//else- let it default to the first item in the list
	 
	///////////////// isActive /////////////////
	//get the state for the isActive CheckBox
	String db_isActive_str = null;
	boolean isActive_bool = false;
	db_isActive_str = __db_RTi_ImportProduct.getIsActive();
	if ( Message.isDebugOn ) {
		Message.printDebug( 25, routine,
		"isActive parameter for Import Product is: \"" + 
		db_isActive_str + "\"." );
	}

	//make sure it is Y or N
	if ( db_isActive_str.equalsIgnoreCase( "Y" ) ) {
		isActive_bool = true;
	}
	else {
		//set it to No
		isActive_bool = false;
	}
	//now set the button
 	__product_info_active_JCheckBox.setSelected( isActive_bool );

	//////////////// FILES TAB /////////////////
	if ( __files_tab_JPanel != null ) {
		update_GUI_fields_filesTab();
	}	

	if ( __properties_tab_JPanel != null ) {
		update_GUI_fields_propertiesTab();
	}
	//////////////// AUTOMATION TAB /////////////////
	if ( __automation_tab_JPanel != null ) {
		update_GUI_fields_automationTab();
	}
	//////////////// ARCHIVING TAB /////////////////
	if ( __archiving_tab_JPanel != null ) {
		update_GUI_fields_archiveTab();
	}
	////////////// SECURITY TAB /////////////////
	if ( __security_tab_JPanel != null ) {
		update_GUI_fields_securityTab();
	}

	//////// TIME SERIES /////////////
	//JWorksheet set up at GUI creation and then is
	//updated in Worksheet by the move_right and clear_selected buttons

	validate();
} //end update_GUI_fields

/**
Fills in the components of the archive tab in the GUI with the 
parameters for the current RiversideDB_Import object
*/
protected void update_GUI_fields_archiveTab( ) {
	String routine = __class + ".update_GUI_fields_archiveTab";
	String perm = null;
	boolean isPerm = false;
	try {
		perm = __db_RTi_ImportProduct.getDoArchive();
		if ( Message.isDebugOn ) {
			Message.printDebug( 25, routine,
			"Value for Permanently Archive Data is: \"" + 
			perm + "\"." );
		}
	} 
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}

	if ( perm.equalsIgnoreCase( "Y" ) ) {
		isPerm = true;
	}
	//else it remains false...

 	__archive_tab_perm_JCheckBox.setSelected( isPerm );

	//if "isPerm" is true, then we need to have the 
	//dir and file names.
	//IF "isPerm" is FALSE (unchecked) , gray out the fields

	String arch_dir = null;
	try {
		arch_dir = __db_RTi_ImportProduct.getArchive_dir();
		if ( Message.isDebugOn ) {
			Message.printDebug( 25, routine,
			"Value for Archive Directory is: \"" + 
			arch_dir + "\"." );
		}
	} 
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}
	
	//For defining a New ImportProduct
	if (( arch_dir == null ) || ( DMIUtil.isMissing(arch_dir) ) ) {
		arch_dir = DMIUtil.MISSING_STRING;
	}

 	__archive_tab_dir_JTextField.setText( arch_dir );

	String arch_file = null;
	try {
		arch_file = __db_RTi_ImportProduct.getArchive_file();
		if ( Message.isDebugOn ) {
			Message.printDebug( 25, routine,
			"Value for Archive File is: \"" + 
			arch_file + "\"." );
		}
	} 
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}

	//For defining a New ImportProduct
	if (( arch_file == null ) || ( DMIUtil.isMissing(arch_file) ) ) {
		arch_file = DMIUtil.MISSING_STRING;
	}

 	__archive_tab_file_JTextField.setText( arch_file );

	if ( isPerm ) {
		//must have fields for the dir and file
 		JGUIUtil.setEnabled(__archive_tab_dir_JTextField, true );
 		JGUIUtil.setEnabled(__archive_tab_file_JTextField, true );
 		JGUIUtil.setEnabled(__archive_tab_wild_JComboBox, true );
	}
	else {
 		//don't let user edit them
 		JGUIUtil.setEnabled(__archive_tab_dir_JTextField, false );
 		JGUIUtil.setEnabled(__archive_tab_file_JTextField, false );
 		JGUIUtil.setEnabled(__archive_tab_wild_JComboBox, false );
	}
} // end update_GUI_fields_archiveTab

/**
Fills in the components of the automation tab in the GUI with the 
parameters for the current RiversideDB_Import object
*/
protected void update_GUI_fields_automationTab( ) {
	String routine = __class + ".update_GUI_fields_automationTab";

	String automated = null;
	boolean isAuto = true;
	try {
		automated = __db_RTi_ImportProduct.getIsAutomated();
		if ( Message.isDebugOn ) {
			Message.printDebug( 25, routine,
			"Value for isAutomated is: \"" + 
			automated + "\"." );
		}
	} 
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}

	//now we have the Y or N string - change to boolean...
	if ( automated.equalsIgnoreCase( "N" ) ) {
		isAuto = false;
		//if isAutomated is false, the other fields should be disabled.
	}
	//else Leave as is: isAuto = true
	
	//set checkBox
 	__automation_tab_automated_JCheckBox.setSelected( isAuto );

	//all the other fields should be active.
	//If the checkbox is checked or not, make sure
	//the year, month, day, etc text fields are active.
 	JGUIUtil.setEnabled(__automation_tab_year_JComboBox, isAuto );
 	JGUIUtil.setEnabled(__automation_tab_month_JComboBox, isAuto );
 	JGUIUtil.setEnabled(__automation_tab_day_JComboBox, isAuto );
 	JGUIUtil.setEnabled(__automation_tab_hour_JComboBox, isAuto );
 	JGUIUtil.setEnabled(__automation_tab_minute_JComboBox, isAuto );
 	JGUIUtil.setEnabled(__automation_tab_second_JComboBox, isAuto );
	/*
 	JGUIUtil.setEnabled(__automation_tab_weekday_JComboBox, isAuto );
	*/
 	JGUIUtil.setEnabled(__automation_tab_delay_int_JComboBox, isAuto );
 	JGUIUtil.setEnabled(__automation_tab_delay_unit_JComboBox, isAuto );

	String interv = null;
	//since these two checkboxes are in a ButtonGroup,
	//only 1 can be checked at a time...
	boolean isInterval = true;

	try {
		interv = __db_RTi_ImportProduct.getIsInterval();
		if ( Message.isDebugOn ) {
			Message.printDebug( 25, routine,
			"Value for isInteval is: \"" + 
			interv + "\"." );
		}
		if ( interv.equalsIgnoreCase( "Y" ) ){ 
			isInterval = true;
		}
		else {
			isInterval = false;
		}
	} 
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}
 	__automation_tab_every_interval_JRadioButton.setSelected( isInterval );
 	__automation_tab_at_time_JRadioButton.setSelected( !isInterval );
	
	String yr = null;
	try {
		yr = __db_RTi_ImportProduct.getImport_year();
		if ( Message.isDebugOn ) {
			Message.printDebug( 25, routine,
			"Value for Import year is: \"" + 
			yr + "\"." );
		}
	} 
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}
	//For defining a New ImportProduct
	if (( yr == null ) || ( DMIUtil.isMissing(yr) ) ) {
		yr = "*";
	}

	//No choice for year- only choice is "* - All"

	String mo = null;

	try {
		mo = __db_RTi_ImportProduct.getImport_month();
		if ( Message.isDebugOn ) {
			Message.printDebug( 25, routine,
			"Value for Import month is: \"" + 
			mo + "\"." );
		}
	} 
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}

	//For defining a New ImportProduct
	if (( mo == null ) || ( DMIUtil.isMissing(mo) ) ) {
		mo = "*";
	}

	try {
		JGUIUtil.selectTokenMatches(
 		__automation_tab_month_JComboBox, true,
		" - ", 0, 0, mo, "" );
	}
	catch (Exception e ) {
		Message.printWarning( 25, routine, e );
	}
	//update days vector to have correct number of days for
	//month selected
	update_automation_days();

	String day = null;
	try {
		day = __db_RTi_ImportProduct.getImport_day();
		if ( Message.isDebugOn ) {
			Message.printDebug( 25, routine,
			"Value for Import day is: \"" + 
			day + "\"." );
		}
	} 
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}

	//For defining a New ImportProduct
	if (( day == null ) || ( DMIUtil.isMissing(day ) ) ) {
		day = "*";
	}
	if ( day.equalsIgnoreCase("*" ) ) {
		try {
			JGUIUtil.selectTokenMatches(
 			__automation_tab_day_JComboBox, true,
			" - ", 0, 0, day, "" );
		}
		catch (Exception e ) {
			Message.printWarning( 25, routine, e );
		}
	}
	else {
 		__automation_tab_day_JComboBox.setSelectedItem( day);
	}

	String hr = null;
	try {
		hr = __db_RTi_ImportProduct.getImport_hour();
		if ( Message.isDebugOn ) {
			Message.printDebug( 25, routine,
			"Value for Import hour is: \"" + 
			hr + "\"." );
		}
	} 
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}

	//For defining a New ImportProduct
	if (( hr == null ) || ( DMIUtil.isMissing(hr) ) ) {
		hr = "*";
	}

 	__automation_tab_hour_JComboBox.setSelectedItem( hr );

	String min = null;
	try {
		min = __db_RTi_ImportProduct.getImport_minute();
		if ( Message.isDebugOn ) {
			Message.printDebug( 25, routine,
			"Value for Import minute is: \"" + 
			min + "\"." );
		}
	} 
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}

	//For defining a New ImportProduct
	if (( min == null ) || ( DMIUtil.isMissing(min) ) ) {
		min = "15";
	}

 	__automation_tab_minute_JComboBox.setSelectedItem( min );

	String sec = null;
	try {
		sec = __db_RTi_ImportProduct.getImport_second();
		if ( Message.isDebugOn ) {
			Message.printDebug( 25, routine,
			"Value for Import second is: \"" + 
			sec + "\"." );
		}
	} 
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}

	//For defining a New ImportProduct
	if (( sec == null ) || ( DMIUtil.isMissing(sec ) ) ) {
		sec = "*";
	}

 	__automation_tab_second_JComboBox.setSelectedItem( sec );

	String weekday = null;
	try {
		weekday = __db_RTi_ImportProduct.getImport_weekday();
		if ( Message.isDebugOn ) {
			Message.printDebug( 25, routine,
			"Value for Import weekday is: \"" + 
			weekday + "\"." );
		}
	} 
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}

	//For defining a New ImportProduct
	if (( weekday == null ) || ( DMIUtil.isMissing(weekday) ) ) {
		weekday = "*";
	}	

	try {
		JGUIUtil.selectTokenMatches(
 		__automation_tab_weekday_JComboBox, true,
		" - ", 0, 0, mo, "" );
	}
	catch (Exception e ) {
		Message.printWarning( 25, routine, e );
	}

	String db_delay = null;
	try {
		db_delay = __db_RTi_ImportProduct.getImport_delay();
		if ( Message.isDebugOn ) {
			Message.printDebug( 25, routine,
			"Value for Import delay is: \"" + 
			db_delay + "\"." );
		}
	} 
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}

	//For defining a New ImportProduct
	String db_int_str= "0";
	String db_unit_str= "DAY";
	//For defining a New ImportProduct
	if (( db_delay == null ) || ( DMIUtil.isMissing(db_delay))) {
		db_int_str= "0";
		db_unit_str= "HOUR";
	}
	else {
		String s = null;
		if (db_delay.length() > 2 ) {
			s = db_delay.substring(0, 2);
			if ( StringUtil.isInteger( s ) ) {
				db_int_str= s;
				db_unit_str= db_delay.substring(2);
			}
			else {
				db_int_str= db_delay.substring( 0,1 );
				db_unit_str= db_delay.substring(1);
			}
		}
		else {
			db_int_str= "0";
			db_unit_str= "HOUR";
		}
	}

 	__automation_tab_delay_unit_JComboBox.setSelectedItem( db_unit_str);

	//update the list of choices in the Multiplier comboBox and then
	//select the correct item.
	int [] arrMults = null;
	//allow all multiplier intervals, not just evenly divisible ones
	//and include zero
	arrMults = TimeInterval.multipliersForIntervalBase(
	db_unit_str, false, true );

 	__automation_tab_delay_int_JComboBox.removeAllItems( );
	for ( int i=0; i<arrMults.length; i++ ) {
 		__automation_tab_delay_int_JComboBox.addItem(
		String.valueOf( arrMults[i] ) );
	}

	//select the item to match value in database
 	__automation_tab_delay_int_JComboBox.setSelectedItem( db_int_str);

} //end update_GUI_fields_automationTab

/**
Fills in the components of the files tab in the GUI with the 
parameters for the current RiversideDB_Import object
*/
protected void update_GUI_fields_filesTab( ) {
	String routine = __class + ".update_GUI_fields_filesTab";

	//get current source dir	
	String db_source_dir= null;
	db_source_dir = __db_RTi_ImportProduct.getSource_URL_base();
	if ( Message.isDebugOn ) {
		Message.printDebug( 25, routine,
		"Source directory is: \"" + db_source_dir + "\"." );
	}

	//For defining a New ImportProduct
 	__files_tab_source_dir_JTextField.setText( db_source_dir );

	//get current source FILE	
	String db_source_file = null;
	db_source_file = __db_RTi_ImportProduct.getSource_URL_file();
	if ( Message.isDebugOn ) {
		Message.printDebug( 25, routine,
		"Source file is: \"" + db_source_file + "\"." );
	}
	if( (db_source_file==null) || ( db_source_file.equalsIgnoreCase("")) ) {
		db_source_file = "*";
	}

 	__files_tab_source_file_JTextField.setText( db_source_file );

	//get current destination DIRECTORY
	String db_dest_dir = null;
	db_dest_dir = __db_RTi_ImportProduct.getDestination_dir();
	if ( Message.isDebugOn ) {
		Message.printDebug( 25, routine,
		"Destination directory is: \"" + db_dest_dir + "\"." );
	}
 	__files_tab_dest_dir_JTextField.setText( db_dest_dir );

	//get current destination FILE
	String db_dest_file = null;
	db_dest_file = __db_RTi_ImportProduct.getDestination_file();
	if ( Message.isDebugOn ) {
		Message.printDebug( 25, routine,
		"Destination file is: \"" + db_dest_file + "\"." );
	}

 	__files_tab_dest_file_JTextField.setText( db_dest_file );
}//end update_GUI_fields_filesTab

/**
Fills in the components of the properties tab in the GUI 
with the parameters for the current RiversideDB_Import object
*/
protected void update_GUI_fields_propertiesTab( ) {
	String routine = __class + ".update_GUI_fields_propertiesTab";

	//get the current import order - REQUIRED FIELD
	long db_imp_order_long = -999;
	db_imp_order_long = __db_RTi_ImportProduct.getImport_order();
	if ( Message.isDebugOn ) {
		Message.printDebug( 25, routine,
		"Import Order = \"" + 
		String.valueOf( db_imp_order_long )+ "\"." );
	}
	//default to 2
	if ( ( db_imp_order_long < 0 ) || 
	( DMIUtil.isMissing(db_imp_order_long) ) ) {
 		__props_tab_import_order_JComboBox.setSelectedItem( "2" );
	}
	else {
 		__props_tab_import_order_JComboBox.setSelectedItem( 
		String.valueOf( db_imp_order_long ) );
	}

	//get the current import Window 
	//Default to 2DAY
	String db_imp_wind = null;
	try {
		db_imp_wind = __db_RTi_ImportProduct.getImport_window();
		if ( Message.isDebugOn ) {
			Message.printDebug( 25, routine,
			"Import Window = \"" + db_imp_wind + "\"." );
		}
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}
	//divide into 2 since this is in 2 comboboxes
	//ASSUMPTION- the integer is either 1 or 2 chars long

	String int_str= "2";
	String unit_str= "DAY";
	//For defining a New ImportProduct
	if (( db_imp_wind == null ) || ( DMIUtil.isMissing(db_imp_wind))) {
		int_str= "2";
		unit_str= "DAY";
	}
	else {
		String s = null;
		if (db_imp_wind.length() > 2 ) {
			s = db_imp_wind.substring(0, 2);
			if ( StringUtil.isInteger( s ) ) {
				int_str= s;
				unit_str= db_imp_wind.substring(2);
			}
			else {
				int_str= db_imp_wind.substring( 0,1 );
				unit_str= db_imp_wind.substring(1);
			}
		}
		else {
			int_str= "0";
			unit_str= "DAY";
		}
	}

 	__props_tab_import_window_unit_JComboBox.setSelectedItem( unit_str);

	//update the list of choices in the Multiplier comboBox and then
	//select the correct item.
	int [] arrMults = null;
	arrMults = TimeInterval.multipliersForIntervalBase(
	unit_str, true, true );

 	__props_tab_import_window_int_JComboBox.removeAllItems( );
	for ( int i=0; i<arrMults.length; i++ ) {
 		__props_tab_import_window_int_JComboBox.addItem(
		String.valueOf( arrMults[i] ) );
	}

	//select the item to match value in database
 	__props_tab_import_window_int_JComboBox.setSelectedItem( int_str);

	//current value for last import date is uneditable 
	Date d = null;
	try {
		d = __db_RTi_ImportProduct.getLast_import_date();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}
	if ( d != null ) {
		if ( Message.isDebugOn ) {
			Message.printDebug( 25, routine,
			"Last Import Date = \"" + d.toString() + "\"." );
		}
		//now try and change it to a DateTime
		DateTime last_rti_date = null;
		last_rti_date = new DateTime ( d );
		last_rti_date.setPrecision( DateTime.PRECISION_SECOND );
	
		StringBuffer date_buf = new StringBuffer();
		date_buf.append( (String.valueOf(last_rti_date.getYear())) );
		date_buf.append( "-" );
		date_buf.append(StringUtil.formatString(
			last_rti_date.getMonth(), "%02d") );
		date_buf.append( "-" );
		date_buf.append(StringUtil.formatString( 
			last_rti_date.getDay(), "%02d") );
		date_buf.append( " " );
		date_buf.append(StringUtil.formatString( 
			last_rti_date.getHour(), "%02d" ) );
		date_buf.append( ":" );
		date_buf.append(StringUtil.formatString( 
			last_rti_date.getMinute(), "%02d" ) );
		date_buf.append( ":" );
		date_buf.append(StringUtil.formatString( 
			last_rti_date.getSecond(), "%02d" ) );
		date_buf.append("\n");

		if ( date_buf != null ) {
			if ( __props_tab_last_import_date_JTextField != null ) {
 				__props_tab_last_import_date_JTextField.setText( 
	 			date_buf.toString() );
			}
		}
	
		d = null;
		last_rti_date = null;
		date_buf = null;
	}
	//For defining a New ImportProduct
	else {
		if ( __props_tab_last_import_date_JTextField != null ) {
 			__props_tab_last_import_date_JTextField.setText( "" );
		}
	}

	//get current value for next import date.
	try {
		d = __db_RTi_ImportProduct.getNext_import_date();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}

	if ( d != null ) {
		if ( Message.isDebugOn ) {
			Message.printDebug( 25, routine,
			"Next Import Date = \"" + d.toString() + "\"." );
		}
		//now try and change it to a DateTime
		DateTime next_rti_date = null;
		next_rti_date = new DateTime ( d );
		next_rti_date.setPrecision( DateTime.PRECISION_SECOND );
	
		StringBuffer date_buf = new StringBuffer();
		date_buf.append( (String.valueOf(next_rti_date.getYear())) );
		date_buf.append( "-" );
		date_buf.append(StringUtil.formatString(
			next_rti_date.getMonth(), "%02d") );
		date_buf.append( "-" );
		date_buf.append(StringUtil.formatString( 
			next_rti_date.getDay(), "%02d") );
		date_buf.append( " " );
		date_buf.append(StringUtil.formatString( 
			next_rti_date.getHour(), "%02d" ) );
		date_buf.append( ":" );
		date_buf.append(StringUtil.formatString( 
			next_rti_date.getMinute(), "%02d" ) );
		date_buf.append( ":" );
		date_buf.append(StringUtil.formatString( 
			next_rti_date.getSecond(), "%02d" ) );
		date_buf.append("\n");
		if ( date_buf != null ) {
			if ( __props_tab_next_import_date_JTextField != null ) {
 			__props_tab_next_import_date_JTextField.setText( 
		 		date_buf.toString() );
			}
		}
		d = null;
		date_buf= null;
		next_rti_date = null;
	}
	//For defining a New ImportProduct
	else {
 		__props_tab_next_import_date_JTextField.setText( "" );
	}

	//get current retries
	long db_retries = 0;
	try {
		db_retries = __db_RTi_ImportProduct.getRetries();
		if ( Message.isDebugOn ) {
			Message.printDebug( 25, routine,
			"Retries: \"" + db_retries + "\"." );
		}
	}
	catch ( Exception e ) {
		Message.printWarning ( 2, routine, e );
	}

	//For defining a New ImportProduct
	if (( DMIUtil.isMissing(db_retries ) ) || ( db_retries < 0 ) ) {
		db_retries = 0;
	}
 	__props_tab_retries_JTextField.setText( String.valueOf(db_retries) );

	//get current properties.
	//As it is in the database, the properties are in one long string 
	//with individual property-value sets seperated by ";"s
	//Use getVectorOfProperties to get a Vector of Vectors where
	//each Vector within the main Vector contains the property
	//key at position 0 and value at position 1.
	Vector props_vect = null;
	props_vect = getVectorOfProperties( __db_RTi_ImportProduct );

	//set properties in GU0
	if ( __db_ImportProduct_type.equalsIgnoreCase( __type_campbellsci_str)) {
		update_GUI_fields_propertiesTab_campbellsci_props( props_vect );
	}
	else {
		update_GUI_fields_propertiesTab_generic_props( props_vect );
	}

}//end update_GUI_fields_propertiesTab

/**
Fills in the properties components on the properties tab in the GUI 
with the parameters for the current RiversideDB_ImportProduct object
of type campbellscientific.
@param all_props_vect Vector containing all the properties for 
this ImportProduct.
*/
protected void update_GUI_fields_propertiesTab_campbellsci_props( 
				Vector all_props_vect ) {
	String routine = __class + 
	".update_GUI_fields_propertiesTab_campbellsci_props";

	int size = 0;
	if ( all_props_vect != null ) {
		size = all_props_vect.size();
	}
	//go through each and set the appropriate components
	String prop_str= null;
	String prop_value_str= null;
	Vector v = null;
	for ( int i=0; i<size; i++ ) {
		v = (Vector) all_props_vect.elementAt(i);	
		prop_str= (String) v.elementAt(0);
		if ( prop_str== null ) {
			continue;
		}
		//String __props_1_campbellsci_JLabel_str = "JULIANDAY";
		else if ( prop_str.equalsIgnoreCase( 
 		__props_1_campbellsci_JLabel_str ) ) {
			//then get its value to set in the combobox/
			prop_value_str= (String)v.elementAt(1);
 			__props_1_campbellsci_JComboBox.
			setSelectedItem( prop_value_str);
		}
		//String __props_2_campbellsci_JLabel_str = "MISSING_VALUE";
		else if ( prop_str.equalsIgnoreCase( 
 		__props_2_campbellsci_JLabel_str ) ) {
			//then get its value to set in the TextField
			prop_value_str= (String)v.elementAt(1);
			if( ( prop_value_str== null ) || 
			( prop_value_str.length() <= 0 ) ) {
 				__props_2_campbellsci_JTextField.setText( "-6999" );
			}
			else {
 				__props_2_campbellsci_JTextField.
				setText( prop_value_str);
			}
		}
		//String __props_3_campbellsci_JLabel_str = "TABLEID";
		else if ( prop_str.equalsIgnoreCase( 
 		__props_3_campbellsci_JLabel_str ) ) {
			//then get its value to set in the combobox/
			prop_value_str= (String)v.elementAt(1);
 			__props_3_campbellsci_JComboBox.
			setSelectedItem( prop_value_str);
		}
		//String __props_4_campbellsci_JLabel_str = "TIME";
		else if ( prop_str.equalsIgnoreCase( 
 		__props_4_campbellsci_JLabel_str ) ) {
			//then get its value to set in the combobox/
			prop_value_str= (String)v.elementAt(1);
 			__props_4_campbellsci_JComboBox.
			setSelectedItem( prop_value_str);
		}
		//String __props_5_campbellsci_JLabel_str = "TIMEZONE";
		else if ( prop_str.equalsIgnoreCase( 
 		__props_5_campbellsci_JLabel_str ) ) {
			//then get its value to set in the combobox/
			prop_value_str= (String)v.elementAt(1);
 			__props_5_campbellsci_JComboBox.
			setSelectedItem( prop_value_str);
		}
		//String __props_6_campbellsci_JLabel_str = "YEAR";
		else if ( prop_str.equalsIgnoreCase( 
 		__props_6_campbellsci_JLabel_str ) ) {
			//then get its value to set in the combobox/
			prop_value_str= (String)v.elementAt(1);
 			__props_6_campbellsci_JComboBox.
			setSelectedItem( prop_value_str);
		}

	}

}//end update_GUI_fields_propertiesTab_campbellsci_props

/**
Fills in the properties components on the properties tab in the GUI 
with the parameters for the current RiversideDB_Import object
of any type but campbellscientific.
@param all_props_vect Vector containing all the properties for 
this ImportProduct.
*/
protected void update_GUI_fields_propertiesTab_generic_props( 
				Vector all_props_vect ) {
	String routine = __class + 
	".update_GUI_fields_propertiesTab_generic_props";

	//For now, the generic ImportPRoduct type just has
	//one JTextField with props seperated by ";" just like in db
	String props_str= null;
	props_str= __db_RTi_ImportProduct.getProperties(); 
 	__props_tab_generic_props_JTextField.setText( props_str);
}//end update_GUI_fields_propertiesTab_generic_props

/**
Fills in the components of the security tab in the GUI with the 
parameters for the current RiversideDB_Import object
*/
protected void update_GUI_fields_securityTab( ) {
	String routine = __class + ".update_GUI_fields_securityTab";

	String db_user_log = null;
	db_user_log = __db_RTi_ImportProduct.getUser_login();
	if ( Message.isDebugOn ) {
		Message.printDebug( 25, routine,
		"Value for User Login is: \"" + 
		db_user_log + "\"." );
	}

 	__security_tab_user_login_JTextField.setText( db_user_log );

	String db_user_pass = null;
	db_user_pass = __db_RTi_ImportProduct.getUser_PWD();
	if ( Message.isDebugOn ) {
		Message.printDebug( 25, routine,
		"Value for User Password is: \"" + 
		db_user_pass + "\"." );
	}
	
 	__security_tab_user_passwd_JTextField.setText( db_user_pass );

	String fire_log = null;
	try {
		fire_log = __db_RTi_ImportProduct.getFirewall_user();
		if ( Message.isDebugOn ) {
			Message.printDebug( 25, routine,
			"Value for Firewall Login is: \"" + 
			fire_log + "\"." );
		}
	} 
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}

	//For defining a New ImportProduct
		if (( fire_log == null ) || ( DMIUtil.isMissing(fire_log) ) ) {
		fire_log = DMIUtil.MISSING_STRING;
	}

 	__security_tab_firewall_login_JTextField.setText( fire_log );

	String fire_pass = null;
	try {
		fire_pass = __db_RTi_ImportProduct.getFirewall_user_PWD();
		if ( Message.isDebugOn ) {
			Message.printDebug( 25, routine,
			"Value for Firewall Password is: \"" + 
			fire_pass + "\"." );
		}
	} 
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}

	//For defining a New ImportProduct
	if (( fire_pass == null ) || ( DMIUtil.isMissing(fire_pass) ) ) {
		fire_pass = DMIUtil.MISSING_STRING;
	}

 	__security_tab_firewall_passwd_JTextField.setText( fire_pass );
}//end update_GUI_fields_securityTab

/**
Verifies all the information on the ARCHIVE tab.
The method <ul><li> fills in the <i>__gui</i> verions of the objects
in memory with the values that are currently set in the GUI</li>
<li>compares the values in the <i>__gui</i> version of the objects to the 
<i>__db</i> version of the objects.  If the <i>__gui</i> versions differ 
from the <i>__db</i> verions:<ul>
<li>the <i>__gui</i> versions are marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the 
<i>__gui</i> differs from the <i>__db</i> version (the <i>__db</i> version 
remember, represents how the object is in the database itself) </li></ul>
</li></ul></ul>
@exception Exception thrown if error encountered
*/
public void verify_archive_tab () throws Exception {
	String routine = __class + ".verify_archive_tab ";

	//ARCHIVE CheckBox - REQUIRED FIELD
	String gui_arch_perm_str= "Y";
	String db_arch_perm_str= "Y";
	String gui_arch_dir_str= null;
	String db_arch_dir_str= null;
	String gui_arch_file_str= null;
	String db_arch_file_str= null;
	String gui_arch_wild_str= null;
	String db_arch_wild_str= null;
	db_arch_perm_str= __db_RTi_ImportProduct.getDoArchive();
	db_arch_dir_str= __db_RTi_ImportProduct.getArchive_dir();
	db_arch_file_str= __db_RTi_ImportProduct.getArchive_file();
	if ( __archive_tab_perm_JCheckBox.isSelected() ) {
		//save its status as "y" since that is what goes into the DB
		gui_arch_perm_str= "Y";

		//make sure was originally "y" in the Database
		if ( ! db_arch_perm_str.equalsIgnoreCase( 
		gui_arch_perm_str) ) {
 			__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
			"Change DoArchive from \"" + db_arch_perm_str+ 
			"\" to \"" + gui_arch_perm_str+ "\"");

 			__gui_RTi_ImportProduct.setDoArchive(gui_arch_perm_str);
		}

		//Since it is Y, then the other fields may be filled in.

		//DIRECTORY - NOT REQURED
		gui_arch_dir_str=( __archive_tab_dir_JTextField.getText()).trim();
		if ( Message.isDebugOn ) {
			Message.printDebug( 35, routine,
			"Value for Archive Tab: Archive directory: \"" + 
			gui_arch_dir_str+ "\"." );
		}

		if ( !db_arch_dir_str.equalsIgnoreCase( gui_arch_dir_str) ) {
 		__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
			"Change Archive Directory from \"" + db_arch_dir_str+ 
			"\" to \"" + gui_arch_dir_str+ "\"");

 			__gui_RTi_ImportProduct.setArchive_dir(gui_arch_dir_str);
		}

		//FILE - NOT REQUIRED
		gui_arch_file_str= 
		( __archive_tab_file_JTextField.getText()).trim();
		if ( Message.isDebugOn ) {
			Message.printDebug( 35, routine,
			"Value for Archive Tab: Archive File: \"" + 
			gui_arch_file_str+ "\"." );
		}
		if ( !db_arch_file_str.equalsIgnoreCase( gui_arch_file_str) ) {
 		__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
			"Change Archive File(s) from \"" + db_arch_file_str+ 
			"\" to \"" + gui_arch_file_str+ "\"");

 			__gui_RTi_ImportProduct.setArchive_file(
			gui_arch_file_str);
		}
	}
	else { //check box is not selected
		//don't need to get the text fields, but store the
		//button status, so can write it to the DB
		gui_arch_perm_str= "N";
		//make sure it was originally "n" in the database.
		if ( ! db_arch_perm_str.equalsIgnoreCase( 
		gui_arch_perm_str) ) {
 			__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
			"Change DoArchve from \"" + db_arch_perm_str+ 
			"\" to \"" + gui_arch_perm_str+ "\"");

 			__gui_RTi_ImportProduct.setDoArchive( "N" );
		}
		//make others missing.
		gui_arch_dir_str= DMIUtil.MISSING_STRING;
		gui_arch_file_str= DMIUtil.MISSING_STRING;
		if ( ! db_arch_dir_str.equalsIgnoreCase( 
		gui_arch_dir_str) ) {
 			__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
			"Change Archive Directory from \"" + db_arch_dir_str+ 
			"\" to \"" + gui_arch_dir_str+ "\"");

 			__gui_RTi_ImportProduct.setArchive_dir(
			gui_arch_dir_str);
		}
		if ( ! db_arch_file_str.equalsIgnoreCase( 
		gui_arch_file_str) ) {
 			__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
			"Change Archive File(s) from \"" + db_arch_file_str+ 
			"\" to \"" + gui_arch_file_str+ "\"");

 			__gui_RTi_ImportProduct.setArchive_file(
			gui_arch_file_str);
		}
	}
	
} //end verify_archive_tab

/**
Verifies and stores all the information on the AUTOMATION tab.
The method <ul><li> fills in the <i>__gui</i> verions of the objects
in memory with the values that are currently set in the GUI</li>
<li>compares the values in the <i>__gui</i> version of the objects to the 
<i>__db</i> version of the objects.  If the <i>__gui</i> versions differ 
from the <i>__db</i> verions:<ul>
<li>the <i>__gui</i> versions are marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the 
<i>__gui</i> differs from the <i>__db</i> version (the <i>__db</i> version 
remember, represents how the object is in the database itself) </li></ul>
</li></ul></ul>
@exception Exception thrown if error encountered
*/
public void verify_automation_tab() throws Exception {
	String routine = __class + ".verify_automation_tab";

	//if isAutomated is selected - all fields can be active,
	//if is in not selected, no fields can be active.
	//isAutomated - REQUIRED
	String gui_isAuto_str= "Y";
	String gui_everyInt_str= "Y";
	String gui_year_str= "*";
	String gui_month_str= "*";
	String gui_day_str= "*";
	String gui_hour_str= "*";
	String gui_minute_str= "15";
	String gui_second_str= "*";
	String gui_weekday_str= "*";
	String gui_delay_str= "";
	String db_isAuto_str= null;
	String db_everyInt_str= null;
	String db_year_str= null;
	String db_month_str= null;
	String db_day_str= null;
	String db_hour_str= null;
	String db_minute_str= null;
	String db_second_str= null;
	String db_weekday_str= null;
	String db_delay_str= null;
	db_isAuto_str= __db_RTi_ImportProduct.getIsAutomated();
	db_everyInt_str= __db_RTi_ImportProduct.getIsInterval();
	db_year_str= __db_RTi_ImportProduct.getImport_year();
	db_month_str= __db_RTi_ImportProduct.getImport_month();
	db_day_str= __db_RTi_ImportProduct.getImport_day();
	db_hour_str= __db_RTi_ImportProduct.getImport_hour();
	db_minute_str = __db_RTi_ImportProduct.getImport_minute();
	db_second_str= __db_RTi_ImportProduct.getImport_second();
	db_weekday_str= __db_RTi_ImportProduct.getImport_weekday();
	db_delay_str= __db_RTi_ImportProduct.getImport_delay();

	if ( __automation_tab_automated_JCheckBox.isSelected() ) {
		gui_isAuto_str= "Y";
		//check that that matches what was originally in DB
		if ( ! db_isAuto_str.equalsIgnoreCase( gui_isAuto_str) ) {
			//mark as dirty
 			__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
			"Change IsAutomated from \"" + db_isAuto_str+ 
			"\" to \"" + gui_isAuto_str+ "\"");

 			__gui_RTi_ImportProduct.setIsAutomated(
			gui_isAuto_str);
		}

		//if the "At Time" checkBox is checked, then
		//check to validate all the date fields.
		//Validate all the fields (year, month, day, etc )
		if ( __automation_tab_at_time_JRadioButton.isSelected() ) {
			//store the value as "N" since 
			//that is what goes into the DB
			//for the value of isInterval
			gui_everyInt_str= "N";
		}
		else {
			gui_everyInt_str= "Y";
		}
		if ( Message.isDebugOn ) {
			Message.printDebug( 25, routine,
			"At Time checkbox on Automation " +
			"tab is selected." );
		}
		//compare it to the original value in DB
		if ( ! db_everyInt_str.equalsIgnoreCase( gui_everyInt_str) ) {
			//mark as dirty
 			__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
			"Change Every Interval from \"" + db_everyInt_str+ 
			"\" to \"" + gui_everyInt_str+ "\"");

 			__gui_RTi_ImportProduct.setIsInterval( gui_everyInt_str);
		}

		//now we have to go through and check each of the other fields.
		//YEAR
		gui_year_str= (String) __automation_tab_year_JComboBox.
		getSelected();
		int ind = -999;
		ind = gui_year_str.indexOf( " -" );
		if ( ind > 0 ) {
			gui_year_str= gui_year_str.substring( 0, ind ).trim();
		}
		//compare value to what was originally in db
		if ( !db_year_str.equalsIgnoreCase( gui_year_str)){
				//set Dirty
 			__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
				"Change Year from \"" + db_year_str+ 
				"\" to \"" + gui_year_str+ "\"");

 			__gui_RTi_ImportProduct.setImport_year( 
				gui_year_str);
		}

		//month
		gui_month_str= (String) __automation_tab_month_JComboBox.
		getSelected();
		ind = -999;
		ind = gui_month_str.indexOf( " -" );
		if ( ind > 0 ) {
			gui_month_str= gui_month_str.substring(0, ind ).trim();
		}

		//compare value to what was originally in db
		if ( !db_month_str.equalsIgnoreCase( gui_month_str)){
				//set Dirty
 			__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
				"Change Month from \"" + db_month_str+ 
				"\" to \"" + gui_month_str+ "\"");

 			__gui_RTi_ImportProduct.setImport_month( 
				gui_month_str);
		}

		//day
		gui_day_str= (String) __automation_tab_day_JComboBox.
		getSelected();
		ind = -999;
		ind = gui_day_str.indexOf( " -" );
		if ( ind > 0 ) {
			gui_day_str= gui_day_str.substring( 0, ind ).trim();
		}
		//compare value to what was originally in db
		if ( !db_day_str.equalsIgnoreCase( gui_day_str)){
				//set Dirty
 			__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
				"Change Day from \"" + db_day_str+ 
				"\" to \"" + gui_day_str+ "\"");

 			__gui_RTi_ImportProduct.setImport_day( 
				gui_day_str);
		}

		//hour
		gui_hour_str= (String) __automation_tab_hour_JComboBox.
		getSelected();
		ind = -999;
		ind = gui_hour_str.indexOf( " -" );
		if ( ind > 0 ) {
			gui_hour_str= gui_hour_str.substring( 0, ind ).trim();
		}
		//compare value to what was originally in db
		if ( !db_hour_str.equalsIgnoreCase( gui_hour_str)){
				//set Dirty
 			__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
				"Change Hour from \"" + db_hour_str+ 
				"\" to \"" + gui_hour_str+ "\"");

 			__gui_RTi_ImportProduct.setImport_hour( 
				gui_hour_str);
		}

		//MINUTE
		gui_minute_str= (String) __automation_tab_minute_JComboBox.
		getSelected();
		ind = -999;
		ind = gui_minute_str.indexOf( " -" );
		if ( ind > 0 ) {
			gui_minute_str= gui_minute_str.substring(0,ind).trim();
		}
		//compare value to what was originally in db
		if ( !db_minute_str.equalsIgnoreCase( gui_minute_str)){
				//set Dirty
 			__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
				"Change Minute from \"" + db_minute_str+ 
				"\" to \"" + gui_minute_str+ "\"");

 			__gui_RTi_ImportProduct.setImport_minute( 
				gui_minute_str);
		}
		
		//Second
		gui_second_str= (String)
 		__automation_tab_second_JComboBox.getSelected();
		ind = -999;
		ind = gui_second_str.indexOf( " -" );
		if ( ind > 0 ) {
			gui_second_str= gui_second_str.substring(0,ind).trim();
		}
		//compare value to what was originally in db
		if ( !db_second_str.equalsIgnoreCase( gui_second_str)){
				//set Dirty
 			__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
				"Change Second from \"" + db_second_str+ 
				"\" to \"" + gui_second_str+ "\"");

 			__gui_RTi_ImportProduct.setImport_second( 
				gui_second_str);
		}

		//weekday
		gui_weekday_str= (String)
 		__automation_tab_weekday_JComboBox.getSelected();
		ind = -999;
		ind = gui_weekday_str.indexOf( " -" );
		if ( ind > 0 ) {
			gui_weekday_str= gui_weekday_str.substring( 0, ind ).
			trim();
		}
		//compare value to what was originally in db
		if ( !db_weekday_str.equalsIgnoreCase( gui_weekday_str)){
				//set Dirty
 			__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
				"Change Weekday from \"" + db_weekday_str+ 
				"\" to \"" + gui_weekday_str+ "\"");

 			__gui_RTi_ImportProduct.setImport_weekday( 
				gui_weekday_str);
		}

		//delay
		String gui_delay_int_str= null;
		String gui_delay_unit_str= null;
		gui_delay_int_str=(String)
 		__automation_tab_delay_int_JComboBox.getSelected();
		gui_delay_unit_str=(String)
 		__automation_tab_delay_unit_JComboBox.getSelected();
		//concatenate the two
		gui_delay_str= gui_delay_int_str+ gui_delay_unit_str;

		if ( gui_delay_int_str.equalsIgnoreCase("0") ) {
			//update database with ""
			gui_delay_str= "";
		}
		//compare value to what was originally in db
		if ( !db_delay_str.equalsIgnoreCase( gui_delay_str)){
				//set Dirty
 			__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
				"Change Delay from \"" + db_delay_str+ 
				"\" to \"" + gui_delay_str+ "\"");

 			__gui_RTi_ImportProduct.setImport_delay( 
				gui_delay_str);
		}

	} //end isAutomated is selected
	else { //if the isAutomated checkbox is not selected, nothing
		//else is active, so don't need checked.
		//but set the isAutomated status so can write it to the db
		gui_isAuto_str= "N";
		//compare value to what was originally in db
		if ( !db_isAuto_str.equalsIgnoreCase( gui_isAuto_str)){
 			__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
				"Change IsAutomated from \"" + db_isAuto_str+ 
				"\" to \"" + gui_isAuto_str+ "\"");

 			__gui_RTi_ImportProduct.setIsAutomated( 
				gui_isAuto_str);
		}
		//leave the rest of the GUI fields to be their defaults
		if ( !db_year_str.equalsIgnoreCase( gui_year_str)){
				//set Dirty
 			__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
				"Change Year from \"" + db_year_str+ 
				"\" to \"" + gui_year_str+ "\"");

 			__gui_RTi_ImportProduct.setImport_year( 
				gui_year_str);
		}
		if ( !db_month_str.equalsIgnoreCase( gui_month_str)){
				//set Dirty
 			__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
				"Change Month from \"" + db_month_str+ 
				"\" to \"" + gui_month_str+ "\"");

 			__gui_RTi_ImportProduct.setImport_month( 
				gui_month_str);
		}
		if ( !db_day_str.equalsIgnoreCase( gui_day_str)){
				//set Dirty
 			__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
				"Change Day from \"" + db_day_str+ 
				"\" to \"" + gui_day_str+ "\"");

 			__gui_RTi_ImportProduct.setImport_day( 
				gui_day_str);
		}
		if ( !db_hour_str.equalsIgnoreCase( gui_hour_str)){
				//set Dirty
 			__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
				"Change Hour from \"" + db_hour_str+ 
				"\" to \"" + gui_hour_str+ "\"");

 			__gui_RTi_ImportProduct.setImport_hour( 
				gui_hour_str);
		}
		if ( !db_minute_str.equalsIgnoreCase( gui_minute_str)){
				//set Dirty
 			__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
				"Change Minute from \"" + db_minute_str+ 
				"\" to \"" + gui_minute_str+ "\"");

 			__gui_RTi_ImportProduct.setImport_minute( 
				gui_minute_str);
		}
		if ( !db_second_str.equalsIgnoreCase( gui_second_str)){
				//set Dirty
 			__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
				"Change Second from \"" + db_second_str+ 
				"\" to \"" + gui_second_str+ "\"");

 			__gui_RTi_ImportProduct.setImport_second( 
				gui_second_str);
		}
		if ( !db_weekday_str.equalsIgnoreCase( gui_weekday_str)){
				//set Dirty
 			__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
				"Change Weekday from \"" + db_weekday_str+ 
				"\" to \"" + gui_weekday_str+ "\"");

 			__gui_RTi_ImportProduct.setImport_weekday( 
				gui_weekday_str);
		}
		if ( !db_delay_str.equalsIgnoreCase( gui_delay_str)){
				//set Dirty
 			__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
				"Change Delay from \"" + db_delay_str+ 
				"\" to \"" + gui_delay_str+ "\"");

 			__gui_RTi_ImportProduct.setImport_delay( 
				gui_delay_str);
		}

	}
	
} //end verify_automation_tab

/**
Verifies and stores all the information on the FILES tab.
The method <ul><li> fills in the <i>__gui</i> verions of the objects
in memory with the values that are currently set in the GUI</li>
<li>compares the values in the <i>__gui</i> version of the objects to the 
<i>__db</i> version of the objects.  If the <i>__gui</i> versions differ 
from the <i>__db</i> verions:<ul>
<li>the <i>__gui</i> versions are marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the 
<i>__gui</i> differs from the <i>__db</i> version (the <i>__db</i> version 
remember, represents how the object is in the database itself) </li></ul>
</li></ul></ul>
@exception Exception thrown if error encountered
*/
public void verify_files_tab() throws Exception {
	String routine = __class + ".verify_files_tab";

	//SOURCE Directory - NOT REQUIRED
	String gui_source_dir = null;
	String gui_source_file = null;
	String gui_dest_dir = null;
	String gui_dest_file = null;
	String db_source_dir = null;
	String db_source_file = null;
	String db_source_wild = null;
	String db_dest_dir = null;
	String db_dest_file = null;
	String db_dest_wild = null;
	db_source_dir = __db_RTi_ImportProduct.getSource_URL_base();
	db_source_file = __db_RTi_ImportProduct.getSource_URL_file();
	db_dest_dir = __db_RTi_ImportProduct.getDestination_dir();
	db_dest_file = __db_RTi_ImportProduct.getDestination_file();

	 gui_source_dir = ( __files_tab_source_dir_JTextField.getText()).trim();
	if ( Message.isDebugOn ) {
		Message.printDebug( 35, routine,
		"Value for File Tab: Source directory: \"" + 
		gui_source_dir + "\"." );
	}

	//compare value to what was originally in db
	if ( !db_source_dir.equalsIgnoreCase( gui_source_dir )){
			//set Dirty
 		__gui_RTi_ImportProduct.setDirty( true );
 		__dirty_vect.addElement(
			"Change Source Directory from \"" + db_source_dir + 
			"\" to \"" + gui_source_dir + "\"");

 		__gui_RTi_ImportProduct.setSource_URL_base( 
			gui_source_dir);
	}

	//Source File
	gui_source_file = ( __files_tab_source_file_JTextField.getText()).trim();
	if ( Message.isDebugOn ) {
		Message.printDebug( 35, routine,
		"Value for File Tab: Source file: \"" + 
		gui_source_file + "\"." );
	}
	//compare value to what was originally in db
	if ( !db_source_file.equalsIgnoreCase( gui_source_file )){
			//set Dirty
 		__gui_RTi_ImportProduct.setDirty( true );
 		__dirty_vect.addElement(
			"Change Source File(s) from \"" + db_source_file + 
			"\" to \"" + gui_source_file + "\"");

 		__gui_RTi_ImportProduct.setSource_URL_file( 
			gui_source_file);
	}

	//Destination Directory
	gui_dest_dir = ( __files_tab_dest_dir_JTextField.getText()).trim();

	if ( Message.isDebugOn ) {
		Message.printDebug( 35, routine,
		"Value for File Tab: destination directory: \"" + 
		gui_dest_dir + "\"." );
	}
	//compare value to what was originally in db
	if ( !db_dest_dir.equalsIgnoreCase( gui_dest_dir )){
			//set Dirty
 		__gui_RTi_ImportProduct.setDirty( true );
 		__dirty_vect.addElement(
			"Change Destination Directory from \"" + db_dest_dir + 
			"\" to \"" + gui_dest_dir + "\"");

 		__gui_RTi_ImportProduct.setDestination_dir( gui_dest_dir);
	}

	//Destination File 
	gui_dest_file = ( __files_tab_dest_file_JTextField.getText()).trim();
	if ( Message.isDebugOn ) {
		Message.printDebug( 35, routine,
		"Value for File Tab: destination file: \"" + 
		gui_dest_file + "\"." );
	}
	//compare value to what was originally in db
	if ( !db_dest_file.equalsIgnoreCase( gui_dest_file )){
			//set Dirty
 		__gui_RTi_ImportProduct.setDirty( true );
 		__dirty_vect.addElement(
			"Change Destination File(s) from \"" + db_dest_file + 
			"\" to \"" + gui_dest_file + "\"");

 		__gui_RTi_ImportProduct.setDestination_file( 
			gui_dest_file);
	}

} //end verify_files_tab

/**
Verifies and stores all the information on the PROPERTIES tab.
The method <ul><li> fills in the <i>__gui</i> verions of the objects
in memory with the values that are currently set in the GUI</li>
<li>compares the values in the <i>__gui</i> version of the objects to the 
<i>__db</i> version of the objects.  If the <i>__gui</i> versions differ 
from the <i>__db</i> verions:<ul>
<li>the <i>__gui</i> versions are marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the 
<i>__gui</i> differs from the <i>__db</i> version (the <i>__db</i> version 
remember, represents how the object is in the database itself) </li></ul>
</li></ul></ul>
@exception Exception thrown if error encountered
*/
public void verify_properties_tab() throws Exception {
	String routine = __class + ".verify_properties_tab";
	String gui_imp_order_str= null;
	String gui_imp_window_str= null;
	String gui_retries_str= null;
	String gui_props_str= null;
	String gui_next_date_str= null;

	long db_imp_order = -999;
	String db_imp_window_str= null;
	long db_retries = -999;
	String db_props_str= null;
	Date db_next_date = null;
	DateTime db_next_DateTime = null;
	String db_next_date_str= null;
	db_imp_order = __db_RTi_ImportProduct.getImport_order();
	db_imp_window_str= __db_RTi_ImportProduct.getImport_window();
	db_retries = __db_RTi_ImportProduct.getRetries();
	db_props_str= __db_RTi_ImportProduct.getProperties();
	db_next_date = __db_RTi_ImportProduct.getNext_import_date();
	//might be null if creating new ImportProduct and new ImportConfs
	if ( db_next_date != null ) {
		db_next_DateTime = new DateTime( db_next_date );
		db_next_DateTime.setPrecision( DateTime.PRECISION_SECOND );
	}
	else {
		db_next_DateTime = new DateTime();
	}

	//Import Order
	long gui_imp_order = -999;
	gui_imp_order_str= (String)
 	__props_tab_import_order_JComboBox.getSelected();
	int ind =-999;
	ind = gui_imp_order_str.indexOf(" -" );
	if ( ind > 0 ) {
		gui_imp_order_str= gui_imp_order_str.substring(0, ind).trim();
	}
	gui_imp_order = StringUtil.atol( gui_imp_order_str);

	//compare value to what was originally in db
	if ( db_imp_order != gui_imp_order ) {
		//set Dirty
 		__gui_RTi_ImportProduct.setDirty( true );
 		__dirty_vect.addElement(
		"Change Import Order from \"" + db_imp_order  + 
		"\" to \"" + gui_imp_order  + "\"");

 		__gui_RTi_ImportProduct.setImport_order( gui_imp_order );
	}
		
	//Import WINDOW FIELD - 
	String gui_imp_window_int_str= null;
	String gui_imp_window_unit_str= null;
	gui_imp_window_int_str=(String)
 	__props_tab_import_window_int_JComboBox.getSelected();
	gui_imp_window_unit_str=(String)
 	__props_tab_import_window_unit_JComboBox.getSelected();
	//concatenate the two
	gui_imp_window_str= gui_imp_window_int_str+ gui_imp_window_unit_str;

	if ( Message.isDebugOn ) {
		Message.printDebug( 35, routine,
		"Value for Properties Tab: import Window: \"" + 
		gui_imp_window_str+ "\"." );
	}
	if ( gui_imp_window_str.startsWith("0" ) ) {
		//set it to missing
		gui_imp_window_str= DMIUtil.MISSING_STRING;
	}

	//compare value to what was originally in db
	if ( !db_imp_window_str.equalsIgnoreCase( gui_imp_window_str)){
		//set Dirty
 		__gui_RTi_ImportProduct.setDirty( true );
 		__dirty_vect.addElement(
		"Change Import Window from \"" + db_imp_window_str + 
		"\" to \"" + gui_imp_window_str + "\"");

 		__gui_RTi_ImportProduct.setImport_window( gui_imp_window_str);
	}

	//Last Import date can't be changed, so does not need checked.
	//NEXT Import Date  
	gui_next_date_str= ( __props_tab_next_import_date_JTextField.
	getText()).trim();
	//Next Date in GUI can be Null, only if we are defining a 
	//new ImportProduct
	DateTime gui_next_DateTime = null;
	if (( gui_next_date_str!= null ) && (gui_next_date_str.length() >0 )) {
		try {
			gui_next_DateTime = DateTime.parse( gui_next_date_str);
		}
		catch (Exception e ) {	
			Message.printWarning( 2, routine, e);
		}
	}
	if ( gui_next_DateTime != null ) {
		//set Precision so we can compare dates.
		gui_next_DateTime.setPrecision( DateTime.PRECISION_SECOND);
		//else compare it to the db_next_date 
		//using the DateTime.equals method
		if ( ! db_next_DateTime.equals( gui_next_DateTime ) ) {
 			__gui_RTi_ImportProduct.setDirty( true );
 			__dirty_vect.addElement(
			"Change Next Date from \"" + 
			db_next_DateTime.toString() +
			"\" to \"" + gui_next_DateTime.toString()  + "\"");
	
			//update database
 			__gui_RTi_ImportProduct.setNext_import_date( 
			gui_next_DateTime.getDate() );
		}
	}
	else {
		//DateTime is Null-- must have been null originally
		//in the database since checkRequiredInput() checks this.
		//_gui_RTi_ImportProduct.setNext_import_date( 
		//new Date() );
	}

	//RETRIES - NOT Editable

	//PROPERTIES FIELDS
	if ( __db_ImportProduct_type.equalsIgnoreCase( __type_campbellsci_str)) {
		verify_properties_tab_campbellsci_props();
	}
	else {
		verify_properties_tab_generic_props();
	}
} //end verify_properties_tab()

/**
Verifies and stores all the information from the properties fields
on the Properties tab for ImportProduct of type CAMPBELLSCIENTIFIC.
The method <ul><li> fills in the <i>__gui</i> verions of the objects
in memory with the values that are currently set in the GUI</li>
<li>compares the values in the <i>__gui</i> version of the objects to the 
<i>__db</i> version of the objects.  If the <i>__gui</i> versions differ 
from the <i>__db</i> verions:<ul>
<li>the <i>__gui</i> versions are marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the 
<i>__gui</i> differs from the <i>__db</i> version (the <i>__db</i> version 
remember, represents how the object is in the database itself) </li></ul>
</li></ul></ul>
@exception Exception thrown if error encountered
*/
public void verify_properties_tab_campbellsci_props() throws Exception {
	String routine = __class + "verify_properties_tab_campbellsci_props";
	//get all props
	String db_props_str= __gui_RTi_ImportProduct.getProperties();
	//concatenate props from gui into one long String
	String gui_props_str= "";
	String prop1 = null;
	String prop2 = null;
	String prop3 = null;
	String prop4 = null;
	String prop5 = null;
	String prop6 = null;

	//order of props in datbase generally is:
	//TABLEID(prop3), YEAR(prop6), JULIANDATE(prop1), 
	//TIME(prop4), TIMEZONE(prop5), MISSING_VALUE(prop2)

	//JulianDate
	prop1 = (String) __props_1_campbellsci_JComboBox.getSelected();
	prop1 = __props_1_campbellsci_JLabel_str + "=" + prop1;

	//MISSING_VALUE
	prop2 = __props_2_campbellsci_JTextField.getText().trim();
	if ( (prop2 != null ) && ( prop2.length() > 0 ) ) {
		prop2 = __props_2_campbellsci_JLabel_str + "=" + 
		prop2.toUpperCase();
	}
	else {
		//prop2 = null;
		prop2 = "-6999";
	}

	//TABLEID
	prop3 = (String) __props_3_campbellsci_JComboBox.getSelected();
	prop3 = __props_3_campbellsci_JLabel_str + "=" + prop3;

	//TIME
	prop4 = (String) __props_4_campbellsci_JComboBox.getSelected();
	prop4 = __props_4_campbellsci_JLabel_str + "=" + prop4;

	//TIMEZONE
	prop5 = (String) __props_5_campbellsci_JComboBox.getSelected();
	if ( prop5.equalsIgnoreCase( "NONE" )) {
		prop5=null;
	}
	else {
		prop5 = __props_5_campbellsci_JLabel_str + "=" + prop5;
	}

	//YEAR
	prop6 = (String) __props_6_campbellsci_JComboBox.getSelected();
	prop6 = __props_6_campbellsci_JLabel_str + "=" + prop6;
	
	//only 2 that can be null are MISSING_VALUE (prop2) and TIMEZONE(prop5)
	StringBuffer b = new StringBuffer();
	//tableid
	b.append(prop3);	
	b.append(";");	
	//year
	b.append(prop6);
	b.append(";");	
	//juliandate
	b.append(prop1);
	b.append(";");	
	//time
	b.append(prop4);
	b.append(";");	
	//timezone
	if ( prop5 !=null ) {
		b.append( prop5 );
		b.append(";");	
	}
	//missing_value
	if ( prop2 !=null ) {
		b.append( prop2 );
		//b.append(";");	
	}
	gui_props_str= b.toString();

	//now compare the properties strings.
	if ( !db_props_str.equalsIgnoreCase( gui_props_str) ){
		//set Dirty
 		__gui_RTi_ImportProduct.setDirty( true );
 		__dirty_vect.addElement(
		"Change Properties from \"" + db_props_str + 
		"\"\nto \"" + gui_props_str + "\"");

 		__gui_RTi_ImportProduct.setProperties( gui_props_str);
	}
}

/**
Verifies and stores all the information from the properties fields
on the Properties tab for ImportProduct of any type but CAMPBELLSCIENTIFIC.
The method <ul><li> fills in the <i>__gui</i> verions of the objects
in memory with the values that are currently set in the GUI</li>
<li>compares the values in the <i>__gui</i> version of the objects to the 
<i>__db</i> version of the objects.  If the <i>__gui</i> versions differ 
from the <i>__db</i> verions:<ul>
<li>the <i>__gui</i> versions are marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the 
<i>__gui</i> differs from the <i>__db</i> version (the <i>__db</i> version 
remember, represents how the object is in the database itself) </li></ul>
</li></ul></ul>
@exception Exception thrown if error encountered
*/
public void verify_properties_tab_generic_props() throws Exception {
	String routine = __class + "verify_properties_tab_generic_props";

	//this just has 1 JTextField with properties seperated
	//by a ";" just like in database
	String db_props = null;
	String gui_props = null;
	db_props = __db_RTi_ImportProduct.getProperties();
	gui_props= __props_tab_generic_props_JTextField.getText().trim();
	if ( !db_props.equalsIgnoreCase( gui_props ) ) {
		//set Dirty
 		__gui_RTi_ImportProduct.setDirty( true );
 		__dirty_vect.addElement(
		"Change Properties from \"" + db_props + 
		"\" to \"" + gui_props + "\"");

 		__gui_RTi_ImportProduct.setProperties( gui_props );
	}
}

/**
Verifies and stores all the information on the SECURITY tab.
The method <ul><li> fills in the <i>__gui</i> verions of the objects
in memory with the values that are currently set in the GUI</li>
<li>compares the values in the <i>__gui</i> version of the objects to the 
<i>__db</i> version of the objects.  If the <i>__gui</i> versions differ 
from the <i>__db</i> verions:<ul>
<li>the <i>__gui</i> versions are marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the 
<i>__gui</i> differs from the <i>__db</i> version (the <i>__db</i> version 
remember, represents how the object is in the database itself) </li></ul>
</li></ul></ul>
@exception Exception thrown if error encountered
*/
public void verify_security_tab() throws Exception {
	String routine = __class + ".verify_security_tab";

	//check all the fields in the Files tab
	String db_user_login_str= null;
	String db_user_passwd_str= null;
	String db_firewall_login_str= null;
	String db_firewall_passwd_str= null;
	db_user_login_str= __db_RTi_ImportProduct.getUser_login();
	db_user_passwd_str= __db_RTi_ImportProduct.getUser_PWD();
	db_firewall_login_str= __db_RTi_ImportProduct.getFirewall_user();
	db_firewall_passwd_str= __db_RTi_ImportProduct.getFirewall_user_PWD();

	//user login
	String gui_user_login_str= null;
	gui_user_login_str=
	( __security_tab_user_login_JTextField.getText()).trim();
	//compare value to what was originally in db
	if ( !db_user_login_str.equalsIgnoreCase( gui_user_login_str)){
		//set Dirty
 		__gui_RTi_ImportProduct.setDirty( true );
 		__dirty_vect.addElement(
		"Change User Login from \"" + db_user_login_str + 
		"\" to \"" + gui_user_login_str + "\"");

 		__gui_RTi_ImportProduct.setProperties( gui_user_login_str);
	}

	//user password
	String gui_user_passwd_str= null;
	gui_user_passwd_str=
	( __security_tab_user_passwd_JTextField.getText()).trim();

	//compare value to what was originally in db
	if ( !db_user_passwd_str.equalsIgnoreCase( gui_user_passwd_str)){
		//set Dirty
 		__gui_RTi_ImportProduct.setDirty( true );
 		__dirty_vect.addElement(
		"Change User Password from \"" + db_user_passwd_str + 
		"\" to \"" + gui_user_passwd_str + "\"");

 		__gui_RTi_ImportProduct.setProperties( gui_user_passwd_str);
	}

	//firewall login
	String gui_firewall_login_str= null;
	gui_firewall_login_str=
	( __security_tab_firewall_login_JTextField.getText()).trim();

	//compare value to what was originally in db
	if ( !db_firewall_login_str.equalsIgnoreCase( gui_firewall_login_str)){
		//set Dirty
 		__gui_RTi_ImportProduct.setDirty( true );
 		__dirty_vect.addElement(
		"Change Firewall Login from \"" + db_firewall_login_str + 
		"\" to \"" + gui_firewall_login_str + "\"");

 		__gui_RTi_ImportProduct.setProperties( gui_firewall_login_str);
	}

	//firewall passwd
	String gui_firewall_passwd_str= null;
	gui_firewall_passwd_str=
	( __security_tab_firewall_passwd_JTextField.getText()).trim();

	//compare value to what was originally in db
	if ( !db_firewall_passwd_str.equalsIgnoreCase(gui_firewall_passwd_str)){
		//set Dirty
 		__gui_RTi_ImportProduct.setDirty( true );
 		__dirty_vect.addElement(
		"Change Firewall Password from \"" + db_firewall_passwd_str+ 
		"\" to \"" + gui_firewall_passwd_str + "\"");

 		__gui_RTi_ImportProduct.setProperties( gui_firewall_passwd_str);
	}
} //end verify_security_tab

/**
Method to verify information on the Time Series selected 
in the JWorksheet of Time Series for Import (right list). 
The method <ul><li> fills in the <i>__gui</i> verions of the ImportConf objects
in memory with the values that are currently set in the GUI</li>
<li>compares the values in the <i>__gui</i> version of the objects to the 
<i>__db</i> version of the objects.  If the <i>__gui</i> versions differ 
from the <i>__db</i> verions:<ul>
<li>the <i>__gui</i> versions are marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the 
<i>__gui</i> differs from the <i>__db</i> version (the <i>__db</i> version 
remember, represents how the object is in the database itself) </li></ul>
</li></ul></ul>
@exception Exception thrown if error encountered
*/
public void verify_timeseries_tab( ) throws Exception {
	verify_timeseries_tab( true );
}

/**
Method to verify information on the Time Series selected 
in the JWorksheet of Time Series for Import (right list).  
@param blnWarningOn True if the warning messages about changes 
to the database version of the ImportConf object and the version of
the object in the GUI should be included.  
The method <ul><li> fills in the <i>__gui</i> verions of the ImportConf objects
in memory with the values that are currently set in the GUI</li>
<li>compares the values in the <i>__gui</i> version of the objects to the 
<i>__db</i> version of the objects.  If the <i>__gui</i> versions differ 
from the <i>__db</i> verions:<ul>
<li>the <i>__gui</i> versions are marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the 
<i>__gui</i> differs from the <i>__db</i> version (the <i>__db</i> version 
remember, represents how the object is in the database itself) </li></ul>
</li></ul></ul>
@exception Exception thrown if error encountered
*/
public void verify_timeseries_tab( boolean blnWarningOn ) throws Exception {

	String routine = __class + ".verify_timeseries_tab";

	//all data is stored in the __timeseries_tab_JWorksheet and
	//is of type: ImportConf
	StringBuffer db_buffer = new StringBuffer();
	StringBuffer gui_buffer = new StringBuffer();

	//get list of TSIdents for the ImportConf objects originally
	//associated with this ImportProduct.
	int orig_num = __gui_RTi_ImportConf_vect.size();

	RiversideDB_ImportConf ic = null;
	if ( orig_num > 0 ) {
		db_buffer.append( "ImportConf objects originally " +
		"associated with this ImportProduct: \n" );
	}
	for ( int i=0; i<orig_num; i++ ) {
		ic = (RiversideDB_ImportConf) __gui_RTi_ImportConf_vect.
		elementAt(i);
		if ( ic == null ) {
			continue;
		}
		long mt_num = -999;
		mt_num = ic.getMeasType_num();
		ic = null;
		//find MeasType with matching MeasType_num
		String tsid_str= getTSIDForMeasType_num( mt_num );
		db_buffer.append( tsid_str+ "\n");
		ic = null;
	}

	//read in all data currently in worksheet.
	Vector table_vect = null;
	table_vect = __timeseries_tab_selTS_JWorksheet.getAllData();

	//compare original number of ImportConf objects
	//to current number of ImportConf objects in worksheet
	int rows = -999;
	rows = table_vect.size();
	if ( Message.isDebugOn ) {
		Message.printDebug( 3, routine, "Number of ImportConf " +
		"objects currently in the worksheet: " + rows +
		" and in the database, there are: " + orig_num +
		" ImportConf objects." );
	}

	if ( rows > 0 ) {
		gui_buffer.append( "ImportConf objects currently " +
		"associated with this ImportProduct: \n" );
	}

	if ( rows != orig_num ) {
		if ( Message.isDebugOn ) {
			Message.printDebug( 3, routine, 
			"Since number of rows in the current worksheet (" +
			rows + ") does not match the number of ImportConf " +
			"objects ("+ orig_num + ") originally associated " +
			"with this ImportProduct, we know that the " +
			"ImportConf objects have changed." );
		}
		//clear out the GUI version since we are updating it now.
 		__gui_RTi_ImportConf_vect.clear();

		//we know right away that the importconf objects are 
		//dirty (when we update ImportConf objects, we delete all the
		//current IC objects from the DB and then write the new ones
		//back. So, just by knowing that there are no longer the
		//same number of ImportConf objects as there originally 
		//were, we know that we need to write to the database.
		
		ic = null;
		for ( int i=0; i<rows; i++ ) {
			ic = (RiversideDB_ImportConf) 
			table_vect.elementAt(i);
			if ( ic == null ) {
				continue;
			}
			ic.setDirty( true );
 			__gui_RTi_ImportProduct.setDirty( true );
			//add to Vector
	
 			__gui_RTi_ImportConf_vect.addElement( ic );
			
			long mt_num = -999;
			mt_num = ic.getMeasType_num();
			ic = null;
			//find MeasType with matching MeasType_num
			String tsid_str= getTSIDForMeasType_num( mt_num );

			//see here if the MeasType was changed from
			//"unknown" create method to "Import"
			RiversideDB_MeasType mt = getMeasTypeForMeasType_num(
			mt_num);
			if ( mt != null ) {
				if ( ! mt.getCreate_method().equalsIgnoreCase(
				"Import" )) {

					//set it dirty and update it.
					mt.setCreate_method( "IMPORT" );
					mt.setDirty( true );
					updateCreateMethodForMeasType(
					mt );
 					__dirty_vect.addElement( "Updated " +
					"MeasType " + tsid_str+ " to " +
					" be an Import Time Series." );
				}
			}

			gui_buffer.append( tsid_str+ "\n" );
		}

 		__dirty_vect.addElement( "Update ImportConf objects " +
		"associated with ImportProduct? \n" + db_buffer.toString() +
		"\n" + gui_buffer.toString() );

	}
	else {
		if ( Message.isDebugOn ) {
			Message.printDebug( 3, routine, 
			"Since number of rows in the current worksheet (" +
			rows + ") does match the number of ImportConf " +
			"objects ("+ orig_num + ") originally associated " +
			"with this ImportProduct, we will go through " +
			"each ImportConf object in the worksheet to " +
			"see if they have changed." );
		}
 		__gui_RTi_ImportConf_vect.clear();

		//go through each and compare each ImportConf object in
		//the worksheet to the one in the original 
		//vector of ImportConf objects, assuming it exists in the
		//original list of ImportConf. If they do exist and do,
		//differ, mark the object dirty. If they don't exist,
		//mark object as dirty.
		RiversideDB_ImportConf table_ic = null;
		RiversideDB_ImportConf orig_ic = null;
		String tsid_str= null;
		boolean blnContinue = true;
		boolean blnNewIC = false;
		for ( int i=0; i<rows; i++ ) {
			table_ic = (RiversideDB_ImportConf) 
			table_vect.elementAt(i);
			if ( table_ic == null ) {
				continue;
			}
			long mt_num = -999;
			mt_num = table_ic.getMeasType_num();
			//find MeasType with matching MeasType_num
			tsid_str= getTSIDForMeasType_num( mt_num );
			
			//now go through original vector of RTi_ImportConf
			for ( int j=0; j<orig_num;j++ ) {
				orig_ic = (RiversideDB_ImportConf) 
 				__db_RTi_ImportConf_vect.elementAt(j);
				if ( orig_ic == null ) {
					continue;
				}
				if ( orig_ic.getMeasType_num() == mt_num ) {
					if ( Message.isDebugOn ) {
						Message.printDebug( 4, routine,
						"Examining ImportConf " +
						"object with MeasType_num ="+
						mt_num + " and tsid = " +
						tsid_str);
					}
					//then we found a matching object that
					//exists in both the worksheet 
					//and in the original list of 
					//ImportConf objects for this
					//ImportProduct

					//compare the 2 objects.
					String gui_ext_field = table_ic.
					getExternal_field();
					String db_ext_field = orig_ic.
					getExternal_field();
					if ( Message.isDebugOn ) {
						Message.printDebug( 5, routine,
						"gui_ext_field = \"" + 
						gui_ext_field +"\" and " +
						"db_ext_field = \"" + 
						db_ext_field +"\"" );
					}

					if (! gui_ext_field.equalsIgnoreCase(
					db_ext_field ) )  {
						table_ic.setDirty( true );
 						__gui_RTi_ImportProduct.setDirty( 
						true );
 						__dirty_vect.addElement( 
						"Update " +
						"Data Column field from:\n \"" +
						db_ext_field + "\" to: \"" +
						gui_ext_field + "\" for Time " +
						"Series:\"" + tsid_str+ "\"?");
					}
	
					String gui_ext_id = table_ic.
					getExternal_id();
					String db_ext_id = orig_ic.
					getExternal_id();
					if ( Message.isDebugOn ) {
						Message.printDebug( 5, routine,
						"gui_ext_id = \"" + 
						gui_ext_id +"\" and " +
						"db_ext_id = \"" + 
						db_ext_id +"\"" );
					}
					if (! gui_ext_id.equalsIgnoreCase(
					db_ext_id ) ) {
						table_ic.setDirty( true );
 						__gui_RTi_ImportProduct.setDirty( 
						true );
 						__dirty_vect.addElement( 
						"Update " +
						"Data File from:\n \"" +
						db_ext_id + "\" to: \"" +
						gui_ext_id + "\" for Time " +
						"Series:\"" + tsid_str+ "\"?");
					}
	
					String gui_ext_table = table_ic.
					getExternal_table();
					String db_ext_table = orig_ic.
					getExternal_table();
					if ( Message.isDebugOn ) {
						Message.printDebug( 5, routine,
						"gui_ext_table = \"" + 
						gui_ext_table +"\" and " +
						"db_ext_table = \"" + 
						db_ext_table +"\"" );
					}
					if (! gui_ext_table.equalsIgnoreCase(
					db_ext_table ) ) {
						table_ic.setDirty( true );
 						__gui_RTi_ImportProduct.setDirty( 
						true );
 						__dirty_vect.addElement( 
						"Update " +
						"Data Table from:\n \"" +
						db_ext_table + "\" to: \"" +
						gui_ext_table + "\" for Time " +
						"Series:\"" + tsid_str+ "\"?");
					}
	
					String gui_ext_units = table_ic.
					getExternal_units();
					String db_ext_units = orig_ic.
					getExternal_units();
					if ( Message.isDebugOn ) {
						Message.printDebug( 5, routine,
						"gui_ext_units = \"" + 
						gui_ext_units +"\" and " +
						"db_ext_units = \"" + 
						db_ext_units +"\"" );
					}
					if (! gui_ext_units.equalsIgnoreCase(
					db_ext_units ) ) {
						if (( db_ext_units == null ) ||
						( db_ext_units.
						length() <= 0 )) {
							db_ext_units ="DEFAULT";
						}
						table_ic.setDirty( true );
 						__gui_RTi_ImportProduct.setDirty( 
						true );
 						__dirty_vect.addElement( 
						"Update " +
						"Data Units from:\n \"" +
						db_ext_units + "\" to: \"" +
						gui_ext_units + "\" for Time " +
						"Series:\"" + tsid_str+ "\"?");
					}
					String gui_isActive = table_ic.
					getIsActive();
					String db_isActive = orig_ic.
					getIsActive();
					if ( Message.isDebugOn ) {
						Message.printDebug( 5, routine,
						"gui_isActive = \"" + 
						gui_isActive +"\" and " +
						"db_isActive = \"" + 
						db_isActive +"\"" );
					}
					if (! gui_isActive.equalsIgnoreCase(
					db_isActive ) ) {
						table_ic.setDirty( true );
 						__gui_RTi_ImportProduct.setDirty( 
						true );
 						__dirty_vect.addElement( 
						"Update " +
						"Data IsActive field from:\n \"" +
						db_isActive + "\" to: \"" +
						gui_isActive + "\" for Time " +
						"Series:\"" + tsid_str+ "\"?");
					}
					
					//now we have compared the 
					//fields, we need to add this
					//ImportConf object back to
					//Vector of ImportConfs

 					__gui_RTi_ImportConf_vect.addElement(
					table_ic );


					blnContinue = false;
					break;
				}

				if ( j == (orig_num -1) ) {
					//then never 
					//found a matching one...
					blnNewIC = true;

				}
			}
			if ( blnNewIC ) {
				if ( Message.isDebugOn ) {
					Message.printDebug( 4, routine,
					"Did not find matching " +
					"ImportConf object with " +
					"MeasType_num " + mt_num + 
					" and tsid = " + tsid_str+
					", so must have added this " +
					"as a new ImportConf object." );
				}
				// we never found a matching
				//ImportConf object
				//so this one must be a new addition
				table_ic.setDirty( true );
 				__gui_RTi_ImportProduct.setDirty( true );
 				__dirty_vect.addElement(
				"Add ImportConf object \"" +
				tsid_str+ "\" to selected ImportProduct?" );

				//see here if the MeasType was changed from
				//"unknown" create method to "Import"
				RiversideDB_MeasType mt = 
				getMeasTypeForMeasType_num( mt_num);
				if ( mt != null ) {
					if ( ! mt.getCreate_method().
					equalsIgnoreCase( "Import" )) {
						//set it dirty and update it.
						mt.setCreate_method( "IMPORT" );
						mt.setDirty( true );
						updateCreateMethodForMeasType(
						mt );
 						__dirty_vect.addElement( 
						"Updated " +
						"MeasType " + tsid_str+ 
						" to be an Import Time " +
						"Series." );
					}
				}
	
				//update vector of GUI importConf objects
 				__gui_RTi_ImportConf_vect.addElement( table_ic );
				table_ic = null;
				orig_ic = null;
			}
		}
	}

} //end verify_timeseries_tab

/**
Verifies all the information on the tabs currently present in the GUI by
calling the verify_xxx() methods such as: verify_automation_tab,
verify_files_tab, verify_properties_tab, verify_timeseries_tab, etc.
@exception Exception thrown if error encountered
*/
public void verify_tab_info( ) throws Exception {
	String routine = __class + ".verify_tab_info";

	//some tabs are not added for certain import product
	//types, so make sure the panel for that tab has been
	//created before checking its fields.
	if ( __files_tab_JPanel != null ) {
		try {
			verify_files_tab();
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e );
		}
	}
	if ( __timeseries_tab_JPanel != null ) {
		try {
			verify_timeseries_tab();
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e );
		}
	}
	if ( __properties_tab_JPanel != null ) {
		try {
			verify_properties_tab();
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e );
			throw new Exception ( "Error verifying the " +
			"properties tab." );
		}
	}
	if ( __security_tab_JPanel != null ) {
		try {
			verify_security_tab();
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e );
		}
	}
	if ( __automation_tab_JPanel != null ) {
		try {
			verify_automation_tab();
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e );
		}
	}
	if ( __archiving_tab_JPanel != null ) {
		try {
			verify_archive_tab();
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e );
		}
	}

}//end verify_tab_info

/**
Verifies all the information in the top portion of the GUI. 
The method <ul><li> fills in the <i>__gui</i> verions of the objects
in memory with the values that are currently set in this part of the GUI</li>
<li>compares the values in the <i>__gui</i> version of the objects to the 
<i>__db</i> version of the objects.  If the <i>__gui</i> versions differ 
from the <i>__db</i> verions:<ul>
<li>the <i>__gui</i> versions are marked dirty (<i>.setDirty(true)</i>)</li>
<li>messages are added to the <i>__dirty_vect</i> indicating how the 
<i>__gui</i> differs from the <i>__db</i> version (the <i>__db</i> version 
remember, represents how the object is in the database itself) </li></ul>
</li></ul></ul>
@exception Exception thrown if error encountered
*/
public void verify_top_fields() throws Exception {
	String routine = __class + ".verify_top_fields";

	//holds original node name from tree- this node may need
	//to be updated if the Import Product name changes
 	__db_tree_node_str = __gui_RTi_ImportProduct.getProduct_name() +
	" (type: " + __gui_RTi_ImportProduct.getProduct_type() + ")";

	//Product ID
	String gui_id =null;
	gui_id = ( __product_info_id_JTextField.getText()).trim();
	String db_id = null;
	db_id = __db_RTi_ImportProduct.getProduct_name();
	if ( ! db_id.equalsIgnoreCase( gui_id) ) {
 		__gui_RTi_ImportProduct.setDirty ( true );
 		__dirty_vect.addElement(
		"Change Product Identifier from \"" + 
		db_id +"\" to \"" +gui_id+"\"");
 		__gui_RTi_ImportProduct.setProduct_name( gui_id );
	}

	//Product Group
	int gui_grp_num = -999;
	String gui_prod_grp = null;
	String gui_prod_grp_num = null;
	String gui_grp = null;
	gui_prod_grp = (String) __product_info_group_JComboBox.getSelected();
	int ind = -999;
	ind = gui_prod_grp.indexOf( " -" );
	if ( ind > 0 ) {
		gui_grp = (gui_prod_grp.substring( ind+2 )).trim();
		gui_prod_grp_num = gui_prod_grp.substring( 0, ind );
	}
	//convert to int
	if ( StringUtil.isInteger( gui_prod_grp_num ) ) {
		gui_grp_num = StringUtil.atoi( gui_prod_grp_num );
	}

	//String db_grp = null;
	int db_grp_num = -999;
	db_grp_num = __db_RTi_ImportProduct.getProductGroup_num();
	//Get Identifier
	String db_grp= "";
	int num_prodgrps = 0;
	Vector prodGrp_vect = null;
	try {
		prodGrp_vect = 
 		__dmi.readProductGroupListForProductType( "IMPORT" );
	}
	catch (Exception e ) {
		Message.printWarning( 2, routine, e );
	}
	if ( prodGrp_vect != null ) {
		num_prodgrps= prodGrp_vect.size();
	}
	RiversideDB_ProductGroup pg = null;
	for ( int i=0; i< num_prodgrps; i++ ) { 
		pg = (RiversideDB_ProductGroup) prodGrp_vect.elementAt(i);
		if ( pg == null ) {
			continue;
		}
		if ( pg.getProductGroup_num() == db_grp_num ) {
			db_grp = pg.getIdentifier();
			pg = null;
			break;
		}
		pg = null;
	}
	prodGrp_vect = null;
	db_grp= __db_RTi_ImportProduct.getProduct_group();
	if ( db_grp_num !=  gui_grp_num ) {
 		__gui_RTi_ImportProduct.setDirty ( true );

 		__dirty_vect.addElement(
		"Change Product Group num from " + db_grp_num +"(\""+
		db_grp +"\") to " + gui_grp_num + " (\"" +gui_grp + "\")");

 		__gui_RTi_ImportProduct.setProductGroup_num(gui_grp_num );
 		__gui_RTi_ImportProduct.setProduct_group(gui_grp);
	}

	//Product Type  
	String gui_type = null;
	gui_type = (String) __product_info_type_JComboBox.getSelected();
	ind = -999;
	ind = gui_type.indexOf( " -" );
	if ( ind > 0 ) {
		gui_type = gui_type.substring( 0, ind );
	}
	String db_type = __db_RTi_ImportProduct.getProduct_type( );

	//compare it to the original value for Product TYpe
	if ( ! db_type.equalsIgnoreCase( gui_type ) ) {
		//mark dirty
 		__gui_RTi_ImportProduct.setDirty ( true );
 		__dirty_vect.addElement(
		"Change Product Type from \"" + 
		db_type +"\" to \"" +gui_type+"\"");
 		__gui_RTi_ImportProduct.setProduct_type( gui_type );
	}	

	//MeasLocgroup_num field - required
	int gui_mlg_num = -999;
	int db_mlg_num = -999;
	String gui_mlg_str= null;
	gui_mlg_str= (String) __product_info_measlocgroup_JComboBox.
	getSelected();
	ind = -999;
	ind = gui_mlg_str.indexOf( " - " );
	if ( ind > 0 ) {
		gui_mlg_str= (gui_mlg_str.substring( 0, ind)).trim();
	}
	if ( StringUtil.isInteger( gui_mlg_str) ) {
		gui_mlg_num = StringUtil.atoi( gui_mlg_str);
	}

	db_mlg_num = __db_RTi_ImportProduct.getMeasLocGroup_num();	
	if ( db_mlg_num != gui_mlg_num ) {
		//mark object as dirty
 		__gui_RTi_ImportProduct.setDirty ( true );
 		__dirty_vect.addElement(
		"Change MeasLocGroup_num from \"" + 
		db_mlg_num +"\" to \"" +gui_mlg_num+"\"");
		//set in memory
 		__gui_RTi_ImportProduct.setMeasLocGroup_num( gui_mlg_num );
	}

	//Check current "isActive" state to original state.
	String gui_isAct_str= null;
	String db_isAct_str= null;
	db_isAct_str= __db_RTi_ImportProduct.getIsActive();
	if ( __product_info_active_JCheckBox.isSelected() ) {
		//isAct_str= "Y";
		gui_isAct_str= "Y";
	}
	else {
		gui_isAct_str= "N";
	}
	//compare
	if ( !db_isAct_str.equalsIgnoreCase( gui_isAct_str)) {
 		__gui_RTi_ImportProduct.setDirty ( true );
 		__dirty_vect.addElement(
		"Change isActive (Y/N) from \"" + 
		db_isAct_str+"\" to \"" +gui_isAct_str+"\"");
		//set in memory
 		__gui_RTi_ImportProduct.setIsActive( gui_isAct_str);
	}

} //end verify_top_fields

/////////////////////////////////////////////////////////////////
////////////////////////  ACTIONS ///////////////////////////////
/////////////////////////////////////////////////////////////////
/**
The event handler manages window events. 
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
 			__dirty_vect.clear();
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
 				__gui_RTi_ImportProduct = null;
 				__gui_RTi_ImportProduct = 
				new RiversideDB_ImportProduct( 
 				__db_RTi_ImportProduct );

				//transfer stuff from 
				//_db_RTi_ImportConf_vect to __gui_ version
				int s = 0;
				if ( __db_RTi_ImportConf_vect != null ){
					s = __db_RTi_ImportConf_vect.size();
				}

 				__gui_RTi_ImportConf_vect.clear();
				for ( int i=0; i<s; i++ ) {
 					__gui_RTi_ImportConf_vect.addElement(
 					__db_RTi_ImportConf_vect.elementAt(i) );
				}

 				__dirty_vect.clear();
			}
		}
		if ( blnUpdated ) {
			//if we got this far, the database was updated,
			//so update objects in memory.  The __gui_ object 
			//was written to the database, so now the 
			//_db_ object should equal the __gui_ object
 			__db_RTi_ImportProduct = null;
 			__db_RTi_ImportProduct = new RiversideDB_ImportProduct( 
 			__gui_RTi_ImportProduct );
 			__db_RTi_ImportProduct.setDirty( false );

			//update ImportConf vector -transfer GUI to db
			int s = 0;
			if ( __gui_RTi_ImportConf_vect != null ){
				s = __gui_RTi_ImportConf_vect.size();
			}
 			__db_RTi_ImportConf_vect.clear();
			RiversideDB_ImportConf ic = null;
			for ( int i=0; i<s; i++ ) {
				ic = (RiversideDB_ImportConf)
 				__gui_RTi_ImportConf_vect.elementAt(i);
				ic.setDirty( false );
 				__db_RTi_ImportConf_vect.addElement(ic );
			}
		}

	}
	else if ( source.equals( __archive_tab_browse_JButton ) ) {

		String dir = null;
		String cur_dir = null;
		cur_dir = __archive_tab_dir_JTextField.getText().trim(); 
		if (( cur_dir == null ) || ( cur_dir.length() <= 0 )) {
			cur_dir = JGUIUtil.getLastFileDialogDirectory();
		}

		JFileChooser chooser = JFileChooserFactory.createJFileChooser();

		chooser.setFileSelectionMode( 
		JFileChooser.FILES_AND_DIRECTORIES );

		String open_file_title = "Browse";
		chooser.setDialogTitle( open_file_title );
		File file = new File( cur_dir );

		//if "file" is null, then it will
		//open the file chooser in the user's defualt directory
		//(usually "My Documents" on Windows OS, and $HOME on Unix)
		chooser.setCurrentDirectory( file );
		int returnVal = chooser.showOpenDialog( this );

		if ( returnVal == JFileChooser.APPROVE_OPTION ) {
 			__archive_tab_dir_JTextField.setText( 
			chooser.getSelectedFile().getPath());
		}
		chooser = null;
	}
	if ( source.equals( __archive_tab_perm_JCheckBox )) {
		//if the JCheckBox is Checked, then
		//the other fields should be filled in.

		//if the JCheckBox is un-checked, then
		//the other fields should be grayed out.
		if ( __archive_tab_perm_JCheckBox.isSelected() ) {
 			JGUIUtil.setEnabled(
			__archive_tab_dir_JTextField, true );
			JGUIUtil.setEnabled(
			__archive_tab_file_JTextField, true );
			JGUIUtil.setEnabled(
			__archive_tab_wild_JComboBox, true );
		}
		else {
 			JGUIUtil.setEnabled(
			__archive_tab_dir_JTextField, false );

 			JGUIUtil.setEnabled(
			__archive_tab_file_JTextField, false );

 			JGUIUtil.setEnabled( 
			__archive_tab_wild_JComboBox, false );
		}
	}//end if source __archive_tab_perm_JCheckBox

	if ( source.equals( __automation_tab_automated_JCheckBox )) {

		//if the JCheckBox is Checked, then
		//the other fields should be filled in.

		//if the JCheckBox is un-checked, then
		//the other fields should be grayed out.
		
		boolean enable_fields = false;
		if ( __automation_tab_automated_JCheckBox.isSelected() ) {
			//if the date fields should be active or not

			enable_fields = true;
			//every interval checkbox
 			JGUIUtil.setEnabled(
			__automation_tab_every_interval_JRadioButton, true );

			//at time checkbox
 			JGUIUtil.setEnabled(
			__automation_tab_at_time_JRadioButton, enable_fields );

 			JGUIUtil.setEnabled(
			__automation_tab_every_interval_JRadioButton, enable_fields );
			if ( enable_fields ) {
				//year
 			JGUIUtil.setEnabled(
			__automation_tab_year_JComboBox, true );

				//month
 			JGUIUtil.setEnabled(
			__automation_tab_month_JComboBox, true );

				//day
 			JGUIUtil.setEnabled(
			__automation_tab_day_JComboBox, true );

				//hour
 			JGUIUtil.setEnabled(
			__automation_tab_hour_JComboBox, true );

				//minute
 			JGUIUtil.setEnabled(
			__automation_tab_minute_JComboBox, true );

				//second
 			JGUIUtil.setEnabled(
			__automation_tab_second_JComboBox, true );

				//weekday
				/*
 			JGUIUtil.setEnabled(
			__automation_tab_weekday_JComboBox, true );
				*/

				//delay
 			JGUIUtil.setEnabled(
			__automation_tab_delay_int_JComboBox, true );
 			JGUIUtil.setEnabled(
			__automation_tab_delay_unit_JComboBox, true );
				
			}
		} //end if the isAutomated button is selected
		else {  //the automation tab not checked, 
			//so no fields are active

 			JGUIUtil.setEnabled(
			__automation_tab_every_interval_JRadioButton, false );

 			JGUIUtil.setEnabled(
			__automation_tab_at_time_JRadioButton, false );

			//year
 			JGUIUtil.setEnabled(
			__automation_tab_year_JComboBox, false );

			//month
 			JGUIUtil.setEnabled(
			__automation_tab_month_JComboBox, false );

			//day
 			JGUIUtil.setEnabled(
			__automation_tab_day_JComboBox, false );

			//hour
 			JGUIUtil.setEnabled(
			__automation_tab_hour_JComboBox, false );

			//minute
 			JGUIUtil.setEnabled(
			__automation_tab_minute_JComboBox, false );

			//second
 			JGUIUtil.setEnabled(
			__automation_tab_second_JComboBox, false );

			//weekday
			/*
 			JGUIUtil.setEnabled(
			__automation_tab_weekday_JComboBox, false );
			*/

			//delay
 			JGUIUtil.setEnabled(
			__automation_tab_delay_int_JComboBox, false );
 			JGUIUtil.setEnabled(
			__automation_tab_delay_unit_JComboBox, false );

		}
	}
	
	else if ( source.equals( __cancel_JButton ) ) {
		if ( __bln_new_object ) {
			//if we are adding a new ImportProduct,
			//then we already wrote a ImportProduct to the
			//database in the constructor in order to get the
			//ImportProduct_num (autonum) for the new ImportProduct.
			//If the user cancels b/f making any other saves,
			//we should delete the ImportProduct from the
			//database.  In order to tell if we need to delete
			//it, check to see if the Object in the Database
			//has been updated to have a Product Identifier field.
			//(since that is one of the required fields, we
			//know that the database Has been updated if it is
			//filled or has Not been updated if it isn't).
			String db_id = null;
			db_id = __db_RTi_ImportProduct.getProduct_name();
			int[] arrDel=null;
			if ( DMIUtil.isMissing( db_id ) ) {
				//delete product that had
				//been written to the database and close GUI
				try {
					arrDel = __dmi.
					deleteImportProductForImportProduct_num(
					(int) __db_ImportProduct_num );
				}
				catch (Exception e ) {
					Message.printWarning( 2, routine, e);
				}
				windowManagerClose();
			}
		}
		//else if(( __cautious_mode ) && ( __dirty_vect.size() > 0 ) ) {}
		else if( __cautious_mode ) {
 
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

			//if the ImportConf is dirty, the ImportProduct
			//would be marked dirty too.
			if ( __gui_RTi_ImportProduct.isDirty() ) {
				//holds messages from __dirty_vect
				StringBuffer b = new StringBuffer();
				b = new StringBuffer();
				for (int i=0;i< __dirty_vect.size();i++) {
					b.append( (String) 
 					__dirty_vect.elementAt(i)+"\n" );
				}

				//write out a confirmation message.
				int x = new ResponseJDialog( this, 
				"Cancel Changes",
				"Are you sure you want to " +
				"Cancel the following changes?\n" +
				b.toString(),
				ResponseJDialog.YES | ResponseJDialog.NO ).
				response();
			
				if ( x == ResponseJDialog.YES ) {
					//write to log file
					Message.printStatus( 5, 
					routine, "User canceled changes: " +
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
	
	//BROWSE BUTTONS
	else if ( source.equals( __files_tab_dest_browse_JButton) ) {
		String dir = null;
		String cur_dir = null;
		cur_dir = __files_tab_dest_dir_JTextField.getText().trim(); 
		if (( cur_dir == null ) || ( cur_dir.length() <= 0 )) {
			cur_dir = JGUIUtil.getLastFileDialogDirectory();
		}

		JFileChooser chooser = JFileChooserFactory.createJFileChooser();
		chooser.setFileSelectionMode( 
		JFileChooser.FILES_AND_DIRECTORIES );
		String open_file_title = "Select Destination Directory";
		chooser.setDialogTitle( open_file_title );
		File file = new File( cur_dir );

		//if "file" is null, then it will
		//open the file chooser in the user's defualt directory
		//(usually "My Documents" on Windows OS, and $HOME on Unix)
		chooser.setCurrentDirectory( file );
		int returnVal = chooser.showOpenDialog( this );

		if ( returnVal == JFileChooser.APPROVE_OPTION ) {
 			__files_tab_dest_dir_JTextField.setText( 
			chooser.getSelectedFile().getPath());
		}
		chooser = null;
	}
	
	else if ( source.equals( __files_tab_source_browse_JButton ) ) {
		String dir = null;
		String cur_dir = null;
		cur_dir = __files_tab_source_dir_JTextField.getText().trim(); 
		if (( cur_dir == null ) || ( cur_dir.length() <= 0 )) {
			cur_dir = JGUIUtil.getLastFileDialogDirectory();
		}

		JFileChooser chooser = JFileChooserFactory.createJFileChooser();
		String open_file_title = "Select Source Directory";
		chooser.setDialogTitle( open_file_title );
		chooser.setFileSelectionMode( 
		JFileChooser.FILES_AND_DIRECTORIES );
		File file = new File( cur_dir );

		//if "file" is null, then it will
		//open the file chooser in the user's defualt directory
		//(usually "My Documents" on Windows OS, and $HOME on Unix)
		chooser.setCurrentDirectory( file );
		int returnVal = chooser.showOpenDialog( this );

		if ( returnVal == JFileChooser.APPROVE_OPTION ) {
 			__files_tab_source_dir_JTextField.setText( 
			chooser.getSelectedFile().getPath());
		}
		chooser = null;

	}
	
	else if ( source.equals( __timeseries_tab_clear_selected_JButton ) ) {

		int row = __timeseries_tab_selTS_JWorksheet.getSelectedRow();

		//get total number of rows--- cannot delete last row
		int total_num = __timeseries_tab_selTS_JWorksheet.getRowCount();
		if( ( total_num ==1 ) || ( total_num == 0 ) ) {
			Message.printWarning( 1, routine, 
			"Unable to delete the last row of data. ");
		}
		else if ( row != -1 ) {
			//get tsid from 1 column to delete MeasType
			//that is also associated with the ImportConf in the
			//table.
			String tsid_str= (String)
 			__timeseries_tab_selTS_JWorksheet.getValueAt(row,
			1) ;

   			int x = new ResponseJDialog(this,
    			"Delete Selected Row", "Delete Import Conf object "+
			"for /n" + tsid_str+ "/n",
			//"no longer be an Import time series?",
    			ResponseJDialog.YES | ResponseJDialog.NO)
    			.response();

   			if (x == ResponseJDialog.NO) {
    				return;
   			}

 			__timeseries_tab_selTS_JWorksheet.cancelEditing();
 			__timeseries_tab_selTS_JWorksheet.deleteRow(row);
 			__table_model.deleteMeasType( tsid_str);

			//get MeasType object for TS
			RiversideDB_MeasType changed_mt = null;
			changed_mt = 
 			__table_model.getMeasTypeForTSIdent( tsid_str);
				changed_mt.setCreate_method( "UNKNOWN" );
				changed_mt.setDirty( true );
			//update it in the list of MeasTypes
			updateCreateMethodForMeasType( changed_mt );
		}
		else {
			Message.printWarning( 1, routine, 
			"You must select a row to delete. " , this );
		}

	} //end move left/clear selected
	 	
	else if ( source.equals( __timeseries_tab_move_right_JButton ) ) {

		//make vector of selected MeasType objects and corresponding
		//Vector of ImportConf objects
		//Vector mt_vect = new Vector();
		//Vector ic_vect = new Vector();
		//read in all ImportConf objects
		Vector all_ImportConf_vect = null;
		try {
			all_ImportConf_vect = 
 			__dmi.readImportConfList();
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e);
			all_ImportConf_vect = null;
		}
		int num_ic = 0;	
		if ( all_ImportConf_vect != null ) {
			num_ic =  all_ImportConf_vect.size();
		}

		//make sure that measType does not already exist in the
		//worksheet of selected importConf objects.
		Object[] selTS_to_move =null; 
		if ( __timeseries_tab_allTS_JList.getModel().getSize() > 0 ) {
			selTS_to_move = 
 			__timeseries_tab_allTS_JList.getSelectedValues();
		}
		//go through each selected item and make sure it 
		//is not already in the right worksheet of selected TS
		String ts = null;
		String ts_nodesc ="";
		boolean blnMove = true;
		for ( int i=0; i< selTS_to_move.length; i++ ) {
			ts = (String) selTS_to_move[i];
			if ( ts == null ) {
				continue;
			}
			ts_nodesc = ts;
			int ind = -999;
			ind = ts.indexOf( " - " );
			if ( ind > 0 ) {
				ts_nodesc = ts.substring( 0, ind ).trim();
			}
			//get number of rows.
			int rows = __timeseries_tab_selTS_JWorksheet.
			getRowCount();
			String s = null;
			for ( int r=0; r< rows; r++ ) {
				s = (String) __timeseries_tab_selTS_JWorksheet.
				getValueAt(r, 1 );
			/* JWorksheet.find() not working.
				int x=
 				__timeseries_tab_selTS_JWorksheet.find(
				ts_nodesc, 2, 0, 
				JWorksheet.FIND_CASE_INSENSITIVE );
				if ( x >= 0  ) {}
			*/
				if ( s.equalsIgnoreCase( ts_nodesc ) ) {
					Message.printWarning( 1, routine,
					"Time Series: \"" +ts_nodesc + 
					"\"\n already in worksheet " +
					"of time series to import from file.", 
					this);
					blnMove = false;
					break;
				}
			}
			RiversideDB_MeasType sel_mt = null;
			long sel_mt_num = -999;
			if ( blnMove ) {
				//now make sure it is not 
				//already used as an ImportConf
				//check to make sure there is 
				//not an importConf
				//object already for this MeasType 
				//( in another import product)
	
				//get MeasType_num for selected TS.
				try {
					sel_mt = 
 					__dmi.readMeasTypeForTSIdent( ts_nodesc);
				}
				catch ( Exception e ) {
					Message.printWarning( 2, routine, e);
					sel_mt_num = -999;
				}
				if ( sel_mt != null ) {
					sel_mt_num = sel_mt.getMeasType_num();
				}
				RiversideDB_ImportConf ic = null;
				long mt_num =-999;
				for ( int j=0;j<num_ic;j++ ) {
					ic =(RiversideDB_ImportConf) 
					all_ImportConf_vect.
					elementAt(j);
					if ( ic == null ) {
						continue;
					}
					//get MeasType num from it.
					mt_num = ic.getMeasType_num();
					if( mt_num == sel_mt_num ) {
						if ( Message.isDebugOn ) {
							Message.printDebug( 4, routine,
							"Found object with matching" +
							"MeasType_num as selected " +
							"MeasType (MeasType_num = "+
							sel_mt_num + ") " );
						}
						//we won't move the selected 
						//time series
						//over since it already is 
						//used as an
						//ImportConf obj.  
						//Find which ImportProduct
						//it is related to.
						RiversideDB_ImportProduct ip = null;
						String ip_name = "";
						try {
							ip = __dmi.
							readImportProductForImportProduct_num(
							ic.getImportProduct_num() );
						}
						catch( Exception e) {
							Message.printWarning( 2, 
							routine,e );
							ip_name = "";
							ip = null;
						}
						if ( ip != null ){
							ip_name = ip.getProduct_name() ;
							Message.printWarning( 1, 
							routine,
							"Time Series: \n\"" +
							ts_nodesc + 
							"\"\n already used as " +
							"ImportConf object "+
							"\nin ImportProduct: \"" + 
							ip_name +
				 			"\"", this);
							blnMove= false;
							break;
						}
					}

					//Create method will be
					//verified as IMPORT in the
					//verify_timeseries_tab method
				}
			}
			//add it to the Vector of 
			//MeasType objects
			if ( blnMove ) {

				RiversideDB_ImportConf new_ic = 
				new RiversideDB_ImportConf();
				new_ic.setMeasType_num( sel_mt_num );
				new_ic.setImportProduct_num( 
 				__db_RTi_ImportProduct.
				getImportProduct_num());

				//ic_vect.addElement( new_ic );

 				__table_model.addMeasType( sel_mt );
 				__timeseries_tab_selTS_JWorksheet.
				addRow( new_ic );

 				__timeseries_tab_selTS_JWorksheet.
				scrollToLastRow();
 				__timeseries_tab_selTS_JWorksheet.
				selectLastRow();
			}
		}

	} //end source move right
	
	else if ( source == __files_tab_dest_wild_JComboBox ) {
		// Get wildcard
		String wild = null;
		wild = (String) __files_tab_dest_wild_JComboBox.
			getSelected();
		int ind = -999;
		ind = wild.indexOf(" -" );
		if ( ind > 0 ) {
			wild = wild.substring(0,ind).trim();
			// Get current text and append to it.
			String gui_file = null;
			gui_file = __files_tab_dest_file_JTextField.
				getText();
 			__files_tab_dest_file_JTextField.setText(
 				gui_file + wild );	
 		}
	}
	
	if ( source == __archive_tab_wild_JComboBox ) {
		//get the wildcard selected and add to textfield
		String wild = null;
		wild = (String) __archive_tab_wild_JComboBox.
			getSelected();
		//remove comments
		int ind = -999;
		ind = wild.indexOf( " -");
		if ( ind > 0 ) {
			wild = wild.substring( 0, ind ).trim();
		}
		if ( wild.equalsIgnoreCase("*") ) {
 			__archive_tab_file_JTextField.
				setText(wild);
		}
		else if ( wild.equalsIgnoreCase("*." ) ) {
			String gui_dir = null;
			gui_dir = __archive_tab_file_JTextField.
				getText();
 			__archive_tab_file_JTextField.setText(
 				"*." + gui_dir );
		}
		else if( wild.equalsIgnoreCase( ".*" ) ) {
			String gui_dir = null;
			gui_dir = __archive_tab_file_JTextField.
				getText();
 			__archive_tab_file_JTextField.setText(
 				gui_dir + ".*" );
		}
	}
	
	else if ( source == __files_tab_source_wild_JComboBox ) {
		//get the wildcard selected and add to textfield
		String wild = null;
		wild = (String) __files_tab_source_wild_JComboBox.
		getSelected();
		//remove comments
		int ind = -999;
		ind = wild.indexOf( " -");
		if ( ind > 0 ) {
			wild = wild.substring( 0, ind ).trim();
		}
		if ( wild.equalsIgnoreCase("*") ) {
 			__files_tab_source_file_JTextField.
				setText(wild);
		}
		else if ( wild.equalsIgnoreCase( "*." ) ) {
			String gui_dir = null;
			gui_dir = __files_tab_source_file_JTextField.
				getText();
 			__files_tab_source_file_JTextField.
				setText( "*." + gui_dir );
		}
		else if ( wild.equalsIgnoreCase( ".*" ) ) {
			String gui_dir = null;
			gui_dir = __files_tab_source_file_JTextField.
				getText();
 			__files_tab_source_file_JTextField.
				setText( gui_dir + ".*" );
		}
	}

	source = null;
	} // End of try
	catch ( Exception e ) {
		if ( Message.isDebugOn ) {
			Message.printWarning ( 2, routine, e );
		}
	}
} //end action performed

/**
Respond to ItemEvents.  
@param event ItemEvent to listen for.
*/
public void itemStateChanged ( ItemEvent event ) {
	String routine = __class + ".itemStateChanged";
	Object source = event.getItemSelectable();
	if ( event.getStateChange() == ItemEvent.SELECTED ) {

		
		if ( source == __automation_tab_delay_unit_JComboBox ) {
			//update integers in multiplier list
			//get new item selected in unit JComboBox
			String unit_base = null;
			unit_base = (String) 
 			__automation_tab_delay_unit_JComboBox.getSelected();

			//update the list of TIme Step Multiplier
			//choices for the specific Base choosen
			int [] arrMults = null;
			arrMults = TimeInterval.multipliersForIntervalBase(
			unit_base, false, true );

 			__automation_tab_delay_int_JComboBox.removeAllItems();
			for ( int i=0; i<arrMults.length; i++ ) {
 				__automation_tab_delay_int_JComboBox.addItem(
				String.valueOf( arrMults[i] ) );
			}
		}
		else if ( source == __automation_tab_month_JComboBox ) {
			//update days_vect to have correct number of days.
			update_automation_days();
		}	
		else if ( source == __props_tab_import_window_unit_JComboBox ) {
			//update integers in multiplier list
			//get new item selected in unit JComboBox
			String unit_base = null;
			unit_base = (String) 
 			__props_tab_import_window_unit_JComboBox.
			getSelected();
			//update the list of TIme Step Multiplier
			//choices for the specific Base choosen
			int [] arrMults = null;
			arrMults = TimeInterval.multipliersForIntervalBase(
			unit_base, true, true );

 			__props_tab_import_window_int_JComboBox.
			removeAllItems();
			for ( int i=0; i<arrMults.length; i++ ) {
 				__props_tab_import_window_int_JComboBox.
				addItem( String.valueOf( arrMults[i] ) );
			}
		}

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
We care if the GUI is closing because we need to shut it down gracefully.
*/
public void windowClosing ( WindowEvent e ) {
	if ( __canWriteImportProduct ) {
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
Message.printStatus(1, "", "WindowManager Close");
	if (__bln_new_object) {
		_windowManager.closeWindowInstance(
			_windowManager.WINDOW_IMPORT_PRODUCT,
			"CREATING NEW OBJECT" );	
	}
	else {
		_windowManager.closeWindowInstance(
			_windowManager.WINDOW_IMPORT_PRODUCT,
			new Long(__db_ImportProduct_num));
	}
}

/////////////////////// END ACTIONS /////////////////////////////

}// end RiversideDB_Import_JFrame class
