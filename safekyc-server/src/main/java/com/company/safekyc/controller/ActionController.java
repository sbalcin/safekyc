package com.company.safekyc.controller;

import com.company.safekyc.service.ActionService;
import com.company.safekyc.type.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/action")
@AllArgsConstructor
public class ActionController {


    private ActionService service;

    @PostMapping("/retrieveQRCode/")
    public ResponseEntity<?> retrieveQRCode(@RequestBody RetrieveQRCodeRequest request) {

        return service.retrieveQRCode(request);
    }

    @PostMapping("/shareQRCode/")
    public ResponseEntity<?> shareQRCode(@RequestBody ShareQRCodeRequest request) {

        return service.shareQRCode(request);
    }

    @PostMapping("/verifyQRCode/")
    public ResponseEntity<?> verifyQRCode(@RequestBody VerifyQRCodeRequest request) {

        return service.verifyQRCode(request);
    }

    @PostMapping("/retrieveSourceActions/")
    public ResponseEntity<?> retrieveSourceActions(@RequestBody RetrieveSourceActionsRequest request) {

        return service.retrieveSourceActions(request);
    }

    @PostMapping("/retrieveTargetActions/")
    public ResponseEntity<?> retrieveTargetActions(@RequestBody RetrieveTargetActionsRequest request) {

        return service.retrieveTargetActions(request);
    }


}