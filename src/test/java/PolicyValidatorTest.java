import com.app.model.AwsIAMRolePolicy;
import com.app.model.PolicyDocument;
import com.app.model.Statement;
import com.app.validator.PolicyValidator;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PolicyValidatorTest {

    @Test
    public void should_return_true_if_statement_without_single_asterisk() {
        List<Statement> statements = List.of(
                new Statement("Sid1", "Allow", List.of("Action1"), "Resource1")
        );
        AwsIAMRolePolicy policy = new AwsIAMRolePolicy("PolicyName",
                new PolicyDocument("2012-10-17", statements));

        boolean result = PolicyValidator.validate(policy);

        assertTrue(result);
    }

    @Test
    public void should_return_true_if_multiple_statements_without_single_asterisk() {
        List<Statement> statements = List.of(
                new Statement("Sid1", "Allow", List.of("Action1"), "Resource1"),
                new Statement("Sid2", "Allow", List.of("Action2"), "Resource2"),
                new Statement("Sid3", "Allow", List.of("Action3"), "Resource3")
        );
        AwsIAMRolePolicy policy = new AwsIAMRolePolicy("PolicyName",
                new PolicyDocument("2012-10-17", statements));

        boolean result = PolicyValidator.validate(policy);

        assertTrue(result);
    }

    @Test
    public void should_return_false_if_statement_is_single_asterisk() {

        List<Statement> statements = List.of(
                new Statement("Sid1", "Allow", List.of("Action1"), "*")
        );
        AwsIAMRolePolicy policy = new AwsIAMRolePolicy("PolicyName",
                new PolicyDocument("2012-10-17", statements));

        boolean result = PolicyValidator.validate(policy);

        assertFalse(result);
    }

    @Test
    public void should_return_false_if_at_least_one_statement_is_single_asterisk() {

        List<Statement> statements = List.of(
                new Statement("Sid1", "Allow", List.of("Action1"), "Resource1"),
                new Statement("Sid2", "Allow", List.of("Action2"), "*"),
                new Statement("Sid3", "Allow", List.of("Action3"), "Resource3")
        );
        AwsIAMRolePolicy policy = new AwsIAMRolePolicy("PolicyName",
                new PolicyDocument("2012-10-17", statements));

        boolean result = PolicyValidator.validate(policy);

        assertFalse(result);
    }

    @Test
    public void should_return_true_if_there_are_no_statements_containing_rescource_provided() {

        AwsIAMRolePolicy emptyPolicy = new AwsIAMRolePolicy("EmptyPolicy",
                new PolicyDocument("2012-10-17", Collections.emptyList()));

        boolean result = PolicyValidator.validate(emptyPolicy);

        assertTrue(result);
    }

    @Test
    public void should_return_false_if_policy_is_null() {
        boolean result = PolicyValidator.validate(null);

        assertFalse(result);
    }

    @Test
    public void should_return_true_if_resource_contains_multiple_asterisks() {
        // Arrange
        List<Statement> statementsWithMultipleAsterisks = List.of(
                new Statement("Sid1", "Allow", List.of("Action1"), "***")
        );
        AwsIAMRolePolicy policyWithMultipleAsterisks = new AwsIAMRolePolicy("Policy4", new PolicyDocument("2012-10-17", statementsWithMultipleAsterisks));

        // Act
        boolean result = PolicyValidator.validate(policyWithMultipleAsterisks);

        // Assert
        assertTrue(result);
    }

    @Test
    public void should_return_proper_reponse_for_mixed_statements() {

        List<Statement> statementsWithoutAsterisk = List.of(
                new Statement("Sid1", "Allow", List.of("Action1"), "Resource1"),
                new Statement("Sid2", "Allow", List.of("Action2"), "Resource2")
        );
        AwsIAMRolePolicy policyWithoutAsterisk = new AwsIAMRolePolicy("Policy1", new PolicyDocument("2012-10-17", statementsWithoutAsterisk));

        List<Statement> statementsWithAsterisk = List.of(
                new Statement("Sid1", "Allow", List.of("Action1"), "*"),
                new Statement("Sid2", "Allow", List.of("Action2"), "*")
        );
        AwsIAMRolePolicy policyWithAsterisk = new AwsIAMRolePolicy("Policy2", new PolicyDocument("2012-10-17", statementsWithAsterisk));

        List<Statement> mixedStatements = List.of(
                new Statement("Sid1", "Allow", List.of("Action1"), "Resource1"),
                new Statement("Sid2", "Allow", List.of("Action2"), "*"),
                new Statement("Sid3", "Allow", List.of("Action3"), "Resource3")
        );
        AwsIAMRolePolicy mixedPolicy = new AwsIAMRolePolicy("Policy3", new PolicyDocument("2012-10-17", mixedStatements));

        assertAll("Mixed Statements Validation",
                () -> assertTrue(PolicyValidator.validate(policyWithoutAsterisk), "All statements without asterisk should return true"),
                () -> assertFalse(PolicyValidator.validate(policyWithAsterisk), "All statements with asterisk should return false"),
                () -> assertFalse(PolicyValidator.validate(mixedPolicy), "Mixed statements should return false")
        );
    }
}
