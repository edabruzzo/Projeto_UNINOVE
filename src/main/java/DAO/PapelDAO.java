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
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import modelo.Papel;

/**
 *
 * @author Emm
 */
public class PapelDAO implements Serializable {

    private static final long serialVersionUID = 618298451758930316L;

   
      FabricaConexao fabrica = new FabricaConexao();
  
    


    public List<Papel> findPapelEntities() throws ClassNotFoundException, SQLException {
        
        String sql = "SELECT * FROM tb_papel;";
        
        ResultSet rs = fabrica.executaQuerieResultSet(sql);
        
        return this.extrairListPapeisResultSet(rs);

    }


    
   
      public List<Papel> findPapelMenosSuper() throws ClassNotFoundException, SQLException{
          
          String sqlString = "SELECT * FROM tb_papel WHERE PRIV_SUPERADMIN IS NOT TRUE;";
         ResultSet rs = fabrica.executaQuerieResultSet(sqlString);
          
          return this.extrairListPapeisResultSet(rs);
          
          
          
      }
   
   
    public Papel findPapel(int id) throws ClassNotFoundException, SQLException {

        String sql = "SELECT * FROM tb_papel WHERE IDPAPEL = "+id+";";
        
       ResultSet rs = fabrica.executaQuerieResultSet(sql);
       Papel papel = this.extraiPapelResultSet(rs);
       rs.close();
       
       return papel;


    }

public List<Papel> extrairListPapeisResultSet(ResultSet rs) throws SQLException{
    
    List<Papel> listaPapeis = new ArrayList();
    
    while(rs.next()){
        
        Papel papel = this.extraiPapelResultSet(rs);
        listaPapeis.add(papel);
        
    }
    
    return listaPapeis;
    
}

     public Papel extraiPapelResultSet(ResultSet rs) throws SQLException{

         Papel papel = new Papel();
         
         papel.setIdPapel(rs.getInt("IDPAPEL"));
         papel.setAtivo(rs.getBoolean("ATIVO"));
         papel.setDescPapel(rs.getString("DESCPAPEL"));
         papel.setPrivAdmin(rs.getBoolean("PRIVADMIN"));
         papel.setPrivSuperAdmin(rs.getBoolean("PRIV_SUPERADMIN"));
         
         return papel;

     }

    Papel findPapelByUsuario(int idUsuario) throws SQLException, ClassNotFoundException {
        

        String sql = "SELECT * FROM tb_papel_tb_usuario  "
                + "WHERE usuario_IDUSUARIO = "+idUsuario+";";
                
        ResultSet rs = fabrica.executaQuerieResultSet(sql);
        Papel papel = this.extraiPapelResultSet(rs);
        rs.close();
        
        return papel;

    }
    
}
