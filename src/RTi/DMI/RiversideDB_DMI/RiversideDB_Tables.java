// ----------------------------------------------------------------------------
// RiversideDB_Tables.java - class to store Tables data
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-06-06	Steven A. Malers, RTi	Initial version (copy and modify
//					RiversideDB_MeasType).
// 2002-07-11	J. Thomas Sapienza, RTi	Added the DB_file field and related
//					functions
// 2002-08-20	JTS, RTi		Made toString() more descriptive
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
//					In finalize(), set to null, not the
//					missing data values.
// 2003-04-22	JTS, RTi		Added the isReference and isTemplate
//					fields.
// 2003-06-05	JTS, RTi		Added IsT, DBUser_num, DBGroup_num,
//					Record_DBPermissions
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Date;
import java.util.Vector;

import RTi.DMI.DMI;
import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import RTi.Util.Message.Message;

/**
Store data from the Tables table.
*/
public class RiversideDB_Tables extends DMIDataObject
{

// From Tables table...
protected long   _Table_num            = DMIUtil.MISSING_LONG;
protected String _Table_name           = DMIUtil.MISSING_STRING;
protected Date   _Created              = DMIUtil.MISSING_DATE;
protected String _Description          = DMIUtil.MISSING_STRING;
protected long   _TableLayout_num      = DMIUtil.MISSING_LONG;
protected String _Archive              = DMIUtil.MISSING_STRING;
protected String _DB_file              = DMIUtil.MISSING_STRING;
protected long   _Active_days          = DMIUtil.MISSING_LONG;
protected String _Date_field           = DMIUtil.MISSING_STRING;
protected long   _Date_precision       = DMIUtil.MISSING_LONG;
protected String _Date_table           = DMIUtil.MISSING_STRING;
protected String _Date_table_Joinfield = DMIUtil.MISSING_STRING;
protected String _Joinfield            = DMIUtil.MISSING_STRING;
protected String _IsReference          = DMIUtil.MISSING_STRING;
protected String _IsTSTemplate         = DMIUtil.MISSING_STRING;
protected String _IsTS                 = DMIUtil.MISSING_STRING;
protected int    _DBUser_num           = DMIUtil.MISSING_INT;
protected int    _DBGroup_num          = DMIUtil.MISSING_INT;
protected String _Record_DBPermissions = DMIUtil.MISSING_STRING;

/**
Constructor.  
*/
public RiversideDB_Tables()
{	super();
}

/**
Cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null.
@exception Throwable if there is an error.
*/
protected void finalize()
throws Throwable
{	_Table_name   = null;
	_Created      = null;
	_Description  = null;
	_Archive      = null;
	_DB_file      = null;
	_Date_field   = null;
	_Date_table   = null;
	_Date_table_Joinfield = null;
	_Joinfield    = null;
	_IsReference  = null;
	_IsTSTemplate = null;
	_IsTS         = null;
	_Record_DBPermissions = null;
	super.finalize();
}

/**
returns _Active_days
@return _Active_days
*/
public long getActive_days() {
	return _Active_days;
}

/**
returns _Archive
@return _Archive
*/
public String getArchive() {
	return _Archive;
}

/**
returns _Created
@return _Created
*/
public Date getCreated() {
	return _Created;
}

/**
returns _Date_field
@return _Date_field
*/
public String getDate_field() {
	return _Date_field;
}

/**
returns _Date_precision
@return _Date_precision
*/
public long getDate_precision() {
	return _Date_precision;
}

/**
returns _Date_table
@return _Date_table
*/
public String getDate_table() {
	return _Date_table;
}

/**
returns _Date_table_Joinfield
@return _Date_table_Joinfield
*/
public String getDate_table_Joinfield() {
	return _Date_table_Joinfield;
}

/**
returns _DB_file
@return _DB_file
*/
public String getDB_file() {
	return _DB_file;
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
returns _Description
@return _Description
*/
public String getDescription() {
	return _Description;
}

/**
Returns _IsReference
@return _IsReference
*/
public String getIsReference() {
	return _IsReference;
}

/**
Returns _IsTS
@return _IsTS
*/
public String getIsTS() {
	return _IsTS;
}

/**
Returns _IsTSTemplate
@return _IsTSTemplate
*/
public String getIsTSTemplate() {
	return _IsTSTemplate;
}

/**
returns _Joinfield
@return _Joinfield
*/
public String getJoinfield() {
	return _Joinfield;
}

/**
Returns _Record_DBPermissions
@return _Record_DBPermissions
*/
public String getRecord_DBPermissions() {
	return _Record_DBPermissions;
}

/**
returns _Table_name
@return _Table_name
*/
public String getTable_name() {
	return _Table_name;
}

/**
returns _TableLayout_num
@return _TableLayout_num
*/
public long getTableLayout_num() {
	return _TableLayout_num;
}

/**
returns _Table_num
@return _Table_num
*/
public long getTable_num() {
	return _Table_num;
}

/**
Determine the position in a Vector of RiversideDB_Tables, using the
Table_num for the search.
@param tables Vector or RiversideDB_Tables to search.
@param Table_num value to compare in objects.
@return the vector position or -1 if not found.
*/
public static int indexOf ( Vector tables, long Table_num )
{	int size = 0;
	if ( tables != null ) {
		size = tables.size();
	}
	for ( int i = 0; i < size; i++ ) {
		if (	((RiversideDB_Tables)tables.elementAt(i))._Table_num ==
			Table_num ) {
			return i;
		}
	}
	return -1;
}

/**
sets _Active_days
@param Active_days values to put in _Active_days
*/
public void setActive_days(long Active_days) {
	_Active_days = Active_days;
}

/**
sets _Archive
@param Archive value to put in _Archive
*/
public void setArchive(String Archive) {
	_Archive = Archive;
}

/**
sets _Created
@param Created value to put in _Created
*/
public void setCreated(Date Created)
{	_Created = Created;
}

/**
sets _Date_field
@param Date_field value to put in _Date_field
*/
public void setDate_field(String Date_field) {
		_Date_field = Date_field;
}

/**
sets _Date_precision
@param Date_precision values to put in _Date_precision
*/
public void setDate_precision(long Date_precision) {
	_Date_precision = Date_precision;
}

/**
sets _Date_table
@param Date_table value to put in _Date_table
*/
public void setDate_table(String Date_table) {
	_Date_table = Date_table;
}

/**
sets _Date_table_Joinfield
@param Date_table_Joinfield value to put in _Date_table_Joinfield
*/
public void setDate_table_Joinfield(String Date_table_Joinfield) {
	_Date_table_Joinfield = Date_table_Joinfield;
}

/**
sets _DB_file
@param DB_file value to put in _DB_file
*/
public void setDB_file(String DB_file) { 
	_DB_file = DB_file;
}

/**
Sets _DBGroup_num
@param DBGroup_num value to put into _DBGroup_num
*/
public void setDBGroup_num(int DBGroup_num) {
	_DBGroup_num = DBGroup_num;
}

/**
Sets _DBUser_num
@param DBUser_num value to put into _DBUser_num
*/
public void setDBUser_num(int DBUser_num) {
	_DBUser_num = DBUser_num;
}

/**
sets _Description
@param Description value to put in _Description
*/
public void setDescription(String Description) {
	_Description = Description;
}

/**
Sets _IsReference
@param IsReference value to put in _IsReference
*/
public void setIsReference(String IsReference) {
	_IsReference = IsReference;
}

/**
Sets _IsTS
@param IsTS value to put into _IsTS
*/
public void setIsTS(String IsTS) {
	_IsTS = IsTS;
}

/**
Sets _IsTSTemplate
@param IsTSTemplate value to put into _IsTSTemplate
*/
public void setIsTSTemplate(String IsTSTemplate) {
	_IsTSTemplate = IsTSTemplate;
}

/**
sets _Joinfield
@param Joinfield value to put in _Joinfield
*/
public void setJoinfield(String Joinfield) {
		_Joinfield = Joinfield;
}

/**
Sets _Record_DBPermissions
@param Record_DBPermissions value to put into _Record_DBPermissions
*/
public void setRecord_DBPermissions(String Record_DBPermissions) {
	_Record_DBPermissions = Record_DBPermissions;
}

/**
sets _TableLayout_num
@param TableLayout_num values to put in _TableLayout_num
*/
public void setTableLayout_num(long TableLayout_num) {
	_TableLayout_num = TableLayout_num;
}

/**
sets _Table_name
@param Table_name value to put in _Table_name
*/
public void setTable_name(String Table_name) {
	_Table_name = Table_name;
}

/**
sets _Table_num
@param Table_num values to put in _Table_num
*/
public void setTable_num(long Table_num) {
	_Table_num = Table_num;
}

/** 
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return "RiversideDB_Tables{" 				+ "\n" + 
		"Table_num:             " + _Table_num		+ "\n" +
		"Table_name:            " + _Table_name		+ "\n" + 
		"Created:               " + _Created		+ "\n" +
		"Description:           " + _Description	+ "\n" +
		"TableLayout_num:       " + _TableLayout_num	+ "\n" +
		"Archive:               " + _Archive		+ "\n" + 
		"DB_file:               " + _DB_file		+ "\n" +
		"Active_days:           " + _Active_days	+ "\n" +
		"Date_field:            " + _Date_field		+ "\n" + 
		"Date_precision:        " + _Date_precision	+ "\n" +
		"Date_table:            " + _Date_table		+ "\n" + 
		"Date_table_Joinfield:  " + _Date_table_Joinfield+ "\n" + 
		"Joinfield:             " + _Joinfield		+ "\n" +
		"IsReference:           " + _IsReference 	+ "\n" +
		"IsTSTemplate:          " + _IsTSTemplate	+ "\n" +
		"IsTS:                  " + _IsTS 		+ "\n" + 
		"DBUser_num:            " + _DBUser_num		+ "\n" + 
		"DBGroup_num:           " + _DBGroup_num	+ "\n" + 
		"Record_DBPermissions: '" + _Record_DBPermissions+ "\n}\n";
}

}
