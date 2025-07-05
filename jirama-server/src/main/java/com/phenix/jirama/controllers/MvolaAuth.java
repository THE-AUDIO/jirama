package com.phenix.jirama.controllers;

import com.phenix.jirama.service.MvolaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("mvola")
public class MvolaAuth {
    @Autowired
    private  MvolaService mvolaService;
    @PostMapping("/auth")
    public Map<String,String> AuthMvola(){
       return this.mvolaService.authMvola();
    }
    }

