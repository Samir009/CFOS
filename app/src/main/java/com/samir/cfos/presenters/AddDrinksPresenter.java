package com.samir.cfos.presenters;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import com.anstrontechnologies.corehelper.AnstronCoreHelper;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.util.FileUtil;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iceteck.silicompressorr.FileUtils;
import com.iceteck.silicompressorr.SiliCompressor;
import com.samir.cfos.constants.AppConstants;
import com.samir.cfos.helpers.ShowToast;
import com.samir.cfos.models.FoodModel;
import com.samir.cfos.utils.Utilities;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class AddDrinksPresenter {


    AnstronCoreHelper coreHelper;

    //    name of folder where images will store
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference(AppConstants.DRINKS_IMG);
    private StorageTask mUploadTask;
    private CollectionReference collectionsReference = Utilities.firebaseFirestore().collection(AppConstants.DRINKS);

    private WeakReference<View> view;
    Context context;
    boolean exists = false;

    private AddDrinksPresenter.View getView() throws NullPointerException {
        if (view != null) {
            return view.get();
        } else {
            throw new NullPointerException("View is unavailable");
        }
    }

    public AddDrinksPresenter(AddDrinksPresenter.View view) {
        this.view = new WeakReference<>(view);
    }

    public interface View {
        void onAddDrinksSuccess(String addDrinksSuccess);

        void onAddDrinksFailure(String addDrinksFail);

        void drinkAlreadyExists(String exists);
    }

    //    get file extension from selected file
    private String getFileExtension(Uri uri) {
        ContentResolver cr = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    public void addDrinksDetails(Context con, String FoodName, String FoodPrice, Uri imgUri) {

        this.context = con;

        coreHelper = new AnstronCoreHelper(context);

        if (mUploadTask != null && mUploadTask.isInProgress()) {
            ShowToast.withLongMessage("upload in progress");

        } else {
            //     documentCollections.add(foodModel);
            collectionsReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    FoodModel foodModel;
                    for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots) {

                        foodModel = querySnapshot.toObject(FoodModel.class);

                        Log.e("getFoodData: ", new GsonBuilder().create().toJson(foodModel));

                        if (foodModel.getName().equals(FoodName)) {
                            exists = true;
                            break;
                        }
                    }

                    if (exists) {
                        exists = false;
                        getView().drinkAlreadyExists(FoodName + " exists");

                    } else {

                        File compImg = new File(imgUri.getPath());

                        String filePath = SiliCompressor.with(con).compress(imgUri.getPath(), compImg);

                        Uri actualImg = Uri.parse(filePath);

                        final File file = new File(SiliCompressor.with(con)
                                .compress(FileUtils.getPath(con, imgUri), new File(con.getCacheDir(), "temp")));

                        Uri uri = Uri.fromFile(file);

                        //           creates name of image file
                        StorageReference storageReference =  mStorageRef.child(FoodName + "." + getFileExtension(imgUri));
                                storageReference.putFile(uri);

                                /*use storage task to upload image */
                        mUploadTask = storageReference.putFile(imgUri);

                        /*retrieve img url from firebase*/
                        Task urlTask = mUploadTask.continueWithTask((Continuation) task -> {
                            if(!task.isSuccessful()){
                                throw Objects.requireNonNull(task.getException());
                            }
                            return storageReference.getDownloadUrl();
                        });

                        urlTask.addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if(task.isSuccessful()){

                                    /*image download string lies here*/
                                    Uri uriImg = (Uri) task.getResult();

                                    assert uriImg != null;
                                    Log.e("hurrey: ", uriImg.toString());

                                    String s = collectionsReference.document().getId();

                                    FoodModel foodModel = new FoodModel(s, FoodName, FoodPrice, uriImg.toString());
                                    collectionsReference.document(FoodName).set(foodModel);
                                    getView().onAddDrinksSuccess("uploaded successfully! :)");
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Failed to get Url: ", e.getMessage());
                            }
                        });

                    }
                }
            });

        }

    }

}

