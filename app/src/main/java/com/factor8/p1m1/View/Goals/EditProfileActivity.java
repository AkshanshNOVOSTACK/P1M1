package com.factor8.p1m1.View.Goals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.factor8.p1m1.Network.VolleyMultipartRequest;
import com.factor8.p1m1.Network.VolleySingleton;
import com.factor8.p1m1.R;
import com.factor8.p1m1.databinding.ActivityEditProfileBinding;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.factor8.p1m1.LoginRegistration.Login2Activity.PREF_KEY_USER_ID;

public class EditProfileActivity extends AppCompatActivity {

    ActivityEditProfileBinding binding;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    Bitmap imageBitMap = null;
    private static final String TAG = "EditProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        pref = getApplicationContext().getSharedPreferences("LoginPreference", MODE_PRIVATE);
        if (getIntent() != null) {
            binding.editTextFullName.setText(getIntent().getStringExtra("fullName"));
            binding.editTextEmail.setText(getIntent().getStringExtra("email"));
            if(!getIntent().getStringExtra("dp").contains("null")){
                Log.d(TAG, "onCreate: loading null");
                Glide.with(this).load(getIntent().getStringExtra("dp")).into(binding.imageViewUserDisplayPicture);
            }
        }
        binding.buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.progressBarUpdate.setVisibility(View.VISIBLE);
                getProfileDetails();
            }
        });
        binding.imageViewUserDisplayPictureEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageCropper();
            }
        });
    }

    private void ImageCropper() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    imageBitMap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                    binding.imageViewUserDisplayPicture.setImageURI(resultUri);
                } catch (IOException e) {
                    Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getProfileDetails() {

        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, "https://dass.io/finance_app/api/updateUserProfile", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {

                String responseString = new String(response.data);
                Log.d("yiauroianf", "onResponse: " + responseString);
                try {
                    JSONObject obj = new JSONObject(responseString);
                    String result = obj.getString("message");
                    if (result.equals("Profile updated."))
                        binding.progressBarUpdate.setVisibility(View.GONE);
                        finish();
                } catch (Exception e) {
                    Toast.makeText(EditProfileActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                    binding.progressBarUpdate.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditProfileActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                binding.progressBarUpdate.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", pref.getString(PREF_KEY_USER_ID, "1"));
                params.put("fullname", binding.editTextFullName.getText().toString());
                params.put("email", binding.editTextEmail.getText().toString());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                params.put("picture", new DataPart("ProfilePicture.jpeg", getFileDataFromDrawable(imageBitMap)));
                return params;
            }
        };
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(EditProfileActivity.this).addToRequestQueue(request);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        if (bitmap == null) {
            bitmap = ((BitmapDrawable) binding.imageViewUserDisplayPicture.getDrawable()).getBitmap();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        Log.d(TAG, "getFileDataFromDrawable: image: " + byteArrayOutputStream.toByteArray());
        return byteArrayOutputStream.toByteArray();

    }
}
