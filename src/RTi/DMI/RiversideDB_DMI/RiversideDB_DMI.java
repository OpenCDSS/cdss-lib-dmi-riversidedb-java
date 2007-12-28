// ----------------------------------------------------------------------------
// RiversideDB_DMI.java - base class for all RiversideDB operations
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// Notes:
// (1) This class must override determineDatabaseVersion() and readGlobalData()
// in DMI.java
//
// To do:
//
// * the default password needs to be encrypted.
// ----------------------------------------------------------------------------
// History:
//
// 2002-05-22	J. Tom Sapienza, RTi	Began keeping the changelog.
//					Added methods for working with table
//					MeasType. Javadoc'd.
// 2002-05-23	JTS, RTi		Moved fillObjectVector out to the
//					individual table objects.
//					Added some new queries for
//					ImportProduct, ImportConf, ExportProduct
//					and ExportConf.
//					Minor coding changes (ie, removed
//					parenthese from returns,
//					alphabetization).
// 2002-05-28	JTS, RTi		Both methods of doing fillObjectVector
//					are in this code now, though the
//					cast-heavy version is commented out.
//					Tests will be run later on which is
//					better performance-wise.
//					Added several new queries for AMS for
//					use in the GUI.  First draft of several
//					TimeSeries queries written.
//					readXXX"For"Value has now become
//					readXXX"By"value.
// 2002-05-29	JTS, RTi		System.out.println's converted to using
//					Message class.
// 2002-05-30	JTS, RTi		Query name constants have been changed
//					from private static to protected.
//					readXXX"By"Value has turned back into 
//					readXXX"For"Value.
//					Query method names had primitive types
//					removed, and List appended if they
//					returned a Vector.
// 2002-05-31	JTS, RTi		Began changing queries from 'SELECT *'
//					to 'SELECT TableName.Field1,
//					TableName.Field2, ...'
// 2002-06-13	Steven A. Malers, RTi	Add readMeasLocList() method.
//					Alphabetize the case statements in
//					buildSQL().
// 2002-06-25	SAM, RTi		Restructure code to put all SQL-related
//					logic in this class (not individual
//					classes).  This centralizes database
//					knowledge and improves performance.
//					Implement versioning.  Alphabetize the
//					statement codes in buildSQL().
//					Add DMIStatement, DMIQuery classes to
//					store SQL to make the code threadsafe.
//					Remove main() - adds to code size and
//					need to test externally.
//					Add readGlobalData() to keep in memory
//					often-used data.
//					Change readMeasLocForMeasLocNum() to
//					take a long argument.
//					Add read methods for all tables
//					other than tables that are being phased
//					out and some time series -
//					will phase in additional methods as
//					needed.  Use fewer SQL statement
//					numbers since buildSQL typically builds
//					only the main part of the Statement.
// 2002-07-10	JTS, RTi		Started creating write functions, at
//					first with MeasType
// 2002-07-11	JTS, RTi		Created the rest of the write functions,
//					with the exception of those for the TS
//					tables.
//					Standardized the scheme used to name
//					the query constants.
// 2002-07-12	JTS, RTi		Changed all the calls to executeXXX to
//					"dmiXXXX" to avoid any confusion with
//					the executeXXX functions located in 
//					java.sql.Statement
//					Removed all dmiInserts and dmiUpdates
//					and replaced them with the appropriate
//					calls to dmiWrite (with the integer
//					flag).  Also changed the existing 
//					dmiWrite functions to use the integer
//					flag to determine operation.
// 2002-07-22	JTS, RTi		Error in writeImportProduct (not all
//					the columns were being added to the
//					write statment) fixed.  Missing WHERE
//					clause added to readGeolocForGeoloc_num
// 2002-07-23	JTS, RTi		Added support for using the testAddValue
//					method.  See below for heaps of comments
//					on where and how to use this.
//					writeGeoloc now returns a RiversideDB_
//					Geoloc object on a successful insert,
//					with the highest Geoloc_num autonumber
//					in theobject's geoloc_num field.
// 2002-07-24	JTS, RTi		Added default date format values for
// 					the date fields in the database. 
//					Compared to the database to see if they
//					had any formatting information, and if
//					not, set them to "yyyy-MM-dd HH:mm:ss"
//					New write methods for ImportProduct and
//					ExportProduct to handle the returning
//					of the most recent autonumber.  Also
//					added delete methods for Import and 
//					Export Product
// 2002-07-25	JTS, RTi		Added readMeasTypeForMeasLoc_num().
//					Corrected a bug in toMeasTypeList where
//					the MeasLoc_num field was never being 
//					set (or even read, actually)
// 2002-07-31	JTS, RTi		Added the first Stored Procedure calls
//					(used by Geoloc, end with _SP), though
//					the stored procedure that has a param
//					absolutely doesn't work, and no info
//					can be found explaining why.
// 2002-08-12	JTS, RTi		New delete methods added that make use
//					of the DMIDeleteStatement class
// 2002-08-15	JTS, RTi		Tried to clean up the muck that was
//					the numbering scheme for the constants
//					used to specify the query to execute.
//					Was an almost random hodgepodge of
//					numbers and spacing between tables.
//					Now each table has been spaced to a 
//					multiple of 100 and its queries inside
//					have been spaced to fit within.
//					Shouldn't ever run out of space, 
//					since ints go from 0 to 2^31
// 2002-08-19	JTS, RTi		Added a cascade delete for MeasLoc
// 2002-08-20	JTS, RTi		Patched MeasLoc's cascade delete.  
//					Added the code that was in DataUnits.
//					readUnitsFile() to the DMI.  Made 
//					changes for a new database version and
//					write javadoc info at the beginning of
//					the class comments 
// 2002-08-21	JTS, RTi		Added code to clean up after jdbc 
//					objects better by closing them out.
//					Introduced a finalize() method.
// 2002-10-23	JTS, RTi		Added VERSION_LATEST to "point" to the
//					latest DB Version value.  Changed 
//					setLogin and setPassword to 
//					setSystemLogin, setSystemPassword to 
//					match changes in DMI.
// 2002-12-09	JTS, RTi		Removed some old debugging code.
// 2002-12-24	SAM, RTi		* Remove main() - should not carry
//					  around in the package - test code
//					  should be in a separate application.
//					* Rework the constructors based on
//					  changes in the DMI base class -
//					  support for ODBC and internal URL
//					  should now be much clearer.
//					* Some DMI functionality is now in the
//					  DMIUtil class.
// 2003-04-22	JTS, RTi		* Added deleteMeasReducRelationForInput
//					  MeasType_num().
// 					* Added new version 02.08.00 and removed
//					  the old 03.00.00 version and put the
//					  code into the revisions directory.
//					* Added readTablesListForTSTemplate()
//					* Added readMeasTypeList()
// 2003-04-28	JTS, RTi		* For each UPDATE_INSERT write*
//					  method, added fields and values for
//					  where clauses.  These where clauses
//					  will be used when an UPDATE is 
//					  attempted in the UPDATE_INSERT code.
// 2003-04-30	JTS, RTi		Added methods for the new 'DBUser' table
// 2003-05-01	JTS, RTi		* Cleaned up some messy 'Tables' code
//					* Added versioning support for the 
//					  new 'Tables' fields.
// 2003-05-08	JTS, RTi		Removed the testAddValue calls.
// 2003-05-13	JTS, RTi		Reinstates the testAddValues calls on
//					a limited basis in a new form.
// 2003-05-15	JTS, RTi		Added readImportConfForMeasType_num.
// 2003-05-27	JTS, RTi		* Added readMeasTypeListForTSIdentBy
//					  Location().
//					* Added readImportConfListForImport
//					  ProductNumByLocation()
// 2003-05-28	JTS, RTi		* Added deleteImportConfForMeasType_num
// 2003-06-02	JTS, RTi		* Added the user security code (canRead,
//					  canWrite, canInsert, canUpdate, 
//					  canDelete)
//					* Added queries for the 
//					  RiversideDB_DBGroup and 
//					  RiversideDB_DBUserDBGroupRelation 
//					  tables
// 2003-06-03	JTS, RTi		* Updated writeImportProduct and 
//					  writeExportProduct for the new fields
//					* Added SQL for the ProductGroup,
//					  ProductType, TSProduct,
//					  TSProductProps, and AutoUpdateProduct
//					  tables.
//					* Updated some old design ideas, naming
//					  conventions, etc, to mesh with the
//					  design being doing with HydroBaseDMI.
//					* Revisited and revised most of the 
//					  javadocs.
//					* Replaced TSDate with DateTime and
//					  TSInterval with TimeInterval, 
//					  following SAM's upgrade to TS.
// 2003-06-05	JTS, RTi		* Changed all "DBUser"s to "DBUser_Num".
//					* Changed all "DBGroups"s to 
//					  "DBGroup_num".
//					* Added IsVisible to MeasType.
//					* Added IsTS, DBUser_num, DBGroup_num,
//					  Record_DBPermissions to Tables.
//					* Added Default_DBPermissions to DBUser.
//					* Added DBUser_num, DBGroup_num and
//					  DBPermissions to MeasLoc, MeasType,
// 2003-06-09	JTS, RTi		* Add PrimaryDBGroup_num to the DBUser
//					  queries.
// 2003-06-10	JTS, RTi		Corrected lots of small errors related
//					to the transition to the DB permissions
//					code
// 2003-06-11	JTS, RTi		* Miscellaneous bug fixes
//					* Added MeasLocGroup_num to all the 
//					  MeasLoc queries
//					* Added MeasLocGroup queries
//					* Added DBUserMeasLocGroupRelation
//					  queries.
// 2003-06-16	JTS, RTi		Added code to escape string parameters
//					to query where clauses (e.g., to catch
//					single-quotes)
// 2003-06-17	JTS, RTi		Added readDBUserMeasLocGroupRelation
//					ListForDBUser_num()
// 2003-06-18	JTS, RTi		* Added MeasLocGroup_num to 
//					  ImportProduct and ExportProduct
//					* Added code to delete and write records
//					  in the MeasLocGroup table
//					* Added code to delete records from the
//					  DBUserMeasLocGroupRelation table
// 2003-10-28	JTS, RTi		* Added 
//					* Added getMaxRecord().
//					* Added getMinRecord().
//					* Added getExtremeRecord().
//					* Added
//				     readStateGroupForMeasLocGroup_numRunDate().
//					* Added 
// 					 readStateListForStateGroup_numModule().
// 2003-10-29	JTS, RTi		Added MeasLocGroup_num to all the 
//					StateGroup queries.
// 2003-11-12	JTS, RTi		* Corrected writeStageDischargeRating().
//					* Corrected error in 
//					 readStateListForStateGroup_numModule().
// 					* Added
//					  deleteRatingTableForRatingTable_num().
//					* Switched addValue(Date, ...) over to
//					  use the new version of the method.
// 2003-11-13	JTS, RTi		* Updated deleteMeasLocForMeasLoc_num()
//					  to also delete records from the 
//					  RatingTable table.
//					* Updated 
//					deleteMeasLocGroupForMeasLocGroup_num().
//					* Added 
//					  deleteStateGroupForMeasLocGroup_num().
//					* Added deleteStateForStateGroup_num().
//					* Added 
//					readStateGroupListForMeasLocGroup_num().
// TO DO:
// 2003-10-20	Anne Morgan Love, RTi	* NEED TO add an additional flag to:
//					readMeasTypeListForTSIdent
//					to distinguish between MeasTypes for
//					MeasLocs with different Meas_loc_types
//					And to readMeasLocForIdentifier(x).
//
// 2004-01-05	AML, RTi		* Added return of MeasLoc object 
//					to writeMeasLoc() method to get the
//					MeasLoc_num autonum back.
// 2004-04-26	JTS, RTi		Added new debugging information to all
//					the permissions-checking methods.
// 2004-09-28	JTS, RTi		Began adding version 3.0.0 
//					functionality, including support for
//					the following new or changed tables:
//					- Operation
//					- OperationStateRelation
//					- State
// 2004-12-07 Luiz Teixeira, RTi	Added public method 
//						getRiversideDB_Tables () 
// 2004-12-20 Luiz Teixeira, RTi	Added methods readPropsForVariable()
//					        and deletePropsForVariable(),
// 2005-01-03 Luiz Teixeira, RTi	Upgraded MeasReduction, MeasScenario and
//					Scenario to use 'IsActive' instead of 
//					'Active' under _VERSION_030000_20041001
// 2005-01-03 Luiz Teixeira, RTi	Upgraded MeasType to use 'IsEditable
//					instead of 'Editable' under
//					_VERSION_030000_20041001
// 2005-01-03 Luiz Teixeira, RTi	Added the DBUser_num field of table 
//					Props under _VERSION_030000_20041001.	
// 2005-01-03 Luiz Teixeira, RTi	Upgraded TSProductProps to use 'Val'
//					instead of 'Value' under
//					_VERSION_030000_20041001
// 2005-01-10 Luiz Teixeira, RTi	Made _VERSION_030000_20041001 public 
//					to be used by the main application which
//					most likely than not, will not be 
//					derived	from the RiversideDB_DMI.
// 2005-06-14	JTS, RTi		Now implements TSProductDMI.
// 2005-08-19	JTS, RTi		Added code to support writing TSProducts
//					to the database.
// 2005-08-23	JTS, RTi		Updated the code for writing TSProducts
//					so that it checks permissions, validates
//					product group nums, etc.
// 2006-04-17	JTS, RTi		Implements the 
//					RTi.DataTest.AlertIOInterface.
// 2006-04-18	JTS, RTi		Implements RTi.TS.TSSupplier.
// 2006-05-03	JTS, RTi		Changed all calls to testAddValue() to
//					addValueOrNull().
// 2006-05-17	JTS, RTi		Changed readTimeSeries() to support 
//					querying time series based on 
//					passed-in MeasType_nums.
// 2006-10-12	SAM, RTi		When reading time series using the
//					MeasType_num, the TSID was not being set
//					correctly - fixed it.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import java.sql.CallableStatement;
import java.sql.ResultSet;

import java.util.Date;
import java.util.Vector;

import javax.swing.JFrame;

import RTi.DataTest.Action;
import RTi.DataTest.ActionDataModel;
import RTi.DataTest.AlertIOInterface;
import RTi.DataTest.Contact;
import RTi.DataTest.DataTest;
import RTi.DataTest.DataTestExpression;
import RTi.DataTest.DataTestFunction;
import RTi.DataTest.DataTestSide;
import RTi.DataTest.DataTestDataModel;
import RTi.DataTest.DataTestExpressionDataModel;
import RTi.DataTest.DataTestFunctionDataModel;
import RTi.DataTest.DataTestResult;
import RTi.DataTest.Severity;

import RTi.DMI.DMI;
import RTi.DMI.DMIDeleteStatement;
import RTi.DMI.DMISelectStatement;
import RTi.DMI.DMIWriteStatement;
import RTi.DMI.DMIStatement;
import RTi.DMI.DMIUtil;

import RTi.GRTS.TSProduct;
import RTi.GRTS.TSProductDMI;

import RTi.TS.DayTS;
import RTi.TS.HourTS;
import RTi.TS.IrregularTS;
import RTi.TS.MinuteTS;
import RTi.TS.MonthTS;
import RTi.TS.TS;
import RTi.TS.TSIdent;
import RTi.TS.TSSupplier;
import RTi.TS.YearTS;

import RTi.Util.GUI.JComboBoxResponseJDialog;
import RTi.Util.GUI.ResponseJDialog;

import RTi.Util.IO.PropList;
import RTi.Util.Message.Message;
import RTi.Util.Time.TimeInterval;

import RTi.Util.IO.DataUnits;
import RTi.Util.IO.DataDimension;
import RTi.Util.IO.IOUtil;
import RTi.Util.IO.Prop;

import RTi.Util.String.StringUtil;

import RTi.Util.Time.DateTime;

/**
The RiversideDB_DMI provides an interface to the RiversideDB database.

<b>SQL Method Naming Conventions</b><p>

The first word in the method name is one of the following:<br>
<ol>
<li>read</li>
<li>write</li>
<li>delete</li>
<li>count</li>
</ol>

The second part of the method name is the data object being operated on.
If a list is returned, then "List" is included in the method name.
Finally, if a select based on a where clause is used, the method includes the
field for the Where.  Examples are:

<ol>
<li>	readMeasTypeList</li>
<li>	readMeasTypeForMeasType_num</li>
</ol>

<p>
<b>Notes on versioning:</b><br>
Version changes require changes throughout the code base.  The following
example tells all the changes that need to be made when a new field is
added to an existing table:<p>
<ul>
<li>in buildSQL(), add the new field to all the select and write statement
sections for the appropriate table.  Do not forget to wrap this new code
with tests for the proper version (DMI.isDatabaseVersionAtLeast())</li>
<li>if, for the table XXXX, a method exists like:<br>
<code>private Vector toXXXXList</code><br>
then add the field to the Vector-filling code in this method</li>
<li>go to the RiversideDB_XXXX.java object that represents the table for
which this field was added.  Add the data member for the field, 
get/set statements, and then add the field (with brief information on the
version in which it was added) to the toString()</li>
<li>add the field, and the appropriate version-checking code, to the 
writeXXXX() method</li>
<li>update determineDatabaseVersion()</li>
</ul>
<p>
<b>User Permissions</b>
User permissions are determined on a record- or table-level basis.  Database
users are not the same as the login/password that is used to actually connect
to the RiversideDB database.  
<p>
In an application, once a user logs in, the database user should be set up
with a call to <tt>setDBUser</tt>.  This method sets a local variable in the 
DMI with the user's information, and also reads the user's group information
and stores it in the DMI.  
<p>
At that point, calls can be made to theDMI methods:<ul>
<li>canCreate</li>
<li>canDelete</li>
<li>canInsert</li>
<li>canRead</li>
<li>canUpdate</li>
<li>canWrite</li>
</ul>
to see if the user has the permissions to perform an action on a table or
record.
<p>
If the user group needs to be changed, a call can be made to 
<tt>changeCurrentGroup()</tt>
*/
public class RiversideDB_DMI 
extends DMI
implements AlertIOInterface, TSProductDMI, TSSupplier {

/**
RiversideDB design for RiverTrak 03.00.00 as of 2004-10-01, including the
following design elements:
<ol>
<li>New table 'Operation'</li>
<li>New table 'OperationStateRelation'</li>
<li>State table lost fields 'Module', 'Variable', 'Val', and 'Seq'</li>
<li>State table added fields 'OperationStateRelation_num', 'Sequence',
and 'ValueStr'</li>
Pending other changes...
</ol>
*/
public final static long _VERSION_030000_20041001 = 3000020041001L;
// This member was made public to be used by the main application, which most
// likely than not will not be derived from the RiversideDB_DMI.

protected final static long _VERSION_LATEST = _VERSION_030000_20041001;

/**
RiversideDB design for RiverTrak 02.08.00 as of 2003-04-22, including the 
following design elements:
<ol>
<li>Tables table got new field IsReference</li>
<li>Tables table got new field IsTSTemplate</li>
</ol>
*/
protected final static long _VERSION_020800_20030422 = 2080020030422L;

/**
RiversideDB version for RiverTrak 02.06.00 as of 2002-06-25, including the
following design elements:
<ol>
<li>	REVISIT - Need to document important items here</li>
</ol>
*/
protected final static long _VERSION_020601_20020625 = 2060120020625L;

/**
Table layout having fields MeasType_num, Date_Time, Revision_num, Val, Quality_flag.
*/
protected final int TABLE_LAYOUT_DATE_VALUE_TO_MINUTE = 100;

/**
Table layout having fields MeasType_num, Date_Time, Revision_num, Val, Duration, Quality_flag.
*/
protected final int TABLE_LAYOUT_DATE_VALUE_TO_MINUTE_WITH_DURATION = 102;

/**
Table layout having fields MeasType_num, Date_Time, Revision_num, Val, Quality_flag, Duration, Creation_Time.
*/
protected final int TABLE_LAYOUT_DATE_VALUE_TO_MINUTE_CREATION = 105;

// TODO (JTS - 2003-06-18)
// Remove all the _D_XXXXX methods -- they are easier to do simply as
// creating the SQL in the methods (e.g., "dmiDelete('DELETE * FROM XXXX');")

/**
Flags for doing specific select, write and delete queries, sorted by 
table name.  Descriptions of the actual queries are in the read*() methods.
*/
// Area
protected final int _S_AREA = 100;
protected final int _W_AREA = 150;
protected final int _D_AREA = 175;

// AutoUpdateProduct
protected final int _S_AUTOUPDATEPRODUCT = 187;
protected final int _W_AUTOUPDATEPRODUCT = 188;	// Future implementation
protected final int _D_AUTOUPDATEPRODUCT = 189;	// Future implementation

// DataDimension
protected final int _S_DATADIMENSION = 200;
protected final int _W_DATADIMENSION = 250;
protected final int _D_DATADIMENSION = 275;

// DataSource
protected final int _S_DATASOURCE = 300;
protected final int _W_DATASOURCE = 350;
protected final int _D_DATASOURCE = 375;

// DataType
protected final int _S_DATATYPE = 400;
protected final int _W_DATATYPE = 450;
protected final int _D_DATATYPE = 475;

// DataUnits
protected final int _S_DATAUNITS = 500;
protected final int _W_DATAUNITS = 550;
protected final int _D_DATAUNITS = 555;

// DBGroup
protected final int _S_DBGROUP = 563;

// DBUser
protected final int _S_DBUSER = 575;

// DBUserDBGroupRelation
protected final int _S_DBUSERDBGROUPRELATION = 587;

// DBUserMeasLocGroupRelation
protected final int _S_DBUSERMEASLOCGROUPRELATION = 593;
protected final int _W_DBUSERMEASLOCGROUPRELATION = 595;

// ExportConf
protected final int _S_EXPORTCONF = 600;
protected final int _W_EXPORTCONF = 650;

// ExportProduct
protected final int _S_EXPORTPRODUCT = 700;
protected final int _W_EXPORTPRODUCT = 750;
protected final int _D_EXPORTPRODUCT = 775;

// ExportType
protected final int _S_EXPORTTYPE = 800;
protected final int _W_EXPORTTYPE = 850;

// Geoloc
protected final int _S_GEOLOC = 900;
protected final int _W_GEOLOC = 950;

// ImportConf
protected final int _S_IMPORTCONF = 1000;
protected final int _W_IMPORTCONF = 1050;
protected final int _D_IMPORTCONF = 1075;

// ImportProduct
protected final int _S_IMPORTPRODUCT = 1100;
protected final int _W_IMPORTPRODUCT = 1150;
protected final int _D_IMPORTPRODUCT = 1175;

// ImportType
protected final int _S_IMPORTTYPE = 1200;
protected final int _W_IMPORTTYPE = 1250;
protected final int _D_IMPORTTYPE = 1275;

// MeasCreateMethod
protected final int _S_MEASCREATEMETHOD = 1300;
protected final int _W_MEASCREATEMETHOD = 1350;
protected final int _D_MEASCREATEMETHOD = 1375;

// MeasLoc
protected final int _S_MEASLOC = 1400;
protected final int _W_MEASLOC = 1450;
protected final int _D_MEASLOC = 1475;

// MeasLocGroup
protected final int _S_MEASLOCGROUP = 1487;
protected final int _W_MEASLOCGROUP = 1489;

// MeasLocType
protected final int _S_MEASLOCTYPE = 1500;
protected final int _W_MEASLOCTYPE = 1550;

// MeasQualityFlag
protected final int _S_MEASQUALITYFLAG = 1600;
protected final int _W_MEASQUALITYFLAG = 1650;
protected final int _D_MEASQUALITYFLAG = 1675;

// MeasReducGridWeight
protected final int _S_MEASREDUCGRIDWEIGHT = 1700;
protected final int _W_MEASREDUCGRIDWEIGHT = 1750;

// MeasReducRelation
protected final int _S_MEASREDUCRELATION = 1800;
protected final int _W_MEASREDUCRELATION = 1850;
protected final int _D_MEASREDUCRELATION = 1875;

// MeasReduction
protected final int _S_MEASREDUCTION = 1900;
protected final int _W_MEASREDUCTION = 1950;
protected final int _W_MEASREDUCTION_UPDATE = 1951;
protected final int _D_MEASREDUCTION = 1975;

// MeasReductionType
protected final int _S_MEASREDUCTIONTYPE = 2000;
protected final int _W_MEASREDUCTIONTYPE = 2050;
protected final int _D_MEASREDUCTIONTYPE = 2075;

// MeasScenario
protected final int _S_MEASSCENARIO = 2100;
protected final int _W_MEASSCENARIO = 2150;

// MeasScenarioRelation
protected final int _S_MEASSCENARIORELATION = 2200;
protected final int _W_MEASSCENARIORELATION = 2250;

// MeasTimeScale
protected final int _S_MEASTIMESCALE = 2300;
protected final int _W_MEASTIMESCALE = 2350;
protected final int _D_MEASTIMESCALE = 2375;

// MeasTransProtocol
protected final int _S_MEASTRANSPROTOCOL = 2400;
protected final int _W_MEASTRANSPROTOCOL = 2450;
protected final int _D_MEASTRANSPROTOCOL = 2475;

// MeasType
protected final int _S_MEASTYPE = 2500;
protected final int _W_MEASTYPE = 2550;
protected final int _D_MEASTYPE = 2575;

// MeasTypeStats
protected final int _S_MEASTYPESTATS = 2600;
protected final int _W_MEASTYPESTATS = 2650;
protected final int _D_MEASTYPESTATS = 2675;

// MeasTypeStatus
protected final int _S_MEASTYPESTATUS = 2700;
protected final int _W_MEASTYPESTATUS = 2750;
protected final int _D_MEASTYPESTATUS = 2775;

// MessageLog
protected final int _S_MESSAGELOG = 2800;
protected final int _W_MESSAGELOG = 2850;
protected final int _D_MESSAGELOG = 2860;

// Operation
protected final int _S_OPERATION = 2868;

// OperationStateRelation
protected final int _S_OPERATIONSTATERELATION = 2870;

// ProductGroup
protected final int _S_PRODUCTGROUP = 2875;
protected final int _W_PRODUCTGROUP = 2877;

// ProductGroup
protected final int _S_PRODUCTTYPE = 2893;

// Props
protected final int _S_PROPS = 2900;
protected final int _W_PROPS = 2950;

// RatingTable
protected final int _S_RATINGTABLE = 3000;
protected final int _W_RATINGTABLE = 3050;
protected final int _D_RATINGTABLE = 3100;

// Revision
protected final int _W_REVISION = 3150;

// Scenario
protected final int _S_SCENARIO = 3200;
protected final int _W_SCENARIO = 3250;
protected final int _D_SCENARIO = 3275;

// SHEFType
protected final int _S_SHEFTYPE = 3300;
protected final int _W_SHEFTYPE = 3350;
protected final int _D_SHEFTYPE = 3375;

// StageRatingDischarge
protected final int _S_STAGEDISCHARGERATING = 3400;
protected final int _W_STAGEDISCHARGERATING = 3450;
protected final int _D_STAGEDISCHARGERATING = 3475;

// State
protected final int _S_STATE = 3500;
protected final int _W_STATE = 3550;

// StateGroup
protected final int _S_STATEGROUP = 3600;
protected final int _W_STATEGROUP = 3650;

// Station
protected final int _S_STATION = 3700;
protected final int _W_STATION = 3750;
protected final int _D_STATION = 3775;

// TableLayout
protected final int _S_TABLELAYOUT = 3800;
protected final int _W_TABLELAYOUT = 3850;
protected final int _D_TABLELAYOUT = 3875;

// Tables
protected final int _S_TABLES = 3900;
protected final int _W_TABLES = 3950;

// TSProduct
protected final int _S_TSPRODUCT = 4000;
protected final int _W_TSPRODUCT_INSERT = 4001;
protected final int _W_TSPRODUCT_UPDATE = 4002;

// TSProductProps
protected final int _S_TSPRODUCTPROPS = 4025;
protected final int _W_TSPRODUCTPROPS = 4026;

// Version
protected final int _W_VERSION = 4050;

// AlertIOInterface queries
// Action
protected final int _S_ACTION = 5000;
protected final int _S_ACTIONCONTACT = 5060;
protected final int _S_ACTIONTYPE = 5005;
protected final int _W_ACTION = 5001;

// DataTest
protected final int _S_DATATEST = 5010;
protected final int _W_DATATEST = 5011;
protected final int _S_DATATESTACTION = 5012;

// DataTestExpression
protected final int _S_DATATESTEXPRESSION = 5020;
protected final int _W_DATATESTEXPRESSION = 5021;

// DataTestFunction
protected final int _S_DATATESTFUNCTION = 5030;
protected final int _W_DATATESTFUNCTION = 5031;
protected final int _S_DATATESTFUNCTIONINPUTID = 5032;
protected final int _W_DATATESTFUNCTIONINPUTID = 5033;

// DataTestStatus
protected final int _S_DATATESTSTATUS = 5040;
protected final int _W_DATATESTSTATUS = 5041;

// Contact
protected final int _S_CONTACT = 5050;

// Result
protected final int _S_DATATESTRESULT = 5100;
protected final int _W_DATATESTRESULT = 5101;

// Severity
protected final int _S_SEVERITYTYPES = 5200;

/**
Vector of RiversideDB_TableLayout, which are referenced when reading and writing time series.
*/
private Vector _RiversideDB_TableLayout_Vector = new Vector();

/**
Vector of RiversideDB_Tables, which are referenced when reading and writing time series.
*/
private Vector _RiversideDB_Tables_Vector = new Vector();

/**
The current user working in the database.  Initialized in the constructors
to be a new and empty RiversideDB_DBUser object.
*/
protected RiversideDB_DBUser _dbuser = null;
/**
The current user's user group.  Initialized in the constructors to be a new
an empty RiversideDB_DBGroup object.
*/
protected RiversideDB_DBGroup _dbgroup = null;

/** 
Constructor for a predefined ODBC DSN.
@param database_engine The database engine to use (see the DMI constructor).
@param odbc_name The ODBC DSN that has been defined on the machine.
@param system_login If not null, this is used as the system login to make the
connection.  If null, the default system login is used.
@param system_password If not null, this is used as the system password to make
the connection.  If null, the default system password is used.
*/
public RiversideDB_DMI (String database_engine, String odbc_name,
String system_login, String system_password)
throws Exception {
	// Use the default system login and password
	super ( database_engine, odbc_name, system_login, system_password );
	if ( system_login == null ) {
		// Use the default...
		setSystemLogin("admin");
	}
	if ( system_password == null ) {
		// Use the default...
		setSystemPassword("rivertrak");
	}
	setEditable(true);
	setSecure  (false);
	_dbuser  = new RiversideDB_DBUser();
	_dbgroup = new RiversideDB_DBGroup();
}

/** 
Constructor for a database server and database name, to use an automatically
created URL.
@param database_engine The database engine to use (see the DMI constructor).
@param database_server The IP address or DSN-resolvable database server
machine name.
@param database_name The database name on the server.  If null, default to
"RiversideDB".
@param port Port number used by the database.  If negative, default to that for
the database engine.
@param system_login If not null, this is used as the system login to make the
connection.  If null, the default system login is used.
@param system_password If not null, this is used as the system password to make
the connection.  If null, the default system password is used.
*/
public RiversideDB_DMI (String database_engine, String database_server,
String database_name, int port, String system_login, String system_password)
throws Exception {
	// Use the default system login and password
	super ( database_engine, database_server, database_name, port,
		system_login, system_password );
	if ( system_login == null ) {
		// Use the default...
		setSystemLogin("admin");
	}
	if ( system_password == null ) {
		// Use the default...
		setSystemPassword("rivertrak");
	}
	setEditable(true);
	setSecure  (false);

	_dbuser  = new RiversideDB_DBUser();
	_dbgroup = new RiversideDB_DBGroup();
}

// A FUNCTIONS
// B FUNCTIONS

/** 
Build an SQL string based on a requested SQL statement code.  This defines 
the basic statement and allows overloaded methods to avoid redundant code.
This method is used to eliminate redundant code where methods use the same
basic statement but with different where clauses.
@param statement Statement to set values in.
@param sqlNumber the number of the SQL statement to build.  Usually defined
as a private constant as a mnemonic aid.
@throws Exception if an error occurs
*/
private void buildSQL ( DMIStatement statement, int sqlNumber )
throws Exception {
	DMISelectStatement select;
	DMIWriteStatement write;
	DMIDeleteStatement del;
	switch ( sqlNumber ) {
//////////////////////////////////////////////////////
// Area
//////////////////////////////////////////////////////
		case _S_AREA:
			select = (DMISelectStatement)statement;
	/* AutoNum */	select.addField ( "Area.Area_num" );
			select.addField ( "Area.MeasLoc_num" );
			select.addField ( "Area.Area_id" );
			select.addField ( "Area.Area_name" );
			select.addField ( "Area.Area" );
			select.addField ( "Area.Area_units" );
			select.addTable ( "Area" );
			break;
		case _W_AREA:
			write = (DMIWriteStatement)statement;
			write.addField ( "MeasLoc_num" );
			write.addField ( "Area_id" );
			write.addField ( "Area_name" );
			write.addField ( "Area" );
			write.addField ( "Area_units" );
			write.addTable ( "Area" );
			break;
		case _D_AREA:
			del = (DMIDeleteStatement)statement;
			del.addTable("Area");
			break;
//////////////////////////////////////////////////////
// AutoUpdateProduct
//////////////////////////////////////////////////////			
		case _S_AUTOUPDATEPRODUCT:
			select = (DMISelectStatement)statement;
	/* AutoNum */	select.addField(
				"AutoUpdateProduct.AutoUpdateProduct_num");
			select.addField("AutoUpdateProduct.TSProduct_num");
			select.addField("AutoUpdateProduct.ProductGroup_num");
			select.addField("AutoUpdateProduct.Identifier");
			select.addField("AutoUpdateProduct.Name");
			select.addField("AutoUpdateProduct.Comment");
			select.addField("AutoUpdateProduct.IsEnabled");
			select.addField("AutoUpdateProduct.X");
			select.addField("AutoUpdateProduct.Y");
			select.addField("AutoUpdateProduct.Width");
			select.addField("AutoUpdateProduct.Height");
			select.addField("AutoUpdateProduct.Properties");
			select.addField("AutoUpdateProduct.DBUser_num");
			select.addField("AutoUpdateProduct.DBGroup_num");
			select.addField("AutoUpdateProduct.DBPermissions");
			select.addTable("AutoUpdateProduct");
			break;
//////////////////////////////////////////////////////
// DataDimension
//////////////////////////////////////////////////////			
		case _S_DATADIMENSION:
			select = (DMISelectStatement)statement;
			select.addField ( "DataDimension.Dimension" );
			select.addField ( "DataDimension.Description" );
			select.addTable ( "DataDimension" );
			break;
		case _W_DATADIMENSION:
			write = (DMIWriteStatement)statement;
			write.addField ( "Dimension" );
			write.addField ( "Description" );
			write.addTable ( "DataDimension" );
			break;
		case _D_DATADIMENSION:
			del = (DMIDeleteStatement)statement;
			del.addTable("DataDimension");
			break;
//////////////////////////////////////////////////////
// DataSource
//////////////////////////////////////////////////////			
		case _S_DATASOURCE:
			select = (DMISelectStatement)statement;
			select.addField ( "DataSource.Source_abbrev" );
			select.addField ( "DataSource.Source_name" );
			select.addTable ( "DataSource" );
			break;			
		case _W_DATASOURCE:
			write = (DMIWriteStatement)statement;
			write.addField ( "Source_abbrev" );
			write.addField ( "Source_name" );
			write.addTable ( "DataSource" );
			break;
		case _D_DATASOURCE:
			del = (DMIDeleteStatement)statement;
			del.addTable("DataSource");
			break;
//////////////////////////////////////////////////////
// DataType
//////////////////////////////////////////////////////			
		case _S_DATATYPE:
			select = (DMISelectStatement)statement;
			select.addField ( "DataType.DataType" );
			select.addField ( "DataType.Description" );
			select.addField ( "DataType.Dimension" );
			select.addField ( "DataType.Meas_time_scale" );
			select.addField ( "DataType.Meas_loc_type" );
			select.addField ( "DataType.Record_type" );
			select.addField ( "DataType.Default_engl_units" );
			select.addField ( "DataType.Default_si_units" );
			select.addField ( "DataType.SHEF_pe" );
			select.addField ( "DataType.Default_engl_min" );
			select.addField ( "DataType.Default_engl_max" );
			select.addField ( "DataType.Default_si_min" );
			select.addField ( "DataType.Default_si_max" );
			select.addTable ( "DataType" );
			break;
		case _W_DATATYPE:
			write = (DMIWriteStatement)statement;
			write.addField ( "DataType" );
			write.addField ( "Description" );
			write.addField ( "Dimension" );
			write.addField ( "Meas_time_scale" );
			write.addField ( "Meas_loc_type" );
			write.addField ( "Record_type" );
			write.addField ( "Default_engl_units" );
			write.addField ( "Default_si_units" );
			write.addField ( "SHEF_pe" );
			write.addField ( "Default_engl_min" );
			write.addField ( "Default_engl_max" );
			write.addField ( "Default_si_min" );
			write.addField ( "Default_si_max" );
			write.addTable ( "DataType" );
			break;
		case _D_DATATYPE:
			del = (DMIDeleteStatement)statement;
			del.addTable("DataType");
			break;
//////////////////////////////////////////////////////
// DataUnits
//////////////////////////////////////////////////////
		case _S_DATAUNITS:
			select = (DMISelectStatement)statement;
			select.addField ( "DataUnits.Units_abbrev" );
			select.addField ( "DataUnits.Units_description" );
			select.addField ( "DataUnits.Output_precision" );
			select.addField ( "DataUnits.Dimension" );
			select.addField ( "DataUnits.Base_unit" );
			select.addField ( "DataUnits.Mult_factor" );
			select.addField ( "DataUnits.Add_factor" );
			select.addField ( "DataUnits.Units_system" );
			select.addTable ( "DataUnits" );
			break;
		case _W_DATAUNITS:
			write = (DMIWriteStatement)statement;
			write.addField ( "Units_abbrev" );
			write.addField ( "Units_description" );
			write.addField ( "Output_precision" );
			write.addField ( "Dimension" );
			write.addField ( "Base_unit" );
			write.addField ( "Mult_factor" );
			write.addField ( "Add_factor" );
			write.addField ( "Units_system" );
			write.addTable ( "DataUnits" );
			break;
		case _D_DATAUNITS:
			del = (DMIDeleteStatement)statement;
			del.addTable("DataUnits");
			break;
//////////////////////////////////////////////////////
// DBGroup
//////////////////////////////////////////////////////			
		case _S_DBGROUP:
			select = (DMISelectStatement)statement;
	/* AutoNum */	select.addField("DBGroup.DBGroup_num");
			select.addField("DBGroup.Description");
			select.addField("DBGroup.ID");
			select.addTable("DBGroup");
			break;
//////////////////////////////////////////////////////
// DBUser
//////////////////////////////////////////////////////			
		case _S_DBUSER:
			select = (DMISelectStatement)statement;
	/* AutoNum */	select.addField("DBUser.DBUser_num");
			select.addField("DBUser.Description");
			select.addField("DBUser.Login");
			select.addField("DBUser.Password");
			select.addField("DBUser.Default_DBPermissions");
			select.addField("DBUser.PrimaryDBGroup_num");
			select.addTable("DBUser");
			break;			
//////////////////////////////////////////////////////
// DBUserDBGroupRelation
//////////////////////////////////////////////////////			
		case _S_DBUSERDBGROUPRELATION:
			select = (DMISelectStatement)statement;
			select.addField("DBUserDBGroupRelation.DBUser_num");
			select.addField("DBUserDBGroupRelation.DBGroup_num");
			select.addTable("DBUserDBGroupRelation");
			break;
//////////////////////////////////////////////////////
// DBUserMeasLocGroupRelation
//////////////////////////////////////////////////////			
		case _S_DBUSERMEASLOCGROUPRELATION:
			select = (DMISelectStatement)statement;
			select.addField(
				"DBUserMeasLocGroupRelation.DBUser_num");
			select.addField(
				"DBUserMeasLocGroupRelation.MeasLocGroup_num");
			select.addTable("DBUserMeasLocGroupRelation");
			break;			
		case _W_DBUSERMEASLOCGROUPRELATION:
			write = (DMIWriteStatement)statement;
			write.addField("DBUser_num");
			write.addField("MeasLocGroup_num");
			write.addTable("DBUserMeasLocGroupRelation");
			break;
//////////////////////////////////////////////////////
// ExportConf
//////////////////////////////////////////////////////			
		case _S_EXPORTCONF:
			select = (DMISelectStatement)statement;
			select.addField ( "ExportConf.ExportProduct_num" );
			select.addField ( "ExportConf.MeasType_num" );
			select.addField ( "ExportConf.Export_id" );
			select.addField ( "ExportConf.Export_units" );
			select.addField ( "ExportConf.IsActive" );
			select.addTable ( "ExportConf" );
			break;
		case _W_EXPORTCONF:
			write = (DMIWriteStatement)statement;
			write.addField ( "ExportProduct_num" );
			write.addField ( "MeasType_num" );
			write.addField ( "Export_id" );
			write.addField ( "Export_units" );
			write.addField ( "IsActive" );
			write.addTable ( "ExportConf" );
			break;	
//////////////////////////////////////////////////////
// ExportProduct
//////////////////////////////////////////////////////			
		case _S_EXPORTPRODUCT:
	/* AutoNum */	select = (DMISelectStatement)statement;
			select.addField ( "ExportProduct.ExportProduct_num" );
			select.addField ( "ExportProduct.Product_name" );
			select.addField ( "ExportProduct.Product_type" );
			select.addField ( "ExportProduct.IsActive" );
// REVISIT (JTS - 2003-06-02)
// This will be phased out later			
//			if (getDatabaseVersion() < _VERSION_020800_20030422) {
				select.addField("ExportProduct.Product_group");
//			}
			select.addField ( "ExportProduct.Export_order" );
			select.addField ( "ExportProduct.Last_export_date" );
			select.addField ( "ExportProduct.Next_export_date" );
			select.addField ( "ExportProduct.Retries" );
			select.addField ( "ExportProduct.User_login" );
			select.addField ( "ExportProduct.User_PWD" );
			select.addField ( "ExportProduct.Firewall_user" );
			select.addField ( "ExportProduct.Firewall_user_PWD" );
			select.addField ( "ExportProduct.Export_start" );
			select.addField ( "ExportProduct.Export_end" );
			select.addField ( "ExportProduct.Destination_dir" );
			select.addField ( "ExportProduct.Destination_file" );
			select.addField ( "ExportProduct.Properties" );
			select.addField ( "ExportProduct.IsAutomated" );
			select.addField ( "ExportProduct.IsInterval" );
			select.addField ( "ExportProduct.Export_year" );
			select.addField ( "ExportProduct.Export_month" );
			select.addField ( "ExportProduct.Export_day" );
			select.addField ( "ExportProduct.Export_hour" );
			select.addField ( "ExportProduct.Export_minute" );
			select.addField ( "ExportProduct.Export_second" );
			select.addField ( "ExportProduct.Export_weekday" );
			if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)){
				select.addField("ExportProduct.TSProduct_num");
				select.addField(
					"ExportProduct.ProductGroup_num");
				select.addField("ExportProduct.DBUser_num");
				select.addField("ExportProduct.DBGroup_num");
				select.addField("ExportProduct.DBPermissions");
				select.addField(
					"ExportProduct.MeasLocGroup_num");
			}
			select.addTable ( "ExportProduct" );
			break;
		case _W_EXPORTPRODUCT:
			write = (DMIWriteStatement)statement;
			write.addField ( "Product_name" );
			write.addField ( "Product_type" );
			write.addField ( "IsActive" );
// REVISIT (JTS - 2003-06-02)
// This will be phased out later			
//			if (getDatabaseVersion() < _VERSION_020800_20030422) {
				write.addField ( "Product_group" );
//			}
			write.addField ( "Export_order" );
			write.addField ( "Last_export_date" );
			write.addField ( "Next_export_date" );
			write.addField ( "Retries" );
			write.addField ( "User_login" );
			write.addField ( "User_PWD" );
			write.addField ( "Firewall_user" );
			write.addField ( "Firewall_user_PWD" );
			write.addField ( "Export_start" );
			write.addField ( "Export_end" );
			write.addField ( "Destination_dir" );
			write.addField ( "Destination_file" );
			write.addField ( "Properties" );
			write.addField ( "IsAutomated" );
			write.addField ( "IsInterval" );
			write.addField ( "Export_year" );
			write.addField ( "Export_month" );
			write.addField ( "Export_day" );
			write.addField ( "Export_hour" );
			write.addField ( "Export_minute" );
			write.addField ( "Export_second" );
			write.addField ( "Export_weekday" );
			if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)){
				write.addField("ExportProduct.TSProduct_num");
				write.addField(
					"ExportProduct.ProductGroup_num");
				write.addField("ExportProduct.DBUser_num");
				write.addField("ExportProduct.DBGroup_num");
				write.addField("ExportProduct.DBPermissions");
				write.addField(
					"ExportProduct.MeasLocGroup_num");
			}			
			write.addTable ( "ExportProduct" );
			break;			
		case _D_EXPORTPRODUCT:
			del = (DMIDeleteStatement)statement;
			del.addTable ("ExportProduct");
			break;
//////////////////////////////////////////////////////
// ExportType
//////////////////////////////////////////////////////			
		case _S_EXPORTTYPE:
			select = (DMISelectStatement)statement;
			select.addField ( "ExportType.Name" );
			select.addField ( "ExportType.Comment" );
			select.addTable ( "ExportType" );
			break;			
		case _W_EXPORTTYPE:
			write = (DMIWriteStatement)statement;
			write.addField ( "Name" );
			write.addField ( "Comment" );
			write.addTable ( "ExportType" );
			break;
//////////////////////////////////////////////////////
// Geoloc
//////////////////////////////////////////////////////			
		case _S_GEOLOC:
			select = (DMISelectStatement)statement;
	/* AutoNum */	select.addField ( "Geoloc.Geoloc_num" );
			select.addField ( "Geoloc.Latitude" );
			select.addField ( "Geoloc.Longitude" );
			select.addField ( "Geoloc.X" );
			select.addField ( "Geoloc.Y" );
			select.addField ( "Geoloc.Country" );
			select.addField ( "Geoloc.State" );
			select.addField ( "Geoloc.County" );
			select.addField ( "Geoloc.Elevation" );
			select.addField ( "Geoloc.Elevation_units" );
			select.addTable ( "Geoloc" );
			break;
		case _W_GEOLOC:
			write = (DMIWriteStatement)statement;
			write.addField ( "Latitude" );
			write.addField ( "Longitude" );
			write.addField ( "X" );
			write.addField ( "Y" );
			write.addField ( "Country" );
			write.addField ( "State" );
			write.addField ( "County" );
			write.addField ( "Elevation" );
			write.addField ( "Elevation_units" );
			write.addTable ( "Geoloc" );
			break;
//////////////////////////////////////////////////////
// ImportConf
//////////////////////////////////////////////////////
		case _S_IMPORTCONF:
			select = (DMISelectStatement)statement;
			select.addField ( "ImportConf.ImportProduct_num" );
			select.addField ( "ImportConf.MeasType_num" );
			select.addField ( "ImportConf.External_table" );
			select.addField ( "ImportConf.External_field" );
			select.addField ( "ImportConf.External_id" );
			select.addField ( "ImportConf.External_units" );
			select.addField ( "ImportConf.IsActive");
			select.addTable ( "ImportConf" );
			break;
		case _W_IMPORTCONF:
			write = (DMIWriteStatement)statement;
			write.addField ( "ImportProduct_num" );
			write.addField ( "MeasType_num" );
			write.addField ( "External_table" );
			write.addField ( "External_field" );
			write.addField ( "External_id" );
			write.addField ( "External_units" );
			write.addField ( "IsActive" );
			write.addTable ( "ImportConf" );
			break;
		case _D_IMPORTCONF:
			del = (DMIDeleteStatement)statement;
			del.addTable("ImportConf");
			break;
//////////////////////////////////////////////////////
// ImportProduct
//////////////////////////////////////////////////////			
		case _S_IMPORTPRODUCT:
			select = (DMISelectStatement)statement;
	/* AutoNum */	select.addField ( "ImportProduct.ImportProduct_num" );
			select.addField ( "ImportProduct.Product_name" );
			select.addField ( "ImportProduct.Product_type" );
			select.addField ( "ImportProduct.IsActive" );
// REVISIT (JTS - 2003-06-02)
// This will be phased out later
//			if (getDatabaseVersion() < _VERSION_020800_20030422) {
				select.addField("ImportProduct.Product_group");
//			}
			select.addField ( "ImportProduct.Import_order" );
			select.addField ( "ImportProduct.Last_import_date" );
			select.addField ( "ImportProduct.Next_import_date" );
			select.addField ( "ImportProduct.Retries" );
			select.addField ( "ImportProduct.User_login" );
			select.addField ( "ImportProduct.User_PWD" );
			select.addField ( "ImportProduct.Firewall_user" );
			select.addField ( "ImportProduct.Firewall_user_PWD" );
			select.addField ( "ImportProduct.Source_URL_base" );
			select.addField ( "ImportProduct.Source_URL_file" );
			select.addField ( "ImportProduct.Import_window" );
			select.addField ( "ImportProduct.Add_source_URL_base" );
			select.addField ( "ImportProduct.Add_source_URL_file" );
			select.addField ( "ImportProduct.Destination_dir" );
			select.addField ( "ImportProduct.Destination_file" );
			select.addField ( "ImportProduct.Properties" );
			select.addField ( "ImportProduct.IsAutomated" );
			select.addField ( "ImportProduct.IsInterval" );
			select.addField ( "ImportProduct.Import_year" );
			select.addField ( "ImportProduct.Import_month" );
			select.addField ( "ImportProduct.Import_day" );
			select.addField ( "ImportProduct.Import_hour" );
			select.addField ( "ImportProduct.Import_minute" );
			select.addField ( "ImportProduct.Import_second" );
			select.addField ( "ImportProduct.Import_weekday" );
			select.addField ( "ImportProduct.Import_delay" );
			select.addField ( "ImportProduct.DoArchive" );
			select.addField ( "ImportProduct.Archive_dir" );
			select.addField ( "ImportProduct.Archive_file" );
			if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)){
				select.addField(
					"ImportProduct.ProductGroup_num");
				select.addField("ImportProduct.DBUser_num");
				select.addField("ImportProduct.DBGroup_num");
				select.addField("ImportProduct.DBPermissions");
				select.addField(
					"ImportProduct.MeasLocGroup_num");
			}						
			select.addTable ( "ImportProduct" );
			break;
		case _W_IMPORTPRODUCT:
			write = (DMIWriteStatement)statement;
			write.addField ( "Product_name" );
			write.addField ( "Product_type" );
			write.addField ( "IsActive" );
// REVISIT (JTS - 2003-06-02)
// This will be phased out later			
//			if (getDatabaseVersion() < _VERSION_020800_20030422) {
				write.addField("ImportProduct.Product_group");
//			}			
			write.addField ( "Import_order" );
			write.addField ( "Last_import_date" );
			write.addField ( "Next_import_date" );
			write.addField ( "Retries" );
			write.addField ( "User_login" );
			write.addField ( "User_PWD" );
			write.addField ( "Firewall_user" );
			write.addField ( "Firewall_user_PWD" );
			write.addField ( "Source_URL_base" );
			write.addField ( "Source_URL_file" );
			write.addField ( "Import_window" );
			write.addField ( "Add_source_URL_base" );
			write.addField ( "Add_source_URL_file" );
			write.addField ( "Destination_dir" );
			write.addField ( "Destination_file" );
			write.addField ( "Properties" );
			write.addField ( "IsAutomated" );
			write.addField ( "IsInterval" );
			write.addField ( "Import_year" );
			write.addField ( "Import_month" );
			write.addField ( "Import_day" );
			write.addField ( "Import_hour" );
			write.addField ( "Import_minute" );
			write.addField ( "Import_second" );
			write.addField ( "Import_weekday" );
			write.addField ( "Import_delay" );
			write.addField ( "DoArchive" );
			write.addField ( "Archive_dir" );
			write.addField ( "Archive_file" );
			if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)){
				write.addField(
					"ImportProduct.ProductGroup_num");
				write.addField("ImportProduct.DBUser_num");
				write.addField("ImportProduct.DBGroup_num");
				write.addField("ImportProduct.DBPermissions");
				write.addField(
					"ImportProduct.MeasLocGroup_num");
			}
			write.addTable ( "ImportProduct" );
			break;			
		case _D_IMPORTPRODUCT:
			del = (DMIDeleteStatement)statement;
			del.addTable ( "ImportProduct" );
			break;
//////////////////////////////////////////////////////
// ImportType
//////////////////////////////////////////////////////			
		case _S_IMPORTTYPE:
			select = (DMISelectStatement)statement;
			select.addField ( "ImportType.Name" );
			select.addField ( "ImportType.Comment" );
			select.addTable ( "ImportType" );
			break;			
		case _W_IMPORTTYPE:
			write = (DMIWriteStatement)statement;
			write.addField ( "Name" );
			write.addField ( "Comment" );
			write.addTable ( "ImportType" );
			break;
		case _D_IMPORTTYPE:
			del = (DMIDeleteStatement)statement;
			del.addTable("ImportType");
			break;
//////////////////////////////////////////////////////
// MeasCreateMethod
//////////////////////////////////////////////////////			
		case _S_MEASCREATEMETHOD:
			select = (DMISelectStatement)statement;
			select.addField ( "MeasCreateMethod.Method" );
			select.addField ( "MeasCreateMethod.Description" );
			select.addTable ( "MeasCreateMethod" );
			break;			
		case _W_MEASCREATEMETHOD:
			write = (DMIWriteStatement)statement;
			write.addField ( "Method" );
			write.addField ( "Description" );
			write.addTable ( "MeasCreateMethod" );
			break;
		case _D_MEASCREATEMETHOD:
			del = (DMIDeleteStatement)statement;
			del.addTable("MeasCreateMethod");
			break;
//////////////////////////////////////////////////////
// MeasLoc
//////////////////////////////////////////////////////			
		case _S_MEASLOC:
			select = (DMISelectStatement)statement;
	/* AutoNum */	select.addField ( "MeasLoc.MeasLoc_num" );
			select.addField ( "MeasLoc.Geoloc_num" ); 
			select.addField ( "MeasLoc.Identifier" );
			select.addField ( "MeasLoc.MeasLoc_name" );
			select.addField ( "MeasLoc.Source_abbrev" );
			select.addField ( "MeasLoc.Meas_loc_type" );
			select.addField ( "MeasLoc.Comment" );
			if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)){
				select.addField("MeasLoc.DBUser_num");
				select.addField("MeasLoc.DBGroup_num");
				select.addField("MeasLoc.DBPermissions");
				select.addField("MeasLoc.MeasLocGroup_num");
			}
			select.addTable ( "MeasLoc" );
			break;
		case _W_MEASLOC:
			write = (DMIWriteStatement)statement;
			write.addField ( "Geoloc_num" ); 
			write.addField ( "Identifier" );
			write.addField ( "MeasLoc_name" );
			write.addField ( "Source_abbrev" );
			write.addField ( "Meas_loc_type" );
			write.addField ( "Comment" );
			if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)){
				write.addField("DBUser_num");
				write.addField("DBGroup_num");
				write.addField("DBPermissions");
				write.addField("MeasLocGroup_num");
			}			
			write.addTable ( "MeasLoc" );
			break;
		case _D_MEASLOC:
			del = (DMIDeleteStatement)statement;
			del.addTable ( "MeasLoc" );
			break;
//////////////////////////////////////////////////////
// MeasLocGroup
//////////////////////////////////////////////////////			
		case _S_MEASLOCGROUP:
			select = (DMISelectStatement)statement;
	/* AutoNum */	select.addField("MeasLocGroup.MeasLocGroup_num");
			select.addField("MeasLocGroup.Identifier");
			select.addField("MeasLocGroup.Name");
			select.addField("MeasLocGroup.Description");
			select.addField("MeasLocGroup.Optable");
			select.addField("MeasLocGroup.DBUser_num");
			select.addField("MeasLocGroup.DBGroup_num");
			select.addField("MeasLocGroup.DBPermissions");
			select.addTable("MeasLocGroup");
			break;
		case _W_MEASLOCGROUP:
			write = (DMIWriteStatement)statement;
			write.addField("Identifier");
			write.addField("Name");
			write.addField("Description");
			write.addField("Optable");
			write.addField("DBUser_num");
			write.addField("DBGroup_num");
			write.addField("DBPermissions");
			write.addTable("MeasLocGroup");
			break;
//////////////////////////////////////////////////////
// MeasLocType
//////////////////////////////////////////////////////			
		case _S_MEASLOCTYPE:
			select = (DMISelectStatement)statement;
			select.addField ( "MeasLocType.Type" );
			select.addField ( "MeasLocType.Description" );
			select.addTable ( "MeasLocType" );
			break;
		case _W_MEASLOCTYPE:
			write = (DMIWriteStatement)statement;
			write.addField ( "Type" );
			write.addField ( "Description" );
			write.addTable ( "MeasLocType" );
			break;
//////////////////////////////////////////////////////
// MeasQuality
//////////////////////////////////////////////////////			
		case _S_MEASQUALITYFLAG:
			select = (DMISelectStatement)statement;
			select.addField ( "MeasQualityFlag.Quality_flag" );
			select.addField ( "MeasQualityFlag.Description" );
			select.addTable ( "MeasQualityFlag" );
			break;			
		case _W_MEASQUALITYFLAG:
			write = (DMIWriteStatement)statement;
			write.addField ( "Quality_flag" );
			write.addField ( "Description" );
			write.addTable ( "MeasQualityFlag" );
			break;
		case _D_MEASQUALITYFLAG:
			del = (DMIDeleteStatement)statement;
			del.addTable("MeasQualityFlag");
			break;
//////////////////////////////////////////////////////
// MeasReducGridWeight
//////////////////////////////////////////////////////			
		case _S_MEASREDUCGRIDWEIGHT:
			select = (DMISelectStatement)statement;
			select.addField(
				"MeasReducGridWeight.OutputMeasType_num");
			select.addField(
				"MeasReducGridWeight.InputMeasType_num");
			select.addField("MeasReducGridWeight.Input_Row");
			select.addField("MeasReducGridWeight.Input_Column");
			select.addField("MeasReducGridWeight.Area");
			select.addField("MeasReducGridWeight.Area_Fraction");
			select.addField("MeasReducGridWeight.Weight");
			select.addTable ( "MeasReducGridWeight" );
			break;
		case _W_MEASREDUCGRIDWEIGHT:
			write = (DMIWriteStatement)statement;
			write.addField( "OutputMeasType_num");
			write.addField( "InputMeasType_num");
			write.addField( "Input_Row");
			write.addField( "Input_Column");
			write.addField( "Area");
			write.addField( "Area_Fraction");
			write.addField( "Weight");
			write.addTable( "MeasReducGridWeight" );
			break;
//////////////////////////////////////////////////////
// MeasReducRelation
//////////////////////////////////////////////////////			
		case _S_MEASREDUCRELATION:
			select = (DMISelectStatement)statement;
			select.addField("MeasReducRelation.OutputMeasType_num");
			select.addField("MeasReducRelation.InputMeasType_num");
			select.addField("MeasReducRelation.Weight");
			select.addTable ( "MeasReducRelation" );
			break;			
		case _W_MEASREDUCRELATION:
			write = (DMIWriteStatement)statement;
			write.addField("OutputMeasType_num");
			write.addField("InputMeasType_num");
			write.addField("Weight");
			write.addTable ( "MeasReducRelation" );
			break;
		case _D_MEASREDUCRELATION:
			del = (DMIDeleteStatement)statement;
			del.addTable("MeasReducRelation");
			break;
//////////////////////////////////////////////////////
// MeasReduction
//////////////////////////////////////////////////////			
		case _S_MEASREDUCTION:
			select = (DMISelectStatement)statement;
			select.addField("MeasReduction.OutputMeasType_num");
			select.addField("MeasReduction.Method");
			select.addField("MeasReduction.Create_order");
			select.addField("MeasReduction.Properties");
			if (isDatabaseVersionAtLeast(_VERSION_030000_20041001)){
				select.addField("MeasReduction.IsActive");
			}
			else {
				select.addField("MeasReduction.Active");
			}
			if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)){
				select.addField("MeasReduction.DBUser_num");
				select.addField("MeasReduction.DBGroup_num");
				select.addField("MeasReduction.DBPermissions");
			}			
			select.addTable ( "MeasReduction" );
			break;			
		case _W_MEASREDUCTION:
			write = (DMIWriteStatement)statement;
			write.addField("OutputMeasType_num");
			write.addField("Method");
			write.addField("Create_order");
			write.addField("Properties");
			if (isDatabaseVersionAtLeast(_VERSION_030000_20041001)){
				write.addField("IsActive");
			}
			else {
				write.addField("Active");
			}
			if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)){
				write.addField("DBUser_num");
				write.addField("DBGroup_num");
				write.addField("DBPermissions");
			}
			write.addTable ( "MeasReduction" );
			break;
		case _W_MEASREDUCTION_UPDATE:
			write = (DMIWriteStatement)statement;
			write.addField("Method");
			write.addField("Create_order");
			write.addField("Properties");
			if (isDatabaseVersionAtLeast(_VERSION_030000_20041001)){
				write.addField("IsActive");
			}
			else {
				write.addField("Active");
			}
			if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)){
				write.addField("DBUser_num");
				write.addField("DBGroup_num");
				write.addField("DBPermissions");
			}
			write.addTable ( "MeasReduction" );
			break;			
		case _D_MEASREDUCTION:
			del = (DMIDeleteStatement)statement;
			del.addTable("MeasReduction");
			break;
//////////////////////////////////////////////////////
// MeasReductionType
//////////////////////////////////////////////////////			
		case _S_MEASREDUCTIONTYPE:
			select = (DMISelectStatement)statement;
			select.addField("MeasReductionType.Type");
			select.addField("MeasReductionType.Description");
			select.addTable ( "MeasReductionType" );
			break;			
		case _W_MEASREDUCTIONTYPE:
			write = (DMIWriteStatement)statement;
			write.addField("Type");
			write.addField("Description");
			write.addTable ( "MeasReductionType" );
			break;
		case _D_MEASREDUCTIONTYPE:
			del = (DMIDeleteStatement)statement;
			del.addTable("MeasReductionType");
			break;
//////////////////////////////////////////////////////
// MeasScenario
//////////////////////////////////////////////////////			
		case _S_MEASSCENARIO:
			select = (DMISelectStatement)statement;
			select.addField ( "MeasScenario.ObsMeasType_num" );
			select.addField ( "MeasScenario.Method" );
			select.addField ( "MeasScenario.Create_order" );
			select.addField ( "MeasScenario.Properties" );
			if (isDatabaseVersionAtLeast(_VERSION_030000_20041001)){
				select.addField("IsActive");
			}
			else {
				select.addField("Active");
			}
			select.addTable ( "MeasScenario" );
			break;			
		case _W_MEASSCENARIO:
			write = (DMIWriteStatement)statement;
			write.addField ( "ObsMeasType_num" );
			write.addField ( "Method" );
			write.addField ( "Create_order" );
			write.addField ( "Properties" );
			if (isDatabaseVersionAtLeast(_VERSION_030000_20041001)){
				write.addField("IsActive");
			}
			else {
				write.addField("Active");
			}
			write.addTable ( "MeasScenario" );
			break;
//////////////////////////////////////////////////////
// MeasScenarioRelation
//////////////////////////////////////////////////////			
		case _S_MEASSCENARIORELATION:
			select = (DMISelectStatement)statement;
			select.addField("MeasScenarioRelation.ObsMeasType_num");
			select.addField("MeasScenarioRelation.QFMeasType_num");
			select.addField("MeasScenarioRelation.Weight" );
			select.addField (
				"MeasScenarioRelation.ScenarioMeasType_num" );
			select.addTable ( "MeasScenarioRelation" );
			break;
		case _W_MEASSCENARIORELATION:
			write = (DMIWriteStatement)statement;
			write.addField("ObsMeasType_num");
			write.addField("QFMeasType_num");
			write.addField("Weight" );
			write.addField("ScenarioMeasType_num" );
			write.addTable("MeasScenarioRelation" );
			break;
//////////////////////////////////////////////////////
// MeasTimeScale
//////////////////////////////////////////////////////			
		case _S_MEASTIMESCALE:
			select = (DMISelectStatement)statement;
			select.addField ( "MeasTimeScale.Scale" );
			select.addField ( "MeasTimeScale.Description" );
			select.addTable ( "MeasTimeScale" );
			break;			
		case _W_MEASTIMESCALE:
			write = (DMIWriteStatement)statement;
			write.addField ( "Scale" );
			write.addField ( "Description" );
			write.addTable ( "MeasTimeScale" );
			break;
		case _D_MEASTIMESCALE:
			del = (DMIDeleteStatement)statement;
			del.addTable("MeasTimeScale");
			break;
//////////////////////////////////////////////////////
// MeasTransProtocol
//////////////////////////////////////////////////////			
		case _S_MEASTRANSPROTOCOL:
			select = (DMISelectStatement)statement;
			select.addField ( "MeasTransProtocol.Protocol" );
			select.addField ( "MeasTransProtocol.Description" );
			select.addTable ( "MeasTransProtocol" );
			break;			
		case _W_MEASTRANSPROTOCOL:
			write = (DMIWriteStatement)statement;
			write.addField ( "Protocol" );
			write.addField ( "Description" );
			write.addTable ( "MeasTransProtocol" );
			break;
		case _D_MEASTRANSPROTOCOL:
			del = (DMIDeleteStatement)statement;
			del.addTable("MeasTransProtocol");
			break;
//////////////////////////////////////////////////////
// MeasType
//////////////////////////////////////////////////////			
		case _S_MEASTYPE: 
			select = (DMISelectStatement)statement;
			// Select from a join of MeasType and MeasLoc
	/* AutoNum */	select.addField ( "MeasType.MeasType_num" );
			select.addField ( "MeasType.MeasLoc_num" );
			select.addField ( "MeasType.Data_type" );
			select.addField ( "MeasType.Sub_type" );
			select.addField ( "MeasType.Time_step_base" );
			select.addField ( "MeasType.Time_step_mult" );
			select.addField ( "MeasType.Source_abbrev" );
			select.addField ( "MeasType.Scenario" );
			select.addField ( "MeasType.Table_num1" );
			select.addField ( "MeasType.Dbload_method1" );
			select.addField ( "MeasType.Table_num2" );
			select.addField ( "MeasType.Dbload_method2" );
			select.addField ( "MeasType.Description" );
			select.addField ( "MeasType.Units_abbrev" );
			select.addField ( "MeasType.Create_method" );
			select.addField ( "MeasType.TransmitProtocol" );
			select.addField ( "MeasType.Status" );
			select.addField ( "MeasType.Min_check" );
			select.addField ( "MeasType.Max_check" );
			if (isDatabaseVersionAtLeast(_VERSION_030000_20041001)){
				select.addField("MeasType.IsEditable");
			} 
			else {
				select.addField("MeasType.Editable");
			}
			if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)){
				select.addField("MeasType.IsVisible");
				select.addField("MeasType.DBUser_num");
				select.addField("MeasType.DBGroup_num");
				select.addField("MeasType.DBPermissions");
				select.addField("MeasType.TS_DBUser_num");
				select.addField("MeasType.TS_DBGroup_num");
				select.addField("MeasType.TS_DBPermissions");
			}			
			select.addField ( "MeasLoc.Identifier" );
			select.addField ( "MeasLoc.Measloc_name" );
			select.addTable ( "MeasType" );
			select.addTable ( "MeasLoc" );
			select.addWhereClause (
				"MeasType.MeasLoc_num=MeasLoc.MeasLoc_num" );
			break;
		case _W_MEASTYPE:
			write = (DMIWriteStatement)statement;
			write.addField ("MeasLoc_num");
			write.addField ("Data_type");
			write.addField ("Sub_type");
			write.addField ("Time_step_base");
			write.addField ("Time_step_mult");
			write.addField ("Source_abbrev");
			write.addField ("Scenario");
			write.addField ("Table_num1");
			write.addField ("Dbload_method1");
			write.addField ("Table_num2");
			write.addField ("Dbload_method2");
			write.addField ("Description");
			write.addField ("Units_abbrev");
			write.addField ("Create_method");
			write.addField ("TransmitProtocol");
			write.addField ("Status");
			write.addField ("Min_check");
			write.addField ("Max_check");
			if (isDatabaseVersionAtLeast(_VERSION_030000_20041001)){
				write.addField("IsEditable");
			} 
			else {
				write.addField("Editable");
			}
			if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)){
				write.addField("IsVisible");
				write.addField("DBUser_num");
				write.addField("DBGroup_num");
				write.addField("DBPermissions");
				write.addField("TS_DBUser_num");
				write.addField("TS_DBGroup_num");
				write.addField("TS_DBPermissions");
			}			
			write.addTable ("MeasType");
			break;
		case _D_MEASTYPE:
			del = (DMIDeleteStatement)statement;
			del.addTable( "MeasType" );
			break;			
//////////////////////////////////////////////////////
// MeasTypeStats
//////////////////////////////////////////////////////			
		case _S_MEASTYPESTATS:
			select = (DMISelectStatement)statement;
			select.addField ( "MeasTypeStats.MeasType_num" );
			select.addField ( "MeasTypeStats.Start_date" );
			select.addField ( "MeasTypeStats.End_date" );
			select.addField (
				"MeasTypeStats.First_date_of_last_edit" );
			select.addField ( "MeasTypeStats.Meas_count" );
			select.addField ( "MeasTypeStats.Min_val" );
			select.addField ( "MeasTypeStats.Max_val" );
			select.addTable ( "MeasTypeStats" );
			break;			
		case _W_MEASTYPESTATS:
			write = (DMIWriteStatement)statement;
			write.addField ( "MeasType_num" );
			write.addField ( "Start_date" );
			write.addField ( "End_date" );
			write.addField ( "First_date_of_last_edit" );
			write.addField ( "Meas_count" );
			write.addField ( "Min_val" );
			write.addField ( "Max_val" );
			write.addTable ( "MeasTypeStats" );
			break;			
		case _D_MEASTYPESTATS:
			del = (DMIDeleteStatement)statement;
			del.addTable("MeasTypeStats");
			break;			
//////////////////////////////////////////////////////
// MeasTypeStatus
//////////////////////////////////////////////////////			
		case _S_MEASTYPESTATUS:
			select = (DMISelectStatement)statement;
			select.addField ( "MeasTypeStatus.MeasType_num" );
			select.addField ( "MeasTypeStatus.Status_date" );
			select.addField ( "MeasTypeStatus.Status" );
			select.addField ( "MeasTypeStatus.Comment" );
			select.addTable ( "MeasTypeStatus" );
			break;
		case _W_MEASTYPESTATUS:
			write = (DMIWriteStatement)statement;
			write.addField ( "MeasType_num" );
			write.addField ( "Status_date" );
			write.addField ( "Status" );
			write.addField ( "Comment" );
			write.addTable ( "MeasTypeStatus" );
			break;
		case _D_MEASTYPESTATUS:
			del = (DMIDeleteStatement)statement;
			del.addTable("MeasTypeStatus");
			break;
//////////////////////////////////////////////////////
// MessageLog
//////////////////////////////////////////////////////			
		case _S_MESSAGELOG:
			select = (DMISelectStatement)statement;
			select.addField ( "MessageLog.Date_Time" );
			select.addField ( "MessageLog.Routine" );
			select.addField ( "MessageLog.Message_Type" );
			select.addField ( "MessageLog.Message_Level" );
			select.addField ( "MessageLog.Message" );
			select.addTable ( "MessageLog" );
			break;			
		case _W_MESSAGELOG:
			write = (DMIWriteStatement)statement;
			write.addField ( "Date_Time" );
			write.addField ( "Routine" );
			write.addField ( "Message_Type" );
			write.addField ( "Message_Level" );
			write.addField ( "Message" );
			write.addTable ( "MessageLog" );
			break;
		case _D_MESSAGELOG:
			del = (DMIDeleteStatement)statement;
			del.addTable("MessageLog");
			break;
//////////////////////////////////////////////////////
// Operation
//////////////////////////////////////////////////////			
		case _S_OPERATION:
			select = (DMISelectStatement)statement;
			select.addField("Operation.Operation_num");
			select.addField("Operation.MeasLocGroup_num");
			select.addField("Operation.Sequence");
			select.addField("Operation.Operation_ID");
			select.addField("Operation.Operation_Type");
			select.addField("Operation.x");
			select.addField("Operation.y");
			select.addTable("Operation");
			break;
//////////////////////////////////////////////////////
// OperationStateRelation
//////////////////////////////////////////////////////			
		case _S_OPERATIONSTATERELATION:
			select = (DMISelectStatement)statement;
			select.addField("OperationStateRelation"
				+ ".OperationStateRelation_num");
			select.addField("OperationStateRelation.Operation_num");
			select.addField("OperationStateRelation.State_name");
			select.addField("OperationStateRelation.Default_value");
			select.addTable("OperationStateRelation");
			break;
//////////////////////////////////////////////////////
// ProductGroup
//////////////////////////////////////////////////////			
		case _S_PRODUCTGROUP:
			select = (DMISelectStatement)statement;
	/* AutoNum */	select.addField("ProductGroup.ProductGroup_num");
			select.addField("ProductGroup.Identifier");
			select.addField("ProductGroup.Name");
			select.addField("ProductGroup.ProductType");
			select.addField("ProductGroup.Comment");
			select.addField("ProductGroup.IsEnabled");
			select.addField("ProductGroup.DBUser_num");
			select.addField("ProductGroup.DBGroup_num");
			select.addField("ProductGroup.DBPermissions");
			select.addTable("ProductGroup");
			break;
		case _W_PRODUCTGROUP:
			write = (DMIWriteStatement)statement;
			write.addField("Identifier");
			write.addField("Name");
			write.addField("ProductType");
			write.addField("Comment");
			write.addField("IsEnabled");
			write.addField("DBUser_num");
			write.addField("DBGroup_num");
			write.addField("DBPermissions");
			write.addTable("ProductGroup");
			break;
//////////////////////////////////////////////////////
// ProductType
//////////////////////////////////////////////////////			
		case _S_PRODUCTTYPE:
			select = (DMISelectStatement)statement;
			select.addField("ProductType.ProductType");
			select.addField("ProductType.Name");
			select.addField("ProductType.Comment");
			select.addTable("ProductType");
			break;
//////////////////////////////////////////////////////
// Props
//////////////////////////////////////////////////////			
		case _S_PROPS:
			select = (DMISelectStatement)statement;
	/* AutoNum */	select.addField ( "Props.Prop_num" );
			select.addField ( "Props.Variable" );
			select.addField ( "Props.Val" );
			select.addField ( "Props.Seq" );
			select.addField ( "Props.Description" );
			if (isDatabaseVersionAtLeast(_VERSION_030000_20041001)){
				select.addField ( "Props.DBUser_num" );
			}
			select.addTable ( "Props" );
			break;
		case _W_PROPS:
			write = (DMIWriteStatement)statement;
			write.addField ( "Variable" );
			write.addField ( "Val" );
			write.addField ( "Seq" );
			write.addField ( "Description" );
			if (isDatabaseVersionAtLeast(_VERSION_030000_20041001)){
				write.addField ( "DBUser_num" );
			}
			write.addTable ( "Props" );
			break;
//////////////////////////////////////////////////////
// RatingTable
//////////////////////////////////////////////////////			
		case _S_RATINGTABLE:
			select = (DMISelectStatement)statement;
			select.addField ( "RatingTable.RatingTable_num" );
			select.addField ( "RatingTable.Value1" );
			select.addField ( "RatingTable.Value2" );
			select.addField ( "RatingTable.Shift1" );
			select.addField ( "RatingTable.Shift2" );
			select.addTable ( "RatingTable" );
			break;			
		case _W_RATINGTABLE:
			write = (DMIWriteStatement)statement;
			write.addField ( "RatingTable_num" );
			write.addField ( "Value1" );
			write.addField ( "Value2" );
			write.addField ( "Shift1" );
			write.addField ( "Shift2" );
			write.addTable ( "RatingTable" );
			break;
		case _D_RATINGTABLE:
			del = (DMIDeleteStatement)statement;
			del.addTable("RatingTable");
			break;			
//////////////////////////////////////////////////////
// Revision
//////////////////////////////////////////////////////	
		/* AutoNum - Revision_num */		
		case _W_REVISION:
			write = (DMIWriteStatement)statement;
			write.addField("Date_Time");
			write.addField("Comment");
			write.addTable("Revision");
			break;
//////////////////////////////////////////////////////
// Scenario
//////////////////////////////////////////////////////			
		case _S_SCENARIO:
			select = (DMISelectStatement)statement;
	/* AutoNum */	select.addField ( "Scenario.Scenario_num" );
			select.addField ( "Scenario.Identifier" );
			select.addField ( "Scenario.Description" );
			if (isDatabaseVersionAtLeast(_VERSION_030000_20041001)){
				select.addField("Scenario.IsActive");
			} 
			else {
				select.addField("Scenario.Active");
			}
			select.addTable ( "Scenario" );
			break;		
		case _W_SCENARIO:
			write = (DMIWriteStatement)statement;
			write.addField ( "Identifier" );
			write.addField ( "Description" );
			if (isDatabaseVersionAtLeast(_VERSION_030000_20041001)){
				write.addField("IsActive");
			} 
			else {
				write.addField("Active");
			}
			write.addTable ( "Scenario" );
			break;
		case _D_SCENARIO:
			del = (DMIDeleteStatement)statement;
			del.addTable("Scenario");
			break;
//////////////////////////////////////////////////////
// SHEFType
//////////////////////////////////////////////////////			
		case _S_SHEFTYPE:
			select = (DMISelectStatement)statement;
			select.addField ( "SHEFType.SHEF_pe" );
			select.addField ( "SHEFType.Units_engl" );
			select.addField ( "SHEFType.Units_si" );
			select.addField ( "SHEFType.Default_base" );
			select.addField ( "SHEFType.Default_mult" );
			select.addField ( "SHEFType.Time_scale" );
			select.addTable ( "SHEFType" );
			break;
		case _W_SHEFTYPE:
			write = (DMIWriteStatement)statement;
			write.addField ( "SHEF_pe" );
			write.addField ( "Units_engl" );
			write.addField ( "Units_si" );
			write.addField ( "Default_base" );
			write.addField ( "Default_mult" );
			write.addField ( "Time_scale" );
			write.addTable ( "SHEFType" );
			break;			
		case _D_SHEFTYPE:
			del = (DMIDeleteStatement)statement;
			del.addTable("SHEFType");
			break;
//////////////////////////////////////////////////////
// StageDischargeRating
//////////////////////////////////////////////////////			
		case _S_STAGEDISCHARGERATING:
			select = (DMISelectStatement)statement;
			select.addField ( "StageDischargeRating.MeasLoc_num" );
			select.addField ( "StageDischargeRating.Start_Date" );
			select.addField ( "StageDischargeRating.End_Date" );
			select.addField("StageDischargeRating.RatingTable_num");
			select.addField("StageDischargeRating.Gage_Zero_Datum");
			select.addField (
				"StageDischargeRating.Gage_Datum_Units" );
			select.addField ( "StageDischargeRating.Warning_Level");
			select.addField ( "StageDischargeRating.Flood_Level" );
			select.addField ( "StageDischargeRating.Stage_Units" );
			select.addField("StageDischargeRating.Discharge_Units");
			select.addField(
				"StageDischargeRating.Interpolation_Method");
			select.addTable ( "StageDischargeRating" );
			break;
		case _W_STAGEDISCHARGERATING:
			write = (DMIWriteStatement)statement;
			write.addField ( "MeasLoc_num" );
			write.addField ( "Start_Date" );
			write.addField ( "End_Date" );
			write.addField ("RatingTable_num");
			write.addField ("Gage_Zero_Datum");
			write.addField ( "Gage_Datum_Units" );
			write.addField ( "Warning_Level");
			write.addField ( "Flood_Level" );
			write.addField ( "Stage_Units" );
			write.addField ("Discharge_Units");
			write.addField ( "Interpolation_Method");
			write.addTable ( "StageDischargeRating" );
			break;			
		case _D_STAGEDISCHARGERATING:
			del = (DMIDeleteStatement)statement;
			del.addTable("StageDischargeRating");
			break;
//////////////////////////////////////////////////////
// State
//////////////////////////////////////////////////////			
		case _S_STATE:
			select = (DMISelectStatement)statement;
			if (isDatabaseVersionAtLeast(_VERSION_030000_20041001)){
				select.addField("State.StateGroup_num");
				select.addField("State"
					+ ".OperationStateRelation_num");
				select.addField("State.Sequence");
				select.addField("State.ValueStr");
				select.addTable("State");
			}
			else {
				select.addField("State.Module");
				select.addField("State.Variable");
				select.addField("State.Seq");
				select.addField("State.StateGroup_num");
				select.addField("State.Val");
				select.addTable("State");
			}
			break;			
		case _W_STATE:
			write = (DMIWriteStatement)statement;
			if (isDatabaseVersionAtLeast(_VERSION_030000_20041001)){
				write.addField("StateGroup_num");
				write.addField("OperationStateRelation_num");
				write.addField("Sequence");
				write.addField("ValueStr");
				write.addTable("State");
			}
			else {		
				write.addField("Module");
				write.addField("Variable");
				write.addField("Seq");
				write.addField("StateGroup_num");
				write.addField("Val");
				write.addTable("State");
			}
			break;
//////////////////////////////////////////////////////
// StateGroup
//////////////////////////////////////////////////////			
		case _S_STATEGROUP:
			select = (DMISelectStatement)statement;
	/* AutoNum */	select.addField ( "StateGroup.StateGroup_num" );
			select.addField ( "StateGroup.Scenario" );
			select.addField ( "StateGroup.Date_Time" );
			select.addField ( "StateGroup.Description" );
			select.addField ( "StateGroup.Status" );
			if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)){
				select.addField("StateGroup.MeasLocGroup_num");
			}
			select.addTable ( "StateGroup" );
			break;
		case _W_STATEGROUP:
			write = (DMIWriteStatement)statement;
			write.addField ( "Scenario" );
			write.addField ( "Date_Time" );
			write.addField ( "Description" );
			write.addField ( "Status" );
			write.addTable ( "StateGroup" );
			if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)){
				write.addField("MeasLocGroup_num");
			}			
			break;			
//////////////////////////////////////////////////////
// Station
//////////////////////////////////////////////////////			
		case _S_STATION:
			select = (DMISelectStatement)statement;
	/* AutoNum */	select.addField ( "Station.Station_num" );
			select.addField ( "Station.MeasLoc_num" );
			select.addField ( "Station.Station_id" );
			select.addField ( "Station.Station_name" );
			select.addField ( "Station.Source_abbrev" );
			select.addField ( "Station.Description" );
			select.addField ( "Station.Primary_flag" );
			select.addTable ( "Station" );
			break;
		case _W_STATION:
			write = (DMIWriteStatement)statement;
			write.addField ( "MeasLoc_num" );
			write.addField ( "Station_id" );
			write.addField ( "Station_name" );
			write.addField ( "Source_abbrev" );
			write.addField ( "Description" );
			write.addField ( "Primary_flag" );
			write.addTable ( "Station" );
			break;
		case _D_STATION:
			del = (DMIDeleteStatement)statement;
			del.addTable("Station");
			break;
//////////////////////////////////////////////////////
// TableLayout
//////////////////////////////////////////////////////			
		case _S_TABLELAYOUT:
			select = (DMISelectStatement)statement;
			select.addField ( "TableLayout.TableLayout_num" );
			select.addField ( "TableLayout.Identifier" );
			select.addField ( "TableLayout.Description" );
			select.addTable ( "TableLayout" );
			break;			
		case _W_TABLELAYOUT:
			write = (DMIWriteStatement)statement;
			write.addField ( "TableLayout_num" );
			write.addField ( "Identifier" );
			write.addField ( "Description" );
			write.addTable ( "TableLayout" );
			break;
		case _D_TABLELAYOUT:
			del = (DMIDeleteStatement)statement;
			del.addTable("TableLayout");
			break;
//////////////////////////////////////////////////////
// Tables
//////////////////////////////////////////////////////			
		case _S_TABLES:
			select = (DMISelectStatement)statement;
	/* AutoNum */	select.addField("Tables.Table_num");
			select.addField("Tables.Table_name");
			select.addField("Tables.Created");
			select.addField("Tables.Description");
			select.addField("Tables.TableLayout_num");
			select.addField("Tables.Archive");
			select.addField("Tables.Active_days");
			select.addField("Tables.Date_field");
			select.addField("Tables.Date_precision");
			select.addField("Tables.Date_table");
			select.addField("Tables.Date_table_Joinfield");
			select.addField("Tables.Joinfield");			
			if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)){
				select.addField("Tables.IsReference");
				select.addField("Tables.IsTSTemplate");
				select.addField("Tables.IsTS");
				select.addField("Tables.DBUser_num");
				select.addField("Tables.DBGroup_num");
				select.addField("Tables.Record_DBPermissions");
			}
			select.addTable("Tables");
			break;
		case _W_TABLES:
			write = (DMIWriteStatement)statement;
			write.addField("Table_name");
			write.addField("Created");
			write.addField("Description");
			write.addField("TableLayout_num");
			write.addField("Archive");
			write.addField("DB_file");
			write.addField("Active_days");
			write.addField("Date_field");
			write.addField("Date_precision");
			write.addField("Date_table");
			write.addField("Date_table_Joinfield");			
			write.addField("Joinfield");
			if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)){
				write.addField("IsReference");
				write.addField("isTSTemplate");
				write.addField("IsTS");
				write.addField("DBUser_num");
				write.addField("DBGroup_num");
				write.addField("Record_DBPermissions");
			}			
			break;
//////////////////////////////////////////////////////
// TSProduct
//////////////////////////////////////////////////////			
		case _S_TSPRODUCT:
			select = (DMISelectStatement)statement;
	/* AutoNum */	select.addField("TSProduct.TSProduct_num");
			select.addField("TSProduct.ProductGroup_num");
			select.addField("TSProduct.Identifier");
			select.addField("TSProduct.Name");
			select.addField("TSProduct.Comment");
			select.addField("TSProduct.DBUser_num");
			select.addField("TSProduct.DBGroup_num");
			select.addField("TSProduct.DBPermissions");
			select.addTable("TSProduct");
			break;
		case _W_TSPRODUCT_INSERT:
		case _W_TSPRODUCT_UPDATE:
			write = (DMIWriteStatement)statement;
			write.addField("ProductGroup_num");
			write.addField("Identifier");
			write.addField("Name");
			write.addField("Comment");
			write.addField("DBUser_num");
			write.addField("DBGroup_num");
			write.addField("DBPermissions");
			write.addTable("tsproduct");
			break;			
//////////////////////////////////////////////////////
// TSProductProps
//////////////////////////////////////////////////////			
		case _S_TSPRODUCTPROPS:
			select = (DMISelectStatement)statement;
			select.addField("TSProductProps.TSProduct_num");
			select.addField("TSProductProps.Property");
			if (isDatabaseVersionAtLeast(_VERSION_030000_20041001)){
				select.addField("TSProductProps.Val");
			}
			else {
				select.addField("TSProductProps.Value");
			}
			select.addField("TSProductProps.Sequence");
			select.addTable("TSProductProps");
			break;
		case _W_TSPRODUCTPROPS:
			write = (DMIWriteStatement)statement;
			write.addField("TSProduct_num");
			write.addField("Property");
			write.addField("Value");
			write.addField("Sequence");
			write.addTable("tsproductprops");
			break;
			
//////////////////////////////////////////////////////
// Version
//////////////////////////////////////////////////////			
		case _W_VERSION:
			write = (DMIWriteStatement)statement;
			write.addField("Table_num");
			write.addField("version_type");
			write.addField("version_id");
			write.addField("version_date");
			write.addField("version_comment");
			break;			

		case _S_ACTION:
			select = (DMISelectStatement)statement;
			select.addField("Action.ActionNum");
			select.addField("Action.IsActive");
			select.addField("Action.ActionType");
			select.addField("Action.Severity");
			select.addField("Action.Name");
			select.addField("Action.Description");
			select.addField("Action.ActionBaseTime");
			select.addField("Action.ActionBaseTimeFormat");
			select.addField("Action.ActionInterval");
			select.addField("Action.EvaluationWindow");
			select.addField("Action.Properties");
			select.addField("Action.ResultLevel");
			select.addTable("Action");
			break;

		case _W_ACTION:
			write = (DMIWriteStatement)statement;
			write.addField("IsActive");
			write.addField("ActionType");
			write.addField("Severity");
			write.addField("Name");
			write.addField("Description");
			write.addField("ActionBaseTime");
			write.addField("ActionBaseTimeFormat");
			write.addField("ActionInterval");
			write.addField("EvaluationWindow");
			write.addField("Properties");
			write.addTable("Action");
			break;

		case _S_DATATEST:
			select = (DMISelectStatement)statement;
			select.addField("DataTest.DataTestNum");
			select.addField("DataTest.Description");
			select.addField("DataTest.DataTestGroupNum");
			select.addField("DataTest.IsActive");
			select.addField("DataTest.ActiveSeasonStart");
			select.addField("DataTest.ActiveSeasonEnd");
			select.addField("DataTest.ActiveSeasonFormat");
			select.addField("DataTest.DataTestExpressionNum");
			select.addField("DataTest.TestBaseTime");
			select.addField("DataTest.TestBaseTimeFormat");
			select.addField("DataTest.TestInterval");
			select.addField("DataTest.PositiveTestInterval");
			select.addField("DataTest.EvaluationWindow");
			select.addField("DataTest.EvaluationInterval");
			select.addField("DataTest.Message");
			select.addField("DataTest.Properties");
			select.addField("DataTest.PositiveCount");
			select.addTable("DataTest");
			break;
			
		case _W_DATATEST:
			write = (DMIWriteStatement)statement;
			write.addField("Description");
			write.addField("DataTestGroupNum");
			write.addField("IsActive");
			write.addField("ActiveSeasonStart");
			write.addField("ActiveSeasonEnd");
			write.addField("ActiveSeasonFormat");
			write.addField("DataTestExpressionNum");
			write.addField("TestBaseTime");
			write.addField("TestBaseTimeFormat");
			write.addField("TestInterval");
			write.addField("PositiveTestInterval");
			write.addField("EvaluationWindow");
			write.addField("EvaluationInterval");
			write.addField("Message");
			write.addField("Properties");
			write.addField("PositiveCount");
			write.addTable("DataTest");
			break;

		case _S_DATATESTACTION:
			select = (DMISelectStatement)statement;
			select.addField("DataTestActionRelationship"
				+ ".DataTestNum");
			select.addField("DataTestActionRelationship.ActionNum");
			select.addTable("DataTestActionRelationship");
			break;

		case _S_DATATESTEXPRESSION:
			select = (DMISelectStatement)statement;
			select.addField("DataTestExpression"
				+ ".DataTestExpressionNum");
			select.addField("DataTestExpression.Operator");
			select.addField("DataTestExpression.LeftSideType");
			select.addField("DataTestExpression.LeftSideNum");
			select.addField("DataTestExpression.RightSideType");
			select.addField("DataTestExpression.RightSideNum");
			select.addTable("DataTestExpression");
			break;

		case _W_DATATESTEXPRESSION:
			write = (DMIWriteStatement)statement;
			write.addField("Operator");
			write.addField("LeftSideType");
			write.addField("LeftSideNum");
			write.addField("RightSideType");
			write.addField("RightSideNum");
			write.addField("Properties");
			write.addTable("DataTestExpression");
			break;
	
		case _S_DATATESTFUNCTION:
			select = (DMISelectStatement)statement;
			select.addField("DataTestFunction.DataTestFunctionNum");
			select.addField("DataTestFunction.FunctionType");
			select.addField("DataTestFunction.Properties");
			select.addField("DataTestFunction.Message");
			select.addTable("DataTestFunction");
			break;
		case _W_DATATESTFUNCTION:
			write = (DMIWriteStatement)statement;
			write.addField("FunctionType");
			write.addField("Properties");
			write.addField("Message");
			write.addTable("DataTestFunction");
			break;

		case _S_DATATESTFUNCTIONINPUTID:
			select = (DMISelectStatement)statement;
			select.addField("DataTestFunctionInputIDs"
				+ ".DataTestFunctionNum");
			select.addField("DataTestFunctionInputIDs.Position");
			select.addField("DataTestFunctionInputIDs.[ID]");
			select.addTable("DataTestFunctionInputIDs");
			break;
	
		case _W_DATATESTFUNCTIONINPUTID:
			write = (DMIWriteStatement)statement;
			write.addField("DataTestFunctionNum");
			write.addField("Position");
			write.addField("[ID]");
			write.addTable("DataTestFunctionInputIDs");
			break;

		case _S_DATATESTSTATUS:
			select = (DMISelectStatement)statement;
			select.addField("DataTestStatus.DataTestNum");
			select.addField("DataTestStatus.LastTestDateTime");
			select.addField("DataTestStatus.NextTestDateTime");
			select.addField("DataTestStatus.WasLastTestPositive");
			select.addTable("DataTestStatus");
			break;
		
		case _W_DATATESTSTATUS:
			write = (DMIWriteStatement)statement;
			write.addField("DataTestNum");
			write.addField("LastTestDateTime");
			write.addField("NextTestDateTime");
			write.addField("WasLastTestPositive");
			write.addTable("DataTestStatus");
			break;

		case _S_CONTACT:
			select = (DMISelectStatement)statement;
			select.addField("Contact.ContactNum");
			select.addField("Contact.FirstName");
			select.addField("Contact.LastName");
			select.addField("Contact.PhoneNumber");
			select.addField("Contact.FaxNumber");
			select.addField("Contact.PagerNumber");
			select.addField("Contact.EmailAddress");
			select.addField("Contact.IMAddress");
			select.addField("Contact.SMSAddress");
			select.addField("Contact.SkypeID");
			select.addField("Contact.Properties");
			select.addTable("Contact");
			break;
		
		case _S_ACTIONCONTACT:
			select = (DMISelectStatement)statement;
			select.addField("ActionContactRelationship.ActionNum");
			select.addField("ActionContactRelationship.ContactNum");
			select.addTable("ActionContactRelationship");
			break;

		case _S_ACTIONTYPE:
			select = (DMISelectStatement)statement;
			select.addField("ActionTypes.ActionType");
			select.addField("ActionTypes.Description");
			select.addTable("ActionTypes");
			break;

		case _S_DATATESTRESULT:
			select = (DMISelectStatement)statement;
			select.addField("DataTestResults.DataTestNum");
			select.addField("DataTestResults.TestTime");
			select.addField("DataTestResults.LeftSideValue");
			select.addField("DataTestResults.RightSideValue");
			select.addField(
				"DataTestResults.DataTestExpressionNum");
			select.addField("DataTestResults.[Level]");
			select.addField("DataTestResults.Message");
			select.addTable("DataTestResults");
			break;

		case _W_DATATESTRESULT:
			write = (DMIWriteStatement)statement;
			write.addField("DataTestNum");
			write.addField("TestTime");
			write.addField("LeftSideValue");
			write.addField("RightSideValue");
			write.addField("DataTestExpressionNum");
			write.addField("[Level]");
			write.addField("Message");
			write.addTable("DataTestResults");
			break;

		case _S_SEVERITYTYPES:
			select = (DMISelectStatement)statement;
			select.addField("SeverityTypes.SeverityType");
			select.addField("SeverityTypes.SeverityLevel");
			select.addField("SeverityTypes.DisplayColor");
			select.addField("SeverityTypes.DisplayFont");
			select.addField("SeverityTypes.DisplayIcon");
			select.addTable("SeverityTypes");
			break;

		default:
			Message.printWarning ( 2, "RiversideDB_DMI.buildSQL",
			"Unknown statement code: " + sqlNumber );
			break;
	}
}

// C FUNCTIONS

/**
Determine whether the user can create the database table/record, given a set of
permissions.
@param DBUser_num the DBUser_num that owns the table/record in the database
@param DBGroup_num the GBGroup that owns the table/record in the database
@param permissions the permissions string (see permissions documentation above)
for the table/record being checked
@return true if the user can create the table/record
@throws Exception if an error occurs
*/
public boolean canCreate(int DBUser_num, int DBGroup_num, String permissions) 
throws Exception {
	String routine = "RiversideDB_DMI.canCreate";
	int dl = 5;

	Message.printDebug(dl, routine, "canCreate(" + DBUser_num + ", " 
		+ DBGroup_num + ", " + permissions + ")");

	if (_dbuser.getLogin().trim().equalsIgnoreCase("root")) {
		Message.printDebug(dl, routine, "Current user is root, can "
			+ "always create.");
		// root can do ANYTHING
		return true;
	}

	// start with the least-restrictive and move to the more-restrictive
	boolean canCreate = false;

	// first check other
	if (StringUtil.indexOfIgnoreCase(permissions, "OC+", 0) > -1) {
		Message.printDebug(dl, routine, "OC+ set, canCreate = true");
		canCreate = true;
	}
	// next check group
	if (DBGroup_num == _dbgroup._DBGroup_num) {
		canCreate = false;
		Message.printDebug(dl, routine, "Group num matches ...");
		if (StringUtil.indexOfIgnoreCase(permissions, "GC+", 0) > -1) {
			Message.printDebug(dl, routine, "GC+ set, canCreate = "
				+ "true");
			canCreate = true;
		}
		else {
			Message.printDebug(dl, routine, "GC+ not set.");
		}
	}

	// finally, user
	if (DBUser_num == _dbuser._DBUser_num) {
		canCreate = false;
		Message.printDebug(dl, routine, "User num matches ...");
		if (StringUtil.indexOfIgnoreCase(permissions, "UC+", 0) > -1) {
			Message.printDebug(dl, routine, "UC+ set, canCreate = "
				+ "true");
			canCreate = true;
		}
		else {
			Message.printDebug(dl, routine, "UC+ not set.");
		}
	}

	return canCreate;	
}

/**
Determine whether the user can delete the database table/record, given a set of
permissions.
@param DBUser_num the DBUser_num that owns the table/record in the database
@param DBGroup_num the GBGroup that owns the table/record in the database
@param permissions the permissions string (see permissions documentation above)
for the table/record being checked
@return true if the user can delete the table/record
@throws Exception if an error occurs
*/
public boolean canDelete(int DBUser_num, int DBGroup_num, String permissions)
throws Exception {
	String routine = "RiversideDB_DMI.canDelete";
	int dl = 5;

	Message.printDebug(dl, routine, "canDelete(" + DBUser_num + ", " 
		+ DBGroup_num + ", " + permissions + ")");

	if (_dbuser.getLogin().trim().equalsIgnoreCase("root")) {
		Message.printDebug(dl, routine, "Current user is root, can "
			+ "always delete.");
		// root can do ANYTHING
		return true;
	}

	// start with the least-restrictive and move to the more-restrictive
	boolean canDelete = false;

	// first check other
	if (StringUtil.indexOfIgnoreCase(permissions, "OD+", 0) > -1) {
		Message.printDebug(dl, routine, "OD+ set, canDelete = true");
		canDelete = true;
	}
	// next check group
	if (DBGroup_num == _dbgroup._DBGroup_num) {
		canDelete = false;
		Message.printDebug(dl, routine, "Group num matches ...");
		if (StringUtil.indexOfIgnoreCase(permissions, "GD+", 0) > -1) {
			Message.printDebug(dl, routine, "GD+ set, canDelete = "
				+ "true");
			canDelete = true;
		}
		else {
			Message.printDebug(dl, routine, "GD+ not set.");
		}
	}

	// finally, user
	if (DBUser_num == _dbuser._DBUser_num) {
		canDelete = false;
		Message.printDebug(dl, routine, "User num matches ...");
		if (StringUtil.indexOfIgnoreCase(permissions, "UD+", 0) > -1) {
			Message.printDebug(dl, routine, "UD+ set, canDelete = "
				+ "true");
			canDelete = true;
		}
		else {
			Message.printDebug(dl, routine, "UD+ not set.");
		}
	}

	return canDelete;	
}

/**
Determine whether the user can insert the database table/record, given a set of
permissions.
@param DBUser_num the DBUser_num that owns the table/record in the database
@param DBGroup_num the GBGroup that owns the table/record in the database
@param permissions the permissions string (see permissions documentation above)
for the table/record being checked
@return true if the user can insert the table/record
@throws Exception if an error occurs
*/
public boolean canInsert(int DBUser_num, int DBGroup_num, String permissions)
throws Exception {
	String routine = "RiversideDB_DMI.canInsert";
	int dl = 5;

	Message.printDebug(dl, routine, "canInsert(" + DBUser_num + ", " 
		+ DBGroup_num + ", " + permissions + ")");

	if (_dbuser.getLogin().trim().equalsIgnoreCase("root")) {
		Message.printDebug(dl, routine, "Current user is root, can "
			+ "always insert.");
		// root can do ANYTHING
		return true;
	}

	// start with the least-restrictive and move to the more-restrictive
	boolean canInsert = false;

	// first check other
	if (StringUtil.indexOfIgnoreCase(permissions, "OI+", 0) > -1) {
		Message.printDebug(dl, routine, "OI+ set, canInsert = true");
		canInsert = true;
	}
	// next check group
	if (DBGroup_num == _dbgroup._DBGroup_num) {
		canInsert = false;
		Message.printDebug(dl, routine, "Group num matches ...");
		if (StringUtil.indexOfIgnoreCase(permissions, "GI+", 0) > -1) {
			Message.printDebug(dl, routine, "GI+ set, canInsert = "
				+ "true");
			canInsert = true;
		}
		else {
			Message.printDebug(dl, routine, "GI+ not set.");
		}
	}

	// finally, user
	if (DBUser_num == _dbuser._DBUser_num) {
		canInsert = false;
		Message.printDebug(dl, routine, "User num matches ...");
		if (StringUtil.indexOfIgnoreCase(permissions, "UI+", 0) > -1) {
			Message.printDebug(dl, routine, "UI+ set, canInsert = "
				+ "true");
			canInsert = true;
		}
		else {
			Message.printDebug(dl, routine, "UI+ not set.");
		}
	}

	return canInsert;	
}

/**
Determine whether the user can read the database table/record, given a set of
permissions.
@param DBUser_num the DBUser_num that owns the table/record in the database
@param DBGroup_num the GBGroup that owns the table/record in the database
@param permissions the permissions string (see permissions documentation above)
for the table/record being checked
@return true if the user can read the table/record
@throws Exception if an error occurs
*/
public boolean canRead(int DBUser_num, int DBGroup_num, String permissions)
throws Exception {
	String routine = "RiversideDB_DMI.canRead";
	int dl = 5;

	Message.printDebug(dl, routine, "canRead(" + DBUser_num + ", " 
		+ DBGroup_num + ", " + permissions + ")");

	if (_dbuser.getLogin().trim().equalsIgnoreCase("root")) {
		Message.printDebug(dl, routine, "Current user is root, can "
			+ "always read.");
		// root can do ANYTHING
		return true;
	}

	// start with the least-restrictive and move to the more-restrictive
	boolean canRead = false;

	// first check other
	if (StringUtil.indexOfIgnoreCase(permissions, "OR+", 0) > -1) {
		Message.printDebug(dl, routine, "OR+ set, canRead = true");
		canRead = true;
	}
	// next check group
	if (DBGroup_num == _dbgroup._DBGroup_num) {
		canRead = false;
		Message.printDebug(dl, routine, "Group num matches ...");
		if (StringUtil.indexOfIgnoreCase(permissions, "GR+", 0) > -1) {
			Message.printDebug(dl, routine, "GR+ set, canRead = "
				+ "true");
			canRead = true;
		}
		else {
			Message.printDebug(dl, routine, "GR+ not set.");
		}
	}

	// finally, user
	if (DBUser_num == _dbuser._DBUser_num) {
		canRead = false;
		Message.printDebug(dl, routine, "User num matches ...");
		if (StringUtil.indexOfIgnoreCase(permissions, "UR+", 0) > -1) {
			Message.printDebug(dl, routine, "UR+ set, canRead = "
				+ "true");
			canRead = true;
		}
		else {
			Message.printDebug(dl, routine, "UR+ not set.");
		}
	}

	return canRead;	
}

/**
Determine whether the user can update the database table/record, given a set of
permissions.
@param DBUser_num the DBUser_num that owns the table/record in the database
@param DBGroup_num the GBGroup that owns the table/record in the database
@param permissions the permissions string (see permissions documentation above)
for the table/record being checked
@return true if the user can update the table/record
@throws Exception if an error occurs
*/
public boolean canUpdate(int DBUser_num, int DBGroup_num, String permissions)
throws Exception {
	String routine = "RiversideDB_DMI.canUpdate";
	int dl = 5;

	Message.printDebug(dl, routine, "canUpdate(" + DBUser_num + ", " 
		+ DBGroup_num + ", " + permissions + ")");

	if (_dbuser.getLogin().trim().equalsIgnoreCase("root")) {
		Message.printDebug(dl, routine, "Current user is root, can "
			+ "always read.");
		// root can do ANYTHING
		return true;
	}

	// start with the least-restrictive and move to the more-restrictive
	boolean canUpdate = false;

	// first check other
	if (StringUtil.indexOfIgnoreCase(permissions, "OU+", 0) > -1) {
		Message.printDebug(dl, routine, "OU+ set, canUpdate = true");
		canUpdate = true;
	}
	// next check group
	if (DBGroup_num == _dbgroup._DBGroup_num) {
		canUpdate = false;
		Message.printDebug(dl, routine, "Group num matches ...");
		if (StringUtil.indexOfIgnoreCase(permissions, "GU+", 0) > -1) {
			Message.printDebug(dl, routine, "GU+ set, canUpdate = "
				+ "true");
			canUpdate = true;
		}
		else {
			Message.printDebug(dl, routine, "GU+ not set.");
		}
	}

	// finally, user
	if (DBUser_num == _dbuser._DBUser_num) {
		canUpdate = false;
		Message.printDebug(dl, routine, "User num matches ...");
		if (StringUtil.indexOfIgnoreCase(permissions, "UU+", 0) > -1) {
			Message.printDebug(dl, routine, "UU+ set, canUpdate = "
				+ "true");
			canUpdate = true;
		}
		else {
			Message.printDebug(dl, routine, "UU+ not set.");
		}
	}

	return canUpdate;	
}

/**
Determine whether the user can write to the table/record, given a set of 
permissions.  This method calls canInsert() and canUpdate() and returns true
only if both methods return true.  This method is meant for data records but
the canCreate() method may be needed to be called in cases where a high-level
record is being created.
@param DBUser_num the DBUser_num that owns the table/record in the database
@param DBGroup_num the GBGroup that owns the table/record in the database
@param permissions the permissions string (see permissions documentation above)
for the table/record being checked
@return true if the user can write the table/record
@throws Exception if an error occurs
*/
public boolean canWrite(int DBUser_num, int DBGroup_num, String permissions) 
throws Exception {
	return (canInsert(DBUser_num, DBGroup_num, permissions) && 
		canUpdate(DBUser_num, DBGroup_num, permissions));
}

/**
Change the current DBGroup to the requested value.
@param new_dbgroup DBGroup identifier to change to (e.e.g, as selected from 
a list in an interface)
@throws exception if the current DBUser does not belong to the requested 
group or if the group does not exist.
*/
public void changeCurrentGroup(String new_dbgroup) 
throws Exception {
	String routine = "RiversideDB_DMI.changeCurrentGroup";

	// look up the group to make sure it's valid
	Vector dbgroupV = readDBGroupList();
	int size = 0;
	if (dbgroupV != null) {
		size = dbgroupV.size();
	}
	int found = -1;
	RiversideDB_DBGroup group = null;
	for (int i = 0; i < size; i++) {
		group = (RiversideDB_DBGroup)dbgroupV.elementAt(i);
		if (group.getID().equalsIgnoreCase(new_dbgroup)) {
			found = i;
			break;
		}
	}

	if (found == -1) {
		Message.printWarning(2, routine, 
			"Requested DBGroup \"" + new_dbgroup + "\" is not "
			+ "valid.");
		throw new Exception ("Requested DBGroup \"" + new_dbgroup 
			+ "\" is not valid.");
	}

	group = (RiversideDB_DBGroup)dbgroupV.elementAt(found);

	// loop through the relationships to see if the current user is
	// in the group
	Vector dbuserGroupV = readDBUserDBGroupRelationListForDBGroup_num(
		group.getDBGroup_num());
	
	size = 0;
	if (dbuserGroupV != null) {
		size = dbuserGroupV.size();
	}

	RiversideDB_DBUserDBGroupRelation rel = null;
	for (int i = 0; i < size; i++) {
		rel = (RiversideDB_DBUserDBGroupRelation)
			dbuserGroupV.elementAt(i);
		if (rel.getDBUser_num() == _dbuser.getDBUser_num()) {
			// users match so switch the current group
			_dbgroup = group;
			return;
		}
	}

	// user is not in the requested group

	Message.printWarning(2, routine, "User does not belong to requested "
		+ "DBGroup \"" + new_dbgroup + "\".");
	throw new Exception ("User does not belong to requested DBGroup \"" 
		+ new_dbgroup + "\".");
}

// D FUNCTIONS

/**
Deletes records from the DataDimension table that have the given 
Dimension. 
@param Dimension the Dimension for which to delete records.
@return the number of records deleted.
@throws Exception if an error occurs.
*/
public int deleteDataDimensionForDimension(String Dimension)
throws Exception {
	DMIDeleteStatement d = new DMIDeleteStatement(this);
	buildSQL(d, _D_DATADIMENSION);
	d.addWhereClause("Dimension = '" + Dimension + "'");
	return dmiDelete(d);
}

/**
Deletes records from the DataSource table that have the given 
Source_abbrev. 
@param Source_abbrev the Source_abbrev for which to delete records.
@return the number of records deleted.
@throws Exception if an error occurs.
*/
public int deleteDataSourceForSource_abbrev(String Source_abbrev)
throws Exception {
	DMIDeleteStatement d = new DMIDeleteStatement(this);
	buildSQL(d, _D_DATASOURCE);
	d.addWhereClause("Source_abbrev = '" + Source_abbrev + "'");
	return dmiDelete(d);
}
/**
Deletes records from the DataType table that have the given 
DataType. 
@param DataType the DataType for which to delete records.
@return the number of records deleted.
@throws Exception if an error occurs.
*/
public int deleteDataTypeForDataType(String DataType)
throws Exception {
	DMIDeleteStatement d = new DMIDeleteStatement(this);
	buildSQL(d, _D_DATATYPE);
	d.addWhereClause("DataType = '" + DataType + "'");
	return dmiDelete(d);
}

/**
Deletes records from the DataUnits table that have the given 
Units_abbrev. 
@param Units_abbrev the Units_abbrev for which to delete records.
@return the number of records deleted.
@throws Exception if an error occurs.
*/
public int deleteDataUnitsForUnits_abbrev(String Units_abbrev)
throws Exception {
	DMIDeleteStatement d = new DMIDeleteStatement(this);
	buildSQL(d, _D_DATAUNITS);
	d.addWhereClause("Units_abbrev = '" + Units_abbrev + "'");
	return dmiDelete(d);
}

/**
Deletes records from the DBUserMeasLocGroupRelation table that have the given
MeasLocGroup_num.  Note that all matching DBUserMeasLocGRoupRelation records 
are deleted, <i>even if they belong to users other than the one 
currently logged in</i>.
@param MeasLocGroup_num the MeasLocGroup_num for which to delete records
@return the number of records deleted from the table
@throws Exception if an error occurs.
*/
public int deleteDBUserMeasLocGroupRelationForMeasLocGroup_num(
int MeasLocGroup_num)
throws Exception {
	String sql = "DELETE FROM DBUserMeasLocGroupRelation WHERE "
		+ "MeasLocGroup_num = " + MeasLocGroup_num;
	return dmiDelete(sql);
}

/**
Deletes records from the DBUserMeasLocGroupRelation table that have the given
MeasLocGroup_num and DBUser_num.
@param MeasLocGroup_num the MeasLocGroup_num for which to delete records
@param DBUser_num the DBUser_num for which to delete records
@return the number of records deleted from the table
@throws Exception if an error occurs.
*/
public int deleteDBUserMeasLocGroupRelationForMeasLocGroup_numDBUser_num(
int MeasLocGroup_num, int DBUser_num)
throws Exception {
	String sql = "DELETE FROM DBUserMeasLocGroupRelation WHERE "
		+ "MeasLocGroup_num = " + MeasLocGroup_num + " AND " 
		+ "DBUser_num = " + DBUser_num;
	return dmiDelete(sql);
}

/**
Deletes records from the ExportConf table that have the given 
ExportProduct_num.
@param ExportProduct_num the ExportProduct_num that determines the records
to be deleted
@return the number of records deleted
@throws Exception if an error occurs
*/
public int deleteExportConfForExportProduct_num(long ExportProduct_num) 
throws Exception {
	String sql = "DELETE FROM ExportConf WHERE ExportProduct_num = " 
		+ ExportProduct_num;
	return dmiDelete(sql);
}

/**
Deletes records from the ExportConf table that have the given 
MeasType_num.
@param MeasType_num the MeasType_num that determines the records
to be deleted
@return an integer which tells how many records were deleted 
@throws Exception if an error occurs
*/
public int deleteExportConfForMeasType_num(int MeasType_num) 
throws Exception {
	String sql = "DELETE FROM ExportConf WHERE MeasType_num = " 
		+ MeasType_num;
	return dmiDelete(sql);
}

/**
Deletes records from ExportProduct that have the given ProductGroup_num,
first deleting the associated child records from ExportConf.
@param ProductGroup_num the ProductGroup_num for which to delete records
@return a 3-element int array.  The array contains the number of records 
deleted in each table.<br>
[0] - the number of records deleted from ExportProduct<br>
[1] - the number of records deleted from ExportConf<br>
[2] - the total number of records deleted<br>
@throws Exception if an error occurs
*/
public int[] deleteExportProductForProductGroup_num(int ProductGroup_num)
throws Exception {
	// First get a list of all the export products with that 
	// ProductGroup_num
	Vector v = readExportProductListForProductGroup_num(ProductGroup_num);

	int confCount = 0;
	for (int i = 0; i < v.size(); i++) {
		RiversideDB_ExportProduct e = (RiversideDB_ExportProduct)
			v.elementAt(i);
		confCount += deleteExportConfForExportProduct_num(
			e.getExportProduct_num());
	}

	DMIDeleteStatement d = new DMIDeleteStatement ( this );
	buildSQL ( d, _D_EXPORTPRODUCT);
	d.addWhereClause ( "ExportProduct.ProductGroup_num = " 
		+ ProductGroup_num);
	int productCount = dmiDelete(d);

	int[] deleted = new int[3];
	deleted[0] = productCount;
	deleted[1] = confCount;
	deleted[2] = productCount + confCount;

	return deleted;
}

/**
Deletes records from ExportProduct that have the given ExportProduct_num,
first deleting the associated child records from ExportConf.
@param ExportProduct_num the ExportProduct_num of which to delete the 
records
@return a 3-element int array.  The array contains the number of records 
deleted in each table.<br>
[0] - the number of records deleted from ExportProduct<br>
[1] - the number of records deleted from ExportConf<br>
[2] - the total number of records deleted<br>
@throws Exception if an error occurs
*/
public int[] deleteExportProductForExportProduct_num(int ExportProduct_num)
throws Exception {
	int confCount = deleteExportConfForExportProduct_num(ExportProduct_num);
	
	DMIDeleteStatement d = new DMIDeleteStatement ( this );
	buildSQL ( d, _D_EXPORTPRODUCT);
	d.addWhereClause ( "ExportProduct.ExportProduct_num = " 
		+ ExportProduct_num);
	int productCount = dmiDelete(d);

	int[] deleted = new int[3];
	deleted[0] = productCount;
	deleted[1] = confCount;
	deleted[2] = productCount + confCount;

	return deleted;
}

/**
Deletes all records from ProductGroup and ExportProduct that match the given
ProductGroup_num, as well as all the child records in ExportConf of all the 
ExportProduct records.
@param ProductGroup_num the Product group against which to match records that
should be deleted.
@return a 4-element int array.  The array contains the number of records 
deleted in each table.<br>
[0] - the number of records deleted from ProductGroup<br>
[1] - the number of records deleted from ExportProduct<br>
[2] - the number of records deleted from ExportConf<br>
[3] - the total number of records deleted<br>
@throws Exception if an error occurs.
*/
public int[] deleteExportProductGroupForProductGroup_num(int ProductGroup_num)
throws Exception {
	int[] subDeleteds = 
		deleteExportProductForProductGroup_num(ProductGroup_num);
	
	int size = subDeleteds.length;
	size++;

	int[] deleted = new int[size];
	for (int i = 0; i < (size - 1); i++) {
		deleted[(i + 1)] = subDeleteds[i];
	}

	String sql = "DELETE FROM ProductGroup WHERE ProductGroup_num = " 
		+ ProductGroup_num;
	int prodDel = dmiDelete(sql);

	deleted[0] = prodDel;
	deleted[(size - 1)] = deleted[(size - 1)] + prodDel;

	return deleted;
}

/**
Deletes records from the ImportConf table that have the given 
ImportProduct_num.
@param ImportProduct_num the ImportProduct_num that determines the records
to be deleted
@return the number of records deleted
@throws Exception if an error occurs.
*/
public int deleteImportConfForImportProduct_num(long ImportProduct_num) 
throws Exception {
	String sql = "DELETE FROM ImportConf WHERE ImportProduct_num = " 
		+ ImportProduct_num;
	return dmiDelete(sql);
}

/**
Deletes records from the ImportConf table that have the given 
MeasType_num.
@param MeasType_num the MeasType_num that determines the records
to be deleted
@return an integer which tells how many records were deleted 
@throws Exception if an error occurs
*/
public int deleteImportConfForMeasType_num(int MeasType_num) 
throws Exception {
	String sql = "DELETE FROM ImportConf WHERE MeasType_num = " 
		+ MeasType_num;
	return dmiDelete(sql);
}


/**
Deletes all records from ProductGroup and ImportProduct that match the given
ProductGroup_num, as well as all the child records in ImportConf of all the 
ImportProduct records.
@param ProductGroup_num the Product group against which to match records that
should be deleted.
@return a 4-element int array.  The array contains the number of records 
deleted in each table.<br>
[0] - the number of records deleted from ProductGroup<br>
[1] - the number of records deleted from ImportProduct<br>
[2] - the number of records deleted from ImportConf<br>
[3] - the total number of records deleted<br>
@throws Exception if an error occurs.
*/
public int[] deleteImportProductGroupForProductGroup_num(int ProductGroup_num)
throws Exception {
	int[] subDeleteds = 
		deleteImportProductForProductGroup_num(ProductGroup_num);
	
	int size = subDeleteds.length;
	size++;

	int[] deleted = new int[size];
	for (int i = 0; i < (size - 1); i++) {
		deleted[(i + 1)] = subDeleteds[i];
	}

	String sql = "DELETE FROM ProductGroup WHERE ProductGroup_num = " 
		+ ProductGroup_num;
	int prodDel = dmiDelete(sql);

	deleted[0] = prodDel;
	deleted[(size - 1)] = deleted[(size - 1)] + prodDel;

	return deleted;
}

/**
Deletes records from ImportProduct that have the given ProductGroup_num,
first deleting the associated child records from ImportConf.
@param ProductGroup_num the ProductGroup_num for which to delete records
@return a 3-element int array.  The array contains the number of records 
deleted in each table.<br>
[0] - the number of records deleted from ImportProduct<br>
[1] - the number of records deleted from ImportConf<br>
[2] - the total number of records deleted<br>
@throws Exception if an error occurs
*/
public int[] deleteImportProductForProductGroup_num(int ProductGroup_num)
throws Exception {
	// First get a list of all the import products with that 
	// ProductGroup_num
	Vector v = readImportProductListForProductGroup_num(ProductGroup_num);

	int confCount = 0;
	for (int i = 0; i < v.size(); i++) {
		RiversideDB_ImportProduct e = (RiversideDB_ImportProduct)
			v.elementAt(i);
		confCount += deleteImportConfForImportProduct_num(
			e.getImportProduct_num());
	}
	
	DMIDeleteStatement d = new DMIDeleteStatement ( this );
	buildSQL ( d, _D_IMPORTPRODUCT);
	d.addWhereClause ( "ImportProduct.ProductGroup_num = " 
		+ ProductGroup_num);
	int productCount = dmiDelete(d);

	int[] deleted = new int[3];
	deleted[0] = productCount;
	deleted[1] = confCount;
	deleted[2] = productCount + confCount;

	return deleted;
}

/**
Deletes records from ImportProduct that have the given ImportProduct_num,
first deleting the associated child records from ImportConf.
@param ImportProduct_num the ImportProduct_num of which to delete the 
records
@return a 3-element int array.  The array contains the number of records 
deleted in each table.<br>
[0] - the number of records deleted from ImportProduct<br>
[1] - the number of records deleted from ImportConf<br>
[2] - the total number of records deleted<br>
@throws Exception if an error occurs
*/
public int[] deleteImportProductForImportProduct_num(int ImportProduct_num)
throws Exception {
	int confCount = deleteImportConfForImportProduct_num(ImportProduct_num);
	
	DMIDeleteStatement d = new DMIDeleteStatement ( this );
	buildSQL ( d, _D_IMPORTPRODUCT);
	d.addWhereClause ( "ImportProduct.ImportProduct_num = " 
		+ ImportProduct_num);
	int productCount = dmiDelete(d);

	int[] deleted = new int[3];
	deleted[0] = productCount;
	deleted[1] = confCount;
	deleted[2] = productCount + confCount;

	return deleted;
}

/**
Deletes records from the ImportType table that have the given 
Name. 
@param Name the Name for which to delete records.
@return the number of records deleted.
@throws Exception if an error occurs.
*/
public int deleteImportTypeForName(String Name)
throws Exception {
	DMIDeleteStatement d = new DMIDeleteStatement(this);
	buildSQL(d, _D_IMPORTTYPE);
	d.addWhereClause("Name = '" + Name + "'");
	return dmiDelete(d);
}

/**
Deletes records from the MeasCreateMethod table that have the given 
Method. 
@param Method the Method for which to delete records.
@return the number of records deleted.
@throws Exception if an error occurs.
*/
public int deleteMeasCreateMethodForMethod(String Method)
throws Exception {
	DMIDeleteStatement d = new DMIDeleteStatement(this);
	buildSQL(d, _D_MEASCREATEMETHOD);
	d.addWhereClause("Method = '" + Method + "'");
	return dmiDelete(d);
}

/**
Deletes records from the MeasLoc table that have the given MeasLoc_num.  In 
doing so, this also deletes the child records in MeasType, Area, Station and
StageDischargeReating and RatingTable.
@param MeasLoc_num the MeasLoc_num of the the records to be deleted
@return a 7-element integer array, each element of which is the number of 
records deleted from one of the tables.<p>  
[0] - the number of records deleted from MeasLoc<br>
[1] - the number of records deleted from StageDischargeRating<br>
[2] - the number of records deleted from RatingTable<br>
[3] - the number of records deleted from Station<br>
[4] - the number of records deleted from Area<br>
[5] - the total number of records deleted from MeasType and its children<br>
[6] - the total number of records deleted
@throws Exception if an error occurs
*/
public int[] deleteMeasLocForMeasLoc_num(long MeasLoc_num) 
throws Exception {
	int[] deleted = new int[7];
	deleted[0] = deleted[1] = deleted[2] = 0;
	deleted[3] = deleted[4] = deleted[5] = deleted[6] = 0;
	Vector v = readMeasTypeListForMeasLoc_num (MeasLoc_num); 
	int total = 0;
	if (v != null) {	
		for (int i = 0; i < v.size(); i++) {
			RiversideDB_MeasType m = 
				(RiversideDB_MeasType)v.elementAt(i);
			long MeasType_num = m.getMeasType_num();
			total += deleteMeasTypeForMeasType_num(MeasType_num)[6];
		}
	}
	deleted[6] = total;

	DMIDeleteStatement d = new DMIDeleteStatement(this);
	buildSQL(d, _D_AREA);
	d.addWhereClause("MeasLoc_num = " + MeasLoc_num);
	deleted[4] = dmiDelete(d);
	total += deleted[4];

	d = new DMIDeleteStatement(this);
	buildSQL(d, _D_STATION);
	d.addWhereClause("MeasLoc_num = " + MeasLoc_num);
	deleted[3] = dmiDelete(d);
	total += deleted[3];

	Vector v2 = readStageDischargeRatingListForMeasLoc_num(MeasLoc_num);
	RiversideDB_StageDischargeRating sdr = null;
	for (int i = 0; i < v2.size(); i++) {
		sdr = (RiversideDB_StageDischargeRating)v2.elementAt(i);
		d = new DMIDeleteStatement(this);
		buildSQL(d, _D_RATINGTABLE);
		d.addWhereClause("RatingTable_num = " 
			+ sdr.getRatingTable_num());
		deleted[2] += dmiDelete(d);
	}	
	total += deleted[2];	

	d = new DMIDeleteStatement(this);
	buildSQL(d, _D_STAGEDISCHARGERATING);
	d.addWhereClause("MeasLoc_num = " + MeasLoc_num);
	deleted[1] = dmiDelete(d);
	total += deleted[1];

	d = new DMIDeleteStatement ( this );
	buildSQL ( d, _D_MEASLOC);
	d.addWhereClause ( "MeasLoc.MeasLoc_num=" + MeasLoc_num ); 
	deleted[0] = dmiDelete(d);
	total += deleted[0];

	deleted[6] = total;

	return deleted;
}

/**
Deletes records from the MeasLocGroup that have the given MeasLocGroup_num.
@param MeasLocGroup_num the MeasLocGroup_num for which to delete records.
@return a 5-element integer array, each element of which is the number of 
records deleted in a specified operation:<br>
[0] - the number of records deleted from MeasLocGroup<br>
[1] - the number of records deleted from DBUserMeasLocGroupRelation<br>
[2] - the number of records deleted from State<br>
[2] - the number of records deleted from StateGroup<br>
[3] - the total number of records deleted
@throws Exception if an error occurs.
*/
public int[] deleteMeasLocGroupForMeasLocGroup_num(int MeasLocGroup_num) 
throws Exception {
	/*
	int relDeleted = 
		deleteDBUserMeasLocGroupRelationForMeasLocGroup_numDBUser_num(
		MeasLocGroup_num, _dbuser.getDBUser_num());
	*/
	int relDeleted = 
		deleteDBUserMeasLocGroupRelationForMeasLocGroup_num(
		MeasLocGroup_num);

	int[] stateGroupDeleted = deleteStateGroupForMeasLocGroup_num(
		MeasLocGroup_num);

	String sql = "DELETE FROM MeasLocGroup WHERE MeasLocGroup_num = "
		+ MeasLocGroup_num;
	int measLocDeleted = dmiDelete(sql);
	
	int[] deleteds = new int[5];
	deleteds[0] = measLocDeleted;
	deleteds[1] = relDeleted;
	deleteds[2] = stateGroupDeleted[1];
	deleteds[3] = stateGroupDeleted[0];
	deleteds[4] = (relDeleted + measLocDeleted);

	return deleteds;
}

/**
Deletes records from the MeasQualityFlag table that have the given 
Quality_flag. 
@param Quality_flag the Quality_flag for which to delete records.
@return the number of records deleted.
@throws Exception if an error occurs.
*/
public int deleteMeasQualityFlagForQuality_flag(String Quality_flag)
throws Exception {
	DMIDeleteStatement d = new DMIDeleteStatement(this);
	buildSQL(d, _D_MEASQUALITYFLAG);
	d.addWhereClause("Quality_flag = '" + Quality_flag + "'");
	return dmiDelete(d);
}

/**
Deletes records from the MeasReducRelation table that have the given 
Input Meas Type number.
@param InputMeasType_num the input meas type num for which to delete records.
@return the number of records deleted.
@throws Exception if an error occurs
*/
public int deleteMeasReducRelationForInputMeasType_num(long InputMeasType_num)
throws Exception {
	DMIDeleteStatement d = new DMIDeleteStatement(this);
	buildSQL(d, _D_MEASREDUCRELATION);
	d.addWhereClause("MeasReducRelation.InputMeasType_num = "
		+ InputMeasType_num);
	return dmiDelete(d);
}

/**
Deletes records from the MeasReducRelation table that have the given 
Output Meas Type number.
@param OutputMeasType_num the Output meas type num for which to delete records.
@return the number of records deleted.
@throws Exception if an error occurs
*/
public int deleteMeasReducRelationForOutputMeasType_num(long OutputMeasType_num)
throws Exception {
	DMIDeleteStatement d = new DMIDeleteStatement(this);
	buildSQL(d, _D_MEASREDUCRELATION);
	d.addWhereClause("MeasReducRelation.OutputMeasType_num = "
		+ OutputMeasType_num);
	return dmiDelete(d);
}

/**
Deletes records from the MeasReductionType table that have the given 
Type. 
@param Type the Type for which to delete records.
@return the number of records deleted.
@throws Exception if an error occurs.
*/
public int deleteMeasReductionTypeForType(String Type)
throws Exception {
	DMIDeleteStatement d = new DMIDeleteStatement(this);
	buildSQL(d, _D_MEASREDUCTIONTYPE);
	d.addWhereClause("Type = '" + Type + "'");
	return dmiDelete(d);
}

/**
Deletes records from the MeasTimeScale table that have the given 
Scale. 
@param Scale the Scale for which to delete records.
@return the number of records deleted.
@throws Exception if an error occurs.
*/
public int deleteMeasTimeScaleForScale(String Scale)
throws Exception {
	DMIDeleteStatement d = new DMIDeleteStatement(this);
	buildSQL(d, _D_MEASTIMESCALE);
	d.addWhereClause("Scale = '" + Scale + "'");
	return dmiDelete(d);
}

/**
Deletes records from the MeasTransProtocol table that have the given 
Protocol. 
@param Protocol the Protocol for which to delete records.
@return the number of records deleted.
@throws Exception if an error occurs.
*/
public int deleteMeasTransProtocolForProtocol(String Protocol)
throws Exception {
	DMIDeleteStatement d = new DMIDeleteStatement(this);
	buildSQL(d, _D_MEASTRANSPROTOCOL);
	d.addWhereClause("Protocol = '" + Protocol + "'");
	return dmiDelete(d);
}

/**
Deletes records from the MeasType table that have the given MeasType_num. 
It does this by first deleting any child records
in MeasReducRelation, MeasReduction, MeasTypeStats, MeasTypeSatus,
and ImportConf, then finally from MeasType.
@param MeasType_num the MeasType_num of the records to be deleted
@return a 15-element integer array, each element of which is the number of 
records deleted in a specific operation:<br>
[0] - the number of records deleted from MeasType<br>
[1] - the number of records deleted from ImportConf<br>
[2] - the number of records deleted from MeasTypeStatus<br>
[3] - the number of records deleted from MeasTypeStats<br>
[4] - the number of records deleted from MeasReduction<br>
[5] - the number of records deleted from MeasReducRelation<br>
[6] - the number of records deleted from TSFloodMonitor<br>
[7] - the number of records deleted from TSHour<br>
[8] - the number of records deleted from TSIrregMin<br>
[9] - the number of records deleted from TSIrregMonth<br>
[10] - the number of records deleted from TSLookup<br>
[11] - the number of records deleted from TSMinute<br>
[12] - the number of records deleted from TSMonth<br>
[13] - the number of records deleted from AutoUpdateProductMeasType<br>
[14] - the total number of records deleted
@throws Exception if an error occurs.
*/
public int[] deleteMeasTypeForMeasType_num(long MeasType_num)
throws Exception {
	int[] deleted = new int[15];

	deleted[0] = deleted[1] = deleted[2] = deleted[3] = 0;
	deleted[4] = deleted[5] = deleted[6] = deleted[7] = 0;
	deleted[8] = deleted[9] = deleted[10] = deleted[11] = 0;
	deleted[12] = deleted[13] = deleted[14] = 0;

	int total = 0;
	String sql = null;
	
	//sql = "DELETE FROM TSProduct WHERE TSProduct.MeasType_num = "
	sql = "DELETE FROM AutoUpdateProductMeasType WHERE " +
	"AutoUpdateProductMeasType.MeasType_num = " + MeasType_num;
	deleted[13] = dmiDelete(sql);
	total += deleted[13];
	
	sql = "DELETE FROM TSMonth WHERE TSMonth.MeasType_num = "
		+ MeasType_num;
	deleted[12] = dmiDelete(sql);
	total += deleted[12];
	
	sql = "DELETE FROM TSMinute WHERE TSMinute.MeasType_num = "
		+ MeasType_num;
	deleted[11] = dmiDelete(sql);
	total += deleted[11];
	
	sql = "DELETE FROM TSLookup WHERE TSLookup.MeasType_num = "
		+ MeasType_num;
	deleted[10] = dmiDelete(sql);
	total += deleted[10];
	
	sql = "DELETE FROM TSIrregMonth WHERE TSIrregMonth.MeasType_num = "
		+ MeasType_num;
	deleted[9] = dmiDelete(sql);
	total += deleted[9];
	
	sql = "DELETE FROM TSIrregMin WHERE TSIrregMin.MeasType_num = "
		+ MeasType_num;
	deleted[8] = dmiDelete(sql);
	total += deleted[8];
	
	sql = "DELETE FROM TSHour WHERE TSHour.MeasType_num = "
		+ MeasType_num;
	deleted[7] = dmiDelete(sql);
	total += deleted[7];
	
	sql = "DELETE FROM TSFloodMonitor WHERE "
		+ "TSFloodMonitor.MeasType_num = " + MeasType_num;
	deleted[6] = dmiDelete(sql);
	total += deleted[6];
	
	DMIDeleteStatement d = new DMIDeleteStatement ( this );
	buildSQL ( d, _D_MEASREDUCRELATION);
	d.addWhereClause ( "MeasReducRelation.OutputMeasType_num=" 
		+ MeasType_num ); 
	deleted[5] = dmiDelete(d);
	total += deleted[5];

	d = new DMIDeleteStatement ( this );
	buildSQL ( d, _D_MEASREDUCTION);
	d.addWhereClause ( "MeasReduction.OutputMeasType_num=" 
		+ MeasType_num ); 
	deleted[4] = dmiDelete(d);
	total += deleted[4];

	d = new DMIDeleteStatement ( this );
	buildSQL ( d, _D_MEASTYPESTATS);
	d.addWhereClause ( "MeasTypeStats.MeasType_num="
		+ MeasType_num ); 		
	deleted[3] = dmiDelete(d);
	total += deleted[3];

	d = new DMIDeleteStatement ( this );
	buildSQL ( d, _D_MEASTYPESTATUS);
	d.addWhereClause ( "MeasTypeStatus.MeasType_num="
		+ MeasType_num ); 				
	deleted[2] = dmiDelete(d);		
	total += deleted[2];
		
	d = new DMIDeleteStatement ( this );
	buildSQL ( d, _D_IMPORTCONF);
	d.addWhereClause ( "ImportConf.MeasType_num="
		+ MeasType_num ); 		
	deleted[1] = dmiDelete(d);		
	total += deleted[1];
		
	d = new DMIDeleteStatement ( this );
	buildSQL ( d, _D_MEASTYPE);
	d.addWhereClause ( "MeasType.MeasType_num=" + MeasType_num ); 
	deleted[0] = dmiDelete(d);	
	total += deleted[0];

	deleted[14] = total;

	return deleted;
}

/**
Deletes records from the MessageLog table that have the given 
Message_num. 
@param Message_num the Message_num for which to delete records.
@return the number of records deleted.
@throws Exception if an error occurs.
*/
public int deleteMessageLogForMessage_num(long Message_num)
throws Exception {
	DMIDeleteStatement d = new DMIDeleteStatement(this);
	buildSQL(d, _D_MESSAGELOG);
	d.addWhereClause("Message_num = " + Message_num);
	return dmiDelete(d);
}

/**
Deletes records from the Props table that have the given 
Variable. 
@param Variable the Variable for which to delete records.
@return the number of records deleted.
@throws Exception if an error occurs.
*/
public int deletePropsForVariable( String Variable )
throws Exception {
	DMIDeleteStatement d = new DMIDeleteStatement( this );
	buildSQL(d, _S_PROPS);
	d.addWhereClause( "Variable = '" + Variable + "'" );
	return dmiDelete(d);
}

/**
Deletes all records with the given RatingTable_Num from RatingTable.
@param RatingTable_num the RatingTable_num for which to delete records.
@return the number of records deleted.
@throws Exception if an error occurs.
*/
public int deleteRatingTableForRatingTable_num(long RatingTable_num)
throws Exception {
	String sql = "DELETE FROM RatingTable WHERE "
		+ "RatingTable.RatingTable_num = " + RatingTable_num;
	return dmiDelete(sql);
}

/**
Deletes records from the Scenario table that have the given 
Scenario_num. 
@param Scenario_num the Scenario_num for which to delete records.
@return the number of records deleted.
@throws Exception if an error occurs.
*/
public int deleteScenarioForScenario_num(long Scenario_num)
throws Exception {
	DMIDeleteStatement d = new DMIDeleteStatement(this);
	buildSQL(d, _D_SCENARIO);
	d.addWhereClause("Scenario_num = " + Scenario_num);
	return dmiDelete(d);
}

/**
Deletes records from the SHEFType table that have the given 
SHEF_pe. 
@param SHEF_pe the SHEF_pe for which to delete records.
@return the number of records deleted.
@throws Exception if an error occurs.
*/
public int deleteSHEFTypeForSHEF_pe(String SHEF_pe)
throws Exception {
	DMIDeleteStatement d = new DMIDeleteStatement(this);
	buildSQL(d, _D_SHEFTYPE);
	d.addWhereClause("SHEF_pe = '" + SHEF_pe + "'");
	return dmiDelete(d);
}

/**
Deletes all records with the given StateGroup_num from StateGroup.
@param StateGroup_num the StateGroup_num for which to delete records.
@return the number of records deleted.
@throws Exception if an error occurs.
*/
public int deleteStateForStateGroup_num(long StateGroup_num)
throws Exception {
	String sql = "DELETE FROM State WHERE State.StateGroup_num = "
		+ StateGroup_num;
	return dmiDelete(sql);
}

/**
Deletes all records with the given StateGroup_num and 
OperationStateRelation_num from StateGroup.
@param StateGroup_num the StateGroup_num for which to delete records.
@param OperationStateRelation_num the OperationStateRelation_num for which 
to delete records.
@return the number of records deleted.
@throws Exception if an error occurs.
*/
public int deleteStateForStateGroup_numOperationStateRelation_num(
long StateGroup_num, int OperationStateRelation_num)
throws Exception {
	String sql = "DELETE FROM State WHERE State.StateGroup_num = "
		+ StateGroup_num + " AND State.OperationStateRelation_num = "
		+ OperationStateRelation_num;
	return dmiDelete(sql);
}

/**
Deletes all records with the given MeasLocGroup_num from StateGroup, and also
deletes all records from State if they share the same StateGroup_num.
@param MeasLocGroup_num the MeasLocGroup_num for which to delete records.
@return a 3-element int array.  The array contains the number of records 
deleted in each table.<br>
[0] - the number of records deleted from StateGroup<br>
[1] - the number of records deleted from State<br>
[2] - the total number of records deleted<br>
@throws Exception if an error occurs.
*/
public int[] deleteStateGroupForMeasLocGroup_num(int MeasLocGroup_num)
throws Exception {
	Vector v = readStateGroupListForMeasLocGroup_num(MeasLocGroup_num);
	RiversideDB_StateGroup sg = null;
	int stateTotal = 0;

	int[] deleteds = new int[3];
	deleteds[0] = deleteds[1] = deleteds[2] = 0;
	
	for (int i = 0; i < v.size(); i++) {
		sg = (RiversideDB_StateGroup)v.elementAt(i);
		stateTotal += deleteStateForStateGroup_num(
			sg.getStateGroup_num());
	}
	
	String sql = "DELETE FROM StateGroup WHERE "
		+ "StateGroup.MeasLocGroup_num = " + MeasLocGroup_num;
	deleteds[0] = dmiDelete(sql);
	deleteds[1] = stateTotal;
	deleteds[2] = deleteds[0] + deleteds[1];
	return deleteds;
}

/**
Deletes records from the TableLayout table that have the given 
TableLayout_num. 
@param TableLayout_num the TableLayout_num for which to delete records.
@return the number of records deleted.
@throws Exception if an error occurs.
*/
public int deleteTableLayoutForTableLayout_num(long TableLayout_num)
throws Exception {
	DMIDeleteStatement d = new DMIDeleteStatement(this);
	buildSQL(d, _D_TABLELAYOUT);
	d.addWhereClause("TableLayout_num = " + TableLayout_num);
	return dmiDelete(d);
}

/**
Deletes records from the TSProduct and TSProductProps tables for the TSProduct
with the specified identifier.<p>  Permissions are not checked in this method 
as to whether the user has permissiont o 
@param identifier the identifier String of the TSProduct in the database.
Identifier is forced to be unique in the code that writes a TSProduct to the
database.  
@return a 3-element array.  The first element contains the number of records
deleted from the TSProduct table.  The second element contains the number of 
records deleted from the TSProductProps table.  The third element contains the
total number of records deleted.
*/
public int[] deleteTSProductForIdentifier(String identifier)
throws Exception {
	int[] deleted = new int[3];

	RiversideDB_TSProduct tsp = readTSProductForIdentifier(identifier);
	if (tsp == null) {
		deleted[0] = 0;
		deleted[1] = 0;
		deleted[2] = 0;
		return deleted;
	}
	
	int tsproduct_num = tsp.getTSProduct_num();	
			
	int propsCount = deleteTSProductPropsForTSProduct_num(tsproduct_num);

	String sql = "DELETE FROM tsproduct WHERE "
		+ "tsproduct.identifier = '"
		+ identifier + "'";
	deleted[0] = dmiDelete(sql);
	deleted[1] = propsCount;
	deleted[2] = deleted[0] + deleted[1];

	return deleted;
}

/**
Deletes records from the TSProduct and TSProductProps tables for the TSProduct
with the specified identifier.<p>  Permissions are not checked in this method 
as to whether the user has permissiont o 
@param tsproduct_num the tsproduct_num of the records to delete.
@return a 3-element array.  The first element contains the number of records
deleted from the TSProduct table.  The second element contains the number of 
records deleted from the TSProductProps table.  The third element contains the
total number of records deleted.
*/
public int[] deleteTSProductForTSProduct_num(int tsproduct_num)
throws Exception {
	int[] deleted = new int[3];

	int propsCount = deleteTSProductPropsForTSProduct_num(tsproduct_num);

	String sql = "DELETE FROM tsproduct WHERE "
		+ "tsproduct.tsproduct_num = " + tsproduct_num;
	deleted[0] = dmiDelete(sql);
	deleted[1] = propsCount;
	deleted[2] = deleted[0] + deleted[1];

	return deleted;
}

/**
Deletes records from the TSProductProps tables for the the tsproduct with the
specified TSProduct_num.<p>
@param tsproduct_num the TSProduct_num of the records to delete.
@return the number of records to be deleted.
*/
public int deleteTSProductPropsForTSProduct_num(int tsproduct_num) 
throws Exception {
		
	String sql = "DELETE FROM TSProductProps WHERE TSProductProps.TSProduct_num = " + tsproduct_num;
	return dmiDelete(sql);
}

/**
Determine the database version by examining the table structure for the
database.  The following versions are known for RiversideDB:
<ul>
<li>	03000020041001 - Latest version</li>
	<ol>
	<li>New table 'Operation'</li>
	<li>New table 'OperationStateRelation'</li>
	<li>State table lost fields 'Module', 'Variable', 'Val', and 'Seq'</li>
	<li>State table added fields 'OperationStateRelation_num', 'Sequence',
	and 'ValueStr'</li>
	</ol>
<li>	02080020030422
	<ol>
	<li>Tables table has IsReference</li>
	<li>Tables table has IsTSTemplate</li>
	</ol>
</ul>
*/
public void determineDatabaseVersion() {
	// Default until more checks are added...
	String routine = "RiversideDB_DMI.determineDatabaseVersion";
	boolean version_found = false;
	try {
		if (DMIUtil.databaseTableHasColumn(this,
			"State", "OperationStateRelation_num")) {
			setDatabaseVersion(_VERSION_030000_20041001);
			version_found = true;
		}
	}
	catch (Exception e) {
		// Ignore ...
		Message.printWarning(2, routine, e);
	}

	if (!version_found) {
		try {	
			if (DMIUtil.databaseTableHasColumn(this, 
				"Tables", "IsReference")) {
				setDatabaseVersion(_VERSION_020800_20030422);
				version_found = true;
			}
		}
		catch (Exception e) {
			// Ignore...
			Message.printWarning ( 2, routine, e );
		}
	}

	if (!version_found) {
		// Assume this...
		setDatabaseVersion ( _VERSION_020601_20020625 );
	}
	Message.printStatus ( 1, routine,
		"RiversideDB version determined to be at least "
		+getDatabaseVersion() );
}

/**
Wrapped for dmiDelete() method that prints the query being executed to 
Status(2) if IOUtil.testing() is on.
*/
public int dmiDelete(String q) 
throws java.sql.SQLException {
	if (IOUtil.testing()) {
		Message.printStatus(2, "", "" + q.toString());
	}

	return super.dmiDelete(q);
}

/**
Wrapped for dmiSelect() method that prints the query being executed to 
Status(2) if IOUtil.testing() is on.
*/
public ResultSet dmiSelect(DMISelectStatement q)
throws java.sql.SQLException {
	if (IOUtil.testing()) {
		Message.printStatus(2, "", "" + q.toString());
	}

	return super.dmiSelect(q);
}

/**
Wrapped for dmiSelect() method that prints the query being executed to 
Status(2) if IOUtil.testing() is on.
*/
public ResultSet dmiSelect(String q) 
throws java.sql.SQLException {
	if (IOUtil.testing()) {
		Message.printStatus(2, "", "" + q.toString());
	}

	return super.dmiSelect(q);
}

/**
Wrapped for dmiWrite() method that prints the query being executed to 
Status(2) if IOUtil.testing() is on.
*/
public int dmiWrite(String q) 
throws java.sql.SQLException {
	if (IOUtil.testing()) {
		Message.printStatus(2, "", "" + q.toString());
	}

	return super.dmiWrite(q);
}

/**
Wrapped for dmiWrite() method that prints the query being executed to 
Status(2) if IOUtil.testing() is on.
*/
public int dmiWrite(DMIWriteStatement q, int type)
throws Exception {
	if (IOUtil.testing()) {
		Message.printStatus(2, "", "" + q.toString());
	}

	return super.dmiWrite(q, type);
}

// E FUNCTIONS
// F FUNCTIONS

/**
Finalize for garbage collection.
@exception Throwable if there is an error.
*/
protected void finalize() throws Throwable {
	_RiversideDB_Tables_Vector = null;
	_dbgroup = null;
	_dbuser = null;
	super.finalize();
}

// G FUNCTIONS

/**
Return a Vector of String containing useful database property information.
@return a Vector containing database properties as Strings.
@param level A numerical value that can be used to control the amount
of output.  A value of 3 returns full output, including database version,
history of changes, server information (e.g., for use in a properties dialog);
2 returns a concise output including server name (e.g., for use in the header
of an output file - NOT IMPLEMENTED; 1 for very concise output (e.g.,
the database name and version, for use in a product footer) - 
<b>NOT IMPLEMENTED</b>.
*/
public Vector getDatabaseProperties ( int level )
{	Vector v = new Vector ();
	v.addElement ( "Database Engine:  " + getDatabaseEngine() );
	if ( getDatabaseName() == null ) {
		v.addElement ( "Connect Method:  ODBC DSN" );
	}
	else {	v.addElement (
		"Connect Method:  JDBC using the following information  " );
		v.addElement ( "Database server:  " + getDatabaseServer() );
		v.addElement ( "Database name:  " + getDatabaseName() );
	}
	v.addElement ( "Database version appears to be (VVVVVVYYYYMMDD):  " +
			getDatabaseVersion() );
	v.addElement ( "" );
	v.addElement ( "Database history (most recent at top):" );
	v.addElement ( "" );
	return v;
}

/**
Returns "RiversideDB"
@return "RiversideDB"
*/
public String getDMIName() {
	return "RiversideDB";
}

/**
This function determines the extreme value of the specified field from the 
reqested table via using max(field) or min(field) and the the specified dmi 
connection.
@param table table name
@param field table field to determine the max value of
@param flag "MAX" or "MIN" depending upon which extreme is desired.
@return returns a int of the extreme record or DMIUtil.MISSING_INT if 
an error occured.
@deprecated use DMIUtil.getExtremeRecord() instead.
*/
public long getExtremeRecord(String table, String field, String flag) {
	String routine = "RiversideDB_DMI.getExtremeRecord";
	try {
		String query = "select " + flag + "(" + field.trim() + ") from "
			+ table.trim();
		ResultSet rs = dmiSelect(query);
	
		long extreme = DMIUtil.MISSING_LONG;
		if (rs.next()) {
			extreme = rs.getLong(1);
			if (rs.wasNull()) {
				extreme = DMIUtil.MISSING_LONG;
			}
		}
	        return extreme;
	}
	catch (Exception e) {
		Message.printWarning(1, routine, "Error finding extreme.");
		Message.printWarning(1, routine, e);
		return DMIUtil.MISSING_LONG;
	}
}

/**
This function determines the max value of the specified field from the reqested
table via using max(field) and the the specified dmi connection.
@param table table name
@param field table field to determine the max value of
@return returns max record or DMIUtil.MISSING_INT if an error occured.
@deprecated use DMIUtil.getExtremeRecord() instead.
*/
public long getMaxRecord (String table, String field) {
	return getExtremeRecord(table, field, "MAX");
}

/**
This function determines the min value of the specified field from the reqested
table via using min(field) and the the specified dmi connection.
@param table table name
@param field table field to determine the min value of
@return returns min record or DMIUtil.MISSING_INT if an error occured.
@deprecated use DMIUtil.getExtremeRecord() instead.
*/
public long getMinRecord(String table, String field) {
	return getExtremeRecord(table, field, "MIN");
}

/**
Returns the database version numbers, each stored in a separate element of a
3-element String array.  For instance, database version 2.08.00 would be 
returned in the following array:<p>
<ul>
<li>[0] = "02"</li>
<li>[1] = "08"</li>
<li>[2] = "00"</li>
</ul>
@return the database version numbers, each stored in a separate element of a
3-element String array.  null is returned if the database version cannot be
determined.
*/
public String[] getDatabaseVersionArray() {
	String[] version = new String[3];

	if (isDatabaseVersionAtLeast(_VERSION_030000_20041001)) {
		version[0] = "03";
		version[1] = "00";
		version[2] = "00";
		return version;
	}
	else if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)) {
		version[0] = "02";
		version[1] = "08";
		version[2] = "00";
		return version;
	}
	else if (isDatabaseVersionAtLeast(_VERSION_020601_20020625)) {
		version[0] = "02";
		version[1] = "06";
		version[2] = "01";
	}
	
	return null;
}

/**
Returns the current db user.
@return the current db user.
*/
public RiversideDB_DBUser getDBUser() {
	return _dbuser;
}

/**
Return the global Vector of RiversideDB_Tables
@return the global Vector of RiversideDB_Tables.
*/
public Vector getRiversideDB_Tables () {
	return _RiversideDB_Tables_Vector;
}

/**
Return the name of the TSSupplier.  This is used for messages.
*/
public String getTSSupplierName() {
	return "RiversideDB";
}

// H FUNCTIONS
// I FUNCTIONS

/**
Convert a Vector of RiversideDB_DataDimension to RTi.Util.IO.DataDimension, to
facilitate efficient access to data dimensions and units throughout all code.
@param dataDimension Vector to process.
@throws Exception if an error occurs
*/
public void initializeDataDimension (Vector dataDimension)
throws Exception {
	String routine = "RiversideDB_DMI.initializeDataDimension";

	int size = 0;
	if ( dataDimension != null ) {
		size = dataDimension.size();
	}
	
	if (size == 0) {
		String message = "No DataDimension read from database "
			+ "-- empty set.";
		Message.printWarning ( 2, routine, message );
		throw new Exception ( message );
	}

	// For each object, break apart and add units to the global list...

	for (int i = 0; i < size; i++) {
		RiversideDB_DataDimension d = 
			(RiversideDB_DataDimension)dataDimension.elementAt(i);
		try {	// Else add the units...
			DataDimension.addDimension ( 
				new DataDimension ( d.getDimension(),
						d.getDescription() ) );
		}
		catch ( Exception e ) {
			Message.printWarning ( 2, routine,
			"Error processing RiversideDB_DataDimension "
			+ " object at object # [" + i +
			"]\nObject dump follows:" + d.toString());
		}
	}

	routine = null;
}

/**
Convert a Vector of RiversideDB_DataUnit to RTi.Util.IO.DataUnit, to facilitate
efficient access to data units by applications.
@param dataUnits Vector to process.
@throws Exception if an error occurs
*/
public void initializeUnits (Vector dataUnits)
throws Exception {	
	String routine = "RiversideDB_DMI.initializeUnits";

	int size = 0;
	if ( dataUnits != null ) {
		size = dataUnits.size();
	}
	
	if (size == 0) {
		String message = "no dataunits read from database -- empty set";
		Message.printWarning ( 2, routine, message );
		throw new Exception ( message );
	}

	// For each object, break apart and add units to the global list...

	DataUnits units;
	String base_string;

	for (int i = 0; i < size; i++) {
		RiversideDB_DataUnits d = 
			(RiversideDB_DataUnits)dataUnits.elementAt(i);
		try {	// Else add the units...
			units = new DataUnits ();

			units.setDimension (d.getDimension());
			base_string = d.getBase_unit();
			if ( base_string.equalsIgnoreCase("Y")) {
				// Base units for the dimension...
				units.setBaseFlag ( 1 );
			}
			else {	
				units.setBaseFlag ( 0 );
			}
	
			units.setAbbreviation (d.getUnits_abbrev());
			units.setSystem (d.getUnits_system());
			units.setLongName (d.getUnits_description());
			units.setOutputPrecision (d.getOutput_precision());
			units.setMultFactor (d.getMult_factor());
			units.setAddFactor (d.getAdd_factor());

			// Add the units to the list...

			DataUnits.addUnits ( units );
		}
		catch ( Exception e ) {
			Message.printWarning ( 2, routine,
				"Error processing RiversideDB_DataUnits "
				+ " object at object # [" + i
				+ "]\nObject dump follows:" + d.toString());
		}
	}

	// Check the units for consistency...

	//DataUnits.checkUnitsData();
}

/**
Returns true if the current user is the root user, false otherwise.
@return true if the current user is the root user, false otherwise.
*/
public boolean isRootUser() {
	if (_dbuser.getLogin().trim().equals("root")) {
		return true;
	}
	return false;
}

// J FUNCTIONS
// K FUNCTIONS
// L FUNCTIONS
// M FUNCTIONS
// N FUNCTIONS
// O FUNCTIONS
// P FUNCTIONS
// Q FUNCTIONS

// R FUNCTIONS

/**
Reads all the action data models from the data source.  From AlertIOInterface.
@return a Vector of data models for all the actions in the system.
*/
public Vector readAllActionDataModels() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_ACTION);
	ResultSet rs = dmiSelect(q);
	Vector v = toActionList(rs);
	closeResultSet(rs);	
	return v;
}

/**
Reads an ActionDataModel with the given ID number.  From AlertIOInterface.
@param actionNum the id number of the data model to be read.
@return the ActionDataModel with the information read from the data provider.
REVISIT (JTS - 2006-03-23)
even necessary anymore?  
*/
public ActionDataModel readActionDataModel(int actionNum) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_ACTION);
	q.addWhereClause("Action.action_num = " + actionNum);
	ResultSet rs = dmiSelect(q);
	Vector v = toActionList(rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	else {
		return (ActionDataModel)v.elementAt(0);
	}
}

/**
Reads the contact nums associated with the given action.
@param actionNum the action num of the action for which to read contact nums.
@return an int array of the contact nums.
@throws Exception if an error occurs.
*/
public int[] readActionContactNums(int actionNum) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_ACTIONCONTACT);
	q.addWhereClause("ActionContactRelationship.ActionNum = " + actionNum);
	ResultSet rs = dmiSelect(q);
	Vector v = new Vector();
	int index = 1;
	int i;
	while (rs.next()) {
		index = 1;
		i = rs.getInt(index++);
		// ... and ignore -- this is the ActionNum
		
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			v.add(new Integer(i));
		}
	}
	closeResultSet(rs);

	int[] nums = new int[v.size()];
	for (i = 0; i < nums.length; i++) {
		nums[i] = ((Integer)v.elementAt(i)).intValue();
	}
	return nums;
}

/**
Read the Area data for all records matching a given MeasLoc_num.
@param MeasLoc_num MeasLoc_num to query for.
@return a vector of objects of type RiversideDB_Area.
@throws Exception if an error occurs
*/
public Vector readAreaListForMeasLoc_num ( long MeasLoc_num )
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_AREA );
	q.addWhereClause ( "Area.MeasLoc_num = " + MeasLoc_num ); 
	ResultSet rs = dmiSelect(q);
	Vector v = toAreaList (rs);
	closeResultSet(rs);
	return v;
}

/**
Read the AutoUpdateProduct table for all records.
@return a Vector of AutoUpdateProduct objects.
@throws Exception if an exception occurs.
*/
public Vector readAutoUpdateProductList()
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_AUTOUPDATEPRODUCT);
	ResultSet rs = dmiSelect(q);
	Vector v = toAutoUpdateProductList(rs);
	closeResultSet(rs);
	return v;
}

/**
Reads the given contact.  From AlertIOInterface.
@param contactNum the contact to read.
@return a Contact.
@throws Exception if an error occurs.
*/
public Contact readContact(int contactNum) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_CONTACT);
	q.addWhereClause("Contact.ContactNum = " + contactNum);
	ResultSet rs = dmiSelect(q);
	Vector v = toDataTestFunctionList(rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	else {
		return (Contact)v.elementAt(0);
	}
}

/**
Read all DataDimension records, ordered by the Dimension field.
@return a Vector of all DataDimension values
@throws Exception if an exception occurs.
*/
public Vector readDataDimensionList()
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_DATADIMENSION );
	q.addOrderByClause("DataDimension.Dimension");
	ResultSet rs = dmiSelect(q);
	Vector v = toDataDimensionList (rs);
	closeResultSet(rs);
	return v;
}

/**
Read the data dimension record that matches the given dimension.
@param dimension the dimension to match
@return a DataDimension values, or null if none matched
@throws Exception if an exception occurs.
*/
public RiversideDB_DataDimension readDataDimensionForDimension(String dimension)
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_DATADIMENSION );
	q.addWhereClause("DataDimension.Dimension = '" + dimension + "'");
	q.addOrderByClause("DataDimension.Dimension");
	ResultSet rs = dmiSelect(q);
	Vector v = toDataDimensionList (rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	return (RiversideDB_DataDimension)v.elementAt(0);
}

/**
Read all DataSource records, ordered by the Source_abbrev field.
@return a Vector of all DataSource objects
@throws Exception if an error occurs.
*/
public Vector readDataSourceList()
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_DATASOURCE );
	q.addOrderByClause("DataSource.Source_abbrev");
	ResultSet rs = dmiSelect(q);
	Vector v = toDataSourceList (rs);
	closeResultSet(rs);
	return v;
}

/**
Returns the RiversideDB_DataSource record that matches the given Source_abbrev.
@param Source_abbrev the Source_abbrev to match.
@return the matching RiversideDB_DataSource record, or null if none match.
@throws Exception if an error occurs.
*/
public RiversideDB_DataSource readDataSourceForSource_abbrev(
String Source_abbrev)
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_DATASOURCE );
	q.addWhereClause("DataSource.Source_abbrev = '" + Source_abbrev + "'");
	q.addOrderByClause("DataSource.Source_abbrev");
	ResultSet rs = dmiSelect(q);
	Vector v = toDataSourceList (rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	return (RiversideDB_DataSource)v.elementAt(0);
}

/**
Reads the action nums that correspond to a data test.
@param dataTestNum the ID of the data test for which to read action nums.
@return an integer array of the action nums (or null if there are no associated
actions).
@throws Exception if an error occurs.
*/
public int[] readDataTestActionNums(int dataTestNum) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_DATATESTACTION);
	q.addWhereClause("DataTestActionRelationship.DataTestNum = " 
		+ dataTestNum);
	ResultSet rs = dmiSelect(q);
	Vector v = new Vector();
	int index = 1;
	int i;
	while (rs.next()) {
		index = 1;
		i = rs.getInt(index++);
		// ... and ignore -- this is the DataTestNum.  Only interested
		// here in the action nums.
		
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			v.add(new Integer(i));
		}
	}
	closeResultSet(rs);

	int size = v.size();

	if (size == 0) {
		return null;
	}
	
	int[] nums = new int[size];
	for (i = 0; i < size; i++) {
		nums[i] = ((Integer)v.elementAt(i)).intValue();
	}
	return nums;
}

/**
Reads a DataTestDataModel with the given ID number.  From AlertIOInterface.
@param dataTestNum the id number of the data model to be read.
@return the DataTestDataModel with the information read from the data provider.
*/
public DataTestDataModel readDataTestDataModel(int dataTestNum) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_DATATEST);
	q.addWhereClause("DataTest.DataTestNum = " + dataTestNum);
	ResultSet rs = dmiSelect(q);
	Vector v = toDataTestList(rs);
	closeResultSet(rs);
	
	if (v == null || v.size() == 0) {
		return null;
	}

	DataTestDataModel model = (DataTestDataModel)v.elementAt(0);
	int[] actionNums = readDataTestActionNums(dataTestNum);
	model.setActionNums(actionNums);
	return readDataTestStatusForDataTestNum(dataTestNum, model);
}

/**
Reads data test status data, puts it in the passed-in data model, and returns
the data model.
*/
public DataTestDataModel readDataTestStatusForDataTestNum(
int dataTestNum, DataTestDataModel model)
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_DATATESTSTATUS);
	q.addWhereClause("DataTestStatus.DataTestNum = " + dataTestNum);
	ResultSet rs = dmiSelect(q);

	String last = DMIUtil.MISSING_STRING;
	String next = DMIUtil.MISSING_STRING;
	boolean wasPositive = false;

	if (rs.next()) {
		int index = 1;
		rs.getInt(index++);
		String s = rs.getString(index++);
		if (!rs.wasNull()) {
			last = s;
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			next = s;
		}
		boolean b = rs.getBoolean(index++);
		if (!rs.wasNull()) {
			wasPositive = b;
		}
	}

	model.setLastTestDateTime(last);
	model.setNextTestDateTime(next);
	model.setWasLastTestPositive(wasPositive);

	return model;
}

/**
Reads a DataTestExpressionDataModel with the given ID number. 
From AlertIOInterface.
@param expressionNum the id number of the data model to be read.
@return a DataTestExpressionDataModel with the information read from the 
data provider.
*/
public DataTestExpressionDataModel readDataTestExpressionDataModel(
int expressionNum) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_DATATESTEXPRESSION);
	q.addWhereClause("DataTestExpression.DataTestExpressionNum = " 
		+ expressionNum);
	ResultSet rs = dmiSelect(q);
	Vector v = toDataTestExpressionList(rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	else {
		return (DataTestExpressionDataModel)v.elementAt(0);
	}
}

/**
Reads a DataTestFunctionDataModel with the given ID number.  
From AlertIOInterface.
@param functionNum the id number of the data model to be read.
@return a DataTestFunctionDataModel with the information read from the 
data provider.
*/
public DataTestFunctionDataModel readDataTestFunctionDataModel(int functionNum)
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_DATATESTFUNCTION);
	q.addWhereClause("DataTestFunction.DataTestFunctionNum = " 
		+ functionNum);
	ResultSet rs = dmiSelect(q);
	Vector v = toDataTestFunctionList(rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	else {
		DataTestFunctionDataModel model 
			= (DataTestFunctionDataModel)v.elementAt(0);
		String[] ids = readDataTestFunctionInputIDs(functionNum);
		model.setInputDataIDs(ids);
		return model;
	}
}

/**
Reads the Input ID nums associated with a data test function.
@param dataTestFunctionNum the ID of the data test function to read input IDs
for.
@return a String array of the input IDs or null if there are no associated 
input IDs.
@throws Exception if an error occurs.
*/
public String[] readDataTestFunctionInputIDs(int dataTestFunctionNum) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_DATATESTFUNCTIONINPUTID);
	q.addWhereClause("DataTestFunctionInputIDs.DataTestFunctionNum = "
		+ dataTestFunctionNum);
	q.addOrderByClause("DataTestFunctionInputIDs.Position");
	ResultSet rs = dmiSelect(q);
	Vector v = new Vector();
	int i;
	int index;
	String s;
	while (rs.next()) {
		index = 1;
		i = rs.getInt(index++);	// function num
		i = rs.getInt(index++);	// position
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			v.add(s.trim());
		}
	}
	closeResultSet(rs);

	int size = v.size();

	if (size == 0) {
		return null;
	}

	String[] ids = new String[size];
	for (i = 0; i < size; i++) {
		ids[i] = (String)v.elementAt(i);
	}
	return ids;
}

/**
Read all DataType records, ordered by the DataType field.
@return a Vector of all DataType objects
@throws Exception if an error occurs.
*/
public Vector readDataTypeList()
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_DATATYPE );
	q.addOrderByClause("DataType.DataType");
	ResultSet rs = dmiSelect(q);
	Vector v = toDataTypeList (rs);
	closeResultSet(rs);
	return v;
}

/**
Returns the RiversideDB_DataType record that matches the given DataType.
@param DataType the DataType to match.
@return the matching RiversideDB_DataType record, or null if none match.
@throws Exception if an error occurs.
*/
public RiversideDB_DataType readDataTypeForDataType(String DataType)
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_DATATYPE );
	q.addWhereClause("DataType.DataType = '" + DataType + "'");
	q.addOrderByClause("DataType.DataType");
	ResultSet rs = dmiSelect(q);
	Vector v = toDataTypeList (rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	return (RiversideDB_DataType)v.elementAt(0);
}

/**
Read all DataUnits records, ordered by the Units_abbrev field.
@return a Vector of all DataUnits objects
@throws Exception if an error occurs.
*/
public Vector readDataUnitsList()
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_DATAUNITS );
	q.addOrderByClause("DataUnits.Units_abbrev");
	ResultSet rs = dmiSelect(q);
	Vector v = toDataUnitsList (rs);
	closeResultSet(rs);
	return v;
}

/**
Returns the RiversideDB_DataUnits record that matches the given Units_abbrev.
@param Units_abbrev the Units_abbrev to match.
@return the matching RiversideDB_DataUnits record, or null if none match.
@throws Exception if an error occurs.
*/
public RiversideDB_DataUnits readDataUnitsForUnits_abbrev(String Units_abbrev)
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_DATAUNITS );
	q.addWhereClause("DataUnits.Units_abbrev = '" + Units_abbrev + "'");
	q.addOrderByClause("DataUnits.Units_abbrev");
	ResultSet rs = dmiSelect(q);
	Vector v = toDataUnitsList (rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	return (RiversideDB_DataUnits)v.elementAt(0);
}

/**
Read all DBGroup records.
@return a Vector of RiversideDB_DBGroup objects
@throws Exception if an error occurs.
*/
public Vector readDBGroupList() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_DBGROUP);
	ResultSet rs = dmiSelect(q);
	Vector v = toDBGroupList(rs);
	closeResultSet(rs);
	return v;
}

/**
Executes a query on the DBGRoup table for the record that matches the given
group number.
@param DBGroup_num the group number for which to return the matching group
@return the matching RiversideDB_DBGroup object, or null if none were found.
@throws Exception if an error occurs.
*/
public RiversideDB_DBGroup readDBGroupForDBGroup_num(int DBGroup_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_DBGROUP);
	q.addWhereClause("DBGroup.DBGroup_num = " + DBGroup_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toDBGroupList(rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	return (RiversideDB_DBGroup)v.elementAt(0);
}

/**
Executes query on DBUser table, returning the user record that matches the 
provided DBUser.
@param DBUser_num the user num for which to return a record from the table
@return a RiversideDB_DBUser object or null if nothing matched.
@throws Exception if an error occurs.
*/
public RiversideDB_DBUser readDBUserForDBUser_num(int DBUser_num)
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_DBUSER);
	q.addWhereClause("DBUser.DBUser_num = " + DBUser_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toDBUserList(rs);
	closeResultSet(rs);
	if (v.size() == 0) {
		return null;
	}
	else {
		return (RiversideDB_DBUser)v.elementAt(0);
	}
}

/**
Executes query on DBUser table, returning the record that matches the 
provided Login.
@param login the login for which to return user data.
@return a RiversideDB_DBUser object or null if nothing is found.
@throws Exception if an error occurs.
*/
public RiversideDB_DBUser readDBUserForLogin(String login) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_DBUSER);
	q.addWhereClause("DBUser.Login = '" + escape(login) + "'");
	ResultSet rs = dmiSelect(q);
	Vector v = toDBUserList(rs);
	closeResultSet(rs);
	if (v.size() == 0) {
		return null;
	}
	else {
		return (RiversideDB_DBUser)v.elementAt(0);
	}
}

/**
Executes a query on the DBUser table and returns all records.
@return a Vector of RiversideDB_DBUser objects, one for each record.
@throws Exception if an error occurs.
*/
public Vector readDBUserList() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_DBUSER);
	ResultSet rs = dmiSelect(q);
	Vector v = toDBUserList(rs);
	closeResultSet(rs);
	
	return v;
}

/**
Executes a query on the DBUserDBGroupRelation table, returning all records
with matching DBGroup_nums
@param DBGroup_num the DBGroup_num for which to match records
@return a Vector of RiversideDB_DBUserDBGroupRelation objects.
@throws Exception if an error occurs
*/
public Vector readDBUserDBGroupRelationListForDBGroup_num(int DBGroup_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_DBUSERDBGROUPRELATION);
	q.addWhereClause("DBUserDBGroupRelation.DBGroup_num = " + DBGroup_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toDBUserDBGroupRelationList(rs);
	closeResultSet(rs);
	return v;
}

/**
Executes a query on the DBUserDBMeasLocRelation table, returning all records
with matching MeasLocGroup_nums
@param MeasLocGroup_num the MeasLocGroup_num for which to match records
@return a Vector of RiversideDB_DBUserDBMeasLocRelation objects.
@throws Exception if an error occurs
*/
public Vector readDBUserMeasLocGroupRelationListForMeasLocGroup_num(
int MeasLocGroup_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_DBUSERMEASLOCGROUPRELATION);
	q.addWhereClause("DBUserMeasLocGroupRelation.MeasLocGroup_num = " 
		+ MeasLocGroup_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toDBUserMeasLocGroupRelationList(rs);
	closeResultSet(rs);
	return v;
}

/**
Executes a query on the DBUserDBMeasLocRelation table, returning all records
with matching DBUser_nums
@param DBUser_num the DBUser_num for which to match records
@return a Vector of RiversideDB_DBUserDBMeasLocRelation objects.
@throws Exception if an error occurs
*/
public Vector readDBUserMeasLocGroupRelationListForDBUser_num(
int DBUser_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_DBUSERMEASLOCGROUPRELATION);
	q.addWhereClause("DBUserMeasLocGroupRelation.DBUser_num = " 
		+ DBUser_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toDBUserMeasLocGroupRelationList(rs);
	closeResultSet(rs);
	return v;
}


/**
Executes query on ExportConf table, returning all records where 
ExportProduct_num is equal to the value passed to the method
@param ExportProduct_num the value to use for the where clause
@return Vector of RiversideDB_ExportConf objects, one per record
@throws Exception if an error occurs
*/
public Vector readExportConfListForExportProduct_num(int ExportProduct_num)
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_EXPORTCONF );
	q.addWhereClause("ExportConf.ExportProduct_num = " + ExportProduct_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toExportConfList (rs);
	closeResultSet(rs);
	return v;
}

/**
Executes query on ExportConf table, returning all records where 
ExportProduct_num is equal to the value passed to the method and ordered
by Identifier
@param ExportProduct_num the value to use for the where clause
@return Vector of RiversideDB_ExportConf objects, one per record
@throws Exception if an error occurs
*/
public Vector readExportConfListForExportProduct_numByLocation
(int ExportProduct_num)
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_EXPORTCONF );
	q.addWhereClause("ExportConf.ExportProduct_num = " + ExportProduct_num);
	q.addTable("MeasType");
	q.addTable("MeasLoc");
	q.addWhereClause("ExportConf.ExportProduct_num = " + ExportProduct_num);
	q.addWhereClause("MeasType.MeasLoc_num = MeasLoc.MeasLoc_num");
	q.addWhereClause("MeasType.MeasType_num = ExportConf.MeasType_num");
	q.addOrderByClause("MeasLoc.Identifier");	
	ResultSet rs = dmiSelect(q);
	Vector v = toExportConfList (rs);
	closeResultSet(rs);
	return v;
}

/**
Executes query on ExportConf table, returning all records.
@return Vector of RiversideDB_ExportConf objects
@throws Exception if an error occurs.
*/
public Vector readExportConfList() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_EXPORTCONF );
	ResultSet rs = dmiSelect(q);
	Vector v = toExportConfList (rs);
	closeResultSet(rs);
	return v;
}	

/**
Reads the export conf table to find the export conf that matches the given
meas type num.
@param MeasType_num the meas type num to find the export conf for
@return the matching export conf object or null if none could be found.
@throws Exception if an error occurs
*/
public RiversideDB_ExportConf readExportConfForMeasType_num(long MeasType_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL (q, _S_EXPORTCONF);
	q.addWhereClause("ExportConf.MeasType_num = " + MeasType_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toExportConfList(rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	return (RiversideDB_ExportConf)v.elementAt(0);
}

/**
Read ExportProduct table, returning ExportProduct record that
that match the given ExportProduct_num.
@param ExportProduct_num the ExportProduct_num for which to return records
@return RiversideDB_ExportProduct object.
@throws Exception if an error occurs
*/
public RiversideDB_ExportProduct readExportProductForExportProduct_num(
int ExportProduct_num) throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_EXPORTPRODUCT );
	q.addWhereClause("ExportProduct_num = " + ExportProduct_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toExportProductList (rs);
	closeResultSet(rs);
	if ( (v == null) || (v.size() == 0) ) {
		return null;	
	}
	return (RiversideDB_ExportProduct)v.elementAt(0);
}

/**
Read ExportProduct table, returning all records ordered by Product_group and
Product_name.
@return Vector of RiversideDB_ExportProduct objects, one per record
@throws Exception if an error occurs
*/
public Vector readExportProductList()
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_EXPORTPRODUCT );
// REVISIT (JTS - 2003-06-02)
// This will be phased out later		
//		if (getDatabaseVersion() < _VERSION_020800_20030422) {		
	q.addOrderByClause("ExportProduct.Product_group");
//	}	
	q.addOrderByClause("ExportProduct.Product_name");
	ResultSet rs = dmiSelect(q);
	Vector v = toExportProductList (rs);
	closeResultSet(rs);
	return v;
}

/**
Read ExportProduct table, returning all records ordered by Product_group and
Product_name that match the given ProductGroup_num.
@param ProductGroup_num the ProductGroup_num for which to return records
@return Vector of RiversideDB_ExportProduct objects, one per record
@throws Exception if an error occurs
*/
public Vector readExportProductListForProductGroup_num(int ProductGroup_num)
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_EXPORTPRODUCT );
// REVISIT (JTS - 2003-06-02)
// This will be phased out later		
//		if (getDatabaseVersion() < _VERSION_020800_20030422) {		
	q.addOrderByClause("ExportProduct.Product_group");
//	}	
	q.addOrderByClause("ExportProduct.Product_name");
	q.addWhereClause("ProductGroup_num = " + ProductGroup_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toExportProductList (rs);
	closeResultSet(rs);
	return v;
}

/**
Read all ExportType records, ordered by Name.
@return a Vector of all ExportType values
@throws Exception if an error occurs
*/
public Vector readExportTypeList()
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_EXPORTTYPE );
	q.addOrderByClause("ExportType.Name");
	ResultSet rs = dmiSelect(q);
	Vector v = toExportTypeList (rs);
	closeResultSet(rs);
	return v;
}

/**
Read the Geoloc data for all records via a Stored Procedure.
REVISIT (JTS - 2003-06-03)
Is anything ever going to be done with these?
@return a vector of objects of type RiversideDB_Geoloc.
@throws Exception if an error occurs
*/
public Vector readGeolocList_SP () 
throws Exception {	
	CallableStatement cs = 
		getConnection().prepareCall("{call Geoloc_Select}");
	ResultSet rs = cs.executeQuery();
	Vector v = toGeolocList(rs);
	cs.close();
	closeResultSet(rs);
	return v;
}

/**
Read the Geoloc data for a given Geoloc record with the given Geoloc_num,
via a stored procedure.
@param Geoloc_num Geoloc_num to query for.
@return a RiversideDB_Geoloc object, or null if no matching record could be 
found
@throws Exception if an error occurs
*/
public RiversideDB_Geoloc readGeolocForGeoloc_num_SP ( long Geoloc_num )
throws Exception {	
	CallableStatement cs = getConnection().prepareCall(
		"{call GeolocDataSet_Select (" + Geoloc_num + ")}"
	);	
	ResultSet rs = cs.executeQuery();

	Vector v = toGeolocList (rs);
	cs.close();
	closeResultSet(rs);
	if ( (v == null) || (v.size() == 0) ) {
		return null;	
	}
	return (RiversideDB_Geoloc)v.elementAt(0);
}

/**
Read the Geoloc data for a record with the given Geoloc_num.
@param Geoloc_num Geoloc_num to query for.
@return a RiversideDB_Geoloc object, or null if no matching records could be 
found
@throws Exception if an error occurs
*/
public RiversideDB_Geoloc readGeolocForGeoloc_num ( long Geoloc_num )
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_GEOLOC );
	q.addWhereClause("Geoloc.Geoloc_num = " + Geoloc_num);

	ResultSet rs = dmiSelect(q);
	Vector v = toGeolocList (rs);
	closeResultSet(rs);
	if ( (v == null) || (v.size() == 0) ) {
		return null;	
	}
	return (RiversideDB_Geoloc)v.elementAt(0);
}

// TODO - evaluate issues that occur when global data are edited in the
// RiversideDB Administrator or other tools.
/**
Read global data that should be kept in memory to increase performance.
This is called from the DMI.open() base class method.
@throws Exception thrown in readTablesList()
*/
public void readGlobalData () 
throws Exception {
	// Read the Tables table.
	_RiversideDB_Tables_Vector = readTablesList ();
    // Read the table layout table
    _RiversideDB_TableLayout_Vector = readTableLayoutList ();
}

/**
Executes query on ImportConf table, returning all objects 
@return Vector of RiversideDB_ImportConf objects
@throws Exception if an error occurs
*/
public Vector readImportConfList() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_IMPORTCONF);
	ResultSet rs = dmiSelect(q);
	Vector v = toImportConfList (rs);
	closeResultSet(rs);
	return v;
}

/**
Executes query on ImportConf table, returning all objects where
ImportProduct_num is equal to the given ImportProduct_num
@param ImportProduct_num the ImportProduct_num for which to return all
matching records
@return Vector of RiversideDB_ImportConf objects
@throws Exception if an error occurs
*/
public Vector readImportConfListForImportProduct_num(int ImportProduct_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_IMPORTCONF);
	q.addWhereClause("ImportConf.ImportProduct_num = " + ImportProduct_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toImportConfList (rs);
	closeResultSet(rs);
	return v;
}

/**
Executes query on ImportConf table, returning all objects where
ImportProduct_num is equal to the given ImportProduct_num, and ordered by
Identifier.
@param ImportProduct_num the value for which to return matching records
@return Vector of RiversideDB_ImportConf objects
@throws Exception if an error occurs
*/
public Vector readImportConfListForImportProduct_numByLocation(
int ImportProduct_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_IMPORTCONF);
	q.addTable("MeasType");
	q.addTable("MeasLoc");
	q.addWhereClause("ImportConf.ImportProduct_num = " + ImportProduct_num);
	q.addWhereClause("MeasType.MeasLoc_num = MeasLoc.MeasLoc_num");
	q.addWhereClause("MeasType.MeasType_num = ImportConf.MeasType_num");
	q.addOrderByClause("MeasLoc.Identifier");
	ResultSet rs = dmiSelect(q);
	Vector v = toImportConfList (rs);
	closeResultSet(rs);
	return v;
}

/**
Reads the import conf table to find the import conf that matches the given
meas type num.
@param MeasType_num the meas type num to find the import conf for
@return the matching import conf object or null if none could be found.
@throws Exception if an error occurs
*/
public RiversideDB_ImportConf readImportConfForMeasType_num(long MeasType_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL (q, _S_IMPORTCONF);
	q.addWhereClause("ImportConf.MeasType_num = " + MeasType_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toImportConfList(rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	return (RiversideDB_ImportConf)v.elementAt(0);
}

/**
Read all records from the ImportProduct table, ordered by Product_group and
Product_name.
@return vector of RiversideDB_ImportProduct objects, one per record
@throws Exception if an error occurs
*/
public Vector readImportProductList()
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_IMPORTPRODUCT );
// REVISIT (JTS - 2003-06-02)
// This will be phased out later		
//	if (getDatabaseVersion() < _VERSION_020800_20030422) {		
		q.addOrderByClause("ImportProduct.Product_group");
//	}
	q.addOrderByClause("ImportProduct.Product_name");
	ResultSet rs = dmiSelect(q);	
	Vector v = toImportProductList (rs);
	closeResultSet(rs);
	return v;
}

/**
Read ImportProduct table, returning ImportProduct record that
that match the given ImportProduct_num.
@param ImportProduct_num the ImportProduct_num for which to return records
@return RiversideDB_ImportProduct object.
@throws Exception if an error occurs
*/
public RiversideDB_ImportProduct readImportProductForImportProduct_num(
int ImportProduct_num) throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_IMPORTPRODUCT );
	q.addWhereClause("ImportProduct_num = " + ImportProduct_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toImportProductList (rs);
	closeResultSet(rs);
	if ( (v == null) || (v.size() == 0) ) {
		return null;	
	}
	return (RiversideDB_ImportProduct)v.elementAt(0);
}

/**
Read ImportProduct table, returning all records ordered by Product_group and
Product_name that match the given ProductGroup_num.
@param ProductGroup_num the ProductGroup_num for which to return records
@return Vector of RiversideDB_ImportProduct objects, one per record
@throws Exception if an error occurs
*/
public Vector readImportProductListForProductGroup_num(int ProductGroup_num)
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_IMPORTPRODUCT );
// REVISIT (JTS - 2003-06-02)
// This will be phased out later		
//	if (getDatabaseVersion() < _VERSION_020800_20030422) {		
		q.addOrderByClause("ImportProduct.Product_group");
//	}	
	q.addOrderByClause("ImportProduct.Product_name");
	q.addWhereClause("ProductGroup_num = " + ProductGroup_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toImportProductList (rs);
	closeResultSet(rs);
	return v;
}


/**
Read all ImportType records, ordered by Name.
@return a Vector of all ImportType values
@throws Exception if an error occurs
*/
public Vector readImportTypeList() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_IMPORTTYPE );
	q.addOrderByClause("ImportType.Name");
	ResultSet rs = dmiSelect(q);
	Vector v = toImportTypeList (rs);
	closeResultSet(rs);
	return v;
}

/**
Returns the RiversideDB_ImportType record that matches the given Name.
@param Name the Name to match.
@return the matching RiversideDB_ImportType record, or null if none match.
@throws Exception if an error occurs.
*/
public RiversideDB_ImportType readImportTypeForName(String Name)
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_IMPORTTYPE );
	q.addWhereClause("ImportType.Name = '" + Name + "'");
	q.addOrderByClause("ImportType.Name");
	ResultSet rs = dmiSelect(q);
	Vector v = toImportTypeList (rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	return (RiversideDB_ImportType)v.elementAt(0);
}

/**
Read all MeasCreateMethod records, ordered by Method.
@return a Vector of all MeasCreateMethods values
@throws Exception if an error occurs
*/
public Vector readMeasCreateMethodList() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASCREATEMETHOD );
	q.addOrderByClause("MeasCreateMethod.Method");
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasCreateMethodList (rs);
	closeResultSet(rs);
	return v;
}

/**
Returns the RiversideDB_MeasCreateMethod record that matches the given Method.
@param Method the Method to match.
@return the matching RiversideDB_MeasCreateMethod record, or null if none match.
@throws Exception if an error occurs.
*/
public RiversideDB_MeasCreateMethod readMeasCreateMethodForMethod(String Method)
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASCREATEMETHOD );
	q.addWhereClause("MeasCreateMethod.Method = '" + Method + "'");
	q.addOrderByClause("MeasCreateMethod.Method");
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasCreateMethodList (rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	return (RiversideDB_MeasCreateMethod)v.elementAt(0);
}

/**
Read the MeasLoc data for the record that matches the given identifier.
@param Identifier Identifier to match against.
@return the matching RiversideDB_MeasLoc object, or null if none could be found
@throws Exception if an error occurs
*/
public RiversideDB_MeasLoc readMeasLocForIdentifier ( String Identifier ) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL (q, _S_MEASLOC);
	q.addWhereClause("MeasLoc.Identifier = '" + escape(Identifier) + "'");
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasLocList (rs);
	closeResultSet(rs);
	if ( (v == null) || (v.size() == 0) ) {
		return null;	
	}
	return (RiversideDB_MeasLoc)v.elementAt(0);
}

/**
Read the MeasLoc data for the record that matches the given identifier
and Meas_loc_type
@param Identifier Identifier to match against.
@param type Meas_loc_type ("A" or "P") to distinguish Identifier.
@return the matching RiversideDB_MeasLoc object, or null if none could be found
@throws Exception if an error occurs
*/
public RiversideDB_MeasLoc readMeasLocForIdentifier ( String Identifier,
String type ) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL (q, _S_MEASLOC);
	q.addWhereClause("MeasLoc.Identifier = '" + escape(Identifier) + "'");
	q.addWhereClause("MeasLoc.Meas_loc_type = '" + type + "'");
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasLocList (rs);
	closeResultSet(rs);
	if ( (v == null) || (v.size() == 0) ) {
		return null;	
	}
	return (RiversideDB_MeasLoc)v.elementAt(0);
}

/**
Read the MeasLoc data for record matching a given MeasLoc_num.
@param MeasLoc_num MeasLoc_num to match against
@return the matching RiversideDB_MeasLoc object, or null if none could be found
@throws Exception if an error occurs
*/
public RiversideDB_MeasLoc readMeasLocForMeasLoc_num ( long MeasLoc_num )
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL(q, _S_MEASLOC);
	q.addWhereClause("MeasLoc.MeasLoc_num = " + MeasLoc_num );
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasLocList (rs);
	closeResultSet(rs);
	if ( (v == null) || (v.size() == 0) ) {
		return null;	
	}
	return (RiversideDB_MeasLoc)v.elementAt(0);
}

/**
Read all MeasLoc data ordered by identifier.
@return a vector of objects of type RiversideDB_MeasLoc.
@throws Exception if an error occurs
*/
public Vector readMeasLocList () throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASLOC );
	q.addOrderByClause("MeasLoc.Identifier");
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasLocList (rs);
	closeResultSet(rs);
	return v;
}

/**
Read all MeasLoc data that has the given MeasLocGroup_num, ordered by 
Identifier.
@param MeasLocGroup_num the MeasLocGroup_num for which to read MeasLoc data
@return a vector of objects of type RiversideDB_MeasLoc.
@throws Exception if an error occurs
*/
public Vector readMeasLocListForMeasLocGroup_num (int MeasLocGroup_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASLOC );
	q.addOrderByClause("MeasLoc.Identifier");
	q.addWhereClause("MeasLoc.MeasLocGroup_num = " + MeasLocGroup_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasLocList (rs);
	closeResultSet(rs);
	return v;
}

/**
Read all MeasLocGroup data ordered by MeasLocGroup_num.
@return a vector of objects of type RiversideDB_MeasLocGroup.
@throws Exception if an error occurs
*/
public Vector readMeasLocGroupList () throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASLOCGROUP );
	q.addOrderByClause("MeasLocGroup.MeasLocGroup_num");
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasLocGroupList (rs);
	closeResultSet(rs);
	return v;
}

/**
Read all MeasLocGroup data with the given DBUser_num
@return a vector of objects of type RiversideDB_MeasLocGroup.
@throws Exception if an error occurs
*/
public Vector readMeasLocGroupListForDBUser_num(int DBUser_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_MEASLOCGROUP);
	q.addWhereClause("MeasLocGroup.DBUser_num = " + DBUser_num);
	q.addOrderByClause("MeasLocGroup.DBUser_num");
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasLocGroupList(rs);
	closeResultSet(rs);
	return v;
}

/**
Read all MeasLocGroup data with the given MeasLocGroup_num
@return a vector of objects of type RiversideDB_MeasLocGroup.
@throws Exception if an error occurs
@deprecated -- this should not return a list!  Only 1.
*/
public Vector readMeasLocGroupListForMeasLocGroup_num(int MeasLocGroup_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASLOCGROUP );
	q.addWhereClause("MeasLocGroup.MeasLocGroup_num = " + MeasLocGroup_num);
	q.addOrderByClause("MeasLocGroup.MeasLocGroup_num");
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasLocGroupList (rs);
	closeResultSet(rs);
	return v;
}

/**
Read all MeasLocGroup data with the given MeasLocGroup_num
@return a vector of objects of type RiversideDB_MeasLocGroup.
@throws Exception if an error occurs
*/
public RiversideDB_MeasLocGroup readMeasLocGroupForMeasLocGroup_num(
int MeasLocGroup_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL ( q, _S_MEASLOCGROUP );
	q.addWhereClause("MeasLocGroup.MeasLocGroup_num = " + MeasLocGroup_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasLocGroupList(rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	else {
		return (RiversideDB_MeasLocGroup)v.elementAt(0);
	}
}

/**
Read all MeasLocType records, ordered by Type.
@return a Vector of all the MeasLocType values
@throws Exception if an error occurs
*/
public Vector readMeasLocTypeList() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASLOCTYPE );
	q.addOrderByClause("MeasLocType.Type");
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasLocTypeList (rs);
	closeResultSet(rs);
	return v;
}

/**
Read all MeasQualityFlag records, ordered by QualityFlag.
@return a Vector of all the MeasQualityFlag values
@throws Exception if an error occurs
*/
public Vector readMeasQualityFlagList() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASQUALITYFLAG );
	q.addOrderByClause("MeasQualityFlag.Quality_flag");
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasQualityFlagList (rs);
	closeResultSet(rs);
	return v;
}

/**
Returns the RiversideDB_MeasQualityFlag record that matches the given 
Quality_flag.
@param Quality_flag the Quality_flag to match.
@return the matching RiversideDB_MeasQualityFlag record, or null if none match.
@throws Exception if an error occurs.
*/
public RiversideDB_MeasQualityFlag readMeasQualityFlagForQuality_flag(
String Quality_flag)
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASQUALITYFLAG );
	q.addWhereClause("MeasQualityFlag.Quality_flag = '" 
		+ Quality_flag + "'");
	q.addOrderByClause("MeasQualityFlag.Quality_flag");
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasQualityFlagList (rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	return (RiversideDB_MeasQualityFlag)v.elementAt(0);
}

/**
Read all MeasReducGridWeight records that match the given OutputMeasType_num.
@param OutputMeasType_num the value to match against
@return a Vector of all the matching MeasReducGridWeight records
@throws Exception if an error occurs
*/
public Vector readMeasReducGridWeightListForOutputMeasType_num 
(long OutputMeasType_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASREDUCGRIDWEIGHT );
	q.addWhereClause ( "MeasReducGridWeight.OutputMeasType_num=" + 
		OutputMeasType_num );
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasReducGridWeightList (rs);
	closeResultSet(rs);
	return v;
}

/**
Read all MeasReducRelation records that match the given OutputMeasType_num.
@param OutputMeasType_num the value to match against
@return a Vector of all the matching MeasReducRelation records
@throws Exception if an error occurs
*/
public Vector readMeasReducRelationListForOutputMeasType_num 
(long OutputMeasType_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASREDUCRELATION );
	q.addWhereClause ( "MeasReducRelation.OutputMeasType_num=" + 
		OutputMeasType_num );
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasReducRelationList (rs);
	closeResultSet(rs);
	return v;
}

/**
Read all MeasReduction records.
@return a Vector of all the MeasReduction values
@throws Exception if an error occurs
*/
public Vector readMeasReductionList() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASREDUCTION );
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasReductionList (rs);
	closeResultSet(rs);
	return v;
}

/*
Read all MeasReduction record that matches the given OutputMeasType_num.
@param OutputMeasType_num the value to match against
@return matching RiversideDB_MeasReduction object
@throws Exception if an error occurs
*/
public RiversideDB_MeasReduction readMeasReductionForOutputMeasType_num 
(long OutputMeasType_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASREDUCTION );
	q.addWhereClause ( "MeasReduction.OutputMeasType_num=" + 
		OutputMeasType_num );
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasReductionList (rs);
	closeResultSet(rs);
	if ( (v == null) || (v.size() == 0) ) {
		return null;
	}
	return (RiversideDB_MeasReduction)v.elementAt(0);
}

/**
Read all MeasReductionType records, ordered by Type.
@return a Vector of all the MeasReductionType values
@throws Exception if an error occurs
*/
public Vector readMeasReductionTypeList() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASREDUCTIONTYPE );
	q.addOrderByClause("MeasReductionType.Type");
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasReductionTypeList (rs);
	closeResultSet(rs);
	return v;
}

/**
Returns the RiversideDB_MeasReductionType record that matches the given Type.
@param Type the Type to match.
@return the matching RiversideDB_MeasReductionType record, or null if none 
match.
@throws Exception if an error occurs.
*/
public RiversideDB_MeasReductionType readMeasReductionTypeForType(String Type)
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASREDUCTIONTYPE );
	q.addWhereClause("MeasReductionType.Type = '" + Type + "'");
	q.addOrderByClause("MeasReductionType.Type");
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasReductionTypeList (rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	return (RiversideDB_MeasReductionType)v.elementAt(0);
}

/**
Read all MeasScenario records, ordered by Method.
@return a Vector of all MeasScenario values
@throws Exception if an error occurs
*/
public Vector readMeasScenarioList() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASSCENARIO );
	q.addOrderByClause("MeasScenario.Method");
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasScenarioList (rs);
	closeResultSet(rs);
	return v;
}

/**
Read all MeasScenarioRelation records.
@return a Vector of all MeasScenario values
@throws Exception if an error occurs
*/
public Vector readMeasScenarioRelationList() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASSCENARIORELATION );
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasScenarioRelationList (rs);
	closeResultSet(rs);
	return v;
}

/**
Read all MeasTimeScale records, ordered by Scale.
@return a Vector of all MeasTimeScale values
@throws Exception if an error occurs
*/
public Vector readMeasTimeScaleList() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASTIMESCALE );
	q.addOrderByClause("MeasTimeScale.Scale");
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasTimeScaleList (rs);
	closeResultSet(rs);
	return v;
}

/**
Returns the RiversideDB_MeasTimeScale record that matches the given Scale.
@param Scale the Scale to match.
@return the matching RiversideDB_MeasTimeScale record, or null if none match.
@throws Exception if an error occurs.
*/
public RiversideDB_MeasTimeScale readMeasTimeScaleForScale(String Scale)
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASTIMESCALE );
	q.addWhereClause("MeasTimeScale.Scale = '" + Scale + "'");
	q.addOrderByClause("MeasTimeScale.Scale");
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasTimeScaleList (rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	return (RiversideDB_MeasTimeScale)v.elementAt(0);
}

/**
Read all MeasTransProtocol records, ordered by Protocol.
@return a Vector of all MeasTransProtocl values
@throws Exception if an error occurs
*/
public Vector readMeasTransProtocolList() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASTRANSPROTOCOL );
	q.addOrderByClause("MeasTransProtocol.Protocol");
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasTransProtocolList (rs);
	closeResultSet(rs);
	return v;
}

/**
Returns the RiversideDB_MeasTransProtocol record that matches the given 
Protocol.
@param Protocol the Protocol to match.
@return the matching RiversideDB_MeasTransProtocol record, or null if none 
match.
@throws Exception if an error occurs.
*/
public RiversideDB_MeasTransProtocol readMeasTransProtocolForProtocol(
String Protocol)
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASTRANSPROTOCOL );
	q.addWhereClause("MeasTransProtocol.Protocol = '" + Protocol + "'");
	q.addOrderByClause("MeasTransProtocol.Protocol");
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasTransProtocolList (rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	return (RiversideDB_MeasTransProtocol)v.elementAt(0);
}

/**
Read all the MeasType records that match the given MeasLoc_num.
@param MeasLoc_num the value to match against
@return a Vector of RiversideDB_MeasType objects.
@throws Exception if an error occurs
*/
public Vector readMeasTypeListForMeasLoc_num(long MeasLoc_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASTYPE );
	q.addWhereClause("MeasType.MeasLoc_num = " + MeasLoc_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasTypeList (rs);
	closeResultSet(rs);
	// Only interested in the first one...
	if ( (v == null) || (v.size() == 0) ) {
		return null;
	}
	return v;
}

/**
Read the MeasType record that matches the given MeasType_Num.
@param MeasType_num the value to match against
@return a RiversideDB_MeasType object, or null if none matched.
@throws Exception if an error occurs
*/
public RiversideDB_MeasType readMeasTypeForMeasType_num(long MeasType_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASTYPE );
	q.addWhereClause("MeasType.MeasType_num = " + MeasType_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasTypeList (rs);
	closeResultSet(rs);
	if ( (v == null) || (v.size() == 0) ) {
		return null;
	}
	return (RiversideDB_MeasType)v.elementAt(0);
}

/**
Read the MeasType table for the record that matches the given tsident string.
@param tsident_string The time series identifier to match against.
@return an object of type RiversideDB_MeasType, or null if none matched.
@throws Exception if an error occurs
*/
public RiversideDB_MeasType readMeasTypeForTSIdent ( String tsident_string ) 
throws Exception {
	Vector v = readMeasTypeListForTSIdent ( tsident_string );
	// Only interested in the first one...
	if ( (v == null) || (v.size() == 0) ) {
		return null;
	}
	return (RiversideDB_MeasType)v.elementAt(0);
}

/**
Reads all the MeasType records that match the given Data_type.
@param Data_type the value to match against.
@return a vector of matching RiversideDB_MeasType objects.
@throws Exception if an error occurs
*/
public Vector readMeasTypeListForData_type(String Data_type) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASTYPE );
	q.addWhereClause("MeasType.Data_type = '" + escape(Data_type) + "'");
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasTypeList (rs);
	closeResultSet(rs);
	return v;
}

/**
Reads all records from MeasType.
@return a vector of objects of type RiversideDB_MeasType 
@throws Exception if an error occurs
*/
public Vector readMeasTypeList() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASTYPE);
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasTypeList (rs);
	closeResultSet(rs);
	return v;
}

/**
Reads all records from MeasType sorted by MeasLoc Identifier (location).
@return a vector of objects of type RiversideDB_MeasType 
@throws Exception if an error occurs
*/
public Vector readMeasTypeListByLocation( ) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASTYPE);
	//q.addTable( "MeasLoc" );
	q.addWhereClause( "MeasType.MeasLoc_num = MeasLoc.MeasLoc_num" );
	q.addOrderByClause( "MeasLoc.Identifier" );
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasTypeList (rs);
	closeResultSet(rs);
	return v;
}

/**
Read MeasType records for distinct data types, ordered by Data_type.
@return a vector of objects of type RiversideDB_MeasType, with only the
Data_type field filled in.
@throws Exception if an error occurs
*/
public Vector readMeasTypeListForDistinctData_type () 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	// Select from a join of MeasType and MeasLoc
	q.addField ( "MeasType.Data_type" );
	q.addTable ( "MeasType" );
	q.selectDistinct(true);
	q.addOrderByClause("MeasType.Data_type");
	ResultSet rs = dmiSelect ( q );
	// Transfer here...
	Vector v = new Vector();
	int index = 1;
	String s;
	RiversideDB_MeasType data = null;
	while ( rs.next() ) {
		data = new RiversideDB_MeasType();
		s = rs.getString ( index );
		if ( !rs.wasNull() ) {
			data.setData_type ( s.trim() );
		}
		v.addElement ( data );
	}
	closeResultSet(rs);
	return v;
}

/**
Reads all records from MeasType that match the given MeasLoc_num.
@param MeasLoc_num the value to match against
@return a Vector of matching RiversideDB_MeasType objects.
@throws Exception if an error occurs
*/
public Vector readMeasTypeListforMeasLoc_num(long MeasLoc_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL ( q, _S_MEASTYPE );
	q.addWhereClause("MeasType.MeasLoc_num = " + MeasLoc_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasTypeList (rs);
	closeResultSet(rs);
	return v;
}

/**
Reads all the records from MeasType that match the given tsident string.
@param tsIdent a ts identifier string that will be split up and its values
set in various where clauses
@return a vector of RiversideDB_MeasType that match the tsident string.
@throws Exception if an error occurs
*/
public Vector readMeasTypeListForTSIdent ( String tsIdent ) 
throws Exception {
	return readMeasTypeListForTSIdent(tsIdent,  null);
}

/**
Reads all the records from MeasType that match the given tsident string, 
ordered by MeasLoc.Identifier.
@param tsIdent a ts identifier string that will be split up and its values
set in various where clauses
@return a Vector of RiversideDB_MeasType objects that match the tsident String.
@throws Exception if an error occurs
*/
public Vector readMeasTypeListForTSIdentByLocation(String tsIdent)
throws Exception {
	return readMeasTypeListForTSIdent(tsIdent, "MeasLoc.Identifier");
}

/**
executes a query on table MeasType, limiting values to a series of things,
should any of them be set in the string passed in to the method.  The 
where clause may set Data_type, Time_step_base, Identifier, Scenario. and/or
Source_abbrev.
@param tsIdent a ts identifier string that will be split up and its values
set in various where clauses
will have its own separate time series.
@param sortField the field to sort on
@return a vector of RiversideDB_MeasType objects filled with rows from the
resultSet
@throws Exception if an error occurs
*/
public Vector readMeasTypeListForTSIdent(String tsIdent, String sortField) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASTYPE );

	TSIdent id = new TSIdent(tsIdent.trim());
	if (id.getMainType().length() > 0) {
		q.addWhereClause("MeasType.Data_type = '"
			+ escape(id.getMainType().toUpperCase()) + "'");
	}

	if (id.getSubType().length() > 0) {
		q.addWhereClause("MeasType.Sub_type = '"
			+ escape(id.getSubType().toUpperCase()) + "'");
	}
	if ( !id.getInterval().equals("") ) {
		// REVISIT
		// This does not work because the case or spelling may not
		// match in the lookup...
		// Need to get directly from the interval part.
		//addWhereClause("MeasType.Time_step_base = " + 
		//	TSInterval.getName(id.getIntervalBase()).toUpperCase());
		TimeInterval interval = TimeInterval.parseInterval (
			id.getInterval() );
		q.addWhereClause("MeasType.Time_step_base = '"
			+ escape(interval.getBaseString()) + "'" );

		// The convention when defining MeasType records is to always
		// include the multiplier.  However, it is not required and will
		// not be present for IRREGULAR time step (for which there is
		// no multiplier).  Because it is expected that the multiplier
		// string in an identifer matches what is in the database, use
		// the string that is passed in to determine the interval,
		// rather that getting an interval string from the integer base.
	
		if ( !interval.getMultiplierString().equals("") ) {
			q.addWhereClause("MeasType.Time_step_mult = " 
				+ interval.getMultiplierString().toUpperCase());
		}
	}
	if (id.getLocation().length() > 0) {
		q.addWhereClause("MeasLoc.Identifier = '"
			+ escape(id.getLocation().toUpperCase()) + "'");
	}

	if (id.getScenario().length() > 0) {
		q.addWhereClause("MeasType.Scenario = '"
			+ escape(id.getScenario().toUpperCase()) + "'");
	}
	String source = id.getSource().toUpperCase();
	// REVISIT [LT] 2005-01-10 - Is this also version dependent ?????
	// REVISIT [LT] 2005-02-02 - In discussion with MT it was decided that
	//                           this check should not be done.
	//			     Keep around, since I do not know if other
	//			     application using this library is still 
	//			     passing tsIdent with HYDROBASE. 
	if (source == "HYDROBASE") {
		source = id.getSubSource().toUpperCase();
	}
	if (source.length() > 0) {
		q.addWhereClause("MeasType.Source_abbrev = '" 
			+ escape(source) + "'");
	}
	if (sortField != null) {
		q.addOrderByClause(sortField);
	}

	ResultSet rs = dmiSelect(q);
	Vector v = toMeasTypeList (rs);
	closeResultSet(rs);
	return v;
}

/**
Read all MeasTypeStats records that match the given MeasType_num
@param MeasType_num value to match against.
@return Vector of objects of type RiversideDB_MeasTypeStats.
@throws Exception if an error occurs
*/
public Vector readMeasTypeStatsListForMeasType_num(long MeasType_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASTYPESTATS );
	q.addWhereClause("MeasTypeStats.MeasType_num = " + MeasType_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasTypeStatsList (rs);
	closeResultSet(rs);
	return v;
}

/**
Read all MeasTypeStatus records that match the given MeasType_num.
@param MeasType_num value to match against.
@return Vector of objects of type RiversideDB_MeasTypeStatus.
@throws Exception if an error occurs
*/
public Vector readMeasTypeStatusListForMeasType_num(long MeasType_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MEASTYPESTATUS );
	q.addWhereClause("MeasTypeStatus.MeasType_num = " + MeasType_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toMeasTypeStatusList (rs);
	closeResultSet(rs);
	return v;
}

/**
Read all MessageLog records, ordered by Date_Time, in Decreasing order.
@return a vector of objects of type RiversideDB_MessageLog.
@throws Exception if an error occurs
*/
public Vector readMessageLogList ()
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MESSAGELOG );
	q.addOrderByClause ( "MessageLog.Date_Time DESC" ); 
	ResultSet rs = dmiSelect(q);
	Vector v = toMessageLogList (rs);
	closeResultSet(rs);
	return v;
}

/**
Returns the RiversideDB_MessageLog record that matches the given Message_num.
@param Message_num the Message_num to match.
@return the matching RiversideDB_MessageLog record, or null if none match.
@throws Exception if an error occurs.
*/
public RiversideDB_MessageLog readMessageLogForMessage_num(int Message_num)
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_MESSAGELOG );
	q.addWhereClause("MessageLog.Message_num = " + Message_num );
	q.addOrderByClause("MessageLog.Message_num");
	ResultSet rs = dmiSelect(q);
	Vector v = toMessageLogList (rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	return (RiversideDB_MessageLog)v.elementAt(0);
}

/**
Reads all Operation records for the specified MeasLocGroup_num.
@param MeasLocGroup_num the MeasLocGroup_num for which to read Operation 
records.
@return a Vector of RiversideDB_Operation objects.
@throws Exception if an error occurs.
*/
public Vector readOperationListForMeasLocGroup_num(int MeasLocGroup_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_OPERATION);
	q.addWhereClause("Operation.MeasLocGroup_num = " + MeasLocGroup_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toOperationList(rs);
	closeResultSet(rs);
	return v;
}

/**
Reads all OperationStateRelation records for the specified Operation_num.
@param Operation_num the Operation_num for which to read 
OperationStateRelation records.
@return a Vector of RiversideDB_OperationStateRelation objects.
@throws Exception if an error occurs.
*/
public Vector readOperationStateRelationListForOperation_num(int Operation_num)
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_OPERATIONSTATERELATION);
	q.addWhereClause("Operation_num = " + Operation_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toOperationStateRelationList(rs);
	closeResultSet(rs);
	return v;
}

/**
Reads the operation with the given measlocgroup_num and operation_id.
@param measLocGroup_num the measLocGroup_num to search for.
@param operation_id to operation_id to match against.
@return the matching operation, or null if none mathc.
@throws Exception if an error occurs.
*/
public RiversideDB_Operation readOperationForMeasLocGroup_numOperation_id(
int measLocGroup_num, String operation_id) 
throws Exception {
	if (!isDatabaseVersionAtLeast(_VERSION_030000_20041001)) {
		return null;
	}

	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_OPERATION);
	q.addWhereClause("Operation.MeasLocGroup_num = " + measLocGroup_num);
	q.addWhereClause("Operation.Operation_id = '" + operation_id + "'");
	ResultSet rs = dmiSelect(q);
	Vector v = toOperationList(rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	return (RiversideDB_Operation)v.elementAt(0);
}

/**
Reads all ProductGroup records.
@return a Vector of RiversideDB_ProductGroup objects.
@throws Exception if an error occurs.
*/
public Vector readProductGroupList() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_PRODUCTGROUP);
	ResultSet rs = dmiSelect(q);
	Vector v = toProductGroupList(rs);
	closeResultSet(rs);
	return v;
}

/**
Reads ProductGroup for all the matching ProductTypes
@param ProductType the ProductType to match against
@return a Vector of matching RiversideDB_ProductGroup objects.
@throws Exception if an error occurs.
*/
public Vector readProductGroupListForProductType(String ProductType) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_PRODUCTGROUP);
	q.addWhereClause("ProductGroup.ProductType = '" 
		+ escape(ProductType) + "'");
	ResultSet rs = dmiSelect(q);
	Vector v = toProductGroupList(rs);
	closeResultSet(rs);
	return v;
}

/**
Reads all ProductType records.
@return a Vector of RiversideDB_ProductType objects.
@throws Exception if an error occurs.
*/
public Vector readProductTypeList() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_PRODUCTTYPE);
	ResultSet rs = dmiSelect(q);
	Vector v = toProductTypeList(rs);
	closeResultSet(rs);
	return v;
}

/**
Reads all RiversideDB properties, ordered by Variable and Seq.
@return a PropList containing the database properties.
@throws Exception if an error occurs
*/
public PropList readPropsList ()
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_PROPS );
	q.addOrderByClause ( "Props.Variable" );
	q.addOrderByClause ( "Props.Seq" );
	ResultSet rs = dmiSelect(q);
	PropList p = toPropsList (rs);
	closeResultSet(rs);
	return p;
}

/**
Reads all RiversideDB properties from the Props table, 
ordered by Variable and Seq.
@return a Vector containing the database properties.
@throws Exception if an error occurs
*/
public Vector readPropsListVector ()
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_PROPS );
	q.addOrderByClause ( "Props.Variable" );
	q.addOrderByClause ( "Props.Seq" );
	ResultSet rs = dmiSelect(q);
	Vector v = toPropsListVector (rs);
	closeResultSet(rs);
	return v;
}

/**
Read the props record that matches the given Variable.
@param variable the variable to match
@return a RiversideDB_Props value, or null if none matched
@throws Exception if an exception occurs.
*/
public RiversideDB_Props readPropsForVariable ( String variable )
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_PROPS );
	q.addWhereClause("Props.Variable = '" + variable + "'");
	q.addOrderByClause("Props.Variable");
	ResultSet rs = dmiSelect(q);
	Vector v = toPropsListVector (rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	return (RiversideDB_Props)v.elementAt(0);
}

/**
Read the RatingTable records for all that match the given RatingTable_num,
ordered by Value1.
@param RatingTable_num the value to match against
@return a Vector of matching RiversideDB_RatingTable objects.
@throws Exception if an error occurs
*/
public Vector readRatingTableListForRatingTable_num ( long RatingTable_num )
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_RATINGTABLE );
	q.addWhereClause ( "RatingTable.RatingTable_num=" + RatingTable_num ); 
	// REVISIT (JTS - 2003-06-03)
	// Might need an argument to sort by a specific field...
	q.addOrderByClause ( "RatingTable.Value1" ); 
	ResultSet rs = dmiSelect(q);
	Vector v = toRatingTableList (rs);
	closeResultSet(rs);
	return v;
}

/**
Reads all Scenario records, ordered by Identifier.
@return a vector of objects of type RiversideDB_Scenario.
@throws Exception if an error occurs
*/
public Vector readScenarioList ()
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_SCENARIO );
	q.addOrderByClause ( "Scenario.Identifier" ); 
	ResultSet rs = dmiSelect(q);
	Vector v = toScenarioList (rs);
	closeResultSet(rs);
	return v;
}

/**
Reads all Scenario records for the one with the matching scenario_num.
@param scenario_num the scenario_num to match against.
@return a vector of objects of type RiversideDB_Scenario.
@throws Exception if an error occurs
*/
public Vector readScenarioListForScenario_num (int scenario_num)
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_SCENARIO );
	q.addOrderByClause ( "Scenario.Identifier" ); 
	q.addWhereClause("Scenario.scenario_num = " + scenario_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toScenarioList (rs);
	closeResultSet(rs);
	return v;
}

/**
Returns the RiversideDB_Scenario record that matches the given Scenario_num.
@param Scenario_num the Scenario_num to match.
@return the matching RiversideDB_Scenario record, or null if none match.
@throws Exception if an error occurs.
*/
public RiversideDB_Scenario readScenarioForScenario_num(int Scenario_num)
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_SCENARIO );
	q.addWhereClause("Scenario.Scenario_num = " + Scenario_num );
	q.addOrderByClause("Scenario.Scenario_num");
	ResultSet rs = dmiSelect(q);
	Vector v = toScenarioList (rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	return (RiversideDB_Scenario)v.elementAt(0);
}

/**
Reads all the SHEFType records, ordered by SHEF_pe.
@return a vector of objects of type RiversideDB_SHEFType.
@throws Exception if an error occurs
*/
public Vector readSHEFTypeList ()
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_SHEFTYPE );
	q.addOrderByClause ( "SHEFType.SHEF_pe" ); 
	ResultSet rs = dmiSelect(q);
	Vector v = toSHEFTypeList (rs);
	closeResultSet(rs);
	return v;
}

/**
Returns the RiversideDB_SHEFType record that matches the given SHEF_pe.
@param SHEF_pe the SHEF_pe to match.
@return the matching RiversideDB_SHEFType record, or null if none match.
@throws Exception if an error occurs.
*/
public RiversideDB_SHEFType readSHEFTypeForSHEF_pe(String SHEF_pe)
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_SHEFTYPE );
	q.addWhereClause("SHEFType.SHEF_pe = '" + SHEF_pe + "'");
	q.addOrderByClause("SHEFType.SHEF_pe");
	ResultSet rs = dmiSelect(q);
	Vector v = toSHEFTypeList (rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	return (RiversideDB_SHEFType)v.elementAt(0);
}

/**
Read all the StageDischargeRating records, ordered by Start_Date in decreasing
order, where the MeasLoc_num matches the given MeasLoc_num.
@param MeasLoc_num the value to match against.
@return a vector of objects of type RiversideDB_StageDischargeRating that 
match the given MeasLoc_num.
@throws Exception if an error occurs
*/
public Vector readStageDischargeRatingListForMeasLoc_num ( long MeasLoc_num )
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_STAGEDISCHARGERATING );
	q.addOrderByClause ( "StageDischargeRating.Start_Date DESC" ); 
	q.addWhereClause ( "StageDischargeRating.MeasLoc_num=" + MeasLoc_num ); 
	ResultSet rs = dmiSelect(q);
	Vector v= toStageDischargeRatingList (rs);
	closeResultSet(rs);
	return v;
}

/**
Read all the StageDischargeRating records, to return one with latest
RatingTable_num.
@return a RiversideDB_StageDischargeRating that has largest RatingTable_num.
@throws Exception if an error occurs
*/
public RiversideDB_StageDischargeRating readStageDischargeRatingListForLatestRatingTable_num( )
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_STAGEDISCHARGERATING );
	q.addOrderByClause ( "StageDischargeRating.RatingTable_num DESC" ); 
	ResultSet rs = dmiSelect(q);
	Vector v= toStageDischargeRatingList (rs);
	closeResultSet(rs);
	return (RiversideDB_StageDischargeRating) v.elementAt(0);
}

/**
Reads all the StateGroup records, ordered by Date_Time in decreasing order and
Scenario.
@return a vector of objects of type RiversideDB_StateGroup.
@throws Exception if an error occurs
*/
public Vector readStateGroupList ()
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_STATEGROUP );
	q.addOrderByClause ( "StateGroup.Date_Time DESC" ); 
	q.addOrderByClause ( "StateGroup.Scenario" ); 
	ResultSet rs = dmiSelect(q);
	Vector v = toStateGroupList (rs);
	closeResultSet(rs);
	return v;
}

/**
Reads all the StateGroup records that have the given MeasLocGroup_num, ordered
by Date_Time in decreasing order, and then by Scenario.
@param MeasLocGroup_num the MeasLocGroup_num for which to delete records.
@return a Vector of objects of type RiversideDB_StateGroup.
@throws Exception if an error occurs.
*/
public Vector readStateGroupListForMeasLocGroup_num(long MeasLocGroup_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_STATEGROUP);
	q.addOrderByClause("StateGroup.Date_Time DESC");
	q.addOrderByClause("StateGroup.Scenario");
	ResultSet rs = dmiSelect(q);
	Vector v = toStateGroupList(rs);
	closeResultSet(rs);
	return v;
}

/**
REVISIT (JTS - 2003-11-06)
Reads all the StateGroup records, for use in the RiverTrakCalibrator, that 
match the specified MeasLocGroup_num and are less than or equal to the run 
time.  It then returns the StateGroup_num of the closest matching stategroup.
If none are found, a new stategroup is added and the number of that is returned.
@param MeasLocGroup_num the measlocgroup_num to match
@param Date_Time the datetime to search for less than or equal to, 
it will be formatted for SQL with DMIUtil.formatDateTime().  Can be null.
@return the stategroup_num of the found state, or DMIUtil.MISSING_INT if 
there is an error formatting the Date_Time to a String (almost 100% guaranteed
not to happen).
@throws Exception if an error occurs
*/
public long readStateGroupForMeasLocGroup_numRunDate(long MeasLocGroup_num,
DateTime Date_Time)
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_STATEGROUP );
	String dateTime = null;
	if (Date_Time != null) {
		try {
			dateTime = DMIUtil.formatDateTime(this, Date_Time);
		}
		catch (Exception e) {
			return DMIUtil.MISSING_INT;
		}
		q.addWhereClause("StateGroup.Date_Time <= " + dateTime);
	}
	else {
		q.addWhereClause("StateGroup.Date_Time IS NULL");
		q.addWhereClause("StateGroup.Scenario = 'DEFAULT'");
	}
		
	q.addWhereClause("StateGroup.MeasLocGroup_num = " + MeasLocGroup_num);
	q.addOrderByClause ( "StateGroup.Date_Time DESC" ); 

	ResultSet rs = dmiSelect(q);	
	Vector v = toStateGroupList (rs);
	closeResultSet(rs);
	if (v.size() == 0) {
		q = new DMISelectStatement(this);
		buildSQL(q, _S_STATEGROUP);
		q.addWhereClause("StateGroup.Date_Time IS NULL");
		q.addWhereClause("StateGroup.Scenario = 'DEFAULT'");
		q.addWhereClause("StateGroup.MeasLocGroup_num = " 
			+ MeasLocGroup_num);
		rs = dmiSelect(q);
		v = toStateGroupList(rs);
		if (v.size() == 0) {
			// this is a problem.
			return -1;
		}
		RiversideDB_StateGroup sg = 
			(RiversideDB_StateGroup)v.elementAt(0);
		return sg.getStateGroup_num();
		
		/*
		// add a new one
		RiversideDB_StateGroup sg = new RiversideDB_StateGroup();
		DateTime dt = new DateTime(Date_Time);
		dt.addHour(-1);
		sg.setDate_Time(dt.getDate());
		sg.setMeasLocGroup_num(MeasLocGroup_num);
		writeStateGroup(sg);
		return getMaxRecord("StateGroup", "StateGroup_num");
		*/
	}
	else {
		RiversideDB_StateGroup sg = 
			(RiversideDB_StateGroup)v.elementAt(0);
		return sg.getStateGroup_num();
	}
}

/**
Read all the State records that match the given StateGroup_num, ordered by
Module, Variable and Seq.
@param StateGroup_num value to match against
@return a vector of objects of type RiversideDB_StateGroup with the given
StateGroup_num.
@throws Exception if an error occurs
*/
public Vector readStateListForStateGroup_num(long StateGroup_num)
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_STATE);

	q.addWhereClause("State.StateGroup_num=" + StateGroup_num); 

	if (isDatabaseVersionAtLeast(_VERSION_030000_20041001)) {
		q.addOrderByClause("State.ValueStr");
		q.addOrderByClause("State.Sequence");
	}
	else {
		q.addOrderByClause("State.Module"); 
		q.addOrderByClause("State.Variable"); 
		q.addOrderByClause("State.Seq"); 
	}
	ResultSet rs = dmiSelect(q);
	Vector v = toStateList (rs);
	closeResultSet(rs);
	return v;
}

/**
Read all the State records that match the given StateGroup_num, ordered by
Module, Variable and Seq.
@param StateGroup_num value to match against
@param module the module to match against
@return a vector of objects of type RiversideDB_StateGroup with the given
StateGroup_num.  Returns null if the database version is 3.00.00 or greater.
@throws Exception if an error occurs
*/
public Vector readStateListForStateGroup_numModule(long StateGroup_num,
String module)
throws Exception {
	if (isDatabaseVersionAtLeast(_VERSION_030000_20041001)) {
		// the module field is not in the database design
		return null;
	}
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_STATE);
	q.addWhereClause("State.StateGroup_num=" + StateGroup_num); 
	q.addWhereClause("State.Module='" + module + "'");
	q.addOrderByClause("State.Module"); 
	q.addOrderByClause("State.Variable"); 
	q.addOrderByClause("State.Seq"); 

	ResultSet rs = dmiSelect(q);
	Vector v = toStateList (rs);
	closeResultSet(rs);
	return v;
}

/**
Reads the state records with the specified OperationStateRelation_num.
@param StateGroup_num the value to match against.
@param OperationStateRelation_num the value to match against.
@return a Vector of RiversideDB_States.
@throws Exception if an error occurs.
*/
public Vector readStateForStateGroup_numOperationStateRelation_num(
long StateGroup_num, int OperationStateRelation_num)
throws Exception {
	if (!isDatabaseVersionAtLeast(_VERSION_030000_20041001)) {
		// the module field is not in the database design
		return null;
	}
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_STATE);
	q.addWhereClause("State.StateGroup_num = " + StateGroup_num);
	q.addWhereClause("State.OperationStateRelation_num = "
		+ OperationStateRelation_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toStateList(rs);
	closeResultSet(rs);
	return v;
}

/**
Read the Station data for all records matching the given MeasLoc_num.
@param MeasLoc_num value to match against.
@return a vector of objects of type RiversideDB_Station that match the given
MeasLoc_num.
@throws Exception if an error occurs
*/
public Vector readStationListForMeasLoc_num ( long MeasLoc_num )
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_STATION );
	q.addWhereClause ( "Station.MeasLoc_num=" + MeasLoc_num ); 
	ResultSet rs = dmiSelect(q);
	Vector v = toStationList (rs);
	closeResultSet(rs);
	return v;
}

/**
Read all data from the RiversideDB Tables table.
@return a Vector or RiversideDB_Tables.
@exception Exception if there is an error reading the data.
*/
public Vector readTablesList () 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL(q, _S_TABLES);
	ResultSet rs = dmiSelect(q);
	Vector v = toTablesList (rs);
	closeResultSet(rs);
	return v;
}

/**
Read all data from Tables for the given IsTSTemplate value.   This method is
only useful in database versions greater than or equal to 02.08.00.
@param IsTSTemplate the value to match against.
@return a Vector of RiversideDB_Tables that match the given IsTSTemplate value.
@throws Exception if an error occurs
*/
public Vector readTablesListForIsTSTemplate(String IsTSTemplate) 
throws Exception {
	if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)){
		DMISelectStatement q = new DMISelectStatement(this);
		buildSQL (q, _S_TABLES);
		q.addWhereClause("IsTSTemplate = '" 
			+ escape(IsTSTemplate) + "'");
		ResultSet rs = dmiSelect(q);
		Vector v = toTablesList(rs);
		closeResultSet(rs);
		return v;
	}
	else {
		return new Vector();
	}
}

/**
Read the Table record that matches the given Table_name.
@param Table_name value to match against
@return a RiversideDB_Tables object with the given Table_name, or null if none
could be found.
@throws Exception if an error occurs
*/
public RiversideDB_Tables readTablesForTable_name(String Table_name) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL (q, _S_TABLES);
	q.addWhereClause("Table_name = '" + escape(Table_name) + "'");
	ResultSet rs = dmiSelect(q);	
	Vector v = toTablesList(rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	return ((RiversideDB_Tables)v.elementAt(0));
}

/**
Read the Table record that matches the given Table_num.
@param Table_num the value to match against
@return a RiversideDB_Tables object with the given Table_num, or null if none
could be found
@throws Exception if an error occurs
*/
public RiversideDB_Tables readTablesForTable_num(long Table_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL (q, _S_TABLES);
	q.addWhereClause("Table_num = " + Table_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toTablesList(rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	return ((RiversideDB_Tables)v.elementAt(0));
}

/**
Read all TableLayout records, ordered by Identifier.
@return a Vector of TableLayout objects
@throws Exception if an error occurs
*/
public Vector readTableLayoutList() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_TABLELAYOUT );
	q.addOrderByClause("TableLayout.Identifier");
	ResultSet rs = dmiSelect(q);
	Vector v = toTableLayoutList (rs);
	closeResultSet(rs);
	return v;
}

/**
Returns the RiversideDB_TableLayout record that matches the given 
TableLayout_num.
@param TableLayout_num the TableLayout_num to match.
@return the matching RiversideDB_TableLayout record, or null if none match.
@throws Exception if an error occurs.
*/
public RiversideDB_TableLayout readTableLayoutForTableLayout_num(
int TableLayout_num)
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL ( q, _S_TABLELAYOUT );
	q.addWhereClause("TableLayout.TableLayout_num = " + TableLayout_num );
	q.addOrderByClause("TableLayout.TableLayout_num");
	ResultSet rs = dmiSelect(q);
	Vector v = toTableLayoutList (rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	return (RiversideDB_TableLayout)v.elementAt(0);
}

/**
Read a time series matching a time series identifier.
@return a time series or null if the time series is not defined in the database.
If no data records are available within the requested period, a call to
hasData() on the returned time series will return false.
@param tsident_string TSIdent string identifying the time series.  
Alternately, this can be a String representation of a Long value, in which
case it is the MeasType_num of the time series to read.
@param req_date1 Optional date to specify the start of the query (specify 
null to read the entire time series).
@param req_date2 Optional date to specify the end of the query (specify 
null to read the entire time series).
@param req_units requested data units (specify null or blank string to 
return units from the database).
@param read_data Indicates whether data should be read (specify false to 
only read header information).
@exception if there is an error reading the time series.
*/
public TS readTimeSeries (String tsident_string, DateTime req_date1,
			  DateTime req_date2, String req_units, boolean read_data )
throws Exception
{	// Read a time series from the database.
	// IMPORTANT - BECAUSE WE CAN'T GET THE LAST RECORD FROM A ResultSet
	// FOR TIME SERIES DATA RECORDS, WE CANNOT GET THE END DATES FOR MEMORY
	// ALLOCATION UP FRONT.  THEREFORE, IT IS REQUIRED THAT THE ResultSet
	// BE CONVERTED TO A VECTOR OF DATA OBJECTS, WHICH CAN THEN BE EXAMINED
	// TO GET THE DATE.  IF THIS WERE NOT THE CASE, THE CODE COULD BE
	// OPTIMIZED TO GO DIRECTLY FROM A ResultSet TO A TS.

	// First determine the MeasType for the time series...
	String routine = "RiversideDB_DMI.readTimeSeries";
	RiversideDB_MeasType mt = null;
	
	boolean isMeasType_num_boolean = false;// True if TSID is a MeasType_num
	if (StringUtil.isLong(tsident_string)) {
		// If the TSIdentString is a long value, assume that it's a MeasType_num that was passed in.
		mt = readMeasTypeForMeasType_num((new Long(tsident_string)).longValue());
		if (mt == null) {
			Message.printWarning(2, routine,
				"Unable to read time series: no MeasType for MeasType_num \"" + tsident_string + "\".");
			return null;
		}
		isMeasType_num_boolean = true;
	}
	else {
		mt = readMeasTypeForTSIdent(tsident_string);
		if (mt == null) {
			Message.printWarning(2, routine,"Unable to read time series:  no MeasType for \"" + tsident_string + "\"");
			return null;
		}
	}
	
	// Determine the table and format to read from...
	int pos = RiversideDB_Tables.indexOf ( _RiversideDB_Tables_Vector, mt.getTable_num1() );

	if ( pos < 0 ) {
		Message.printWarning ( 2, routine, "Unable to read time series:  no Tables record for table number"
		+ mt.getTable_num1() );
		return null;
	}
	// Based on the table format, call the appropriate read method...
	RiversideDB_Tables t = (RiversideDB_Tables)_RiversideDB_Tables_Vector.elementAt(pos);
	long table_layout = t.getTableLayout_num();
	// First define the time series to be returned, based on the MeasType interval base and multiplier...
	TS ts = null;
	if ( mt._Time_step_base.equalsIgnoreCase("Min") || mt._Time_step_base.equalsIgnoreCase("Minute") ) {
		ts = new MinuteTS ();
		ts.setDataInterval ( TimeInterval.MINUTE,(int)mt.getTime_step_mult());
	}
	else if ( mt._Time_step_base.equalsIgnoreCase("Hour") ) {
		ts = new HourTS ();
		ts.setDataInterval ( TimeInterval.HOUR,(int)mt.getTime_step_mult());
	}
	else if ( mt._Time_step_base.equalsIgnoreCase("Day") ) {
		ts = new DayTS ();
		ts.setDataInterval ( TimeInterval.DAY,(int)mt.getTime_step_mult());
	}
	else if ( mt._Time_step_base.equalsIgnoreCase("Month") ) {
		ts = new MonthTS ();
		ts.setDataInterval ( TimeInterval.MONTH,(int)mt.getTime_step_mult());
	}
	else if ( mt._Time_step_base.equalsIgnoreCase("Year") ) {
		ts = new YearTS ();
		ts.setDataInterval ( TimeInterval.YEAR,(int)mt.getTime_step_mult());
	}
	else if (mt._Time_step_base.equalsIgnoreCase("Irreg") || mt._Time_step_base.equalsIgnoreCase("Irregular") ) {
		ts = new IrregularTS ();
	}
	else {
        String message = "Time step " + mt._Time_step_base + " is not supported.";
        Message.printWarning ( 2, routine, message );
		throw new Exception ( message );
	}
	if ( isMeasType_num_boolean ) {
		// If a MeasType_num was used to identify the time series, set the TSID to a new string.
		ts.setIdentifier ( mt.toTSIdent() ); 
	}
	else {
        // If a full TSID was used to identify the time series, use
		// the original string because it may have specific meaning to the calling code.
		// TODO SAM 2006-10-12 Need to evaluate if the above can be used in all cases but
		// do not have tests in place to check right now.
		ts.setIdentifier ( tsident_string ); 
	}
	ts.setDescription ( mt.getDescription() );
	ts.setDataType ( mt.getData_type() );
	ts.setDataUnits ( mt.getUnits_abbrev() );
	ts.setDataUnitsOriginal ( mt.getUnits_abbrev() );
	if ( req_date1 != null ) {
		ts.setDate1 ( req_date1 );
	}
	if ( req_date2 != null ) {
		ts.setDate2 ( req_date2 );
	}
	// TODO - problem here - in order to read the header and get the
	// dates, we really need to get the dates from somewhere.  Currently
	// RiversideDB does not store the most current period dates in the
	// database - this needs to be corrected.
	if ( !read_data ) {
		return ts;
	}
	// Read the data...
	// The layout numbers are static.  Use the following to get the data records...
	DMISelectStatement q = new DMISelectStatement ( this );
	String ts_table = t.getTable_name();
	q.addTable ( ts_table );
	q.addWhereClause ( ts_table + ".MeasType_num=" + mt.getMeasType_num() );
	if ( table_layout == TABLE_LAYOUT_DATE_VALUE_TO_MINUTE ||
            table_layout == TABLE_LAYOUT_DATE_VALUE_TO_MINUTE_WITH_DURATION ||
            table_layout == TABLE_LAYOUT_DATE_VALUE_TO_MINUTE_CREATION ) {
		q.addField ( ts_table + ".Date_Time" );
		q.addField ( ts_table + ".Val" );
        q.addField ( ts_table + ".Quality_flag" );
        if ( table_layout == TABLE_LAYOUT_DATE_VALUE_TO_MINUTE_WITH_DURATION ) {
            q.addField ( ts_table + ".Duration" );
        }
		q.addOrderByClause ( ts_table + ".Date_Time" );
        if ( table_layout == TABLE_LAYOUT_DATE_VALUE_TO_MINUTE_CREATION ) {
            q.addField ( ts_table + ".Creation_Time" );
            q.addOrderByClause ( ts_table + ".Creation_Time" );
        }
		if ( req_date1 != null ) {
			q.addWhereClause ( ts_table + ".Date_Time >= " + DMIUtil.formatDateTime( this, req_date1));
		}
		if ( req_date2 != null ) {
			q.addWhereClause ( ts_table + ".Date_Time <= " + DMIUtil.formatDateTime( this, req_date2));
		}
		// Submit the query...
		ResultSet rs = dmiSelect ( q );
		// Convert the data to a Vector of records so we can get the first and last dates to allocate memory...
		Vector v = toTSDateValueToMinuteList ( table_layout, rs );
		closeResultSet(rs);
		int size = 0;
		if ( v != null ) {
			size = v.size();
		}
		if ( size == 0 ) {
			// Return the TS because there are no data to set dates.
			// The header will be complete other than dates but no data will be filled in...
			return ts;
		}
		RiversideDB_TSDateValueToMinute data = null;

		if ( (req_date1 != null) && (req_date2 != null) ) {
			// Allocate the memory regardless of whether there was
			// data.  If no data have been found then missing data will be initialized...
			ts.setDate1(req_date1);
			ts.setDate1Original(req_date1);
			ts.setDate2(req_date2);
			ts.setDate2Original(req_date2);
            // All the minute data has flags.
            ts.hasDataFlags(true, 4);
			ts.allocateDataSpace();
		}
		else if ( size > 0 ) {
			// Set the date from the records...
			data = (RiversideDB_TSDateValueToMinute)v.elementAt(0);
			ts.setDate1(new DateTime(data._Date_Time));
			ts.setDate1Original(new DateTime(data._Date_Time));

			data = (RiversideDB_TSDateValueToMinute)v.elementAt(size - 1);
			ts.setDate2(new DateTime(data._Date_Time));
			ts.setDate2Original(new DateTime(data._Date_Time));
			// All the minute data has flags.
            ts.hasDataFlags(true, 4);
			ts.allocateDataSpace();
		}
		// The date needs to be the correct precision so assign from
		// the TS start date (the precision is adjusted when dates are set)...
		DateTime date = new DateTime ( ts.getDate1() );
		for ( int i = 0; i < size; i++ ) {
			// Loop through and assign the data...
			data = (RiversideDB_TSDateValueToMinute)v.elementAt(i);
			// Set the date rather than declaring a new instance
			// to increase performance... data._Date_Time is a
			// Date so cannot be used directly in code that needs a DateTime instance...
			date.setDate ( data._Date_Time );
			// For now ignore the revision number and quality...
			if ( !DMIUtil.isMissing(data._Val) ) {
                if ( table_layout == TABLE_LAYOUT_DATE_VALUE_TO_MINUTE ||
                        table_layout == TABLE_LAYOUT_DATE_VALUE_TO_MINUTE_CREATION ) {
                    // Has flag but no duration.
                    ts.setDataValue ( date, data._Val, data._Quality_flag, 0 );
                }
                else if ( table_layout == TABLE_LAYOUT_DATE_VALUE_TO_MINUTE_WITH_DURATION ) {
                    // Also need to set the duration and quality flag...
                    ts.setDataValue ( date, data._Val, data._Quality_flag, data._Duration );
                }
            }
		}
	}
	else {
        String message = "RiversideDB TableLayout " + table_layout + " is not supported.";
        Message.printWarning ( 2, routine, message );
        throw new Exception ( message );
        // FIXME SAM 2007-12-21 Need to look up the table number from the table format table and not hard-code numbers.
	}
	return ts;
}

/**
Unsupported.
*/
public TS readTimeSeries(TS req_ts, String fname, DateTime date1, DateTime date2, String req_units, boolean read_data)
throws Exception {
	return null;
}

/**
Unsupported.
*/
public Vector readTimeSeriesList(String fname, DateTime date1, DateTime date2, String req_units, boolean read_data)
throws Exception {
	return null;
}

/**
Unsupported.
*/
public Vector readTimeSeriesList(TSIdent tsident, String fname, DateTime date1, 
DateTime date2, String req_units, boolean read_data)
throws Exception {
	return null;
}

/**
Read all TSProduct records.
@return a Vector of TSProduct objects
@throws Exception if an error occurs
*/
public Vector readTSProductList() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL(q, _S_TSPRODUCT);
	ResultSet rs = dmiSelect(q);
	Vector v = toTSProductList (rs);
	closeResultSet(rs);
	return v;
}

/**
Read all TSProductProps records.
@return a Vector of TSProductProps objects
@throws Exception if an error occurs
*/
public Vector readTSProductPropsList() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement ( this );
	buildSQL(q, _S_TSPRODUCTPROPS);
	ResultSet rs = dmiSelect(q);
	Vector v = toTSProductPropsList (rs);
	closeResultSet(rs);
	return v;
}

/**
Reads the TSProduct table for the record that matches the specified identifier
and user_num.<p>Permissions are not checked here for whether the user has 
permission to read the record -- that should be handled by the calling code.
@param identifier the identifier for which to search.
@return a RiversideDB_TSProduct object, or null if no matching records could be
found.
@throws Exception if an error occurs.
*/
public RiversideDB_TSProduct readTSProductForIdentifier(String identifier)
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_TSPRODUCT);
	
	q.addWhereClause("TSProduct.identifier = '" + identifier + "'");
	
	ResultSet rs = dmiSelect(q);
	Vector v = toTSProductList(rs);
	closeResultSet(rs);

	if (v == null || v.size() == 0) {
		return null;
	}
	else {
		return (RiversideDB_TSProduct)v.elementAt(0);
	}
}

/**
Reads the TSProduct table for all records that matches the specified 
tsproduct_num, productgroup_num.
@param tsproduct_num the tsproduct_num for which to search.  If not missing,
then productgroup_num MUST be missing.
@param productgroup_num the productgroup_num for which to search.  If not 
missing, then tsproduct_num MUST be missing.
@return a Vector of matching HydroBase_TSProduct records.
@throws Exception if an error occurs.
@deprecated the user_num was removed from the call.
*/
public Vector readTSProductListForTSProduct_numProductGroup_numUser_num(
int tsproduct_num, int productgroup_num)
throws Exception {
	return readTSProductListForTSProduct_numProductGroup_num(	
		tsproduct_num, productgroup_num);
}

/**
Reads the TSProduct table for all records that matches the specified 
tsproduct_num and productgroup_num.
@param tsproduct_num the tsproduct_num for which to search.  If not missing,
then productgroup_num MUST be missing.
REVISIT (JTS - 2005-08-29) 
Why this provision??
@param productgroup_num the productgroup_num for which to search.  If not 
missing, then tsproduct_num MUST be missing.
REVISIT (JTS - 2005-08-29) 
Why this provision??
@return a Vector of matching HydroBase_TSProduct records.
@throws Exception if an error occurs.
@deprecated
*/
public Vector readTSProductListForTSProduct_numProductGroup_num(
int tsproduct_num, int productgroup_num)
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_TSPRODUCT);

	// REVISIT (JTS - 2005-08-29)
	// As I mentioned in the Javadocs, I am unsure right now about why
	// there is the provision in this code that one or the other can
	// be non-missing, but not both.  Further research is necessary.

	if (!DMIUtil.isMissing(tsproduct_num)
	    && !DMIUtil.isMissing(productgroup_num)) {
	    	throw new Exception(
			"One and only one value can be non-missing."
			+ "  TSProduct_num: " + tsproduct_num 
			+ "  ProductGroup_num: " + productgroup_num);
	}
	
	if (DMIUtil.isMissing(tsproduct_num)
	    && DMIUtil.isMissing(productgroup_num)) {
		// query for all
	}
	else if (!DMIUtil.isMissing(tsproduct_num)) {
		q.addWhereClause("TSProduct.tsproduct_num = " 
			+ tsproduct_num);
	}
	else {
		q.addWhereClause("TSProduct.productgroup_num = " 
			+ productgroup_num);
	}

	ResultSet rs = dmiSelect(q);
	Vector v = toTSProductList(rs);
	closeResultSet(rs);
	return v;
}

/**
Reads the TSProductProps table for all records that matches the specified 
tsproduct_num.<p>
@param tsproduct_num the tsproduct_num to use for matching records.
@return a Vector of the matching RiversideDB_TSProductProps records.
@throws Exception if an error occurs.
*/
public Vector readTSProductPropsListForTSProduct_num(int tsproduct_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_TSPRODUCTPROPS);
	if (tsproduct_num < 0) {
		throw new Exception("TSProduct_num (" + tsproduct_num + ") "
			+ "must be >= 0");
	}
	q.addWhereClause("TSProductProps.tsproduct_num = " + tsproduct_num);
	ResultSet rs = dmiSelect(q);
	Vector v = toTSProductPropsList(rs);
	closeResultSet(rs);
	return v;
}

// S FUNCTIONS

/**
Sets the DBuser for the session (e.g., from a login).  The DBGroup_num defaults
to the primary group for the user.
@param user a valid DBUser object.
@throws Exception if an error occurs when trying to read from the dbgroup table
or if no matching DB Group could be found for the user.
*/
public void setDBUser(RiversideDB_DBUser user) 
throws Exception {
	String routine = "setDBUser";
	
	_dbuser = user;
	_dbgroup = readDBGroupForDBGroup_num(user.getPrimaryDBGroup_num());
	if (_dbgroup == null) {
		String message = "Unable to read a user group for "
			+ "the specified user with PrimaryDBGroup_num of '"
			+ user.getPrimaryDBGroup_num() + "'";

		Message.printWarning(1, routine, message);
		throw new Exception(message);
	}
}

// T FUNCTIONS

/**
Converts a result set to a Vector of ActionDataModels.
@param rs the ResultSet to process.
@return a Vector of ActionDataModels.
@throws Exception if an error occurs.
*/
private Vector toActionList(ResultSet rs) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	boolean b = false;
	String s;
	int i;
	ActionDataModel data = null;
	int[] nums = null;
	while (rs.next()) {
		data = new ActionDataModel();
		index = 1;
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setActionNum(i);
		}
		nums = readActionContactNums(i);
		data.setContactIDs(nums);
		b = rs.getBoolean(index++);
		if (!rs.wasNull()) {		
			data.setIsActive(b);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setActionType(Action.getActionType(s.trim()));
			data.setActionName(s.trim());
		}		
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setSeverityType(i);
		}				
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setActionName(s.trim());
		}				
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setDescription(s.trim());
		}				
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setActionBaseTime(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setActionBaseTimeFormat(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setActionInterval(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setEvaluationWindow(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setProperties(s.trim());
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setResultLevel(i);
		}
		
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_Area.
@param rs ResultSet from a Area table query.
@throws Exception if an error occurs
*/
private Vector toAreaList ( ResultSet rs ) 
throws Exception {
 	Vector v = new Vector();
	int index = 1;
	String s;
	long l;
	double d;
	RiversideDB_Area data = null;
	while ( rs.next() ) {
		data = new RiversideDB_Area();
		index = 1;
		l = rs.getLong(index++);
		if ( !rs.wasNull() ) {
			data.setArea_num(l);
		}
		l = rs.getLong(index++);
		if ( !rs.wasNull() ) {
			data.setMeasLoc_num(l);
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setArea_id(s.trim());
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setArea_name(s.trim());
		}
		d = rs.getDouble(index++);
		if ( !rs.wasNull() ) {
			data.setArea(d);
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setArea_units(s.trim());
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_AutoUpdateProduct.
@param rs ResultSet from a AutoUpdateProduct table query.
@throws Exception if an error occurs
*/
private Vector toAutoUpdateProductList ( ResultSet rs ) 
throws Exception {
 	Vector v = new Vector();
	int index = 1;
	String s;
	int i;
	RiversideDB_AutoUpdateProduct data = null;
	while ( rs.next() ) {
		data = new RiversideDB_AutoUpdateProduct();
		index = 1;
		i= rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setAutoUpdateProduct_num(i);
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setTSProduct_num(i);
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setProductGroup_num(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setIdentifier(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setName(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setComment(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setIsEnabled(s.trim());
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setX(i);
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setY(i);
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setWidth(i);
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setHeight(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setProperties(s.trim());
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setDBUser_num(i);
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setDBGroup_num(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setDBPermissions(s.trim());
		}
		
		v.addElement(data);
	}
	return v;
}

/** 
Converts a ResultSet into a Vector of Contacts.
@param rs the ResultSet to convert.
@return a Vector of Contacts.
@throws Exception if an error occurs.
*/
/* TODO SAM Evaluate whether needed
private Vector toContactList(ResultSet rs) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	int i;
	String s;
	Contact data = null;
	while (rs.next()) {
		data = new Contact();
		index = 1;
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setContactNum(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setFirstName(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setLastName(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setPhoneNumber(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setFaxNumber(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setPagerNumber(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) { 
			data.setEmailAddress(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setIMAddress(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setSMSAddress(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setSkypeID(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setProperties(s.trim());
		}
		v.add(data);
	}
	return v;
}
*/

/**
Convert a ResultSet to a Vector of RiversideDB_DataDimension.
@param rs ResultSet from a DataDimension table query.
@throws Exception if an error occurs
*/
private Vector toDataDimensionList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	RiversideDB_DataDimension data = null;
	while ( rs.next() ) {
		data = new RiversideDB_DataDimension();
		index = 1;
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDimension ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDescription ( s.trim() );
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_DataSource.
@param rs ResultSet from a DataSource table query.
@throws Exception if an error occurs
*/
private Vector toDataSourceList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	RiversideDB_DataSource data = null;
	while ( rs.next() ) {
		data = new RiversideDB_DataSource();
		index = 1;
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setSource_abbrev ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setSource_name ( s.trim() );
		}
		v.addElement(data);
	}
	return v;
}

/**
Converts a ResultSet to a Vector of DataTestDataModels.
@param rs the ResultSet to convert.
@return a Vector of DataTestDataModels.
@throws Exception if an error occurs.
*/
private Vector toDataTestList(ResultSet rs) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	boolean b = false;
	int i;
	String s;
	DataTestDataModel data = null;
	while (rs.next()) {
		data = new DataTestDataModel();
		index = 1;
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setDataTestNum(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setDescription(s.trim());
		}				
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setDataTestGroupNum(i);
		}
		b = rs.getBoolean(index++);
		if (!rs.wasNull()) {
			data.setIsActive(b);
		}				
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setActiveSeasonStart(s.trim());
		}				
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setActiveSeasonEnd(s.trim());
		}				
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setActiveSeasonFormat(s.trim());
		}				
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setDataTestExpressionNum(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setTestBaseTime(s.trim());
		}				
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setTestBaseTimeFormat(s.trim());
		}				
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setTestInterval(s.trim());
		}				
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setPositiveTestInterval(s.trim());
		}				
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setEvaluationWindow(s.trim());
		}				
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setEvaluationInterval(s.trim());
		}						
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setMessage(s.trim());
		}				
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setProperties(s.trim());
		}				
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setPositiveCount(i);
		}		
		
		v.addElement(data);
	}
	return v;
}

/**
Converts a ResultSet to a Vector of DataTestExpressionDataModels.
@param rs the ResultSet to convert.
@return a Vector of DataTestExpressionDataModels.
@throws Exception if an error occurs.
*/
private Vector toDataTestExpressionList(ResultSet rs) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	int i;
	DataTestExpressionDataModel data = null;
	while (rs.next()) {
		data = new DataTestExpressionDataModel();
		index = 1;
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setExpressionNum(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setOperator(s.trim());
			data.setExpressionType(
				DataTestExpression.getExpressionType(s.trim()));
		}						
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setLeftSideType(
				DataTestSide.getSideType(s.trim()));
		}		
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setLeftSideIDNum(i);
		}		
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setRightSideType(DataTestSide.getSideType(
				s.trim()));
		}		
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setRightSideIDNum(i);
		}		
		
		v.addElement(data);
	}
	return v;
}

/**
Converts a ResultSet to a Vector of DataTestFunctionDataModels.
@param rs the ResultSet to convert.
@return a Vector of DataTestFunctionDataModels.
@throws Exception if an error occurs.
*/
private Vector toDataTestFunctionList(ResultSet rs) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	int i;
	DataTestFunctionDataModel data = null;
	while (rs.next()) {
		data = new DataTestFunctionDataModel();
		index = 1;
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setFunctionNum(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setFunctionType(DataTestFunction.getFunctionType(
				s.trim()));
			data.setFunctionName(s.trim());
		}		
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setProperties(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setMessage(s.trim());
		}				
		
		v.addElement(data);
	}
	return v;
}

/**
Converts a ResultSet to a Vector of DataTestResult.
@param rs the ResultSet to convert.
@return a Vector of DataTestResults.
@throws Exception if an error occurs.
*/
private Vector toDataTestResultsList(ResultSet rs) 
throws Exception {
	Vector v = new Vector();
	double d;
	int index = 1;
	int i;
	String s;
	DataTestResult data = null;
	while (rs.next()) {
		data = new DataTestResult();
		index = 1;
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setDataTestNum(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setTestTime(data.getTestTimeFormat().parse(
				s.trim()));
		}
		d = rs.getDouble(index++);
		if (!rs.wasNull()) {
			data.setLeftSideValue(d);
		}
		d = rs.getDouble(index++);
		if (!rs.wasNull()) {
			data.setRightSideValue(d);
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setDataTestExpressionNum(i);
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setLevel(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setMessage(s.trim());
		}
		v.add(data);	
	}

	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_DataType.
@param rs ResultSet from a DataType table query.
@throws Exception if an error occurs
*/
private Vector toDataTypeList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	double d;
	RiversideDB_DataType data = null;
	while ( rs.next() ) {
		data = new RiversideDB_DataType();
		index = 1;
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDataType ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDescription ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDimension ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setMeas_time_scale ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setMeas_loc_type ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setRecord_type ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDefault_engl_units ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDefault_si_units ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setSHEF_pe ( s.trim() );
		}
		d = rs.getDouble ( index++ );
		if ( !rs.wasNull() ) {
			data.setDefault_engl_min ( d );
		}
		d = rs.getDouble ( index++ );
		if ( !rs.wasNull() ) {
			data.setDefault_engl_max ( d );
		}
		d = rs.getDouble ( index++ );
		if ( !rs.wasNull() ) {
			data.setDefault_si_min ( d );
		}
		d = rs.getDouble ( index++ );
		if ( !rs.wasNull() ) {
			data.setDefault_si_max ( d );
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_DataUnits.
@param rs ResultSet from a DataUnits table query.
@throws Exception if an error occurs
*/
private Vector toDataUnitsList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	double d;
	int i;
	RiversideDB_DataUnits data = null;
	while ( rs.next() ) {
		data = new RiversideDB_DataUnits();
		index = 1;
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setUnits_abbrev ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setUnits_description ( s.trim() );
		}
		i = rs.getInt ( index++ );
		if ( !rs.wasNull() ) {
			data.setOutput_precision ( i );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDimension ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setBase_unit ( s.trim() );
		}
		d = rs.getDouble ( index++ );
		if ( !rs.wasNull() ) {
			data.setMult_factor ( d );
		}
		d = rs.getDouble ( index++ );
		if ( !rs.wasNull() ) {
			data.setAdd_factor ( d );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setUnits_system ( s.trim() );
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_DBGroup.
@param rs ResultSet from a DBGroup table query.
@throws Exception if an error occurs
*/
private Vector toDBGroupList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	int i;
	RiversideDB_DBGroup data = null;
	while ( rs.next() ) {
		data = new RiversideDB_DBGroup();
		index = 1;
		i = rs.getInt ( index++ );
		if ( !rs.wasNull() ) {
			data.setDBGroup_num( i );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDescription( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setID( s.trim() );
		}		
		v.addElement(data);
	}
	return v;
}	

/**
Convert a ResultSet to a Vector of RiversideDB_DBUser.
@param rs ResultSet from a DBUser table query.
@throws Exception if an error occurs
*/
private Vector toDBUserList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	int i;
	RiversideDB_DBUser data = null;
	while ( rs.next() ) {
		data = new RiversideDB_DBUser();
		index = 1;
		i = rs.getInt ( index++ );
		if ( !rs.wasNull() ) {
			data.setDBUser_num( i );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDescription( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setLogin( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setPassword( s.trim() );
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setDefault_DBPermissions(s.trim());
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setPrimaryDBGroup_num(i);
		}
		v.addElement(data);
	}
	return v;
}	

/**
Convert a ResultSet to a Vector of RiversideDB_DBUserDBGroupRelation.
@param rs ResultSet from a DBUserDBGroupRelation table query.
@throws Exception if an error occurs
*/
private Vector toDBUserDBGroupRelationList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	int i;
	RiversideDB_DBUserDBGroupRelation data = null;
	while ( rs.next() ) {
		data = new RiversideDB_DBUserDBGroupRelation();
		index = 1;
		i = rs.getInt ( index++ );
		if ( !rs.wasNull() ) {
			data.setDBUser_num( i );
		}
		i = rs.getInt ( index++ );
		if ( !rs.wasNull() ) {
			data.setDBGroup_num( i );
		}		
		v.addElement(data);
	}
	return v;
}	

/**
Convert a ResultSet to a Vector of RiversideDB_DBUserMeasLocGroupRelation.
@param rs ResultSet from a DBUserMeasLocGroupRelation table query.
@throws Exception if an error occurs
*/
private Vector toDBUserMeasLocGroupRelationList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	int i;
	RiversideDB_DBUserMeasLocGroupRelation data = null;
	while ( rs.next() ) {
		data = new RiversideDB_DBUserMeasLocGroupRelation();
		index = 1;
		i = rs.getInt ( index++ );
		if ( !rs.wasNull() ) {
			data.setDBUser_num( i );
		}
		i = rs.getInt ( index++ );
		if ( !rs.wasNull() ) {
			data.setMeasLocGroup_num( i );
		}		
		v.addElement(data);
	}
	return v;
}	

/**
Convert a ResultSet to a Vector of RiversideDB_ExportConf.
@param rs ResultSet from an ExportConf table query.
@throws Exception if an error occurs
*/
private Vector toExportConfList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	int i;
	long l;
	RiversideDB_ExportConf data = null;
	while ( rs.next() ) {
		data = new RiversideDB_ExportConf();
		index = 1;
		i = rs.getInt ( index++ );
		if ( !rs.wasNull() ) {
			data.setExportProduct_num ( i );
		}
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setMeasType_num ( l );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setExport_id ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setExport_units ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setIsActive ( s.trim() );
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_ExportProduct.
@param rs ResultSet from an ExportProduct table query.
@throws Exception if an error occurs
*/
private Vector toExportProductList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	int i;
	Date d;
	long l;
	RiversideDB_ExportProduct data = null;
	while ( rs.next() ) {
		data = new RiversideDB_ExportProduct();
		index = 1;
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setExportProduct_num ( l );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setProduct_name ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setProduct_type ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setIsActive ( s.trim() );
		}
// REVISIT (JTS - 2003-06-02)
// This will be phased out later		
//		if (getDatabaseVersion() < _VERSION_020800_20030422) {		
			s = rs.getString ( index++ );
			if ( !rs.wasNull() ) {
				data.setProduct_group ( s.trim() );
			}
//		}
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setExport_order ( l );
		}
		d = rs.getTimestamp ( index++ );
		if ( !rs.wasNull() ) {
			data.setLast_export_date ( d );
		}
		d = rs.getTimestamp ( index++ );
		if ( !rs.wasNull() ) {
			data.setNext_export_date ( d );
		}
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setRetries ( l );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setUser_login ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setUser_PWD ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setFirewall_user ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setFirewall_user_PWD ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setExport_start ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setExport_end ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDestination_dir ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDestination_file ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setProperties ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setIsAutomated ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setIsInterval ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setExport_year ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setExport_month ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setExport_day ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setExport_hour ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setExport_minute ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setExport_second ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setExport_weekday ( s.trim() );
		}
		if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)) {
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setTSProduct_num(i);
			}
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setProductGroup_num(i);
			}
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setDBUser_num(i);
			}
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setDBGroup_num(i);
			}
			s = rs.getString(index++);
			if (!rs.wasNull()) {
				data.setDBPermissions(s.trim());
			}
			i = rs.getInt(index);
			if (!rs.wasNull()) {
				data.setMeasLocGroup_num(i);
			}
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_ExportType.
@param rs ResultSet from a ExportType table query.
@throws Exception if an error occurs
*/
private Vector toExportTypeList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	RiversideDB_ExportType data = null;
	while ( rs.next() ) {
		data = new RiversideDB_ExportType();
		index = 1;
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setName ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setComment ( s.trim() );
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_Geoloc.
@param rs ResultSet from a Geoloc table query.
@throws Exception if an error occurs
*/
private Vector toGeolocList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	long l;
	double d;
	RiversideDB_Geoloc data = null;
	while ( rs.next() ) {
		data = new RiversideDB_Geoloc();
		index = 1;
		l = rs.getLong(index++);
		if ( !rs.wasNull() ) {
			data.setGeoloc_num(l);
		}
		d = rs.getDouble(index++);
		if ( !rs.wasNull() ) {
			data.setLatitude(d);
		}
		d = rs.getDouble(index++);
		if ( !rs.wasNull() ) {
			data.setLongitude(d);
		}
		d = rs.getDouble(index++);
		if ( !rs.wasNull() ) {
			data.setX(d);
		}
		d = rs.getDouble(index++);
		if ( !rs.wasNull() ) {
			data.setY(d);
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setCountry(s.trim());
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setState(s.trim());
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setCounty(s.trim());
		}
		d = rs.getDouble(index++);
		if ( !rs.wasNull() ) {
			data.setElevation(d);
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setElevation_units(s.trim());
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_ImportConf.
@param rs ResultSet from an ImportConf table query.
@throws Exception if an error occurs
*/
private Vector toImportConfList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	int i;
	long l;
	RiversideDB_ImportConf data = null;
	while ( rs.next() ) {
		data = new RiversideDB_ImportConf();
		index = 1;
		i = rs.getInt ( index++ );
		if ( !rs.wasNull() ) {
			data.setImportProduct_num ( i );
		}
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setMeasType_num ( l );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setExternal_table ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setExternal_field ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setExternal_id ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setExternal_units ( s.trim() );
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setIsActive(s.trim());
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_ImportProduct.
@param rs ResultSet from an ImportProduct table query.
@throws Exception if an error occurs
*/
private Vector toImportProductList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	int i;
	Date d;
	long l;
	RiversideDB_ImportProduct data = null;
	while ( rs.next() ) {
		data = new RiversideDB_ImportProduct();
		index = 1;
		i = rs.getInt ( index++ );
		if ( !rs.wasNull() ) {
			data.setImportProduct_num ( i );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setProduct_name ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setProduct_type ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setIsActive ( s.trim() );
		}
// REVISIT (JTS - 2003-06-02)
// This will be phased out later		
//		if (getDatabaseVersion() < _VERSION_020800_20030422) {		
			s = rs.getString ( index++ );
			if ( !rs.wasNull() ) {
				data.setProduct_group ( s.trim() );
			}
//		}
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setImport_order ( l );
		}
		d = rs.getTimestamp ( index++ );
		if ( !rs.wasNull() ) {
			data.setLast_import_date ( d );
		}
		d = rs.getTimestamp ( index++ );
		if ( !rs.wasNull() ) {
			data.setNext_import_date ( d );
		}
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setRetries ( l );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setUser_login ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setUser_PWD ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setFirewall_user ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setFirewall_user_PWD ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setSource_URL_base ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setSource_URL_file ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setImport_window ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setAdd_source_URL_base ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setAdd_source_URL_file ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDestination_dir ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDestination_file ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setProperties ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setIsAutomated ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setIsInterval ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setImport_year ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setImport_month ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setImport_day ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setImport_hour ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setImport_minute ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setImport_second ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setImport_weekday ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setImport_delay ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDoArchive ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setArchive_dir ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setArchive_file ( s.trim() );
		}
		if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)) {
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setProductGroup_num(i);
			}
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setDBUser_num(i);
			}
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setDBGroup_num(i);
			}
			s = rs.getString(index++);
			if (!rs.wasNull()) {
				data.setDBPermissions(s.trim());
			}
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setMeasLocGroup_num(i);
			}
		}		
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_ImportType.
@param rs ResultSet from a ImportType table query.
@throws Exception if an error occurs
*/
private Vector toImportTypeList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	RiversideDB_ImportType data = null;
	while ( rs.next() ) {
		data = new RiversideDB_ImportType();
		index = 1;
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setName ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setComment ( s.trim() );
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_MeasCreateMethod.
@param rs ResultSet from a MeasCreateMethod table query.
@throws Exception if an error occurs
*/
private Vector toMeasCreateMethodList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	RiversideDB_MeasCreateMethod data = null;
	while ( rs.next() ) {
		data = new RiversideDB_MeasCreateMethod();
		index = 1;
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setMethod ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDescription ( s.trim() );
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_MeasLoc.
@param rs ResultSet from a MeasLoc table query.
@throws Exception if an error occurs
*/
private Vector toMeasLocList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	int i;
	long l;
	RiversideDB_MeasLoc data = null;
	while ( rs.next() ) {
		data = new RiversideDB_MeasLoc();
		index = 1;
		l = rs.getLong(index++);
		if ( !rs.wasNull() ) {
			data.setMeasLoc_num(l);
		}
		l = rs.getLong(index++);
		if ( !rs.wasNull() ) {
			data.setGeoloc_num(l);
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setIdentifier(s.trim());
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setMeasLoc_name(s.trim());
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setSource_abbrev(s.trim());
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setMeas_loc_type(s.trim());
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setComment(s.trim());
		}
		if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)) {
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setDBUser_num(i);
			}
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setDBGroup_num(i);
			}
			s = rs.getString(index++);
			if (!rs.wasNull()) {
				data.setDBPermissions(s.trim());
			}
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setMeasLocGroup_num(i);
			}
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_MeasLocGroup
@param rs ResultSet from a MeasLocGroup table query.
@throws Exception if an error occurs
*/
private Vector toMeasLocGroupList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	int i;
	RiversideDB_MeasLocGroup data = null;
	while ( rs.next() ) {
		data = new RiversideDB_MeasLocGroup();
		index = 1;
		i = rs.getInt(index++);
		if ( !rs.wasNull() ) {
			data.setMeasLocGroup_num(i);
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setIdentifier(s.trim());
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setName(s.trim());
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setDescription(s.trim());
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setOptable(s.trim());
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setDBUser_num(i);
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setDBGroup_num(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setDBPermissions(s.trim());
		}

		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_MeasLocType
@param rs ResultSet from a MeasLocType table query.
@throws Exception if an error occurs
*/
private Vector toMeasLocTypeList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	RiversideDB_MeasLocType data = null;
	while ( rs.next() ) {
		data = new RiversideDB_MeasLocType();
		index = 1;
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setType ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDescription ( s.trim() );
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_MeasQualityFlag.
@param rs ResultSet from a MeasQualityFlag table query.
@throws Exception if an error occurs
*/
private Vector toMeasQualityFlagList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	RiversideDB_MeasQualityFlag data = null;
	while ( rs.next() ) {
		data = new RiversideDB_MeasQualityFlag();
		index = 1;
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setQuality_flag ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDescription ( s.trim() );
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_MeasReducGridWeight.
@param rs ResultSet from a MeasReducGridWeight table query.
@throws Exception if an error occurs
*/
private Vector toMeasReducGridWeightList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	double d;
	long l;
	RiversideDB_MeasReducGridWeight data = null;
	while ( rs.next() ) {
		data = new RiversideDB_MeasReducGridWeight();
		index = 1;
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setOutputMeasType_num ( l );
		}
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setInputMeasType_num ( l );
		}
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setInput_Row ( l );
		}
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setInput_Column ( l );
		}
		d = rs.getDouble ( index++ );
		if ( !rs.wasNull() ) {
			data.setArea ( d );
		}
		d = rs.getDouble ( index++ );
		if ( !rs.wasNull() ) {
			data.setArea_Fraction ( d );
		}
		d = rs.getDouble ( index++ );
		if ( !rs.wasNull() ) {
			data.setWeight ( d );
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_MeasReducRelation.
@param rs ResultSet from a MeasReducRelation table query.
@throws Exception if an error occurs
*/
private Vector toMeasReducRelationList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	double d;
	long l;
	RiversideDB_MeasReducRelation data = null;
	while ( rs.next() ) {
		data = new RiversideDB_MeasReducRelation();
		index = 1;
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setOutputMeasType_num ( l );
		}
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setInputMeasType_num ( l );
		}
		d = rs.getDouble ( index++ );
		if ( !rs.wasNull() ) {
			data.setWeight ( d );
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_MeasReduction.
@param rs ResultSet from a MeasReduction table query.
@throws Exception if an error occurs
*/
private Vector toMeasReductionList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	long l;
	int i;
	RiversideDB_MeasReduction data = null;
	while ( rs.next() ) {
		data = new RiversideDB_MeasReduction();
		index = 1;
		l = rs.getLong ( index++ );
		// REVISIT [LT[ 2005-01-04. Checking if null after getLong?
		//                          Same ? for the others????
		if ( !rs.wasNull() ) {
			data.setOutputMeasType_num ( l );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setMethod ( s.trim() );
		}
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setCreate_order ( l );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setProperties ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			if (isDatabaseVersionAtLeast(_VERSION_030000_20041001)){
				data.setIsActive ( s.trim() );
			}
			else {
				data.setActive   ( s.trim() );
			}
		}
		if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)) {
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setDBUser_num(i);
			}
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setDBGroup_num(i);
			}
			s = rs.getString(index++);
			if (!rs.wasNull()) {
				data.setDBPermissions(s.trim());
			}
		}		
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_MeasReductionType.
@param rs ResultSet from a MeasReductionType table query.
@throws Exception if an error occurs
*/
private Vector toMeasReductionTypeList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	RiversideDB_MeasReductionType data = null;
	while ( rs.next() ) {
		data = new RiversideDB_MeasReductionType();
		index = 1;
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setType ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDescription ( s.trim() );
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_MeasScenario.
@param rs ResultSet from a MeasScenario table query.
@throws Exception if an error occurs
*/
private Vector toMeasScenarioList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	long l;
	RiversideDB_MeasScenario data = null;
	while ( rs.next() ) {
		data = new RiversideDB_MeasScenario();
		index = 1;
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setObsMeasType_num ( l );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setMethod ( s.trim() );
		}
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setCreate_order ( l );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setProperties ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			if (isDatabaseVersionAtLeast(_VERSION_030000_20041001)){
				data.setIsActive ( s.trim() );
			}
			else {
				data.setActive ( s.trim() );
			}
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_MeasScenarioRelation.
@param rs ResultSet from a MeasScenarioRelation table query.
@throws Exception if an error occurs
*/
private Vector toMeasScenarioRelationList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	double d;
	long l;
	RiversideDB_MeasScenarioRelation data = null;
	while ( rs.next() ) {
		data = new RiversideDB_MeasScenarioRelation();
		index = 1;
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setObsMeasType_num ( l );
		}
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setQFMeasType_num ( l );
		}
		d = rs.getDouble ( index++ );
		if ( !rs.wasNull() ) {
			data.setWeight ( d );
		}
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setScenarioMeasType_num ( l );
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_MeasTimeScale.
@param rs ResultSet from a MeasTimeScale table query.
@throws Exception if an error occurs
*/
private Vector toMeasTimeScaleList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	RiversideDB_MeasTimeScale data = null;
	while ( rs.next() ) {
		data = new RiversideDB_MeasTimeScale();
		index = 1;
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setScale ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDescription ( s.trim() );
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_MeasTransProtocol.
@param rs ResultSet from a MeasTransProtocol table query.
@throws Exception if an error occurs
*/
private Vector toMeasTransProtocolList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	RiversideDB_MeasTransProtocol data = null;
	while ( rs.next() ) {
		data = new RiversideDB_MeasTransProtocol();
		index = 1;
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setProtocol ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDescription ( s.trim() );
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_MeasType.
@param rs ResultSet from a MeasType/MeasLoc table query.
@throws Exception if an error occurs
*/
private Vector toMeasTypeList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	double d;
	long l;
	int i;
	RiversideDB_MeasType data = null;
	while ( rs.next() ) {
		data = new RiversideDB_MeasType();
		index = 1;
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setMeasType_num ( l );
		}
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setMeasLoc_num ( l);
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setData_type ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setSub_type ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setTime_step_base ( s.trim() );
		}
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setTime_step_mult ( l );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setSource_abbrev ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setScenario ( s.trim() );
		}
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setTable_num1 ( l );
		}
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setDbload_method1 ( l );
		}
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setTable_num2 ( l );
		}
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setDbload_method2 ( l );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDescription ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setUnits_abbrev ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setCreate_method ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setTransmitProtocol ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setStatus ( s.trim() );
		}
		d = rs.getDouble ( index++ );
		if ( !rs.wasNull() ) {
			data.setMin_check ( d );
		}
		d = rs.getDouble ( index++ );
		if ( !rs.wasNull() ) {
			data.setMax_check ( d );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			if ( isDatabaseVersionAtLeast(_VERSION_030000_20041001) ) {
				data.setIsEditable ( s.trim() );
			}
			else {
				data.setEditable   ( s.trim() );
			}
		}
		if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)){
			s = rs.getString(index++);
			if (!rs.wasNull()) {
				data.setIsVisible(s.trim());
			}
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setDBUser_num(i);
			}
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setDBGroup_num(i);
			}
			s = rs.getString(index++);
			if (!rs.wasNull()) {
				data.setDBPermissions(s.trim());
			}			
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setTS_DBUser_num(i);
			}
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setTS_DBGroup_num(i);
			}
			s = rs.getString(index++);
			if (!rs.wasNull()) {
				data.setTS_DBPermissions(s.trim());
			}						
		}		
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setIdentifier ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setMeasLoc_name ( s.trim() );
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_MeasTypeStats.
@param rs ResultSet from a MeasTypeStats table query.
@throws Exception if an error occurs
*/
private Vector toMeasTypeStatsList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	double d;
	Date dt;
	long l;
	RiversideDB_MeasTypeStats data = null;
	while ( rs.next() ) {
		data = new RiversideDB_MeasTypeStats();
		index = 1;
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setMeasType_num ( l );
		}
		dt = rs.getTimestamp ( index++ );
		if ( !rs.wasNull() ) {
			data.setStart_date ( dt );
		}
		dt = rs.getTimestamp ( index++ );
		if ( !rs.wasNull() ) {
			data.setEnd_date ( dt );
		}
		dt = rs.getTimestamp ( index++ );
		if ( !rs.wasNull() ) {
			data.setFirst_date_of_last_edit ( dt );
		}
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setMeas_count ( l );
		}
		d = rs.getDouble ( index++ );
		if ( !rs.wasNull() ) {
			data.setMin_val ( d );
		}
		d = rs.getDouble ( index++ );
		if ( !rs.wasNull() ) {
			data.setMax_val ( d );
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_MeasTypeStatus.
@param rs ResultSet from a MeasTypeStatus table query.
@throws Exception if an error occurs
*/
private Vector toMeasTypeStatusList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	Date d;
	long l;
	RiversideDB_MeasTypeStatus data = null;
	while ( rs.next() ) {
		data = new RiversideDB_MeasTypeStatus();
		index = 1;
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setMeasType_num ( l );
		}
		d = rs.getTimestamp ( index++ );
		if ( !rs.wasNull() ) {
			data.setStatus_date ( d );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setStatus ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setComment ( s.trim() );
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_MessageLog.
@param rs ResultSet from a MessageLog table query.
@throws Exception if an error occurs
*/
private Vector toMessageLogList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	long l;
	Date d;
	RiversideDB_MessageLog data = null;
	while ( rs.next() ) {
		data = new RiversideDB_MessageLog();
		index = 1;
		d = rs.getTimestamp(index++);
		if ( !rs.wasNull() ) {
			data.setDate_Time(d);
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setRoutine(s.trim());
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setMessage_Type(s.trim());
		}
		l = rs.getInt(index++);
		if ( !rs.wasNull() ) {
			data.setMessage_Level(l);
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setMessage(s.trim());
		}
		v.addElement(data);
	}
	return v;
}

/**
Converts a ResultSet to a Vector of RiversideDB_Operation.
@param rs ResultSet from a Operation table query.
@throws Exception if an error occurs
*/
private Vector toOperationList(ResultSet rs) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	float f;
	int i;
	String s;
	RiversideDB_Operation data = null;
	while (rs.next()) {
		data = new RiversideDB_Operation();
		index = 1;
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setOperation_num(i);
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setMeasLocGroup_num(i);
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setSequence(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setOperation_ID(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setOperation_type(s.trim());
		}
		f = rs.getFloat(index++);
		if (!rs.wasNull()) {
			data.setX(f);
		}
		f = rs.getFloat(index++);
		if (!rs.wasNull()) {
			data.setY(f);
		}
		v.addElement(data);
	}
	return v;
}

/**
Converts a ResultSet to a Vector of RiversideDB_OperationStateRelation.
@param rs ResultSet from an OperationStateRelation query.
@throws Exception if an error occurs.
*/
private Vector toOperationStateRelationList(ResultSet rs) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	int i;
	String s;
	RiversideDB_OperationStateRelation data = null;
	while (rs.next()) {
		data = new RiversideDB_OperationStateRelation();
		index = 1;
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setOperationStateRelation_num(i);
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setOperation_num(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setState_name(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setDefault_value(s.trim());
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_ProductGroup.
@param rs ResultSet from a ProductGroup table query.
@throws Exception if an error occurs
*/
private Vector toProductGroupList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	int i;
	RiversideDB_ProductGroup data = null;
	while ( rs.next() ) {
		data = new RiversideDB_ProductGroup();
		index = 1;
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setProductGroup_num(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setIdentifier(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setName(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setProductType(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setComment(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setIsEnabled(s.trim());
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setDBUser_num(i);
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setDBGroup_num(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setDBPermissions(s.trim());
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_ProductType.
@param rs ResultSet from a ProductType table query.
@throws Exception if an error occurs
*/
private Vector toProductTypeList(ResultSet rs) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	RiversideDB_ProductType data = null;
	while ( rs.next() ) {
		data = new RiversideDB_ProductType();
		index = 1;

		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setProductType(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setName(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setComment(s.trim());
		}
		v.addElement(data);
	}
	return v;
}


/**
Convert a ResultSet to a Vector for RiversideDB_Props.
@param rs ResultSet from a Props table query.
@throws Exception if an error occurs
*/
private Vector toPropsListVector ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	RiversideDB_Props data = null;
	long l=-999;
	String s;
	while ( rs.next() ) {
		data = new RiversideDB_Props();
		index = 1;
		l = rs.getLong(index++);
		if ( !rs.wasNull() ) {
			data.setProp_num(l);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setVariable(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setVal(s.trim());
		}
		l = rs.getLong(index++);
		if (!rs.wasNull()) {
			data.setSeq(l);
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setDescription(s.trim());
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a PropList for RiversideDB_Props.
@param rs ResultSet from a Props table query.
@throws Exception if an error occurs
*/
private PropList toPropsList ( ResultSet rs ) 
throws Exception {
	PropList props = new PropList("RiversideDBProps");
	int index = 1;
	String s;
	String Variable;
	String Val;
	String Description;
	// For now assume that there are no multi-record properties.  Use the
	// Contents part of the properties for the description.
	while ( rs.next() ) {
		index = 1;
		Variable = null;
		Val = "";
		Description = "";
		rs.getLong(index++);	// Prop_num, Ignored
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			Variable = s.trim();
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			Val = s.trim();
		}
		rs.getLong(index++);	// Seq, Ignored
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			Description = s.trim();
		}
		if ( Variable != null ) {
			props.set ( Variable, Description, Val );
		}
	}
	return props;
}

/**
Convert a ResultSet to a Vector of RiversideDB_RatingTable.
@param rs ResultSet from a RatingTable table query.
@throws Exception if an error occurs
*/
private Vector toRatingTableList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	long l;
	double d;
	RiversideDB_RatingTable data = null;
	while ( rs.next() ) {
		data = new RiversideDB_RatingTable();
		index = 1;
		l = rs.getLong(index++);
		if ( !rs.wasNull() ) {
			data.setRatingTable_num(l);
		}
		d = rs.getDouble(index++);
		if ( !rs.wasNull() ) {
			data.setValue1(d);
		}
		d = rs.getDouble(index++);
		if ( !rs.wasNull() ) {
			data.setValue2(d);
		}
		d = rs.getDouble(index++);
		if ( !rs.wasNull() ) {
			data.setShift1(d);
		}
		d = rs.getDouble(index++);
		if ( !rs.wasNull() ) {
			data.setShift2(d);
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_Scenario.
@param rs ResultSet from a Scenario table query.
@throws Exception if an error occurs
*/
private Vector toScenarioList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	long l;
	int i;
	RiversideDB_Scenario data = null;
	while ( rs.next() ) {
		data = new RiversideDB_Scenario();
		index = 1;
		l = rs.getLong(index++);
		if ( !rs.wasNull() ) {
			data.setScenario_num(l);
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setIdentifier(s.trim());
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setDescription(s.trim());
		}
		i = rs.getInt(index++);
		if ( !rs.wasNull() ) {
			if (isDatabaseVersionAtLeast(_VERSION_030000_20041001)){
				data.setIsActive ( i );
			}
			else {
				data.setActive( i );
			}
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_SHEFType.
@param rs ResultSet from a SHEFType table query.
@throws Exception if an error occurs
*/
private Vector toSHEFTypeList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	long l;
	RiversideDB_SHEFType data = null;
	while ( rs.next() ) {
		data = new RiversideDB_SHEFType();
		index = 1;
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setSHEF_pe(s.trim());
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setUnits_engl(s.trim());
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setUnits_si(s.trim());
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setDefault_base(s.trim());
		}
		l = rs.getLong(index++);
		if ( !rs.wasNull() ) {
			data.setDefault_mult(l);
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setTime_scale(s.trim());
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_StageDischargeRating.
@param rs ResultSet from a StageDischargeRating table query.
@throws Exception if an error occurs
*/
private Vector toStageDischargeRatingList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	long l;
	double d;
	Date dt;
	RiversideDB_StageDischargeRating data = null;
	while ( rs.next() ) {
		data = new RiversideDB_StageDischargeRating();
		index = 1;
		l = rs.getLong(index++);
		if ( !rs.wasNull() ) {
			data.setMeasLoc_num(l);
		}
		dt = rs.getTimestamp(index++);
		if ( !rs.wasNull() ) {
			data.setStart_Date(dt);
		}
		dt = rs.getTimestamp(index++);
		if ( !rs.wasNull() ) {
			data.setEnd_Date(dt);
		}
		l = rs.getLong(index++);
		if ( !rs.wasNull() ) {
			data.setRatingTable_num(l);
		}
		d = rs.getDouble(index++);
		if ( !rs.wasNull() ) {
			data.setGage_Zero_Datum(d);
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setGage_Datum_Units(s.trim());
		}
		d = rs.getDouble(index++);
		if ( !rs.wasNull() ) {
			data.setWarning_Level(d);
		}
		d = rs.getDouble(index++);
		if ( !rs.wasNull() ) {
			data.setFlood_Level(d);
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setStage_Units(s.trim());
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setDischarge_Units(s.trim());
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setInterpolation_Method(s.trim());
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_StateGroup.
@param rs ResultSet from a StateGroup table query.
@throws Exception if an error occurs
*/
private Vector toStateGroupList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	long l;
	int i;
	Date d;
	RiversideDB_StateGroup data = null;
	while ( rs.next() ) {
		data = new RiversideDB_StateGroup();
		index = 1;
		l = rs.getLong(index++);
		if ( !rs.wasNull() ) {
			data.setStateGroup_num(l);
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setScenario(s.trim());
		}
		d = rs.getTimestamp(index++);
		if ( !rs.wasNull() ) {
			data.setDate_Time(d);
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setDescription(s.trim());
		}
		i = rs.getInt(index++);
		if ( !rs.wasNull() ) {
			data.setStatus(i);
		}
		if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)){
			l = rs.getLong(index++);
			if (!rs.wasNull()) {
				data.setMeasLocGroup_num(l);
			}
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_State.
@param rs ResultSet from a Statetable query.
@throws Exception if an error occurs
*/
private Vector toStateList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	long l;
	int i;
	RiversideDB_State data = null;
	if (isDatabaseVersionAtLeast(_VERSION_030000_20041001)){
		while (rs.next()) {
			data = new RiversideDB_State();
			index = 1;
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setStateGroup_num(i);
			}
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setOperationStateRelation_num(i);
			}
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setSequence(i);
			}
			s = rs.getString(index++);
			if (!rs.wasNull()) {
				// specifically NOT trimmed in order to 
				// accomodate multi-sequence values.
				// IMPORTANT
				data.setValueStr(s);
			}
			v.add(data);
		}
	}
	else {
		while (rs.next()) {
			data = new RiversideDB_State();
			index = 1;
			s = rs.getString(index++);
			if ( !rs.wasNull() ) {
				data.setModule(s.trim());
			}
			s = rs.getString(index++);
			if ( !rs.wasNull() ) {
				data.setVariable(s.trim());
			}
			i = rs.getInt(index++);
			if ( !rs.wasNull() ) {
				data.setSeq(i);
			}
			l = rs.getLong(index++);
			if ( !rs.wasNull() ) {
				data.setStateGroup_num(l);
			}
			s = rs.getString(index++);
			if ( !rs.wasNull() ) {
				data.setVal(s.trim());
			}
			v.addElement(data);
		}
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_Station.
@param rs ResultSet from a Station table query.
@throws Exception if an error occurs
*/
private Vector toStationList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	long l;
	RiversideDB_Station data = null;
	while ( rs.next() ) {
		data = new RiversideDB_Station();
		index = 1;
		l = rs.getLong(index++);
		if ( !rs.wasNull() ) {
			data.setStation_num(l);
		}
		l = rs.getLong(index++);
		if ( !rs.wasNull() ) {
			data.setMeasLoc_num(l);
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setStation_id(s.trim());
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setStation_name(s.trim());
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setSource_abbrev(s.trim());
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setDescription(s.trim());
		}
		s = rs.getString(index++);
		if ( !rs.wasNull() ) {
			data.setPrimary_flag(s.trim());
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_TableLayout.
@param rs ResultSet from a TableLayout table query.
@throws Exception if an error occurs
*/
private Vector toTableLayoutList ( ResultSet rs ) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	String s;
	long l;
	RiversideDB_TableLayout data = null;
	while ( rs.next() ) {
		data = new RiversideDB_TableLayout();
		index = 1;
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setTableLayout_num ( l );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setIdentifier ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDescription ( s.trim() );
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_Tables.
@param rs ResultSet from a Tables table query.
@throws Exception if an error occurs
*/
private Vector toTablesList ( ResultSet rs ) 
throws Exception {
	if ( rs == null ) {
		return null;
	}
	Vector v = new Vector();
	int index = 1;
	String s;
	Date d;
	long l;
	int i;
	RiversideDB_Tables data = null;
	while ( rs.next() ) {
		data = new RiversideDB_Tables();
		index = 1;
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setTable_num ( l );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setTable_name ( s.trim() );
		}
		d = rs.getTimestamp ( index++ );
		if ( !rs.wasNull() ) {
			data.setCreated ( d );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDescription ( s.trim() );
		}
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setTableLayout_num ( l );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setArchive ( s.trim() );
		}
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setActive_days ( l );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDate_field ( s.trim() );
		}
		l = rs.getLong ( index++ );
		if ( !rs.wasNull() ) {
			data.setDate_precision ( l );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDate_table ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setDate_table_Joinfield ( s.trim() );
		}
		s = rs.getString ( index++ );
		if ( !rs.wasNull() ) {
			data.setJoinfield ( s.trim() );
		}
		if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)){
			s = rs.getString(index++);
			if (!rs.wasNull()) {
				data.setIsReference(s.trim());
			}
			s = rs.getString(index++);
			if (!rs.wasNull()) {
				data.setIsTSTemplate(s.trim());
			}
			s = rs.getString(index++);
			if (!rs.wasNull()) {
				data.setIsTS(s.trim());
			}
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setDBUser_num(i);
			}
			i = rs.getInt(index++);
			if (!rs.wasNull()) {
				data.setDBGroup_num(i);
			}
			s = rs.getString(index++);
			if (!rs.wasNull()) {
				data.setRecord_DBPermissions(s.trim());
			}
		}		
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_Tables.
@param table_format Indicate the time series table format.
@param rs ResultSet from a Tables table query.
@throws Exception if an error occurs
*/
private	Vector toTSDateValueToMinuteList ( long table_layout, ResultSet rs ) 
throws Exception {
	if ( rs == null ) {
		return null;
	}
	Vector v = new Vector();
	int index = 1;
	Date dt;
	double d;
    String s;
    int i;
	RiversideDB_TSDateValueToMinute data = null;
	while ( rs.next() ) {
		data = new RiversideDB_TSDateValueToMinute();
		index = 1;
		dt = rs.getTimestamp ( index++ );
		if ( !rs.wasNull() ) {
			data._Date_Time = dt;
		}
		d = rs.getDouble ( index++ );
		if ( !rs.wasNull() ) {
			data._Val = d;
		}
        s = rs.getString ( index++ );
        if ( !rs.wasNull() ) {
            data._Quality_flag = s;
        }
        else {
            data._Quality_flag = "";
        }
        if ( table_layout == TABLE_LAYOUT_DATE_VALUE_TO_MINUTE_WITH_DURATION ) {
            i = rs.getInt ( index++ );
            if ( !rs.wasNull() ) {
                data._Duration = i;
            }
        }
        if ( table_layout == TABLE_LAYOUT_DATE_VALUE_TO_MINUTE_CREATION ) {
            // Add the creation time.
            dt = rs.getTimestamp ( index++ );
            if ( !rs.wasNull() ) {
                data._Date_Time = dt;
            }
        }
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_TSProduct.
@param rs ResultSet from a TSProduct table query.
@throws Exception if an error occurs
*/
private Vector toTSProductList ( ResultSet rs ) 
throws Exception {
	if ( rs == null ) {
		return null;
	}
	Vector v = new Vector();
	int index = 1;
	String s;
	int i;
	RiversideDB_TSProduct data = null;
	while ( rs.next() ) {
		data = new RiversideDB_TSProduct();
		index = 1;
		i = rs.getInt( index++ );
		if ( !rs.wasNull() ) {
			data.setTSProduct_num(i);
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setProductGroup_num(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setIdentifier(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setName(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setComment(s.trim());
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setDBUser_num(i);
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setDBGroup_num(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setDBPermissions(s.trim());
		}
		v.addElement(data);
	}
	return v;
}

/**
Convert a ResultSet to a Vector of RiversideDB_TSProductProps.
@param rs ResultSet from a TSProductProps table query.
@throws Exception if an error occurs
*/
private Vector toTSProductPropsList ( ResultSet rs ) 
throws Exception {
	if ( rs == null ) {
		return null;
	}
	Vector v = new Vector();
	int index = 1;
	String s;
	int i;
	RiversideDB_TSProductProps data = null;
	while ( rs.next() ) {
		data = new RiversideDB_TSProductProps();
		index = 1;
		i = rs.getInt( index++ );
		if ( !rs.wasNull() ) {
			data.setTSProduct_num(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setProperty(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			if (isDatabaseVersionAtLeast(_VERSION_030000_20041001)){
				data.setVal(s.trim());
			} 
			else {
				data.setValue(s.trim());
			}
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setSequence(i);
		}
		v.addElement(data);
	}
	return v;
}

// U FUNCTIONS
// V FUNCTIONS
// W FUNCTIONS

/**
Writes a record to the Area table
@param r an object of type RiversideDB_Area to write to the table.  If the 
Area_num value in the object is Missing, the DMI will attempt to insert a new
record into the table.  Otherwise, it tries to update the existing record.
@throws Exception if an error occurs
*/
public void writeArea(RiversideDB_Area r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_AREA);
	w.addValue(r.getMeasLoc_num());
	w.addValue(r.getArea_id());
	w.addValue(r.getArea_name());
	w.addValue(r.getArea());
	w.addValue(r.getArea_units());

	if (!DMIUtil.isMissing(r.getArea_num())) {
		w.addWhereClause(
			"Area_num = " + r.getArea_num());
		dmiWrite(w, DMI.UPDATE);
	}
	else {
		dmiWrite(w, DMI.INSERT);
	}
}

/**
Writes a record to the DataDimension table by first attempting to update an
existing record, and if none exists, a new record is inserted.
@param r an object of type RiversideDB_DataDimension to write to the table
@throws Exception if an error occurs
*/
public void writeDataDimension(RiversideDB_DataDimension r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);
	
	buildSQL(w, _W_DATADIMENSION);
	w.addValue(r.getDimension());
	w.addValue(r.getDescription());

	w.addWhereClause("Dimension = '" + escape(r.getDimension()) + "'");

	dmiWrite(w, DMI.UPDATE_INSERT);
}

/**
Writes a record to the DataSource table by first attempting to update an 
existing record, and if none exists, a new one is inserted.
@param r an object of type RiversideDB_DataSource to write to the table
@throws Exception if an error occurs
*/
public void writeDataSource(RiversideDB_DataSource r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_DATASOURCE);
	w.addValue(r.getSource_abbrev());
	w.addValue(r.getSource_name());

	w.addWhereClause("Source_abbrev = '" 
		+ escape(r.getSource_abbrev()) + "'");

	dmiWrite(w, DMI.UPDATE_INSERT);	
}

/**
Writes data test data.  From AlertIOInterface.
@param dataModel the DataTestDataModel containing the data to be written.
@return the ID num of the data test that was written.
@throws Exception if an error occurs.
*/
public int writeDataTestDataModel(DataTestDataModel dataModel) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	boolean insert = false;
	
	if (DMIUtil.isMissing(dataModel.getDataTestNum())) {
		insert = true;
	}

	buildSQL(w, _W_DATATEST);
	w.addValueOrNull(dataModel.getDescription());
	w.addValueOrNull(dataModel.getDataTestGroupNum());
	if (dataModel.isActive()) {
		w.addValue(1);
	}
	else {
		w.addValue(0);
	}
	w.addValueOrNull(dataModel.getActiveSeasonStartString());
	w.addValueOrNull(dataModel.getActiveSeasonEndString());
	w.addValueOrNull(dataModel.getActiveSeasonFormatString());
	w.addValue(dataModel.getDataTestExpressionNum());
	w.addValue(dataModel.getTestBaseTimeString());
	w.addValue(dataModel.getTestBaseTimeFormatString());
	w.addValue(dataModel.getTestIntervalString());
	w.addValueOrNull(dataModel.getPositiveTestIntervalString());
	w.addValueOrNull(dataModel.getEvaluationIntervalString());
	w.addValue(dataModel.getEvaluationWindowString());
	w.addValueOrNull(dataModel.getMessage());
	w.addValueOrNull(dataModel.getPropertiesString());
	w.addValueOrNull(dataModel.getPositiveCount());

	if (insert) {
//		Message.printStatus(1, "", "" + w.toInsertString() + "\n\n\n");
		dmiWrite(w, DMI.INSERT);
		return (int)getMaxRecord("DataTest", "DataTestNum");
	}
	else {
		w.addWhereClause("DataTestNum = " + dataModel.getDataTestNum());
//		Message.printStatus(1, "", "" + w.toUpdateString() + "\n\n\n");
		dmiWrite(w, DMI.UPDATE);
		return dataModel.getDataTestNum();
	}
}

/**
Writes expression data.  From AlertIOInterface.
@param dataModel the DataTestExpressionDataModel containing the data to be
written.
@return the ID num of the data test expression that was written.
@throws Exception if an error occurs.
*/
public int writeDataTestExpressionDataModel(
DataTestExpressionDataModel dataModel) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	boolean insert = false;
	
	if (DMIUtil.isMissing(dataModel.getExpressionNum())) {
		insert = true;
	}

	buildSQL(w, _W_DATATESTEXPRESSION);
	w.addValue(dataModel.getOperator());
	w.addValue(DataTestSide.getSideName(dataModel.getLeftSideType()));
	w.addValue(dataModel.getLeftSideIDNum());
	w.addValue(DataTestSide.getSideName(dataModel.getRightSideType()));
	w.addValue(dataModel.getRightSideIDNum());
	w.addValueOrNull(dataModel.getPropertiesString());

	if (insert) {
		dmiWrite(w, DMI.INSERT);
//		Message.printStatus(1, "", "" + w.toInsertString() + "\n\n\n");
		return (int)getMaxRecord("DataTestExpression", 
			"DataTestExpressionNum");
	}
	else {
		w.addWhereClause("DataTestExpressionNum = " 
			+ dataModel.getExpressionNum());
		dmiWrite(w, DMI.UPDATE);
//		Message.printStatus(1, "", "" + w.toUpdateString() + "\n\n\n");
		return dataModel.getExpressionNum();
	}
}

/**
Writes expression data.  From AlertIOInterface.
@param dataModel the DataTestFunctionDataModel containing the data to be
written.
@return the ID num of the data test function that was written.
@throws Exception if an error occurs.
*/
public int writeDataTestFunctionDataModel(
DataTestFunctionDataModel dataModel) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	boolean insert = false;
	
	if (DMIUtil.isMissing(dataModel.getFunctionNum())) {
		insert = true;
	}

	buildSQL(w, _W_DATATESTFUNCTION);
	w.addValue(dataModel.getFunctionName());
	w.addValueOrNull(dataModel.getPropertiesString());
	w.addValueOrNull(dataModel.getMessage());

	int num = -1;

	if (insert) {
//		Message.printStatus(1, "", "" + w.toInsertString() + "\n\n\n");
		dmiWrite(w, DMI.INSERT);
		num = (int)getMaxRecord("DataTestFunction", 
			"DataTestFunctionNum");
	}
	else {
		w.addWhereClause("DataTestFunctionNum = " 
			+ dataModel.getFunctionNum());
//		Message.printStatus(1, "", "" + w.toUpdateString() + "\n\n\n");
		dmiWrite(w, DMI.UPDATE);
		num = dataModel.getFunctionNum();
	}

	// Clear out all the existing InputDataIDs.  This makes it easier
	// to write the InputDataIDs -- all are inserts, rather than figuring
	// out which are updates.
	String sql = "DELETE FROM DataTestFunctionInputIDs WHERE "
		+ "DataTestFunctionInputIDs.DataTestFunctionNum = " 
		+ num;
	dmiDelete(sql);

	String[] ids = dataModel.getInputDataIDs();

	if (ids != null) {
		for (int i = 0; i < ids.length; i++) {
			writeDataTestFunctionInputID(num, (i + 1), ids[i]);
		}
	}
	else {
	}

	return num;
}

/**
Writes an input ID for a data test function.  
@param functionNum the function ID num.
@param position the position (base-1) of the input ID in the function.
@param id the input ID to write.
@throws Exception if an error occurs.
*/
public void writeDataTestFunctionInputID(int functionNum, int position,
String id) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_DATATESTFUNCTIONINPUTID);
	w.addValue(functionNum);
	w.addValue(position);
	w.addValue(id);

	dmiWrite(w, DMI.INSERT);
//	Message.printStatus(1, "", "" + w.toInsertString() + "\n\n\n");
}

/**
Writes the given result to the database.  From AlertIOInterface.
@throws Exception if an error occurs.
*/
public int writeDataTestResult(DataTestResult result)
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_DATATESTRESULT);
	w.addValue(result.getDataTestNum());
	w.addValue(result.getTestTimeFormat().format(result.getTestTime()));
	w.addValueOrNull(result.getLeftSideValue());
	w.addValueOrNull(result.getRightSideValue());
	w.addValueOrNull(result.getDataTestExpressionNum());
	w.addValueOrNull(result.getLevel());
	w.addValueOrNull(result.getMessage());

	dmiDelete("DELETE FROM DataTestResults WHERE DataTestNum = "
		+ result.getDataTestNum() + " AND TestTime = '"
		+ result.getTestTimeFormat().format(result.getTestTime()) 
		+ "'");

	dmiWrite(w, DMI.INSERT);
//	Message.printStatus(1, "", "" + w.toInsertString() + "\n\n\n");
	return 1;
}

/**
Writes a record to the DataType table by first attempting to update an 
existing record, and if none exists, a new one is inserted.
@param r an object of type RiversideDB_DataType to write to the table
@throws Exception if an error occurs
*/
public void writeDataType(RiversideDB_DataType r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_DATATYPE);
	w.addValue(r.getDataType());
	w.addValue(r.getDescription());
	w.addValue(r.getDimension());
	w.addValue(r.getMeas_time_scale());
	w.addValue(r.getMeas_loc_type());
	w.addValue(r.getRecord_type());
	w.addValue(r.getDefault_engl_units());
	w.addValue(r.getDefault_si_units());
	String s = r.getSHEF_pe();
	if (s.trim().equals("")) {
		w.addNullValue();
	}
	else {
		w.addValue(r.getSHEF_pe());
	}
	w.addValue(r.getDefault_engl_min());
	w.addValue(r.getDefault_engl_max());
	w.addValue(r.getDefault_si_min());
	w.addValue(r.getDefault_si_max());	

	w.addWhereClause("DataType = '" + escape(r.getDataType()) + "'");

//Message.printStatus(1, "", "" + w.toUpdateString());
//Message.printStatus(1, "", "" + w.toInsertString());
	dmiWrite(w, DMI.UPDATE_INSERT);
}

/**
Writes a record to the DataUnits table by first attempting to update an 
existing record, and if none exists, a new one is inserted.
@param r an object of type RiversideDB_DataUnits to write to the table
@throws Exception if an error occurs
*/
public void writeDataUnits(RiversideDB_DataUnits r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_DATAUNITS);

	w.addValue(r.getUnits_abbrev());
	w.addValue(r.getUnits_description());
	w.addValue(r.getOutput_precision());
	w.addValue(r.getDimension());
	w.addValue(r.getBase_unit());
	w.addValue(r.getMult_factor());
	w.addValue(r.getAdd_factor());
	w.addValue(r.getUnits_system());

	w.addWhereClause("Units_abbrev = '" 
		+ escape(r.getUnits_abbrev()) + "'");
	
	dmiWrite(w, DMI.UPDATE_INSERT);
}

/**
REVISIT (JTS - 2003-06-26)
Writes a vector of RiversideDB_DBUserMeasLocGroupRelation objects to the 
database.
@param v a Vector of RiversideDB_DBUserMeasLocGroupRelation objects to be
written
@throws Exception if an error occurs.
*/
public void writeDBUserMeasLocGroupRelation(Vector v) 
throws Exception {
	if (v == null) {
		return;
	}

	int size = v.size();
	if (size == 0) {
		return;
	}
	
	RiversideDB_DBUserMeasLocGroupRelation r = 
		(RiversideDB_DBUserMeasLocGroupRelation)v.elementAt(0);
	int firstMLG = r.getMeasLocGroup_num();
	
	int currMLG = -1;

	String error = "";
	boolean abort = false;

	for (int i = 1; i < size; i++) {	
		r = (RiversideDB_DBUserMeasLocGroupRelation)v.elementAt(i);
		currMLG = r.getMeasLocGroup_num();
		if (currMLG != firstMLG) {
			abort = true;
			error += "[" + i + "] ";
		}
	}

	if (abort) {
		throw new Exception ("Not all records in the Vector had the "
			+ "same MeasLocGroup_num as the first element in the "
			+ "Vector.  Records " + error + "had different "
			+ "MeasLocGroup_nums.");
	}

	deleteDBUserMeasLocGroupRelationForMeasLocGroup_num(firstMLG);	

	for (int i = 0; i < size; i++) {	
		r = (RiversideDB_DBUserMeasLocGroupRelation)v.elementAt(i);
		writeDBUserMeasLocGroupRelation(r);
	}
}

/**
Writes a RiversideDB_DBUserMeasLocGroupRelation object to the database.
@param r a RiversideDB_DBUserMeasLocGroupRelation object with the data to
write.
@throws Exception if an error occurs.
*/
public void writeDBUserMeasLocGroupRelation(
RiversideDB_DBUserMeasLocGroupRelation r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_DBUSERMEASLOCGROUPRELATION);
	
	w.addValue(r.getDBUser_num());
	w.addValue(r.getMeasLocGroup_num());
	
	dmiWrite(w, DMI.INSERT);
}

/**
Writes a record to the ExportConf table by first attempting to update an 
existing record, and if none exists, a new one is inserted.
@param r an object of type RiversideDB_ExportConf to write to the table
@throws Exception if an error occurs
*/
public void writeExportConf(RiversideDB_ExportConf r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_EXPORTCONF);
	w.addValue(r.getExportProduct_num());
	w.addValue(r.getMeasType_num());
	w.addValue(r.getExport_id());
	w.addValueOrNull(r.getExport_units());
	w.addValueOrNull(r.getIsActive());

	w.addWhereClause("ExportProduct_num = " + r.getExportProduct_num());
	w.addWhereClause("MeasType_num = " + r.getMeasType_num());

	dmiWrite(w, DMI.UPDATE_INSERT);
}

/**
Writes a record to the ExportProduct table.  
@param r an object of type RiversideDB_ExportProduct to write to the table.
If the ExportProduct_num of the object is missing, a new record will be 
inserted in the table.  Otherwise, an existing record will be updated.
@return a RiversideDB_ExportProduct object when a new record is inserted
into the ExportProduct table.  The ExportProduct_num value in this object
is the newest autonumber ExportProduct_num entered.  Otherwise, it returns
null
@throws Exception if an error occurs
*/
public RiversideDB_ExportProduct writeExportProduct(RiversideDB_ExportProduct r)
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_EXPORTPRODUCT);
	w.addValue(r.getProduct_name());
	w.addValue(r.getProduct_type());
	w.addValue(r.getIsActive());
// REVISIT (JTS - 2003-06-02)
// This will be phased out later			
//	if (getDatabaseVersion() < _VERSION_020800_20030422) {	
		w.addValue(r.getProduct_group());
//	}
	w.addValue(r.getExport_order());
	w.addValue(r.getLast_export_date(), DateTime.PRECISION_MINUTE);
	w.addValue(r.getNext_export_date(), DateTime.PRECISION_MINUTE);
	w.addValue(r.getRetries());
	w.addValue(r.getUser_login());
	w.addValue(r.getUser_PWD());
	w.addValue(r.getFirewall_user());
	w.addValue(r.getFirewall_user_PWD());
	w.addValue(r.getExport_start());
	w.addValue(r.getExport_end());
	w.addValue(r.getDestination_dir());
	w.addValue(r.getDestination_file());
	w.addValue(r.getProperties());
	w.addValue(r.getIsAutomated());
	w.addValue(r.getIsInterval());
	w.addValue(r.getExport_year());
	w.addValue(r.getExport_month());
	w.addValue(r.getExport_day());
	w.addValue(r.getExport_hour());
	w.addValue(r.getExport_minute());
	w.addValue(r.getExport_second());
	w.addValue(r.getExport_weekday());
	if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)) {
		w.addValueOrNull(r.getTSProduct_num());
		w.addValue(r.getProductGroup_num());
		w.addValue(r.getDBUser_num());
		w.addValue(r.getDBGroup_num());
		w.addValue(r.getDBPermissions());
		w.addValue(r.getMeasLocGroup_num());
	}				

	if (!DMIUtil.isMissing(r.getExportProduct_num())) {
		w.addWhereClause(
			"ExportProduct_num = " + r.getExportProduct_num());
		dmiWrite(w, DMI.UPDATE);
	}
	else {
		dmiWrite(w, DMI.INSERT);
		// on an insert, the number of the autonumber inserted needs
		// to be returned, so execute a max() on the ExportProduct_num
		// field to get the highest value (the last one inserted).  

		String sql = "SELECT MAX(ExportProduct_num) from " +
			"ExportProduct";
		ResultSet rs = dmiSelect(sql);
		int ExportProduct_num;
		if (rs.next()) {
			ExportProduct_num = rs.getInt(1);
		}
		else {
			return null;
		}
		closeResultSet(rs);
		r.setExportProduct_num(ExportProduct_num);
		return r;
		
	}
	return null;
}

/**
Writes a record to the ExportType table by first attempting to update an
existing record, and if none exists, inserted a new one.
@param r an object of type RiversideDB_ExportType to write to the table
@throws Exception if an error occurs
*/
public void writeExportType(RiversideDB_ExportType r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_EXPORTTYPE);
	w.addValue(r.getName());
	w.addValue(r.getComment());

	w.addWhereClause("Name = '" + escape(r.getName()) + "'");

	dmiWrite(w, DMI.UPDATE_INSERT);
}

/**
Writes a record to the Geoloc table.
@param r an object of type RiversideDB_Geoloc to write to the table.  If the
value of the objects Geoloc_num is missing, a new record is inserted.  Otherwise
a record will be added.
@return a RiversideDB_Geoloc object when a new record is inserted
into the Geoloc table.  The Geoloc_num value in this object
is the newest autonumber Geoloc_num entered.  Otherwise, it returns
null 
@throws Exception if an error occurs
*/
public RiversideDB_Geoloc writeGeoloc(RiversideDB_Geoloc r) throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_GEOLOC);
	w.addValue(r.getLatitude());
	w.addValue(r.getLongitude());
	w.addValue(r.getX());
	w.addValue(r.getY());
	w.addValue(r.getCountry());
	w.addValue(r.getState());
	w.addValue(r.getCounty());
	w.addValue(r.getElevation());
	w.addValue(r.getElevation_units());

	if (!DMIUtil.isMissing(r.getGeoloc_num())) {
		w.addWhereClause(
			"Geoloc_num = " + r.getGeoloc_num());
		dmiWrite(w, DMI.UPDATE);
		return null;
	}
	else {
		dmiWrite(w, DMI.INSERT);		

		// on an insert, the number of the autonumber inserted needs
		// to be returned, so execute a max() on the geoloc_num field
		// to get the highest value (the last one inserted).  

		String sql = "SELECT MAX(Geoloc_num) from Geoloc";
		ResultSet rs = dmiSelect(sql);
		long geoloc_num;
		if (rs.next()) {
			geoloc_num = rs.getLong(1);
		}
		else {
			Message.printWarning(1, "writeGeoloc", "Error getting "
				+ "new geoloc_num from database.");
			closeResultSet(rs);
			return r;
		}
		closeResultSet(rs);
		r.setGeoloc_num(geoloc_num);
		return r;
	}
}

/**
Writes a record to the ImportConf table by first attempting to update an 
existing record, and if none exists, inserted a new one.
@param r an object of type RiversideDB_ImportConf to write to the table
@throws Exception if an error occurs
*/
public void writeImportConf(RiversideDB_ImportConf r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_IMPORTCONF);
	w.addValue(r.getImportProduct_num());
	w.addValue(r.getMeasType_num());
	w.addValue(r.getExternal_table());
	w.addValue(r.getExternal_field());
	w.addValue(r.getExternal_id());
	w.addValueOrNull(r.getExternal_units());
	w.addValue(r.getIsActive());

	w.addWhereClause("ImportProduct_num = " + r.getImportProduct_num());
	w.addWhereClause("MeasType_num = " + r.getMeasType_num());

	dmiWrite(w, DMI.UPDATE_INSERT);
}

/**
Writes a record to the ImportProduct table
@param r an object of type RiversideDB_ImportProduct to write to the table.
If the Object's ImportProduct_num is missing, a new record will be inserted
in the table.  Otherwise, the existing one will be updated.
@return a RiversideDB_ImportProduct object when a new record is inserted
into the ImportProduct table.  The ImportProduct_num value in this object
is the newest autonumber ImportProduct_num entered.  Otherwise, it returns
null
@throws Exception if an error occurs
*/
public RiversideDB_ImportProduct writeImportProduct(RiversideDB_ImportProduct r)
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_IMPORTPRODUCT);
	w.addValue(r.getProduct_name());
	w.addValue(r.getProduct_type());
	w.addValue(r.getIsActive());
// REVISIT (JTS - 2003-06-02)
// This will be phased out later			
//	if (getDatabaseVersion() < _VERSION_020800_20030422) {	
		w.addValue(r.getProduct_group());
//	}
	w.addValue(r.getImport_order());
	w.addValue(r.getLast_import_date(), DateTime.PRECISION_MINUTE);
	w.addValue(r.getNext_import_date(), DateTime.PRECISION_MINUTE);
	w.addValue(r.getRetries());
	w.addValue(r.getUser_login());
	w.addValue(r.getUser_PWD());
	w.addValue(r.getFirewall_user());
	w.addValue(r.getFirewall_user_PWD());
	w.addValue(r.getSource_URL_base());
	w.addValue(r.getSource_URL_file());
	w.addValue(r.getImport_window());
	w.addValue(r.getAdd_source_URL_base());
	w.addValue(r.getAdd_source_URL_file());
	w.addValue(r.getDestination_dir());
	w.addValue(r.getDestination_file());
	w.addValue(r.getProperties());
	w.addValue(r.getIsAutomated());
	w.addValue(r.getIsInterval());
	w.addValue(r.getImport_year());
	w.addValue(r.getImport_month());
	w.addValue(r.getImport_day());
	w.addValue(r.getImport_hour());
	w.addValue(r.getImport_minute());
	w.addValue(r.getImport_second());
	w.addValue(r.getImport_weekday());
	w.addValue(r.getImport_delay());
	w.addValue(r.getDoArchive());
	w.addValue(r.getArchive_dir());
	w.addValue(r.getArchive_file());
	if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)) {
		w.addValue(r.getProductGroup_num());
		w.addValue(r.getDBUser_num());
		w.addValue(r.getDBGroup_num());
		w.addValue(r.getDBPermissions());
		w.addValue(r.getMeasLocGroup_num());
	}
	if (!DMIUtil.isMissing(r.getImportProduct_num())) {
		w.addWhereClause(
			"ImportProduct_num = " + r.getImportProduct_num());
		dmiWrite(w, DMI.UPDATE);
	}
	else { 
		dmiWrite(w, DMI.INSERT);

		// on an insert, the number of the autonumber inserted needs
		// to be returned, so execute a max() on the ImportProduct_num
		// field to get the highest value (the last one inserted).  

		String sql = "SELECT MAX(ImportProduct_num) from " +
			"ImportProduct";
		ResultSet rs = dmiSelect(sql);
		int ImportProduct_num;
		if (rs.next()) {
			ImportProduct_num = rs.getInt(1);
		}
		else {
			return null;
		}
		closeResultSet(rs);
		r.setImportProduct_num(ImportProduct_num);
		return r;
	}
	return null;
}

/**
Writes a record to the ImportType table by first attempting to update an 
existing record, and if none exists, inserting a new one.
@param r an object of type RiversideDB_ImportType to write to the table
@throws Exception if an error occurs
*/
public void writeImportType(RiversideDB_ImportType r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_IMPORTTYPE);
	w.addValue(r.getName());
	w.addValue(r.getComment());

	w.addWhereClause("Name = '" + escape(r.getName()) + "'");

	dmiWrite(w, DMI.UPDATE_INSERT);
}

/**
Writes a record to the MeasCreateMethod table by first attempting to update
an existing record, and if none exists, inserting a new one.
@param r an object of type RiversideDB_MeasCreateMethod to write to the table
@throws Exception if an error occurs
*/
public void writeMeasCreateMethod(RiversideDB_MeasCreateMethod r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_MEASCREATEMETHOD);
	w.addValue(r.getMethod());
	w.addValue(r.getDescription());

	w.addWhereClause("Method = '" + escape(r.getMethod()) + "'");

	dmiWrite(w, DMI.UPDATE_INSERT);
}


/**
Writes a record to the MeasLoc table.
@param r an object of type RiversideDB_MeasLoc to write to the table
If the Object's MeasLoc_num is missing, a new record will be inserted in the
table.  Otherwise, an existing record will be updated.
@throws Exception if an error occurs
*/
public RiversideDB_MeasLoc writeMeasLoc(RiversideDB_MeasLoc r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_MEASLOC);
	w.addValue(r.getGeoloc_num());
	w.addValue(r.getIdentifier());
	w.addValue(r.getMeasLoc_name());
	w.addValue(r.getSource_abbrev());
	w.addValue(r.getMeas_loc_type());
	w.addValue(r.getComment());
	if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)) {	
		w.addValue(r.getDBUser_num());
		w.addValue(r.getDBGroup_num());
		w.addValue(r.getDBPermissions());
		w.addValueOrNull(r.getMeasLocGroup_num());
	}

	boolean bolReadBack = false;
	if (!DMIUtil.isMissing(r.getMeasLoc_num())) {
		w.addWhereClause(
			"MeasLoc_num = " + r.getMeasLoc_num());
		dmiWrite(w, DMI.UPDATE);
	}
	else { 
		dmiWrite(w, DMI.INSERT);
		bolReadBack = true;
	}
	if ( bolReadBack ) { //we need to read in newly written MeasLoc since
		// on an insert, the number of the autonumber inserted needs
		// to be returned, so execute a max() on the MeasLoc_num 
		// field to get the highest value (the last one inserted).  
		String sql = "SELECT MAX(MeasLoc_num) from MeasLoc";
		ResultSet rs = dmiSelect(sql);
		int ml_num = DMIUtil.MISSING_INT;
		if (rs.next()) {
			ml_num = rs.getInt(1);
		}
		else {
			return null;
		}
		closeResultSet(rs);
		r.setMeasLoc_num(ml_num);

		return r;
	}
	return null;
}


/**
Writes a record to the MeasLoc table.
@param r an object of type RiversideDB_MeasLoc to write to the table
If the Object's MeasLoc_num is missing, a new record will be inserted in the
table.  Otherwise, an existing record will be updated.
@throws Exception if an error occurs
*/
/*
public void writeMeasLoc_OLD(RiversideDB_MeasLoc r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_MEASLOC);
	w.addValue(r.getGeoloc_num());
	w.addValue(r.getIdentifier());
	w.addValue(r.getMeasLoc_name());
	w.addValue(r.getSource_abbrev());
	w.addValue(r.getMeas_loc_type());
	w.addValue(r.getComment());
	if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)) {	
		w.addValue(r.getDBUser_num());
		w.addValue(r.getDBGroup_num());
		w.addValue(r.getDBPermissions());
		w.addValueOrNull(r.getMeasLocGroup_num());
	}

	if (!DMIUtil.isMissing(r.getMeasLoc_num())) {
		w.addWhereClause(
			"MeasLoc_num = " + r.getMeasLoc_num());
		dmiWrite(w, DMI.UPDATE);
	}
	else { 
		dmiWrite(w, DMI.INSERT);
	}
}//old
*/


/**
Writes a record to the MeasLocGroup table by first attempting to update an
existing record, and if none exist, inserting a new one.  At the same time,
it also adds a new record to the DBUserMeasLocGroupRelation table for the
currently logged-in user and the MeasLocGroup record being written, but
<b>only</b> if the other record was first successfully written to
MeasLocGroup.
@param r an object of type RiversideDB_MeasLocGroup to write to the table.
@return null if an existing record was updated.  Otherwise it returns a 
new RiversideDB_MeasLocGroup object with the new MeasLocGroup_num (from the 
new record) in it.
@throws Exception if an error occurs.
*/
public RiversideDB_MeasLocGroup writeMeasLocGroup(RiversideDB_MeasLocGroup r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_MEASLOCGROUP);
	
	int MeasLocGroup_num = r.getMeasLocGroup_num();
	
	int operation = -1;
	
	if (DMIUtil.isMissing(MeasLocGroup_num)) {
		operation = DMI.INSERT;
	}
	else {
		operation = DMI.UPDATE;
		w.addWhereClause("MeasLocGroup.MeasLocGroup_num = " 
			+ MeasLocGroup_num);
	}
	w.addValue(r.getIdentifier());
	w.addValue(r.getName());
	w.addValue(r.getDescription());
	w.addValue(r.getOptable());
	w.addValue(r.getDBUser_num());
	w.addValue(r.getDBGroup_num());
	w.addValue(r.getDBPermissions());

	dmiWrite(w, operation);

	if (operation == DMI.INSERT) {
		// on an insert, the number of the autonumber inserted needs
		// to be returned, so execute a max() on the MeasLocGroup_num 
		// field to get the highest value (the last one inserted).  

		String sql = "SELECT MAX(MeasLocGroup_num) from " +
			"MeasLocGroup";
		ResultSet rs = dmiSelect(sql);
		int group_num = DMIUtil.MISSING_INT;
		if (rs.next()) {
			group_num = rs.getInt(1);
		}
		else {
			return null;
		}
		closeResultSet(rs);
		r.setMeasLocGroup_num(group_num);

		return r;
	}

	return null;
}

/**
Writes a record to the MeasLocType table by first attempting to update
an existing record, and if none exists, inserting a new one.
@param r an object of type RiversideDB_MeasLocType to write to the table
@throws Exception if an error occurs
*/
public void writeMeasLocType(RiversideDB_MeasLocType r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_MEASLOCTYPE);
	w.addValue(r.getType());
	w.addValue(r.getDescription());

	w.addWhereClause("Type = '" + escape(r.getType()) + "'");
	
	dmiWrite(w, DMI.UPDATE_INSERT);
}

/**
Writes a record to the MeasQualityFlag table by first attempting to update
an existing record, and if none exists, inserting a new one.
@param r an object of type RiversideDB_MeasQualityFlag to write to the table
@throws Exception if an error occurs
*/
public void writeMeasQualityFlag(RiversideDB_MeasQualityFlag r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_MEASQUALITYFLAG);
	w.addValue(r.getQuality_flag());
	w.addValue(r.getDescription());

	w.addWhereClause("Quality_flag = '" 
		+ escape(r.getQuality_flag()) + "'");

	dmiWrite(w, DMI.UPDATE_INSERT);	
}

/**
Writes a record to the MeasReducGridWeight table by first attempting to update
an existing record, and if none exists, inserting a new one.
@param r an object of type RiversideDB_MEasReducGridweight to write 
to the table
@throws Exception if an error occurs
*/
public void writeMeasReducGridWeight(RiversideDB_MeasReducGridWeight r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_MEASREDUCGRIDWEIGHT);
	w.addValue(r.getOutputMeasType_num());
	w.addValue(r.getInputMeasType_num());
	w.addValue(r.getInput_Row());
	w.addValue(r.getInput_Column());
	w.addValue(r.getArea());
	w.addValue(r.getArea_Fraction());
	w.addValue(r.getWeight());

	w.addWhereClause("OutputMeasType_num = " + r.getOutputMeasType_num());
	w.addWhereClause("InputMeasType_num = " + r.getInputMeasType_num());
	w.addWhereClause("Input_Row = " + r.getInput_Row());
	w.addWhereClause("Input_Column = " + r.getInput_Column());

	dmiWrite(w, DMI.UPDATE_INSERT);	
}

/**
Writes a record to the MeasReducRelation table by first attempting to update
an existing record, and if none exists, inserting a new one.
@param r an object of type RiversideDB_MeasReducRelation 
to write to the table
@throws Exception if an error occurs
*/
public void writeMeasReducRelation(RiversideDB_MeasReducRelation r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_MEASREDUCRELATION);
	w.addValue(r.getOutputMeasType_num());
	w.addValue(r.getInputMeasType_num());
	w.addValue(r.getWeight());

	w.addWhereClause("OutputMeasType_num = " + r.getOutputMeasType_num());
	w.addWhereClause("InputMeasType_num = " + r.getInputMeasType_num());
	
	dmiWrite(w, DMI.UPDATE_INSERT);
}

/**
Writes a record to the MeasReduction table by first attempting to update
an existing record, and if none exists, inserting a new one.
@param r an object of type RiversideDB_MeasReduction to write to the table
@throws Exception if an error occurs
*/
public void writeMeasReduction(RiversideDB_MeasReduction r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	// first check to see if any other meas reductions already have the
	// output meas type num in the object above ...
	RiversideDB_MeasReduction mr = readMeasReductionForOutputMeasType_num(
		r.getOutputMeasType_num());

	// if none have that output meas type num, then a new record needs to 
	// be inserted
	if (mr == null) {
		buildSQL(w, _W_MEASREDUCTION);
		w.addValue(r.getOutputMeasType_num());
		w.addValue(r.getMethod());
		w.addValue(r.getCreate_order());
		w.addValue(r.getProperties());
		if ( isDatabaseVersionAtLeast(_VERSION_030000_20041001) ) {
			w.addValue(r.getIsActive());
		}
		else {
			w.addValue(r.getActive());
		}
		if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)) {	
			w.addValue(r.getDBUser_num());
			w.addValue(r.getDBGroup_num());
			w.addValue(r.getDBPermissions());
		}	

		dmiWrite(w, DMI.INSERT);	
	}
	// otherwise, update the existing record, but don't try to change 
	// the output meas type num
	else {
		buildSQL(w, _W_MEASREDUCTION_UPDATE);
		w.addValue(r.getMethod());
		w.addValue(r.getCreate_order());
		w.addValue(r.getProperties());
		if ( isDatabaseVersionAtLeast(_VERSION_030000_20041001) ) {
			w.addValue(r.getIsActive());
		}
		else {
			w.addValue(r.getActive());
		}
		if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)) {	
			w.addValue(r.getDBUser_num());
			w.addValue(r.getDBGroup_num());
			w.addValue(r.getDBPermissions());
		}	

		w.addWhereClause("OutputMeasType_num = " 
			+ r.getOutputMeasType_num());

		dmiWrite(w, DMI.UPDATE);	
	}
}

/**
Writes a record to the MeasReductionType table by first attempting to update
an existing record, and if none exists, inserting a new one.
@param r an object of type RiversideDB_MeasReductionType 
to write to the table
@throws Exception if an error occurs
*/
public void writeMeasReductionType(RiversideDB_MeasReductionType r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_MEASREDUCTIONTYPE);
	w.addValue(r.getType());
	w.addValue(r.getDescription());

	w.addWhereClause("Type = '" + escape(r.getType()) + "'");
	
	dmiWrite(w, DMI.UPDATE_INSERT);
}

/**
Writes a record to the MeasScenarion table by first attempting to update
an existing record, and if none exists, inserting a new one.
@param r an object of type RiversideDB_MeasScenario to write to the table
@throws Exception if an error occurs
*/
public void writeMeasScenario(RiversideDB_MeasScenario r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_MEASSCENARIO);
	w.addValue(r.getObsMeasType_num());
	w.addValue(r.getMethod());
	w.addValue(r.getCreate_order());
	w.addValue(r.getProperties());
	if ( isDatabaseVersionAtLeast(_VERSION_030000_20041001) ) {
		w.addValue(r.getIsActive());
	}
	else {
		w.addValue(r.getActive());
	}
	w.addWhereClause("ObsMeasType_num = " + r.getObsMeasType_num());

	dmiWrite(w, DMI.UPDATE_INSERT);
}

/**
Writes a record to the MeasScenarioRelation table by first attempting to update
an existing record, and if none exists, inserting a new one.
@param r an object of type RiversideDB_MeasScenarioRelation
to write to the table
@throws Exception if an error occurs
*/
public void writeMeasScenarioRelation(RiversideDB_MeasScenarioRelation r)
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_MEASSCENARIORELATION);
	w.addValue(r.getObsMeasType_num());
	w.addValue(r.getQFMeasType_num());
	w.addValue(r.getWeight());
	w.addValue(r.getScenarioMeasType_num());

	w.addWhereClause("ObsMeasType_num = " + r.getObsMeasType_num());
	w.addWhereClause("QFMeasType_num = " + r.getQFMeasType_num());

	dmiWrite(w, DMI.UPDATE_INSERT);	
}

/**
Writes a record to the MeasTimeScale table by first attempting to update
an existing record, and if none exists, inserting a new one.
@param r an object of type RiversideDB_MeasTimeScale to write to the table
@throws Exception if an error occurs
*/
public void writeMeasTimeScale(RiversideDB_MeasTimeScale r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_MEASTIMESCALE);
	w.addValue(r.getScale());
	w.addValue(r.getDescription());

	w.addWhereClause("Scale = '" + escape(r.getScale()) + "'");

	dmiWrite(w, DMI.UPDATE_INSERT);	
}

/**
Writes a record to the MeasTransProtocol table by first attempting to update
an existing record, and if none exists, inserting a new one.
@param r an object of type RiversideDB_MeasTransProtocol 
to write to the table
@throws Exception if an error occurs
*/
public void writeMeasTransProtocol(RiversideDB_MeasTransProtocol r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_MEASTRANSPROTOCOL);
	w.addValue(r.getProtocol());
	w.addValue(r.getDescription());

	w.addWhereClause("Protocol = '" + escape(r.getProtocol()) + "'");

	dmiWrite(w, DMI.UPDATE_INSERT);
}

/**
Writes a record to the MeasType table
@param r an object of type RiversideDB_MeasType to write to the table.  If the
Object's MeasType_num is missing, a new record will be inserted.  Otherwise,
an existing record will be updated.
@return null if an existing record was updated.  Otherwise it returns a 
new RiversideDB_MeasType object with the new MeasType_num (from the new record) in it.
@throws Exception if an error occurs
*/
public RiversideDB_MeasType writeMeasType(RiversideDB_MeasType r) 
throws Exception {

	DMIWriteStatement w = new DMIWriteStatement ( this );
	buildSQL ( w, _W_MEASTYPE );
	w.addValue(r.getMeasLoc_num());
	w.addValue(r.getData_type());
	w.addValue(r.getSub_type());
	w.addValue(r.getTime_step_base());
	w.addValue(r.getTime_step_mult());
	w.addValue(r.getSource_abbrev());
	if ( !DMIUtil.isMissing(r.getScenario() ) ) {
		w.addValue(r.getScenario());
	}
	else {
		w.addNullValue();
	}
	w.addValue(r.getTable_num1());
	w.addValue(r.getDbload_method1());
	w.addValueOrNull(r.getTable_num2());
	w.addValueOrNull(r.getDbload_method2());
	w.addValue(r.getDescription());
	w.addValue(r.getUnits_abbrev());
	w.addValue(r.getCreate_method());
	w.addValue(r.getTransmitProtocol());
	w.addValue(r.getStatus());
	w.addValue(r.getMin_check());
	w.addValue(r.getMax_check());
	if ( isDatabaseVersionAtLeast(_VERSION_030000_20041001) ) {
		w.addValue(r.getIsEditable());
	}
	else {			
		w.addValue(r.getEditable());
	}
	if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)){
		w.addValue(r.getIsVisible());
		w.addValue(r.getDBUser_num());
		w.addValue(r.getDBGroup_num());
		w.addValue(r.getDBPermissions());		
		w.addValue(r.getTS_DBUser_num());
		w.addValue(r.getTS_DBGroup_num());
		w.addValue(r.getTS_DBPermissions());				
	}

	if (!DMIUtil.isMissing(r.getMeasType_num())) {
		w.addWhereClause(
			"MeasType.MeasType_num = " + r.getMeasType_num());
		dmiWrite(w, DMI.UPDATE);
	} 
	else {
		dmiWrite(w, DMI.INSERT);
		// on an insert, the number of the autonumber inserted needs
		// to be returned, so execute a max() on the ProductGroup_num 
		// field to get the highest value (the last one inserted).  

		String sql = "SELECT MAX(MeasType_num) from " +
			"MeasType";
		ResultSet rs = dmiSelect(sql);
		int measType_num;
		if (rs.next()) {
			measType_num = rs.getInt(1); //meastype num is 1st 
		}
		else {
			return null;
		}
		closeResultSet(rs);
		r.setMeasType_num(measType_num);
		return r;		
	}
	return null;
}

/**
Writes a record to the MeasTypeStats table by first attempting to update 
an existing record, and if none exists, inserting a new one.
@param r an object of type RiversideDB_MeasTypeStats to write to the table
@throws Exception if an error occurs
*/
public void writeMeasTypeStats(RiversideDB_MeasTypeStats r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_MEASTYPESTATS);
	w.addValue(r.getMeasType_num());
	w.addValue(r.getStart_date(), DateTime.PRECISION_MINUTE);
	w.addValue(r.getEnd_date(), DateTime.PRECISION_MINUTE);
	w.addValue(r.getFirst_date_of_last_edit(), 
		DateTime.PRECISION_SECOND);
	w.addValue(r.getMeas_count());
	w.addValue(r.getMin_val());
	w.addValue(r.getMax_val());

	w.addWhereClause("MeasType_num = " + r.getMeasType_num());

	dmiWrite(w, DMI.UPDATE_INSERT);
}

/**
Writes a record to the MeasTypeStatus table by first attempting to update 
an existing record, and if none exists, inserting a new one.
@param r an object of type RiversideDB_MeasTypeStatus to write to the table
@throws Exception if an error occurs
*/
public void writeMeasTypeStatus(RiversideDB_MeasTypeStatus r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_MEASTYPESTATUS);
	w.addValue(r.getMeasType_num());
	w.addValue(r.getStatus_date(), DateTime.PRECISION_SECOND);
	w.addValue(r.getStatus());
	w.addValue(r.getComment());

	w.addWhereClause("MeasType_num = " + r.getMeasType_num());
	w.addWhereClause("Status_date = " + r.getStatus_date());

	dmiWrite(w, DMI.UPDATE_INSERT);
}

/**
Writes a record to the MessageLog table by first attempting to update 
an existing record, and if none exists, inserting a new one.
@param r an object of type RiversideDB_MessageLog to write to the table
@throws Exception if an error occurs
*/
public void writeMessageLog(RiversideDB_MessageLog r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_MESSAGELOG);
	w.addValue(r.getDate_Time(), DateTime.PRECISION_SECOND);
	w.addValue(r.getRoutine());
	w.addValue(r.getMessage_Type());
	w.addValue(r.getMessage_Level());
	w.addValue(r.getMessage());

	dmiWrite(w, DMI.INSERT);	
}

/**
Writes a record to the ProductGroup table.
@param r an object of type RiversideDB_ProductGroup to write to the table.
If the Object's ProductGroup_num is missing, a new record will be inserted.
Otherwise, an existing record will be updated.
@return null if doing an Update.  On an insert, the object being written will
be returned, except it will have its ProductGroup_num filled in with the 
value the database assigned to it.
@throws Exception if an error occurs
*/
public RiversideDB_ProductGroup writeProductGroup(RiversideDB_ProductGroup r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_PRODUCTGROUP);
	w.addValue(r.getIdentifier());
	w.addValue(r.getName());
	w.addValue(r.getProductType());
	w.addValue(r.getComment());
	w.addValue(r.getIsEnabled());
	w.addValue(r.getDBUser_num());
	w.addValue(r.getDBGroup_num());
	w.addValue(r.getDBPermissions());

	if (!DMIUtil.isMissing(r.getProductGroup_num())) {
		w.addWhereClause("ProductGroup.ProductGroup_num = " 
			+ r.getProductGroup_num());	
		dmiWrite(w, DMI.UPDATE);
		return null;
	}
	else {
		dmiWrite(w, DMI.INSERT);

		// on an insert, the number of the autonumber inserted needs
		// to be returned, so execute a max() on the ProductGroup_num 
		// field to get the highest value (the last one inserted).  

		String sql = "SELECT MAX(ProductGroup_num) from " +
			"ProductGroup";
		ResultSet rs = dmiSelect(sql);
		int ProductGroup_num;
		if (rs.next()) {
			ProductGroup_num = rs.getInt(1);
		}
		else {
			return null;
		}
		closeResultSet(rs);
		r.setProductGroup_num(ProductGroup_num);
		return r;		
	}
}

/**
Writes a record to the Props table
@param r an object of type RiversideDB_Props to write to the table.  If the 
Object's Prop_num value is missing, a new record will be inserted.  Otherwise,
an existing record will be updated.
@throws Exception if an error occurs
*/
public void writeProps(RiversideDB_Props r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	// REVISIT [LT] 2005-01-10 - Problem here and all the other similar
	//         places. Problems with validation roles for the fields
	//         If lower case is entered when upper case is expected the
	//         the writeProps will throw an exception (ACCESS).
	// Would be the solution to add toUpper() for the fields needing uppper
	// case? Maybe not. 
	// Solution: ZeroLenght and lowercase restriction will be removed from
	//		RiversideDB as per January 2005 software group meeting.

	buildSQL(w, _W_PROPS);
	w.addValue(r.getVariable());
	w.addValue(r.getVal());
	w.addValue(r.getSeq());
	w.addValue(r.getDescription());
	if ( isDatabaseVersionAtLeast(_VERSION_030000_20041001) ) {
		w.addValue(r.getDBUser_num());
	}
	if (!DMIUtil.isMissing(r.getProp_num())) {
		w.addWhereClause(
			"Prop_num = " + r.getProp_num());
		Message.printWarning ( 2, "DMI writeProps", w.toString() );	
		dmiWrite(w, DMI.UPDATE);
	}
	else {
		Message.printWarning ( 2, "DMI writeProps", w.toString() );
		dmiWrite(w, DMI.INSERT);
	}
}

/**
Writes a record to the RatingTable table by first attempting to update 
an existing record, and if none exists, inserting a new one.
@param r an object of type RiversideDB_RatingTable to write to the table
@throws Exception if an error occurs
*/
public void writeRatingTable(RiversideDB_RatingTable r) throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_RATINGTABLE);
	w.addValue(r.getRatingTable_num());
	w.addValue(r.getValue1());
	w.addValue(r.getValue2());
	w.addValue(r.getShift1());
	w.addValue(r.getShift2());

	w.addWhereClause("RatingTable_num = " + r.getRatingTable_num());

	dmiWrite(w, DMI.INSERT);
}

/**
Writes a record to the Revision table.
@param r an object of type RiversideDB_Revision to write to the table. 
If the value of Revision_num is missing, a new record will be inserted.  
Otherwise, an existing record will be updated.
@throws Exception if an error occurs
*/
public void writeRevision(RiversideDB_Revision r) throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_REVISION);
	w.addValue(r.getDate_Time(), DateTime.PRECISION_SECOND);
	w.addValue(r.getComment());

	if (!DMIUtil.isMissing(r.getRevision_num())) {
		w.addWhereClause(
			"Revision_num = " + r.getRevision_num());
		dmiWrite(w, DMI.UPDATE);
	}
	else {
		dmiWrite(w, DMI.INSERT);
	}
}

/**
Writes a record to the Scenario table.
@param r an object of type RiversideDB_Scenario to write to the table. 
If the value of Scenario_num is missing, a new record will be inserted.  
Otherwise, an existing record will be updated.
@throws Exception if an error occurs
*/
public long writeScenario(RiversideDB_Scenario r)
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_SCENARIO);
	w.addValue(r.getIdentifier());

	w.addValue(r.getDescription());
	if ( isDatabaseVersionAtLeast(_VERSION_030000_20041001) ) {
		w.addValue(r.getIsActive());
	}
	else {
		w.addValue(r.getActive());
	}

	if (!DMIUtil.isMissing(r.getScenario_num())) {
		w.addWhereClause(
			"Scenario_num = " + r.getScenario_num());
		dmiWrite(w, DMI.UPDATE);
		return DMIUtil.MISSING_LONG;
	}
	else { 
		dmiWrite(w, DMI.INSERT);
		long l = getMaxRecord("Scenario", "Scenario_num");
		return l;
	}	
}

/**
Writes a record to the SHEFType table by first attempting to update 
an existing record, and if none exists, inserting a new one.
@param r an object of type RiversideDB_SHEFType to write to the table
@throws Exception if an error occurs
*/
public void writeSHEFType(RiversideDB_SHEFType r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_SHEFTYPE);
	w.addValue(r.getSHEF_pe());
	w.addValue(r.getUnits_engl());
	w.addValue(r.getUnits_si());
	w.addValue(r.getDefault_base());
	w.addValue(r.getDefault_mult());
	w.addValue(r.getTime_scale());

	w.addWhereClause("SHEF_pe = '" + escape(r.getSHEF_pe()) + "'");

	dmiWrite(w, DMI.UPDATE_INSERT);	
}

/**
Writes a record to the StageDischargeRating table by first attempting to update 
an existing record, and if none exists, inserting a new one.
@param r an object of type RiversideDB_StageDischargeRating 
to write to the table
@throws Exception if an error occurs
*/
/*
public void writeStageDischargeRating(
RiversideDB_StageDischargeRating r) throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_STAGEDISCHARGERATING);
	w.addValue(r.getMeasLoc_num());
	w.addValue(r.getStart_Date(), DateTime.PRECISION_SECOND);
	w.addValue(r.getEnd_Date(), DateTime.PRECISION_SECOND);
	w.addValue(r.getRatingTable_num());
	w.addValue(r.getGage_Zero_Datum());
	w.addValue(r.getGage_Datum_Units());
	w.addValue(r.getWarning_Level());
	w.addValue(r.getFlood_Level());
	w.addValue(r.getStage_Units());
	w.addValue(r.getDischarge_Units());
	w.addValue(r.getInterpolation_Method());


	//w.addWhereClause("MeasLoc_num = " + r.getMeasLoc_num());
	//w.addWhereClause("Start_Date = " + r.getStart_Date() );
	//w.addWhereClause("End_Date = " + r.getEnd_Date() );
	DateTime start_dt = new DateTime( r.getStart_Date(),
	DateTime.PRECISION_MINUTE);
	DateTime end_dt = new DateTime( r.getEnd_Date(),
	DateTime.PRECISION_MINUTE);

	//w.addWhereClause("MeasLoc_num = " + r.getMeasLoc_num());
	//w.addWhereClause("Start_Date = " + start_dt.toString() );
	//w.addWhereClause("End_Date = " + end_dt.toString() );

	start_dt = null;
	end_dt = null;

	dmiWrite(w, DMI.INSERT);
}
*/

/**
Writes a record to the StageDischargeRating table by first attempting to update 
an existing record, and if none exists, inserting a new one.
@param r an object of type RiversideDB_StageDischargeRating 
to write to the table
@throws Exception if an error occurs
*/
public RiversideDB_StageDischargeRating writeStageDischargeRating(
RiversideDB_StageDischargeRating r) throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_STAGEDISCHARGERATING);

	if (!DMIUtil.isMissing(r.getRatingTable_num())) {
		w.addValue(r.getMeasLoc_num());
		w.addValue(r.getStart_Date(), DateTime.PRECISION_SECOND);
		w.addValue(r.getEnd_Date(), DateTime.PRECISION_SECOND);
		w.addValue(r.getRatingTable_num());
		w.addValue(r.getGage_Zero_Datum());
		w.addValue(r.getGage_Datum_Units());
		w.addValue(r.getWarning_Level());
		w.addValue(r.getFlood_Level());
		w.addValue(r.getStage_Units());
		w.addValue(r.getDischarge_Units());
		w.addValue(r.getInterpolation_Method());
		
		w.addWhereClause(
			"StageDischargeRating.RatingTable_num = " + 
			r.getRatingTable_num());
		DateTime start_dt = new DateTime( r.getStart_Date(),
		DateTime.PRECISION_MINUTE);
		DateTime end_dt = new DateTime( r.getEnd_Date(),
		DateTime.PRECISION_MINUTE);

		w.addWhereClause("MeasLoc_num = " + r.getMeasLoc_num());
		w.addWhereClause("Start_Date = "
			+ DMIUtil.formatDateTime(this, start_dt));
		w.addWhereClause("End_Date = "
			+ DMIUtil.formatDateTime(this, end_dt));

//Message.printStatus(1, "", w.toUpdateString());
		dmiWrite(w, DMI.UPDATE);

		start_dt = null;
		end_dt = null;
		return null;
	} 
	else {
		long l = getMaxRecord("RatingTable", "RatingTable_num");

		r.setRatingTable_num(l + 1);
		w.addValue(r.getMeasLoc_num());
		w.addValue(r.getStart_Date(), 
			DateTime.PRECISION_SECOND);
		w.addValue(r.getEnd_Date(), 
			DateTime.PRECISION_SECOND);
		w.addValue(r.getRatingTable_num());
		w.addValue(r.getGage_Zero_Datum());
		w.addValue(r.getGage_Datum_Units());
		w.addValue(r.getWarning_Level());
		w.addValue(r.getFlood_Level());
		w.addValue(r.getStage_Units());
		w.addValue(r.getDischarge_Units());
		w.addValue(r.getInterpolation_Method());
//Message.printStatus(1, "", w.toInsertString());
		dmiWrite(w, DMI.INSERT);

		RiversideDB_RatingTable rt = new RiversideDB_RatingTable();
		rt.setRatingTable_num(l + 1);
		// value2 has a constraint that it must be non-negative
		rt.setValue2(0);
		writeRatingTable(rt);

		return r;
	}
}

/**
Writes a record to the State table by first attempting to update 
an existing record, and if none exists, inserting a new one.
@param r an object of type RiversideDB_State to write to the table
@throws Exception if an error occurs
*/
public void writeState(RiversideDB_State r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_STATE);
	if (isDatabaseVersionAtLeast(_VERSION_030000_20041001)) {
		w.addValue(r.getStateGroup_num());
		w.addValue(r.getOperationStateRelation_num());
		w.addValue(r.getSequence());
		w.addValue(r.getValueStr());

		w.addWhereClause("StateGroup_num = " + r.getStateGroup_num());
		w.addWhereClause("OperationStateRelation_num = " 
			+ r.getOperationStateRelation_num());
		w.addWhereClause("Sequence = " + r.getSequence());
	}
	else {
		w.addValue(r.getModule());
		w.addValue(r.getVariable());
		w.addValue(r.getSeq());
		w.addValue(r.getStateGroup_num());
		w.addValue(r.getVal());
	
		w.addWhereClause("Module = '" + escape(r.getModule()) + "'");
		w.addWhereClause("Variable = '" + escape(r.getVariable())+ "'");
		w.addWhereClause("Seq = " + r.getSeq());
		w.addWhereClause("StateGroup_num = " + r.getStateGroup_num());
	}

	dmiWrite(w, DMI.UPDATE_INSERT);	
}

/**
Writes a record to the StateGroup table.
@param r an object of type RiversideDB_StateGroup to write to the table. 
If the value of StateGroup_num is missing, a new record will be inserted.  
Otherwise, an existing record will be updated.
@throws Exception if an error occurs
*/
public void writeStateGroup(RiversideDB_StateGroup r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_STATEGROUP);
	w.addValue(r.getScenario());
	w.addValue(r.getDate_Time(), DateTime.PRECISION_MINUTE);
	w.addValue(r.getDescription());
	w.addValue(r.getStatus());
	w.addValue(r.getMeasLocGroup_num());

	if (!DMIUtil.isMissing(r.getStateGroup_num())) {
		w.addWhereClause(
			"StateGroup_num = " + r.getStateGroup_num());
		dmiWrite(w, DMI.UPDATE);
	}
	else {
		dmiWrite(w, DMI.INSERT);
	}	
}

/**
Writes a record to the Station table.
@param r an object of type RiversideDB_Station to write to the table. 
If the value of Station_num is missing, a new record will be inserted.  
Otherwise, an existing record will be updated.
@throws Exception if an error occurs
*/
public void writeStation(RiversideDB_Station r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_STATION);
	w.addValue(r.getMeasLoc_num());
	w.addValue(r.getStation_id());
	w.addValue(r.getStation_name());
	w.addValue(r.getSource_abbrev());
	w.addValue(r.getDescription());
	w.addValue(r.getPrimary_flag());

	if (!DMIUtil.isMissing(r.getStation_num())) {
		w.addWhereClause(
			"Station_num = " + r.getStation_num());
		dmiWrite(w, DMI.UPDATE);
	}
	else {
		dmiWrite(w, DMI.INSERT);
	}
}

/**
Writes a record to the TableLayout table by first attempting to update 
an existing record, and if none exists, inserting a new one.
@param r an object of type RiversideDB_TableLayout to write to the table
@throws Exception if an error occurs
*/
public void writeTableLayout(RiversideDB_TableLayout r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_TABLELAYOUT);
	w.addValue(r.getTableLayout_num());
	w.addValue(r.getIdentifier());
	w.addValue(r.getDescription());

	w.addWhereClause("TableLayout_num = " + r.getTableLayout_num());

	dmiWrite(w, DMI.UPDATE_INSERT);
}

/**
Writes a record to the Tables table.
@param r an object of type RiversideDB_tables to write to the table. 
If the value of Table_num is missing, a new record will be inserted.  
Otherwise, an existing record will be updated.
@throws Exception if an error occurs
*/
public void writeTables(RiversideDB_Tables r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_TABLES);
	w.addValue(r.getTable_name());
	w.addValue(r.getCreated(), DateTime.PRECISION_SECOND);
	w.addValue(r.getDescription());
	w.addValue(r.getTableLayout_num());
	w.addValue(r.getArchive());
	w.addValue(r.getDB_file());
	w.addValue(r.getActive_days());
	w.addValue(r.getDate_field());
	w.addValue(r.getDate_precision());
	w.addValue(r.getDate_table());
	w.addValue(r.getDate_table_Joinfield());
	w.addValue(r.getJoinfield());
	if (isDatabaseVersionAtLeast(_VERSION_020800_20030422)){
		w.addValue(r.getIsReference());
		w.addValue(r.getIsTSTemplate());
		w.addValue(r.getIsTS());
		w.addValue(r.getDBUser_num());
		w.addValue(r.getDBGroup_num());
		w.addValue(r.getRecord_DBPermissions());
	}				

	if (!DMIUtil.isMissing(r.getTable_num())) {
		w.addWhereClause(
			"Table_num = " + r.getTable_num());
		dmiWrite(w, DMI.UPDATE);
	} 
	else {
		dmiWrite(w, DMI.INSERT);
	}
}

/**
Writes a TSProduct to the database, creating a new product or updating an 
existing product as appropriate.  Writes records to both the
TSProduct and TSProductProps tables.  This method is from the TSProductDMI 
interface.<p>
This method enforces the policy of no duplicate TSProduct identifiers.  This
was done because products may be used commonly among a group of users and right
now there's no need to allow users to override a common TSProduct with one of 
their own.  This may be REVISIT ed later.<p>
Permissions for the user to update or insert a TSProduct are checked within
this method, so in this case calling code does not have to assume anything 
about permissions.
@param product the TSProduct to write to the table.
@return true if the TSProduct was written successfully, false if not.
*/
public boolean writeTSProduct(TSProduct tsproduct) {
	String routine = "RiversideDB_DMI.writeTSProduct";
	
	// First, check to see if the TSProduct already exists in the database.

	String id = tsproduct.getPropValue("Product.ProductID");

	RiversideDB_TSProduct tsp = null;
	
	try {
		tsp = readTSProductForIdentifier(id);
	}
	catch (Exception e) {
		Message.printWarning(1, routine, "An error occured while "
			+ "trying to determine if a TSProduct with the given "
			+ "\nidentifier (\"" + id + "\") exists in the "
			+ "database.\n  The TSProduct will not be written to "
			+ "the database.");
		Message.printWarning(3, routine, e);
		return false;
	}

	boolean createNew = false;
	if (tsp == null) {
		// if the tsp object read from the database above is null, 
		// then there are no matching records in TSProduct for the 
		// user and the product id.
		createNew = true;
		tsp = new RiversideDB_TSProduct();

		Vector choices = null;
		try {
			choices = RiversideDB_Util.getProductGroupsChoices(
				this);
		}
		catch (Exception e) {
			Message.printWarning(2, routine,	
				"An error occurred while reading the "
				+ "ProductGroups that the current user\n(\""
				+ _dbuser.getLogin() + "\", "
				+ _dbuser.getDBUser_num() + ", "
				+ _dbuser.getPrimaryDBGroup_num() 
				+ ") has permission "
				+ "to access.");
			Message.printWarning(3, routine, e);
			return false;
		}

		// note: choices will never be null.

		if (choices.size() == 0) {
			new ResponseJDialog(new JFrame(), 
				"No Permissions to Write",
				"You do not have permission to write a "
				+ "time series product for any of the\n"
				+ "Product Groups in the database. You can "
				+ "save the time series product to a\n"
				+ "TSProduct file, instead.",
				ResponseJDialog.OK);
			return false;
		}

		String group = (new JComboBoxResponseJDialog(
			new JFrame(), "Select Product Group",
			"Select the Product Group for which to save the "
			+ "time series product.",
			choices, ResponseJDialog.OK | ResponseJDialog.CANCEL))
			.response();

		if (group == null) {
			return false;
		}

		String s = StringUtil.getToken(group, "-", 0, 0);

		tsp.setProductGroup_num(StringUtil.atoi(s));
	}
	else {
		try {
		if (!canUpdate(tsp.getDBUser_num(), tsp.getDBGroup_num(),
		    tsp.getDBPermissions())) {
			new ResponseJDialog(new JFrame(),
				"Invalid Permissions",
				"A time series product with the identifier '" 
				+ tsp.getIdentifier() + "' already exists in\n"
				+ "the database, but you do not have "
				+ "permissions to change it.\nSave the product "
				+ "using another identifier.",
				ResponseJDialog.OK);
			return false;
		}
		}
		catch (Exception e) {
			Message.printWarning(2, routine,
				"An error occurred while trying to read user "
				+ "permissions from the database.  The time "
				+ "series product will not be able to be "
				+ "written to the database.");
			Message.printWarning(3, routine, e);
		}
	}

	tsp.setName(tsproduct.getPropValue("Product.ProductName"));

	// REVISIT (JTS - 2005-08-19)
	// this code is HydroBase-specific right now but I am leaving it in
	// *for now*.  I think that in the future there might be call to use
	// it in some RiversideDB apps, and if it's here already we probably
	// won't spend as much time reinventing the wheel when we come to 
	// that bridge.

	// This property is the original TSProduct ID of a TSProduct when it 
	// was opened on the SelectTSProduct screen.  If the original
	// ID is not equal to the current ID, then the ID was changed in the
	// properties screen and the TSProduct must be saved to a new record.

	String orig = tsproduct.getPropValue("Product.ProductIDOriginal");
	if (orig == null) {
		orig = "";
	}
	else {
		orig = orig.trim();
	}

	if (!orig.equalsIgnoreCase(id)) {
		createNew = true;
	}

	int tsproduct_num = -1;

	if (!createNew) {
		tsproduct_num = tsp.getTSProduct_num();
		tsp.setName(tsproduct.getPropValue("Product.ProductName"));
		
		// To avoid complication, when re-writing an existing TSProduct
		// to the database, all its properties are simply deleted.

		try {
			deleteTSProductPropsForTSProduct_num(
				tsp.getTSProduct_num());
		}
		catch (Exception e) {
			Message.printWarning(2, routine, 
				"An error occurred while trying to delete "
				+ "records with TSProduct_num=" 
				+ tsp.getTSProduct_num() + " from the "
				+ "TSProductProps table.  The time series "
				+ "product will not be written to the "
				+ "database.");
			Message.printWarning(3, routine, e);
			return false;
		}
	}
	else {
		// If creating a new TSProduct for the specified product ID 
		// and user_num this first deletes any records belonging to 
		// that ID and user_num.  This is cleaner than overwriting 
		// them and hoping all the records are caught.
		try {
			deleteTSProductForIdentifier(id);
		}
		catch (Exception e) {
			Message.printWarning(2, routine, 
				"An error occurred when trying to delete "
				+ "an existing record with the identifier: \""
				+ id + "\" from the TSProduct table.  The time "
				+ "series product will not be written to the "
				+ "database.");
			Message.printWarning(3, routine, e);
			return false;
		}
	
		// Writes a new TSProduct record into the database.
		String sql = null;
		sql = "INSERT INTO TSProduct (ProductGroup_num, Identifier, "
			+ "Name, DBUser_num,DBGroup_num, DBPermissions) "
			+ "VALUES ("
			+ tsp.getProductGroup_num() + ", '" + id + "', '" + 
			tsproduct.getPropValue("Product.ProductName") + "',"
			+ _dbuser.getDBUser_num() + ", " 
			+ _dbuser.getPrimaryDBGroup_num() + ", '"
			+ _dbuser.getDefault_DBPermissions() + "')";
		try {
			dmiWrite(sql);
		}
		catch (Exception e) {
			Message.printWarning(2, routine, 
				"An error occurred while inserting a record "
				+ "into the time series product table.  "
				+ "The time series product will not be written "
				+ "to the database.");
			Message.printWarning(3, routine, e);
			return false;
		}
	
		tsproduct_num = (int)getMaxRecord("TSProduct", "TSProduct_num");
	}	
	
	// Now write all the properties from the TSProduct to the database.

	// returns all the properties, even the override properties, in one
	// single Vector.  All the properties will be written as long as 
	// they were not set as SET_AS_RUNTIME_DEFAULT props.
	Vector v = tsproduct.getAllProps();
	// v will never be null

	int count = 1;		// used to keep track of the sequence number
				// of the property in the database
	int size = v.size();
	Prop p = null;
	String sql = null;

	String error = "";
	Vector exceptions = new Vector();

	for (int i = 0; i < size; i++) {
		p = (Prop)v.elementAt(i);
		if (p.getHowSet() == Prop.SET_AS_RUNTIME_DEFAULT) {
			// do not store properties that are runtime
			// defaults.  They will be set automatically
			// next time at runtime.
		}
		else if (p.getValue().toUpperCase().endsWith(
			"PRODUCTIDORIGINAL")) {
			// This property is never stored.  It is used to know
			// whether a Product was read from the database and
			// then saved under a new Product Identifier.
		}		
		else {
			sql = "INSERT INTO TSProductProps Values ("
				+ tsproduct_num + ", '" 
				+ p.getKey() + "', "
				+ count + ", '" + p.getValue() + "')";
			try {
				dmiWrite(sql);
				count++;
			}
			catch (Exception e) {
				error += "\t\"" + p.getKey() + "\"\n";
				exceptions.add(e);
			}
		}
	}

	if (!error.equals("")) {
		Message.printWarning(2, routine,
			"An error occurred writing the following product "
		 	+ "properties to the database: \n" + error);
		size = exceptions.size();
		for (int i = 0; i < size; i++) {
			Message.printWarning(3, routine,
				(Exception)exceptions.elementAt(i));
		}
	}
			
	return true;
}

/**
Writes a RiversideDB_TSProduct record to the database.<p>
No permissions are checked as to whether the current user can write this object
to the database.
@param tsp the RiversideDB_TSProduct to write.
@return -1 if the tsproduct already exists in the database, but if a new record
is being added, returns the number of the new record's TSProduct_num.
@throws Exception if an error occurs.
*/
public int writeTSProduct(RiversideDB_TSProduct tsp) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	int action = 0;
	if (DMIUtil.isMissing(tsp.getTSProduct_num())) {
		action = _W_TSPRODUCT_INSERT;
	}
	else {
		action = _W_TSPRODUCT_UPDATE;
	}

	buildSQL(w, action);
	w.addValue(tsp.getProductGroup_num());
	w.addValue(tsp.getIdentifier());
	w.addValue(tsp.getName());
	w.addValue(tsp.getComment());
	w.addValue(tsp.getDBUser_num());
	w.addValue(tsp.getDBGroup_num());
	w.addValue(tsp.getDBPermissions());

	if (action == _W_TSPRODUCT_INSERT) {
		// Need to create a new record
		dmiWrite(w, DMI.INSERT);
		return (int)getMaxRecord("TSProduct", "TSProduct_num");
		// returns the TSproduct_num of the just-inserted record
	}
	else {
		// Update an existing record
		w.addWhereClause("TSProduct.TSProduct_num = " 
			+ tsp.getTSProduct_num());
		dmiWrite(w, DMI.UPDATE);
		return -1;
		// returns -1 because the tsproduct_num has already been 
		// determined and is present in the tsp object passed into this
		// method.
	}
}

/**
Writes a RiversideDB_TSProductProps object to the database.<p>
@param tspp the RiversideDB_TSProductProps object to write.
@throws Exception if an error occurs.
@deprecated because it's old
*/
public void writeTSProductProps(RiversideDB_TSProductProps tspp) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_TSPRODUCTPROPS);

	w.addValue(tspp.getTSProduct_num());
	w.addValue(tspp.getProperty());
	w.addValue(tspp.getValue());
	w.addValue(tspp.getSequence());

	w.addWhereClause("TSProductProps.TSProduct_num = " 
		+ tspp.getTSProduct_num());
	w.addWhereClause("TSProductProps.Property = '" 
		+ tspp.getProperty() + "'");

	dmiWrite(w, DMI.UPDATE_INSERT);
}

/**
Writes a record to the Version table.
@param r an object of type RiversideDB_Version to write to the table. 
If the value of Version_num is missing, a new record will be inserted.  
Otherwise, an existing record will be updated.
@throws Exception if an error occurs
*/
public void writeVersion(RiversideDB_Version r) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_VERSION);
	w.addValue(r.getTable_num());
	w.addValue(r.getversion_type());
	w.addValue(r.getversion_id());
	w.addValue(r.getversion_date(), DateTime.PRECISION_SECOND);
	w.addValue(r.getversion_comment());

	if (!DMIUtil.isMissing(r.getVersion_num())) {
		w.addWhereClause(
			"Version_num = " + r.getVersion_num());
		dmiWrite(w, DMI.UPDATE);
	}
	else {
		dmiWrite(w, DMI.INSERT);
	}
}

// X FUNCTIONS
// Y FUNCTIONS
// Z FUNCTIONS


// Above are useful for testing.
/////////////////////////////////////////////////////////////


/**
Returns a list of the TSProducts in the database, filtered for those that the
currently-logged in user can read, edit, or delete.  This method is 
from TSProductDMI.
@return a list of the TSProducts in the database.
*/
public Vector readTSProductDMITSProductList(boolean newProduct) {
	try {
		Vector v = readTSProductList();
		int size = v.size();
		RiversideDB_TSProduct tsp = null;
		Vector ret = new Vector();
		
		for (int i = 0; i < size; i++) {
			tsp = (RiversideDB_TSProduct)v.elementAt(i);
			if (canDelete(tsp.getDBUser_num(), tsp.getDBGroup_num(),
				tsp.getDBPermissions())
			  ||canUpdate(tsp.getDBUser_num(), tsp.getDBGroup_num(),
				tsp.getDBPermissions())
			  || canRead(tsp.getDBUser_num(), tsp.getDBGroup_num(),
				tsp.getDBPermissions())) {
				ret.add(tsp);
			}
		}

		return ret;
	}
	catch (Exception e) {
		Message.printWarning(2, "getTSProductList", 
			"An error occurred while reading the list of time "
			+ "series products from the database.");
		Message.printWarning(3, "getTSProductList", e);
		return new Vector();
	}
}

/**
Updates the Identifier of all TSProduct records having the given TSProduct_num.
@param tsproduct_num the TSProduct_num to match
@param id the new identifier to update the matching records with.
@return a count of the number of records updated
@throws Exception if there is an error updating the database.
*/
public int updateTSProductIdentifierForTSProduct_num(int tsproduct_num, 
String id)
throws Exception {
	String dmiString = "UPDATE tsproduct SET identifier='" + id 
		+ "' where tsproduct_num = " + tsproduct_num;
	return dmiWrite(dmiString);
}

/**
Updates the Permissions of all TSProduct records having the given TSProduct_num.
@param tsproduct_num the TSProduct_num to match
@param id the new identifier to update the matching records with.
@return a count of the number of records updated
@throws Exception if there is an error updating the database.
*/
public int updateTSProductDBPermissionsForTSProduct_num(int tsproduct_num, 
String permissions)
throws Exception {
	String dmiString = "UPDATE tsproduct SET dbpermissions ='" + permissions
		+ "' where tsproduct_num = " + tsproduct_num;
	return dmiWrite(dmiString);
}

/**
Updates the ProductGroup_num of all TSProduct records having the given 
TSProduct_num.
@param tsproduct_num the TSProduct_num to match
@param id the new identifier to update the matching records with.
@return a count of the number of records updated
@throws Exception if there is an error updating the database.
*/
public int updateTSProductProductGroup_numForTSProduct_num(int tsproduct_num, 
int productgroup_num)
throws Exception {
	String dmiString = "UPDATE tsproduct SET ProductGroup_num=" 
		+ productgroup_num 
		+ " where tsproduct_num = " + tsproduct_num;
	return dmiWrite(dmiString);
}



/**
Writes Action data.  From AlertIOInterface
@param dataModel the ActionDataModel containing the data to be written.
*/
public int writeActionDataModel(ActionDataModel dataModel) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	boolean insert = false;
	
	if (DMIUtil.isMissing(dataModel.getActionNum())) {
		insert = true;
	}

	buildSQL(w, _W_ACTION);
	if (dataModel.isActive()) {
		w.addValue(1);
	}
	else {
		w.addValue(0);
	}
	w.addValue(Action.getActionName(dataModel.getActionType()));
	w.addValueOrNull(dataModel.getSeverityType());
	w.addValue(dataModel.getActionName());
	w.addValueOrNull(dataModel.getDescription());
	w.addValue(dataModel.getActionBaseTimeString());
	w.addValue(dataModel.getActionBaseTimeFormatString());
	w.addValue(dataModel.getActionIntervalString());
	w.addValueOrNull(dataModel.getEvaluationWindowString());
	w.addValueOrNull(dataModel.getPropertiesString());

	if (insert) {
//		Message.printStatus(1, "", "" + w.toInsertString() + "\n\n\n");
		dmiWrite(w, DMI.INSERT);
		return (int)getMaxRecord("Action", "ActionNum");
	}
	else {
		w.addWhereClause("ActionNum = " + dataModel.getActionNum());
//		Message.printStatus(1, "", "" + w.toUpdateString() + "\n\n\n");
		dmiWrite(w, DMI.UPDATE);
		return dataModel.getActionNum();
	}
}

public DataTestResult readDataTestResult(int resultNum)
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_DATATESTRESULT);
// REVISIT (JTS - 2006-05-10)
// How to query?
//	q.addWhereClause("DataTestResults.DataTestNum = " + resultNum);
	ResultSet rs = dmiSelect(q);
	Vector v = toDataTestResultsList(rs);
	closeResultSet(rs);
	if (v == null || v.size() == 0) {
		return null;
	}
	else {
		return (DataTestResult)v.elementAt(0);
	}
}

public void deleteAction(int actionNum)
throws Exception {
	dmiDelete("DELETE FROM DataTestActionRelationship where ActionNum = "
		+ actionNum);
	dmiDelete("DELETE FROM ActionContactRelationship where ActionNum = "
		+ actionNum);
	dmiDelete("DELETE FROM ActionGroupActionRelationship where ActionNum = "
		+ actionNum);
	dmiDelete("DELETE FROM Action where ActionNum = " + actionNum);
}

public void deleteContact(int contactNum)
throws Exception {
	dmiDelete("DELETE FROM ActionContactRelationship where ContactNum = "
		+ contactNum);
	dmiDelete("DELETE FROM Contact where ContactNum = " + contactNum);
	
}

public void deleteDataTest(int dataTestNum)
throws Exception {
	dmiDelete("DELETE FROM DataTestStatus where DataTestNum = "
		+ dataTestNum);
	dmiDelete("DELETE FROM DataTestGroupsDataTestRelationship where "
		+ "DataTestNum = " + dataTestNum);
	
	deleteDataTestExpression(readDataTestExpressionNumForDataTestNum(
		dataTestNum));

	dmiDelete("DELETE FROM DataTestActionRelationship where DataTestNum = "
		+ dataTestNum);
	dmiDelete("DELETE FROM DataTest where DataTestNum = " + dataTestNum);
}

public int readDataTestExpressionNumForDataTestNum(int dataTestNum) 
throws Exception {
	ResultSet rs = dmiSelect("SELECT DataTestExpressionNum from DataTest "
		+ "WHERE DataTestNum = " + dataTestNum);
	int exprNum = -1;
	if (rs.next()) {
		exprNum = rs.getInt(1);
	}
	closeResultSet(rs);

	return exprNum;
}

public void deleteDataTestExpression(int expressionNum)
throws Exception {
	ResultSet rs = dmiSelect("SELECT LeftSideNum, LeftSideType, "
		+ "RightSideNum, RightSideType from DataTestExpression "
		+ "WHERE DataTestExpressionNum = " + expressionNum);
	
	int lnum = -1;
	int rnum = -1;
	String ltype = null;
	String rtype = null;

	boolean found = false;

	if (rs.next()) {
		found = true;
		lnum = rs.getInt(1);
		ltype = rs.getString(2);
		rnum = rs.getInt(3);
		rtype = rs.getString(4);
	}

	closeResultSet(rs);

	if (!found) {
		return;
	}

	if (DataTestSide.getSideType(ltype) == DataTestSide.EXPRESSION) {
		deleteDataTestExpression(lnum);	
	}
	else if (DataTestSide.getSideType(ltype) == DataTestSide.FUNCTION) {
		deleteDataTestFunction(lnum);
	}

	if (DataTestSide.getSideType(rtype) == DataTestSide.EXPRESSION) {
		deleteDataTestExpression(rnum);	
	}
	else if (DataTestSide.getSideType(rtype) == DataTestSide.FUNCTION) {
		deleteDataTestFunction(rnum);
	}
	
	dmiDelete("DELETE FROM DataTestExpression WHERE "
		+ "DataTestExpressionNum = " + expressionNum);
}

public void deleteDataTestFunction(int functionNum)
throws Exception {
	dmiDelete("DELETE FROM DataTestFunctionInputIDs WHERE "
		+ "DataTestFunctionNum = " + functionNum);
	dmiDelete("DELETE FROM DataTestFunction WHERE DataTestFunctionNum = "
		+ functionNum);
}

public Vector readActionTypes() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_ACTIONTYPE);
	ResultSet rs = dmiSelect(q);
	
	Vector v = new Vector();

	while (rs.next()) {
		v.add(rs.getString(1) + " - " + rs.getString(2));
	}

	closeResultSet(rs);

	return v;
}

public Vector readAllDataTestDataModels() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_DATATEST);
	ResultSet rs = dmiSelect(q);
	Vector v = toDataTestList(rs);
	closeResultSet(rs);

	DataTestDataModel model = null;
	int[] actionNums = null;
	int size = v.size();
	Vector ret = new Vector();

	for (int i = 0; i < size; i++) {
		model = (DataTestDataModel)v.elementAt(i);
		actionNums = readDataTestActionNums(model.getDataTestNum());
		model.setActionNums(actionNums);
		model = readDataTestStatusForDataTestNum(model.getDataTestNum(),
			model);
		ret.add(model);
	}

	return ret;
}

public void writeDataTestStatusData(DataTest test)
throws Exception {
	// clear out any old records
	dmiDelete("DELETE FROM DataTestStatus WHERE DataTestNum = "
		+ test.getDataTestNum());
	
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_DATATESTSTATUS);
	w.addValue(test.getDataTestNum());
	w.addValue("" + test.getLastTestDateTime());
	w.addValue("" + test.getNextTestDateTime());
	if (test.wasLastTestPositive()) {
		w.addValue(1);
	}
	else {
		w.addValue(0);
	}

	dmiWrite(w, DMI.INSERT);
//	Message.printStatus(1, "", "" + w.toInsertString() + "\n\n\n");
}

public Vector readSeverityTypes() 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_SEVERITYTYPES);
	ResultSet rs = dmiSelect(q);
	Vector v = toSeverityTypesList(rs);
	closeResultSet(rs);
	return v;
}

/**
Converts a ResultSet to a Vector of DataTestDataModels.
@param rs the ResultSet to convert.
@return a Vector of DataTestDataModels.
@throws Exception if an error occurs.
*/
private Vector toSeverityTypesList(ResultSet rs) 
throws Exception {
	Vector v = new Vector();
	int index = 1;
	int i;
	String s;
	Severity data = null;
	while (rs.next()) {
		data = new Severity();
		index = 1;
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setSeverityType(i);
		}
		i = rs.getInt(index++);
		if (!rs.wasNull()) {
			data.setSeverityLevel(i);
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setDisplayColor(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setDisplayFont(s.trim());
		}
		s = rs.getString(index++);
		if (!rs.wasNull()) {
			data.setDisplayIcon(s.trim());
		}
		v.add(data);
	}
	return v;
}

} // End RiversideDB_DMI
