package com.samir.cfos.presenters;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.samir.cfos.constants.AppConstants;
import com.samir.cfos.helpers.ShowToast;
import com.samir.cfos.models.RegisterModel;
import com.samir.cfos.utils.Utilities;

import java.lang.ref.WeakReference;

public class EditProfilePresenter {

    private CollectionReference collectionReference = Utilities.firebaseFirestore().collection(AppConstants.USER_DETAILS);
    private RegisterModel registerModel;

    private WeakReference<EditProfilePresenter.View> view;

    private EditProfilePresenter.View getView() throws NullPointerException{
        if(view != null){
            return view.get();
        } else {
            throw new NullPointerException("View is unavailable");
        }
    }

    public EditProfilePresenter (EditProfilePresenter.View view){
        this.view = new WeakReference<>(view);
    }

    public interface View {
        void onSuccess(RegisterModel registerModel);
        void onFailure(String s);
        void onEditSuccess(String string);
        void onEditFailure(String fail);
    }

    public void getUserData(){


        collectionReference.get().addOnSuccessListener(queryDocumentSnapshots -> {

//                iterate all the collection to identify user's docId
            for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                if(Utilities.getSavedDocId() == null){

                    Log.e( "getUserData: ", "EditProfile data retrieve got null");
                    ShowToast.withLongMessage("User data is empty");

                } else {
                    //                    get document id
                    Log.e( "onSuccess: ", documentSnapshot.getId());
                    if(Utilities.getSavedDocId().equals(documentSnapshot.getId())){

                        Log.e( "DocId: ",Utilities.getSavedDocId() + "\t" + documentSnapshot.getId());

                        registerModel = documentSnapshot.toObject(RegisterModel.class);
                        getView().onSuccess(registerModel);
                        break;
                }

                }
            }
        }).addOnFailureListener(e -> getView().onFailure(e.getMessage()));
    }

    public void editDetails(String name, String gender, String contact, String address, String email){

        collectionReference.document(Utilities.getSavedDocId()).update("name", name,
                "gender", gender,
                "contact", contact, "address", address, "email", email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                getView().onEditSuccess("Edit Successful !!!");
            }
        }). addOnFailureListener(e -> getView().onEditFailure(e.getMessage()));
    }
}
