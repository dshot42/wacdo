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
    
}
