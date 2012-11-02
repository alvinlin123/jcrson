package alvin.java.jcr.content.importer;


public interface ImportDestination {

	public ImportConfigs withConcifgs();
	public void doImport() throws ImportRuntimeException;
}
