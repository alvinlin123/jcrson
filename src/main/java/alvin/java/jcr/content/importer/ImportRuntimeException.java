package alvin.java.jcr.content.importer;

public class ImportRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -3794066176167553346L;

	public ImportRuntimeException(String msg, Throwable cause) {
		
		super(msg, cause);
	}
	
	public ImportRuntimeException(String msg) {
		
		super(msg);
	}
}
