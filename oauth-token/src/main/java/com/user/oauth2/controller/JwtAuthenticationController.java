package com.user.oauth2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.user.oauth2.config.JwtTokenUtil;
import com.user.oauth2.config.JwtUserDetailsService;
import com.user.oauth2.global.JwtRequest;
import com.user.oauth2.global.JwtResponse;
import com.user.oauth2.model.UserDTO;


@RestController
@CrossOrigin
@RequestMapping("/api")
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;
	@GetMapping("/")
	public  String msg(){
		return  "yes running";
	}
	
	@PostMapping("/oauth/token")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        // verified user is active/not
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
         System.err.println("userName: "+ authenticationRequest.getUsername()+"pwd: "+authenticationRequest.getPassword());
		final UserDetails userDetails = jwtUserDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}
	@PostMapping("/user/register")
	public ResponseEntity<?> saveUser(@RequestBody UserDTO user) {
		return ResponseEntity.ok(jwtUserDetailsService.save(user));
	}
	
	private void authenticate(String username, 
			String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
