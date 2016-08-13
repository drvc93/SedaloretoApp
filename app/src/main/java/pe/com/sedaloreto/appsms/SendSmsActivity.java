package pe.com.sedaloreto.appsms;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import DatabaseModel.Cliente;
import Task.ActualizarEstadoTask;
import Task.ListaClienteTask;
import  au.com.bytecode.opencsv.CSVReader;




public class SendSmsActivity extends Activity {

    ProgressDialog barProgressDialog;
    int contadorEnvio=0;
    Handler updateBarHandler;
    EditText txtMensaje;
    String sharedName = "MyPrefers";
    SharedPreferences sharedPreferences;
    String cod_cliente_update;
    ArrayList<String> listNumeros ;
    ArrayList<Cliente> aux_cliente = new ArrayList<Cliente>();

    Context context=this;
    Button  btnActivar ;
    ArrayList<String> arrayListNumero  = new ArrayList<String>();
    ArrayList<String> arrayListTextsms = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        btnActivar = (Button)findViewById(R.id.btnActivar);
        sharedPreferences = getSharedPreferences(sharedName, MODE_PRIVATE);
        LoadDataCliente();


        btnActivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Aviso();
       //       GetdataCSV();
            }
        });
    }

    public void LoadDataCliente (){

        ListaClienteTask  listaClienteTask = new ListaClienteTask();
        AsyncTask<String,String, Cliente[]> asyncTask ;
        Cliente[] clientes;
        listNumeros = new ArrayList<String>();
        String url = "http://daniel88344-001-site1.etempurl.com/";


        try {
            asyncTask = listaClienteTask.execute("1",url);
            clientes = (Cliente[])asyncTask.get();
            if (clientes!=null && clientes.length>0){

                for (int i  = 0 ; i<clientes.length; i++){
                    aux_cliente.add(clientes[i]);
                    String num = clientes[i].Celular;
                    listNumeros.add(num);
                    cod_cliente_update = clientes[i].codCliente;

                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

     public  void GetdataCSV () {

         List<String[]> list = new ArrayList<String[]>();
         String next[] = {};
         try {
             String url = Environment.getExternalStorageDirectory().toString();
             url=url+"/datamensajeria.csv";
             // File  file = new File(url.toString());
             //  FileInputStream fileInputStream= new FileInputStream(file);
             Log.i("Dir ---> ",Environment.getDataDirectory().toString()+"/"+ "datamensajeria.csv");
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

             GetDataFromArray(list);


         } catch (IOException e) {
             Log.i("ERROR", e.getMessage());
             e.printStackTrace();

         }



     }


    public  void  Aviso (){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("¿Desea hacer el envio de mensajes?");
        builder1.setCancelable(true);
        builder1.setIcon(R.drawable.aler24);
        builder1.setPositiveButton(
                "Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //dialog.cancel();
                       // GetdataCSV();
                        CreateSMS();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();




    }




    public  void  CreateSMS (){
        String nomcliente = aux_cliente.get(0).NombreCliente.toString().substring(0,10);
        String deuda =aux_cliente.get(0).Deuda;
        String meses = aux_cliente.get(0).Meses;
        final String msj =  "Estimado "+ nomcliente+ " SEDALORETO le informa que tiene una deuda de S/. "+deuda+" soles por " + meses + ", meses,sirvase a pagar a la oficina  por favor.";

        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0 ; i<listNumeros.size();i++){


                    try {


                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(listNumeros.get(i), null, msj, null,null);
                        Log.i("cont mensajes ", String.valueOf(i));
                        Thread.sleep(3000);
                        ActulizarCliente(cod_cliente_update);
                        GetNextCliente(msj);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i("Value",String.valueOf(i));



                }


            }
        }).start();



    }


    public void  ActulizarCliente (String cod) {

        AsyncTask<String,String,String> async ;
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
        listNumeros = new ArrayList<String>();
        String url = "http://daniel88344-001-site1.etempurl.com/";


        try {
            asyncTask = listaClienteTask.execute("1",url);
            clientes = (Cliente[])asyncTask.get();
            if (clientes.length>0){
                for (int i  = 0 ; i<clientes.length; i++){
                    clts = ConvertArrToList(clientes);
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("NextNumber",clts.get(0).Celular);
                editor.putString("NextCod",clts.get(0).codCliente);
                editor.putString("NextMsj",msj);
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





    public  void GetDataFromArray (List<String[]>  var_list ){
            String msjFinal = "";
            String numeroFinal = "";
         for (int i = 1 ; i<var_list.size(); i++){

             String var_strings [] = var_list.get(i);
             String numero = var_strings[8];
              int cantMeses =    Integer.valueOf(var_strings[7]);
               String varDeuda = var_strings[6];
              if (cantMeses ==1){

                  msjFinal ="Sedaloreto le informa que el moto de su recibo es de " + varDeuda + " soles, por favor sirvase a realizar el pago. " ;

              }

             else  if (cantMeses>=2){

                  msjFinal = "Sedaloreto le informa que el monto de su deuda es de " + varDeuda + " soles, por favor sirvase a pagar para evitar el corte de su servicio. ";

              }
             contadorEnvio = i;
             SendSms(numero,msjFinal);

             Log.i("msj final ",msjFinal);
             Log.i("msj final ",numero);
         }

        launchRingDialog(var_list.size());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_sms, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public  void SendSms(  String  number , String sms){


        try{

            SmsManager smsManager =  SmsManager.getDefault();
            smsManager.sendTextMessage(number,null,sms,null,null);
            Toast.makeText(getApplicationContext(),"Enviado",Toast.LENGTH_SHORT).show();

        }

        catch (Exception e){

            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();


        }

    }


    public void launchRingDialog( final int  timelife) {

        final Toast toast = Toast.makeText(context, "Se hizo el envio de mensajes correctamente ", Toast.LENGTH_LONG);
        final ProgressDialog ringProgressDialog = ProgressDialog.show(context, "Espere...",	"Enviando Mensajes...", true);
           final int  final_timelife=1000*timelife/2;
        ringProgressDialog.setCancelable(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Here you should write your time consuming task...
                    // Let the progress ring for 10 seconds...
                    Thread.sleep(final_timelife);
                } catch (Exception e) {

                }


                //Toast.makeText(context,"Se hizo el envio de mensajes correctamente ",Toast.LENGTH_SHORT).show();
               // Intent i = new Intent(getApplicationContext(),MenuOpciones.class);
               // startActivity(i);
                toast.show();
                Volver();

                ringProgressDialog.dismiss();


            }
        }).start();
    }

    public void   Volver (){
        // Toast.makeText(getApplicationContext(),"Se enviaron los mensajes correctamente",Toast.LENGTH_SHORT).show();
         Intent i = new Intent(getApplicationContext(),MenuOpciones.class);
         startActivity(i);


    }


    public void launchBarDialog( int MaxVal ) {
        barProgressDialog = new ProgressDialog(context);

        barProgressDialog.setTitle("Enviando mensaje");
        barProgressDialog.setMessage("Mensajes en progreso ...");
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_HORIZONTAL);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(MaxVal);
        barProgressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    // Here you should write your time consuming task...
                    while (contadorEnvio <= barProgressDialog.getMax()) {

                        Thread.sleep(2000);

                        updateBarHandler.post(new Runnable() {

                            public void run() {

                                barProgressDialog.incrementProgressBy(1);

                            }

                        });

                        if (contadorEnvio == barProgressDialog.getMax()) {

                            barProgressDialog.dismiss();
                            onBackPressed();


                        }
                    }
                } catch (Exception e) {
                }
            }
        }).start();
    }

}
