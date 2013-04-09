// This file contains all visualizing stuff



/* Some global objects go here!*/

var dataSets = null;
/* 

dataSets - array of arrays containing nrOfNGrams for relevant period for one party
 eg. dataSets
        => "PIS"
            => [0] : 1
            => [2] : 3
            => .......
            => [10] : 1;
        => "PO"
            => [0] : 1
            => [2] : 3
            => .......
            => [10] : 1;
*/

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

    //first we should query for config data
    d3.text("mysql/queryConfig.php", function(cnfTxt){
      jsonConfRespose = JSON.parse(cnfTxt);
    });


    // here we're launching the php script that queries the mysql and returns json with response
    console.log("start querying the SQL DB..");
    d3.text("mysql/queryNgrams.php" + optionsString, function(txt) {
      console.log("stopped querying the SQL DB..");
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
      useAllDays = document.getElementById("inputChkUseAllDataPoints").checked; //checks inputChkUseAllDataPoints checkbox

      for(partyLines in partiesIdsNames){
        dataSets[partyLines] = getNGramsListFromJSON(jsonResponse, startDate, 
                        stopDate, step, partyLines, nGramToVisualize, useAllDays);
      }
      console.log("processing the retrieved data.. stop");
      console.log("DataSets to be visualized (each array element is a separated path on the chart): ")
      console.log(dataSets);

      //Here, for each party, we are erasing the dates that have 0 ngram occurencies
      // (if the checkbox is true)
      if(!document.getElementById("inputChkBoxIncludeZeros").checked){
        console.log("Wr are erasing the dates with 0 ngram occurencies");
        // console.log(dataSets);
        for(partyLines in dataSets){
          for(dataPoints in dataSets[partyLines]){
            if(dataSets[partyLines][dataPoints] == 0){
              delete dataSets[partyLines][dataPoints];
            }
          }
        }
        console.log("Final Data to be visualized (after erasing 0-s)");
        console.log(dataSets);
      }
      visualize(partiesIdsNames, parseDate(startDate), parseDate(stopDate), step, ngram);
    });

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

        var maxVal = maxValueFromDataSetS(dataSets);
        console.log("Max  Y-value (for scale calculation): " + maxVal);

        // define dimensions of graph
        var m = [80, 80, 80, 80]; // margins
        var w = 1000 - m[1] - m[3]; // width
        var h = 400 - m[0] - m[2]; // height

        var color_hash = {  "0": "green",
                            "1" :  "orange",
                            "2" : "blue",
                            "3" : "red",
                            "4": "black",
                            "5": "yellow",
                            "6": "violet",
                            "7": "brown",
                      }      

        var x = d3.time.scale().domain([startDate, stopDate]).range([0, w]);

        // Y scale will fit values from 0-10 within pixels h-0 (Note the inverted domain for the y-scale: bigger is up!)
        var y = d3.scale.linear().domain([0, maxVal]).range([h, 0]);
            // automatically determining max range can work something like this
            // var y = d3.scale.linear().domain([0, d3.max(data)]).range([h, 0]);

        // create a line function that can convert data[] into x and y points
        var line = d3.svg.line()
            // assign the X function to plot our line as we wish
            .x(function(d,i) { 
                // verbose logging to show what's actually being done
                  // console.log('Plotting X value for data point: ' + d.key + ' using index: ' + i + ' to be at: ' + x(d.key) + ' using our xScale.');
                return x(new Date(d.key)); 
            })
            .y(function(d) { 
                // verbose logging to show what's actually being done
//                 console.log('Plotting Y value for data point: ' + d.value + ' to be at: ' + y(d.value) + " using our yScale.");
                // return the Y coordinate where we want to plot this datapoint
                return y(d.value); 
            }
)
            // Add an SVG element with the desired dimensions and margin.
            var graph = d3.select("#graph").append("svg:svg")
                  .attr("width", w + m[1] + m[3])
                  .attr("height", h + m[0] + m[2])
                .append("svg:g")
                  .attr("transform", "translate(" + m[3] + "," + m[0] + ")");

            // create yAxis
            var xAxis = d3.svg.axis().scale(x).tickSize(-h).tickSubdivide(true);

            // Add the x-axis.
            graph.append("svg:g")
                  .attr("class", "x axis")
                  .attr("transform", "translate(0," + h + ")")
                  .call(xAxis);

            // create left yAxis
            var yAxisLeft = d3.svg.axis().scale(y).ticks(4).orient("left");
            // Add the y-axis to the left
            graph.append("svg:g")
                  .attr("class", "y axis")
                  .attr("transform", "translate(-25,0)")
                  .call(yAxisLeft);

            // add proper data lines
            // Add the line by appending an svg:path element with the data line we created above
            // do this AFTER the axes above so that the line is above the tick-lines

            // console.log("COLORS COME HERE!!");
            // console.log(dataSets);
            for(partyLines in dataSets){
                
                //create a svg:g group "line" for path + cricles
                lineGroup = 
                  graph
                    .append('svg:g')
                    .attr('class', 'lineGroup' + partyLines);

                      // document.geEle
                // Append & draw the Linex (if checkbox is true)
                // if(document.getElementById("inputChkBoxDrawLines").checked){
                  lineGroup.append("svg:path")
                        .attr("d", line(d3.entries(dataSets[partyLines])))
                        .attr("id", "idPathPoint" + partyLines)
                        .attr("class", "path" + partyLines)
                        .attr("visibility", function(){
                          return document.getElementById("inputChkBoxDrawLines").checked ? "visible" : "hidden";
                         })
                        .style("stroke", color_hash[partyLines])
                        .style("stroke-width", 2)
                        .style("fill", "none");
                // }

                //here I do draw points
                // Draw the points
                dataCirclesGroup = 
                   lineGroup
                     .append('svg:g')
                     .attr('class', 'circlesgroup' + partyLines);
              
                var circles = dataCirclesGroup.selectAll('.data-point')
                  .data(d3.entries(dataSets[partyLines]));
              
                circles
                  .enter()
                    .append('svg:circle')
                      .attr('class', 'data-point')
                      .style('opacity', 1.0)
                      .style("stroke", color_hash[partyLines])
                      .style("stroke-width", 2)
                      .style("fill", "#FFF")
                      .attr('cx', function(d) { console.log("drawing circle "); return x(new Date(d.key)) })
                      .attr('cy', function(d) { return y(d.value) })
                      .attr('r', function() {
                        return 4;
                       // return (data.length <= maxDataPointsForDots) ? pointRadius : 0 
                     })
            }

            //adding the tooltips to be shown on top of data points  (using jquery tipsy)
            $('svg circle').tipsy({ 
              gravity: 'w', 
              html: true, 
              title: function() {
                var d = this.__data__;
                var pDate = new Date(d.key);
                pDate = formatDate(pDate, true);
                return 'Year: ' + pDate + '<br>Value: ' + d.value; 
              }
            });

            //add title
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
             })
                

            //add legend
            var legend = graph.append("g")
              .attr("class", "legend")
              .attr("x", w - 65)
              .attr("y", 25)
              .attr("height", 100)
              .attr("width", 100);

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

            // graph.append("svg:path").attr("d", line(data)).attr("class", "path1");
            // graph.append("svg:path").attr("d", line(data2)).attr("class", "path2");
            
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


/* 
This one finds max val in the whole dataSets structure;
It's used for scaling the graph
*/
function maxValueFromDataSetS(dataSets){
    var maxVal = 0;
    for(a in dataSets)
        for(b in dataSets[a])
            if(dataSets[a][b] > maxVal)
                maxVal = dataSets[a][b]

  
    // for(var x = 0; x < dataSets.length; x++)
    //     for(var i = 0; i < dataSets[x].length; i++){
    //         console.log(dataSets[x][i]);
    //         if(dataSets[x][i] > maxVal)
    //             maxVal = dataSets[x][i];
    //     }
    return maxVal;
 }