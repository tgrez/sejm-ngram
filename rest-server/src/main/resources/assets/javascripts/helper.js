/* This file contains varions tools for dealing with javascript data*/



/**
    Returns a string with date 
*/
function formatDate(jsDate, includeHours){
    var stringDate = jsDate.getFullYear() + "-" + (jsDate.getMonth() +1) + "-" + jsDate.getDate();                
    // var stringDateTo = stopDate.getFullYear() + "-" + (stopDate.getMonth() +1) + "-" + stopDate.getDate(); 
    if(includeHours){
        var hours = jsDate.getHours()
        var minutes = jsDate.getMinutes();
        minutes = checkTime(minutes); //adding 0 where appropriate
        stringDate = stringDate + " " + hours + ":" + minutes;
    }
    return stringDate;
}


/* adds 0 to minutes / seconds (since js date.getMinutes / .getSeconds returns 5 instead of 05 ..*/
function checkTime(i){
    if (i<10){
      i="0" + i;
    }
    return i;
}

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

/* Function for replacing spaces from 2- and mode- grams into %20*/
function checkNgramUrlSpaces(ngram){
    return ngram.replace(/\s/g,"%20");
}