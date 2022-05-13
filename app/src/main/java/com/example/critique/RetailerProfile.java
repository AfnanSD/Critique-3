package com.example.critique;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

public class RetailerProfile extends AppCompatActivity {
    int InvitationId = 0;
    DBHelper database;
    int id;
    //22
    //SharedPreferences prefs;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.retailer_profile);
        database = DBHelper.getInstance(this);

        //22
        /*
        if(savedInstanceState!=null){
            MenuItem item = findViewById(R.id.myswitch);
            Switch darkmode = item.getActionView().findViewById(R.id.darkmode);
            String state = savedInstanceState.getString("state");
            if(state.equals("checked")){

//it was checked

                darkmode.setChecked(true);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

            }else{

//it was unchecked

                darkmode.setChecked(false);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

            }
        }

         */

        //22
        /*
        prefs = this.getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        if(prefs!=null){
            MenuItem item = findViewById(R.id.myswitch);
            Switch darkmode = item.getActionView().findViewById(R.id.darkmode);
            String state = savedInstanceState.getString("state");
            if(state.equals("checked")){

    //it was checked

                darkmode.setChecked(true);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

            }else{

    //it was unchecked

                darkmode.setChecked(false);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

            }
        }

         */

        //
        Intent intent = getIntent();
        id = intent.getIntExtra("ID",-1);

        //adding username and store to profile
        String name = intent.getStringExtra("name");
        getUserName(name);
        showStores(id);

        TableLayout table = (TableLayout)findViewById(R.id.tableLayout);

        Button add = findViewById(R.id.button2);
        final String[] nameOfStore = {""};//to be accessed?

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(RetailerProfile.this);
                builder.setTitle("Title");
                EditText retailersInput = new EditText(RetailerProfile.this);
                retailersInput.setInputType(InputType.TYPE_CLASS_TEXT);//type text
                builder.setView(retailersInput);//show input field in dialog
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        nameOfStore[0] = retailersInput.getText().toString();

