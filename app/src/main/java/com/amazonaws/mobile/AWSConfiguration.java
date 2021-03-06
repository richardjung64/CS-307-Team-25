//
// Copyright 2017 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.15
//
package com.amazonaws.mobile;

import com.amazonaws.regions.Regions;

/**
 * This class defines constants for the developer's resource
 * identifiers and API keys. This configuration should not
 * be shared or posted to any public source code repository.
 */
public class AWSConfiguration {
    // AWS MobileHub user agent string
    public static final String AWS_MOBILEHUB_USER_AGENT =
        "MobileHub bd8276aa-8f6d-49e4-9731-8dd62f215481 aws-my-sample-app-android-v0.15";
    // AMAZON COGNITO
    public static final Regions AMAZON_COGNITO_REGION =
      Regions.fromName("us-east-1");
    public static final String  AMAZON_COGNITO_IDENTITY_POOL_ID =
        "us-east-1:43cde55a-51f7-4d7a-a2ab-f77c948eed21";
    // Google Client ID for Web application
    public static final String GOOGLE_CLIENT_ID =
        "710056015402-mvb5r0ilp48gicet7c5il7emneqpvqav.apps.googleusercontent.com";
    // S3 BUCKET
    public static final String AMAZON_S3_USER_FILES_BUCKET =
        "stylin-userfiles-mobilehub-1048106400";
    // S3 BUCKET REGION
    public static final Regions AMAZON_S3_USER_FILES_BUCKET_REGION =
        Regions.fromName("us-east-1");
    public static final Regions AMAZON_DYNAMODB_REGION =
       Regions.fromName("us-east-1");
}
