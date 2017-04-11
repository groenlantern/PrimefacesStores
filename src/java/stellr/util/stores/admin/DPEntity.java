/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 

CREATE TABLE `distributionpartner` (
  `distributionpartnerid` varchar(15) NOT NULL,
  `merchantcategory_merchantcategorycode` varchar(4) NOT NULL,
  `distributionpartneraggregator_distributionpartneraggregatorid` bigint(20) NOT NULL,
  `catalogue_catalogueid` bigint(20) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `phonenumber` varchar(45) DEFAULT NULL,
  `createdate` date DEFAULT NULL,
  `updatedate` date DEFAULT NULL,
  `certthumbprint` varchar(45) DEFAULT NULL,
  `timezone` decimal(4,2) DEFAULT '0.00',
  `validateaccount` tinyint(1) DEFAULT '0',
  `dptimeout` int(11) DEFAULT NULL,
  `keyvalue` varchar(512) DEFAULT NULL,
  `ftproutepath` varchar(512) DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  `currentbalance` varchar(12) DEFAULT NULL,
  `accounttype` varchar(45) DEFAULT 'NONE',
  `settlementtime` datetime DEFAULT NULL,
  `accountid` int(11) DEFAULT NULL,
  `accountstatus` varchar(45) DEFAULT NULL,
  `pendingbalance` varchar(12) DEFAULT NULL,
  `accountlimit` varchar(12) DEFAULT NULL,
  PRIMARY KEY (`distributionpartnerid`),
  KEY `fk_distributionpartner_distributionpartneraggregator1_idx` (`distributionpartneraggregator_distributionpartneraggregatorid`),
  KEY `fk_catalogue_catalogueid12_idx` (`catalogue_catalogueid`),
  CONSTRAINT `fk_catalogue_catalogueid12_idx` FOREIGN KEY (`catalogue_catalogueid`) REFERENCES `catalogue` (`catalogueid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_distributionpartner_distributionpartneraggregator1` FOREIGN KEY (`distributionpartneraggregator_distributionpartneraggregatorid`) REFERENCES `distributionpartneraggregator` (`distributionpartneraggregatorid`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



 */
package stellr.util.stores.admin;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Timestamp;
import javax.sql.DataSource;

/**
 *
 * @author Jean-Pierre Erasmus
 */
public class DPEntity implements Serializable {
    private String returnMessage = ""; 
    
    //Variables
    private String    distributionpartnerid;
    private String    merchantcategory_merchantcategorycode;
    private long      distributionpartneraggregator_distributionpartneraggregatorid;
    private long      catalogue_catalogueid;
    private String    name;
    private String    email;
    private String    phonenumber;
    private Timestamp createdate;
    private Timestamp updatedate;
    private String    certthumbprint;
    private double    timezone;
    private boolean   validateaccount;
    private long      dptimeout;
    private String    keyvalue;
    private String    ftproutepath;
    private boolean   active;
    private String    currentbalance;
    private String    accounttype;
    private Timestamp settlementtime;
    private long      accountid;
    private String    accountstatus;
    private String    pendingbalance;
    private String    accountlimit;
    
    
   /**
     * 
     */
    private DataSource dataSourceGridController;

     
    /**
     * 
     * @return
     * @throws Exception 
     */
    public Connection getMySqlConnection() throws Exception {
         return getDataSourceGridController().getConnection();
    }
    
    /**
     * @return the dataSourceGridController
     */
    public DataSource getDataSourceGridController() {
        return dataSourceGridController;
    }

    /**
     * @param dataSourceGridController the dataSourceGridController to set
     */
    public void setDataSourceGridController(DataSource dataSourceGridController) {
        this.dataSourceGridController = dataSourceGridController;
    }

    /**
     * @return the returnMessage
     */
    public String getReturnMessage() {
        return returnMessage;
    }

    /**
     * @param returnMessage the returnMessage to set
     */
    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the phone number
     */
    public String getPhonenumber() {
        return phonenumber;
    }
    
    /**
     * 
     * @return 
     */
    public Double getPhonenumberNum() {
        return Double.parseDouble(getPhonenumber().trim().replaceAll(" ","").replaceAll("-","").replaceAll("/+","") ) ;
    }

    /**
     * @param phonenumber the phone number to set
     */
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    /**
     * @return the update date
     */
    public Timestamp getUpdatedate() {
        return updatedate;
    }

    /**
     * @param updatedate the update date to set
     */
    public void setUpdatedate(Timestamp updatedate) {
        this.updatedate = updatedate;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @return the account id
     */
    public long getAccountid() {
        return accountid;
    }

    /**
     * @param accountid the account id to set
     */
    public void setAccountid(long accountid) {
        this.accountid = accountid;
    }

    /**
     * @return the account type
     */
    public String getAccounttype() {
        return accounttype;
    }

    /**
     * @param accounttype the account type to set
     */
    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype;
    }

    /**
     * @return the distribution partner id
     */
    public String getDistributionpartnerid() {
        return distributionpartnerid;
    }

    /**
     * @param distributionpartnerid the distribution partner id to set
     */
    public void setDistributionpartnerid(String distributionpartnerid) {
        this.distributionpartnerid = distributionpartnerid;
    }

    /**
     * @return the merchant category_merchant category code
     */
    public String getMerchantcategory_merchantcategorycode() {
        return merchantcategory_merchantcategorycode;
    }

    /**
     * @param merchantcategory_ merchant category code the merchantcategory_merchantcategorycode to set
     */
    public void setMerchantcategory_merchantcategorycode(String merchantcategory_merchantcategorycode) {
        this.merchantcategory_merchantcategorycode = merchantcategory_merchantcategorycode;
    }

    /**
     * @return the distribution partner aggregator_distribution partner aggregatorid
     */
    public long getDistributionpartneraggregator_distributionpartneraggregatorid() {
        return distributionpartneraggregator_distributionpartneraggregatorid;
    }

    /**
     * @param distributionpartneraggregator_distribution partneraggregatorid the distributionpartneraggregator_distributionpartneraggregatorid to set
     */
    public void setDistributionpartneraggregator_distributionpartneraggregatorid(long distributionpartneraggregator_distributionpartneraggregatorid) {
        this.distributionpartneraggregator_distributionpartneraggregatorid = distributionpartneraggregator_distributionpartneraggregatorid;
    }

    /**
     * @return the catalogue_catalogue id
     */
    public long getCatalogue_catalogueid() {
        return catalogue_catalogueid;
    }

    /**
     * @param catalogue_catalogue id the catalogue_catalogue id to set
     */
    public void setCatalogue_catalogueid(long catalogue_catalogueid) {
        this.catalogue_catalogueid = catalogue_catalogueid;
    }

    /**
     * @return the create date
     */
    public Timestamp getCreatedate() {
        return createdate;
    }

    /**
     * @param createdate the create date to set
     */
    public void setCreatedate(Timestamp createdate) {
        this.createdate = createdate;
    }

    /**
     * @return the cert thumbprint
     */
    public String getCertthumbprint() {
        return certthumbprint;
    }

    /**
     * @param certthumbprint the cert thumbprint to set
     */
    public void setCertthumbprint(String certthumbprint) {
        this.certthumbprint = certthumbprint;
    }

    /**
     * @return the time zone
     */
    public double getTimezone() {
        return timezone;
    }

    /**
     * @param timezone the time zone to set
     */
    public void setTimezone(double timezone) {
        this.timezone = timezone;
    }

    /**
     * @return the validate account
     */
    public boolean isValidateaccount() {
        return validateaccount;
    }

    /**
     * @param validateaccount the validate account to set
     */
    public void setValidateaccount(boolean validateaccount) {
        this.validateaccount = validateaccount;
    }

    /**
     * @return the dp timeout
     */
    public long getDptimeout() {
        return dptimeout;
    }

    /**
     * @param dptimeout the dp timeout to set
     */
    public void setDptimeout(long dptimeout) {
        this.dptimeout = dptimeout;
    }

    /**
     * @return the key value
     */
    public String getKeyvalue() {
        return keyvalue;
    }

    /**
     * @param keyvalue the key value to set
     */
    public void setKeyvalue(String keyvalue) {
        this.keyvalue = keyvalue;
    }

    /**
     * @return the ftp route path
     */
    public String getFtproutepath() {
        return ftproutepath;
    }

    /**
     * @param ftproutepath the ftp route path to set
     */
    public void setFtproutepath(String ftproutepath) {
        this.ftproutepath = ftproutepath;
    }

    /**
     * @return the current balance
     */
    public String getCurrentbalance() {
        return currentbalance;
    }

    /**
     * @param currentbalance the current balance to set
     */
    public void setCurrentbalance(String currentbalance) {
        this.currentbalance = currentbalance;
    }

    /**
     * @return the settlement time
     */
    public Timestamp getSettlementtime() {
        return settlementtime;
    }

    /**
     * @param settlementtime the settlement time to set
     */
    public void setSettlementtime(Timestamp settlementtime) {
        this.settlementtime = settlementtime;
    }

    /**
     * @return the account status
     */
    public String getAccountstatus() {
        return accountstatus;
    }

    /**
     * @param accountstatus the account status to set
     */
    public void setAccountstatus(String accountstatus) {
        this.accountstatus = accountstatus;
    }

    /**
     * @return the pending balance
     */
    public String getPendingbalance() {
        return pendingbalance;
    }

    /**
     * @param pendingbalance the pending balance to set
     */
    public void setPendingbalance(String pendingbalance) {
        this.pendingbalance = pendingbalance;
    }

    /**
     * @return the account limit
     */
    public String getAccountlimit() {
        return accountlimit;
    }

    /**
     * @param accountlimit the account limit to set
     */
    public void setAccountlimit(String accountlimit) {
        this.accountlimit = accountlimit;
    }


}
