function codeAddress(qstr) {
    alert('ok'+qstr);
}



// input: '?a=b&b=c&e=f'
// output: { a: 'b', b: 'c', e: 'f' }
function buildParameterMap(queryString) {
    // ignore the ? at the beginning and split the query string into 
    // pieces separated by &
    var pairs = queryString.replace(/^\?/, '').split('&');
    var map = {};
  
    pairs.forEach(function(pair) {
      // further split each piece to the left and right of the =
      // ignore pairs that are empty strings
      if (pair) {
        var sides = pair.split('=');
        map[sides[0]] = decodeURIComponent(sides[1]);
      }
    });
  
    return map;
  }
  
  // input: { FilterField0: 'Name', FilterValue0: 'Fred',
  //          FilterField1: 'age', FilterValue1: '30' }
  // output: { name: 'Fred', age: '30' }
function buildFilterFieldMap(parameterMap) {
    var maxFieldCount = 15;
  
    var map = {};
  
    for (var i = 0; i < maxFieldCount; i += 1) {
      var filterFieldName = parameterMap['FilterField' + i];
      if (filterFieldName) {
        map[filterFieldName.toLowerCase()] = parameterMap['FilterValue' + i];
      }
    }
  
    return map;
}

function populateSearchFields(doc, queryString) {
    // build a map from URL query string parameter -> value
    var parameterMap = buildParameterMap(queryString);
  
    // build a map from search field name -> value
    var filterFieldMap = buildFilterFieldMap(parameterMap);
  
    //  for (it in filterFieldMap.keys){
    //      if (it=="keyword"){
    //          var a = document.getElementById(it);
    //          $(a).val(filterFieldMap[it]);
    //      }
    //  }

    if ("keyword" in filterFieldMap.keys ){
        doc.getElementById("keyword").value = filterFieldMap["keyword"];
    }

    Object.keys(filterFieldMap).forEach(function(field) {
      $('#' + field).val(filterFieldMap[field]);
    });
}
  