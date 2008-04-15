// ----------------------------------------------------------------------------
// RiversideDB_OperationStateRelation - corresponds to RiversideDB 
//	OperationStateRelation table
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
Class to store data from the OperationStateRelation table, introduced in 
RiversideDB version 3.0.0.
*/
public class RiversideDB_OperationStateRelation 
extends DMIDataObject {

// From table OperationStateRelation

protected int _OperationStateRelation_num = 	DMIUtil.MISSING_INT;
protected int _Operation_num = 			DMIUtil.MISSING_INT;
protected String _State_name = 			DMIUtil.MISSING_STRING;
protected String _Default_value = 		DMIUtil.MISSING_STRING;

/**
RiversideDB_OperationStateRelation constructor.
*/
public RiversideDB_OperationStateRelation () {	
	super();
}

/**
Copy constructor.
@param o the RiversideDB_OperationStateRelation object to copy.
*/
public RiversideDB_OperationStateRelation(RiversideDB_OperationStateRelation o){
	super();
	setOperationStateRelation_num(o.getOperationStateRelation_num());
	setOperation_num(o.getOperation_num());
	setState_name(new String(o.getState_name()));
	setDefault_value(new String(o.getDefault_value()));
	
	setDirty(o.isDirty());
}

/**
Cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null.
*/
protected void finalize() 
throws Throwable {
	_State_name = null;
	_Default_value = null;
	super.finalize();
}

/**
Returns _Default_value
@return _Default_value
*/
public String getDefault_value() {
	return _Default_value;
}

/**
Returns _Operation_num
@return _Operation_num
*/
public int getOperation_num() {
	return _Operation_num;
}

/**
Returns _OperationStateRelation_num
@return _OperationStateRelation_num
*/
public int getOperationStateRelation_num() {
	return _OperationStateRelation_num;
}

/**
Returns _State_name
@return _State_name
*/
public String getState_name() {
	return _State_name;
}

/**
Sets _Default_value
@param Default_value value to put into _Default_value
*/
public void setDefault_value(String Default_value) {
	_Default_value = Default_value;
}

/**
Sets _Operation_num
@param Operation_num the value to put into _Operation_num.
*/
public void setOperation_num(int Operation_num) {
	_Operation_num = Operation_num;
}

/**
Sets _OperationStateRelation_num
@param OperationStateRelation_num value to put into _OperationStateRelation_num
*/
public void setOperationStateRelation_num(int OperationStateRelation_num) {
	_OperationStateRelation_num = OperationStateRelation_num;
}

/**
Sets _State_name
@param State_name value to put into _State_name
*/
public void setState_name(String State_name) {
	_State_name = State_name;
}

/** 
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_OperationStateRelation{" 		+ "\n" +
		"OperationStateRelation_num: " + _OperationStateRelation_num
			+ "\n" + 
		"Operation_num:              " + _Operation_num	+ "\n" +
		"State_name:                '" + _State_name 	+ "'\n" +
		"Default_value:             '" + _Default_value	+ "'\n}\n";
}

}
