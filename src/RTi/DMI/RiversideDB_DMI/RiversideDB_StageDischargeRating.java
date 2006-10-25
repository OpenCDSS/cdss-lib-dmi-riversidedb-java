// ----------------------------------------------------------------------------
// RiversideDB_StageDischargeRating
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-07-02	Steven A. Malers, RTi	Initial version.
// 2002-08-20	J. Thomas Sapienza, RTi	Made toString() more descriptive
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// 2003-11-12	Anne Morgan Love, RTi 	Added copy constructor.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMI;
import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import java.util.Date;

/**
class to store data from the StageDischargeRating table
*/
public class RiversideDB_StageDischargeRating extends DMIDataObject
{

protected long _MeasLoc_num = DMIUtil.MISSING_LONG;
protected Date _Start_Date = DMIUtil.MISSING_DATE;
protected Date _End_Date = DMIUtil.MISSING_DATE;
protected long _RatingTable_num = DMIUtil.MISSING_LONG;
protected double _Gage_Zero_Datum = DMIUtil.MISSING_DOUBLE;
protected String _Gage_Datum_Units = DMIUtil.MISSING_STRING;
protected double _Warning_Level = DMIUtil.MISSING_DOUBLE;
protected double _Flood_Level = DMIUtil.MISSING_DOUBLE;
protected String _Stage_Units = DMIUtil.MISSING_STRING;
protected String _Discharge_Units = DMIUtil.MISSING_STRING;
protected String _Interpolation_Method = DMIUtil.MISSING_STRING;

/** 
Constructor. 
*/
public RiversideDB_StageDischargeRating() {
	super ();
}

/**
Copy constructor.
*/
public RiversideDB_StageDischargeRating( RiversideDB_StageDischargeRating s ) {
	super();
	setDischarge_Units( new String(s.getDischarge_Units() ) );
	Date tempDate = s.getEnd_Date();
	Date clonedDate = null;
	if (tempDate != null){ 
		clonedDate = new Date(tempDate.getTime());
		setEnd_Date(clonedDate);
	}
	setFlood_Level( s.getFlood_Level() );
	setGage_Datum_Units( new String(s.getGage_Datum_Units() ));
	setGage_Zero_Datum( s.getGage_Zero_Datum());
	setInterpolation_Method( new String(s.getInterpolation_Method() ));
	setMeasLoc_num( s.getMeasLoc_num() );
	setRatingTable_num( s.getRatingTable_num() );
	setStage_Units( new String( s.getStage_Units() ) );
	tempDate = s.getStart_Date();
	clonedDate = null;
	if (tempDate != null){ 
		clonedDate = new Date(tempDate.getTime());
		setStart_Date(clonedDate);
	}

	setWarning_Level( s.getWarning_Level() );
	setDirty(s.isDirty());
	

}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Start_Date = null;
	_End_Date = null;
	_Gage_Datum_Units = null;
	_Stage_Units = null;
	_Discharge_Units = null;
	_Interpolation_Method = null;

	super.finalize();
}

/**
returns _Discharge_Units

@return _Discharge_Units
*/
public String getDischarge_Units() {
	return _Discharge_Units;
}

/**
returns _End_Date

@return _End_Date
*/
public Date getEnd_Date() {
	return _End_Date;
}

/**
returns _Flood_Level

@return _Flood_Level
*/
public double getFlood_Level() {
	return _Flood_Level;
}

/**
returns _Gage_Datum_Units

@return _Gage_Datum_Units
*/
public String getGage_Datum_Units() {
	return _Gage_Datum_Units;
}

/**
returns _Gage_Zero_Datum

@return _Gage_Zero_Datum
*/
public double getGage_Zero_Datum() {
	return _Gage_Zero_Datum;
}

/**
returns _Interpolation_Method

@return _Interpolation_Method
*/
public String getInterpolation_Method() {
	return _Interpolation_Method;
}

/**
returns _MeasLoc_num

@return _MeasLoc_num
*/
public long getMeasLoc_num() {
	return _MeasLoc_num;
}

