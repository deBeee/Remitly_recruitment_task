package com.app.model;

public record AwsIAMRolePolicy(
        String PolicyName,
        PolicyDocument PolicyDocument
) {
}
