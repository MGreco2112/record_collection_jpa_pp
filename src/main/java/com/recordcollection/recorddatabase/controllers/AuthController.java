package com.recordcollection.recorddatabase.controllers;

import com.recordcollection.recorddatabase.models.auth.ERole;
import com.recordcollection.recorddatabase.models.auth.Role;
import com.recordcollection.recorddatabase.models.auth.User;
import com.recordcollection.recorddatabase.payloads.api.request.DiscogsTokenRequest;
import com.recordcollection.recorddatabase.payloads.api.response.OauthRequestResponse;
import com.recordcollection.recorddatabase.payloads.request.LoginRequest;
import com.recordcollection.recorddatabase.payloads.request.SignupRequest;
import com.recordcollection.recorddatabase.payloads.response.JwtResponse;
import com.recordcollection.recorddatabase.payloads.response.MessageResponse;
import com.recordcollection.recorddatabase.repositories.RoleRepository;
import com.recordcollection.recorddatabase.repositories.UserRepository;
import com.recordcollection.recorddatabase.security.JwtUtils;
import com.recordcollection.recorddatabase.security.services.UserDetailsImpl;

import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserRepository repository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${Spring.datasource.driver-class-name}")
    private String myDriver;

    @Value("${Spring.datasource.url}")
    private String myUrl;

    @Value("${Spring.datasource.username}")
    private String username;

    @Value("${Spring.datasource.password}")
    private String password;

    @Value("${recorddatabase.app.consumerKey}")
    private String consumerKey;

    @Value("${recorddatabase.app.consumerSecret}")
    private String consumerSecret;

    @Value("${recorddatabase.app.requestTokenURL}")
    private String requestTokenURL;

    @Value("${recorddatabase.app.callbackURL}")
    private String callbackURL;

    @Value("${recorddatabase.app.accessTokenURL}")
    private String accessTokenURL;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        if (repository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error, Email already in use. Please login or reset password"));
        }

        //create new account

        User user = new User(signupRequest.getUsername(), encoder.encode(signupRequest.getPassword()));

        Set<String> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        //check role table for enums
        int roleCheck = roleRepository.isRoleEmpty();

        if (roleCheck < ERole.values().length) {
            int id = 1;
            for (ERole role : ERole.values()) {
                if (roleRepository.findByName(role).isEmpty()) {
                    try {
                        Connection conn = DriverManager.getConnection(myUrl, username, password);
                        Class.forName(myDriver);
                        String query = "Insert into role (id, name) values (?,?)";
                        PreparedStatement statement = conn.prepareStatement(query);

                        statement.setString(1, Integer.toString(id));
                        statement.setString(2, role.toString());

                        statement.executeUpdate();

                    } catch (Exception e) {
                        Logger logger = LoggerFactory.getLogger(AuthController.class);
                        System.out.println(e.getMessage());

                    }
                }
                id++;
            }
        }

        if (strRoles == null) {
            System.out.println(true);
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException("Error: Role is not found"));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch(role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(adminRole);

                        break;
                    case "artist":
                        Role artistRole = roleRepository.findByName(ERole.ROLE_ARTIST)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(artistRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(userRole);
                }
            });

        }
        user.setRoles(roles);
        for (Role role : user.getRoles()) {
            System.out.println(role);
        }
        repository.save(user);

        return new ResponseEntity(new MessageResponse("User Registered Successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/discogsTokenRequest")
    public ResponseEntity<OauthRequestResponse> getDiscogsOauthToken() {
        Date date = new Date();

        long timestamp = date.getTime();

        String response = Unirest.get(requestTokenURL)
                .header("Authorization", "OAuth oauth_consumer_key=" + consumerKey + ", oauth_nonce=" + timestamp +
                        ", oauth_signature=" + consumerSecret + "&, oauth_signature_method=\"PLAINTEXT\", oauth_timestamp=" + timestamp +
                        ", oauth_callback=" + callbackURL)
                .asString()
                .getBody();

        if (response.length() == 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(new OauthRequestResponse(response));
    }

    //TODO get functioning to authorize users
    @PostMapping("/discogsAccessToken")
    public ResponseEntity<OauthRequestResponse> giveAccess(@RequestBody DiscogsTokenRequest request) {
        Date date = new Date();

        long timestamp = date.getTime();

        String response = Unirest.post(accessTokenURL)
                .header("Authorization", "OAuth oauth_consumer_key=" + consumerKey + ", oauth_nonce=" + timestamp +
                        ", oauth_token=" + request.getToken() + ", oauth_signature=" + consumerSecret + "%26" /* another string of unknown origin */ +
                        ", oauth_signature_method=\"PLAINTEXT\", oauth_timestamp=" + timestamp +
                        "oauth_verifier=" + request.getSecret())
                /*
                    Attempted to follow the Discogs forum post about appending a %26 to the consumer secret without success
                    Will attempt to follow up
                */

                .asString()
                .getBody();

        if (response.length() == 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(new OauthRequestResponse(response));
    }

}
