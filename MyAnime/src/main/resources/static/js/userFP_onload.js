function codeAddress(qstr) {
    alert('ok'+qstr);
}



$(document).ready(function() {
    console.log($(location).attr('href'));
    console.log(window.location.search);
    //'?keyword=aaa'
    let urlParams = window.location.search;
    let parlist = urlParams.replace(/^\?/, '').split('&');
    for (it in parlist){
        let sides = parlist[it].split("=");
        if (sides[0]=="keyword"){
            let t_srvword = sides[1];
            $("#keyword").val(t_srvword);
            break;
        }
    }
    
    console.log(parlist);
    
});


