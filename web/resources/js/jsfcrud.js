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
function handleSubmit(args, dialog) {
    var jqDialog = jQuery('#' + dialog);
    if (args.validationFailed) {
        jqDialog.effect('shake', {times: 3}, 100);
    } else {
        PF(dialog).hide();
    }
}
