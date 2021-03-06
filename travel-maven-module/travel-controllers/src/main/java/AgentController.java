import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*",allowedHeaders="*")
@RequestMapping("/api/user/{userId}/agent")
@PreAuthorize("hasRole('ADMIN')")
public class AgentController {

    @Autowired
    AgentService agentService;

    @Autowired
    AgentRepository agentRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/hello")
    ResponseEntity<String> hello() {
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }

    @GetMapping(value = "/list", produces = "application/json")
    public List<Agents> getAllPosts(@PathVariable Long userId) throws JsonProcessingException {

        List<Agents> result = agentService.allAgentDetails(userId);
                return  result;
    }

    @PostMapping("/add")
    public Agents createUserAgent(@PathVariable (value = "userId") Long userId,
                                 @Valid @RequestBody Agents agent) {
        return  userRepository.findById(userId).map(a -> {
            agent.setUser(a);
            return agentRepository.save(agent);
        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
    }

    @PutMapping(value = ("/{agentId}"), produces = "application/json")
    public Optional<ResponseEntity<Agents>> updateAgentDetails(@PathVariable(value = "userId") Long userId,
                                                              @PathVariable(value = "agentId") Long agentId,
                                                              @Valid @RequestBody Agents agentRequest) {
        Optional<Long> validateId = userRepository.findById(userId).map(user -> user.getId());
        if (validateId.isPresent()) {
            return agentRepository.findById(agentId).map(agent -> {
                agent.setAgentName(agentRequest.getAgentName());
                agent.setEmail(agentRequest.getEmail());
                agent.setAgentMobileNumber(agentRequest.getAgentMobileNumber());
                return ResponseEntity.ok(agentRepository.save(agent));
            });
        }
        return null;
    }

    @DeleteMapping("/{agentId}")
    public ResponseEntity<?> deleteUserAgent(@PathVariable (value = "userId") Long userId,
                                           @PathVariable (value = "agentId") Long agentId) {
        return agentRepository.findByIdAndUserId(agentId, userId).map(agent -> {
            agentRepository.delete((Agents) agent);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Comment not found with id " + agentId + " and userId " + userId));
    }
}
