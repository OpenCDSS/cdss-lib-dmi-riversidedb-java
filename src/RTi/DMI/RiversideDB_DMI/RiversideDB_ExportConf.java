// ----------------------------------------------------------------------------
// RiversideDB_ExportConf.java - class for doing I/O with these values
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
// 2002-05-22	J. Thomas Sapienza, RTi	Initial version
// 2002-05-23	JTS, RTi		Added support for ExportProduct_num 
//					and MeasType_num.  Added 
//					fillObjectVector method.  Added 
//					constructor, removed parentheses from 
//					returns
// 2002-05-29	JTS, RTi		Added __dirty variable and methods
// 2002-06-26	Steven A. Malers, RTi	Remove I/O from code.
// 2002-08-20	JTS, RTi		Added IsActive (replaced Active in
//					Database version 02.07.00).  Made
//					the toString better for displaying
//					different fields version info.
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// 2003-06-05	JTS, RTi		Added a copy constructor.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMI;
import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import java.util.Vector;

/**
class to store data from the ExportConf table
*/
public class RiversideDB_ExportConf extends DMIDataObject
{

protected long	_ExportProduct_num	= DMIUtil.MISSING_LONG;
protected long	_MeasType_num		= DMIUtil.MISSING_LONG;

protected String _Export_id 		= DMIUtil.MISSING_STRING;
protected String _Export_units 		= DMIUtil.MISSING_STRING;
protected String _Active 		= DMIUtil.MISSING_STRING;
protected String _IsActive		= DMIUtil.MISSING_STRING;

/**
Constructor.
*/
public RiversideDB_ExportConf() {
	super();
}

/**
Copy constructor.
@param r the RiversideDB_ExportConf object to copy.
*/
public RiversideDB_ExportConf(RiversideDB_ExportConf r) {
	super();
	
	setExportProduct_num(r.getExportProduct_num());
	setMeasType_num(r.getMeasType_num());
	setExport_id(new String(r.getExport_id()));
	setExport_units(new String(r.getExport_units()));
	setActive(new String(r.getActive()));
	setIsActive(new String(r.getIsActive()));
	
	setDirty(r.isDirty());
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Export_id = null;
	_Export_units = null;
	_Active = null;
	
	super.finalize();
}

/**
returns _Active
@return _Active
*/
public String getActive() {
	return _Active;
}

/**
returns _Export_id
@return _Export_id
*/
public String getExport_id() {
	return _Export_id;
}

/**
returns _Export_units
@return _Export_units
*/
public String getExport_units() {
	return _Export_units;
}

/**
returns _ExportProduct_num
@return _ExportProduct_num
*/
public long getExportProduct_num() {
	return _ExportProduct_num;
}

/**
returns _IsActive
@return _IsAcgive
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
sets _Active
@param Active value to set _Active to
*/
public void setActive(String Active) {
	if ( Active != null ) {
		_Active = Active;
	}
}

/** 
sets _Export_id
@param Export_id value to set _Export_id to
*/
public void setExport_id(String Export_id) {
	if ( Export_id != null ) {
		_Export_id = Export_id;
	}
}

/**
sets _Export_units
@param Export_units value to set _Export_units to
*/
public void setExport_units(String Export_units) {
	if ( Export_units != null ) {
		_Export_units = Export_units;
	}
}

/**
sets _IsActive
@param IsActive value to set _IsActive to
*/
public void setIsActive (String IsActive) {
	_IsActive = IsActive;
}

/**
sets _ExportProduct_num
@param ExportProduct_num value to set _ExportProduct_num to
*/
public void setExportProduct_num(long ExportProduct_num) {
	_ExportProduct_num = ExportProduct_num;
}

/**
sets _MeasType_num
@param MeasType_num value to set _MeasType_num to
*/
public void setMeasType_num(long MeasType_num) {
	_MeasType_num = MeasType_num;
}

/** 
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_ExportConf{ " 			+ "\n" +
		"ExportProduct_num:    " + _ExportProduct_num	+ "\n" +
		"MeasType_num:         " + _MeasType_num	+ "\n" +
		"Export_id:            " + _Export_id 		+ "\n" +
		"Export_units:         " + _Export_units 	+ "\n" + 
		"IsActive (02.07.00):  " + _IsActive		+ "\n" +
		"Active (pre-02.07.00):" + _Active 		+ "}"
	;
}

} // end RiversideDB_ExportConf
