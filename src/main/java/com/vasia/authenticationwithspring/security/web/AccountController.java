package com.vasia.authenticationwithspring.security.web;


import com.vasia.authenticationwithspring.security.entities.AppUser;
import com.vasia.authenticationwithspring.security.service.AccountService;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping("/users")
    public List<AppUser> appUsers(){
        return accountService.findAllUser();
    }
}
