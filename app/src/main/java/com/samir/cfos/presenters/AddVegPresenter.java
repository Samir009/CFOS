package com.samir.cfos.presenters;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.gson.GsonBuilder;
import com.samir.cfos.constants.AppConstants;
import com.samir.cfos.helpers.ShowToast;
import com.samir.cfos.models.FoodModel;
import com.samir.cfos.utils.Utilities;

import java.lang.ref.WeakReference;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class AddVegPresenter {

    //    name of folder where images will store
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference(AppConstants.VEG_IMG);
    private StorageTask mUploadTask;
    private CollectionReference collectionsReference = Utilities.firebaseFirestore().collection(AppConstants.VEG);

    private WeakReference<View> view;
    Context context;
    boolean exists = false;

    private View getView() throws NullPointerException{
        if(view != null){
            return view.get();
        } else {
            throw new NullPointerException("View is unavailable");
        }
    }

    public AddVegPresenter(View view){
        this.view = new WeakReference<>(view);
    }

    public interface View {
        void onAddVegSuccess(String s);
        void onAddVegExist(String s);
        void onAddVegFailure(String s);
    }

    //    get file extension from selected file
    private String getFileExtension(Uri uri) {
        ContentResolver cr = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


    public void addVegDetails(Context con, String name, String price, Uri imgUri){

        this.context = con;

        if (mUploadTask != null && mUploadTask.isInProgress()) {
            ShowToast.withLongMessage("upload in progress");

        } else {
            collectionsReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    FoodModel foodModel;
                    for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots) {

                        foodModel = querySnapshot.toObject(FoodModel.class);

                        Log.e("vegFoodData: ", new GsonBuilder().create().toJson(foodModel));

                        if (foodModel.getName().equals(name)) {
                            exists = true;
                            break;
                        }
                    }

                    if (exists) {
                        exists = false;
                        getView().onAddVegExist(name + " exists");

                    } else {
                        uploadToFirebase(name, price, imgUri);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("onFailure: ", Objects.requireNonNull(e.getMessage()));
                }
            });
        }

    }

    //    upload data to firebase
    private void uploadToFirebase(String FoodName, String FoodPrice, Uri imgUri) {

        //   creates name of image file
        StorageReference fileRef = mStorageRef.child(FoodName + "." + getFileExtension(imgUri));

//        put image file and upload
        mUploadTask = fileRef.putFile(imgUri);

//        use Task to retrieve imgUri stored in mUploadTask
        Task urlTask = mUploadTask.continueWithTask((Continuation) task -> {
            if(!task.isSuccessful()){
                throw Objects.requireNonNull(task.getException());
            }
            return fileRef.getDownloadUrl();
        });

        urlTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {

                if(task.isSuccessful()){
                    Uri uriImg = task.getResult();
                    Log.e("hurrey: ", uriImg.toString());

//                    String s = " ";
                    String s = collectionsReference.document().getId();

                    FoodModel foodModel = new FoodModel(s, FoodName, FoodPrice, uriImg.toString());
                    collectionsReference.document(FoodName).set(foodModel);
                    getView().onAddVegSuccess("uploaded successfully!");
                }
            }

        }).addOnFailureListener(e -> getView().onAddVegFailure(e.getMessage()));

    }
}
