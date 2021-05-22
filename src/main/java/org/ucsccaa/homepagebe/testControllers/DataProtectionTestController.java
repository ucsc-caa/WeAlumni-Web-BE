package org.ucsccaa.homepagebe.testControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ucsccaa.homepagebe.exceptions.GenericServiceException;
import org.ucsccaa.homepagebe.services.DataProtection;

@RestController
@RequestMapping("/test/data_protection")
public class DataProtectionTestController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DataProtection dataProtection;

    @GetMapping("/encrypt_string")
    public String encryptString(@RequestParam String plain) {
        try {
            logger.info("Try to encrypt string: plain - {}", plain);
            return dataProtection.encrypt(plain);
        } catch (GenericServiceException e) {
            logger.warn("Failed to encrypt input: plain - {}, e - {}", plain, e.getMessage());
            return "Failed to encrypt your input: e - " + e.getMessage();
        }
    }

    @GetMapping("/decrypt_string")
    public String decryptString(@RequestParam String cipher) {
        try {
            logger.info("Try to decrypt string: cipher - {}", cipher);
            return dataProtection.decrypt(cipher);
        } catch (GenericServiceException e) {
            logger.warn("Failed to decrypt input: cipher - {}, e - {}", cipher, e.getMessage());
            return "Failed to decrypt your input: e - " + e.getMessage();
        }
    }

    @GetMapping("/encrypt")
    public <T> ResponseEntity encryptObject(@RequestBody T plain, @RequestParam String classType) {
        try {
            Class<?> type = Class.forName(classType);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Object obj = objectMapper.readValue(objectMapper.writeValueAsString(plain), type);
            logger.info("Try to encrypt object: plain - {}", obj);
            return ResponseEntity.ok(dataProtection.encrypt(obj));
        } catch (ClassNotFoundException e) {
            logger.warn("No Class Found: type - {}, plain - {}", classType, plain);
            return new ResponseEntity<>("No Class Found this name: " + classType, HttpStatus.BAD_REQUEST);
        } catch (JsonProcessingException | ClassCastException e) {
            logger.warn("Failed to parse input: type - {}, cipher - {}", classType, plain);
            return new ResponseEntity<>("Failed to parse your input: " + classType, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.warn("Failed to encrypt input: plain - {}, e - {}", plain, e.getMessage());
            return new ResponseEntity<>("Failed to encrypt your input: e - " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/decrypt")
    public <T> ResponseEntity decryptObject(@RequestBody T cipher, @RequestParam String classType) {
        try {
            Class<?> type = Class.forName(classType);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Object obj = objectMapper.readValue(objectMapper.writeValueAsString(cipher), type);
            logger.info("Try to decrypt object: cipher - {}", obj);
            return ResponseEntity.ok(dataProtection.decrypt(obj));
        } catch (ClassNotFoundException e) {
            logger.warn("No Class Found: type - {}, cipher - {}", classType, cipher);
            return new ResponseEntity<>("No Class Found this name: " + classType, HttpStatus.BAD_REQUEST);
        } catch (JsonProcessingException | ClassCastException e) {
            logger.warn("Failed to parse input: type - {}, cipher - {}", classType, cipher);
            return new ResponseEntity<>("Failed to parse your input: " + classType, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.warn("Failed to decrypt input: cipher - {}, e - {}", cipher, e.getMessage());
            return new ResponseEntity<>("Failed to decrypt your input: e - " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
