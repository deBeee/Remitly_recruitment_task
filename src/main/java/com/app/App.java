package com.app;

import com.app.model.AwsIAMRolePolicy;
import com.app.validator.PolicyValidator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class App {
    public static void main(String[] args) {
        String filename = "data.json";
        Gson gson = new GsonBuilder().create();

        try (FileReader fileReader = new FileReader(filename)) {
            AwsIAMRolePolicy awsIAMRolePolicy = gson.fromJson(fileReader, AwsIAMRolePolicy.class);
            System.out.println(awsIAMRolePolicy);
            System.out.println("Policy validation result: " + PolicyValidator.validate(awsIAMRolePolicy));
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(e);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}