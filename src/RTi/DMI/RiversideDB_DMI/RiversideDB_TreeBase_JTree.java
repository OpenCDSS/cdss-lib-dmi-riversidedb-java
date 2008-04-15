

// REVISIT [LT] - Protected members need to be renamed from __???? to _????. 
//		  They are incorrected named because originally they were all
//		  private, since the whole code was in the single 
//		  RTAssistent_Main_JFrame.java code.
// 2004-12-15 Luiz Teixeira.


//-----------------------------------------------------------------------------
// RiversideDB_TreeBase_JTree - Base class for the RiversideDB_?_JTree objects.
//-----------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
//-----------------------------------------------------------------------------
// History:
//
// 2004-11-22 Luiz Teixeira, RTi 	Original version derived from
//						RiversideDB_Import_JTree.
// 2004-12-14 Luiz Teixeira, RTi	Added method depopulateTree();
// 2004-12-15 Luiz Teixeira, RTi	Moved methods addProductGroupNode and 
//					              updateProductGroupNode
//					from the derived classes
//						RiversideDB_Import_JTree and
//						RiversideDB_Export_JTree 
//					to this base classes. There is no need
//					to override these methods because they
//					are the same for the import and export.
//					I also moved from these derived classes
//					(and renamed to be general) the
//					following members:						
//						__product_vect
//						__productGroup_vect
//						__canWriteProduct
//						__canWriteProductGroup
//-----------------------------------------------------------------------------
package RTi.DMI.RiversideDB_DMI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleJTree;
import RTi.Util.GUI.SimpleJTree_Node;
import RTi.Util.IO.IOUtil;
import RTi.Util.IO.LanguageTranslator;
import RTi.Util.Message.Message;

/**
Base class for the RiversideDB_?_JTree objects.
*/
abstract public class RiversideDB_TreeBase_JTree extends SimpleJTree
	implements ActionListener,
  		   KeyListener,
	           MouseListener,
	           RiversideDB_System_Listener
{

/**
Application title that will appear as part of the title bar of the editor
windows. It defaults to "", but it will be updated during construction to
contain the caller application name.
*/
protected String __app_title = "";

/**
Class name
*/
private static String __class = "RiversideDB_TreeBase_JTree";

/**
__treeString - Title of the tree (should be initialized in the derived classes..
__treeJScrollPane - JScrollPane that contain the JTree.
__treeTopNode - SimpleJTree_Node of the top mode of the JTree.
__treeJPopupMenu -
__closedFolderIcon - Icon representing a closed JTree folder.
*/
protected String           __treeString       = "?";
protected JScrollPane      __treeJScrollPane  = null;
protected SimpleJTree_Node __treeTopNode      = null;
protected JPopupMenu       __treeJPopupMenu   = null;
protected Icon             __closedFolderIcon = null;

// REVISIT [LT] A better explanation is required.
// String descriptions for JTree items.
// The object type is determined by the String descriptions that preceeds the
//	string in the tree. For instance:
//  	"Group: " preceeds "Import" Group names
String GRP_STRING        = "Group";
String PROD_STRING       = "Product";
String MEASLOCGRP_STRING = "Locations Group";
String ID_STRING         = "ID";
String TSID_STRING       = "TSID";
String TS_REDUCT_STRING  = "REDUCTION";
String TS_UNKNOWN_STRING = "UNKNOWN";
String TS_IMPORT_STRING  = "Import";

/**
Reference to the RiversideDB dmi connection.
*/
protected RiversideDB_DMI __dmi = null;

/**
Vectors of RiversideDB_* table objects. Used to check if user has permisssions
to write records to a particular table.
*/
protected Vector __riversideDB_TablesVector = null;

/**
Reference to the RiversideDB_WindowManager object that manages the windows of
the editors created rom this JTree.
*/
protected RiversideDB_WindowManager __windowManager = null;

/**
Holds RiversideDB_product and RiversideDB_productGroup objects used to populate
the JTrees for import and export product.
*/
protected Vector __product_vect      = null;
protected Vector __productGroup_vect = null;

/**
Write permissions flag. These flags are set during construction and are used
while adding or removing adding and removing the menus from the popup menus.
(in responce to mouseReleased event).
*/
protected boolean __canWriteProduct       = false;
protected boolean __canWriteProductGroup  = false;

/**
RiversideDB_TreeBase_JTree default constructor.
@param windowManager Reference to the RiversideDB_WindowManager object that
manages the windows of the editors created from the RiversideDB_?_JTree object
derived from this class.
@param dmi Reference to the RiversideDB dmi connection.
*/
public RiversideDB_TreeBase_JTree (
		RiversideDB_WindowManager windowManager,
		RiversideDB_DMI dmi )
{
	super();

	String routine = __class + ".constructor", mssg;

	// 1) Update the member __app_title to conatain the application name
	__app_title = JGUIUtil.getAppNameForWindows();

	// 3) Set instance for the RiversideDB_DMI
	__dmi = dmi;

	// 4) Set instance for the RiversideDB_WindowManager
	__windowManager = windowManager;
	
	if (Message.isDebugOn) {
		if (__windowManager == null ) {
			mssg = "Window manager is null";
		} else {
			mssg = "Window manager is not null";
		}
		Message.printWarning(2, routine, mssg );
	}

	// 5) __riversideDB_TablesVector - Used to check if user has
	//    permisssions to write records to a particular table.
	try {
		__riversideDB_TablesVector = __dmi.getRiversideDB_Tables();
	} catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		Message.printWarning( 2, routine,
			"Unable to get a list of Tables objects.");
		__riversideDB_TablesVector = new Vector();
	}

	// 6) The derived classes should initialize their __canWrite...
	//    instance members, assign the tree title to the __treeString and
	//    run their instances of the createTree and populateTree methods.
}

