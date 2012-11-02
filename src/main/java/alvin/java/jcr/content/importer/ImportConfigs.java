package alvin.java.jcr.content.importer;

public interface ImportConfigs  {

	public ImportConfigs importRoot(String path);
	public ImportConfigs importMode(String mode);
	
	public ImportDestination doneConfigs();
}
