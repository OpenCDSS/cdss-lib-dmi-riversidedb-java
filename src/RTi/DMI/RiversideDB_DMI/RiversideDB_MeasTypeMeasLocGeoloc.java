package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMIUtil;

import RTi.TS.TSIdent;

/**
Class to store data from join of the MeasType, MeasLoc, and Geoloc tables.
TODO SAM 2010-03-04 need to evaluate defining a view for the join.
*/
public class RiversideDB_MeasTypeMeasLocGeoloc extends RiversideDB_MeasLocGeoloc
{

// MeasLoc and Geoloc data members are in base class

// From table MeasType
protected long _MeasType_num = DMIUtil.MISSING_LONG;
protected long _MeasLoc_num = DMIUtil.MISSING_LONG;
protected String _Data_type = DMIUtil.MISSING_STRING;
protected String _Sub_type = DMIUtil.MISSING_STRING;
protected String _Time_step_base = DMIUtil.MISSING_STRING;
protected long _Time_step_mult = DMIUtil.MISSING_LONG;
protected String _Source_abbrev = DMIUtil.MISSING_STRING;
protected String _Scenario = DMIUtil.MISSING_STRING;
protected int _Sequence_num = DMIUtil.MISSING_INT; // Added 2012-03 - not sure of RiversideDB version (SAM)
protected long _Table_num1 = DMIUtil.MISSING_LONG;
protected long _Dbload_method1 = DMIUtil.MISSING_LONG;
protected long _Table_num2 = DMIUtil.MISSING_LONG;
protected long _Dbload_method2 = DMIUtil.MISSING_LONG;
protected String _Description = DMIUtil.MISSING_STRING;
protected String _Units_abbrev = DMIUtil.MISSING_STRING;
protected String _Create_method = DMIUtil.MISSING_STRING;
protected String _TransmitProtocol = DMIUtil.MISSING_STRING;
protected String _Status = DMIUtil.MISSING_STRING;
protected double _Min_check = DMIUtil.MISSING_DOUBLE;
protected double _Max_check = DMIUtil.MISSING_DOUBLE;
protected String _Editable = DMIUtil.MISSING_STRING; // pre 03.00.00
protected String _IsEditable = DMIUtil.MISSING_STRING; // 03.00.00 and later
protected String _IsVisible = DMIUtil.MISSING_STRING;
protected int _DBUser_num = DMIUtil.MISSING_INT;
protected int _DBGroup_num = DMIUtil.MISSING_INT;
protected String _DBPermissions = DMIUtil.MISSING_STRING;
protected int _TS_DBUser_num = DMIUtil.MISSING_INT;
protected int _TS_DBGroup_num = DMIUtil.MISSING_INT;
protected String _TS_DBPermissions = DMIUtil.MISSING_STRING;

/**
Copy constructor.
*/
/* FIXME SAM 2010-03-04 Fully enable later
public RiversideDB_MeasTypeMeasLoc(RiversideDB_MeasTypeMeasLoc m) {
	super();
	setIdentifier      (new String(m.getIdentifier      ()));
	setMeasLoc_name    (new String(m.getMeasLoc_name    ()));
	setMeasType_num    (           m.getMeasType_num    () );
	setMeasLoc_num     (           m.getMeasLoc_num     () );
	setData_type       (new String(m.getData_type       ()));
	setSub_type        (new String(m.getSub_type        ()));
	setTime_step_base  (new String(m.getTime_step_base  ()));
	setTime_step_mult  (           m.getTime_step_mult  () );
	setSource_abbrev   (new String(m.getSource_abbrev   ()));
	setScenario        (new String(m.getScenario        ()));
	setSequence_num(m.getSequence_num());
	setTable_num1      (           m.getTable_num1      () );
	setDbload_method1  (           m.getDbload_method1  () );
	setTable_num2      (           m.getTable_num2      () );
	setDbload_method2  (           m.getDbload_method2  () );
	setDescription     (new String(m.getDescription     ()));
	setUnits_abbrev    (new String(m.getUnits_abbrev    ()));
	setCreate_method   (new String(m.getCreate_method   ()));
	setTransmitProtocol(new String(m.getTransmitProtocol()));
	setStatus          (new String(m.getStatus          ()));
	setMin_check       (           m.getMin_check       () );
	setMax_check       (           m.getMax_check       () );
	setEditable        (new String(m.getEditable        ()));//pre 03.00.00
	setIsEditable      (new String(m.getIsEditable      ()));//    03.00.00
	setIsVisible       (new String(m.getIsVisible       ()));
	setDBUser_num      (           m.getDBUser_num      () );
	setDBGroup_num     (           m.getDBGroup_num     () );
	setDBPermissions   (new String(m.getDBPermissions   ()));	
	setTS_DBUser_num   (           m.getTS_DBUser_num   () );
	setTS_DBGroup_num  (           m.getTS_DBGroup_num  () );
	setTS_DBPermissions(new String(m.getTS_DBPermissions()));
			
	setDirty(m.isDirty());
}
*/

/**
Constructor.  
*/
public RiversideDB_MeasTypeMeasLocGeoloc()
{	super();
}

/**
Cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null.
@exception Throwable if there is an error.
*/
protected void finalize()
throws Throwable
{
    super.finalize();
}

//   Get and set methods presented in alphabetic order.
// ----------------------------------------------------

/**
Returns _Create_method
@return _Create_method
*/
public String getCreate_method() {
	return _Create_method;
}

/**
Returns _Data_type
@return _Data_type
*/
public String getData_type() {
	return _Data_type;
}

/**
Returns _Dbload_method1
@return _Dbload_method1
*/
public long getDbload_method1() {
	return _Dbload_method1;
}

/**
Returns _Dbload_method2
@return _Dbload_method2
*/
public long getDbload_method2() {
	return _Dbload_method2;
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
Returns _Description
@return _Description
*/
public String getDescription() {
	return _Description;
}

/**
Returns _Editable  ( pre 03.00.00 )
@return _Editable
*/
public String getEditable() {
	return _Editable;
}

/**
Returns _Identifier
@return _Identifier
*/
public String getIdentifier() {
	return _Identifier;
}

/**
Returns _IsEditable ( 03.00.00 )
@return _IsEditable
*/
public String getIsEditable() {
	return _IsEditable;
}

/**
Returns _IsVisible
@return _IsVisible
*/
public String getIsVisible() {
	return _IsVisible;
}

/**
Returns _Max_check
@return _Max_check
*/
public double getMax_check() {
	return _Max_check;
}

/** 
Returns _MeasLoc_name
@return _MeasLoc_name
*/
public String getMeasLoc_name() {
	return _MeasLoc_name;
}

/**
Returns _MeasLoc_num
@return _MeasLoc_num
*/
public long getMeasLoc_num() {
	return _MeasLoc_num;
}

/**
Returns _MeasType_num
@return _MeasType_num
*/
public long getMeasType_num() {
	return _MeasType_num;
}

/**
Returns _Min_check
@return _Min_check
*/
public double getMin_check() {
	return _Min_check;
}

/**
Returns _Scenario
@return _Scenario
*/
public String getScenario() {
	return _Scenario;
}

/**
Returns _Sequence_num
@return _Sequence_num
*/
public int getSequence_num() {
    return _Sequence_num;
}

/**
Returns _Source_abbrev
@return _Source_abrev
*/
public String getSource_abbrev() {
	return _Source_abbrev;
}

/**
Returns _Status
@return _Status
*/
public String getStatus() {
	return _Status;
}

/** 
Returns_Sub_type
@return _Sub_type
*/
public String getSub_type() {
	return _Sub_type;
}

/**
Returns _Table_num1
@return _Table_num1
*/
public long getTable_num1() {
	return _Table_num1;
}

/**
Returns _Table_num2
@return _Table_num2
*/
public long getTable_num2() {
	return _Table_num2;
}

/**
Returns _Time_step_base
@return _Time_step_base
*/
public String getTime_step_base() {
	return _Time_step_base;
}

/**
Returns _Time_step_mult
@return _Time_step_mult
*/
public long getTime_step_mult() {
	return _Time_step_mult;
}

/**
Returns _TransmitProtocol
@return _TransmitProtocol
*/
public String getTransmitProtocol() {
	return _TransmitProtocol;
}

/**
Returns _TS_DBGroup_num
@return _TS_DBGroup_num
*/
public int getTS_DBGroup_num() {
	return _TS_DBGroup_num;
}

/**
Returns _TS_DBPermissions
@return _TS_DBPermissions
*/
public String getTS_DBPermissions() {
	return _TS_DBPermissions;
}

/**
Returns _TS_DBUser_num
@return _TS_DBUser_num
*/
public int getTS_DBUser_num() {
	return _TS_DBUser_num;
}

/**
Returns _Units_abbrev
@return _Units_abbrev
*/
public String getUnits_abbrev() {
	return _Units_abbrev;
}

/**
Sets _Create_method
@param Create_method value to put in _Create_method
*/
public void setCreate_method(String Create_method) {
	if ( Create_method != null ) {
		_Create_method = Create_method;
	}
}

/**
Sets _Data_type
@param Data_type value to put in _Data_type
*/
public void setData_type(String Data_type) {
	if ( Data_type != null ) {
		_Data_type = Data_type;
	}
}

/**
Sets _Dbload_method1
@param Dbload_method1 values to put in _Dbload_method1
*/
public void setDbload_method1(long Dbload_method1) {
	_Dbload_method1 = Dbload_method1;
}

/**
Sets _Dbload_method2
@param Dbload_method2 value to put in _Dbload_method2
*/
public void setDbload_method2(long Dbload_method2) {
	_Dbload_method2 = Dbload_method2;
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
Sets _Description
@param Description value to put in _Description
*/
public void setDescription(String Description) {
	if ( Description != null ) {
		_Description = Description;
	}
}

/**
Sets _Editable  ( pre 03.00.00 )
@param Editable value to put in _Editable
*/
public void setEditable(String Editable) {
	if ( Editable != null ) {
		_Editable = Editable;
	}
}

/**
Sets _Identifier
@param Identifier value to put in _Identifier
*/
public void setIdentifier(String Identifier) {
	if ( Identifier != null ) {
		_Identifier = Identifier;
	}
}

/**
Sets _IsEditable ( 03.00.00 )
@param IsEditable value to put in _IsEditable
*/
public void setIsEditable(String IsEditable) {
	if ( IsEditable != null ) {
		_IsEditable = IsEditable;
	}
}

/**
Sets _IsVisible
@param IsVisible value to put into _IsVisible
*/
public void setIsVisible(String IsVisible) {
	_IsVisible = IsVisible;
}

/**
Sets _Max_check
@param Max_check value to put in _Max_check
*/
public void setMax_check(double Max_check) {
	_Max_check = Max_check;
}

/** 
Sets _MeasLoc_name
@param MeasLoc_name value to put in _MeasLoc_name
*/
public void setMeasLoc_name(String MeasLoc_name) {
	if ( MeasLoc_name != null ) {
		_MeasLoc_name = MeasLoc_name;
	}
}

/**
Sets _MeasLoc_num
@param MeasLoc_num value to put in _MeasLoc_num
*/
public void setMeasLoc_num(long MeasLoc_num) {
	_MeasLoc_num = MeasLoc_num;
}

/**
Sets _MeasType_num
@param MeasType_num value to put in _MeasType_num
*/
public void setMeasType_num(long MeasType_num) {	
	_MeasType_num = MeasType_num;
}

/**
Sets _Min_check
@param Min_check value to put in _Min_check
*/
public void setMin_check(double Min_check) {
	_Min_check = Min_check;
}

/**
Sets _Scenario
@param Scenario value to put in _Scenario
*/
public void setScenario(String Scenario) {
	if ( Scenario != null ) {
		_Scenario = Scenario;
	}
}

/**
Sets _Sequence_num
@param Sequence_num value to put in _Sequence_num
*/
public void setSequence_num(int Sequence_num) {
    _Sequence_num = Sequence_num;
}

/**
Sets _Source_abbrev
@param Source_abbrev value to put in _Source_abbrev
*/
public void setSource_abbrev(String Source_abbrev) {
	if ( Source_abbrev != null ) {
		_Source_abbrev = Source_abbrev;
	}
}

/**
Sets _Status
@param Status value to put in _Status
*/
public void setStatus(String Status) {
	if ( Status != null ) {
		_Status = Status;
	}
}

/** 
Sets _Sub_type
@param Sub_type value to put in _Sub_type
*/
public void setSub_type(String Sub_type) {
	if ( Sub_type != null ) {
		_Sub_type = Sub_type;
	}
}

/**
Sets _Table_num1
@param Table_num1 value to put in _Table_num1
*/
public void setTable_num1(long Table_num1) {
	_Table_num1 = Table_num1;
}

/**
Sets _Table_num2
@param Table_num2 value to put in _Table_num2
*/
public void setTable_num2(long Table_num2) {
	_Table_num2 = Table_num2;
}

/**
Sets _Time_step_base
@param Time_step_base value to put in _Time_step_base
*/
public void setTime_step_base(String Time_step_base) {
	if ( Time_step_base != null ) {
		_Time_step_base = Time_step_base;
	}
}

/**
Sets _Time_step_mult
@param Time_step_mult value to put in _Time_step_mult
*/
public void setTime_step_mult(long Time_step_mult) {
	_Time_step_mult = Time_step_mult;
}

/**
Sets _TransmitProtocol
@param TransmitProtocol value to put in _TransmitProtocol
*/
public void setTransmitProtocol(String TransmitProtocol) {
	if ( TransmitProtocol != null ) {
		_TransmitProtocol = TransmitProtocol;
	}
}

/**
Sets _TS_DBGroup_num
@param TS_DBGroup_num value to put into _TS_DBGroup_num
*/
public void setTS_DBGroup_num(int TS_DBGroup_num) {
	_TS_DBGroup_num = TS_DBGroup_num;
}

/**
Sets _TS_DBPermissions
@param TS_DBPermissions value to put into _TS_DBPermissions
*/
public void setTS_DBPermissions(String TS_DBPermissions) {
	_TS_DBPermissions = TS_DBPermissions;
}

/**
Sets _TS_DBUser_num
@param TS_DBUser_num value to put into _TS_DBUser_num
*/
public void setTS_DBUser_num(int TS_DBUser_num) {
	_TS_DBUser_num = TS_DBUser_num;
}

/**
Sets _Units_abbrev
@param Units_abbrev value to put in _Units_abbrev
*/
public void setUnits_abbrev(String Units_abbrev) {
	if ( Units_abbrev != null ) {
		_Units_abbrev = Units_abbrev;
	}
}

/** 
Returns a string representation of this object
@return a string representation of this object
*/
public String toString() {
	return  "RiversideDB_MeasType{"			+ " \n" +
		"MeasType_num:      "+ _MeasType_num	+ " \n" +
		"MeasLoc_name:      "+ _MeasLoc_num	+ " \n" +
		"Data_type:        '"+ _Data_type	+ "'\n" +
		"Sub_type:         '"+ _Sub_type	+ "'\n" +
		"Time_step_base:   '"+ _Time_step_base	+ "'\n" + 
		"Time_step_mult:    "+ _Time_step_mult	+ " \n" +
		"Source_abbrev:    '"+ _Source_abbrev	+ "'\n" + 
		"Scenario:         '"+ _Scenario	+ "'\n" +
	    "Sequence_num:      "+ _Sequence_num  + "\n" +
		"Table_num1:        "+ _Table_num1	+ " \n" + 
		"Dbload_method1:    "+ _Dbload_method1	+ " \n" + 
		"Table_num2:        "+ _Table_num2	+ " \n" +
		"Dbload_method2:    "+ _Dbload_method2	+ " \n" +
		"Description:      '"+ _Description	+ "'\n" +
		"Units_abbrev:     '"+ _Units_abbrev	+ "'\n" + 
		"Create_method:    '"+ _Create_method	+ "'\n" +
		"TransmitProtocol: '"+ _TransmitProtocol+ "'\n" +
		"Status:           '"+ _Status		+ "'\n" +
		"Min_check:         "+ _Min_check	+ " \n" + 
		"Max_check:         "+ _Max_check	+ " \n" +
		"Identifier:       '"+ _Identifier	+ "'\n" + //From MeasLoc
		"MeasLoc_name:     '"+ _MeasLoc_name	+ "'\n" + //From MeasLoc
		"Editable:         '"+ _Editable 	+ "'\n" + //pre 03.00.00
		"IsEditable:       '"+ _IsEditable 	+ "'\n" + //    03.00.00
		"IsVisible:        '"+ _IsVisible 	+ "'\n" +
		"DBUser_num:        "+ _DBUser_num	+ " \n" + 
		"DBGroup_num:       "+ _DBGroup_num	+ " \n" +
		"DBPermissions:    '"+ _DBPermissions	+ " \n" +
		"TS_DBUser_num:     "+ _TS_DBUser_num	+ " \n" + 
		"TS_DBGroup_num:    "+ _TS_DBGroup_num	+ " \n" +
		"TS_DBPermissions: '"+ _TS_DBPermissions+ "}";
}

/**
Create and return a TSIdent instance for the MeasType.
@return a TSIdent instance for the MeasType.
*/
public TSIdent toTSIdent()
throws Exception {
	String data_type = _Data_type;
	if ( !_Sub_type.equals("") ) {
		data_type = _Data_type + "-" + _Sub_type;
	}
	String timestep = _Time_step_base;
	if ( !DMIUtil.isMissing(_Time_step_mult) ) {
		timestep = "" + _Time_step_mult + _Time_step_base;
	}
	TSIdent tsident = new TSIdent ( _Identifier, _Source_abbrev, data_type, timestep, _Scenario );
    if ( !DMIUtil.isMissing(_Sequence_num) ) {
        tsident.setSequenceID("" + _Sequence_num);
    }
	return tsident;
}

}