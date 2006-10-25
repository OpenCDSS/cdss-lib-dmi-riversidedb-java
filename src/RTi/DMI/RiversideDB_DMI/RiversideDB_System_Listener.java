
// -----------------------------------------------------------------------------
// RiversideDB_System_Listener - Listener used by the RiversideDB_..._JFrame
// objects to communicate back to the RiversideDB_..._JTree objects.
// -----------------------------------------------------------------------------
// History:
//
// 2004-11-26 Luiz Teixeira 		Original version.
// -----------------------------------------------------------------------------
package RTi.DMI.RiversideDB_DMI;

/**
Listener used by the RiversideDB_..._JFrame objects to communicate back to the
RiversideDB_..._JTree objects.
*/
public interface RiversideDB_System_Listener
{
	
/**
Add a new export product node to the export group identified by "parent".
@param parent String identifier of the export group where the new Export node
will be added to.
@param child String identifier for the new Export node.
@param new_exportProduct Reference to the new RiversideDB_ExportProduct object.
*/
public void addExportProductNode (
	String parent,
	String child,
	RiversideDB_ExportProduct new_exportProduct );	
	
/**
Add a new import product node to the import group identified by "parent".
@param parent String identifier of the import group where the new Import node
will be added to.
@param child String identifier for the new Import node.
@param new_importProduct Reference to the new RiversideDB_ImportProduct object.
*/
public void addImportProductNode (
	String parent,
	String child,
	RiversideDB_ImportProduct new_importProduct );

/**
Add a new MeasLocGroup node.
@param new_measLocGroup Reference to the new RiversideDB_MeasLocGroup object
to be used to populate the new node.
*/
public void addMeasLocGroupNode (
	RiversideDB_MeasLocGroup new_measLocGroup );
			
/**
Add a new MeasLoc node.
@param measLocGroup String identifier for the new MeasLoc node.
@param new_measLoc Reference to the new RiversideDB_MeasLoc object to be used
to populate the new node. 
*/
public void addMeasLocNode (
	String measLocGroup,
 	RiversideDB_MeasLoc new_measLoc );

/**
Add a new MeasTypeNode node.
@param new_measType Reference to the new RiversideDB_MeasType object
to be used to populate the new node.
*/
public void addMeasTypeNode (
	RiversideDB_MeasType new_measType );
	
/**
Add a new Product Group node to the RiversideDB_Import_JTree or to the
RiversideDB_Export_JTree.
@param new_ProductGroup Reference to the new RiversideDB_ProductGroup to add.
*/
public void addProductGroupNode (
 	RiversideDB_ProductGroup new_ProductGroup );

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
@param new_mt RiversideDB_MeasType new MeasType object the JTree
*/
public void addUpdatedReductionNode( 
	RiversideDB_MeasType old_mt,
	RiversideDB_MeasType new_mt ); 	
 	
 		 	
/**
Update the Export Product node identified by the String "existing_name"
@param new_exportProduct Reference to the new RiversideDB_ExportProduct object
to be used to update the node.
@param existing_name String identifier of the Export node to update.
*/
public void updateExportProductNode (
	RiversideDB_ExportProduct new_exportProduct,
	String existing_name );	
/**
Update the Import Product node identified by the "existing_name"
@param new_importProduct Reference to the new RiversideDB_ImportProduct
object to be used to update the node.
@param existing_name String identifier of the Import node to update.
*/
public void updateImportProductNode (
	RiversideDB_ImportProduct new_importProduct,
	String existing_name );	

/**
Update the MeasLocGroup node
@param new_measLoc Reference to the new RiversideDB_MeasLocGroup object
to be used to update the node.
@param old_measLoc Reference to the old RiversideDB_MeasLocGroup object
used to populate the node.
*/
public void updateMeasLocGroupNode (
	RiversideDB_MeasLocGroup new_measLocGroup,
	RiversideDB_MeasLocGroup old_measLocGroup );		

/**
Update the MeasLoc node
@param new_measLoc Reference to the new RiversideDB_MeasLoc object to be used
to update the node.
@param old_measLoc Reference to the old RiversideDB_MeasLoc object used to
populate the node.
*/
public void updateMeasLocNode (
	RiversideDB_MeasLoc new_measLoc,
	RiversideDB_MeasLoc old_measLoc );

/**
Update the MeasType node
@param new_measLoc Reference to the new RiversideDB_MeasType object to be used
to update the node.
@param old_measLoc Reference to the old RiversideDB_MeasType object used to
populate the node.
*/
public void updateMeasTypeNode	(
	RiversideDB_MeasType new_measType,
	RiversideDB_MeasType old_measType );

/**
Updates a Product Group node in the RiversideDB_Import_JTree or in the
RiversideDB_Export_JTree.
@param new_ProductGroup Reference to the new RiversideDB_ProductGroup object
to be used to update the node.
@param existing_name String identifier of the Product Group node to update.
*/
public void updateProductGroupNode (
	RiversideDB_ProductGroup new_ProductGroup,
	String existing_name );
	
/**
Update the updateReductionNode node
@param new_measLoc Reference to the new RiversideDB_MeasType object to be used
to update the node.
@param old_measLoc Reference to the old RiversideDB_MeasType object used to
populate the node.
*/
public void updateReductionNode (
	RiversideDB_MeasType old_measType,
	RiversideDB_MeasType new_measType );

}
// ---------------------------------------------------------------------------//




						 