package com.test.gemahormasbuana.tesandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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

public class LoginActivity extends AppCompatActivity {
    TextView _response,_email,_pass;
    EditText email,pass;
    Button _login;
    public static final String EXTRA_MESSAGE= "com.test.gemahormasbuana.tes.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login);

        _response = (TextView) findViewById(R.id.resulttoken);
        email=(EditText) findViewById(R.id.inputusername);
        pass=(EditText) findViewById(R.id.inputpassword);
        _email=(TextView) findViewById(R.id.username);
        _pass=(TextView) findViewById(R.id.password);
        _login=(Button) findViewById(R.id.buttonlogin);

        _login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                String url = "https://api.tiketextra.com/auth/getToken";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    JSONObject data = jsonObj.getJSONObject("data");
                                    String token = data.getString("token");
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra(EXTRA_MESSAGE,token);
                                    startActivity(intent);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        _response.setText("That didn't work!");
                    }
                }) {
                    //adding parameters to the request
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("email",""+email.getText().toString());
                        params.put("password",""+pass.getText().toString());
                        //params.put("email","test@test.com");
                        //params.put("password","test@test.com");
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("Content-Type","application/x-www-form-urlencoded");
                        return params;
                    }
                };
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });





    }
}

