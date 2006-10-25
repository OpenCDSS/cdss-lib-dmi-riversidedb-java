

// REVISIT [LT] 2004-12-15 - Protected members need to be renamed from __???? to _????. 
//		  They are incorrected named because originally they were all
//		  private, since the whole code was in the single 
//		  RTAssistent_Main_JFrame.java code.


// REVISIT [LT] 2004-11-30 Bug - __measLocGroup_vect - There is a bug in this code.
//                    See all REVISIT associated with __measLocGroup_vect


//------------------------------------------------------------------------------
// RiversideDB_Export_JTree -  - Creates an Export SimpleJTree object.
//------------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
//------------------------------------------------------------------------------
// History:
//
// 2004-11-22 Luiz Teixeira, RTi 	Original version derived from
//					RTAssistant_Main_Frame.
// 2004-11-30 Luiz Teixeira, RTi	Implemented RiversideDB_System_Listener
// 2004-12-08 Luiz Teixeira, RTi	Cleanup while synchronizing the code
//					against the similar classes
//						RiversideDB_Import_JTree
//						RiversideDB_MeasLoc_JTree
// 2004-12-08 Luiz Teixeira, RTi	Add __popup_..._String members matching
//						all __popup_..._JMenuItem
//						members. Revise these members
//						names, making them consistent
//						and compatible between all three
//						RiversideDB_..._JTree classes.
// 2004-12-09 Luiz Teixeira, RTi	Now extending from the base class
//						RiversideDB_TreeBase_JTree
//					Additional cleanup and documentation.
// 2004-12-10 Luiz Teixeira, RTi	Added the 'boolean populate' parameter 
//					to the constructor method. If this para
//					meter is true, the tree will be popula-
//					ted during construction, otherwise the
//					caller object should populate the tree
//					afterwards by running the public method
//					RiversideDB_Export_JTree.populateTree()
// 2004-12-15 Luiz Teixeira, RTi	Moved methods addProductGroupNode and 
//					              updateProductGroupNode
//					to the base class
//						RiversideDB_TreeBase_JTree 
//					There is no need to override these
//					methods here because they are the same
//					for the import and export.
//					I also moved to the base class
//					(and renamed to be general) the
//					following members:						
//						__exportProduct_vect
//						__exportProductGroup_vect
//						__canWriteExportProduct
//						__canWriteExportProductGroup
//-----------------------------------------------------------------------------
package RTi.DMI.RiversideDB_DMI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.lang.Thread;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.tree.TreeSelectionModel;

import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleJMenuItem;
import RTi.Util.GUI.SimpleJTree;
import RTi.Util.GUI.SimpleJTree_Node;
import RTi.Util.IO.IOUtil;
import RTi.Util.Message.Message;

