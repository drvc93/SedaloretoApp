package pe.com.sedaloreto.appsms;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Process;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import DatabaseModel.Cliente;
import Task.ActualizarEstadoTask;
import Task.ListaClienteTask;

public class Login extends Activity {

    Button btnEntrar , btnSalir;
    FrameLayout  frameLogo ;
    String sharedName = "MyPrefers";
    SharedPreferences sharedPreferences;
    EditText txtUser , txtContra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        btnSalir = (Button) findViewById(R.id.btnSalir);
        frameLogo = (FrameLayout) findViewById(R.id.frameLogo);
        txtContra=(EditText) findViewById(R.id.txtContra);
        txtUser=(EditText)findViewById(R.id.txtUser);
        //Toast.makeText(Login.this, "test", Toast.LENGTH_SHORT).show();
        sharedPreferences = getSharedPreferences(sharedName, MODE_PRIVATE);


        if (sharedPreferences.getString("NextNumber",null) != null){
            sharedPreferences = getSharedPreferences(sharedName,MODE_PRIVATE);
            String num = sharedPreferences.getString("NextNumber", null);
            String  msj  = sharedPreferences.getString("NextMsj", null);
            String cod = sharedPreferences.getString("NextCod",null);
            String deuda = sharedPreferences.getString("NextDeuda",null);
            String mesesDeuda = sharedPreferences.getString("NextNumMeses",null);
            String NombreCli = sharedPreferences.getString("NextNombre",null);

            Toast.makeText(Login.this, "Enviando SMS... ", Toast.LENGTH_SHORT).show();
            if (sharedPreferences.getString("NextNombre",null) ==null  ) {
                CreateSMS(num, cod, msj);
            }
            else {

                CreateSMSPersonalizado(num,deuda,NombreCli,mesesDeuda,cod);
            }

        }

