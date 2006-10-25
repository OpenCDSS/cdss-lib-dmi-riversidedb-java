// ----------------------------------------------------------------------------
// RiversideDB_MeasReductionType
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-06-28	Steven A. Malers, RTi	Initial version.
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
class to store data from the MeasReductionType table
*/
public class RiversideDB_MeasReductionType 
extends DMIDataObject
implements Cloneable {

// from table MeasReductionType
protected String _Type        = DMIUtil.MISSING_STRING; // Key
protected String _Description = DMIUtil.MISSING_STRING;

/**
Constructor.  
*/
public RiversideDB_MeasReductionType() {
	super();
}

/**
Copy constructor.
*/
public RiversideDB_MeasReductionType(RiversideDB_MeasReductionType m) {
	super();
	setType(new String(m.getType()));
	setDescription(new String(m.getDescription()));
	setDirty(m.isDirty());
}

/**
Clones the data object.
@return a clone of the data object.
*/
public Object clone() 
throws CloneNotSupportedException {
	RiversideDB_MeasReductionType mrt = 
		(RiversideDB_MeasReductionType)super.clone();
	return mrt;
}

/**
Clones the data object.
@return a clone of the data object.
*/
public RiversideDB_MeasReductionType cloneSelf() {
	RiversideDB_MeasReductionType mrt = null;
	try {
		mrt = (RiversideDB_MeasReductionType)clone();
	}
	catch (CloneNotSupportedException e) {
		mrt = new RiversideDB_MeasReductionType(this);
	}
	return mrt;
}

/**
Checks to see if this object has the same values as another 
RiversideDB_MeasReductionType object.  
@param o the Object against which to compare this object's values.
@return true if the objects have the same values, false if not.
*/
public boolean equals(Object o) {
	if (o == null) { 
		return false;
	}
	if (!(o instanceof RiversideDB_MeasReductionType)) {
		return false;
	}
	RiversideDB_MeasReductionType mrt = (RiversideDB_MeasReductionType)o;
	if (!StringUtil.stringsAreEqual(getType(), mrt.getType())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDescription(), 
		mrt.getDescription())) {
		return false;
	}
	return true;
}

/**
Checks to see if this object has the same values as another 
RiversideDB_MeasReductionType object.
@param mrt the RiversideDB_MeasReductionType object against which to compare
values.
@return true if the values are the same, false if not.
*/
public boolean equals(RiversideDB_MeasReductionType mrt) {
	if (mrt == null) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getType(), mrt.getType())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDescription(), 
		mrt.getDescription())) {
		return false;
	}
	return true;
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Type = null;
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
Returns _Type
@return _Type
*/
public String getType() {
	return _Type;
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
Sets _Type
@param Type value to set _Type to
*/
public void setType (String Type) {
	if ( Type != null ) {
		_Type = Type;
	}
}

/** 
Returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_MeasReductionType{" 	+ "\n" +
		"Type:       " + _Type			+ "\n" +
		"Description:" + _Description		+ "}";
}

}
