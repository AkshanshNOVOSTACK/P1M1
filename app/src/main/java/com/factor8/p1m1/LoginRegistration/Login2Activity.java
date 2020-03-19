package com.factor8.p1m1.LoginRegistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.factor8.p1m1.Model.OtpVModel;
import com.factor8.p1m1.R;
import com.factor8.p1m1.UtilitiesClasses.Utils;
import com.factor8.p1m1.View.MainActivity;
import com.factor8.p1m1.databinding.ActivityLogin2Binding;
import com.factor8.p1m1.Network.BaseApi;
import com.factor8.p1m1.UtilitiesClasses.Config;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login2Activity extends AppCompatActivity {
    private ActivityLogin2Binding binding;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public static final String PREF_KEY_PHONE = "phone";
    public static final String PREF_KEY_SALARY = "salary";
    public static final String PREF_KEY_USER_ID = "uder_id";
    public static final String PREF_KEY_IS_LOGGED_IN = "isLoggedIn";
    public static final String PREF_KEY_IS_SERVICE_RUNNING = "isServiceRunning";

    private String phone, otp, salary;
    private static final String TAG = "Login2Activity";

    private SmsVerifyCatcher smsVerifyCatcher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        pref = getApplicationContext().getSharedPreferences("LoginPreference", MODE_PRIVATE);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login2);
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                 String code = parseCode(message);
                Log.d(TAG, "onSmsCatch: "+ code.charAt(0));
                Log.d(TAG, "onSmsCatch: "+ code.charAt(1));
                Log.d(TAG, "onSmsCatch: "+ code.charAt(2));
                Log.d(TAG, "onSmsCatch: "+ code.charAt(3));
                 binding.edit1.setText(code.charAt(0)+"");
                binding.edit2.setText(code.charAt(1)+"");
                binding.edit3.setText(code.charAt(2)+"");
                binding.edit4.setText(code.charAt(3)+"");
            }
        });
        phone = getIntent().getStringExtra("phone");
        otp = getIntent().getStringExtra("otp");
        salary = getIntent().getStringExtra("salary");

        binding.edit1.addTextChangedListener(new GenericTextWatcher(binding.edit1));
        binding.edit2.addTextChangedListener(new GenericTextWatcher(binding.edit2));
        binding.edit3.addTextChangedListener(new GenericTextWatcher(binding.edit3));
        binding.edit4.addTextChangedListener(new GenericTextWatcher(binding.edit4));

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otpEditext = "";
                otpEditext = binding.edit1.getText().toString();
                otpEditext = otpEditext + binding.edit2.getText().toString();
                otpEditext = otpEditext + binding.edit3.getText().toString();
                otpEditext = otpEditext + binding.edit4.getText().toString();
                if (!(otpEditext.length() == 4)) {
                    Toast.makeText(Login2Activity.this, "Please Enter the OPT", Toast.LENGTH_SHORT).show();
                } else if (otpEditext.equals(otp)) {
                    networkCall();

                }else{
                    Toast.makeText(Login2Activity.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    public String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        Log.d(TAG, "parseCode: otp: "+ code);
        return code;
    }

    private void createLoginPreference(String phone, String Salary, String userID) {
        editor = pref.edit();
        editor.putString(PREF_KEY_PHONE, phone);
        editor.putString(PREF_KEY_SALARY, Salary);
        editor.putString(PREF_KEY_USER_ID, userID);
        editor.putBoolean(PREF_KEY_IS_LOGGED_IN, true);
        editor.putBoolean(PREF_KEY_IS_SERVICE_RUNNING, false);
        editor.apply();

        Intent p = new Intent(Login2Activity.this, MainActivity.class);
        p.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        p.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(p);
        finish();
    }

    public class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch (view.getId()) {
                case R.id.edit1:
                    if (text.length() == 1)
                        binding.edit2.requestFocus();
                    break;
                case R.id.edit2:
                    if (text.length() == 1)
                        binding.edit3.requestFocus();
                    else if (text.length() == 0)
                        binding.edit1.requestFocus();
                    break;
                case R.id.edit3:
                    if (text.length() == 1)
                        binding.edit4.requestFocus();
                    else if (text.length() == 0)
                        binding.edit2.requestFocus();
                    break;
                case R.id.edit4:
                    if (text.length() == 0)
                        binding.edit3.requestFocus();
                    else if (text.length() == 1) {
                        binding.next.setEnabled(true);
                        Utils.hideSoftKeyboard(Login2Activity.this);
                    }
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

    }


    public void networkCall(){
         OtpVModel model;
        Utils.showProgress(Login2Activity.this, "Please wait...");
        OkHttpClient okHttpClient = Utils.okClient(Login2Activity.this);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();
        BaseApi api = retrofit.create(BaseApi.class);
        Call<JsonElement> call = api.login2(phone, salary);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                Utils.dismissProgress();
                if(response.isSuccessful()){
                    Utils.dismissProgress();
                      JsonElement jsonBody  =  response.body();
                          if(jsonBody!=null){
                              OtpVModel modelClass = new Gson().fromJson(jsonBody.getAsJsonObject(), OtpVModel.class);
                              editor = pref.edit();
                              editor.putString(PREF_KEY_USER_ID, modelClass.getUserData().getUser_id());
                              editor.putString(PREF_KEY_SALARY, modelClass.getUserData().getSalary());
                              editor.apply();
                              createLoginPreference(phone, salary, pref.getString(PREF_KEY_USER_ID, "1"));
                          }
                }else{
                    Utils.dismissProgress();
                    Log.d(TAG, "onResponse: response is not successful" );
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Utils.dismissProgress();
                Log.d(TAG, "onFailure: Call Failed");
            }
        });

    }
}
