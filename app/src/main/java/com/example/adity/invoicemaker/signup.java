package com.example.adity.invoicemaker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {

    EditText fn,email,pass,repass,phone;
    CheckBox cb;
    Button signup;
    DatabaseReference db;
    Map<String,String> mp=new HashMap<>();
    ProgressDialog pd;
    FirebaseAuth mAuth;
    Boolean mAllowNavigation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fn = (EditText) findViewById(R.id.fn);
        email = (EditText) findViewById(R.id.enteremail);
        pass = (EditText) findViewById(R.id.enterpass);
        repass = (EditText) findViewById(R.id.repass);
        phone = (EditText) findViewById(R.id.num);
        cb = (CheckBox) findViewById(R.id.cb);
        signup = (Button) findViewById(R.id.signup);
        pd = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name, em, pwd, no;
                name = fn.getText().toString().trim();
                em = email.getText().toString().trim();
                pwd = pass.getText().toString().trim();
                no = phone.getText().toString().trim();

                if (name.equals("")) {
                    fn.setText("");
                    fn.setHint("Enter a valid name");
                }
                else if ( (em.isEmpty()) || !(em.contains("@")) || !(em.contains("."))) {
                    email.setText("");
                    email.setHint("Enter  valid Email");
                }
                else if (pwd.isEmpty()) {
                    pass.setText("");
                    pass.setHint("please Enter a valid Password");
                }
                else if (no.length() < 10) {
                    phone.setText("");
                    phone.setHint("Please enter a valid number");
                }
                else if (!cb.isChecked()) {
                    cb.setHint("please check this");
                }
                else {
                    mp.put("Company", name);
                    mp.put("Email", em);
                    mp.put("Password", pwd);
                    mp.put("Mobile number", no);
                    mp.put("contact person",repass.getText().toString());
                    pd.setMessage("Registering User");
                    pd.show();


                    mAuth.createUserWithEmailAndPassword(em, pwd).addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();

                                mAuth.getCurrentUser().updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                }
                                            }
                                        });
                                mAuth.signInWithEmailAndPassword(em,pwd).addOnCompleteListener(signup.this,new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful())
                                        {
                                            db = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                            db.child("details").push().setValue(mp, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                    if (databaseError == null) {
                                                        Toast.makeText(signup.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(signup.this, "database error" + databaseError.toString(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                            pd.setMessage("Successfully Registered");
                                            pd.hide();

                                            startActivity(new Intent(signup.this,NavigationDrawer.class));

                                        }
                                        else {
                                            Toast.makeText(signup.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                });

                            }
                            else
                            {
                                pd.hide();
                                Toast.makeText(signup.this, "Could not Register " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }
            }
        });



    }
}
