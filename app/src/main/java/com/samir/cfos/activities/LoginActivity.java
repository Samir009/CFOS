package com.samir.cfos.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.internal.RegistrationMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.samir.cfos.R;
import com.samir.cfos.constants.AppConstants;
import com.samir.cfos.databinding.ActivityLoginBinding;
import com.samir.cfos.helpers.ShowToast;
import com.samir.cfos.models.RegisterModel;
import com.samir.cfos.presenters.LoginPresenter;
import com.samir.cfos.utils.Util;
import com.samir.cfos.utils.Utilities;
import com.samir.cfos.utils.UtilitiesFunctions;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements LoginPresenter.View, View.OnClickListener {

    private ActivityLoginBinding binding;
    private LoginPresenter loginPresenter;

    private boolean isCanteenStaff = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        checkUserLogIn();

        initializeListeners();
    }

    private void checkUserLogIn(){
        if(Utilities.isUserLoggedIn()){
            startActivity(new Intent(this, CanteenHomeActivity.class));
            finish();
        }
    }

    protected void initializeListeners() {

        loginPresenter = new LoginPresenter(this);
        binding.LoginBtnId.setOnClickListener(this);
        binding.RegisterHereId.setOnClickListener(this);
    }

    private void showProgressBar() {
        binding.myProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        binding.myProgressBar.setVisibility(View.GONE);

    }

    private void checkInternet(){
        if(UtilitiesFunctions.isNetworkAvailable(this)){
            if(Utilities.firebaseAuth().getCurrentUser() != null){
                Log.e( "FirebaseLogin: ", "User already logged in");
                ShowToast.withLongMessage("User already logged in!... Now try again");
                Utilities.firebaseAuth().signOut();
            } else {
                checkLoginDetails();
            }
        } else {
            ShowToast.withLongMessage("Internet failure!");
        }
    }

    private void checkLoginDetails(){

        showProgressBar();

        if(!validUsername() | !validPassword()){
            return;
        }

//        send data to presenter
        loginPresenter.login(Objects.requireNonNull(binding.EmailId.getText()).toString(),
                Objects.requireNonNull(binding.PasswordId.getText()).toString());
    }

    private boolean validUsername(){
        if(Objects.requireNonNull(binding.EmailId.getText()).toString().isEmpty()){
            binding.EmailId.setError("Empty");
            hideProgressBar();
            return false;

        } else {
            return true;
        }
    }

    private boolean validPassword(){
        if(Objects.requireNonNull(binding.PasswordId.getText()).toString().isEmpty()){
            binding.PasswordId.setError("Empty");
            hideProgressBar();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.LoginBtnId:
              checkInternet();
               break;

           case R.id.RegisterHereId:
               startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
               break;

//           case R.id.fingerprint_id:
//               break;
       }
    }

    @Override
    public void loginSuccess(String success) {
        Log.e( "loginSuccess: ", success);
        hideProgressBar();

        CollectionReference collectionReference = Utilities.firebaseFirestore().collection(AppConstants.USER_DETAILS);
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    RegisterModel registerModel = documentSnapshot.toObject(RegisterModel.class);

//                    get current logged user email
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String userEmail = user.getEmail();
                        assert userEmail != null;
                        if(userEmail.equals(registerModel.getEmail())){


                            Utilities.setLoggedIn(true);

                            startActivity(new Intent(getApplicationContext(), CanteenHomeActivity.class));
                            finish();

                            break;
                        }

                    } else {
                        // No user is signed in
                        ShowToast.withMessage("You are not authorized");
                        Utilities.firebaseAuth().signOut();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("LoginFailure: ", e.getMessage());
            }
        });


        //  get token
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(
                new OnCompleteListener<InstanceIdResult>() {

                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        Log.e("login get TOken: ", Objects.requireNonNull(task.getResult()).getToken());
//                        Utilities.saveDeviceToken();
                    }});


//        Utilities.saveDeviceToken("dnkdn");

    }

    @Override
    public void loginFailure(String failure) {
        Log.e("loginFailure: ", failure);
        binding.EmailId.setError("Email or password wrong");
        binding.PasswordId.setError("Email or password wrong");
        hideProgressBar();
    }
}

//save logged in status on shared preferences