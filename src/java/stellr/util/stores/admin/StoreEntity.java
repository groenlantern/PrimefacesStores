/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 
CREATE TABLE `store` (
  `storeid` bigint(20) NOT NULL AUTO_INCREMENT,
  `distributionpartner_distributionpartnerid` varchar(15) NOT NULL,
  `address_addressid` bigint(20) NOT NULL,
  `storetype_storetypecode` bigint(20) NOT NULL,
  `sourceidentifier` varchar(16) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `phonenumber` varchar(45) DEFAULT NULL,
  `createddate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `active` tinyint(1) DEFAULT '1',
  `accountid` int(11) DEFAULT NULL,
  `accounttype` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`storeid`),
  KEY `fk_Store_distributionpartner1_idx` (`distributionpartner_distributionpartnerid`),
  KEY `fk_store_address1_idx` (`address_addressid`),
  KEY `fk_store_storetype1_idx` (`storetype_storetypecode`),
  CONSTRAINT `fk_Store_distributionpartner1` FOREIGN KEY (`distributionpartner_distributionpartnerid`) REFERENCES `distributionpartner` (`distributionpartnerid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_store_address1` FOREIGN KEY (`address_addressid`) REFERENCES `address` (`addressid`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_store_storetype1` FOREIGN KEY (`storetype_storetypecode`) REFERENCES `storetype` (`storetypecode`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1185 DEFAULT CHARSET=utf8;


CREATE TABLE `address` (
  `addressid` bigint(20) NOT NULL AUTO_INCREMENT,
  `address1` varchar(1024) DEFAULT NULL,
  `region` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `postalcode` varchar(12) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`addressid`)
) ENGINE=InnoDB AUTO_INCREMENT=1189 DEFAULT CHARSET=utf8;


 */
package stellr.util.stores.admin;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;
import javax.sql.DataSource;

/**
 *
 * @author Jean-Pierre Erasmus
 */
public class StoreEntity implements Serializable {

    public StoreEntity(DataSource dataSourceGridController) {
        this.returnMessage = ""; 
        this.storeid = 0;
        this.distributionpartner_distributionpartnerid = "";
        this.address_addressid = 0; 
        this.storetype_storetypecode = 0;
        this.sourceidentifier = "";
        this.storename = "";
        this.email = "";
        this.phonenumber = "";
        this.active = false;
        this.accountid = 0;
        this.accounttype = "";
    
        this.addressid = 0;
        this.address1 = "";
        this.address_main = "";
        this.gps_lon = "";
        this.gps_lat = "";
        this.region = "";
        this.city = "";
        this.postalcode = "";
        this.country = "";

        this.address_block = "";
        this.address_building = "";
        this.address_floor = "";
        this.address_shopNo = "";
        
        this.createddate = new Timestamp( (new Date()).getTime() );
        this.updatedate = null;
        this.dataSourceGridController = dataSourceGridController;
        
        this.distName = "";
        this.stypeDesc = "";
    }
    
    private String distName = "";
    private String stypeDesc = "";
                
    private String returnMessage = ""; 
    private long storeid = 0;
    public String distributionpartner_distributionpartnerid = "";
    private long address_addressid = 0; 
    public long storetype_storetypecode = 0;
    private String sourceidentifier = "";
    private String storename = "";
    private String email = "";
    private String phonenumber = "";
    private Timestamp createddate; 
    private Timestamp updatedate; 
    private boolean active = false;
    private long accountid = 0;
    private String accounttype = "";
    
    //Address details
    private long addressid = 0;
    private String address1 = "";
    private String address_main = "";
    private String address_block = "";
    private String address_building = "";
    private String address_floor = "";
    private String address_shopNo = "";
    private String gps_lon = "";
    private String gps_lat = "";
    private String region = "";
    private String city = "";
    private String postalcode = "";
    private String country = "";
            
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
     * @return the storeid
     */
    public long getStoreid() {
        return storeid;
    }

    /**
     * @param storeid the storeid to set
     */
    public void setStoreid(long storeid) {
        this.storeid = storeid;
    }

    /**
     * @return the distributionpartner_distributionpartnerid
     */
    public String getDistributionpartner_distributionpartnerid() {
                
        return distributionpartner_distributionpartnerid;
    }

    /**
     * @param distributionpartner_distributionpartnerid the distributionpartner_distributionpartnerid to set
     */
    public void setDistributionpartner_distributionpartnerid(String distributionpartner_distributionpartnerid) {
        this.distributionpartner_distributionpartnerid = distributionpartner_distributionpartnerid;
    }

    /**
     * @return the address_addressid
     */
    public long getAddress_addressid() {
        return address_addressid;
    }

    /**
     * @param address_addressid the address_addressid to set
     */
    public void setAddress_addressid(long address_addressid) {
        this.address_addressid = address_addressid;
    }

    /**
     * @return the storetype_storetypecode
     */
    public long getStoretype_storetypecode() {
        return storetype_storetypecode;
    }

    /**
     * @param storetype_storetypecode the storetype_storetypecode to set
     */
    public void setStoretype_storetypecode(long storetype_storetypecode) {
        this.storetype_storetypecode = storetype_storetypecode;
    }

    /**
     * @return the sourceidentifier
     */
    public String getSourceidentifier() {
        return sourceidentifier;
    }

    /**
     * @param sourceidentifier the sourceidentifier to set
     */
    public void setSourceidentifier(String sourceidentifier) {
        this.sourceidentifier = sourceidentifier;
    }

    /**
     * @return the name
     */
    public String getStorename() {
        return storename;
    }

    /**
     * @param name the name to set
     */
    public void setStorename(String storname) {
        this.storename = storname;
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
     * @return the phonenumber
     */
    public String getPhonenumber() {
        return phonenumber;
    }
    
    /**
     * 
     * @return 
     */
    public Double getPhonenumberNum() {
        return Double.parseDouble(  phonenumber.trim().replaceAll(" ","").replaceAll("-","").replaceAll("/+","") ) ;
    }

    /**
     * @param phonenumber the phonenumber to set
     */
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    /**
     * @return the createddate
     */
    public Timestamp getCreateddate() {
        return createddate;
    }

    /**
     * @param createddate the createddate to set
     */
    public void setCreateddate(Timestamp createddate) {
        this.createddate = createddate;
    }

    /**
     * @return the updatedate
     */
    public Timestamp getUpdatedate() {
        return updatedate;
    }

    /**
     * @param updatedate the updatedate to set
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
     * @return the accountid
     */
    public long getAccountid() {
        return accountid;
    }

    /**
     * @param accountid the accountid to set
     */
    public void setAccountid(long accountid) {
        this.accountid = accountid;
    }

    /**
     * @return the accounttype
     */
    public String getAccounttype() {
        return accounttype;
    }

    /**
     * @param accounttype the accounttype to set
     */
    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype;
    }

    /**
     * @return the addressid
     */
    public long getAddressid() {
        return addressid;
    }

    /**
     * @param addressid the addressid to set
     */
    public void setAddressid(long addressid) {
        this.addressid = addressid;
    }

    /**
     * @return the address1
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * @param address1 the address1 to set
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     * @return the address_main
     */
    public String getAddress_main() {
        return address_main;
    }

    /**
     * @param address_main the address_main to set
     */
    public void setAddress_main(String address_main) {
        this.address_main = address_main;
    }

    /**
     * @return the gps_lon
     */
    public String getGps_lon() {
        return gps_lon;
    }

    /**
     * @param gps_lon the gps_lon to set
     */
    public void setGps_lon(String gps_lon) {
        this.gps_lon = gps_lon;
    }

    /**
     * @return the gps_lat
     */
    public String getGps_lat() {
        return gps_lat;
    }

    /**
     * @param gps_lat the gps_lat to set
     */
    public void setGps_lat(String gps_lat) {
        this.gps_lat = gps_lat;
    }

    /**
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region the region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the postalcode
     */
    public String getPostalcode() {
        return postalcode;
    }

    /**
     * @param postalcode the postalcode to set
     */
    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the address_block
     */
    public String getAddress_block() {
        return address_block;
    }

    /**
     * @param address_block the address_block to set
     */
    public void setAddress_block(String address_block) {
        this.address_block = address_block;
    }

    /**
     * @return the address_building
     */
    public String getAddress_building() {
        return address_building;
    }

    /**
     * @param address_building the address_building to set
     */
    public void setAddress_building(String address_building) {
        this.address_building = address_building;
    }

    /**
     * @return the address_floor
     */
    public String getAddress_floor() {
        return address_floor;
    }

    /**
     * @param address_floor the address_floor to set
     */
    public void setAddress_floor(String address_floor) {
        this.address_floor = address_floor;
    }

    /**
     * @return the address_shopNo
     */
    public String getAddress_shopNo() {
        return address_shopNo;
    }

    /**
     * @param address_shopNo the address_shopNo to set
     */
    public void setAddress_shopNo(String address_shopNo) {
        this.address_shopNo = address_shopNo;
    }

    /**
     * @return the distName
     */
    public String getDistName() {
        return distName;
    }

    /**
     * @param distName the distName to set
     */
    public void setDistName(String distName) {
        this.distName = distName;
    }

    /**
     * @return the stypeDesc
     */
    public String getStypeDesc() {
        return stypeDesc;
    }

    /**
     * @param stypeDesc the stypeDesc to set
     */
    public void setStypeDesc(String stypeDesc) {
        this.stypeDesc = stypeDesc;
    }


}
