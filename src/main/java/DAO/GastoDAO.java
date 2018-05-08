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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import modelo.Gasto;
import modelo.Local;
import modelo.Usuario;

/**
 *
 * @author Emm
 */
public class GastoDAO implements Serializable {

    @Inject
    FabricaConexao fabrica;
    
    @Inject
    UsuarioDAO usuarioDAO;

    public boolean criarGasto(Gasto gasto) throws ClassNotFoundException, SQLException {

        String sql1 = "INSERT INTO tb_gasto (DATAGASTO, "
                + "MODALIDADEPAGAMENTO, TIPOGASTO, VALORGASTO, "
                + "USUARIO_IDUSUARIO, LOCAL_ID_LOCAL) "
                + "VALUES ('" + gasto.getDataGasto()
                + "', '" + gasto.getModalidadePagamento()
                + "', " + gasto.getTipoGasto()
                + "', " + gasto.getValorGasto()
                + " , " + gasto.getUsuario().getIdUsuario()
                + " , " + gasto.getLocal().getId_local();

        try {
            fabrica.executaQuerieUpdate(sql1);
        } catch (SQLException ex) {
            Logger.getLogger(GastoDAO.class.getName()).log(Level.SEVERE, null, ex);

            return false;
        }

        String sql2 = "INSERT INTO tb_usuario_tb_gasto (Usuario_IDUSUARIO, gastos_ID_GASTO) "
                + "VALUES (" + gasto.getUsuario().getIdUsuario()
                + " , (SELECT last_insert_id() FROM tb_gasto));";

        fabrica.executaQuerieUpdate(sql2);

        String sql3 = "INSERT INTO tb_usuario_tb_gasto (Usuario_IDUSUARIO, gastos_ID_GASTO) "
                + "VALUES (" + gasto.getUsuario().getIdUsuario()
                + " , (SELECT last_insert_id() FROM tb_gasto));";

        fabrica.executaQuerieUpdate(sql3);

        String sql4 = "INSERT INTO tb_local_tb_gasto (Local_ID_LOCAL, gastos_ID_GASTO) "
                + "VALUES ((SELECT last_insert_id() FROM tb_gasto), " + gasto.getLocal().getId_local()
                + " );";

        fabrica.executaQuerieUpdate(sql4);

        return true;

    }



    public void edit(Gasto gasto) throws  SQLException, ClassNotFoundException {

        String sql1 = "UPDATE tb_gasto "
                    + " SET DATAGASTO = '"+gasto.getDataGasto()
                + "', MODALIDADEPAGAMENTO = '"+gasto.getModalidadePagamento()
                + "', TIPOGASTO = '"+gasto.getTipoGasto()
                + "', VALORGASTO = "+gasto.getValorGasto()
                + ", USUARIO_IDUSUARIO = "+gasto.getUsuario().getIdUsuario()
                + ", LOCAL_ID_LOCAL = "+gasto.getLocal().getId_local()
                + "WHERE ID_GASTO = "+gasto.getId_gasto()+";";
        
            fabrica.executaQuerieUpdate(sql1);
            
            String sql2 = "UPDATE tb_usuario_tb_gasto "
                    + " SET USUARIO_IDUSUARIO = "+gasto.getUsuario().getIdUsuario()
                    + " WHERE GASTOS_ID_GASTO = "+gasto.getId_gasto()+" ;";
            
            fabrica.executaQuerieUpdate(sql2);
        
            String sql3 = "UPDATE tb_local_tb_gasto "
                    + " SET Local_ID_LOCAL = "+gasto.getLocal().getId_local()
                    + " WHERE gastos_ID_GASTO = "+gasto.getId_gasto()+";";
            
            fabrica.executaQuerieUpdate(sql3);
        

            }
    
    

    public void destroy(int id) throws ClassNotFoundException, SQLException {
        
        String sql = "DELETE FROM tb_gasto WHERE id_gasto = "+id+";";
        
        fabrica.executaQuerieSemResultado(sql);
        
        String sql2 = "DELETE FROM tb_usuario_tb_gasto WHERE gastos_ID_GASTO = "
                + id + ";";
        
        fabrica.executaQuerieUpdate(sql2);
        
        String sql3 = "DELETE FROM tb_local_tb_gasto WHERE gastos_ID_GASTO = "
                + id + ";";
        
        fabrica.executaQuerieUpdate(sql3);

                
      
       }
        

