/*
 * $Id: StoresController.java 284 2017-01-23 06:12:07Z tungsten $ 
 * $Author: tungsten $
 * $Date: 2017-01-23 08:12:07 +0200 (Mon, 23 Jan 2017) $
 *  
 * $Rev: 284 $
 *  
 * $LastChangedBy: tungsten $ 
 * $LastChangedRevision: 284 $
 * $LastChangedDate: 2017-01-23 08:12:07 +0200 (Mon, 23 Jan 2017) $
 *  
 * $URL: svn://10.100.0.35/stellrrepo/stellr172/StoresAdminConsole/src/java/stellr/util/accounts/admin/AccountController.java $
 *  
 * (C) 2016 Stellr  All Rights Reserved
 */
package stellr.util.stores.admin;

import stellr.util.stores.admin.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;
import stellr.util.stores.admin.util.JsfUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.CharacterData;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@ManagedBean(name = "storesController")
@SessionScoped
public class StoresController implements Serializable {

    /**
     *
     */
    @Resource(name = "java:app/haloLocal")
    private DataSource dataSourceGridController;

    private static final Logger LOG = Logger.getLogger(StoresController.class.getName());
    private static final long serialVersionUID = -2308423502247453109L;

    private List<DPEntity> dps = null;
    private List<StoreEntity> storeItems = null;
    private List<StoreEntity> filteredStoreItems = null;
    private StoreEntity selectedStore = null;
    private String distributionpartner_distributionpartnerid;
    private long storetypecode;
    private List<StoreTypeEntity> stps = null;
    
    private String userName = null;
    private String serverName = null;
    
    //Constants
    final static String XML_HEADER = "http://stellr-net";
    final static String XML_ADDRESS1 = "address1";
    final static String XML_ADDRESS = "address";
    final static String XML_GPS = "gps";
    final static String XML_GPS_LON = "lon";
    final static String XML_GPS_LAT = "lat";
    final static String XML_ADDRESS_BUILDING = "addressBuilding";
    final static String XML_BLOCK = "block";
    final static String XML_BUILDING = "building";
    final static String XML_FLOOR = "floor";
    final static String XML_SHOP = "shop";

    /**
     *
     * @return @throws Exception
     */
    public Connection getMySqlConnection() throws Exception {
        return getDataSourceGridController().getConnection();
    }

    public StoresController() {
    }

    /**
     * 
     */
    public void reset() {
        LOG.log(Level.INFO, "Resetting");

    }
    /**
     * 
     */
    public void prepareCreate() {

        this.selectedStore = new StoreEntity(dataSourceGridController);

        try {

            selectedStore.setAccountid(0);
            selectedStore.setAccounttype("");
            selectedStore.setActive(true);
            selectedStore.setAddress_addressid(0);
            selectedStore.setCreateddate(null);
            selectedStore.setDistributionpartner_distributionpartnerid("");
            selectedStore.setEmail("");
            selectedStore.setStorename("");
            selectedStore.setPhonenumber("");
            selectedStore.setReturnMessage("");
            selectedStore.setSourceidentifier("");
            selectedStore.setStoreid(0);
            selectedStore.setStoretype_storetypecode(0);
            selectedStore.setUpdatedate(null);
            selectedStore.setDistName("");
            selectedStore.setStypeDesc("");
            selectedStore.setAddress1("");
            selectedStore.setAddress_addressid(0);
            selectedStore.setAddress_block("");
            selectedStore.setAddress_building("");
            selectedStore.setAddress_floor("");
            selectedStore.setAddress_main("");
            selectedStore.setAddress_shopNo("");
            selectedStore.setAddressid(0);
            selectedStore.setCity("");
            selectedStore.setCountry("");
            selectedStore.setGps_lat("");
            selectedStore.setGps_lon("");

        } catch (Exception e1) {

        }
        try {
            distributionpartner_distributionpartnerid = null;

        } catch (Exception e1) {

        }
        try {
            storetypecode = 0;
        } catch (Exception e1) {

        }

    }

