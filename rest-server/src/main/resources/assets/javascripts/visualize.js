// This file contains all visualizing stuff



/* Some global objects go here!*/
var dataSets = null;

var color_hash = {  "0": "green",
                    "1" :  "orange",
                    "2" : "blue",
                    "3" : "red",
                    "4": "black",
                    "5": "yellow",
                    "6": "violet",
                    "7": "brown"
              }  

/* 
Converts associative array ("key1" => "value1", "key2" => "value2" ... )
to normal one [a, b,c ]
params:
 associativeArray - array that is to be converted
 length  - desired length of array
*/
function convertAssArrayToNormal( associativeArray, length ){
    
    var normalArray = [];
    for(var i = 0; i < length; i++){
        var tmpData = associativeArray[i];
        if(typeof tmpData == 'undefined')
            tmpData = 0;
        normalArray.push(tmpData)
    }
    return normalArray;
}

/* This function creates legend on a basis od global dataSets object 
(assuming it's already loaded). The legend is created in div of id
"ledendDiv"
params:
- partiesIdNames - dict <id, party>
*/
function create_legend(partiesIdNames){
  var legendDiv = document.getElementById("legendDiv");
  
  console.log(partiesIdNames);
  
  //first we remove all of the children of the div
  while (legendDiv.hasChildNodes()) {
    legendDiv.removeChild(legendDiv.lastChild);
  }
  //just to be sure (on stackoverflow some memory leaks were reported)
  legendDiv.innerHTML = "";

  //now we are creating new elements 
  /*
            <div> 
                <input type="checkbox" id= "inputChkUseAllDataPoints" size="25" value="!" onClick="console.log('chbox pressed');" checked />  
                PartyName_1;
            </div>
  */
  for(partyNr in dataSets){
    var partyName = partiesIdNames[partyNr];


    var divTag = document.createElement("div");
      divTag.id = "legendDiv" + partyNr;
      divTag.setAttribute("style", "color:" + color_hash[partyNr]);
      divTag.innerHTML = partyName;
      var inputTag = document.createElement("input");
        inputTag.setAttribute("type", "checkbox");
        inputTag.setAttribute("value", "!");
        inputTag.setAttribute("onClick", "partyLegendCheckBoxClicked(this);");
        inputTag.checked = true;
      divTag.appendChild(inputTag);
    legendDiv.appendChild(divTag); 
  }
}

/**
  This function extracts from JsonConfResponse the entries (partyID : partyName)
  and put all of them as a properties of object that is returned (a "dictionary"
  of <partyId, partyName> is returned)
*/
function extractPartiesId(jsonConfResponse){
  var partiesIdArray = jsonConfResponse.parties_id;
  var partiesIdDict = {};
  for( var i = 0; i < partiesIdArray.length; i++){
    var entryKey = partiesIdArray[i].id;
    var entryVal = partiesIdArray[i].nazwa;
    partiesIdDict[entryKey] = entryVal;
  }
  return partiesIdDict;
}


/* 
This one finds max val in the whole dataSets structure;
It's used for scaling the graph
*/
function getMaxValueFromDataSetS(dataSets){
    var maxVal = 0;

    for (i in dataSets){
        for (j in dataSets[i]["listDates"]){
            var value = dataSets[i]["listDates"][j]["count"];
            if ( value > maxVal) maxVal = value;
        }
    }

    return maxVal;
 }

function onAjaxSuccessVisualize(json) {
    console.log("received:");
    console.log( json);

    dataSets = json["partiesNgramses"];

    console.log("try to visualize");
    var startDate = getMinDateFromDataSet( dataSets );
    var endDate = getMaxDateFromDataSet( dataSets );

    visualize(null, startDate, endDate, null, null);

}

function startJsonApiBasedVisualization(nGramToVisualize) {
    $.ajax({
        url: "service/api/ngram/" + nGramToVisualize,
        cache: false,
        success: onAjaxSuccessVisualize,
        fail: function (json) {
            console.log("error occured!");

            return console.warn(error);
        }
    });
}


