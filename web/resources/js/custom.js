/*
 * $Id$ 
 * $Author$
 * $Date$
 *  
 * $Rev$
 *  
 * $LastChangedBy$ 
 * $LastChangedRevision$
 * $LastChangedDate$
 *  
 * $URL$
 *  
 * (C) 2016 Stellr  All Rights Reserved
 *
*/

$(document).ready(function() {
	      $(document).foundation();
	      
	      //add up to date copyright text to footer
	      var dates = new Date();
	      var thisYear = dates.getFullYear();
              var version = "";//getStoresAdminConsoleVersion();
              if(!version) {
                  version = "";
              }
	      $('#footer').html('<h6 class="text-right">Stores Admin Console Administration ' + version + ' | &copy; Stellr ' + thisYear + '</h6>');
	   });