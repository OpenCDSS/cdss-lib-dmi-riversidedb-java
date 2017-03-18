package RTi.DMI.RiversideDB_DMI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleJButton;
import RTi.Util.IO.Cipher;
import RTi.Util.Message.Message;

import RTi.DMI.RiversideDB_DMI.RiversideDB_DMI;
import RTi.DMI.RiversideDB_DMI.RiversideDB_DBUser;

/**
LoginJDialog creates a JDialog for entering User ID and Password to login to the RiversideDB database.
Cancelling will retain a low-level connection to the database for read-only access but the user will
not be able to write or perform other actions that require credentials. 
To instantiate a LoginJDialog, use:
<pre>
int x= new LoginJDialog( JFrame parent, String title, RiversideDB_DMI datbase_connection  ).response();
</pre>
*/
@SuppressWarnings("serial")
public class LoginJDialog extends JDialog
implements ActionListener, KeyListener, FocusListener, WindowListener
{

/**
Connection to the RiversideDB database.
*/
private RiversideDB_DMI __dmi = null;

/**
Class name.
*/
private static String __class = "LoginJDialog";

/**
Login screen fields
*/
private JTextField     __username_JTextField     = null;
private String         __username_String         = "User ID: ";
private JPasswordField __password_JPasswordField = null;
private String         __password_String         = "Password: ";

private SimpleJButton  __ok_JButton              = null;
private String         __ok_String               = "OK";
private SimpleJButton  __cancel_JButton          = null;
private String         __cancel_String           = "Cancel";

private JTextField     __status_JTextField       = null;

/**
Predefined message strings.
*/
private final static String 
	__usernameEmpty_String = "Username field is empty.",
	__passwordEmpty_String = "Password field is empty.",
	__bothFieldsEmpty_String = "Username and password fields are empty.",
	__invalidUsername_String = "Invalid login. Please try again or cancel.",
	__invalidPassword_String = "Invalid login. Please try again or cancel.",
	__pleaseEnterBoth_String = " Enter username and password for full permissions. ";

/**
Identifiers of all implemented login status:
<pre>
	LOGIN_TRY_AGAIN  - The user may try again.
	LOGIN_ERROR      - Indicates an error while trying to validate a login.
	LOGIN_CANCELLED  - Indicates that the user cancelled the login section.
	LOGIN_EXCEED_MAX - Indicates that the maximum number of tries was reached.			  
	LOGIN_OK    	 - Indicates that the login was properly validated.
</pre> 
*/
public final static int	LOGIN_TRY_AGAIN    = 0,
			LOGIN_ERROR        = 1,
			LOGIN_CANCELLED    = 2,
			LOGIN_EXCEEDED_MAX = 3,
			LOGIN_OK           = 4;

/**
Holds one of the login status during the section. This login status response is
returned to the caller when the login section is closed.
*/
private int __response;

/**
Maximum number of login tries per section. If this number is reached the login
status is set to LOGIN_CANCELLED and the login section is automatically closed.
*/
private int __tryMax; 

/**
Keep track of the number of login attempts and is checked against the maximum
number of login attempts (__tryMax). 
*/
private int __tryCount; 

/**
LoginJDialog constructor.
@param parent JFrame parent to which the JDialog is associated
@param title Title for JDialog
@param db Database connection
Assembles the GUI for the Login JFrame.
@throws Exception throws an exception if the dialog can not be created.
*/
public LoginJDialog ( JFrame parent, String title, RiversideDB_DMI dmi )
throws Exception
{
	super( parent, title, true );

	__dmi = dmi;
	
	__tryMax   = 3;
	__tryCount = 1;
	
	assemble_login_JDialog();
}

/**
Assembles the GUI for the Login JFrame.
@throws Exception throws an exception if the dialog can not be created.
*/
protected void assemble_login_JDialog()
throws Exception
{
	String routine = __class + ".assemble_login_JDialog", mssg;

	JPanel main_JPanel = new JPanel();
	main_JPanel.setLayout( new GridBagLayout() );

	Insets insets = new Insets( 5, 5, 5, 5 );

	// Username Label and TextField
	JLabel username_JLabel = new JLabel ( __username_String );
	__username_JTextField  = new JTextField( 15 );
	__username_JTextField.addKeyListener( this );

	// Password Label and TextField
	JLabel password_JLabel = new JLabel ( __password_String );
	__password_JPasswordField = new JPasswordField( 25 );
	__password_JPasswordField.setEchoChar ( '*' );
	__password_JPasswordField.addKeyListener( this );

	// OK button
	__ok_JButton = new SimpleJButton ( __ok_String, __ok_String, this );
	__ok_JButton.addFocusListener ( this );
	__ok_JButton.setToolTipText( "Attempt to login as specified user." );

	// Cancel button	
	__cancel_JButton = new SimpleJButton ( __cancel_String, __cancel_String, this );
	__cancel_JButton.addFocusListener ( this );
	__cancel_JButton.setToolTipText( "Cancel login.  Read-only permissions will still be in effect." );

	// Status area
	__status_JTextField = new JTextField();
	__status_JTextField.setText ( __pleaseEnterBoth_String );
	__status_JTextField.setEditable( false );

	// Assemble
	try {
		int x = 0;
		int y = 0;
		JGUIUtil.addComponent( 
			main_JPanel, username_JLabel,
			x, y, 1, 1, 1, 0, insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );

		JGUIUtil.addComponent( 
			main_JPanel, __username_JTextField,
			++x, y, 2, 1, 1, 0, insets,
			GridBagConstraints.HORIZONTAL,
			GridBagConstraints.WEST );

		JGUIUtil.addComponent( 
			main_JPanel, password_JLabel,
			--x, ++y, 1, 1, 1, 0, insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );

		JGUIUtil.addComponent( 
			main_JPanel, __password_JPasswordField,
			++x, y, 2, 1, 1, 0, insets,
			GridBagConstraints.HORIZONTAL,
			GridBagConstraints.WEST );

		JPanel p = new JPanel();
		p.setLayout( new GridLayout( 1, 2, 2, 2 ) );
		p.add( __ok_JButton     );
		p.add( __cancel_JButton );
		JGUIUtil.addComponent( 
			main_JPanel, p,
			x, ++y, 1, 1, 1, 0, insets,
			GridBagConstraints.NONE,
			GridBagConstraints.EAST );

		x=0;
		JGUIUtil.addComponent( 
			main_JPanel, __status_JTextField,
			x, ++y, 2, 1, 1, 0, insets,
			GridBagConstraints.HORIZONTAL,
			GridBagConstraints.CENTER );

	}
	catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
		mssg = "Error creating the login GUI.";
		Message.printWarning( 2, routine, mssg ); 
		throw new Exception ( mssg );	
	}

	getContentPane().add( "Center", main_JPanel );
	setResizable( false);
	pack();
	JGUIUtil.center ( this );
	setVisible( true );
	__ok_JButton.requestFocus();

	addWindowListener( this );
}

