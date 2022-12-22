package dgmp.sigrh.archivemodule.controller.service;

import dgmp.sigrh.archivemodule.model.constants.ArchivageConstants;
import dgmp.sigrh.archivemodule.model.enums.ArchiveTypes;
import dgmp.sigrh.shared.controller.exception.AppException;
import dgmp.sigrh.shared.utilities.StringUtils;
import dgmp.sigrh.typemodule.controller.repositories.TypeRepo;
import dgmp.sigrh.typemodule.model.enums.TypeGroup;
import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

@Component
public class FilesManager implements IFilesManager
{
	@Override
	public void uploadFile(MultipartFile file, String destinationPath) throws RuntimeException
	{
		try
		{
			Files.write(Paths.get(destinationPath), file.getBytes());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public byte[] downloadFile(String filePAth)
	{
		File file = new File(filePAth);
		Path path = Paths.get(file.toURI());
		try
		{
			return Files.readAllBytes(path);
		} catch (IOException e)
		{
			e.printStackTrace();
			throw new AppException("Erreur de téléchargement");
		}
	}
	@Override
	public String generatePath(MultipartFile file, String typeCode, String agentName, Long agtId, Long archiveId)
	{
		if(ArchiveTypes.getByUniqueCode(typeCode) == null) return "";
		return ArchivageConstants.AGENT_UPLOADS_DIR + "\\" + typeCode + "\\" + ArchiveTypes.getByUniqueCode(typeCode).getDescription().replace(" ", "_").toUpperCase(Locale.ROOT) + "_" +
				StringUtils.stripAccents(agentName).replace(" ", "_") + "_" + agtId + "_" + archiveId + "." + FilenameUtils.getExtension(file.getOriginalFilename());
	}

	@Override
	public String generateHistoPath(MultipartFile file, String typeCode, String agentName, Long agtId, Long archiveId, Long histoId)
	{
		if(ArchiveTypes.getByUniqueCode(typeCode) == null) return "";
		return ArchivageConstants.AGENT_UPLOADS_DIR + "\\" + typeCode + "\\" + ArchiveTypes.getByUniqueCode(typeCode).getDescription().replace(" ", "_").toUpperCase(Locale.ROOT) + "_" +
				StringUtils.stripAccents(agentName).replace(" ", "_") + "_" + agtId + "_" + archiveId + "_Histo_" + histoId + "." + FilenameUtils.getExtension(file.getOriginalFilename());
	}

	@Override
	public void renameFile(String oldPath, String newPath)
	{
		if(new File(oldPath).exists())
		{
			try {
				Files.move(Paths.get(oldPath), Paths.get(newPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Bean
	CommandLineRunner createSystemDirectories(TypeRepo typeRepo)
	{
		return(args)->
		{
			File agtUploadDir= new File(ArchivageConstants.AGENT_UPLOADS_DIR);
			if(!agtUploadDir.exists()) agtUploadDir.mkdirs();
			typeRepo.findByTypeGroup(TypeGroup.ARCHIVE).forEach(type->
			{
				File typeDir = new File(ArchivageConstants.AGENT_UPLOADS_DIR + "\\" + type.getUniqueCode()) ;
				if(!typeDir.exists()) typeDir.mkdirs();
			});
		};
	}
}
