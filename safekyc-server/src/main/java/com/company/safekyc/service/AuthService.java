package com.company.safekyc.service;

import com.company.safekyc.model.QRCode;
import com.company.safekyc.model.User;
import com.company.safekyc.h2.repository.AuthRepository;
import com.company.safekyc.h2.repository.QRCodeRepository;
import com.company.safekyc.type.SignInRequest;
import com.company.safekyc.type.SignInResponse;
import com.company.safekyc.type.SignUpRequest;
import com.company.safekyc.type.SignUpResponse;
import com.company.safekyc.util.DateUtil;
import com.company.safekyc.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Date;
import java.util.UUID;


@Service("authService")
public class AuthService {

    @Autowired
    AuthRepository repository;

    @Autowired
    QRCodeRepository qrCodeRepository;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);


    public ResponseEntity<?> signIn(SignInRequest request) {
        try {
            SignInResponse signInResponse = new SignInResponse();

            User byEmail = repository.findByEmail(request.getEmail());
            if (byEmail == null) {
                signInResponse.setResult("user not found");
            } else if (PasswordUtil.decodeString(byEmail.getPassword()).equals(request.getPassword())) {
                signInResponse.setResult("success");

                String token = UUID.randomUUID().toString();
                byEmail.setToken(token);
                byEmail.setTokenExpire(DateUtil.addHourToDate(new Date(), 1));
                repository.save(byEmail);
                signInResponse.setToken(token);
            } else{
                signInResponse.setResult("incorrect password");
            }
            return new ResponseEntity<SignInResponse>(signInResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("signIn err => " + e.getMessage());

            if (e instanceof HttpClientErrorException) {
                final String responseStr = ((HttpClientErrorException) e).getResponseBodyAsString();
                return new ResponseEntity<String>(responseStr, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<String>("Unexpected error" + e.getMessage(), HttpStatus.BAD_REQUEST);
        } finally {
        }
    }

    public ResponseEntity<?> signUp(SignUpRequest request) {
        try {
            SignUpResponse signUpResponse = new SignUpResponse();

            User byEmail = repository.findByEmail(request.getEmail());
            if (byEmail != null) {
                signUpResponse.setResult("user already exist");

            } else {
                final User user = new User();
                user.setName(request.getName());
                user.setBirthDate(request.getBirthDate());
                user.setEmail(request.getEmail());
                user.setNationality(request.getNationality());
                user.setPhoneNumber(request.getPhoneNumber());
                user.setPassword(PasswordUtil.encodeString(request.getPassword()));
                user.setCreationDate(new Date());
                user.setClientIp(request.getClientIp());
                User savedUser = repository.save(user);

                QRCode qrCode = new QRCode();
                qrCode.setUser(savedUser);
                qrCode.setUuid(UUID.randomUUID().toString());
                qrCode.setCreationDate(new Date());
                qrCode.setStatus("active");
                qrCodeRepository.save(qrCode);

                signUpResponse.setResult("success");
            }

            return new ResponseEntity<SignUpResponse>(signUpResponse, HttpStatus.OK);
        } catch (Exception e) {
            log.error("signUp err => " + e.getMessage());

            if (e instanceof HttpClientErrorException) {
                final String responseStr = ((HttpClientErrorException) e).getResponseBodyAsString();
                return new ResponseEntity<String>(responseStr, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<String>("Unexpected error" + e.getMessage(), HttpStatus.BAD_REQUEST);
        } finally {
        }
    }


}
