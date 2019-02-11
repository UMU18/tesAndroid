package com.test.gemahormasbuana.tesandroid;



import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class KeretaActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
    public static final String EXTRA_MESSAGE= "com.test.gemahormasbuana.tes.MESSAGE";
    String msg;
    EditText _tanggalBerangkat,_tanggalKembali;
    Button _cariKereta;

    ListView _kereta;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kereta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        if(message==""){
            message = intent.getStringExtra(BankActivity.EXTRA_MESSAGE);
        }
        msg=message;


        _tanggalBerangkat = (EditText) findViewById(R.id.tanggalBerangkat);
        _tanggalKembali = (EditText) findViewById(R.id.tanggalKembali);
        _cariKereta = (Button) findViewById(R.id.buttonCariKereta);
        _kereta = (ListView) findViewById(R.id.viewKereta);



        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        final Calendar tanggalBerangkat = Calendar.getInstance();
        final Calendar tanggalPulang = Calendar.getInstance();

     final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                tanggalBerangkat.set(Calendar.YEAR, year);
                tanggalBerangkat.set(Calendar.MONTH, monthOfYear);
                tanggalBerangkat.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                _tanggalBerangkat.setText(dateFormat.format(tanggalBerangkat.getTime()));
            }
        };

        final DatePickerDialog.OnDateSetListener dates = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                tanggalPulang.set(Calendar.YEAR, year);
                tanggalPulang.set(Calendar.MONTH, monthOfYear);
                tanggalPulang.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel2();
            }

            private void updateLabel() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                _tanggalBerangkat.setText(dateFormat.format(tanggalBerangkat.getTime()));
            }

            private void updateLabel2() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                _tanggalKembali.setText(dateFormat.format(tanggalPulang.getTime()));
            }
        };
        _tanggalBerangkat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(KeretaActivity.this, date, tanggalBerangkat.get(Calendar.YEAR),tanggalBerangkat.get(Calendar.MONTH),tanggalBerangkat.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        _tanggalKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(KeretaActivity.this, dates, tanggalPulang.get(Calendar.YEAR), tanggalPulang.get(Calendar.MONTH),tanggalPulang.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        _cariKereta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
                RequestQueue queue = Volley.newRequestQueue(KeretaActivity.this);
                String url = "https://api.tiketextra.com/test/getAvailability?token="+msg;



                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    JSONObject data = obj.getJSONObject("data");


                                    JSONArray array = data.getJSONArray("go");
                                    String[] arrayNamaKereta = new String[array.length()];
                                    String[] arrayJamBerangkat = new String[array.length()];
                                    String[] arrayJamSampai = new String[array.length()];
                                    String[] arraySeatAvail = new String[array.length()];
                                    String[] arrayKelas = new String[array.length()];

                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject kereta = array.getJSONObject(i);
                                        String NamaKereta = kereta.getString("trainName");
                                        String JamBerangkat = "Depature Time: "+kereta.getString("departureTime");
                                        String JamSampai = "Arival Time: "+kereta.getString("arrivalTime");
                                        String seatAvail = "Seat Avail: "+kereta.getString("seatAvail");
                                        String kelas = "kelas: "+kereta.getString("classCategory");

                                        arrayNamaKereta[i] = NamaKereta;
                                        arrayJamBerangkat[i] = JamBerangkat;
                                        arrayJamSampai[i] = JamSampai;
                                        arraySeatAvail[i] = seatAvail;
                                        arrayKelas[i] = kelas;
                                    }
                                    CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), arrayNamaKereta, arrayJamBerangkat, arrayJamSampai, arraySeatAvail, arrayKelas);
                                    _kereta.setAdapter(customAdapter);
                                } catch (JSONException e1) {
                                    e1.printStackTrace();



                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }) {
                    //adding parameters to the request
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("date", "" + _tanggalBerangkat.getText().toString());
                        params.put("date_back", "" + _tanggalKembali.getText().toString());
                        params.put("origin", "GMR");
                        params.put("destination", "BD");
                        params.put("adult", "1");
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/x-www-form-urlencoded");
                        return params;
                    }
                };
                queue.add(stringRequest);
            }
        });
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
            Intent intent = new Intent(KeretaActivity.this, MainActivity.class);
            intent.putExtra(EXTRA_MESSAGE,msg);
            startActivity(intent);
        } else if (id == R.id.nav_bank) {
            Intent intent = new Intent(KeretaActivity.this, BankActivity.class);
            intent.putExtra(EXTRA_MESSAGE,msg);
            startActivity(intent);
        } else if (id == R.id.nav_kereta) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