/**
Creates a standard JTree that will hold the information extract from the objects
supplied by the derived classes. For instance, for the RiversideDB_ImportJTree
object the object RiversideDB_ImportProduct and RiversideDB_ImportType will be
used to populate the JTree.
Must be implemented in the derived classes.
*/
protected void createTree()
{
}

/**
Remove all Children Nodes from the tree, leaving only the top Node
*/
public void depopulateTree()
{
	try {
		removeChildren( __treeTopNode );
		refresh();
	} catch ( Exception e ) {
		;
	}	
}

/**
Finalize.  Free memory for garbage collection.
@exception Throwable if there is an error.
*/
public void finalize()
throws Throwable
{
	__treeJScrollPane          = null;
	__treeTopNode              = null;
	__treeJPopupMenu           = null;
	__closedFolderIcon         = null;

	__dmi                      = null;
	__riversideDB_TablesVector = null;
	__windowManager            = null;
	
	__product_vect             = null;
	__productGroup_vect        = null;

	super.finalize();
}

/**
Returns a reference to the __treeString
@return a reference to the __treeString
*/
public String get_treeString()
{
	return __treeString;
}

/**
/**
Returns a reference to the __treeJScrollPane.
@return a reference to the __treeJScrollPane.
*/
public JScrollPane get_treeJScrollPane()
{
	return __treeJScrollPane;
}

/**
Creates vectors of RiversideDB_DMI objects that are used to create the JTree.
Must be implemented in the derived class.
*/
protected void initRiversideDB_DMI_Vectors()
{
}

/**
Updates global variable that indicate whether the user logged in
has permission to write records in specified table.  This is
used to determine what popup menu items are displayed on the JTree.
For instance, if user does not have permissions to write a MeasLoc object
to the database, the popup for "Add New MeasLoc" will not display.
*/
protected boolean initTableWritePermission ( String table )
{
	boolean wPermission = false;

	if ( __riversideDB_TablesVector != null ) {

		// Loop through the tables in the __riversideDB_TablesVector.
		RiversideDB_Tables dbTable = null;
		for ( int i = 0; i < __riversideDB_TablesVector.size(); i++ ) {

        		// Get the table at 'i'
			dbTable = ( RiversideDB_Tables )
				__riversideDB_TablesVector.elementAt(i);

			// If our table, check for the write permission
			if ( (dbTable != null) &&
			     (table.equalsIgnoreCase(dbTable.getTable_name()))){
				wPermission = RiversideDB_Util.canWrite (
					__dmi, dbTable );
			}
		}
	}

	return wPermission;
}

