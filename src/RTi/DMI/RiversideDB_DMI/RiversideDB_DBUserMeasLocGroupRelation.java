// ----------------------------------------------------------------------------
// RiversideDB_DBUserMeasLocGroupRelation
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2003-06-11	J. Thomas Sapienza, RTi	Initial Version
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMI;
import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import java.util.Date;

/**
class to store the relationships between dbusers and meas loc groups
*/
public class RiversideDB_DBUserMeasLocGroupRelation extends DMIDataObject
{

protected int _DBUser_num = DMIUtil.MISSING_INT;
protected int _MeasLocGroup_num = DMIUtil.MISSING_INT;

/**
constructor.  
*/
public RiversideDB_DBUserMeasLocGroupRelation() {
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
Returns _MeasLocGroup_num
@return _MeasLocGroup_num
*/
public int getMeasLocGroup_num() {
	return _MeasLocGroup_num;
}

/**
Sets _DBUser_num
@param DBUser_num value to put into _DBUser_num
*/
public void setDBUser_num(int DBUser_num) {
	_DBUser_num = DBUser_num;
}

/**
Sets _MeasLocGroup_num
@param MeasLocGroup_num value to put into _MeasLocGroup_num
*/
public void setMeasLocGroup_num(int MeasLocGroup_num) {
	_MeasLocGroup_num = MeasLocGroup_num;
}

public String toString() {
	return "RiversideDB_DBUserMeasLocGroupRelation {\n" + 
		"MeasLocGroup_num:         " + _MeasLocGroup_num + "\n" + 
		"DBUser_num:         " + _DBUser_num + "\n}\n";
}

} // End RiversideDB_DBUserMeasLocGroupRelation
