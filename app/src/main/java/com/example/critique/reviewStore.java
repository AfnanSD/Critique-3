//users shall be able to review stores,restaurents,cafe
//need to be connected to another activity
//or added it as a dialog
package com.example.critique;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class reviewStore extends AppCompatActivity {
    //customer id need to be included??
    int id;//storeid
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_store);

        DBHelper db = DBHelper.getInstance(this);

        Intent i = getIntent();
        id = i.getIntExtra("StoreID",-1);

        RatingBar r = findViewById(R.id.ratingBar);
        EditText review = (EditText) findViewById(R.id.reviewtext);


        Button b = findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float customerRating = r.getRating();
                String customerReview = review.getText().toString();
                db.insertDataIntoReviewsTable(id,customerReview,customerRating);
                Toast.makeText(reviewStore.this,"review has been sent succesfully",Toast.LENGTH_SHORT).show();
                reviewStore.this.startActivity(new Intent(reviewStore.this,ViewStores.class));
            }
        });
    }
}