// REVISIT [LT] - Not used in the original (pre 03.00.00) code. Ignored during
//                this upgrade (2004-12-08).  Revisit ASAP.
/**
Currently NOT USED ...
If there is a key: "Language" in the configuration file,
the GUI will loook for a translation file for that language.
For example: if LANGUAGE is set to: "Spanish", the GUI
will look in the configuration file for the name and
location of the translation file, specified by the
"TranslationFile" key in the config file.
*/
protected boolean init_translationTable()
{
	String routine = __class + ".init_translationTable";

	//holds name for language - retrieved from config file
	String lang = null;
	lang = IOUtil.getPropValue( "LANGUAGE" );
	if( ( lang == null ) || ( lang.equalsIgnoreCase( "English" )) ) {
		//don't do anything b/c all the defaults are
		//english and we have No tranlsation file if
		//we are using English.

		Message.printStatus( 20 , routine,
			"Using Language: \"English\"" );

		return false;
	}

	//Language must not be english, so let's see where
	//the translation file is.

	//print out language used.
	Message.printStatus( 2, routine,
		"Using Language: \"" + lang + "\"." );

	//holds name retrieved from config__file for lookup table
	String trans_table = null;

	//hold name of base path to translation file (not including file name
	//itself )
	boolean valid_trans__file = true;

	trans_table = IOUtil.getPropValue( "TRANSLATION_FILE" );

	if ( trans_table == null ) {
		//then there is no user specified  translation table,
		//so use the English version
		valid_trans__file = false;
		Message.printStatus( 2, routine,
			"No translation table to use." );

		return false;
	}

	//now make sure translation table is valid and usable
	if (( valid_trans__file )  &&
	( IOUtil.fileExists( trans_table ) )) {

		//set boolean to inidicate a translation table is used.
		valid_trans__file = true;

		Message.printStatus( 4, routine,
			"Translation table to use for translations: \"" +
		trans_table + "\"." );

		//create an instance of LanguageTranslator
		//so that it reads in and holds the information
		//in the translation vector and can be
		//queried to get the needed translations.
		LanguageTranslator lg = null;
		try {
			lg = new LanguageTranslator(
			trans_table );
		}
		catch ( Exception e ) {
			Message.printWarning( 3, routine,
				"Unable to use translation file: \"" +
				trans_table + "\", will default to " +
				"English." );
		}

		//set Translator so can be know throughout the
		//entire application!!!!!!!!!
		LanguageTranslator.setTranslator( lg );

		//set it in StringUtil so that it is known to it.
		if ( lg != null ) {
		    LanguageTranslator.setTranslator( lg );
		}
		else {
			valid_trans__file = false;
		}

	} //end if IOUtil.fileExists

	else {
		Message.printWarning( 2, routine,
			"Supposed to use translation table: \"" +
			trans_table + "\", but the file is unreadable. " +
			"Will use English. " );

		valid_trans__file = false;
	}
	return valid_trans__file;
}

/**
Fills in the tree with the actual data, using the permissions from the DBUser to
determine what should be displayed in the JTree.
Should be implemented in the derived classes. Make sure the last line of these
method is refresh();
*/
abstract public void populateTree();

/**
Remove all nodes and refresh.
*/
public void refreshTree()
{
	try {
		removeChildren( __treeTopNode );
		populateTree();
	} catch ( Exception e ) {
		;
	}
}


//============================================================================//
//                  Action events from the Tree Popup menus                   //
//              Method must be overrided in the derived classes               //                                                           //
//============================================================================//

