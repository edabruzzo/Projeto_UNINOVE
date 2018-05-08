/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import DAO.ProjetoDAO;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import modelo.Projeto;

/**
 *
 * @author Emm
 */
@ManagedBean
@SessionScoped
public class ProjetoBean implements Serializable{

    private static final long serialVersionUID = 8173294256756299119L;

    /**
     * Creates a new instance of ProjetoBean
     */
    public ProjetoBean() {
    }
    
    private Projeto projeto = new Projeto();

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }
    
    
    public void criarNovoProjeto() throws SQLException, ClassNotFoundException{
        
        ProjetoDAO projetoDAO = new ProjetoDAO();
        this.projeto.setAtivo(true);
        projetoDAO.create(this.projeto);
        
    }
    
    
    public List<Projeto> listaProjetos() throws ClassNotFoundException, SQLException{
        
    ProjetoDAO projetoDAO = new ProjetoDAO();
    List<Projeto> listaProjetos = new ArrayList();
    
    listaProjetos = projetoDAO.findProjetoEntities();
    return listaProjetos;
        
    }
    
    
    
    
    
    
    
}
