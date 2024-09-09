package com.company.project.uber.uperApp.controllers;


import com.company.project.uber.uperApp.TestContainerConfiguration;
import com.company.project.uber.uperApp.dto.OnboardDriverDto;
import com.company.project.uber.uperApp.dto.SignUpDto;
import com.company.project.uber.uperApp.entities.User;
import com.company.project.uber.uperApp.entities.enums.Role;
import com.company.project.uber.uperApp.repositories.RiderRepositories;
import com.company.project.uber.uperApp.repositories.UserRepositories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;


import java.util.Set;


@AutoConfigureWebTestClient(timeout = "100000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainerConfiguration.class)
 class AuthControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserRepositories userRepositories;

    @Autowired
    private RiderRepositories riderRepositories;

    private User user;

    @BeforeEach
    void setUpEach(){
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRoles(Set.of(Role.RIDER));
    }

    @Test
    void testSignUp_success(){
        SignUpDto signupDto = new SignUpDto();
        signupDto.setEmail("test@example.com");
        signupDto.setName("Test name");
        signupDto.setPassword("password");


        webTestClient.post()
                .uri("/auth/signup")
                .bodyValue(signupDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.data.email").isEqualTo(signupDto.getEmail())
                .jsonPath("$.data.name").isEqualTo(signupDto.getName());
    }

//    @Test
//    @WithUserDetails("admin@gmail.com")
    void testOnBoardDriver_success(){
        if(!userRepositories.existsById(1L)){
            userRepositories.save(user);
        }

        OnboardDriverDto onboardDriverDto = new OnboardDriverDto();
        onboardDriverDto.setVehicleId("ABC123");

        webTestClient
                .post()
                .uri("/auth/onBoardDriver/1")
                .bodyValue(onboardDriverDto)
                .exchange()
                .expectStatus().isCreated();
    }


}
