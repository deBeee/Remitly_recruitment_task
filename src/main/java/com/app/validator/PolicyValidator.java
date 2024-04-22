package com.app.validator;

import com.app.exceptions.InvalidPolicyException;
import com.app.model.AwsIAMRolePolicy;
import com.app.model.Statement;

import java.util.List;
import java.util.regex.Pattern;

public class PolicyValidator {
    private static final Pattern POLICY_NAME_PATTERN = Pattern.compile("[\\w+=,.@-]+");
    private static final List<String> ALLOWED_EFFECTS = List.of("Allow", "Deny");
    public static boolean validate(AwsIAMRolePolicy policy) {

        if(policy == null) {
            throw new NullPointerException("Policy is null!");
        }

        if (policy.PolicyName() == null || policy.PolicyName().isEmpty() || policy.PolicyName().length() > 128 || !POLICY_NAME_PATTERN.matcher(policy.PolicyName()).matches()) {
            throw new InvalidPolicyException("Invalid policy name: " + policy.PolicyName());
        }

        for (Statement statement : policy.PolicyDocument().Statement()) {

            if (!ALLOWED_EFFECTS.contains(statement.Effect())) {
                throw new InvalidPolicyException("Invalid effect in statement: " + statement.Effect());
            }
            if ("*".equals(statement.Resource())) {
                return false;
            }
        }
        return true;
    }
}