/* 
Main visualization function
*/
function startVisualization(nGramToVisualize, datefrom, dateto, xRes){

    console.log("startVisuzalition() method called");
    console.log(twoGrams);

    var step = xRes;
    // var datefrom = "2011-11-08";
    // var dateto = "2011-12-01";
    var ngram = nGramToVisualize;
    var ngramToUrl = checkNgramUrlSpaces(ngram); //method from helper.js

    var optionsString = "?datefrom=" +datefrom+ "&dateto=" + dateto + "&ngram=" + ngramToUrl;
    console.log("URL suffix (for querying SQL DB purposes:) " + optionsString);
    
    var jsonConfRespose = {}; //this will contain response with config (parties - ids)

    console.log("try to query!");
    //first we should query for config data
    d3.text("mysql/queryConfig.php", function(cnfTxt){
      jsonConfRespose = JSON.parse(cnfTxt);
    });


    // here we're launching the php script that queries the mysql and returns json with response
    console.log("start querying the SQL DB..");

    //show loading db giff
    document.getElementById("queryDbInfoDiv").style.display="inline";

    d3.text("mysql/queryNgrams.php" + optionsString, function(txt) {
      console.log("stopped querying the SQL DB..");
      
      //hide loading db gif
      document.getElementById("queryDbInfoDiv").style.display="none";
      
      var jsonResponse = JSON.parse(txt); 
      
      console.log("below JSON response (ngrams queried from SQL)::");
      console.log(jsonResponse);
      // console.log(jsonConfRespose);

      //extract a dict of <partyId, partyName> (needed for labels)
      var partiesIdsNames = extractPartiesId(jsonConfRespose);

      var startDate = datefrom;
      var stopDate = dateto;

      //for each party retrieved from DB (partiesIdsNames) 
      dataSets = new Array();
    //  console.log("party lines!");

      // Here we're processing the data: from entries partyId, date we're creating the dataSets array
      // containing the points for each party. The points are arranged in the manner <date => nrOfNgramOccurencies>
      console.log("processing the retrieved data.. start");

      //show processing the date gif
      document.getElementById("processDataInfoDiv").style.display="inline";

      useAllDays = document.getElementById("inputChkUseAllDataPoints").checked; //checks inputChkUseAllDataPoints checkbox

      for(partyNr in partiesIdsNames){
        dataSets[partyNr] = getNGramsListFromJSON(jsonResponse, startDate,
                        stopDate, step, partyNr, nGramToVisualize, useAllDays);
      }
      console.log("processing the retrieved data.. stop");
      
      //hide processing data gif
      document.getElementById("processDataInfoDiv").style.display="none";

      console.log("DataSets to be visualized (each array element is a separated path on the chart): ")
      console.log(dataSets);

      //Here, for each party, we are erasing the dates that have 0 ngram occurencies
      // (if the checkbox is true)
      if(!document.getElementById("inputChkBoxIncludeZeros").checked){
        console.log("Wr are erasing the dates with 0 ngram occurencies");
        // console.log(dataSets);
        for(partyNr in dataSets){
          for(dataPoints in dataSets[partyNr]){
            if(dataSets[partyNr][dataPoints] == 0){
              delete dataSets[partyNr][dataPoints];
            }
          }
        }
        console.log("Final Data to be visualized (after erasing 0-s)");
        console.log(dataSets);
      }

     
      visualize(partiesIdsNames, parseDate(startDate), parseDate(stopDate), step, ngram);

       //create the legend in here
      create_legend(partiesIdsNames);
    });

}



