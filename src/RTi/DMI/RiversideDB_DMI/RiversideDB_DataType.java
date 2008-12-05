// ----------------------------------------------------------------------------
// RiversideDB_DataType - class for doing I/O with these values
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-05-30	J. Thomas Sapienza, RTi	Initial version
// 2002-06-26	Steven A. Malers, RTi	Remove I/O from code.
// 2002-08-20	JTS, RTi		made toString() more descriptive
// 2004-01-26	JTS, RTi		Added copy constructor.
// 2004-01-27	JTS, RTi		* Added equals().
//					* Member variables now are initialized
//					  to missing values.
// 2004-02-02	JTS, RTi		Class is now cloneable.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import java.util.List;

import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import RTi.Util.IO.DataType;
import RTi.Util.String.StringUtil;

/**
class to store data from the DataType table
*/
public class RiversideDB_DataType 
extends DMIDataObject
implements Cloneable {

protected String _DataType = DMIUtil.MISSING_STRING; // Key
protected String _Description = DMIUtil.MISSING_STRING;
protected String _Dimension = DMIUtil.MISSING_STRING;
protected String _Meas_time_scale = DMIUtil.MISSING_STRING;
protected String _Meas_loc_type = DMIUtil.MISSING_STRING;
protected String _Record_type = DMIUtil.MISSING_STRING;
protected String _Default_engl_units = DMIUtil.MISSING_STRING;
protected String _Default_si_units = DMIUtil.MISSING_STRING;
protected String _SHEF_pe = DMIUtil.MISSING_STRING;
protected double _Default_engl_min = DMIUtil.MISSING_DOUBLE;
protected double _Default_engl_max = DMIUtil.MISSING_DOUBLE;
protected double _Default_si_min = DMIUtil.MISSING_DOUBLE;
protected double _Default_si_max = DMIUtil.MISSING_DOUBLE;

/** 
Constructor. 
*/
public RiversideDB_DataType()
{	super();
}

/**
Copy constructor.
*/
public RiversideDB_DataType(RiversideDB_DataType d) {
	super();
	setDataType(new String(d.getDataType()));
	setDescription(new String(d.getDescription()));
	setDimension(new String(d.getDimension()));
	setMeas_time_scale(new String(d.getMeas_time_scale()));
	setMeas_loc_type(new String(d.getMeas_loc_type()));
	setRecord_type(new String(d.getRecord_type()));
	setDefault_engl_units(new String(d.getDefault_engl_units()));
	setDefault_si_units(new String(d.getDefault_si_units()));
	setSHEF_pe(new String(d.getSHEF_pe()));
	setDefault_engl_min(d.getDefault_engl_min());
	setDefault_engl_max(d.getDefault_engl_max());
	setDefault_si_min(d.getDefault_si_min());
	setDefault_si_max(d.getDefault_si_max());
	setDirty(d.isDirty());
}

/**
Clones the data object.
@return a clone of the data object.
*/
public Object clone() 
throws CloneNotSupportedException {
	RiversideDB_DataType dt = (RiversideDB_DataType)super.clone();
	return dt;
}

/**
Clones the data object.
@return a clone of the data object.
*/
public RiversideDB_DataType cloneSelf() {
	RiversideDB_DataType dt = null;
	try {
		dt = (RiversideDB_DataType)clone();
	}
	catch (CloneNotSupportedException e) {
		dt = new RiversideDB_DataType(this);
	}
	return dt;
}

/**
Checks to see if this object has the same values as another 
RiversideDB_DataType object.
@param o the object against which to compare
@return true if the objects have the same values, false if not.
*/
public boolean equals(Object o) {
	if (o == null) { 
		return false;
	}
	if (!(o instanceof RiversideDB_DataType)) {
		return false;
	}
	RiversideDB_DataType dt = (RiversideDB_DataType)o;
	if (!StringUtil.stringsAreEqual(getDataType(), dt.getDataType())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDescription(), dt.getDescription())){
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDimension(), dt.getDimension())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getMeas_time_scale(), 
		dt.getMeas_time_scale())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getMeas_loc_type(), 
		dt.getMeas_loc_type())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getRecord_type(), 
		dt.getRecord_type())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDefault_engl_units(), 
		dt.getDefault_engl_units())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDefault_si_units(), 
		dt.getDefault_si_units())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getSHEF_pe(), dt.getSHEF_pe())) {
		return false;
	}
	if (getDefault_engl_min() != dt.getDefault_engl_min()) {
		return false;
	}
	if (getDefault_engl_max() != dt.getDefault_engl_max()) {
		return false;
	}
	if (getDefault_si_min() != dt.getDefault_si_min()) {
		return false;
	}
	if (getDefault_si_max() != dt.getDefault_si_max()) {
		return false;
	}
	return true;
}

