package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;
import RTi.Util.Time.DateTime;

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
private long _MeasType_num = DMIUtil.MISSING_LONG;

/**
DateTime corresponding to the data value.
*/
private DateTime _Date_Time = null;

/**
Data value.
*/
private double _Val = DMIUtil.MISSING_DOUBLE;

/**
Optional revision number the number of times revised.
*/
private long _Revision_num = DMIUtil.MISSING_LONG;

/**
Optional data quality flag.
*/
private String _Quality_flag = DMIUtil.MISSING_STRING;

/**
Optional duration of value in seconds.
*/
private int _Duration = DMIUtil.MISSING_INT;

/**
Optional DateTime corresponding to insert date/time for the value.  Tables that use this
column must be sorted first by _Date_Time and then _Creation_Time.
*/
private DateTime _Creation_Time = null;

/**
Constructor.  
*/
public RiversideDB_TSDateValueRecord()
{	super();
}

/**
Returns Creation_Time
@return Creation_Time
*/
public DateTime getCreation_Time() {
    return _Creation_Time;
}

/**
Returns Date_Time
@return Date_Time
*/
public DateTime getDate_Time() {
    return _Date_Time;
}

/**
Returns Duration
@return Duration
*/
public int getDuration() {
    return _Duration;
}

/**
Returns MeasType_num
@return MeasType_num
*/
public long getMeasType_num() {
    return _MeasType_num;
}

/**
Returns Quality_flag
@return Quality_flag
*/
public String getQuality_flag() {
    return _Quality_flag;
}

/**
Returns Revision_num
@return Revision_num
*/
public long getRevision_num() {
    return _Revision_num;
}

/**
Returns Val
@return Val
*/
public double getVal() {
    return _Val;
}

/**
Sets Creation_Time
@param Creation_Time value to put in Creation_Time
*/
public void setCreation_Time(DateTime Creation_Time) {
    _Creation_Time = Creation_Time;
}

/**
Sets Date_Time
@param Date_Time value to put in Date_Time
*/
public void setDate_Time(DateTime Date_Time) {
    _Date_Time = Date_Time;
}

/**
Sets Duration
@param Duration value to put in Duration
*/
public void setDuration(int Duration) {
    _Duration = Duration;
}

/**
Sets MeasType_num
@param MeasType_num value to put in MeasType_num
*/
public void setMeasType_num(long MeasType_num) {
    _MeasType_num = MeasType_num;
}

/**
Sets Quality_flag
@param Quality_flag value to put in Quality_flag
*/
public void setQuality_flag(String Quality_flag) {
    _Quality_flag = Quality_flag;
}

/**
Sets Revision_num
@param Revision_num value to put in Revision_num
*/
public void setRevision_num(long Revision_num) {
    _Revision_num = Revision_num;
}

/**
Sets Val
@param Val value to put in Val
*/
public void setVal(double Val) {
    _Val = Val;
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