package Task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import DatabaseModel.Cliente;
import DatabaseModel.DBHelper;
import DatabaseModel.ProdMantDataBase;
import Util.Constantes;

/**
 * Created by drvc_ on 26/08/2016.
 */
public class SincronizarBDTask extends AsyncTask<Void,Void,Void> {

    ProgressDialog progressDialog;
    Context context ;
    Cliente [] clientes ;
    public SincronizarBDTask(ProgressDialog progressDialog, Context context, Cliente[] clientes) {
        this.progressDialog = progressDialog;
        this.context = context;
        this.clientes = clientes;
    }


    @Override
    protected Void doInBackground(Void... params) {
        ProdMantDataBase db = new ProdMantDataBase(context);
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase dbBase = dbHelper.getWritableDatabase();
        dbBase.execSQL("DELETE FROM " + Constantes.TABLA_SED_CLIENTES_NAME);

        if (clientes != null && clientes.length > 0) {


            for (int i = 0; i < clientes.length; i++) {

                db.InserClientes(clientes[i]);
            }
        }

        return  null;
    }

    @Override
    protected void onPreExecute() {
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        Toast.makeText(context, "Sincronizacioón terminada", Toast.LENGTH_SHORT).show();
    }
}
