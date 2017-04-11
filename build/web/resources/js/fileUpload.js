 var dropzoneInstance = null; 
 
 Dropzone.options.dropForm = {
  paramName: "file", // The name that will be used to transfer the file
  maxFilesize: 10, // MB
  parallelUploads: 1,
  uploadMultiple: false,
  maxFiles: 1,
  
  accept: function(file, done) {
    if (file.name == "none.none.none") {
      done("Exception test");
    } else { 
      done(); 
    }
  } ,
  
  init: function() { 
      dropzoneInstance = Dropzone.forElement("#dropForm");
      
      this.on("drop", function(file) { 
          dropzoneInstance.removeAllFiles();
      });
      
       this.on("success", function(file) { 
           
           setTimeout(function(){ rcall(); }, 3000);
           
       });
      
  } 
  
};

 
 
