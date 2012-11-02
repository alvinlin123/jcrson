package alvin.java.jcr.content.importer;

import java.io.InputStream;

public interface Importer {

	public ImportSource from(InputStream is);
}
