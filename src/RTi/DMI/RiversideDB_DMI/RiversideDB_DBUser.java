// ----------------------------------------------------------------------------
// RiversideDB_DBUser
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2003-04-30	J. Thomas Sapienza, RTi	Initial Version
// 2003-06-02	JTS, RTi		Added the PrimaryDBGroup_num field
// 2003-06-05	JTS, RTi		Added Default_DBPermissions field
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMI;
import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import java.util.Date;

/**
class to store data from the DBUser table
*/
public class RiversideDB_DBUser extends DMIDataObject
{

protected int _DBUser_num = DMIUtil.MISSING_INT;
protected String _Description = DMIUtil.MISSING_STRING;
protected String _Login = DMIUtil.MISSING_STRING;
protected String _Password = DMIUtil.MISSING_STRING;
protected int _PrimaryDBGroup_num = DMIUtil.MISSING_INT;
protected String _Default_DBPermissions = DMIUtil.MISSING_STRING;

/**
constructor.  
*/
public RiversideDB_DBUser() {
	super();
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Description = null;
	_Login = null;
	_Password = null;
	_Default_DBPermissions = null;
	super.finalize();
}

/**
Returns _DBUser_num
@return _DBUser_num
*/
public int getDBUser_num() {
	return _DBUser_num;
}

/**
Returns _Default_DBPermissions
@return _Default_DBPermissions
*/
public String getDefault_DBPermissions() {
	return _Default_DBPermissions;
}

/**
Returns _Description
@return _Description
*/
public String getDescription() {
	return _Description;
}

/**
Returns _Login
@return _Login
*/
public String getLogin() {
	return _Login;
}

/**
Returns _Password
@return _Password
*/
public String getPassword() {
	return _Password;
}

/**
Returns _PrimaryDBGroup_num 
@return _PrimaryDBGroup_num
*/
public int getPrimaryDBGroup_num() {
	return _PrimaryDBGroup_num;
}

/**
Sets _DBUser_num
@param DBUser_num value to put into _DBUser_num
*/
public void setDBUser_num(int DBUser_num) {
	_DBUser_num = DBUser_num;
}

/**
Sets _Default_DBPermissions
@param Default_DBPermissions value to put into _Default_DBPermissions
*/
public void setDefault_DBPermissions(String Default_DBPermissions) {
	_Default_DBPermissions = Default_DBPermissions;
}

/**
Sets _Description
@param Description value to put into _Description
*/
public void setDescription(String Description) {
	_Description = Description;
}

/**
Sets _Login
@param Login value to put into _Login
*/
public void setLogin(String Login) {
	_Login = Login;
}

/**
Sets _Password
@param Password value to put into _Password
*/
public void setPassword(String Password) {
	_Password = Password;
}

/**
Sets _PrimaryDBGroup_num
@param PrimaryDBGroup_num value to put into _PrimaryDBGroup_num
*/
public void setPrimaryDBGroup_num(int PrimaryDBGroup_num) {
	_PrimaryDBGroup_num = PrimaryDBGroup_num;
}

public String toString() {
	return "RiversideDB_DBUser {\n" + 
		"DBUser_num:            " + _DBUser_num + "\n" + 
		"Description:           '" + _Description + "'\n" + 
		"Login:                 '" + _Login + "'\n" + 
		"Password:              '" + _Password + "'\n" + 
		"PrimaryDBGroup_num:    " + _PrimaryDBGroup_num + "\n" +
		"Default_DBPermissions: '" + _Default_DBPermissions + "\n}\n";
}

} // End RiversideDB_DBUser
