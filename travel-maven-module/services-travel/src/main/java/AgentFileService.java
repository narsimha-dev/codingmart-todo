import org.springframework.web.multipart.MultipartFile;

public interface AgentFileService {

    AgentFile storeFile(MultipartFile file, Long userId, Long agentId);

    AgentFile getFile(String fileId);
}
