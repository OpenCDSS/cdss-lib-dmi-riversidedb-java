
//------------------------------------------------------------------------------
// RiversideDB_System_JPanel - Creates a RiversideDB JPanel component that
// contains a JTabbedPane currently populated with three JTrees
// (Import, MeasLoc and Export).
//------------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
//------------------------------------------------------------------------------
// History:
//
// 2004-11-22 Luiz Teixeira, RTi 	Original version derived from
//					RTAssistant_Main_Frame.
// 2004-12-07 Luiz Teixeira, RTi 	Added third argument to the constructor
//						RiversideDB_WindowManager
//					Method getWindowManager become useless,
//						so it was removed.
// 2004-12-?? Luiz Teixeira, RTi	Added the refreshTrees() method.
// 2004-12-10 Luiz Teixeira, RTi	Added the populateTrees() method.
// 2004-12-10 Luiz Teixeira, RTi	Added the 'boolean populate' parameter
//					to the constructor method. If this para
//					meter is true, the trees will be popula-
//					ted during construction, otherwise the
//					caller object should populate the trees
//					afterwards by running the public method
//					RiversideDB_System_JPanel.populateTrees.
// 2004-12-10 Luiz Teixeira, RTi	Removed parameter 'JFrame main' from the
//					constructor, since it was not being used
//------------------------------------------------------------------------------
package RTi.DMI.RiversideDB_DMI;

import  RTi.Util.GUI.JGUIUtil;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import RTi.Util.Message.Message;

/**
Creates a RiversideDB JPanel component that contains a JTabbedPane currently
populated with three JTrees (Import, MeasLoc and Export).
*/
public class RiversideDB_System_JPanel extends JPanel
{

/**
Class name
*/
private static String __class = "RiversideDB_System_JPanel";

/**
Reference to the RiversideDB connection
*/
private RiversideDB_DMI __dmi = null;

/**
Reference to the RiversideDB_WindowManager object.
*/
private RiversideDB_WindowManager __windowManager = null;

/**
Reference to the RiversideDB_System_JTabbedPane object, instantiated during
construction.
*/
private RiversideDB_System_JTabbedPane __rdb_systemJTabbedPane = null;

/**
Flag indicating if the trees are to be populated during construction (true) or
if they will be populated afterwards, by the caller, running the public method
populateTrees()
*/
private boolean __populate = false;

/**
Default constructor.
@param dmi Reference to the RiversideDB_DMI connection.
@param windowManager Reference to the RiversideDB_WindowManager object.
*/
public RiversideDB_System_JPanel ( RiversideDB_DMI dmi,
				   RiversideDB_WindowManager windowManager,
				   boolean populate )
{
	super();

	// Set instance members.
	__dmi           = dmi;
	__windowManager = windowManager;
	__populate      = populate;

	// Initialize the GUI
	initializeGUILayout();
}

/**
DepPopulate the JTree(s)
*/
public void depopulateTrees()
{
	__rdb_systemJTabbedPane.depopulateTrees();
//	JGUIUtil.forceRepaint( __rdb_systemJTabbedPane );
}

/**
Initialize the GUI.
*/
private void initializeGUILayout ()
{
	String routine = __class + ".initializeGUILayout";

	// JPanel to hold the JTabbedPane
	this.setLayout ( new GridBagLayout() );
	this.setBackground ( Color.white );

	// Create the make the RiversideDB_System_JTabbedPane
	__rdb_systemJTabbedPane = new RiversideDB_System_JTabbedPane(
			__dmi, __windowManager, __populate );
	__rdb_systemJTabbedPane.setBorder(
		BorderFactory.createLineBorder( Color.black ));

	// Add RiversideDB_System_JTabbedPane to RiversideDB_System_JPanel
	JGUIUtil.addComponent( this, __rdb_systemJTabbedPane,
		0, 0, 1, 1, 1, 1,
		GridBagConstraints.BOTH,
		GridBagConstraints.CENTER );
}

/**
Finalize.  Free memory for garbage collection.
@exception Throwable if there is an error.
*/
public void finalize ()
throws Throwable
{
	__dmi			= null;
	__windowManager         = null;
	__rdb_systemJTabbedPane = null;

	super.finalize();
}

/**
Refresh ( depopulate and populate ) the JTree(s)
*/
public void refreshTrees()
{
	__rdb_systemJTabbedPane.refreshTrees();

//	JGUIUtil.forceRepaint( __rdb_systemJTabbedPane );
}

/**
Populate the JTree(s)
*/
public void populateTrees()
{
	__rdb_systemJTabbedPane.populateTrees();
	
//	JGUIUtil.forceRepaint( __rdb_systemJTabbedPane );
}

}
//------------------------------------------------------------------------------
