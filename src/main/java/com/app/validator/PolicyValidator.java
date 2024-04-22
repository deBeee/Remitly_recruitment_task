package com.app.validator;

import com.app.model.AwsIAMRolePolicy;
import com.app.model.Statement;

public class PolicyValidator {
    public static boolean validate(AwsIAMRolePolicy policy){

        if(policy == null) return false;

        for (Statement statement : policy.PolicyDocument().Statement()) {
            if ("*".equals(statement.Resource())) {
                return false;
            }
        }
        return true;
    }
}
