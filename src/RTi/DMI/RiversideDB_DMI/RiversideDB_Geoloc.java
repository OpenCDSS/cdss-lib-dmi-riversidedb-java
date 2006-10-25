// ----------------------------------------------------------------------------
// RiversideDB_Geoloc.java - corresponds to RiversideDB Geoloc table
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-06-27	Steven A. Malers, RTi	Initial version.  Copy and modify
//					RiversideDB_MeasLoc.
// 2002-08-20	J. Thomas Sapienza, RTi	Made toString() more desriptive
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// 2003-05-08	JTS, RTi		Added a copy constructor.
// 2003-05-09	JTS, RTi		Copy constructor sets the dirty flag.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMI;
import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;

import RTi.Util.Message.Message;

/**
Class to store data from the Geoloc table
*/
public class RiversideDB_Geoloc extends DMIDataObject
{

// From table Geoloc

protected long _Geoloc_num = DMIUtil.MISSING_LONG;
protected double _Latitude = DMIUtil.MISSING_DOUBLE;
protected double _Longitude = DMIUtil.MISSING_DOUBLE;
protected double _X = DMIUtil.MISSING_DOUBLE;
protected double _Y = DMIUtil.MISSING_DOUBLE;
protected String _Country = DMIUtil.MISSING_STRING;
protected String _State = DMIUtil.MISSING_STRING;
protected String _County = DMIUtil.MISSING_STRING;
protected double _Elevation = DMIUtil.MISSING_DOUBLE;
protected String _Elevation_units = DMIUtil.MISSING_STRING;

/**
Copy constructor.
*/
public RiversideDB_Geoloc(RiversideDB_Geoloc g) {
	super();
	setGeoloc_num(g.getGeoloc_num());
	setLatitude(g.getLatitude());
	setLongitude(g.getLongitude());
	setX(g.getX());
	setY(g.getY());
	setCountry(new String(g.getCountry()));
	setState(new String(g.getState()));
	setCounty(new String(g.getCounty()));
	setElevation(g.getElevation());
	setElevation_units(new String(g.getElevation_units()));
	setDirty(g.isDirty());
}

/**
RiversideDB_Geoloc constructor.
*/
public RiversideDB_Geoloc ()
{	super();
}

/**
cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize() throws Throwable {
	_Country = null;
	_State = null;
	_County = null;
	_Elevation_units = null;
	super.finalize();
}

/**
returns _Country
@return _Country
*/
public String getCountry() {
	return _Country;
}

/**
returns _County
@return _County
*/
public String getCounty() {
	return _County;
}

/**
returns _Elevation
@return _Elevation
*/
public double getElevation() {
	return _Elevation;
}

/**
returns _Elevation_units
@return _Elevation_units
*/
public String getElevation_units() {
	return _Elevation_units;
}

/**
returns _Geoloc_num
@return _Geoloc_num
*/
public long getGeoloc_num() {
	return _Geoloc_num;
}

/**
returns _Latitude
@return _Latitude
*/
public double getLatitude() {
	return _Latitude;
}

/**
returns _Longitude
@return _Longitude
*/
public double getLongitude() {
	return _Longitude;
}

/**
returns _State
@return _State
*/
public String getState() {
	return _State;
}

/**
returns _X
@return _X
*/
public double getX() {
	return _X;
}

/**
returns _Y
@return _Y
*/
public double getY() {
	return _Y;
}

/**
sets _Country
@param Country value to put in _Country
*/
public void setCountry(String Country) {
	if ( Country != null ) {
		_Country = Country;
	}
}

/**
sets _County
@param County value to put in _County
*/
public void setCounty(String County) {
	if ( County != null ) {
		_County = County;
	}
}

/**
sets _Elevation
@param Elevation value to put in _Elevation
*/
public void setElevation(double Elevation) {
	_Elevation = Elevation;
}

/**
sets _Elevation_units
@param Elevation_units value to put in _Elevation_units
*/
public void setElevation_units(String Elevation_units) {
	if ( Elevation_units != null ) {
		_Elevation_units = Elevation_units;
	}
}

/**
sets _Geoloc_num
@param Geoloc_num value to put in _MeasLoc_num
*/
public void setGeoloc_num(long Geoloc_num) {
	_Geoloc_num = Geoloc_num;
}

/**
sets _Latitude
@param Latitude value to put in _Latitude
*/
public void setLatitude(double Latitude) {
	_Latitude = Latitude;
}

/**
sets _Longitude
@param Longitude value to put in _Longitude
*/
public void setLongitude(double Longitude) {
	_Longitude = Longitude;
}

/**
sets _State
@param State value to put in _State
*/
public void setState(String State) {
	if ( State != null ) {
		_State = State;
	}
}

/**
sets _X
@param X value to put in _X
*/
public void setX(double X) {
	_X = X;
}

/**
sets _Y
@param Y value to put in _Y
*/
public void setY(double Y) {
	_Y = Y;
}

/** 
returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return ( "RiversideDB_Geoloc{" 			+ "\n" +  
		"Geoloc_num:     " + _Geoloc_num	+ "\n" +
		"Latitude:       " + _Latitude		+ "\n" +
		"Longitude:      " + _Longitude		+ "\n" +
		"X               " + _X			+ "\n" + 
		"Y               " + _Y			+ "\n" + 
		"Country:        '" + _Country		+ "'\n" + 
		"State:          '" + _State		+ "'\n" + 
		"County:         '" + _County		+ "'\n" + 
		"Elevation:      " + _Elevation		+ "\n" + 
		"Elevation_units:'" + _Elevation_units	+ "'\n}\n"
	);
}

} // End RiversideDB_Geoloc
