// ----------------------------------------------------------------------------
// RiversideDB_MeasLoc.java - corresponds to RiversideDB MeasLoc table
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-06-13	Steven A. Malers, RTi	Initial version.  Copy and modify
//					RiversideDB_MeasType.
// 2002-06-26	SAM, RTi		Update to only include data (no
//					database I/O).
// 2002-07-11	J. Thomas Sapienza, RTi	Changed "Measloc_num" to "MeasLoc_num"
// 2002-08-20	JTS, RTi		Made toString() more descriptive
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// 2003-05-08	JTS, RTi		Added a copy constructor.
// 2003-05-09	JTS, RTi		Copy constructor sets the dirty flag.
// 2003-06-05	JTS, RTi		Added DBUser_num, DBGroup_num and
//					DBPermissions fields.
// 2003-06-11	JTS, RTi		Added MeasLocGroup_num
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

/**
Class to store data from the MeasLoc table
*/
public class RiversideDB_MeasLoc extends DMIDataObject
{

// From table MeasLoc

protected long _MeasLoc_num = DMIUtil.MISSING_LONG;
protected long _Geoloc_num = DMIUtil.MISSING_LONG;
protected String _Identifier = DMIUtil.MISSING_STRING;
protected String _MeasLoc_name = DMIUtil.MISSING_STRING;
protected String _Source_abbrev = DMIUtil.MISSING_STRING;
protected String _Meas_loc_type = DMIUtil.MISSING_STRING;
protected String _Comment = DMIUtil.MISSING_STRING;
protected int _DBUser_num = DMIUtil.MISSING_INT;
protected int _DBGroup_num = DMIUtil.MISSING_INT;
protected String _DBPermissions = DMIUtil.MISSING_STRING;
protected int _MeasLocGroup_num = DMIUtil.MISSING_INT;

/**
RiversideDB_MeasLoc constructor.
*/
public RiversideDB_MeasLoc ()
{	super();
}

/**
Copy constructor.
*/
public RiversideDB_MeasLoc(RiversideDB_MeasLoc m) {
	super();
	setMeasLoc_num(m.getMeasLoc_num());
	setGeoloc_num(m.getGeoloc_num());
	setIdentifier(new String(m.getIdentifier()));
	setMeasLoc_name(new String(m.getMeasLoc_name()));
	setSource_abbrev(new String(m.getSource_abbrev()));
	setMeas_loc_type(new String(m.getMeas_loc_type()));
	setComment(new String(m.getComment()));
	setDBUser_num(m.getDBUser_num());
	setDBGroup_num(m.getDBGroup_num());
	setDBPermissions(new String(m.getDBPermissions()));
	setMeasLocGroup_num(m.getMeasLocGroup_num());
	
	setDirty(m.isDirty());
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Identifier = null;
	_MeasLoc_name = null;
	_Source_abbrev = null;
	_Meas_loc_type = null;
	_Comment = null;
	_DBPermissions = null;
	
	super.finalize();
}

/**
returns _Comment
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
returns _Geoloc_num
@return _Geoloc_num
*/
public long getGeoloc_num() {
	return _Geoloc_num;
}

/**
returns _Identifier
@return _Identifier
*/
public String getIdentifier() {
	return _Identifier;
}

/**
returns _MeasLoc_name
@return _MeasLoc_name
*/
public String getMeasLoc_name() {
	return _MeasLoc_name;
}

/**
returns _MeasLoc_num
@return _MeasLoc_num
*/
public long getMeasLoc_num() {
	return _MeasLoc_num;
}

/**
Returns _MeasLocGroup_num
@return _MeasLocGroup_num
*/
public int getMeasLocGroup_num() {
	return _MeasLocGroup_num;
}

/**
returns _Meas_loc_type
@return _Meas_loc_type
*/
public String getMeas_loc_type() {
	return _Meas_loc_type;
}

/**
returns _Source_abbrev
@return _Source_abbrev
*/
public String getSource_abbrev() {
	return _Source_abbrev;
}

/**
sets _Comment
@param Comment value to put in _Comment
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
sets _Geoloc_num
@param Geoloc_num value to put in _MeasLoc_num
*/
public void setGeoloc_num(long Geoloc_num) {
	_Geoloc_num = Geoloc_num;
}

/**
sets _Identifier
@param Identifier value to put in _Identifier
*/
public void setIdentifier(String Identifier) {
	_Identifier = Identifier;
}

/**
sets _MeasLoc_name
@param MeasLoc_name value to put in _MeasLoc_name
*/
public void setMeasLoc_name(String MeasLoc_name) {
	_MeasLoc_name = MeasLoc_name;
}

/**
sets _MeasLoc_num
@param MeasLoc_num value to put in _MeasLoc_num
*/
public void setMeasLoc_num(long MeasLoc_num) {
	_MeasLoc_num = MeasLoc_num;
}

/**
Sets _MeasLocGroup_num
@param MeasLocGroup_num value to put into _MeasLocGroup_num
*/
public void setMeasLocGroup_num(int MeasLocGroup_num) {
	_MeasLocGroup_num = MeasLocGroup_num;
}

/**
sets _Meas_loc_type
@param Meas_loc_type value to put in _Meas_loc_type
*/
public void setMeas_loc_type(String Meas_loc_type) {
	_Meas_loc_type = Meas_loc_type;
}

/**
sets _Source_abbrev
@param Source_abbrev value to put in _Source_abbrev
*/
public void setSource_abbrev(String Source_abbrev) {
	_Source_abbrev = Source_abbrev;
}

/** 
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_MeasLoc{" 			+ "\n" +
		"MeasLoc_num:      " + _MeasLoc_num	+ "\n" +
		"Geoloc_num:       " + _Geoloc_num	+ "\n" +
		"Identifier:       '" + _Identifier	+ "'\n" +
		"MeasLoc_name:     '" + _MeasLoc_name	+ "'\n" +
		"Source_abbreb:    '" + _Source_abbrev	+ "'\n" + 
		"Meas_loc_type:    '" + _Meas_loc_type	+ "'\n" + 
		"Comment:          '" + _Comment	+ "'\n" +
		"DBUser_num:       " + _DBUser_num	+ "\n" + 
		"DBGroup_num:      " + _DBGroup_num	+ "\n" +
		"DBPermissions:    '" + _DBPermissions	+ "'\n" +
		"MeasLocGroup_num: " + _MeasLocGroup_num+ "\n}\n";
}

}
