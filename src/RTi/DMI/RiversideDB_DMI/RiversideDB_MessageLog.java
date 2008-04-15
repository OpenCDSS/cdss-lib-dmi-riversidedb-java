// ----------------------------------------------------------------------------
// RiversideDB_MessageLog
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-06-27	Steven A. Malers, RTi	Initial version.
// 2002-08-20	J. Thomas Sapienza, RTi	Made toString() more descriptive
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// 2004-01-26	JTS, RTi 		* Added Message_num field.
// 					* Added copy constructor.
// 2004-01-27	JTS, RTi		Added equals().
// 2004-02-02	JTS, RTi		Class is now cloneable.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import java.util.Date;

import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import RTi.Util.String.StringUtil;

/**
class to store data from the MessageLog table
*/
public class RiversideDB_MessageLog 
extends DMIDataObject
implements Cloneable {

protected long _Message_num    = DMIUtil.MISSING_LONG;
protected Date _Date_Time      = DMIUtil.MISSING_DATE;
protected String _Routine      = DMIUtil.MISSING_STRING;
protected String _Message_Type = DMIUtil.MISSING_STRING;
protected long _Message_Level  = DMIUtil.MISSING_LONG;
protected String _Message      = DMIUtil.MISSING_STRING;

/** 
Constructor. 
*/
public RiversideDB_MessageLog() {
	super ();
}

/**
Copy constructor.
*/
public RiversideDB_MessageLog(RiversideDB_MessageLog m) {
	super();
	setMessage_num(m.getMessage_num());
	Date tempDate = m.getDate_Time();
	Date clonedDate = null;
	if (tempDate != null){ 
		clonedDate = new Date(tempDate.getTime());
		setDate_Time(clonedDate);
	}
	setRoutine(new String(m.getRoutine()));
	setMessage_Type(new String(m.getMessage_Type()));
	setMessage_Level(m.getMessage_Level());
	setMessage(new String(m.getMessage()));
	setDirty(m.isDirty());
}

/**
Clones the data object.
@return a clone of the data object.
*/
public Object clone() 
throws CloneNotSupportedException {
	RiversideDB_MessageLog ml = (RiversideDB_MessageLog)super.clone();
	return ml;
}

/**
Clones the data object.
@return a clone of the data object.
*/
public RiversideDB_MessageLog cloneSelf() {
	RiversideDB_MessageLog ml = null;
	try {
		ml = (RiversideDB_MessageLog)clone();
	}
	catch (CloneNotSupportedException e) {
		ml = new RiversideDB_MessageLog(this);
	}
	return ml;
}

/**
Checks to see if this object has the same values as another 
RiversideDB_MessageLog object.  
@param o the Object against which to compare this object's values.
@return true if the objects have the same values, false if not.
*/
public boolean equals(Object o) {
	if (o == null) { 
		return false;
	}
	if (!(o instanceof RiversideDB_MessageLog)) {
		return false;
	}
	RiversideDB_MessageLog ml = (RiversideDB_MessageLog)o;
	if (getMessage_num() != ml.getMessage_num()) {
		return false;
	}
	if (!getDate_Time().equals(ml.getDate_Time())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getRoutine(), ml.getRoutine())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getMessage_Type(), 
		ml.getMessage_Type())) {
		return false;
	}
	if (getMessage_Level() != ml.getMessage_Level()) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getMessage(), ml.getMessage())) {
		return false;
	}
	return true;
}

/**
Checks to see if this object has the same values as another 
RiversideDB_MessageLog object.
@param ml the RiversideDB_MessageLog object against which to compare this one.
@return true if the values are the same, false if not.
*/
public boolean equals(RiversideDB_MessageLog ml) {
	if (ml == null) {
		return false;
	}
	if (getMessage_num() != ml.getMessage_num()) {
		return false;
	}
	if (!getDate_Time().equals(ml.getDate_Time())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getRoutine(), ml.getRoutine())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getMessage_Type(), 
		ml.getMessage_Type())) {
		return false;
	}
	if (getMessage_Level() != ml.getMessage_Level()) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getMessage(), ml.getMessage())) {
		return false;
	}
	return true;
}

/**
Cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Date_Time = null;
	_Routine = null;
	_Message_Type = null;
	_Message = null;

	super.finalize();
}

/**
Returns _Date_Time
@return _Date_Time
*/
public Date getDate_Time() {
	return _Date_Time;
}

/**
Returns _Message
@return _Message
*/
public String getMessage() {
	return _Message;
}

/**
Returns _Message_Level
@return _Message_Level
*/
public long getMessage_Level() {
	return _Message_Level;
}

/**
Returns _Message_num
@return _Message_num
*/
public long getMessage_num() {
	return _Message_num;
}

/**
Returns _Message_Type
@return _Message_Type
*/
public String getMessage_Type() {
	return _Message_Type;
}

/**
Returns _Routine
@return _Routine
*/
public String getRoutine() {
	return _Routine;
}

/**
Sets _Date_Time
@param Date_Time value to set _Date_Time to
*/
public void setDate_Time(Date Date_Time) {
	_Date_Time = Date_Time;
}

/**
Sets _Message
@param Message value to set _Message to
*/
public void setMessage(String Message) {
	if ( Message != null ) {
		_Message = Message;
	}
}

/**
Sets _Message_Level
@param Message_Level value to set _Message_Level to
*/
public void setMessage_Level(long Message_Level) {
	_Message_Level = Message_Level;
}

/**
Sets _Message_num
@param Message_num value to set _Message_num to
*/
public void setMessage_num(long Message_num) {
	_Message_num = Message_num;
}

/**
Sets _Message_Type
@param Message_Type value to set _Message_Type to
*/
public void setMessage_Type(String Message_Type) {
	if ( Message_Type != null ) {
		_Message_Type = Message_Type;
	}
}

/**
Sets _Routine
@param Routine value to set _Routine to
*/
public void setRoutine(String Routine) {
	if ( Routine != null ) {
		_Routine = Routine;
	}
}

/** 
Returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_MessageLog{ " 		+ "\n" +
		"Date_time:    " + _Date_Time		+ "\n" +
		"Routine:      " + _Routine		+ "\n" +
		"Message_Type: " + _Message_Type	+ "\n" +
		"Message_Level:" + _Message_Level	+ "\n" +
		"Message:      " + _Message		+ "}";
}

}
