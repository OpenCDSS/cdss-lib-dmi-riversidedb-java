// ----------------------------------------------------------------------------
// RiversideDB_DataSource.java - class for doing I/O with these values
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

import RTi.Util.String.StringUtil;

/**
class to store data from the DataSource table
*/
public class RiversideDB_DataSource 
extends DMIDataObject
implements Cloneable {

// from table DataSource
protected String	_Source_abbrev	= DMIUtil.MISSING_STRING; // Key
protected String	_Source_name	= DMIUtil.MISSING_STRING;

/**
Constructor.  
*/
public RiversideDB_DataSource()
{	super();
}

/**
Copy constructor.
*/
public RiversideDB_DataSource(RiversideDB_DataSource d) {
	super();
	setSource_abbrev(new String(d.getSource_abbrev()));
	setSource_name(new String(d.getSource_name()));
	setDirty(d.isDirty());
}

/**
Clones the data object.
@return a clone of the data object.
*/
public Object clone() 
throws CloneNotSupportedException {
	RiversideDB_DataSource ds = (RiversideDB_DataSource)super.clone();
	return ds;
}

/**
Clones the data object.
@return a clone of the data object.
*/
public RiversideDB_DataSource cloneSelf() {
	RiversideDB_DataSource ds = null;
	try {
		ds = (RiversideDB_DataSource)clone();
	}
	catch (CloneNotSupportedException e) {
		ds = new RiversideDB_DataSource(this);
	}
	return ds;
}

/**
Checks to see if this object has the same values as another 
RiversideDB_DataSource object.
@param ds the RiversideDB_DataSource object against which to compare this one.
@return true if the objects have the sames values, false if not.
*/
public boolean equals(RiversideDB_DataSource ds) {
	if (ds == null) {	
		return false;
	}
	if (!StringUtil.stringsAreEqual(getSource_abbrev(), 
		ds.getSource_abbrev())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getSource_name(), ds.getSource_name())){
		return false;
	}
	return true;
}

/**
Checks to see if this object has the same values as another 
RiversideDB_DataSource object.
@param o the object against which to compare.
@return true if the objects have the sames values, false if not.
*/
public boolean equals(Object o) {
	if (o == null) { 
		return false;
	}
	if (!(o instanceof RiversideDB_DataSource)) {
		return false;
	}
	RiversideDB_DataSource ds = (RiversideDB_DataSource)o;
	if (!StringUtil.stringsAreEqual(getSource_abbrev(), 
		ds.getSource_abbrev())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getSource_name(), ds.getSource_name())){
		return false;
	}
	return true;
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Source_abbrev = null;
	_Source_name = null;
	super.finalize();
}

/**
returns _Source_abbrev
@return _Source_abbrev
*/
public String getSource_abbrev() {
	return _Source_abbrev;
}

/**
returns _Source_name
@return _Source_name
*/
public String getSource_name() {
	return _Source_name;
}

/**
sets _Source_abbrev
@param Source_abbrev value to set _Source_abbrev to
*/
public void setSource_abbrev (String Source_abbrev) {
	if ( Source_abbrev != null ) {
		_Source_abbrev = Source_abbrev;
	}
}

/**
sets _Source_name
@param Source_name value to set _Source_name to
*/
public void setSource_name (String Source_name) {
	if ( Source_name != null ) {
		_Source_name = Source_name;
	}
}

/** 
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_DataSource{" 		+ "\n" +  
		"Source_abbrev:" + _Source_abbrev	+ "\n" +
		"Source_name:  " + _Source_name		+ "}";
}

} // End RiversideDB_DataSource
