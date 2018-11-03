package com.thoughtpeak.docconverter.configs;

public class ConfigurationFactory {

    /**
     * This is a sample configuration factory that you can use to create different configs.
     * Typically the region and bucket name will be the same for this lambda
     *
     * @return a new DocumentCOnfiguration for use in the S3 writer
     */
    public static DocumentConfiguration getDocumentConfiguration(String fileName){
        // Make sure that the region is properly set to your bucket, otherwise this will fail during DNS host lookup
        // in the lambda
        return new DocumentConfiguration("us-east-1","com.thoughtpeak.codedeploy",fileName);
    }
}
