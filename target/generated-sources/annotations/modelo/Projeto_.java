package modelo;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import modelo.Local;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-05-08T16:50:14")
@StaticMetamodel(Projeto.class)
public class Projeto_ { 

    public static volatile SingularAttribute<Projeto, Boolean> ativo;
    public static volatile SingularAttribute<Projeto, String> prioridade;
    public static volatile SingularAttribute<Projeto, Integer> id_projeto;
    public static volatile SingularAttribute<Projeto, String> nome;
    public static volatile ListAttribute<Projeto, Local> locais;
    public static volatile SingularAttribute<Projeto, String> status;

}