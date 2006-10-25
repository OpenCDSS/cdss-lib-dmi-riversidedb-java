/**
Deletes records from the TSProduct and TSProductProps tables for the TSProduct
with the specified identifier and the user with the specified user_num.<p>
@param identifier the identifier String of the TSProduct in the database.
@return a 3-element array.  The first element contains the number of records
deleted from the TSProduct table.  The second element contains the number of 
records deleted from the TSProductProps table.  The third element contains the
total number of records deleted.
*/
public int[] deleteTSProductForIdentifier(String identifier)
throws Exception {
	int[] deleted = new int[3];

	RiversideDB_TSProduct tsp = readTSProductForIdentifier(identifier);
	if (tsp == null) {
		deleted[0] = 0;
		deleted[1] = 0;
		deleted[2] = 0;
		return deleted;
	}
	
	int tsproduct_num = tsp.getTSProduct_num();	
			
	int propsCount = deleteTSProductPropsForTSProduct_num(tsproduct_num);

	String sql = "DELETE FROM tsproduct WHERE "
		+ "tsproduct.identifier = '"
		+ identifier + "'";
	deleted[0] = dmiDelete(sql);
	deleted[1] = propsCount;
	deleted[2] = deleted[0] + deleted[1];

	return deleted;
}

public int[] deleteTSProductForTSProduct_num(int tsproduct_num)
throws Exception {
	int[] deleted = new int[3];

	int propsCount = deleteTSProductPropsForTSProduct_num(tsproduct_num);

	String sql = "DELETE FROM tsproduct WHERE "
		+ "tsproduct.tsproduct_num = " + tsproduct_num;
	deleted[0] = dmiDelete(sql);
	deleted[1] = propsCount;
	deleted[2] = deleted[0] + deleted[1];

	return deleted;
}

/**
Deletes records from the TSProductProps tables for the the tsproduct with the
specified TSProduct_num.<p>
@param tsproduct_num the TSProduct_num of the records to delete.
@return the number of records to be deleted.
*/
public int deleteTSProductPropsForTSProduct_num(int tsproduct_num) 
throws Exception {
	DMIDeleteStatement del = new DMIDeleteStatement(this);
		
	String sql = "DELETE FROM TSProductProps WHERE "
		+ "TSProductProps.TSProduct_num = " + tsproduct_num;
	return dmiDelete(sql);
}

/**
Reads the TS Product table for the record that matches the specified identifier
and user_num.<p>
@param identifier the identifier for which to search.
@return a RiversideDB_TSProduct object, or null if no matching records could be
found.
@throws Exception if an error occurs.
*/
public RiversideDB_TSProduct readTSProductForIdentifier(String identifier)
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_TSPRODUCT);
	
	q.addWhereClause("TSProduct.identifier = '" + identifier + "'");
	
	ResultSet rs = dmiSelect(q);
	Vector v = toTSProductList(rs);
	closeResultSet(rs);

	if (v == null || v.size() == 0) {
		return null;
	}
	else {
		return (RiversideDB_TSProduct)v.elementAt(0);
	}
}

/**
Reads the TS Product table for all records that matches the specified 
tsproduct_num, productgroup_num.
@param tsproduct_num the tsproduct_num for which to search.  If not missing,
then productgroup_num MUST be missing.
@param productgroup_num the productgroup_num for which to search.  If not 
missing, then tsproduct_num MUST be missing.
@return a Vector of matching HydroBase_TSProduct records.
@throws Exception if an error occurs.
*/
public Vector readTSProductListForTSProduct_numProductGroup_numUser_num(
int tsproduct_num, int productgroup_num)
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_TSPRODUCT);

	if (!DMIUtil.isMissing(tsproduct_num)
	    && !DMIUtil.isMissing(productgroup_num)) {
	    	throw new Exception(
			"One and only one value can be non-missing."
			+ "  TSProduct_num: " + tsproduct_num 
			+ "  ProductGroup_num: " + productgroup_num);
	}
	
	if (DMIUtil.isMissing(tsproduct_num)
	    && DMIUtil.isMissing(productgroup_num)) {
		// query for all
	}
	else if (!DMIUtil.isMissing(tsproduct_num)) {
		q.addWhereClause("TSProduct.tsproduct_num = " 
			+ tsproduct_num);
	}
	else {
		q.addWhereClause("TSProduct.productgroup_num = " 
			+ productgroup_num);
	}

	ResultSet rs = dmiSelect(q);
	Vector v = toTSProductList(rs);
	closeResultSet(rs);
	return v;
}


