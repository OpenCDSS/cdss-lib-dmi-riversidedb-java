// ----------------------------------------------------------------------------
// RiversideDB_Area
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-06-27	Steven A. Malers, RTi	Initial version.
// 2002-07-11	J. Thomas Sapienza, RTi	Changed "Measloc_num" to "MeasLoc_num"
//					to match the database
// 2002-08-20	JTS, RTi		toString() made more descriptive
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMI;
import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import RTi.Util.Message.Message;

/**
Class to store data from the Area table
*/
public class RiversideDB_Area extends DMIDataObject
{

// From table Area

protected long _Area_num = DMIUtil.MISSING_LONG;
protected long _MeasLoc_num = DMIUtil.MISSING_LONG;
protected String _Area_id = DMIUtil.MISSING_STRING;
protected String _Area_name = DMIUtil.MISSING_STRING;
protected double _Area = DMIUtil.MISSING_DOUBLE;
protected String _Area_units = DMIUtil.MISSING_STRING;

/**
RiversideDB_Area constructor.
*/
public RiversideDB_Area ()
{	super();
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Area_id = null;
	_Area_name = null;
	_Area_units = null;
	
	super.finalize();
}

/**
returns _Area
@return _Area
*/
public double getArea() {
	return _Area;
}

/**
returns _Area_id
@return _Area_id
*/
public String getArea_id () {
	return _Area_id;
}

/**
returns _Area_name
@return _Area_name
*/
public String getArea_name () {
	return _Area_name;
}

/**
returns _Area_num
@return _Area_num
*/
public long getArea_num() {
	return _Area_num;
}

/**
returns _Area_units
@return _Area_units
*/
public String getArea_units() {
	return _Area_units;
}

/**
returns _MeasLoc_num
@return _MeasLoc_num
*/
public long getMeasLoc_num() {
	return _MeasLoc_num;
}

/**
sets _Area
@param Area value to put in _Area
*/
public void setArea(double Area) {
	_Area = Area;
}

/**
sets _Area_id
@param Area_id value to put in _Area_id
*/
public void setArea_id(String Area_id) {
	if ( Area_id != null ) {
		_Area_id = Area_id;
	}
}

/**
sets _Area_name
@param Area_name value to put in _Area_name
*/
public void setArea_name(String Area_name) {
	if ( Area_name != null ) {
		_Area_name = Area_name;
	}
}

/**
sets _Area_num
@param Area_num value to put in _Area_num
*/
public void setArea_num(long Area_num) {
	_Area_num = Area_num;
}

/**
sets _Area_units
@param Area_units value to put in _Area_units
*/
public void setArea_units(String Area_units) {
	if ( Area_units != null ) {
		_Area_units = Area_units;
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
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return "RiversideDB_Area{" 			+ "\n" + 
		"Area_num:   " + _Area_num		+ "\n" +
		"MeasLoc_num:" + _MeasLoc_num		+ "\n" +
		"Area_id:    " + _Area_id		+ "\n" +
		"Area_name:  " + _Area_name		+ "\n" +
		"Area:       " + _Area			+ "\n" + 
		"Area_units: " + _Area_units		+ "}";
}

} // End RiversideDB_Area
