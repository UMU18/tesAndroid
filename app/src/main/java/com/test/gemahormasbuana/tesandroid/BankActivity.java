package com.test.gemahormasbuana.tesandroid;

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
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BankActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView _bank;
    public static final String EXTRA_MESSAGE= "com.test.gemahormasbuana.tes.MESSAGE";
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent = getIntent();
        //String message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        if(message==""){
            message = intent.getStringExtra(KeretaActivity.EXTRA_MESSAGE);
        }
        msg=message;

        _bank = (ListView) findViewById(R.id.view);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        RequestQueue queue = Volley.newRequestQueue(BankActivity.this);
        String url = "https://api.tiketextra.com/test/bank";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            String[] arrayNamaBank = new String[array.length()];
                            String[] arrayKodeBank = new String[array.length()];



                            for (int i = 0; i < array.length(); i++) {
                                JSONObject bank = array.getJSONObject(i);
                                String kodeBank = bank.getString("bank_code");
                                String namaBank = bank.getString("bank_name");
                                arrayNamaBank[i] = namaBank;
                                arrayKodeBank[i] = kodeBank;


                            }
                            CustomerAdapter customAdapter = new CustomerAdapter(getApplicationContext(), arrayKodeBank, arrayNamaBank);
                            _bank.setAdapter(customAdapter);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);


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
            Intent intent = new Intent(BankActivity.this, MainActivity.class);
            intent.putExtra(EXTRA_MESSAGE,msg);
            startActivity(intent);
        } else if (id == R.id.nav_bank) {

        } else if (id == R.id.nav_kereta) {
            Intent intent = new Intent(BankActivity.this, KeretaActivity.class);
            intent.putExtra(EXTRA_MESSAGE,msg);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
