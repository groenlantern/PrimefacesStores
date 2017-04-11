/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stellr.util.stores.admin;

/**
 *
 * @author Jean-Pierre Erasmus
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import stellr.util.stores.admin.util.JsfUtil;

/**
 *
 * @author Jean-Pierre Erasmus
 */
@WebServlet("/UploadServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 50,
        maxFileSize = 1024 * 1024 * 50,
        maxRequestSize = 1024 * 1024 * 100)
@ManagedBean(name = "UploadServlet")
@SessionScoped
public class FileUploadServlet extends HttpServlet {

    /**
     *
     */
    @Resource(name = "java:app/haloLocal")
    private DataSource dataSourceGridController;

    private static final Logger LOG = Logger.getLogger(StoresController.class.getName());
    private static final long serialVersionUID = -2308423502247453109L;

    private boolean isMultipart;
    private final int maxFileSize = 1024 * 1024 * 50;
    private final int maxMemSize = 1024 * 1024 * 50;

    private static ArrayList<ArrayList> rowData = null;
    private static ArrayList<Object> colData = null;

    /**
     */
    public void initRowData() {
        this.rowData = null;
        this.colData = null;
        System.gc();
    }

    /**
     *
     * @return
     */
    public ArrayList<ColumnSelection> getColumnSelection() {
        ArrayList<ColumnSelection> tempList = new ArrayList<>();

        tempList.addAll(Arrays.asList(ColumnSelection.values()));

        return tempList;
    }

    /**
     *
     */
    public enum ColumnSelection {

        /**
         * VALUES
         */
        DISTRIBUTIONPARTNERID("Distribution Partner"),
        STORETYPECODE("StoreType"),
        SOURCEIDENTIFIER("Source ID"),
        NAME("Store Name"),
        EMAIL("Email"),
        PHONENUMBER("Phone No"),
        ACTIVE("Active"),
        ADDRESS_MAIN("Address Line 1"),
        ADDRESS_BLOCK("Block"),
        ADDRESS_BUILDING("Building"),
        ADDRESS_FLOOR("Floor"),
        ADDRESS_SHOPNO("Shop No"),
        GPS_LON("GPS Longtitude"),
        GPS_LAT("GPS Latitude"),
        REGION("Region"),
        CITY("City"),
        POSTALCODE("Postal Code"),
        COUNTRY("Country"),
        NOTAPPLICABLE("Not Applicable");

        /**
         *
         */
        public final String columnDescription;

        /**
         *
         * @return
         */
        public String getColumnDescription() {
            return columnDescription;
        }

        /**
         * Constructor
         *
         * @param columnDescriptn
         */
        ColumnSelection(String columnDescriptn) {
            this.columnDescription = columnDescriptn;
        }
    }