/**
Finalize.  Free memory for garbage collection.
@exception Throwable if there is an error.
*/
public void finalize()
throws Throwable
{ 	
	__dmi = null;	
	super.finalize();
}

/**
Attempts to login to the application
*/
protected void login()
{
	String routine = __class + ".login";
	
	String username = ( __username_JTextField.getText() ).trim();
	String password = new String( __password_JPasswordField.getPassword() ).trim();
	
	// Check if the username and password were entered
	if( ( username == null || username.length() <= 0 ) && ( password == null || password.length() <= 0 ) ) {
		__status_JTextField.setText( __bothFieldsEmpty_String );
		Message.printWarning( 2, routine, __bothFieldsEmpty_String );
		__response = LOGIN_TRY_AGAIN;
	}
	
	// Check if the username was entered
	else if( ( username == null ) || ( username.length() <= 0 ) ) {
		__status_JTextField.setText( __usernameEmpty_String );
		Message.printWarning( 2, routine, __usernameEmpty_String );
		__response = LOGIN_TRY_AGAIN;
	}
	
	// Check if the password was entered
	else if( ( password == null ) || ( password.length() <= 0 ) ) {	
		__status_JTextField.setText( __passwordEmpty_String );
		Message.printWarning( 2, routine, __passwordEmpty_String );
		__response = LOGIN_TRY_AGAIN;
	}
	
	// Check the username and password can be validated
	else {
		
		// Checking the username...
		RiversideDB_DBUser du = null;
		try {
			du = __dmi.readDBUserForLogin( username );
		} catch ( Exception e ) {
			Message.printWarning( 2, routine, e);
			Message.printWarning( 1, routine, "Database access error. Exiting.");
			__response = LOGIN_ERROR;
			response();
		}

		if ( du != null ) {
 		
			// Checking the password for this valid user.
			String db_password = du.getPassword();
		 
			Cipher c = new Cipher( "00" );
			try {
				if ( c.validateString(password, db_password) ) {
					
					Message.printStatus(5, routine, "User '" + username + "' logged in." );
		
					// Set DBUser for this session
					try {
						__dmi.setDBUser( du );
						printDebugMessage();
						__response = LOGIN_OK;
					}
					catch ( Exception e ) {
						Message.printWarning ( 2, routine, e );
						Message.printWarning(1, routine, "Unable to set DBUser information. Exiting.");
						__response = LOGIN_ERROR;
					}
					response();
				} else {
					// Invalid password, Try again...
					__status_JTextField.setText( __invalidPassword_String );
					Message.printWarning( 1, routine, __invalidPassword_String );
		
					// Set username/password fields to empty
					__username_JTextField.setText ( "" );
					__password_JPasswordField.setText( "" );
					if ( __tryCount < __tryMax ) {
						__tryCount++;
						__response = LOGIN_TRY_AGAIN;	
					}
					else {
						// Exceeded the max # of tries, so cancel this login section.						
						__response = LOGIN_EXCEEDED_MAX;
						response();
					}
				}
			}
			catch ( Exception e ) {
				Message.printWarning( 2, routine, e );
				Message.printWarning( 1, routine, "Database access error. Exiting.");
				__response = LOGIN_ERROR;
				response();
			}
			
		} else {
			// Invalid username, Try again...
			__status_JTextField.setText( __invalidUsername_String );
			Message.printWarning(1,routine,__invalidUsername_String);

			// Set username/password fields to empty
			__username_JTextField.setText ( "" );
			__password_JPasswordField.setText( "" );
			if ( __tryCount < __tryMax ) {
				__tryCount++;
				__response = LOGIN_TRY_AGAIN;
			}
			else {
				// Exceeded the max # of tries, so cancel this login section.
				__response = LOGIN_EXCEEDED_MAX;
				response();
			}		
		}
	}
}

