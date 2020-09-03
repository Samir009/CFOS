package com.samir.cfos.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.anstrontechnologies.corehelper.AnstronCoreHelper;
import com.bumptech.glide.Glide;
import com.camerakit.CameraKitView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iceteck.silicompressorr.SiliCompressor;
import com.samir.cfos.R;
import com.samir.cfos.databinding.ActivityAddDrinksBinding;
import com.samir.cfos.helpers.MyApplication;
import com.samir.cfos.helpers.ShowToast;

import com.samir.cfos.presenters.AddDrinksPresenter;

import com.samir.cfos.utils.FileUtils;
import com.samir.cfos.utils.ImageFilePath;
import com.samir.cfos.utils.UtilitiesFunctions;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import static android.media.ThumbnailUtils.extractThumbnail;
import static com.samir.cfos.constants.AppConstants.REQUEST_CAMERA_PERMISSION;
import static com.samir.cfos.constants.AppConstants.REQUEST_STORAGE_PERMISSION;

public class  AddDrinksActivity extends AppCompatActivity implements AddDrinksPresenter.View, View.OnClickListener {

    private ActivityAddDrinksBinding binding;
    private AddDrinksPresenter addDrinksPresenter;

    private Uri mImageUri;
    File photoFile = null;
    String mCurrentPhotoPath = "";
    Bitmap mBitmap;
    String imgExt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddDrinksBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.addDrinksToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initializeListener();

    }

    private void initializeListener() {
//        storageReference = FirebaseStorage.getInstance().getReference();
        addDrinksPresenter = new AddDrinksPresenter(this);
        binding.selectDrinksImg.setOnClickListener(this);
        binding.addDrinksBtn.setOnClickListener(this);
    }

    private void showProgressBar() {
        binding.myProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        binding.myProgressBar.setVisibility(View.GONE);

    }

    //    get file extension from selected file
    private String getFileExtension(Uri uri) {
        ContentResolver cr = this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    //    check whether fields are empty or not
    private void checkEmptyFields() {

        if (binding.selectedDrinksName.getText().toString().equals("")
                | binding.selectedDrinksPrice.getText().toString().equals("") | mBitmap == null) {
            ShowToast.withLongMessage("Fields or image may be empty");

        } else {
            showProgressBar();
            addDrinksPresenter.addDrinksDetails(this, binding.selectedDrinksName.getText().toString(),
                    binding.selectedDrinksPrice.getText().toString(), mBitmap, imgExt);

        }
    }

    //    select image dialog box
    private void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Image");
        builder.setItems(items, (dialog, which) -> {
            if (items[which].equals("Camera")) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED ){

//                    dispatchTakePictureIntent();

                } else {
                    requestCameraPermission();
                }

            } else if (items[which].equals("Gallery")) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else {
                    requestStoragePermission();
                }

            } else {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    //    open camera
    private void dispatchTakePictureIntent() {

        Log.e( "dispatchTakePicIntent: ", "Camera called");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            photoFile = null;
//            photoFile = createImageFile();

            Log.e("dispatchTakePicIntent: ", photoFile.getAbsolutePath());

            mImageUri = FileProvider.getUriForFile(this,
                    "com.samir.cfos.fileprovider",
                    photoFile);

//            compressImage(mImageUri, mCurrentPhotoPath);

            Log.e("disPicIntent: ", Objects.requireNonNull(mImageUri.getPath()));

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
//            takePictureIntent.putExtra("return-data", true);
            startActivityForResult(takePictureIntent, REQUEST_CAMERA_PERMISSION);

        }
    }

    public class BackgroundImageResize extends AsyncTask<Uri, Integer, byte[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ShowToast.withLongMessage("Compressing Image");
            Log.e("onPreExecute: ", "Compressing Image");
        }

        @Override
        protected byte[] doInBackground(Uri... uris) {
            Log.e("doInBackground: ", "started");

            return new byte[0];
        }


        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
        }
    }

    //    create image as a file
    @NotNull
    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.e("createImageFile: ", mCurrentPhotoPath);
        return image;
    }

    //    open gallery
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_STORAGE_PERMISSION);
    }

    //    ask camera permission
    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Arrays.toString(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
            )) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission is needed")
                    .setMessage("Permission is needed to access the camera")
                    .setPositiveButton("Ok", (dialog, which) ->
                            ActivityCompat.requestPermissions(AddDrinksActivity.this, new String[]{Manifest.permission.CAMERA},
                                    REQUEST_CAMERA_PERMISSION))
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
        }
    }

    //    ask storage permission
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission is needed")
                    .setMessage("Permission is needed to access storage")
                    .setPositiveButton("Ok", (dialog, which) -> ActivityCompat.requestPermissions(AddDrinksActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION))
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
        }
    }

    //    after permission is asked
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();

            } else {
                Toast.makeText(this, "Camera permission not granted", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Storage permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*when activity is performed after giving permission*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && null != data) {
            if (requestCode == REQUEST_STORAGE_PERMISSION) {

                /*set Selected image uri*/
                mImageUri = data.getData();

                /*convert uriData to bitmap*/
                try
                {
                    mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver() , mImageUri);
                    Glide.with(MyApplication.getAppContext()).load(mBitmap).into(binding.drinksImg);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                /*get the extension of selected image*/
                imgExt = getFileExtension(mImageUri);
                Log.e("FileExtension: ", imgExt);

                /*actual file location*/
                String imgPath = ImageFilePath.getPath(this, mImageUri);
                Log.e("actual path:", "\t"+ imgPath);


            } else if (requestCode == REQUEST_CAMERA_PERMISSION) {

                Log.e("cameraRequest: ", "after capturing img, it is redirected to onActivityResult");

            }

        } else {
            Log.e("onActivityResult: ",  "data may be empty or permission not given or other errors");
        }
    }

/*    public Bitmap resizeBitmap(String photoPath, int targetW, int targetH) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true; //Deprecated API 21

        return BitmapFactory.decodeFile(photoPath, bmOptions);
    }*/

/*    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_drinks_img:
                selectImage();
                break;

            case R.id.add_drinks_btn:
                if (UtilitiesFunctions.isNetworkAvailable(this)) {

                    checkEmptyFields();
                } else {
                    ShowToast.withLongMessage("Internet error!");
                }
                break;
        }
    }

    @Override
    public void onAddDrinksSuccess(String addDrinksSuccess) {
        hideProgressBar();
//        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT
//        );
//        binding.drinksImg.setLayoutParams(param);
//        binding.drinksImg.setImageResource(R.drawable.ic_photo);
        Glide.with(MyApplication.getAppContext()).load(R.drawable.ic_photo).into(binding.drinksImg);
        binding.selectedDrinksName.setText(null);
        binding.selectedDrinksPrice.setText(null);
        ShowToast.withLongMessage(addDrinksSuccess);
    }

    @Override
    public void onAddDrinksFailure(String addDrinksFail) {
        hideProgressBar();
        ShowToast.withLongMessage(addDrinksFail);
    }

    @Override
    public void drinkAlreadyExists(String exists) {
        hideProgressBar();
        Glide.with(MyApplication.getAppContext()).load(R.drawable.ic_photo).into(binding.drinksImg);
        binding.selectedDrinksName.setText(null);
        binding.selectedDrinksPrice.setText(null);
        ShowToast.withLongMessage(exists);
    }
}