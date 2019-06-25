package com.company.safekyc.service;

import com.company.safekyc.enumeration.ActionType;
import com.company.safekyc.model.*;
import com.company.safekyc.h2.repository.*;
import com.company.safekyc.type.*;
import com.company.safekyc.util.DateUtil;
import com.company.safekyc.util.ZXingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.util.*;


@Service("actionService")
public class ActionService {

    @Autowired
    AuthRepository authRepository;

    @Autowired
    ActionRepository actionRepository;

    @Autowired
    QRCodeRepository qrCodeRepository;

    private static final Logger log = LoggerFactory.getLogger(ActionService.class);

    @Transactional
    public ResponseEntity<?> retrieveQRCode(RetrieveQRCodeRequest request) {
        try {
            User user = checkTokenIsActive(request.getToken());

            QRCode qrCode = loadActiveQRCodeByUserId(user);
            if(qrCode == null){
                return new ResponseEntity<String>("QRCode not found !", HttpStatus.NOT_FOUND);
            }

            byte[] qrCodeImage = ZXingUtil.getQRCodeImage(qrCode.getUuid(), 256, 256);

            RetrieveQRCodeResponse response = new RetrieveQRCodeResponse();
            response.setData(qrCodeImage);
            response.setResult("success");
            return new ResponseEntity<RetrieveQRCodeResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("retrieveQRCode err => " + e.getMessage());

            if (e instanceof HttpClientErrorException) {
                final String responseStr = ((HttpClientErrorException) e).getResponseBodyAsString();
                return new ResponseEntity<String>(responseStr, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<String>("Unexpected error" + e.getMessage(), HttpStatus.BAD_REQUEST);
        } finally {
        }
    }

    @Transactional
    public ResponseEntity<?> shareQRCode(ShareQRCodeRequest request) {
        try {
            User user = checkTokenIsActive(request.getToken());

            Action action = new Action();
            action.setSourceUser(user);
            action.setActionType(ActionType.SHARE_QR_CODE.name());
            action.setCreationDate(new Date());
            QRCode sourceQRCode = loadActiveQRCodeByUserId(user);

            Random rand = new Random();
            String secret = String.format("%04d", rand.nextInt(10000));
            sourceQRCode.setSecret(secret);
            Date secretExpire = DateUtil.addMinuteToDate(new Date(), 5);
            sourceQRCode.setSecretExpire(secretExpire);

            qrCodeRepository.save(sourceQRCode);
            action.setSourceQRCode(sourceQRCode);
            Action savedAction = actionRepository.save(action);

            ShareQRCodeResponse response = new ShareQRCodeResponse();
            response.setSecret(secret);
            response.setExpire(secretExpire);
            response.setResult("success");
            return new ResponseEntity<ShareQRCodeResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("shareQRCode err => " + e.getMessage());

            if (e instanceof HttpClientErrorException) {
                final String responseStr = ((HttpClientErrorException) e).getResponseBodyAsString();
                return new ResponseEntity<String>(responseStr, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<String>("Unexpected error" + e.getMessage(), HttpStatus.BAD_REQUEST);
        } finally {
        }
    }

    private QRCode loadActiveQRCodeByUserId(User user) {
        List<QRCode> byUserUserId = qrCodeRepository.findByUserUserId(user.getUserId());
        for (QRCode qrCode : byUserUserId) {
            if("active".equals(qrCode.getStatus()))
                return qrCode;
        }
        return null;
    }

    private User checkTokenIsActive(String token) {
        User user = authRepository.findByToken(token);

        //TODO check expire date

        return user;
    }

    @Transactional
    public ResponseEntity<?> verifyQRCode(VerifyQRCodeRequest request) {
        try {
            User user = checkTokenIsActive(request.getToken());
            QRCode byUuid = qrCodeRepository.findByUuid(request.getUuid());
            if(byUuid == null)
                return new ResponseEntity<String>("Invalid qr code", HttpStatus.BAD_REQUEST);
            User targetUser = byUuid.getUser();

            Action action = new Action();
            action.setSourceUser(user);
            action.setTargetUser(targetUser);
            action.setActionType(ActionType.VERIFY_QR_CODE.name());
            action.setCreationDate(new Date());
            QRCode sourceQRCode = loadActiveQRCodeByUserId(user);
            action.setSourceQRCode(sourceQRCode);
            QRCode targetQRCode = loadActiveQRCodeByUserId(targetUser);
            action.setTargetQRCode(targetQRCode);

            if(!targetQRCode.getSecret().equals(request.getSecret()))
                return new ResponseEntity<String>("Invalid secret", HttpStatus.BAD_REQUEST);

            actionRepository.save(action);


            VerifyQRCodeResponse response = new VerifyQRCodeResponse();
            response.setName(targetUser.getName());
            response.setEmail(targetUser.getEmail());
            response.setPhoneNumber(targetUser.getPhoneNumber());
            response.setResult("success");
            return new ResponseEntity<VerifyQRCodeResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("verifyQRCode err => " + e.getMessage());

            if (e instanceof HttpClientErrorException) {
                final String responseStr = ((HttpClientErrorException) e).getResponseBodyAsString();
                return new ResponseEntity<String>(responseStr, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<String>("Unexpected error" + e.getMessage(), HttpStatus.BAD_REQUEST);
        } finally {
        }
    }

    public ResponseEntity<?> retrieveSourceActions(RetrieveSourceActionsRequest request) {
        try {
            User user = checkTokenIsActive(request.getToken());
            List<Action> bySourceUser = actionRepository.findBySourceUserUserId(user.getUserId());

            List<ActionItemResponse> actionItemResponses = convertActionToDto(bySourceUser);

            ActionResponse response = new ActionResponse();
            response.setActionItemResponses(actionItemResponses);
             response.setResult("success");
            return new ResponseEntity<ActionResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("retrieveSourceActions err => " + e.getMessage());

            if (e instanceof HttpClientErrorException) {
                final String responseStr = ((HttpClientErrorException) e).getResponseBodyAsString();
                return new ResponseEntity<String>(responseStr, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<String>("Unexpected error" + e.getMessage(), HttpStatus.BAD_REQUEST);
        } finally {
        }
    }

    public ResponseEntity<?> retrieveTargetActions(RetrieveTargetActionsRequest request) {
        try {
            User user = checkTokenIsActive(request.getToken());
            List<Action> byTargetUser = actionRepository.findByTargetUserUserId(user.getUserId());

            List<ActionItemResponse> actionItemResponses = convertActionToDto(byTargetUser);

            ActionResponse response = new ActionResponse();
            response.setActionItemResponses(actionItemResponses);
            response.setResult("success");
            return new ResponseEntity<ActionResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("retrieveTargetActions err => " + e.getMessage());

            if (e instanceof HttpClientErrorException) {
                final String responseStr = ((HttpClientErrorException) e).getResponseBodyAsString();
                return new ResponseEntity<String>(responseStr, HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<String>("Unexpected error" + e.getMessage(), HttpStatus.BAD_REQUEST);
        } finally {
        }
    }

    private List<ActionItemResponse> convertActionToDto(List<Action> byTargetUser) {
        List<ActionItemResponse> actionItemResponses = new ArrayList<>();
        for (Action action : byTargetUser) {
            ActionItemResponse actionItemResponse = new ActionItemResponse();
            actionItemResponse.setActionItemId(action.getActionId());
            actionItemResponse.setActionType(action.getActionType());
            actionItemResponse.setCreationDate(action.getCreationDate());
            actionItemResponse.setSourceUserName(action.getSourceUser() != null ? action.getSourceUser().getName(): "");
            actionItemResponse.setTargetUserName(action.getTargetUser() != null ? action.getTargetUser().getName(): "");
            actionItemResponses.add(actionItemResponse);
        }
        return actionItemResponses;
    }

}
