// ----------------------------------------------------------------------------
// RiversideDB_ExportType
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
class to store data from the ExportType table
*/
public class RiversideDB_ExportType extends DMIDataObject
{

// from table ExportType
protected String _Name = DMIUtil.MISSING_STRING;
protected String _Comment = DMIUtil.MISSING_STRING;

/**
constructor.  
*/
public RiversideDB_ExportType()
{	super();
}

/**
cleans up variables when the class is disposed of.  Sets all the member
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
	return  "RiversideDB_ExportType{" 	+ "\n" +
		"Name:   " + _Name		+ "\n" 	+
		"Comment:" + _Comment		+ "}";
}

} // End RiversideDB_ExportType
