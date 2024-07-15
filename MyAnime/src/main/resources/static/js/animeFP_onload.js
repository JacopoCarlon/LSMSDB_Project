function codeAddress(qstr) {
    alert('ok'+qstr);
}



$(document).ready(function() {
    console.log($(location).attr('href'));
    console.log(window.location.search);
    //'?keyword=aaa'
    var urlParams = window.location.search;
    var parlist = urlParams.replace(/^\?/, '').split('&');
    for (it in parlist){
        var sides = parlist[it].split("=");
        if (sides[0]=="keyword"){
            var t_srvword = sides[1];
            $("#keyword").val(t_srvword);
            break;
        }
    }
    
    console.log(parlist);
    
});


