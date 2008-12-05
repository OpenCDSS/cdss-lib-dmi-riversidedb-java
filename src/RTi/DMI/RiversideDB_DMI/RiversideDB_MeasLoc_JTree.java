

// REVISIT [LT] - Protected members need to be renamed from __???? to _????.
//		  They are incorrected named because originally they were all
//		  private, since the whole code was in the single
//		  RTAssistent_Main_JFrame.java code.
// 2004-12-15 Luiz Teixeira.


//------------------------------------------------------------------------------
// RiversideDB_MeasLoc_JTree - Creates an MeasLoc SimpleJTree object.
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
//						RiversideDB_Import_JTree
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
//					RiversideDB_MeasLoc_JTree.populateTree.
//------------------------------------------------------------------------------
package RTi.DMI.RiversideDB_DMI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.tree.TreeSelectionModel;

import RTi.TS.TSIdent;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleJMenuItem;
import RTi.Util.GUI.SimpleJTree;
import RTi.Util.GUI.SimpleJTree_Node;
import RTi.Util.Message.Message;

/**
Creates an MeasLoc SimpleJTree object
*/
public class RiversideDB_MeasLoc_JTree extends RiversideDB_TreeBase_JTree
{

/**
Class name
*/
private static String __class = "RiversideDB_MeasLoc_JTree";

/**
Main PopupMenus
*/
private SimpleJMenuItem __popup_AddArea_JMenuItem                 = null;
private SimpleJMenuItem __popup_DeleteArea_JMenuItem              = null;
private SimpleJMenuItem __popup_AreaProperties_JMenuItem          = null;

private SimpleJMenuItem __popup_AddStationPoint_JMenuItem         = null;
private SimpleJMenuItem __popup_DeleteStationPoint_JMenuItem      = null;
private SimpleJMenuItem __popup_StationPointProperties_JMenuItem  = null;

private SimpleJMenuItem __popup_AddTimeSeries_JMenuItem           = null;
private SimpleJMenuItem __popup_DeleteTimeSeries_JMenuItem        = null;
private SimpleJMenuItem __popup_TimeSeries_Properties_JMenuItem   = null;

private SimpleJMenuItem __popup_DefineOriginAsReduction_JMenuItem = null;
private SimpleJMenuItem __popup_DeleteReduction_JMenuItem         = null;
private SimpleJMenuItem __popup_ReductionProperties_JMenuItem     = null;

private SimpleJMenuItem __popup_ImportProductProperties_JMenuItem = null;

private String __measLoc_Areas_String               =                   "AREAS";
private String __measLoc_StationsPoints_String 	    =         "STATIONS/POINTS";

private String __popup_AddArea_String               =                "Add Area";
private String __popup_DeleteArea_String            =             "Delete Area";
private String __popup_AreaProperties_String        =         "Area Properties";

private String __popup_AddStationPoint_String       =       "Add Station/Point";
private String __popup_DeleteStationPoint_String    =    "Delete Station/Point";
private String __popup_StationPointProperties_String="Station/Point Properties";

private String __popup_AddTimeSeries_String         =         "Add Time Series";
private String __popup_DeleteTimeSeries_String      =      "Delete Time Series";
private String __popup_TimeSeriesProperties_String  =  "Time Series Properties";

private String __popup_DefineOriginAsReduction_String
						 = "Define Origin as Reduction";
private String __popup_DeleteReduction_String       =        "Delete Reduction";
private String __popup_ReductionProperties_String   =    "Reduction Properties";

private String __popup_ImportProductProperties_String
						  = "Import Product Properties";
/**
MeasLocGroup
*/
private SimpleJMenuItem __popup_AddLocationGroup_JMenuItem        = null;
private SimpleJMenuItem __popup_DeleteLocationGroup_JMenuItem     = null;
private SimpleJMenuItem __popup_LocationGroupProperties_JMenuItem = null;

private String __popup_AddLocationGroup_String        =    "Add Location Group";
private String __popup_DeleteLocationGroup_String     = "Delete Location Group";
private String __popup_LocationGroupProperties_String
						  = "Location Group Properties";

/**
Holds RiversideDB_MeasLoc, RiversideDB_MeasLocType and RiversideDB_MeasLocGroup
objects used to populate the JTree.
*/
private List __measLoc_vect      = null;
private List __measLocType_vect  = null;
private List __measLocGroup_vect = null;

/**
Write permissions flag. These flags are set during construction and are used
while adding or removing adding and removing the menus from the popup menus.
(in responce to mouseReleased event).
*/
private boolean __canWriteMeasLoc      = false;
private boolean __canWriteMeasLocGroup = false;
private boolean __canWriteMeasType     = false;

/**
RiversideDB_MeasLoc_JTree default constructor.
@param dmi Reference to the RiversideDB dmi connection.
@param windowManager Reference to the RiversideDB_WindowManager object that
manages the windows of the editors created rom this JTree
@param populate Flag indicating if the tree is to be populated during
construction (true) or if it will be populated afterwards, by the caller,
running the public method populateTree()
*/
public RiversideDB_MeasLoc_JTree ( RiversideDB_DMI dmi,
				   RiversideDB_WindowManager windowManager,
				   boolean populate )
{
	// 1) The base class will take care of the initialization of the
	//    instance members __app_title, __dmi, __windowManager and
	//    __riversideDB_TablesVector
	super( windowManager, dmi );

	// 2) Assign the tree title to the __treeString instance member.
	__treeString = "Station/Area Locations";

	// 3) Initialize the write-permission instance members
	__canWriteMeasLoc      = initTableWritePermission (       "MeasLoc" );
	__canWriteMeasLocGroup = initTableWritePermission (  "MeasLocGroup" );
	__canWriteMeasType     = initTableWritePermission (      "MeasType" );

	// 4) Creates empty tree.
	createTree();

	// 5) Populate the tree.
	if ( populate ) populateTree();
}

/**
Creates a standard JTree that will hold the RiversideDB_MeasLocGroups,
MeasLocs, MeasTypes, etc.
*/
protected void createTree()
{
	String routine = __class + ".createTree";

	new SimpleJTree();
	setTreeTextEditable( false );

	// Set to single selection
	getSelectionModel().setSelectionMode(
		TreeSelectionModel.SINGLE_TREE_SELECTION );

	// Create top node
	__treeTopNode = new SimpleJTree_Node( __treeString );

	// Get closed folder icon
	if ( __closedFolderIcon == null ) {
		__closedFolderIcon = getClosedIcon();
	}

	// Add node
	try {
		addNode( __treeTopNode );
	}
	catch ( Exception e ) {
		if ( Message.isDebugOn ) {
			Message.printWarning( 2, routine, e );
			Message.printDebug( 1, routine,
			"Error adding top node to the MeasLoc tree." );
		}
	}

	// Now add a popup menu to the tree.
	__treeJPopupMenu = new JPopupMenu();
	JPopupMenu.setDefaultLightWeightPopupEnabled( false );

	// Menu items
	// The items are added and removed from the POPUP menu
	// in the ACTION LISTENER - depending on which element in the
	// tree is selected.
	__popup_AddArea_JMenuItem = new SimpleJMenuItem(
		__popup_AddArea_String, this );

	__popup_DeleteArea_JMenuItem = new SimpleJMenuItem(
		__popup_DeleteArea_String, this );

	__popup_AreaProperties_JMenuItem = new SimpleJMenuItem(
		__popup_AreaProperties_String, this );

	__popup_AddStationPoint_JMenuItem = new SimpleJMenuItem(
		__popup_AddStationPoint_String, this );

	__popup_DeleteStationPoint_JMenuItem = new SimpleJMenuItem(
		__popup_DeleteStationPoint_String, this );

	__popup_StationPointProperties_JMenuItem = new SimpleJMenuItem(
		__popup_StationPointProperties_String, this );

	__popup_AddTimeSeries_JMenuItem = new SimpleJMenuItem(
		__popup_AddTimeSeries_String, this );

	__popup_DeleteTimeSeries_JMenuItem = new SimpleJMenuItem(
		__popup_DeleteTimeSeries_String, this );

	__popup_TimeSeries_Properties_JMenuItem = new SimpleJMenuItem(
		__popup_TimeSeriesProperties_String,
		__popup_TimeSeriesProperties_String, this );

	__popup_DefineOriginAsReduction_JMenuItem = new SimpleJMenuItem(
		__popup_DefineOriginAsReduction_String, this );

	__popup_DeleteReduction_JMenuItem = new SimpleJMenuItem(
		__popup_DeleteReduction_String , this );

	__popup_ReductionProperties_JMenuItem = new SimpleJMenuItem(
		__popup_ReductionProperties_String,
		__popup_ReductionProperties_String, this );

	__popup_ImportProductProperties_JMenuItem = new SimpleJMenuItem(
		__popup_ImportProductProperties_String, this );

	 __popup_AddLocationGroup_JMenuItem = new SimpleJMenuItem(
		__popup_AddLocationGroup_String, this );

	 __popup_DeleteLocationGroup_JMenuItem = new SimpleJMenuItem(
		__popup_DeleteLocationGroup_String, this );

	__popup_LocationGroupProperties_JMenuItem = new SimpleJMenuItem(
		__popup_LocationGroupProperties_String,
		__popup_LocationGroupProperties_String, this );

	// Add tree to scrollpane
	__treeJScrollPane = new JScrollPane( this );
	__treeJScrollPane.setVerticalScrollBarPolicy(
		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
	__treeJScrollPane.setPreferredSize(
		new Dimension( 250, 500 ) );
	__treeJScrollPane.setBorder(
		BorderFactory.createCompoundBorder( (
			BorderFactory.createEmptyBorder( 5, 5, 5, 5 )),
			__treeJScrollPane.getBorder() ));

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
	__popup_AddStationPoint_JMenuItem         = null;
	__popup_DeleteStationPoint_JMenuItem      = null;
	__popup_StationPointProperties_JMenuItem  = null;

	__popup_AddArea_JMenuItem                 = null;
	__popup_DeleteArea_JMenuItem              = null;
	__popup_AreaProperties_JMenuItem          = null;

	__popup_AddTimeSeries_JMenuItem           = null;
	__popup_DeleteTimeSeries_JMenuItem        = null;
	__popup_TimeSeries_Properties_JMenuItem   = null;

	__popup_DefineOriginAsReduction_JMenuItem = null;
	__popup_DeleteReduction_JMenuItem         = null;
	__popup_ReductionProperties_JMenuItem     = null;

	__popup_ImportProductProperties_JMenuItem = null;

	__popup_AddLocationGroup_JMenuItem        = null;
	__popup_DeleteLocationGroup_JMenuItem     = null;
	__popup_LocationGroupProperties_JMenuItem = null;

	__measLoc_vect      = null;
	__measLocType_vect  = null;
	__measLocGroup_vect = null;

 	super.finalize();
}

/**
Creates vectors of RiversideDB_DMI objects that are used to create the JTree.
*/
protected void initRiversideDB_DMI_Vectors()
{
	String routine = __class + ".initRiversideDB_DMI_Vectors";

	// Get List of MeasLocs
	try {
		__measLoc_vect = __dmi.readMeasLocList();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		Message.printWarning( 1, routine,
			"Unable to get a list of MeasLoc objects. ");
		__measLoc_vect = new Vector();
	}

	// Get List of MeasLocTypes
	try {
		__measLocType_vect = __dmi.readMeasLocTypeList();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		Message.printWarning( 1, routine,
			"Unable to get a list of MeasLocType objects. " );
		__measLocType_vect = new Vector();
	}

	// Get List of MeasLocGroups
	try {
		__measLocGroup_vect = __dmi.readMeasLocGroupList();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		Message.printWarning( 1, routine,
			"Unable to get a list of MeasLocGroups objects. " );
		__measLocGroup_vect = new Vector();
	}
}

/**
Fills in the MeasLoc Tree with the actual data for RiversideDB_MeasLocGroup,
RiversideDB_MeasLoc, RiversideDB_MeasType, etc, objects, using the
permissions from the DBUser to determine what should be displayed in the JTree.
*/
public void populateTree()
{
	String routine = __class + ".populateTree";

	// Initialize the RiversideDB_DMI Vectors.
	initRiversideDB_DMI_Vectors();

	// Determine the sizes of the RiversideDB_DMI Vectors.
	int size_measlocs = 0;
	if ( __measLoc_vect != null ) {
		size_measlocs = __measLoc_vect.size();
	}
	int size_measloctypes = 0;
	if ( __measLocType_vect != null ) {
		size_measloctypes =  __measLocType_vect.size();
	}

	int size_measlocgrps = 0;
	if ( __measLocGroup_vect!= null ) {
		size_measlocgrps =  __measLocGroup_vect.size();
	}

	setFastAdd( true );

	// MeasLocGroups are the top level items in the tree.
	RiversideDB_MeasLocGroup mlg = null;
	RiversideDB_MeasLocType mlt  = null;
	RiversideDB_MeasLoc ml       = null;
	RiversideDB_MeasType mt      = null;
	boolean canRead_measlocgroup = false;
	boolean canRead_measloc      = false;
	boolean canRead_meastype     = false;

	if ( Message.isDebugOn ) {
		Message.printDebug( 4, routine, "When building MeasLoc " +
		"JTree, number of MeasLocGroup objects: " + size_measlocgrps +
		", number of MeasLocType objects: " + size_measloctypes +
		", and number of MeasLocs = " + size_measlocs );
	}

	for ( int i=0; i<size_measlocgrps; i++ ) {
		mlg = (RiversideDB_MeasLocGroup)
			__measLocGroup_vect.get(i);
		if ( mlg == null ) continue;

		SimpleJTree_Node measlocgrp_node = new SimpleJTree_Node(
		//MEASLOCGRP_STRING + ": " +( mlg.getName() + " - " +
		MEASLOCGRP_STRING + ": " +( mlg.getIdentifier() + " - " +
		mlg.getName() ).toUpperCase() );
		measlocgrp_node.setData( mlg );
		measlocgrp_node.setIcon( __closedFolderIcon );

		try {
			canRead_measlocgroup =  __dmi.canRead(
			mlg.getDBUser_num(),mlg.getDBGroup_num(),
			mlg.getDBPermissions() );
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e);
			canRead_measlocgroup = false;
		}
		if ( Message.isDebugOn ) {
			Message.printDebug( 3, routine,
			"dmi.canRead() returned \"" + canRead_measlocgroup
			+ "\" for ProductGroup" + mlg.getName()
			+ " ( MeasLocGroup_num = " + mlg.getMeasLocGroup_num()+ ") "
			+ "for user with DBUser_num: \"" +
			mlg.getDBUser_num() + "\" and DBGroup_num: \"" +
			mlg.getDBGroup_num() + " \" and DBPermissions: \"" +
			mlg.getDBPermissions() +"\"" );
		}

		// add to tree.
		if ( canRead_measlocgroup ) {
			try {
				addNode( measlocgrp_node, __treeTopNode );
			}
			catch ( Exception e ) {
				Message.printWarning( 2, routine, e );
				Message.printWarning( 3, routine,
				"Error adding " + "MeasLocGroup node \"" +
				measlocgrp_node.getName()+ "\" to " +
				"MeasLoc JTree top node \"" +
				__treeTopNode.getName() + "\"" );
			}
		}
		else {
			if ( Message.isDebugOn ) {
				Message.printDebug( 5, routine,
				"Current user does not have permissions " +
				"to see MeasLocGroup: " + mlg.getIdentifier() +
				" - " + mlg.getName() ) ;
			}
			continue;
		}


		// go through and add MeasLocTypes at the next level
		for ( int j=0; j<size_measloctypes; j++ ) {
			mlt = ( RiversideDB_MeasLocType )
			__measLocType_vect.get( j );
			if ( mlt == null ) {
				continue;
			}
			String mt_name = null;
			if ( mlt.getType().equalsIgnoreCase( "A" ) ) {
				mt_name = __measLoc_Areas_String;
			}
			else if ( mlt.getType().equalsIgnoreCase( "P" ) ) {
				mt_name = __measLoc_StationsPoints_String;
			}
			else {
				continue;
			}

			// add under each node
			SimpleJTree_Node measloctype_node =
			new SimpleJTree_Node(
			GRP_STRING + ": " + mt_name );
			measloctype_node.setData( mlt );
			measloctype_node.setIcon( __closedFolderIcon );

			// add to each MeasLocGroup node
			try {
				addNode( measloctype_node, measlocgrp_node );
			}

			catch ( Exception e ) {
				Message.printWarning( 2, routine, e );
				Message.printWarning( 3, routine,
				"Error adding MeasLoctype node \"" +
				measloctype_node.getName() + "\" to " +
				"MeasLocGroup node \"" + measlocgrp_node.
				getName() + "\"" );
			}

			// go through and add the MeasLocs for this MeasLocGroup
			for ( int k=0; k<size_measlocs; k++ ) {
				ml = (RiversideDB_MeasLoc) __measLoc_vect.
				get(k);
				if ( ml == null ) {
					continue;
				}
				try {
					canRead_measloc =  __dmi.canRead(
					ml.getDBUser_num(),ml.getDBGroup_num(),
					ml.getDBPermissions() );
				}
				catch ( Exception e ) {
					Message.printWarning( 2, routine, e );
				}
				if ( Message.isDebugOn ) {
					Message.printDebug( 3, routine,
					"dmi.canRead() returned \"" +
					canRead_measloc +
					"\" for MeasLoc\n" +
					ml.getMeasLoc_name()+
					" ( MeasLoc_num= " +
					ml.getMeasLoc_num()+ ") " +
					"\nfor user with DBUser_num: \"" +
					ml.getDBUser_num() +
					"\" and DBGroup_num: \"" +
					ml.getDBGroup_num() +
					" \" and DBPermissions: \"" +
					ml.getDBPermissions() +"\"" );
				}
				//if the MeasLoc is not in the
				//current MeasLocGroup, skip it.
				if ( ml.getMeasLocGroup_num() !=
				mlg.getMeasLocGroup_num() ) {
					continue;
				}
				//if the MeasLoc is not in the current
				//MeasLocType, skip it
				else if ( !ml.getMeas_loc_type().
				equalsIgnoreCase( mlt.getType() ) ) {
					continue;
				}

				//else, add node to the MeasType node.
				SimpleJTree_Node measloc_node =
				new SimpleJTree_Node(
				ID_STRING + ": " + ml.getIdentifier() +
				" - " +ml.getMeasLoc_name() );

				measloc_node.setData( ml );
				measloc_node.setIcon( __closedFolderIcon );

				//add to MeasType node
				if ( canRead_measloc ) {
					try {
						addNode( measloc_node,
							 measloctype_node );
					}
					catch ( Exception e ) {
						Message.printWarning( 2,
						routine, e );
						Message.printWarning( 3,
						routine,
						"Error adding MeasLoc "+
						"node \"" +
						measloc_node.getName() +
						"\" to " +
						"MeasLocType node \"" +
						measloctype_node.
						getName() + "\"" );
					}
				}
				else {
					continue;
				}

				//get MeasLoc Identifier which can
				//be used to get the MeasTypes associated
				//with this MeasLoc
				String measloc_id = null;
				measloc_id = ml.getIdentifier();
				List mt_vect = null;
				try {
					mt_vect =__dmi.
					readMeasTypeListForMeasLoc_num(
					ml.getMeasLoc_num() );
				}
				catch ( Exception e ) {
					Message.printWarning( 2, routine, e );
					Message.printWarning( 2, routine,
					"Unable to get list of MeasTypes " +
					"for \"" + measloc_id + "\"." );
					mt_vect = new Vector();
				}
				if ( mt_vect == null ) {
					mt_vect = new Vector();
				}
				int size_meastypes= mt_vect.size();
				if ( Message.isDebugOn ) {
					Message.printDebug( 4, routine,
					"Number of MeasTypes for MeasLoc " +
					ml.getMeasLoc_name() + " is: " +
					size_meastypes );
				}

				//get the time series for each MeasLoc.
				for ( int l=0; l<size_meastypes; l++ ) {
					mt = ( RiversideDB_MeasType )
					mt_vect.get(l);
					if ( mt == null ) {
						continue;
					}
					try {
						canRead_meastype =
						__dmi.canRead(
						mt.getDBUser_num(),
						mt.getDBGroup_num(),
						mt.getDBPermissions() );
					}
					catch ( Exception e ) {
						Message.printWarning( 2,
						routine, e );
					}
					if ( Message.isDebugOn ) {
						Message.printDebug( 3, routine,
						"dmi.canRead() returned \"" +
						canRead_meastype +
						"\" for MeasType_num\n" +
						mt.getMeasType_num()+
						"\nfor user with " +
						"DBUser_num: \"" +
						mt.getDBUser_num() +
						"\" and DBGroup_num: \"" +
						mt.getDBGroup_num() +
						" \" and DBPermissions: \"" +
						mt.getDBPermissions() +"\"" );
					}

					//get TSIdent from MeasType and descrip
					TSIdent tsid = null;
					String desc_str = null;
					String ts_create_method = null;
					try {
						tsid = mt.toTSIdent();
					}
					catch ( Exception e ) {
						Message.printWarning( 2,
						routine, e );
					}
					if ( tsid == null ) {
						continue;
					}
					desc_str = mt.getDescription();
					//toUpperCase();
					ts_create_method= mt.getCreate_method();
					//toUpperCase();

					SimpleJTree_Node
					meastype_node =
					new SimpleJTree_Node(
					TSID_STRING + ": " +
					tsid.toString() +
					" - " + desc_str.trim());
					meastype_node.setData( mt );
					meastype_node.setIcon(
					__closedFolderIcon );

					if ( canRead_meastype ) {
						//add MeasType to
						try {
							addNode( meastype_node,
							         measloc_node );
						}
						catch ( Exception e ) {
							Message.printWarning(
							2, routine, e);
							Message.printWarning( 3,
							routine,
							"Error adding " +
							"MeasType node \"" +
							meastype_node.
							getName()+
							"\" to " +
							"MeasLoc node \"" +
							measloc_node.
							getName() + "\"" );
						}

						//make create method node
						SimpleJTree_Node
						meastype_create_node =
						new SimpleJTree_Node(
						ts_create_method);
						meastype_create_node.setData(
						mt );

						//add node
						try {
							addNode(
							meastype_create_node,
							meastype_node );
						}
						catch ( Exception e ) {
							Message.printWarning(
							2, routine, e);
							Message.printWarning( 3,
							routine,
							"Error adding " +
							"MeasType Create node \""
							+ meastype_create_node.
								getName()+
							"\" to " +
							"MeasType node \"" +
							meastype_node.
							getName() + "\"" );
						}

					}

					tsid = null;
					canRead_meastype = false;
					meastype_node = null;
					mt = null;
				}//end l-loop

				canRead_measloc = false;
				measloc_node = null;
				ml = null;
			}//end k-loop ( MeasLoc )

			measloctype_node = null;
			mlt = null;
		}//end j-loop ( MeasLocType )
		if ( measlocgrp_node != null ) {
			expandNode( measlocgrp_node );
		}

		measlocgrp_node = null;
		mlg = null;
	} //end i-loop (MeasLocGroup)

	setFastAdd( false );

	// Expand
	//expandAllNodes();

}


//============================================================================//
//              Action events from the MeasLoc Tree Popup menus               //                                                          //
//============================================================================//

/**
The event handler manages action events.
@param event Event to handle.
*/
public void actionPerformed (ActionEvent event)
{
	String routine = __class + ".actionPerformed", mssg;

	try {
	Object source = event.getSource();

	// Add Station or area
	if (( source == __popup_AddStationPoint_JMenuItem ) ||
	    ( source == __popup_AddArea_JMenuItem    ) ) {

		// Get selected node ( which will be parent of new node )
		// parent type is: MeasLocType
		// ( syntax: "Group: AREAS" or "Group: STATIONS/POINTS" )
		SimpleJTree_Node parent_node = getSelectedNode();

		// Get type (Area or Station) to use in title of the new
		// MeasLoc_Location gui
		String type_new   = null;
		String mlt_str    = null;
		if ( source == __popup_AddArea_JMenuItem ) {
			mlt_str = __popup_AreaProperties_String;
			type_new = "New Area";
		}
		else {
			mlt_str = __popup_StationPointProperties_String;
			type_new = "New Station/Point";
		}

		//get parent of parent, which will be a MeasLocGroup
		//node since we need to assign the MeasLocGroup_num to
		//the new MeasLoc object.
		SimpleJTree_Node grandparent_node= getParentOfNode(parent_node);

		//create new MeasLoc object and give it the MeasLocGroup_num
		//of its parent
		RiversideDB_MeasLoc ml = new RiversideDB_MeasLoc();

		ml.setMeasLocGroup_num( ((RiversideDB_MeasLocGroup)
		grandparent_node.getData() ).getMeasLocGroup_num() );

		if ( Message.isDebugOn ) {
			Message.printDebug( 3, routine,"Creating new " +
			"Station/Area (MeasLoc object) with parent: \""+
			parent_node.getName() + "\" and grandparent= \"" +
			grandparent_node.getName() + "\" " +
			"and MeasLocGroup_num = \"" + ml.getMeasLocGroup_num() +
			"\"." );
		}

		grandparent_node = null;

		RiversideDB_Location_JFrame mlf = null;
		String title = __app_title + " - " + mlt_str + " - " + type_new;
		if ( __windowManager == null ) {
			mlf = new RiversideDB_Location_JFrame(
				__dmi,
				__windowManager,
				title,
				ml );
		} else {
			mlf =  (RiversideDB_Location_JFrame)
				__windowManager.displayWindow(
				__windowManager.WINDOW_MEASLOC,
				true,
				title,
				ml );
		}
		mlf.addRiversideDBSystemListener (this);

		Message.printStatus(1, "", "------------------- LOCATION");
	}
	else if(source == __popup_DefineOriginAsReduction_JMenuItem ) {
		//"Define Origin as Reduction";

		//get TS selected.
		SimpleJTree_Node node = getSelectedNode();

		if ( Message.isDebugOn ) {
			Message.printDebug( 3, routine,
			"Selected Reduction node: \"" +
			node.getName() + "\"" );
		}

		//Get the MeasType object
		RiversideDB_MeasType sel_mt =( RiversideDB_MeasType )
		node.getData();
		//leave create_method set to what it is... will
		//set it to reduction in the Reduction GUI.

		RiversideDB_MeasReduction mr = new
		RiversideDB_MeasReduction();
		mr.setOutputMeasType_num( sel_mt.getMeasType_num() );

		//make new Reduction GUI
		Long id = new Long(mr.getOutputMeasType_num());

		RiversideDB_Reduction_JFrame red_JFrame = null;
		if ( __windowManager == null ) {
			red_JFrame = new RiversideDB_Reduction_JFrame (
				__dmi,
				__windowManager,
				__app_title + " - " +
				  __popup_ReductionProperties_String
					+ " - " + node.getName(),
				sel_mt,
				mr );
		} else {
			red_JFrame = (RiversideDB_Reduction_JFrame)
				__windowManager.displayWindow(
				__windowManager.WINDOW_REDUCTION,
				true,
				__app_title + " - " +
				  __popup_ReductionProperties_String
				   	+ " - " + node.getName(),
				sel_mt,
				mr,
				id );
		}
		red_JFrame.addRiversideDBSystemListener (this);
	}
	else if ( source ==  __popup_AddTimeSeries_JMenuItem ) {
		//create new RiversideDB_MeasType object
		RiversideDB_MeasType mt = new RiversideDB_MeasType();

		SimpleJTree_Node node = getSelectedNode();

		//get Identifier, name, number from MeasLoc Object
		//to assign in new MeasType object
		RiversideDB_MeasLoc ml = (RiversideDB_MeasLoc)
		node.getData();

		mt.setMeasLoc_num( ml.getMeasLoc_num() );
		mt.setMeasLoc_name( ml.getMeasLoc_name() );
		mt.setIdentifier( ml.getIdentifier() );
		//mt.setIdentifier( ml.getIdentifier() );

		String type = ml.getMeas_loc_type();
		String name = ml.getMeasLoc_name();
		if ( type.equals("A") ) {
			type =  __popup_AreaProperties_String;
		}
		else if( type.equals("P" ) ){
			type = __popup_StationPointProperties_String;
		}


		if ( Message.isDebugOn ) {
			Message.printDebug( 4 ,routine,
			"Name of Selected node = " + name );
		}

		//now make TIME SERIES GUI - adding a new TS
		//use the MeasLoc_name for the identifier so that
		//users can open multiple "new TS" windows, but not
		//of from the same location
		RiversideDB_TS_JFrame mlf = null;
		if ( __windowManager == null ) {
			//Message.printWarning( 1, routine, 
			//	"WM is null. Using new RiversideDB_TS_JFrame");
			mlf = new RiversideDB_TS_JFrame(
				__dmi,
				__windowManager,
				__app_title + " - " + type
					+ " - New Time Series",
				mt );
		} else {
			//Message.printWarning(1, routine, 
			//"WM is not null. Using __windowManager.displayWindow(" );
			mlf =  (RiversideDB_TS_JFrame)
				__windowManager.displayWindow(
				__windowManager.WINDOW_MEASTYPE,
				true,
				__app_title + " -  " + type
					+ " - New Time Series",
				mt,
				name );
		}
		mlf.addRiversideDBSystemListener (this);

	} //end add TS

	else if ( source ==  __popup_DeleteArea_JMenuItem ) {
		//get AREA selected and get its parent.
		SimpleJTree_Node node = getSelectedNode();

		boolean canDeleteMeasLoc = false;
		RiversideDB_MeasLoc ml = (RiversideDB_MeasLoc)node.getData();
		long ml_num = ml.getMeasLoc_num();
		if ( Message.isDebugOn ) {
			Message.printDebug( 4, routine,
			"MeasLoc_num for Selected area node to delete: \"" +
			node.getName() + "\" is: " + ml_num + "." );
		}
		try {
			canDeleteMeasLoc=
			__dmi.canDelete(
			ml.getDBUser_num(),
			ml.getDBGroup_num(),
			ml.getDBPermissions() );
		}
		catch( Exception e ){
			Message.printWarning( 2,"", e );
		}
		ml = null;
		if ( Message.isDebugOn ) {
			Message.printDebug( 2, routine,
			"Current user has permissions to delete this "+
			"MeasLoc area (T/F): " + canDeleteMeasLoc );
		}
		if ( !canDeleteMeasLoc ) {
			Message.printWarning( 1, routine,
			"You do not have permisssions to delete " +
			"this Area." );
		}
		//now should have the MeasLoc num
		//can can call delete!
		else if ( ml_num >= 0 ) {
			int confirm =
			JOptionPane.showConfirmDialog( this,
			"Delete Area: \"" + node.getName() + "\"" +
			" and all of its time series?",
			"Confirm Deletion of Area " +  node.getName(),
			JOptionPane.YES_NO_OPTION );

			int[] arrDels = null;
			if ( confirm == JOptionPane.OK_OPTION ) {
				try {	arrDels = __dmi.
					deleteMeasLocForMeasLoc_num(
					ml_num );
				}
				catch ( Exception e ) {
					Message.printWarning( 2,"", e );
					Message.printWarning( 2,"",
					"Unable to delete selected " +
					"MeasLoc object: \""  +
					node.getName() + " \"." );
				}
				// REMOVE that node from the tree
				removeNode( node, false);

				Message.printStatus( 10, routine,
				"Number records deleted from MeasLoc= " +
				arrDels[0] + ", number records " +
				"deleted from StageDischargeRating = " +
				arrDels[1] + ", number records " +
				"deleted from Station = " +
				arrDels[2] + ", number records " +
				"deleted from Area = " +
				arrDels[3] + ", number records " +
				"deleted from MeasType = " +
				arrDels[4] + " . (Total number records " +
				arrDels[5] + ")." );

			} //end if OK
		} //end if ml_num > 0

		//Re-read database to create new Vector of MeasLoc Products
		try {	__measLoc_vect =
			__dmi.readMeasLocList();
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e );
			//create an empty vector
			__measLoc_vect = new Vector();
		}

	}//end delete_area_JMenuItem
	else if ( source ==  __popup_DeleteLocationGroup_JMenuItem ) {

		//get name of MeasLoc
		SimpleJTree_Node node = getSelectedNode();
		if ( Message.isDebugOn ) {
			Message.printDebug( 4, routine,
			"Selected MeasLocGroup node to delete: \"" +
			node.getName() + "\"." );
		}

		RiversideDB_MeasLocGroup mlg = (RiversideDB_MeasLocGroup)
		node.getData();

		boolean canDeleteMeasLocGroup = true;
		int mlg_num_to_del = mlg.getMeasLocGroup_num();
		try {
			canDeleteMeasLocGroup= __dmi.canDelete(
			mlg.getDBUser_num(),
			mlg.getDBGroup_num(),
			mlg.getDBPermissions() );
		}
		catch( Exception e ){
			Message.printWarning( 2,"", e );
			canDeleteMeasLocGroup = false;
			mlg_num_to_del = -999;
		}

		if ( !canDeleteMeasLocGroup ) {
			Message.printWarning( 1, routine,
			"You do not have permisssions to delete " +
			"this Location Group." );
		}
		else {
			int confirm =
			JOptionPane.showConfirmDialog( this,
			"Delete Location Group: \"" + mlg.getName() +"\" ("+
			mlg.getIdentifier() +
			"\") and all its Locations (Areas/Stations/Points) " +
			" and all their time series?",
			"Confirm Deletion of Location Group " +
			mlg.getName(), JOptionPane.YES_NO_OPTION );

			boolean blnDeleted = true;
			int[] arrDeletedRecords = null;
			if ( confirm == JOptionPane.OK_OPTION ) {
				try {
					arrDeletedRecords =
					__dmi.
					deleteMeasLocGroupForMeasLocGroup_num(
					mlg_num_to_del );
				}
				catch ( Exception e ) {
					Message.printWarning( 2,"", e );
					Message.printWarning( 2,"",
					"Unable to delete selected " +
					"Location Group: \""  + mlg.getName() +
					"\" (\"" + mlg.getIdentifier()+ "\")." +
					" (Database error)");
					blnDeleted=false;
				}
				//if ( Message.isDebugOn ) {
					if ( arrDeletedRecords != null ) {
						Message.printStatus( 3, routine,
						"Number of MeasLocGroup " +
						"records deleted = " +
						arrDeletedRecords[0] +
						" and number of related " +
						" records from " +
						"DBUserMeasLocGroupRelation " +
						"deleted = "+
						arrDeletedRecords[1] +
						" and number of related " +
						" records from " +
						"State deleted = "+
						arrDeletedRecords[2] +
						" and number of related " +
						" records from " +
						"StateGroup deleted = "+
						arrDeletedRecords[3] +
						" ( total number of " +
						" records deleted =" +
						arrDeletedRecords[4] +
						")" );
					}
				//}
				if ( blnDeleted ) {
					// REMOVE that node from the tree
					removeNode(node, false);

					// Re-read database to get new Vector
					try {	__measLocGroup_vect =
						__dmi.readMeasLocGroupList();
					}
					catch ( Exception e ) {
						Message.printWarning(
						2, routine, e );

						//create an empty vector
						__measLocGroup_vect =
						new Vector();
					}
					//new vector of MeasLocs
					try {
						__measLoc_vect =
						__dmi.readMeasLocList();
					}
					catch ( Exception e ) {
						Message.printWarning( 2,
						routine, e );
						//create an empty vector
						__measLoc_vect =
						new Vector();
					}
				}
			} //end if OK
		mlg = null;
		}
	}//end delete measlocgroup
	else if ( source ==  __popup_DeleteStationPoint_JMenuItem ) {
		//get name of MeasLoc
		SimpleJTree_Node sta_node = getSelectedNode();
		if ( Message.isDebugOn ) {
			Message.printDebug( 4, routine,
			"Selected station node to delete: \"" +
			sta_node.getName() + "\"." );
		}

		//get MeasLoc num for item selected.
		RiversideDB_MeasLoc ml =
		(RiversideDB_MeasLoc) sta_node.getData();
		long ml_num = -999;

		boolean canDeleteMeasLoc = false;
		ml_num = ml.getMeasLoc_num();
		if ( Message.isDebugOn ) {
			Message.printDebug(
			10, routine, "MeasLoc number " +
			"to delete is: " + ml_num );
		}
		try {
			canDeleteMeasLoc=
			__dmi.canDelete(
			ml.getDBUser_num(),
			ml.getDBGroup_num(),
			ml.getDBPermissions() );
		}
		catch( Exception e ){
			Message.printWarning( 2,"", e );
		}
		ml = null;
		if ( Message.isDebugOn ) {
			Message.printDebug( 2, routine,
			"Current user has permissions to delete this "+
			"MeasLoc station (T/F): " + canDeleteMeasLoc );
		}

		if ( !canDeleteMeasLoc ) {
			Message.printWarning( 1, routine,
			"You do not have permisssions to delete " +
			"this station." );
		}
		//now should have the MeasLoc num
		//can can call delete!
		else if ( ml_num >= 0 ) {
			int confirm =
			JOptionPane.showConfirmDialog( this,
			"Delete Station/Point: \"" + sta_node.getName() + "\"" +
			" and all of its time series and ratings?",
			"Confirm Deletion of Station/Point " +
			sta_node.getName(), JOptionPane.YES_NO_OPTION );

			int[] arrDels = null;
			if ( confirm == JOptionPane.OK_OPTION ) {
				try {	arrDels = __dmi.
					deleteMeasLocForMeasLoc_num(
					ml_num );
				}
				catch ( Exception e ) {
					Message.printWarning( 2,"", e );
					Message.printWarning( 2,"",
					"Unable to delete selected " +
					"MeasLoc object: \""  +
					sta_node.getName() + " \"." );
				}
				// REMOVE that node from the tree
				removeNode( sta_node, false);

				Message.printStatus( 10, routine,
				"Number records deleted from MeasLoc= " +
				arrDels[0] + ", number records " +
				"deleted from MeasLoc = " +
				arrDels[1] + ", number records " +
				"deleted from StageDischargeRating = " +
				arrDels[2] + ", number records " +
				"deleted from RatingTable = " +
				arrDels[3] + ", number records " +
				"deleted from Station = " +
				arrDels[4] + ", number records " +
				"deleted from Area = " +
				arrDels[5] + ", number records " +
				"deleted from MeasType = " +
				arrDels[6] + " . (Total number records " +
				"deleted = " + arrDels[5] + ")."  );

				//Re-read database to create
				//new Vector of MeasLoc Products
				try {	__measLoc_vect =
					__dmi.readMeasLocList();
				}
				catch ( Exception e ) {
					Message.printWarning(
					2, routine, e );

					//create an empty vector
					__measLoc_vect =
					new Vector();
				}

			} //end if OK
		} //end if ml_num > 0
	} //end delete station
	else if ( source ==  __popup_DeleteTimeSeries_JMenuItem ) {
		//get TS selected.
		SimpleJTree_Node ts_node = getSelectedNode();
		if ( Message.isDebugOn ) {
			Message.printDebug( 3, routine,
			"Selected MeasType node to delete: \"" +
			ts_node.getName() + "\"." );
		}

		//remove preceeding "TSID:" string.
		//Format: "TSID: FFDN3.NHDES.PTPX.1IRREGULAR - Precipit"
		String selected_name = null;
		String name_desc = "";
		if ( ts_node.getName().regionMatches(
			true, 0, TSID_STRING, 0, TSID_STRING.length() ) ) {

			//remove the TSID: from the string
			name_desc = ((ts_node.getName()).substring(
			TSID_STRING.length()+1)).trim();
		}
		//now remove the description that follows it.
		int ind = -999;
		ind = name_desc.indexOf( "-" );
		if ( ind >=0 ) {
			selected_name = ( name_desc.substring(0,ind)).trim();
		}
		else {
			//try leaving name as is.
			selected_name = name_desc;
		}

		//now should have the TSIdent-  something like:
		//Format: "FFDN3.NHDES.PTPX.1IRREGULAR"

		//get MeasType num for item selected.
		RiversideDB_MeasType mt = null;
		boolean canDeleteMeasType = false;
		long mt_num = -999;

		try {	mt = __dmi.readMeasTypeForTSIdent(
			selected_name );
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e );
		}
		if ( mt != null ) {
			//then we found match, get MeasType
			//num from it and break out of loop
			mt_num = mt.getMeasType_num();
			try {
				canDeleteMeasType= __dmi.canDelete(
				mt.getDBUser_num(), mt.getDBGroup_num(),
				mt.getDBPermissions() );
			}
			catch( Exception e ){
				Message.printWarning( 2,"", e );
			}
		}
		mt = null;
		if ( !canDeleteMeasType ) {
			Message.printWarning( 1, routine,
			"You do not have permisssions to delete " +
			"this Time Series." );
		}
		//now should have the MeasType num so can can call delete!
		else if ( mt_num >= 0 ) {
			int confirm =
			JOptionPane.showConfirmDialog( this,
			"Delete \"" + ts_node.getName() + "\"?",
			"Confirm Time Series Deletion",
			JOptionPane.YES_NO_OPTION );

			int [] arrDels = null;
			if ( confirm == JOptionPane.OK_OPTION ) {

				try {	arrDels = __dmi.
					deleteMeasTypeForMeasType_num(
					mt_num );
				}
				catch ( Exception e ) {
					Message.printWarning( 2,"", e );
					Message.printWarning( 2,"",
					"Unable to delete selected " +
					"MeasType product: \""  +
					selected_name + " \"." );
				}

				// REMOVE that node from the tree
				removeNode( ts_node );

				Message.printStatus( 10, routine,
				"Number records deleted from MeasType = " +
				arrDels[0] + ", number records " +
				"deleted from ImportConf = " +
				arrDels[1] + ", number records " +
				"deleted from MeasTypeStatus = " +
				arrDels[2] + ", number records " +
				"deleted from MeasTypeStats = " +
				arrDels[3] + ", number records " +
				"deleted from MeasReduction = " +
				arrDels[4] + ", number records " +
				"deleted from MeasReductionRelation = " +
				arrDels[5] + " . (Total number records " +
				arrDels[6] + ")." );

				//re-create the MeasLoc vector so that the
				//the MeasType that was removed is no
				//longer included
				try {	__measLoc_vect =
					__dmi.readMeasLocList();
				}
				catch ( Exception e ) {
					Message.printWarning(
					2, routine, e );

					//create an empty vector
					__measLoc_vect = new Vector();
				}

			} //end if OK
		} //end if mt_num > 0
	} //end delete TS
	else if ( source ==  __popup_ImportProductProperties_JMenuItem  ) {
		//node will be a node that says "Import" but will
		//have no data behind it... it is just the create_method of
		//the parent MeasType
		SimpleJTree_Node sel_node = getSelectedNode();
		SimpleJTree_Node parent_node = getParentOfNode( sel_node );

		//get data from the parent node
		RiversideDB_MeasType mt =
		(RiversideDB_MeasType) parent_node.getData();

		//now we have MeasType object (which has create_method of
		//Import), we can use it to find the ImportProduct
		RiversideDB_ImportConf ic = null;
		try {
			ic = __dmi.readImportConfForMeasType_num(
			mt.getMeasType_num() );
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e );
			ic = null;
		}
		if ( ic == null ) {
			Message.printWarning( 1, routine,
			"No Import time series (ImportConf objects) found for "+
			"time series \"" + parent_node.getName() + "\". " +
			"Unable to open Import GUI." );
			if ( Message.isDebugOn ) {
				Message.printDebug( 2, routine,
				"Query to identify ImportConf objects " +
				"related to MeasType with MeasType_num = " +
				mt.getMeasType_num() +
				" (\"__dmi.readImportConfForMeasType_num \") "+
				"failed. MeasType object =" + mt.toString() );
			}
		}
		if ( ic != null ) {
			RiversideDB_ImportProduct ip = null;
			try {
				ip =
				__dmi.readImportProductForImportProduct_num(
				ic.getImportProduct_num() );
			}
			catch ( Exception e ) {
				Message.printWarning( 2, routine, e );
				ip = null;
			}
			mt = null;
			ic = null;

			Long id = new Long(ip.getImportProduct_num());

			RiversideDB_Import_JFrame ipf = null;
			if ( __windowManager == null ) {
				ipf = new RiversideDB_Import_JFrame(
					__dmi,
					__windowManager,
					__app_title + " - Import Product - "
						+ ip.getProduct_name(),
					ip );
			} else {
				ipf =  (RiversideDB_Import_JFrame)
					__windowManager.displayWindow(
					__windowManager.WINDOW_IMPORT_PRODUCT,
					true,
					__app_title + " - Import Product - "
						    + ip.getProduct_name(),
					ip,
					id );
			}
			ipf.addRiversideDBSystemListener (this);
		}

	}
	else if ( source ==  __popup_AddLocationGroup_JMenuItem ) {
		//open MeasLocGroup GUI to create a new MeasLocGroup object
		RiversideDB_MeasLocGroup mlg = new RiversideDB_MeasLocGroup();

		RiversideDB_MeasLocGroup_JFrame mlg_JFrame = null;
		if ( __windowManager == null ) {
			mlg_JFrame = new RiversideDB_MeasLocGroup_JFrame(
				__dmi,
				__windowManager,
				__app_title + " - "
					+ __popup_LocationGroupProperties_String
					+ " - New Location Group",
				mlg );
		} else {
			mlg_JFrame = (RiversideDB_MeasLocGroup_JFrame)
				__windowManager.displayWindow(
				__windowManager.WINDOW_MEASLOCGROUP,
				true,
				__app_title + " -  "
					+ __popup_LocationGroupProperties_String
					+ " - New Location Group",
				mlg,
				null );
		}
		mlg_JFrame.addRiversideDBSystemListener (this);
	}

	//AREA
	else if ( source == __popup_AreaProperties_JMenuItem ) {
		//show the MeasLoc GUI Station Properties.
		//get selected node
		SimpleJTree_Node node = getSelectedNode();

		String stripped_name = null;
		String name = node.getName();
		//now  remove the string from the front of
		//the group name: "ID:"
		if (name.regionMatches(
		true, 0, ID_STRING, 0, ID_STRING.length() ) ) {
			stripped_name = name.substring( ID_STRING.length() +1);
		}
		//now get parent, which will be MeasLocType object.
		//In the tree, this is the "Group: AREA" or "Group: STATIONS/
		//POINTS" node
		//SimpleJTree_Node parent_node = getParentOfNode( node );

		/*
		//get parent of the MeasLocType node to get the MeasLocGroup
		//node
		SimpleJTree_Node grandparent_node= getParentOfNode(parent_node);
		*/

		//show the MeasLoc GUI AREA Properties.
		RiversideDB_MeasLoc ml = (RiversideDB_MeasLoc)node.getData();
		Long id = new Long(ml.getMeasLoc_num());

		RiversideDB_Location_JFrame mlf = null;
		if ( __windowManager == null ) {
			 mlf = new RiversideDB_Location_JFrame(
				__dmi,
				__windowManager,
				__app_title + " - "
					+ __popup_AreaProperties_String
					+ " - " + stripped_name,
				(RiversideDB_MeasLoc) node.getData() );
		} else {
			mlf = (RiversideDB_Location_JFrame)
				__windowManager.displayWindow(
				__windowManager.WINDOW_MEASLOC,
				true,
				__app_title + " - "
					+ __popup_AreaProperties_String
					+ " - " + stripped_name,
				ml,
				id );
		}
		mlf.addRiversideDBSystemListener (this);

		//Message.printStatus(1, "", "------------------- LOCATION");

	}//end areas_properties
	else if ( source ==  __popup_LocationGroupProperties_JMenuItem ) {
		//find corresponding MeasLocGroup object by getting selected
		//node
		SimpleJTree_Node node = getSelectedNode();

		if ( Message.isDebugOn ) {
			Message.printDebug( 3, routine,
			"Selected Reduction node: \"" +
			node.getName() + "\"");
		}
		RiversideDB_MeasLocGroup mlg =
			(RiversideDB_MeasLocGroup) node.getData();

		Long id = new Long(mlg.getMeasLocGroup_num());

		RiversideDB_MeasLocGroup_JFrame mlg_JFrame = null;
		if ( __windowManager == null ) {
			mlg_JFrame = new RiversideDB_MeasLocGroup_JFrame(
				__dmi,
				__windowManager,
				__app_title + " - "
					+ __popup_LocationGroupProperties_String
					+ " - " + mlg.getName(),
				(RiversideDB_MeasLocGroup) node.getData() );
		} else {
			mlg_JFrame = (RiversideDB_MeasLocGroup_JFrame)
				__windowManager.displayWindow(
				__windowManager.WINDOW_MEASLOCGROUP,
				true,
				__app_title + " -  "
					+ __popup_LocationGroupProperties_String
					+ " - " + mlg.getName(),
				(RiversideDB_MeasLocGroup) node.getData(),
				id );
		}
		mlg_JFrame.addRiversideDBSystemListener (this);

	}//end measlocgrp properties

	//STATION
	else if ( source == __popup_StationPointProperties_JMenuItem ) {

		//show the MeasLoc GUI Station Properties.
		//get selected node
		SimpleJTree_Node node = getSelectedNode();

		String stripped_name = null;
		String name = node.getName();
		//now  remove the string from the front of
		//the group name: "ID:"
		if (name.regionMatches(
		true, 0, ID_STRING, 0, ID_STRING.length() ) ) {
			stripped_name = name.substring( ID_STRING.length() +1);
		}
		//now get parent which will be MeasLocType node (appears in
		//tree as: "Group: AREA" or "Group: STATIONS/POINTS" )
		//SimpleJTree_Node parent_node = getParentOfNode( node );

		/*
		//get parent of the MeasLocType node to get the MeasLocGroup
		//node
		SimpleJTree_Node grandparent_node= getParentOfNode(parent_node);
		*/

		// make STATIONS GUI
		//Pass in the station or area that is selected
		RiversideDB_MeasLoc ml = (RiversideDB_MeasLoc)node.getData();
		Long id = new Long(ml.getMeasLoc_num());

		RiversideDB_Location_JFrame mlf = null;
		if ( __windowManager == null ) {
			mlf = new RiversideDB_Location_JFrame(
				__dmi,
				__windowManager,
				__app_title + " - "
				    + __popup_StationPointProperties_String
				    + " - " + stripped_name,
				(RiversideDB_MeasLoc) node.getData() );
		} else {
			mlf = (RiversideDB_Location_JFrame)
				__windowManager.displayWindow(
				__windowManager.WINDOW_MEASLOC,
				true,
				__app_title + " - "
				    + __popup_StationPointProperties_String
				    + " - " + stripped_name,
				ml,
				id );
		}
		mlf.addRiversideDBSystemListener (this);
	}

	//Time Series
	else if ( source ==  __popup_TimeSeries_Properties_JMenuItem ) {

		SimpleJTree_Node node = getSelectedNode();

		//get data object to get name
		RiversideDB_MeasType mt = (RiversideDB_MeasType)
		node.getData();

		String name = mt.toTSIdent().toString();

		//now make TIME SERIES GUI - editing existing TS
		//use the MeasType_num as the identifier for an
		//existing TS, but pass it in as a String (since for
		//a new TS, the name (String) is passed in).
		String id =""+ new Long(mt.getMeasType_num());

		RiversideDB_TS_JFrame mlf = null;
		if ( __windowManager == null ) {
			mlf = new RiversideDB_TS_JFrame (
				__dmi,
				__windowManager,
				__app_title + " - "
					+ __popup_TimeSeriesProperties_String
					+ " - " + name,
				mt );
		} else {
			mlf = (RiversideDB_TS_JFrame)
				__windowManager.displayWindow(
				__windowManager.WINDOW_MEASTYPE,
				true,
				__app_title + " -  "
					+ __popup_TimeSeriesProperties_String
					+ " - " + name,
				mt,
				id );
		}
		mlf.addRiversideDBSystemListener (this);

	}//end TS properties
	//delete reduction
	else if ( source ==  __popup_DeleteReduction_JMenuItem ) {
		//this is the MeasReduction object that is selected
		SimpleJTree_Node node = getSelectedNode();

		//get node's parent, which is the MeasType object
		SimpleJTree_Node parent_node = getParentOfNode( node );

		if ( Message.isDebugOn ) {
			Message.printDebug( 3, routine,
			"Selected Reduction node: \"" +
			node.getName() + "\" and its parent node is: \"" +
			parent_node.getName() + "\"" );
		}
		RiversideDB_MeasType existing_mt = (RiversideDB_MeasType)
		parent_node.getData();
		String tsid_str = null;
		try {
			tsid_str = existing_mt.toTSIdent().toString();
		}
		catch ( Exception  e ) {
			Message.printWarning( 2, routine, e );
			tsid_str = "";
		}

		boolean blnContinue = true;
		boolean canDeleteMeasType = false;
		try {
			canDeleteMeasType= __dmi.canDelete(
			existing_mt.getDBUser_num(),
			existing_mt.getDBGroup_num(),
			existing_mt.getDBPermissions() );
		}
		catch( Exception e ){
			Message.printWarning( 2,"", e );
		}

		RiversideDB_MeasType new_mt = new RiversideDB_MeasType(
		existing_mt );

		if ( !canDeleteMeasType ) {
			Message.printWarning( 1, routine,
			"You do not have permisssions to delete " +
			"this (\"" +tsid_str +"\") Reduction Time Series." );
		}
		else if ( blnContinue ) {
			if ( Message.isDebugOn ) {
				Message.printDebug( 3, routine,
				"Changing selected MeasType time series: \"" +
				tsid_str + "\" from having create_method = \"" +
				"REDUCTION\" to \"UNKNOWN\"" );
			}
			new_mt.setCreate_method( "UNKNOWN" );
			int confirm =
			JOptionPane.showConfirmDialog( this,
			"Deleting reduction will require that " +
			"another reduction or an import be " +
			"defined to provide time series data.",
			"Confirm Deleting Reduction for \"" + parent_node.
			getName()+ "\"", JOptionPane.YES_NO_OPTION );
			if ( confirm == JOptionPane.OK_OPTION ) {

				//write it back to the database
				try {
					__dmi.writeMeasType( new_mt );
				}
				catch (Exception e ) {
					Message.printWarning( 2, routine, e );
					Message.printWarning( 2, routine,
					"Failed to delete Reduction from " +
					"database.  Please see log " +
					"file for details." );
					blnContinue = false;
				}
			}
			else { //they hit NO at the confirmation message
				blnContinue=false;
			}
		}

		//update tree to reflect that the time series is now
		//UNKNOWN
		if ( blnContinue ) {
			updateReductionNode(existing_mt, new_mt );
		}

	}//end delete reduction
	//Define origin as Reduction properties... means a reduction
	//node is selected
	else if ( source ==
	__popup_ReductionProperties_JMenuItem ) {
		//this node has no data... it is just a string
		//that contains the word "REDUCTION"
		SimpleJTree_Node node = getSelectedNode();
		//get node's parent, which is the MeasLoc object
		SimpleJTree_Node parent_node = getParentOfNode( node );

		//parent node is a MeasType object
		RiversideDB_MeasType parent_mt = ( RiversideDB_MeasType )
		parent_node.getData();

		if ( Message.isDebugOn ) {
			Message.printDebug( 3, routine,
			"Selected Reduction node: \"" +
			node.getName() + "\" and its parent node is: \"" +
			parent_node.getName() + "\"" );
		}

		//get current MeasReduction object.
		RiversideDB_MeasReduction mr =null;
		try {
			mr = __dmi.readMeasReductionForOutputMeasType_num(
			parent_mt.getMeasType_num() );
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e );
			mr = null;
		}

		if ( mr == null ) {
			//should we fix this this now, aka,
			//do we change MeasType's create method??
			//This should not happen at all if
			//DB set up correctly...
			mssg = "Although parent node:\n\""
				+ parent_node.getName()
				+ "\" has create method of \"reduction\",\n"
				+ "no matching entry is found in the MeasReduction "
				+ "table (for MeasType_num= "
				+ parent_mt.getMeasType_num()
				+ "). \nPlease update "
				+ "MeasType object to have create method: \""
				+ "unknown\".";
			Message.printWarning( 1, routine, mssg );
// REVISIT [LT] 2005-01-07 - Temporarely modified until a correction for this
//			     kind of problems is coded. For now just make sure
//			     the user receive a GUI message also.
			JOptionPane.showMessageDialog(
				this, mssg, "Warning",
				JOptionPane.WARNING_MESSAGE);
			//parent_mt.setCreate_method("UNKNOWN");
		}
		else {

			//make new Reduction GUI
			Long id = new Long(mr.getOutputMeasType_num());

			RiversideDB_Reduction_JFrame red_JFrame = null;
			if ( __windowManager == null ) {
				red_JFrame = new RiversideDB_Reduction_JFrame (
					__dmi,
					__windowManager,
					__app_title + " - " +
				__popup_ReductionProperties_String
						+ " - " + parent_node.getName(),
					parent_mt,
					mr );
			} else {
				red_JFrame = (RiversideDB_Reduction_JFrame)
					__windowManager.displayWindow(
					__windowManager.WINDOW_REDUCTION,
					true,
					__app_title + " - " +
				__popup_ReductionProperties_String
						+ " - " + parent_node.getName(),
					parent_mt,
					mr,
					id );
			}
			red_JFrame.addRiversideDBSystemListener (this);
		}
	}

	} // End of try
	catch ( Exception e ) {
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

		//UPdate POPUP menu to portray menu
		//items specific to item selected.
		//now see if it is a segment...
		//TOP NODE - no menus
		// __treeString = "Station/Area Locations"
		if ( (node.getName() ).equals( __treeString ) ) {
			//then we have the TOPMost part of tree selected
			//add New MeasLocgroup menu item
			__treeJPopupMenu.removeAll();

			if ( __canWriteMeasLocGroup ) {
				//add menu we want
				__treeJPopupMenu. add(
					__popup_AddLocationGroup_JMenuItem );
			} //else don't add menu since
			/*
			else {
				Message.printStatus(1,"",
				"User does not have permissions to " +
				"add new MeasLocGroup record" );
			}
			*/

		}

		// MeasLocGroup_nums
		// MEASLOCGRP_STRING = "Locations Group"
		else if ( (node.getName() ).startsWith(
			MEASLOCGRP_STRING + ": " ) ) {
			//remove all menus
			__treeJPopupMenu.removeAll();

			//Add PROPERTIES for MeasLocGroup
			__treeJPopupMenu.add(
				__popup_LocationGroupProperties_JMenuItem );
			//Add DELETE MeasLocGrp
			__treeJPopupMenu.add(
				__popup_DeleteLocationGroup_JMenuItem );
		}

		// "STATION/POINT" - "Group: STATIONS/POINTS"
		// Should match the Top-level Stations/Points Menu item
		else if ( (node.getName() ).equals( GRP_STRING + ": " +
			__measLoc_StationsPoints_String ) ) {
			//then we have the Station/Point top element
			//Add ADD STATION
			__treeJPopupMenu.removeAll( );

			if ( __canWriteMeasLoc ) {
				//add menu we want
				__treeJPopupMenu.
				add(
				__popup_AddStationPoint_JMenuItem );
			} //else don't add menu since
			/*
			else {
				Message.printStatus(1,"",
				"User does not have permissions to " +
				"add new MeasLoc (station) record" );
			}
			*/

		}

		// "AREA"  -  "Group: AREAS"
		// Should match the Top-level Stations/Points Menu item
		else if ( (node.getName() ).equals(
			GRP_STRING + ": " + __measLoc_Areas_String ) ) {
			//remove all menu items
			__treeJPopupMenu.removeAll();

			if ( __canWriteMeasLoc ) {
				//then we have the AREA top element
				//Add ADD AREA
				__treeJPopupMenu.add(
					__popup_AddArea_JMenuItem );
			}
			/*
			else {
				Message.printStatus(1,"",
				"User does not have permissions to " +
				"add new MeasLoc (area) record" );
			}
			*/

		}

		// Particular STATION/area NAME SELECTED
		else if ( (node.getName() ).regionMatches(
			true, 0, ID_STRING, 0, ID_STRING.length()) ) {
			//then we have a ID: with station or
			//area name selected, show

			//get parent to see if we are
			//dealing with a station or an area
			SimpleJTree_Node parent_node = getParentOfNode( node );

			//if parent is "Stations/Points" top menu
			if ( (parent_node.getName()).equalsIgnoreCase(
			GRP_STRING + ": " + __measLoc_StationsPoints_String ) ) {

				//remove all menu items
				__treeJPopupMenu.removeAll();

				//Add PROPERTIES for STATION
				__treeJPopupMenu.add(
					__popup_StationPointProperties_JMenuItem );
				//Add DELETE station/point
				__treeJPopupMenu.add(
					__popup_DeleteStationPoint_JMenuItem );
			}
			//if parent is "AREAS" top menu
			else if ( (parent_node.getName()).
			equalsIgnoreCase( GRP_STRING + ": " +
			__measLoc_Areas_String ) ) {
				//make menus specific for areas.

				//remove all menu items
				__treeJPopupMenu.removeAll();

				//Add PROPERTIES for AREAS
				__treeJPopupMenu.add(
					__popup_AreaProperties_JMenuItem );

				//Add DELETE area
				__treeJPopupMenu.add(
					__popup_DeleteArea_JMenuItem );

			}

			if ( __canWriteMeasType ) {
				//now add the Generic menu items
				//that are the same for stations and areas
				//Add ADD TS
				__treeJPopupMenu.add(
					__popup_AddTimeSeries_JMenuItem );
			}

			/*
			else {
				Message.printStatus(1,"",
				"User does not have permissions to " +
				"add new MeasType record" );
			}
			*/

		}

		// TIMESERIES Selected - Properties
		else if ( (node.getName() ).regionMatches(
			true, 0, TSID_STRING, 0, TSID_STRING.length() ) ) {
			//Then a TS is selected - get it properties

			//remove all menu items
			__treeJPopupMenu.removeAll();

			//Add PROPERTIES menu for TS
			__treeJPopupMenu.add(
				__popup_TimeSeries_Properties_JMenuItem );

			//Add DELETE TS
			__treeJPopupMenu.add(
				__popup_DeleteTimeSeries_JMenuItem );

			//if create_method is UNKNOWN only,
			//then add the Define TS as Reduction menu.

			//get Data object to determine create_method
			RiversideDB_MeasType mt = (RiversideDB_MeasType)
			node.getData();

			String create_str = null;
			if ( mt != null ) {
				create_str = mt.getCreate_method();
			}
			if ( create_str == null  ) {
				create_str = TS_UNKNOWN_STRING;
			}

			if ( create_str.equals(TS_UNKNOWN_STRING )) {
			//if ( ( ! create_str.equals(TS_REDUCT_STRING )) ||
			//( ! create_str.equals(TS_IMPORT_STRING ) ) ) {}
				//add "define TS as reduction menu
				__treeJPopupMenu.add(
					__popup_DefineOriginAsReduction_JMenuItem );
			}
		}

		// Check to see if we have a REDUCTION selected
		else if ( (node.getName() ).regionMatches(
			true, 0, TS_REDUCT_STRING, 0, TS_REDUCT_STRING.length() ) ) {
			//remove all menu items
			__treeJPopupMenu.removeAll();

			//add Reduction Properties
			__treeJPopupMenu.add(
				__popup_ReductionProperties_JMenuItem );
			//add Delete Reduction
			__treeJPopupMenu.add(
				__popup_DeleteReduction_JMenuItem );
		}

		// Check to see if we have a TS_IMPORT selected
		else if ( (node.getName() ).regionMatches(
			true, 0, TS_IMPORT_STRING, 0, TS_IMPORT_STRING.length() ) ) {

			//remove all menu items
			__treeJPopupMenu.removeAll();

			//add the Import
			__treeJPopupMenu.add(
				__popup_ImportProductProperties_JMenuItem );

		}

		// Check to see if we have a TS_UNKNOWN selected
		else if ( (node.getName() ).regionMatches(
			true, 0, TS_UNKNOWN_STRING, 0, TS_UNKNOWN_STRING.length() ) ) {

			// Remove all menu items
			__treeJPopupMenu.removeAll();
		}

		else {
			// Remove all menu items
			__treeJPopupMenu.removeAll();
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
Adds a MeasLocGroup node to the MeasLoc tree and updates the vector of
RTiDB_MeasLocGroup objects.
@param mlg RiversideDB_MeasLocGroup object to add to MeasLoc JTree.
These objects are the top-level items in the MeasLoc JTree, so
just need to be added to the top node.
*/
public void addMeasLocGroupNode( RiversideDB_MeasLocGroup mlg )
{
	String routine = __class + ".addMeasLocGroupNode";

	if ( Message.isDebugOn ) {
		Message.printDebug( 4, routine,
			" called with MeasLocGroup object: "
			+ mlg.toString( ) );
	}

	if ( mlg == null ) {
		return;
	}

	SimpleJTree_Node node = new SimpleJTree_Node( MEASLOCGRP_STRING
		+ ": " + mlg.getIdentifier() + " - " + mlg.getName() );
	node.setData( mlg);
	node.setIcon( __closedFolderIcon );

	// add new child node to parent which is the TOP NODE
	try {
		addNode( node, __treeTopNode );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, "Error adding new "
			+ "Locations Group to the Locations tree.  "
			+ "Will try to add new Locations Group to database,"
			+ " so tree should be updated on restart of the"
			+ " application.  "
			+ "See log files for details about the error." );
		Message.printWarning( 2, routine, e );
	}

	// Update tree
	refresh();

	// Select added node
	scrollToVisibleNode( node );

	// Now add this new MeasLocGroup to the vector of
	// MeasLocGroup products by re-reading the database!
	// get vector of all MeasLoc objects...
	if ( __measLocGroup_vect == null ) {
		try {	__measLocGroup_vect = __dmi.readMeasLocGroupList();
		}
		catch ( Exception e ) {
			Message.printWarning( 2, routine, e );
			// Create an empty vector
			__measLocGroup_vect = new Vector();
		}
	}
}

/**
Adds a MeasLoc node to the MeasLoc tree and updates the vector of
RTiDB_MeasLoc objects.
@param ml RiversideDB_MeasLoc object to add to MeasLoc JTree.  The
MeasLoc object contains information on the MeasLocGroup it belongs to,
which will be its parent node.
*/
public void addMeasLocNode( String grandparent, RiversideDB_MeasLoc new_ml )
{
	String routine = __class + ".addMeasLocNode";

	if ( Message.isDebugOn ) {
		Message.printDebug( 4, routine,
		"Called with grandparent: \"" + grandparent + "\"" );
	}

	// Make new node
	SimpleJTree_Node child_node = new SimpleJTree_Node(
	ID_STRING + ": " + new_ml.getIdentifier() + " - " +
	new_ml.getMeasLoc_name() );

	child_node.setData( new_ml );
	child_node.setIcon( __closedFolderIcon );

	// Find grandparent (the correct MeasLocGroup) of node
	// (since direct parent is either: A or P and are not unique)
	SimpleJTree_Node grandparent_node = findNodeByName (
		MEASLOCGRP_STRING + ": " + grandparent );

	Object [] arrChildren =null;
	if ( grandparent_node == null ) {
		arrChildren = null;
	}
	else {
		// Get children of this node which will be: "Group: AREAS" and
		// "Group: STATIONS/POINTS"
		arrChildren = getChildrenArray( grandparent_node );
	}

	if ( arrChildren == null ) {
		Message.printWarning( 2, routine, "Error updating " +
		"Station/Area Locations tree with new Location. " +
		"Will try to update database, but new node will not " +
		"display in tree. See log for error details" );
		Message.printWarning( 4, routine,
		"Unable to locate children nodes (AREA or STATIONS/PONTS) " +
		"for Locations Group node: \"" +grandparent + "\"" );

		return;
	}

	int num = arrChildren.length;
	RiversideDB_MeasLocType mlt = null;
	SimpleJTree_Node parent_node = null;
	String type = null;
	//String full_type = null;
	for ( int i=0; i< num; i++ ) {
		parent_node = (SimpleJTree_Node)arrChildren[i];
		// Get data
		mlt = (RiversideDB_MeasLocType) parent_node.getData();
		if ( mlt == null ) {
			continue;
		}
		// Get type.
		type = mlt.getType();
		mlt = null;
		/*
		if ( type.equalsIgnoreCase( "A" ) ) {
			full_type = GRP_STRING + ": " + __measLoc_Areas_String;
		}
		else if ( type.equalsIgnoreCase( "P" ) ) {
			full_type = GRP_STRING + ": " + __measLoc_StationsPoints_String;
		}
		*/

		if ( type.equals( new_ml.getMeas_loc_type() ) ) {
			// Found the correct node to attach new measloc to.
			try {
				addNode( child_node, parent_node );
			}
			catch ( Exception e ) {
				Message.printWarning( 2, routine, e );
			}
			break;
		}
		parent_node = null;
		mlt = null;
		type = null;
	}

	expandNode( child_node );
	scrollToVisibleNode( child_node );

	// Now add this new MeasLoc to the vector of
	// MeasLoc products by re-reading the database!
	// get vector of all MeasLoc objects...
	try {	__measLoc_vect = __dmi.readMeasLocList();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		// create an empty vector
		__measLoc_vect = new Vector();
	}
}

/**
Add a new MeasType node to the MeasLoc JTree.
@param mt RiversideDB_MeasType object to add to tree.  Using the
MeasType, the parent RiversideDB_MeasLoc can be identified.
*/
public void addMeasTypeNode( RiversideDB_MeasType mt )
{
	String routine = __class + ".addMeasTypeNode";

	if ( mt == null ) {
		return;
	}

	String tsid_str = null;
	try {
		tsid_str = mt.toTSIdent().toString();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		Message.printWarning( 2, routine,
			"Unable to convert MeasType to TSIdent "
			+ " for MeasType object: " + mt.toString() );
		tsid_str = "";
	}
	String desc_str = mt.getDescription();

	SimpleJTree_Node node = new SimpleJTree_Node( TSID_STRING +
		": " + tsid_str + " - " + desc_str );

	if ( Message.isDebugOn) {
		Message.printDebug( 3,routine, "Making new MeasType "
			+ "node named:\"" + node.getName() + "\"." );
	}

	node.setData( mt);
	node.setIcon( __closedFolderIcon );

	// Get information from MeasType object to find the MeasLoc node
	// it belongs to.
	String measloc_name = mt.getMeasLoc_name();
	String measloc_id = mt.getIdentifier();

	SimpleJTree_Node parent_node = findNodeByName(
		ID_STRING + ": " + measloc_id + " - " + measloc_name );

	if ( Message.isDebugOn) {
		Message.printDebug( 2, routine,
			"Parent of new MeasType node is: \"" +
			parent_node.getName() + "\"" );
	}

	//add new child node to parent which is the TOP NODE
	try {
		addNode( node, parent_node );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, "Error encountered " +
		"while adding new Time Series node to the Location " +
		"tree.  Time series added to the database, but will not " +
		"appear in the tree until application is re-started. " +
		"Please see log file for details about the error." );
		Message.printWarning( 2, routine, e);
	}

	// Since this is an entirely new MeasType object, the Create
	// Method should be UNKNOWN
	SimpleJTree_Node create_node= new SimpleJTree_Node( TS_UNKNOWN_STRING );

	// Add this to the tree
	try {
		addNode( create_node, node );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, "Error encountered " +
		"while adding Create Method node to the new MeasType node " +
		"in the Locations tree.  The create method should " +
		"alredy be set correctly in the database as \"UNKNOWN\"." +
		"Please see log file for details about the error." );
	}

	// Reload
	refresh();
	// Select added node
	scrollToVisibleNode( node );

	// Re-read the MeasLoc vector so that the vector will be updated and
	// the correct parent MeasLoc will have the new child MeasType
	try {	__measLoc_vect = __dmi.readMeasLocList();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		// create an empty vector
		__measLoc_vect = new Vector();
	}
}

