// ----------------------------------------------------------------------------
// RiversideDB_ImportType
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
class to store data from the ImportType table
*/
public class RiversideDB_ImportType 
extends DMIDataObject
implements Cloneable {

// from table ImportType
protected String _Name = DMIUtil.MISSING_STRING;
protected String _Comment = DMIUtil.MISSING_STRING;

/**
Constructor.  
*/
public RiversideDB_ImportType() {
	super();
}

/**
Copy constructor.
*/
public RiversideDB_ImportType(RiversideDB_ImportType i) {
	super();
	setName(new String(i.getName()));
	setComment(new String(i.getComment()));
	setDirty(i.isDirty());
}

/**
Clones the data object.
@return a clone of the data object.
*/
public Object clone() 
throws CloneNotSupportedException {
	RiversideDB_ImportType it = (RiversideDB_ImportType)super.clone();
	return it;
}

/**
Clones the data object.
@return a clone of the data object.
*/
public RiversideDB_ImportType cloneSelf() {
	RiversideDB_ImportType it = null;
	try {
		it = (RiversideDB_ImportType)clone();
	}
	catch (CloneNotSupportedException e) {
		it = new RiversideDB_ImportType(this);
	}
	return it;
}

/**
Checks to see if this object has the same values as another 
RiversideDB_ImportType object.
@param o the object against which to compare
@return true if the objects have the same values, false if not.
*/
public boolean equals(Object o) {
	if (o == null) { 
		return false;
	}
	if (!(o instanceof RiversideDB_ImportType)) {
		return false;
	}
	RiversideDB_ImportType it = (RiversideDB_ImportType)o;
	if (!StringUtil.stringsAreEqual(getName(), it.getName())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getComment(), it.getComment())) {
		return false;
	}
	return true;
}

/**
Checks to see if this object has the same values as another 
RiversideDB_ImportType object.
@param it the RiversideDB_ImportType object to compare against.
@return true if the objects have the same values, false if not.
*/
public boolean equals(RiversideDB_ImportType it) {
	if (it == null) {	
		return false;
	}
	if (!StringUtil.stringsAreEqual(getName(), it.getName())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getComment(), it.getComment())) {
		return false;
	}
	return true;
}

/**
Cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Name = null;
	_Comment = null;
	super.finalize();
}

/**
returns _Comment
@return _Comment
*/
public String getComment() {
	return _Comment;
}

/**
returns _Name
@return _Name
*/
public String getName() {
	return _Name;
}

/**
sets _Comment
@param Comment value to set _Comment to
*/
public void setComment (String Comment) {
	if ( Comment != null ) {
		_Comment = Comment;
	}
}

/**
sets _Name
@param Name value to set _Name to
*/
public void setName (String Name) {
	if ( Name != null ) {
		_Name = Name;
	}
}

/** 
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_ImportType{" 	+ "\n" + 
		"Name:   " + _Name		+ "\n" +
		"Comment:" + _Comment		+ "}";
}

} // End RiversideDB_ImportType
