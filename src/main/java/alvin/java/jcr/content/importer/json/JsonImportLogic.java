package alvin.java.jcr.content.importer.json;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import alvin.java.jcr.content.importer.ImportRuntimeException;
import alvin.java.jcr.content.importer.JcrStringLikeTypes;
import alvin.java.jcr.content.importer.RootPathNotFoundException;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

class JsonImportLogic {

	private Session session;
	private JsonObject json;
	private String rootPath = "/"; 
	
	public void setJsonInput(JsonObject obj) {
		
		json = obj;
	}
	
	public void setJcrSession(Session s) {
		
		session = s;
	}
	
	public void setImportRoot(String path) {
		
		rootPath = path;
	}
	
	public void perform() {
		validatePreImportState();
		
		try {
			_perform();
		} catch (RepositoryException e) {
			throw new ImportRuntimeException("Error writing data to JCR", e);
		}
	}
	
	private void _perform() throws RepositoryException {
		
		Node root = null;
		
		if (session.nodeExists(rootPath)) {
			root = session.getNode(rootPath);
		} else {
			throw new RootPathNotFoundException("Root node does not exist");
		}
		
		handleObject(root, json);
	}

	private void handleObject(Node root, JsonObject obj) 
	throws RepositoryException {
		
		Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();
		
		for (Map.Entry<String, JsonElement> entry : entries) {
			
			JsonElement element = entry.getValue();
			
			if (element.isJsonPrimitive()) {
				handlePrimitive(root, entry.getKey(), element.getAsJsonPrimitive());
			} else if (element.isJsonObject()) {
				String nodeName = entry.getKey();
				JsonObject childNodeJson = element.getAsJsonObject();
				Node newRoot = createNewNodeByReplacing(root, nodeName);
				handleObject(newRoot, childNodeJson.getAsJsonObject());
			} else if (element.isJsonNull()) {
				handleNull(root, entry.getKey(), element.getAsJsonNull());
			}
		}		
	}

	private void handleNull(Node root, String name, JsonNull nullVal) 
	throws RepositoryException {
		
		if (root.hasProperty(name)) {
			root.setProperty(name, (Value)null);
		}
	}

	private void handlePrimitive(Node root, String name, JsonPrimitive primitive)
	throws RepositoryException {
		
		if (primitive.isNumber()) {
			handleNumber(root, name, primitive.getAsNumber());
		} else if (primitive.isBoolean()) {
			handleBoolean(root, name, primitive.getAsBoolean());
		} else if (primitive.isString()) {
			handleString(root, name, primitive.getAsString());
		}
	}
	
	private void handleString(Node root, String name, String val)
	throws RepositoryException {
		
		/* we need further analysis on the type of property this is; Name? Path? etc.*/
		/*Type information is encoded in the property name*/
		PropNameTypeTuple tuple = parseType(name);
		
		switch (tuple.type) {
			case STRING:
				setStringProperty(root, tuple.name, val);
				break;
			case DATE:
				setDateProperty(root, tuple.name, val);
				break;
			case PATH:
				setPathProperty(root, tuple.name, val);
				break;
			case REFERENCE:
				setReferenceProperty(root, tuple.name, val);
				break;
			case NAME:
				setNameProperty(root, tuple.name, val);
				break;
			default:
				throw new ImportRuntimeException("Unsupport type: " + tuple.type);
		}
		
	}

	private void setNameProperty(Node root, String name, String val)
	throws RepositoryException {
		
		root.setProperty(name, val, PropertyType.NAME);
	}

	private void setReferenceProperty(Node root, String name, String val)
	throws RepositoryException {
		
		root.setProperty(name, val, PropertyType.REFERENCE);
	}

	private void setPathProperty(Node root, String name, String val)
	throws RepositoryException{
		
		root.setProperty(name, val, PropertyType.PATH);
	}

	private static final DateTimeFormatter  DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	private void setDateProperty(Node root, String name, String val)
	throws RepositoryException {
		
		DateTime date = null;
		
		try {
			date = DATE_FORMAT.parseDateTime(val);
		} catch (IllegalArgumentException e) {
			throw new ImportRuntimeException("Invalid date string format " + val, e);
		}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date.toDate());
		
		root.setProperty(name, cal);
	}

	private void setStringProperty(Node root, String name, String val)
	throws RepositoryException {
		
		root.setProperty(name, val);
		
	}

	private PropNameTypeTuple parseType(String name) {
		
		int sepIndex = name.lastIndexOf(";");
		PropNameTypeTuple tuple = new PropNameTypeTuple();
		tuple.name = name;
		tuple.type = JcrStringLikeTypes.STRING; //default to string
		
		if (sepIndex > 0) {
			
			String propName = name.substring(0, sepIndex);
			String typeStr = name.substring(sepIndex + 1).toUpperCase();
			JcrStringLikeTypes type = null;
			
			try {
				type = JcrStringLikeTypes.valueOf(typeStr);
			} catch (IllegalArgumentException e) {
				throw new ImportRuntimeException("Type " + typeStr + " is not a known JCR type");
			}
			
			
			tuple.name = propName;
			tuple.type = type;
		}
		
		
		return tuple;
	}

	private void handleBoolean(Node root, String name, boolean val) 
	throws RepositoryException {
		
		root.setProperty(name, val);
	}

	private void handleNumber(Node root, String name, Number number) 
	throws RepositoryException {
	
		//we will guess if a number is double or long.
		String numStr = number.toString();
		if (numStr.indexOf(".") > 0 ) {
			root.setProperty(name, number.doubleValue());
		} else {
			root.setProperty(name, number.longValue());
		}
	}
	
	private Node createNewNodeByReplacing(Node root, String nodeName) throws RepositoryException {
		
		if (root.hasNode(nodeName)) {
			
			Node toDeleteNode = root.getNode(nodeName);
			toDeleteNode.remove();
		}
		
		return root.addNode(nodeName);
	}

	private void validatePreImportState() {
		
		if (session == null || json == null) {
			throw new IllegalStateException("session or json input is null");
		}
	}
	
	private class PropNameTypeTuple {
		
		String name;
		JcrStringLikeTypes type;
	}
}
