// @param requestBody as String
// @param requestHeader as Map<String, String>
// @return a postfix string, to append at the end of the response file name, which content will send back as response
// example:
// var isJson = header["Content-Type"].includes('json');
// var json = JSON.parse(requestBody);
// return isJson ? 'json' : 'xml';
var isJson = requestHeader["Content-Type"].includes('json');
var json = JSON.parse(requestBody);
return isJson ? 'json' : 'xml';
