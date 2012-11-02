package alvin.java.jcr.content.importer;


public interface ImportDestination {

	public ImportConfigs withConfigs();
	public void doImport() throws ImportRuntimeException;
}
