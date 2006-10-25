// ----------------------------------------------------------------------------
// RiversideDB_StateGroup
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-05-29	J. Thomas Sapienza, RTi	Initial version.
// 2002-06-27	Steven A. Malers, RTi	Remove I/O code.
// 2002-07-11	JTS, RTi		Changed "Date_time" to "Date_Time" so
//					that it matched the database
// 2002-08-20	JTS, RTi		Made toString() more descriptive
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// 2003-10-29	JTS, RTi		Added MeasLocGroup_num
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMI;
import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import java.util.Date;

/**
class to store data from the StateGroup table
*/
public class RiversideDB_StateGroup extends DMIDataObject {

protected long _StateGroup_num = DMIUtil.MISSING_LONG;
protected String _Scenario = DMIUtil.MISSING_STRING;
protected Date _Date_Time = DMIUtil.MISSING_DATE;
protected String _Description = DMIUtil.MISSING_STRING;
protected long _Status = DMIUtil.MISSING_LONG;
protected long _MeasLocGroup_num = DMIUtil.MISSING_LONG;

/** 
Constructor. 
*/
public RiversideDB_StateGroup() {
	super ();
}

/**
Cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Scenario = null;
	_Date_Time = null;
	_Description = null;

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
Returns _Description
@return _Description
*/
public String getDescription() {
	return _Description;
}

/**
Returns _MeasLocGroup_num
@return _MeasLocGroup_num
*/
public long getMeasLocGroup_num() {
	return _MeasLocGroup_num;
}

/**
Returns _Scenario
@return _Scenario
*/
public String getScenario() {
	return _Scenario;
}

/**
Returns _Status
@return _Status
*/
public long getStatus() {
	return _Status;
}

/**
Returns _StateGroup_num
@return _StateGroup_num
*/
public long getStateGroup_num() {
	return _StateGroup_num;
}

/**
Sets _Date_Time
@param Date_Time value to set _Date_Time to
*/
public void setDate_Time(Date Date_Time) {
	_Date_Time = Date_Time;
}

/**
Sets _Description
@param Description value to set _Description to
*/
public void setDescription(String Description) {
	if ( Description != null ) {
		_Description = Description;
	}
}

/**
Sets _MeasLocGroup_num
@param MeasLocGroup_num value to set _MeasLocGroup_num to
*/
public void setMeasLocGroup_num(long MeasLocGroup_num) {
	_MeasLocGroup_num = MeasLocGroup_num;
}

/**
Sets _Status
@param Status value to set _Status to
*/
public void setStatus(long Status) {
	_Status = Status;
}

/**
Sets _Scenario
@param Scenario value to set _Scenario to
*/
public void setScenario(String Scenario) {
	if ( Scenario != null ) {
		_Scenario = Scenario;
	}
}

/**
Sets StateGroup_num
@param StateGroup_num
*/
public void setStateGroup_num(long StateGroup_num) {
	_StateGroup_num = StateGroup_num;
}

/** 
Returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_StateGroup{ " 		+ "\n" + 
		"StateGroup_num:   " + _StateGroup_num	+ "\n" +
		"Scenario:         " + _Scenario	+ "\n" +
		"Date_Time:        " + _Date_Time	+ "\n" +
		"Description:      " + _Description	+ "\n" +
		"Status:           " + _Status		+ "\n" +
		"MeasLocGroup_num: " + _MeasLocGroup_num + "\n}\n";
}

}
