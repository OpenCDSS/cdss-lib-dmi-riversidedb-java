// ----------------------------------------------------------------------------
// RiversideDB_MeasTypeStats
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

import RTi.DMI.DMI;
import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import java.util.Date;

/**
class to store data from the MeasTypeStats table
*/
public class RiversideDB_MeasTypeStats extends DMIDataObject
{

protected long _MeasType_num = DMIUtil.MISSING_LONG;
protected Date _Start_date = DMIUtil.MISSING_DATE;
protected Date _End_date = DMIUtil.MISSING_DATE;
protected Date _First_date_of_last_edit = DMIUtil.MISSING_DATE;
protected long _Meas_count = DMIUtil.MISSING_LONG;
protected double _Min_val = DMIUtil.MISSING_DOUBLE;
protected double _Max_val = DMIUtil.MISSING_DOUBLE;

/** 
Constructor. 
*/
public RiversideDB_MeasTypeStats() {
	super ();
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Start_date = null;
	_End_date = null;
	_First_date_of_last_edit = null;
	super.finalize();
}

/**
returns _End_date

@return _End_date
*/
public Date getEnd_date() {
	return _End_date;
}

/**
returns _First_date_of_last_edit

@return _First_date_of_last_edit
*/
public Date getFirst_date_of_last_edit() {
	return _First_date_of_last_edit;
}

/**
returns _Max_val

@return _Max_val
*/
public double getMax_val() {
	return _Max_val;
}

/**
returns _Meas_count

@return _Meas_count
*/
public long getMeas_count() {
	return _Meas_count;
}

/**
returns _MeasType_num

@return _MeasType_num
*/
public long getMeasType_num() {
	return _MeasType_num;
}

/**
returns _Min_val

@return _Min_val
*/
public double getMin_val() {
	return _Min_val;
}

/**
returns _Start_date

@return _Start_date
*/
public Date getStart_date() {
	return _Start_date;
}

/**
sets _End_date

@param End_date value to set _End_date to
*/
public void setEnd_date(Date End_date) {
	_End_date = End_date;
}

/**
sets _First_date_of_last_edit

@param First_date_of_last_edit value to set _First_date_of_last_edit to
*/
public void setFirst_date_of_last_edit(Date First_date_of_last_edit) {
	_First_date_of_last_edit = First_date_of_last_edit;
}

/**
sets _Max_val

@param Max_val value to set _Max_val to
*/
public void setMax_val(double Max_val) {
	_Max_val = Max_val;
}

/**
sets _Meas_count

@param Meas_count value to set _Meas_count to
*/
public void setMeas_count(long Meas_count) {
	_Meas_count = Meas_count;
}

/**
sets _MeasType_num

@param MeasType_num value to set _MeasType_num to
*/
public void setMeasType_num(long MeasType_num) {
	_MeasType_num = MeasType_num;
}

/**
sets _Min_val

@param Min_val value to set _Min_val to
*/
public void setMin_val(double Min_val) {
	_Min_val = Min_val;
}

/**
sets _Start_date

@param Start_date value to set _Start_date to
*/
public void setStart_date(Date Start_date) {
	_Start_date = Start_date;
}

/** 
returns a string representation of this object

@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_MeasTypeStats{ " 				+ "\n" +
		"MeasType_num:           " + _MeasType_num		+ "\n" +
		"Start_date:             " + _Start_date		+ "\n" +
		"End_date:               " + _End_date			+ "\n" +
		"First_date_of_last_edit:" + _First_date_of_last_edit	+ "\n" +
		"Meas_count:             " + _Meas_count		+ "\n" +
		"Min_val:                " + _Min_val			+ "\n" +
		"Max_val:                " + _Max_val			+ "}";
}

} // End RiversideDB_MeasTypeStats
