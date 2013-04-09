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
function getNGramsDatesList(data, startDate, stopDate, step, party, nGramToVisualize){
    var nGramNumbers = new Array();

    for (var i=0,len=data.length; i<len; i++){
        //we want to use only particular Partia and Ngram ...
        if(data[i]["Partia"] != party) continue;
        if(data[i]["Ngram"] != nGramToVisualize) continue;

        nGramDate = parseDate(data[i]["Data"]);

    }
}

/** 
This method is meant to provide an array of [] of objects containing
pairs:
The ngrams are grouped into "boxes" of a equal period from time.
There is "step" number of boxed between "startDate" and "stopDate", each
of a length l = [(stopDate - startDate) / "step"] 
- showAllDays - bool; is TRUE - then ngrams are grouped by each date they occured on
                if FALSE - then ngrams are grouped into "step" number of dates, being spread equally
                from starDate to stopDate

[ 
    {date: "2022-22-22", nrngrams: 45},
    {date: "2022-22-22", nrngrams: 45},
    ...
]
*/
function getNGramsListFromJSON(data, startDate, stopDate, step, partyNr, nGramToVisualize, useAllDays ){

    //will return this stuff, create associative array
    var nGramNumbers = {}

    //create JS dates and calculate relevant periods
    var jsStartDate = parseDate(startDate);
    var jsStopDate = parseDate(stopDate);
    msPeriod = jsStopDate - jsStartDate; //miliseconds between start and stop date
    msStep = msPeriod / step;

    //fill the array with relevant dates associations
    for(var i = 0; i < step; i++ ){
        nGramNumbers[new Date(jsStartDate.getTime() + (i * msStep))] = 0;
    }

    for (var i=0,len=data.length; i<len; i++){

        // Object {klub_id: "2", data: "2011-12-01"}  
        if(data[i].klub_id != partyNr) continue;

        nGramDate = parseDate(data[i].data);

        var dateToPutNgramTo = null;

        //if showAllDAys is TRUE - then each date contained in SQL will be shown separatedly
        // o therwise - ngrams will be show into "step" number of "boxes" spread equally 
        // through the (starDate; stopDate) period
        if(useAllDays){
            dateToPutNgramTo = nGramDate;
        }else{
            //calculte ms betwen ngramdate and stardate
            msBetweenNgram = nGramDate - jsStartDate;
            if(msBetweenNgram > msPeriod){ //in case this date is greater than the first
                console.log("WARN: The date in the set " + nGramDate + " is greater than the stopDate: " + stopDate);
                continue;
            }

             //calculate the nr of segment it should go to -> and dateToPutNGramTo
            if(msBetweenNgram < 0) console.log(" < 0: " + nGramDate );
            nrSegment = msBetweenNgram / msStep;
            nrSegment = Math.floor(nrSegment); //so segment shows the center point, we can calculate date then!
            segmentDate = new Date(jsStartDate.getTime() + (nrSegment * msStep));

            dateToPutNgramTo = segmentDate;
        }

        if(typeof nGramNumbers[dateToPutNgramTo] == 'undefined') nGramNumbers[dateToPutNgramTo] = 0;
        nGramNumbers[dateToPutNgramTo] = nGramNumbers[dateToPutNgramTo] + 1;

    }
    return nGramNumbers;
}








//function getFrom