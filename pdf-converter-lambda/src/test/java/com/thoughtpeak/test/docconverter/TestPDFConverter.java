package com.thoughtpeak.test.docconverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtpeak.docconverter.PDFConverter;
import com.thoughtpeak.docconverter.FileRequest;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class TestPDFConverter {

    private FileRequest sampleRequest;

    @Before
    public void setup(){

        ObjectMapper mapper = new ObjectMapper();

        try {

            Path path = Paths.get(getClass().getResource("/sampleRequest.json").toURI());
            String content = new String(Files.readAllBytes(path));
            sampleRequest = mapper.readValue(content,FileRequest.class);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testConversionFromPdfToText() throws IOException, SAXException {

        byte[] decodedString = Base64.getDecoder().decode(new String(sampleRequest.getEncodedContent()).getBytes("UTF-8"));
        String convertedText = PDFConverter.convertToPlainText(new ByteArrayInputStream(decodedString));
        System.out.println(convertedText);
    }
}
