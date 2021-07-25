package com.example.wedding;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailSignUp , usernameSignUp , passwordSignUp;
    private Button signUpButton;
    private DataBaseHelper myDB;
    String tmpNama, tmpEmail, tmpPass, BaseUrl;
    Boolean CheckisEmpty;

    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailSignUp = findViewById(R.id.signupemail);
        usernameSignUp = findViewById(R.id.signupusername);
        passwordSignUp = findViewById(R.id.siguppassword);

        signUpButton = findViewById(R.id.signupbutton);
        progressDialog = new ProgressDialog(SignUpActivity.this);
        BaseUrl = "http://workshopjti.com/wedding/";

        myDB = new DataBaseHelper(this);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckEditTextIsEmptyOrNot();

                if (CheckisEmpty){
                    if (tmpPass.length() > 5){
                        RegisterAPI();
                    }else{
                        Toast.makeText(SignUpActivity.this, "Password harus lebih dari 5 digit", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(SignUpActivity.this, "Mohon isi semua data", Toast.LENGTH_LONG).show();
                }
            }
//                boolean var = myDB.registerterUser(usernameSignUp.getText().toString() , emailSignUp.getText().toString() , passwordSignUp.getText().toString());
//                if(var){
//                    Toast.makeText(SignUpActivity.this, "User Registered Successfully !!", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(SignUpActivity.this , LoginActivity.class));
//                    finish();
//                }
//                else
//                    Toast.makeText(SignUpActivity.this, "Registration Error !!", Toast.LENGTH_SHORT).show();
//                }
        });
    }
    private void RegisterAPI() {
        progressDialog.setMessage("Mohon Tunggu");
        progressDialog.show();
        signUpButton.setVisibility(View.GONE);

        String HttpUrl = BaseUrl + "api/mobile/register";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            String status = jsonObject.getString("status");

                            if (status.equals("200")){
                                Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                signUpButton.setVisibility(View.VISIBLE);
                                finish();
                            } else {
                                signUpButton.setVisibility(View.VISIBLE);
                                Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                            signUpButton.setVisibility(View.VISIBLE);
                            Toast.makeText(SignUpActivity.this, "Error" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "Error Response" + volleyError.toString(), Toast.LENGTH_LONG).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {

                //Creating map string params.
                Map<String, String> params = new HashMap<>();

                //Adding all values to params.
                //The first argument should be same your MySql database table columns.
                params.put("name", tmpNama);
                params.put("email", tmpEmail);
                params.put("password", tmpPass);
                params.put("Content-Type","application/x-www-form-urlencoded");

                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                (int) TimeUnit.SECONDS.toMillis(20), //After the set time elapses the request will timeout
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void CheckEditTextIsEmptyOrNot(){

        //Getting values from EditText.
        tmpNama = usernameSignUp.getText().toString().trim();
        tmpEmail = emailSignUp.getText().toString().trim();
        tmpPass = passwordSignUp.getText().toString().trim();

        CheckisEmpty = !TextUtils.isEmpty(tmpNama) && !TextUtils.isEmpty(tmpEmail) && !TextUtils.isEmpty(tmpPass);
    }

}

