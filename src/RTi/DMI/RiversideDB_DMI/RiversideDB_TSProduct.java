// ----------------------------------------------------------------------------
// RiversideDB_TSProduct - class to store information from the TSProduct
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
public class RiversideDB_TSProduct extends DMIDataObject
{

protected int _TSProduct_num = DMIUtil.MISSING_INT;
protected int _ProductGroup_num = DMIUtil.MISSING_INT;
protected String _Identifier = DMIUtil.MISSING_STRING;
protected String _Name = DMIUtil.MISSING_STRING;
protected String _Comment = DMIUtil.MISSING_STRING;
protected int _DBUser_num = DMIUtil.MISSING_INT;
protected int _DBGroup_num = DMIUtil.MISSING_INT;
protected String _DBPermissions = DMIUtil.MISSING_STRING;

/**
RiversideDB_TSProduct constructor.
*/
public RiversideDB_TSProduct ()
{	super();
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Identifier = null;
	_Name = null;
	_Comment = null;
	_DBPermissions = null;
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
Returns _DBGroup_num
@return _DBGroup_num
*/
public int getDBGroup_num() {
	return _DBGroup_num;
}

/**
Returns _DBPermissions
@return _DBPermissions
*/
public String getDBPermissions() {
	return _DBPermissions;
}

/**
Returns _DBUser_num
@return _DBUser_num
*/
public int getDBUser_num() {
	return _DBUser_num;
}

/**
Returns _Identifier
@return _Identifier
*/
public String getIdentifier() {
	return _Identifier;
}

/**
Returns _Name
@return _Name
*/
public String getName() {
	return _Name;
}

/**
Returns _ProductGroup_num
@return _ProductGroup_num
*/
public int getProductGroup_num() {
	return _ProductGroup_num;
}

/**
Returns _TSProduct_num
@return _TSProduct_num
*/
public int getTSProduct_num() {
	return _TSProduct_num;
}

/**
Sets _Comment
@param Comment value to put into _Comment
*/
public void setComment(String Comment) {
	 _Comment = Comment;
}

/**
Sets _DBGroup_num
@param DBGroup_num value to put into _DBGroup_num
*/
public void setDBGroup_num(int DBGroup_num) {
	 _DBGroup_num = DBGroup_num;
}

/**
Sets _DBPermissions
@param DBPermissions value to put into _DBPermissions
*/
public void setDBPermissions(String DBPermissions) {
	 _DBPermissions = DBPermissions;
}

/**
Sets _DBUser_num
@param DBUser_num value to put into _DBUser_num
*/
public void setDBUser_num(int DBUser_num) {
	 _DBUser_num = DBUser_num;
}

/**
Sets _Identifier
@param Identifier value to put into _Identifier
*/
public void setIdentifier(String Identifier) {
	 _Identifier = Identifier;
}

/**
Sets _Name
@param Name value to put into _Name
*/
public void setName(String Name) {
	 _Name = Name;
}

/**
Sets _ProductGroup_num
@param ProductGroup_num value to put into _ProductGroup_num
*/
public void setProductGroup_num(int ProductGroup_num) {
	 _ProductGroup_num = ProductGroup_num;
}

/**
Sets _TSProduct_num
@param TSProduct_num value to put into _TSProduct_num
*/
public void setTSProduct_num(int TSProduct_num) {
	 _TSProduct_num = TSProduct_num;
}

/** 
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return "RiversideDB_TSProduct{" 			+ "\n" + 
		"TSProduct_num:    " + _TSProduct_num + "\n" +
		"ProductGroup_num: " + _ProductGroup_num + "\n" +
		"Identifier:       '" + _Identifier + "'\n" + 
		"Name:             '" + _Name + "'\n" + 
		"Comment:          '" + _Comment + "'\n" + 
		"DBUser_num:       " + _DBUser_num + "\n" +
		"DBGroup_num:      " + _DBGroup_num + "\n" +
		"DBPermissions:    " + _DBPermissions + "\n}\n";
}

}
