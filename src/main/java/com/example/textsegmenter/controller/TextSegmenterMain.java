package com.example.textsegmenter.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.textsegmenter.service.TextSegmenterService;

@RestController
public class TextSegmenterMain {
	 @Autowired
	    private TextSegmenterService textSegmenterService;

	    @PostMapping("/segment")
	    public ResponseEntity<?> segmentPDF(@RequestParam("file") MultipartFile file, 
	                                        @RequestParam("cuts") int cuts) {
	        try {
	            List<String> outputFiles = textSegmenterService.processPDF(file, cuts);
	            
	            return new ResponseEntity<>(outputFiles, HttpStatus.OK);
	        } catch (IOException e) {
	            return new ResponseEntity<>("Error processing PDF file.", HttpStatus.BAD_REQUEST);
	        }
	    }

}