/**
The event handler manages action events.
@param event Event to handle.
*/
public void actionPerformed (ActionEvent event)
{
}


//============================================================================//
//                                Key events                                  //
// REVISIT [LT]- May not be required.                                         //                                                          //
//============================================================================//

/**
Respond to a key pressed event.  Most single-key events are handled in 
keyReleased to prevent multiple events. Do track when the shift is pressed here.
@param event Event code.
*/
public void keyPressed ( KeyEvent event )
{
	int code = event.getKeyCode();

	if ( code == KeyEvent.VK_SHIFT ) {
		JGUIUtil.setShiftDown ( true );
	}
	else if ( code == KeyEvent.VK_CONTROL ) {
		JGUIUtil.setControlDown ( true );
	}
}

/**
Respond to a key released event.
@param event Event code.
*/
public void keyReleased ( KeyEvent event )
{
	int code = event.getKeyCode();

	if ( code == KeyEvent.VK_ENTER ) {
	}
	else if ( code == KeyEvent.VK_DELETE ) {
		// Clear an expression (need to determine which list it
		// comes from but for now assume expressions)...
	}
	else if ( code == KeyEvent.VK_SHIFT ) {
		JGUIUtil.setShiftDown ( false );
	}
	else if ( code == KeyEvent.VK_CONTROL ) {
		JGUIUtil.setControlDown ( false );
	}
}

/**
Respond to a key typed event.
@param event Event code.
*/
public void keyTyped ( KeyEvent event )
{
}


//============================================================================//
//                               Mouse events                                 //
//       Methods should be overrided in the derived classes when needed       //                                                          //
//============================================================================//

/**
Handle mouse clicked event.
@param event Event code.
*/
public void mouseClicked ( MouseEvent event )
{
}

/**
Handle mouse entered event.
@param event Event code.
*/
public void mouseEntered ( MouseEvent event )
{
}

/**
Handle mouse exited event.
@param event Event code.
*/
public void mouseExited ( MouseEvent event )
{
}

/**
Handle mouse pressed event.
@param event Event code.
*/
public void mousePressed ( MouseEvent event ) {
}

/**
Handle mouse released event. This is used to add and remove the appropriate
menus for the popup menus that appear on the JTrees.
@param event Event code.
*/
public void mouseReleased ( MouseEvent event )
{
}


//============================================================================//
//                  RiversideDB_System_Listener implementation                //
//         Methods may be overrided in the derived classes when needed        //
//============================================================================//

/**
Implement in the derived class if needed.
*/
public void addExportProductNode ( String parent, String child,
				   RiversideDB_ExportProduct newProduct )
{
}

/**
Implement in the derived class if needed.
*/
public void addImportProductNode( String parent, String child,
				  RiversideDB_ImportProduct newProduct  )
{
}

/**
Implement in the derived class if needed.
*/
public void addMeasLocGroupNode ( RiversideDB_MeasLocGroup newMeasLocGroup )
{
}

/**
Implement in the derived class if needed.
*/
public void addMeasLocNode( String grandParent, RiversideDB_MeasLoc newMeasLoc )
{
}

/**
Implement in the derived class if needed.
*/
public void addMeasTypeNode ( RiversideDB_MeasType newMeasType )
{
}

