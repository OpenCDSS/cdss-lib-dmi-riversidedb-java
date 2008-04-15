// ----------------------------------------------------------------------------
// RiversideDB_AutoUpdateProductMeasType - class to store information from the TSProduct
//	table
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
// 2002-06-02	J. Thomas Sapienza, RTi	Initial version.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

/**
Class to store data from the Area table
*/
public class RiversideDB_AutoUpdateProductMeasType extends DMIDataObject
{

protected int _AutoUpdateProduct_num = DMIUtil.MISSING_INT;
protected int _MeasType_num = DMIUtil.MISSING_INT;
protected int _Sequence = DMIUtil.MISSING_INT;

/**
Returns _AutoUpdateProduct_num
@return _AutoUpdateProduct_num
*/
public int getAutoUpdateProduct_num() {
	return _AutoUpdateProduct_num;
}

/**
Returns _MeasType_num
@return _MeasType_num
*/
public int getMeasType_num() {
	return _MeasType_num;
}

/**
Returns _Sequence
@return _Sequence
*/
public int getSequence() {
	return _Sequence;
}

/**
Sets _AutoUpdateProduct_num
@param AutoUpdateProduct_num value to put into _AutoUpdateProduct_num
*/
public void setAutoUpdateProduct_num(int AutoUpdateProduct_num) {
	 _AutoUpdateProduct_num = AutoUpdateProduct_num;
}

/**
Sets _MeasType_num
@param MeasType_num value to put into _MeasType_num
*/
public void setMeasType_num(int MeasType_num) {
	_MeasType_num = MeasType_num;
}

/**
Sets _Sequence
@param Sequence value to put into _Sequence
*/
public void setSequence(int Sequence) {
	_Sequence = Sequence;
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	super.finalize();
}

/** 
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return "RiversideDB_AutoUpdateProductMeasType{" 	+ "\n" + 
		"AutoUpdateProduct_num: " + _AutoUpdateProduct_num + "\n" +
		"MeasType_num:          " + _MeasType_num + "\n" +
		"Sequence:              " + _Sequence + "\n}\n";
}

}