/**
Static login method to be used in order to connect to a database with a
username and password without having to open the login dialog.
*/
public static boolean login ( RiversideDB_DMI dmi, String username, String password, boolean isPasswordEncrypted )
{
	String routine=__class+ ".login(RiversideDB_DMI,String,String,boolean)";

	// Check id and password
	if ( (username == null) || (username.length() <= 0) || (password == null) || (password.length() <= 0) ) {
		Message.printWarning( 2, routine, __pleaseEnterBoth_String );
		return false;
	}
	else {
		RiversideDB_DBUser dbUser = null;
		try {
			dbUser = dmi.readDBUserForLogin( username );
		}
		catch (Exception e) {
			Message.printWarning( 2, routine, e );
			Message.printWarning( 2, routine, "Unable to verify name and password. Exiting.");
			return false;
		}

		if ( dbUser == null ) {
			Message.printWarning( 1, routine, __invalidUsername_String );
			return false;
		}
		else {
			// Get database password now for this user.
			String encryptedDatabasePassword = dbUser.getPassword();
		
			if (!isPasswordEncrypted) {
				Cipher c = new Cipher("00");
				try {
					if ( c.validateString( password, encryptedDatabasePassword ) ) {
						Message.printStatus( 5, routine, "User \"" + username + "\" logged in.");
					}
					else {
						return false;
					}
				}
				catch ( Exception e ) {
					Message.printWarning( 2, routine, e );
					return false;
				}
			}
			else {
				if ( !password.equals(encryptedDatabasePassword) ) {
					return false;
				}
			}
	
			// Set DBUser for session
			try {
				dmi.setDBUser(dbUser);
			}
			catch ( Exception e ) {
				return false;
			}

			return true;
		}
	}
}