    /**
     * @return the distributionpartner_distributionpartnerid
     */
    public String getDistributionpartner_distributionpartnerid() {

        return distributionpartner_distributionpartnerid;
    }

    /**
     * @param distributionpartner_distributionpartnerid the
     * distributionpartner_distributionpartnerid to set
     */
    public void setDistributionpartner_distributionpartnerid(String distributionpartner_distributionpartnerid) {
        this.distributionpartner_distributionpartnerid = distributionpartner_distributionpartnerid;
    }

    /**
     *
     * @param e
     * @return
     */
    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();

        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }

        return "?";
    }

    /**
     *
     * @param xmlInput
     */
    private void splitXMLAddressToSelected(String xmlInput) {
        try {
            if (this.selectedStore == null) {
                this.selectedStore = new StoreEntity(dataSourceGridController);
            }

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            //Read XML String
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlInput));

            //Extract first Address details
            Document doc = db.parse(is);
            NodeList addressNodes = doc.getElementsByTagName(XML_ADDRESS1);

            //use first instance of address tag
            for (int i = 0; i < 1; i++) {
                Element addressOneNEl = (Element) addressNodes.item(i);

                //Main Address Line 
                NodeList mainAddressNode = addressOneNEl.getElementsByTagName(XML_ADDRESS);
                for (int j = 0; j < 1; j++) {
                    Element addressMainEl = (Element) mainAddressNode.item(j);
                    this.selectedStore.setAddress_main(getCharacterDataFromElement(addressMainEl));
                }

                //GPS Lat and Lon
                NodeList gpsNode = addressOneNEl.getElementsByTagName(XML_GPS);
                for (int j = 0; j < 1; j++) {
                    Element gpsEl = (Element) gpsNode.item(j);

                    NodeList lonNode = gpsEl.getElementsByTagName(XML_GPS_LON);
                    NodeList latNode = gpsEl.getElementsByTagName(XML_GPS_LAT);

                    //Lon
                    for (int k = 0; k < 1; k++) {
                        Element lonEl = (Element) lonNode.item(k);
                        this.selectedStore.setGps_lon(getCharacterDataFromElement(lonEl));
                    }
                    //Lat
                    for (int k = 0; k < 1; k++) {
                        Element latEl = (Element) latNode.item(k);
                        this.selectedStore.setGps_lat(getCharacterDataFromElement(latEl));
                    }
                }

                //Address Building Details
                NodeList mainBuildingNode = addressOneNEl.getElementsByTagName(XML_ADDRESS_BUILDING);
                for (int j = 0; j < 1; j++) {
                    Element mainBuildEl = (Element) mainBuildingNode.item(j);

                    NodeList blockNode = mainBuildEl.getElementsByTagName(XML_BLOCK);
                    NodeList buildingNode = mainBuildEl.getElementsByTagName(XML_BUILDING);
                    NodeList floorNode = mainBuildEl.getElementsByTagName(XML_FLOOR);
                    NodeList shopNode = mainBuildEl.getElementsByTagName(XML_SHOP);

                    //Block
                    for (int k = 0; k < 1; k++) {
                        Element blockEl = (Element) blockNode.item(k);
                        this.selectedStore.setAddress_block(getCharacterDataFromElement(blockEl));
                    }
                    //building
                    for (int k = 0; k < 1; k++) {
                        Element buildingEl = (Element) buildingNode.item(k);
                        this.selectedStore.setAddress_building(getCharacterDataFromElement(buildingEl));
                    }
                    //floor
                    for (int k = 0; k < 1; k++) {
                        Element floorEl = (Element) floorNode.item(k);
                        this.selectedStore.setAddress_floor(getCharacterDataFromElement(floorEl));
                    }
                    //shop
                    for (int k = 0; k < 1; k++) {
                        Element shopEl = (Element) shopNode.item(k);
                        this.selectedStore.setAddress_shopNo(getCharacterDataFromElement(shopEl));
                    }
                }

            }

        } catch (Exception e) {
            //Ignore - Not XML
        }

    }

    /**
     *
     * @return
     */
    private String getAddressXML() {
        if (this.selectedStore == null) {
            this.selectedStore = new StoreEntity(dataSourceGridController);

            return "";
        }
        String xmlOutput = "";
        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        try {
            icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();
            Element mainRootElement = doc.createElementNS(XML_HEADER, XML_ADDRESS1);
            doc.appendChild(mainRootElement);
            //Create Main Address Line Node
            Element addressNode = doc.createElement(XML_ADDRESS);

            addressNode.appendChild(doc.createTextNode(this.selectedStore.getAddress_main()));
            //Append to main XML
            mainRootElement.appendChild(addressNode);
            //Create GPS Node
            Element gpsNode = doc.createElement(XML_GPS);
            Element gpsLonNode = doc.createElement(XML_GPS_LON);
            Element gpsLatNode = doc.createElement(XML_GPS_LAT);
            //Append GPS Nodes
            gpsLonNode.appendChild(doc.createTextNode(this.selectedStore.getGps_lon()));
            gpsLatNode.appendChild(doc.createTextNode(this.selectedStore.getGps_lat()));
            gpsNode.appendChild(gpsLonNode);
            gpsNode.appendChild(gpsLatNode);
            //Append to main XML
            mainRootElement.appendChild(gpsNode);
            //Create Building Node
            Element buildingDetailNode = doc.createElement(XML_ADDRESS_BUILDING);
            Element blockNode = doc.createElement(XML_BLOCK);
            Element buildingNode = doc.createElement(XML_BUILDING);
            Element floorNode = doc.createElement(XML_FLOOR);
            Element shopNode = doc.createElement(XML_SHOP);
            //Append Building Nodes
            blockNode.appendChild(doc.createTextNode(this.selectedStore.getAddress_block()));
            buildingNode.appendChild(doc.createTextNode(this.selectedStore.getAddress_building()));
            floorNode.appendChild(doc.createTextNode(this.selectedStore.getAddress_floor()));
            shopNode.appendChild(doc.createTextNode(this.selectedStore.getAddress_shopNo()));
            //Add address buildingdetails
            buildingDetailNode.appendChild(blockNode);
            buildingDetailNode.appendChild(buildingNode);
            buildingDetailNode.appendChild(floorNode);
            buildingDetailNode.appendChild(shopNode);
            //Append to main XML
            mainRootElement.appendChild(buildingDetailNode);

            // output DOM XML to String to Save 
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "no");
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, result);
            String xmlOutputAddress = result.getWriter().toString();

            xmlOutput = xmlOutputAddress;

        } catch (Exception e) {
            Throwable cause = e.getCause();
            String msg = "";

            if (cause != null) {
                msg = cause.getLocalizedMessage();
            }

            JsfUtil.addErrorMessage("Exception : " + msg + " : " + e.getMessage());

            Logger.getLogger(StoresController.class.getName()).log(Level.SEVERE, null, e);
        }
        return xmlOutput;
    }

    /**
     *
     */
    public void create() {
        LoadUserDetails();
        
        Connection conn = null;
        try {
            //Connection Setup
            conn = getMySqlConnection();

            //Only do create if store details are loaded            
            if (this.selectedStore != null) {
                //Combine all address details into XML String  

                this.selectedStore.setAddress1(getAddressXML());

                //SQL Call variables
                CallableStatement cs;
                String resultParam = "";
                String sqlProc = " ";

                try {
                    //Start Transaction
                    conn.setAutoCommit(false);

                    //Add Address first to get address ID
                    sqlProc = "{  CALL addStellrAddress( ?, ?, ?, ?, ? , ?, ?   )  }";

                    cs = conn.prepareCall(sqlProc);

                    cs.setString(1, this.selectedStore.getAddress1());
                    cs.setString(2, this.selectedStore.getRegion());
                    cs.setString(3, this.selectedStore.getCity());
                    cs.setString(4, this.selectedStore.getPostalcode());
                    cs.setString(5, this.selectedStore.getCountry());
                    cs.setString(6, this.userName);

                    cs.registerOutParameter(7, java.sql.Types.VARCHAR);

                    cs.execute();

                    //Error or Address ID
                    resultParam = cs.getString(7);

                    //If error message - Long.parseLong will through exception
                    long addressID = Long.parseLong(resultParam);
                    this.selectedStore.setAddressid(addressID);

                    //Add Store record
                    sqlProc = "{  CALL addStellrStores( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?  )  }";

                    cs = conn.prepareCall(sqlProc);

                    cs.setString(1, this.distributionpartner_distributionpartnerid);
                    cs.setLong(2, this.selectedStore.getAddressid());
                    cs.setLong(3, this.storetypecode);
                    cs.setString(4, this.selectedStore.getSourceidentifier().trim());
                    cs.setString(5, this.selectedStore.getStorename().trim());
                    cs.setString(6, this.selectedStore.getEmail().trim());
                    cs.setString(7, this.selectedStore.getPhonenumber().trim());
                    cs.setTimestamp(8, new Timestamp((new java.util.Date()).getTime()));
                    cs.setTimestamp(9, null);
                    cs.setBoolean(10, this.selectedStore.isActive());
                    cs.setLong(11, this.selectedStore.getAccountid());
                    cs.setString(12, this.selectedStore.getAccounttype());
                    cs.setString(13, this.userName);

                    cs.registerOutParameter(14, java.sql.Types.VARCHAR);

                    cs.execute();

                    //Error or Store ID
                    resultParam = cs.getString(14);

                    //If error message - Long.parseLong will through exception                
                    this.selectedStore.setStoreid(Long.parseLong(resultParam));

                    //Commit Transaction 
                    conn.commit();

                    JsfUtil.addSuccessMessage("Store Added Successfully");
                } catch (Exception ex) {
                    System.out.println("Store Exception " + ex.getMessage());

                    //Output Exception Message
                    Logger.getLogger(StoresController.class.getName()).log(Level.SEVERE, null, ex + " : " + resultParam);

                    String msg = "";

                    Throwable cause = ex.getCause();

                    if (cause != null) {
                        msg = cause.getLocalizedMessage();
                    }

                    if (msg.length() > 0) {
                        JsfUtil.addErrorMessage("Create Store Failed : " + msg + " : " + resultParam);
                    } else {
                        JsfUtil.addErrorMessage(ex, ("Create Store Failed : " + ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured") + " : " + resultParam));
                    }

                    //RollBack 
                    try {
                        conn.rollback();
                    } catch (Exception e1) {
                        Logger.getLogger(StoresController.class.getName()).log(Level.SEVERE, null, e1 + " : Rollback Exception !!!");
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Connection Exception " + ex.getMessage());

            //Connection Exception Message
            Logger.getLogger(StoresController.class.getName()).log(Level.SEVERE, null, ex + " : Connection Exception");

            String msg = "";

            Throwable cause = ex.getCause();

            if (cause != null) {
                msg = cause.getLocalizedMessage();
            }

            if (msg.length() > 0) {
                JsfUtil.addErrorMessage("Create Store Failed : " + msg + " : Connection Exception");
            } else {
                JsfUtil.addErrorMessage(ex, ("Create Store Failed : " + ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured") + " : Connection Exception"));
            }

        } finally {
            try {
                if (conn != null) {
                    //Close Transaction
                    conn.setAutoCommit(true);

                    //Close Connection
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(StoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!JsfUtil.isValidationFailed()) {
            this.selectedStore = null;
            this.storeItems = null;
        }
    }

    /**
     *
     */
    public void doDelete() {
        LoadUserDetails();
        
        Connection conn = null;

        try {
            //Connection Setup
            conn = getMySqlConnection();

            //Only do delete if store details are loaded
            if (this.selectedStore != null) {
                //SQL Call variables
                CallableStatement cs;
                String resultParam = "";
                String sqlProc = " ";

                try {
                    //Start Transaction
                    conn.setAutoCommit(false);
                    //Add Address first to get address ID
                    sqlProc = "{  CALL deleteStellrStores( ?, ?, ?, ?  )  }";

                    cs = conn.prepareCall(sqlProc);

                    cs.setLong(1, this.selectedStore.getStoreid());
                    cs.setLong(2, this.selectedStore.getAddressid());
                    cs.setString(3, this.userName );

                    cs.registerOutParameter(4, java.sql.Types.VARCHAR);

                    cs.execute();

                    //Error or last Rows Deleted 
                    resultParam = cs.getString(4);

                    //If error message - Long.parseLong will through exception
                    Long.parseLong(resultParam);

                    //Commit Transaction 
                    conn.commit();

                    JsfUtil.addSuccessMessage("Store Deleted Successfully");

                } catch (Exception ex) {
                    System.out.println("exception " + ex);
                    //Output Exception Message
                    Logger.getLogger(StoresController.class.getName()).log(Level.SEVERE, null, ex + " : " + resultParam);

                    String msg = "";

                    Throwable cause = ex.getCause();

                    if (cause != null) {
                        msg = cause.getLocalizedMessage();
                    }

                    if (msg.length() > 0) {
                        JsfUtil.addErrorMessage("Delete Store Failed : " + msg + " : " + resultParam);
                    } else {
                        JsfUtil.addErrorMessage(ex, ("Delete Store Failed : " + ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured") + " : " + resultParam));
                    }

                    //RollBack 
                    try {
                        conn.rollback();
                    } catch (Exception e1) {
                        Logger.getLogger(StoresController.class.getName()).log(Level.SEVERE, null, e1 + " : Delete Rollback Exception !!!");
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("connection error ");
            //Connection Exception Message
            Logger.getLogger(StoresController.class.getName()).log(Level.SEVERE, null, ex + " : Delete Connection Exception");

            String msg = "";

            Throwable cause = ex.getCause();

            if (cause != null) {
                msg = cause.getLocalizedMessage();
            }

            if (msg.length() > 0) {
                JsfUtil.addErrorMessage("Delete Store Failed : " + msg + " : Delete Connection Exception");
            } else {
                JsfUtil.addErrorMessage(ex, ("Delete Store Failed : " + ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured") + " : Connection Exception"));
            }

        } finally {
            try {
                if (conn != null) {
                    //Close Transaction
                    conn.setAutoCommit(true);

                    //Close Connection
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(StoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!JsfUtil.isValidationFailed()) {
            this.selectedStore = null;
            this.storeItems = null;
        }
    }

    /**
     * 
     */
    public void LoadUserDetails() {     
        try {
            userName = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();          
            serverName = FacesContext.getCurrentInstance().getExternalContext().getRequestServerName() ;
        } catch (Exception e) {
            userName = "ExceptionUser";
            serverName = "ExceptionServer";

            //Output Exception Message
            Logger.getLogger(StoresController.class.getName()).log(Level.SEVERE, null, e );

            String msg = "";

            Throwable cause = e.getCause();

            if (cause != null) {
                msg = cause.getLocalizedMessage();
            }

            if (msg.length() > 0) {
                JsfUtil.addErrorMessage("Username  9001 : " + msg );
            } else {
                JsfUtil.addErrorMessage(e, ("Username 9881 : " + e.getMessage() ));
            }
        }
        
    }
    
    /**
     *
     */
    public void update() {
        LoadUserDetails();
        
        Connection conn = null;
        try {
            //Connection Setup
            conn = getMySqlConnection();

            //Only do update if store details are loaded
            if (this.selectedStore != null) {
                //Combine all address details into XML String  
                this.selectedStore.setAddress1(getAddressXML());

                //SQL Call variables
                CallableStatement cs;
                String resultParam = "";
                String sqlProc = " ";

                try {
                    //Start Transaction
                    conn.setAutoCommit(false);

                    //Update Address
                    sqlProc = "{  CALL editStellrAddress( ?, ?, ?, ?, ? , ? , ? , ? )  }";

                    cs = conn.prepareCall(sqlProc);

                    cs.setLong(1, this.selectedStore.getAddressid());
                    cs.setString(2, this.selectedStore.getAddress1());
                    cs.setString(3, this.selectedStore.getRegion());
                    cs.setString(4, this.selectedStore.getCity());
                    cs.setString(5, this.selectedStore.getPostalcode());
                    cs.setString(6, this.selectedStore.getCountry());
                    cs.setString(7, this.userName );

                    cs.registerOutParameter(8, java.sql.Types.VARCHAR);

                    cs.execute();

                    //Error or Rows Updated
                    resultParam = cs.getString(8);

                    //If error message - Long.parseLong will through exception
                    Long.parseLong(resultParam);

                    //Update Store record
                    sqlProc = "{  CALL editStellrStores( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?   )  }";

                    cs = conn.prepareCall(sqlProc);

                    cs.setLong(1, this.selectedStore.getStoreid());
                    cs.setString(2, this.distributionpartner_distributionpartnerid);
                    cs.setLong(3, this.selectedStore.getAddressid());
                    cs.setLong(4, this.storetypecode);
                    cs.setString(5, this.selectedStore.getSourceidentifier());
                    cs.setString(6, this.selectedStore.getStorename());
                    cs.setString(7, this.selectedStore.getEmail());
                    cs.setString(8, this.selectedStore.getPhonenumber());
                    cs.setTimestamp(9, new Timestamp((new java.util.Date()).getTime()));
                    cs.setTimestamp(10, null);
                    cs.setBoolean(11, this.selectedStore.isActive());
                    cs.setLong(12, this.selectedStore.getAccountid());
                    cs.setString(13, this.selectedStore.getAccounttype());
                    cs.setString(14, this.userName );

                    cs.registerOutParameter(15, java.sql.Types.VARCHAR);

                    cs.execute();

                    //Error or Rows Updated - should be one 
                    resultParam = cs.getString(15);

                    //If error message - Long.parseLong will through exception                
                    Long.parseLong(resultParam);

                    //Commit Transaction 
                    conn.commit();

                    JsfUtil.addSuccessMessage("Store Updated Successfully");

                } catch (Exception ex) {
                    //Output Exception Message
                    Logger.getLogger(StoresController.class.getName()).log(Level.SEVERE, null, ex + " : " + resultParam);

                    String msg = "";

                    Throwable cause = ex.getCause();

                    if (cause != null) {
                        msg = cause.getLocalizedMessage();
                    }

                    if (msg.length() > 0) {
                        JsfUtil.addErrorMessage("Update Store Failed 1001 : " + msg + " : " + resultParam);
                    } else {
                        JsfUtil.addErrorMessage(ex, ("Update Store Failed 2002 : " + ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured") + " : " + resultParam));
                    }

                    //RollBack 
                    try {
                        conn.rollback();
                    } catch (Exception e1) {
                        Logger.getLogger(StoresController.class.getName()).log(Level.SEVERE, null, e1 + " : Store Update - Rollback Exception !!!");
                    }
                }
            }
        } catch (Exception ex) {
            //Connection Exception Message
            Logger.getLogger(StoresController.class.getName()).log(Level.SEVERE, null, ex + " : Store Update - Connection Exception");

            String msg = "";

            Throwable cause = ex.getCause();

            if (cause != null) {
                msg = cause.getLocalizedMessage();
            }

            if (msg.length() > 0) {
                JsfUtil.addErrorMessage("Update Store Failed : " + msg + " : Store Update - Connection Exception");
            } else {
                JsfUtil.addErrorMessage(ex, ("Update Store Failed : " + ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured") + " : Store Update - Connection Exception"));
            }

        } finally {
            try {
                if (conn != null) {
                    //Close Transaction
                    conn.setAutoCommit(true);

                    //Close Connection
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(StoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!JsfUtil.isValidationFailed()) {
            this.selectedStore = null;
            this.storeItems = null;
        }
    }

    /**
     * @return the distribution parters
     */
    public List<DPEntity> getDps() {

        //Connection details    
        Connection conn = null;
        CallableStatement cs;
        ResultSet rs;

        try {
            String returnMessage = "";

            DPEntity singleItem;

            dps = new ArrayList<>();

            conn = getMySqlConnection();

            String sqlProc = "{  CALL getStellrDistributionPartnerNames( ? )  }";

            cs = conn.prepareCall(sqlProc);
            cs.registerOutParameter(1, java.sql.Types.VARCHAR);

            cs.execute();

            returnMessage = cs.getString(1);

            rs = cs.getResultSet();

            // Fetch each row from the result set
            while (rs.next()) {
                singleItem = new DPEntity();
                singleItem.setDataSourceGridController(dataSourceGridController);

                singleItem.setDistributionpartnerid(rs.getString("distributionpartnerid"));
                singleItem.setName(rs.getString("name"));
                singleItem.setReturnMessage(returnMessage);

                dps.add(singleItem);
            }

            if (returnMessage != null && !returnMessage.trim().isEmpty()) {
                throw new Exception(returnMessage);
            }

        } catch (Exception ex) {
            Throwable cause = ex.getCause();
            String msg = "";

            if (cause != null) {
                msg = cause.getLocalizedMessage();
            }

            JsfUtil.addErrorMessage("Exception : " + msg + " : " + ex.getMessage());

            Logger.getLogger(StoresController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(StoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return dps;

    }
 
   
    /**
     * @return the storeItems
     */
    public List<StoreEntity> getStoreItems() {
        //Connection details    
        Connection conn = null;
        CallableStatement cs;
        ResultSet rs;

        try {
            String returnMessage = "";

            StoreEntity singleItem;

            storeItems = new ArrayList<>();

            conn = getMySqlConnection();

            String sqlProc = "{  CALL getStellrStores( ? )  }";

            cs = conn.prepareCall(sqlProc);
            cs.registerOutParameter(1, java.sql.Types.VARCHAR);

            cs.execute();

            returnMessage = cs.getString(1);

            rs = cs.getResultSet();

            // Fetch each row from the result set
            while (rs.next()) {
                singleItem = new StoreEntity(dataSourceGridController);

                singleItem.setStoreid(rs.getLong("storeid"));
                singleItem.setDistributionpartner_distributionpartnerid(rs.getString("distributionpartner_distributionpartnerid"));
                singleItem.setAddress_addressid(rs.getLong("address_addressid"));
                singleItem.setStoretype_storetypecode(rs.getLong("storetype_storetypecode"));
                singleItem.setSourceidentifier(rs.getString("sourceidentifier"));
                singleItem.setStorename(rs.getString("name"));

                singleItem.setReturnMessage(returnMessage);

                singleItem.setEmail(rs.getString("email"));
                singleItem.setPhonenumber(rs.getString("phonenumber"));
                singleItem.setCreateddate(rs.getTimestamp("createddate"));
                singleItem.setUpdatedate(rs.getTimestamp("updatedate"));
                singleItem.setActive(rs.getBoolean("active"));
                singleItem.setAccountid(rs.getLong("accountid"));
                singleItem.setAccounttype(rs.getString("accounttype"));

                singleItem.setDistName(rs.getString("distName"));
                singleItem.setStypeDesc(rs.getString("stypeDesc"));

                singleItem.setAddressid(rs.getLong("address_addressid"));
                singleItem.setAddress1(rs.getString("address1"));
                singleItem.setAddress_main(rs.getString("address1"));
                singleItem.setAddress_block("");
                singleItem.setAddress_building("");
                singleItem.setAddress_floor("");
                singleItem.setAddress_shopNo("");
                singleItem.setGps_lon("");
                singleItem.setGps_lat("");
                singleItem.setRegion(rs.getString("region"));
                singleItem.setCity(rs.getString("city"));
                singleItem.setPostalcode(rs.getString("postalcode"));
                singleItem.setCountry(rs.getString("country"));

                storeItems.add(singleItem);
            }

            if (returnMessage != null && !returnMessage.trim().isEmpty()) {
                throw new Exception(returnMessage);
            }

        } catch (Exception ex) {
            Throwable cause = ex.getCause();
            String msg = "";

            if (cause != null) {
                msg = cause.getLocalizedMessage();
            }

            JsfUtil.addErrorMessage("Exception : " + msg + " : " + ex.getMessage());

            Logger.getLogger(StoresController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(StoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return storeItems;
    }

    /**
     * @param storeItems the storeItems to set
     */
    public void setStoreItems(List<StoreEntity> storeItems) {
        this.storeItems = storeItems;
    }

    /**
     * @return the filteredStoreItems
     */
    public List<StoreEntity> getFilteredStoreItems() {
        return filteredStoreItems;
    }

    /**
     * @param filteredStoreItems the filteredStoreItems to set
     */
    public void setFilteredStoreItems(List<StoreEntity> filteredStoreItems) {
        this.filteredStoreItems = filteredStoreItems;
    }

    /**
     * @return the selectedStore
     */
    public StoreEntity getSelectedStore() {
        return selectedStore;
    }

    /**
     * @param selectedStore the selectedStore to set
     */
    public void setSelectedStore(StoreEntity selectedStore) {
        try {
            this.selectedStore = selectedStore;
        } catch (Exception e1) {
        }

        try { //This is uded from these fieldsas it would be loaded from the selection boxes
            this.distributionpartner_distributionpartnerid = this.selectedStore.getDistributionpartner_distributionpartnerid();
        } catch (Exception e1) {
        }

        try { //This is uded from these fieldsas it would be loaded from the selection boxes
            this.storetypecode = this.selectedStore.getStoretype_storetypecode();
        } catch (Exception e1) {
        }

        //Split Address 1 Details to the different fields if it contains XML
        try {
            if (this.selectedStore.getAddress1() != null
                    && this.selectedStore.getAddress1().contains("<")
                    && this.selectedStore.getAddress1().contains(">")) {
                splitXMLAddressToSelected(this.selectedStore.getAddress1());
            }
        } catch (Exception e1) {
            //ignore - XML is not valid            
        }
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
     * @return the stps
     */
    public List<StoreTypeEntity> getStps() {

        //Connection details    
        Connection conn = null;
        CallableStatement cs;
        ResultSet rs;

        try {
            String returnMessage = "";

            StoreTypeEntity singleItem;

            stps = new ArrayList<>();

            conn = getMySqlConnection();

            String sqlProc = "{  CALL getStellrStoreType( ? )  }";

            cs = conn.prepareCall(sqlProc);
            cs.registerOutParameter(1, java.sql.Types.VARCHAR);

            cs.execute();

            returnMessage = cs.getString(1);

            rs = cs.getResultSet();

            // Fetch each row from the result set
            while (rs.next()) {
                singleItem = new StoreTypeEntity();
                singleItem.setDataSourceGridController(dataSourceGridController);

                singleItem.setReturnMessage(returnMessage);

                singleItem.setStoretypecode(rs.getLong("storetypecode"));
                singleItem.setStoretypedescription(rs.getString("storetypedescription"));

                stps.add(singleItem);
            }

            if (returnMessage != null && !returnMessage.trim().isEmpty()) {
                throw new Exception(returnMessage);
            }

        } catch (Exception ex) {
            Throwable cause = ex.getCause();
            String msg = "";

            if (cause != null) {
                msg = cause.getLocalizedMessage();
            }

            JsfUtil.addErrorMessage("Exception : " + msg + " : " + ex.getMessage());

            Logger.getLogger(StoresController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(StoresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return stps;
    }

    /**
     * @param stps the stps to set
     */
    public void setStps(List<StoreTypeEntity> stps) {
        this.stps = stps;
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
}