/**
dataSets - ?
partiesIdsNames - dict <partyId, PartyName>
startDate, stopDate - should be provided as JavaScript DATE objects
step - number of data points between start date and stop date; a resolution of X-scale
ngram - the ngram word
*/
function visualize(partiesIdsNames, startDate, stopDate, step, ngram){
    /* implementation heavily influenced by http://bl.ocks.org/1166403 */

    dataCirclesGroup = null;

    //firstly, we should clean the last viz
    document.getElementById("graph").innerHTML = "";

    var maxVal = getMaxValueFromDataSetS(dataSets);
    console.log("Max  Y-value (for scale calculation): " + maxVal);

    // define dimensions of graph
    var m = [80, 80, 80, 80]; // margins
    var w = 1000 - m[1] - m[3]; // width
    var h = 400 - m[0] - m[2]; // height


    var x = d3.time.scale().domain([startDate, stopDate]).range([0, w]);

    // Y scale will fit values from 0-10 within pixels h-0 (Note the inverted domain for the y-scale: bigger is up!)
    var y = d3.scale.linear().domain([0, maxVal]).range([h, 0]);
    // automatically determining max range can work something like this
    // var y = d3.scale.linear().domain([0, d3.max(data)]).range([h, 0]);

    // create a line function that can convert data[] into x and y points
    var line = d3.svg.line()
        // assign the X function to plot our line as we wish
        .x(function (d, i) {
            // verbose logging to show what's actually being done
            console.log('Plotting X value for data point: ' + new Date( d.date ) + ' using index: ' + i + ' to be at: ' + x(new Date(d.date )) + ' using our xScale.');
//            console.log('Plotting X value for data point: ' + d.key + ' using index: ' + i + ' to be at: ' + x((d.key)) + ' using our xScale.');
            return x(new Date(d.date));
        })
        .y(function (d) {
            // verbose logging to show what's actually being done
            console.log('Plotting Y value for data point: ' + d.count + ' to be at: ' + y(d.count) + " using our yScale.");
            // return the Y coordinate where we want to plot this datapoint
            return y(d.count);
        });


    console.log("testing vis ")
    console.log(d3.entries(dataSets["partyA"]))

    // Add an SVG element with the desired dimensions and margin.
    var graph = d3.select("#graph").append("svg:svg")
        .attr("width", w + m[1] + m[3])
        .attr("height", h + m[0] + m[2])
        .append("svg:g")
        .attr("transform", "translate(" + m[3] + "," + m[0] + ")");

    // create yAxis
    var xAxis = d3.svg.axis().scale(x).tickSize(-h).tickSubdivide(true);

    // x-axis.
    graph.append("svg:g")
        .attr("class", "xaxis")
        .attr("transform", "translate(0," + h + ")")
        .style("stroke-width", 2)
        .call(xAxis);

    // yAxis
    var yAxisLeft = d3.svg.axis().scale(y).ticks(6).orient("left");
    // Add the y-axis to the left
    graph.append("svg:g")
        .attr("class", "y axis")
        .attr("transform", "translate(-25,0)")
        .call(yAxisLeft);



    //add X - grid
    graph.append("g")
        .attr("class", "grid")
        .attr("transform", "translate(0," + h + ")")
        .call(make_x_axis(x)
            .tickSize(-h, 0, 0)
            .tickFormat("")
        )

    // Y - grid
    graph.append("g")
        .attr("class", "grid")
        .call(make_y_axis(y)
            .tickSize(-w, 0, 0)
            .tickFormat("")
        )


    // add proper data lines
    // Add the line by appending an svg:path element with the data line we created above
    // do this AFTER the axes above so that the line is above the tick-lines

    // console.log("COLORS COME HERE!!");

    console.log("Presenting the Data")
    for (partyNr in dataSets) {
        console.log( dataSets[partyNr]["listDates"])

        //create a svg:g group "line" for path + cricles
        lineGroup =
            graph
                .append('svg:g')
                .attr('class', 'lineGroup' + partyNr)
                .attr("id", "lineGroupId" + partyNr);

        // document.geEle
        // Append & draw the Linex (if checkbox is true)
        // if(document.getElementById("inputChkBoxDrawLines").checked){
        lineGroup.append("svg:path")
//            .attr("d", line(d3.entries(dataSets[partyNr])))
            .attr("d", line( dataSets[partyNr]["listDates"] ))
            .attr("id", "idPathPoint" + partyNr)
            .attr("class", "path" + partyNr)
            .attr("visibility", function () {
                return document.getElementById("inputChkBoxDrawLines").checked ? "visible" : "hidden";
            })
            .style("stroke", color_hash[partyNr])
            .style("stroke-width", 2)
            .style("fill", "none");
        // }

        //here I do draw points
        // Draw the points
        dataCirclesGroup =
            lineGroup
                .append('svg:g')
                .attr('class', 'circlesgroup' + partyNr);

        var circles = dataCirclesGroup.selectAll('.data-point')
            .data( dataSets[partyNr]["listDates"] );
//            .data(d3.entries(dataSets[partyNr]));

        circles
            .enter()
            .append('svg:circle')
            .attr('class', 'data-point')
            .style('opacity', 1.0)
            .style("stroke", color_hash[partyNr])
            .style("stroke-width", 2)
            .style("fill", function (d, i) {
                console.log("fill : " + color_hash[ partyNr]);
                console.log("i2 : " + partyNr);
                return color_hash[partyNr]
            })
            .attr('cx', function (d) {
                console.log("drawing circle ");
                return x(new Date(d.date))
            })
            .attr('cy', function (d) {
                return y(d.count)
            })
            .attr('r', function () {
                return 3;
                // return (data.length <= maxDataPointsForDots) ? pointRadius : 0
            })
    }

        //adding the tooltips to be shown on top of data points  (using jquery tipsy)
  /*


    $('svg circle').tipsy({
     gravity: 'w',
     html: true,
     title: function() {
     var d = this.__data__;
     var pDate = new Date(d.key);
     pDate = formatDate(pDate, true);
     return 'Year: ' + pDate + '<br>Value: ' + d.value;
     }
     })

     */

    /*    //add title
     graph.append("svg:text")
     .attr("class", "title")
     .attr("x", 20)
     .attr("y", -20)
     .text(function(d){
     // I simply hate JavaScript
     // var stringDateFrom = startDate.getFullYear() + "-" + (startDate.getMonth() +1) + "-" + startDate.getDate();
     var stringDateFrom = formatDate(startDate, false);
     var stringDateTo = formatDate(stopDate, false);
     // var stringDateTo = stopDate.getFullYear() + "-" + (stopDate.getMonth() +1) + "-" + stopDate.getDate();
     return "Ngram: " + ngram + ",     from: " + stringDateFrom  + "      to: " + stringDateTo ;
     })*/


    //add legend
    var legend = graph.append("g")
        .attr("id", "legendOnChart")
        .attr("class", "legend")
        .attr("visibility", "hidden")
        .attr("x", w - 300)
        .attr("y", 25)
        .attr("height", 100)
        .attr("width", 100);




    /*

     //here we're preparing the datalabels.
     // leneg rectangles here
     legend.selectAll('rect')
     .data(Object.keys(dataSets))  //this is tricky: we provide a list of dataSets properties.. so parties IDs
     .enter()
     .append("rect")
     .attr("x", w - 65)
     .attr("y", function(d, i){
     return i *  20;})
     .attr("width", 10)
     .attr("height", 10)
     .style("fill", function(d) {
     return color_hash[d];
     });

     legend.selectAll('text')
     .data(Object.keys(dataSets))
     .enter()
     .append("text")
     .attr("x", w - 52)
     .attr("y", function(d, i){ return i *  20 + 9;})
     .text(function(d) {
     return partiesIdsNames[d];
     });


     */

    // graph.append("svg:path").attr("d", line(data)).attr("class", "path1");
    // graph.append("svg:path").attr("d", line(data2)).attr("class", "path2");
            
}