/**
Reads the TS Product Props table for all records that matches the specified 
tsproduct_num.<p>
@param tsproduct_num the tsproduct_num to match props by.
@return a Vector of the matching RiversideDB_TSProductProps records.
@throws Exception if an error occurs.
*/
public Vector readTSProductPropsListForTSProduct_num(int tsproduct_num) 
throws Exception {
	DMISelectStatement q = new DMISelectStatement(this);
	buildSQL(q, _S_TSPRODUCTPROPS);
	if (tsproduct_num < 0) {
		throw new Exception("TSProduct_num (" + tsproduct_num + ") "
			+ "must be greater than -1");
	}
	q.addWhereClause("TSProductProps.tsproduct_num = " + tsproduct_num);
	ResultSet rs = dmiSelect(q);
	// checked the stored procedure
	Vector v = toTSProductPropsList(rs);
	closeResultSet(rs);
	return v;
}

/**
Writes a TSProduct to the database.  Writes records to both the
TSProduct and TSProductProps tables.  From the TSProductDMI interface.<p>
@param product the TSProduct to write to the table.
@return true if the TSProduct was written successfully, false if not.
*/
public boolean writeTSProduct(TSProduct tsproduct) {
	String routine = "RiversideDB_DMI.writeTSProduct";
	
	// First, check to see if the TSProduct already exists in the database.

	String id = tsproduct.getPropValue("Product.ProductID");

	RiversideDB_TSProduct tsp = null;
	
	try {
		tsp = readTSProductForIdentifier(id);
	}
	catch (Exception e) {
		Message.printWarning(1, routine, "Error reading records from "
			+ "database.");
		Message.printWarning(2, routine, e);
		return false;
	}

	boolean createNew = false;
	if (tsp == null) {
		// if the tsp object read from the database above is null, 
		// then there are no matching records in TSProduct for the 
		// user and the product id.
		createNew = true;
		tsp = new RiversideDB_TSProduct();

		Vector choices = null;
		try {
			choices = getProductGroupsList();
		}
		catch (Exception e) {
			Message.printWarning(2, routine,	
				"Could not determine product groups for user.");
			Message.printWarning(2, routine, e);
			return false;
		}

		if (choices.size() == 0) {
			new ResponseJDialog(new javax.swing.JFrame(), 
				"No Permissions to Write",
				"You do not have permission to write a "
				+ "TSProduct for any of the\n"
				+ "Product Groups in the database. You can "
				+ "save the TSProduct to a\n"
				+ "TSProduct file, instead.",
				ResponseJDialog.OK);
			return false;
		}

		String group = (new JComboBoxResponseJDialog(
			new javax.swing.JFrame(), "Select Product Group",
			"Select the Product Group for which to save the "
			+ "TSProduct.",
			choices, ResponseJDialog.OK | ResponseJDialog.CANCEL))
			.response();

		if (group == null) {
			return false;
		}

		int index = group.indexOf("-");
		String s = group.substring(0, index).trim();

		tsp.setProductGroup_num(StringUtil.atoi(s));
	}
	else {
		RiversideDB_DBUser dbuser = _dbuser;
		int userNum = dbuser.getDBUser_num();
		int groupNum = dbuser.getPrimaryDBGroup_num();
	
		boolean canWrite = false;
	
		if (dbuser.getLogin().trim().equalsIgnoreCase("root")) {
			// root can do whatever root wants
			canWrite = true;
		}
		else {
			String p = tsp.getDBPermissions();
			if (userNum == tsp.getDBUser_num()) {
				if (p.indexOf("UU+") > -1) {
					canWrite = true;	
				}
			}
			else if (groupNum == tsp.getDBGroup_num()) {
				if (p.indexOf("GU+") > -1) {
					canWrite = true;	
				}
			}
			else {
				if (p.indexOf("OU+") > -1) {
					canWrite = true;	
				}
			}
		}

		if (!canWrite) {
			new ResponseJDialog(new javax.swing.JFrame(),
				"Invalid Permissions",
				"A TSProduct with the identifier '" 
				+ tsp.getIdentifier() + "' already exists in\n"
				+ "the database, but you do not have "
				+ "permissions to change it.\nSave the product "
				+ "under another identifier.",
				ResponseJDialog.OK);
			return false;
		}
	}
	

	tsp.setName(tsproduct.getPropValue("Product.ProductName"));

	// to keep track of whether a new TSProduct record will need to be
	// created ....

	
	// otherwise, check the ProductIDOriginal property in the product.  This
	// is a property only used internally and currently only set by the
	// HydroBase_GUI_SelectTSProduct screen when opening a TSProduct.  It
	// is never saved to persistent storage.

	// This property is the original TSProduct ID of a TSProduct when that
	// product was opened on the SelectTSProduct screen.  If the original
	// ID is not equal to the current ID, then the ID was changed in the
	// properties screen and the TSProduct must be saved to a new record.

	// REVISIT (JTS - 2005-08-19)
	// this code is HydroBase-specific right now but I am leaving it in
	// *for now*.  I think that in the future there might be call to use
	// it in some RiversideDB apps, and if it's here already we probably
	// won't spend as much time reinventing the wheel when we come to 
	// that bridge.
	
	String orig = tsproduct.getPropValue("Product.ProductIDOriginal");
	if (orig == null) {
		orig = "";
	}
	else {
		orig = orig.trim();
	}

	if (!orig.equalsIgnoreCase(id)) {
		createNew = true;
	}

	int tsproduct_num = -1;
	if (!createNew) {
		tsproduct_num = tsp.getTSProduct_num();
		tsp.setName(tsproduct.getPropValue("Product.ProductName"));
		
		// for simplicity's sake, when re-writing an existing TSProduct
		// to the database, all its properties are simply wiped out
		// and rewritten. 

		try {
			deleteTSProductPropsForTSProduct_num(
				tsp.getTSProduct_num());
		}
		catch (Exception e) {
			Message.printWarning(1, routine, 
				"Error deleting records from database.");
			Message.printWarning(2, routine, e);
			return false;
		}
	}
	else {
		// if creating a new TSProduct for the specified ID and user_num
		// this first deletes any records belonging to that ID and 
		// user_num.  Easier than just overwriting them and hoping all
		// the records are caught.
		try {
			deleteTSProductForIdentifier(id);
		}
		catch (Exception e) {
			Message.printWarning(1, routine, 
				"Error deleting records from database.");
			Message.printWarning(2, routine, e);
			return false;
		}
	
		// Writes a new TSProduct record into the database.
		String sql = null;
		sql = "INSERT INTO TSProduct (ProductGroup_num, Identifier, "
			+ "Name, DBUser_num,DBGroup_num, DBPermissions) "
			+ "VALUES ("
			+ tsp.getProductGroup_num() + ", '" + id + "', '" + 
			tsproduct.getPropValue("Product.ProductName") + "',"
			+ _dbuser.getDBUser_num() + ", " 
			+ _dbuser.getPrimaryDBGroup_num() + ", '"
			+ _dbuser.getDefault_DBPermissions() + "')";
		try {
			dmiWrite(sql);
		}
		catch (Exception e) {
			Message.printWarning(1, routine, 
				"Error writing to database.");
			Message.printWarning(2, routine, e);
			return false;
		}
	
		tsproduct_num = (int)getMaxRecord("TSProduct", "TSProduct_num");
	}	
	
	// Now write all the properties from the TSProduct to the database.

	// returns all the properties, even the override properties, in one
	// single Vector.
	Vector v = tsproduct.getAllProps();

	int count = 1;		// used to keep track of the sequence number
				// of the property in the database
	int size = v.size();
	Prop p = null;
	String sql = null;
	try {
		// loop through and write almost all of them out.
		for (int i = 0; i < size; i++) {
			p = (Prop)v.elementAt(i);
			if (p.getHowSet() == Prop.SET_AS_RUNTIME_DEFAULT) {
				// do not store properties that are runtime
				// defaults.  They will be set automatically
				// next time at runtime.
			}
			else if (p.getValue().toUpperCase().endsWith(
				"PRODUCTIDORIGINAL")) {
				// This property is never stored.
			}
			else {
				sql = "INSERT INTO TSProductProps Values ("
					+ tsproduct_num + ", '" 
					+ p.getKey() + "', "
					+ count + ", '" + p.getValue() + "')";
				dmiWrite(sql);
				count++;
			}
		}
	}
	catch (Exception e) {
		Message.printWarning(1, routine, "Error writing to database.");
		Message.printWarning(2, routine, e);
		return false;
	}

	return true;
}