/**
Creates an Export SimpleJTree object.
*/
public class RiversideDB_Export_JTree extends RiversideDB_TreeBase_JTree
{

/**
Class name
*/
private static String __class = "RiversideDB_Export_JTree";

/**
Main PopupMenus
*/
private SimpleJMenuItem __popup_AddExportProduct_JMenuItem        = null;
private SimpleJMenuItem __popup_DeleteExportProduct_JMenuItem     = null;
private SimpleJMenuItem __popup_ExportProductProperties_JMenuItem = null;

private SimpleJMenuItem __popup_AddExportGroup_JMenuItem          = null;
private SimpleJMenuItem __popup_DeleteExportGroup_JMenuItem       = null;
private SimpleJMenuItem __popup_ExportGroupProperties_JMenuItem   = null;

private String __popup_AddExportProduct_String     =       "Add Export Product";
private String __popup_DeleteExportProduct_String  =    "Delete Export Product";
private String __popup_ExportProductProperties_String
						   ="Export Product Properties";

private String __popup_AddExportGroup_String       =         "Add Export Group";
private String __popup_DeleteExportGroup_String    =      "Delete Export Group";
private String __popup_ExportGroupProperties_String=  "Export Group Properties";

// REVISIT [LT] associated with __measLocGroup_vect
/**
Holds MeasLocGroup objects NOT USED??? to populate the JTree.
*/
private Vector __measLocGroup_vect = null;

/**
RiversideDB_Export_JTree default constructor.
@param dmi Reference to the RiversideDB dmi connection.
@param windowManager Reference to the RiversideDB_WindowManager object that
manages the windows of the editors created rom this JTree
@param populate Flag indicating if the tree is to be populated during
construction (true) or if it will be populated afterwards, by the caller,
running the public method populateTree() 
*/
public RiversideDB_Export_JTree ( RiversideDB_DMI dmi,
				  RiversideDB_WindowManager windowManager,
				  boolean populate )
{
	// 1) The base class will take care of the initialization of the
	//    instance members __app_title, __dmi, __windowManager and
	//    __riversideDB_TablesVector
	super( windowManager, dmi );

	// 2) Assign the tree title to the __treeString instance member.
	__treeString = "Exports";

	// 3) Initialize the write-permission instance members
	__canWriteProduct       = initTableWritePermission ( "ExportProduct" );
	__canWriteProductGroup  = initTableWritePermission (  "ProductGroup" );

	// 4) Creates empty tree.
	createTree();

	// 5) Populate the tree.
	if ( populate ) populateTree();
}

/**
Creates a standard JTree that will hold the RiversideDB_ExportProduct and
RiversideDB_ExportType.
*/
protected void createTree()
{
	String routine = __class + ".createTree";

	new SimpleJTree();
	setTreeTextEditable( false );

	// Create top node
	__treeTopNode = new  SimpleJTree_Node( __treeString );

	// Get closed folder icon
	if ( __closedFolderIcon == null ) {
		__closedFolderIcon = getClosedIcon();
	}

	// Set it to support SINGLE Selection ONLY
	getSelectionModel().setSelectionMode(
		TreeSelectionModel.SINGLE_TREE_SELECTION );

	// Add top mode
	try {
		addNode( __treeTopNode );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		if ( Message.isDebugOn ) {
			Message.printDebug( 1, routine,
				"Error adding top node to export JTree." );
		}
	}

	// Now add a popup menu to the tree.
	__treeJPopupMenu = new JPopupMenu();
	__treeJPopupMenu.setDefaultLightWeightPopupEnabled( false );

	// Menu items: define properties, add new product, delete product
	__popup_AddExportProduct_JMenuItem = new SimpleJMenuItem(
		__popup_AddExportProduct_String, this );

	__popup_DeleteExportProduct_JMenuItem = new SimpleJMenuItem(
		__popup_DeleteExportProduct_String, this );

	__popup_ExportProductProperties_JMenuItem = new SimpleJMenuItem(
		__popup_ExportProductProperties_String, this );

	__popup_AddExportGroup_JMenuItem = new SimpleJMenuItem(
		__popup_AddExportGroup_String, this );

	__popup_DeleteExportGroup_JMenuItem = new SimpleJMenuItem(
		__popup_DeleteExportGroup_String, this );

	__popup_ExportGroupProperties_JMenuItem = new SimpleJMenuItem(
		__popup_ExportGroupProperties_String, this );

	// Add tree to scrollpane
	__treeJScrollPane = new JScrollPane( this );
	__treeJScrollPane.setVerticalScrollBarPolicy(
		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
	__treeJScrollPane.setPreferredSize( new Dimension( 250, 500 ) );
	__treeJScrollPane.setBorder(
		BorderFactory.createCompoundBorder(
			BorderFactory.createEmptyBorder( 5, 5, 5, 5 ),
			__treeJScrollPane.getBorder() ) );

	// Add Listener
	addMouseListener( this );
}

/**
Finalize.  Free memory for garbage collection.
@exception Throwable if there is an error.
*/
public void finalize()
throws Throwable
{
	__popup_AddExportProduct_JMenuItem        = null;
	__popup_DeleteExportProduct_JMenuItem     = null;
	__popup_ExportProductProperties_JMenuItem = null;

	__popup_AddExportGroup_JMenuItem          = null;
	__popup_DeleteExportGroup_JMenuItem       = null;
	__popup_ExportGroupProperties_JMenuItem   = null;

	// REVISIT [LT] associated with __measLocGroup_vect
	__measLocGroup_vect                      = null;

	super.finalize();
}

/**
Creates vectors of RiversideDB_DMI objects that are used to create the JTree.
*/
protected void initRiversideDB_DMI_Vectors()
{
	String routine = __class + ".initRiversideDB_DMI_Vectors";

 	// Get List of Import ProductGroups
	try {
		__product_vect = __dmi.readExportProductList();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine,
			"Unable to get a Vector of ExportProduct objects. "+ e);
		__product_vect =  new Vector();
	}

	// Get List of Export ProductGroups
	try {
		__productGroup_vect = __dmi.
			readProductGroupListForProductType( "EXPORT" );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine,
			"Unable to get a Vector of Export ProductGroups. "+ e);	
		__productGroup_vect = null;
	}
}

