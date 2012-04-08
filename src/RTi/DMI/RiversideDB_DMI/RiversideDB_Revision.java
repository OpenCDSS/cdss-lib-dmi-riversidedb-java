package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import java.util.Date;

/**
class to store data from the Revision table
*/
public class RiversideDB_Revision extends DMIDataObject
{

private long _Revision_num = DMIUtil.MISSING_LONG;
private Date _Date_Time = DMIUtil.MISSING_DATE;
private String _User = DMIUtil.MISSING_STRING;
private String _Comment = DMIUtil.MISSING_STRING;

/**
Constructor.  
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
Returns _Comment
@return _Comment
*/
public String getComment() {
	return _Comment;
}

/**
Returns _Date_Time
@return _Date_Time
*/
public Date getDate_Time() {
	return _Date_Time;
}

/**
Returns _Revision_num
@return _Revision_num
*/
public long getRevision_num() {
	return _Revision_num;
}

/**
Returns _User
@return _User
*/
public String getUser() {
    return _User;
}

/**
Sets _Comment
@param Comment value to set _Comment
*/
public void setComment (String Comment) {
	_Comment = Comment;
}

/** 
Sets _Date_Time
@param Date_Time value to set _Date_Time
*/
public void setDate_Time (Date Date_Time) {
	_Date_Time = Date_Time;
}

/**
Sets _Revision_num
@param Revision_num value to set _Revision_num
*/
public void setRevision_num (long Revision_num) {
	_Revision_num = Revision_num;
}

/**
Sets _User
@param User value to set _User
*/
public void setUser (String User) {
    _User = User;
}

/** 
Returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_Revision{\n" + 
		"Revision_num:" + _Revision_num + "\n" +
		"Date_Time:   " + _Date_Time + "\n" +
		"User:        " + _Date_Time + "\n" +
		"Comment:     " + _Comment + " }";
}

}