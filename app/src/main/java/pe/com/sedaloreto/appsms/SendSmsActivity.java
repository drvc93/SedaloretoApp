package pe.com.sedaloreto.appsms;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import  au.com.bytecode.opencsv.CSVReader;




public class SendSmsActivity extends Activity {

    ProgressDialog barProgressDialog;
    int contadorEnvio=0;
    Handler updateBarHandler;
    EditText txtMensaje;

    Context context=this;
    Button  btnActivar ;
    ArrayList<String> arrayListNumero  = new ArrayList<String>();
    ArrayList<String> arrayListTextsms = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        btnActivar = (Button)findViewById(R.id.btnActivar);




        btnActivar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Aviso();
       //       GetdataCSV();
            }
        });
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
                        GetdataCSV();
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
