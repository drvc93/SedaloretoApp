package DatabaseModel;

/**
 * Created by Daniel on 15/05/2016.
 */
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;


public class Cliente implements KvmSerializable {


    public String codCliente ;
    public   String NombreCliente;
    public  String DescpCortaCalle;
    public  String DescpCalle;
    public  String NumeroCalle;
    public String Urban;
    public  String Deuda;
    public  String Meses;
    public  String Celular;


    public Cliente (){

    }
    public Cliente(String codCliente, String nombreCliente, String descpCortaCalle, String descpCalle, String numeroCalle, String urban, String deuda, String meses, String celular) {
        this.codCliente = codCliente;
        NombreCliente = nombreCliente;
        DescpCortaCalle = descpCortaCalle;
        DescpCalle = descpCalle;
        NumeroCalle = numeroCalle;
        Urban = urban;
        Deuda = deuda;
        Meses = meses;
        Celular = celular;
    }






    @Override
    public Object getProperty(int i) {

        switch (i){

            case  0 :
                return codCliente;
            case 1:
                return  NombreCliente;
            case 2 :
                return DescpCortaCalle;
            case 3:
                return DescpCalle;
            case 4:
                return NumeroCalle;
            case 5:
                return Urban;
            case 6:
                return Deuda;
            case 7 :
                return Meses;
            case 8:
                return Celular;

        }

        return null;
    }

    @Override
    public int getPropertyCount() {
        return 9;
    }

    @Override
    public void setProperty(int i, Object o) {

        switch (i){

            case 0:
                codCliente = o.toString();
                break;
            case 1:
                NombreCliente = o.toString();
                break;
            case 2:
                DescpCortaCalle = o.toString();
                break;
            case 3:
                DescpCalle = o.toString();
                break;
            case 4:
                NumeroCalle= o.toString();
                break;
            case 5:
                Urban = o.toString();
                break;
            case 6:
                Deuda = o.toString();
                break;
            case 7 :
                Meses = o.toString();
                break;
            case 8:
                Celular = o.toString();
                break;
            default:
                break;

        }


    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {


        switch (i){

            case 0:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name="codCliente";
                break;
            case 1:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name="NombreCliente";
                break;
            case 2:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name="DescpCortaCalle";
                break;
            case 3:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name="DescpCalle";
                break;
            case 4:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name="NumeroCalle";
                break;
            case 5:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name="Urban";
                break;
            case 6:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name="Deuda";
                break;
            case 7:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name="Meses";
                break;
            case 8:
                propertyInfo.type= PropertyInfo.STRING_CLASS;
                propertyInfo.name="Celular";
                break;


        }



    }
}
