package com.samir.cfos.presenters;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.samir.cfos.constants.AppConstants;
import com.samir.cfos.helpers.ShowToast;
import com.samir.cfos.models.FoodModel;
import com.samir.cfos.utils.Utilities;

import java.lang.ref.WeakReference;
import java.util.Objects;


public class AddNonVegPresenter {

    //    name of folder where images will store
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference(AppConstants.NON_VEG_IMG);
    private StorageTask mUploadTask;
    private CollectionReference collectionsReference = Utilities.firebaseFirestore().collection(AppConstants.NON_VEG);

    private WeakReference<View> view;
    Context context;
    boolean exists = false;

    public AddNonVegPresenter (View view){
        this.view = new WeakReference<> (view);
    }

    private View getView() throws NullPointerException{
        if(view != null){
            return view.get();
        } else {
            throw new NullPointerException("View is unavailable");
        }
    }

    public interface View{
        void addNonVegSuccess(String s);
        void nonVegExist(String s);
        void addNonVegFailure(String s);
    }

    //    get file extension from selected file
    private String getFileExtension(Uri uri) {
        ContentResolver cr = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    public void addNonVegDetails(Context con, String name, String price, Uri imgUri){
        this.context = con;

        Log.e("addNonVegDetails: ", "non veg presenter called");

        if (mUploadTask != null && mUploadTask.isInProgress()) {
            ShowToast.withLongMessage("upload in progress");

        } else {

//            retrieve stored non-veg items
            collectionsReference.get().addOnSuccessListener(queryDocumentSnapshots -> {

                FoodModel foodModel1;
                for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots) {

                    foodModel1 = querySnapshot.toObject(FoodModel.class);
                    foodModel1.setDocId(querySnapshot.getId());

                    Log.e("Doc Id: ", foodModel1.getDocId());

                    if (foodModel1.getName().equals(name)) {
                        exists = true;
                        break;
                    }
                }

                if (exists) {
                    exists = false;
                    getView().nonVegExist(name + " exists");

                } else {
                    uploadToFirebase(name, price, imgUri);
                }
            });
            }
        }

    //    upload data to firebase
    private void uploadToFirebase(String FoodName, String FoodPrice, Uri imgUri) {

        //   creates name of image file
        StorageReference fileRef = mStorageRef.child(FoodName + "." + getFileExtension(imgUri));

        mUploadTask = fileRef.putFile(imgUri);

//        use Task to retrieve img stored in mUploadTask
        Task urlTask = mUploadTask.continueWithTask((Continuation) task -> {
            if(!task.isSuccessful()){
                throw Objects.requireNonNull(task.getException());
            }
            return fileRef.getDownloadUrl();
        });

        urlTask.addOnCompleteListener((OnCompleteListener<Uri>) task -> {

            if(task.isSuccessful()){

                Uri uriImg = task.getResult();
                Log.e("hurrey: ", uriImg.toString());

                    String s = collectionsReference.document().getId();

                FoodModel foodModel = new FoodModel(s, FoodName, FoodPrice, uriImg.toString());
                collectionsReference.document(FoodName).set(foodModel);
                getView().addNonVegSuccess("uploaded successfully!");
            }
        }).addOnFailureListener(e -> getView().addNonVegFailure(e.getMessage()));

    }

}
