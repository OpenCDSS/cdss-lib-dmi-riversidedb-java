// -----------------------------------------------------------------------------
// RiversideDB_MeasLocGroup.java - corresponds to RiversideDB MeasLocGroup table
// -----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// -----------------------------------------------------------------------------
// History:
//
// 2003-06-11	J. Thomas Sapienza, RTi	Initial version.
// 2003-06-19	JTS, RTi		Added DBUser_num, DBGroup_num and
//					DBPermissions field.
// 2005-01-06 Luiz Teixeira, RTi 	Verified for Optable field. Some cleanup
// -----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMI;
import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import RTi.Util.Message.Message;

/**
Class to store data from the MeasLocGroup table
*/
public class RiversideDB_MeasLocGroup extends DMIDataObject
{
protected int    _MeasLocGroup_num = DMIUtil.MISSING_INT;    /* AutoNum */
protected String _Identifier       = DMIUtil.MISSING_STRING;
protected String _Name             = DMIUtil.MISSING_STRING;
protected String _Description      = DMIUtil.MISSING_STRING;
protected String _Optable          = DMIUtil.MISSING_STRING;

protected int    _DBUser_num       = DMIUtil.MISSING_INT;
protected int    _DBGroup_num      = DMIUtil.MISSING_INT;
protected String _DBPermissions    = DMIUtil.MISSING_STRING;

/**
RiversideDB_MeasLocGroup constructor.
*/
public RiversideDB_MeasLocGroup ()
{	super();
}

/**
Copy constructor.
*/
public RiversideDB_MeasLocGroup(RiversideDB_MeasLocGroup m) {
	super();
	setMeasLocGroup_num(           m.getMeasLocGroup_num() );
	setIdentifier      (new String(m.getIdentifier      ()));
	setName            (new String(m.getName            ()));
	setDescription     (new String(m.getDescription     ()));
	setOptable         (new String(m.getOptable         ()));

	setDBUser_num      (           m.getDBUser_num      () );
	setDBGroup_num     (           m.getDBGroup_num     () );
	setDBPermissions   (new String(m.getDBPermissions   ()));
	
	setDirty(m.isDirty());
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable
{
	_Identifier    = null;
	_Name          = null;
	_Description   = null;
	_Optable       = null;

	_DBPermissions = null;
	
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
returns _Description
@return _Description
*/
public String getDescription() {
	return _Description;
}

/**
returns _Identifier
@return _Identifier
*/
public String getIdentifier() {
	return _Identifier;
}

/**
Returns _MeasLocGroup_num
@return _MeasLocGroup_num
*/
public int getMeasLocGroup_num() {
	return _MeasLocGroup_num;
}

/**
returns _Name
@return _Name
*/
public String getName() {
	return _Name;
}

/**
Returns _Optable
@return _Optable
*/
public String getOptable() {
	return _Optable;
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
sets _Description
@param Description value to put in _Description
*/
public void setDescription(String Description) {
	_Description = Description;
}

/**
sets _Identifier
@param Identifier value to put in _Identifier
*/
public void setIdentifier(String Identifier) {
	_Identifier = Identifier;
}

/**
Sets _MeasLocGroup_num
@param MeasLocGroup_num value to put into _MeasLocGroup_num
*/
public void setMeasLocGroup_num(int MeasLocGroup_num) {
	_MeasLocGroup_num = MeasLocGroup_num;
}

/**
sets _Name
@param Name value to put in _Name
*/
public void setName(String Name) {
	_Name = Name;
}

/**
Sets _Optable
@param Optable value to put into _Optable
*/
public void setOptable(String Optable) {
	_Optable = Optable;
}

/** 
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_MeasLocGroup{" 		+ "\n" +
		"MeasLocGroup_num: "  + _MeasLocGroup_num	+ " \n" +
		"Identifier:       '" + _Identifier		+ "'\n" +
		"Name:             '" + _Name			+ "'\n" +
		"Description:      '" + _Description		+ "'\n" +
		"Optable:          '" + _Optable 		+ "'\n" +
		"DBUser_num:        " + _DBUser_num 		+ " \n" +
		"DBGroup_num:       " + _DBGroup_num		+ " \n" +
		"DBPermissions:    '" + _DBPermissions 		+ "'\n}\n";
}

}
