package com.ea.origin.cq5.content.importer.json;

import java.io.InputStream;

import com.ea.origin.cq5.content.importer.ImportSource;
import com.ea.origin.cq5.content.importer.Importer;

public class JsonImporter implements Importer {


	@Override
	public ImportSource from(InputStream is) {
		
		JsonImportSource src = new JsonImportSource(is);
		
		return src;
	}
}