    /**
     * Get Column Descriptions/Names Ignore Header rows Create StoreEntity for
     * update Call update for each row
     *
     */
    public void commitFileData() {
        ArrayList<ColumnSelection> colType = new ArrayList<>();
        StoreEntity storeData;
        boolean loadFirstRow = true;

        for (ArrayList<Object> cells : rowData) {
            //Create new row for storeData
            storeData = new StoreEntity(dataSourceGridController);

            int cellCounter = 0;
            for (Object cellData : cells) {
                //Initialize null cells
                if (cellData == null) {
                    cellData = "";
                }

                //Get Column Types/names/Descriptions                    
                if (loadFirstRow) { //First row is column selections
                    if (cellData.getClass().equals(java.lang.String.class)) {
                        if (cellData.toString().trim().isEmpty()) { //If no column selected make it N/A
                            colType.add(ColumnSelection.NOTAPPLICABLE);
                        } else {
                            colType.add(ColumnSelection.valueOf((String) cellData));   //Set column name to ENUM Value
                        }
                    } else {
                        colType.add(ColumnSelection.NOTAPPLICABLE);   //If not string columnmake it N/A column - i.e. header switch - boolean
                    }

                } //Load cells as column names 
                else {
                    if (cellData.getClass().equals(java.lang.Boolean.class)) { //Skip header rows (check header switch)
                        boolean isHeader = (boolean) cellData;

                        if (isHeader) {
                            cellCounter++;
                            break; //Skip Row
                        } else { //First Column - Header boolean switch is false - data will be String even if boolean value
                            cellCounter++;
                            continue; //Skip Cell - Not data to be saved, util data
                        }
                    }

                    //Get Column Type from ENUM and column array
                    ColumnSelection cellName = colType.get(cellCounter);

                    //Process Data row, load StoreEntity for Create() dependent on column type                                                 
                    switch (cellName) {
                        case DISTRIBUTIONPARTNERID:
                            storeData.setDistributionpartner_distributionpartnerid((String) cellData);
                            break;
                        case STORETYPECODE:
                            try {
                                storeData.setStoretype_storetypecode(Long.parseLong((String) cellData));
                            } catch (Exception e) {
                                storeData.setStoretype_storetypecode(0);
                                System.out.println("STORETYPECODE Ex" + e.getMessage());
                            }
                            break;
                        case SOURCEIDENTIFIER:
                            storeData.setSourceidentifier((String) cellData);
                            break;
                        case NAME:
                            storeData.setStorename((String) cellData);
                            break;
                        case EMAIL:
                            storeData.setEmail((String) cellData);
                            break;
                        case PHONENUMBER:
                            break;
                        case ACTIVE:
                            try {
                                String tmpCell = (String) cellData;
                                
                                if ( tmpCell!= null && 
                                       ( tmpCell.trim().equals("1") || 
                                         tmpCell.trim().toUpperCase().equals(Boolean.TRUE.toString().toUpperCase()) ) ) { 
                                    tmpCell = Boolean.TRUE.toString();
                                } else { 
                                    tmpCell = Boolean.FALSE.toString();
                                }                                
                                
                                storeData.setActive( Boolean.parseBoolean( tmpCell.trim().toUpperCase() ) );
                            } catch (Exception e) {
                                storeData.setActive(false);
                                System.out.println("setActive Ex" + e.getMessage());
                            }

                            break;
                        case ADDRESS_MAIN:
                            storeData.setAddress_main((String) cellData);
                            storeData.setAddress1((String) cellData);
                            break;
                        case ADDRESS_BLOCK:
                            storeData.setAddress_block((String) cellData);
                            break;
                        case ADDRESS_BUILDING:
                            storeData.setAddress_building((String) cellData);
                            break;
                        case ADDRESS_FLOOR:
                            storeData.setAddress_floor((String) cellData);
                            break;
                        case ADDRESS_SHOPNO:
                            storeData.setAddress_shopNo((String) cellData);
                            break;
                        case GPS_LON:
                            storeData.setGps_lon((String) cellData);
                            break;
                        case GPS_LAT:
                            storeData.setGps_lat((String) cellData);
                            break;
                        case REGION:
                            storeData.setRegion((String) cellData);
                            break;
                        case CITY:
                            storeData.setCity((String) cellData);
                            break;
                        case POSTALCODE:
                            storeData.setPostalcode((String) cellData);
                            break;
                        case COUNTRY:
                            storeData.setCountry((String) cellData);
                            break;
                        default:
                            //Ignore column where type is not set 
                            break;
                    }
                }
                //increment cell counter
                cellCounter++;
            }
            loadFirstRow = false; //First Row Containing column types loaded

            //Call Store Create with new SrtoreEntity Object for creation
            try {
                if (storeData.getStorename() != null
                        && !storeData.getStorename().trim().isEmpty()
                        && storeData.getDistributionpartner_distributionpartnerid() != null
                        && !storeData.getDistributionpartner_distributionpartnerid().trim().isEmpty()) {

                    StoresController storeController = new StoresController();
                    storeController.setDataSourceGridController(dataSourceGridController);

                    storeController.prepareCreate();
                    storeController.setSelectedStore(storeData);
                    storeController.create();
                }

            } catch (Exception e1) {
                System.out.println("Create Store Exception : " + e1.getMessage());

                Throwable cause = e1.getCause();
                String msg = "";

                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }

                JsfUtil.addErrorMessage("Exception : " + msg + " : " + e1.getMessage());

                Logger.getLogger(FileUploadServlet.class.getName()).log(Level.SEVERE, null, e1);
            }
        }
        
