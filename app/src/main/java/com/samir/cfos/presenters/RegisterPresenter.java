package com.samir.cfos.presenters;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.samir.cfos.constants.AppConstants;
import com.samir.cfos.helpers.ShowToast;
import com.samir.cfos.models.RegisterModel;
import com.samir.cfos.utils.Utilities;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class RegisterPresenter {

//    create User_details collection on firebase
    private CollectionReference collectionReference = Utilities.firebaseFirestore().collection(AppConstants.USER_DETAILS);
    private RegisterModel registerModel;

    private WeakReference<View> view;

    private RegisterPresenter.View getView() throws NullPointerException {
        if (view != null) {
            return view.get();
        } else {
            throw new NullPointerException("View is unavailable");
        }
    }

    public RegisterPresenter(RegisterPresenter.View view) {
        this.view = new WeakReference<>(view);
    }

    public interface View {
        void onRegisterSuccess(RegisterModel registerModel);
        void onRegisterFailure(String failure);
        void onAlreadySignedIn(String s);
    }

    public void registerUser(String fullname, String gender, String address, String contact, String email, String password, String role) {

//        check whether user is signed or not
        Utilities.firebaseAuth().fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            if (Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getSignInMethods()).isEmpty()) {
                Log.d("onComplete: ", "User is not registered");

//                  register user in firebase
                Utilities.firebaseAuth().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener((Task<AuthResult> task1) -> {
                            if (task1.isSuccessful())//

                                //  get token
                                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<InstanceIdResult> token) {
                                        if (task.isSuccessful()) {
                                            Log.e("token: ", Objects.requireNonNull(token.getResult()).getToken());
                                            Utilities.saveDeviceToken(Objects.requireNonNull(token.getResult()).getToken());

//                                            initialize RegisterModel to store data
                                            registerModel = new RegisterModel(fullname, gender, address, contact, email, token.getResult().getToken(), role);
                                            uploadToFirebase(registerModel);
                                        } else {
                                            Log.e("token: ", String.valueOf(task.getException()));
                                        }
                                    }
                                });
                        })
                        .addOnFailureListener(e -> {
                            ShowToast.withLongMessage("Unable to register user");
                            getView().onRegisterFailure(e.getMessage());
                        });

            } else {
                getView().onAlreadySignedIn("User already registered with this mail");
            }
        })
                .addOnFailureListener(e -> Log.d("onFailure: ", "sign in fetch process error \n " + e.getMessage()));

    }

//    upload registration details to firebase
    private void uploadToFirebase(RegisterModel regModel) {

//        set timestamp as document name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Log.e("uploadToFirebase: ", timeStamp);

//        save timestamp to shared preferences
        Utilities.saveDocId(timeStamp);

//        add data to firebase
        collectionReference.document(timeStamp).set(regModel);
        getView().onRegisterSuccess(regModel);
    }

}