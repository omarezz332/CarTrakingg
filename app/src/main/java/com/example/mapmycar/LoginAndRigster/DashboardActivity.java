package com.example.mapmycar.LoginAndRigster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapmycar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity  extends AppCompatActivity {

    String EmailHolder;
    TextView Email;
    Button LogOUT ;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseUser mUser;
    //@SuppressLint("SetTextI18n")
    public static final String TAG="LOGIN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Email = (TextView)findViewById(R.id.textView1);
        LogOUT = (Button)findViewById(R.id.button1);

        Intent intent = getIntent();

        // Receiving User Email Send By MainActivity.
      //  EmailHolder = intent.getStringExtra(MainActivity.userEmail);

        // Setting up received email to TextView.
      //  Email.setText(Email.getText().toString()+ EmailHolder);

        // Adding click listener to Log Out button.

        LogOUT.setOnClickListener(new View.OnClickListener() {
            // @Override
            public void onClick(View v) {


                //Finishing current DashBoard activity on button click.
                finish();

                Toast.makeText(DashboardActivity.this,"Log Out Successfull", Toast.LENGTH_LONG).show();


            }
        });

    }

}
