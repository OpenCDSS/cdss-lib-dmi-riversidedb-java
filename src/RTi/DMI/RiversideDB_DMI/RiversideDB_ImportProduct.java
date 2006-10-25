// ----------------------------------------------------------------------------
// RiversideDB_ImportProduct.java - class for doing I/O with these values
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-05-22	J. Thomas Sapienza, RTi	Initial Version
// 2002-05-23	JTS, RTi    		Added missing methods for 
//					ImportProduct_num.  Added 
//					fillObjectVector method.  Added 
//					constructor, removed parentheses 
//					in returns
// 2002-05-29	JTS, RTi    		Added __dirty variable and methods
// 2002-06-26	Steven A. Malers, RTi	Update so class does not contain I/O
//					features.
// 2002-08-20	JTS, RTi		Made toString() more descriptive
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// 2003-05-12	JTS, RTi		Added a copy constructor.
// 2003-05-15	JTS, RTi		Made ImportProduct_num an int instead
//					of a long
// 2003-05-27	JTS, RTi		Revised how the copy constructor handles
//					the date fields due to problems in how
//					JDK 1.1.8's Date object implemented the
//					Cloneable interface.
// 2003-06-02	JTS, RTi		Updated code for 02.08.00 db design:
//					added fields ProductGroup_num, 
//					DBUser_num,
//					DBGroup_num and DBPermissions
// 2003-06-18	JTS, RTi		Added field MeasLocGroup_num
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMI;
import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import java.util.Date;
import java.util.Vector;

