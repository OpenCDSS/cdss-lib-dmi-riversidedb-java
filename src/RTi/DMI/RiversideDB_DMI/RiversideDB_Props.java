//----------------------------------------------------------------------------//
// RiversideDB_Props
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
//
// 2002-07-11	J. Thomas Sapienza, RTi	Initial Version
// 2002-08-20	JTS, RTi		Made toString() more descriptive
// 2003-01-03	SAM, RTi		Use DMIUtil for missing data.
// 2004-12-20 Luiz Teixeira, RTi 	Added the Copy constructor, the lone and
//					cloneSelf methods.
// 2005-01-04 Luiz Teixeira, RTi	Added the _DBUser_num member used under
//          				_VERSION_030000_20041001 and the 
//					methods getDBUser_num and setDBUser_num.
//					Some cleanup.
//----------------------------------------------------------------------------//
package RTi.DMI.RiversideDB_DMI;

import RTi.DMI.DMI;
import RTi.DMI.DMIDataObject;
import RTi.DMI.DMIUtil;
import RTi.Util.String.StringUtil;

/**
class to store data from the Props table
*/
public class RiversideDB_Props extends DMIDataObject
	implements Cloneable
{

// From table Props
protected long   _Prop_num    = DMIUtil.MISSING_LONG;                // Key
protected String _Variable    = DMIUtil.MISSING_STRING;
protected String _Val	      = DMIUtil.MISSING_STRING;
protected long   _Seq	      = DMIUtil.MISSING_LONG;
protected String _Description = DMIUtil.MISSING_STRING;
protected long   _DBUser_num  = DMIUtil.MISSING_LONG;                // 03.00.00

/**
constructor.  
*/
public RiversideDB_Props()
{	super();
}

/**
Copy constructor.
*/
public RiversideDB_Props(RiversideDB_Props p) {
	super();
	setProp_num   (             p.getProp_num    ()   );
	setVariable   ( new String( p.getVariable    () ) );
	setVal        ( new String( p.getVal         () ) );
	setSeq        (             p.getSeq         ()   );
	setDescription( new String( p.getDescription () ) );
	setDBUser_num (             p.getDBUser_num  ()   );         // 03.00.00
	setDirty(p.isDirty());
}

/**
Clones the data object.
@return a clone of the data object.
*/
public Object clone() 
throws CloneNotSupportedException {
	RiversideDB_Props p = (RiversideDB_Props) super.clone();
	return p;
}

/**
Clones the data object.
@return a clone of the data object.
*/
public RiversideDB_Props cloneSelf()
{
	RiversideDB_Props p = null;
	try {
		p = (RiversideDB_Props) clone();
	} catch ( CloneNotSupportedException e ) {
		p = new RiversideDB_Props( this );
	}
	return p;
}

/**
Checks whether this object has the same values as another RiversideDB_Props
object.
@param o the object against which to compare.
@return true if all values are equal, false if not.
*/
public boolean equals( Object o )
{
	// Imediatelly return false if the passed-in object is null or not an
	// instance of the RiversideDB_Props object.
	if( ( o==null ) || ( !(o instanceof RiversideDB_Props) ) ) return false;
	
	// Check if the passed-in RiversideDB_Props is equal to this object.
	return equals ( (RiversideDB_Props) o );
}

/**
Checks if this object has the same values as the passed-in RiversideDB_Props.
@param dd the RiversideDB_Props object to compare agains this.
@return true if all values are equal, false otherwise.
*/
public boolean equals( RiversideDB_Props p )
{
	if ( ( p == null )
	  || ( getProp_num() != p.getProp_num() )
	  || ( !StringUtil.stringsAreEqual(getVariable(),p.getVariable()) )
	  || ( !StringUtil.stringsAreEqual(getVal(),p.getVal()) )
	  || ( getSeq() != p.getSeq() )
	  || ( !StringUtil.stringsAreEqual(getDescription(),p.getDescription()) )
	  || ( getDBUser_num() != p.getDBUser_num() )                // 03.00.00
	  ) {	
	   	return false;
	}
	
	// Otherwise, the passing-in object is equal to this object.
	return true;
}

/**
Cleans up variables when the class is disposed of.  Sets all the member
variables (that aren't primitives) to null
*/
protected void finalize()
throws Throwable
{
	_Variable    = null;
	_Val         = null;
	_Description = null;
	
	super.finalize();
}

//   Get snd set methods presented in the order the members appear in the table.
// -----------------------------------------------------------------------------

/**
Returns the value of the member _Prop_num
@return the value of the member _Prop_num
*/
public long getProp_num() { return _Prop_num; }

/**
returns the value of the member _Variable
@return the value of the member _Variable
*/
public String getVariable() { return _Variable; }

/**
returns the value of the member _Val
@return the value of the member _Val
*/
public String getVal() { return _Val; }

/**
Returns the value of the member _Seq
@return the value of the member _Seq
*/
public long getSeq() { return _Seq; }

/** 
Returns the value of the member _Description
@return the value of the member _Description
*/
public String getDescription() { return _Description; }

/**
Returns the value of the member _DBUser_num (03.00.00)
@return the value of the member _DBUser_num
*/
public long getDBUser_num() { return _DBUser_num; }

/**
Sets the value of the member _Prop_num
@param Prop_num value to set _Prop_num to
*/
public void setProp_num( long Prop_num ) {
	_Prop_num = Prop_num;
}

/**
Sets the value of the member _Variable
@param Variable value to set _Variable to
*/
public void setVariable( String Variable ) {
	_Variable = Variable;
}

/**
Sets the value of the member _Val
@param Val value to set _Val to
*/
public void setVal( String Val) {
	_Val = Val;
}

/** 
Sets the value of the member _Seq
@param Seq value to set _Seq to
*/
public void setSeq( long Seq ) {
	_Seq = Seq;
}

/**
Sets the value of the member _Description
@param Description value to set _Description to
*/
public void setDescription( String Description ) {
	_Description = Description;
}

/** 
Sets the value of the member _DBUser_num (03.00.00)
@param DBUser_num value to set _DBUser_num to
*/
public void setDBUser_num( long DBUser_num ) {
	_DBUser_num = DBUser_num;
}
// -----------------------------------------------------------------------------

/** 
Returns a string representation of this object
@return a string representation of this object
*/
public String toString()
{
	return  "RiversideDB_Props{" 			 + "\n" +
		"Prop_num:              " + _Prop_num    + "\n" +
		"Variable:              " + _Variable    + "\n" +
		"Val:                   " + _Val         + "\n" +
		"Seq:                   " + _Seq         + "\n" +
		"Description:           " + _Description + "\n" +
		"DBUser_num (03-00-00): " + _DBUser_num  +  "}";
}

}
//----------------------------------------------------------------------------//
