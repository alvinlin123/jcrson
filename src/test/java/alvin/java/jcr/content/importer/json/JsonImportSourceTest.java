package alvin.java.jcr.content.importer.json;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import org.junit.Test;

import alvin.java.jcr.content.importer.RootPathNotFoundException;

public class JsonImportSourceTest {
	
	@Test(expected=RootPathNotFoundException.class)
	public void testImportToGhostRoot() throws RepositoryException {
		
		JsonImporter importer = new JsonImporter();
		String json = "{ \"node1\":{}}";
		ByteArrayInputStream is = new ByteArrayInputStream(json.getBytes());
		
		Session s = createMock(Session.class);
		expect(s.nodeExists(anyObject(String.class))).andReturn(false).once();
		replay(s);
		importer
			.from(is)
			.importTo(s)
			.withConfigs()
				.importRoot("/non-exist/root")
				.doneConfigs()
			.doImport();
	}
	
	@Test
	public void testImportToNotExistingRoot() throws Exception {
		
		JsonImporter importer = new JsonImporter();
		String json = "{ \"node1\":{}}";
		ByteArrayInputStream is = new ByteArrayInputStream(json.getBytes());
		
		Node newNode = createMock(Node.class);
		replay(newNode);
	
		/* ok to use same node as jcr root and import root */
		Node root = createMock(Node.class);
		expect(root.addNode("node1")).andReturn(newNode).once();
		expect(root.hasNode(anyObject(String.class))).andReturn(false).anyTimes();
		replay(root);
		
		Session s = createMock(Session.class);
		expect(s.nodeExists("/testroot")).andReturn(true).atLeastOnce();
		expect(s.getNode("/testroot")).andReturn(root).once();
		expect(s.getRootNode()).andReturn(root).anyTimes();
		replay(s);
		
		
		importer
			.from(is)
			.importTo(s)
				.withConfigs()
				.importRoot("/testroot")
				.doneConfigs()
			.doImport();
		
		verify(root);
		verify(s);
		verify(newNode);
	}

	
	@Test
	public void testImportSimpleNode() throws Exception {
		
		JsonImporter importer = new JsonImporter();
		String json = "{ \"node1\":{}}";
		ByteArrayInputStream is = new ByteArrayInputStream(json.getBytes());
		
		Node newNode = createMock(Node.class);
		replay(newNode);
		
		Node root = createMock(Node.class);
		expect(root.addNode("node1")).andReturn(newNode).once();
		expect(root.hasNode(anyObject(String.class))).andReturn(false).anyTimes();
		replay(root);
		
		Session s = createMock(Session.class);
		expect(s.nodeExists("/")).andReturn(true).atLeastOnce();
		expect(s.getNode("/")).andReturn(root).anyTimes();
		expect(s.getRootNode()).andReturn(root).anyTimes();
		replay(s);
		
		
		importer
			.from(is)
			.importTo(s)
			.doImport();
		
		verify(root);
		verify(s);
		verify(newNode);
	}
	
	@Test
	public void testImportSimpleNodeWithNumberProperty() throws Exception {
		
		JsonImporter importer = new JsonImporter();
		String json = "{ \"node1\":{\"number\":123,\"number2\":123.0}}";
		ByteArrayInputStream is = new ByteArrayInputStream(json.getBytes());
		
		Node newNode = createMock(Node.class);
		expect(newNode.setProperty("number", 123)).andReturn(null).once();
		expect(newNode.setProperty("number2", 123.0)).andReturn(null).once();
		replay(newNode);
		
		Node root = createMock(Node.class);
		expect(root.addNode("node1")).andReturn(newNode).once();
		expect(root.hasNode(anyObject(String.class))).andReturn(false).anyTimes();
		replay(root);
		
		Session s = createMock(Session.class);
		expect(s.nodeExists("/")).andReturn(true).atLeastOnce();
		expect(s.getNode("/")).andReturn(root).anyTimes();
		expect(s.getRootNode()).andReturn(root).anyTimes();
		replay(s);
		
		
		importer
			.from(is)
			.importTo(s)
			.doImport();
		
		verify(root);
		verify(s);
		verify(newNode);
	}
	
	@Test
	public void testImportSimpleNodeWithBooleanProperty() throws Exception {
		
		JsonImporter importer = new JsonImporter();
		String json = "{ \"node1\":{\"boolean\":true,\"boolean2\":false}}";
		ByteArrayInputStream is = new ByteArrayInputStream(json.getBytes());
		
		Node newNode = createMock(Node.class);
		expect(newNode.setProperty("boolean", true)).andReturn(null).once();
		expect(newNode.setProperty("boolean2", false)).andReturn(null).once();
		replay(newNode);
		
		Node root = createMock(Node.class);
		expect(root.addNode("node1")).andReturn(newNode).once();
		expect(root.hasNode(anyObject(String.class))).andReturn(false).anyTimes();
		replay(root);
		
		Session s = createMock(Session.class);
		expect(s.nodeExists("/")).andReturn(true).atLeastOnce();
		expect(s.getNode("/")).andReturn(root).anyTimes();
		expect(s.getRootNode()).andReturn(root).anyTimes();
		replay(s);
		
		
		importer
			.from(is)
			.importTo(s)
			.doImport();
		
		verify(root);
		verify(s);
		verify(newNode);
	}
	
