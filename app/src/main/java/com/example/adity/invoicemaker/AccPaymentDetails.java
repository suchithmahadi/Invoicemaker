package com.example.adity.invoicemaker;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;


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
    public AccPaymentDetails() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayList=new ArrayList<ObjectAcc>();
        adapter =new bankDetailsAdapter(getContext(),arrayList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_acc_payment_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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
                i.putExtra("Type","BankDetails");
                startActivityForResult(i,6);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 5 ){
            Toast.makeText(getContext(), "RESULT", Toast.LENGTH_SHORT).show();
           ObjectAcc Ob= new ObjectAcc(data.getStringExtra("bank_name"),data.getStringExtra("account_holder"),data.getStringExtra("account_number"));
            arrayList.add(Ob);
            adapter.notifyDataSetChanged();

        }
    }

    public class ObjectAcc{
        public String accno,accname,bankname;
        ObjectAcc(String A,String B,String C){
            accname=B;
            accno=A;
            bankname=C;
        }
        public String getBankname()
        { return bankname;

        }
    }
}
