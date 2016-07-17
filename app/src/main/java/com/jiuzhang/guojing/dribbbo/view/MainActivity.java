package com.jiuzhang.guojing.dribbbo.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jiuzhang.guojing.dribbbo.R;
import com.jiuzhang.guojing.dribbbo.dribbble.Dribbble;
import com.jiuzhang.guojing.dribbbo.dribbble.auth.Auth;
import com.jiuzhang.guojing.dribbbo.dribbble.auth.AuthActivity;
import com.jiuzhang.guojing.dribbbo.view.shot_list.ShotListFragment;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Dribbble.init(this);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.string.open_drawer,  /* "open drawer" description */
                R.string.close_drawer  /* "close drawer" description */
        );
        drawerLayout.setDrawerListener(drawerToggle);

        setupDrawerHeader();

        ShotListFragment shotListFragment = new ShotListFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, shotListFragment)
                .commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == Auth.REQ_CODE) {
            final String authCode = data.getStringExtra(AuthActivity.KEY_CODE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String token = Auth.fetchAccessToken(MainActivity.this, authCode);
                        Dribbble.login(MainActivity.this, token);

                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);

                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private void setupDrawerHeader() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.drawer);
        View headerView = navigationView.inflateHeaderView(Dribbble.isLoggedIn()
                ? R.layout.nav_header_logged_in
                : R.layout.nav_header);
        if (!Dribbble.isLoggedIn()) {
            Button loginButton = (Button) headerView.findViewById(R.id.nav_header_login_btn);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Auth.openAuthActivity(MainActivity.this);
                }
            });
        } else {
            TextView tvUsername = (TextView) headerView.findViewById(R.id.nav_header_logged_in_user);
            tvUsername.setText(Dribbble.getCurrentUser().name);

            Button logoutButton = (Button) headerView.findViewById(R.id.nav_header_logout_btn);
            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dribbble.logout(MainActivity.this);

                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);

                    finish();
                }
            });
        }
    }
}
