package alvin.java.jcr.content.importer;

import javax.jcr.Session;

public interface ImportSource {

	public ImportDestination importTo(Session s);
}