/**
Return the response to the login attempt. 
*/
public int response()
{
	if ( __response == LOGIN_TRY_AGAIN ) {
		// Response to LOGIN_TRY_AGAIN. Nothing to do.
	}
	else {	
		// Response to LOGIN_OK, LOGIN_ERROR, LOGIN_CANCELLED and
		// LOGIN_EXCEEDED_MAX. Dispose of the dialog. 
		setVisible( false );
		dispose();
	}
	
	// Return the login status.
	return __response;
}

/**
Print user information to the log file if running under the debug settings
and the login was successful.
*/
private void printDebugMessage()
{
	String routine = __class + ".printDebugMeesage", mssg;
	
	try {
		if ( Message.isDebugOn ) {
			RiversideDB_DBUser DBUser = __dmi.getDBUser();
			mssg = "Current DBUser set to: "
			+ "DBUser_num = " + DBUser.getDBUser_num()
			+ "Login = \"" + DBUser.getLogin()
			+ "\" Description = \"" + DBUser.getDescription()
			+ "\" PrimaryDBGroup_num = " + DBUser.getPrimaryDBGroup_num()
			+ "Default_DBPermissions = \"" + DBUser.getDefault_DBPermissions() + "\"";
			Message.printDebug( 2, routine, mssg );
		}
	} catch ( Exception e ) {
		Message.printWarning( 2, routine, e );
	}
}

//----------------------------------------------------------------------------// 
//                                 Action Events                              //
//----------------------------------------------------------------------------// 


/**
Action performed event handler.
@param event Event to handle.
*/
public void actionPerformed ( ActionEvent event )
{
	String routine = __class + ".actionPerformed";

	try {
		Object source  = event.getSource();
        	
       	// OK clicked, check username and password.
		if ( source.equals( __ok_JButton ) ) {
			login();
		}
		
		// CANCEL clicked. Nothing else to do.
		else if ( source.equals( __cancel_JButton ) ) {
			__response = LOGIN_CANCELLED;
			response();
		}
        	
	} catch ( Exception e ) {
		Message.printWarning( 2, routine, e);
	}
}

//----------------------------------------------------------------------------// 
//                                   Key Events                               //
//----------------------------------------------------------------------------// 

/**
Key pressed event handler.
@param event Event to handle.
*/
public void keyPressed ( KeyEvent event )
{
	int code = event.getKeyCode();
	
	// Enter key is the same as OK
	if ( code == KeyEvent.VK_ENTER ) {
		// OK clicked
		login();
	}
	
	// Escape key is the same as Cancel
	else if ( code == KeyEvent.VK_ESCAPE ) {
		// Cancel clicked
		__response = LOGIN_CANCELLED;
		response();
	}
}

/**
Key release event handler.
@param event Event to handle.
*/
public void keyReleased ( KeyEvent event )
{
}

/**
Key typed event handler.
@param event Event to handle.
*/
public void keyTyped ( KeyEvent event )
{
}

//----------------------------------------------------------------------------// 
//                                 Focus Events                               //
//----------------------------------------------------------------------------// 

/**
Focus gained event handler.
@param event Event to handle.
*/
public void focusGained( FocusEvent event )
{
	getRootPane().setDefaultButton( (SimpleJButton) event.getComponent() );
}

/**
Focus lost event handler.
@param event Event to handle.
*/
public void focusLost( FocusEvent event )
{
}

//----------------------------------------------------------------------------// 
//                                 Window Events                              //
//----------------------------------------------------------------------------// 

/**
Window activated event handler.
@param event Event to handle.
*/
public void windowActivated ( WindowEvent event )
{
}

/**
Window closed event handler.
@param event Event to handle.
*/ 
public void windowClosed ( WindowEvent event )
{	
}

/**
Window closing event handler.
@param event Event to handle.
*/
public void windowClosing ( WindowEvent event )
{
	__response = LOGIN_CANCELLED;
	response();
}

/**
Window deactivated event handler.
@param event Event to handle.
*/
public void windowDeactivated ( WindowEvent event )
{
}

/**
Window deiconified event handler.
@param event Event to handle.
*/
public void windowDeiconified ( WindowEvent event ) {
}

/**
Window iconified event handler.
@param event Event to handle.
*/
public void windowIconified ( WindowEvent event )
{
}

/**
Window opened event handler.
@param event Event to handle.
*/
public void windowOpened ( WindowEvent event )
{
}

}
