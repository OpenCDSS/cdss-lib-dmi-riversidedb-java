// ----------------------------------------------------------------------------
// RiversideDB_MeasScenario
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-06-27	Steven A. Malers, RTi	Initial version.
// 2002-08-20	J. Thomas Sapienza, RTi	Made toString() more descriptive
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// 2005-01-04 Luiz Teixeira, RTi	Added the _IsActive member used under
//          				_VERSION_030000_20041001 and the 
//					methods getIsActive and setIsActive.
//					This member and methods replace the pre
//					03.00.00 _Active and methods getActive
//					and setActive.  Some cleanup...
// ----------------------------------------------------------------------------
package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

/**
Class to store data from the MeasScenario table
*/
public class RiversideDB_MeasScenario extends DMIDataObject
{

protected long   _ObsMeasType_num = DMIUtil.MISSING_LONG;
protected String _Method          = DMIUtil.MISSING_STRING;
protected long   _Create_order    = DMIUtil.MISSING_LONG;
protected String _Properties      = DMIUtil.MISSING_STRING;
protected String _Active          = DMIUtil.MISSING_STRING;      // Pre 03.00.00
protected String _IsActive        = DMIUtil.MISSING_STRING;      //     03.00.00

/** 
Constructor. 
*/
public RiversideDB_MeasScenario() {
	super();
}

/**
Copy constructor.
*/
public RiversideDB_MeasScenario( RiversideDB_MeasScenario m ) {
	setObsMeasType_num (            m.getObsMeasType_num() );
	setMethod          ( new String(m.getMethod         ()) );
	setCreate_order    (            m.getCreate_order   ()  );
	setProperties      ( new String(m.getProperties     ()) );
	setActive          ( new String(m.getActive         ()) );
	setIsActive        ( new String(m.getIsActive       ()) );

	setDirty(m.isDirty());
}

/**
Cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Method     = null;
	_Properties = null;
	_Active     = null;                                      // Pre 03.00.00
	_IsActive   = null;                                      //     03.00.00
	super.finalize();
}

//   Get snd set methods presented in the order the members appear in the table.
// -----------------------------------------------------------------------------

/**
Returns _ObsMeasType_num
@return _ObsMeasType_num
*/
public long getObsMeasType_num() {
	return _ObsMeasType_num;
}

/**
Returns _Method
@return _Method
*/
public String getMethod() {
	return _Method;
}

/**
Returns _Create_order
@return _Create_order
*/
public long getCreate_order() {
	return _Create_order;
}

/**
Returns _Properties
@return _Properties
*/
public String getProperties() {
	return _Properties;
}

/**
Returns _Active (Pre 03.00.00)
@return _Active
*/
public String getActive() {
	return _Active;
}

/**
Returns _IsActive (03.00.00)
@return _IsActive
*/
public String getIsActive() {
	return _IsActive;
}

/**
Sets _ObsMeasType_num
@param ObsMeasType_num value to set ObsMeasType_num to
*/
public void setObsMeasType_num(long ObsMeasType_num) {
	_ObsMeasType_num = ObsMeasType_num;
}

/**
Sets _Method
@param Method value to set _Method to
*/
public void setMethod(String Method) {
	if ( Method != null ) {
		_Method = Method;
	}
}

/**
Sets _Create_order
@param Create_order value to set Create_order to
*/
public void setCreate_order(long Create_order) {
	_Create_order = Create_order;
}

/**
Sets _Properties
@param Properties value to set _Properties to
*/
public void setProperties(String Properties) {
	if ( Properties != null ) {
		_Properties = Properties;
	}
}

/**
Sets _Active (Pre 03.00.00)
@param Active value to set _Active to
*/
public void setActive(String Active) {
	if ( Active != null ) {
		_Active = Active;
	}
}

/**
Sets _IsActive (03.00.00)
@param IsActive value to set _IsActive to
*/
public void setIsActive(String IsActive) {
	if ( IsActive != null ) {
		_IsActive = IsActive;
	}
}

/** 
Returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_MeasScenario{ "                 + "\n" + 
		"ObsMeasType_num:       " + _ObsMeasType_num + "\n" +
		"Method:                " + _Method          + "\n" +
		"Create_order:          " + _Create_order    + "\n" +
		"Properties:            " + _Properties	     + "\n" +
		"Active (pre 03.00.00): " + _Active          + "\n" + 
		"IsActive:  (03.00.00): " + _IsActive        + "}";
}

} // End RiversideDB_MeasScenario
