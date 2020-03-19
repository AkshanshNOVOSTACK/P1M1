package com.factor8.p1m1.View;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.factor8.p1m1.View.Goals.EditProfileActivity;
import com.factor8.p1m1.databinding.FragmentProfileBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.factor8.p1m1.LoginRegistration.Login2Activity.PREF_KEY_IS_LOGGED_IN;
import static com.factor8.p1m1.LoginRegistration.Login2Activity.PREF_KEY_USER_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
     FragmentProfileBinding binding;
     String dpURL;
     public static final String PRE_URL = "http://dass.io/finance_app/storage/app/images/";

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public ProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile, container, false);
        View view = binding.getRoot();
        pref = this.getActivity().getSharedPreferences("LoginPreference", MODE_PRIVATE);
        binding.constraintLayoutSettingsBlockSafety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Will be added later", Toast.LENGTH_SHORT).show();
            }
        });
        binding.constraintLayoutSettingsBlockPrivacySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Will be added later", Toast.LENGTH_SHORT).show();
            }
        });
        binding.constraintLayoutSettingsBlockSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Will be added later", Toast.LENGTH_SHORT).show();
            }
        });

        binding.shimmerBackground.setVisibility(View.VISIBLE);
        binding.shimmerContainer.setVisibility(View.VISIBLE);
       binding.shimmerContainer.startShimmer();


        binding.textViewEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent i  = new Intent(getActivity(), EditProfileActivity.class);
                    i.putExtra("fullName", binding.textViewFullName.getText().toString());
                    i.putExtra("email", binding.textViewEmail.getText().toString());
                    i.putExtra("dp", PRE_URL+dpURL);
                    startActivity(i);
            }
        });
        binding.textViewSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor = pref.edit();
                editor.clear();
                editor.commit();
                Intent i = new Intent(getActivity(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                getActivity().finish();
            }
        });

        binding.constraintLayoutSettingsBlockService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  startActivity(new Intent(getActivity(), BackgroundProcessesActivity.class));
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getProfileDetails();
    }

    private void getProfileDetails() {

        VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, "https://dass.io/finance_app/api/getUserProfile", new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {

                binding.shimmerBackground.setVisibility(View.GONE);
                binding.shimmerContainer.setVisibility(View.GONE);
                binding.shimmerContainer.stopShimmer();
                String responseString = new String(response.data);
                try {
                    JSONObject obj = new JSONObject(responseString);
                    JSONArray dataArray = obj.getJSONArray("data");
                    JSONObject details = dataArray.getJSONObject(0);
                    binding.textViewFullName.setText(details.getString("fullname"));
                    binding.textViewEmail.setText(details.getString("email"));
                    binding.textViewPhone.setText(details.getString("mobile"));
                    dpURL = details.getString("picture");
                    Glide.with(getActivity()).load(PRE_URL+details.getString("picture")).into(binding.imageViewProfileImage);

                } catch (Exception e) {
                    Toast.makeText(getContext(), ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
                binding.shimmerBackground.setVisibility(View.GONE);
                binding.shimmerContainer.setVisibility(View.GONE);
                binding.shimmerContainer.stopShimmer();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params  = new HashMap<>();
                params.put("user_id",pref.getString(PREF_KEY_USER_ID,"1"));
                return params;
            }
        };
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                5,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

}
