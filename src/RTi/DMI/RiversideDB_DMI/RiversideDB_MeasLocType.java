// ----------------------------------------------------------------------------
// RiversideDB_MeasLocType
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-06-27	Steven A. Malers, RTi	Initial version.
// 2002-08-20	J. Thomas Sapienza, RTi	Made toString() more descriptive
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMI;
import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

/**
class to store data from the MeasLocType table
*/
public class RiversideDB_MeasLocType extends DMIDataObject
{

// from table MeasLocType
protected String _Type = DMIUtil.MISSING_STRING;
protected String _Description = DMIUtil.MISSING_STRING;

/**
constructor.  
*/
public RiversideDB_MeasLocType()
{	super();
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
returns _Description

@return _Description
*/
public String getDescription() {
	return _Description;
}

/**
returns _Type

@return _Type
*/
public String getType() {
	return _Type;
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
sets _Type

@param Type value to set _Type to
*/
public void setType (String Type) {
	if ( Type != null ) {
		_Type = Type;
	}
}

/** 
returns a string representation of this object

@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_MeasLocType{" 	+ "\n" +
		"Type:       " + _Type		+ "\n" +
		"Description:" + _Description	+ "}";
}

} // End RiversideDB_MeasLocType
