// ----------------------------------------------------------------------------
// RiversideDB_MeasTransProtocol
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
class to store data from the MeasTransProtocol table
*/
public class RiversideDB_MeasTransProtocol 
extends DMIDataObject
implements Cloneable {

// from table MeasTransProtocol

protected String _Protocol    = DMIUtil.MISSING_STRING; // Key
protected String _Description = DMIUtil.MISSING_STRING;

/**
Constructor.  
*/
public RiversideDB_MeasTransProtocol() {
	super();
}

/**
Copy constructor.
*/
public RiversideDB_MeasTransProtocol(RiversideDB_MeasTransProtocol m) {
	super();
	setProtocol(new String(m.getProtocol()));
	setDescription(new String(m.getDescription()));
	setDirty(m.isDirty());
}

/**
Clones the data object.
@return a clone of the data object.
*/
public Object clone() 
throws CloneNotSupportedException {
	RiversideDB_MeasTransProtocol mtp = 
		(RiversideDB_MeasTransProtocol)super.clone();
	return mtp;
}

/**
Clones the data object.
@return a clone of the data object.
*/
public RiversideDB_MeasTransProtocol cloneSelf() {
	RiversideDB_MeasTransProtocol mtp = null;
	try {
		mtp = (RiversideDB_MeasTransProtocol)clone();
	}
	catch (CloneNotSupportedException e) {
		mtp = new RiversideDB_MeasTransProtocol(this);
	}
	return mtp;
}

/**
Checks to see if this object has the same values as another 
RiversideDB_MeasTransProtocol object.  
@param o the Object against which to compare this object's values.
@return true if the objects have the same values, false if not.
*/
public boolean equals(Object o) {
	if (o == null) { 
		return false;
	}
	if (!(o instanceof RiversideDB_MeasTransProtocol)) {
		return false;
	}
	RiversideDB_MeasTransProtocol mtp = (RiversideDB_MeasTransProtocol)o;
	if (!StringUtil.stringsAreEqual(getProtocol(), mtp.getProtocol())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDescription(), 
		mtp.getDescription())) {
		return false;
	}
	return true;
}

/**
Checks to see if this object has the same values as another
RiversideDB_MeasTransProtocol object.
@param mtp the RiversideDB_MeasTransProtocol object against which to compare
this object's values.
@return true if the values are the same, false if not.
*/
public boolean equals(RiversideDB_MeasTransProtocol mtp) {
	if (mtp == null) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getProtocol(), mtp.getProtocol())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDescription(), 
		mtp.getDescription())) {
		return false;
	}
	return true;
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Protocol = null;
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
Returns _Protocol
@return _Protocol
*/
public String getProtocol() {
	return _Protocol;
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
Sets _Protocol
@param Protocol value to set _Protocol to
*/
public void setProtocol (String Protocol) {
	if ( Protocol != null ) {
		_Protocol = Protocol;
	}
}

/** 
Returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_MeasTransProtocol{" 	+ "\n" + 
		"Protocol:   " + _Protocol		+ "\n" +
		"Description:" + _Description		+ "}";
}

}
