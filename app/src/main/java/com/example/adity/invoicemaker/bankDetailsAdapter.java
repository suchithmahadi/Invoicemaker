package com.example.adity.invoicemaker;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by shivani on 13/7/17.
 */

public class bankDetailsAdapter extends RecyclerView.Adapter<bankDetailsAdapter.ViewHolder> {
   public  Context mContext;
    ArrayList<AccPaymentDetails.ObjectAcc> objects;
    bankDetailsAdapter(Context mContext, ArrayList<AccPaymentDetails.ObjectAcc> objects){
        this.mContext=mContext;
        this.objects=objects;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(mContext).inflate(R.layout.bankitemdetail, parent, false);
        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final AccPaymentDetails.ObjectAcc obj=objects.get(position);

        holder.bankName.setText(obj.bankname);

        holder.Acc_no.setText(obj.accno);

        holder.Acc_name.setText(obj.accname);

        holder.ifsc.setText(obj.ifsc_code);

    }


    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      public TextView Acc_name,Acc_no,bankName,ifsc;
        public ViewHolder(View itemView) {
            super(itemView);
            Acc_name=(TextView)itemView.findViewById(R.id.ac_name);
            Acc_no=(TextView)itemView.findViewById(R.id.ac_no);
            bankName=(TextView)itemView.findViewById(R.id.bname);

           ifsc=(TextView)itemView.findViewById(R.id.ifsc_code);

        }
    }
}
