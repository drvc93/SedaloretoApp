package pe.com.sedaloreto.appsms;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Process;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Handler;

import DatabaseModel.Cliente;
import Task.ActualizarEstadoTask;
import Task.ListaClienteTask;
import Util.Constantes;

public class MensajePersonalizado extends Activity {







    Button  btnConfirm , btnVolver;
    ArrayList<String> listNumeros ;
    EditText txtMensaje  ;
    ArrayList<Cliente> arrClientes;
    String var_numbers;
    int contador =0;
    String cod_cliente_update;
    String sharedName = "MyPrefers";
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje_personalizado);
        setTitle("Mensaje personalizado");
        sharedPreferences = getSharedPreferences(sharedName, MODE_PRIVATE);
        LoadDataCliente();
        btnConfirm = (Button)findViewById(R.id.btnConfirmar);
        btnVolver = (Button) findViewById(R.id.btVolver);
         txtMensaje = (EditText) findViewById(R.id.txtMensaje);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Aviso();
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Volver();
            }
        });
    }

    public  void  Aviso (){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(MensajePersonalizado.this);
        builder1.setMessage("¿Desea enviar   el mensaje?");
        builder1.setCancelable(true);
        builder1.setIcon(R.drawable.aler24);
        builder1.setPositiveButton(
                "Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //dialog.cancel();
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

    public void Volver (){

        super.onBackPressed();
    }

    public void LoadDataCliente (){

        ListaClienteTask  listaClienteTask = new ListaClienteTask();
        AsyncTask<String,String, Cliente[]> asyncTask ;
        Cliente[] clientes;
        listNumeros = new ArrayList<String>();
        String url = Constantes.UrlWebServices;


        try {
            asyncTask = listaClienteTask.execute("1",url);
            clientes = (Cliente[])asyncTask.get();
            if (clientes!=null && clientes.length>0){
            for (int i  = 0 ; i<clientes.length; i++){

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

    public  void  CreateSMS (){
        final String msj =  txtMensaje.getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0 ; i<listNumeros.size();i++){


                    try {


                        SmsManager sms = SmsManager.getDefault();
                        if (msj.length()>140){

                            ArrayList<String>  parts = sms.divideMessage(msj);
                            sms.sendMultipartTextMessage(listNumeros.get(i),null,parts,null,null);


                        }
                        else {
                        sms.sendTextMessage(listNumeros.get(i), null, msj, null,null);
                        }
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

        AsyncTask <String,String,String> async ;
        ActualizarEstadoTask  actualizarEstadoTask = new ActualizarEstadoTask();

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




        ListaClienteTask  listaClienteTask = new ListaClienteTask();
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

    public  void SendSms(  String  number , String mensaje){



        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, mensaje, sentPI, deliveredPI);

       /* try{
           // SmsManager ss = new SmsManager.
            SmsManager smsManager =  SmsManager.getDefault();

            smsManager.sendTextMessage(number, null, sms, null, null);
          //  Toast.makeText(getApplicationContext(), "Enviado", Toast.LENGTH_SHORT).show();

        }

        catch (Exception e){

            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();


        }*/

    }

    private void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
                SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        // ---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        ContentValues values = new ContentValues();

                        getContentResolver().insert(
                                Uri.parse("content://sms/sent"), values);
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        // ---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }


    public ArrayList<Cliente>  ConvertArrToList(Cliente[] clt){

        ArrayList<Cliente> res = new ArrayList<Cliente>();

        for (int i = 0 ; i< clt.length ;i++){

            res.add(clt[i]);


        }

        return  res;
    }



}
