package com.ea.origin.cq5.content.importer.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.jcr.Session;

import com.ea.origin.cq5.content.importer.ImportDestination;
import com.ea.origin.cq5.content.importer.ImportRuntimeException;
import com.ea.origin.cq5.content.importer.ImportSource;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

class JsonImportSource implements ImportSource {

	private JsonObject json;
	
	public JsonImportSource(InputStream is) {
		
		if (is == null) {
			throw new IllegalArgumentException("Given inputstream cannot be null");
		}
		
		try {
			json = parseToJson(is);
		} catch (IOException e) {
			throw new ImportRuntimeException("Error reading json input stream", e);
		}
	}
	
	@Override
	public ImportDestination importTo(Session s) {
				
		JsonImportDestination dest = new JsonImportDestination();
		dest.setDestination(s);
		dest.setSource(json);
		
		return dest;
	}
	
	private JsonObject parseToJson(InputStream is) throws IOException {

		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(new InputStreamReader(is));
		
		return element.getAsJsonObject();
	}
}
