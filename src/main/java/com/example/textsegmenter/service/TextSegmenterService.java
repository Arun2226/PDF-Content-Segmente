package com.example.textsegmenter.service;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.textsegmenter.dto.Gap;
import com.example.textsegmenter.dto.TextBlock;
@Service
public class TextSegmenterService {
	public List<String> processPDF(MultipartFile file, int cuts) throws IOException {
		
		PDDocument document = PDDocument.load(file.getInputStream());

		
		List<TextBlock> textBlocks = extractTextBlocks(document);

		List<Integer> cutPositions = detectWhitespaceAndCuts(textBlocks, cuts);
		
		List<String> outputFiles = splitPDF(document, cutPositions);

		document.close();
		return outputFiles;
	}

	private List<TextBlock> extractTextBlocks(PDDocument document) throws IOException {
		List<TextBlock> textBlocks = new ArrayList<>();

		PDFTextStripperByArea stripper = new PDFTextStripperByArea();
		stripper.setSortByPosition(true);

		Rectangle rect = new Rectangle(0, 0, 595, 842);
		stripper.addRegion("region", rect);

		for (int page = 0; page < document.getNumberOfPages(); ++page) {
			stripper.extractRegions(document.getPage(page));
			String pageText = stripper.getTextForRegion("region");

			textBlocks.add(new TextBlock(pageText, 0, 0, 0, 0)); 
		}

		return textBlocks;
	}

	private List<Integer> detectWhitespaceAndCuts(List<TextBlock> textBlocks, int cuts) {
		  textBlocks.sort(Comparator.comparing(TextBlock::getY));

		    // Step 2: Calculate vertical gaps between consecutive text blocks
		    List<Gap> gaps = new ArrayList<>();
		    for (int i = 0; i < textBlocks.size() - 1; i++) {
		        float gapSize = textBlocks.get(i + 1).getY() - textBlocks.get(i).getY();
		        gaps.add(new Gap(gapSize, (textBlocks.get(i).getY() + textBlocks.get(i + 1).getY()) / 2));  // midpoint of the gap
		    }

		    gaps.sort((g1, g2) -> Float.compare(g2.getSize(), g1.getSize()));

		    List<Integer> cutPositions = new ArrayList<>();
		    for (int i = 0; i < Math.min(cuts, gaps.size()); i++) {
		        cutPositions.add(Math.round(gaps.get(i).getPosition()));
		    }
		    
		    cutPositions.sort(Integer::compareTo);

		    return cutPositions;
	}

	private List<String> splitPDF(PDDocument document, List<Integer> cutPositions) throws IOException {
		List<String> outputFiles = new ArrayList<>();
		for (int i = 0; i < cutPositions.size(); i++) {
            
            PDDocument newDocument = new PDDocument();
            
            String fileName = "output_segment_" + i + ".pdf";
            newDocument.save(fileName);
            outputFiles.add(fileName);
            newDocument.close();
        }
		return outputFiles;
	}

}