/**
Fills in the Export Tree with the actual data for RiversideDB_ExportType and
RiversideDB_ExportProducts, using the permissions from the DBUser to
determine what should be displayed in the JTree.
*/

public void populateTree()
{
	String routine = __class + ".populateTree";

	// Initialize the RiversideDB_DMI Vectors.
	initRiversideDB_DMI_Vectors();

	// Determine the sizes of the RiversideDB_DMI Vectors.
	int obj_size = 0;
	if ( __product_vect != null ) {
		obj_size = __product_vect.size();
	}
	int grp_size = 0;
	if ( __productGroup_vect != null ) {
		grp_size = __productGroup_vect.size();	
	}

	// ...
	String grp  = null;
	String prod = null;
	SimpleJTree_Node grp_node   = null;
	SimpleJTree_Node prod_node  = null;
	RiversideDB_ProductGroup pg = null;
	int top_grp_num =-999;
	for ( int g=0; g<grp_size; g++ ) {
		pg = (RiversideDB_ProductGroup)
			__productGroup_vect.elementAt(g);
		if ( pg == null ) continue;

		boolean canReadProductGroup = false;
		try {
			canReadProductGroup = __dmi.canRead(
				pg.getDBUser_num(), pg.getDBGroup_num(),
			pg.getDBPermissions() );
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e);
		}
		if ( Message.isDebugOn ) {
			Message.printDebug( 3, routine,
			"dmi.canRead() returned \"" + canReadProductGroup +
			"\" for ProductGroup\n" + pg.getName() +
			" ( ProductGroup_num= "+pg.getProductGroup_num()+ ") " +
			"\nfor user with DBUser_num: \"" +
			pg.getDBUser_num() + "\" and DBGroup_num: \"" +
			pg.getDBGroup_num() + " \" and DBPermissions: \"" +
			pg.getDBPermissions() +"\"" );
		}

		grp = pg.getName();
		top_grp_num = pg.getProductGroup_num();

		grp_node = new SimpleJTree_Node( GRP_STRING + ": " + grp );
		grp_node.setData( pg );

		// Set icon
		grp_node.setIcon( __closedFolderIcon );

		if ( canReadProductGroup ) {
			try {
				addNode( grp_node, __treeTopNode);
			}
			catch ( Exception e ) {
				Message.printWarning( 2, routine, e );
			}
		}
		else {
			continue;
		}

		// Expand the tree so that all groups are displayed
		expandNode( grp_node );

		// Now loop and add any products that fall within that grp type.
		int grp_num = -999;

		RiversideDB_ExportProduct ep = null;
		for ( int p=0; p< obj_size; p++ ) {
			ep = (RiversideDB_ExportProduct)
				__product_vect.elementAt(p);
			if ( ep == null ) {
				continue;
			}
			boolean canReadExportProduct = false;
			try {
				canReadExportProduct = __dmi.canRead(
					ep.getDBUser_num(),
					ep.getDBGroup_num(),
					ep.getDBPermissions());
			}
			catch ( Exception e ) {
				Message.printWarning( 2, routine, e);
			}
			if ( Message.isDebugOn ) {
				Message.printDebug( 3, routine,
				"dmi.canRead() returned \"" +
				canReadExportProduct +
				"\" for ExportProduct\n" +
				ep.getProduct_name()+
				" ( ExportProduct_num= " +
				ep.getExportProduct_num()+ ") " +
				"\nfor user with DBUser_num: \"" +
				ep.getDBUser_num() + "\" and DBGroup_num: \"" +
				ep.getDBGroup_num() +
				" \" and DBPermissions: \"" +
				ep.getDBPermissions() +"\"" );
			}

			grp_num = ep.getProductGroup_num();
			if( top_grp_num == grp_num ) {
				// Add that product name to the grp node
				prod_node = new SimpleJTree_Node(
					PROD_STRING
					+ ": "
					+ ep.getProduct_name()
					+ " (type: "
					+ ep.getProduct_type()
					+ ")" );
				prod_node.setData(ep);

				if ( canReadExportProduct ) {
					// Add it!
					try {
						addNode( prod_node, grp_node );
					}
					catch ( Exception e ) {
						Message.printWarning(
							2, routine, e );
					}
				}
				else {
					continue;
				}
			}
		}
		pg = null;
	}

	// Refresh ( not necessary )
	// refresh();
}


