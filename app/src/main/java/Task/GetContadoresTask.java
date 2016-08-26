package Task;

import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import DatabaseModel.Cliente;
import Util.Constantes;

/**
 * Created by drvc_ on 26/08/2016.
 */
public class GetContadoresTask extends AsyncTask <String,String,String> {
    String  result;
    @Override
    protected String doInBackground(String... params) {
        String result ="";
        // String urlserver = params[2];
        final String NAMESPACE = Constantes.UrlWebServices;
        final String URL=NAMESPACE+"WSParticipante.asmx";
        final String METHOD_NAME = "GetContadores";
        final String SOAP_ACTION = NAMESPACE+METHOD_NAME;

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        request.addProperty("tipoContador", params[0]);


        SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE transporte = new HttpTransportSE(URL);

        try
        {
            transporte.call(SOAP_ACTION, envelope);

            SoapPrimitive resultado_xml =(SoapPrimitive)envelope.getResponse();
            String res = resultado_xml.toString();

            result=res;


        }
        catch (Exception e)
        {
            result = "";
        }
        Log.i("mensaje", result);
        return result;
    }
}
