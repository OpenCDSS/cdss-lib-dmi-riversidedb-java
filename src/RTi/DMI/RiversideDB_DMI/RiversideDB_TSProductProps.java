
// ----------------------------------------------------------------------------
// RiversideDB_TSProductProps - class to store information from the TSProduct
//	table
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
// 2002-06-02	J. Thomas Sapienza, RTi	Initial version.
// 2005-01-04 Luiz Teixeira, RTi	Added the _Val member used under
//          				_VERSION_030000_20041001 and the 
//					methods getVal and setVal.
//					This member and methods replace the pre
//					03.00.00 _Value and methods getValue
//					and setValue.  Some cleanup...
// ----------------------------------------------------------------------------
package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

/**
Class to store data from the Area table
*/
public class RiversideDB_TSProductProps extends DMIDataObject
{

protected int    _TSProduct_num = DMIUtil.MISSING_INT;
protected String _Property      = DMIUtil.MISSING_STRING;
protected String _Value         = DMIUtil.MISSING_STRING;        // pre 03.00.00
protected String _Val           = DMIUtil.MISSING_STRING;        //     03.00.00
protected int    _Sequence      = DMIUtil.MISSING_INT;

/**
RiversideDB_TSProductProps constructor.
*/
public RiversideDB_TSProductProps ()
{	super();
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Property = null;
	_Value    = null;                                        // pre 03.00.00
	_Val      = null;                                        //     03.00.00
	super.finalize();
}

//   Get snd set methods presented in the order the members appear in the table.
// -----------------------------------------------------------------------------

/**
Returns _TSProduct_num
@return _TSProduct_num
*/
public int getTSProduct_num() {
	return _TSProduct_num;
}

/**
Returns _Property
@return _Property
*/
public String getProperty() {
	return _Property;
}

/**
Returns _Sequence
@return _Sequence
*/
public int getSequence() {
	return _Sequence;
}

/**
Returns _Value ( pre 03.00.00 )
@return _Value
*/
public String getValue() {
	return _Value;
}

/**
Returns _Val ( 03.00.00 )
@return _Val
*/
public String getVal() {
	return _Val;
}

/**
Sets _TSProduct_num
@param TSProduct_num value to put into _TSProduct_num
*/
public void setTSProduct_num(int TSProduct_num) {
	 _TSProduct_num = TSProduct_num;
}

/**
Sets _Property
@param Property value to put into _Property
*/
public void setProperty(String Property) {
	 _Property = Property;
}

/**
Sets _Sequence
@param Sequence value to put into _Identifier
*/
public void setSequence(int Sequence) {
	 _Sequence = Sequence;
}

/**
Sets _Value ( pre 03.00.00 )
@param Value value to put into _Value
*/
public void setValue(String Value) {
	 _Value = Value;
}

/**
Sets _Val ( 03.00.00 )
@param Val value to put into _Val
*/
public void setVal(String Val) {
	 _Val = Val;
}

/** 
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return "RiversideDB_TSProductProps{"        + "\n" + 
		"TSProduct_num:         " + _TSProduct_num + " \n" +
		"Property:             '" + _Property      + "'\n" + 
		"Value (pre 03.00.00): '" + _Value         + "'\n" + 
		"Value:    (03.00.00): '" + _Val           + "'\n" + 
		"Sequence:              " + _Sequence      + " }";
}

}
