package modelo;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modelo.Usuario;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-05-08T16:50:14")
@StaticMetamodel(Papel.class)
public class Papel_ { 

    public static volatile SingularAttribute<Papel, Boolean> privSuperAdmin;
    public static volatile SingularAttribute<Papel, Boolean> ativo;
    public static volatile SingularAttribute<Papel, Integer> idPapel;
    public static volatile SingularAttribute<Papel, Boolean> privAdmin;
    public static volatile SingularAttribute<Papel, String> descPapel;
    public static volatile ListAttribute<Papel, Usuario> usuario;

}