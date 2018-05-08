/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;


import Util.FabricaConexao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.Local;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import modelo.Projeto;

/**
 *
 * @author Emm
 */
public class ProjetoDAO implements Serializable {

    private static final long serialVersionUID = -6130058936322421816L;

    @Inject
    LocalDAO localDAO;
    
    @Inject
    FabricaConexao fabrica;
    
    
    
    public void create(Projeto projeto) throws SQLException, ClassNotFoundException {
       
        ArrayList<String> listaSQLs = new ArrayList();
        
        String sql1 = "INSERT INTO tb_projeto (ATIVO, NOME, PRIORIDADE, STATUS_PROJETO) "
                + "VALUES ("+projeto.isAtivo()+", '"+projeto.getNome()+"'"
                + ", '"+projeto.getStatus()+"');";
                listaSQLs.add(sql1);
                
         List<Local> listaLocais = projeto.getLocais();
         
         for(Local local : listaLocais){
             
             String sql2 = "INSERT INTO tb_projeto_tb_local (Projeto_ID_PROJETO, locais_ID_LOCAL) "
                     + "VALUES ("+projeto.getId_projeto()+", "+local.getId_local()+");";
             listaSQLs.add(sql2);
                          
         }
        
        fabrica.executaBatchUpdate(listaSQLs);
        
        
    }
        
        
     
    public void edit(Projeto projeto) throws SQLException, ClassNotFoundException  {
        
        
        ArrayList<String> listaSQLs = new ArrayList();
        
        String sql1 = "UPDATE tb_projeto SET ATIVO ="+projeto.isAtivo()+", "
                + "NOME = '"+projeto.getNome()
                + "',  PRIORIDADE = '"+projeto.getPrioridade()
                + "', STATUS_PROJETO = '"+projeto.getStatus()+"' "
                + "WHERE id_projeto = "+projeto.getId_projeto()+";";
        
        listaSQLs.add(sql1);

        List<Local> listaLocaisAtuais = projeto.getLocais();

        String sql2 = "DELETE FROM tb_projeto_tb_local  "
                + "WHERE Projeto_ID_PROJETO  = "+projeto.getId_projeto()+";";
        listaSQLs.add(sql2);
        
        for(Local local : listaLocaisAtuais){
            
            String sql3 = "INSERT INTO tb_projeto_tb_local(Projeto_ID_PROJETO, locais_ID_LOCAL) "
                    + " VALUES("+projeto.getId_projeto()+", "+local.getId_local()+");";
            listaSQLs.add(sql3);
            
        }

        fabrica.executaBatchUpdate(listaSQLs);

    }


    public void destroy(int id) throws SQLException, ClassNotFoundException {
        
        ArrayList<String> listaSQLs = new ArrayList();
        String sql = "DELETE FROM tb_local WHERE PROJETO_ID_PROJETO = "+id+";";
        listaSQLs.add(sql);
        
        String sql2 = "DELETE FROM tb_projeto_tb_local WHERE PROJETO_ID_PROJETO = "+id+";";
        listaSQLs.add(sql2);
        
        String sql3 = "DELETE FROM tb_projeto WHERE ID_PROJETO = "+id+";";
        listaSQLs.add(sql3);
        
        fabrica.executaBatchUpdate(listaSQLs);
        

    }

    public List<Projeto> findProjetoEntities() throws ClassNotFoundException, SQLException {

       
        String sql = "SELECT * FROM tb_projeto;";
        ResultSet rs = fabrica.executaQuerieResultSet(sql);
        return this.extraiListaProjetosResultSet(rs);
        

    }
        
        
    public Projeto findProjeto(int id) throws ClassNotFoundException, SQLException  {

        String sql = "SELECT * FROM tb_projeto WHERE id_projeto = "+id+";";
        ResultSet rs = fabrica.executaQuerieResultSet(sql);
        Projeto projeto = this.extraiProjetoResultSet(rs);
        rs.close();
        
        return projeto;

    }


       public List<Projeto> extraiListaProjetosResultSet(ResultSet rs) throws ClassNotFoundException, SQLException{

           Projeto projeto = new Projeto();
           List<Projeto> listaProjetos = new ArrayList();

           while(rs.next()){
               
           projeto = this.extraiProjetoResultSet(rs);
           listaProjetos.add(projeto);
               
            }
           
           return listaProjetos;
       }
    
    
    
        public Projeto extraiProjetoResultSet(ResultSet rs) throws ClassNotFoundException, SQLException{
            
            Projeto projeto = new Projeto();
            
            projeto.setId_projeto(rs.getInt("ID_PROJETO"));
            projeto.setNome(rs.getString("NOME"));
            projeto.setAtivo(rs.getBoolean("ATIVO"));
            projeto.setPrioridade(rs.getString("PRIORIDADE"));
            projeto.setStatus(rs.getString("STATUS_PROJETO"));
                        
            projeto.setLocais(localDAO.findLocalByProjeto(projeto.getId_projeto()));
            
            return projeto;
        }



    
}
