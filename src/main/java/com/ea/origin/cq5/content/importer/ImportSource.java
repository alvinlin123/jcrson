package com.ea.origin.cq5.content.importer;

import javax.jcr.Session;

public interface ImportSource {

	public ImportDestination importTo(Session s);
}
