package DatabaseModel;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by Daniel on 24/06/2016.
 */
public class Persona implements KvmSerializable {
    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    String Nombre;
    String  Apellido;

    public Persona(String apellido, String nombre) {
        Apellido = apellido;
        Nombre = nombre;
    }

    @Override
    public Object getProperty(int i) {

        switch (i){
            case  0 :
                return  Nombre;
            case 1 :
                return  Apellido;

        }

        return null;
    }

    @Override
    public int getPropertyCount() {
        return 2;
    }

    @Override
    public void setProperty(int i, Object o) {

        switch (i){

            case  0 :
                Nombre = o.toString();
                break;
            case  1 :
                Apellido  = o.toString();
                break;
            default: break;

        }

    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {


        switch (i){


            case  0 :
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "Nombre";
                break;
            case  1:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name="Apellido";
                break;
            default: break;


        }

    }
}
