package RTi.DMI.RiversideDB_DMI;

import java.util.Date;

import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

/**
Store single value record data from time series table layouts.  Some data fields are optional
depending on the table layout.  This class is meant to be used internally in the RiversideDB_DMI package,
for example during transfer of database records to time series objects.
*/
public class RiversideDB_TSDateValueRecord extends DMIDataObject
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
Optional revision number the number of times revised.
*/
protected long _Revision_num = DMIUtil.MISSING_LONG;

/**
Optional data quality flag.
*/
protected String _Quality_flag = DMIUtil.MISSING_STRING;

/**
Optional duration of value in seconds.
*/
protected int _Duration = DMIUtil.MISSING_INT;

/**
Optional DateTime corresponding to insert date/time for the value.  Tables that use this
column must be sorted first by _Date_Time and then _Creation_Time.
*/
protected Date _Creation_Time = DMIUtil.MISSING_DATE;

/**
Constructor.  
*/
public RiversideDB_TSDateValueRecord()
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

}

