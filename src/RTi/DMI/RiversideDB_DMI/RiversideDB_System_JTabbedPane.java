
//------------------------------------------------------------------------------
// RiversideDB_System_JTabbedPane - Creates a JTabbedPane currently populated
// with three JTrees (Import, MeasLoc and Export).
//------------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
//------------------------------------------------------------------------------
// History:
//
// 2004-11-22 Luiz Teixeira, RTi 	Original version.
// 2004-12-?? Luiz Teixeira, RTi	Added the refreshTrees() method.
// 2004-12-10 Luiz Teixeira, RTi	Added the populateTrees() method.
// 2004-12-10 Luiz Teixeira, RTi	Added the 'boolean populate' parameter 
//					to the constructor method. If this para
//					meter is true, the trees will be popula-
//					ted during construction, otherwise the
//					caller object should populate the trees
//					afterwards by running the public method
//					RiversideDB_System_JTabbedPane.
//						populateTrees() 
//------------------------------------------------------------------------------
package RTi.DMI.RiversideDB_DMI;

import javax.swing.JTabbedPane;

import RTi.Util.Message.Message;

import RTi.DMI.RiversideDB_DMI.RiversideDB_MeasLoc_JTree;
import RTi.DMI.RiversideDB_DMI.RiversideDB_Import_JTree;
import RTi.DMI.RiversideDB_DMI.RiversideDB_Export_JTree;
//port RTi.DMI.RiversideDB_DMI.RiversideDB_States_JTree;
import RTi.DMI.RiversideDB_DMI.RiversideDB_WindowManager;

/**
Creates a JTabbedPane currently populated with three JTrees
(Import, MeasLoc and Export).
*/
public class RiversideDB_System_JTabbedPane extends JTabbedPane
{

/**
Class name
*/
private static String __class = "RiversideDB_System_JTabbedPane";

/**
Reference to the RiversideDB connection
*/
private RiversideDB_DMI __dmi = null;

/**
Reference to the RiversideDB_WindowManager object.
*/
private RiversideDB_WindowManager __windowManager = null;

/**
Flag indicating if the trees are to be populated during construction (true) or
if they will be populated afterwards, by the caller, running the public method
populateTrees() 
*/
private boolean __populate = false;

/**
References to the RiversideDB_*_JTree object that populates the JTabbedPane.
*/
private RiversideDB_MeasLoc_JTree __rdb_measLocJTree = null;
private RiversideDB_Import_JTree  __rdb_importJTree  = null;
private RiversideDB_Export_JTree  __rdb_exportJTree  = null;
//ivate RiversideDB_MeasLoc_JTree __rdb_statesJTree  = null;

/**
Default constructor.
@param dmi Reference to the RiversideDB_DMI connection.
@param windowManager Reference to the RiversideDB_WindowManager object.
@param riversideDB_TablesVector Reference to a vector containing the
list of RiversideDB table names.
*/
public RiversideDB_System_JTabbedPane( RiversideDB_DMI dmi,
				       RiversideDB_WindowManager windowManager,
				       boolean populate )
{
	super( JTabbedPane.TOP );

	// Set instance members.
	__dmi            = dmi;
	__windowManager  = windowManager;
	__populate       = populate;

	// Initialize the GUI
	initializeGUILayout();
}

/**
Depopulate the JTree(s)
*/
public void depopulateTrees()
{
	__rdb_measLocJTree.depopulateTree();
	__rdb_importJTree .depopulateTree();
	__rdb_exportJTree .depopulateTree();
//	__rdb_statesJTree .depopulateTree();
}

/**
Initialize the GUI.
*/
private void initializeGUILayout ()
{
	String routine = __class + ".initializeGUILayout";

	// Instantiate the RiversideDB_MeasLoc_JTree object.
	// The tree will be created and populated.
	__rdb_measLocJTree =  new RiversideDB_MeasLoc_JTree (
		__dmi, __windowManager, __populate );

	// Instantiate the RiversideDB_Import_JTree object.
	// The tree will be created and populated.
	__rdb_importJTree = new RiversideDB_Import_JTree (
		__dmi, __windowManager,  __populate );

	// Instantiate the RiversideDB_Export_JTree object.
	// The tree will be created and populated.
	__rdb_exportJTree = new RiversideDB_Export_JTree (
		__dmi, __windowManager, __populate );

	// Instantiate the RiversideDB_States_JTree object.
	// The tree will be created and populated.
//	__rdb_statesJTree = new RiversideDB_States_JTree (
//		__dmi, __windowManager,  __populate );

	// Add the RiversideDB_MeasLoc_JTree JScrollPane to the JTabbedPane
	if ( __rdb_measLocJTree != null ) {
		// Add pane without an icon, but with a ToolTip
		this.addTab( 	__rdb_measLocJTree.get_treeString(), null,
				__rdb_measLocJTree.get_treeJScrollPane(),
				__rdb_measLocJTree.get_treeString());
	}

	// Add the RiversideDB_Import_JTree JScrollPane to the JTabbedPane
	if ( __rdb_importJTree != null ) {
		// Add pane without an icon, but with a ToolTip
		this.addTab( 	__rdb_importJTree.get_treeString(), null,
				__rdb_importJTree.get_treeJScrollPane(),
				__rdb_importJTree.get_treeString());
	}

	// Add the RiversideDB_Export_JTree JScrollPaneto the JTabbedPane
	if ( __rdb_exportJTree != null ) {
		// Add pane without an icon, but with a ToolTip
		this.addTab( 	__rdb_exportJTree .get_treeString(), null,
				__rdb_exportJTree .get_treeJScrollPane(),
				__rdb_exportJTree .get_treeString());
	}

	// Add the RiversideDB_States_JTree JScrollPane to the JTabbedPane
//	if ( __rdb_statesJTree != null ) {
//		this.addTab( 	__rdb_statesJTree .get_treeString(), null,
//				__rdb_statesJTree .get_treeJScrollPane(),
//				__rdb_statesJTree .get_treeString());
//	}

	if ( Message.isDebugOn ) {
		Message.printDebug( 8, routine,
			"After call to add all trees to JTabbedPane");
	}
}

/**
Finalize. Free memory for garbage collection.
@exception Throwable if there is an error.
*/
public void finalize ()
throws Throwable
{
	__dmi              = null;
	__windowManager    = null;
	
	__rdb_measLocJTree = null;
	__rdb_importJTree  = null;
	__rdb_exportJTree  = null;
//	__rdb_statesJTree  = null;

	super.finalize();
}

/**
Refresh ( depopulate and populate ) the JTree(s)
*/
public void refreshTrees()
{
	__rdb_measLocJTree.refreshTree();
	__rdb_importJTree .refreshTree();
	__rdb_exportJTree .refreshTree();
//	__rdb_statesJTree .refreshTree();
}

/**
Populate the JTree(s)
*/
public void populateTrees()
{
	__rdb_measLocJTree.populateTree();
	__rdb_importJTree .populateTree();
	__rdb_exportJTree .populateTree();
//	__rdb_statesJTree .populateTree();
}

}
//------------------------------------------------------------------------------