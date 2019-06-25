package com.company.safekyc.controller;

import com.company.safekyc.model.Action;
import com.company.safekyc.payload.ActionItemResponse;
import com.company.safekyc.payload.PagedResponse;
import com.company.safekyc.payload.VerifyQRCodeRequest;
import com.company.safekyc.security.CurrentUser;
import com.company.safekyc.security.UserPrincipal;
import com.company.safekyc.service.ActionService;
import com.company.safekyc.util.AppConstants;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/action")
@AllArgsConstructor
public class ActionController {


    private final ActionService service;

    @PostMapping("/retrieveQRCode/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> retrieveQRCode(@CurrentUser UserPrincipal principal){

        return service.retrieveQRCode(principal);
    }

    @PostMapping("/shareQRCode/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> shareQRCode(@CurrentUser UserPrincipal principal){

        return service.shareQRCode(principal);
    }

    @PostMapping("/verifyQRCode/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> verifyQRCode(VerifyQRCodeRequest request, @CurrentUser UserPrincipal principal){

        return service.verifyQRCode(request, principal);
    }

    @GetMapping("/retrieveSourceActions/")
    @PreAuthorize("hasRole('USER')")
    public PagedResponse<Action> retrieveSourceActions(@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                                       @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size, Principal principal){

        return service.retrieveSourceActions(page, size, principal);
    }

    @GetMapping("/retrieveTargetActions/")
    @PreAuthorize("hasRole('USER')")
    public PagedResponse<Action> retrieveTargetActions(@RequestParam(name = "page", required = false, defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) Integer page,
                                                                   @RequestParam(name = "size", required = false, defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer size, Principal principal){

        return service.retrieveTargetActions(page, size, principal);
    }


}