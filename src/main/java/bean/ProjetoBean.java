/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import DAO.ProjetoDAO;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.Projeto;

/**
 *
 * @author Emm
 */
@ManagedBean
@ViewScoped
public class ProjetoBean {

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
    
    
    public void criarNovoProjeto(){
        
        ProjetoDAO projetoDAO = new ProjetoDAO();
        this.projeto.setAtivo(true);
        projetoDAO.create(this.projeto);
        
    }
    
    
    public List<Projeto> listaProjetos(){
        
    ProjetoDAO projetoDAO = new ProjetoDAO();
    List<Projeto> listaProjetos = new ArrayList();
    
    listaProjetos = projetoDAO.findProjetoEntities();
    return listaProjetos;
        
    }
    
    
    
    
    
    
    
}
