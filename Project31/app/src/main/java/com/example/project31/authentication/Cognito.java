package com.example.project31.authentication;

/*
 * Developed by Keivan Kiyanfar on 10/7/18 10:35 PM
 * Last modified 10/7/18 10:35 PM
 * Copyright (c) 2018. All rights reserved.
 */

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentityprovider.model.SignUpResult;
import com.example.project31.Utils.SharedPreferencesHelper;

import static android.content.ContentValues.TAG;

public class Cognito {
    // ############################################################# Information about Cognito Pool
    private String poolID = "us-east-1_yG7LK12Ge";
    private String clientID = "6hfks3br50851m6bcc58dcfo6u";
    private String clientSecret = "1i1ctr44t4ehhpfcqkg5gkrudn740papq3f6lu0kp0nv40s0a2bp";
    private Regions awsRegion = Regions.US_EAST_1;         // Place your Region
    // ############################################################# End of Information about Cognito Pool
    private CognitoUserPool userPool;
    private CognitoUserAttributes userAttributes;       // Used for adding attributes to the user
    private Context appContext;

    private String userPassword;                        // Used for Login

    public Cognito(Context context){
        appContext = context;
        userPool = new CognitoUserPool(context, this.poolID, this.clientID, this.clientSecret, this.awsRegion);
        userAttributes = new CognitoUserAttributes();
    }

    public void signUpInBackground(String userId, String password){
        userPool.signUpInBackground(userId, password, this.userAttributes, null, signUpCallback);
        //userPool.signUp(userId, password, this.userAttributes, null, signUpCallback);
    }

    public void logout() {
        if (userPool != null) {
            CognitoUser currentUser = userPool.getCurrentUser();
            if (currentUser != null) {
                currentUser.signOut();
                SharedPreferencesHelper.getInstance(appContext).removeUser();
            }
        }
    }


    SignUpHandler signUpCallback = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, SignUpResult signUpResult) {
            Log.d(TAG, "Sign-up success");
            Toast.makeText(appContext,"Sign-up success"+signUpResult, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Exception exception) {
            Toast.makeText(appContext,"Sign-up failed", Toast.LENGTH_LONG).show();
            Log.d(TAG, "Sign-up failed: " + exception);
            exception.printStackTrace();
        }
    };

    public void confirmUser(String userId, String code){
        CognitoUser cognitoUser =  userPool.getUser(userId);
        cognitoUser.confirmSignUpInBackground(code,false, confirmationCallback);
        cognitoUser.confirmSignUp(code,false, confirmationCallback);
        SharedPreferencesHelper.getInstance(appContext).setUserEmail(userId);
    }
    // Callback handler for confirmSignUp API
    GenericHandler confirmationCallback = new GenericHandler() {

        @Override
        public void onSuccess() {


            Toast.makeText(appContext,"User Confirmed", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onFailure(Exception exception) {
            // User confirmation failed. Check exception for the cause.
            SharedPreferencesHelper.getInstance(appContext).removeUser();
        }
    };

    public void addAttribute(String key, String value){
        userAttributes.addAttribute(key, value);
    }

    public void userLogin(String userId, String password){
        CognitoUser cognitoUser =  userPool.getUser(userId);
        this.userPassword = password;
        SharedPreferencesHelper.getInstance(appContext).setUserEmail(userId);
        cognitoUser.getSessionInBackground(authenticationHandler);
    }
    // Callback handler for the sign-in process
    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
        @Override
        public void authenticationChallenge(ChallengeContinuation continuation) {

        }

        @Override
        public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
           Toast.makeText(appContext,"Sign in success", Toast.LENGTH_SHORT).show();
            //******


//            String userEmail= SharedPreferencesHelper.getInstance(appContext).getUserEmail();
//            Logger.getLogger(CreatePost.class.getName()).log(Level.SEVERE, "Check SP" + userEmail);

        }

        @Override
        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
            // The API needs user sign-in credentials to continue
            AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, userPassword, null);
            // Pass the user sign-in credentials to the continuation
            authenticationContinuation.setAuthenticationDetails(authenticationDetails);
            // Allow the sign-in to continue
            authenticationContinuation.continueTask();
        }

        @Override
        public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {
            // Multi-factor authentication is required; get the verification code from user
            //multiFactorAuthenticationContinuation.setMfaCode(mfaVerificationCode);
            // Allow the sign-in process to continue
            //multiFactorAuthenticationContinuation.continueTask();
        }

        @Override
        public void onFailure(Exception exception) {
            // Sign-in failed, check exception for the cause
            Toast.makeText(appContext,"Sign in Failure", Toast.LENGTH_SHORT).show();
            SharedPreferencesHelper.getInstance(appContext).removeUser();
        }
    };

    public String getPoolID() {
        return poolID;
    }

    public void setPoolID(String poolID) {
        this.poolID = poolID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public Regions getAwsRegion() {
        return awsRegion;
    }

    public void setAwsRegion(Regions awsRegion) {
        this.awsRegion = awsRegion;
    }

}


