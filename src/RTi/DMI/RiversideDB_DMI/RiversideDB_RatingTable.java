// ----------------------------------------------------------------------------
// RiversideDB_RatingTable
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-07-02	Steven A. Malers, RTi	Initial version.
// 2002-08-20	J. Thomas Sapienza, RTi	Made toString() more descriptive and
//					corrected an error in variable naming
//					in it
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

/**
class to store data from the RatingTable table
*/
public class RiversideDB_RatingTable extends DMIDataObject
{

protected long _RatingTable_num = DMIUtil.MISSING_LONG;
protected double _Value1 = DMIUtil.MISSING_DOUBLE;
protected double _Value2 = DMIUtil.MISSING_DOUBLE;
protected double _Value3 = DMIUtil.MISSING_DOUBLE;
protected double _Shift1 = DMIUtil.MISSING_DOUBLE;
protected double _Shift2 = DMIUtil.MISSING_DOUBLE;
protected double _Shift3 = DMIUtil.MISSING_DOUBLE;

/**
constructor.  
*/
public RiversideDB_RatingTable()
{	super();
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	super.finalize();
}

/**
returns _Shift1

@return _Shift1
*/
public double getShift1() {
	return _Shift1;
}

/**
returns _Shift2

@return _Shift2
*/
public double getShift2() {
	return _Shift2;
}

/**
returns _Shift3

@return _Shift3
*/
public double getShift3() {
	return _Shift3;
}

/**
returns _RatingTable_num

@return _RatingTable_num
*/
public long getRatingTable_num() {
	return _RatingTable_num;
}

/**
returns _Value1

@return _Value1
*/
public double getValue1() {
	return _Value1;
}

/**
returns _Value2

@return _Value2
*/
public double getValue2() {
	return _Value2;
}

/**
returns _Value3

@return _Value3
*/
public double getValue3() {
	return _Value3;
}

/**
sets _Shift1

@param Shift1 value to set _Shift1 to
*/
public void setShift1 (double Shift1) {
	_Shift1 = Shift1;
}

/**
sets _Shift2

@param Shift2 value to set _Shift2 to
*/
public void setShift2 (double Shift2) {
	_Shift2 = Shift2;


}

/**
sets _Shift3

@param Shift3 value to set _Shift3 to
*/
public void setShift3 (double Shift3) {
	_Shift3 = Shift3;
}

/**
sets _RatingTable_num

@param RatingTable_num value to set _RatingTable_num to
*/
public void setRatingTable_num (long RatingTable_num) {
	_RatingTable_num = RatingTable_num;
}

/**
sets _Value1

@param Value1 value to set _Value1 to
*/
public void setValue1 (double Value1) {
	_Value1 = Value1;
}

/**
sets _Value2

@param Value2 value to set _Value2 to
*/
public void setValue2 (double Value2) {
	_Value2 = Value2;


}

/**
sets _Value3

@param Value3 value to set _Value3 to
*/
public void setValue3 (double Value3) {
	_Value3 = Value3;
}

/** 
returns a string representation of this object

@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_RatingTable{" 		+ "\n" + 
		"RatingTable_num:" + _RatingTable_num 	+ "\n" +
		"Value1:         " + _Value1		+ "\n" +
		"Value2:         " + _Value2		+ "\n" +
		"Shift1:         " + _Shift1		+ "\n" +
		"Shift2:         " + _Shift2		+ "}";
}

} // End RiversideDB_RatingTable