	@Test
	public void testImportSimpleNodeWithStringProperty() throws Exception {
		
		JsonImporter importer = new JsonImporter();
		String json = "{ \"node1\":{\"string\":\"some string\"}}";
		ByteArrayInputStream is = new ByteArrayInputStream(json.getBytes());
		
		Node newNode = createMock(Node.class);
		expect(newNode.setProperty("string", "some string")).andReturn(null).once();
		replay(newNode);
		
		Node root = createMock(Node.class);
		expect(root.addNode("node1")).andReturn(newNode).once();
		expect(root.hasNode(anyObject(String.class))).andReturn(false).anyTimes();
		replay(root);
		
		Session s = createMock(Session.class);
		expect(s.nodeExists("/")).andReturn(true).atLeastOnce();
		expect(s.getNode("/")).andReturn(root).anyTimes();
		expect(s.getRootNode()).andReturn(root).anyTimes();
		replay(s);
		
		
		importer
			.from(is)
			.importTo(s)
			.doImport();
		
		verify(root);
		verify(s);
		verify(newNode);
	}
	
	@Test
	public void testImportSimpleNodeWithNullValProperty() throws Exception {
		
		JsonImporter importer = new JsonImporter();
		String json = "{ \"node1\":{\"nullVal\":null}}";
		ByteArrayInputStream is = new ByteArrayInputStream(json.getBytes());
		
		Node newNode = createMock(Node.class);
		expect(newNode.hasProperty(anyObject(String.class))).andReturn(true).anyTimes();
		expect(newNode.setProperty("nullVal", (Value)null)).andReturn(null).once();
		replay(newNode);
		
		Node root = createMock(Node.class);
		expect(root.addNode("node1")).andReturn(newNode).once();
		expect(root.hasNode(anyObject(String.class))).andReturn(false).anyTimes();
		replay(root);
		
		Session s = createMock(Session.class);
		expect(s.nodeExists("/")).andReturn(true).atLeastOnce();
		expect(s.getNode("/")).andReturn(root).anyTimes();
		expect(s.getRootNode()).andReturn(root).anyTimes();
		replay(s);
		
		
		importer
			.from(is)
			.importTo(s)
			.doImport();
		
		verify(root);
		verify(s);
		verify(newNode);
	}
	
	@Test
	public void testImportSimpleNodeWithDateProperty() throws Exception {
		
		JsonImporter importer = new JsonImporter();
		String json = "{ \"node1\":{\"date;DATE\":\"2012-11-01T14:13:47.773-07:00\"}}";
		ByteArrayInputStream is = new ByteArrayInputStream(json.getBytes());
		
		Calendar expectedDate = Calendar.getInstance();
		expectedDate.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse("2012-11-01T14:13:47.773-0700"));
		
		Node newNode = createMock(Node.class);
		expect(newNode.hasProperty(anyObject(String.class))).andReturn(true).anyTimes();
		expect(newNode.setProperty("date", expectedDate)).andReturn(null).once();
		replay(newNode);
		
		Node root = createMock(Node.class);
		expect(root.addNode("node1")).andReturn(newNode).once();
		expect(root.hasNode(anyObject(String.class))).andReturn(false).anyTimes();
		replay(root);
		
		Session s = createMock(Session.class);
		expect(s.nodeExists("/")).andReturn(true).atLeastOnce();
		expect(s.getNode("/")).andReturn(root).anyTimes();
		expect(s.getRootNode()).andReturn(root).anyTimes();
		replay(s);
		
		importer
			.from(is)
			.importTo(s)
			.doImport();
		
