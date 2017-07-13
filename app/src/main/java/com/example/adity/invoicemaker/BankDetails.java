package com.example.adity.invoicemaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class BankDetails extends AppCompatActivity {

    EditText bankname,ifsc,accholdername,accnumber;
    String bank,ifsccode,accholder,accno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);


        bankname=(EditText)findViewById(R.id.bankname);
        ifsc=(EditText)findViewById(R.id.IfSc);
        accholdername=(EditText)findViewById(R.id.accholder);
        accnumber=(EditText)findViewById(R.id.accno);

        bank=bankname.getText().toString();
        ifsccode=ifsc.getText().toString();
        accholder=accholdername.getText().toString();
        accno=accnumber.getText().toString();

        Intent i=new Intent();
        i.putExtra("bank_name",bank);
        i.putExtra("ifsc_code",ifsccode);
        i.putExtra("account_holder",accholder);
        i.putExtra("account_number",accno);
        setResult(1,i);


    }
}
