package com.phenix.jirama.controllers;

import com.phenix.jirama.models.Payement;
import com.phenix.jirama.service.MvolaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("init")
    private Map<String, String> IniateTransaction(@RequestBody Payement body, @RequestHeader Map<String, String> head){
        return  this.mvolaService.InitiateTransaction(body, head);
    }
    }

