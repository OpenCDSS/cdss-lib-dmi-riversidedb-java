package RTi.DMI.RiversideDB_DMI;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.util.List;
import java.util.Vector;

import RTi.GRTS.TSProduct;
import RTi.GRTS.TSProductDMI;
import RTi.GRTS.TSViewJFrame;

import RTi.DMI.DMI;

import RTi.TS.TS;

import RTi.Util.GUI.JComboBoxResponseJDialog;
import RTi.Util.GUI.JGUIUtil;
import RTi.Util.GUI.SimpleJList;
import RTi.Util.GUI.ResponseJDialog;
import RTi.Util.GUI.SimpleJComboBox;
import RTi.Util.GUI.SimpleJMenuItem;
import RTi.Util.GUI.TextResponseJDialog;

import RTi.Util.IO.Prop;
import RTi.Util.IO.PropList;

import RTi.Util.Message.Message;

import RTi.Util.String.StringUtil;

/**
This class offers a GUI for managing products stored in DMI databases.  
Product group numbers can be changed, permissions changed, products deleted, 
and products loaded for editing.
*/
public class RiversideDB_TSProductManager_JFrame 
extends JFrame 
implements ActionListener, ListSelectionListener, MouseListener, 
WindowListener {

/**
The class name -- used in routine names to Message.
*/
public final String CLASS = "RiversideDB_TSProductManager_JFrame";

/**
Labels for buttons and menu items.
*/
private final String 
	__BUTTON_CLOSE = "Close",
	__BUTTON_DELETE = "Delete",
	__BUTTON_OPEN = "Open",
	__BUTTON_RENAME = "Rename",
	__MENU_PERMISSIONS = "Change Permissions",
	__MENU_PRODUCTGROUP = "Change Product Group";

/**
GUI buttons.
*/
private JButton 
	__deleteButton = null,
	__openButton = null,
	__renameButton = null;

/**
The popup menu for when RiversideDB products are right-clicked on.
*/
private JPopupMenu __popup = null;

/**
The combo box containing the list of DMI instances the user can choose from.
*/
SimpleJComboBox __databaseComboBox = null;

/**
The list in which the product descriptions are stored.
*/
private SimpleJList __productJList = null;

/**
The menu item for changing a product's product group.
*/
private SimpleJMenuItem __productGroupMenuItem = null;

/**
A list of all the TSProduct objects read from databases, in the order they appear in the list.
*/
private List<RiversideDB_TSProduct> __allTSPs = null;

/**
The list of DMIs passed into the constructor.
*/
private List<TSProductDMI> __dmis = null;

/**
A list wherein each element corresponds to one of the rows in the list, and
contains the DMI used to query that TSProduct.
*/
private List<TSProductDMI> __rowDMIs = null;

/**
The list of data that appears in the list.
*/
private List<String> __listData = null;

/**
Private to avoid instantiation.
*/
@SuppressWarnings("unused")
private RiversideDB_TSProductManager_JFrame() {}

/**
Constructor.
@param dmis a Vector of TSProductDMIs to query TSProducts from.
*/
public RiversideDB_TSProductManager_JFrame(List<TSProductDMI> dmis) {
	this(dmis, "");
}

/**
Constructor.
@param dmis a list of TSProductDMIs to query TSProducts from.
@param dmiInUse the default DMI to select from the combo box at the top 
of the dialog.  If null, the default DMI will be the first one in the list.
*/
public RiversideDB_TSProductManager_JFrame ( List<TSProductDMI> dmis, String dmiInUse ) {
	super();

	__dmis = dmis;

	if (__dmis == null) {
		__dmis = new Vector();
		// The user won't be able to do anything from the dialog, but
		// it will at least show up and pretend to be useful
	}
	
	JGUIUtil.setIcon(this, JGUIUtil.getIconImage());

	setupGUI(dmiInUse);

	showProducts(__databaseComboBox.getSelected());
}

/**
Responds to action performed events.
@param event the event that happened.
*/
public void actionPerformed(ActionEvent event) {
	String command = event.getActionCommand();

	if (command.equals(__BUTTON_CLOSE)) {
		closeClicked();
	}
	else if (command.equals(__BUTTON_DELETE)) {
		deleteClicked();
	}
	else if (command.equals(__BUTTON_OPEN)) {
		int row = __productJList.getSelectedIndex();
		if (row >= 0) {
			openTSProduct(row);
		}
	}
	else if (command.equals(__BUTTON_RENAME)) {
		renameClicked();
	}
	else if (command.equals(__MENU_PERMISSIONS)) {
		changePermissions();	
	}
	else if (command.equals(__MENU_PRODUCTGROUP)) {
		changeProductGroup();
	}
	else if (event.getSource() == __databaseComboBox) {
		showProducts(__databaseComboBox.getSelected());
	}
}

/**
Checks to see if the user can change the product group of the product in the currently-selected row.
@return true if the user can change the group, false if not.
*/
private boolean canChangeCurrentRowProductGroup(RiversideDB_DMI dmi) {
	String routine = CLASS + ".canChangeCurrentRowProductGroup()";

	List<String> choices = null;

	try {
		choices = RiversideDB_Util.getProductGroupsChoices(dmi);
	}
	catch (Exception e) {
		Message.printWarning(2, routine, "Could not determine product groups for currently-logged in user "
				+ " (\"" + dmi.getDBUser().getLogin() + "\").");
		Message.printWarning(3, routine, e);
		return false;
	}

	if (choices.size() == 0) {
		return false;
	}
	
	return true;
}

/**
Checks to see whether the currently-logged in user has permissions to update
the TSProduct currently selected in the list.
@return true if the user has sufficient permissions to update the row, false if not.
*/
private boolean canUpdateCurrentRow(boolean isPopup) {
	String routine = CLASS + ".canUpdateCurrentRow()";

	int row = __productJList.getSelectedIndex();

	if (row == -1) {
		return false;
	}

	RiversideDB_TSProduct tsp = __allTSPs.get(row);
	RiversideDB_DMI dmi = (RiversideDB_DMI)__rowDMIs.get(row);
	RiversideDB_DBUser user = dmi.getDBUser();

	if (isPopup) {
		// check to see if the user is in the other group, in which case 
		// they cannot change permissions
		if (user.getLogin().equals("root")) {
			// root can do anything
		}
		if (user.getDBUser_num() != tsp.getDBUser_num()
		    && user.getPrimaryDBGroup_num() != tsp.getDBGroup_num()) {
			return false;
		}
	}

	boolean canUpdate = false;

	try {
		if (dmi.canUpdate(tsp.getDBUser_num(), tsp.getDBGroup_num(), tsp.getDBPermissions())) {
			canUpdate = true;
		}
	}
	catch (Exception e) {
		Message.printWarning(2, routine,
			"An error occurred in checking whether you have "
			+ "permissions to update the current time series product's permissions.");
		Message.printWarning(3, routine, e);
	}

	// for popup menus, do additional checks to make sure that the user
	// never locks themselves out of setting permissions on a record

	if (!isPopup) {
		return canUpdate;
	}

	if (user.getLogin().equals("root")) {
		// root can do anything
		return true;
	}
	if (user.getDBUser_num() == tsp.getDBUser_num()) {
		// if the user owns the record, they can always change the permissions
		return true;
	}
	if (user.getPrimaryDBGroup_num() == tsp.getDBGroup_num()) {
		// if the user is in the group, they can change the record's permission
		return true;
	}

	// other cannot change permissions
	return false;
}

/**
Changes permissions for a TSProduct.
*/
private void changePermissions() {	
	String routine = CLASS + ".changePermissions";

	int row = __productJList.getSelectedIndex();

	if (row == -1) {
		return;
	}

	RiversideDB_TSProduct tsp = __allTSPs.get(row);
	RiversideDB_DMI dmi = (RiversideDB_DMI)__rowDMIs.get(row);
	RiversideDB_DBUser user = dmi.getDBUser();

	boolean isUser = false;
	boolean isGroup = false;

	if (user.getDBUser_num() == tsp.getDBUser_num()) {
		isUser = true;
	}
	if (user.getPrimaryDBGroup_num() == tsp.getDBGroup_num()) {
		isGroup = true;
	}

	try {
		String permissions = tsp.getDBPermissions();
		permissions = (new RiversideDB_Permissions_JDialog(this, permissions, isUser, isGroup)).response();

		if (permissions == null) {
			// cancel was pressed
			return;
		}

		dmi.updateTSProductDBPermissionsForTSProduct_num(tsp.getTSProduct_num(), permissions);
		tsp.setDBPermissions(permissions);
		__allTSPs.set(row, tsp);
	}
	catch (Exception e) {
		Message.printWarning(2, routine, "Database error.");
		Message.printWarning(3, routine, e);
		return;
	}
}

/**
Changes the product group for a TSProduct.
*/
private void changeProductGroup() {	
	String routine = CLASS + ".changeProductGroup";

	int row = __productJList.getSelectedIndex();

	RiversideDB_TSProduct tsp = __allTSPs.get(row);
	RiversideDB_DMI dmi = (RiversideDB_DMI)__rowDMIs.get(row);

	try {
		List<String> choices = null;
		try {
			choices = RiversideDB_Util.getProductGroupsChoices(dmi);
		}
		catch (Exception e) {
			Message.printWarning(2, routine,	
				"Could not determine product groups for currently-logged in user "
					+ " (\"" + dmi.getDBUser().getLogin() + "\").");
			Message.printWarning(3, routine, e);
			return;
		}

		String group = (new JComboBoxResponseJDialog(
			this, "Select Product Group",
			"Select the Product Group for which to save the TSProduct.",
			choices, ResponseJDialog.OK | ResponseJDialog.CANCEL)).response();
		if (group == null) {
			return;
		}

		group = StringUtil.getToken(group, "-", 0, 0);

		dmi.updateTSProductProductGroup_numForTSProduct_num(tsp.getTSProduct_num(), StringUtil.atoi(group));
		tsp.setProductGroup_num(StringUtil.atoi(group));
		__allTSPs.set(row,tsp);
	}
	catch (Exception e) {
		Message.printWarning(2, routine, "Database error.");
		Message.printWarning(3, routine, e);
		return;
	}
}

/**
Closes the GUI.
*/
private void closeClicked() {
    setVisible(false);
	dispose();
}

/**
Deletes the selected TSProduct from the database.
*/
private void deleteClicked() {
	String s = (String)__productJList.getSelectedItem();

	int x = new ResponseJDialog(this, "Confirm Delete",
		"Are you sure that you want to delete this time series product:"
		+ " '" + s + "'?",
		ResponseJDialog.YES | ResponseJDialog.NO).response();
	
	if (x == ResponseJDialog.NO) {
		return;
	}

	deleteTSProduct(__productJList.getSelectedIndex());
}

/**
Deletes a TSProduct.
@param row the row in the list of the product being deleted.
*/
private void deleteTSProduct(int row) {
	String routine = CLASS + ".processTSPVectorForList";

	RiversideDB_TSProduct tsp = (RiversideDB_TSProduct)__allTSPs.get(row);

	try {

		RiversideDB_DMI dmi = (RiversideDB_DMI)__rowDMIs.get(row);
		if (dmi.canDelete(tsp.getDBUser_num(), tsp.getDBGroup_num(),tsp.getDBPermissions())) {
			dmi.deleteTSProductForTSProduct_num(tsp.getTSProduct_num());
		}
		else {
			new ResponseJDialog(this, "Invalid Permissions",
				"You do not have adequate permissions to delete this TSProduct.", 
				ResponseJDialog.OK);
			return;
		}
		
		__allTSPs.remove(row);
		__listData.remove(row);
		__rowDMIs.remove(row);
		__productJList.setListData(__listData);

	}
	catch (Exception e) {
		Message.printWarning(2, routine, 
			"An error occurred in deleting the time series product "
			+ "with identifier: \"" + tsp.getIdentifier() + "\" from the database.");
		Message.printWarning(3, routine, e);
	}
}

/**
Cleans up member variables.
*/
public void finalize()
throws Throwable {
	__deleteButton = null;
	__openButton = null;
	__renameButton = null;
	__popup = null;
	__productJList = null;
	__allTSPs = null;
	__rowDMIs = null;
	__listData = null;
	super.finalize();
}

/**
Finds a valid identifier for the product.  A valid identifier is one that has
no spaces, apostrophes or dashes and is at least one character long and also
which does not already exist in the database.  TSProduct identifiers have to be
unique through the entire table, not just on a per-user basis.
@param dmi the dmi to use for reading from the database.
@param baseIdentifier the original identifier when the method is called.
@param group_num the product group num of the product being changed.
@return a valid identifier or null if the user canceled the action.
*/
private String getValidIdentifier(DMI dmi, String baseIdentifier, int group_num) {
	String routine = CLASS + ".getValidIdentifier";
	String identifier = new String(baseIdentifier);
	// count is a count of the number of times the following loop has gone
	// through.  If count > 0 then the loop has gone through 1 time and the
	// user entered an invalid value for the TSProduct identifier.  
	boolean inDB = false;
	int count = 0;

	do {
		inDB = false;
		if (count > 0) {
			new ResponseJDialog(this, "Invalid Identifier",
				"Identifiers cannot contain spaces, apostrophes"
				+ " or dashes, must be at least one character long and cannot already exist "
				+ "in the database.",
				ResponseJDialog.OK);
		}
		
		identifier = new TextResponseJDialog(this, 
			"Enter Time Series Product Identifier",
			"Enter a unique identifier for the time series product."
			+ "\nThe identifier must not exist in the database and \n"
			+ "cannot contain spaces, apostrophes or dashes, and "
			+ "\nmust be at least one character long.", identifier,
			ResponseJDialog.OK | ResponseJDialog.CANCEL).response();

		if (identifier == null) {
			return null;
		}

		identifier = identifier.trim();
		count++;

		inDB = false;
		try {
			if (((RiversideDB_DMI)dmi).readTSProductForIdentifier(identifier) != null) {
			    	inDB = true;
			}
		}
		catch (Exception e) {
			Message.printWarning(2, routine,
				"An error occurred in trying to read the "
				+ "time series product with identifier: \""
				+ identifier + "\" from the database.");
			Message.printWarning(3, routine, e);
			inDB = true;
		}
	} 
	while ((identifier.equals(""))
		|| (identifier.indexOf(" ") > -1) 
		|| (identifier.indexOf("'") > -1) 
		|| (identifier.indexOf("-") > -1)
		|| inDB);

	return identifier;
}

/**
Responds when the list is double-clicked, and opens the corresponding TSProduct.
@param event the MouseEvent that happened.
*/
public void mouseClicked(MouseEvent event) {
	int count = event.getClickCount();
        if (count >= 2) {
		int row = __productJList.getSelectedIndex();
		if (row >= 0) {
			openTSProduct(row);
		}
	}
}

/**
Does nothing.
*/
public void mouseEntered(MouseEvent event) {}

/**
Does nothing.
*/
public void mouseExited(MouseEvent event) {}

/**
Show popup menu. 
@param event the MouseEvent that happened.
*/
public void mousePressed(MouseEvent event) {
	if (!canUpdateCurrentRow(true)) {
		return;
	}

	int row = __productJList.getSelectedIndex();

	if (row == -1 ) {
		return;
	}

	if (__popup.isPopupTrigger(event)) {
		if (canChangeCurrentRowProductGroup( (RiversideDB_DMI)__rowDMIs.get(row))) {
			__productGroupMenuItem.setEnabled(true);
		}
		else {
			__productGroupMenuItem.setEnabled(false);
		}
		__popup.show(event.getComponent(), event.getX(), event.getY());
	}
}

/**
Show popup menu.
@param event the MouseEvent that happened.
*/
public void mouseReleased(MouseEvent event) {
	if (!canUpdateCurrentRow(true)) {
		return;
	}

	int row = __productJList.getSelectedIndex();

	if (row == -1 ) {
		return;
	}

	if (__popup.isPopupTrigger(event)) {
		if (canChangeCurrentRowProductGroup( (RiversideDB_DMI)__rowDMIs.get(row))) {
			__productGroupMenuItem.setEnabled(true);
		}
		else {
			__productGroupMenuItem.setEnabled(false);
		}	
		__popup.show(event.getComponent(), event.getX(), event.getY());
	}
}

/**
Opens the TSProduct from the database for the specified identifier 
string and belonging to the currently-logged-in user.  Identifier should already exist.  
@param row the row in the list of the product being opened.
*/
private void openTSProduct(int row) {
	String routine = CLASS + ".openRiversideTSProduct";

	List dbProps = null;
	RiversideDB_TSProduct tsp = __allTSPs.get(row);
	String identifier = tsp.getIdentifier();
	RiversideDB_DMI dmi = (RiversideDB_DMI)__rowDMIs.get(row);	
	Message.printStatus(1, "", "Open product: " + identifier);
	try {
		// read all the properties for that TSProduct from the database.
		dbProps = dmi.readTSProductPropsListForTSProduct_num(tsp.getTSProduct_num());
	}
	catch (Exception e) {
		Message.printWarning(2, routine,
			"An error occurred in trying to read product properties"
			+ " for TSProduct \"" + identifier + "\" from the database.");
		Message.printWarning(3, routine, e);
		return;
	}

	PropList props = new PropList("TSProduct");
	props.setHowSet(Prop.SET_FROM_PERSISTENT);
	int size = dbProps.size();
	List<String> tsids = new Vector();
	RiversideDB_TSProductProps tspp = null;

	// Loops through all the properties and adds them to an actual proplist.
	// Also keeps track of any properties that end with TSID, as these 
	// are time series that will need to be queried from the database.
	for (int i = 0; i < size; i++) {
		tspp = (RiversideDB_TSProductProps)dbProps.get(i);
		try {
			props.set(tspp.getProperty() + "=" + tspp.getVal());
			if (StringUtil.endsWithIgnoreCase(tspp.getProperty(), "TSID")) {
				tsids.add(tspp.getVal());
			}
		}
		catch (Exception e) {
			Message.printWarning(2, routine, 
				"An error occurred in setting the following property: \"" + tspp.getProperty() 
				+ "\"=\"" + tspp.getVal() + "\".");
			Message.printWarning(3, routine, e);
		}
	}

	TSProduct product = null;
	try {
		if (props.getValue("InitialView") != null) {
			props.set("InitialView", props.getValue("InitialView"));
		}
		else {
			props.set("InitialView", "Graph");		
		}
		props.set("ProductIDOriginal", identifier);
		product = new TSProduct(props, null);
		TS ts = null;
		List<TS> tsList = new Vector();
		String s = null;
		size = tsids.size();
		for (int i = 0; i < size; i++) {
			s = tsids.get(i);
			ts = dmi.readTimeSeries(s, null, null, null, true);
			tsList.add(ts);
		}
		product.setTSList(tsList);
		product.showProps(1);

		TSViewJFrame view = new TSViewJFrame(product);
	
		tsp = __allTSPs.get(row);

		if (dmi.canUpdate(tsp.getDBUser_num(), tsp.getDBGroup_num(), tsp.getDBPermissions())) {		
			view.addTSProductDMI(dmi);
		}
	}
	catch (Exception e) {
		Message.printWarning(2, routine, 
			"An error occurred in opening the time series product."
			+ "  Consult the log file for more information.");
		Message.printWarning(3, routine, e);
	}	
}

/**
Processes a list of TSPs in the __tspVector[] array and sets up other lists to be used internally.
@param dmi the dmi that corresponds to the tsps being processed.
@param tspVector the Vector of time series products to process.  Cannot be null.
*/
private void processTSPVectorForList(TSProductDMI dmi, List<RiversideDB_TSProduct> tspVector) {

	int size = tspVector.size();

	RiversideDB_TSProduct tsp = null;

	for (int i = 0; i < size; i++) {
		__rowDMIs.add(dmi);
		tsp = tspVector.get(i);
		__allTSPs.add(tsp);
		__listData.add(tsp.getIdentifier() + " - " + tsp.getName());
	}
}

/**
Renames a TSProduct.
*/
private void renameClicked() {
	String routine = CLASS + ".renameClicked";
	int row = __productJList.getSelectedIndex();

	RiversideDB_TSProduct tsp = __allTSPs.get(row);
	RiversideDB_DMI dmi = (RiversideDB_DMI)__rowDMIs.get(row);
	String currentID = tsp.getIdentifier();
	int tsproduct_num = tsp.getTSProduct_num();

	try {
		if (!dmi.canUpdate(tsp.getDBUser_num(), tsp.getDBGroup_num(), tsp.getDBPermissions())) {
		 	new ResponseJDialog(this, "Invalid Permissions",
				"You do not have adequate permissions to change this TSProduct.", 
				ResponseJDialog.OK);
			return;
		}
	}
	catch (Exception e) {
		Message.printWarning(2, routine, "Unable to check DB permissions.");
		Message.printWarning(3, routine, e);
		return;
	}
	
	String id = getValidIdentifier(dmi, currentID, tsp.getProductGroup_num());
	if (id == null) {	
		return;
	}

	try {
		dmi.updateTSProductIdentifierForTSProduct_num(tsproduct_num, id);
		tsp.setIdentifier(id);
		__allTSPs.set(row,tsp);
		__listData.set(row,tsp.getIdentifier() + " - " + tsp.getName());
	}
	catch (Exception e) {
		Message.printWarning(2, routine, "Error writing to database.");
		Message.printWarning(3, routine, e);
	}		

	__productJList.setListData(__listData);
}

/**
Sets up the GUI.
*/
private void setupGUI(String dmiInUse) {
	addWindowListener(this);

	String appName = JGUIUtil.getAppNameForWindows();
	if (appName == null) {
		appName = "";
	}

    JPanel northJPanel = new JPanel();
    northJPanel.setLayout(new GridBagLayout());
    getContentPane().add("North", northJPanel);

	int y = 0;
	JGUIUtil.addComponent(northJPanel, 
		new JLabel("Time series plots are managed as \"time series products\"" +
		" (TSProducts), which have an identifier and name."),
		0, y++, 5, 1, 1, 0, 
		GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST);		
	JGUIUtil.addComponent(northJPanel,
		new JLabel("To manage an existing product, select it and press OK."),
		0, y++, 5, 1, 1, 0, 
		GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST);		
	/*
	JGUIUtil.addComponent(northJPanel, 
		new JLabel(
		"To modify an existing product, select it and press OK." +
		"  Then right-click on the graph and edit its properties."),
		0, y++, 5, 1, 1, 0, 
		GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST);		
	*/
	JGUIUtil.addComponent(northJPanel, 
		new JLabel("Time series products are saved using the current login."),
		0, y++, 5, 1, 1, 0, 
		GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST);		
	JGUIUtil.addComponent(northJPanel, 
		new JLabel( "To modify the permissions or product group for a time series "
		+ "product, right click on the product in the list"),
		0, y++, 5, 1, 1, 0, 
		GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST);		
	JGUIUtil.addComponent(northJPanel, 
		new JLabel("to bring up a menu from which both can be modified."),
		0, y++, 5, 1, 1, 0, 
		GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST);		
	JGUIUtil.addComponent(northJPanel, 
		new JLabel("Depending on the permissions you have for the selected "
		+ "product you may see some of the following behavior:"),
		0, y++, 5, 1, 1, 0, 
		GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST);		
	JGUIUtil.addComponent(northJPanel, 
		new JLabel("- the popup menu will not appear for some products"),
		0, y++, 5, 1, 1, 0, 
		GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST);		
	JGUIUtil.addComponent(northJPanel, 
		new JLabel("- popup menu items may be disabled"),
		0, y++, 5, 1, 1, 0, 
		GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST);		
	JGUIUtil.addComponent(northJPanel, 
		new JLabel("- GUI buttons may be disabled"),
		0, y++, 5, 1, 1, 0, 
		GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST);		

	JGUIUtil.addComponent(northJPanel, 
		new JLabel(" "),
		0, y++, 5, 1, 1, 0, 
		GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST);		

	List v = new Vector();
	DMI dmi = null;
	for (int i = 0; i < __dmis.size(); i++) {
		dmi = (DMI)__dmis.get(i);
		v.add(dmi.getInputName());
	}

	__databaseComboBox = new SimpleJComboBox(v);

	if (dmiInUse != null) {
		__databaseComboBox.select(dmiInUse);
	}

	if (v.size() > 1) {
		JGUIUtil.addComponent(northJPanel, 
			new JLabel("Select the database instance for which to manage TSProducts: "),
			0, y, 1, 1, 0, 0, 
			GridBagConstraints.NONE, GridBagConstraints.WEST);
		JGUIUtil.addComponent(northJPanel,
			__databaseComboBox,
			1, y++, 1, 1, 0, 0,
			GridBagConstraints.NONE, GridBagConstraints.WEST);
		JGUIUtil.addComponent(northJPanel, 
			new JLabel(" "),
			0, y++, 5, 1, 1, 0, 
			GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST);
	}

	__databaseComboBox.addActionListener(this);

	JGUIUtil.addComponent(northJPanel, 
		new JLabel("Select the time series product to open:"),
		0, y, 5, 1, 1, 0, 
		GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST);

	JPanel centerJPanel = new JPanel();
	centerJPanel.setLayout(new GridBagLayout());
	getContentPane().add("Center", centerJPanel);

	__productJList = new SimpleJList();
	__productJList.addMouseListener(this);
	JGUIUtil.addComponent(centerJPanel, new JScrollPane(__productJList),
		0, 0, 1, 1, 1, 1,
		GridBagConstraints.BOTH, GridBagConstraints.NORTHWEST);

        JPanel southJPanel = new JPanel();
        southJPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add("South", southJPanel);

	__deleteButton = new JButton(__BUTTON_DELETE);
	__deleteButton.addActionListener(this);
	southJPanel.add(__deleteButton);
	__deleteButton.setEnabled(false);
	__deleteButton.setToolTipText("<html>Delete the selected<br>TSProduct from the database.</html>");

	__renameButton = new JButton(__BUTTON_RENAME);
	__renameButton.addActionListener(this);
	southJPanel.add(__renameButton);
	__renameButton.setEnabled(false);
	__renameButton.setToolTipText("<html>Open a dialog to change the TSProduct's identifier.</html>");

	__openButton = new JButton(__BUTTON_OPEN);
	__openButton.addActionListener(this);
	southJPanel.add(__openButton);
	__openButton.setEnabled(false);
	__openButton.setToolTipText("<html>Reads the TSProduct and opens it in a graph window.</html>");

	JButton cancelButton = new JButton(__BUTTON_CLOSE);
	cancelButton.addActionListener(this);
	southJPanel.add(cancelButton);
	cancelButton.setToolTipText("Closes this window.");

	if (appName == null || appName.equals("")) {
		setTitle("Manage Time Series Product");
	}
	else {	
		setTitle(appName + " - Manage Time Series Product");
	}		
	
	__popup = new JPopupMenu();
	SimpleJMenuItem mi = new SimpleJMenuItem(__MENU_PERMISSIONS, this);
	__popup.add(mi);

	__productGroupMenuItem = new SimpleJMenuItem(__MENU_PRODUCTGROUP, this);
	__popup.add(__productGroupMenuItem);

	__productJList.addListSelectionListener(this);
	
	pack();
	setSize(getWidth() + 100, 400);
	setVisible(true);
    JGUIUtil.center(this);
}

/**
Shows the products for the DMI with the given Input Name in the list.
@param inputName the inputName of the DMI for which to display the products.
*/
private void showProducts(String inputName) {
	__productJList.removeAll();
	TSProductDMI tspd = null;
	__allTSPs = new Vector();
	__listData = new Vector();
	__rowDMIs = new Vector();

	DMI dmi = null;

	for (int i = 0; i < __dmis.size(); i++) {
		dmi = (DMI)__dmis.get(i);

		if (!dmi.getInputName().equals(inputName)) {
			continue;
		}

		tspd = __dmis.get(i);
		
		try {
			processTSPVectorForList(__dmis.get(i), tspd.readTSProductDMITSProductList(false));
		}
		catch (Exception e) {
			String routine = CLASS + ".constructor";
			Message.printWarning(2, routine, "Error reading from database for TSProductDMI:"
				+ " '" + tspd.getDMIName() + "'.");
			Message.printWarning(3, routine, e);
		}
	}

	__productJList.setListData(__listData);
}	

/**
Responds to changes in the list selection.
@param event the ListSelectionEvent that happened.
*/
public void valueChanged(ListSelectionEvent event) {
	if (__productJList.getSelectedIndex() == -1) {
		__renameButton.setEnabled(false);
		__deleteButton.setEnabled(false);
		__openButton.setEnabled(false);
	}
	else {
		__renameButton.setEnabled(true);
		__deleteButton.setEnabled(true);
		__openButton.setEnabled(true);
	}

	if (!canUpdateCurrentRow(false)) {
		__renameButton.setEnabled(false);
		__deleteButton.setEnabled(false);
		__openButton.setEnabled(false);
	}	
}

/**
Responds to the window closing event.
@param event the window event that happened.
*/
public void windowClosing(WindowEvent event) {
    closeClicked();
}

/**
Responds to the window activated event. Updates the list of products in the database.
@param event the window event that happened.
*/
public void windowActivated(WindowEvent event) {
	if (__productJList != null && __listData != null) {
		__productJList.setListData(__listData);
	}
}

/**
Does nothing.
*/
public void windowClosed(WindowEvent event) {}

/**
Does nothing.
*/
public void windowDeactivated(WindowEvent event) {}

/**
Does nothing.
*/
public void windowDeiconified(WindowEvent event) {}

/**
Does nothing.
*/
public void windowOpened(WindowEvent event) {}

/**
Does nothing.
*/
public void windowIconified(WindowEvent event) {}

}