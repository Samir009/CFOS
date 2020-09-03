package com.samir.cfos.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.samir.cfos.R;
import com.samir.cfos.databinding.ActivityAddNonVegItemBinding;
import com.samir.cfos.helpers.MyApplication;
import com.samir.cfos.helpers.ShowToast;
import com.samir.cfos.presenters.AddNonVegPresenter;
import com.samir.cfos.utils.UtilitiesFunctions;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

import static com.samir.cfos.constants.AppConstants.REQUEST_CAMERA_PERMISSION;
import static com.samir.cfos.constants.AppConstants.REQUEST_STORAGE_PERMISSION;


public class AddNonVegActivity extends AppCompatActivity implements View.OnClickListener, AddNonVegPresenter.View {

    private ActivityAddNonVegItemBinding binding;
    private AddNonVegPresenter addNonVegPresenter;

    private Uri mImageUri;
    File photoFile = null;
    String mCurrentPhotoPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNonVegItemBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.addNonVegItemToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initializeListener();
    }

    private void initializeListener() {
        addNonVegPresenter = new AddNonVegPresenter(this);
        binding.selectNonVegImg.setOnClickListener(this);
        binding.addNonVegBtn.setOnClickListener(this);
    }

    private void showProgressBar() {
        binding.myProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        binding.myProgressBar.setVisibility(View.GONE);

    }

    private void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Image");
        builder.setItems(items, (dialog, which) -> {

            if (items[which].equals("Camera")) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "you have already camera permission", Toast.LENGTH_SHORT).show();

                    dispatchTakePictureIntent();
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

            photoFile = createImageFile();

            if (photoFile != null) {
                Log.e("dispatchTakePicIntent: ", photoFile.getAbsolutePath());

                mImageUri = FileProvider.getUriForFile(this,
                        "com.samir.cfos.fileprovider",
                        photoFile);

                Log.e("disPicIntent: ", Objects.requireNonNull(mImageUri.getPath()));

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                takePictureIntent.putExtra("return-data", true);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA_PERMISSION);
            }

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

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pick a image"), REQUEST_STORAGE_PERMISSION);
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Arrays.toString(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
        )){
            new AlertDialog.Builder(this)
                    .setTitle("Permission is needed")
                    .setMessage("Permission is needed to access the camera")
                    .setPositiveButton("Ok", (dialog, which) ->
                            ActivityCompat.requestPermissions(AddNonVegActivity.this, new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_CAMERA_PERMISSION))
                    .setNegativeButton("Cancel", (dialog, which) ->
                            dialog.dismiss())
                    .create()
                    .show();
        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission is needed")
                    .setMessage("Permission is needed to access storage")
                    .setPositiveButton("Ok", (dialog, which) ->
                            ActivityCompat.requestPermissions(AddNonVegActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION))
                    .setNegativeButton("Cancel", (dialog, which) ->
                            dialog.dismiss())
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
        }
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        binding.nonVegImg.setImageURI(mImageUri);

        if (resultCode == RESULT_OK && null != data) {
            if (requestCode == REQUEST_STORAGE_PERMISSION) {

                mImageUri = data.getData();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT
                );
                binding.nonVegImg.setLayoutParams(params);
                Glide.with(MyApplication.getAppContext()).load(mImageUri).into(binding.nonVegImg);

            } else if (requestCode == REQUEST_CAMERA_PERMISSION) {

//                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//                binding.selectNonVegImg.setImageBitmap(bitmap);
            }

        } else {
            Log.e("onActivityResult: ", "permission not given or other errors");
        }
    }

    private void checkEmptyFields() {
        if (binding.selectedNonVegName.getText().toString().equals("")
                || binding.selectedNonVegPrice.getText().toString().equals("") || mImageUri == null) {
            ShowToast.withLongMessage("Fields may be empty");
        } else {
            showProgressBar();
            uploadNonVegDetails();
        }
    }

    private void uploadNonVegDetails() {
        if (UtilitiesFunctions.isNetworkAvailable(this)) {
            Log.e("uploadDrinksDetails: ", "sent to add non veg presenter");
            addNonVegPresenter.addNonVegDetails(this, binding.selectedNonVegName.getText().toString(),
                    binding.selectedNonVegPrice.getText().toString()
                    , mImageUri);
        } else {
            Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.select_non_veg_img:
                selectImage();
                break;
            case R.id.add_non_veg_btn:
                checkEmptyFields();
                break;
        }
    }

    @Override
    public void addNonVegSuccess(String s) {
        hideProgressBar();
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        binding.nonVegImg.setLayoutParams(param);
        Glide.with(MyApplication.getAppContext()).load(R.drawable.ic_photo).into(binding.nonVegImg);
        binding.selectedNonVegName.setText(null);
        binding.selectedNonVegPrice.setText(null);
        ShowToast.withLongMessage(s);
    }

    @Override
    public void nonVegExist(String s) {
        hideProgressBar();
        Glide.with(MyApplication.getAppContext()).load(R.drawable.ic_photo).into(binding.nonVegImg);
        binding.selectedNonVegName.setText(null);
        binding.selectedNonVegPrice.setText(null);
        ShowToast.withLongMessage(s);
    }

    @Override
    public void addNonVegFailure(String s) {
        hideProgressBar();
        ShowToast.withLongMessage(s);
    }

}
