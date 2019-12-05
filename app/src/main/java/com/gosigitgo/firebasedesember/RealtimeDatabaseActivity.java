package com.gosigitgo.firebasedesember;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RealtimeDatabaseActivity extends AppCompatActivity {
    //1 deklar
    private EditText edtNama, edtAlamat;
    private RadioButton rbLakiLaki, rbPerempuan;
    private Button btnSimpan;
    private Spinner spinPendidikan;

    //5
    private DatabaseReference databaseReference;
    //8
    private RecyclerView rvBiodata;

    //12
    public static String TABLE_BIODATA ="Biodata";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_database);

        //2 inisialisasi
        edtNama = findViewById(R.id.edt_nama);
        edtAlamat = findViewById(R.id.edt_alamat);
        rbLakiLaki = findViewById(R.id.rb_lakilaki);
        rbPerempuan = findViewById(R.id.rb_perempuan);
        btnSimpan = findViewById(R.id.btn_simpan);
        spinPendidikan = findViewById(R.id.spin_pendidikan);
        rvBiodata = findViewById(R.id.rv_biodata);
//14
        rvBiodata.setLayoutManager(new LinearLayoutManager(this));
        rvBiodata.setHasFixedSize(true);
        //11
        reloadData();

        //6
        databaseReference = FirebaseDatabase.getInstance().getReference(TABLE_BIODATA);
        //9
        rvBiodata = findViewById(R.id.rv_biodata);

        //4
        String[] pendidikan = {"Pilih Pendidikan","SMP","SMA","SMK","DIPLOMA"};
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, pendidikan);
        spinPendidikan.setAdapter(adapter);

        //3 action button
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ambil inputan
                String nama = edtNama.getText().toString();
                String alamat = edtAlamat.getText().toString();
                String pendidikan = spinPendidikan.getSelectedItem().toString();
                String gender = null;

                if (TextUtils.isEmpty(nama)){
                    edtNama.setError("Nama harus diisi");
                }else if(TextUtils.isEmpty(alamat)){
                    edtAlamat.setError("Alamat harus diisi");
                }else if (!rbLakiLaki.isChecked() && !rbPerempuan.isChecked()){
                    Toast.makeText(RealtimeDatabaseActivity.this, "Pilih Gender", Toast.LENGTH_SHORT).show();
                }else if (pendidikan.equals("Pilih Pendidikan")){
                    Toast.makeText(RealtimeDatabaseActivity.this, "Pilih Pendidikan", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (rbLakiLaki.isChecked()){
                        gender = rbLakiLaki.getText().toString();
                    }else if (rbPerempuan.isChecked()){
                        gender = rbPerempuan.getText().toString();
                    }
                    //save to realtime database
                    Biodata bd = new Biodata();
                    bd.setNama(nama);
                    bd.setAlamat(alamat);
                    bd.setGender(gender);
                    bd.setPendidikan(pendidikan);

                    //7
                    String childId = databaseReference.push().getKey();

                    databaseReference.child(childId).setValue(bd).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(RealtimeDatabaseActivity.this, "Berhasil simpan data", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(RealtimeDatabaseActivity.this, "gagal", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

    }
    //13
    private void reloadData(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(TABLE_BIODATA).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Biodata> biodataArrayList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Biodata biodata = snapshot.getValue(Biodata.class);
                    biodata.setId(snapshot.getKey());
                    biodataArrayList.add(biodata);
                }
                BiodataAdapter adapter = new BiodataAdapter(biodataArrayList, RealtimeDatabaseActivity.this);
                adapter.notifyDataSetChanged();
                rvBiodata.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
