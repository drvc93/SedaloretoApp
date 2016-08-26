package Task;

import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import DatabaseModel.Cliente;
import Util.Constantes;

/**
 * Created by Daniel on 15/05/2016.
 */
public class ListaClienteTask extends AsyncTask<String,String, Cliente[]> {

    Cliente[] clientes;

    @Override
    protected Cliente[] doInBackground(String... params) {

        Cliente[] result =  null;
        String urlserver = params[1];
        final String NAMESPACE = Constantes.UrlWebServices;
        final String URL=NAMESPACE+"WSParticipante.asmx";
        final String METHOD_NAME = "ListaClientes";
        final String SOAP_ACTION = NAMESPACE+METHOD_NAME;
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("codAccion", params[0]);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transporte = new HttpTransportSE(URL);
        transporte.debug = true;
        try
        {
            transporte.call(SOAP_ACTION, envelope);

            SoapObject resSoap =(SoapObject)envelope.getResponse();
            Log.i("result Soap ==>",resSoap.toString());
            clientes = new Cliente[resSoap.getPropertyCount()];
            for (int i = 0 ; i<clientes.length; i++){
                SoapObject ic=(SoapObject)resSoap.getProperty(i);
                Cliente cli = new Cliente();

                cli.codCliente = ic.getProperty(0).toString();
                cli.NombreCliente= ic.getProperty(1).toString();
                cli.DescpCortaCalle = ic.getProperty(2).toString();
                cli.DescpCalle = ic.getProperty(3).toString();
                cli.NumeroCalle = ic.getProperty(4).toString();
                cli.Urban = ic.getProperty(5).toString();
                cli.Deuda= ic.getProperty(6).toString();
                cli.Meses= ic.getProperty(7).toString();
                cli.Celular = ic.getProperty(8).toString();
                cli.EstadoSms=ic.getProperty(9).toString();





                clientes[i]=cli;


            }
            if (resSoap.getPropertyCount()>0){
                result=clientes;

            }


        }
        catch (Exception e){

            String B = e.getMessage();
            Log.i("Error AsynctaTask ==>" , e.getMessage());

        }

//        Log.i("Size result array ==>" ,String.valueOf(result.length) );

        return result;




    }
}
