// ----------------------------------------------------------------------------
// RiversideDB_Station
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-06-27	Steven A. Malers, RTi	Initial version.
// 2002-07-11	J. Thomas Sapienza, RTi	Changed "Measloc_num" to "MeasLoc_num"
//					to match the database
// 2002-08-20	JTS, RTi		Made toString() more descriptive
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

/**
Class to store data from the Station table
*/
public class RiversideDB_Station extends DMIDataObject
{

// From table Station

protected long _Station_num = DMIUtil.MISSING_LONG;
protected long _MeasLoc_num = DMIUtil.MISSING_LONG;
protected String _Station_id = DMIUtil.MISSING_STRING;
protected String _Station_name = DMIUtil.MISSING_STRING;
protected String _Source_abbrev = DMIUtil.MISSING_STRING;
protected String _Description = DMIUtil.MISSING_STRING;
protected String _Primary_flag = DMIUtil.MISSING_STRING;

/**
RiversideDB_MeasLoc constructor.
*/
public RiversideDB_Station ()
{	super();
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Station_id = null;
	_Station_name = null;
	_Source_abbrev = null;
	_Description = null;
	_Primary_flag = null;
	
	super.finalize();
}

/**
returns _Description

@return _Description
*/
public String getDescription() {
	return _Description;
}

/**
returns _MeasLoc_num

@return _MeasLoc_num
*/
public long getMeasLoc_num() {
	return _MeasLoc_num;
}

/**
returns _Primary_flag

@return _Primary_flag
*/
public String getPrimary_flag() {
	return _Primary_flag;
}

/**
returns _Source_abbrev

@return _Source_abbrev
*/
public String getSource_abbrev() {
	return _Source_abbrev;
}

/**
returns _Station_id

@return _Station_id
*/
public String getStation_id() {
	return _Station_id;
}

/**
returns _Station_name

@return _Station_name
*/
public String getStation_name() {
	return _Station_name;
}

/**
returns _Station_num

@return _Station_num
*/
public long getStation_num() {
	return _Station_num;
}

/**
sets _Description

@param Description value to put in _Description
*/
public void setDescription(String Description) {
	if ( Description != null ) {
		_Description = Description;
	}
}

/**
sets _MeasLoc_num

@param MeasLoc_num value to put in _MeasLoc_num
*/
public void setMeasLoc_num(long MeasLoc_num) {
	_MeasLoc_num = MeasLoc_num;
}

/**
sets _Primary_flag

@param Primary_flag value to put in _Primary_flag
*/
public void setPrimary_flag(String Primary_flag) {
	if ( Primary_flag != null ) {
		_Primary_flag = Primary_flag;
	}
}

/**
sets _Source_abbrev

@param Source_abbrev value to put in _Source_abbrev
*/
public void setSource_abbrev(String Source_abbrev) {
	if ( Source_abbrev != null ) {
		_Source_abbrev = Source_abbrev;
	}
}

/**
sets _Station_id

@param Station_id value to put in _Station_id
*/
public void setStation_id(String Station_id) {
	if ( Station_id != null ) {
		_Station_id = Station_id;
	}
}

/**
sets _Station_name

@param Station_name value to put in _Station_name
*/
public void setStation_name(String Station_name) {
	if ( Station_name != null ) {
		_Station_name = Station_name;
	}
}

/**
sets _Station_num

@param Station_num value to put in _Station_num
*/
public void setStation_num(long Station_num) {
	_Station_num = Station_num;
}

/** 
returns a string representation of this object

@return a string representation of this object
*/
public String toString() {
	return ( "RiversideDB_Station{" 		+ "\n" +
		"Station_num:  " + _Station_num		+ "\n" +
		"MeasLoc_num:  " + _MeasLoc_num		+ "\n" +
		"Station_id:   " + _Station_id		+ "\n" +
		"Station_name: " + _Station_name	+ "\n" +
		"Source_abbrev:" + _Source_abbrev	+ "\n" + 
		"Description:  " + _Description		+ "\n" + 
		"Primary_flag: " + _Primary_flag	+ "}"
	);
}

} // End RiversideDB_Station
