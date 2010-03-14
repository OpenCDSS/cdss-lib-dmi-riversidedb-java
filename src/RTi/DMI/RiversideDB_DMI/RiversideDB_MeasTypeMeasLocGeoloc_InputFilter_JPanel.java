package RTi.DMI.RiversideDB_DMI;

import java.util.List;
import java.util.Vector;

import RTi.Util.GUI.InputFilter;
import RTi.Util.GUI.InputFilter_JPanel;

import RTi.Util.IO.PropList;

import RTi.Util.String.StringUtil;

/**
This class is an input filter for querying RiversideDB joined records from MeasType, MeasLoc, and Geoloc.
*/
public class RiversideDB_MeasTypeMeasLocGeoloc_InputFilter_JPanel
extends InputFilter_JPanel {

/**
Constructor.
@param dmi the dmi to use to connect to the database.  Cannot be null.
*/
public RiversideDB_MeasTypeMeasLocGeoloc_InputFilter_JPanel(RiversideDB_DMI dmi) {
	String rd = dmi.getRightIdDelim();
	String ld = dmi.getLeftIdDelim();

	List filters = new Vector();

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
	
	// Omit Geoloc.country for now - typically will be in one country
	filters.add(new InputFilter("Data Source Abbreviation",
        measTypeTableName + "source_abbrev" + rd, "source_abbrev",
        StringUtil.TYPE_STRING, measTypeDataSourceAbbrevList, measTypeDataSourceAbbrevList, false));
	/*
    filters.add(new InputFilter("Data Type (Main)",
        measTypeTableName + "data_type" + rd, "data_type",
        StringUtil.TYPE_STRING, measTypeDataTypeList, measTypeDataTypeList, false));
        */
    filters.add(new InputFilter("Data Sub-type",
        measTypeTableName + "sub_type" + rd, "sub_type",
        StringUtil.TYPE_STRING, measTypeSubTypeList, measTypeSubTypeList, false));
    filters.add(new InputFilter("Data Units",
        measTypeTableName + "units_abbrev" + rd, "units_abbrev",
        StringUtil.TYPE_STRING, measTypeUnitsList, measTypeUnitsList, false));
    filters.add(new InputFilter("Description",
        measTypeTableName + "description" + rd, "description",
        StringUtil.TYPE_STRING, null, null, false));
    filters.add(new InputFilter("Scenario",
        measTypeTableName + "scenario" + rd, "scenario",
        StringUtil.TYPE_STRING, null, null, false));
    filters.add(new InputFilter("Station County",
       geolocTableName + "county" + rd, "county", StringUtil.TYPE_STRING,
       geolocCountyList, geolocCountyList, false));
    filters.add(new InputFilter("Station Elevation",
        geolocTableName + "elevation" + rd, "elevation",
        StringUtil.TYPE_DOUBLE, null, null, false));
    filters.add(new InputFilter("Station Latitude",
        geolocTableName + "latitude" + rd, "latitude",
        StringUtil.TYPE_DOUBLE, null, null, false));
    filters.add(new InputFilter("Station Longitude",
        geolocTableName + "longitude" + rd, "longitude",
        StringUtil.TYPE_DOUBLE, null, null, false));
    filters.add(new InputFilter("Station State",
        geolocTableName + "state" + rd, "state", StringUtil.TYPE_STRING,
        geolocStateList, geolocStateList, false));
    filters.add(new InputFilter("Station Identifier",
        measLocTableName + "identifier" + rd, "identifier", 
        StringUtil.TYPE_STRING, null, null, false));
    filters.add(new InputFilter("Station Name",
        measLocTableName + "measloc_name" + rd, "measloc_name", 
        StringUtil.TYPE_STRING, null, null, false));
    filters.add(new InputFilter("Station X",
        geolocTableName + "x" + rd, "x",
        StringUtil.TYPE_DOUBLE, null, null, false));
    filters.add(new InputFilter("Station Y",
        geolocTableName + "y" + rd, "y",
        StringUtil.TYPE_DOUBLE, null, null, false));
		
	PropList filterProps = new PropList("InputFilter");
	filterProps.set("NumFilterGroups=3");
	filterProps.set("NumWhereRowsToDisplay=15"); // Display all without scrolling
	setToolTipText("<html>RiversideDB queries can be filtered " 
		+ "based on station and time series metadata.</html>");
	setInputFilters(filters, filterProps);
}

}