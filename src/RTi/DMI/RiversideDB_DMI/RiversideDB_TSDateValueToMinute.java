// ----------------------------------------------------------------------------
// RiversideDB_TSDateValueToMinute.java - class to store time series records
//				for the table layout DATE_VALUE_TO_MINUTE
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-06-06	Steven A. Malers, RTi	Initial version.
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
//					In finalize() set values to null, not
//					missing values.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
import java.util.Date;
//import java.util.Vector;

//import RTi.DMI.DMI;
import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

//import RTi.Util.Message.Message;

/**
Store data from DATE_VALUE_TO_MINUTE time series table layouts.  This class
is meant to be used internally in the RiversideDB_DMI package.
*/
public class RiversideDB_TSDateValueToMinute extends DMIDataObject
{

/**
MeasType.MeasType_num internal key to link time series record to MeasType metadata.
*/
protected long _MeasType_num = DMIUtil.MISSING_LONG;

/**
DateTime corresponding to the data value.
*/
protected Date _Date_Time = DMIUtil.MISSING_DATE;

/**
Data value.
*/
protected double _Val = DMIUtil.MISSING_DOUBLE;

/**
Revision number the number of times revised.
*/
protected long _Revision_num = DMIUtil.MISSING_LONG;

/**
The data quality flag.
*/
protected String _Quality_flag = DMIUtil.MISSING_STRING;

/**
Duration of value in seconds.
*/
protected int _Duration = DMIUtil.MISSING_INT;

/**
DateTime corresponding to insert date/time for the value.  Tables that use this
column must be sorted first by _Date_Time and then _Creation_Time.
*/
protected Date _Creation_Time = DMIUtil.MISSING_DATE;

/**
Constructor.  
*/
public RiversideDB_TSDateValueToMinute()
{	super();
}

/**
Cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null.
@exception Throwable if there is an error.
*/
protected void finalize()
throws Throwable
{	_Date_Time = null;
	_Quality_flag = null;
    _Creation_Time = null;
	super.finalize();
}

} // End RiversideDB_TSDateValueToMinute
