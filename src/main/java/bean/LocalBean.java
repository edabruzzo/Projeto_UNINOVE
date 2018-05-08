/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import DAO.LocalDAO;
import DAO.ProjetoDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import modelo.Local;
import modelo.Projeto;

/**
 *
 * @author Emm
 */
@ManagedBean
@ViewScoped
public class LocalBean {

    /**
     * Creates a new instance of LocalBean
     */
    public LocalBean() {
    }
    
    private int projetoID;
    private Projeto projeto = new Projeto();
    private Local local = new Local();

    public Local getLocal() {
        return local;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }
    

    public int getProjetoID() {
        return projetoID;
    }

    public void setProjetoID(int projetoID) {
        this.projetoID = projetoID;
    }
    
    
    public List<Projeto> listaProjetos() throws ClassNotFoundException, SQLException{
        
        ProjetoDAO projetoDAO = new ProjetoDAO();
         List<Projeto> listaProjetos = new ArrayList();
         listaProjetos = projetoDAO.findProjetoEntities();
         return listaProjetos;
        
    }
    
    public void gravaProjetoNoLocal() throws ClassNotFoundException, SQLException{
         ProjetoDAO projetoDAO = new ProjetoDAO();
         this.projeto = projetoDAO.findProjeto(projetoID);
         this.local.setProjeto(this.projeto);
    }
    
    
    public void criaNovoLocal() throws SQLException, ClassNotFoundException{
        
        LocalDAO localDAO = new LocalDAO();
        localDAO.create(this.local);
        
    }
    
    
    public List<Local> listaLocais() throws SQLException, ClassNotFoundException{
          LocalDAO localDAO = new LocalDAO();
        List<Local> listaLocais = new ArrayList();  
        listaLocais = localDAO.findLocalEntities();
        return listaLocais;
    }
   
    
    
}
