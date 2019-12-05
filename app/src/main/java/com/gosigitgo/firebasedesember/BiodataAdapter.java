package com.gosigitgo.firebasedesember;

import android.content.Context;
import android.content.Intent;
import android.text.style.UpdateAppearance;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.security.PrivateKey;
import java.util.List;
//1
//implement metod, create class dan constructor
public class BiodataAdapter extends RecyclerView.Adapter<BiodataAdapter.ViewHolder> {
    //2
    //buat constructor
    List<Biodata> biodataList;
    Context context;

    public BiodataAdapter(List<Biodata> biodataList, Context context) {
        this.biodataList = biodataList;
        this.context = context;
    }

    @NonNull
    @Override
    public BiodataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //3
        View view = LayoutInflater.from(context).inflate(R.layout.item_biodata,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BiodataAdapter.ViewHolder holder, final int position) {
        //6
        holder.tvNama.setText(biodataList.get(position).getNama());
        holder.tvAlamat.setText(biodataList.get(position).getAlamat());
        holder.tvGender.setText(biodataList.get(position).getGender());
        holder.tvPendidikan.setText(biodataList.get(position).getPendidikan());
        //8 melempar data
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateDeleteActivity.class);
                intent.putExtra(UpdateDeleteActivity.TABLE_BIODATA, biodataList.get(position));
                context.startActivity(intent);
            }
        });

    }
    //7
    @Override
    public int getItemCount() {
        return biodataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //4
        private TextView tvNama, tvAlamat, tvGender, tvPendidikan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //5
            tvNama = itemView.findViewById(R.id.item_nama);
            tvAlamat = itemView.findViewById(R.id.item_alamat);
            tvGender = itemView.findViewById(R.id.item_gender);
            tvPendidikan = itemView.findViewById(R.id.item_pendidikan);

        }
    }
}