/**
Checks to see if this object has the same values as another 
RiversideDB_DataType object.
@param dt the RiversideDB_DataType object against which to compare.
@return true if the objects have the same values, false if not.
*/
public boolean equals(RiversideDB_DataType dt) {
	if (dt == null) {	
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDataType(), dt.getDataType())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDescription(), dt.getDescription())){
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDimension(), dt.getDimension())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getMeas_time_scale(), 
		dt.getMeas_time_scale())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getMeas_loc_type(), 
		dt.getMeas_loc_type())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getRecord_type(), dt.getRecord_type())){
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDefault_engl_units(), 
		dt.getDefault_engl_units())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDefault_si_units(), 
		dt.getDefault_si_units())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getSHEF_pe(), dt.getSHEF_pe())) {
		return false;
	}
	if (getDefault_engl_min() != dt.getDefault_engl_min()) {
		return false;
	}
	if (getDefault_engl_max() != dt.getDefault_engl_max()) {
		return false;
	}
	if (getDefault_si_min() != dt.getDefault_si_min()) {
		return false;
	}
	if (getDefault_si_max() != dt.getDefault_si_max()) {
		return false;
	}
	return true;
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_DataType = null;
	_Description = null;
	_Dimension = null;
	_Meas_time_scale = null;
	_Meas_loc_type = null;
	_Record_type = null;
	_Default_engl_units = null;
	_Default_si_units = null;
	_SHEF_pe = null;

	super.finalize();
}

/**
returns _DataType
@return _DataType
*/
public String getDataType() {
	return _DataType;
}

/**
returns _Default_engl_max
@return _Default_engl_max
*/
public double getDefault_engl_max() {
	return _Default_engl_max;
}

/**
returns _Default_engl_min
@return _Default_engl_min
*/
public double getDefault_engl_min() {
	return _Default_engl_min;
}

/**
returns _Default_engl_units
@return _Default_engl_units
*/
public String getDefault_engl_units() {
	return _Default_engl_units;
}

/**
returns _Default_si_max
@return _Default_si_max
*/
public double getDefault_si_max() { 
	return _Default_si_max;
}

/**
returns _Default_si_min
@return _Default_si_min
*/
public double getDefault_si_min() {
	return _Default_si_min;
}

/**
returns _Default_si_units
@return _Default_si_units
*/
public String getDefault_si_units() {
	return _Default_si_units;
}

/**
returns _Description
@return _Description
*/
public String getDescription() {
	return _Description;
}

/**
returns _Dimension
@return _Dimension
*/
public String getDimension() {
	return _Dimension;
}

/**
returns _Meas_time_scale
@return _Meas_time_scale
*/
public String getMeas_time_scale() {
	return _Meas_time_scale;
}

/**
returns _Meas_loc_type
@return _Meas_loc_type
*/
public String getMeas_loc_type() {
	return _Meas_loc_type;
}

/** 
returns _Record_type
@return _Record_type
*/
public String getRecord_type() {
	return _Record_type;
}

/**
returns _SHEF_pe
@return _SHEF_pe
*/
public String getSHEF_pe() {
	return _SHEF_pe;
}

/**
Determine the position in a Vector of RiversideDB_DataType, using the DataType for the search.
@param datatypes Vector or RiversideDB_DataType to search.
@param DataType value to compare in objects.
@return the vector position or -1 if not found.
*/
public static int indexOf ( List datatypes, String DataType )
{	int size = 0;
	if ( datatypes != null ) {
		size = datatypes.size();
	}
	for ( int i = 0; i < size; i++ ) {
		if ( ((RiversideDB_DataType)datatypes.get(i))._DataType.equalsIgnoreCase( DataType) ) {
			return i;
		}
	}
	return -1;
}

