package com.ea.origin.cq5.content.importer;


public interface ImportDestination {

	public ImportConfigs withConcifgs();
	public void doImport() throws ImportRuntimeException;
}
