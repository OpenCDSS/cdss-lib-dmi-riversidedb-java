// ----------------------------------------------------------------------------
// RiversideDB_MeasTypeStatus
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-06-28	Steven A. Malers, RTi	Initial version.
// 2002-08-20	J. Thomas Sapienza, RTi	Made toString() more descriptive
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import java.util.Date;

/**
class to store data from the MeasTypeStatus table
*/
public class RiversideDB_MeasTypeStatus extends DMIDataObject
{

protected long _MeasType_num = DMIUtil.MISSING_LONG;
protected Date _Status_date = DMIUtil.MISSING_DATE;
protected String _Status = DMIUtil.MISSING_STRING;
protected String _Comment = DMIUtil.MISSING_STRING;

/** 
Constructor. 
*/
public RiversideDB_MeasTypeStatus() {
	super ();
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Comment = null;
	_Status_date = null;
	_Status = null;
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
returns _Status

@return _Status
*/
public String getStatus() {
	return _Status;
}

/**
returns _Status_date

@return _Status_date
*/
public Date getStatus_date() {
	return _Status_date;
}

/**
returns _MeasType_num

@return _MeasType_num
*/
public long getMeasType_num() {
	return _MeasType_num;
}

/**
sets _Comment

@param Comment value to set _Comment to
*/
public void setComment(String Comment) {
	if ( Comment != null ) {
		_Comment = Comment;
	}
}

/**
sets _MeasType_num

@param MeasType_num value to set _MeasType_num to
*/
public void setMeasType_num(long MeasType_num) {
	_MeasType_num = MeasType_num;
}

/**
sets _Status

@param Status value to set _Status to
*/
public void setStatus(String Status) {
	if ( Status != null ) {
		_Status = Status;
	}
}

/**
sets _Status_date

@param Status_date value to set _Status_date to
*/
public void setStatus_date(Date Status_date) {
	_Status_date = Status_date;
}

/** 
returns a string representation of this object

@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_MeasTypeStatus{ " 		+ "\n" + 
		"MeasType_num:" + _MeasType_num		+ "\n" +
		"Status_date: " + _Status_date		+ "\n" +
		"Status:      " + _Status		+ "\n" +
		"Comment:     " + _Comment		+ "}";
}

} // End RiversideDB_MeasTypeStatus
