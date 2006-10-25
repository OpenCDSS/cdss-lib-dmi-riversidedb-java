// ----------------------------------------------------------------------------
// RiversideDB_ProductType - class to store a record from the ProductType
//	table
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
// 2002-06-02	J. Thomas Sapienza, RTi	Initial version.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMI;
import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import RTi.Util.Message.Message;

/**
Class to store data from the Area table
*/
public class RiversideDB_ProductType extends DMIDataObject
{

protected String _ProductType = DMIUtil.MISSING_STRING;
protected String _Name = DMIUtil.MISSING_STRING;
protected String _Comment = DMIUtil.MISSING_STRING;

/**
RiversideDB_ProductType constructor.
*/
public RiversideDB_ProductType ()
{	super();
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_ProductType = null;
	_Name = null;
	_Comment = null;
	super.finalize();
}

/**
Returns _Comment
@return _Comment
*/
public String getComment() {
	return _Comment;
}

/**
Returns _Name
@return _Name
*/
public String getName() {
	return _Name;
}

/**
Returns _ProductType
@return _ProductType
*/
public String getProductType() {
	return _ProductType;
}

/**
Sets _Comment
@param Comment value to put into _Comment
*/
public void setComment(String Comment) {
	 _Comment = Comment;
}

/**
Sets _Name
@param Name value to put into _Name
*/
public void setName(String Name) {
	 _Name = Name;
}

/**
Sets _ProductType
@param ProductType value to put into _ProductType
*/
public void setProductType(String ProductType) {
	 _ProductType = ProductType;
}

/** 
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return "RiversideDB_ProductType{" 			+ "\n" + 
		"ProductType: '" + _ProductType + "'\n" + 
		"Name:        '" + _Name + "'\n" + 
		"Comment:     '" + _Comment + "'\n}\n";
}

}
