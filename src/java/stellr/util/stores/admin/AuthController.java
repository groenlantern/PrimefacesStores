/* 
 * $Id: AuthController.java 320 2017-04-07 13:08:18Z jp $ 
 * $Author: jp $
 * $Date: 2017-04-07 15:08:18 +0200 (Fri, 07 Apr 2017) $
 * 
 * $Rev: 320 $
 * 
 * $LastChangedBy: jp $ 
 * $LastChangedRevision: 320 $
 * $LastChangedDate: 2017-04-07 15:08:18 +0200 (Fri, 07 Apr 2017) $
 * 
 * $URL: svn://10.100.0.35/stellrrepo/stellr170/StoresAdminConsole/src/java/stellr/util/stores/admin/AuthController.java $
 * 
 * (C) 2016 Stellr  All Rights Reserved 
 * 
 */

package stellr.util.stores.admin;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author stu
 */
@ManagedBean (name = "authController")
@RequestScoped
public class AuthController {
    private static Logger log = Logger.getLogger(AuthController.class.getName());
  
  public String logout() {
    String result="/index?faces-redirect=true";
    
    FacesContext context = FacesContext.getCurrentInstance();
    HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
    
    try {
      request.logout();
    } catch (ServletException e) {
      log.log(Level.WARNING, "Failed to logout user!", e);
      result = "/loginError?faces-redirect=true";
    }
    
    return result;
  }
}


