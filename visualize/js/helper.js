/* This file contains varions tools for dealing with javascript data*/





/**      It's parsing String in this format 18-06-2008 22:51 
and creating a JS Date object.
 */
function parseDate(csvDateString){
    // nGramDateRawString = data[i]["Data"];
    // var dateParts = csvDateString.split(" ");
    var dateYearPart = csvDateString.split("-");
    // var dateHourPart = dateParts[1].split(":");
    var year = dateYearPart[0];
    var month = dateYearPart[1];
    var day = dateYearPart[2];
    //var hour = dateHourPart[0];
   // var minutes = dateHourPart[1];

    nGramDate = new Date(year, month - 1, day);
    // nGramDate = new Date(year, month - 1, day, hour, minutes, 0, 0);
    return nGramDate;
}