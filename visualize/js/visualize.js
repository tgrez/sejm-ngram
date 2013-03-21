// This file contains all visualizing stuff

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
function startVisualization(nGramToVisualize){


            console.log("startVisuzalition() with param: " + nGramToVisualize);

            // reset the div with graph
            document.getElementById("graph").innerHTML = "";
            d3.csv("fake_data.csv", function(data) {

                startDate = new Date("01/01/2008");  //it's so stuping, JavaScript uses month from 0
                stopDate = new Date("01/01/2009");
                step = 12;

                var dataSets = new Array();
                    dataSets["PIS"] = getNGramsList(data, startDate, stopDate, step, "PIS", nGramToVisualize);
                    dataSets["PO"] = getNGramsList(data, startDate, stopDate, step, "PO", nGramToVisualize);
                    dataSets["RP"] = getNGramsList(data, startDate, stopDate, step, "RP", nGramToVisualize);
                    dataSets["SLD"] = getNGramsList(data, startDate, stopDate, step, "SLD", nGramToVisualize);
                    dataSets["PSL"] = getNGramsList(data, startDate, stopDate, step, "PSL", nGramToVisualize);
                visualize(dataSets, startDate, stopDate, step);
        });
}


function visualize(dataSets, startDate, stopDate, step){
    /* implementation heavily influenced by http://bl.ocks.org/1166403 */
        // data = [];

         // console.log(dataSets);

        // firstly we are converint associative arrays into "normal" ones
        for(a in dataSets){
            dataSets[a] =  convertAssArrayToNormal(dataSets[a], step);
        }

        var maxVal = maxValueFromDataSetS(dataSets);
        console.log("max val: " + maxVal);

        // define dimensions of graph
        var m = [80, 80, 80, 80]; // margins
        var w = 1000 - m[1] - m[3]; // width
        var h = 400 - m[0] - m[2]; // height


        var color_hash = {  "PIS": "green",
                            "PO" :  "orange",
                            "PSL" : "blue",
                            "SLD" : "red",
                            "RP": "brown"
                      }    

        // var color_hash = {  0 : ["PIS", "green"],
        //                     1 : ["PO", "orange"],
        //                     2 : ["PSL", "blue"],
        //                     3 : ["SLD", "red"],
        //                     4 : ["RP", "brown"]
        //               }    
        
        // create a simple data array that we'll plot with a line (this array represents only the Y values, X will just be the index location)

        // var data = dataSets[];
        // var data2 = dataSetPo;
        // console.log(data2);
        // console.log([2,3,4,5,3]);

         // var data = [3, 6, 2, 7, 5, 2, 0, 3, 8, 9, 2, 5, 9, 3, 6, 3, 6, 2, 7, 5, 2, 1, 3, 8, 9, 2, 5, 9, 2, 7];
         // var data2 = [2, 5, 1, 6, 2, 0, 7, 4, 3, 9, 5, 5, 3, 3, 3, 5, 6, 7, 7, 5, 2, 3, 3, 4, 9, 0, 1, 5, 4, 0];

        // X scale will fit all values from data[] within pixels 0-w
        // var x = d3.scale.linear().domain([0, 12]).range([0, w]); //12 used to be data.length
        // console.log(dataSets["PIS"]);
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
                // console.log('Plotting X value for data point: ' + d + ' using index: ' + i + ' to be at: ' + x(i) + ' using our xScale.');
                // return the X coordinate where we want to plot this datapoint

                return x(i + 1); 
            })
            .y(function(d) { 
                // verbose logging to show what's actually being done
               // console.log('Plotting Y value for data point: ' + d + ' to be at: ' + y(d) + " using our yScale.");
                // return the Y coordinate where we want to plot this datapoint
                return y(d); 
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
            var i = 1
            for(partyLines in dataSets){
                graph.append("svg:path").attr("d", line(dataSets[partyLines])).attr("class", "path" + i);
                i++;
            }

            //add title
            graph.append("svg:text")
               .attr("class", "title")
               .attr("x", 20)
               .attr("y", 20)
               .text("Our Super N-gram!");

            //add legend
            var legend = graph.append("g")
              .attr("class", "legend")
              .attr("x", w - 65)
              .attr("y", 25)
              .attr("height", 100)
              .attr("width", 100);

            //here we're preparing the datalabels. it's quite nasty
            //since the legend labels could be made on top of dataSets
            //but somehow it's now a proper "array" (and .data() needs
            //such a proper array;
            console.log(color_hash["PIS"]);

            dataLabels = [];
            for(a in dataSets)
                dataLabels.push(a);
            // leneg rectangles here             
            legend.selectAll('rect')
                  .data(dataLabels)
                  .enter()
                  .append("rect")
                  .attr("x", w - 65)
                  .attr("y", function(d, i){ 
                    return i *  20;})
                  .attr("width", 10)
                  .attr("height", 10)
                  .style("fill", function(d) { 
                    // var color = color_hash[dataset.indexOf(d)][1];
                    return color_hash[d];
                  });

            legend.selectAll('text')
              .data(dataLabels)
              .enter()
              .append("text")
              .attr("x", w - 52)
              .attr("y", function(d, i){ return i *  20 + 9;})
              .text(function(d) {
                return d;
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