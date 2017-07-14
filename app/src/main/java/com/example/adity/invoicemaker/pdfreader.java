package com.example.adity.invoicemaker;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Environment;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 * Created by Simrandeep Singh
 *
 */
public class pdfreader extends AppCompatActivity {
   private ImageView imageView;
     private int currentPage = 0;
     private Button next, previous;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfreader);


       next = (Button) findViewById(R.id.next);
        previous = (Button) findViewById(R.id.previous);

        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                currentPage++;
                render();
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                currentPage--;
                render();
            }
        });

        render();
    }

    /**
     * method to show the stored PDF in app
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void render() {
        try{
            imageView = (ImageView) findViewById(R.id.image);
            int REQ_WIDTH = imageView.getWidth();
            int REQ_HEIGHT = imageView.getHeight();

            Bitmap bitmap = Bitmap.createBitmap(REQ_WIDTH, REQ_HEIGHT, Bitmap.Config.ARGB_4444);
            File file = new File(Environment.getExternalStorageDirectory()+"/mypdf2.pdf");
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));

            if(currentPage < 0) {
                currentPage = 0;
            } else if(currentPage > renderer.getPageCount()) {
                currentPage = renderer.getPageCount() - 1;
            }

            Matrix m = imageView.getImageMatrix();
            Rect rect = new Rect(0, 0, REQ_WIDTH, REQ_HEIGHT);
            renderer.openPage(currentPage).render(bitmap, rect, m, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            imageView.setImageMatrix(m);
            imageView.setImageBitmap(bitmap);
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
       //     PhotoViewAttacher photoView= new PhotoViewAttacher(imageView);
         //   photoView.update();
            imageView.invalidate();

        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
