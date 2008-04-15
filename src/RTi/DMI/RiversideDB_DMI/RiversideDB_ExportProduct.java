// ----------------------------------------------------------------------------
// RiversideDB_ExportProduct.java - class for doing I/O with these values
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-05-22	J. Thomas Sapienza, RTi	Initial Version
// 2002-05-23	JTS, RTi		Added support for ExportProduct_num.
//                     			Added fillObjectVector method.  Added 
//					constructor, removed parentheses from 
//					returns
// 2002-05-29	JTS, RTi 		Added __dirty variable and methods.
// 2002-06-13	Steven A. Malers, RTi	Add getIsInterval(), setIsInterval().
// 2002-06-26	SAM, RTi		Remove I/O from code.
// 2002-08-20	JTS, RTi		made toString() more descriptive
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// 2003-06-02	JTS, RTi		Updated code for 02.08.00 db design:
//					added fields TSProduct_num, 
//					ProductGroup_num, DBUser_num,
//					DBGroup_num and DBPermissions
// 2003-06-05	JTS, RTi		Added copy constructor.
// 2003-06-18	JTS, RTi		Added MeasLocGroup_num field.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import java.util.Date;

/**
class to store data from the ExportProduct table
*/
public class RiversideDB_ExportProduct extends DMIDataObject
{

// from table ExportProduct
protected long	_ExportProduct_num = 	DMIUtil.MISSING_LONG;

protected String _Product_name = 	DMIUtil.MISSING_STRING;
protected String _Product_type = 	DMIUtil.MISSING_STRING;
protected String _IsActive = 		DMIUtil.MISSING_STRING;
protected String _Product_group	= 	DMIUtil.MISSING_STRING;
protected long 	_Export_order = 	DMIUtil.MISSING_LONG;
protected Date 	_Last_export_date = 	DMIUtil.MISSING_DATE;
protected Date 	_Next_export_date = 	DMIUtil.MISSING_DATE;
protected long 	_Retries = 		DMIUtil.MISSING_LONG;
protected String _User_login = 		DMIUtil.MISSING_STRING;
protected String _User_PWD = 		DMIUtil.MISSING_STRING;
protected String _Firewall_user	= 	DMIUtil.MISSING_STRING;
protected String _Firewall_user_PWD = 	DMIUtil.MISSING_STRING;
protected String _Export_start = 	DMIUtil.MISSING_STRING;
protected String _Export_end = 		DMIUtil.MISSING_STRING;
protected String _Destination_dir = 	DMIUtil.MISSING_STRING;
protected String _Destination_file = 	DMIUtil.MISSING_STRING;
protected String _Properties = 		DMIUtil.MISSING_STRING;
protected String _IsAutomated = 	DMIUtil.MISSING_STRING;
protected String _IsInterval = 		DMIUtil.MISSING_STRING;
protected String _Export_year = 	DMIUtil.MISSING_STRING;
protected String _Export_month = 	DMIUtil.MISSING_STRING;
protected String _Export_day = 		DMIUtil.MISSING_STRING;
protected String _Export_hour = 	DMIUtil.MISSING_STRING;
protected String _Export_minute = 	DMIUtil.MISSING_STRING;
protected String _Export_second = 	DMIUtil.MISSING_STRING;
protected String _Export_weekday = 	DMIUtil.MISSING_STRING;

// added for DB design 02.08.00
protected int _TSProduct_num = 		DMIUtil.MISSING_INT;
protected int _ProductGroup_num = 	DMIUtil.MISSING_INT;
protected int _DBUser_num = 		DMIUtil.MISSING_INT;
protected int _DBGroup_num = 		DMIUtil.MISSING_INT;
protected String _DBPermissions = 	DMIUtil.MISSING_STRING;
protected int _MeasLocGroup_num = 	DMIUtil.MISSING_INT;

/**
Constructor.  
*/
public RiversideDB_ExportProduct()
{	super();
}

/**
Copy constructor.
@param r the RiversideDB_ExportProduct object to copy.
*/
public RiversideDB_ExportProduct(RiversideDB_ExportProduct r) {
	setExportProduct_num(r.getExportProduct_num());
	setProduct_name(new String(r.getProduct_name()));
	setProduct_type(new String(r.getProduct_type()));
	setIsActive(new String(r.getIsActive()));
	setProduct_group(new String(r.getProduct_group()));
	setExport_order(r.getExport_order());
	
	Date tempDate = r.getLast_export_date();
	Date clonedDate = null;
	if (tempDate != null){ 
		clonedDate = new Date(tempDate.getTime());
		setLast_export_date(clonedDate);
	}

	tempDate = r.getNext_export_date();
	if (tempDate != null) {
		clonedDate = new Date(tempDate.getTime());
		setNext_export_date(clonedDate);
	}

	setRetries(r.getRetries());
	setUser_login(new String(r.getUser_login()));
	setUser_PWD(new String(r.getUser_PWD()));
	setFirewall_user(new String(r.getFirewall_user()));
	setFirewall_user_PWD(new String(r.getFirewall_user_PWD()));
	setExport_start(new String(r.getExport_start()));
	setExport_end(new String(r.getExport_end()));
	setDestination_dir(new String(r.getDestination_dir()));
	setDestination_file(new String(r.getDestination_file()));
	setProperties(new String(r.getProperties()));
	setIsAutomated(new String(r.getIsAutomated()));
	setIsInterval(new String(r.getIsInterval()));
	setExport_year(new String(r.getExport_year()));
	setExport_month(new String(r.getExport_month()));
	setExport_day(new String(r.getExport_day()));
	setExport_hour(new String(r.getExport_hour()));
	setExport_minute(new String(r.getExport_minute()));
	setExport_second(new String(r.getExport_second()));
	setExport_weekday(new String(r.getExport_weekday()));
	setTSProduct_num(r.getTSProduct_num());
	setProductGroup_num(r.getProductGroup_num());
	setDBUser_num(r.getDBUser_num());
	setDBGroup_num(r.getDBGroup_num());
	setDBPermissions(new String(r.getDBPermissions()));
	setMeasLocGroup_num(r.getMeasLocGroup_num());
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Destination_dir	= null;
	_Destination_file 	= null;
	_Export_day 		= null;
	_Export_end 		= null;
	_Export_hour 		= null;
	_Export_minute 		= null;
	_Export_month 		= null;
	_Export_second 		= null;
	_Export_start 		= null;
	_Export_weekday 	= null;
	_Export_year 		= null;
	_Firewall_user 		= null;
	_Firewall_user_PWD 	= null;
	_IsActive 		= null;
	_IsAutomated 		= null;
	_IsInterval 		= null;
	_Last_export_date 	= null;
	_Next_export_date 	= null;
	_Product_group 		= null;
	_Product_name 		= null;
	_Product_type 		= null;
	_Properties 		= null;
	_User_login 		= null;
	_User_PWD 		= null;

	_DBPermissions = null;

	super.finalize();
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
returns _Export_day
@return _Export_day
*/
public String getExport_day() {
	return _Export_day;
}

/**
returns _Export_end
@return _Export_end
*/
public String getExport_end() {
	return _Export_end;
}

/**
returns _Export_hour
@return _Export_hour
*/
public String getExport_hour() {
	return _Export_hour;
}

/**
returns _Export_minute
@return _Export_minute
*/
public String getExport_minute() {
	return _Export_minute;
}

/**
returns _Export_month
@return _Export_month
*/
public String getExport_month() {
	return _Export_month;
}

/**
returns _Export_order
@return _Export_order
*/
public long getExport_order() {
	return _Export_order;
}

/**
returns _Export_second
@return _Export_second
*/
public String getExport_second() {
	return _Export_second;
}

/**
returns _Export_start
@return _Export_start
*/
public String getExport_start() {
	return _Export_start;
}

/**
returns _Export_weekday
@return _Export_weekday
*/
public String getExport_weekday() {
	return _Export_weekday;
}

/**
returns _Export_year
@return _Export_year
*/
public String getExport_year() {
	return _Export_year;
}

/**
returns _ExportProduct_num
@return _ExportProduct_num
*/
public long getExportProduct_num() {
	return _ExportProduct_num;
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
returns _Last_export_date
@return _Last_export_date
*/
public Date getLast_export_date() {
	return _Last_export_date;
}

/**
Returns _MeasLocGroup_num
@return _MeasLocGroup_num
*/
public int getMeasLocGroup_num() {
	return _MeasLocGroup_num;
}

/**
returns _Next_export_date
@return _Next_export_date
*/
public Date getNext_export_date() {
	return _Next_export_date;
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
Returns _TSProduct_num
@return _TSProduct_num
*/
public int getTSProduct_num() {
	return _TSProduct_num;
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
public void setDestination_dir(String Destination_dir) {
	_Destination_dir = Destination_dir;
}

/**
sets _Destination_file
@param Destination_file value to set _Destination_dir to
*/
public void setDestination_file(String Destination_file) {
	_Destination_file = Destination_file;
}

/**
sets _Export_day
@param Export_day value to set _Export_day to
*/
public void setExport_day(String Export_day) {
	_Export_day = Export_day;
}

/**
sets _Export_end
@param Export_end value to set _Export_end to
*/
public void setExport_end(String Export_end) {
	_Export_end = Export_end;
}

/**
sets _Export_hour
@param Export_hour value to set _Export_hour to
*/
public void setExport_hour(String Export_hour) {
	_Export_hour = Export_hour;
}

/**
sets _Export_minute
@param Export_minute value to set _Export_minute to
*/
public void setExport_minute(String Export_minute) {
	_Export_minute = Export_minute;
}

/**
sets _Export_month
@param Export_month value to set _Export_month to
*/
public void setExport_month(String Export_month) {
	_Export_month = Export_month;
}

/** 
sets _Export_order
@param Export_order value to set _Export_order to
*/
public void setExport_order(long Export_order) {
	_Export_order = Export_order;
}

/**
sets _Export_second
@param Export_second value to set _Export_second to
*/
public void setExport_second(String Export_second) {
	_Export_second = Export_second;
}

/**
sets _Export_start
@param Export_start value to set _Export_start to
*/
public void setExport_start(String Export_start) {
	_Export_start = Export_start;
}

/**
sets _Export_weekday
@param Export_weekday value to _Export_weekday to
*/
public void setExport_weekday(String Export_weekday) {
	_Export_weekday = Export_weekday;
}

/**
sets _Export_year
@param Export_year value to set _Export_year to
*/
public void setExport_year(String Export_year) {
	_Export_year = Export_year;
}

/**
sets _ExportProduct_num
@param ExportProduct_num value to set _ExportProduct_num to
*/
public void setExportProduct_num(long ExportProduct_num) {
	_ExportProduct_num = ExportProduct_num;
}

/**
sets _Firewall_user
@param Firewall_user value to set _Firewall_user to
*/
public void setFirewall_user(String Firewall_user) {
	_Firewall_user = Firewall_user;
}

/**
sets _Firewall_user_PWD
@param Firewall_user_PWD value to set _Firewall_user_PWD to
*/
public void setFirewall_user_PWD(String Firewall_user_PWD) {
	_Firewall_user_PWD = Firewall_user_PWD;
}

/**
sets _IsActive
@param IsActive value to set _IsActive to
*/
public void setIsActive(String IsActive) {
	_IsActive = IsActive;
}

/**
sets _IsAutomated
@param IsAutomated value to set _IsAutomated to
*/
public void setIsAutomated(String IsAutomated) {
	_IsAutomated = IsAutomated;
}

/**
sets _IsInterval
@param IsInterval value to set _IsInterval to
*/
public void setIsInterval(String IsInterval) {
	_IsInterval = IsInterval;
}

/**
sets _Last_export_date
@param Last_export_date value to set _Last_export_date to
*/
public void setLast_export_date(Date Last_export_date) {
	_Last_export_date = Last_export_date;
}

/**
Sets _MeasLocGroup_num
@param MeasLocGroup_num value to put into _MeasLocGroup_num
*/
public void setMeasLocGroup_num(int MeasLocGroup_num) {	
	_MeasLocGroup_num = MeasLocGroup_num;
}

/**
sets _Next_export_date
@param Next_export_date value to set _Next_export_date to
*/
public void setNext_export_date(Date Next_export_date) {
	_Next_export_date = Next_export_date;
}

/**
sets _Product_group
@param Product_group value to set _Product_group to
*/
public void setProduct_group(String Product_group) {
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
public void setProduct_name(String Product_name) {
	_Product_name = Product_name;
}

/**
sets _Product_type
@param Product_type value to set _Product_type to
*/
public void setProduct_type(String Product_type) {
	_Product_type = Product_type;
}

/**
sets _Properties
@param Properties value to set _Properties to
*/
public void setProperties(String Properties) {
	_Properties = Properties;
}

/**
sets _Retries
@param Retries value to set _Retries to
*/
public void setRetries(long Retries) {
	_Retries = Retries;
}

/**
Sets _TSProduct_num
@param TSProduct_num value to put into _TSProduct_num
*/
public void setTSProduct_num(int TSProduct_num) {
	_TSProduct_num = TSProduct_num;
}

/**
sets _User_login
@param User_login value to set _User_login to
*/
public void setUser_login(String User_login) {
	_User_login = User_login;
}

/**
sets _User_PWD
@param User_PWD value to set _User_PWD to
*/
public void setUser_PWD(String User_PWD) {
	_User_PWD = User_PWD;
}

/** 
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_ExportProduct{" 			+ "\n" + 
		"ExportProduct_num:" + _ExportProduct_num	+ "\n" +
		"Product_name:     " + _Product_name		+ "\n" +
		"Product_type:     " + _Product_type		+ "\n" +
		"IsActive:         " + _IsActive		+ "\n" +
		"Product_group:    " + _Product_group		+ "\n" +
		"Export_order:     " + _Export_order		+ "\n" +
		"Last_export_date: " + _Last_export_date	+ "\n" +
		"Next_export_date: " + _Next_export_date	+ "\n" +
		"Retries:          " + _Retries			+ "\n" +
		"User_login:       " + _User_login		+ "\n" +
		"User_PWD:         " + _User_PWD		+ "\n" +
		"Firewall_user:    " + _Firewall_user		+ "\n" +
		"Firewall_user_PWD:" + _Firewall_user_PWD	+ "\n" +
		"Export_start:     " + _Export_start		+ "\n" +
		"Export_end:       " + _Export_end		+ "\n" +
		"Destination_dir:  " + _Destination_dir		+ "\n" +
		"Destination_file: " + _Destination_file	+ "\n" +
		"Properties:       " + _Properties		+ "\n" +
		"IsAutomated:      " + _IsAutomated		+ "\n" +
		"IsInterval:       " + _IsInterval		+ "\n" +
		"Export_year:      " + _Export_year		+ "\n" +
		"Export_month:     " + _Export_month		+ "\n" +
		"Export_day:       " + _Export_day		+ "\n" +
		"Export_hour:      " + _Export_hour		+ "\n" +
		"Export_minute:    " + _Export_minute		+ "\n" +
		"Export_second:    " + _Export_second		+ "\n" +
		"Export_weekday:   " + _Export_weekday		+ "\n" +
		"TSProduct_num:    " + _TSProduct_num		+ "\n" + 
		"ProductGroup_num: " + _ProductGroup_num	+ "\n" +
		"DBUser_num:       " + _DBUser_num 		+ "\n" +
		"DBGroup_num:      " + _DBGroup_num		+ "\n" +
		"DBPermissions:    '" + _DBPermissions 		+ "\n" +
		"MeasLocGroup_num: " + _MeasLocGroup_num	+ "\n}\n";
}

}
