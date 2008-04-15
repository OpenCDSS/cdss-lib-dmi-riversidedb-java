

// REVISIT [LT] - Protected members need to be renamed from __???? to _????. 
//		  They are incorrected named because originally they were all
//		  private, since the whole code was in the single 
//		  RTAssistent_Main_JFrame.java code.
// 2004-12-15 Luiz Teixeira.


//------------------------------------------------------------------------------
// RiversideDB_Import_JTree - Creates an Import SimpleJTree object.
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
//						RiversideDB_Export_JTree
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
//					RiversideDB_Import_JTree.populateTree()
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
//						__importProduct_vect
//						__importProductGroup_vect
//						__canWriteImportProduct
//						__canWriteImportProductGroup
//------------------------------------------------------------------------------
package RTi.DMI.RiversideDB_DMI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
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
import RTi.Util.Message.Message;

/**
Creates an Import SimpleJTree object.
*/
public class RiversideDB_Import_JTree extends RiversideDB_TreeBase_JTree
{

/**
Class name
*/
private static String __class = "RiversideDB_Import_JTree";

/**
Main PopupMenus
*/
private SimpleJMenuItem __popup_AddImportProduct_JMenuItem        = null;
private SimpleJMenuItem __popup_DeleteImportProduct_JMenuItem     = null;
private SimpleJMenuItem __popup_ImportProductProperties_JMenuItem = null;

private SimpleJMenuItem __popup_AddImportGroup_JMenuItem          = null;
private SimpleJMenuItem __popup_DeleteImportGroup_JMenuItem       = null;
private SimpleJMenuItem __popup_ImportGroupProperties_JMenuItem   = null;

private String __popup_AddImportProduct_String     =       "Add Import Product";
private String __popup_DeleteImportProduct_String  =    "Delete Import Product";
private String __popup_ImportProductProperties_String
						   ="Import Product Properties";

private String __popup_AddImportGroup_String       =         "Add Import Group";
private String __popup_DeleteImportGroup_String    =      "Delete Import Group";
private String __popup_ImportGroupProperties_String=  "Import Group Properties";

/**
RiversideDB_Import_JTree default constructor.
@param dmi Reference to the RiversideDB dmi connection.
@param windowManager Reference to the RiversideDB_WindowManager object that
manages the windows of the editors created rom this JTree
@param populate Flag indicating if the tree is to be populated during
construction (true) or if it will be populated afterwards, by the caller,
running the public method populateTree() 
*/
public RiversideDB_Import_JTree ( RiversideDB_DMI dmi,
				  RiversideDB_WindowManager windowManager,
				  boolean populate )
{
	// 1) The base class will take care of the initialization of the
	//    instance members __app_title, __dmi, __windowManager and
	//    __riversideDB_TablesVector
	super( windowManager, dmi );

	// 2) Assign the tree title to the __treeString instance member.
	__treeString = "Imports";

	// 3) Initialize the write-permission instance members
	__canWriteProduct      = initTableWritePermission ("ImportProduct" );
	__canWriteProductGroup = initTableWritePermission (  "ProductGroup" );

	// 4) Creates the empty tree.
	createTree();

	// 5) Populate the tree.
	if ( populate ) populateTree();
}

/**
Creates a standard JTree that will hold the RiversideDB_ImportProduct and
RiversideDB_ImportType.
*/
protected void createTree()
{
	String routine = __class + ".createTree";

	new SimpleJTree();
	setTreeTextEditable( false );

	// Create top node
	__treeTopNode = new SimpleJTree_Node ( __treeString );

	// Get closed folder icon
	if ( __closedFolderIcon == null ) {
		__closedFolderIcon = getClosedIcon();
	}

	// Set it to support SINGLE Selection ONLY
	getSelectionModel().setSelectionMode(
		TreeSelectionModel.SINGLE_TREE_SELECTION );

	// Add top node
	try {
		addNode( __treeTopNode );
	}
	catch (Exception e ) {
		Message.printWarning( 2, routine, e );
		if ( Message.isDebugOn ) {
			Message.printDebug( 1, routine,
				"Error adding top node to import JTree." );
		}
	}

	// Now add a popup menu to the tree.
	__treeJPopupMenu = new JPopupMenu();
	JPopupMenu.setDefaultLightWeightPopupEnabled( false );

	// Menu items: define properties, add new product, delete product
	__popup_AddImportProduct_JMenuItem = new SimpleJMenuItem(
		__popup_AddImportProduct_String, this );

	__popup_DeleteImportProduct_JMenuItem = new SimpleJMenuItem(
		__popup_DeleteImportProduct_String, this );

	__popup_ImportProductProperties_JMenuItem = new SimpleJMenuItem(
		__popup_ImportProductProperties_String, this );

	__popup_AddImportGroup_JMenuItem = new SimpleJMenuItem(
		__popup_AddImportGroup_String, this );

	__popup_DeleteImportGroup_JMenuItem = new SimpleJMenuItem(
		__popup_DeleteImportGroup_String, this );

	__popup_ImportGroupProperties_JMenuItem = new SimpleJMenuItem(
		__popup_ImportGroupProperties_String, this );

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
	__popup_AddImportProduct_JMenuItem        = null;
	__popup_DeleteImportProduct_JMenuItem     = null;
	__popup_ImportProductProperties_JMenuItem = null;

	__popup_AddImportGroup_JMenuItem          = null;
	__popup_DeleteImportGroup_JMenuItem       = null;
	__popup_ImportGroupProperties_JMenuItem   = null;

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
		__product_vect = __dmi.readImportProductList();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		Message.printWarning( 1, routine,
			"Unable to get a Vector of ImportProduct objects. " );
	__product_vect =  new Vector();
	}