/**
Writes a RiversideDB_TSProduct record to the database.<p>
This is called by:<ul>
@param tsp the RiversideDB_TSProduct to write.
@return -1 if the tsproduct already exists in the database, but if a new record
is being added, returns the number of the new record's TSProduct_num.
@throws Exception if an error occurs.
*/
public int writeTSProduct(RiversideDB_TSProduct tsp) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	int action = 0;
	if (DMIUtil.isMissing(tsp.getTSProduct_num())) {
		action = _W_TSPRODUCT_INSERT;
	}
	else {
		action = _W_TSPRODUCT_UPDATE;
	}

	buildSQL(w, action);
	w.addValue(tsp.getProductGroup_num());
	w.addValue(tsp.getIdentifier());
	w.addValue(tsp.getName());
	w.addValue(tsp.getComment());
	w.addValue(tsp.getDBUser_num());
	w.addValue(tsp.getDBGroup_num());
	w.addValue(tsp.getDBPermissions());

	if (action == _W_TSPRODUCT_INSERT) {
		// need to create a new record
		dmiWrite(w, DMI.INSERT);
		return (int)getMaxRecord("TSProduct", "TSProduct_num");
	}
	else {
		// update an existing record
		w.addWhereClause("TSProduct.TSProduct_num = " 
			+ tsp.getTSProduct_num());
		dmiWrite(w, DMI.UPDATE);
		return -1;
	}
}

