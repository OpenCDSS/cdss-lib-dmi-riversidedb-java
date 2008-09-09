// ----------------------------------------------------------------------------
// RiversideDB_DataDimension
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-06-27	Steven A. Malers, RTi	Initial version.
// 2002-08-20	J. Thomas Sapienza, RTi	toString() made more descriptive
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// 2004-01-26	JTS, RTi		Added copy constructor.
// 2004-01-27	JTS, RTi		Added equals().
// 2004-02-02	JTS, RTi		Class is now cloneable.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import RTi.Util.IO.DataDimension;
import RTi.Util.String.StringUtil;

/**
class to store data from the DataDimension table
*/
public class RiversideDB_DataDimension extends DMIDataObject
	implements Cloneable
{

// from table DataDimension

protected String _Dimension   = DMIUtil.MISSING_STRING; // Key
protected String _Description = DMIUtil.MISSING_STRING;

/**
constructor.  
*/
public RiversideDB_DataDimension() {
	super();
}

/**
Copy constructor.
*/
public RiversideDB_DataDimension(RiversideDB_DataDimension d) {
	super();
	setDimension(new String(d.getDimension()));
	setDescription(new String(d.getDescription()));
	setDirty(d.isDirty());
}

/**
Clones the data object.
@return a clone of the data object.
*/
public Object clone() 
throws CloneNotSupportedException {
	RiversideDB_DataDimension dd = (RiversideDB_DataDimension)super.clone();
	return dd;
}

/**
Clones the data object.
@return a clone of the data object.
*/
public RiversideDB_DataDimension cloneSelf() {
	RiversideDB_DataDimension dd = null;
	try {
		dd = (RiversideDB_DataDimension)clone();
	}
	catch (CloneNotSupportedException e) {
		dd = new RiversideDB_DataDimension(this);
	}
	return dd;
}

/**
Checks whether this object has the same values as another 
RiversideDB_DataDimension object.
@param o the object against which to compare.
@return true if all values are equal, false if not.
*/
public boolean equals(Object o) {
	if (o == null) {
		return false;
	}
	if (!(o instanceof RiversideDB_DataDimension)) {
		return false;
	}
	RiversideDB_DataDimension dd = (RiversideDB_DataDimension)o;
	if (dd == null) {	
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDimension(), dd.getDimension())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDescription(), dd.getDescription())){
		return false;
	}
	return true;
}

/**
Checks whether this object has the same values as another 
RiversideDB_DataDimension object.
@param dd the RiversideDB_DataDimension object to compare to.
@return true if all values are equal, false if not.
*/
public boolean equals(RiversideDB_DataDimension dd) {
	if (dd == null) {	
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDimension(), dd.getDimension())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDescription(), dd.getDescription())){
		return false;
	}
	return true;
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Dimension = null;
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
returns _Dimension
@return _Dimension
*/
public String getDimension() {
	return _Dimension;
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
sets _Dimension
@param Dimension value to set _Dimension to
*/
public void setDimension (String Dimension) {
	if ( Dimension != null ) {
		_Dimension = Dimension;
	}
}

/** 
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_DataDimension{" 	+ "\n" +
		"Dimension:  " + _Dimension	+ "\n" +
		"Description:" + _Description	+ "}";
}

/** 
returns a DataDimension representation of this object
@return a DataDimension representation of this object
*/
public DataDimension toDataDimension() {
    return new DataDimension(_Dimension,_Description);
}


} // End RiversideDB_DataDimension
