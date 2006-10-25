// ----------------------------------------------------------------------------
// RiversideDB_MeasCreateMethod
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
class to store data from the MeasCreateMethod table
*/
public class RiversideDB_MeasCreateMethod 
extends DMIDataObject
implements Cloneable {

// from table DataSource
protected String _Method      = DMIUtil.MISSING_STRING; // Key
protected String _Description = DMIUtil.MISSING_STRING;

/**
Constructor.  
*/
public RiversideDB_MeasCreateMethod() {
	super();
}

/**
Copy constructor.
*/
public RiversideDB_MeasCreateMethod(RiversideDB_MeasCreateMethod m) {
	super();
	setMethod(new String(m.getMethod()));
	setDescription(new String(m.getDescription()));
	setDirty(m.isDirty());
}

/**
Clones the data object.
@return a clone of the data object.
*/
public Object clone() 
throws CloneNotSupportedException {
	RiversideDB_MeasCreateMethod mcm = 	
		(RiversideDB_MeasCreateMethod)super.clone();
	return mcm;
}

/**
Clones the data object.
@return a clone of the data object.
*/
public RiversideDB_MeasCreateMethod cloneSelf() {
	RiversideDB_MeasCreateMethod mcm = null;
	try {
		mcm = (RiversideDB_MeasCreateMethod)clone();
	}
	catch (CloneNotSupportedException e) {
		mcm = new RiversideDB_MeasCreateMethod(this);
	}
	return mcm;
}

/**
Checks to see if this object has the same values as another 
RiversideDB_MeasCreateMethod object.  
@param o the Object against which to compare this object's values.
@return true if the objects have the same values, false if not.
*/
public boolean equals(Object o) {
	if (o == null) { 
		return false;
	}
	if (!(o instanceof RiversideDB_MeasCreateMethod)) {
		return false;
	}
	RiversideDB_MeasCreateMethod mcm = (RiversideDB_MeasCreateMethod)o;
	if (!StringUtil.stringsAreEqual(getMethod(), mcm.getMethod())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDescription(), 
		mcm.getDescription())) {
		return false;
	}
	return true;
}

/**
Checks to see if this object has the same values as another 
RiversideDB_MeasCreateMethod object.  
@param mcm the RiversideDB_MeasCreateMethod object against which to compare this
object's values.
@return true if the objects have the same values, false if not.
*/
public boolean equals(RiversideDB_MeasCreateMethod mcm) {
	if (mcm == null) {	
		return false;
	}
	if (!StringUtil.stringsAreEqual(getMethod(), mcm.getMethod())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDescription(), 
		mcm.getDescription())) {
		return false;
	}
	return true;
}

/**
Cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Method = null;
	_Description = null;
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
returns _Method
@return _Method
*/
public String getMethod() {
	return _Method;
}

/**
sets _Description
@param Description value to set _Description to
*/
public void setDescription (String Description) {
	if ( Description != null ) {
		_Description = Description;
	}
}

/**
sets _Method
@param Method value to set _Method to
*/
public void setMethod (String Method) {
	if ( Method != null ) {
		_Method = Method;
	}
}

/** 
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_MeasCreateMethod{" 	+ "\n" + 
		"Description:" + _Description		+ "\n" +
		"Method:     " + _Method		+ "}";
}

} // End RiversideDB_MeasCreateMethod