/**
Add a new Product Group node to the RiversideDB_Export_JTree.
@param new_ProductGroup Reference to the new RiversideDB_ProductGroup to add.
*/
public void addProductGroupNode( RiversideDB_ProductGroup new_ProductGroup )
{
	String routine = __class + ".addProductGroupNode";

	if ( Message.isDebugOn ) {
		Message.printDebug( 4, routine,
			"Processing for type: '"
			+ new_ProductGroup.getProductType()
			+ "' and name: '"
			+ new_ProductGroup.getName() + "'." );
	}

	// Get name
	String name = null;
	name = new_ProductGroup.getName();

	SimpleJTree_Node parent_node= findNodeByName( __treeString );
	if ( parent_node == null ) {
		Message.printWarning( 2, routine,
			"Unable to identify parent of child: \"" + name
			+ "\". Will not update the tree, but will try to "
			+ "update database." );
	}

	// We know the parent node is the top level node, make child node
	SimpleJTree_Node child_node =
		new SimpleJTree_Node ( GRP_STRING + ": " + name );
	child_node.setData(   new_ProductGroup );
	child_node.setIcon( __closedFolderIcon );

	if ( parent_node != null ) {
		try {
			Message.printStatus( 5, routine,
				"Trying to add child node: \"" + name + "\""
				+ " to parent node: \"" + parent_node.getName()
				+ "\" to tree." );
			addNode( child_node, parent_node );
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e );
		}

		// Refresh, select added node
		refresh();
		expandNode( child_node );
		child_node.setIcon( __closedFolderIcon );
	}

	// Update vector of ProductGroups. Re-read the ProductGroup vector so
	// that the vector will be updated with new ProductGroup.
	try {
		__productGroup_vect = __dmi.
			readProductGroupListForProductType( "EXPORT" );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		__productGroup_vect = new Vector();
	}
}

/**
Implement in the derived class if needed.
*/
public void addUpdatedReductionNode( RiversideDB_MeasType oldMeasType,
				     RiversideDB_MeasType newMeasType )
{
}

/**
Implement in the derived class if needed.
*/
public void updateExportProductNode ( RiversideDB_ExportProduct newExportProduct,
				      String existing_name )
{
}

/**
Implement in the derived class if needed.
*/
public void updateImportProductNode( RiversideDB_ImportProduct newImportProduct,
				     String existing_name )
{
}

/**
Implement in the derived class if needed.
*/
public void updateMeasLocGroupNode ( RiversideDB_MeasLocGroup newMeasLocGroup,
				     RiversideDB_MeasLocGroup oldMeasLocGroup )
{
}

/**
Implement in the derived class if needed.
*/
public void updateMeasLocNode ( RiversideDB_MeasLoc newMeasLoc,
			        RiversideDB_MeasLoc oldMeasLoc )
{
}

/**
Implement in the derived class if needed.
*/
public void updateMeasTypeNode( RiversideDB_MeasType newMeasType,
				RiversideDB_MeasType oldMeasType )
{
}

/**
Updates a Product Group node in the RiversideDB_Export_JTree.
@param RiversideDB_ProductGroup new_ProductGroup - Reference to the new
RiversideDB_ProductGroup object to be used to update the node.
@param existing_name - String identifier of the Product Group node to update.
*/
public void updateProductGroupNode( RiversideDB_ProductGroup pg_new,
				    String existing_child )
{
	String routine = __class + ".updateProductGroupNode";

	if ( Message.isDebugOn ) {
		Message.printDebug( 2, routine,
			" Called with existing_child: '"+ existing_child+ "'" );
	}

	// Existing child node that we need to find and change
	SimpleJTree_Node existing_node = null;
	existing_node = findNodeByName( GRP_STRING + ": " + existing_child );

	SimpleJTree_Node new_node = new SimpleJTree_Node(
		GRP_STRING + ": " + pg_new.getIdentifier() );
	new_node.setData( pg_new );
	new_node.setIcon( __closedFolderIcon );
	try {
		replaceNode( existing_node, new_node );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine,
			"Unable to update "
			+ "Product Group node in tree, but will still update "
			+ "database, so next time application is re-started, "
			+ "node should be updated in tree.  "
			+ "See log for error details." );
		Message.printWarning( 2, routine, e );
	}

	// Refresh and scroll to the visible node
	refresh();
	scrollToVisibleNode( new_node );
}

/**
Implement in the derived class if needed.
*/
public void updateReductionNode ( RiversideDB_MeasType oldMeasType,
				  RiversideDB_MeasType newMeasType )
{
}

}
//---------------------------------------------------------------------------//
