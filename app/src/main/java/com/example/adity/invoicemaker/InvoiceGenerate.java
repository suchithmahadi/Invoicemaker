package com.example.adity.invoicemaker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.FontSelector;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    TextView bank_details;
    LinearLayout l,ClientDetails;
    DatabaseReference db;
    ListView lv;
    EditText invoice;
    ImageView image;
    int i=1;
    ArrayList<String [] > items;
    Map<String,String> mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_gen);
        dateString=(TextView)findViewById(R.id.textdate);
         bank_details=(TextView)findViewById(R.id.bank);
        image=(ImageView)findViewById(R.id.SEAL);
        items=new ArrayList<>();
        adapter=new listadapt(InvoiceGenerate.this,items);


        bank_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),BankDetails.class);
                intent.putExtra("Type","VENDOR");
                startActivityForResult(intent,1);

            }
        });



        Button preview=(Button)findViewById(R.id.previewbutton);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InvoiceGenerate.this,pdfreader.class));
            }
        });




         invoice=(EditText)findViewById(R.id.invoiceid);
        rv= (RecyclerView)findViewById(R.id.itemlist);
        pd  =new ProgressDialog(InvoiceGenerate.this);

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

         ClientDetails =(LinearLayout) findViewById(R.id.linearLayout3);
        ClientDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),ClientDetails.class);
                intent.putExtra("Type","Vendor");
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

                save();
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
            if (resultCode == 1) {
                bank = data.getStringExtra("bank_name");
                ifsccode = data.getStringExtra("ifsc_code");
                accholder = data.getStringExtra("account_holder");
                accno = data.getStringExtra("account_number");
                bank_details.setText(accholder);

            }
           else if (resultCode == 2) {

                Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
                description = data.getStringExtra("description");
                HSNcode = data.getStringExtra("HSNcode");
                unitcost = data.getStringExtra("unitcost");
                quantity = data.getStringExtra("quantity");
                amount = data.getStringExtra("amount");

                items.add(new String[]{description, HSNcode, unitcost, quantity, amount});
                adapter.notifyDataSetChanged();
                String invoiceid = invoice.getText().toString();


                db = FirebaseDatabase.getInstance().getReference("Invoice").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(invoiceid);
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
            else if (resultCode == 3) {
                Name = data.getStringExtra("name");
                Phone = data.getStringExtra("phone");
                Email = data.getStringExtra("email");
                Address = data.getStringExtra("address");
                Gstin=data.getStringExtra("gstin");
                Pan_no=data.getStringExtra("pan");
                Toast.makeText(this, "Client", Toast.LENGTH_SHORT).show();

                TextView company,gst,pan;
                company=(TextView)findViewById(R.id.com);
                gst=(TextView)findViewById(R.id.gst);
                pan=(TextView)findViewById(R.id.pan);

                company.setText(Name);
                gst.setText(Gstin);
                pan.setText(Pan_no);

                LinearLayout clients=(LinearLayout)findViewById(R.id.clients);
                clients.setVisibility(View.VISIBLE);




            }
            else if (requestCode == ADD_SEAL) {
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


        public void save()
        {
            com.itextpdf.text.Document doc=new com.itextpdf.text.Document(PageSize.A4, 0f, 0f, 0f, 0f); //creating document
            String outPath= Environment.getExternalStorageDirectory()+"/mypdf2.pdf"; //location where the pdf will store
            try{
                PdfWriter.getInstance(doc,new FileOutputStream(outPath));
                doc.open();
                   /* PdfPTable table1=new PdfPTable(1);
                    PdfPCell pdfPCell=new PdfPCell(new Paragraph("Tax Invoice"));
                    pdfPCell.addElement(new Paragraph("Company Name"));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setColspan(4);
                    pdfPCell.setBackgroundColor(BaseColor.GRAY);
                    Chunk linebreak = new Chunk(new DottedLineSeparator());
                    doc.add(linebreak);
                    table1.addCell(pdfPCell);

                    doc.add(new Paragraph(t.getText().toString()));
                    doc.add(new Paragraph("hi how are you?"));
                    doc.add(table1);
                    table1.addCell("Company name");
                    table1.addCell("date");
                    doc.add(table1);*/



                doc.setMargins(0,0, 0,0);           // setting margin
                PdfPTable innertable = new PdfPTable(1);  //first table
                //innertable.setWidths(new int[]{8, 12, 1, 4, 12});


// column 1
                PdfPCell cell = new PdfPCell(new Phrase("Tax Invoice",
                        FontFactory.getFont(FontFactory.COURIER_BOLD,18,Font.NORMAL,BaseColor.BLACK)));
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                innertable.addCell(cell);


// column 2
                cell = new PdfPCell(new Paragraph("Company name"));
                //cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                innertable.addCell(cell);
// column 3
                cell = new PdfPCell(new Paragraph("Address"));
                //cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                innertable.addCell(cell);
// column 4
                cell = new PdfPCell(new Paragraph("Contact Person"));
                //cell.setPaddingLeft(2);
                //cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                innertable.addCell(cell);
// spacing
                cell = new PdfPCell();
                cell.setColspan(5);
                cell.setFixedHeight(6);
                cell.setBorder(Rectangle.NO_BORDER);
                innertable.addCell(cell);
                doc.add(innertable);
                //      Chunk linebreak = new Chunk(new LineSeparator());
                //      doc.add(linebreak);

//Second table
                PdfPTable innertable2 = new PdfPTable(4);
                PdfPCell cell1 = new PdfPCell(new Phrase("Invoice no."));
                cell1.setBorder(Rectangle.NO_BORDER);
                cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                innertable2.addCell(cell1);

                cell1 = new PdfPCell(new Phrase("--"));
                //cell.setBorder(Rectangle.NO_BORDER);
                cell1.setPaddingLeft(20);
                // cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                innertable2.addCell(cell1);

                cell1 = new PdfPCell(new Phrase("Transporter Details"));
                cell1.setBorder(Rectangle.NO_BORDER);
                // cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                innertable2.addCell(cell1);


                cell1 = new PdfPCell(new Phrase("--"));
                //cell.setBorder(Rectangle.NO_BORDER);
                cell.setPaddingLeft(2);
                // cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                innertable2.addCell(cell1);
                cell1 = new PdfPCell(new Phrase("Invoice date"));
                cell1.setBorder(Rectangle.NO_BORDER);
                cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                innertable2.addCell(cell1);

                cell1 = new PdfPCell(new Phrase("--"));
                //cell.setBorder(Rectangle.NO_BORDER);
                cell1.setPaddingLeft(20);
                // cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                innertable2.addCell(cell1);

                cell1 = new PdfPCell(new Phrase("Vehicle number"));
                cell1.setBorder(Rectangle.NO_BORDER);
                // cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                innertable2.addCell(cell1);


                cell1 = new PdfPCell(new Phrase("--"));
                //cell.setBorder(Rectangle.NO_BORDER);
                cell.setPaddingLeft(2);
                // cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                innertable2.addCell(cell1);
                cell1 = new PdfPCell(new Phrase("Reverse Charge (Y/N)"));
                cell1.setBorder(Rectangle.NO_BORDER);
                cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                innertable2.addCell(cell1);

                cell1 = new PdfPCell(new Phrase("--"));
                //cell.setBorder(Rectangle.NO_BORDER);
                cell1.setPaddingLeft(20);
                // cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                innertable2.addCell(cell1);

                cell1 = new PdfPCell(new Phrase("Date of Supply"));
                cell1.setBorder(Rectangle.NO_BORDER);
                // cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                innertable2.addCell(cell1);


                cell1 = new PdfPCell(new Phrase("--"));
                //cell.setBorder(Rectangle.NO_BORDER);
                cell.setPaddingLeft(2);
                // cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                innertable2.addCell(cell1);

                doc.add(innertable2);
                //   Chunk linebreak1 = new Chunk(new LineSeparator());
                // doc.add(linebreak1);

                PdfPTable innertable3 = new PdfPTable(2);
                PdfPCell celll = new PdfPCell(new Phrase("RECIPIENT (BILL TO)",
                        FontFactory.getFont(FontFactory.COURIER,15,Font.NORMAL,BaseColor.BLACK)));
                celll.setHorizontalAlignment(Element.ALIGN_CENTER);
                innertable3.addCell(celll);

                celll = new PdfPCell(new Phrase("ADDRESS OF DELIVERY (SHIP TO)",
                        FontFactory.getFont(FontFactory.COURIER,15,Font.NORMAL,BaseColor.BLACK)));
                celll.setHorizontalAlignment(Element.ALIGN_CENTER);
                innertable3.addCell(celll);


                doc.add(innertable3);
                //Chunk linebreak2 = new Chunk(new LineSeparator());
                //doc.add(linebreak2);

                PdfPTable innertable4 = new PdfPTable(4);
                PdfPCell cell4 = new PdfPCell(new Phrase("Name :"));
                innertable4.addCell(cell4);

                cell4 = new PdfPCell(new Phrase("--------"));
                innertable4.addCell(cell4);
                cell4 = new PdfPCell(new Phrase("Name :"));
                innertable4.addCell(cell4);

                cell4 = new PdfPCell(new Phrase("--------"));
                innertable4.addCell(cell4);

                cell4 = new PdfPCell(new Phrase("Address :"));
                innertable4.addCell(cell4);

                cell4 = new PdfPCell(new Phrase("--------"));
                innertable4.addCell(cell4);

                cell4 = new PdfPCell(new Phrase("Address :"));
                innertable4.addCell(cell4);

                cell4 = new PdfPCell(new Phrase("--------"));
                innertable4.addCell(cell4);

                    /*cell4 = new PdfPCell(new Phrase("____________"));
                    innertable4.addCell(cell4);

                    cell4 = new PdfPCell(new Phrase("--------"));
                    innertable4.addCell(cell4);
                    cell4 = new PdfPCell(new Phrase("______________-"));
                    innertable4.addCell(cell4);

                    cell4 = new PdfPCell(new Phrase("--------"));
                    innertable4.addCell(cell4);*/

                cell4 = new PdfPCell(new Phrase("GSTIN :"));
                innertable4.addCell(cell4);

                cell4 = new PdfPCell(new Phrase("--------"));
                innertable4.addCell(cell4);
                cell4 = new PdfPCell(new Phrase("GSTIN :"));
                innertable4.addCell(cell4);

                cell4 = new PdfPCell(new Phrase("--------"));
                innertable4.addCell(cell4);

                cell4 = new PdfPCell(new Phrase("State :"));
                innertable4.addCell(cell4);

                cell4 = new PdfPCell(new Phrase("--------"));
                innertable4.addCell(cell4);
                cell4 = new PdfPCell(new Phrase("State :"));
                innertable4.addCell(cell4);

                cell4 = new PdfPCell(new Phrase("--------"));
                innertable4.addCell(cell4);
                cell4 = new PdfPCell(new Phrase("code :"));
                innertable4.addCell(cell4);

                cell4 = new PdfPCell(new Phrase("--------"));
                innertable4.addCell(cell4);
                cell4 = new PdfPCell(new Phrase("code :"));
                innertable4.addCell(cell4);

                cell4 = new PdfPCell(new Phrase("--------"));
                cell4.setMinimumHeight(50f);
                innertable4.addCell(cell4);

                doc.add(innertable4);
                // Chunk linebreak3 = new Chunk(new LineSeparator());
                //doc.add(linebreak3);


                PdfPTable innertable5 = new PdfPTable(11);
                innertable5.setWidths(new int[]{4,11,4,4,3,4,6,5,5,5,4});
                PdfPCell cell5 = new PdfPCell(new Phrase("S.No:"));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("Product Description"));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("HSN code"));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("UQC"));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("Qty"));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("Rate"));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("Taxable Value"));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("SGST"));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("CGST"));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("IGST"));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("Total"));
                cell5.setMinimumHeight(10f);
                innertable5.addCell(cell5);

                //first item
                cell5 = new PdfPCell(new Phrase("        "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("       "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("      "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("    "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("    "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("     "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("      "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("      "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("     "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("     "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("    "));
                cell5.setMinimumHeight(10f);
                innertable5.addCell(cell5);

                //second item
                cell5 = new PdfPCell(new Phrase("        "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("       "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("      "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("    "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("    "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("     "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("      "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("      "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("     "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("     "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("    "));
                cell5.setMinimumHeight(10f);
                innertable5.addCell(cell5);


                //Third item
                cell5 = new PdfPCell(new Phrase("        "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("       "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("      "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("    "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("    "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("     "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("      "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("      "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("     "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("     "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("    "));
                cell5.setMinimumHeight(10f);
                innertable5.addCell(cell5);


                //Fourth item
                cell5 = new PdfPCell(new Phrase("        "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("        "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("      "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("    "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("    "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("     "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("      "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("      "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("     "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("     "));
                innertable5.addCell(cell5);
                cell5 = new PdfPCell(new Phrase("    "));
                cell5.setMinimumHeight(10f);
                innertable5.addCell(cell5);







                doc.add(innertable5);
                //Chunk linebreak4 = new Chunk(new LineSeparator());
                //doc.add(linebreak4);



                //next step
                PdfPTable innertable6 = new PdfPTable(3);
                innertable6.setWidths(new int[]{20,20,20});
                PdfPCell cell6 = new PdfPCell(new Phrase("Total Invoice Amount In Words:"));
                innertable6.addCell(cell6);
                cell6 = new PdfPCell(new Phrase("Place Of Supply :"));
                innertable6.addCell(cell6);
                cell6 = new PdfPCell(new Phrase("Total Amount:"));
                innertable6.addCell(cell6);
                doc.add(innertable6);
                // Chunk linebreak5 = new Chunk(new LineSeparator());
                // doc.add(linebreak5);

                PdfPTable innertable7 = new PdfPTable(3);
                innertable6.setWidths(new int[]{20,20,20});
                PdfPCell cell7 = new PdfPCell(new Phrase("Terms and Condition"));
                cell7.setVerticalAlignment(Element.ALIGN_TOP);
                innertable7.addCell(cell7);
                cell7 = new PdfPCell(new Phrase("Common Seal"));
                cell7.setVerticalAlignment(Element.ALIGN_BOTTOM);
                innertable7.addCell(cell7);
                cell7 = new PdfPCell(new Phrase("Authorised Signatory"));
                cell7.setMinimumHeight(100f);
                cell7.setVerticalAlignment(Element.ALIGN_BOTTOM);
                innertable7.addCell(cell7);






                doc.add(innertable7);
                // Chunk linebreak6 = new Chunk(new LineSeparator());
                //doc.add(linebreak6);
                doc.close();


            }
            catch (DocumentException e)
            {
                e.printStackTrace();

            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }

        }









    }



