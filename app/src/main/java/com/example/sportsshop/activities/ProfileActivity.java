package com.example.sportsshop.activities;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sportsshop.R;
import com.example.sportsshop.models.ProfileModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    FirebaseAuth auth;
    FirebaseDatabase database;
    TextView myName, myEmail;
    EditText name, email, newPassword, oldPassword;
    Button changeBtn;
    String oldEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null)
        oldEmail = auth.getCurrentUser().getEmail();
        database = FirebaseDatabase.getInstance();
        toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

        myEmail = findViewById(R.id.my_email);
        name = findViewById(R.id.user_name);
        newPassword = findViewById(R.id.password);
        oldPassword = findViewById(R.id.old_password);
        changeBtn = findViewById(R.id.change_btn);
            if(auth.getCurrentUser() != null) {
                myEmail.setText(auth.getCurrentUser().getEmail());
            }

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    updateUserprofile();

            }
        });





    }

    private void updateUserprofile(){

        if(auth.getCurrentUser() !=null && !name.getText().toString().isEmpty()   && !newPassword.getText().toString().isEmpty()  && !oldPassword.getText().toString().isEmpty()){

            String newNewPassword =newPassword.getText().toString();
            String newName =name.getText().toString();
            String old = oldPassword.getText().toString();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


            AuthCredential credential = EmailAuthProvider
                    .getCredential(oldEmail, old);

                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {

                                    user.updatePassword(newNewPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(ProfileActivity.this, "password", Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(ProfileActivity.this, "Error!" +task.getException(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    ProfileModel profileModel = new ProfileModel(newName, oldEmail, newNewPassword);
                                    database.getReference().child("Users").child(auth.getCurrentUser().getUid()).setValue(profileModel);

                                    Toast.makeText(ProfileActivity.this, "Profile updated!", Toast.LENGTH_SHORT).show();
                                    auth.signOut();
                                    startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
                                }else{

                                    Toast.makeText(ProfileActivity.this, "Wrong old password", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });



        }else {
            Toast.makeText(ProfileActivity.this, "Fill all the fields!", Toast.LENGTH_SHORT).show();
        }
    }
}