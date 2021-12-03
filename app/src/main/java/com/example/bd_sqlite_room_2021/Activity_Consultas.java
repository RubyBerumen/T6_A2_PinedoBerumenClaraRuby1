package com.example.bd_sqlite_room_2021;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import baseDeDatos.EscuelaBD;
import entidades.Alumno;

public class Activity_Consultas extends Activity {
    ListView lista;
    RecyclerView recicler;
    RecyclerView.Adapter adaper;
    RecyclerView.LayoutManager layoutManager;
    List<Alumno> e;
    EditText noControl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas);
        lista=findViewById(R.id.listview1);
        noControl=  findViewById(R.id.caja_id);



        new Thread(new Runnable() {
            @Override
            public void run() {
                EscuelaBD conexion=EscuelaBD.gettAppDatabase(getBaseContext());
                e= conexion.alumnoDAO().optenerTodos();
                for (Alumno a:e) {
                    Log.d("datos->",a.toString());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter adaptador=new ArrayAdapter(getBaseContext(),
                                android.R.layout.simple_list_item_1,e);
                        lista.setAdapter(adaptador);

                    }
                });



            }
        }).start();

        //---------------------Recycler View
        recicler=findViewById(R.id.recyclerView1);

        recicler.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(this);

        recicler.setLayoutManager(layoutManager);

        String nombres[]={"Leila","luke","Han","C3PO","Holu","gr","Padme","1","2","3","4","5","6","7"};

       // adaper=new AdaptadorRegistros(nombres);
        //recicler.setAdapter(adaper);


    }
    public void busqueda (View v){
        String[] datos = {""};
        recicler=findViewById(R.id.recyclerView1);

        recicler.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(this);
        recicler.setLayoutManager(layoutManager);
        int[] c = new int[1];
        if(!noControl.getText().toString().isEmpty()){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    EscuelaBD conexion=EscuelaBD.gettAppDatabase(getBaseContext());
                    e= conexion.alumnoDAO().optenerFiltrando("%"+noControl.getText().toString()+"%");
                    c[0] =e.size();
                    for(int i=0;i<c[0];i++){
                        System.out.println(i+"--> "+e.get(i).toString());
                        datos[0] = datos[0]+e.get(i).toString()+"/";
                    }
                    System.out.println("Datos----->"+datos[0]);
                    adaper=new AdaptadorRegistros(datos[0].split("/"));
                    recicler.setAdapter(adaper);
                }
            }).start();

        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    EscuelaBD conexion=EscuelaBD.gettAppDatabase(getBaseContext());
                    e= conexion.alumnoDAO().optenerTodos();
                    c[0] =e.size();
                    for(int i=0;i<c[0];i++){
                        datos[0] = datos[0]+e.get(i).toString()+"/";
                    }
                    System.out.println("Datos----->"+datos[0]);
                    adaper=new AdaptadorRegistros(datos[0].split("/"));
                    recicler.setAdapter(adaper);
                }
            }).start();
        }
    }
}
class AdaptadorRegistros extends RecyclerView.Adapter<AdaptadorRegistros.MyViewHolder>{

    private String[] mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public MyViewHolder(TextView t) {
            super(t);
            textView = t;
        }
    }

    public AdaptadorRegistros(String [] mDataset){
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public AdaptadorRegistros.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView tv = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.textview_reciclerview,
                parent, false);
        MyViewHolder vh = new MyViewHolder(tv);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.textView.setText(mDataset[position]);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}