

//------------------------------------------------------------------------------
// RiversideDB_EditorBase_JFrame - Base class for all RiversideDB_???_JFrame
//                                 classes.
//------------------------------------------------------------------------------
// Copyright:	See the COPYRIGHT file.
//------------------------------------------------------------------------------
// History:
//
// 2004_11_29 Luiz Teixeira, RTi	Initial version while upgrading to
//					version 03.00.00 (the new component
//					object RiversideDB_System_JPanel.)
// 2004-12-07 Luiz Teixeira, RTi	Moved methods
//						addVectors(),
//						findAdditions(),
//						findRemovals() and
//					  removeDuplicateStringsFromVector()
//					from RiversideDB_Util. These methods
//					are only used by few class objects
//					extended from this base class.
// 2004-12-09 Luiz Teixeira, RTi	Some cleanup and documentation.	
//------------------------------------------------------------------------------
package RTi.DMI.RiversideDB_DMI;

import RTi.Util.Message.Message;

import java.util.Vector;
import javax.swing.JFrame;

/**
Base class for all RiversideDB_???_JFrame derived object. These extended
RiversideDB_???_JFrame editor objects allow changes in the database that
would require updates of the RiversideDB_???_JTree objects. These
RiversideDB_???_JFrame editors communicate back to the RiversideDB_???_JTree
objectse via RiversideDB_System_Listener. This class implements the
RiversideDB_System_Listener methods. It also keep the reference to the
RiversideDB_WindowManager.
*/
public class RiversideDB_EditorBase_JFrame extends JFrame {

/**
Class name.
*/
private static String __class = "RiversideDB_EditorBase_JFrame";

/**
Reference to the RiversideDB_WindowManager object that manages the windows.
*/
protected RiversideDB_WindowManager _windowManager = null;
						// If the windowManager is
						// instantiated it will be used
						// to create and manage the
						// RiversideDB_???_JFrame editor
						// objects

// Vector of RiversideDB_System_Listener listeners
protected RiversideDB_System_Listener [] _listeners = null;
						// Listeners that want to know
						// when its tree should change
						// because of changes done here.

/**
Default Constructor.
@param title The text that will be used in the title bar of the window
created by the derived classes.
*/
public RiversideDB_EditorBase_JFrame( String title )
{
	super( title );
}

/**
Add a RiversideDBSystemListener to receive RiversideDB_???_JFrame events.
Multiple listeners can be registered.
@param listener The listener to add.
*/
public void addRiversideDBSystemListener (
	RiversideDB_System_Listener listener )
{
	String routine = __class + ".addRiversideDBSystemListener";

	// Use arrays to make a little simpler than Vectors to use later...
	if ( listener != null ) {
		// Resize the listener array...
		if ( _listeners == null ) {
			_listeners = new RiversideDB_System_Listener[1];
			_listeners[0] = listener;
			if ( Message.isDebugOn ) {
				Message.printDebug ( 10, routine,
					"Added RiversideDB_System_Listener" );
			}
		}
		else {	// Need to resize and transfer the list...
			int size = _listeners.length;
			RiversideDB_System_Listener [] newlisteners =
				new RiversideDB_System_Listener[size + 1];
			for ( int i = 0; i < size; i++ ) {
				newlisteners[i] = _listeners[i];
			}
			_listeners = newlisteners;
			_listeners[size] = listener;
			newlisteners = null;
		}
	}
}

/**
Finalizes and cleans up.
*/
protected void finalize() throws Throwable
{
	// Set __listener to null to aid garbage collection.
	if ( _listeners != null ) {
		_listeners = null;
	}
	
	super.finalize();
}

// REVISIT [LT] 2004-12-22 - This method needs to be implemented here to allow
// the main application to check, and inform the user if any of the opened data
// windows have unsaved data (is dirty). The same method already exists in the
// RiversideDB_RefernceTable_Abstract_JFrame class. All the higher level code is
// already in place. This method is currently simple returning false no matter
// what.
// The code needed to implement this method can be founded in the CloseGUI()
// method of each derived class. That code is marked 
// "REVISIT [LT] 2004-12-22 (isDataDirty())" in the class
// RiversideDB_Export_JFrame.java.
// If this method can not be made general (implemented only here, base class),
// make it abstract here to force its implementation in all the derived classes.
/**
Checks if the data is dirty.
@return true if any are dirty, false otherwise.
*/
public boolean isDataDirty()
{
	return true;
}


// REVISIT [LT] Should the methods inside this code block be moved to the Util
//              library? They were in the RiversideDB_Util, but SAM's suggested 
//		that they should go in more general code, maybe in the Util
//		package. 
//??????????????????????????????????????????????????????????????????????????????

// REVISIT [LT] Used by RiversideDB_Reduction_JFrame.java only
/**
Adds the contents of one Vector to the contents of another vector.
@param vect_to_add Vector to get new contents from.
@param main_vect Vector to add to.
@return Vector containing the original contents and the additions.
*/
public static Vector addVectors( Vector vect_to_add, Vector main_vect )
{
	String routine = __class + ".addVectors";

	if ( main_vect == null ) {
		Message.printWarning( 2, routine,
			"Vector to add contents to is null." );
		return new Vector();
	}
	int size = 0;
	if ( vect_to_add != null ) {
		size = vect_to_add.size();
	}
	if ( size == 0 ) {
		Message.printWarning( 5, routine,
			"No data to add to main Vector." );
	}
	for ( int i=0; i<size; i++ ) {
		main_vect.addElement( vect_to_add.elementAt(i) );
	}
	return main_vect;
}

// REVISIT [LT] Used by RiversideDB_Reduction_JFrame.java and
//		        RiversideDB_MeasLocGroup_JFrame.java
/**
Compares Strings within Vectors and returns a Vector containing containing only
the entries in the New Vector that and not in the Old Vector
@param vecOld the Old Vector
@param vecNew the New Vector
@return a Vector containing only the entries in the New Vector that and not in
the Old Vector.
*/
public static Vector findAdditions( Vector vecOld, Vector vecNew )
{
	Vector vecReturn = new Vector();
	for (int i = 0; i < vecNew.size(); i++) {
		if ( !vecOld.contains(vecNew.get(i)) ) {
			vecReturn.add(vecNew.get(i));
		}
	}
	return vecReturn;
}

// REVISIT [LT] Used by RiversideDB_Reduction_JFrame.java and
//		        RiversideDB_MeasLocGroup_JFrame.java
/**
Compares Strings within Vectors and returns a Vector containing the
Strings that exist in the vecOld and not in the vecNew.
@param vecOld the Old Vector
@param vecNew the New Vector
@return a Vector containing entries found in the Old Vector and not in New.
*/
public static Vector findRemovals( Vector vecOld, Vector vecNew )
{
	Vector vecReturn = new Vector();
	for (int i = 0; i < vecOld.size(); i++) {
		if ( !vecNew.contains(vecOld.get(i)) ) {
			vecReturn.add(vecOld.get(i));
		}
	}
	return vecReturn;
}

// REVISIT [LT] Not used at all
/**
Method used to remove any duplicate Strings from a Vector.
@param v Vector to remove duplicate Strings from.
@param return Vector containing non-duplicated Strings.
*/
public static Vector removeDuplicateStringsFromVector( Vector v )
{
	Vector no_dupes_vect = new Vector();

	int size = 0;
	if ( v != null ) {
		size = v.size();
	}
	int last_ind = 0;
	String s = null;
	for ( int i=0; i<size; i++ ) {
		s = (String) v.elementAt(i);
		last_ind = v.lastIndexOf( s );
		if ( last_ind == i ) {
			//not a repeat
			no_dupes_vect.addElement( s );
		}
	}

	return no_dupes_vect;
}
//??????????????????????????????????????????????????????????????????????????????



//============================================================================//
//              Methods that interact with the registered listeners           //
//                                                                            //
// To be consistent the names and parameters of these methods are exactly the //
//         same as the methods and parameters called on the listeners.        //
//                                                                            //
//         This class does not implement RiversideDB_System_Listeners,        //
//                        the registered listeners do.                        //
//============================================================================//

/**
Add a new export product node to the export group identified by "parent".
@param parent String identifier of the export group where the new
Export node will be added to.
@param child String identifier for the new Export node.
@param new_exportProduct Reference to the new RiversideDB_ExportProduct object.
*/
public void addExportProductNode ( String parent, String child,
				   RiversideDB_ExportProduct new_exportProduct )
{
		if ( _listeners != null ) {
		int size = _listeners.length;
		if ( size > 0 ) {
			for ( int i = 0; i < size; i++ ) {
				_listeners[i].addExportProductNode(
				 parent, child, new_exportProduct );
			}
		}
	}
}

/**
Add a new import product node to the import group identified by "parent".
@param parent String identifier of the import group where the new Import node
will be added to.
@param child String identifier for the new Import node.
@param new_importProduct Reference to the new RiversideDB_ImportProduct
object.
*/
public void addImportProductNode ( String parent, String child,
				   RiversideDB_ImportProduct new_importProduct )
{
		if ( _listeners != null ) {
		int size = _listeners.length;
		if ( size > 0 ) {
			for ( int i = 0; i < size; i++ ) {
				_listeners[i].addImportProductNode(
					parent, child, new_importProduct );
			}
		}
	}
}

/**
Add a new MeasLocGroup node.
@param new_measLocGroup Reference to the new RiversideDB_MeasLocGroup object
to be used to populate the new node.
*/
public void addMeasLocGroupNode ( RiversideDB_MeasLocGroup new_measLocGroup )
{
		if ( _listeners != null ) {
		int size = _listeners.length;
		if ( size > 0 ) {
			for ( int i = 0; i < size; i++ ) {
				_listeners[i].addMeasLocGroupNode(
					new_measLocGroup );
			}
		}
	}
}

/**
Add a new MeasLoc node.
@param measLocGroup String identifier for the new MeasLoc node.
@param new_measLoc Reference to the new RiversideDB_MeasLoc object
to be used to populate the new node.
*/
public void addMeasLocNode (String measLocGroup,RiversideDB_MeasLoc new_measLoc)
{
	if ( _listeners != null ) {
		int size = _listeners.length;
		if ( size > 0 ) {
			for ( int i = 0; i < size; i++ ) {
				_listeners[i].addMeasLocNode(
					measLocGroup, new_measLoc );
			}
		}
	}
}

/**
Add a new MeasTypeNode node.
@param new_measType Reference to the new RiversideDB_MeasType object
to be used to populate the new node.
*/
public void addMeasTypeNode ( RiversideDB_MeasType new_measType )
{
		if ( _listeners != null ) {
		int size = _listeners.length;
		if ( size > 0 ) {
			for ( int i = 0; i < size; i++ ) {
				_listeners[i].addMeasTypeNode(
					new_measType );
			}
		}
	}
}

/**
Add a new Product Group node to the RiversideDB_Import_JTree or to the
RiversideDB_Export_JTree.
@param new_ProductGroup Reference to the new RiversideDB_ProductGroup to add.
*/
public void addProductGroupNode ( RiversideDB_ProductGroup new_ProductGroup )
{
		if ( _listeners != null ) {
		int size = _listeners.length;
		if ( size > 0 ) {
			for ( int i = 0; i < size; i++ ) {
				_listeners[i].addProductGroupNode(
					new_ProductGroup );
			}
		}
	}
}

// REVISIT [LT] 2004-12-08 - The implementation of this method in the original
//	RTAssistant_Main_JFrame.java (prior to 03.00.00) was commented out while
//	the implementation of the updatedReductionNode method was not commented
//	out and was used. Need to figure out what should be the logic and fix if
//	needed.
//      See also RiversideDB_System_Listener and RiversideDB_MeasLoc_JTree.
/**
Updates the node under a Time Series node to REDUCTION. This
is called only if the Time Series had not had a node previously
signifying that it had a create_method of UNKNOWN.
@param old_mt RiversideDB_MeasType object that needs updating
@param new_mt RiversideDB_MeasType new MeasType object the JTree
*/
public void addUpdatedReductionNode( RiversideDB_MeasType old_mt,
				     RiversideDB_MeasType new_mt )
{
	if ( _listeners != null ) {
		int size = _listeners.length;
		if ( size > 0 ) {
			for ( int i = 0; i < size; i++ ) {
				_listeners[i].addUpdatedReductionNode(
					old_mt, new_mt );
			}
		}
	}
}


/**
Update the Export Product node identified by the String "existing_name"
@param new_exportProduct Reference to the new RiversideDB_ExportProduct
object to be used to update the node.
@param existing_name String identifier of the Export node to update.
*/
public void updateExportProductNode(RiversideDB_ExportProduct new_exportProduct,
				    String existing_name )
{
		if ( _listeners != null ) {
		int size = _listeners.length;
		if ( size > 0 ) {
			for ( int i = 0; i < size; i++ ) {
				_listeners[i].updateExportProductNode(
					new_exportProduct, existing_name );
			}
		}
	}
}

/**
Update the Import Product node identified by the "existing_name"
@param new_importProduct Reference to the new RiversideDB_ImportProduct
object to be used to update the node.
@param existing_name String identifier of the Import node to update.
*/
public void updateImportProductNode(RiversideDB_ImportProduct new_importProduct,
				    String existing_name )
{
		if ( _listeners != null ) {
		int size = _listeners.length;
		if ( size > 0 ) {
			for ( int i = 0; i < size; i++ ) {
				_listeners[i].updateImportProductNode(
					new_importProduct, existing_name );
			}
		}
	}
}

/**
Update the MeasLocGroup node
@param new_measLoc Reference to the new RiversideDB_MeasLocGroup object
to be used to update the node.
@param old_measLoc Reference to the old RiversideDB_MeasLocGroup object
used to populate the node.
*/
public void updateMeasLocGroupNode ( RiversideDB_MeasLocGroup new_measLocGroup,
				     RiversideDB_MeasLocGroup old_measLocGroup )
{
		if ( _listeners != null ) {
		int size = _listeners.length;
		if ( size > 0 ) {
			for ( int i = 0; i < size; i++ ) {
				_listeners[i].updateMeasLocGroupNode(
					new_measLocGroup, old_measLocGroup );
			}
		}
	}
}

/**
Update the MeasLoc node
@param new_measLoc Reference to the new RiversideDB_MeasLoc object
to be used to update the node.
@param old_measLoc Reference to the old RiversideDB_MeasLoc object
used to populate the node.
*/
public void updateMeasLocNode ( RiversideDB_MeasLoc new_measLoc,
				RiversideDB_MeasLoc old_measLoc )
{
	if ( _listeners != null ) {
		int size = _listeners.length;
		if ( size > 0 ) {
			for ( int i = 0; i < size; i++ ) {
				_listeners[i].updateMeasLocNode(
					new_measLoc, old_measLoc );
			}
		}
	}
}

/**
Update the MeasType node
@param new_measLoc Reference to the new RiversideDB_MeasType object
to be used to update the node.
@param old_measLoc Reference to the old RiversideDB_MeasType object
used to populate the node.
*/
public void updateMeasTypeNode	( RiversideDB_MeasType new_measType,
				  RiversideDB_MeasType old_measType )
{
		if ( _listeners != null ) {
		int size = _listeners.length;
		if ( size > 0 ) {
			for ( int i = 0; i < size; i++ ) {
				_listeners[i].updateMeasTypeNode(
					new_measType, old_measType );
			}
		}
	}
}

/**
Updates a Product Group node in the RiversideDB_Import_JTree or in the
RiversideDB_Export_JTree.
@param new_ProductGroup Reference to the new RiversideDB_ProductGroup object
to be used to update the node.
@param existing_name String identifier of the Product Group node to update.
*/
public void updateProductGroupNode ( RiversideDB_ProductGroup new_ProductGroup,
				     String existing_name )
{
		if ( _listeners != null ) {
		int size = _listeners.length;
		if ( size > 0 ) {
			for ( int i = 0; i < size; i++ ) {
				_listeners[i].updateProductGroupNode(
					new_ProductGroup, existing_name );
			}
		}
	}
}

/**
Update the updateReductionNode node
@param new_measLoc Reference to the new RiversideDB_MeasType object
to be used to update the node.
@param old_measLoc Reference to the old RiversideDB_MeasType object
used to populate the node.
*/
public void updateReductionNode ( RiversideDB_MeasType old_measType,
				  RiversideDB_MeasType new_measType )
{
		if ( _listeners != null ) {
		int size = _listeners.length;
		if ( size > 0 ) {
			for ( int i = 0; i < size; i++ ) {
				_listeners[i].updateReductionNode(
					old_measType, new_measType );
			}
		}
	}
}

}
//------------------------------------------------------------------------------
