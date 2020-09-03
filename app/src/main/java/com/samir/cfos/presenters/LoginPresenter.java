package com.samir.cfos.presenters;

import com.google.firebase.firestore.FirebaseFirestore;
import com.samir.cfos.utils.Utilities;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class LoginPresenter {

    private WeakReference<View> view;

    private LoginPresenter.View getView() throws NullPointerException{
        if (view != null) {
            return view.get();
        } else {
            throw new NullPointerException("View is unavailable");
        }
    }

    public LoginPresenter(LoginPresenter.View view){
        this.view = new WeakReference<>(view);
    }

    public interface View {
        void loginSuccess(String success);
        void loginFailure(String failure);
    }

    public void login(String email, String password){
//        Log.e("loginPre: ", email + "\t" + password);

        FirebaseFirestore firebaseFirestore = Utilities.firebaseFirestore();


        Utilities.firebaseAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        getView().loginSuccess(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).toString());
                    }
                })
                .addOnFailureListener(e -> getView().loginFailure(e.getMessage()));
    }
}