		verify(root);
		verify(s);
		verify(newNode);
	}
	
	@Test
	public void testImportSimpleNodeWithPathProperty() throws Exception {
		
		JsonImporter importer = new JsonImporter();
		String json = "{ \"node1\":{\"path;PATH\":\"/this/is/a/path\"}}";
		ByteArrayInputStream is = new ByteArrayInputStream(json.getBytes());
		
		Node newNode = createMock(Node.class);
		expect(newNode.hasProperty(anyObject(String.class))).andReturn(true).anyTimes();
		expect(newNode.setProperty("path", "/this/is/a/path", PropertyType.PATH)).andReturn(null).once();
		replay(newNode);
		
		Node root = createMock(Node.class);
		expect(root.addNode("node1")).andReturn(newNode).once();
		expect(root.hasNode(anyObject(String.class))).andReturn(false).anyTimes();
		replay(root);
		
		Session s = createMock(Session.class);
		expect(s.nodeExists("/")).andReturn(true).atLeastOnce();
		expect(s.getNode("/")).andReturn(root).anyTimes();
		expect(s.getRootNode()).andReturn(root).anyTimes();
		replay(s);
		
		importer
			.from(is)
			.importTo(s)
			.doImport();
		
		verify(root);
		verify(s);
		verify(newNode);
	}
	
	@Test
	public void testImportSimpleNodeWithReferenceProperty() throws Exception {
		
		JsonImporter importer = new JsonImporter();
		String json = "{ \"node1\":{\"ref;REFERENCE\":\"reference\"}}";
		ByteArrayInputStream is = new ByteArrayInputStream(json.getBytes());
			
		Node newNode = createMock(Node.class);
		expect(newNode.hasProperty(anyObject(String.class))).andReturn(true).anyTimes();
		expect(newNode.setProperty("ref", "reference", PropertyType.REFERENCE)).andReturn(null).once();
		replay(newNode);
		
		Node root = createMock(Node.class);
		expect(root.addNode("node1")).andReturn(newNode).once();
		expect(root.hasNode(anyObject(String.class))).andReturn(false).anyTimes();
		replay(root);
		
		Session s = createMock(Session.class);
		expect(s.nodeExists("/")).andReturn(true).atLeastOnce();
		expect(s.getNode("/")).andReturn(root).anyTimes();
		expect(s.getRootNode()).andReturn(root).anyTimes();
		replay(s);
		
		importer
			.from(is)
			.importTo(s)
			.doImport();
		
		verify(root);
		verify(s);
		verify(newNode);
	}
	
	@Test
	public void testImportSimpleNodeWithNameProperty() throws Exception {
		
		JsonImporter importer = new JsonImporter();
		String json = "{ \"node1\":{\"name;NAME\":\"name\"}}";
		ByteArrayInputStream is = new ByteArrayInputStream(json.getBytes());
			
		Node newNode = createMock(Node.class);
		expect(newNode.hasProperty(anyObject(String.class))).andReturn(true).anyTimes();
		expect(newNode.setProperty("name", "name", PropertyType.NAME)).andReturn(null).once();
		replay(newNode);
		
		Node root = createMock(Node.class);
		expect(root.addNode("node1")).andReturn(newNode).once();
		expect(root.hasNode(anyObject(String.class))).andReturn(false).anyTimes();
		replay(root);
		
		Session s = createMock(Session.class);
		expect(s.nodeExists("/")).andReturn(true).atLeastOnce();
		expect(s.getNode("/")).andReturn(root).anyTimes();
		expect(s.getRootNode()).andReturn(root).anyTimes();
		replay(s);
		
		importer
			.from(is)
			.importTo(s)
			.doImport();
		
		verify(root);
		verify(s);
		verify(newNode);
	}
	
	@Test
	public void testSomewhatComplexNodeStructure() throws Exception {
		
		JsonImporter importer = new JsonImporter();
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream is = cl.getResourceAsStream("alvin/java/jcr/content/importer/json/somewhatComplex.json");
		
		assertTrue(is != null);
		
		Node applianceNode = createMock(Node.class);
		expect(applianceNode.setProperty("jcr:primaryType", "sling:Folder")).andReturn(null).once();
		replay(applianceNode);
		
		Node macProNode = createMock(Node.class);
		expect(macProNode.setProperty("price", 1999.99)).andReturn(null).once();
		replay(macProNode);
		
		Node macNode = createMock(Node.class);
		expect(macNode.setProperty("jcr:primaryType", "sling:Folder")).andReturn(null).once();
		expect(macNode.hasNode("macbook-pro")).andReturn(false).once();
		expect(macNode.addNode("macbook-pro")).andReturn(macProNode).once();
		replay(macNode);
		
		Node computerNode = createMock(Node.class);
		expect(computerNode.setProperty("jcr:primaryType", "sling:Folder")).andReturn(null).once();
		expect(computerNode.hasNode("mac")).andReturn(false).once();
		expect(computerNode.addNode("mac")).andReturn(macNode).once();
		replay(computerNode);
		
		Node productsNode = createMock(Node.class);
		expect(productsNode.setProperty("jcr:primaryType", "sling:Folder")).andReturn(null).once();
		expect(productsNode.addNode("computer")).andReturn(computerNode).once();
		expect(productsNode.addNode("appliance")).andReturn(applianceNode).once();
		expect(productsNode.hasNode("computer")).andReturn(false).once();
		expect(productsNode.hasNode("appliance")).andReturn(false).once();
		replay(productsNode);
		
		
		Node root = createMock(Node.class);
		expect(root.hasNode("products")).andReturn(false).anyTimes();
		expect(root.addNode("products")).andReturn(productsNode).once();
		replay(root);
		
		Session s = createMock(Session.class);
		expect(s.nodeExists("/")).andReturn(true).atLeastOnce();
		expect(s.getNode("/")).andReturn(root).anyTimes();
		expect(s.getRootNode()).andReturn(root).anyTimes();
		replay(s);
		importer
			.from(is)
			.importTo(s)
			.doImport();
		
		verify(root);
		verify(s);
		verify(applianceNode);
		verify(computerNode);
		verify(productsNode);
	}
}
