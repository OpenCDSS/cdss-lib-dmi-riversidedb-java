// ----------------------------------------------------------------------------
// RiversideDB_AutoUpdateProduct - class to store information from the TSProduct
//	table
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
// 2002-06-02	J. Thomas Sapienza, RTi	Initial version.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

/**
Class to store data from the Area table
*/
public class RiversideDB_AutoUpdateProduct extends DMIDataObject
{

protected int _AutoUpdateProduct_num = DMIUtil.MISSING_INT;
protected int _TSProduct_num = DMIUtil.MISSING_INT;
protected int _ProductGroup_num = DMIUtil.MISSING_INT;
protected String _Identifier = DMIUtil.MISSING_STRING;
protected String _Name = DMIUtil.MISSING_STRING;
protected String _Comment = DMIUtil.MISSING_STRING;
protected String _IsEnabled = DMIUtil.MISSING_STRING;
protected int _X = DMIUtil.MISSING_INT;
protected int _Y = DMIUtil.MISSING_INT;
protected int _Width = DMIUtil.MISSING_INT;
protected int _Height = DMIUtil.MISSING_INT;
protected String _Properties = DMIUtil.MISSING_STRING;
protected int _DBUser_num = DMIUtil.MISSING_INT;
protected int _DBGroup_num = DMIUtil.MISSING_INT;
protected String _DBPermissions = DMIUtil.MISSING_STRING;

/**
Returns _AutoUpdateProduct_num
@return _AutoUpdateProduct_num
*/
public int getAutoUpdateProduct_num() {
	return _AutoUpdateProduct_num;
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
Returns _Height
@return _Height
*/
public int getHeight() {
	return _Height;
}

/**
Returns _Identifier
@return _Identifier
*/
public String getIdentifier() {
	return _Identifier;
}

/**
Returns _IsEnabled
@return _IsEnabled
*/
public String getIsEnabled() {
	return _IsEnabled;
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
Returns _Properties
@return _Properties
*/
public String getProperties() {
	return _Properties;
}

/**
Returns _TSProduct_num
@return _TSProduct_num
*/
public int getTSProduct_num() {
	return _TSProduct_num;
}

/**
Returns _Width
@return _Width
*/
public int getWidth() {
	return _Width;
}

/**
Returns _X
@return _X
*/
public int getX() {
	return _X;
}

/**
Returns _Y
@return _Y
*/
public int getY() {
	return _Y;
}

/**
Sets _AutoUpdateProduct_num
@param AutoUpdateProduct_num value to put into _AutoUpdateProduct_num
*/
public void setAutoUpdateProduct_num(int AutoUpdateProduct_num) {
	 _AutoUpdateProduct_num = AutoUpdateProduct_num;
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
Sets _Height
@param Height value to put into _Height
*/
public void setHeight(int Height) {
	 _Height = Height;
}

/**
Sets _Identifier
@param Identifier value to put into _Identifier
*/
public void setIdentifier(String Identifier) {
	 _Identifier = Identifier;
}

/**
Sets _IsEnabled
@param IsEnabled value to put into _IsEnabled
*/
public void setIsEnabled(String IsEnabled) {
	 _IsEnabled = IsEnabled;
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
Sets _Properties
@param Properties value to put into _Properties
*/
public void setProperties(String Properties) {
	 _Properties = Properties;
}

/**
Sets _TSProduct_num
@param TSProduct_num value to put into _TSProduct_num
*/
public void setTSProduct_num(int TSProduct_num) {
	 _TSProduct_num = TSProduct_num;
}

/**
Sets _Width
@param Width value to put into _Width
*/
public void setWidth(int Width) {
	 _Width = Width;
}

/**
Sets _X
@param X value to put into _X
*/
public void setX(int X) {
	 _X = X;
}

/**
Sets _Y
@param Y value to put into _Y
*/
public void setY(int Y) {
	 _Y = Y;
}

/**
RiversideDB_AutoUpdateProduct constructor.
*/
public RiversideDB_AutoUpdateProduct ()
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
	_IsEnabled = null;
	_Properties = null;
	_DBPermissions = null;
	super.finalize();
}

/** 
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return "RiversideDB_AutoUpdateProduct{" 		+ "\n" + 
		"AutoUpdateProduct_num: " + _AutoUpdateProduct_num + "\n" +
		"TSProduct_num:         " + _TSProduct_num + "\n" + 
		"ProductGroup_num:      " + _ProductGroup_num + "\n" + 
		"Identifier:            '" + _Identifier + "'\n" + 
		"Name:                  '" + _Name + "'\n" + 
		"Comment:               '" + _Comment + "'\n" + 
		"IsEnabled:             '" + _IsEnabled + "'\n" +
		"X:                     " + _X + "\n" + 
		"Y:                     " + _Y + "\n" +
		"Width:                 " + _Width + "\n" + 
		"Height:                " + _Height + "\n" + 
		"Properties:            '" + _Properties + "'\n" + 
		"DBUser_num:                " + _DBUser_num + "\n" +
		"DBGroup_num:               " + _DBGroup_num + "\n" + 
		"DBPermissions:         '" + _DBPermissions + "'\n}\n";
}

}
