// ----------------------------------------------------------------------------
// RiversideDB_ImportConf.java - class for doing I/O with these values
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-05-22	J. Thomas Sapienza, RTi	Initial Version
// 2002-05-23	JTS, RTi		Added support for ImportProduct_num 
//					and MeasType_num.  Added 
//					fillObjectVector method.  Added 
//					constructor, removed parentheses 
//					from returns
// 2002-05-29   JTS, RTi	    	Added __dirty variable and methods.
// 2002-06-26	Steven A. Malers, RTi	Change so class has no I/O code.
// 2002-08-20	JTS, RTi		Added IsActive (version 02.07.00)
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// 2003-05-12	JTS, RTi		Added a copy constructor.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMI;
import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

/**
class to store data from the ImportConf table
*/
public class RiversideDB_ImportConf extends DMIDataObject
{

// from table ImportConf
protected int 		_ImportProduct_num= DMIUtil.MISSING_INT;
protected long		_MeasType_num	= DMIUtil.MISSING_LONG;

protected String	_External_table	= DMIUtil.MISSING_STRING;
protected String	_External_field	= DMIUtil.MISSING_STRING;
protected String	_External_id	= DMIUtil.MISSING_STRING;
protected String	_External_units	= DMIUtil.MISSING_STRING;
protected String	_IsActive 	= DMIUtil.MISSING_STRING;

/**
Copy constructor.
@param i the RiversideDB_ImportConf object to duplicate.
*/
public RiversideDB_ImportConf(RiversideDB_ImportConf i) {
	setImportProduct_num(i.getImportProduct_num());
	setMeasType_num(i.getMeasType_num());
	setExternal_table(new String(i.getExternal_table()));
	setExternal_field(new String(i.getExternal_field()));
	setExternal_id(new String(i.getExternal_id()));
	setExternal_units(new String(i.getExternal_units()));
	setIsActive(new String(i.getIsActive()));

	setDirty(i.isDirty());
}

/**
Constructor.  
*/
public RiversideDB_ImportConf()
{	super();
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_External_field = null;
	_External_id	= null;
	_External_table	= null;
	_External_units	= null;

	super.finalize();
}

/**
returns _External_field
@return _External_field
*/
public String getExternal_field() {
	return _External_field;
}

/**
returns _External_id
@return _External_id
*/
public String getExternal_id() {
	return _External_id;
}

/**
returns _External_table
@return _External_table
*/
public String getExternal_table() {
	return _External_table;
}

/**
returns _External_units
@return _External_units
*/
public String getExternal_units() {
	return _External_units;
}

/**
returns _ImportProduct_num
@return _ImportProduct_num
*/
public int getImportProduct_num() {	
	return _ImportProduct_num;
}

/**
returns _IsActive
@return _IsActive
*/
public String getIsActive() {
	return _IsActive;
}

/**
returns _MeasType_num
@return _MeasType_num
*/
public long getMeasType_num() {
	return _MeasType_num;
}

/**
sets _External_field
@param External_field value to set _External_field to
*/
public void setExternal_field (String External_field) {
	if ( External_field != null ) {
		_External_field = External_field;
	}
}

/**
sets _External_id
@param External_id value to set _External_id to
*/
public void setExternal_id (String External_id) {
	if ( External_id != null ) {
		_External_id = External_id;
	}
}

/**
sets _External_table
@param External_table value to set _External_table to
*/
public void setExternal_table (String External_table) {
	if ( External_table != null ) {
		_External_table = External_table;
	}
}

/**
sets _External_units
@param External_units value to set _External_units to
*/
public void setExternal_units (String External_units) {
	if ( External_units != null ) {
		_External_units = External_units;
	}
}

/**
sets _ImportProduct_num
@param ImportProduct_num value to set _ImportProduct_num to
*/
public void setImportProduct_num (int ImportProduct_num) {
	_ImportProduct_num = ImportProduct_num;
}

/**
sets _IsActive
@param IsActive value to set _IsActive to
*/
public void setIsActive (String IsActive) {
	_IsActive = IsActive;
}

/**
sets _MeasType_num
@param MeasType_num value to set _MeasType_num to
*/
public void setMeasType_num (long MeasType_num) {
	_MeasType_num = MeasType_num;
}

/** 
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_ImportConf{\n" 	+ 
		"ImportProduct_num:  " + _ImportProduct_num	+ "\n" +
		"MeasType_num:       " + _MeasType_num		+ "\n" +
		"External_table:     " + _External_table	+ "\n" +
		"External_field:     " + _External_field	+ "\n" +
		"External_id:        " + _External_id		+ "\n" +
		"External_units:     " + _External_units	+ "\n" + 
		"IsActive (02.07.00):'" + _IsActive		+ "'\n}\n";
}

} // End RiversideDB_ImportConf
