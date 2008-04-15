// ----------------------------------------------------------------------------
// RiversideDB_TableLayout
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
class to store data from the TableLayout table
*/
public class RiversideDB_TableLayout 
extends DMIDataObject
implements Cloneable {

// from table ImportType
protected long _TableLayout_num = DMIUtil.MISSING_LONG; // Key
protected String _Identifier    = DMIUtil.MISSING_STRING;
protected String _Description   = DMIUtil.MISSING_STRING;

/**
Constructor.  
*/
public RiversideDB_TableLayout() {
	super();
}

/**
Copy constructor.
*/
public RiversideDB_TableLayout(RiversideDB_TableLayout t) {
	super();
	setTableLayout_num(t.getTableLayout_num());
	setIdentifier(new String(t.getIdentifier()));
	setDescription(new String(t.getDescription()));
	setDirty(t.isDirty());
}

/**
Clones the data object.
@return a clone of the data object.
*/
public Object clone() 
throws CloneNotSupportedException {
	RiversideDB_TableLayout tl = (RiversideDB_TableLayout)super.clone();
	return tl;
}

/**
Clones the data object.
@return a clone of the data object.
*/
public RiversideDB_TableLayout cloneSelf() {
	RiversideDB_TableLayout tl = null;
	try {
		tl = (RiversideDB_TableLayout)clone();
	}
	catch (CloneNotSupportedException e) {
		tl = new RiversideDB_TableLayout(this);
	}
	return tl;
}

/**
Checks to see if this object has the same values as another 
RiversideDB_TableLayout object.  
@param o the Object against which to compare this object's values.
@return true if the objects have the same values, false if not.
*/
public boolean equals(Object o) {
	if (o == null) { 
		return false;
	}
	if (!(o instanceof RiversideDB_TableLayout)) {
		return false;
	}
	RiversideDB_TableLayout tl = (RiversideDB_TableLayout)o;
	if (getTableLayout_num() != tl.getTableLayout_num()) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getIdentifier(), tl.getIdentifier())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDescription(), tl.getDescription())){
		return false;
	}
	return true;
}

/**
Checks to see if this object's values are the same as the values in another
RiversideDB_TableLayout object.
@param tl the RiversideDB_TableLayout object against which to compare values.
@return true if the values are the same, false if not.
*/
public boolean equals(RiversideDB_TableLayout tl) {
	if (tl == null) {
		return false;
	}
	if (getTableLayout_num() != tl.getTableLayout_num()) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getIdentifier(), tl.getIdentifier())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDescription(), tl.getDescription())){
		return false;
	}
	return true;
}

/**
Cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Identifier = null;
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
Returns _Identifier
@return _Identifier
*/
public String getIdentifier() {
	return _Identifier;
}

/**
Returns _TableLayout_num
@return _TableLayout_num
*/
public long getTableLayout_num() {
	return _TableLayout_num;
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
Sets _Identifier
@param Identifier value to set _Identifier to
*/
public void setIdentifier (String Identifier) {
	if ( Identifier != null ) {
		_Identifier = Identifier;
	}
}

/**
Sets _TableLayout_num
@param TableLayout_num value to set _TableLayout_num to
*/
public void setTableLayout_num (long TableLayout_num) {
	_TableLayout_num = TableLayout_num;
}

/** 
Returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_ImportType{" 		+ "\n" + 
		"TableLayout_num:" + _TableLayout_num	+ "\n" +
		"Identifier:     " + _Identifier	+ "\n" +
		"Description:    " + _Description	+ "}";
}

}
