package com.company.safekyc.service;

import com.company.safekyc.enumeration.ActionType;
import com.company.safekyc.exception.ResourceNotFoundException;
import com.company.safekyc.model.Action;
import com.company.safekyc.model.QRCode;
import com.company.safekyc.model.User;
import com.company.safekyc.payload.*;
import com.company.safekyc.repository.ActionRepository;
import com.company.safekyc.repository.QRCodeRepository;
import com.company.safekyc.repository.UserRepository;
import com.company.safekyc.security.UserPrincipal;
import com.company.safekyc.util.DateUtil;
import com.company.safekyc.util.ZXingUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.*;


@Service("actionService")
@AllArgsConstructor
public class ActionService extends BaseService {

    private final ActionRepository actionRepository;

    private final QRCodeRepository qrCodeRepository;

    private final UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(ActionService.class);

    @Transactional
    public ResponseEntity<?> retrieveQRCode(UserPrincipal currentUser) {
        User user = userRepository.findByUsername(currentUser.getUsername()).orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUser.getUsername()));

        QRCode qrCode = loadActiveQRCodeByUserId(user);
        if (qrCode == null) {
            return new ResponseEntity<>(new ApiResponse(false, "QRCode not found !"), HttpStatus.BAD_REQUEST);
        }

        byte[] qrCodeImage;
        try {
            qrCodeImage = ZXingUtil.getQRCodeImage(qrCode.getUuid(), 256, 256);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }

        RetrieveQRCodeResponse response = new RetrieveQRCodeResponse();
        response.setData(qrCodeImage);
        response.setResult("success");
        return new ResponseEntity<RetrieveQRCodeResponse>(response, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> shareQRCode(UserPrincipal currentUser) {
        try {
            User user = userRepository.findByEmail(currentUser.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "email", currentUser.getEmail()));

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
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);

        } finally {
        }
    }

    private QRCode loadActiveQRCodeByUserId(User user) {
        List<QRCode> byUserUserId = qrCodeRepository.findByUserId(user.getId());
        for (QRCode qrCode : byUserUserId) {
            if ("active".equals(qrCode.getStatus()))
                return qrCode;
        }
        return null;
    }


    @Transactional
    public ResponseEntity<?> verifyQRCode(VerifyQRCodeRequest request, UserPrincipal currentUser) {
        try {
            User user = userRepository.findByEmail(currentUser.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User", "email", currentUser.getEmail()));
            QRCode byUuid = qrCodeRepository.findByUuid(request.getUuid());
            if (byUuid == null)
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

            if (!targetQRCode.getSecret().equals(request.getSecret()))
                return new ResponseEntity<String>("Invalid secret", HttpStatus.BAD_REQUEST);

            actionRepository.save(action);


            VerifyQRCodeResponse response = new VerifyQRCodeResponse();
            response.setName(targetUser.getUsername());
            response.setEmail(targetUser.getEmail());
            response.setPhoneNumber(targetUser.getPhone());
            response.setResult("success");
            return new ResponseEntity<VerifyQRCodeResponse>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);

        } finally {
        }
    }

    public PagedResponse<Action> retrieveSourceActions(int page, int size, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new ResourceNotFoundException("User", "username", principal.getName()));
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Action> items = actionRepository.findBySourceUserId(user.getId(), pageable);
        List<ActionItemResponse> actionItemResponses = convertActionToDto(items);

        if (items.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), items.getNumber(), items.getSize(), items.getTotalElements(), items.getTotalPages(), items.isLast());
        }
        return new PagedResponse<>(items.getContent(), items.getNumber(), items.getSize(), items.getTotalElements(), items.getTotalPages(), items.isLast());
    }

    public PagedResponse<Action> retrieveTargetActions(int page, int size, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow(() -> new ResourceNotFoundException("User", "username", principal.getName()));
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Action> items = actionRepository.findByTargetUserId(user.getId(), pageable);
        List<ActionItemResponse> actionItemResponses = convertActionToDto(items);

        if (items.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), items.getNumber(), items.getSize(), items.getTotalElements(), items.getTotalPages(), items.isLast());
        }
        return new PagedResponse<>(items.getContent(), items.getNumber(), items.getSize(), items.getTotalElements(), items.getTotalPages(), items.isLast());
    }

    private List<ActionItemResponse> convertActionToDto(Page<Action> byTargetUser) {
        List<ActionItemResponse> actionItemResponses = new ArrayList<>();
        for (Action action : byTargetUser) {
            ActionItemResponse actionItemResponse = new ActionItemResponse();
            actionItemResponse.setActionItemId(action.getActionId());
            actionItemResponse.setActionType(action.getActionType());
            actionItemResponse.setCreationDate(action.getCreationDate());
            actionItemResponse.setSourceUserName(action.getSourceUser() != null ? action.getSourceUser().getUsername() : "");
            actionItemResponse.setTargetUserName(action.getTargetUser() != null ? action.getTargetUser().getUsername() : "");
            actionItemResponses.add(actionItemResponse);
        }
        return actionItemResponses;
    }

}
