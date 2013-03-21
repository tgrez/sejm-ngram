function hello(){
    alert("Hello World!");
}

/* 
Deprecated.
data - rows with data
But it was trying to transform all of the points into "sectors"
on the graph.. Which is so stupid and naive and not-using D3 features
check out the getNGramsDatesList() function
*/
function getNGramsList(data, startDate, stopDate, step, party, nGramToVisualize){

    // ngram = "Polska jest super";
    // console.log(data);

    var nGramNumbers = new Array();

    msPeriod = stopDate - startDate; //miliseconds between start and stop date
    msStep = msPeriod / step;

    // console.log("msStep:" + msStep)
    for (var i=0,len=data.length; i<len; i++){

          // console.log("bierzemy!");
        // console.log(data[i]);
        if(data[i]["Partia"] != party) continue;
        if(data[i]["Ngram"] != nGramToVisualize) continue;



         nGramDate = parseDate(data[i]["Data"]);

         //calculte ms betwen ngramdate and stardate
         msBetweenNgram = nGramDate - startDate;
         if(msBetweenNgram > msPeriod){ //in case this date is greater than the first
            console.log("WARN: The date in the set " + nGramDate + " is greater than the stopDate: " + stopDate);
            continue;
         }

         //calculate the nr of segment it should go to 
         // console.log("msBetweenNGram " + msBetweenNgram );
         if(msBetweenNgram < 0) console.log(" < 0: " + nGramDate );
         nrSegment = msBetweenNgram / msStep;

        nrSegment = Math.floor(nrSegment);

         if(typeof nGramNumbers[nrSegment] == 'undefined') nGramNumbers[nrSegment] = 0;
         nGramNumbers[nrSegment] = nGramNumbers[nrSegment] + 1;

         // if(isNaN(nrSegment)) console.log("NaN " + msBetweenNgram + "date " + nGramDate + " strinDate " + data[i]["Data"]);
         // console.log(nrSegment  + " " + nGramDate);
    }

    return nGramNumbers;
}

/** Not really finished, you have to think it over...
    (since you want to group Ngrams into certain 
    fields, at which point should it be done?)
*/
function getNGramsDatesList(data, startDate, stopDate, step, party, nGramToVisualize)){
    var nGramNumbers = new Array();

    for (var i=0,len=data.length; i<len; i++){
        //we want to use only particular Partia and Ngram ...
        if(data[i]["Partia"] != party) continue;
        if(data[i]["Ngram"] != nGramToVisualize) continue;

        nGramDate = parseDate(data[i]["Data"]);

    }
}


/**      It's parsing String in this format 18-06-2008 22:51 
and creating a JS Date object.
 */
function parseDate(csvDateString){
    // nGramDateRawString = data[i]["Data"];
    var dateParts = csvDateString.split(" ");
    var dateYearPart = dateParts[0].split("-");
    var dateHourPart = dateParts[1].split(":");
    var year = dateYearPart[2];
    var month = dateYearPart[1];
    var day = dateYearPart[0];
    var hour = dateHourPart[0];
    var minutes = dateHourPart[1];

    nGramDate = new Date(year, month - 1, day, hour, minutes, 0, 0);
    return nGramDate;
}