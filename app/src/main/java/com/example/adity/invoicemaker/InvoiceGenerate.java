package com.example.adity.invoicemaker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class InvoiceGenerate extends AppCompatActivity {
 static TextView dateString;
    String bank,ifsccode,accholder,accno;
    String description,HSNcode,unitcost,quantity,amount;
    String Name,Phone,Email,Address;
    TextView ClientDetails,addItem,bank_details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_gen);
        dateString=(TextView)findViewById(R.id.textdate);
         bank_details=(TextView)findViewById(R.id.bank);
        bank_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),BankDetails.class);
                intent.putExtra("Type","VENDOR");

                startActivityForResult(intent,1);

            }
        });
         addItem=(TextView)findViewById(R.id.addItem);
        addItem.setOnClickListener(new View.OnClickListener() {
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
        dateString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
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
        if (resultCode == 1) {
            bank=data.getStringExtra("bank_name");
            ifsccode=data.getStringExtra("ifsc_code");
            accholder=data.getStringExtra("account_holder");
            accno=data.getStringExtra("account_number");
            bank_details.setText(accholder);
            Toast.makeText(this, "bank "+accholder, Toast.LENGTH_SHORT).show();
            }
        if (resultCode == 2) {
            description= data.getStringExtra("description");
            HSNcode =data.getStringExtra("HSNcode");
            unitcost = data.getStringExtra("unitcost");
            quantity =data.getStringExtra("quantity");
            amount = data.getStringExtra("amount");
            addItem.setText(description);
            Toast.makeText(this, "Item", Toast.LENGTH_SHORT).show();


        }
        if (resultCode == 3) {
            Name= data.getStringExtra("name");
            Phone =data.getStringExtra("phone");
            Email = data.getStringExtra("email");
            Address =data.getStringExtra("address");
            Toast.makeText(this, "Client", Toast.LENGTH_SHORT).show();
            ClientDetails.setText(Name);


        }
        }
    }



