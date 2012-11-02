package alvin.java.jcr.content.importer.json;

import javax.jcr.Session;

import alvin.java.jcr.content.importer.ImportConfigs;
import alvin.java.jcr.content.importer.ImportDestination;
import alvin.java.jcr.content.importer.ImportRuntimeException;

import com.google.gson.JsonObject;

class JsonImportDestination implements ImportDestination, ImportConfigs {

	
	private JsonImportLogic _importLogic = new JsonImportLogic();
	
	public void setSource(JsonObject json) {
		
		_importLogic.setJsonInput(json);
	}
	
	public void setDestination(Session s) {
		
		_importLogic.setJcrSession(s);
	}
		
	@Override
	public void doImport() throws ImportRuntimeException {
		
		_importLogic.perform();
	}

	@Override
	public ImportConfigs importRoot(String path) {
		
		_importLogic.setImportRoot(path);
		
		return this;
	}

	@Override
	public ImportConfigs withConfigs() {
		
		return this;
	}
	
	@Override
	public ImportDestination doneConfigs() {
		
		return this;
	}

	@Override
	public ImportConfigs importMode(String mode) {
		
		throw new UnsupportedOperationException("This configuration is not implemented yet");
	}
	
	
}
