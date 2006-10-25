// ----------------------------------------------------------------------------
// RiversideDB_DBGroup
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
class to store data from the DBGroup_num table
*/
public class RiversideDB_DBGroup extends DMIDataObject
{

protected int _DBGroup_num = DMIUtil.MISSING_INT;
protected String _ID = DMIUtil.MISSING_STRING;
protected String _Description = DMIUtil.MISSING_STRING;

/**
constructor.  
*/
public RiversideDB_DBGroup() {
	super();
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Description = null;
	_ID = null;
	super.finalize();
}

/**
Returns _DBGroup_num
@return _DBGroup_num
*/
public int getDBGroup_num() {
	return _DBGroup_num;
}

/**
Returns _Description
@return _Description
*/
public String getDescription() {
	return _Description;
}

/**
Returns _ID
@return _ID
*/
public String getID() {
	return _ID;
}

/**
Sets _DBGroup_num
@param DBGroup_num value to put into _DBGroup_num
*/
public void setDBGroup_num(int DBGroup_num) {
	_DBGroup_num = DBGroup_num;
}

/**
Sets _Description
@param Description value to put into _Description
*/
public void setDescription(String Description) {
	_Description = Description;
}

/**
Sets _ID
@param ID value to put into _ID
*/
public void setID(String ID) {
	_ID = ID;
}

public String toString() {
	return "RiversideDB_DBGroup {\n" + 
		"DBGroup_num: " + _DBGroup_num + "\n" + 
		"ID:          '" + _ID + "'\n" +
		"Description: '" + _Description + "'\n}\n";
}

} // End RiversideDB_DBGroup_num
