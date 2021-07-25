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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText loginUsername , loginPassword;
    private Button loginButton;
    private DataBaseHelper myDb;
    private String EmailHolder, PasswordHolder, BaseUrl, tokenDevice;
    Boolean CheckEditText;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.loginusername);
        loginPassword = findViewById(R.id.loginpassword);
        loginButton = findViewById(R.id.loginbutton);
        progressDialog = new ProgressDialog(LoginActivity.this);
        BaseUrl = "http://workshopjti.com/wedding/";


        myDb = new DataBaseHelper(this);

        loginUser();

    }

    private void loginUser(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckEditTextIsEmptyOrNot();

                if (CheckEditText){
                    UserLogin();
                }else{
                    Toast.makeText(LoginActivity.this, "Mohon isi semua data", Toast.LENGTH_LONG).show();

                }

//                boolean var = myDb.checkUser(loginUsername.getText().toString() , loginPassword.getText().toString());
//                if (var){
//                    Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(LoginActivity.this , HomeActivity.class));
//                    finish();
//                }else{
//                    Toast.makeText(LoginActivity.this, "Login Failed !!", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    public void UserLogin(){

        progressDialog.setMessage("Mohon Tunggu");
        progressDialog.show();
        loginButton.setVisibility(View.GONE);

        String HttpUrl = BaseUrl + "api/mobile/show";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("status");

                            if (message.equals("200")){
                                String data = jsonObject.getString("data");
//                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject object = new JSONObject(data);
                                    String id = object.getString("id").trim();
//                                    sessionManager.createSession(id);


                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    loginButton.setVisibility(View.VISIBLE);
                                    finish();
//                                }
                            } else {
                                loginButton.setVisibility(View.VISIBLE);
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                            loginButton.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "Error Login Gagal :" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        //Hiding the progress dialog after all task complete.
                        progressDialog.dismiss();

                        //Showing error message if something goes wrong.
                        Toast.makeText(LoginActivity.this, "Error Response" + volleyError.toString(), Toast.LENGTH_LONG).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {

                //Creating map string params.
                Map<String, String> params = new HashMap<>();

                //Adding all values to params.
                //The first argument should be same your MySql database table columns.
                params.put("email", EmailHolder);
                params.put("password", PasswordHolder);
                params.put("Content-Type","application/x-www-form-urlencoded");

                return params;
            }

        };
        //Creating RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }

    public void CheckEditTextIsEmptyOrNot(){

        //Getting values from EditText.
        EmailHolder = loginUsername.getText().toString().trim();
        PasswordHolder = loginPassword.getText().toString().trim();

        //Checking wheter EditText value is empty or not.
        if (TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder))
        {

            //if any of EditText is empty then set variable value as False.
            CheckEditText = false;

        }else{
            //if any of EditText is empty then set variable value as false.
            CheckEditText = true;
        }
    }




}