//============================================================================//
//              Action events from the Export Tree Popup menus                //                                                          //
//============================================================================//

/**
The event handler manages action events.
@param event Event to handle.
*/
public void actionPerformed (ActionEvent event)
{
	String routine = __class + ".actionPerformed";

	try {
	String command = event.getActionCommand();
	Object source  = event.getSource();

	if ( source == __popup_DeleteExportProduct_JMenuItem  ) {

		// Get name of ExportProduct selected
		SimpleJTree_Node prod_node = getSelectedNode();

		// Get Data
		RiversideDB_ExportProduct ep = (RiversideDB_ExportProduct)
		prod_node.getData();

		// Get ExportProduct num for item selected.
		long ep_num = ep.getExportProduct_num();

		// See if user has delete permission
		boolean canDeleteExportProduct=false;

		try {
			canDeleteExportProduct = __dmi.canDelete(
				ep.getDBUser_num(),
				ep.getDBGroup_num(),
				ep.getDBPermissions() );
		}
		catch( Exception e ){
			Message.printWarning( 2, routine, e );
		}

		if ( !canDeleteExportProduct ) {
			Message.printWarning( 1, routine,
				"You do not have permisssions to delete " +
				"this Export Product." );
		}
		// Can call delete!
		else if ( ep_num >= 0 ) {
			int confirm = JOptionPane.showConfirmDialog(
					this,
					"Delete Export \""
					+ prod_node.getName()
					+ "\" and "
					+ "its export configuration?",
					"Confirm Deletion of Export "
					+ prod_node.getName(),
					JOptionPane.YES_NO_OPTION );

			boolean blnDeleted = true;
			// Delete ExportProductForExportProduct_num
			// returns an array of ints.
			// at [0]= integer representing the number of
			// records deleted from ExportProduct table
			// at[1]= integer representing the number of
			// records deleted from ExportConf table

			int[] arrDeletedRecords = null;
			if ( confirm == JOptionPane.OK_OPTION ) {
				try {
					arrDeletedRecords = __dmi.
					deleteExportProductForExportProduct_num(
						(int)ep_num );
				}
				catch ( Exception e ) {
					Message.printWarning( 2,"", e );
					Message.printWarning( 2,"",
						"Unable to delete selected "
						+ "ExportProduct: \""
						+ prod_node.getName()
						+ " \"."
						+ " (Database error)");
					blnDeleted=false;
				}
				if ( Message.isDebugOn ) {
					if ( arrDeletedRecords != null ) {
						Message.printStatus( 3, routine,
						"Number of ExportProduct " +
						"records deleted = " +
						arrDeletedRecords[0] +
						" and number of related " +
						"ExportConf records deleted = "+
						arrDeletedRecords[1] );
					}
				}
				if ( blnDeleted ) {
					// REMOVE that node from the tree.
					removeNode( prod_node, false);

					//Re-read database to create
					//new Vector of Export Products
					try {
						__product_vect =	
						__dmi.readExportProductList();
					}
					catch ( Exception e ) {
						Message.printWarning(
							2, routine, e );

						//create an empty vector
						__product_vect = new Vector();
					}
				}
			} //end if OK

		} //end if ep_num > 0
	}

	else if ( source == __popup_AddExportGroup_JMenuItem ) {

		// Create interface for adding a new ProductGroup for Exports
		// Pass in EXPORT for the type.
		RiversideDB_ProductGroup pg = new RiversideDB_ProductGroup();
		pg.setProductType( "EXPORT" );

		RiversideDB_ProductGroup_JFrame pgf = null;
		if ( __windowManager == null ) {
			// Simple open.
			pgf = new RiversideDB_ProductGroup_JFrame(
					__dmi,
					__windowManager,
					__app_title
						+ " - New Export Product Group",
					pg );
		} else {
			pgf =  (RiversideDB_ProductGroup_JFrame)
				__windowManager.displayWindow(
				__windowManager.WINDOW_PRODUCTGROUP,
				true,
				__app_title + " - New Export Product Group",
				pg,
				null);
		}
		pgf.addRiversideDBSystemListener (this);
	}

	else if ( source == __popup_DeleteExportGroup_JMenuItem ) {

		// Get node and determine which ExportProductGroup it represents
		SimpleJTree_Node grp_node = getSelectedNode();
		// Get data
		RiversideDB_ProductGroup pg = (RiversideDB_ProductGroup)
						grp_node.getData();

		boolean canDeleteExportProduct = false;
		boolean canDeleteProductGroup = false;
		// Check Delete permissions
		try {
			canDeleteProductGroup = __dmi.canDelete(
				pg.getDBUser_num(),
				pg.getDBGroup_num(),
				pg.getDBPermissions() );
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e );
			canDeleteProductGroup = false;
		}

		if ( !canDeleteProductGroup ) {
			Message.printWarning( 1, routine,
				"You do not have permisssions to delete "
				+ "this Export Product Group." );
		}
		else if ( pg != null ) {
			int confirm = JOptionPane.showConfirmDialog(
				this,
				"Delete Export Product Group: \""
				+ grp_node.getName()
				+ "\" and "
				+ "its Export Products and time series?",
				"Confirm Deletion of Product Group "
				+ grp_node.getName(),
				JOptionPane.YES_NO_OPTION );

			int[] arrDels = null;
			boolean blnDeleted = true;
			if ( confirm == JOptionPane.OK_OPTION ) {
				try {
				   arrDels = __dmi.
				   deleteExportProductGroupForProductGroup_num(
					pg.getProductGroup_num() );
				}
				catch ( Exception e ) {
					Message.printWarning( 2, routine, e );
				}

				// REMOVE that node from the tree
				removeNode( grp_node, false);

				Message.printStatus( 10, routine,
				     arrDels[0]
				   + " records deleted from ProductGroup, "
				   + arrDels[1]
				   + " records deleted from ExportProduct and "
				   + arrDels[2]
				   + " records deleted from ExportConf." );
			}
		}
	}

	else if ( source == __popup_ExportGroupProperties_JMenuItem ) {

		// Get Export Product Group selected.
		SimpleJTree_Node prodgrp_node = getSelectedNode();

		// Get name of Product Group to use in title
		String name = prodgrp_node.getName();
		String stripped_name = null;
		// Remove the preceeding "Product:" tag
		if ( name.regionMatches(
			true, 0, GRP_STRING, 0, GRP_STRING.length() ) ) {
			// remove the PROD: from the string
			stripped_name = (name.substring(
				GRP_STRING.length() + 1)).trim();
		}

		// Make new instance of ExportJFrame w/ these items preselected.
		RiversideDB_ProductGroup pg = (RiversideDB_ProductGroup)
			prodgrp_node.getData();
		Long id = new Long(pg.getProductGroup_num());

		RiversideDB_ProductGroup_JFrame pgf = null;
		if ( __windowManager == null ) {
			// Simple open
			pgf = new RiversideDB_ProductGroup_JFrame(
					__dmi,
					__windowManager,
					__app_title
						+ " - Export Product Group - "
						+ stripped_name,
					pg );
		} else {
			// Open via the RiversideDB_WindowManager.
			pgf = (RiversideDB_ProductGroup_JFrame)
				__windowManager.displayWindow(
				__windowManager.WINDOW_PRODUCTGROUP,
				true,
				__app_title
					+ " - Export Product Group - "
					+ stripped_name,
				pg,
				id );
		}
		pgf.addRiversideDBSystemListener (this);
	}

	else if ( source ==  __popup_ExportProductProperties_JMenuItem  ) {

		// Get Export Product Group selected.
		SimpleJTree_Node prod_node = getSelectedNode();

		// Get name used in title bar of export product GUI
		String stripped_prod = null;
		String name = prod_node.getName();
		// Remove the preceeding "Product: " tag
		if ( name.regionMatches(
			true, 0, PROD_STRING, 0, PROD_STRING.length() ) ) {
			stripped_prod= name.substring(PROD_STRING.length() + 1);
		}

		// Make new instance of ExportJFrame w/ these items preselected.
		RiversideDB_ExportProduct ep = (RiversideDB_ExportProduct)
			prod_node.getData();

		Long id = new Long(ep.getExportProduct_num());

		RiversideDB_Export_JFrame ipg = null;
		if ( __windowManager == null ) {
			// Simple open.
			ipg = new RiversideDB_Export_JFrame(
				__dmi,
				__windowManager,
				__app_title + " - Export Product - "
					+ stripped_prod,
				(RiversideDB_ExportProduct)
					prod_node.getData() );
		} else {
			// Open via the RiversideDB_WindowManager.
			ipg = (RiversideDB_Export_JFrame)
				__windowManager.displayWindow(
				__windowManager.WINDOW_EXPORT_PRODUCT,
				true,
				__app_title + " - Export Product - "
					+ stripped_prod,
				ep,
				id);
		}
		ipg.addRiversideDBSystemListener (this);
	}

	else if ( source == __popup_AddExportProduct_JMenuItem ) {

		// Get the node selected, which should be the Group name
		SimpleJTree_Node prodgrp_node = getSelectedNode();

		String stripped_grp = null;
		String name = prodgrp_node.getName();
		// Remove the string from the front of the group name: "GRP:"
		if ( name.regionMatches(
			true, 0, GRP_STRING, 0, GRP_STRING.length() ) ) {
			stripped_grp = name.substring( GRP_STRING.length() +1 );
		}

		// Get ProductGroup
		RiversideDB_ProductGroup pg = (RiversideDB_ProductGroup)
			prodgrp_node.getData();

		// REVISIT [LT] associated with __measLocGroup_vect
		// What this block of code is supose to do? num is set to 0, so
		// the "if ( num > 0 )" block will never execute and mlg_num
		// will always be -999.
		// ????????????????????????????????????????????????????????????
		// Need to assign a (at least temporary) MeasLocGroup_num to
		// the ExportProduct, so do now, using the first entry
		int num = 0;
		if ( __measLocGroup_vect != null ) {
			__measLocGroup_vect.size();
		}
		int mlg_num = -999;
		RiversideDB_MeasLocGroup mlg = null;
		if ( num > 0 ) {
			mlg = (RiversideDB_MeasLocGroup)
				__measLocGroup_vect.elementAt(0);
			mlg_num = mlg.getMeasLocGroup_num();
		}
		mlg = null;
		// ????????????????????????????????????????????????????????????
		// REVISIT [LT] associated with __measLocGroup_vect

		RiversideDB_ExportProduct ep = new RiversideDB_ExportProduct();
		ep.setProductGroup_num( pg.getProductGroup_num() );
		ep.setProduct_group   ( pg.getIdentifier() );
		ep.setProduct_name    ( pg.getName() );
		ep.setMeasLocGroup_num( mlg_num );

		RiversideDB_Export_JFrame ipg = null;
		if ( __windowManager == null ) {
			// Simple open.
			ipg = new RiversideDB_Export_JFrame(
				__dmi,
				__windowManager,
				__app_title + " - Export Product - "
					    + "New Export Product",
				ep );
		} else {
			ipg = (RiversideDB_Export_JFrame)
				__windowManager.displayWindow(
					__windowManager.WINDOW_EXPORT_PRODUCT,
					true,
					__app_title + " - Export Product - "
						+ "New Export Product",
					ep);
		}
		ipg.addRiversideDBSystemListener (this);
	}

	} catch ( Exception e ) {
		if ( Message.isDebugOn ) {
			Message.printWarning ( 2, routine, e );
		}
		JGUIUtil.setWaitCursor ( this, false );
	}
}


