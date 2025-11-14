package com.gdu.wacdo.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class AssignementId  {

    private Long employeeId;
    private Long restaurantId;

    public AssignementId() {}
    public AssignementId(Long employeeId, Long restaurantId) {
        this.employeeId = employeeId;
        this.restaurantId = restaurantId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
    public Long getRestaurantId() {
        return restaurantId;
    }
    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
}
