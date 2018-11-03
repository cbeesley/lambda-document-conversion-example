package com.thoughtpeak.docconverter;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.thoughtpeak.docconverter.configs.DocumentConfiguration;

import java.io.IOException;
import java.io.InputStream;

public class WriteToS3 {

    /**
     * This is used to tag meta data in S3 using the AWS meta prefix
     */
    private static String S3_META_PREFIX = "x-amz-meta-";

    /**
     *
     * @param configuration The doc config that has metadata
     * @param contentType Such as plain/text or other mime type
     * @param targetStream - The byte stream to write to the target
     * @throws IOException
     */
    public static void writeToBucket(DocumentConfiguration configuration, String contentType,long inputContentLength, InputStream targetStream) throws IOException {

        try {

            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(configuration.getClientRegion())
                    // this is for local testing
                    //.withCredentials(new ProfileCredentialsProvider())
                    // This is needed so that the lambda IAM credentials are used
                    .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                    .build();

            // Upload a text string as a new object.
            //s3Client.putObject(bucketName, stringObjKeyName, "Uploaded String Object");

            ObjectMetadata metadata = new ObjectMetadata();

            // Add the user defined metadata
            configuration.getMetaData().forEach((k,v) -> {
                        metadata.addUserMetadata(S3_META_PREFIX + k, v);
                    }
            );
            metadata.setContentType(contentType);
            metadata.setContentLength(inputContentLength);
            //metadata.addUserMetadata("x-amz-meta-title", "someTitle");
            // Upload a file as a new object with ContentType and title specified.
            PutObjectRequest request = new PutObjectRequest(configuration.getBucketName(), configuration.getFileObjKeyName(), targetStream,metadata);

            s3Client.putObject(request);
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            throw new IOException("Amazon Service Exception: " + e.getMessage());
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.

            throw new IOException("SDK Client Exception: " + e.getMessage());
        }
    }

}
