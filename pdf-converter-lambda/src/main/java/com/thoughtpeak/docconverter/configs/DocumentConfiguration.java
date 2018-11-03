package com.thoughtpeak.docconverter.configs;

import java.util.HashMap;
import java.util.Map;

/**
 * The configuration to send a file to S3. This should be internally defined and not
 * exposed to web api configuration for security reasons. The preffered way is to
 * create a factory that generates the configuration.
 */
public class DocumentConfiguration {

    /**
     * Client region, might hardcode this if you only have one
     * region
     */
    private String clientRegion;
    /**
     * Name of the S3 bucket
     */
    private String bucketName;

    /**
     * Name of the file as you want defined in S3 such as convertedDocs/someFilename.pdf
     */
    private String fileObjKeyName;

    /**
     * Used to set custom meta data on the S3 object
     */
    private Map<String,String> metaData = new HashMap<>();

    public DocumentConfiguration(String clientRegion, String bucketName, String fileObjKeyName) {
        this.clientRegion = clientRegion;
        this.bucketName = bucketName;

        this.fileObjKeyName = fileObjKeyName;
    }

    public String getClientRegion() {
        return clientRegion;
    }

    public String getBucketName() {
        return bucketName;
    }

    public String getFileObjKeyName() {
        return fileObjKeyName;
    }

    public void setMetaDataAttribute(String key,String value){
        metaData.put(key,value);
    }

    public Map<String,String> getMetaData(){
        return metaData;
    }
}
