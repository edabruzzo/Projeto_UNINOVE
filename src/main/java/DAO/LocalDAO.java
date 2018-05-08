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
import modelo.Projeto;
import modelo.Gasto;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import modelo.Local;

/**
 *
 * @author Emm
 */
public class LocalDAO implements Serializable {

    @Inject
    GastoDAO gastoDAO;

    @Inject
    ProjetoDAO projetoDAO;

    @Inject
    FabricaConexao fabrica;

    public void create(Local local) throws SQLException, ClassNotFoundException {
    
        
        ArrayList<String> listaSQLs = new ArrayList();
        
        String sql1 = "INSERT INTO tb_local(NOME, PROJETO_ID_PROJETO) "
                + "VALUES ('"+local.getNome()
                + "', "+local.getProjeto().getId_projeto()+");";
        
        listaSQLs.add(sql1);
        
        String sql2 = "INSERT INTO tb_projeto_tb_local "
                + "(Projeto_ID_PROJETO, locais_ID_LOCAL) "
                + "VALUES("+local.getProjeto().getId_projeto()+", "
                + " (SELECT last_insert_id() FROM tb_local))";
        
        listaSQLs.add(sql2);
        
        
        for (Gasto gasto : local.getGastos()){
        
         String sql3 = "INSERT INTO tb_local_tb_gasto ((Local_ID_LOCAL, "
                 + "gastos_ID_GASTO) VALUES ((SELECT last_insert_id() FROM tb_local), "
                 + gasto.getId_gasto()+");";
         listaSQLs.add(sql3);
            
        }
        
        
        fabrica.executaBatchUpdate(listaSQLs);
        
        
    }
    
    

    public void edit(Local local) throws ClassNotFoundException, SQLException {
    
        ArrayList<String> listaSQLs = new ArrayList();
        
        String sql = "UPDATE tb_local "
                + "SET NOME = '"+local.getNome()
                + "', PROJETO_ID_PROJETO = "+local.getProjeto().getId_projeto()
                +" WHERE id_local = "+local.getId_local()+";";
    
        
        listaSQLs.add(sql);
        
        String sql2 = "UPDATE tb_projeto_tb_local "
                + "SET Projeto_ID_PROJETO = "+local.getProjeto().getId_projeto()
                +" WHERE id_local = "+local.getId_local()+";";
    
    
        List<Gasto> gastosAtuais = local.getGastos();
        
        
        List<Gasto> gastosVelhos = gastoDAO.listaGastosByLocal(local.getId_local());
        
        for (Gasto gasto : gastosVelhos){
            
            String sql3 = "DELETE FROM tb_local_tb_gasto WHERE gastos_ID_GASTO = "
                    + gasto.getId_gasto()+";";
            listaSQLs.add(sql3);
        }
       
         for (Gasto gasto : gastosAtuais){
            
            String sql4= "INSERT INTO tb_local_tb_gasto (Local_ID_LOCAL, gastos_ID_GASTO) "
                    + "VALUES ("+local.getId_local()+", "+gasto.getId_gasto()+");";
            
            listaSQLs.add(sql4);
        }
         
         Projeto projetoAtual = local.getProjeto();
         String sql5 = "UPDATE tb_projeto_tb_local"
                 + "SET Projeto_ID_PROJETO = "+local.getProjeto().getId_projeto()
                 + "WHERE locais_ID_LOCAL = "+local.getId_local();
             listaSQLs.add(sql5);

        fabrica.executaBatchUpdate(listaSQLs);
    }

    public void destroy(int id) throws SQLException, ClassNotFoundException {

        ArrayList<String> listaSQLs = new ArrayList();
        
        String sql1 = "DELETE FROM tb_local_tb_gasto  WHERE Local_ID_LOCAL  = "+id+";";
        listaSQLs.add(sql1);
               
        String sql2 = "DELETE FROM tb_projeto_tb_local  WHERE locais_ID_LOCAL   = "+id+";";
        listaSQLs.add(sql2);        
        
        String sql3 = "DELETE FROM tb_local WHERE id_local = "+id+";";
        listaSQLs.add(sql3);
        
        fabrica.executaBatchUpdate(listaSQLs);
        
    }

    public List<Local> findLocalEntities() throws SQLException, ClassNotFoundException {

        String sql = "SELECT * FROM tb_local;";
        ResultSet rs = fabrica.executaQuerieResultSet(sql);
        return this.extrairListaLocaisResultSet(rs);
    }

    public Local findLocal(int id) throws ClassNotFoundException, SQLException {

        String sql = "SELECT * FROM tb_local WHERE ID_LOCAL = " + id
                + ";";
        ResultSet rs = fabrica.executaQuerieResultSet(sql);
        Local local = extraiLocalResultSet(rs);
        rs.close();

        return local;
    }

    public Local extraiLocalResultSet(ResultSet rs) throws SQLException, ClassNotFoundException {

        Local local = new Local();
        Projeto projeto = new Projeto();

        local.setId_local(rs.getInt("ID_LOCAL"));
        local.setNome(rs.getString("NOME"));

        local.setGastos(gastoDAO.listaGastosByLocal(rs.getInt("ID_LOCAL")));

        projeto = projetoDAO.findProjeto(rs.getInt("PROJETO_ID_PROJETO "));

        local.setProjeto(projeto);

        return local;
    }

    public List<Local> findLocalByProjeto(int id_projeto) throws ClassNotFoundException, SQLException {

        String sql = "SELECT * FROM tb_local WHERE PROJETO_ID_PROJETO  = " + id_projeto + ";";
        ResultSet rs = fabrica.executaQuerieResultSet(sql);
        return this.extrairListaLocaisResultSet(rs);
    }

    
        public List<Local> extrairListaLocaisResultSet(ResultSet rs) throws SQLException, ClassNotFoundException {

        List<Local> listaLocais = new ArrayList();

        while (rs.next()) {
            listaLocais.add(this.extraiLocalResultSet(rs));
        }
        rs.close();

        return listaLocais;

    }

    
    
}
