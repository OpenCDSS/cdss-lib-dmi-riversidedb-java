// ----------------------------------------------------------------------------
// RiversideDB_DBUserDBGroupRelation
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2003-06-02	J. Thomas Sapienza, RTi	Initial Version
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMI;
import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import java.util.Date;

/**
class to store the relationships between dbusers and db groups
*/
public class RiversideDB_DBUserDBGroupRelation extends DMIDataObject
{

protected int _DBUser_num = DMIUtil.MISSING_INT;
protected int _DBGroup_num = DMIUtil.MISSING_INT;

/**
constructor.  
*/
public RiversideDB_DBUserDBGroupRelation() {
	super();
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	super.finalize();
}

/**
Returns _DBUser_num
@return _DBUser_num
*/
public int getDBUser_num() {
	return _DBUser_num;
}

/**
Returns _DBGroup_num
@return _DBGroup_num
*/
public int getDBGroup_num() {
	return _DBGroup_num;
}

/**
Sets _DBUser_num
@param DBUser_num value to put into _DBUser_num
*/
public void setDBUser_num(int DBUser_num) {
	_DBUser_num = DBUser_num;
}

/**
Sets _DBGroup_num
@param DBGroup_num value to put into _DBGroup_num
*/
public void setDBGroup_num(int DBGroup_num) {
	_DBGroup_num = DBGroup_num;
}

public String toString() {
	return "RiversideDB_DBUserDBGroupRelation {\n" + 
		"DBUser_num:  " + _DBUser_num + "\n" + 
		"DBGroup_num: " + _DBGroup_num + "\n}\n";
}

}