        else {
            Toast.makeText(Login.this, "ya no hay registros", Toast.LENGTH_SHORT).show();
        if (sharedPreferences.getString("usuario", null) == null) {
            CreateSharedPreferences(sharedPreferences);
        } else {

        }
        }

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtUser.getText().toString().equals("user") && txtContra.getText().toString().equals("2016") ){
                    Intent i = new Intent(getApplicationContext(), MenuOpciones.class);

                    startActivity(i);

                }

                else  {

                    Toast.makeText(Login.this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }


               /* boolean res = AutenticarUsuario(txtUser.getText().toString(),txtContra.getText().toString());

                if (res==true){


                Intent i = new Intent(getApplicationContext(), MenuOpciones.class);

                startActivity(i);
                }

                else {

                    Toast.makeText(getApplicationContext(),"Usuario o contraseña Incorrectos , intente de nuevo ",Toast.LENGTH_SHORT).show();

                }*/


            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Process.killProcess(Process.myPid());
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        frameLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://www.sedaloreto.com.pe/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    public void CreateSharedPreferences(SharedPreferences var_sharePrefers){

        Editor editor = sharedPreferences.edit();
        editor.putString("Usuario","sedaloreto");
        editor.putString("Contra", "user2016");
        editor.commit();

    }

    public  boolean AutenticarUsuario (String user , String contra){

        boolean result = false;

        sharedPreferences = getSharedPreferences(sharedName,MODE_PRIVATE);
        String getName = sharedPreferences.getString("Usuario","0");
        String getPass = sharedPreferences.getString("Contra","0");
        if (getName.equals(user)&& getPass.equals(contra) ){
            result=true;

        }

        else {

            result = false;
        }

        return result;

    }



    public  void  CreateSMSPersonalizado (final String numero , String deuda, String nomcli, String meses, final String cod){

        nomcli=nomcli.substring(0,10);

      final String msj = "Estimado "+ nomcli+ " SEDALORETO le informa que tiene una deuda de S/. "+deuda+" soles por " + meses + " meses,sirvase a pagar a la oficina  por favor.";
        new Thread(new Runnable() {
            @Override
            public void run() {




                try {


                    SmsManager sms = SmsManager.getDefault();

                    sms.sendTextMessage(numero, null, msj, null,null);
                    Log.i("NUMERO Y MENSAJE  >> ", numero + " - " + msj );
                    // Log.i("cont mensajes ", String.valueOf(i));
                    Thread.sleep(3000);
                    ActulizarCliente( cod);
                    NextClientePerson(msj);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("Log acitivity","- OK -");





            }
        }).start();
    }


    public  void  CreateSMS (final String numero , final String cod, final String msj){
      //  final String msj =  txtMensaje.getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {




                    try {


                        SmsManager sms = SmsManager.getDefault();

                        sms.sendTextMessage(numero, null, msj, null,null);
                        Log.i("NUMERO Y MENSAJE  >> ", numero + " - " + msj );
                       // Log.i("cont mensajes ", String.valueOf(i));
                        Thread.sleep(3000);
                        ActulizarCliente( cod);
                        GetNextCliente(msj);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i("Log acitivity","- OK -");





            }
        }).start();



    }



    public void  ActulizarCliente (String cod) {

        AsyncTask <String,String,String> async ;
        ActualizarEstadoTask actualizarEstadoTask = new ActualizarEstadoTask();

        try {
            async = actualizarEstadoTask.execute(cod);
            String res = (String)async.get();
            Log.i("Cliente actulizado >>>" , res);
//            Toast.makeText(MensajePersonalizado.this, res, Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }


    public  void  GetNextCliente (String msj){




        ListaClienteTask listaClienteTask = new ListaClienteTask();
        AsyncTask<String,String, Cliente[]> asyncTask ;
        Cliente[] clientes;
        ArrayList<Cliente> clts  = new ArrayList<Cliente>();
       // listNumeros = new ArrayList<String>();
        String url = "http://daniel88344-001-site1.etempurl.com/";


        try {
            asyncTask = listaClienteTask.execute("1",url);
            clientes = (Cliente[])asyncTask.get();
            if (clientes != null && clientes.length>0){
                for (int i  = 0 ; i<clientes.length; i++){

                    clts = ConvertArrToList(clientes);


                }


                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("NextNumber",clts.get(0).Celular);
               editor.putString("NextMsj",msj);
                editor.putString("NextCod",clts.get(0).codCliente);

                editor.commit();
                //Process.killProcess(Process.myPid());
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }

            else
            {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }




    }

    public  void  NextClientePerson (String mensaje){

        ListaClienteTask listaClienteTask = new ListaClienteTask();
        AsyncTask<String,String, Cliente[]> asyncTask ;
        Cliente[] clientes;
        ArrayList<Cliente> clts  = new ArrayList<Cliente>();
        // listNumeros = new ArrayList<String>();
        String url = "http://daniel88344-001-site1.etempurl.com/";


        try {
            asyncTask = listaClienteTask.execute("1",url);
            clientes = (Cliente[])asyncTask.get();
            if (clientes != null && clientes.length>0){
                for (int i  = 0 ; i<clientes.length; i++){

                    clts = ConvertArrToList(clientes);


                }


                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("NextNumber",clts.get(0).Celular);
                editor.putString("NextMsj",mensaje);
                editor.putString("NextCod",clts.get(0).codCliente);
                editor.putString("NextDeuda",clts.get(0).Deuda );
                editor.putString("NextNumMeses",clts.get(0).Meses);
                editor.putString("NextNombre",clts.get(0).NombreCliente);
                editor.commit();
                //Process.killProcess(Process.myPid());
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }

            else
            {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Cliente>  ConvertArrToList(Cliente[] clt){

        ArrayList<Cliente> res = new ArrayList<Cliente>();

        for (int i = 0 ; i< clt.length ;i++){

            res.add(clt[i]);


        }

        return  res;
    }


}
