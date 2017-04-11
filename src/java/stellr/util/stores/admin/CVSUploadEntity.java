/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
  
 
 */
package stellr.util.stores.admin;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import javax.sql.DataSource;

/**
 *
 * @author Jean-Pierre Erasmus
 */
public class CVSUploadEntity implements Serializable {

    public CVSUploadEntity(DataSource dataSourceGridController) {
        this.returnMessage = ""; 
        this.columnsNames = new ArrayList<>();
        this.columnsType = new ArrayList<>();
        this.rowData = new ArrayList<>();
    }
    
    private String returnMessage = ""; 
    private boolean isHeader = false;
     
    private ArrayList<String> columnsNames = new ArrayList<>();
    private ArrayList<String> columnsType = new ArrayList<>();
    private ArrayList<String> rowData = new ArrayList<>();
          
          
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
     * @return the isHeader
     */
    public boolean isIsHeader() {
        return isHeader;
    }

    /**
     * @param isHeader the isHeader to set
     */
    public void setIsHeader(boolean isHeader) {
        this.isHeader = isHeader;
    }

    /**
     * @return the columnsNames
     */
    public ArrayList<String> getColumnsNames() {
        return columnsNames;
    }

    /**
     * @param columnsNames the columnsNames to set
     */
    public void setColumnsNames(ArrayList<String> columnsNames) {
        this.columnsNames = columnsNames;
    }

    /**
     * @return the columnsType
     */
    public ArrayList<String> getColumnsType() {
        return columnsType;
    }

    /**
     * @param columnsType the columnsType to set
     */
    public void setColumnsType(ArrayList<String> columnsType) {
        this.columnsType = columnsType;
    }

    /**
     * @return the rowData
     */
    public ArrayList<String> getRowData() {
        return rowData;
    }

    /**
     * @param rowData the rowData to set
     */
    public void setRowData(ArrayList<String> rowData) {
        this.rowData = rowData;
    }


}
