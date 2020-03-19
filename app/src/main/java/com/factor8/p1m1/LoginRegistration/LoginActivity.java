package com.factor8.p1m1.LoginRegistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.factor8.p1m1.R;
import com.factor8.p1m1.UtilitiesClasses.Utils;
import com.factor8.p1m1.Model.OtpMOdel;
import com.factor8.p1m1.databinding.ActivityLoginBinding;
import com.factor8.p1m1.Network.BaseApi;
import com.factor8.p1m1.UtilitiesClasses.Config;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;


    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.mobNo.getText().toString().isEmpty() | binding.salary.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Either Mobile Number or password field(s) is empty", Toast.LENGTH_LONG).show();
                } else if (!(binding.mobNo.getText().toString().length() == 10)) {
                    Toast.makeText(LoginActivity.this, "Invalid Phone Number", Toast.LENGTH_LONG).show();
                } else {
                  //  binding.progressbar.setVisibility(View.VISIBLE);
                    register_business();
                }
            }
        });
    }

    private void register_business() {
          Utils.showProgress(LoginActivity.this, "Please wait...");
        OkHttpClient okHttpClient = Utils.okClient(LoginActivity.this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        BaseApi api = retrofit.create(BaseApi.class);
        Call<JsonElement> call = api.login(binding.mobNo.getText().toString().trim());
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, retrofit2.Response<JsonElement> response) {
                Utils.dismissProgress();
                if (response.isSuccessful()) {
                    JsonElement questions = response.body();
                    if (questions != null) {
                        OtpMOdel businessCreateModel = new Gson().fromJson(questions.getAsJsonObject(), OtpMOdel.class);
                        if (businessCreateModel.isSuccess()) {
                            Log.d("OTP",businessCreateModel.getData().getOtp()+"");
                            Intent i = new Intent(LoginActivity.this, Login2Activity.class);
                            i.putExtra("phone", binding.mobNo.getText().toString());
                            i.putExtra("otp", "" + businessCreateModel.getData().getOtp());
                            i.putExtra("salary", binding.salary.getText().toString());
                            startActivity(i);
                        } else {
                             Utils.toast(LoginActivity.this, businessCreateModel.getMessage());
                            Log.e("OTP","error1");
                        }
                    } else {
                        Utils.toast(LoginActivity.this, "No Internet Connection");
                        Log.e("OTP","error2");
                    }
                } else {
                    //   Utils.toast(getActivity(), getResources().getString(R.string.no_internet_connection));
                    Log.e("OTP","error3");
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Api Call", t.getMessage().toString() + "");
                Log.d("OTP","error4");
                //  Utils.toast(getActivity(), getResources().getString(R.string.no_internet_connection));
                    Utils.dismissProgress();
            }
        });
    }

}
