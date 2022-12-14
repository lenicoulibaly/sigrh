package dgmp.sigrh.archivemodule.model.constants;

import java.util.Arrays;
import java.util.List;

public class ArchivageConstants
{
	public static final String UPLOADS_DIR = System.getProperty("user.home") + "\\workspace\\sigrh\\docs\\uploads";

	public static final String AGENT_UPLOADS_DIR = UPLOADS_DIR + "\\agent";
	public static final long UPLOAD_MAX_SIZE = 1 * 1024 * 1024;;
	public static final List<String> PHOTO_AUTHORIZED_TYPE = Arrays.asList("jpeg", "jpg", "png");
	public static final List<String> DOCUMENT_AUTHORIZED_TYPE = Arrays.asList("jpeg", "jpg", "png", "pdf");
	
	public static void main(String[] args) {
		System.out.println("user.home = "+ System.getProperty("user.home"));
		System.out.println("UPLOADS_DIR = "+ UPLOADS_DIR);
		System.out.println("AGENT_UPLOADS_DIR = "+ AGENT_UPLOADS_DIR);
	}
}
