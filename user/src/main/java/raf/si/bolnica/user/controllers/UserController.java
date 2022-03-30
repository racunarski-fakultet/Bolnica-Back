package raf.si.bolnica.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import raf.si.bolnica.user.constants.Constants;
import raf.si.bolnica.user.exceptionHandler.user.UserExceptionHandler;
import raf.si.bolnica.user.interceptors.LoggedInUser;
import raf.si.bolnica.user.models.Odeljenje;
import raf.si.bolnica.user.models.User;
import raf.si.bolnica.user.requests.CreateEmployeeRequestDTO;
import raf.si.bolnica.user.responses.UserResponseDTO;
import raf.si.bolnica.user.service.OdeljenjeService;
import raf.si.bolnica.user.service.UserService;
import raf.si.bolnica.user.service.EmailService;


import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = Constants.BASE_API)
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserExceptionHandler userExceptionHandler;

    @Autowired
    private OdeljenjeService odeljenjeService;

    @Autowired
    private LoggedInUser loggedInUser;

    @Autowired
    private EmailService emailService;

    @GetMapping(value = "/fetch-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDTO> fetchAdminByUsername(@RequestParam String username) {
        User user = userService.fetchUserByEmail(username);
        if (user != null) {
            UserResponseDTO userResponseDTO = new UserResponseDTO(user.getUserId(), user.getName(), user.getSurname(), user.getPassword(), user.getEmail(), user.getRoles());
            return ok(userResponseDTO);
        }
        return ResponseEntity.status(403).build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword() {

        User user = userService.fetchUserByEmail(loggedInUser.getUsername());

        if (user == null) {
            // Username doesn't exist, return 403
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            // Username exists, proceeds to generate and save new password, also send email
            String generatedPassword = userService.generateNewPassword(user);

            userService.savePassword(user, generatedPassword);
            emailService.sendEmail(loggedInUser.getUsername(), generatedPassword);

            return ok().build();
        }

    }

    @PostMapping(value = Constants.CREATE_EMPLOYEE)
    public ResponseEntity<?> createEmployee(@RequestBody CreateEmployeeRequestDTO requestDTO) {
        if (loggedInUser.getRoles().contains("ROLE_ADMIN")) {
            Odeljenje odeljenje = odeljenjeService.fetchOdeljenjeById(requestDTO.getDepartment());
            String username = requestDTO.getEmail().substring(0, requestDTO.getEmail().indexOf("@"));
            String password = requestDTO.getEmail().substring(0, requestDTO.getEmail().indexOf("@"));

            userExceptionHandler.validateUsername.accept(username);
            userExceptionHandler.validateUserTitle.accept(requestDTO.getTitle());
            userExceptionHandler.validateUserProfession.accept(requestDTO.getProfession());
            userExceptionHandler.validateUserGender.accept(requestDTO.getGender());

            User user = new User();

            user.setOdeljenje(odeljenje);
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            user.setKorisnickoIme(username);
            user.setEmail(requestDTO.getEmail());
            user.setName(requestDTO.getName());
            user.setSurname(requestDTO.getSurname());
            user.setAdresaStanovanja(requestDTO.getAddress());
            user.setDatumRodjenja(requestDTO.getDob());
            user.setMestoStanovanja(requestDTO.getCity());
            user.setJmbg(requestDTO.getJmbg());
            user.setKontaktTelefon(requestDTO.getContact());
            user.setPol(requestDTO.getGender());
            user.setTitula(requestDTO.getTitle());
            user.setZanimanje(requestDTO.getProfession());

            User userToReturn = userService.createEmployee(user);
            return ok(userToReturn);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}
