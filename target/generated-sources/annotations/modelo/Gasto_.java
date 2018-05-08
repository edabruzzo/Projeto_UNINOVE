package modelo;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modelo.Local;
import modelo.Usuario;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-05-08T16:50:14")
@StaticMetamodel(Gasto.class)
public class Gasto_ { 

    public static volatile SingularAttribute<Gasto, Integer> id_gasto;
    public static volatile SingularAttribute<Gasto, String> modalidadePagamento;
    public static volatile SingularAttribute<Gasto, Date> dataGasto;
    public static volatile SingularAttribute<Gasto, String> tipoGasto;
    public static volatile SingularAttribute<Gasto, Double> valorGasto;
    public static volatile SingularAttribute<Gasto, Usuario> usuario;
    public static volatile SingularAttribute<Gasto, Local> local;

}