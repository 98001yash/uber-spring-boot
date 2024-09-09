package com.company.project.uber.uperApp.services.Impl;

import com.company.project.uber.uperApp.Security.JWTService;
import com.company.project.uber.uperApp.dto.SignUpDto;
import com.company.project.uber.uperApp.dto.UserDto;
import com.company.project.uber.uperApp.entities.User;
import com.company.project.uber.uperApp.entities.enums.Role;
import com.company.project.uber.uperApp.repositories.UserRepositories;
import com.company.project.uber.uperApp.services.impl.AuthServiceImpl;
import com.company.project.uber.uperApp.services.impl.DriverServiceImpl;
import com.company.project.uber.uperApp.services.impl.RiderServiceImpl;
import com.company.project.uber.uperApp.services.impl.WalletServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {


    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    @Mock
    private UserRepositories userRepositories;

    @Mock
    private RiderServiceImpl riderService;

    @Mock
    private WalletServiceImpl walletService;

    @Mock
    private DriverServiceImpl driverService;

    @Spy
    private ModelMapper modelMapper;

    @Spy
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRoles(Set.of(Role.RIDER));

       SignUpDto  signUpDto = new SignUpDto();
        signUpDto.setEmail("test@example.com");
        signUpDto.setPassword("password");
    }

    @Test
    void testLogin_whenSuccess() {
        // arrange
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwtService.generateAccessToken(any(User.class))).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

        // act
        String[] tokens = authService.login(user.getEmail(),user.getPassword());


        // assert
        assertThat(tokens).hasSize(2);
        assertThat(tokens[0]).isEqualTo("accessToken");
        assertThat(tokens[1]).isEqualTo("refreshToken");
    }

    @Test
    void testSignup_whenSuccess(){
        // arrange
        when(userRepositories.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepositories.save(any(User.class))).thenReturn(user);

        // Act
        SignUpDto  signUpDto = new SignUpDto();
        signUpDto.setEmail("test@example.com");
        signUpDto.setPassword("password");
        UserDto userDto = authService.signup(signUpDto);

        // Assert
        assertThat(userDto).isNotNull();
        assertThat(userDto.getEmail()).isEqualTo(signUpDto.getEmail());

        verify(riderService).createNewRider(any(User.class));
        verify(walletService).createNewWallet(any(User.class));
    }









}