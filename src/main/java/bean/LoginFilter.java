/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;



import DAO.UsuarioDAO;
import Util.ContextoJSF;
import Util.CriptografiaSenha;
import java.io.IOException;
import java.sql.SQLException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.Filter;
  import javax.servlet.FilterChain;
  import javax.servlet.FilterConfig;
  import javax.servlet.ServletException;
  import javax.servlet.ServletRequest;
  import javax.servlet.ServletResponse;
  import javax.servlet.http.HttpServletRequest;
  import javax.servlet.http.HttpServletResponse;
  import javax.servlet.http.HttpSession;
  import modelo.Usuario;



/**
 *
 * @author Emm
 */
@ManagedBean
@SessionScoped
public class LoginFilter implements Filter {
    
private static final long serialVersionUID = 1L;

private  boolean permiteAcesso = false;

  private static Usuario usuario = new Usuario();
  
@Inject  
ContextoJSF contextoJSF;

    public boolean isPermiteAcesso() {
        return permiteAcesso;
    }

    public void setPermiteAcesso(boolean permiteAcesso) {
        this.permiteAcesso = permiteAcesso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    
    
    
        public String validaAcesso() throws ClassNotFoundException, SQLException{
            
           String redireciona = "login?faces-redirect=true";
           
            
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario novoUsuario = new Usuario();
         
            //faz a criptografia da senha entrada pelo usuário antes de 
           //gravar no banco
            CriptografiaSenha criptoSenha = new CriptografiaSenha();
            String senhaCriptografada = criptoSenha.convertStringToMd5(this.usuario.getPassword());
            this.usuario.setPassword(senhaCriptografada);
            novoUsuario = usuarioDAO.findByLoginSenha(this.usuario.getLogin(), this.usuario.getPassword());
 
            if(novoUsuario == null){
                    
                String mensagem = "O PROCESSO DE LOGIN FALHOU ! USUÁRIO INEXISTENTE OU SENHA INCORRETA !";
                contextoJSF.adicionaMensagem("fatal", mensagem);
                
            }else{
           
              contextoJSF.guardarUsuarioMapaSessao(this.usuario);
              this.usuario = novoUsuario;
              this.permiteAcesso = true;
                redireciona = "/restricted/gastos?faces-redirect=true";
            }   
                
             return redireciona;
            
        }
        
        
         public boolean verificaPapel() throws ClassNotFoundException, SQLException{
            
             boolean permitido = false;
             Usuario usuarioLogado = contextoJSF.verificarUsuarioLogado();
             
             if (usuarioLogado != null & usuarioLogado.getPapel().isPrivAdmin()){
             permitido = true;
             }else{
                 
                 contextoJSF.adicionaMensagem("erro", "Você não possui privilégio");  
             }
             return permitido;
         }
         
         
         public boolean verificaPrivilegioSuperAdmin() throws ClassNotFoundException, SQLException{
             
             boolean possuiPrivilegioSuperAdmin = false;

            Usuario usuarioLogado = contextoJSF.verificarUsuarioLogado();

             if (usuarioLogado != null & usuarioLogado.getPapel().isPrivSuperAdmin()){
             possuiPrivilegioSuperAdmin = true;
             }else{
                 
                 contextoJSF.adicionaMensagem("erro", "Você não possui privilégio");  
             }
             return possuiPrivilegioSuperAdmin;
         }
      
         
         
         public String redirecionaUsuários() throws ClassNotFoundException, SQLException{
             String redireciona = null;
             boolean permitidoRedirecionamento = verificaPapel();
             if(permitidoRedirecionamento){
                 redireciona = "/restricted/usuarios?faces-redirect=true";
             }
             return redireciona;
         }
         
         
         
             public String redirecionaGráficos() throws ClassNotFoundException, SQLException{
             String redireciona = null;
             boolean permitidoRedirecionamento = verificaPapel();
             if(permitidoRedirecionamento){
                 redireciona = "/restricted/graficos?faces-redirect=true";
             }
             return redireciona;
         }
         
         
         
                  
         public String redirecionaPesquisas() throws ClassNotFoundException, SQLException{
             String redireciona = null;
             boolean permitidoRedirecionamento = verificaPapel();
             if(permitidoRedirecionamento){
                 redireciona = "/restricted/pesquisas?faces-redirect=true";
             }
             return redireciona;
         }
         
         
           public String redirecionaLocais() throws ClassNotFoundException, SQLException{
             String redireciona = null;
             boolean permitidoRedirecionamento = verificaPapel();
             if(permitidoRedirecionamento){
                 redireciona = "/restricted/locais?faces-redirect=true";
             }
             return redireciona;
         }
           
            public String redirecionaProjetos() throws ClassNotFoundException, SQLException{
             String redireciona = null;
             boolean permitidoRedirecionamento = verificaPapel();
             if(permitidoRedirecionamento){
                 redireciona = "/restricted/projetos?faces-redirect=true";
             }
             return redireciona;
         }
         
            
            
         
          public String logout(){
            
         
             this.permiteAcesso = false;
             String redireciona = "login?faces-redirect=true";
             this.usuario = new Usuario();
             contextoJSF.retirarUsuarioMapaSessao();
             return redireciona;
         }
          
          
          public boolean verificaPrivilegio(){
              
           boolean possuiPrivilegio = false;
         
                     
              if(this.usuario.getPapel().isPrivAdmin()){
                  possuiPrivilegio = true;
              }
              return possuiPrivilegio;
         }
          
          
            public boolean verificaUsuarioLogado() throws SQLException, ClassNotFoundException{

            Usuario usuario = contextoJSF.verificarUsuarioLogado();   
           
             if (this.usuario.getNome() != null){
                    return true;
             }
             else return false;
            
            }
            
               public void solicitarNovaSenha() throws Exception {
       
             
             UsuarioDAO usuarioDAO = new UsuarioDAO();
             Usuario usuarioSemSenha = usuarioDAO.findByLogin(this.usuario.getLogin());
             if(usuarioSemSenha != null){
                 
             CriptografiaSenha criptoSenha = new CriptografiaSenha();
             criptoSenha.gerarNovaSenha(usuarioSemSenha);
             
            String mensagem = "                                            "
                    + "                                                         "
                    + "                                       ************************"
                    + "************************************************************"
                    + "************************************************************"
                    + "                                                              "
                    + "                                                             "
                    + "NOVA SENHA ENVIADA PARA O EMAIL :"+usuarioSemSenha.getEmail()+"            "
                            + "                                                       "
                            + "                                                          "
                            + "                                                          "
                            + "                                                          "
                            + "***********************************************************"
                            + "***********************************************************"
                            + "***********************************************************";    
            contextoJSF.adicionaMensagem("alerta", mensagem);

             }
            
             
         }
      
    
    
           @Override
           public void destroy() {
                     // TODO Auto-generated method stub
   
           }
           
           
           
           /*
           
           https://stackoverflow.com/questions/1026846/how-to-redirect-to-login-page-when-session-is-expired-in-java-web-application
           PARA MELHORAR O PROCESSO DE LOGIN
           
           
           */
           
           
   
           @Override
           public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
               
               HttpSession sess = ((HttpServletRequest) request).getSession(false);
               
              if(sess != null)  { 
                     if (permiteAcesso == false) {
                    
                    String contextPath = ((HttpServletRequest) request).getContextPath();
                              
                    ((HttpServletResponse) response).sendRedirect(contextPath + "login.xhtml?faces-redirect=true");
                     }
                     
                    else {
                              chain.doFilter(request, response);
                     }
                }
           }
   
           @Override
           public void init(FilterConfig arg0) throws ServletException {
                     // TODO Auto-generated method stub
   
           }
           
           
          
   
  }
