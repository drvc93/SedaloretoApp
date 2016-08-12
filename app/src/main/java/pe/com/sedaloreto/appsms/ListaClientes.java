package pe.com.sedaloreto.appsms;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;

public class ListaClientes extends Activity {

    ListView listViewCliente ;
    EditText txtFilter ;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes);
        setTitle("Lista de clientes");

        listViewCliente = (ListView) findViewById(R.id.listViewClientes);
        txtFilter = (EditText) findViewById(R.id.txtFilterText);

        adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.simple_list,ListString());
        listViewCliente.setAdapter(adapter);

        txtFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                ListaClientes.this.adapter.getFilter().filter(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });

    }




    public  List<String[]> GetdataCSV () {

        List<String[]> list = new ArrayList<String[]>();
        String next[] = {};
        try {
            String url = Environment.getExternalStorageDirectory().toString();
            url=url+"/datamensajeria.csv";
            // File  file = new File(url.toString());
            //  FileInputStream fileInputStream= new FileInputStream(file);
            Log.i("Dir ---> ", Environment.getDataDirectory().toString() + "/" + "datamensajeria.csv");
///*

            CSVReader reader = new CSVReader(new FileReader(  "/sdcard/datamensajeria.csv"));

            for (;;) {
                next = reader.readNext();
                if (next != null) {
                    list.add(next);
                    Log.i("item",next.toString());
                } else {

                    Log.i("msj" , String.valueOf(list.size()) );
                    break;
                }
            }

          //  GetDataFromArray(list);


        } catch (IOException e) {
            Log.i("ERROR", e.getMessage());
            e.printStackTrace();

        }

        return  list;


    }

    public String[] ListString (){

        List<String[]> var_array = GetdataCSV();
        String[] res = new String[var_array.size()];

        for (int i = 0; i< var_array.size();i++){

            String nombre_cliente  = var_array.get(i)[1];
            res[i]=nombre_cliente;


        }


        return  res;

    }
}
