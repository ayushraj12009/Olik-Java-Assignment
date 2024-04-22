package com.OlikAssignment.Olik;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class spring {


    @GetMapping("/hi")
    public String hello(){
        return "api is working";
    }


}