    public List<Gasto> findGastoEntities() throws ClassNotFoundException, SQLException {
      
      String sql = "SELECT * FROM tb_gasto;";
      
      ResultSet rs = fabrica.executaQuerieResultSet(sql);
      List<Gasto> listaGastos = new ArrayList();
      Gasto gasto = new Gasto();
      Usuario usuario = new Usuario();
      Local  local = new Local();
      while(rs.next()){
          
          gasto.setId_gasto(rs.getInt("id_gasto"));
          gasto.setDataGasto(rs.getDate("DATAGASTO"));
          gasto.setModalidadePagamento(rs.getString("MODALIDADEPAGAMENTO"));
          gasto.setTipoGasto(rs.getString("TIPOGASTO"));
          gasto.setValorGasto(rs.getDouble("VALORGASTO"));
          
          Usuario usuario = rs.getInt("USUARIO_IDUSUARIO");
          ResultSet rs2 = fabrica.executaQuerieResultSet(sql2);
          
          
              
          }
          
          
          
          
         
        }
    }
    
    
    public Gasto findGasto(int id) {
        EntityManager em = getEntityManager();
        



try{
            return em.find(Gasto.class

, id);
        }finally {
           em.close();
        
        }
    }

    public int getGastoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root



<Gasto> rt = cq.from(Gasto.class

);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
         
        }
    }
    
     public Double calculaGastosByLocalUsuarioLogado(Integer localID, Integer idUsuario){
         
         EntityManager em = getEntityManager();
         
          double resultado = 0;
        
           String sqlString = "SELECT sum(g.VALORGASTO) FROM tb_gasto g "
              + " WHERE g.LOCAL_ID_LOCAL = "+localID+" "
                   + " AND USUARIO_IDUSUARIO = "+idUsuario+";";
          Query q = em.createNativeQuery(sqlString);
          
          try{
           resultado = (Double) q.getSingleResult();
          }catch(NullPointerException npe){
               apresentaMensagem();
           }
            
           
           em.close();
        
        return resultado;
           
       }
    
       public double calculaGastosByLocal(Integer localID){
            EntityManager em = getEntityManager();
        double resultado = 0;
        
           String sqlString = "SELECT sum(g.VALORGASTO) FROM tb_gasto g "
              + " WHERE g.LOCAL_ID_LOCAL = "+localID+";";
            Query q = em.createNativeQuery(sqlString);
           try{
               resultado = (Double) q.getSingleResult();
           }catch(NullPointerException npe){
               apresentaMensagem();
           }
            
           em.close();
        
        return resultado;
           
       }
       
       
               
      public double calculaGastosBySQL(String sql){
              EntityManager em = getEntityManager();
        double resultado = 0;
        
         String sqlString = sql.replace("*", "SUM(VALORGASTO)");
         String sqlString2 = sqlString.replace("G.SUM(VALORGASTO)", "SUM(VALORGASTO)");
          Query q = em.createNativeQuery(sqlString2);
          try{
           resultado = (Double) q.getSingleResult();
          //aqui pode lançar uma NullPointerException 
          //aprender como pegar dois tipos de exceções
          }catch (PersistenceException pe) {
              apresentaMensagemPersistence(pe.getMessage());
              
          }
           
           em.close();
        
        return resultado;
           
        }
       
       
       public List<Gasto> listaGastosByConsultaSQL(){
        EntityManager em = getEntityManager();
           String sqlString = "SELECT * FROM tb_gasto ORDER BY ID_GASTO DESC;";
          Query 



q = em.createNativeQuery(sqlString, Gasto.class

);
           List<Gasto> listaGasto = new ArrayList();
          try{
         
          listaGasto = q.getResultList();

          }catch (PersistenceException pe) {
              apresentaMensagemPersistence(pe.getMessage());
         // }catch(NullPointerException npex){
           //   apresentaMensagem();
         // }
           
          }
           em.close();
             
           return listaGasto;
       }
       
       
       
       
       
        public List<Gasto> listaGastosByLocal(Integer localID){
        EntityManager em = getEntityManager();
           String sqlString = "SELECT g.* FROM tb_gasto g "
              + " WHERE g.LOCAL_ID_LOCAL = "+localID+" ORDER BY DATAGASTO DESC;";
          Query 



q = em.createNativeQuery(sqlString, Gasto.class

);
          try{
           return (List<Gasto>) q.getResultList();
         // }catch(NullPointerException npex){
           //   apresentaMensagem();
         // }
           
          }finally{
           em.close();
              
          } 
           
       }
       
       
       
        public List<Gasto> listaGastosByUsuario(Integer idUsuario){
        EntityManager em = getEntityManager();
           String sqlString = "SELECT g.* FROM tb_gasto g "
              + " WHERE g.USUARIO_IDUSUARIO = "+idUsuario+" ORDER BY DATAGASTO DESC;";
          Query 



q = em.createNativeQuery(sqlString, Gasto.class

);
          List<Gasto> listaGastos = new ArrayList();
          try{
          listaGastos = (List<Gasto>) q.getResultList();
         }catch(NullPointerException npex){
            apresentaMensagem();
         }
          em.close();
          return listaGastos;
          
              
           
           
       }
        
         
        public List<Gasto> listaGastosByUsuarioLogado(Integer idUsuario){
        EntityManager em = getEntityManager();
           String sqlString = "SELECT g.* FROM tb_gasto g "
              + " WHERE g.USUARIO_IDUSUARIO = "+idUsuario+" ORDER BY DATAGASTO DESC;";
          Query 



q = em.createNativeQuery(sqlString, Gasto.class

);
          try{
           return (List<Gasto>) q.getResultList();
         // }catch(NullPointerException npex){
           //   apresentaMensagem();
         // }
           
          }finally{
           em.close();
              
          } 
           
       }
        
       public double calculaGastosByUsuario(Integer usuarioID){
            EntityManager em = getEntityManager();
        double resultado = 0;
        
           String sqlString = "SELECT sum(g.VALORGASTO) FROM tb_gasto g "
              + " WHERE g.USUARIO_IDUSUARIO = "+usuarioID+";";
          Query q = em.createNativeQuery(sqlString);
          try{
           resultado = (Double) q.getSingleResult();
           
          }catch(NullPointerException npex){
              apresentaMensagem();
          }
           em.close();
        
        return resultado;
       }
       
       
         public List<Gasto> listaGastosByProjetoUsuarioLogado(Integer projetoID, Integer idUsuario){
              EntityManager em = getEntityManager();
        List<Gasto> listaGastos = new ArrayList();
        
           String sqlString = "SELECT g.* FROM tb_gasto g "
           + "INNER JOIN tb_projeto_tb_local pl ON g.LOCAL_ID_LOCAL = pl.locais_ID_LOCAL "
           + "INNER JOIN tb_projeto p ON pl.Projeto_ID_PROJETO = p.ID_PROJETO "        
              + " WHERE p.ID_PROJETO = "+projetoID+" "
                   + " AND g.USUARIO_IDUSUARIO = "+idUsuario+" ORDER BY DATAGASTO DESC;";
          Query q = em.createNativeQuery(sqlString);
          try{
           listaGastos  = (List<Gasto>) q.getResultList();
              
          }catch(NullPointerException npex){
              apresentaMensagem();
          }
           em.close();
        
        return listaGastos;
           
        }
       
        public List<Gasto> listaGastosByProjeto(Integer projetoID){
              EntityManager em = getEntityManager();
           String sqlString = "SELECT g.* FROM tb_gasto g "
           + "INNER JOIN tb_projeto_tb_local pl ON g.LOCAL_ID_LOCAL = pl.locais_ID_LOCAL "
           + "INNER JOIN tb_projeto p ON pl.Projeto_ID_PROJETO = p.ID_PROJETO "        
              + " WHERE p.ID_PROJETO = "+projetoID+";";                   
          Query 



q = em.createNativeQuery(sqlString, Gasto.class

);
          try{
           return (List<Gasto>) q.getResultList();
            }finally{
           em.close();
          }
    
           
        }
       
       
       
       public double calculaGastosByProjeto(Integer projetoID){
              EntityManager em = getEntityManager();
        double resultado = 0;
        
           String sqlString = "SELECT sum(g.VALORGASTO) FROM tb_gasto g "
           + "INNER JOIN tb_projeto_tb_local pl ON g.LOCAL_ID_LOCAL = pl.locais_ID_LOCAL "
           + "INNER JOIN tb_projeto p ON pl.Projeto_ID_PROJETO = p.ID_PROJETO "        
              + " WHERE p.ID_PROJETO = "+projetoID+";";
          Query q = em.createNativeQuery(sqlString);
          try{
           resultado = (Double) q.getSingleResult();
            }catch (java.lang.NullPointerException npex) {
        
        apresentaMensagem();
          }
           
           em.close();
        
        return resultado;
           
        }
       
       
              public double calculaGastosByProjetoUsuarioLogado(Integer projetoID, Integer idUsuario){
              EntityManager em = getEntityManager();
        double resultado = 0;
        
           String sqlString = "SELECT sum(g.VALORGASTO) FROM tb_gasto g "
           + "INNER JOIN tb_projeto_tb_local pl ON g.LOCAL_ID_LOCAL = pl.locais_ID_LOCAL "
           + "INNER JOIN tb_projeto p ON pl.Projeto_ID_PROJETO = p.ID_PROJETO "        
              + " WHERE p.ID_PROJETO = "+projetoID+" "
                   + " AND g.USUARIO_IDUSUARIO = "+idUsuario+";";
          Query q = em.createNativeQuery(sqlString);
          try{
              resultado = (Double) q.getSingleResult();
           
          }catch (java.lang.NullPointerException npex) {
        
    
       apresentaMensagem();
          }
           em.close();
        
        return resultado;
           
        }
    
       
         public List<Gasto> listaGastosByMesUsuarioLogado(Integer month, Integer year, Integer idUsuario){
        
        EntityManager em = getEntityManager();
        List<Gasto> listaGastos = new ArrayList();
                  
                  
        String sqlString = "SELECT g.* FROM tb_gasto g "
                + "WHERE USUARIO_IDUSUARIO = "+idUsuario
              + " AND g.DATAGASTO BETWEEN '"+year+"-"+month+"-01' AND '"+year+"-"+month+"-31' "
                + "ORDER BY g.DATAGASTO DESC;";
          Query 



q = em.createNativeQuery(sqlString, Gasto.class

);
          try{
                     listaGastos = (List<Gasto>) q.getResultList();
          }catch (NullPointerException npex) {
        
      
       apresentaMensagem();
          }

           em.close();
           
        return listaGastos;
    }
    
       
    
    
    public List<Gasto> listaGastosByMes(Integer month, Integer year) {
        
        EntityManager em = getEntityManager();
        List<Gasto> listaGastos = new ArrayList();
                  
        String sqlString = "SELECT g.* FROM tb_gasto g "
              + " WHERE g.DATAGASTO BETWEEN '"+year+"-"+month+"-01' AND '"+year+"-"+month+"-31' "
                + " ORDER BY DATAGASTO DESC;";
          Query 



q = em.createNativeQuery(sqlString, Gasto.class

);
         
          try{
              q.getResultList();
          listaGastos = (List<Gasto>) q.getResultList();
          
        }catch (java.lang.NullPointerException npex) {
        
     
       apresentaMensagem();
        
        }
          
           em.close();
           
        return listaGastos;
    }
    
    
    
    
    
    
       public double calculaGastosMensais(Integer month, Integer year) {
        
        EntityManager em = getEntityManager();
        double resultado = 0;
                  
        String sqlString = "SELECT sum(g.VALORGASTO) FROM tb_gasto g "
              + " WHERE g.DATAGASTO BETWEEN '"+year+"-"+month+"-01' AND '"+year+"-"+month+"-31';";
          Query q = em.createNativeQuery(sqlString);
          try{
               resultado = (Double) q.getSingleResult();
          }catch(NullPointerException npex){
             
              apresentaMensagem();

        }
          
        em.close();
           
        return resultado;
    }
       
       
        public double calculaGastosTotais() {
        
        EntityManager em = getEntityManager();
        double resultado = 0;
                  
        String sqlString = "SELECT sum(g.VALORGASTO) FROM tb_gasto g ;";
         
          Query q = em.createNativeQuery(sqlString);
          try{
               resultado = (Double) q.getSingleResult();
          }catch(NullPointerException npex){
              
        
          apresentaMensagem();
        }
          
        em.close();
           
        return resultado;
    }
        
        
         public double calculaGastosTotaisUsuarioLogado(Integer idUsuario) {
        
        EntityManager em = getEntityManager();
        double resultado = 0;
                  
        String sqlString = "SELECT sum(g.VALORGASTO) FROM tb_gasto g WHERE "
                + " USUARIO_IDUSUARIO = "+idUsuario+";";
         
          Query q = em.createNativeQuery(sqlString);
          try{
               resultado = (Double) q.getSingleResult();
          }catch(NullPointerException npex){
              
                      apresentaMensagem();
        }
          
        em.close();
           
        return resultado;
    }
       
       
            public List<Gasto> listaGastosByLocalUsuarioLogado(Integer localID, Integer idUsuario){
                EntityManager em = getEntityManager();
                List<Gasto> listaGastos = new ArrayList();
                
                
        String sqlString = null;
        sqlString = "SELECT g.* FROM tb_gasto g "
                + "WHERE LOCAL_ID_LOCAL = "+localID+" AND USUARIO_IDUSUARIO = "
                + idUsuario+" ORDER BY DATAGASTO DESC;";
           Query q = em.createNativeQuery(sqlString);
           try{
           
           listaGastos = (List<Gasto>) q.getResultList();
           }catch(NullPointerException npex){
             apresentaMensagem();
          }
           
           em.close();
         
           return listaGastos;
        
            }
       
            
            
            public double calculaGastosMensaisUsuarioLogado(Integer month, Integer year, Integer idUsuario){
                EntityManager em = getEntityManager();
                double resultado = 0;
             
                
        String sqlString = null;
        sqlString = "SELECT sum(g.VALORGASTO) FROM tb_gasto g WHERE "
                + " DATAGASTO BETWEEN '"+year+"-"+month+"-01' AND '"+year+"-"+month+"-31' "
                + " AND g.USUARIO_IDUSUARIO = "+idUsuario+";";
          Query q = em.createNativeQuery(sqlString);
          try{
              resultado = (Double) q.getSingleResult();
          }catch(NullPointerException npex){
              
              apresentaMensagem();
          }
           
           em.close();
         
           return resultado;
        
            }
          public void apresentaMensagem(){
              GastoBean gb = new GastoBean();
              String mensagem = "NÃO FOI POSSÍVEL ENCONTRAR GASTOS PARA O CRITÉRIO INFORMADO";
              gb.mostraMensagemErro(mensagem);
          }
          
           public void apresentaMensagemPersistence(String causa){
              GastoBean gb = new GastoBean();
              String mensagem = " NA BOA, VOCÊ DISSE QUE SABIA O QUE TAVA FAZENDO!!!"
                      + ""
                      + " OU SUA QUERY TÁ MAL FORMADA OU VOCÊ "
                      + " MONTOU ELA EM MINÚSCULA. NÃO FALEI PARA USAR MAIÚSCULAS ?"
                      + " DICA: BASTA QUE G.* SEJA MAIÚSCULA "
                      + " "
                      + ""
                      + ""
                      + ""
                      + ""
                      + ""
                      + "                            ***********************************        "
                      + "                            ************************************                                           "
                      + "                            DÁ UMA OLHADA AÍ NA CAUSA E VÊ SE FAZ DIREITO DA PRÓXIMA VEZ :                           "
                      + "                           ******************************************************   "
                      + "                                                                                    "
                      + "                                                                                    "
                      + "                                                                                    "+causa;
              gb.mostraMensagemErro(mensagem);
          }
    
}