/**
returns _RatingTable_num

@return _RatingTable_num
*/
public long getRatingTable_num() {
	return _RatingTable_num;
}

/**
returns _Stage_Units

@return _Stage_Units
*/
public String getStage_Units() {
	return _Stage_Units;
}

/**
returns _Start_Date

@return _Start_Date
*/
public Date getStart_Date() {
	return _Start_Date;
}

/**
returns _Warning_Level

@return _Warning_Level
*/
public double getWarning_Level() {
	return _Warning_Level;
}

/**
sets _Discharge_Units

@param Discharge_Units value to set _Discharge_Units to
*/
public void setDischarge_Units(String Discharge_Units) {
	if ( Discharge_Units != null ) {
		_Discharge_Units = Discharge_Units;
	}
}

/**
sets _End_Date

@param End_Date value to set _End_Date to
*/
public void setEnd_Date(Date End_Date) {
	_End_Date = End_Date;
}

/**
sets _Flood_Level

@param Flood_Level value to set _Flood_Level to
*/
public void setFlood_Level(double Flood_Level) {
	_Flood_Level = Flood_Level;
}

/**
sets _Gage_Datum_Units

@param Gage_Datum_Units value to set _Gage_Datum_Units to
*/
public void setGage_Datum_Units(String Gage_Datum_Units) {
	if ( Gage_Datum_Units != null ) {
		_Gage_Datum_Units = Gage_Datum_Units;
	}
}

/**
sets _Gage_Zero_Datum

@param Gage_Zero_Datum value to set _Gage_Zero_Datum to
*/
public void setGage_Zero_Datum(double Gage_Zero_Datum) {
	_Gage_Zero_Datum = Gage_Zero_Datum;
}

/**
sets _Interpolation_Method

@param Interpolation_Method value to set _Interpolation_Method to
*/
public void setInterpolation_Method(String Interpolation_Method) {
	if ( Interpolation_Method != null ) {
		_Interpolation_Method = Interpolation_Method;
	}
}

/**
sets _MeasLoc_num

@param MeasLoc_num value to set _MeasLoc_num to
*/
public void setMeasLoc_num(long MeasLoc_num) {
	_MeasLoc_num = MeasLoc_num;
}

/**
sets _RatingTable_num

@param RatingTable_num value to set _RatingTable_num to
*/
public void setRatingTable_num(long RatingTable_num) {
	_RatingTable_num = RatingTable_num;
}

/**
sets _Stage_Units

@param Stage_Units value to set _Stage_Units to
*/
public void setStage_Units(String Stage_Units) {
	if ( Stage_Units != null ) {
		_Stage_Units = Stage_Units;
	}
}

/**
sets _Start_Date

@param Start_Date value to set _Start_Date to
*/
public void setStart_Date(Date Start_Date) {
	_Start_Date = Start_Date;
}

/**
sets _Warning_Level

@param Warning_Level value to set _Warning_Level to
*/
public void setWarning_Level(double Warning_Level) {
	_Warning_Level = Warning_Level;
}

/**
returns String used in JComboBox for Dates.
*/
/*
public String toString() {
	return _Start_Date.toString() + " to " + _End_Date.toString();
}
*/

/** 
returns a string representation of this object

@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_StageDischargeRating{ " 		+ "\n" + 
		"MeasLoc_num:         " + _MeasLoc_num		+ "\n" +
		"Start_Date:          " + _Start_Date		+ "\n" +
		"End_Date:            " + _End_Date		+ "\n" +
		"RatingTable_num:     " + _RatingTable_num	+ "\n" +
		"Gage_Zero_Datum:     " + _Gage_Zero_Datum	+ "\n" +
		"Gage_Datum_Units:    " + _Gage_Datum_Units	+ "\n" +
		"Warning_Level:       " + _Warning_Level	+ "\n" +
		"Flood_Level:         " + _Flood_Level		+ "\n" +
		"Stage_Units:         " + _Stage_Units		+ "\n" +
		"Discharge_Units:     " + _Discharge_Units	+ "\n" +
		"Interpolation_Method:" + _Interpolation_Method	+ "}";
}

} // End RiversideDB_MessageLog
