package alvin.java.jcr.content.importer.json;

import java.io.InputStream;

import alvin.java.jcr.content.importer.ImportSource;
import alvin.java.jcr.content.importer.Importer;

public class JsonImporter implements Importer {


	@Override
	public ImportSource from(InputStream is) {
		
		JsonImportSource src = new JsonImportSource(is);
		
		return src;
	}
}
