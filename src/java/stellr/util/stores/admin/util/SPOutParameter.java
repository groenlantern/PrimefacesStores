/* 
 * $Id: SPOutParameter.java 284 2017-01-23 06:12:07Z tungsten $ 
 * $Author: tungsten $
 * $Date: 2017-01-23 08:12:07 +0200 (Mon, 23 Jan 2017) $
 * 
 * $Rev: 284 $
 * 
 * $LastChangedBy: tungsten $ 
 * $LastChangedRevision: 284 $
 * $LastChangedDate: 2017-01-23 08:12:07 +0200 (Mon, 23 Jan 2017) $
 * 
 * $URL: svn://10.100.0.35/stellrrepo/stellr172/StoresAdminConsole/src/java/stellr/util/accounts/admin/util/SPOutParameter.java $
 * 
 * (C) 2016 Stellr  All Rights Reserved 
 * 
 */
package stellr.util.stores.admin.util;

/**
 *
 * @author Andre Labuschagne
 */
public class SPOutParameter {

    protected String name;
    protected Class klas;

    public SPOutParameter(String input) throws Exception {
        set(input);
    }
    
    public SPOutParameter(String name, Class klas) {
        this.name = name;
        this.klas = klas;
    }

    private void set(String input) throws Exception {
        String[] a = input.split(",");
        if (a.length < 2) {
            throw new Exception("Not enough data for parameter");
        }
        name = a[0];
        klas = Class.forName(a[1]);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the klas
     */
    public Class getKlas() {
        return klas;
    }
}
