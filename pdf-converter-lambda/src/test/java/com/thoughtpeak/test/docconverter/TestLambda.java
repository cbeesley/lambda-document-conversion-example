package com.thoughtpeak.test.docconverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtpeak.docconverter.DocConverterLambda;
import com.thoughtpeak.docconverter.FileRequest;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class TestLambda {

    private FileRequest sampleRequest;

    @Before
    public void setup(){

        sampleRequest = new FileRequest();
        sampleRequest.setContentType("application/pdf");
        sampleRequest.setFileName("sampleFile.txt");
        File file = new File("/Users/chrisbeesley/Downloads/apple_developer_agreement.pdf");


        ObjectMapper mapper = new ObjectMapper();
        try {

            byte[] fileContent = Files.readAllBytes(file.toPath());

            sampleRequest.setLength(file.length());
            sampleRequest.setEncodedContent(Base64.getEncoder().encodeToString(fileContent));

            System.out.println(mapper.writeValueAsString(sampleRequest));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFunction() throws IOException{

        //DocConverterLambda test = new DocConverterLambda();
        //System.out.println(test.handleFileRequest(sampleRequest));

    }

    public String generateBase64Encoding() throws IOException {

        File file = new File("");
        byte[] fileContent = Files.readAllBytes(file.toPath());
        return Base64.getEncoder().encodeToString(fileContent);
    }
}
