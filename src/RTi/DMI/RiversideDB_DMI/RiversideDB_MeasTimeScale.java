// ----------------------------------------------------------------------------
// RiversideDB_MeasTimeScale
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

import RTi.DMI.DMI;
import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import RTi.Util.String.StringUtil;

/**
class to store data from the MeasTimeScale table
*/
public class RiversideDB_MeasTimeScale 
extends DMIDataObject
implements Cloneable {

// from table MeasTimeScale

protected String _Scale       = DMIUtil.MISSING_STRING; // Key
protected String _Description = DMIUtil.MISSING_STRING;

/**
Constructor.
*/
public RiversideDB_MeasTimeScale() {
	super();
}

/**
Copy constructor.
*/
public RiversideDB_MeasTimeScale(RiversideDB_MeasTimeScale m) {
	super();
	setScale(new String(m.getScale()));
	setDescription(new String(m.getDescription()));
	setDirty(m.isDirty());
}

/**
Clones the data object.
@return a clone of the data object.
*/
public Object clone() 
throws CloneNotSupportedException {
	RiversideDB_MeasTimeScale mts = 
		(RiversideDB_MeasTimeScale)super.clone();
	return mts;
}

/**
Clones the data object.
@return a clone of the data object.
*/
public RiversideDB_MeasTimeScale cloneSelf() {
	RiversideDB_MeasTimeScale mts = null;
	try {
		mts = (RiversideDB_MeasTimeScale)clone();
	}
	catch (CloneNotSupportedException e) {
		mts = new RiversideDB_MeasTimeScale(this);
	}
	return mts;
}

/**
Checks to see if this object has the same values as another 
RiversideDB_MeasTimeScale object.  
@param o the Object against which to compare this object's values.
@return true if the objects have the same values, false if not.
*/
public boolean equals(Object o) {
	if (o == null) { 
		return false;
	}
	if (!(o instanceof RiversideDB_MeasTimeScale)) {
		return false;
	}
	RiversideDB_MeasTimeScale mts = (RiversideDB_MeasTimeScale)o;
	if (!StringUtil.stringsAreEqual(getScale(), mts.getScale())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDescription(), 
		mts.getDescription())) {
		return false;
	}
	return true;
}

/**
Checks to see if this object has the same values as another 
RiversideDB_MeasTimeScale object.
@param mts the RiversideDB_MeasTimeScale object against which to compare this
one.
@return true if the values are the same, false if not.
*/
public boolean equals(RiversideDB_MeasTimeScale mts) {
	if (mts == null) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getScale(), mts.getScale())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDescription(), 
		mts.getDescription())) {
		return false;
	}
	return true;
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Scale = null;
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
Returns _Scale
@return _Scale
*/
public String getScale() {
	return _Scale;
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
Sets _Scale
@param Scale value to set _Scale to
*/
public void setScale (String Scale) {
	if ( Scale != null ) {
		_Scale = Scale;
	}
}

/** 
Returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_MeasTimeScale{" 		+ "\n" + 
		"Scale:      " + _Scale			+ "\n" +
		"Description:" + _Description		+ "}";
}

}
