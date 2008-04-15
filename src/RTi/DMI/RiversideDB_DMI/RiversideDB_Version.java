// ----------------------------------------------------------------------------
// RiversideDB_Version
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-07-11	J. Thomas Sapienza, RTi	Initial Version
// 2002-08-20	JTS, RTi		Made toString() more descriptive
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import java.util.Date;

/**
class to store data from the Version table
*/
public class RiversideDB_Version extends DMIDataObject
{

// from table Version
protected long _Version_num 	= DMIUtil.MISSING_LONG;
protected long _Table_num	= DMIUtil.MISSING_LONG;
protected String _version_type	= DMIUtil.MISSING_STRING;
protected long _version_id	= DMIUtil.MISSING_LONG;
protected Date _version_date	= DMIUtil.MISSING_DATE;
protected String _version_comment = DMIUtil.MISSING_STRING;

/**
constructor.  
*/
public RiversideDB_Version()
{	super();
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_version_type = null;
	_version_date = null;
	_version_comment = null;
	super.finalize();
}

/**
returns _Table_num

@return _Table_num
*/
public long getTable_num() {
	return _Table_num;
}

/**
returns _version_comment

@return _version_comment
*/
public String getversion_comment() {
	return _version_comment;
}

/**
returns _version_date

@return _version_date
*/
public Date getversion_date() {
	return _version_date;
}

/**
returns _version_id

@return _version_id
*/
public long getversion_id() {
	return _version_id;
}

/**
returns _version_type

@return _version_type
*/
public String getversion_type() {
	return _version_type;
}

/**
returns _Version_num

@return _Version_num
*/
public long getVersion_num() {
	return _Version_num;
}

/**
sets _Table_num

@param Table_num value to set _Table_num to
*/
public void setTable_num (long Table_num) {
	_Table_num = Table_num;
}

/**
sets _version_comment

@param version_comment value to set _version_comment to
*/
public void setversion_comment (String version_comment) {
	_version_comment = version_comment;
}

/**
sets _version_date

@param version_date value to set _version_date to
*/
public void setversion_date (Date version_date) {
	_version_date = version_date;
}

/**
sets _version_id

@param version_id value to set _version_id to
*/
public void setversion_id (long version_id) {
	_version_id = version_id;
}

/**
sets _version_type

@param version_type value to set _version_type to
*/
public void setversion_type (String version_type) {
	_version_type = version_type;
}

/**
sets _Version_num 

@param Version_num value to set _Version_num to
*/
public void setVersion_num (long Version_num) {
	_Version_num = Version_num;
}

/** 
returns a string representation of this object

@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_Version {" 		+ "\n" + 
		"Version_num:    " + _Version_num	+ "\n" +
		"Table_num:      " + _Table_num		+ "\n" +
		"Version_type:   " + _version_type	+ "\n" +
		"Version_id:     " + _version_id	+ "\n" + 
		"Version_date:   " + _version_date	+ "\n" +
		"Version_comment:" + _version_comment	+ "}";
}

} // End RiversideDB_Version
