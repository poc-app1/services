package com.poc.boldconnect.controller;

import com.poc.boldconnect.dao.BoldConnectDao;
import com.poc.boldconnect.model.domain.Account;
import com.poc.boldconnect.model.request.AccountRequest;
import com.poc.boldconnect.model.response.AccountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.poc.boldconnect.model.response.ErrorResponse;
import com.poc.boldconnect.util.CommonUtils;


@RestController
public class AccountController {

    private static Logger LOG = LoggerFactory.getLogger(AccountController.class);
    public static final String RESOURCE_NAME = "/service/v1/boldConnect/accounts";
    public static final String UUID = "uuid";

    private final BoldConnectDao boldConnectDao;

    @Autowired
    public AccountController(BoldConnectDao boldConnectDao) {
        this.boldConnectDao = boldConnectDao;

    }

    @ApiOperation(value = "Lookup an account", nickname = "accountLookup", response = Account.class,
            notes = "mock account data response ")
    @ApiResponses({ @ApiResponse(code = 200, message = "OK", response = Account.class), @ApiResponse(code = 404, message = "NOT_FOUND", response = ErrorResponse.class), })
    @RequestMapping(value = RESOURCE_NAME , method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> lookupAccount(@RequestHeader(value = "uuid", required = true) String uuid, @RequestParam(required = false) String accountNumberValue) {
        LOG.debug("---  Beginning Action ---");

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(UUID, uuid);

        String json = CommonUtils.getInputFromFileUsingStream("AccountRequests.json");
        final AccountRequest accountRequest = CommonUtils.convertJSONToObject(json, AccountRequest.class);

        if (null != accountRequest) {
            AccountResponse accountResponse = new AccountResponse();
            accountResponse.setData(accountRequest.getAccounts());
            return new ResponseEntity<>(accountResponse, responseHeaders, HttpStatus.OK);
        } else {// TODO if some other error thrown handle it
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setMessage("Look on message properties file and global exception file for error binding");
            return new ResponseEntity<>(errorResponse, responseHeaders, HttpStatus.NOT_FOUND);
        }
    }


}