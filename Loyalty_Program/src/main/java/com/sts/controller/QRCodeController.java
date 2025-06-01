package com.sts.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpHeaders;
import com.sts.service.QRCodeService;

	@RestController
	@RequestMapping("/api/qrcode")
	public class QRCodeController {
		 @Autowired
		    private QRCodeService qrCodeService;
	// code for show your qrcode in postman
		    @GetMapping("/qr")
		    public ResponseEntity<byte[]> getQRCode(@RequestParam String email) {
		        try {
		            byte[] qrImage = qrCodeService.generateUserQRCode(email);
	
		            HttpHeaders headers = new HttpHeaders();
		            headers.setContentType(MediaType.IMAGE_PNG);
	
		            return new ResponseEntity<>(qrImage, headers, HttpStatus.OK);
		        } catch (Exception e) {
		            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		        }
		    }
		    
}