/**
Writes a RiversideDB_TSProductProps object to the database.<p>
@param tspp the RiversideDB_TSProductProps object to write.
@throws Exception if an error occurs.
@deprecated because it's old
*/
public void writeTSProductProps(RiversideDB_TSProductProps tspp) 
throws Exception {
	DMIWriteStatement w = new DMIWriteStatement(this);

	buildSQL(w, _W_TSPRODUCTPROPS);

	w.addValue(tspp.getTSProduct_num());
	w.addValue(tspp.getProperty());
	w.addValue(tspp.getValue());
	w.addValue(tspp.getSequence());

	w.addWhereClause("TSProductProps.TSProduct_num = " 
		+ tspp.getTSProduct_num());
	w.addWhereClause("TSProductProps.Property = '" 
		+ tspp.getProperty() + "'");

	dmiWrite(w, DMI.UPDATE_INSERT);
}


/**
Returns a Vector of the product groups in the database, in the form:<br>
[ProductGroup_num] - [ProductGroup Identifier] - [ProductGroup Name]
@return a Vector of the product groups in the database.
*/
protected Vector getProductGroupsList() 
throws Exception {
/*
JTS - 2005-08-24
See comment below.
	boolean root = false;

	RiversideDB_DBUser dbuser = _dbuser;
	
	if (dbuser.getLogin().trim().equalsIgnoreCase("root")) {
		root = true;
	}

	int userNum = dbuser.getDBUser_num();
	int groupNum = dbuser.getPrimaryDBGroup_num();
*/

	Vector groups = new Vector();

	Vector v = readProductGroupList();
	
	int size = v.size();
	int pgGroupNum = 0;
	int pgUserNum = 0;
	RiversideDB_ProductGroup pg = null;
	String p = null;
	for (int i = 0; i < size; i++) {
		pg = (RiversideDB_ProductGroup)v.elementAt(i);
		pgGroupNum = pg.getDBGroup_num();
		pgUserNum = pg.getDBUser_num();

		groups.add("" + pg.getProductGroup_num()
			+ " - " + pg.getIdentifier() + " - "
			+ pg.getName());

/*
JTS - 2005-08-24
Originally, I had though that the permissions controls in the ProductGroup
table could be used to control who is allowed to add/delete/etc records 
related to that product group, but on further examination of the permissions
system it's evident that those permissions simply control permissions on the
specific record in the ProductGroup table.  Therefore, there's no way to control
who can add TSProducts to certain groups.  The user and group numbers could be
checked, but at least half the current users in RiversideDB don't have 
corresponding records in the ProductGroup table.  This means they could go 
through the effort of creating a TSProduct and then try to save it and not
have any valid ProductGroups be found.   For now it seems best to simply allow
them to save as a certain ProductGroup -- this won't control their permissions
to edit other TSProducts in the same group.  

So ... leaving this code in here in case I need to come back to it and use it
somehow:

		if (root) {
			groups.add("" + pg.getProductGroup_num()
				+ " - " + pg.getIdentifier() + " - "
				+ pg.getName());
			continue;
		}

		p = pg.getDBPermissions().toUpperCase();

		if (p.indexOf("UI+") > -1 || p.indexOf("UC+") > -1
		    || p.indexOf("UU+") > -1) {
			if (userNum == pgUserNum) {
				groups.add("" + pg.getProductGroup_num()
					+ " - " + pg.getIdentifier() + " - "
					+ pg.getName());
				continue;
			}
		}
		
		if (p.indexOf("GI+") > -1 || p.indexOf("GC+") > -1
		    || p.indexOf("GU+") > -1) {
			if (groupNum == pgGroupNum) {
				groups.add("" + pg.getProductGroup_num()
					+ " - " + pg.getIdentifier() + " - "
					+ pg.getName());
				continue;
			}
		}

		if (p.indexOf("OI+") > -1 || p.indexOf("OC+") > -1
		    || p.indexOf("OU+") > -1) {
			groups.add("" + pg.getProductGroup_num()
				+ " - " + pg.getIdentifier() + " - "
				+ pg.getName());
			continue;
		}
*/
	}

	return groups;
}

