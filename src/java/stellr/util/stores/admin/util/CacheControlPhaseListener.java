/* 
 * $Id: CacheControlPhaseListener.java 182 2016-12-07 09:52:16Z tungsten $ 
 * $Author: tungsten $
 * $Date: 2016-12-07 11:52:16 +0200 (Wed, 07 Dec 2016) $
 * 
 * $Rev: 182 $
 * 
 * $LastChangedBy: tungsten $ 
 * $LastChangedRevision: 182 $
 * $LastChangedDate: 2016-12-07 11:52:16 +0200 (Wed, 07 Dec 2016) $
 * 
 * $URL: svn://10.100.0.35/stellrrepo/stellr172/StoresAdminConsole/src/java/stellr/util/accounts/admin/util/CacheControlPhaseListener.java $
 * 
 * (C) 2016 Stellr  All Rights Reserved 
 * 
 */
package stellr.util.stores.admin.util;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;



public class CacheControlPhaseListener implements PhaseListener {
    private static final long serialVersionUID = -4487802076271329615L;

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

    @Override
    public void afterPhase(PhaseEvent event) {
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        FacesContext facesContext = event.getFacesContext();
        HttpServletResponse response = (HttpServletResponse) facesContext
                .getExternalContext().getResponse();
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        // Stronger according to blog comment below that references HTTP spec
        response.addHeader("Cache-Control", "no-store");
        response.addHeader("Cache-Control", "must-revalidate");
        // some date in the past
        response.addHeader("Expires", "Mon, 8 Aug 2006 10:00:00 GMT");
    }

}
