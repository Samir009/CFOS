package com.samir.cfos.presenters;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.samir.cfos.constants.AppConstants;
import com.samir.cfos.models.FoodModel;
import com.samir.cfos.utils.Utilities;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CanteenHomePresenter {

    private CollectionReference collectionReference = Utilities.firebaseFirestore().collection(AppConstants.DRINKS);
    private CollectionReference colVegReference = Utilities.firebaseFirestore().collection(AppConstants.VEG);
    private CollectionReference colNonVegReference = Utilities.firebaseFirestore().collection(AppConstants.NON_VEG);

    List<FoodModel> mFoodModelList ;
    List<FoodModel> mVegModelList ;
    List <FoodModel> mNonVegFoodModelList ;

    private WeakReference<View> view;

    public interface View {
        void onDrinksRetrieved(List<FoodModel> foodModel);
        void onVegFoodRetrieved(List<FoodModel> mVegModelList);
        void onNonVegFoodRetrieved(List<FoodModel> mFoodModelList);

        void onDrinksRetrieveFailure();
        void onVegFoodRetrieveFailure();
        void onNonVegFoodRetrieveFailure();

        void onDrinksDeleted(String s);
        void onVegDeleted(String s);
        void onNonVegDeleted(String s);

    }

    public CanteenHomePresenter(CanteenHomePresenter.View view){
        this.view = new WeakReference<>(view);
    }

    private CanteenHomePresenter.View getView() throws NullPointerException{
        if(view != null)
            return view.get();
        else
            throw new NullPointerException("View is unavailable");
    }

//    retrieve drinks from firebase
    public void retrieveDrinksFromFirebase(){

        Log.e( "retrieveDrinkFirebase: ", "called");

        mFoodModelList = null;
        mFoodModelList = new ArrayList<>();
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.isEmpty()){
                    Log.e("onSuccess: ", "drinks data is empty");
                } else {
                    FoodModel foodModel;
                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        foodModel = documentSnapshot.toObject(FoodModel.class);

//                        Log.e("onSucc: ", foodModel.getName());
                        mFoodModelList.add(foodModel);
                }

                    getView().onDrinksRetrieved(mFoodModelList);
                }
            }

        });
    }

//    retrieve non veg from firebase
    public void retrieveNonVegFromFirebase(){

        Log.e( "retrieveNonVegFirebase:", "called");

        mNonVegFoodModelList = null;
        mNonVegFoodModelList = new ArrayList<>();

        colNonVegReference.get().addOnSuccessListener(queryDocumentSnapshots -> {

            if(queryDocumentSnapshots.isEmpty()){
                Log.e("onSuccess: ", "drinks data is empty");
            } else {
                FoodModel foodModel;
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    foodModel = documentSnapshot.toObject(FoodModel.class);

//                        Log.e("onSucc: ", foodModel.getName());
                    mNonVegFoodModelList.add(foodModel);
                }
//                Log.e( "NON-VEG: ", new GsonBuilder().create().toJson(mNonVegFoodModelList));
                getView().onNonVegFoodRetrieved(mNonVegFoodModelList);
            }
        });
    }

    //    retrieve veg from firebase
    public void retrieveVegFromFirebase(){

        Log.e( "retrieveVegFirebase:", "called");

        mVegModelList = null;
        mVegModelList = new ArrayList<>();

        colVegReference.get().addOnSuccessListener(queryDocumentSnapshots -> {

            if(queryDocumentSnapshots.isEmpty()){
                Log.e("onSuccess: ", "drinks data is empty");
            } else {
                FoodModel foodModel;
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    foodModel = documentSnapshot.toObject(FoodModel.class);

//                        Log.e("onSucc: ", foodModel.getName());
                    mVegModelList.add(foodModel);
                }
//                Log.e( "NON-VEG: ", new GsonBuilder().create().toJson(mNonVegFoodModelList));
                getView().onVegFoodRetrieved(mVegModelList);
            }
        });
    }

    public void deleteDrinksItem(String id){
        collectionReference.document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                getView().onDrinksDeleted(id + " \t deleted");;
                Log.e("deleteDrinksItem: ", "deleted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.e("deleteDrinksItem: ", "failed to delete");
            }
        });
    }

    public void deleteVegItem(String id){
        colVegReference.document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                getView().onVegDeleted(id + " \t deleted");;
                Log.e("deleteVegItem: ", "deleted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.e("deleteVegItem: ", "failed to delete");
            }
        });
    }

    public void deleteNonVegItem(String id){
        colNonVegReference.document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                getView().onNonVegDeleted(id + " \t deleted");;
                Log.e("deleteVegItem: ", "deleted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.e("deleteNonVegItem: ", "failed to delete");
            }
        });
    }
}