	// Get List of Import ProductGroups
	try {
		__productGroup_vect = __dmi.
			readProductGroupListForProductType( "IMPORT" );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		Message.printWarning( 1, routine,
			"Unable to get a Vector of Import ProductGroups. " );
		__productGroup_vect = null;
	}
}

/**
Fills in the Import Tree with the actual data for RiversideDB_ImportType and
RiversideDB_ImportProducts, using the permissions from the DBUser to
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

		grp_node= new SimpleJTree_Node( GRP_STRING + ": " + grp );
		grp_node.setData( pg);

		// Set icon
		grp_node.setIcon( __closedFolderIcon );

		if ( canReadProductGroup ) {
			try {
				addNode( grp_node, __treeTopNode );
			}
			catch ( Exception e ) {
				Message.printWarning( 2, routine, e );
			}
		}
		else {
			continue;
		}

		// At This point, expand the tree so that all groups will be
		// displayed
		expandNode(grp_node);

		// Now loop and add any products that fall within that grp type.
		int grp_num = -999;

		RiversideDB_ImportProduct ip = null;
		for ( int p=0; p< obj_size; p++ ) {
			ip = (RiversideDB_ImportProduct)
			__product_vect.elementAt(p);
			if ( ip == null ) {
				continue;
			}
			boolean canReadImportProduct = false;
			try {
				canReadImportProduct = __dmi.canRead(
					ip.getDBUser_num(),
					ip.getDBGroup_num(),
					ip.getDBPermissions());
			}
			catch ( Exception e ) {
				Message.printWarning( 2, routine, e);
			}
			if ( Message.isDebugOn ) {
				Message.printDebug( 3, routine,
				"dmi.canRead() returned \"" +
				canReadImportProduct +
				"\" for ImportProduct\n" +
				ip.getProduct_name()+
				" ( ExportProduct_num= " +
				ip.getImportProduct_num()+ ") " +
				"\nfor user with DBUser_num: \"" +
				ip.getDBUser_num() + "\" and DBGroup_num: \"" +
				ip.getDBGroup_num() +
				" \" and DBPermissions: \"" +
				ip.getDBPermissions() +"\"" );
			}
			grp_num = ip.getProductGroup_num();
			if( top_grp_num == grp_num ) {
				// Add that product name under the grp node
				prod_node = new SimpleJTree_Node(
					PROD_STRING
					+ ": "
					+ ip.getProduct_name()
					+ " (type: "
					+ ip.getProduct_type()
					+ ")" );
				prod_node.setData( ip);

				if ( canReadImportProduct ) {
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
//              Action events from the Import Tree Popup menus                //                                                          //
//============================================================================//

/**
The event handler manages action events.
@param event Event to handle.
*/
public void actionPerformed (ActionEvent event)
{
	String routine = __class + ".actionPerformed";

	try {
	Object source  = event.getSource();

	if ( source == __popup_DeleteImportProduct_JMenuItem  ) {

		// Get name of ImportProduct selected
		SimpleJTree_Node prod_node = getSelectedNode();

		// Get data
		RiversideDB_ImportProduct ip = (RiversideDB_ImportProduct)
		prod_node.getData();

		// Get ImportProduct num for item selected.
		long ip_num = ip.getImportProduct_num();

		//see if user has delete permissions
		boolean canDeleteImportProduct = false;

		try {
			canDeleteImportProduct = __dmi.canDelete(
				ip.getDBUser_num(),
				ip.getDBGroup_num(),
				ip.getDBPermissions() );
		}
		catch( Exception e ){
			Message.printWarning( 2, routine, e );
		}

		if ( !canDeleteImportProduct ) {
			Message.printWarning( 1, routine,
				"You do not have permisssions to delete "
				+ "this Import Product." );
		}

		// Can call delete!
		else if ( ip_num >= 0 ) {
			int confirm = JOptionPane.showConfirmDialog(
					this,
					"Delete Import \""
					+ prod_node.getName()
					+ "\" and "
					+ "its import configuration?",
					"Confirm Deletion of Import "
					+ prod_node.getName(),
					JOptionPane.YES_NO_OPTION );

			boolean blnDeleted = true;
			//delete ImportProductForImportProduct_num
			//returns an array of ints.
			//at [0]= integer representing the number of
			//records deleted from ImportProduct table
			//at[1]= integer representing the number of
			//records deleted from ImportConf table

			int[] arrDeletedRecords = null;
			if ( confirm == JOptionPane.OK_OPTION ) {
				try {
					arrDeletedRecords = __dmi.
					deleteImportProductForImportProduct_num(
						(int)ip_num );
				}
				catch ( Exception e ) {
					Message.printWarning( 2,"", e );
					Message.printWarning( 2,"",
						"Unable to delete selected "
						+ "ImportProduct: \""
						+ prod_node.getName()
						+ " \"."
						+ " (Database error)");
					blnDeleted=false;
				}
				if ( Message.isDebugOn ) {
					if ( arrDeletedRecords != null ) {
						Message.printStatus( 3, routine,
						"Number of ImportProduct " +
						"records deleted = " +
						arrDeletedRecords[0] +
						" and number of related " +
						"ImportConf records deleted = "+
						arrDeletedRecords[1] );
					}
				}
				if ( blnDeleted ) {
					// REMOVE that node from the tree
					removeNode( prod_node, false );

					//Re-read database to create
					//new Vector of Import Products
					try {	__product_vect =
						__dmi.readImportProductList();
						}
					catch ( Exception e ) {
						Message.printWarning(
							2, routine, e );

						// Create an empty vector
						__product_vect = new Vector();
					}

				}
			} //end OK

		} //end if ip_num > 0
	}

	else if ( source == __popup_AddImportGroup_JMenuItem ) {

		// Create interface for adding a new ProductGroup for Imports
		// Pass in INPORT for the type.
		RiversideDB_ProductGroup pg = new RiversideDB_ProductGroup();
		pg.setProductType("IMPORT");

		RiversideDB_ProductGroup_JFrame pgf = null;
		if ( __windowManager == null ) {
			// Simple open.
			pgf = new RiversideDB_ProductGroup_JFrame(
				__dmi,
				__windowManager,
				__app_title + " - New Import Product Group",
				pg );
		} else {
			// Open via the RiversideDB_WindowManager.
			pgf =  (RiversideDB_ProductGroup_JFrame)
				__windowManager.displayWindow(
				__windowManager.WINDOW_PRODUCTGROUP,
				true,
				__app_title + " - New Import Product Group",
				pg,
				null );
		}
		pgf.addRiversideDBSystemListener (this);
	}

	else if ( source == __popup_DeleteImportGroup_JMenuItem ) {

		// Get node and determine which ImportProductGroup it represents
		SimpleJTree_Node grp_node = getSelectedNode();
		// Get Data
		RiversideDB_ProductGroup pg = (RiversideDB_ProductGroup)
						grp_node.getData();

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
				+ "this Import Product Group." );
		}
		else if ( pg != null ) {
			int confirm = JOptionPane.showConfirmDialog(
				this,
				"Delete Import Product Group: \""
				+ grp_node.getName()
				+ "\" and "
				+ "its Import Products and time series?",
				"Confirm Deletion of Product Group "
				+ grp_node.getName(),
				JOptionPane.YES_NO_OPTION );

			int[] arrDels = null;
			if ( confirm == JOptionPane.OK_OPTION ) {
				try {
				   arrDels = __dmi.
				   deleteImportProductGroupForProductGroup_num(
				   	pg.getProductGroup_num() );
				}
				catch ( Exception e ) {
					Message.printWarning( 2, routine, e );
				}

				// REMOVE that node from the tree
				removeNode( grp_node, false );

				Message.printStatus( 10, routine,
				     arrDels[0]
				   + " records deleted from ProductGroup, "
				   + arrDels[1]
				   + " records deleted from ImportProduct and "
				   + arrDels[2]
				   + " records deleted from ImportConf." );
			}
		}
	}

	else if ( source == __popup_ImportGroupProperties_JMenuItem ) {

		// Get Import Product Group selected.
		SimpleJTree_Node prodgrp_node = getSelectedNode();

		// Get name of Product Group to use in title
		String name =  prodgrp_node.getName();
		String stripped_name = null;
		if ( name.regionMatches(
			true, 0, GRP_STRING, 0, GRP_STRING.length() ) ) {
			// remove the PROD: from the string
			stripped_name = name.substring(
				GRP_STRING.length() + 1).trim();
		}

		// Make new instance of ImportJFrame w/ these items preselected.
		RiversideDB_ProductGroup pg = (RiversideDB_ProductGroup)
			prodgrp_node.getData();
		Long id = new Long(pg.getProductGroup_num());
		RiversideDB_ProductGroup_JFrame pgf = null;
		if ( __windowManager == null ) {
			// Simple open.
			pgf = new RiversideDB_ProductGroup_JFrame(
				__dmi,
				__windowManager,
				__app_title + " - Import Product Group - "
					    + stripped_name,
				pg );
		} else {
			// Open via the RiversideDB_WindowManager.
			pgf =  (RiversideDB_ProductGroup_JFrame)
				__windowManager.displayWindow(
				__windowManager.WINDOW_PRODUCTGROUP,
				true,
				__app_title + " - Import Product Group - "
					    + stripped_name,
				pg,
				id );
		}
		pgf.addRiversideDBSystemListener (this);
	}

	else if ( source == __popup_ImportProductProperties_JMenuItem  ) {

		// Get Import Product Group selected.
		SimpleJTree_Node prod_node = getSelectedNode();

		// Get name used in title bar of Import GUI
		String stripped_prod = null;
		String name = prod_node.getName();
		// Remove the preceeding "Product:" tag
		if ( name.regionMatches(
		true, 0, PROD_STRING, 0, PROD_STRING.length() ) ) {
			//remove the PROD: from the string
			stripped_prod = (name.substring(
				PROD_STRING.length() + 1)).trim();
		}

		// Make new instance of ImportJFrame w/ these items preselected.
		RiversideDB_ImportProduct ip = (RiversideDB_ImportProduct)
			prod_node.getData();

		Long id = new Long(ip.getImportProduct_num());

		RiversideDB_Import_JFrame ipg = null;
		if ( __windowManager == null ) {
			// Simple open.
			ipg = new RiversideDB_Import_JFrame(
				__dmi,
				__windowManager,
				__app_title + " - Import Product - "
					    + stripped_prod,
				ip );
		} else {
			// Open via the RiversideDB_WindowManager.
			ipg =  (RiversideDB_Import_JFrame)
				__windowManager.displayWindow(
				__windowManager.WINDOW_IMPORT_PRODUCT,
				true,
				__app_title + " - Import Product - "
					    + stripped_prod,
				ip,
				id );
		}
		ipg.addRiversideDBSystemListener (this);
	}

	else if ( source == __popup_AddImportProduct_JMenuItem ) {

		// Get the node selected, which should be the Group name
		SimpleJTree_Node group_node = getSelectedNode();

		/*
		String stripped_grp = null;
		String name = group_node.getName();
		// Remove the string from the front of the group name: "GRP:"
		if (name.regionMatches(
			true, 0, GRP_STRING, 0, GRP_STRING.length() ) ) {
			stripped_grp = name.substring( GRP_STRING.length() +1 );
		}
		*/

		// Get ProductGroup
		RiversideDB_ProductGroup pg = (RiversideDB_ProductGroup)
			group_node.getData();

		RiversideDB_ImportProduct ip = new RiversideDB_ImportProduct();
		ip.setProductGroup_num( pg.getProductGroup_num() );
		ip.setProduct_group   ( pg.getIdentifier() );
		ip.setProduct_name    ( pg.getName() );

		RiversideDB_Import_JFrame ipg = null;
		if ( __windowManager == null ) {
			// Simple open.
			ipg = new RiversideDB_Import_JFrame(
				__dmi,
				__windowManager,
				__app_title + " - Import Product - "
					    + "New Import Product",
				ip );
		} else {
			// Open via the RiversideDB_WindowManager.
			ipg =  (RiversideDB_Import_JFrame)
				__windowManager.displayWindow(
				__windowManager.WINDOW_IMPORT_PRODUCT,
				true,
				__app_title + " - Import Product - "
					    + "New Import Product",
				ip);
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
			//import product selected
			//remove all menu items
			__treeJPopupMenu.removeAll();

			__treeJPopupMenu.add(
				__popup_ImportProductProperties_JMenuItem );

			//add Delete
			__treeJPopupMenu.add(
				__popup_DeleteImportProduct_JMenuItem );
		}

		// Group
		if ( (node.getName()).regionMatches(
			true, 0, GRP_STRING, 0, GRP_STRING.length() ) ) {
			//remove all menu items
			__treeJPopupMenu.removeAll();

			//add properties for Import Product Group
			__treeJPopupMenu.add(
				__popup_ImportGroupProperties_JMenuItem );

			if ( __canWriteProduct )  {
				__treeJPopupMenu.add(
					__popup_AddImportProduct_JMenuItem );
			}

			//add delete
			__treeJPopupMenu.add(
				__popup_DeleteImportGroup_JMenuItem );

		}

		// Import - Top of tree node.
		if ( (node.getName()).regionMatches(
			true, 0, __treeString, 0, 4 ) ) {
			// Remove all menu items
			__treeJPopupMenu.removeAll();
			if ( __canWriteProductGroup )  {
				__treeJPopupMenu.add(
					__popup_AddImportGroup_JMenuItem );
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
Adds a node to the import tree and updates the vector of
RTiDB_ImportProduct objects.
@param parent String name of parent node as it appears on the JTree,
without the preceeding type key (aka "Group: " ). The parent node of
an ImportProduct is a ProductGroup object and it is displayed on the JTree
listed as: "Group: NAME".
@param child String name of child node to add to parent
@param ip ImportProduct object to add to JTree.
*/
public void addImportProductNode( String parent,
				  String child,
				  RiversideDB_ImportProduct new_ip )
{
	String routine = __class + ".addImportProductNode";

	if ( Message.isDebugOn ) {
		Message.printDebug( 4, routine,
			" Called with parent: \"" + parent + "\" and child: \""
			+ child + "\"." );
	}

	SimpleJTree_Node parent_node= findNodeByName(GRP_STRING + ": " +parent);

	if ( parent_node == null ) {
		Message.printWarning( 1, routine, "Error adding new " +
		"Import Product node to Import Tree. \nAlthough tree " +
		"has not been updated, database should have been so next " +
		"time application is refreshed, Import Product should " +
		"be present in the tree." );
	}

	// Now we have the parent node, make child node
	SimpleJTree_Node child_node = new SimpleJTree_Node(
		PROD_STRING + ": " + child );

	// Add Data to node
	child_node.setData( new_ip );

	// Add Node to Tree
	try {
		addNode( child_node, parent_node );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}
	expandNode( child_node );

	// Now add this new ImportProduct to the vector of import products by
	// re-reading the database! get vector of all ImportProduct objects...
	try {
		__product_vect = __dmi.readImportProductList();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		// Create an empty vector
		__product_vect = new Vector();
	}
}

/**
Update the Import Product node identified by the "existing_name"
@param new_importProduct Reference to the new RiversideDB_ImportProduct
object to be used to update the node.
@param existing_name String identifier of the Import node to update.
*/
public void updateImportProductNode( RiversideDB_ImportProduct ip_new,
				     String existing_name )
{
	String routine = __class + ".updateImportProductNode";

	Message.printStatus( 3, routine, "Updating node: \"" +
		existing_name + "\"" );

	// Find node by finding node with ImportProduct name and num
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
		PROD_STRING + ": " + ip_new.getProduct_name() + " (type: " +
		ip_new.getProduct_type() + ")" );
	new_node.setData( ip_new );

	// Update node
	try {
		replaceNode( existing_node, new_node );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, "Unable to update " +
		"Import Product node in tree, but will still update "+
		"database, so next time application is re-started, "+
		"node should be updated in tree.  " +
		"See log for error details." );
		Message.printWarning( 2, routine, e );
	}

	// Scroll to node
	scrollToVisibleNode( new_node );

	// If product group has changed for this node, its parent will
	// not match the product_group field.
	SimpleJTree_Node parent_node = getParentOfNode(new_node);
	String parent_name = parent_node.getName();

	RiversideDB_ProductGroup orig_pg = ( RiversideDB_ProductGroup)
		parent_node.getData();
	long pg_num = -999;
	if ( orig_pg == null ) {
	}
	else {
		// Get ProductGroup_num
		pg_num = orig_pg.getProductGroup_num();
		orig_pg = null;
	}

	int index = -999;
	index = parent_name.indexOf("Group: " );
	if ( index > 0 ) {
		parent_name = parent_name.substring(
			index+7, parent_name.length()+1 ).trim();
	}

	SimpleJTree_Node new_parent_node = null;
	//if ( !parent_name.equals(ip_new.getProduct_group() ) ) {}
	if ( pg_num != ip_new.getProductGroup_num() )  {

		// Find new parent
		new_parent_node = findNodeByName( "Group: " +
			ip_new.getProduct_group() );

		// Then update tree
		try {
			moveNode( new_node, new_parent_node, true );
			refresh();
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, "Unable to update "+
			"position of updated node under new parent: \"" +
			ip_new.getProduct_group() + "\" on import tree. "+
			"Database may already be updated, so next time " +
			"application is re-started, node should appear in " +
			"correct location.  See log file for error details." );
			Message.printWarning( 2, routine, e );
		}
	}

	scrollToVisibleNode( new_node );

	// Now add this new ImportProduct to the vector of
	// import products by re-reading the database!
	// get vector of all ImportProduct objects...
	try {	__product_vect = __dmi.readImportProductList();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		__product_vect = new Vector();
	}
}

}
//------------------------------------------------------------------------------