// REVISIT [LT] 2004-12-08 - The implementation of this method in the original
//	RTAssistant_Main_JFrame.java (prior to 03.00.00) was commented out while
//	the implementation of the updatedReductionNode method was not commented
//	out and was used. Need to figure out what should be the logic and fix if
//	needed.
//      See also RiversideDB_EditorBase_JFrame and RiversideDB_MeasLoc_JTree.
/**
Updates the node under a Time Series node to REDUCTION. This
is called only if the Time Series had not had a node previously
signifying that it had a create_method of UNKNOWN.
@param old_mt RiversideDB_MeasType object that needs updating
@param new_mt RiversideDB_MeasType new MeasType object
the JTree
*/
public void addUpdatedReductionNode(
	RiversideDB_MeasType old_mt,
	RiversideDB_MeasType new_mt ) {

/*	String routine = __class + ".addUpdatedReductionNode";
	//find this node
	SimpleJTree_Node existing_node = findNodeWithData( old_mt );

	if ( node == null ) {
		Message.printWarning( 2, routine,
		"Unable to find Time Series node on JTree to " +
		"update from having create method: \"UNKNOWN\" " +
		"to: \"REDUCTION\".  Data should be updated in " +
		"database, so next time application is run, the " +
		"time series should appear correctly in the tree." );
	}

	Vector v = getChildrenVector( existing_node );
	SimpleJTree_Node existing_child_node =
	(SimpleJTree_Node) v.elementAt(i);
	v = null;

	SimpleJTree_Node new_node = new SimpleJTree_Node(
	TSID_STRING + ": " + tsid.toString() + " - " +
	new_mt.getDescription() );
	new_node.setData( new_mt );
	meastype_node.setIcon( __closedFolderIcon );

	//update node
	try {
		replaceNode( existing_node, new_node );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, "Unable to update " +
		"Time Series node in tree, but will still update "+
		"database, so next time application is re-started, "+
		"node should be updated in tree.  " +
		"See log for error details." );
		Message.printWarning( 2, routine, e );
	}

	//now update the node below it which has the create method
	existing_child_node.setName( new_mt.getCreate_method() );

	//reload
	refresh();
	//select added node
	scrollToVisibleNode( node );
*/
}

