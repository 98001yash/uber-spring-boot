package com.company.project.uber.uperApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiderDto {

    private Long id;
    private UserDto user;
    private Double rating;
}
