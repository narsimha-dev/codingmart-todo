import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user/")
public class UserRolesController {

	@GetMapping("/roles/user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public String agentUserAccess() {
		return "User role Content.";
	}

	@GetMapping("/roles/mode")
	@PreAuthorize("hasRole('MODERATOR')")
	public String moderatorAccess() {
		return " User role Moderator Board.";
	}

	@GetMapping("/roles/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "User role Admin Board.";
	}
}