function make_x_axis(x) {
    return d3.svg.axis()
        .scale(x)
        .orient("bottom")
        .ticks(5)
}

function make_y_axis(y) {
    return d3.svg.axis()
        .scale(y)
        .orient("left")
        .ticks(5)
}

/** *
 *  "partyA":{
         "2012-05-02": 5,
         "2012-05-03": 3,
         "2012-05-04": 9,
         "2012-05-05": 11
     },
 "partyB":{
         "2012-05-02": 11,
         "2012-05-03": 9,
         "2012-05-04": 3,
         "2012-05-05": 5
     }
 };
 */

/** Array of "partiesNgramses" (a values of key "partiesNgramses" should be provided*/
function getMaxDateFromDataSet( arrayPartyNgrams ){
    maxDate = new Date("0000-01-01");

    for ( partyIter in arrayPartyNgrams ){
        var partyNgram = arrayPartyNgrams[partyIter];
        for ( dateIter in partyNgram["listDates"]){
            var nGramValue = partyNgram["listDates"][ dateIter ]["date"];
            var nGramDate =  new Date( nGramValue);
            if ( maxDate < nGramDate ) maxDate = nGramDate
        }
    }
    return maxDate;
}


/** Array of "partiesNgramses" (a values of key "partiesNgramses" should be provided*/
function getMinDateFromDataSet( arrayPartyNgrams ){
    var minDate = new Date("2999-01-01");

    for ( partyIter in arrayPartyNgrams ){
        var partyNgram = arrayPartyNgrams[partyIter];
        for ( dateIter in partyNgram["listDates"]){
            var nGramValue = partyNgram["listDates"][ dateIter ]["date"];
            var nGramDate =  new Date( nGramValue);
            if ( minDate > nGramDate ) minDate = nGramDate
        }
    }
    return minDate;
}