                        if(database.insertDataIntoStoresTable(id,nameOfStore[0])){
                            Toast.makeText(RetailerProfile.this,"The store has been added successfully",Toast.LENGTH_SHORT).show();
                            //add store to table
                            //when adding the first store
                            Cursor c = database.getStores(id);
                            if(c.getCount()==1){
                                TextView noStores = findViewById(R.id.noStores);
                                noStores.setTextColor(Color.rgb(241,98,80));
                                noStores.setVisibility(View.GONE);

                                TableRow row = new TableRow(RetailerProfile.this);
                                TextView c1 = new TextView(RetailerProfile.this);
                                TextView c2 = new TextView(RetailerProfile.this);
                                TextView c3 = new TextView(RetailerProfile.this);
                                c1.setText("id");
                                c1.setBackgroundColor(Color.rgb(90, 0, 238));
                                c1.setTextColor(Color.WHITE);
                                c1.setTextSize(17);
                                c1.setWidth(150);
                                c2.setText("name");
                                c2.setBackgroundColor(Color.rgb(90, 0, 238));
                                c2.setTextColor(Color.WHITE);
                                c2.setTextSize(17);
                                c2.setWidth(400);
                                c3.setText("reviews");
                                c3.setBackgroundColor(Color.rgb(90, 0, 238));
                                c3.setTextColor(Color.WHITE);
                                c3.setTextSize(17);
                                c3.setWidth(250);
                                c3.setPadding(50,0,0,0);

                                row.addView(c1);
                                row.addView(c2);
                                row.addView(c3);
                                table.addView(row);
                            }
                            c.moveToLast();//last added store
                            StringBuffer buffer1 = new StringBuffer();
                            StringBuffer buffer2 = new StringBuffer();
                            buffer1.append(c.getInt(0));
                            buffer2.append(c.getString(2));
                            TableRow row = new TableRow(RetailerProfile.this);
                            TextView c1 = new TextView(RetailerProfile.this);
                            TextView c2 = new TextView(RetailerProfile.this);
                            c1.setText(buffer1);
                            c1.setBackgroundColor(Color.WHITE);
                            c1.setTextColor(Color.BLACK);
                            c1.setTextSize(17);
                            c1.setWidth(150);
                            c2.setText(buffer2);
                            c2.setBackgroundColor(Color.WHITE);
                            c2.setTextColor(Color.BLACK);
                            c2.setTextSize(17);
                            c2.setWidth(350);

                            Button reviewButton = new Button((RetailerProfile.this));
                            reviewButton.setText("Check");
                            reviewButton.setTextSize(14);
                            reviewButton.setOnClickListener(reviewButtonOnClick);
                            reviewButton.setId(Integer.parseInt(buffer1.toString()));
                            reviewButton.setWidth(100);

                            row.addView(c1);
                            row.addView(c2);
                            row.addView(reviewButton);
                            table.addView(row);
                        }
                        else
                            Toast.makeText(RetailerProfile.this,"The store was not added successfully!",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();

            }
        });

        Button setInvitation = findViewById(R.id.setInvitation);
        setInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RetailerProfile.this, SetInvitation.class);
                intent.putExtra("ID", id);
                activityResultLauncher.launch(intent);
            }
        });
    }

    //start activity for result alt
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult activityResult) {
            Toast.makeText(RetailerProfile.this, "Invitation is set successfully", Toast.LENGTH_SHORT).show();
        }
    });


    public void getUserName(String name){//int retailerID
        TextView userName = findViewById(R.id.userName);
        userName.setText(name);
    }

    public void showStores(int retailerID){
        Cursor cd = database.getStores(retailerID);
        TableLayout table = (TableLayout)findViewById(R.id.tableLayout);
        TextView noStores = findViewById(R.id.noStores);

        //retailer's does not hvae any sotres
        if(cd.getCount()==0){
            noStores.setText("You have not added any stores");
            noStores.setTextColor(Color.rgb(241,98,80));
        }

        else{
            //columons header
            {
                noStores.setVisibility(View.GONE);
                TableRow row = new TableRow(RetailerProfile.this);
                TextView c1 = new TextView(RetailerProfile.this);
                TextView c2 = new TextView(RetailerProfile.this);
                TextView c3 = new TextView(RetailerProfile.this);
                c1.setText("id");
                c1.setBackgroundColor(Color.rgb(90, 0, 238));
                c1.setTextColor(Color.WHITE);
                c1.setTextSize(17);
                c1.setWidth(150);
                c2.setText("name");
                c2.setBackgroundColor(Color.rgb(90, 0, 238));
                c2.setTextColor(Color.WHITE);
                c2.setTextSize(17);
                c2.setWidth(400);
                c3.setText("reviews");
                c3.setBackgroundColor(Color.rgb(90, 0, 238));
                c3.setTextColor(Color.WHITE);
                c3.setTextSize(17);
                c3.setWidth(250);
                c3.setPadding(50,0,0,0);

                row.addView(c1);
                row.addView(c2);
                row.addView(c3);
                table.addView(row);
            }
            //content
            while(cd.moveToNext()){
                StringBuffer buffer1 = new StringBuffer();
                StringBuffer buffer2 = new StringBuffer();
                buffer1.append(cd.getInt(0));
                buffer2.append(cd.getString(2));
                TableRow row = new TableRow(RetailerProfile.this);
                TextView c1 = new TextView(RetailerProfile.this);
                TextView c2 = new TextView(RetailerProfile.this);
                c1.setText(buffer1);
                c1.setBackgroundColor(Color.WHITE);
                c1.setTextColor(Color.BLACK);
                c1.setTextSize(17);
                c1.setWidth(150);
                c2.setText(buffer2);
                c2.setBackgroundColor(Color.WHITE);
                c2.setTextColor(Color.BLACK);
                c2.setTextSize(17);
                c2.setWidth(400);
                Button reviewButton = new Button((RetailerProfile.this));
                reviewButton.setText("Check");
                reviewButton.setTextSize(14);
                reviewButton.setOnClickListener(reviewButtonOnClick);
                reviewButton.setId(Integer.parseInt(buffer1.toString()));
                reviewButton.setWidth(150);

                row.addView(c1);
                row.addView(c2);
                row.addView(reviewButton);
                table.addView(row);
            }
        }
        cd.close();
    }

    View.OnClickListener reviewButtonOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(RetailerProfile.this);
            //builder.setCancelable(false);
            builder.setTitle("Reviews");
            /*does not appear all the time?!
            builder.setNeutralButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
             */
            //builder.setScrollable(true);
            builder.setView(LayoutInflater.from(RetailerProfile.this).inflate(R.layout.scrollable_dialog,null));//make it scrollable if needed
            StringBuffer buffer = new StringBuffer();
            Cursor c = database.getReviews(view.getId());
            if(c.getCount()==0)
                buffer.append("No reviews");
            else{
                while(c.moveToNext()) {
                    buffer.append("rating: "+c.getFloat(3)+" | ");
                    buffer.append("review: "+c.getString(2)+"\n");
                    buffer.append("--------------------------\n");
                }
            }
            builder.setMessage(buffer);
            builder.show();
        }
    };


    @Override
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
                if (darkmode.isChecked()) {
                    //SharedPreferences.Editor editor = getSharedPreferences("switch", MODE_PRIVATE).edit();
                    //SharedPreferences.Editor editor = prefs.edit();
                    //editor.putString("state", "checked");
                    //editor.apply();
                    //getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else
                    //getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
        TextView logout = item2.getActionView().findViewById(R.id.logoutText);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetailerProfile.this.startActivity(new Intent(RetailerProfile.this, SignIn.class));
            }
        });
        return true;
    }


    /*222
    @Override
    protected void onStart() {
        super.onStart();

        MenuItem item = findViewById(R.id.myswitch);
        Switch darkmode = item.getActionView().findViewById(R.id.darkmode);
        //SharedPreferences pref = getSharedPreferences("switch", MODE_PRIVATE);
        //SharedPreferences pref = prefs;
        //String state = pref.getString("state", "default");
        String state = prefs.getString("state", "default");

        if(state.equals("checked")){

//it was checked

            darkmode.setChecked(true);

        }else{

//it was unchecked

            darkmode.setChecked(false);

        }

    }
    */

    //22

    /*
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        //super.onSaveInstanceState(outState);

        MenuItem item = findViewById(R.id.myswitch);
        Switch darkmode = item.getActionView().findViewById(R.id.darkmode);

        if (darkmode.isChecked()) {
            //SharedPreferences.Editor editor = getSharedPreferences("switch", MODE_PRIVATE).edit();
            //SharedPreferences.Editor editor = prefs.edit();
            outState.putString("state", "checked");
            //editor.apply();
            //getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        super.onSaveInstanceState(outState);
    }

     */

    /*
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        MenuItem item = findViewById(R.id.myswitch);
        Switch darkmode = item.getActionView().findViewById(R.id.darkmode);
        String state = savedInstanceState.getString("state", "default");
        if(state.equals("checked")){

//it was checked

            darkmode.setChecked(true);

        }else{

//it was unchecked

            darkmode.setChecked(false);

        }
    }
    */
}
//}