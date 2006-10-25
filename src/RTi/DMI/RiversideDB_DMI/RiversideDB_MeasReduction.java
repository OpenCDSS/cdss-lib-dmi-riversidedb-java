// ----------------------------------------------------------------------------
// RiversideDB_MeasReduction
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-06-28	Steven A. Malers, RTi	Initial version.
// 2002-08-20	J. Thomas Sapienza, RTi	Made toString() more descriptive
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// 2003-05-13	JTS, RTi		Added a copy constructor.
// 2003-06-05	JTS, RTi		Added DBUser_num, DBGroup_num and
//					DBPermissions fields.
// 2005-01-04 Luiz Teixeira, RTi	Added the _IsActive member used under
//          				_VERSION_030000_20041001 and the 
//					methods getIsActive and setIsActive.
//					This member and methods replace the pre
//					03.00.00 _Active and methods getActive
//					and setActive. Some cleanup...
// ----------------------------------------------------------------------------
package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMI;
import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

/**
Class to store data from the MeasReduction table
*/
public class RiversideDB_MeasReduction extends DMIDataObject
{

protected long   _OutputMeasType_num = DMIUtil.MISSING_LONG;
protected String _Method             = DMIUtil.MISSING_STRING;
protected long   _Create_order       = DMIUtil.MISSING_LONG;
protected String _Properties         = DMIUtil.MISSING_STRING;
protected String _Active             = DMIUtil.MISSING_STRING;   // Pre 03.00.00
protected String _IsActive           = DMIUtil.MISSING_STRING;   //     03.00.00

protected int    _DBUser_num         = DMIUtil.MISSING_INT;
protected int    _DBGroup_num        = DMIUtil.MISSING_INT;
protected String _DBPermissions      = DMIUtil.MISSING_STRING;

/** 
Constructor. 
*/
public RiversideDB_MeasReduction() {
	super();
}

/**
Copy constructor.
*/
public RiversideDB_MeasReduction(RiversideDB_MeasReduction m) {
	setOutputMeasType_num(           m.getOutputMeasType_num() );
	setMethod            (new String(m.getMethod            ()));
	setCreate_order      (           m.getCreate_order      () );
	setProperties        (new String(m.getProperties        ()));
	setActive            (new String(m.getActive            ()));
	setIsActive          (new String(m.getIsActive          ()));
	setDBUser_num        (           m.getDBUser_num        () );
	setDBGroup_num       (           m.getDBGroup_num       () );
	setDBPermissions(new String(     m.getDBPermissions     ()));

	setDirty(m.isDirty());
}

/**
Cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Method        = null;
	_Properties    = null;
	_Active        = null;                                   // Pre 03.00.00
	_IsActive      = null;                                   //     03.00.00
	_DBPermissions = null;
	super.finalize();
}

//   Get snd set methods presented in the order the members appear in the table.
// -----------------------------------------------------------------------------

/**
Returns _OutputMeasType_num
@return _OutputMeasType_num
*/
public long getOutputMeasType_num() {
	return _OutputMeasType_num;
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
Returns _DBGroup_num
@return _DBGroup_num
*/
public int getDBGroup_num() {
	return _DBGroup_num;
}

/**
Returns _DBUser_num
@return _DBUser_num
*/
public int getDBUser_num() {
	return _DBUser_num;
}

/**
Returns _DBPermissions
@return _DBPermissions
*/
public String getDBPermissions() {
	return _DBPermissions;
}

/**
Sets _OutputMeasType_num
@param OutputMeasType_num value to set OutputMeasType_num to
*/
public void setOutputMeasType_num(long OutputMeasType_num) {
	_OutputMeasType_num = OutputMeasType_num;
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
Sets _DBUser_num
@param DBUser_num value to put into _DBUser_num
*/
public void setDBUser_num(int DBUser_num) {
	_DBUser_num = DBUser_num;
}

/**
Sets _DBGroup_num
@param DBGroup_num value to put into _DBGroup_num
*/
public void setDBGroup_num(int DBGroup_num) {
	_DBGroup_num = DBGroup_num;
}

/**
Sets _DBPermissions
@param DBPermissions value to put into _DBPermissions
*/
public void setDBPermissions(String DBPermissions) {
	_DBPermissions = DBPermissions;
}

/** 
Returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_MeasReduction{ " 			+ "\n" + 
		"OutputMeasType_num:    " + _OutputMeasType_num	+ "\n" +
		"Method:                " + _Method		+ "\n" +
		"Create_order:          " + _Create_order	+ "\n" +
		"Properties:            " + _Properties		+ "\n" +
		"Active (pre 03.00.00): " + _Active		+ "\n" +
		"IsActive   (03.00.00): " + _IsActive		+ "\n" +
		"DBUser_num:            " + _DBUser_num		+ "\n" + 
		"DBGroup_num:           " + _DBGroup_num	+ "\n" +
		"DBPermissions:        '" + _DBPermissions	+ "'\n}\n";
}

}
