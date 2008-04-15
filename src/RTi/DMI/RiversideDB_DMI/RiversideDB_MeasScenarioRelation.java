// ----------------------------------------------------------------------------
// RiversideDB_MeasScenarioRelation
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

import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

/**
class to store data from the MeasScenarioRelation table
*/
public class RiversideDB_MeasScenarioRelation extends DMIDataObject
{

protected long _ObsMeasType_num = DMIUtil.MISSING_LONG;
protected long _QFMeasType_num = DMIUtil.MISSING_LONG;
protected long _ScenarioMeasType_num = DMIUtil.MISSING_LONG;
protected double _Weight = DMIUtil.MISSING_DOUBLE;

/** 
Constructor. 
*/
public RiversideDB_MeasScenarioRelation() {
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
returns _ObsMeasType_num

@return _ObsMeasType_num
*/
public long getObsMeasType_num() {
	return _ObsMeasType_num;
}

/**
returns _QFMeasType_num

@return _QFMeasType_num
*/
public long getQFMeasType_num() {
	return _QFMeasType_num;
}

/**
returns _ScenarioMeasType_num

@return _ScenarioMeasType_num
*/
public long getScenarioMeasType_num() {
	return _ScenarioMeasType_num;
}

/**
returns _Weight

@return _Weight
*/
public double getWeight() {
	return _Weight;
}

/**
sets _ObsMeasType_num

@param ObsMeasType_num value to set ObsMeasType_num to
*/
public void setObsMeasType_num(long ObsMeasType_num) {
	_ObsMeasType_num = ObsMeasType_num;
}

/**
sets _QFMeasType_num

@param QFMeasType_num value to set QFMeasType_num to
*/
public void setQFMeasType_num(long QFMeasType_num) {
	_QFMeasType_num = QFMeasType_num;
}

/**
sets _ScenarioMeasType_num

@param ScenarioMeasType_num value to set ScenarioMeasType_num to
*/
public void setScenarioMeasType_num(long ScenarioMeasType_num) {
	_ScenarioMeasType_num = ScenarioMeasType_num;
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
	return  "RiversideDB_MeasScenarioRelation{ " 		+ "\n" +
		"ObsMeasType_num:     " + _ObsMeasType_num	+ "\n" +
		"QFMeasType_num:      " + _QFMeasType_num	+ "\n" +
		"Weight:              " + _Weight		+ "\n" +
		"ScenarioMeasType_num:" + _ScenarioMeasType_num	+ "}";
}

} // End RiversideDB_MeasScenarioRelation
