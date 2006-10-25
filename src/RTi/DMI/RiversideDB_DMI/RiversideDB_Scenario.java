// ----------------------------------------------------------------------------
// RiversideDB_Scenario
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-06-27	Steven A. Malers, RTi	Initial version.
// 2002-08-20	J. Thomas Sapienza, RTi	Made toString() more descriptive
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// 2004-01-26	JTS, RTi		Added copy constructor.
// 2004-01-27	JTS, RTi		Added equals().
// 2004-02-02	JTS, RTi		Class is now cloneable.
// 2005-01-04 Luiz Teixeira, RTi	Added the _IsActive member used under
//          				_VERSION_030000_20041001 and the
//					methods getIsActive and setIsActive.
//					This member and methods replace the pre
//					03.00.00 _Active and methods getActive
//					and setActive. Some cleanup...
// ---------------------------------------------------------------------------
package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMI;
import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import RTi.Util.String.StringUtil;

/**
class to store data from the State table
*/
public class RiversideDB_Scenario
extends DMIDataObject
implements Cloneable {

protected long   _Scenario_num = DMIUtil.MISSING_LONG;
protected String _Identifier   = DMIUtil.MISSING_STRING;
protected String _Description  = DMIUtil.MISSING_STRING;
protected int    _Active       = DMIUtil.MISSING_INT;            // Pre 03.00.00
protected int    _IsActive     = DMIUtil.MISSING_INT;            //     03.00.00

/**
Constructor.
*/
public RiversideDB_Scenario() {
	super();
}

/**
Copy constructor.
*/
public RiversideDB_Scenario(RiversideDB_Scenario s) {
	super();
	setScenario_num (            s.getScenario_num()  );
	setIdentifier   ( new String(s.getIdentifier  ()) );
	setDescription  ( new String(s.getDescription ()) );
	setActive       (            s.getActive      ()  );     // Pre 03.00.00
	setIsActive     (            s.getIsActive    ()  );     //     03.00.00

	setDirty (s.isDirty());
}

/**
Clones the data object.
@return a clone of the data object.
*/
public Object clone()
throws CloneNotSupportedException {
	RiversideDB_Scenario s = (RiversideDB_Scenario)super.clone();
	return s;
}

/**
Clones the data object.
@return a clone of the data object.
*/
public RiversideDB_Scenario cloneSelf() {
	RiversideDB_Scenario s = null;
	try {
		s = (RiversideDB_Scenario)clone();
	}
	catch (CloneNotSupportedException e) {
		s = new RiversideDB_Scenario(this);
	}
	return s;
}

/**
Checks to see if this object has the same values as another
RiversideDB_Scenario object.
@param o the Object against which to compare this object's values.
@return true if the objects have the same values, false if not.
*/
public boolean equals(Object o) {
	if (o == null) {
		return false;
	}
	if (!(o instanceof RiversideDB_Scenario)) {
		return false;
	}
	RiversideDB_Scenario s = (RiversideDB_Scenario)o;
	if (getScenario_num() != s.getScenario_num()) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getIdentifier(), s.getIdentifier())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDescription(), s.getDescription())) {
		return false;
	}
	if (getActive() != s.getActive()) {                      // Pre 03.00.00
		return false;
	}
	if (getIsActive() != s.getIsActive()) {                  //     03.00.00
		return false;
	}

	return true;
}

/**
Checks this object to see if its values are the same as the values in another
RiversideDB_Scenario object.
@param s the RiversideDB_Scenario object against which to compare values.
@return true if the values are the same, false if not.
*/
public boolean equals(RiversideDB_Scenario s) {
	if (s == null) {
		return false;
	}
	if (getScenario_num() != s.getScenario_num()) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getIdentifier(), s.getIdentifier())) {
		return false;
	}
	if (!StringUtil.stringsAreEqual(getDescription(), s.getDescription())) {
		return false;
	}
	if (getActive() != s.getActive()) {                      // Pre 03.00.00
		return false;
	}
	if (getIsActive() != s.getIsActive()) {                  //     03.00.00
		return false;
	}
	return true;
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Identifier  = null;
	_Description = null;
	super.finalize();
}

//   Get snd set methods presented in the order the members appear in the table.
// -----------------------------------------------------------------------------

/**
Returns _Scenario_num
@return _Scenario_num
*/
public long getScenario_num() {
	return _Scenario_num;
}

/**
Returns _Identifier
@return _Identifier
*/
public String getIdentifier() {
	return _Identifier;
}

/**
Returns _Description
@return _Description
*/
public String getDescription() {
	return _Description;
}

/**
Returns _Active (Pre 03.00.00)
@return _Active
*/
public int getActive() {
	return _Active;
}

/**
Returns _IsActive (03.00.00
@return _IsActive
*/
public int getIsActive() {
	return _IsActive;
}

/**
Sets _Scenario_num
@param Scenario_num value to set Scenario_num to
*/
public void setScenario_num(long Scenario_num) {
	_Scenario_num = Scenario_num;
}

/**
Sets _Identifier
@param Identifier value to set _Identifier to
*/
public void setIdentifier(String Identifier) {
	if ( Identifier != null ) {
		_Identifier = Identifier;
	}
}

/**
Sets _Description
@param Description value to set _Description to
*/
public void setDescription(String Description) {
	if ( Description != null ) {
		_Description = Description;
	}
}

/**
Sets _Active (Pre 03.00.00)
@param Active value to set _Active to
*/
public void setActive(int Active) {
	_Active = Active;
}

/**
Sets _IsActive (03.00.00)
@param IsActive value to set _IsActive to
*/
public void setIsActive(int IsActive) {
	_IsActive = IsActive;
}

/**
Returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_Scenario{ " 		  + "\n" +
		"Scenario_num:          " + _Scenario_num + "\n" +
		"Identifier:            " + _Identifier	  + "\n" +
		"Description:           " + _Description  + "\n" +
		"Active (pre 03.00.00): " + _Active	  + "\n" +
		"IsActive   (03.00.00): " + _IsActive	  + "}";
}

}
