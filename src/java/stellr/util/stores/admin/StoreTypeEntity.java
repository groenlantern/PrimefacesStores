/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 
CREATE TABLE `storetype` (
  `storetypecode` bigint(20) NOT NULL AUTO_INCREMENT,
  `storetypedescription` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`storetypecode`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;



 */
package stellr.util.stores.admin;

import java.io.Serializable;
import java.sql.Connection;
import javax.sql.DataSource;

/**
 *
 * @author Jean-Pierre Erasmus
 */
public class StoreTypeEntity implements Serializable {
    private String returnMessage = ""; 
    private long storetypecode = 0;
    private String storetypedescription = "";
    
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
     * @return the storetypecode
     */
    public long getStoretypecode() {
        return storetypecode;
    }

    /**
     * @param storetypecode the storetypecode to set
     */
    public void setStoretypecode(long storetypecode) {
        this.storetypecode = storetypecode;
    }

    /**
     * @return the storetypedescription
     */
    public String getStoretypedescription() {
        return storetypedescription;
    }

    /**
     * @param storetypedescription the storetypedescription to set
     */
    public void setStoretypedescription(String storetypedescription) {
        this.storetypedescription = storetypedescription;
    }

}
