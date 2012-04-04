package RTi.DMI.RiversideDB_DMI;

/**
Methods of writing to the database.  In particular these are used when writing time series.
*/
public enum RiversideDB_WriteMethodType
{
    /**
    Delete the data but do not write.
    */
    DELETE("Delete"),
    /**
    Delete first and then insert the data records.
    */
    DELETE_INSERT("DeleteInsert"),
    /**
    Track revisions using more complex process (see TSTool WriteRiversideDB() command documentation).
    */
    TRACK_REVISIONS("TrackRevisions");
    
    private final String displayName;

    /**
     * Name that should be displayed in choices, etc.
     * @param displayName
     */
    private RiversideDB_WriteMethodType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Return the display name.
     * @return the display name.
     */
    @Override
    public String toString() {
        return displayName;
    }
    
    /**
     * Return the enumeration value given a string name (case-independent).
     * @return the enumeration value given a string name (case-independent), or null if not matched.
     */
    public static RiversideDB_WriteMethodType valueOfIgnoreCase(String name)
    {
        RiversideDB_WriteMethodType [] values = values();
        for ( RiversideDB_WriteMethodType t : values ) {
            if ( name.equalsIgnoreCase(t.toString()) ) {
                return t;
            }
        } 
        return null;
    }
}