//import android.content.Context;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
//import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
//import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
//import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
//import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
//import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
//import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
//import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
//import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
//import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
//import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
//import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
//import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
//import com.amazonaws.regions.Regions;
//
//import static android.content.ContentValues.TAG;
//
//public class Cognito {
//    // ############################################################# Information about Cognito Pool
//    private String poolID = "us-east-1_yG7LK12Ge";
//    private String clientID = "6hfks3br50851m6bcc58dcfo6u";
//    private String clientSecret = "1i1ctr44t4ehhpfcqkg5gkrudn740papq3f6lu0kp0nv40s0a2bp";
//    private Regions awsRegion = Regions.US_EAST_1;         // Place your Region
//    // ############################################################# End of Information about Cognito Pool
//    private CognitoUserPool userPool;
//    private CognitoUserAttributes userAttributes;       // Used for adding attributes to the user
//    private Context appContext;
//
//    private String userPassword;                        // Used for Login
//
//    public Cognito(Context context){
//        appContext = context;
//        userPool = new CognitoUserPool(context, this.poolID, this.clientID, this.clientSecret, this.awsRegion);
//        userAttributes = new CognitoUserAttributes();
//    }
//
//    public void signUpInBackground(String userId, String password){
//        userPool.signUpInBackground(userId, password, this.userAttributes, null, signUpCallback);
//        //userPool.signUp(userId, password, this.userAttributes, null, signUpCallback);
//    }
//
//    SignUpHandler signUpCallback = new SignUpHandler() {
//        @Override
//        public void onSuccess(CognitoUser cognitoUser, boolean userConfirmed, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
//            // Sign-up was successful
//            Log.d(TAG, "Sign-up success");
//            Toast.makeText(appContext,"Sign-up success", Toast.LENGTH_LONG).show();
//            // Check if this user (cognitoUser) needs to be confirmed
//            if(!userConfirmed) {
//                // This user must be confirmed and a confirmation code was sent to the user
//                // cognitoUserCodeDeliveryDetails will indicate where the confirmation code was sent
//                // Get the confirmation code from user
//            }
//            else {
//                Toast.makeText(appContext,"Error: User Confirmed before", Toast.LENGTH_LONG).show();
//                // The user has already been confirmed
//            }
//        }
//        @Override
//        public void onFailure(Exception exception) {
//            Toast.makeText(appContext,"Sign-up failed", Toast.LENGTH_LONG).show();
//            Log.d(TAG, "Sign-up failed: " + exception);
//            exception.printStackTrace();
//        }
//    };
//
//    public void confirmUser(String userId, String code){
//        CognitoUser cognitoUser =  userPool.getUser(userId);
//        cognitoUser.confirmSignUpInBackground(code,false, confirmationCallback);
//        //cognitoUser.confirmSignUp(code,false, confirmationCallback);
//    }
//    // Callback handler for confirmSignUp API
//    GenericHandler confirmationCallback = new GenericHandler() {
//
//        @Override
//        public void onSuccess() {
//            // User was successfully confirmed
//            Toast.makeText(appContext,"User Confirmed", Toast.LENGTH_LONG).show();
//
//        }
//
//        @Override
//        public void onFailure(Exception exception) {
//            // User confirmation failed. Check exception for the cause.
//
//        }
//    };
//
//    public void addAttribute(String key, String value){
//        userAttributes.addAttribute(key, value);
//    }
//
//    public void userLogin(String userId, String password){
//        CognitoUser cognitoUser =  userPool.getUser(userId);
//        this.userPassword = password;
//        cognitoUser.getSessionInBackground(authenticationHandler);
//    }
//    // Callback handler for the sign-in process
//    AuthenticationHandler authenticationHandler = new AuthenticationHandler() {
//        @Override
//        public void authenticationChallenge(ChallengeContinuation continuation) {
//
//        }
//
//        @Override
//        public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
//            Toast.makeText(appContext,"Sign in success", Toast.LENGTH_LONG).show();
//
//        }
//
//        @Override
//        public void getAuthenticationDetails(AuthenticationContinuation authenticationContinuation, String userId) {
//            // The API needs user sign-in credentials to continue
//            AuthenticationDetails authenticationDetails = new AuthenticationDetails(userId, userPassword, null);
//            // Pass the user sign-in credentials to the continuation
//            authenticationContinuation.setAuthenticationDetails(authenticationDetails);
//            // Allow the sign-in to continue
//            authenticationContinuation.continueTask();
//        }
//
//        @Override
//        public void getMFACode(MultiFactorAuthenticationContinuation multiFactorAuthenticationContinuation) {
//            // Multi-factor authentication is required; get the verification code from user
//            //multiFactorAuthenticationContinuation.setMfaCode(mfaVerificationCode);
//            // Allow the sign-in process to continue
//            //multiFactorAuthenticationContinuation.continueTask();
//        }
//
//        @Override
//        public void onFailure(Exception exception) {
//            // Sign-in failed, check exception for the cause
//            Toast.makeText(appContext,"Sign in Failure", Toast.LENGTH_LONG).show();
//        }
//    };
//
//    public String getPoolID() {
//        return poolID;
//    }
//
//    public void setPoolID(String poolID) {
//        this.poolID = poolID;
//    }
//
//    public String getClientID() {
//        return clientID;
//    }
//
//    public void setClientID(String clientID) {
//        this.clientID = clientID;
//    }
//
//    public String getClientSecret() {
//        return clientSecret;
//    }
//
//    public void setClientSecret(String clientSecret) {
//        this.clientSecret = clientSecret;
//    }
//
//    public Regions getAwsRegion() {
//        return awsRegion;
//    }
//
//    public void setAwsRegion(Regions awsRegion) {
//        this.awsRegion = awsRegion;
//    }
//
//}


