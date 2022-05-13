package com.example.critique;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;

public class ViewStores extends AppCompatActivity {

    private DBHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stores);

        database = DBHelper.getInstance(ViewStores.this);
        showStores();
    }

    public void showStores(){
        Cursor cd = database.getAllStores();
        TableLayout table = (TableLayout)findViewById(R.id.tableLayout);
        TextView noStores = findViewById(R.id.noStores);

        //DB does not hvae any sotres
        if(cd.getCount()==0){
            noStores.setText("There are not any stores");
            noStores.setTextColor(Color.rgb(241,98,80));
        }

        else{
            //columons header
            {
                noStores.setVisibility(View.GONE);
                TableRow row = new TableRow(ViewStores.this);
                TextView c1 = new TextView(ViewStores.this);
                TextView c2 = new TextView(ViewStores.this);

                c1.setText("Store");
                c1.setBackgroundColor(Color.rgb(90, 0, 238));
                c1.setTextColor(Color.WHITE);
                c1.setTextSize(17);
                c1.setWidth(400);
                c2.setText("Review Store");
                c2.setBackgroundColor(Color.rgb(90, 0, 238));
                c2.setTextColor(Color.WHITE);
                c2.setTextSize(17);
                c2.setWidth(400);
                c2.setPadding(50,0,0,0);

                row.addView(c1);
                row.addView(c2);
                table.addView(row);
            }

            //content
            while(cd.moveToNext()){

                StringBuffer buffer1 = new StringBuffer();
                buffer1.append(cd.getString(2));
                TableRow row = new TableRow(ViewStores.this);
                TextView c1 = new TextView(ViewStores.this);
                c1.setText(cd.getString(2));//
                c1.setBackgroundColor(Color.WHITE);
                c1.setTextColor(Color.BLACK);
                c1.setTextSize(17);
                c1.setWidth(150);

                Button reviewButton = new Button((ViewStores.this));
                reviewButton.setText("review");
                reviewButton.setTextSize(14);
                reviewButton.setOnClickListener(reviewButtonOnClick);
                reviewButton.setId(cd.getInt(0));
                reviewButton.setWidth(150);



                row.addView(c1);
                row.addView(reviewButton);
                table.addView(row);

            }

        }
        cd.close();
    }


    View.OnClickListener reviewButtonOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent i = new Intent(ViewStores.this,reviewStore.class);
            i.putExtra("StoreID",view.getId());
            startActivity(i);
        }
    };



    @Override
    /*
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        MenuItem item = menu.findItem(R.id.myswitch);
        MenuItem item2 = menu.findItem(R.id.logout);
        item.setActionView(R.layout.switch_layout);
        item2.setActionView(R.layout.logout_layout);

        Switch darkmode = item.getActionView().findViewById(R.id.darkmode);
        darkmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (darkmode.isChecked())
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                else
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
        TextView logout = item2.getActionView().findViewById(R.id.logoutText);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewStores.this.startActivity(new Intent(ViewStores.this, SignIn.class));
            }
        });
        return true;
    }
     */

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        MenuItem item = menu.findItem(R.id.myswitch);
        MenuItem item2 = menu.findItem(R.id.logout);
        item.setActionView(R.layout.switch_layout);
        item2.setActionView(R.layout.logout_layout);

        Switch darkmode = item.getActionView().findViewById(R.id.darkmode);
        darkmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (darkmode.isChecked())
                    //getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                else
                    //getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
        TextView logout = item2.getActionView().findViewById(R.id.logoutText);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewStores.this.startActivity(new Intent(ViewStores.this, SignIn.class));
            }
        });
        return true;
    }
}
