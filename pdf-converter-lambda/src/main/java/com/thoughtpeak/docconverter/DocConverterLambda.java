package com.thoughtpeak.docconverter;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtpeak.docconverter.configs.ConfigurationFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;


public class DocConverterLambda {


    /**
     * This runs through a gateway so we need to pass back a response object
     *
     * @param fileRequest
     * @param context - AWS Lambda context
     * @return
     */
    public GatewayResponse fileUploadHandler(FileRequest fileRequest, Context context) {

        LambdaLogger logger = context.getLogger();
        logger.log("received : " + fileRequest);

        String result = "";
        long contentLength;
        try {
            // first decode the base 64 content
            byte[] decodedString = Base64.getDecoder().decode(new String(fileRequest.getEncodedContent()).getBytes("UTF-8"));
            InputStream targetStream;

            //transform to text
            if(PDFConverter.PDF_CONTENT_TYPE.equals(fileRequest.getContentType())){

                String convertedText = PDFConverter.convertToPlainText(new ByteArrayInputStream(decodedString));
                contentLength = convertedText.length();
                targetStream = new ByteArrayInputStream(convertedText.getBytes());

            }else {// else its plain text so no conversion unless add html/xml parsing
                targetStream = new ByteArrayInputStream(decodedString);
                contentLength = fileRequest.getLength();
            }

            String fileName = "convertedDocs/" + fileRequest.getFileName();

            WriteToS3.writeToBucket(ConfigurationFactory.getDocumentConfiguration(fileName),"text/plain",contentLength,targetStream);

        } catch (JsonProcessingException e) {
            // Check cloudwatch logging
            logger.log("Problem with the json");
        } catch (IOException e) {
            logger.log("Problem with writing file to S3:" + e.getMessage());
            return new GatewayResponse("500","Error occurred during processing");
        }

        return new GatewayResponse("200","File has been converted");
    }

}