//============================================================================//
//              Mouse events                                                  //                                                          //
//============================================================================//

/**
Handle mouse released event. This is used to add and remove the appropriate
menus for the popup menus that appear on the JTrees.
@param event Event code.
*/
public void mouseReleased ( MouseEvent event )
{
	Object source = null;
	source = event.getSource();

	if ( source == this ) {

		SimpleJTree_Node node = getSelectedNode();
		if ( node == null ) return;

		// Product
		if ( (node.getName()).regionMatches(
			true, 0, PROD_STRING, 0, PROD_STRING.length() ) ) {

			// When a PRODUCT is selected, the menu items should be:
			// "PROPERTIES" and "DELETE"

			// Remove all menu items
			__treeJPopupMenu.removeAll();

			__treeJPopupMenu.add(
				__popup_ExportProductProperties_JMenuItem );

			__treeJPopupMenu.add(
				__popup_DeleteExportProduct_JMenuItem );

		}

		// Group
		if ( (node.getName()).regionMatches(
			true, 0, GRP_STRING, 0, GRP_STRING.length() ) ) {
			// Then we have a GROUP selected, show
			// the menu items adding a NEW PRODUCT

			// Remove all menu items
			__treeJPopupMenu.removeAll();

			// Add properties for Export Product Group
			__treeJPopupMenu.add(
				__popup_ExportGroupProperties_JMenuItem );

			if ( __canWriteProduct )  {
				__treeJPopupMenu.add(
					__popup_AddExportProduct_JMenuItem );
			}

			// Add delete
			__treeJPopupMenu.add(
				__popup_DeleteExportGroup_JMenuItem );

		}

		// Export - Top of the Tree
		if ( (node.getName() ).regionMatches(
			true, 0, __treeString, 0, 4 ) ) {

			// Remove all menu items
			__treeJPopupMenu.removeAll();

			if ( __canWriteProductGroup )  {
				// Add ADD NEW Export GROUP menu
				__treeJPopupMenu.add(
					__popup_AddExportGroup_JMenuItem );
			}
		}

		// Show popup menu
		int mods = event.getModifiers();
		if ((( mods & MouseEvent.BUTTON3_MASK) != 0 )) {
		}
		// isPopupTrigger not working - use getModifiers==4 instead
		if ( event.getModifiers()== 4 ) {
			__treeJPopupMenu.show( event.getComponent(),
						  event.getX(),
						  event.getY() );
		}
	}
}


