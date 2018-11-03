package com.thoughtpeak.docconverter;

import java.util.HashMap;
import java.util.Map;

/**
 * Object that represents the request to the webservice to upload a
 * base 64 encoded file
 */
public class FileRequest {

    private String contentType;

    private String fileName;

    private long length;

    private String encodedContent;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getEncodedContent() {
        return encodedContent;
    }

    public void setEncodedContent(String encodedContent) {
        this.encodedContent = encodedContent;
    }

    @Override
    public String toString() {
        return "FileRequest{" +
                "contentType='" + contentType + '\'' +
                ", fileName='" + fileName + '\'' +
                ", length=" + length +
                '}';
    }
}
