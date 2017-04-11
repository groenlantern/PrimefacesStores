DROP table IF EXISTS `StoreAudit`;

CREATE TABLE `StoreAudit` (
  `storeAuditID` bigint(20) NOT NULL AUTO_INCREMENT,
  `storeID` bigint(20) NOT NULL,
  `addressID` bigint(20) NULL,
  `storeName` varchar(150) DEFAULT NULL,
  `userName` varchar(100) DEFAULT NULL,
  `transactionType` varchar(100) DEFAULT NULL,
  `beforeUpdate` varchar(2200) DEFAULT NULL,
  `afterUpdate` varchar(2200) DEFAULT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`storeAuditID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


 
DELIMITER $$
DROP PROCEDURE IF EXISTS `getStellrStores`$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getStellrStores`( OUT ret VARCHAR(255) )
BEGIN

	DECLARE code CHAR(5) DEFAULT '00000';
	DECLARE msg TEXT;

	DECLARE EXIT HANDLER FOR
		SQLEXCEPTION
			BEGIN
				GET DIAGNOSTICS CONDITION 1
				code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
				SELECT CONCAT('Error getStellrStores ( ) code: ', code, ' message: ', msg) INTO ret;
            END;

 select a.*,
        coalesce(b.name,a.distributionpartner_distributionpartnerid) as distName,
        coalesce(c.storetypedescription,a.storetype_storetypecode) as stypeDesc,
        d.address1 ,
		d.region ,
		d.city,
		d.postalcode,
		d.country
 from store as a 
 left join distributionpartner as b on b.distributionpartnerid = a.distributionpartner_distributionpartnerid
 left join storetype as c on c.storetypecode = a.storetype_storetypecode
 left join address as d on d.addressid = a.address_addressid
 order by a.name;                       
    
    
END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS `addStellrStores`$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `addStellrStores`(	IN distributionpartnerid varchar(15),
																IN addressid bigint(20), 
																IN storetypecode bigint(20), 
																IN sourceidentifier varchar(16), 
																IN storeName varchar(45), 
																IN email varchar(45), 
																IN phonenumber varchar(45), 
																IN createddate timestamp, 
																IN updatedate timestamp, 
																IN active tinyint(1), 
																IN accountid int(11), 
																IN accounttype varchar(45), 
                                                                IN userName varchar(150), 
																OUT ret VARCHAR(2000) )
BEGIN	
	DECLARE storeID BIGINT(20) DEFAULT 0;
    DECLARE beforeRecord varchar(2100);
    DECLARE afterRecord varchar(2100);
    
	DECLARE code CHAR(5) DEFAULT '00000';
	DECLARE msg TEXT;

	DECLARE EXIT HANDLER FOR
		SQLEXCEPTION
			BEGIN
				GET DIAGNOSTICS CONDITION 1
				code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
				SELECT CONCAT('Error Inserting Store (',storeName,') code: ', code, ' message: ', msg) INTO ret;
            END;

    IF (active IS NULL) THEN
		set active = false;
    END IF;
    
    IF (storeName IS NULL) THEN
		SELECT CONCAT('Error Inserting Store - Null Name is Not Allowed ' ) INTO ret;
    END IF;
    
    IF ( rtrim(ltrim(storeName)) = '' ) THEN
		SELECT CONCAT('Error Inserting Store  - Blank Name is Not Allowed ' ) INTO ret;
    END IF;

    IF (userName IS NULL) THEN
		SELECT CONCAT('Error Inserting Store - Null userName is Not Allowed ' ) INTO ret;
    END IF;
    
    IF ( rtrim(ltrim(userName)) = '' ) THEN
		SELECT CONCAT('Error Inserting Store  - Blank userName is Not Allowed ' ) INTO ret;
    END IF;
    
    IF ( ret IS NULL OR rtrim(ltrim(ret)) = '' )
	THEN  	
		select '' INTO beforeRecord;
            
		INSERT INTO store ( `distributionpartner_distributionpartnerid`,`address_addressid` , `storetype_storetypecode` ,
							 `sourceidentifier` , `name`  , `email`  , `phonenumber` , `createddate`, `updatedate` ,
							 `active` , `accountid` , `accounttype` )
		VALUES ( distributionpartnerid , addressid , storetypecode , sourceidentifier , 
				 storeName , email , phonenumber , createddate , updatedate , 
				 active , accountid , accounttype );

		SET storeID = LAST_INSERT_ID(); 
	    
		IF (storeID < 1) THEN
			SELECT CONCAT('Error Inserting Store (', storeName, ') ' ) INTO ret;
		ELSE
			SELECT storeID INTO ret;
            
			call getStoreAddressDataAsString(storeID ,afterRecord);
    
			call doStoresAuditTrail(  storeID, addressid , storeName, userName,  'CREATE STORE', 
			     beforeRecord, afterRecord, @outt );
            
		END IF;
	END IF;
	
END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS `addStellrAddress`$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `addStellrAddress`(	IN address1 varchar(1024),
																IN region varchar(45), 
																IN city varchar(45), 
																IN postalcode varchar(12), 
																IN country varchar(45), 
																IN userName varchar(150), 
																OUT ret VARCHAR(2000) )
BEGIN	
	DECLARE addressID BIGINT(20) DEFAULT 0;
	DECLARE code CHAR(5) DEFAULT '00000';
	DECLARE msg TEXT;
    DECLARE beforeRecord varchar(2100);
    DECLARE afterRecord varchar(2100);

	DECLARE EXIT HANDLER FOR
		SQLEXCEPTION
			BEGIN
				GET DIAGNOSTICS CONDITION 1
				code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
				SELECT CONCAT('Error Inserting Address (',address1,') code: ', code, ' message: ', msg) INTO ret;
            END;

    IF (address1 IS NULL) THEN
		SELECT CONCAT('Error Inserting Address - Null address is Not Allowed ' ) INTO ret;
    END IF;
    
    IF ( rtrim(ltrim(address1)) = '' ) THEN
		SELECT CONCAT('Error Inserting Address  - Blank address is Not Allowed ' ) INTO ret;
    END IF;
    
    IF ( ret IS NULL OR rtrim(ltrim(ret)) = '' )
	THEN  	
		select '' INTO beforeRecord;
        
		INSERT INTO address ( `address1`,`region` , `city` , `postalcode` , `country` )
		VALUES (               address1 , region ,   city ,   postalcode ,   country  );

		SET addressID = LAST_INSERT_ID(); 
	    
		IF (addressID < 1) THEN
			SELECT CONCAT('Error Inserting Address (', address1, ') ' ) INTO ret;
		ELSE
			SELECT addressID INTO ret;
                        
			call getAddressDataAsString(addressID,afterRecord);
			
			call doStoresAuditTrail( 0, addressID , 'Address Only', userName,  'CREATE ADDRESS', 
			     beforeRecord, afterRecord, @outt );
                 
		END IF;
	END IF;
	
END$$
DELIMITER ;
 

DELIMITER $$
DROP PROCEDURE IF EXISTS `editStellrStores`$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `editStellrStores`(	IN updateID bigint(20),
																IN distributionpartnerid varchar(15),
																IN addressid bigint(20), 
																IN storetypecode bigint(20), 
																IN sourceidentifier varchar(16), 
																IN storeName varchar(45), 
																IN email varchar(45), 
																IN phonenumber varchar(45), 
																IN createddate timestamp, 
																IN updatedate timestamp, 
																IN active tinyint(1), 
																IN accountid int(11), 
																IN accounttype varchar(45),
                                                                IN userName varchar(150), 
																OUT ret VARCHAR(1024) )
BEGIN	
	DECLARE storeRows BIGINT(20) DEFAULT 0;
	DECLARE code CHAR(5) DEFAULT '00000';
	DECLARE msg TEXT;
    DECLARE beforeRecord varchar(2100);
    DECLARE afterRecord varchar(2100);

	DECLARE EXIT HANDLER FOR
		SQLEXCEPTION
			BEGIN
				GET DIAGNOSTICS CONDITION 1
				code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
				SELECT CONCAT('Error Editing Store 1003 (',updateID , ' - ' , storeName,') code: ', code, ' message: ', msg) INTO ret;
            END;
            	
    IF (active IS NULL) THEN
		set active = false;
    END IF;

   
    IF (storeName IS NULL) THEN
		SELECT CONCAT('Error Updating Store - Null store Name is Not Allowed ' ) INTO ret;
    END IF;
    
    IF ( rtrim(ltrim(storeName)) = '' ) THEN
		SELECT CONCAT('Error Updating Store - Blank store Name is Not Allowed ' ) INTO ret;
    END IF;
    
    IF ( ret IS NULL OR rtrim(ltrim(ret)) = '' )
	THEN  			 
	    call getStoreAddressDataAsString(updateID ,beforeRecord);
            
		UPDATE store SET `distributionpartner_distributionpartnerid` = distributionpartnerid ,
						 `address_addressid` = addressid , 
						 `storetype_storetypecode` = storetypecode ,
						 `sourceidentifier` = sourceidentifier , 
						 `name` = storeName , 
						 `email` =  email , 
						 `phonenumber` = phonenumber , 
						 `createddate` = createddate , 
						 `updatedate` = updatedate,
						 `active` = active , 
						 `accountid` =accountid , 
						 `accounttype` = accounttype 

		WHERE storeid = updateID;

		SELECT ROW_COUNT() into storeRows;
			
		IF (storeRows < 1) THEN
			SELECT CONCAT('Error Editing Store 20012 (', storeRows, ' : ', updateID , ' - ' , storeName, ') ' ) INTO ret;
		ELSE
			SELECT storeRows INTO ret;
            
            call getStoreAddressDataAsString(updateID ,afterRecord);
    
			call doStoresAuditTrail( updateID, addressid , storeName, userName,  'EDIT STORE', 
			     beforeRecord, afterRecord, @outt );
                 
		END IF;            
         
	END IF;
	
END$$
DELIMITER ;


DELIMITER $$
DROP PROCEDURE IF EXISTS `editStellrAddress`$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `editStellrAddress`(	IN updateID bigint(20),
																	IN address1 varchar(1024),
																	IN region varchar(45), 
																	IN city varchar(45), 
																	IN postalcode varchar(12), 
																	IN country varchar(45), 	
                                                                    IN userName varchar(150), 
																	OUT ret VARCHAR(1024) )
BEGIN	
	DECLARE addressRows BIGINT(20) DEFAULT 0;
	DECLARE code CHAR(5) DEFAULT '00000';
	DECLARE msg TEXT;
    DECLARE beforeRecord varchar(2100);
    DECLARE afterRecord varchar(2100);

	DECLARE EXIT HANDLER FOR
		SQLEXCEPTION
			BEGIN
				GET DIAGNOSTICS CONDITION 1
				code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
				SELECT CONCAT('Error Editing Address SQL Exception (',updateID , ' - ' , address1,') code: ', code, ' message: ', msg) INTO ret;
            END;
            	
    IF (address1 IS NULL) THEN
		SELECT CONCAT('Error Updating Address - Null store address is Not Allowed ' ) INTO ret;
    END IF;
    
    IF ( rtrim(ltrim(address1)) = '' ) THEN
		SELECT CONCAT('Error Updating Address - Blank store Name is Not Allowed ' ) INTO ret;
    END IF;
    
    IF ( ret IS NULL OR rtrim(ltrim(ret)) = '' )
	THEN  			 
        call getAddressDataAsString(updateID ,beforeRecord);
        
		UPDATE address SET `address1` = address1 ,
							`region` = region , 
							`city` = city ,
							`postalcode` = postalcode , 
							`country` = country 
                         
		WHERE addressid = updateID;

		SELECT ROW_COUNT() into addressRows;
			
		IF (addressRows < 1) THEN
			SELECT CONCAT('Error Editing Address Rows Updated (', updateID , ' - ' , address1, ') ' ) INTO ret;
		ELSE
			SELECT addressRows INTO ret;
            
            call getAddressDataAsString(updateID,afterRecord);
			
			call doStoresAuditTrail( 0, updateID , 'Address Only', userName,  'EDIT ADDRESS', 
			     beforeRecord, afterRecord, @outt );
                 
		END IF;            
         
	END IF;
	
END$$
DELIMITER ;
 

DELIMITER $$
DROP PROCEDURE IF EXISTS `getStellrDistributionPartners`$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getStellrDistributionPartners`( OUT ret VARCHAR(255) )
BEGIN

	DECLARE code CHAR(5) DEFAULT '00000';
	DECLARE msg TEXT;

	DECLARE EXIT HANDLER FOR
		SQLEXCEPTION
			BEGIN
				GET DIAGNOSTICS CONDITION 1
				code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
				SELECT CONCAT('Error getStellrDistributionPartners ( ) code: ', code, ' message: ', msg) INTO ret;
            END;

 select a.*            
 from distributionpartner as a 
 order by name;                       
    
END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS `getStellrDistributionPartnerNames`$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getStellrDistributionPartnerNames`( OUT ret VARCHAR(255) )
BEGIN

	DECLARE code CHAR(5) DEFAULT '00000';
	DECLARE msg TEXT;

	DECLARE EXIT HANDLER FOR
		SQLEXCEPTION
			BEGIN
				GET DIAGNOSTICS CONDITION 1
				code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
				SELECT CONCAT('Error getStellrDistributionPartnerNames ( ) code: ', code, ' message: ', msg) INTO ret;
            END;

 select a.distributionpartnerid, 
		a.name
 from distributionpartner as a 
 order by name;                       
    
END$$
DELIMITER ;


DELIMITER $$
DROP PROCEDURE IF EXISTS `getStellrStoreType`$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getStellrStoreType`( OUT ret VARCHAR(255) )
BEGIN

	DECLARE code CHAR(5) DEFAULT '00000';
	DECLARE msg TEXT;

	DECLARE EXIT HANDLER FOR
		SQLEXCEPTION
			BEGIN
				GET DIAGNOSTICS CONDITION 1
				code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
				SELECT CONCAT('Error getStellrStoreType ( ) code: ', code, ' message: ', msg) INTO ret;
            END;

 select a.storetypecode, 
		a.storetypedescription
 from storetype as a 
 order by storetypedescription;                       
    
END$$
DELIMITER ;


DELIMITER $$
DROP PROCEDURE IF EXISTS `deleteStellrStores`$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `deleteStellrStores`(	IN delStoreID bigint(20),
																	IN delAddressid bigint(20),
                                                                    IN userName varchar(150), 
																	OUT ret VARCHAR(1024) )
BEGIN	
	DECLARE storeRows BIGINT(20) DEFAULT 0;
	DECLARE code CHAR(5) DEFAULT '00000';
	DECLARE msg TEXT;
    DECLARE beforeRecord varchar(2100);
    DECLARE afterRecord varchar(2100);

	DECLARE EXIT HANDLER FOR
		SQLEXCEPTION
			BEGIN
				GET DIAGNOSTICS CONDITION 1
				code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
				SELECT CONCAT('Error Deleting Store (',updateID , ' - ' , delStoreID, ' - ' , delAddressid,') code: ', code, ' message: ', msg) INTO ret;
            END;
            	
    IF (delStoreID IS NULL) THEN
		SELECT CONCAT('Error Deleting Store - Null store ID is Not Allowed ' ) INTO ret;
    END IF;
    
    IF ( rtrim(ltrim(delStoreID)) = '' ) THEN
		SELECT CONCAT('Error Deleting Store - Blank store ID is Not Allowed ' ) INTO ret;
    END IF;
    
    IF ( ret IS NULL OR rtrim(ltrim(ret)) = '' )
	THEN  			 
		call getStoreAddressDataAsString(delStoreID ,beforeRecord);
        
        DELETE FROM store 
		WHERE storeid = delStoreID;

		SELECT ROW_COUNT() into storeRows;
			
		IF (storeRows < 1) THEN
			SELECT CONCAT('Error Deleting Store (', storeRows, ' : ', delStoreID , ' - ' , delAddressid, ') ' ) INTO ret;
		ELSE
			SELECT storeRows INTO ret;
               
		    IF (delAddressid IS NOT NULL) THEN
				IF ( rtrim(ltrim(delAddressid)) != '' ) THEN			 
                    
					DELETE FROM address 
					WHERE addressid = delAddressid;

					SELECT ROW_COUNT() into storeRows;
						
					IF (storeRows < 1) THEN
						SELECT CONCAT('Error Deleting Address (', storeRows, ' : ', delStoreID , ' - ' , delAddressid, ') ' ) INTO ret;
					ELSE
						SELECT storeRows INTO ret;					
						
                        call getStoreAddressDataAsString(delStoreID ,afterRecord);
    
						call doStoresAuditTrail( delStoreID, delAddressid , 'DELETED', userName,  'DELETE STORE', 
							 beforeRecord, afterRecord, @outt );
                 
					END IF;            
                    
				END IF;    	
			END IF;
		END IF;            
         
	END IF;
	
END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS `getStoreAddressDataAsString`$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getStoreAddressDataAsString`( IN upStoreID bigint(20),
																		   OUT ret VARCHAR(2100) )
BEGIN

	DECLARE code CHAR(5) DEFAULT '00000';
	DECLARE msg TEXT;

	DECLARE EXIT HANDLER FOR
		SQLEXCEPTION
			BEGIN
				GET DIAGNOSTICS CONDITION 1
				code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
				SELECT CONCAT('Error getStoreAddressDataAsString ( ) code: ', code, ' message: ', msg) INTO ret;
            END;



  select concat  ( 'Store [', Cast(storeid as char), ':',
                   Cast(distributionpartner_distributionpartnerid as char) , ':',
                   Cast(address_addressid as char) ,':',
				   Cast(storetype_storetypecode as char), ':',
                   coalesce( Cast(sourceidentifier as char), '') , ':',
                   coalesce(name,'') , ':',
                   coalesce ( email,'') ,':',
				   coalesce(phonenumber,'') , ':',
                   Cast(createddate as char) ,  ':',
                   coalesce( Cast(updatedate as char), ''), ':',
                   coalesce( Cast(active as char), ''), ':',
				   coalesce( Cast(accountid as char), ''), ':',
                   coalesce( Cast(accounttype as char), '' ),':',
                   '] Address [' , Cast(`addressid` as char),':',
				   coalesce(`address1` , '' ),':',
				   coalesce(`region` , '' ),':',
				   coalesce(`city` , '' ),':',
				   coalesce(Cast(`postalcode` as char), '' ),':',
				   coalesce(`country`, '' ), ']'                                  ) as outP
  
  from   store, address 
  where  address.addressid = store.address_addressid
  and    storeid = upStoreID INTO ret;
    
END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS `getAddressDataAsString`$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `getAddressDataAsString`( IN addressINID bigint(20),
																	  OUT ret VARCHAR(2100) )
BEGIN

	DECLARE code CHAR(5) DEFAULT '00000';
	DECLARE msg TEXT;

	DECLARE EXIT HANDLER FOR
		SQLEXCEPTION
			BEGIN
				GET DIAGNOSTICS CONDITION 1
				code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
				SELECT CONCAT('Error getAddressDataAsString ( ) code: ', code, ' message: ', msg) INTO ret;
            END;

  select concat  ( 'Address [' , Cast(`addressid` as char),':',
				   coalesce(`address1` , '' ),':',
				   coalesce(`region` , '' ),':',
				   coalesce(`city` , '' ),':',
				   coalesce(Cast(`postalcode` as char), '' ),':',
				   coalesce(`country`, '' ), ']'                                  ) as outP
  
  from   address where addressid = addressINID INTO ret;
    
END$$
DELIMITER ;

DELIMITER $$
DROP PROCEDURE IF EXISTS `doStoresAuditTrail`$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `doStoresAuditTrail`(  IN upStoreID bigint(20),
																   IN upAddressID bigint(20),
																   IN storeName varchar(200), 
																   IN userName varchar(200), 
																   IN transactionType varchar(200), 
																   IN beforeRec varchar(2100), 
                                                                   IN afterRec varchar(2100), 
																   OUT ret VARCHAR(2100) )
BEGIN	
	DECLARE auditID BIGINT(20) DEFAULT 0;
	DECLARE code CHAR(5) DEFAULT '00000';
	DECLARE msg TEXT;

	DECLARE EXIT HANDLER FOR
		SQLEXCEPTION
			BEGIN
				GET DIAGNOSTICS CONDITION 1
				code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;
				SELECT CONCAT('Error Inserting AuditRecord (',upStoreID,') code: ', code, ' message: ', msg) INTO ret;
            END;

    IF (upStoreID IS NULL) THEN
		SELECT CONCAT('Error Inserting Audit - Null update StoreID is Not Allowed ' ) INTO ret;
    END IF;
    
    IF ( rtrim(ltrim(upStoreID)) = '' ) THEN
		SELECT CONCAT('Error Inserting Audit  - Blank update StoreID is Not Allowed ' ) INTO ret;
    END IF;

    IF (upAddressID IS NULL) THEN
		SELECT CONCAT('Error Inserting Audit - Null update AddressID is Not Allowed ' ) INTO ret;
    END IF;
    
    IF ( rtrim(ltrim(upAddressID)) = '' ) THEN
		SELECT CONCAT('Error Inserting Audit  - Blank update AddressID is Not Allowed ' ) INTO ret;
    END IF;

    IF (userName IS NULL) THEN
		SELECT CONCAT('Error Inserting Audit - Null update UserName is Not Allowed ' ) INTO ret;
    END IF;
    
    IF ( rtrim(ltrim(userName)) = '' ) THEN
		SELECT CONCAT('Error Inserting Audit  - Blank update UserName is Not Allowed ' ) INTO ret;
    END IF;

    IF (transactionType IS NULL) THEN
		SELECT CONCAT('Error Inserting Audit - Null update Transaction Type is Not Allowed ' ) INTO ret;
    END IF;
    
    IF ( rtrim(ltrim(transactionType)) = '' ) THEN
		SELECT CONCAT('Error Inserting Audit  - Blank update Transaction Type is Not Allowed ' ) INTO ret;
    END IF;
    
    IF ( ret IS NULL OR rtrim(ltrim(ret)) = '' )
	THEN  	
    
		INSERT INTO StoreAudit ( `storeID`,   `addressID` , `storeName` , `userName` , `transactionType`, `beforeUpdate` , `afterUpdate` )
		VALUES (                  upStoreID , upAddressID ,   storeName ,   userName,    transactionType ,   beforeRec,       afterRec );

		SET auditID = LAST_INSERT_ID(); 
	    
		IF (auditID < 1) THEN
			SELECT auditID;
			SELECT CONCAT('Error Inserting Audit (', upStoreID, ') ' ) INTO ret;
		ELSE
			SELECT auditID INTO ret;
		END IF;
	END IF;
	
END$$
DELIMITER ;
 
