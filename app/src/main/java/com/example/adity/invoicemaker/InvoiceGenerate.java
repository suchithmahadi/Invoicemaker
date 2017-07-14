package com.example.adity.invoicemaker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InvoiceGenerate extends AppCompatActivity {
 static TextView dateString;
    int ADD_SEAL=99;
    ProgressDialog pd;
    String bank,ifsccode,accholder,accno;
    String description,HSNcode,unitcost,quantity,amount;
    listadapt adapter;
    RecyclerView rv;
    String Name,Phone,Email,Address,Gstin,Pan_no;
    TextView ClientDetails,bank_details;
    LinearLayout l;
    DatabaseReference db;
    ListView lv;
    EditText invoice;
    ImageView image;
    int i=1;
    ArrayList<String [] > items=new ArrayList<>();
    Map<String,String> mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_gen);
        dateString=(TextView)findViewById(R.id.textdate);
         bank_details=(TextView)findViewById(R.id.bank);
        image=(ImageView)findViewById(R.id.SEAL);
        bank_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),BankDetails.class);
                intent.putExtra("Type","VENDOR");

                startActivityForResult(intent,1);

            }
        });
         invoice=(EditText)findViewById(R.id.invoiceid);
        rv= (RecyclerView)findViewById(R.id.itemlist);
        pd  =new ProgressDialog(InvoiceGenerate.this);
        adapter=new listadapt(InvoiceGenerate.this,items);
        rv.setLayoutManager(new LinearLayoutManager(InvoiceGenerate.this));
        rv.setAdapter(adapter);

         l=(LinearLayout) findViewById(R.id.linearLayout4);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),AddItem.class);
                startActivityForResult(intent,2);

            }
        });

         ClientDetails =(TextView)findViewById(R.id.client_details);
        ClientDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ClientDetails.class);
                startActivityForResult(intent,3);

            }
        });

        mp=new HashMap<>();
        dateString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        TextView save=(TextView)findViewById(R.id.saveinvoice);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadInvoice();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),ADD_SEAL);
            }
        });
    }


    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String date;
            date=setDateString(year, monthOfYear, dayOfMonth);

            dateString.setText(date);
        }


    }
    private static String setDateString(int year, int monthOfYear, int dayOfMonth) {

        // Increment monthOfYear for Calendar/Date -> Time Format setting
        monthOfYear++;
        String mon = "" + monthOfYear;
        String day = "" + dayOfMonth;

        if (monthOfYear < 10)
            mon = "0" + monthOfYear;
        if (dayOfMonth < 10)
            day = "0" + dayOfMonth;

        String s= year + "-" + mon + "-" + day;
    return s;
    }
    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1)

        {
            if (resultCode == 1) {
                bank = data.getStringExtra("bank_name");
                ifsccode = data.getStringExtra("ifsc_code");
                accholder = data.getStringExtra("account_holder");
                accno = data.getStringExtra("account_number");
                bank_details.setText(accholder);
                Toast.makeText(this, "bank " + accholder, Toast.LENGTH_SHORT).show();
            }
            if (resultCode == 2) {

                description = data.getStringExtra("description");
                HSNcode = data.getStringExtra("HSNcode");
                unitcost = data.getStringExtra("unitcost");
                quantity = data.getStringExtra("quantity");
                amount = data.getStringExtra("amount");

                items.add(new String[]{description, HSNcode, unitcost, quantity, amount});
                adapter.notifyDataSetChanged();
                String invoiceid = invoice.getText().toString();

                db = FirebaseDatabase.getInstance().getReference("Invoice/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + invoiceid);
                mp.put("Description", description);
                mp.put("HSN code", HSNcode);
                mp.put("unit cost", unitcost);
                mp.put("quantity", quantity);
                mp.put("amount", amount);

                db.child("Items").child("Item " + i).setValue(mp, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    }
                });

                i++;
                mp.clear();
                Toast.makeText(this, "Item", Toast.LENGTH_SHORT).show();

            }
            if (resultCode == 3) {
                Name = data.getStringExtra("name");
                Phone = data.getStringExtra("phone");
                Email = data.getStringExtra("email");
                Address = data.getStringExtra("address");
                Gstin=data.getStringExtra("gstin");
                Pan_no=data.getStringExtra("pan");
                Toast.makeText(this, "Client", Toast.LENGTH_SHORT).show();
                ClientDetails.setText(Name);

            }
        } else if (requestCode == ADD_SEAL) {




            try {
                switch (resultCode) {

                    case  Activity.RESULT_OK:
                            Picasso.with(this).load(data.getData()).into(image);
                            break;
                        case  Activity.RESULT_CANCELED:
                         Log.e("", "Selecting picture cancelled");
                            break;
                }
            } catch (Exception e) {
                Log.e("", "Exception in onActivityResult : " + e.getMessage());
            }
        }
    }
        public void uploadInvoice()
        {

            pd.setMessage("Generating Invoice");
            pd.show();
             db= FirebaseDatabase.getInstance().getReference("Invoice");


            String invoiceid=invoice.getText().toString();

            mp.put("Date_of_Invoice",dateString.getText().toString());
         //   mp.put("Invoice_ID",invoiceid);
            mp.put("Amount",amount);
            mp.put("place_of_supply","india");

            db.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(invoiceid).child("Details").setValue(mp, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
            if(databaseError==null)
                pd.hide();
                }
            });










        }









    }



