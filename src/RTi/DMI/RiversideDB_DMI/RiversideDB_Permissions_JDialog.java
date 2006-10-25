// ----------------------------------------------------------------------------
// RiversideDB_Permissions_JDialog - A dialog for interactively editing the
//	permissions of a record in RiversideDB
// ----------------------------------------------------------------------------
// Copyright:   See the COPYRIGHT file
// ----------------------------------------------------------------------------
// History:
// 2005-08-23	J. Thomas Sapienza, RTi	Initial version.
// ----------------------------------------------------------------------------

package RTi.DMI.RiversideDB_DMI;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.ResponseJDialog;

/**
This class is a dialog for interactively editing permissions.  A Permissions
string from a record in the database is provided in the constructor and the
check boxes on the dialog are filled in to represent the permissions settings.
When the dialog is closed, the permissions are returned and set according to 
how the check boxes have been selected.<p>
This dialog can be used for any user who has write permission on the record 
in question.  The following is an example of how it might be used to change
the permissions on records in the TSProduct table:<p>
<code><pre>
	Vector v = dmi.readTSProductList();
	
	int size = v.size();
	RiversideDB_TSProduct tsp = null;
	String permissions = null;
	
	for (int i = 0;i < size; i++) {
		tsp = (RiversideDB_TSProduct)v.elementAt(i);

		if (dmi.canUpdate(tsp.getDBUser_num(), tsp.getDBGroup_num(),
			tsp.getDBPermissions())) {
			int x = (new ResponseJDialog(this,"Update permissions?",
				"Do you want to update permissions for "
				+ "TSProduct '" + tsp.getIdentifier() 
				+ "'?",
				ResponseJDialog.YES | ResponseJDialog.NO))
				.response();
			if (x == ResponseJDialog.NO) {
				continue;
			}

			permissions = (new RiversideDB_Permissions_JDialog(
				this, tsp.getDBPermissions()).response();
			if (permissions != null) {
				// the permissions were changed
				tsp.getDBPermissions(permissions);
			}
		}
	}
</pre></code>
<p>At this point, the changes in the permissions would still need to be
written back to the database.  However, this example shows how the permissions
are handled on a record-by-record basis, based on whether the user has 
permission to update each individual record.
*/
public class RiversideDB_Permissions_JDialog
extends JDialog
implements ActionListener, WindowListener {

/**
Button labels.
*/
private final String
	__BUTTON_CANCEL = "Cancel",
	__BUTTON_OK = "OK";

/**
Enums to correspond to the positions in the checkbox arrays for each type of
permission (read/create/delete/update/insert).
*/
public final int 
	__R = 0,
	__C = 1,
	__D = 2,
	__U = 3,
	__I = 4;

/**
The string returned from response(), containing the new permissions string that
was built, or null if CANCEL was pressed.
*/
private String __response;

/**
An array of checkboxes for each permission type (user/group/other).
*/
private JCheckBox[] 
	__u_JCheckBox = null,
	__g_JCheckBox = null,
	__o_JCheckBox = null;

/**
Constructor.  This is private to prevent developers from calling it.
*/
private RiversideDB_Permissions_JDialog() {}

/**
Constructor.
@param parent the parent JFrame on which the dialog should appear.  Cannot be 
null.
@param permissions the permissions to change for a record.  Can be null, in
which case this dialog will act as a way to build a new permissions string.
*/
public RiversideDB_Permissions_JDialog(JFrame parent, String permissions,
boolean isUser, boolean isGroup) {
	super(parent, "Set/Edit Permissions", true);
	setupGUI(permissions);

	if (isUser) {
		// all checkboxes are enabled
	}
	else if (isGroup) {
		// only group and other checkboxes are enabled
		for (int i = 0; i < __u_JCheckBox.length; i++) {
			__u_JCheckBox[i].setEnabled(false);
		}
	}
}

/**
Responds to ActionEvents.
@param event ActionEvent object
*/
public void actionPerformed(ActionEvent event) {
	String s = event.getActionCommand();
	if (s.equals(__BUTTON_CANCEL)) {
		__response = null;
	}
	else if (s.equals(__BUTTON_OK)) {
		__response = buildNewPermissions();
	}

	// response is called here because all actions (OK/CANCEL) result in the
	// dialog being closed.
	response();
}

/**
Builds a permissions string based on the selection of the check boxes on the
dialog.
@return a String representing the permissions set on the dialog.
*/
private String buildNewPermissions() {
	String p = "";

	StringBuffer buffer = new StringBuffer();

	buffer.append(getPermission("UR", __u_JCheckBox[__R].isSelected()));
	buffer.append(getPermission("UC", __u_JCheckBox[__C].isSelected()));
	buffer.append(getPermission("UD", __u_JCheckBox[__D].isSelected()));
	buffer.append(getPermission("UU", __u_JCheckBox[__U].isSelected()));
	buffer.append(getPermission("UI", __u_JCheckBox[__I].isSelected()));

	buffer.append(getPermission("GR", __g_JCheckBox[__R].isSelected()));
	buffer.append(getPermission("GC", __g_JCheckBox[__C].isSelected()));
	buffer.append(getPermission("GD", __g_JCheckBox[__D].isSelected()));
	buffer.append(getPermission("GU", __g_JCheckBox[__U].isSelected()));
	buffer.append(getPermission("GI", __g_JCheckBox[__I].isSelected()));

	buffer.append(getPermission("OR", __o_JCheckBox[__R].isSelected()));
	buffer.append(getPermission("OC", __o_JCheckBox[__C].isSelected()));
	buffer.append(getPermission("OD", __o_JCheckBox[__D].isSelected()));
	buffer.append(getPermission("OU", __o_JCheckBox[__U].isSelected()));
	buffer.append(getPermission("OI", __o_JCheckBox[__I].isSelected()));

	return p.toString();
}

/**
Returns a permission for a base permission type, based on whether a check box
was selected.
@param base the base permission type (UD, GR, OI, etc.)
@param selected whether the check box corresponding to this permission type
was selected or not.
@return the base string with a "+" if the permission type is allowed or a "-"
if the permission is not allowed.
*/
private String getPermission(String base, boolean selected) {
	if (selected) {
		return base + "+";
	}
	else {
		return base + "-";
	}
}

/**
Checks to see if a permissions string has a certain kind of permission.
@param permissionsString the permissions String to check.
@param permissionType the kind of permission (UC+, GI-, OD-, etc.) to search
the permission string for.
@return true if the permissions string has the permission, false if not.
*/
private boolean has(String permissionsString, String permissionType) {
	if (permissionsString.indexOf(permissionType) > -1) {
		return true;
	}
	return false;
}

/**
Selects the check boxes on the dialog based on the values in the permissions
string.
*/
private void markCheckBoxesBasedOnPermissions(String permissions) {
	if (permissions == null) {
		// this dialog is creating a new permissions string.
		// All check boxes can remain unchecked.
		return;
	}

	// do the following to avoid any case worries later
	String permissionsUpper = permissions.toUpperCase();

	// The JCheckBoxes were all instantiated with "false" as their 
	// default selected, so things only need to be selected in this 
	// method, not deselected.

	if (has(permissionsUpper, "UR+")) {
		__u_JCheckBox[__R].setSelected(true);
	}
	if (has(permissionsUpper, "UC+")) {
		__u_JCheckBox[__C].setSelected(true);
	}
	if (has(permissionsUpper, "UD+")) {
		__u_JCheckBox[__D].setSelected(true);
	}
	if (has(permissionsUpper, "UU+")) {
		__u_JCheckBox[__U].setSelected(true);
	}
	if (has(permissionsUpper, "UI+")) {
		__u_JCheckBox[__I].setSelected(true);
	}

	if (has(permissionsUpper, "GR+")) {
		__g_JCheckBox[__R].setSelected(true);
	}
	if (has(permissionsUpper, "GC+")) {
		__g_JCheckBox[__C].setSelected(true);
	}
	if (has(permissionsUpper, "GD+")) {
		__g_JCheckBox[__D].setSelected(true);
	}
	if (has(permissionsUpper, "GU+")) {
		__g_JCheckBox[__U].setSelected(true);
	}
	if (has(permissionsUpper, "GI+")) {
		__g_JCheckBox[__I].setSelected(true);
	}
	
	if (has(permissionsUpper, "OR+")) {
		__o_JCheckBox[__R].setSelected(true);
	}
	if (has(permissionsUpper, "OC+")) {
		__o_JCheckBox[__C].setSelected(true);
	}
	if (has(permissionsUpper, "OD+")) {
		__o_JCheckBox[__D].setSelected(true);
	}
	if (has(permissionsUpper, "OU+")) {
		__o_JCheckBox[__U].setSelected(true);
	}
	if (has(permissionsUpper, "OI+")) {
		__o_JCheckBox[__I].setSelected(true);
	}	
}

/**
Return the user response and dispose the dialog.
@return the new permission created by the dialog.  If null, the user pressed
CANCEL.
*/
public String response() {
	setVisible(false);
	dispose();
	return __response;
}

/**
Sets up the GUI.
@param permissions the permissions to set up the check boxes with.
*/
private void setupGUI(String permissions) {
	JPanel panel = new JPanel();
	panel.setLayout(new GridBagLayout());
	
	__u_JCheckBox = new JCheckBox[5];
	__g_JCheckBox = new JCheckBox[5];
	__o_JCheckBox = new JCheckBox[5];
	
	for (int i = 0; i < 5; i++) {
		__u_JCheckBox[i] = new JCheckBox((String)null, false);
		__g_JCheckBox[i] = new JCheckBox((String)null, false);
		__o_JCheckBox[i] = new JCheckBox((String)null, false);
	}
	
	int y = 0;
	
	JGUIUtil.addComponent(panel, 
		new JLabel("Select/edit the new permissions and press OK."),
		0, y++, 4, 1, 0, 0, 
		GridBagConstraints.NONE, GridBagConstraints.WEST);
	JGUIUtil.addComponent(panel, 
		new JLabel("Be careful changing update permissions because it"),
		0, y++, 4, 1, 0, 0, 
		GridBagConstraints.NONE, GridBagConstraints.WEST);
	JGUIUtil.addComponent(panel, 
		new JLabel("is possible to set the permissions such that you "),
		0, y++, 4, 1, 0, 0, 
		GridBagConstraints.NONE, GridBagConstraints.WEST);
	JGUIUtil.addComponent(panel, 
		new JLabel("no longer have permission to change them."),
		0, y++, 4, 1, 0, 0, 
		GridBagConstraints.NONE, GridBagConstraints.WEST);
	JGUIUtil.addComponent(panel, 
		new JLabel(""),
		0, y++, 4, 1, 0, 0, 
		GridBagConstraints.NONE, GridBagConstraints.WEST);

	JGUIUtil.addComponent(panel, 
		new JLabel(""),
		0, y++, 4, 1, 0, 0, 
		GridBagConstraints.NONE, GridBagConstraints.WEST);

	Insets insets = new Insets(0, 5, 0, 5);

	JGUIUtil.addComponent(panel, 
		new JLabel("<html><u>User</u></html>     "),
		1, y, 1, 1, 0, 0, insets,
		GridBagConstraints.CENTER, GridBagConstraints.WEST);
	
	JGUIUtil.addComponent(panel, 
		new JLabel("<html><u>Group</u></html>     "),
		2, y, 1, 1, 0, 0, insets,
		GridBagConstraints.CENTER, GridBagConstraints.WEST);

	JGUIUtil.addComponent(panel, 
		new JLabel("<html><u>Other</u></html>     "),
		3, y, 1, 1, 0, 0, insets,
		GridBagConstraints.CENTER, GridBagConstraints.WEST);
	y++;

	for (int i = 0; i < 5; i++) {
		switch (i) {
			case __R:
				JGUIUtil.addComponent(panel, 
					new JLabel("Read: "),
					0, y, 1, 1, 0, 0, 
					GridBagConstraints.EAST, 
					GridBagConstraints.WEST);
				break;
			case __C:
				JGUIUtil.addComponent(panel, 
					new JLabel("Create: "),
					0, y, 1, 1, 0, 0, 
					GridBagConstraints.EAST, 
					GridBagConstraints.WEST);
				break;
			case __D:
				JGUIUtil.addComponent(panel, 
					new JLabel("Delete: "),
					0, y, 1, 1, 0, 0, 
					GridBagConstraints.EAST, 
					GridBagConstraints.WEST);
				break;
			case __U:
				JGUIUtil.addComponent(panel, 
					new JLabel("Update: "),
					0, y, 1, 1, 0, 0, 
					GridBagConstraints.EAST, 
					GridBagConstraints.WEST);
				break;
			case __I:
				JGUIUtil.addComponent(panel, 
					new JLabel("Insert: "),
					0, y, 1, 1, 0, 0, 
					GridBagConstraints.EAST, 
					GridBagConstraints.WEST);
				break;
		}

		JGUIUtil.addComponent(panel, __u_JCheckBox[i],
			1, y, 1, 1, 0, 0, 
			GridBagConstraints.CENTER, GridBagConstraints.WEST);
		JGUIUtil.addComponent(panel, __g_JCheckBox[i],
			2, y, 1, 1, 0, 0, 
			GridBagConstraints.CENTER, GridBagConstraints.WEST);
		JGUIUtil.addComponent(panel, __o_JCheckBox[i],
			3, y, 1, 1, 0, 0, 
			GridBagConstraints.CENTER, GridBagConstraints.WEST);
		y++;
	}

	markCheckBoxesBasedOnPermissions(permissions);	

	getContentPane().add("Center", panel);

	JPanel south = new JPanel();
	south.setLayout(new FlowLayout(FlowLayout.RIGHT));

	JButton ok = new JButton(__BUTTON_OK);
	ok.addActionListener(this);
	ok.setToolTipText("Accept changes to the permissions and close the "
		+ "window.");
	
	JButton cancel = new JButton(__BUTTON_CANCEL);
	cancel.addActionListener(this);
	cancel.setToolTipText("Reject any changes to the permissions and close "
		+ "the window.");

	south.add(ok);
	south.add(cancel);
	getContentPane().add("South", south);

	JGUIUtil.center(this);

	pack();
	setVisible(true);
}

/**
Respond to WindowEvents.
@param event WindowEvent object.
*/
public void windowClosing(WindowEvent event) {
	__response = null;
	response();
}

/**
Does nothing.
*/
public void windowActivated(WindowEvent evt) {}

/**
Does nothing.
*/
public void windowClosed(WindowEvent evt) {}

/**
Does nothing.
*/
public void windowDeactivated(WindowEvent evt) {}

/**
Does nothing.
*/
public void windowDeiconified(WindowEvent evt) {}

/**
Does nothing.
*/
public void windowIconified(WindowEvent evt) {}

/**
Does nothing.
*/
public void windowOpened(WindowEvent evt) {}

}
