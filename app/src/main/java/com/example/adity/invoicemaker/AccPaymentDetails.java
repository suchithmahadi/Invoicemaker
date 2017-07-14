package com.example.adity.invoicemaker;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.R.drawable.ic_delete;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccPaymentDetails} factory method to
 * create an instance of this fragment.
 */
public class AccPaymentDetails extends Fragment {
    bankDetailsAdapter adapter;
    RecyclerView rv;
    FloatingActionButton fab;
    ArrayList<ObjectAcc> arrayList;
    String ifsc,bname,accnum,accname;
    ProgressDialog pd;
    public AccPaymentDetails() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayList=new ArrayList<ObjectAcc>();
        pd=new ProgressDialog(getActivity());
        pd.setMessage("Please Wait ...");
        pd.show();
        arrayList.clear();
        adapter =new bankDetailsAdapter(getContext(),arrayList);

       Read();




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_acc_payment_details, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        fab=(FloatingActionButton)getActivity().findViewById(R.id.fab) ;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv= (RecyclerView)getActivity().findViewById(R.id.list_item);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        rv.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getContext(),BankDetails.class);
                arrayList.clear();
                i.putExtra("Type","BankDetails");
                startActivityForResult(i,6);
            }
        });

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT ){
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int pos=viewHolder.getAdapterPosition();
                ObjectAcc obj=arrayList.get(pos);
                AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getActivity())
                        //set message, title, and icon
                        .setTitle("Delete")
                        .setMessage("Do you really want to delete the following bank details?\n\n"+"\t\tAccount Holder -"+obj.accname+"\n\t\tAccount Number -"
            +obj.accno+"\n\t\tBank Name -"+obj.bankname)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //your deleting code
                               arrayList.remove(pos);
                               adapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), "DELETED", Toast.LENGTH_SHORT).show();

                                dialog.dismiss();
                            }

                        })

                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyDataSetChanged();
                                dialog.dismiss();

                            }
                        })
                        .create();
                myQuittingDialogBox.show();
            }
        };
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rv);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 5 ){
            Toast.makeText(getContext(), "RESULT", Toast.LENGTH_SHORT).show();
            ObjectAcc Ob= new ObjectAcc(data.getStringExtra("account_holder"),data.getStringExtra("bank_name"),data.getStringExtra("account_number"),data.getStringExtra("ifsc_code"));

        }
    }

    public class ObjectAcc{
        public String accno,accname,bankname,ifsc_code;
        ObjectAcc(String name,String bname,String acno,String ifsc){
            accname=name;
            accno=acno;
            bankname=bname;
            ifsc_code=ifsc;
        }

    }


    public void Read()
    {
        DatabaseReference db= FirebaseDatabase.getInstance().getReference("Account Details/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());

        // String name,String bname,String acno,String ifsc

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot bank:dataSnapshot.getChildren())
                {

                    bname=bank.getKey();
                    for(DataSnapshot accno:bank.getChildren())
                    {
                        accnum=accno.getKey();
                        for(DataSnapshot details:accno.getChildren())
                        {
                            if(details.getKey().equals("Ifsc Code"))
                            {
                                ifsc=details.getValue(String.class);
                            }
                            else if(details.getKey().equals("Account Holder"))
                            {
                                accname=details.getValue(String.class);
                            }



                        }

                    }
                    ObjectAcc obj=new ObjectAcc(accname,bname,accnum,ifsc);
                    arrayList.add(obj);
                    adapter.notifyDataSetChanged();

                }



                pd.hide();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "error"+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