        JsfUtil.addSuccessMessage( "All Store Data Rows Processed" );
        initRowData();
    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        doPost(request, response);
    }

    /**
     * handles file upload
     *
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        //Clear current rowdata
        this.rowData = null;
        this.colData = null;
        System.gc();
        
        // Check that we have a file upload request
        isMultipart = ServletFileUpload.isMultipartContent(request);

        if (!isMultipart) {
            System.out.println("Not file Exception Upload");
            return;
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();
        FileItem fileIn;

        // maximum size that will be stored in memory
        factory.setSizeThreshold(maxMemSize);
        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        // maximum file size to be uploaded.
        upload.setSizeMax(maxFileSize);

        try {
            // Parse the request to get file items.
            List fileItems = upload.parseRequest(request);

            // Process the uploaded file items
            Iterator i = fileItems.iterator();

            while (i.hasNext()) {
                fileIn = (FileItem) i.next();

                if (!fileIn.isFormField()) {

                    // Get the uploaded file parameters
                    String fileName = fileIn.getName();

                    try {
                        String str = fileIn.getString();
                        //Rows
                        String[] arr = str.split("\n\\s*(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                        //Rows Initialize
                        String[][] csv = new String[arr.length][];

                        //Split csv data for rows and columns
                        for (int r = 0; r < arr.length; r++) {
                            String[] tmpRowData = arr[r].split(",\\s*(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                            //Add column infront
                            String[] addColData = new String[tmpRowData.length + 1];
                            addColData[0] = Boolean.FALSE.toString();

                            try {
                                //Copy Row Column Celldata to new Array Row for datatable
                                //Data should now have a new column in the front saying FALSE, used for is header tick box
                                int cdidx = 1;
                                for (String colCellData : tmpRowData) {
                                    addColData[cdidx++] = colCellData;
                                }
                                csv[r] = addColData;
                            } catch (Exception e12) {
                                csv[r] = arr[r].split(",\\s*(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                            }
                        }

                        //Setup new rowdata for datatable
                        setRowData(new ArrayList<>());

                        //Add Header Row
                        for (int x = 0; x < 1; x++) {
                            //Create String array with one extra cell for is header checkbox
                            //Initialise column header data
                            setColData(new ArrayList(Arrays.asList(new String[csv[x].length])));

                            //Add first empty row to Row Data
                            getRowData().add(getColData());

                        }

                        //Add DataRows from CSV
                        for (String[] csvRow : csv) {
                            //Add Each row from CSV as Row Data
                            getRowData().add((ArrayList<String>) (new ArrayList(Arrays.asList(csvRow))));
                        }

                    } catch (Exception e) {
                        System.out.println(" File Write Exception e " + e.getMessage());

                        Throwable cause = e.getCause();
                        String msg = "";

                        if (cause != null) {
                            msg = cause.getLocalizedMessage();
                        }
                    
                        Logger.getLogger(FileUploadServlet.class.getName()).log(Level.SEVERE, null, e);

                    }
                }
            }

        } catch (Exception ex) {
            System.out.println("DoPost Ex " + ex);
        }  
    }

    /**
     * @return the rowData
     */
    public ArrayList<ArrayList> getRowData() {
        return rowData;
    }

    /**
     * @param rowData the rowData to set
     */
    public void setRowData(ArrayList<ArrayList> rowData) {
        this.rowData = rowData;
    }

    /**
     * @return the colData
     */
    public ArrayList<Object> getColData() {
        return colData;
    }

    /**
     * @param colData the colData to set
     */
    public void setColData(ArrayList<Object> colData) {
        this.colData = colData;
    }

}
