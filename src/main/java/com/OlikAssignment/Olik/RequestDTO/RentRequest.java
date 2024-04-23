package com.OlikAssignment.Olik.RequestDTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RentRequest {
    private Long bookID;
    private String renterName;
    private Date rentalDate;
}
