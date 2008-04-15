// ----------------------------------------------------------------------------
// RiversideDB_DataUnits
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-07-01	Steven A. Malers, RTi	Initial version.
// 2002-08-20	J. Thomas Sapienza, RTi	made toString() more descriptive
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// 2004-01-26	JTS, RTi		Added copy constructor.
// 2004-01-27	JTS, RTi		Added equals().
// 2004-02-02	JTS, RTi		Class is now cloneable.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import RTi.Util.String.StringUtil;

/**
class to store data from the DataUnits table
*/
public class RiversideDB_DataUnits 
extends DMIDataObject
implements Cloneable {

protected String _Units_abbrev = DMIUtil.MISSING_STRING; // Key
protected String _Units_description = DMIUtil.MISSING_STRING;
protected int _Output_precision = DMIUtil.MISSING_INT;
protected String _Dimension = DMIUtil.MISSING_STRING;
protected String _Base_unit = DMIUtil.MISSING_STRING;
protected double _Mult_factor = DMIUtil.MISSING_DOUBLE;
protected double _Add_factor = DMIUtil.MISSING_DOUBLE;
protected String _Units_system = DMIUtil.MISSING_STRING;

/** 
Constructor. 
*/
public RiversideDB_DataUnits()
{	super();
}

/**
Copy constructor.
*/
public RiversideDB_DataUnits(RiversideDB_DataUnits d) {
	super();
	setUnits_abbrev(new String(d.getUnits_abbrev()));
	setUnits_description(new String(d.getUnits_description()));
	setOutput_precision(d.getOutput_precision());
	setDimension(new String(d.getDimension()));
	setBase_unit(new String(d.getBase_unit()));
	setMult_factor(d.getMult_factor());
	setAdd_factor(d.getAdd_factor());
	setUnits_system(new String(d.getUnits_system()));
	setDirty(d.isDirty());
}

/**
Clones the data object.
@return a clone of the data object.
*/
public Object clone() 
throws CloneNotSupportedException {
	RiversideDB_DataUnits du = (RiversideDB_DataUnits)super.clone();
	return du;
}

/**
Clones the data object.
@return a clone of the data object.
*/
public RiversideDB_DataUnits cloneSelf() {
	RiversideDB_DataUnits du = null;
	try {
		du = (RiversideDB_DataUnits)clone();
	}
	catch (CloneNotSupportedException e) {
		du = new RiversideDB_DataUnits(this);
	}
	return du;
}

/**
Checks to see if this object has the same value as another
RiversideDB_DataUnits object.
@param o the object against which to compare
@return true if the objects have the same values, false if not.
*/
public boolean equals(Object o) {
	if (o == null) { 
		return false;
	}
	if (!(o instanceof RiversideDB_DataUnits)) {
		return false;
	}
	RiversideDB_DataUnits du = (RiversideDB_DataUnits)o;
	if (!StringUtil.stringsAreEqual(getUnits_abbrev(), 
		du.getUnits_abbrev())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getUnits_description(), 
		du.getUnits_description())) {
		return false;
	}
	if (getOutput_precision() != du.getOutput_precision()) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDimension(), du.getDimension())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getBase_unit(), du.getBase_unit())) {
		return false;
	}
	if (getMult_factor() != du.getMult_factor()) {
		return false;
	}
	if (getAdd_factor() != du.getAdd_factor()) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getUnits_system(), 
		du.getUnits_system())) {
		return false;
	}
	return true;
}

/**
Checks to see if this object has the same value as another
RiversideDB_DataUnits object.
@param du the RiversideDB_DataUnits object against which to compare this one.
@return true if the objects have the same values, false if not.
*/
public boolean equals(RiversideDB_DataUnits du) {
	if (du == null) {	
		return false;
	}
	if (!StringUtil.stringsAreEqual(getUnits_abbrev(), 
		du.getUnits_abbrev())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getUnits_description(), 
		du.getUnits_description())) {
		return false;
	}
	if (getOutput_precision() != du.getOutput_precision()) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDimension(), du.getDimension())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getBase_unit(), du.getBase_unit())) {
		return false;
	}
	if (getMult_factor() != du.getMult_factor()) {
		return false;
	}
	if (getAdd_factor() != du.getAdd_factor()) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getUnits_system(), 
		du.getUnits_system())) {
		return false;
	}
	return true;
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Units_abbrev = null;
	_Units_description = null;
	_Dimension = null;
	_Base_unit = null;
	_Units_system = null;
	super.finalize();
}

/**
returns _Add_factor
@return _Add_factor
*/
public double getAdd_factor() {
	return _Add_factor;
}

/**
returns _Base_unit
@return _Base_unit
*/
public String getBase_unit() {
	return _Base_unit;
}

/**
returns _Dimension
@return _Dimension
*/
public String getDimension() {
	return _Dimension;
}

/**
returns _Mult_factor
@return _Mult_factor
*/
public double getMult_factor() {
	return _Mult_factor;
}

/**
returns _Output_precision
@return _Output_precision
*/
public int getOutput_precision() {
	return _Output_precision;
}

/**
returns _Units_abbrev
@return _Units_abbrev
*/
public String getUnits_abbrev() {
	return _Units_abbrev;
}

/**
returns _Units_description
@return _Units_description
*/
public String getUnits_description() {
	return _Units_description;
}

/**
returns _Units_system
@return _Units_system
*/
public String getUnits_system() {
	return _Units_system;
}

/**
sets _Add_factor
@param Add_factor value to set _Add_factor to
*/
public void setAdd_factor(double Add_factor) {
	_Add_factor = Add_factor;
}

/**
sets _Base_unit
@param Base_unit value to set Base_unit to
*/
public void setBase_unit(String Base_unit) {
	if ( Base_unit != null ) {
		_Base_unit = Base_unit;
	}
}

/**
sets _Dimension
@param Dimension value to set Dimension to
*/
public void setDimension(String Dimension) {
	if ( Dimension != null ) {
		_Dimension = Dimension;
	}
}

/**
sets _Mult_factor
@param Mult_factor value to set _Mult_factor to
*/
public void setMult_factor(double Mult_factor) {
	_Mult_factor = Mult_factor;
}

/**
sets _Output_precision
@param Output_precision value to set _Output_precision to
*/
public void setOutput_precision(int Output_precision) {
	_Output_precision = Output_precision;
}

/**
sets _Units_abbrev
@param Units_abbrev value to set _Units_abbrev to
*/
public void setUnits_abbrev(String Units_abbrev) {
	if ( Units_abbrev != null ) {
		_Units_abbrev = Units_abbrev;
	}
}

/**
sets _Units_description
@param Units_description value to set _Units_description to
*/
public void setUnits_description(String Units_description) {
	if ( Units_description != null ) {
		_Units_description = Units_description;
	}
}

/**
sets _Units_system
@param Units_system value to set _Units_system to
*/
public void setUnits_system(String Units_system) {
	if ( Units_system != null ) {
		_Units_system = Units_system;
	}
}

/** 
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_DataUnits{ " 			+ "\n" + 
		"Units_abbrev:      " + _Units_abbrev		+ "\n" +
		"Units_description: " + _Units_description	+ "\n" +
		"Output_precision:  " + _Output_precision	+ "\n" +
		"Dimension:         " + _Dimension		+ "\n" +
		"Base_unit:         " + _Base_unit		+ "\n" +
		"Mult_factor:       " + _Mult_factor		+ "\n" +
		"Add_factor:        " + _Add_factor		+ "\n" +
		"Units_system:      " + _Units_system		+ "}";
}

} // End RiversideDB_DataUnits