/**
Returns a list of the TSProducts in the database, filtered for those that the
currently-logged in user can read, edit, or delete.
@return a list of the TSProducts in the database.
*/
public Vector getTSProductList() {
	try {
		Vector v = readTSProductList();
		int size = v.size();
		RiversideDB_TSProduct tsp = null;
		Vector ret = new Vector();
		
		for (int i = 0; i < size; i++) {
			tsp = (RiversideDB_TSProduct)v.elementAt(i);
			if (tsp.getDBUser_num() == _dbuser.getDBUser_num()) {
				ret.add(tsp);
			}
			else {
				String p = tsp.getDBPermissions().toUpperCase();
				if (tsp.getDBGroup_num() 
				    == _dbuser.getPrimaryDBGroup_num()
				    && (p.indexOf("GD+") > -1
				     || p.indexOf("GU+") > -1
				     || p.indexOf("GR+") > -1)) {
				     	ret.add(tsp);
				}
				else if (p.indexOf("OD+") > -1
				      || p.indexOf("OU+") > -1
				      || p.indexOf("OR+") > -1) {
				      	ret.add(tsp);
				}
			}
		}

		return ret;
	}
	catch (Exception e) {
		Message.printWarning(2, "getTSProductList", 
			"Error reading time series.");
		Message.printWarning(2, "getTSProductList", e);
		return new Vector();
	}
}

/**
Updates the Identifier of all TSProduct records having the given TSProduct_num.
@param tsproduct_num the TSProduct_num to match
@param id the new identifier to update the matching records with.
@throws Exception if there is an error updating the database.
*/
public int updateTSProductIdentifierForTSProduct_num(int tsproduct_num, 
String id)
throws Exception {
	String dmiString = "UPDATE tsproduct SET identifier='" + id 
		+ "' where tsproduct_num = " + tsproduct_num;
	return dmiWrite(dmiString);
}

/**
Updates the Permissions of all TSProduct records having the given TSProduct_num.
@param tsproduct_num the TSProduct_num to match
@param id the new identifier to update the matching records with.
@throws Exception if there is an error updating the database.
*/
public int updateTSProductDBPermissionsForTSProduct_num(int tsproduct_num, 
String permissions)
throws Exception {
	String dmiString = "UPDATE tsproduct SET dbpermissions ='" + permissions
		+ "' where tsproduct_num = " + tsproduct_num;
	return dmiWrite(dmiString);
}

/**
Updates the ProductGroup_num of all TSProduct records having the given 
TSProduct_num.
@param tsproduct_num the TSProduct_num to match
@param id the new identifier to update the matching records with.
@throws Exception if there is an error updating the database.
*/
public int updateTSProductProductGroup_numForTSProduct_num(int tsproduct_num, 
int productgroup_num)
throws Exception {
	String dmiString = "UPDATE tsproduct SET ProductGroup_num=" 
		+ productgroup_num 
		+ " where tsproduct_num = " + tsproduct_num;
	return dmiWrite(dmiString);
}

