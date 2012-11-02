package com.ea.origin.cq5.content.importer.json;

import javax.jcr.Session;

import com.ea.origin.cq5.content.importer.ImportConfigs;
import com.ea.origin.cq5.content.importer.ImportDestination;
import com.ea.origin.cq5.content.importer.ImportRuntimeException;
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
	public ImportConfigs withConcifgs() {
		
		return this;
	}
}
