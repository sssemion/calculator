package com.sssemion.calculator;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class ParentNavigationActivity extends AppCompatActivity {
    NavigationLayout navigationLayout;
    RelativeLayout left_drawer;
    DrawerLayout drawerLayout;
    Button navButton;

    Intent i;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setupMenu();
    }

    public void setupMenu() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        left_drawer = (RelativeLayout) findViewById(R.id.left_drawer);
        navigationLayout = new NavigationLayout(getApplicationContext(), left_drawer);

        left_drawer.addView(navigationLayout);


        navButton = (Button) findViewById(R.id.navButton);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(left_drawer);
            }
        });


    }

    public void changeMode(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main:
                i = new Intent(ParentNavigationActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
            case R.id.nav_dev:
                i = new Intent(ParentNavigationActivity.this, DevActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
            case R.id.nav_engineer:
                i = new Intent(ParentNavigationActivity.this, EngineerActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
        }
    }
}