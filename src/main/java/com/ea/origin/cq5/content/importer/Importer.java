package com.ea.origin.cq5.content.importer;

import java.io.InputStream;

public interface Importer {

	public ImportSource from(InputStream is);
}
