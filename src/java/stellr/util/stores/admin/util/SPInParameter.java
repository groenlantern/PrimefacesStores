/* 
 * $Id: SPInParameter.java 284 2017-01-23 06:12:07Z tungsten $ 
 * $Author: tungsten $
 * $Date: 2017-01-23 08:12:07 +0200 (Mon, 23 Jan 2017) $
 * 
 * $Rev: 284 $
 * 
 * $LastChangedBy: tungsten $ 
 * $LastChangedRevision: 284 $
 * $LastChangedDate: 2017-01-23 08:12:07 +0200 (Mon, 23 Jan 2017) $
 * 
 * $URL: svn://10.100.0.35/stellrrepo/stellr172/StoresAdminConsole/src/java/stellr/util/accounts/admin/util/SPInParameter.java $
 * 
 * (C) 2016 Stellr  All Rights Reserved 
 * 
 */
package stellr.util.stores.admin.util;

/**
 *
 * @author Andre Labuschagne
 */
public class SPInParameter extends SPOutParameter{

    protected Object value;

    public SPInParameter(String input) throws Exception {
        super(input);
        set(input);
    }
    
    public SPInParameter(String name, Class klas, Object value){
        super(name, klas);
        this.value = value;
    }

    public void set(String input) throws Exception {
        String[] a = input.split(",");
        if (a.length != 3) {
            throw new Exception("Not enough data for parameter");
        }
        value = a[2];
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

}
