// ----------------------------------------------------------------------------
// RiversideDB_MeasQualityFlag
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-06-27	Steven A. Malers, RTi	Initial version.
// 2002-08-20	J. Thomas Sapienza, RTi	Made toString() more descriptive
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// 2004-01-26	JTS, RTi		Added copy constructor. 
// 2004-01-27	JTS, RTi		Added equals().
// 2004-02-02	JTS, RTi		Class is now cloneable.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import RTi.Util.String.StringUtil;

/**
class to store data from the MeasQualityFlag table
*/
public class RiversideDB_MeasQualityFlag 
extends DMIDataObject
implements Cloneable {

// from table MeasQualityFlag

protected String _Quality_flag = DMIUtil.MISSING_STRING; // Key
protected String _Description  = DMIUtil.MISSING_STRING;

/**
Constructor.  
*/
public RiversideDB_MeasQualityFlag() {
	super();
}

/**
Copy constructor.
*/
public RiversideDB_MeasQualityFlag(RiversideDB_MeasQualityFlag m) {
	super();
	setQuality_flag(new String(m.getQuality_flag()));
	setDescription(new String(m.getDescription()));
	setDirty(m.isDirty());
}

/**
Clones the data object.
@return a clone of the data object.
*/
public Object clone() 
throws CloneNotSupportedException {
	RiversideDB_MeasQualityFlag mqf = 
		(RiversideDB_MeasQualityFlag)super.clone();
	return mqf;
}

/**
Clones the data object.
@return a clone of the data object.
*/
public RiversideDB_MeasQualityFlag cloneSelf() {
	RiversideDB_MeasQualityFlag mqf = null;
	try {
		mqf = (RiversideDB_MeasQualityFlag)clone();
	}
	catch (CloneNotSupportedException e) {
		mqf = new RiversideDB_MeasQualityFlag(this);
	}
	return mqf;
}

/**
Checks to see if this object has the same values as another 
RiversideDB_MeasQualityFlag object.  
@param o the Object against which to compare this object's values.
@return true if the objects have the same values, false if not.
*/
public boolean equals(Object o) {
	if (o == null) { 
		return false;
	}
	if (!(o instanceof RiversideDB_MeasQualityFlag)) {
		return false;
	}
	RiversideDB_MeasQualityFlag mqf = (RiversideDB_MeasQualityFlag)o;
	if (!StringUtil.stringsAreEqual(getQuality_flag(), 
		mqf.getQuality_flag())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDescription(), 
		mqf.getDescription())) {
		return false;
	}
	return true;
}

/**
Checks to see if this object has the same values as another
RiversideDB_MeasQualityFlag object.
@param mqf the RiversideDB_MeasQualityFlag object against which to compare
this one.
@return true if the objects have the same values, otherwise false.
*/
public boolean equals(RiversideDB_MeasQualityFlag mqf) {
	if (mqf == null) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getQuality_flag(), 
		mqf.getQuality_flag())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDescription(), 
		mqf.getDescription())) {
		return false;
	}
	return true;
}
	
/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Quality_flag = null;
	_Description = null;
	super.finalize();
}

/**
Returns _Description
@return _Description
*/
public String getDescription() {
	return _Description;
}

/**
Returns _Quality_flag
@return _Quality_flag
*/
public String getQuality_flag() {
	return _Quality_flag;
}

/**
Sets _Description
@param Description value to set _Description to
*/
public void setDescription (String Description) {
	if ( Description != null ) {
		_Description = Description;
	}
}

/**
Sets _Quality_flag
@param Quality_flag value to set _Quality_flag to
*/
public void setQuality_flag (String Quality_flag) {
	if ( Quality_flag != null ) {
		_Quality_flag = Quality_flag;
	}
}

/** 
Returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_MeasQualityFlag{" 	+ "\n" + 
		"Quality_flag:" + _Quality_flag	+ "\n" +
		"Description: " + _Description	+ "}";
}

}