/**
class to store data from the ImportProduct table
*/
public class RiversideDB_ImportProduct extends DMIDataObject
implements Cloneable
{

// from table ImportProduct
protected int _ImportProduct_num	= DMIUtil.MISSING_INT;
protected String	_Product_name		= DMIUtil.MISSING_STRING;
protected String 	_Product_type		= DMIUtil.MISSING_STRING;
protected String	_IsActive		= DMIUtil.MISSING_STRING;
protected String	_Product_group		= DMIUtil.MISSING_STRING;
protected long		_Import_order		= DMIUtil.MISSING_LONG;
protected Date		_Last_import_date	= DMIUtil.MISSING_DATE;
protected Date		_Next_import_date	= DMIUtil.MISSING_DATE;
protected long		_Retries		= DMIUtil.MISSING_LONG;
protected String	_User_login		= DMIUtil.MISSING_STRING;
protected String	_User_PWD		= DMIUtil.MISSING_STRING;

protected String	_Firewall_user		= DMIUtil.MISSING_STRING;
protected String	_Firewall_user_PWD	= DMIUtil.MISSING_STRING;
protected String	_Source_URL_base	= DMIUtil.MISSING_STRING;
protected String	_Source_URL_file	= DMIUtil.MISSING_STRING;
protected String	_Import_window		= DMIUtil.MISSING_STRING;
protected String	_Add_source_URL_base	= DMIUtil.MISSING_STRING;
protected String	_Add_source_URL_file	= DMIUtil.MISSING_STRING;
protected String	_Destination_dir	= DMIUtil.MISSING_STRING;
protected String	_Destination_file	= DMIUtil.MISSING_STRING;
protected String	_Properties		= DMIUtil.MISSING_STRING;

protected String	_IsAutomated		= DMIUtil.MISSING_STRING;
protected String	_IsInterval		= DMIUtil.MISSING_STRING;
protected String	_Import_year		= DMIUtil.MISSING_STRING;
protected String	_Import_month		= DMIUtil.MISSING_STRING;
protected String	_Import_day		= DMIUtil.MISSING_STRING;
protected String	_Import_hour		= DMIUtil.MISSING_STRING;
protected String	_Import_minute		= DMIUtil.MISSING_STRING;
protected String	_Import_second		= DMIUtil.MISSING_STRING;
protected String	_Import_weekday		= DMIUtil.MISSING_STRING;
protected String	_Import_delay		= DMIUtil.MISSING_STRING;

protected String	_DoArchive		= DMIUtil.MISSING_STRING;
protected String	_Archive_dir		= DMIUtil.MISSING_STRING;
protected String	_Archive_file		= DMIUtil.MISSING_STRING;

// added for DB design 02.08.00
protected int _ProductGroup_num = DMIUtil.MISSING_INT;
protected int _DBUser_num = DMIUtil.MISSING_INT;
protected int _DBGroup_num = DMIUtil.MISSING_INT;
protected String _DBPermissions = DMIUtil.MISSING_STRING;
protected int _MeasLocGroup_num = DMIUtil.MISSING_INT;

/**
Copy constructor.
@param i the RiversideDB_ImportProduct to duplicate.
*/
public RiversideDB_ImportProduct(RiversideDB_ImportProduct i) {
	setImportProduct_num(i.getImportProduct_num());
	setProduct_name(new String(i.getProduct_name()));
	setProduct_type(new String(i.getProduct_type()));
	setIsActive(new String(i.getIsActive()));
	setProduct_group(new String(i.getProduct_group()));
	setImport_order(i.getImport_order());

	Date tempDate = i.getLast_import_date();
	Date clonedDate = null;
	if (tempDate != null){ 
		clonedDate = new Date(tempDate.getTime());
		setLast_import_date(clonedDate);
	}

	tempDate = i.getNext_import_date();
	if (tempDate != null) {
		clonedDate = new Date(tempDate.getTime());
		setNext_import_date(clonedDate);
	}
	
	setRetries(i.getRetries());
	setUser_login(new String(i.getUser_login()));
	setUser_PWD(new String(i.getUser_PWD()));

	setFirewall_user(new String(i.getFirewall_user()));
	setFirewall_user_PWD(new String(i.getFirewall_user()));
	setSource_URL_base(new String(i.getSource_URL_base()));
	setSource_URL_file(new String(i.getSource_URL_file()));
	setImport_window(new String(i.getImport_window()));
	setAdd_source_URL_base(new String(i.getAdd_source_URL_base()));
	setAdd_source_URL_file(new String(i.getAdd_source_URL_file()));
	setDestination_dir(new String(i.getDestination_dir()));
	setDestination_file(new String(i.getDestination_file()));
	setProperties(new String(i.getProperties()));

	setIsAutomated(new String(i.getIsAutomated()));
	setIsInterval(new String(i.getIsInterval()));
	setImport_year(new String(i.getImport_year()));
	setImport_month(new String(i.getImport_month()));
	setImport_day(new String(i.getImport_day()));
	setImport_hour(new String(i.getImport_hour()));
	setImport_minute(new String(i.getImport_minute()));
	setImport_second(new String(i.getImport_second()));
	setImport_weekday(new String(i.getImport_weekday()));
	setImport_delay(new String(i.getImport_delay()));

	setDoArchive(new String(i.getDoArchive()));
	setArchive_dir(new String(i.getArchive_dir()));
	setArchive_file(new String(i.getArchive_file()));

	setProductGroup_num(i.getProductGroup_num());
	setDBUser_num(i.getDBUser_num());
	setDBGroup_num(i.getDBGroup_num());
	setDBPermissions(new String(i.getDBPermissions()));
	setMeasLocGroup_num(i.getMeasLocGroup_num());

	setDirty(i.isDirty());
}

/**
Constructor.  
*/
public RiversideDB_ImportProduct()
{ 	super();
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Add_source_URL_base 	= null;
	_Add_source_URL_file	= null;
	_Archive_dir		= null;
	_Archive_file		= null;

	_Destination_dir	= null;
	_Destination_file	= null;
	_DoArchive		= null;

	_Firewall_user		= null;
	_Firewall_user_PWD	= null;

	_Import_day		= null;
	_Import_delay		= null;
	_Import_hour		= null;
	_Import_minute		= null;
	_Import_month		= null;
	_Import_second		= null;
	_Import_weekday		= null;
	_Import_window		= null;
	_Import_year		= null;
	_IsActive		= null;
	_IsAutomated		= null;
	_IsInterval		= null;

	_Last_import_date	= null;

	_Next_import_date	= null;

	_Product_group		= null;
	_Product_name		= null;
	_Product_type		= null;
	_Properties		= null;

	_Source_URL_base	= null;
	_Source_URL_file	= null;

	_User_login		= null;
	_User_PWD		= null;
	
	super.finalize();
}

/**
returns _Add_source_URL_base
@return _Add_source_URL_base
*/
public String getAdd_source_URL_base() {
	return _Add_source_URL_base;
}

/**
returns _Add_source_URL_file
@return _Add_source_URL_file
*/
public String getAdd_source_URL_file() {
	return _Add_source_URL_file;
}

/**
returns _Archive_dir
@return _Archive_dir
*/
public String getArchive_dir() {
	return _Archive_dir;
}

/**
returns _Archive_file
@return _Archive_file
*/
public String getArchive_file() {
	return _Archive_file;
}

/**
Returns _DBGroup_num
@return _DBGroup_num
*/
public int getDBGroup_num() {
	return _DBGroup_num;
}

/**
Returns _DBPermissions
@return _DBPermissions
*/
public String getDBPermissions() {
	return _DBPermissions;
}

/**
Returns _DBUser_num
@return _DBUser_num
*/
public int getDBUser_num() {
	return _DBUser_num;
}

/** 
returns _Destination_dir
@return _Destination_dir
*/
public String getDestination_dir() {
	return _Destination_dir;
}

/**
returns _Destination_file
@return _Destination_file
*/
public String getDestination_file() {
	return _Destination_file;
}

/**
returns _DoArchive
@return _DoArchive
*/
public String getDoArchive() {
	return _DoArchive;
}

/**
returns _Firewall_user
@return _Firewall_user
*/
public String getFirewall_user() {
	return _Firewall_user;
}

/**
returns _Firewall_user_PWD
@return _Firewall_user_PWD
*/
public String getFirewall_user_PWD() {
	return _Firewall_user_PWD;
}

/**
returns _Import_day
@return _Import_day
*/
public String getImport_day() {
	return _Import_day;
}

/**
returns _Import_delay
@return _Import_delay
*/
public String getImport_delay() {
	return _Import_delay;
}

/**
returns _Import_hour
@return _Import_hour
*/
public String getImport_hour() {
	return _Import_hour;
}

/**
returns _Import_minute
@return _Import_minute
*/
public String getImport_minute() {
	return _Import_minute;
}

/**
returns _Import_month
@return _Import_month
*/
public String getImport_month() {
	return _Import_month;
}

/**
returns _Import_order
@return _Import_order
*/
public long getImport_order() {
	return _Import_order;
}

/**
returns _Import_second
@return _Import_second
*/
public String getImport_second() {
	return _Import_second;
}

/**
returns _Import_weekday 
@return _Import_weekday
*/
public String getImport_weekday() {
	return _Import_weekday;
}

/**
returns _Import_window
@return _Import_window
*/
public String getImport_window() {
	return _Import_window;
}

/**
returns _Import_year
@return _Import_year
*/
public String getImport_year() {
	return _Import_year;
}

/**
returns _ImportProduct_num
@return _ImportProduct_num
*/
public int getImportProduct_num() {
	return _ImportProduct_num;
}

/**
returns _IsActive
@return _IsActive
*/
public String getIsActive() {
	return _IsActive;
}

/**
returns _IsAutomated
@return _IsAutomated
*/
public String getIsAutomated() {
	return _IsAutomated;
}

/**
returns _IsInterval
@return _IsInterval
*/
public String getIsInterval() {
	return _IsInterval;
}

/**
returns _Last_import_date
@return _Last_import_date
*/
public Date getLast_import_date() {
	return _Last_import_date;
}

/**
Returns _MeasLocGroup_num
@return _MeasLocGroup_num
*/
public int getMeasLocGroup_num() {
	return _MeasLocGroup_num;
}

/**
returns _Next_import_date
@return _Next_import_date
*/
public Date getNext_import_date() {
	return _Next_import_date;
}

/**
returns _Product_group
@return _Product_group
*/
public String getProduct_group() {
	return _Product_group;
}

/**
Returns _ProductGroup_num
@return _ProductGroup_num
*/
public int getProductGroup_num() {
	return _ProductGroup_num;
}

/**
returns _Product_name
@return _Product_name
*/
public String getProduct_name() {
	return _Product_name;
}

/**
returns _Product_type
@return _Product_type
*/
public String getProduct_type() {
	return _Product_type;
}

/**
returns _Properties
@return _Properties
*/
public String getProperties() {
	return _Properties;
}

/**
returns _Retries
@return _Retries
*/
public long getRetries() {
	return _Retries;
}

/**
returns _Source_URL_base
@return _Source_URL_base
*/
public String getSource_URL_base() {
	return _Source_URL_base;
}

/**
returns _Source_URL_file
@return _Source_URL_file
*/
public String getSource_URL_file() {
	return _Source_URL_file;
}

/**
returns _User_login
@return _User_login
*/
public String getUser_login() {
	return _User_login;
}

/**
returns _User_PWD
@return _User_PWD
*/
public String getUser_PWD() {
	return _User_PWD;
}

/**
sets _Add_source_URL_base
@param Add_source_URL_base value to set _Add_source_URL_base to
*/
public void setAdd_source_URL_base (String Add_source_URL_base) {
	_Add_source_URL_base = Add_source_URL_base;
}

/**
sets _Add_source_URL_file
@param Add_source_URL_file value to set _Add_source_URL_file to
*/
public void setAdd_source_URL_file (String Add_source_URL_file) {
	_Add_source_URL_file = Add_source_URL_file;
}

/**
sets _Archive_dir
@param Archive_dir value to set _Archive_dir to
*/
public void setArchive_dir (String Archive_dir) {
	_Archive_dir = Archive_dir;
}

/**
sets _Archive_file
@param Archive_file value to set _Archive_file to
*/
public void setArchive_file (String Archive_file) {
	_Archive_file = Archive_file;
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
Sets _DBUser_num
@param DBUser_num value to put into _DBUser_num
*/
public void setDBUser_num(int DBUser_num) {
	_DBUser_num = DBUser_num;
}

/**
sets _Destination_dir
@param Destination_dir value to set _Destination_dir to
*/
public void setDestination_dir (String Destination_dir) {
	_Destination_dir = Destination_dir;
}

/**
sets _Destination_file
@param Destination_file value to set _Destination_file to
*/
public void setDestination_file (String Destination_file) {
	_Destination_file = Destination_file;
}

/**
sets _DoArchive
@param DoArchive
*/
public void setDoArchive (String DoArchive) {
	_DoArchive = DoArchive;
}

/**
sets _Firewall_user
@param Firewall_user value to set _Firewall_user to
*/
public void setFirewall_user (String Firewall_user) {
	_Firewall_user = Firewall_user;
}

/**
sets _Firewall_user_PWD
@param Firewall_user_PWD value to set _Firewall_user_PWD to
*/
public void setFirewall_user_PWD (String Firewall_user_PWD) {
	_Firewall_user_PWD = Firewall_user_PWD;
}

/**
sets _Import_day
@param Import_day value to set _Import_day to
*/
public void setImport_day (String Import_day) {
	_Import_day = Import_day;
}

/**
sets _Import_delay
@param Import_delay value to set _Import_delay to
*/
public void setImport_delay (String Import_delay) {
	_Import_delay = Import_delay;
}

/**
sets _Import_hour
@param Import_hour value to set _Import_hour to
*/
public void setImport_hour (String Import_hour) {
	_Import_hour = Import_hour;
}

/**
sets _Import_minute
@param Import_minute value to set _Import_minute to
*/
public void setImport_minute (String Import_minute) {
	_Import_minute = Import_minute;
}

/**
sets _Import_month
@param Import_month value to set _Import_month to
*/
public void setImport_month (String Import_month) {
	_Import_month = Import_month;
}

/**
sets _Import_order
@param Import_order value to set _Import_order to
*/
public void setImport_order (long Import_order) {
	_Import_order = Import_order;
}

/**
sets _Import_second
@param Import_second value to set _Import_second to
*/
public void setImport_second (String Import_second) {
	_Import_second = Import_second;
}

/**
sets _Import_weekday 
@param Import_weekday value to set _Import_weekday to
*/
public void setImport_weekday (String Import_weekday) {
	_Import_weekday = Import_weekday;
}

/**
sets _Import_window
@param Import_window value to set _Import_window to
*/
public void setImport_window (String Import_window) {
	_Import_window = Import_window;
}

/**
sets _Import_year
@param Import_year value to set _Import_year to
*/
public void setImport_year (String Import_year) {
	_Import_year = Import_year;
}

/**
sets _ImportProduct_num
@param ImportProduct_num value to set _ImportProduct_num to
*/
public void setImportProduct_num (int ImportProduct_num) {
	_ImportProduct_num = ImportProduct_num;
}

/**
sets _IsActive
@param IsActive value to set _IsActive to
*/
public void setIsActive (String IsActive) {
	_IsActive = IsActive;
}

/**
sets _IsAutomated
@param IsAutomated value to set _IsAutomated to
*/
public void setIsAutomated (String IsAutomated) {
	_IsAutomated = IsAutomated;
}

/**
sets _IsInterval
@param IsInterval value to set _IsInterval to
*/
public void setIsInterval (String IsInterval) {
	_IsInterval = IsInterval;
}

/**
sets _Last_import_date
@param Last_import_date value to set _Last_import_date to
*/
public void setLast_import_date (Date Last_import_date) {
	_Last_import_date = Last_import_date;
}

/**
Sets _MeasLocGroup_num
@param MeasLocGroup_num value to put into _MeasLoc
*/
public void setMeasLocGroup_num(int MeasLocGroup_num) {
	_MeasLocGroup_num = MeasLocGroup_num;
}

/**
sets _Next_import_date
@param Next_import_date value to set _Next_import_date to
*/
public void setNext_import_date (Date Next_import_date) {
	_Next_import_date = Next_import_date;
}

/**
sets _Product_group
@param Product_group value to set _Product_group to
*/
public void setProduct_group (String Product_group) {
	_Product_group = Product_group;
}

/**
Sets _ProductGroup_num
@param ProductGroup_num value to put into _ProductGroup_num
*/
public void setProductGroup_num(int ProductGroup_num) {
	_ProductGroup_num = ProductGroup_num;
}

/**
sets _Product_name
@param Product_name value to set _Product_name to
*/
public void setProduct_name (String Product_name) {
	_Product_name = Product_name;
}

/**
sets _Product_type
@param Product_type value to set _Product_type to
*/
public void setProduct_type (String Product_type) {
	_Product_type = Product_type;
}

/**
sets _Properties
@param Properties value to set _Properties to
*/
public void setProperties (String Properties) {
	_Properties = Properties;
}

/**
sets _Retries
@param Retries value to set _Retries to
*/
public void setRetries (long Retries) {
	_Retries = Retries;
}

/**
sets _Source_URL_base
@param Source_URL_base value to _Source_URL_base
*/
public void setSource_URL_base (String Source_URL_base) {
	_Source_URL_base = Source_URL_base;
}

/**
sets _Source_URL_file
@param Source_URL_file avlue to set _Source_URL_file to
*/
public void setSource_URL_file (String Source_URL_file) {
	_Source_URL_file = Source_URL_file;
}

/**
sets _User_login
@param User_login value to set _User_login to
*/
public void setUser_login (String User_login) {
	_User_login = User_login;
}

/**
sets _User_PWD
@param User_PWD value to set _User_PWD to
*/
public void setUser_PWD (String User_PWD) {
	_User_PWD = User_PWD;
}

/** 
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_ImportProduct{" 			+ "\n" + 
		"ImportProduct_num:  " + _ImportProduct_num	+ "\n" +
		"Product_name:       " + _Product_name		+ "\n" + 
		"Product_type:       " + _Product_type		+ "\n" + 
		"IsActive:           " + _IsActive		+ "\n" + 
		"Product_group:      " + _Product_group		+ "\n" + 
		"Import_order:       " + _Import_order		+ "\n" + 
		"Last_import_date:   " + _Last_import_date	+ "\n" + 
		"Next_import_date:   " + _Next_import_date	+ "\n" + 
		"Retries:            " + _Retries		+ "\n" + 
		"User_login:         " + _User_login		+ "\n" + 
		"User_PWD:           " + _User_PWD		+ "\n" + 
		"Firewall_user:      " + _Firewall_user		+ "\n" + 
		"Firewall_user_PWD:  " + _Firewall_user_PWD	+ "\n" + 
		"Source_URL_base:    " + _Source_URL_base	+ "\n" + 
		"Source_URL_file:    " + _Source_URL_file	+ "\n" + 
		"Import_window:      " + _Import_window		+ "\n" + 
		"Add_source_URL_base:" + _Add_source_URL_base	+ "\n" + 
		"Add_source_URL_file:" + _Add_source_URL_file	+ "\n" + 
		"Destination_dir:    " + _Destination_dir	+ "\n" + 
		"Destination_file:   " + _Destination_file	+ "\n" + 
		"Properties:         " + _Properties		+ "\n" + 
		"IsAutomated:        " + _IsAutomated		+ "\n" + 
		"IsInterval:         " + _IsInterval		+ "\n" + 
		"Import_year:        " + _Import_year		+ "\n" + 
		"Import_month:       " + _Import_month		+ "\n" + 
		"Import_day:         " + _Import_day		+ "\n" + 
		"Import_hour:        " + _Import_hour		+ "\n" + 
		"Import_minute:      " + _Import_minute		+ "\n" + 
		"Import_second:      " + _Import_second		+ "\n" + 
		"Import_weekday:     " + _Import_weekday	+ "\n" + 
		"Import_delay:       " + _Import_delay		+ "\n" + 
		"DoArchive:          " + _DoArchive		+ "\n" + 
		"Archive_dir:        " + _Archive_dir		+ "\n" + 
		"Archive_fie:        " + _Archive_file		+ "\n" + 
		"ProductGroup_num:   " + _ProductGroup_num	+ "\n" + 
		"DBUser_num:         " + _DBUser_num		+ "\n" + 
		"DBGroup_num:        " + _DBGroup_num 		+ "\n" + 
		"DBPermissions:      '" + _DBPermissions	+ "'\n" +
		"MeasLocGroup_num:   " + _MeasLocGroup_num	+ "\n}\n";
}

}
