package com.kahfi.arief.belajarfirebaseorderybyquery;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.kahfi.arief.belajarfirebaseorderybyquery.model.Siswa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private FirebaseDatabase fb;
    private DatabaseReference ref;

    private ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fb = FirebaseDatabase.getInstance();
        ref  = fb.getReference("data-siswa");






    }


    private void orderByChild(DatabaseReference ref){
       ref.orderByChild("noAbsen").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
              for(DataSnapshot snap : dataSnapshot.getChildren()){
                 Log.e("noAbsen", String.valueOf(snap.child("noAbsen").getValue()));
              }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });
    }


    private void orderByKey(DatabaseReference ref){
        ref.child("hobi-siswa").orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("Key-Sorting,"+dataSnapshot.getKey(), String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void orderByValue(){
        ref.child("hobi-siswa").child("baco").child("hobi").orderByValue().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e(dataSnapshot.getKey(), String.valueOf(dataSnapshot.getValue()));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void insertNewValue(DatabaseReference ref, final String namaSiswa, final List<String> hobi){
       ref.child("hobi-siswa").runTransaction(new Transaction.Handler() {
           @Override
           public Transaction.Result doTransaction(MutableData mutableData) {

               if(mutableData.hasChild(namaSiswa)){
                   return Transaction.abort();
               }else{
                   mutableData.child(namaSiswa).child("hobi").setValue(hobi);
                   return Transaction.success(mutableData);
               }

           }

           @Override
           public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
               if(databaseError==null){
                   Log.i("Sukses","insert value");
               }else{
                   Log.e("Error","error insert");
               }
           }
       });
    }

    private void insertValue(final DatabaseReference ref, final Siswa siswa){
        ref.child(ref.push().getKey()).setValue(new Siswa(siswa.getNoAbsen(),siswa.getNama(),siswa.getUmur()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("insert data");
        menu.add("orderByChild");
        menu.add("orderByKey");
        menu.add("orderByValue");
        menu.add("run Transaction");

        return super.onCreateOptionsMenu(menu);
    }

    private void buatAlertDialogInsertValue(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        View v = getLayoutInflater().inflate(R.layout.dialog_insert,null);

        alertDialog.setView(v);


        final EditText noAbsen = (EditText)v.findViewById(R.id.editAbsen);
        final EditText nama = (EditText)v.findViewById(R.id.editNama);
        final EditText umur = (EditText)v.findViewById(R.id.editUmur);


        alertDialog.setPositiveButton("insert", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    if(nama.getText().toString().trim().equals("")){
                        Toast.makeText(MainActivity.this, "field nama masih kosong", Toast.LENGTH_SHORT).show();
                    }else{
                        Siswa s = new Siswa(
                                Integer.parseInt(noAbsen.getText().toString()),
                                nama.getText().toString().trim(),
                                Integer.parseInt(umur.getText().toString().trim()));

                        insertValue(ref,s);

                    }
                }catch (NumberFormatException ex){
                    Toast.makeText(MainActivity.this, "Masih ada form yang kosong", Toast.LENGTH_SHORT).show();
                }catch (Exception ex){
                    Toast.makeText(MainActivity.this, "Error ex : " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Error dialog",ex.getMessage());
                }
            }
        });

        alertDialog.setNegativeButton("close",null);

        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getTitle()=="insert data"){
            buatAlertDialogInsertValue();
        }else if(item.getTitle()=="orderByChild"){
            orderByChild(ref);
        }else if(item.getTitle()=="orderByKey"){
            orderByKey(ref);
        }else if(item.getTitle()=="run Transaction"){
            List<String> list = new ArrayList<>();

            list.add("Belajar");
            list.add("Coding");
            list.add("Basket");


            insertNewValue(ref,"alo",list);
            insertNewValue(ref,"arief", Arrays.asList("Coding","Membaca","Berenang"));
            insertNewValue(ref,"aco",Arrays.asList("Soldering","Engineering","autowiring"));
            insertNewValue(ref,"baco",Arrays.asList("B","C","A","D"));
            insertNewValue(ref,"code",Arrays.asList("Spring mvc","Spring data"));
        }else{
            orderByValue();
        }
        return super.onOptionsItemSelected(item);
    }
}