//============================================================================//
//                  RiversideDB_System_Listener implementation                //                                                          //
//============================================================================//

/**
Adds a node to the export tree and updates the vector of
RTiDB_ExportProduct objects.
@param parent String name of parent node, without the preceeding
type key (aka "Group: " )
@param child String name of child node to add to parent
*/
public void addExportProductNode( String parent,
				  String child,
				  RiversideDB_ExportProduct new_ep )
{
	String routine = __class + ".addExportProductNode";

	if ( Message.isDebugOn ) {
		Message.printDebug( 4, routine,
			" Called with parent: \"" + parent + "\" and child: \""
			+ child + "\"." );
	}

	SimpleJTree_Node parent_node=findNodeByName(GRP_STRING + ": " + parent);
		
	if ( parent_node == null ) {
		Message.printWarning( 1, routine, "Error adding new " +
		"Export Product node to Import Tree. \nAlthough tree " +
		"has not been updated, database should have been so next " +
		"time application is refreshed, Export Product should " +
		"be present in the tree." );
	}

	// Now we have the parent node, make child node
	SimpleJTree_Node child_node = new SimpleJTree_Node(
		PROD_STRING + ": " + child );

	// Add Data to node
	child_node.setData( new_ep );

	// Add Node to Tree
	try {
		addNode( child_node, parent_node );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}

	expandNode( child_node );

	// Now add this new ExportProduct to the vector of export products by
	// re-reading the database! get vector of all ExportProduct objects...
	try {
		__product_vect = __dmi.readExportProductList();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		__product_vect = new Vector();
	}
}

