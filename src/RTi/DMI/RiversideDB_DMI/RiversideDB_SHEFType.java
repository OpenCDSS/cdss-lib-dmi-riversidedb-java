// ----------------------------------------------------------------------------
// RiversideDB_SHEFType
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-07-01	Steven A. Malers, RTi	Initial version.
// 2002-08-20	J. Thomas Sapienza, RTI	made toString() more descriptive
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
class to store data from the SHEFType table
*/
public class RiversideDB_SHEFType 
extends DMIDataObject
implements Cloneable {

protected String _SHEF_pe      = DMIUtil.MISSING_STRING; // Key
protected String _Units_engl   = DMIUtil.MISSING_STRING;
protected String _Units_si     = DMIUtil.MISSING_STRING;
protected String _Default_base = DMIUtil.MISSING_STRING;
protected long   _Default_mult = DMIUtil.MISSING_LONG;
protected String _Time_scale   = DMIUtil.MISSING_STRING;

/** 
Constructor. 
*/
public RiversideDB_SHEFType() {
	super();
}

/**
Copy constructor.
*/
public RiversideDB_SHEFType(RiversideDB_SHEFType s) {
	super();
	setSHEF_pe(new String(s.getSHEF_pe()));
	setUnits_engl(new String(s.getUnits_engl()));
	setUnits_si(new String(s.getUnits_si()));
	setDefault_base(new String(s.getDefault_base()));
	setDefault_mult(s.getDefault_mult());
	setTime_scale(new String(s.getTime_scale()));
	setDirty(s.isDirty());
}

/**
Clones the data object.
@return a clone of the data object.
*/
public Object clone() 
throws CloneNotSupportedException {
	RiversideDB_SHEFType s = (RiversideDB_SHEFType)super.clone();
	return s;
}

/**
Clones the data object.
@return a clone of the data object.
*/
public RiversideDB_SHEFType cloneSelf() {
	RiversideDB_SHEFType st = null;
	try {
		st = (RiversideDB_SHEFType)clone();
	}
	catch (CloneNotSupportedException e) {
		st = new RiversideDB_SHEFType(this);
	}
	return st;
}

/**
Checks to see if this object has the same values as another 
RiversideDB_SHEFType object.  
@param o the Object against which to compare this object's values.
@return true if the objects have the same values, false if not.
*/
public boolean equals(Object o) {
	if (o == null) { 
		return false;
	}
	if (!(o instanceof RiversideDB_SHEFType)) {
		return false;
	}
	RiversideDB_SHEFType st = (RiversideDB_SHEFType)o;
	if (!StringUtil.stringsAreEqual(getSHEF_pe(), st.getSHEF_pe())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getUnits_engl(), st.getUnits_engl())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getUnits_si(), st.getUnits_si())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDefault_base(), 
		st.getDefault_base())) {
		return false;
	}
	if (getDefault_mult() != st.getDefault_mult()) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getTime_scale(), st.getTime_scale())) {
		return false;
	}
	return true;
}

/**
Checks to see if this object has the same values as another 
RiversideDB_SHEFType object.
@param st the RiversisdeDB_SHEFTYpe object against which to compare values.
@return true if the values are the same, false if not.
*/
public boolean equals(RiversideDB_SHEFType st) {
	if (st == null) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getSHEF_pe(), st.getSHEF_pe())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getUnits_engl(), st.getUnits_engl())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getUnits_si(), st.getUnits_si())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDefault_base(), 
		st.getDefault_base())) {
		return false;
	}
	if (getDefault_mult() != st.getDefault_mult()) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getTime_scale(), st.getTime_scale())) {
		return false;
	}
	return true;
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_SHEF_pe = null;
	_Units_engl = null;
	_Units_si = null;
	_Default_base = null;
	_Time_scale = null;
	
	super.finalize();
}

/**
Returns _Default_base
@return _Default_base
*/
public String getDefault_base() {
	return _Default_base;
}

/**
Returns _Default_mult
@return _Default_mult
*/
public long getDefault_mult() {
	return _Default_mult;
}

/**
Returns _SHEF_pe
@return _SHEF_pe
*/
public String getSHEF_pe() {
	return _SHEF_pe;
}

/**
Returns _Time_scale
@return _Time_scale
*/
public String getTime_scale() {
	return _Time_scale;
}

/**
Returns _Units_engl
@return _Units_engl
*/
public String getUnits_engl() {
	return _Units_engl;
}

/**
Returns _Units_si
@return _Units_si
*/
public String getUnits_si() {
	return _Units_si;
}

/**
Sets _Default_base
@param Default_base value to set Default_base to
*/
public void setDefault_base(String Default_base) {
	if ( Default_base != null ) {
		_Default_base = Default_base;
	}
}

/**
Sets _Default_mult
@param Default_mult value to set _Default_mult to
*/
public void setDefault_mult(long Default_mult) {
	_Default_mult = Default_mult;
}

/**
Sets _SHEF_pe
@param SHEF_pe value to set _SHEF_pe to
*/
public void setSHEF_pe(String SHEF_pe) {
	if ( SHEF_pe != null ) {
		_SHEF_pe = SHEF_pe;
	}
}

/**
Sets _Time_scale
@param Time_scale value to set _Time_scale to
*/
public void setTime_scale(String Time_scale) {
	if ( Time_scale != null ) {
		_Time_scale = Time_scale;
	}
}

/**
Sets _Units_engl
@param Units_engl value to set _Units_engl to
*/
public void setUnits_engl(String Units_engl) {
	if ( Units_engl != null ) {
		_Units_engl = Units_engl;
	}
}

/**
Sets _Units_si
@param Units_si value to set _Units_si to
*/
public void setUnits_si(String Units_si) {
	if ( Units_si != null ) {
		_Units_si = Units_si;
	}
}

/** 
Returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_SHEF_pe{ " 		+ "\n" + 
		"SHEF_pe:     " + _SHEF_pe		+ "\n" +
		"Units_engl:  " + _Units_engl		+ "\n" +
		"Units_si:    " + _Units_si		+ "\n" +
		"Default_base:" + _Default_base		+ "\n" +
		"Deafult_mult:" + _Default_mult		+ "\n" +
		"Time_scale:  " + _Time_scale		+ "}";
}

}
