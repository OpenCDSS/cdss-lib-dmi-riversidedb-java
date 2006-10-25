// ----------------------------------------------------------------------------
// RiversideDB_Revision
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-07-11	J. Thomas Sapienza, RTi	Initial Version
// 2002-08-20	JTS, RTi		Made toString() more descriptive
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMI;
import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import java.util.Date;

/**
class to store data from the Revision table
*/

public class RiversideDB_Revision extends DMIDataObject
{

protected long _RatingTable_num = DMIUtil.MISSING_LONG;
protected long _Revision_num = DMIUtil.MISSING_LONG;
protected Date _Date_Time = DMIUtil.MISSING_DATE;
protected String _Comment = DMIUtil.MISSING_STRING;

/**
constructor.  
*/

public RiversideDB_Revision()
{	super();
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/

protected void finalize() throws Throwable {
	_Date_Time = null;
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
returns _Date_Time

@return _Date_Time
*/

public Date getDate_Time() {
	return _Date_Time;
}

/**
returns _Revision_num

@return _Revision_num
*/

public long getRevision_num() {
	return _Revision_num;
}


/**
sets _Comment

@param Comment value to set _Comment to
*/

public void setComment (String Comment) {
	_Comment = Comment;
}

/** 
sets _Date_Time

@param Date_Time value to set _Date_Time to
*/

public void SetDate_Time (Date Date_Time) {
	_Date_Time = Date_Time;
}

/**
sets _Revision_num

@param Revision_num value to set _Revision_num to
*/

public void setRevision_num (long Revision_num) {
	_Revision_num = Revision_num;
}

/** 
returns a string representation of this object

@return a string representation of this object
*/

public String toString() {
	return  "RiversideDB_Revision{" 		+ "\n" + 
		"Revision_num:" + _Revision_num 	+ "\n" +
		"Date_Time:   " + _Date_Time 		+ "\n" +
		"Comment:     " + _Comment		+ "}";
}

} // End RiversideDB_Revision
