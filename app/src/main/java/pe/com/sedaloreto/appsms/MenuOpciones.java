package pe.com.sedaloreto.appsms;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import Task.GetContadoresTask;
import Task.ResetEstadoSms;

public class MenuOpciones extends Activity {

    Button btnSMS ,btnListar, btnSalir, btnMsnPerson , btnResetCliente, btnContadorSms;
    String totalReg , faltan ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Menú de opcines | Sedaloreto App");
        setContentView(R.layout.activity_menu_opciones);
        btnSMS = (Button)findViewById(R.id.btnOpcEnvioSMS);
        btnResetCliente = (Button)findViewById(R.id.btnResetCliente);
        btnSalir=(Button)findViewById(R.id.btnOpcCerrarSesion);
        btnListar = (Button)findViewById(R.id.btnOpcLista);
        btnMsnPerson  = (Button)findViewById(R.id.btnMsnPersonalizado);
        btnContadorSms = (Button) findViewById(R.id.btnContadorSms);


        Toast.makeText(getApplicationContext(),"!Bienvedido!", Toast.LENGTH_SHORT).show();

        btnSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), SendSmsActivity.class);
                startActivity(i);

            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /// finishAffinity();
                Process.killProcess(Process.myPid());
            }
        });

        btnListar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ListaClientes.class);
                startActivity(i);
            }
        });

        btnMsnPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i   = new Intent(getApplicationContext(),MensajePersonalizado.class);
                startActivity(i);
            }
        });

        btnResetCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Avisoreset();
            }
        });
        btnContadorSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCometarioCabDialog();
            }
        });

    }

    public  void  Avisoreset (){

        AlertDialog.Builder builder1 = new AlertDialog.Builder(MenuOpciones.this);
        builder1.setMessage("¿Se reiniciara el estado de los clientes , esta seguro?");
        builder1.setCancelable(true);
        builder1.setIcon(R.drawable.aler24);
        builder1.setPositiveButton(
                "Si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ResetClientes();
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

    public  void  ResetClientes(){
        AsyncTask<String,String,String> asyncTask;
        ResetEstadoSms resetEstadoSmstask =  new ResetEstadoSms();
        String result = "";


        try {
            asyncTask = resetEstadoSmstask.execute("1");
            result =  (String)asyncTask.get();

            Toast.makeText(MenuOpciones.this, "Se actulizaron "+result + " registros.", Toast.LENGTH_SHORT).show();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


    public void ShowCometarioCabDialog() {
        GetCOntadores();
        final Dialog dialog = new Dialog(MenuOpciones.this);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.setTitle("Información");


        TextView  txtTotal =(TextView) dialog.findViewById(R.id.txtRegistrados);
        TextView txtFaltan = (TextView) dialog.findViewById(R.id.txtFaltan);
        Button btnSalir = (Button) dialog.findViewById(R.id.btnSalirDialog);
        txtFaltan.setText(faltan);
        txtTotal.setText(totalReg);

       // final String coment = txtDComentario.getText().toString();



        dialog.show();
        //dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.icn_alert);


        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });


    }

    public  void  GetCOntadores () {

        String  res =  "";
        AsyncTask<String,String,String> asyncTaskGetCont;
        GetContadoresTask getContadoresTask = new GetContadoresTask();
        AsyncTask<String,String,String> asyncTaskGetCont2;
        GetContadoresTask getContadoresTask2 = new GetContadoresTask();



        try {
            asyncTaskGetCont =  getContadoresTask.execute("total");
            totalReg = (String) asyncTaskGetCont.get();


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }



        try {
            asyncTaskGetCont2 = getContadoresTask2.execute("falta");
            faltan = (String) asyncTaskGetCont2.get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}
