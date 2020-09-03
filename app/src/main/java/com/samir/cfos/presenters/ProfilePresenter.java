package com.samir.cfos.presenters;

import android.util.Log;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.samir.cfos.constants.AppConstants;
import com.samir.cfos.helpers.ShowToast;
import com.samir.cfos.models.RegisterModel;
import com.samir.cfos.utils.Utilities;

import java.lang.ref.WeakReference;

public class ProfilePresenter {

    private CollectionReference collectionReference = Utilities.firebaseFirestore().collection(AppConstants.USER_DETAILS);
    private RegisterModel registerModel;

    private WeakReference<View> view;

    public ProfilePresenter(ProfilePresenter.View view){
        this.view = new WeakReference<>(view);
    }

    public interface View {
        void onProfileRetrieveSuccess(RegisterModel registerModel);
        void onProfileRetrieveFailure(String s);
    }

    private ProfilePresenter.View getView() throws NullPointerException{
        if(view != null){
            return view.get();
        } else {
            throw new NullPointerException("View is unavailable");
        }
    }

    public void retrieveUser(){

        collectionReference.get().addOnSuccessListener(queryDocumentSnapshots -> {

//                iterate all the collection to identify user's docId
            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

//                   check document id from sharedPreferences
                if(Utilities.getSavedDocId() == null){
                    Log.e("retrieveUser: ", "Profile data is empty");
                    ShowToast.withLongMessage("Profile is empty");

                } else {

                    Log.e( "onSuccess: ", documentSnapshot.getId());
                    if(Utilities.getSavedDocId().equals(documentSnapshot.getId())){

                        Log.e( "DocId: ",Utilities.getSavedDocId() + "\t" + documentSnapshot.getId());

                        registerModel = documentSnapshot.toObject(RegisterModel.class);
                        getView().onProfileRetrieveSuccess(registerModel);
                        break;
                }

                }
            }
        }).addOnFailureListener(e -> getView().onProfileRetrieveFailure(e.getMessage()));
    }
}
