/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.NonexistentEntityException;
import Util.FabricaConexao;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.Papel;
import modelo.Gasto;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import static modelo.Gasto_.usuario;
import modelo.Usuario;

/**
 *
 * @author Emm
 */
public class UsuarioDAO implements Serializable {

  
    @Inject
    private FabricaConexao fabrica;


    public void criarUsuario(Usuario usuario) throws ClassNotFoundException, SQLException {

        
        if (usuario.getGastos() == null) {
            usuario.setGastos(new ArrayList<Gasto>());
        }

            for (Gasto gasto : usuario.getGastos()) {
                
              String  sql = "INSERT INTO tb_tb_usuario_tb_gasto((Usuario_IDUSUARIO, gastos_ID_GASTO)"
                        + "VALUES("+usuario.getIdUsuario()+ ", "+gasto.getId_gasto()+");";
                
             fabrica.executaQuerieUpdate(sql);
            }

            String sql2 = "INSERT INTO tb_usuario (email, LOGIN, NOME, PASSWORD, PAPEL_IDPAPEL)"
                    + "VALUES ('"+usuario.getEmail()+"', '"+usuario.getLogin()+"', "
                    + "'"+usuario.getPassword()+"', "+usuario.getPapel().getIdPapel()+");";
            
            fabrica.executaQuerieUpdate(sql2);
                    
            if (usuario.getPapel() != null) {
                
             String sql3 = "INSERT INTO tb_papel_tb_usuario (Papel_IDPAPEL, usuario_IDUSUARIO)"
                        + "VALUES("+usuario.getPapel().getIdPapel()+", "+usuario.getIdUsuario()+");";
                        
                fabrica.executaQuerieUpdate(sql3);
  
            }
  
            
        }
    

    public void editarUsuario(Usuario usuario) throws NonexistentEntityException, Exception {

            String sql1 = "UPDATE tb_usuario "
                    + "SET email = '"+usuario.getEmail()
                    + "', LOGIN = '"+usuario.getLogin()
                    + "', NOME =  '"+usuario.getNome()
                    + "', PASSWORD = '"+usuario.getPassword()
                    + "', PAPEL_IDPAPEL = "+usuario.getPapel().getIdPapel()
                    + "WHERE id_usuario = "+usuario.getIdUsuario();
            
            fabrica.executaQuerieUpdate(sql1);

            String sql2 = "UPDATE tb_papel_tb_usuario"
                    + "SET Papel_IDPAPEL =  "+usuario.getPapel().getIdPapel()
                    + "WHERE usuario_IDUSUARIO = "+usuario.getIdUsuario();
            
            fabrica.executaQuerieUpdate(sql2);
                
            
    }
                
          
            
    
    public void removerUsuario(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            Papel papel = usuario.getPapel();
            if (papel != null) {
                papel.getUsuario().remove(usuario);
                papel = em.merge(papel);
            }
            List<Gasto> gastos = usuario.getGastos();
            for (Gasto gastosGasto : gastos) {
                gastosGasto.setUsuario(null);
                gastosGasto = em.merge(gastosGasto);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
           
            }
        }
    }

    public List<Usuario> consultaUsuarios() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        List<Usuario> listaUsuario = new ArrayList();
       
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            listaUsuario =  q.getResultList();
        
        if (listaUsuario.isEmpty()){
        
        PapelDAO papelDAO = new PapelDAO();
        Papel papel1 = new Papel();
        papel1.setAtivo(true);
        papel1.setDescPapel("SUPER ADMINISTRADOR");
        papel1.setPrivAdmin(true);
        papel1.setPrivSuperAdmin(true);
        papelDAO.create(papel1);
        
        Papel papel2 = new Papel();
        papel2.setAtivo(true);
        papel2.setDescPapel("ADMINISTRADOR");
        papel2.setPrivAdmin(true);
        papelDAO.create(papel2);
        
        
        Papel papel3 = new Papel();
        papel3.setAtivo(true);
        papel3.setDescPapel("USUÁRIO");
        papelDAO.create(papel3);
        
        
            
        Usuario usuario = new Usuario();
        usuario.setLogin("SUPERADMIN");
        
        CriptografiaSenha criptografar = new CriptografiaSenha();
        String senhaCriptografada = criptografar.convertStringToMd5("SUPERADMIN.123");
        
        usuario.setPassword(senhaCriptografada);
        usuario.setNome("EDITE SEU NOME, LOGIN E SENHA");
        usuario.setEmail("ALTERE SEU E-MAIL");
        usuario.setPapel(papel1);
        criarUsuario(usuario);
            
       }
            
        em.close();
        
        return listaUsuario;
        
        
    }

    public Usuario findUsuario(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
           
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
           
        }
    }
    
    
    public Usuario findByLoginSenha(String login, String senha){
        
        Usuario usuario = new Usuario();
        EntityManager em = getEntityManager();
        List<Usuario> listaUsuarios = new ArrayList();
        listaUsuarios = consultaUsuarios();
        if (listaUsuarios != null || !listaUsuarios.isEmpty()){
         
        String sql = "SELECT * FROM tb_usuario WHERE LOGIN LIKE '"+
        login+"' AND PASSWORD LIKE '"+senha+"'";
        
        Query q = em.createNativeQuery(sql, Usuario.class);
        
     
        try{
        Object o = q.getSingleResult();
        usuario = (Usuario) o;
        }catch(NoResultException e) {
           
            apresentaMensagemErro("validaAcesso", "USUÁRIO NÃO ENCONTRADO");
            e.printStackTrace();
            return null;
            
        }finally{
        
        em.close();
         
        }
     }
    return usuario;
    
    }
    
    
    
    public Usuario findByLogin(String login){
        Usuario usuario = new Usuario();
        EntityManager em = getEntityManager();
        
        String sql = "SELECT * FROM tb_usuario WHERE LOGIN LIKE '"+
                login+"'";
        
        Query q = em.createNativeQuery(sql, Usuario.class);
        
        try{
        Object o = q.getSingleResult();
        usuario = (Usuario) o;
        }catch(NoResultException e) {
           
          String   mensagem = "USUÁRIO NÃO ENCONTRADO"+e.getMessage();
          
          apresentaMensagemErro("solicitaSenha", mensagem);
          return null;
            
        }finally{
        
       em.close();
          
        }
    return usuario;
    
    }
    
    public void apresentaMensagemErro(String idElemento, String mensagemErro){
        LoginFilter lf = new LoginFilter();
        lf.apresentaMensagemErro(idElemento, mensagemErro);
           
    }
    
    
}
