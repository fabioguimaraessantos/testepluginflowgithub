package com.ciandt.pms.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Attachments {
    @JsonProperty("from_s3")
    private boolean fromS3;
    @JsonProperty("s3_bucket")
    private String s3Bucket;
    @JsonProperty("s3_key")
    private String s3Key;
    private String filename;
    private String content;
    private String encoding;

    public boolean isFromS3() {
        return fromS3;
    }

    public void setFromS3(boolean fromS3) {
        this.fromS3 = fromS3;
    }

    public String getS3Bucket() {
        return s3Bucket;
    }

    public void setS3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