/**
Updates a MeasLocGroup node on the MeasLoc tree and updates the vector of
RTiDB_MeasLocGroup objects.
@param new_mlg RiversideDB_MeasLocGroup object containing the information
for the updated node.
@param old_mlg RiversideDB_MeasLocGroup object containing the information
for the old node that is to be replaced.
*/
public void updateMeasLocGroupNode( RiversideDB_MeasLocGroup new_mlg,
				    RiversideDB_MeasLocGroup old_mlg )
{
	String routine = __class + ".updateMeasLocGroupNode";

	SimpleJTree_Node existing_node = findNodeByName( MEASLOCGRP_STRING
		+ ": " + old_mlg.getIdentifier() + " - " + old_mlg.getName() );

	SimpleJTree_Node new_node = new SimpleJTree_Node( MEASLOCGRP_STRING
		+ ": " + new_mlg.getIdentifier() + " - " + new_mlg.getName() );
	new_node.setData( new_mlg );
	new_node.setIcon( __closedFolderIcon );

	try {
		replaceNode( existing_node, new_node );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, "Unable to update " +
		"Location Group node in tree, but will still update "+
		"database, so next time application is re-started, "+
		"node should be updated in tree.  " +
		"See log for error details." );
		Message.printWarning( 2, routine, e );
	}

	// Update model
	refresh();

	// Scroll to node
	scrollToVisibleNode( new_node );

	// Now add this new MeasLocGroup to the vector of
	// MeasLocGroup products by re-reading the database!
	try {	__measLocGroup_vect = __dmi.readMeasLocGroupList();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		//create an empty vector
		__measLocGroup_vect = new Vector();
	}
}

