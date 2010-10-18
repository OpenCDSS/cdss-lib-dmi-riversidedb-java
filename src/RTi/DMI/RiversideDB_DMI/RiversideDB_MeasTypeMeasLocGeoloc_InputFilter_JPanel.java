package RTi.DMI.RiversideDB_DMI;

import java.util.List;
import java.util.Vector;

import RTi.Util.GUI.InputFilter;
import RTi.Util.GUI.InputFilter_JPanel;

import RTi.Util.String.StringUtil;

/**
This class is an input filter for querying RiversideDB joined records from MeasType, MeasLoc, and Geoloc.
*/
public class RiversideDB_MeasTypeMeasLocGeoloc_InputFilter_JPanel
extends InputFilter_JPanel {

/**
Data store corresponding to the input filter panel.
*/
private RiversideDBDataStore __dataStore = null;

/**
Constructor.
@param dataStore the DataStore to use to connect to the database.  Cannot be null.
*/
public RiversideDB_MeasTypeMeasLocGeoloc_InputFilter_JPanel(RiversideDBDataStore dataStore)
{
    __dataStore = dataStore;
    RiversideDB_DMI dmi = (RiversideDB_DMI)dataStore.getDMI();
	String rd = dmi.getRightIdDelim();
	String ld = dmi.getLeftIdDelim();

	List<InputFilter> filters = new Vector();

	String geolocTableName = "Geoloc." + ld;
	String measLocTableName = "MeasLoc." + ld;
	String measTypeTableName = "MeasType." + ld;

	filters.add(new InputFilter("", "", StringUtil.TYPE_STRING, null, null, false)); // Blank

	// Get lists for choices...
	List<String> geolocCountyList = dmi.getGeolocCountyList();
	List<String> geolocStateList = dmi.getGeolocStateList();
	List<String> measTypeDataSourceAbbrevList = dmi.getMeasTypeDataSourceAbbrevList();
	// Don't include - redundant with TSTool data type
	//List<String> measTypeDataTypeList = dmi.getMeasTypeDataTypeList();
	List<String> measTypeSubTypeList = dmi.getMeasTypeSubTypeList();
	List<String> measTypeUnitsList = dmi.getMeasTypeUnitsList();
	
	// Choices are editable to allow multiple choices to be matched with string matching filter options
	
	// Omit Geoloc.country for now - typically will be in one country
	filters.add(new InputFilter("Data Source Abbreviation",
        measTypeTableName + "source_abbrev" + rd, "source_abbrev",
        StringUtil.TYPE_STRING, measTypeDataSourceAbbrevList, measTypeDataSourceAbbrevList, true));
	/*
    filters.add(new InputFilter("Data Type (Main)",
        measTypeTableName + "data_type" + rd, "data_type",
        StringUtil.TYPE_STRING, measTypeDataTypeList, measTypeDataTypeList, true));
        */
    filters.add(new InputFilter("Data Sub-type",
        measTypeTableName + "sub_type" + rd, "sub_type",
        StringUtil.TYPE_STRING, measTypeSubTypeList, measTypeSubTypeList, true));
    filters.add(new InputFilter("Data Units",
        measTypeTableName + "units_abbrev" + rd, "units_abbrev",
        StringUtil.TYPE_STRING, measTypeUnitsList, measTypeUnitsList, true));
    filters.add(new InputFilter("Description",
        measTypeTableName + "description" + rd, "description",
        StringUtil.TYPE_STRING, null, null, true));
    filters.add(new InputFilter("Scenario",
        measTypeTableName + "scenario" + rd, "scenario",
        StringUtil.TYPE_STRING, null, null, true));
    filters.add(new InputFilter("Station County",
       geolocTableName + "county" + rd, "county", StringUtil.TYPE_STRING,
       geolocCountyList, geolocCountyList, true));
    filters.add(new InputFilter("Station Elevation",
        geolocTableName + "elevation" + rd, "elevation",
        StringUtil.TYPE_DOUBLE, null, null, true));
    filters.add(new InputFilter("Station Latitude",
        geolocTableName + "latitude" + rd, "latitude",
        StringUtil.TYPE_DOUBLE, null, null, true));
    filters.add(new InputFilter("Station Longitude",
        geolocTableName + "longitude" + rd, "longitude",
        StringUtil.TYPE_DOUBLE, null, null, true));
    filters.add(new InputFilter("Station State",
        geolocTableName + "state" + rd, "state", StringUtil.TYPE_STRING,
        geolocStateList, geolocStateList, true));
    filters.add(new InputFilter("Station Identifier (ID)",
        measLocTableName + "identifier" + rd, "identifier", 
        StringUtil.TYPE_STRING, null, null, true));
    filters.add(new InputFilter("Station Name",
        measLocTableName + "measloc_name" + rd, "measloc_name", 
        StringUtil.TYPE_STRING, null, null, true));
    filters.add(new InputFilter("Station X",
        geolocTableName + "x" + rd, "x",
        StringUtil.TYPE_DOUBLE, null, null, true));
    filters.add(new InputFilter("Station Y",
        geolocTableName + "y" + rd, "y",
        StringUtil.TYPE_DOUBLE, null, null, true));
		
	setToolTipText("<html>RiversideDB queries can be filtered based on station and time series metadata.</html>");
	// At the request of the NCWCD, use 6 filters
	// TODO SAM 2010-09-13 may want to make the number of filters configurable
	setInputFilters(filters, 6, 15);
}

/**
Return the data store corresponding to this input filter panel.
@return the data store corresponding to this input filter panel.
*/
public RiversideDBDataStore getDataStore ( )
{
    return __dataStore;
}

}