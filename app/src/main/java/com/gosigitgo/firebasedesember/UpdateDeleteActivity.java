package com.gosigitgo.firebasedesember;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateDeleteActivity extends AppCompatActivity {
    //1 deklar
    private EditText edtNama, edtAlamat;
    private RadioButton rbLakiLaki, rbPerempuan;
    private Button btnUpdate, btnDelete;
    private Spinner spinPendidikan;
    private DatabaseReference databaseReference;
    public static String TABLE_BIODATA ="Biodata";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);

        //2 inisialisasi
        edtNama = findViewById(R.id.edt_update_nama);
        edtAlamat = findViewById(R.id.edt_update_alamat);
        rbLakiLaki = findViewById(R.id.rb_update_lakilaki);
        rbPerempuan = findViewById(R.id.rb_update_perempuan);
        btnUpdate = findViewById(R.id.btn_update);
        btnDelete = findViewById(R.id.btn_delete);
        spinPendidikan = findViewById(R.id.spin_update_pendidikan);


        //3
        String[] pendidikan = {"Pilih Pendidikan","SMP","SMA","SMK","DIPLOMA"};
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, pendidikan);
        spinPendidikan.setAdapter(adapter);
        //4
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //5 terima data dari adapter
        final Biodata getBiodata = getIntent().getParcelableExtra(TABLE_BIODATA);
        //6 tampilkan di view
        edtNama.setText(getBiodata.getNama());
        edtAlamat.setText(getBiodata.getAlamat());
        if (getBiodata.getGender().equals("Laki-Laki")){
            rbLakiLaki.setChecked(true);
        }else {
            rbPerempuan.setChecked(true);
        }
        for (int i = 0; i < adapter.getCount(); i++){
            if (spinPendidikan.getItemAtPosition(i).equals(getBiodata.getPendidikan())){
                spinPendidikan.setSelection(i);
            }
        }
        //7
    btnDelete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        databaseReference.child(TABLE_BIODATA).child(getBiodata.getId()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Toast.makeText(UpdateDeleteActivity.this, "berhasil dihapus", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
});
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //8 ambil inputan
                String nama = edtNama.getText().toString();
                String alamat = edtAlamat.getText().toString();
                String pendidikan = spinPendidikan.getSelectedItem().toString();
                String gender = null;

                if (TextUtils.isEmpty(nama)){
                    edtNama.setError("Nama harus diisi");
                }else if(TextUtils.isEmpty(alamat)){
                    edtAlamat.setError("Alamat harus diisi");
                }else if (!rbLakiLaki.isChecked() && !rbPerempuan.isChecked()){
                    Toast.makeText(UpdateDeleteActivity.this, "Pilih Gender", Toast.LENGTH_SHORT).show();
                }else if (pendidikan.equals("Pilih Pendidikan")){
                    Toast.makeText(UpdateDeleteActivity.this, "Pilih Pendidikan", Toast.LENGTH_SHORT).show();
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
                    //9
                  databaseReference.child(TABLE_BIODATA).child(getBiodata.getId()).setValue(bd).addOnCompleteListener(new OnCompleteListener<Void>() {
                      @Override
                      public void onComplete(@NonNull Task<Void> task) {
                          if (task.isSuccessful()){
                              Toast.makeText(UpdateDeleteActivity.this, "update berhasil", Toast.LENGTH_SHORT).show();
                          }else {
                              Toast.makeText(UpdateDeleteActivity.this, "update gagal", Toast.LENGTH_SHORT).show();
                          }
                      }
                  });
                }

            }
        });

    }
}
