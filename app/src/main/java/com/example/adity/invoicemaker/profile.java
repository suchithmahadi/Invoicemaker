package com.example.adity.invoicemaker;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * Created by adity on 7/10/2017.
 */

public class profile extends Fragment {

    TextView name,email;
    ImageView iv;
    ListView lv;
    String[] items={"Personal Details","Payment Details","Verify Email","Change Password","Change Email Address","Logout"};
    FirebaseAuth auth = FirebaseAuth.getInstance();
    final FirebaseUser user=auth.getCurrentUser();


    public profile() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




        return (inflater.inflate(R.layout.profile,container,false));
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Profile");


        name=(TextView)getActivity().findViewById(R.id.name1);
        email=(TextView)getActivity().findViewById(R.id.email1);

        iv=(ImageView)getActivity().findViewById(R.id.pic);

        char let= FirebaseAuth.getInstance().getCurrentUser().getDisplayName().trim().charAt(0);
        String letter=String.valueOf(let);



        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color1 = generator.getRandomColor();
        TextDrawable drawable1=TextDrawable.builder().buildRound(letter.toUpperCase(), color1);
        iv.setImageDrawable(drawable1);





        DatabaseReference db= FirebaseDatabase.getInstance().getReference();

        auth =FirebaseAuth.getInstance();
        final FirebaseUser user=auth.getCurrentUser();

        name.setText(""+user.getDisplayName());

        email.setText(""+user.getEmail());

        lv=(ListView)getActivity().findViewById(R.id.lv);

        ArrayAdapter adapter=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,items);
        lv.setAdapter(adapter);


        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),123);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==2)
                {
                    verifyEmail();
                }
                else if(position==3)
                {
                   resetPassword();
                }
                else if(position==4)
                {
                    auth.signOut();
                    startActivity(new Intent(getActivity(),MainActivity.class));
                }

            }
        });



    }






    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {

                case 123:
                    if (resultCode == Activity.RESULT_OK) {
                        Picasso.with(getActivity()).load(data.getData()).into(iv);
                        break;
                    } else if (resultCode == Activity.RESULT_CANCELED) {
                        Log.e("", "Selecting picture cancelled");
                    }
                    break;
            }
        } catch (Exception e) {
            Log.e("", "Exception in onActivityResult : " + e.getMessage());
        }
    }




    public void verifyEmail()
    {


        user.sendEmailVerification().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(),
                            "Verification email sent to " + user.getEmail(),
                            Toast.LENGTH_SHORT).show();
                    Log.e("", "email sent");


                } else {
                    Toast.makeText(getActivity(),
                            "Failed to send verification email." + task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        lv.getChildAt(2).setEnabled(false);
    }




    public void resetPassword()
    {
        auth.sendPasswordResetEmail(user.getEmail())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Email Sent", Toast.LENGTH_SHORT).show();

                        }
                        else
                            Toast.makeText(getActivity(), ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }











}
