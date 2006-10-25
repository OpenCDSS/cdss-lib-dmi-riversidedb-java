// ----------------------------------------------------------------------------
// RiversideDB_State
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-05-29	J. Thomas Sapienza, RTi	Initial version.
// 2002-06-27	Steven A. Malers, RTi	Remove all I/O code.
// 2002-08-20	JTS, RTi		Made toString() more descriptive
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// 2004-09-28	JTS, RTi		Added fields for DB ver 3.00.00		
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMI;
import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import java.util.Vector;

/**
Class to store data from the State table.
*/
public class RiversideDB_State 
extends DMIDataObject {

protected long _StateGroup_num = DMIUtil.MISSING_LONG;

// class variables for RiverTrak version 2.08.00
protected String _Module = DMIUtil.MISSING_STRING;
protected String _Variable = DMIUtil.MISSING_STRING;
protected String _Val = DMIUtil.MISSING_STRING;
protected int _Seq = DMIUtil.MISSING_INT;

// class variables for RiverTrak version 3.00.00
protected int _OperationStateRelation_num = 	DMIUtil.MISSING_INT;
protected int _Sequence = 			DMIUtil.MISSING_INT;
protected String _ValueStr =	 		DMIUtil.MISSING_STRING;

// used in calibration work
private String __stateName = null;
private boolean __isDefault = false;

/** 
Constructor. 
*/
public RiversideDB_State() {
	super();
}

/**
Cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() 
throws Throwable {
	_Module = null;
	_Variable = null;
	_Val = null;

	_ValueStr = null;
	
	super.finalize();
}

/**
/**
Returns _Module
@return _Module
*/
public String getModule() {
	return _Module;
}

/** 
Returns _OperationStateRelation_num
@return _OperationStateRelation_num
*/
public int getOperationStateRelation_num() {
	return _OperationStateRelation_num;
}

/**
Returns _Seq
@return _Seq
*/
public int getSeq() {
	return _Seq;
}

/**
Returns _Sequence
@return _Sequence
*/
public int getSequence() {
	return _Sequence;
}

/**
Returns _StateGroup_num
@return _StateGroup_num
*/
public long getStateGroup_num() {
	return _StateGroup_num;
}

/**
Returns the state name.
@return the state name.
*/
public String getStateName() {
	return __stateName;
}

/**
Returns _Val
@return _Val
*/
public String getVal() {
	return _Val;
}

/**
Returns _ValueStr
@return _ValueStr
*/
public String getValueStr() {
	return _ValueStr;
}

/**
Returns _Variable
@return _Variable
*/
public String getVariable() {
	return _Variable;
}

/**
Returns whether this state is a default value.
@return whether this state is a default value.
*/
public boolean isDefault() {
	return __isDefault;
}

/**
Sets whether this state is a default value.
@param isDefault if true, this state is a default value.
*/
public void setIsDefault(boolean isDefault) {
	__isDefault = isDefault;
}

/**
Sets _Module
@param Module value to set _Module to
*/
public void setModule(String Module) {
	if (Module != null) {
		_Module = Module;
	}
}

/**
Sets _OperationStateRelation_num
@param OperationStateRelation_num value to put into _OperationStateRelation_num
*/
public void setOperationStateRelation_num(int OperationStateRelation_num) {
	_OperationStateRelation_num = OperationStateRelation_num;
}

/**
Sets _Seq
@param Seq value to set _Seq to
*/
public void setSeq(int Seq) {
	_Seq = Seq;
}

/**
Sets _Sequence
@param Sequence value to set _Sequence to
*/
public void setSequence(int Sequence) {
	_Sequence = Sequence;
}

/**
Sets _StateGroup_num
@param StateGroup_num value to set StateGroup_num to
*/
public void setStateGroup_num(long StateGroup_num) {
	_StateGroup_num = StateGroup_num;
}

/**
Sets the state name.
@param stateName the state name.
*/
public void setStateName(String stateName) {
	__stateName = stateName;
}

/**
Sets _Variable
@param Variable value to set _Variable to
*/
public void setVariable(String Variable) {
	if (Variable != null) {
		_Variable = Variable;
	}
}

/**
Sets _Val
@param Val value to set _Val to
*/
public void setVal(String Val) {
	if (Val != null) {
		_Val = Val;
	}
}

/**
Sets _ValueStr
@param ValueStr value to set _ValueStr to
*/
public void setValueStr(String ValueStr) {
	_ValueStr = ValueStr;
}

/** 
Returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_State{ " 			+ "\n" + 
		"Database version 2.08.00: \n" + 
		"------------------------- \n" +
		"Module:        " + _Module		+ "\n" +
		"Variable:      " + _Variable		+ "\n" +
		"Val:           " + _Val		+ "\n" +
		"Seq:           " + _Seq		+ "\n" +
		"StateGroup_num:" + _StateGroup_num	+ "\n" + 
		"Database version 3.00.00: \n" + 
		"------------------------- \n" +
		"StateGroup_num:             " + _StateGroup_num + "\n" +
		"OperationStateRelation_num: " + _OperationStateRelation_num
			+ "\n" +
		"Sequence:                   " + _Sequence 	+ "\n" +
		"ValueStr:                  '" + _ValueStr	+ "'\n}\n";
}

}
