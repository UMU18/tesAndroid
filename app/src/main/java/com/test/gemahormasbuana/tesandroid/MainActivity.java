package com.test.gemahormasbuana.tesandroid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView _name, _mobile, _email;
    ImageView _image;
    Button _changePic;
    public static final String EXTRA_MESSAGE= "com.test.gemahormasbuana.tes.MESSAGE";
    String msg;
    Bitmap bitmap;
    private static int RESULT_LOAD_IMAGE=1;
    String url2 = "https://api.tiketextra.com/test/updateUserImage?token="+msg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
        if (message==""){
            message = intent.getStringExtra(BankActivity.EXTRA_MESSAGE);
        }
        if(message==""){
            message = intent.getStringExtra(KeretaActivity.EXTRA_MESSAGE);
        }
        msg=message;


        _name= (TextView) findViewById(R.id.showName);
        _mobile=(TextView) findViewById(R.id.showMobile);
        _email=(TextView) findViewById(R.id.showEmail);
        _image=(ImageView) findViewById(R.id.showImage);
        _changePic=(Button) findViewById(R.id.changePic);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        _changePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");

                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }



        });


        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.tiketextra.com/auth/getUser?token="+msg;
        String url1 = "https://api.tiketextra.com/test/getUserImage?token="+msg;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            JSONObject data = jsonObj.getJSONObject("data");
                            String name = data.getString("name");
                            String mobile = data.getString("mobile");
                            String email = data.getString("email");
                            String image = data.getString("user_image");

                            _name.setText(name);
                            _mobile.setText(mobile);
                            _email.setText(email);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                _name.setText("That didn't work!");
            }
        });
        queue.add(stringRequest);

        InputStreamVolleyRequest insRequest = new InputStreamVolleyRequest(Request.Method.GET, url1,
                new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {
                Bitmap decodeByte = BitmapFactory.decodeByteArray(response,0, response.length );
                if(decodeByte==null){
                    _image.setImageResource(R.drawable.ic_menu_camera);
                }
                else{
                    _image.setImageBitmap(decodeByte);
                }

            }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                _image.setImageResource(R.drawable.ic_menu_camera);
            }

        });

        queue.add(insRequest);

    }


    public class InputStreamVolleyRequest extends Request<byte[]>{

        private final Response.Listener<byte[]> listener;
        private Map<String, String> params;
        public Map<String, String> responseHeaders;

        public InputStreamVolleyRequest(int method, String url, Response.Listener<byte[]> listener, Response.ErrorListener errorListener){
            super(method, url, errorListener);
            setShouldCache(false);
            this.listener = listener;
            this.params = params;
        }
        @Override
        protected Map<String, String> getParams() throws AuthFailureError{
            return params;
        }

        @Override
        protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
            responseHeaders = response.headers;
            return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
        }

        @Override
        protected void deliverResponse(byte[] response) {
            listener.onResponse(response);
        }
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data){
        super.onActivityResult(reqCode,resultCode,data);
        if(resultCode==RESULT_OK){
            try {
                final Uri imageUri=data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                bitmap = BitmapFactory.decodeStream(imageStream);

                _image.setImageBitmap(bitmap);

                uploaduserimage();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void uploaduserimage(){

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url2 = "https://api.tiketextra.com/test/updateUserImage?token="+msg;

        InputStreamVolleyRequest is = new InputStreamVolleyRequest(Request.Method.POST, url2, new Response.Listener<byte[]>() {

            @Override
            public void onResponse(byte[] response) {
                Log.i("Myresponse", "" + response);
                Toast.makeText(MainActivity.this, "" + response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Mysmart",""+error);
                Toast.makeText(MainActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                String images = getStringImage(bitmap).toString();
                Log.i("Mynewsam",""+images);
                param.put("image",images);
                return param;
            }
        };

        requestQueue.add(is);


    }

    public byte[] getStringImage(Bitmap bitmap){
        Log.i("MyHitesh",""+bitmap);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] bytesArray = stream.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bytesArray);

        return bytesArray;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profil) {
            // Handle the camera action
        } else if (id == R.id.nav_bank) {
            Intent intent = new Intent(MainActivity.this, BankActivity.class);
            intent.putExtra(EXTRA_MESSAGE,msg);
            startActivity(intent);
        } else if (id == R.id.nav_kereta) {
            Intent intent = new Intent(MainActivity.this, KeretaActivity.class);
            intent.putExtra(EXTRA_MESSAGE,msg);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
