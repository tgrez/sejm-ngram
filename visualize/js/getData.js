function hello(){
    alert("Hello World!");
}

/* 
data - rows with data
...
*/
function getNGramsList(data, startDate, stopDate, step, party){

    ngram = "Polska jest super";
    // console.log(data);

    var nGramNumbers = new Array();

    msPeriod = stopDate - startDate; //miliseconds between start and stop date
    msStep = msPeriod / step;

    // console.log("msStep:" + msStep)
    for (var i=0,len=data.length; i<len; i++){

          // console.log("bierzemy!");
        // console.log(data[i]);
        if(data[i]["Partia"] != party) continue;
        if(data[i]["Ngram"] != ngram) continue;



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

//     var numeric_array = [];
//     for ( var item in nGramNumbers ){
//         numeric_array.push( nGramNumbers[ item ] );
// }


    return nGramNumbers;
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