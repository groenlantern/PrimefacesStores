/*
 * $Id: Constants.java 182 2016-12-07 09:52:16Z tungsten $ 
 * $Author: tungsten $
 * $Date: 2016-12-07 11:52:16 +0200 (Wed, 07 Dec 2016) $
 *  
 * $Rev: 182 $
 *  
 * $LastChangedBy: tungsten $ 
 * $LastChangedRevision: 182 $
 * $LastChangedDate: 2016-12-07 11:52:16 +0200 (Wed, 07 Dec 2016) $
 *  
 * $URL: svn://10.100.0.35/stellrrepo/stellr172/StoresAdminConsole/src/java/stellr/util/accounts/admin/util/Constants.java $
 *  
 * (C) 2016 Stellr  All Rights Reserved
 */
package stellr.util.stores.admin.util;

/**
 *
 * @author Andre Labuschagne
 */
public interface Constants {
    
    String POSTPAID = "POSTPAID";
    String PREPAID = "PREPAID";
    String DAILY = "DAILY";
    short NOTIFY_ON_FLAG_NONE = 0b00;
    short NOTIFY_ON_FLAG_BALANCE = 0b01;
    short NOTIFY_ON_FLAG_DAILY = 0b10;
    short NOTIFY_ON_FLAG_BOTH = 0b11;
    String KEY_DAILY_MESSAGE = "dailymessage";
    String KEY_DAILY_SUBJECT = "dailysubject";
    String KEY_ACCOUNT_MESSAGE = "accountmessage";
    String KEY_ACCOUNT_SUBJECT = "accountsubject";
    String KEY_CHECK_FREQ_H = "checkfreq_h";
    String KEY_START_DELAY_M = "startdelay_m";
    String SUBPROJECT = "stellr.util.stores.admin";
    String SUBPROJECT_EMAIL = "stellr.evi.admin";
}
