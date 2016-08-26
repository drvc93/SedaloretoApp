package Util;

/**
 * Created by drvc_ on 26/08/2016.
 */
public class Constantes {

    public  static  final  String UrlWebServices = "http://drvc2110-001-site1.btempurl.com/";

    public static final String DB_NAME = "Sedaloreto.db";
    public static final int DB_VERSION = 1;

    public static final String TABLA_SED_CLIENTES_NAME = "SED_CLIENTES";
    public static final String  SED_CLIENTES_ID = "_id" ;
    public static final String  SED_CLIENTES_COD_CLIENTE="CodCliente";
    public static final String  SED_CLIENTES_NOM_CLIENTE ="NomCliente";
    public static final String  SED_CLIETES_DESC_CORTA_CALLE = "DescCortaCalle";
    public static final String  SED_CLIENTES_DESCP_CALLE = "DescpCalle";
    public static final String  SED_CLIENTES_NRO_CALLE = "NroCalle";
    public static final String  SED_CLIENTES_URB = "Urbanizacion";
    public static final String  SED_CLIENTES_DEUDA = "Deuda";
    public static final String SED_CLIENTES_MESES = "Meses";
    public static final String SED_CLIENTES_CELULAR ="Celular";
    public static final String SED_CLIENTES_ESTADOSMS = "EstadoSMS";

    public static final String TABLA_SED_CLIENTES_SQL =
            "CREATE TABLE "+ TABLA_SED_CLIENTES_NAME +
                    " ( "+ SED_CLIENTES_ID + "  INTEGER PRIMARY KEY AUTOINCREMENT,  "+
                    SED_CLIENTES_COD_CLIENTE+ " TEXT , " +
                    SED_CLIENTES_NOM_CLIENTE+ " TEXT , " +
                    SED_CLIETES_DESC_CORTA_CALLE+ " TEXT , " +
                    SED_CLIENTES_DESCP_CALLE  + " TEXT , " +
                    SED_CLIENTES_NRO_CALLE  + " TEXT , " +
                    SED_CLIENTES_URB+ " TEXT , " +
                    SED_CLIENTES_DEUDA+ " TEXT , " +
                    SED_CLIENTES_MESES+ " TEXT , " +
                    SED_CLIENTES_CELULAR+ " TEXT , " +
                    SED_CLIENTES_ESTADOSMS+ " TEXT ); " ;


}
