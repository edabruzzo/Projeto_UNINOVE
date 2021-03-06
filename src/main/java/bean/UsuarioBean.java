/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import DAO.PapelDAO;
import DAO.UsuarioDAO;
import Util.CriptografiaSenha;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import modelo.Papel;
import modelo.Usuario;

/**
 *
 * @author Emm
 */

@ManagedBean
@javax.faces.bean.SessionScoped
public class UsuarioBean implements Serializable{

    private static final long serialVersionUID = -7444696162507993250L;

    /**
     * Creates a new instance of UsuarioBean
     */
    public UsuarioBean() {
    }
    
    private Usuario usuario = new Usuario();
    private Integer idPapel;
    private Papel papel = new Papel();
    private boolean canEdit = false;

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public Papel getPapel() {
        return papel;
    }

    public void setPapel(Papel papel) {
        this.papel = papel;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getIdPapel() {
        return idPapel;
    }

    public void setIdPapel(Integer idPapel) {
        this.idPapel = idPapel;
    }
    
    
    public List<Papel> listaPapeis() throws ClassNotFoundException, SQLException{
       
        boolean possuiPrivilegio = false;
        boolean possuiPrivilegioSuper = false;
        LoginFilter lf = new LoginFilter();
        possuiPrivilegio = lf.verificaPrivilegio();
        possuiPrivilegioSuper = lf.verificaPrivilegioSuperAdmin();
        
         PapelDAO papelDAO = new PapelDAO();
        
        if(possuiPrivilegio & !possuiPrivilegioSuper){
       
      
       return papelDAO.findPapelMenosSuper();
        
    }else if(possuiPrivilegioSuper){
        
       return papelDAO.findPapelEntities();
    }else {
        return null;
    }
        
    }
    

    
    public void criarNovoUsuario() throws Exception{
        
         boolean possuiPrivilegio = false;
        LoginFilter lf = new LoginFilter();
        possuiPrivilegio = lf.verificaPrivilegio();
        
        if(possuiPrivilegio){
            
        PapelDAO papelDAO = new PapelDAO();
        this.papel = papelDAO.findPapel(this.idPapel); 
        this.usuario.setPapel(this.papel);
            
         UsuarioDAO usuarioDAO = new UsuarioDAO();
         CriptografiaSenha criptoSenha = new CriptografiaSenha();
         String senhaCriptografada = criptoSenha.convertStringToMd5(this.usuario.getPassword());
         this.usuario.setPassword(senhaCriptografada);
         if(usuarioDAO.findByLogin(this.usuario.getLogin()) == null){
                 usuarioDAO.criarUsuario(this.usuario);
                 this.usuario =  new Usuario();
         }else {
                      usuarioDAO.editarUsuario(this.usuario);
                      this.usuario =  new Usuario();
                      this.canEdit = false;

         }
            
            
            
        }else return;
 
         
    }
    
    
    public List<Usuario> listaUsuarios() throws ClassNotFoundException, SQLException{
         UsuarioDAO usuarioDAO = new UsuarioDAO();
         return usuarioDAO.consultaUsuarios();
    }
    
    public void deletaUsuario(Usuario usuario) throws SQLException, ClassNotFoundException {
         UsuarioDAO usuarioDAO = new UsuarioDAO();
         usuarioDAO.removerUsuario(usuario.getIdUsuario());
        
    }
    
    public void editaUsuario(){
        
        boolean possuiPrivilegio = false;
        LoginFilter lf = new LoginFilter();
        possuiPrivilegio = lf.verificaPrivilegio();
        if(possuiPrivilegio){
            this.canEdit = true;
        }else return;
    
    }
    
    
    public void cancelarEdicao(){
        
           this.canEdit = false;
           this.usuario = new Usuario();
        
    
    }
        
        
        /*
        http://respostas.guj.com.br/20826-problema-ao-tentar-setpropertyactionlistener-do-jsf-para-editar-datagrid
        
        O que eu vou te falar é muito sério, mais detalhes, só com consultoria Nunca use setPropertyActionListener

<h:commandLink value="Alterar" update=":formCliente" immediate="true">
                        <f:setPropertyActionListener value="#{cliente}"
                            target="#{clienteBean.cliente}" />
                    </h:commandLink>
faça assim

<p:commandLink value="Alterar" update=":formCliente" immediate="true" action="#{clienteBean.cliente(cliente)}"/>
        
no seu objeto faça o método que recebe o objeto cliente*/
     public void salvarUsuarioEditado() throws Exception{
        
        boolean possuiPrivilegio = false;
        LoginFilter lf = new LoginFilter();
        possuiPrivilegio = lf.verificaPrivilegio();
        this.canEdit = false;
        
        if(possuiPrivilegio & this.usuario != null){
                 
                    criarNovoUsuario();
        
        }else {
            return;
        }
        
        this.usuario = new Usuario();
        this.canEdit = false;
    }
    
    
    
}
