package com.example.adity.invoicemaker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AddItem extends AppCompatActivity {

    EditText descrip,HSN,cost,quant;
    TextView amt;
    String description,HSNcode,unitcost,quantity,amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        descrip=(EditText)findViewById(R.id.description);
        HSN=(EditText)findViewById(R.id.HSNcode);
        cost=(EditText)findViewById(R.id.cost);
        quant=(EditText)findViewById(R.id.quant);
        amt=(TextView)findViewById(R.id.amt);


        description=descrip.getText().toString();
        HSNcode=HSN.getText().toString();
        unitcost=cost.getText().toString();
        quantity=quant.getText().toString();
        amount=amt.getText().toString();


        Button save=(Button)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();
                i.putExtra("description",description);
                i.putExtra("HSNcode",HSNcode);
                i.putExtra("unitcost",unitcost);
                i.putExtra("quantity",quantity);
                i.putExtra("amount",amount);
                setResult(2,i);
                finish();
            }
        });
        Intent i=new Intent();
        i.putExtra("description",description);
        i.putExtra("HSNcode",HSNcode);
        i.putExtra("unitcost",unitcost);
        i.putExtra("quantity",quantity);
        i.putExtra("amount",amount);
        setResult(1,i);









    }
}
