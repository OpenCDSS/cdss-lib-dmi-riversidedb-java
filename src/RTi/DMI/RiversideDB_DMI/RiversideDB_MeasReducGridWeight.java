// ----------------------------------------------------------------------------
// RiversideDB_MeasReducGridWeight
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-07-01	Steven A. Malers, RTi	Initial version.
// 2002-08-20	J. Thomas Sapienza, RTi	Made toString() more descriptive
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMI;
import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

/**
class to store data from the MeasReducGridWeight table
*/
public class RiversideDB_MeasReducGridWeight extends DMIDataObject
{

protected long _OutputMeasType_num = DMIUtil.MISSING_LONG;
protected long _InputMeasType_num = DMIUtil.MISSING_LONG;
protected long _Input_Row = DMIUtil.MISSING_LONG;
protected long _Input_Column = DMIUtil.MISSING_LONG;
protected double _Area = DMIUtil.MISSING_DOUBLE;
protected double _Area_Fraction = DMIUtil.MISSING_DOUBLE;
protected double _Weight = DMIUtil.MISSING_DOUBLE;

/** 
Constructor. 
*/
public RiversideDB_MeasReducGridWeight() {
	super();
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	super.finalize();
}

/**
returns _Area

@return _Area
*/
public double getArea() {
	return _Area;
}

/**
returns _Area_Fraction

@return _Area_Fraction
*/
public double getArea_Fraction() {
	return _Area_Fraction;
}

/**
returns _Input_Column

@return _Input_Column
*/
public long getInput_Column() {
	return _Input_Column;
}

/**
returns _InputMeasType_num

@return _InputMeasType_num
*/
public long getInputMeasType_num() {
	return _InputMeasType_num;
}

/**
returns _Input_Row

@return _Input_Row
*/
public long getInput_Row() {
	return _Input_Row;
}

/**
returns _OutputMeasType_num

@return _OutputMeasType_num
*/
public long getOutputMeasType_num() {
	return _OutputMeasType_num;
}

/**
returns _Weight

@return _Weight
*/
public double getWeight() {
	return _Weight;
}

/**
sets _Area

@param Area value to set _Area to
*/
public void setArea(double Area) {
	_Area = Area;
}

/**
sets _Area_Fraction

@param Area_Fraction value to set _Area_Fraction to
*/
public void setArea_Fraction(double Area_Fraction) {
	_Area_Fraction = Area_Fraction;
}

/**
sets _Input_Column

@param Input_Column value to set Input_Column to
*/
public void setInput_Column(long Input_Column) {
	_Input_Column = Input_Column;
}

/**
sets _InputMeasType_num

@param InputMeasType_num value to set InputMeasType_num to
*/
public void setInputMeasType_num(long InputMeasType_num) {
	_InputMeasType_num = InputMeasType_num;
}

/**
sets _Input_Row

@param Input_Row value to set Input_Row to
*/
public void setInput_Row(long Input_Row) {
	_Input_Row = Input_Row;
}

/**
sets _OutputMeasType_num

@param OutputMeasType_num value to set OutputMeasType_num to
*/
public void setOutputMeasType_num(long OutputMeasType_num) {
	_OutputMeasType_num = OutputMeasType_num;
}

/**
sets _Weight

@param Weight value to set _Weight to
*/
public void setWeight(double Weight) {
	_Weight = Weight;
}

/** 
returns a string representation of this object

@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_MeasReducGridWeight { " 		+ "\n" + 
		"OutputMeasType_num:" + _OutputMeasType_num	+ "\n" +
		"InputMeasType_num: " + _InputMeasType_num	+ "\n" +
		"Input_Row:    "      + _Input_Row		+ "\n" +
		"Input_Column: "      + _Input_Column		+ "\n" +
		"Area:         "      + _Area			+ "\n" +
		"Area_Fraction:"      + _Area_Fraction		+ "\n" +
		"Weight:       "      + _Weight			+ "}";
}

} // End RiversideDB_MeasReducGridWeight 
