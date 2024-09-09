package com.company.project.uber.uperApp.services;

import com.company.project.uber.uperApp.dto.DriverDto;
import com.company.project.uber.uperApp.dto.SignUpDto;
import com.company.project.uber.uperApp.dto.UserDto;

public interface AuthService {

    String[] login(String email, String password);

    UserDto signup(SignUpDto signupDto);

    DriverDto onboardNewDriver(Long userId, String vehicleId);

    String refreshToken(String refreshToken);
}
