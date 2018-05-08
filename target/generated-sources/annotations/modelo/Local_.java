package modelo;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modelo.Gasto;
import modelo.Projeto;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-05-08T16:50:14")
@StaticMetamodel(Local.class)
public class Local_ { 

    public static volatile SingularAttribute<Local, Projeto> projeto;
    public static volatile SingularAttribute<Local, String> nome;
    public static volatile SingularAttribute<Local, Integer> id_local;
    public static volatile ListAttribute<Local, Gasto> gastos;

}