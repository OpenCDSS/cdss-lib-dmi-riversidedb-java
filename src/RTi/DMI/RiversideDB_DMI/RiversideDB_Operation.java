// ----------------------------------------------------------------------------
// RiversideDB_Operation - corresponds to RiversideDB Operation table
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2004-09-28	J. Thomas Sapienza, RTi	Initial version.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

/**
Class to store data from the Operation table, introduced in RiversideDB 
version 3.0.0.
*/
public class RiversideDB_Operation 
extends DMIDataObject {

// From table Operation

protected int _Operation_num = 		DMIUtil.MISSING_INT;
protected int _MeasLocGroup_num = 	DMIUtil.MISSING_INT;
protected int _Sequence = 		DMIUtil.MISSING_INT;
protected String _Operation_ID = 	DMIUtil.MISSING_STRING;
protected String _Operation_type = 	DMIUtil.MISSING_STRING;
protected float _x = 			DMIUtil.MISSING_FLOAT;
protected float _y = 			DMIUtil.MISSING_FLOAT;

/**
RiversideDB_Operation constructor.
*/
public RiversideDB_Operation () {	
	super();
}

/**
Copy constructor.
@param o the RiversideDB_Operation object to copy.
*/
public RiversideDB_Operation(RiversideDB_Operation o) {
	super();
	setOperation_num(o.getOperation_num());
	setMeasLocGroup_num(o.getMeasLocGroup_num());
	setSequence(o.getSequence());
	setOperation_ID(new String(o.getOperation_ID()));
	setOperation_type(new String(o.getOperation_type()));
	setX(o.getX());
	setY(o.getY());
	
	setDirty(o.isDirty());
}

/**
Cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null.
*/
protected void finalize() 
throws Throwable {
	_Operation_ID = null;
	_Operation_type = null;
	super.finalize();
}

/**
Returns _MeasLocGroup_num
@return _MeasLocGroup_num
*/
public int getMeasLocGroup_num() {
	return _MeasLocGroup_num;
}

/**
Returns _Operation_ID
@return _Operation_ID
*/
public String getOperation_ID() {
	return _Operation_ID;
}

/**
Returns _Operation_num
@return _Operation_num
*/
public int getOperation_num() {
	return _Operation_num;
}

/**
Returns _Operation_type
@return _Operation_type
*/
public String getOperation_type() {
	return _Operation_type;
}

/**
Returns _Sequence
@return _Sequence
*/
public int getSequence() {
	return _Sequence;
}

/**
Returns _x
@return _x
*/
public float getX() {
	return _x;
}

/**
Returns _y
@return _y
*/
public float getY() {
	return _y;
}

/**
Sets _MeasLocGroup_num
@param MeasLocGroup_num the value to put into _MeasLocGroup_num.
*/
public void setMeasLocGroup_num(int MeasLocGroup_num) {
	_MeasLocGroup_num = MeasLocGroup_num;
}

/**
Sets _Operation_ID
@param Operation_ID the value to put into _Operation_ID.
*/
public void setOperation_ID(String Operation_ID) {
	_Operation_ID = Operation_ID;
}

/**
Sets _Operation_num
@param Operation_num the value to put into _Operation_num.
*/
public void setOperation_num(int Operation_num) {
	_Operation_num = Operation_num;
}

/**
Sets _Operation_type
@param Operation_type the value to put into _Operation_type.
*/
public void setOperation_type(String Operation_type) {
	_Operation_type = Operation_type;
}

/**
Sets _Sequence
@param Sequence the value to put into _Sequence.
*/
public void setSequence(int Sequence) {
	_Sequence = Sequence;
}

/**
Sets _x
@param x the value to put into _x.
*/
public void setX(float x) {
	_x = x;
}

/**
Sets _y
@param y the value to put into _y.
*/
public void setY(float y) {
	_y = y;
}

/** 
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_Operation{" 			+ "\n" +
		"Operation_num:    " + _Operation_num 		+ "\n" + 
		"MeasLocGroup_num: " + _MeasLocGroup_num	+ "\n" +
		"Sequence:         " + _Sequence 		+ "\n" + 
		"Operation_ID:    '" + _Operation_ID		+ "'\n" + 
		"Operation_type:  '" + _Operation_type		+ "'\n" +
		"x:                " + _x			+ "\n" +
		"y:                " + _y			+ "\n}\n";
}

}
