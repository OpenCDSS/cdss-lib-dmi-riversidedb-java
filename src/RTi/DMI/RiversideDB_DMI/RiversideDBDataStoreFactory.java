package RTi.DMI.RiversideDB_DMI;

import RTi.Util.IO.PropList;
import riverside.datastore.DataStore;
import riverside.datastore.DataStoreFactory;

/**
 * Factory to instantiate RiversideDBDataStore instances.
 * @author sam
 *
 */
public class RiversideDBDataStoreFactory implements DataStoreFactory
{

/**
Create a RiversideDBDataStore instance and open the encapsulated RiversideDB_DMI using the specified
properties.
*/
public DataStore create ( PropList props )
{
    String name = props.getValue ( "Name" );
    String description = props.getValue ( "Description" );
    if ( description == null ) {
        description = "";
    }
    String databaseEngine = props.getValue ( "DatabaseEngine" );
    String databaseServer = props.getValue ( "DatabaseServer" );
    String databaseName = props.getValue ( "DatabaseName" );
    String systemLogin = props.getValue ( "SystemLogin" );
    String systemPassword = props.getValue ( "SystemPassword" );
    try {
        RiversideDB_DMI rdmi = new RiversideDB_DMI ( databaseEngine, databaseServer, databaseName,
        -1, // Don't use the port number - use the database name instead
        systemLogin, // OK if null - use read-only guest
        systemPassword ); // OK if null - use read-only guest
        rdmi.open();
        return new RiversideDBDataStore ( name, description, rdmi );
    }
    catch ( Exception e ) {
        // TODO SAM 2010-09-02 Wrap the exception because need to move from default Exception
        throw new RuntimeException ( e );
    }
}

}