/**
Updates a MeasLoc node on the MeasLoc tree and updates the vector of
RTiDB_MeasLoc objects.
@param new_ml RiversideDB_MeasLoc object containing the information
for the updated node.
@param old_ml RiversideDB_MeasLoc object containing the information
for the old node that is to be replaced.
*/
public void updateMeasLocNode( RiversideDB_MeasLoc new_ml,
			       RiversideDB_MeasLoc old_ml )
{
	String routine = __class + ".updateMeasLocNode";

	// Find MeasLocGroup string that will match String in JTree
	SimpleJTree_Node existing_node =
	findNodeByName( ID_STRING + ": " +
		old_ml.getIdentifier() + " - " + old_ml.getMeasLoc_name() );

	// Make new node:
	SimpleJTree_Node new_node = new SimpleJTree_Node(ID_STRING + ": " +
		new_ml.getIdentifier() + " - " + new_ml.getMeasLoc_name() );
	new_node.setData( new_ml );
	new_node.setIcon( __closedFolderIcon );

	try {
		replaceNode( existing_node, new_node );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, "Unable to update " +
		"Location node in tree, but will still update database, so " +
		"next time application is re-started, node should be " +
		"updated in tree.  See log for error details." );
		Message.printWarning( 2, routine, e );
	}
	// Update JTree
	refresh();

	// Scroll to node
	scrollToVisibleNode( new_node );

	// Now add this new MeasLoc to the vector of
	// MeasLoc products by re-reading the database!
	// get vector of all MeasLoc objects...
	try {
		__measLoc_vect = __dmi.readMeasLocList();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		// Create an empty vector
		__measLoc_vect = new Vector();
	}
}

