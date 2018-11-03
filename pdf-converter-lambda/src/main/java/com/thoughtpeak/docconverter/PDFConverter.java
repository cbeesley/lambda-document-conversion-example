package com.thoughtpeak.docconverter;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Converts the source document into text
 */
public class PDFConverter {

    public static final String PDF_CONTENT_TYPE = "application/pdf";

    /**
     * Takes an input stream of bytes from the PDF and converts
     * it to plain text
     * @param is
     * @return
     */
    public static String convertToPlainText(InputStream is) {

        PDFTextStripper pdfStripper = null;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;

        try {
            // PDFBox 2.0.8 require org.apache.pdfbox.io.RandomAccessRead
            // RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            // PDFParser parser = new PDFParser(randomAccessFile);

            PDFParser parser = new PDFParser(new RandomAccessBuffer(is));
            parser.parse();
            cosDoc = parser.getDocument();
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            pdfStripper.setStartPage(1);
            pdfStripper.setEndPage(5);
            String parsedText = pdfStripper.getText(pdDoc);
            return parsedText;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "";
    }

}
