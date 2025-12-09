package com.gdu.wacdo.model.DTO;

import java.time.LocalDate;
import java.util.List;

public class WacdoDTO {

    public record ResponsabilityDto(Long id, String role) {
    }

    public record EmployeeDto(Long id, String name, String surName, String email, List<AssignementDto> assignements) {
    }

    public record AssignementDto(Long id, LocalDate startDate, LocalDate endDate, EmployeeDto employee,
                                 RestaurantDto restaurant,
                                 ResponsabilityDto responsability) {
    }

    public record RestaurantDto(Long id, String name, String image, RestaurantAddressDto restaurantAddress,
                                List<AssignementDto> assignements) {
    }

    public record RestaurantAddressDto(Long id, String city, String postalCode, String address, float cordX,
                                       float cordY) {
    }

}
