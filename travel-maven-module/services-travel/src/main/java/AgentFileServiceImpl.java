import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
public class AgentFileServiceImpl implements AgentFileService {

    @Autowired
    AgentFileRepository agentFileRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AgentRepository agentRepository;

    private boolean findByUserIdAndAgentId(Long userId) {
        Optional<Long> validateId = userRepository.findById(userId).map(User::getId);
        if (validateId.isPresent())
            return true;
        return false;
    }

    @Override
    public AgentFile storeFile(MultipartFile file, Long userId, Long agentId) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            AgentFile dbFile = new AgentFile(fileName, file.getContentType(), file.getBytes());
            if(findByUserIdAndAgentId(userId)) {
                Optional<Agents> getAgentDetails = agentRepository.findById(agentId);
            getAgentDetails.map(agent -> { dbFile.setAgent(agent);
                return dbFile;
            });
            }
            return agentFileRepository.save(dbFile);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }catch (NumberFormatException n){
            System.err.print("Id is expe");
            return null;
        }
    }

    @Override
    public AgentFile getFile(String fileId) {
        return  agentFileRepository.findById(fileId)
                .orElseThrow(()->new ResourceNotFoundException("File not found with id " + fileId));
        }
}
