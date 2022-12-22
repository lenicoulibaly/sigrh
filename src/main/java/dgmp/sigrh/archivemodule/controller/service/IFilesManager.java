package dgmp.sigrh.archivemodule.controller.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFilesManager {

	void uploadFile(MultipartFile file, String destinationPath) throws RuntimeException;
	public byte[] downloadFile(String filePAth);

    String generatePath(MultipartFile file, String typeCode, String agentName, Long agtId, Long archiveId);

	String generateHistoPath(MultipartFile file, String typeCode, String agentName, Long agtId, Long archiveId, Long histoId);

	void renameFile(String oldPath, String newPath);
}