/**
Updates a MeasType in the MeasLoc JTree.
@param existing_child Name of node to change as it currently appears on
the JTree.
@param new_child Name to change node to.
*/
public void updateMeasTypeNode( RiversideDB_MeasType new_mt,
				RiversideDB_MeasType old_mt )
{
	String routine = __class + ".updateMeasTypeNode";

	// Find MeasType node
	// get TSIdent
	TSIdent new_tsident = null;
	TSIdent old_tsident = null;
	try {
		new_tsident = new_mt.toTSIdent();
		old_tsident = old_mt.toTSIdent();
	}
	catch( Exception e ) {
		Message.printWarning( 2, routine, e );
		new_tsident = null;
		old_tsident = null;
	}

	SimpleJTree_Node existing_node = null;

	if ( Message.isDebugOn ) {
		Message.printDebug( 3,routine,"Looking to update node: \"" +
			TSID_STRING + ": " + old_tsident.toString() + " - " +
			old_mt.getDescription() +"\"");
	}
	try {
		existing_node = findNodeByName( TSID_STRING + ": " +
			old_tsident.toString()+ " - "+ old_mt.getDescription());
	}
	catch( Exception e ) {
		Message.printWarning( 2, routine, e );
	}

	if ( existing_node == null ) {
		Message.printWarning( 2,routine,"Could NOT find node: \""
			+ TSID_STRING + ": " + old_tsident.toString()
			+ " - " + old_mt.getDescription()
			+ "\" to update on JTree.  "
			+ "Will try to update database still. "
			+ "See log file for more details." );
	}

	//make new node:
	SimpleJTree_Node new_node = new SimpleJTree_Node(
		TSID_STRING + ": " + new_tsident.toString()
		+ " - " + new_mt.getDescription() );

	new_node.setData( new_mt );
	new_node.setIcon( __closedFolderIcon );

	if ( Message.isDebugOn ) {
		Message.printDebug( 3,routine,
			"Will try to add new node: \""
			+ new_node.getName() +"\"" );
	}

	try {
		replaceNode( existing_node, new_node );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, "Unable to update " +
		"Time Series node in tree, but will still update database, so " +
		"next time application is re-started, node should be " +
		"updated in tree.  See log for error details." );
		Message.printWarning( 2, routine, e );
	}

	//get child which is the Create_method
	List v = getChildrenList( new_node );
	SimpleJTree_Node child_node =null;
	if ( ( v != null ) && ( v.size() == 1 )) {
		child_node = (SimpleJTree_Node )v.get(0);
	}
	String child_name = "";
	if ( child_node != null ) {
		child_name = child_node.getName();
		if ( !child_name.equalsIgnoreCase(new_mt.getCreate_method()) ) {
			child_node.setName( new_mt.getCreate_method() );
		}
	}

	// Update JTree
	refresh();

	// Select added node
	scrollToVisibleNode( new_node );

	// Now add this new MeasLoc to the vector of
	// MeasLoc products by re-reading the database!
	// get vector of all MeasLoc objects...
	try {	__measLoc_vect = __dmi.readMeasLocList();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		// Create an empty vector
		__measLoc_vect = new Vector();
	}
}