/**
Update the Export Product node identified by the String "existing_name"
@param new_exportProduct Reference to the new RiversideDB_ExportProduct object
to be used to update the node.
@param existing_name String identifier of the Export node to update.
*/
public void updateExportProductNode( RiversideDB_ExportProduct ep_new,
				     String existing_name )
{
	String routine = __class + ".updateExportProductNode";

	if ( Message.isDebugOn ) {
		Message.printDebug( 4, routine,
			"Called with to update node: "
			+ existing_name +
			" with new ExportProduct: "
			+ ep_new.toString() );
	}

	// Find node by finding node with ExportProduct name and num
	// matching that of the product passed in
	SimpleJTree_Node existing_node = findNodeByName(
		"Product: " + existing_name);
	if ( existing_node == null ) {
		Message.printWarning( 2, routine,
			"Unable to locate node named: \""
			+ existing_name
			+ "\".  Will not update tree, "
			+ "but will try to update database." );
	}
	// New node
	SimpleJTree_Node new_node = new SimpleJTree_Node(
		PROD_STRING + ": " + ep_new.getProduct_name() +
		" (type: " + ep_new.getProduct_type() + ")" );
	new_node.setData( ep_new );

	// Update node
	try {
		replaceNode( existing_node, new_node );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, "Unable to update " +
		"Export Product node in tree, but will still update "+
		"database, so next time application is re-started, "+
		"node should be updated in tree.  " +
		"See log for error details." );
		Message.printWarning( 2, routine, e );
	}

	// Scroll to node
	scrollToVisibleNode( new_node );

	// Now add this new ImportProduct to the vector of
	// import products by re-reading the database!
	// get vector of all ImportProduct objects...
	try {	__product_vect = __dmi.readExportProductList();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		__product_vect = new Vector();	
	}
}

}
//------------------------------------------------------------------------------
