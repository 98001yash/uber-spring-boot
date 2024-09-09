package com.company.project.uber.uperApp.services.impl;

import com.company.project.uber.uperApp.Security.JWTService;
import com.company.project.uber.uperApp.dto.DriverDto;
import com.company.project.uber.uperApp.dto.SignUpDto;
import com.company.project.uber.uperApp.dto.UserDto;
import com.company.project.uber.uperApp.entities.Driver;
import com.company.project.uber.uperApp.entities.User;
import com.company.project.uber.uperApp.entities.enums.Role;
import com.company.project.uber.uperApp.exceptions.ResourceNotFoundException;
import com.company.project.uber.uperApp.exceptions.RuntimeConflictException;
import com.company.project.uber.uperApp.repositories.UserRepositories;
import com.company.project.uber.uperApp.services.AuthService;
import com.company.project.uber.uperApp.services.DriverService;
import com.company.project.uber.uperApp.services.RiderService;
import com.company.project.uber.uperApp.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.company.project.uber.uperApp.entities.enums.Role.DRIVER;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepositories userRepositories;
    private final ModelMapper modelMapper;
    private final RiderService riderService;
    private final WalletService walletService;
    private final DriverService driverService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    @Override
    public String[] login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new String[]{accessToken, refreshToken};
    }

    @Override
    @Transactional
    public UserDto signup(SignUpDto signUpDto) {
        User user = userRepositories.findByEmail(signUpDto.getEmail()).orElse(null);
        if(user != null)
            throw new RuntimeConflictException("Cannot signup, User already exists with email "+signUpDto.getEmail());

        User mappedUser = modelMapper.map(signUpDto, User.class);
        mappedUser.setRoles(Set.of(Role.RIDER));
        mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));
        User savedUser = userRepositories.save(mappedUser);

//        create user related entities
        riderService.createNewRider(savedUser);
        walletService.createNewWallet(savedUser);

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public DriverDto onboardNewDriver(Long userId, String vehicleId) {
        User user = userRepositories.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id "+userId));

        if(user.getRoles().contains(DRIVER))
            throw new RuntimeConflictException("User with id "+userId+" is already a Driver");

        Driver createDriver = Driver.builder()
                .user(user)
                .rating(0.0)
                .vehicleId(vehicleId)
                .available(true)
                .build();
        user.getRoles().add(DRIVER);
        userRepositories.save(user);
        Driver savedDriver = driverService.createNewDriver(createDriver);
        return modelMapper.map(savedDriver, DriverDto.class);
    }

    @Override
    public String refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        User user = userRepositories.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found " +
                "with id: "+userId));

        return jwtService.generateAccessToken(user);
    }
}