/**
sets _DataType
@param DataType the value to set _DataType to
*/
public void setDataType(String DataType) {
	_DataType = DataType;
}

/**
sets _Default_engl_max
@param Default_engl_max the value to set _Default_engl_max to
*/ 
public void setDefault_engl_max(double Default_engl_max) {
	_Default_engl_max = Default_engl_max;
}

/** 
sets _Default_engl_min
@param Default_engl_min the value to set _Default_engl_min to
*/
public void setDefault_engl_min(double Default_engl_min) {
	_Default_engl_min = Default_engl_min;
}

/**
sets _Default_engl_units
@param Default_engl_units the value to set _Default_engl_units to
*/
public void setDefault_engl_units(String Default_engl_units) {
	_Default_engl_units = Default_engl_units;
}

/**
sets _Default_si_max
@param Default_si_max the value to set _Default_si_max to
*/
public void setDefault_si_max(double Default_si_max) {
	_Default_si_max = Default_si_max;
}

/**
sets _Default_si_min
@param Default_si_min the value to set _Default_si_min to
*/
public void setDefault_si_min(double Default_si_min) {
	_Default_si_min = Default_si_min;
}

/**
sets _Default_si_units
@param Default_si_units the value to set _Default_si_units to
*/
public void setDefault_si_units(String Default_si_units) {
	_Default_si_units = Default_si_units;
}

/**
sets _Description
@param Description the value to set _Description to
*/
public void setDescription(String Description) {
	_Description = Description;
}

/**
sets _Dimension
@param Dimension the value to set _Dimension to
*/
public void setDimension(String Dimension) {
	_Dimension = Dimension;
}

/**
sets _Meas_loc_type
@param Meas_loc_type the value to set _Meas_loc_type to
*/
public void setMeas_loc_type(String Meas_loc_type) {
	_Meas_loc_type = Meas_loc_type;
}

/**
sets _Meas_time_scale
@param Meas_time_scale
*/
public void setMeas_time_scale(String Meas_time_scale) {
	_Meas_time_scale = Meas_time_scale;
}

/** 
sets _Record_type
@param Record_type the value to set _Record_type to
*/
public void setRecord_type(String Record_type) {
	_Record_type = Record_type;
}

/**
sets _SHEF_pe
@param SHEF_pe the value to set _SHEF_pe to
*/
public void setSHEF_pe(String SHEF_pe) {
	_SHEF_pe = SHEF_pe;
}

/** 
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_DataType{ " 			+ "\n" + 
		"Data_type:         " + _DataType		+ "\n" +
		"Description:       " + _Description		+ "\n" +
		"Dimension:         " + _Dimension		+ "\n" +
		"Meas_time_scale:   " + _Meas_time_scale	+ "\n" +
		"Meas_loc_type:     " + _Meas_loc_type		+ "\n" +
		"Record_type:       " + _Record_type		+ "\n" +
		"Default_engl_units:" + _Default_engl_units	+ "\n" +
		"Default_si_units:  " + _Default_si_units	+ "\n" +
		"SHEF_pe:           " + _SHEF_pe		+ "\n" +
		"Default_engl_min:  " + _Default_engl_min	+ "\n" +
		"Default_engl_max:  " + _Default_engl_max	+ "\n" +
		"Default_si_min:    " + _Default_si_min		+ "\n" +
		"Default_si_max:    " + _Default_si_max		+ "}";
}
/** 
returns a DataType that contains the fields of this object
@return a DataType representation of this object
*/
public DataType toDataType() throws Exception {
    DataType dt = new DataType();
    dt.setAbbreviation(_DataType);
    dt.setDefaultEnglishMax(_Default_engl_max);
    dt.setDefaultEnglishMin(_Default_engl_min);
    dt.setDefaultEnglishUnits(_Default_engl_units);
    dt.setDefaultSIMax(_Default_si_max);
    dt.setDefaultSIMin(_Default_si_max);
    dt.setDefaultSIUnits(_Default_si_units);
    dt.setDescription(_Description);
    dt.setDimension(_Dimension);
    dt.setMeasLocType(_Meas_loc_type);
    dt.setMeasTimeScale(_Meas_time_scale);
    dt.setSHEFpe(_SHEF_pe);
    return dt;
}

} // end RiversideDB_DataType
