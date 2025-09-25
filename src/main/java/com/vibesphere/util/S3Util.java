package com.vibesphere.util;

import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.*;
import software.amazon.awssdk.services.s3.model.*;
import java.io.InputStream;
import java.util.Properties;

public class S3Util {
    private static S3Client s3;
    private static String bucket;
    private static String prefix = "";

    static {
        try (InputStream in = S3Util.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties p = new Properties();
            p.load(in);
            bucket = p.getProperty("aws.s3.bucket");
            String region = p.getProperty("aws.region", "ap-south-1");
            String pre = p.getProperty("aws.s3.prefix", "");
            if (pre != null && !pre.isBlank()) prefix = pre.endsWith("/") ? pre : pre + "/";

            Region r = Region.of(region);

            // Credentials: SDK will pick up from env vars or default provider chain (recommended)
            s3 = S3Client.builder()
                    .region(r)
                    .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("S3Util init failed: " + e.getMessage(), e);
        }
    }

    public static String upload(String keyName, InputStream data, long length, String contentType) {
        String key = prefix + keyName;
        PutObjectRequest por = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        s3.putObject(por, RequestBody.fromInputStream(data, length));
        // Return public URL (depends on bucket policy)
        return String.format("https://%s.s3.amazonaws.com/%s", bucket, key);
    }
}
