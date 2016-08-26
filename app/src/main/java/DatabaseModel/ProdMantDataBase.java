package DatabaseModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import Util.Constantes;

/**
 * Created by drvc_ on 26/08/2016.
 */
public class ProdMantDataBase {
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public ProdMantDataBase(Context context) {
        dbHelper = new DBHelper(context);
    }

    private void OpenReadableDB() {
        db = dbHelper.getReadableDatabase();

    }

    private void OpenWritableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void CloseDB() {
        if (db != null) {
            db.close();
        }
    }

    private ContentValues   ClinetesContentValues (Cliente c){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constantes.SED_CLIENTES_COD_CLIENTE , c.getCodCliente());
        contentValues.put(Constantes.SED_CLIENTES_NOM_CLIENTE,c.getNombreCliente());
        contentValues.put(Constantes.SED_CLIETES_DESC_CORTA_CALLE , c.getDescpCortaCalle());
        contentValues.put(Constantes.SED_CLIENTES_DESCP_CALLE,c.getDescpCalle());
        contentValues.put(Constantes.SED_CLIENTES_NRO_CALLE,c.getNumeroCalle());
        contentValues.put(Constantes.SED_CLIENTES_URB,c.getUrban());
        contentValues.put(Constantes.SED_CLIENTES_DEUDA,c.getDeuda());
        contentValues.put(Constantes.SED_CLIENTES_MESES,c.getMeses());
        contentValues.put(Constantes.SED_CLIENTES_CELULAR,c.getCelular());
        contentValues.put(Constantes.SED_CLIENTES_ESTADOSMS,c.getEstadoSms());

        return  contentValues;


    }

    public long InserClientes  (Cliente c) {

        this.OpenWritableDB();
        long rowid = db.insert(Constantes.TABLA_SED_CLIENTES_NAME,null,ClinetesContentValues(c));
        this.CloseDB();
        return  rowid;

    }
}