/**
Updates the MeasType and Reduction nodes based on the MeasType object passed in.
@param old_mt RiversideDB_MeasType object that needs updating
@param new_mt RiversideDB_MeasType new MeasType object the JTree
*/
public void updateReductionNode( RiversideDB_MeasType old_mt,
				 RiversideDB_MeasType new_mt )
{
	String routine = __class + ".updateReductionNode";

	// Find this node
	SimpleJTree_Node existing_node = findNodeWithData( old_mt );

	if ( existing_node == null ) {
		Message.printWarning( 2, routine,
		"Unable to find Time Series node on JTree to " +
		"update from having create method: \"UNKNOWN\" " +
		"to: \"REDUCTION\".  Data should be updated in " +
		"database, so next time application is run, the " +
		"time series should appear correctly in the tree." );
	}

	List v = getChildrenList( existing_node );
	// Should only be 1 child node at this point.
	SimpleJTree_Node existing_child_node =
		(SimpleJTree_Node) v.get(0);
	v = null;

	// Get the TSID for new node
	String tsid_str = null;
	try {
		tsid_str = new_mt.toTSIdent().toString();
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		tsid_str = null;
	}

	//make new node
	SimpleJTree_Node new_node = new SimpleJTree_Node(
		TSID_STRING + ": " + tsid_str.toString() + " - " +
	new_mt.getDescription() );
	new_node.setData( new_mt );
	new_node.setIcon( __closedFolderIcon );

	// Update node
	try {
		replaceNode( existing_node, new_node );
	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, "Unable to update " +
		"Time Series node in tree, but will still update "+
		"database, so next time application is re-started, "+
		"node should be updated in tree.  " +
		"See log for error details." );
		Message.printWarning( 2, routine, e );
	}

	// Now update the node below it which has the create method
	existing_child_node.setName( new_mt.getCreate_method() );

	// Refresh ( not necessary )
	// refresh();

	// Select added node
	scrollToVisibleNode( new_node );
}

}
//------------------------------------------------------------------------------