package com.mahesh.HMS.controller;

import com.mahesh.HMS.dto.LoginResponse;
import com.mahesh.HMS.dto.NotificationDTO;
import com.mahesh.HMS.dto.RegisterRequest;
import com.mahesh.HMS.model.*;
import com.mahesh.HMS.notificationConfig.NotificationService;
import com.mahesh.HMS.repository.*;
import com.mahesh.HMS.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired private UserRepo userRepo;
    @Autowired private PatientRepository patientRepo;
    @Autowired private DoctorRepository doctorRepo;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtService jwtService;
    @Autowired private NotificationService notificationService;


    // ---------------- REGISTER ----------------
// ---------------- REGISTER ----------------
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("status", "error", "message", "Invalid role"));
        }

        // Create User
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        userRepo.save(user); // save to get generated id

        // Generate roleId as <ROLE>-<userId>
        user.setRoleId(role.name() + "-" + user.getId());
        userRepo.save(user);

        // Handle role-based logic
        if (role == Role.PATIENT) {
            Patient patient = new Patient();
            patient.setName(request.getName());
            patient.setGender(request.getGender());
            patient.setAge(request.getAge());
            patient.setUser(user);
            patientRepo.save(patient);

            // Send notification to PATIENTSUPERVISOR topic
            NotificationDTO notification = new NotificationDTO(
                    Role.PATIENTSUPERVISOR.name(),
                    "New Patient Registered",
                    "Patient " + patient.getName() + " has been registered.",
                    "PATIENT",
                    patient.getId()
            );
            notificationService.sendNotification(notification);

        } else if (role == Role.DOCTOR) {
            Doctor doctor = new Doctor();
            doctor.setName(request.getName());
            doctor.setSpeciality(request.getSpeciality());
            doctor.setUser(user);
            doctorRepo.save(doctor);

            // Send notification to DOCTORSUPERVISOR topic
            NotificationDTO notification = new NotificationDTO(
                    Role.DOCTORSUPERVISOR.name(),
                    "New Doctor Registered",
                    "Doctor " + doctor.getName() + " has been registered.",
                    "DOCTOR",
                    doctor.getId()
            );
            notificationService.sendNotification(notification);
        }

        return ResponseEntity.ok(Map.of("status", "success", "role", role, "roleId", user.getRoleId()));
    }



    // ---------------- LOGIN ----------------
    // ---------------- LOGIN ----------------
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User userRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userRequest.getUsername(),
                            userRequest.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                User dbUser = userRepo.findByUsername(userRequest.getUsername())
                        .orElseThrow(() -> new RuntimeException("User not found"));

                String token = jwtService.generateToken(dbUser.getUsername());

                // Include id in the login response
                return ResponseEntity.ok(
                        new LoginResponse(
                                token,
                                "Login successful",
                                dbUser.getRole(),
                                dbUser.getRoleId(),
                                dbUser.getId()   // <-- Added id here
                        )
                );
            } else {
                return ResponseEntity.badRequest()
                        .body(new LoginResponse(null, "Login failed", null, null, null));
            }
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                    .body(new LoginResponse(null, "Login failed: " + ex.getMessage(), null, null, null));
        }
    }



    // ---------------- UPDATE PROFILE ----------------
    @PutMapping("/update-profile/{id}")
    public ResponseEntity<?> updateProfile(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        return userRepo.findById(id).map(user -> {
            if (updates.containsKey("username"))
                user.setUsername((String) updates.get("username"));

            if (updates.containsKey("password"))
                user.setPassword(passwordEncoder.encode((String) updates.get("password")));

            userRepo.save(user);
            return ResponseEntity.ok(Map.of("status", "success", "message", "Profile updated successfully"));
        }).orElse(ResponseEntity.badRequest().body(Map.of("status", "error", "message", "User not found")));
    }


    // ---------------- UPLOAD PHOTO ----------------
    @PostMapping("/upload-photo/{id}")
    public ResponseEntity<?> uploadPhoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        try {
            String uploadDir = "uploads/";
            Files.createDirectories(Paths.get(uploadDir));
            String filePath = uploadDir + id + "_" + file.getOriginalFilename();
            file.transferTo(new File(filePath));

            return ResponseEntity.ok(Map.of("status", "success", "message", "Photo uploaded successfully", "path", filePath));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", e.getMessage()));
        }
    }
}
