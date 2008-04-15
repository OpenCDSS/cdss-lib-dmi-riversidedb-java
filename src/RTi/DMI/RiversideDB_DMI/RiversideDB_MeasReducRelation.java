// ----------------------------------------------------------------------------
// RiversideDB_MeasReducRelation
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-06-28	Steven A. Malers, RTi	Initial version.
// 2002-08-20	J. Thomas Sapienza, RTi	Made toString() more descriptive
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// 2003-05-13	JTS, RTi		Added copy constructor.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

/**
class to store data from the MeasReducRelation table
*/
public class RiversideDB_MeasReducRelation extends DMIDataObject
{

protected long _OutputMeasType_num = DMIUtil.MISSING_LONG;
protected long _InputMeasType_num = DMIUtil.MISSING_LONG;
protected double _Weight = DMIUtil.MISSING_DOUBLE;

/**
Copy constructor.
*/
public RiversideDB_MeasReducRelation(RiversideDB_MeasReducRelation m) {
	setOutputMeasType_num(m.getOutputMeasType_num());
	setInputMeasType_num(m.getInputMeasType_num());
	setWeight(m.getWeight());

	setDirty(m.isDirty());
}

/** 
Constructor. 
*/
public RiversideDB_MeasReducRelation() {
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
returns _InputMeasType_num

@return _InputMeasType_num
*/
public long getInputMeasType_num() {
	return _InputMeasType_num;
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
sets _InputMeasType_num

@param InputMeasType_num value to set InputMeasType_num to
*/
public void setInputMeasType_num(long InputMeasType_num) {
	_InputMeasType_num = InputMeasType_num;
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
	return  "RiversideDB_MeasReducRelation{ " 		+ "\n" +
		"OutputMeasType_num:" + _OutputMeasType_num	+ "\n" +
		"InputMeasType_num: " + _InputMeasType_num	+ "\n" +
		"Weight:            " + _Weight			+ "\n}";
}

} // End RiversideDB_MeasReducRelation
