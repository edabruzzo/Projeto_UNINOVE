/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import DAO.UsuarioDAO;
import java.sql.SQLException;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import modelo.Usuario;

/**
 *
 * @author Emm
 */
public class ContextoJSF {

    @Inject
    UsuarioDAO usuarioDAO;

    public void adicionaMensagem(String severidade, String mensagem) {

        switch (severidade) {

            case "sucesso":
                FacesContext.getCurrentInstance().addMessage("mensagens-erro", new FacesMessage(FacesMessage.SEVERITY_INFO, severidade, mensagem));

            case "alerta":
                FacesContext.getCurrentInstance().addMessage("mensagens-erro", new FacesMessage(FacesMessage.SEVERITY_WARN, severidade, mensagem));

            case "erro":
                FacesContext.getCurrentInstance().addMessage("mensagens-erro", new FacesMessage(FacesMessage.SEVERITY_ERROR, severidade, mensagem));

            case "fatal":
                FacesContext.getCurrentInstance().addMessage("mensagens-erro", new FacesMessage(FacesMessage.SEVERITY_FATAL, severidade, mensagem));

            default:
                FacesContext.getCurrentInstance().addMessage("mensagens-erro", new FacesMessage(FacesMessage.SEVERITY_INFO, severidade, mensagem));

        }

    }
    
    
    public void guardarUsuarioMapaSessao(Usuario usuario){
        
        
           FacesContext fc = FacesContext.getCurrentInstance();
            fc.getExternalContext().getSessionMap().put("usuarioLogado", usuario);
            //Não consigo incluir este novo elemento no Map da sessão
            //fc.getExternalContext().getRequestParameterMap().put("login", this.usuario.getLogin());
            //PARA RECUPERAR DEPOIS O USUÁRIO LOGADO BASTA USAR O MÉTODO ABAIXO
            // String loginUsuarioLogado = params.get("j_idt6:login");
            String mensagem = "LOGIN EFETUADO COM SUCESSO ! USUÁRIO LOGADO: ";
            
            this.adicionaMensagem("sucesso", mensagem);
        
    }
    
    
    

    public Usuario verificarUsuarioLogado() throws ClassNotFoundException, SQLException {
    
      
        
              FacesContext fc = FacesContext.getCurrentInstance();
              Map<String, Object> params = fc.getExternalContext().getSessionMap();

           Usuario usuarioLogado = (Usuario) params.get("usuarioLogado");
              
             
              return usuarioLogado;
    
    }

    public void retirarUsuarioMapaSessao() {

              FacesContext fc = FacesContext.getCurrentInstance();
              fc.getExternalContext().getSessionMap().clear();

    }

}

