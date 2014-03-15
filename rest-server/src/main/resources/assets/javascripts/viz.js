var width=1000;
var height=1000;
var border=50;

function visualize(term) {
    var graph = function(data) {
        var svg=d3.select("#graph > svg");

        var colors=d3.scale.category10();

        svg.selectAll("g.party").remove();

        // convert dates to timestamps 
        var ngrams=_.map(data.partiesNgrams,function(d) {
            return {"name": d.name,
                    listDates: _.map(d.listDates,
                    function(d) {
                        return {
                            "count": d.count,
                            "date": new Date(d.date).getTime()
                        }})
                    }
                });
        
        // apply a function to a specific parameter in the dataset
        // used below to get minimum and maximum dates and counts.
        var gmm= function(d,what,fn) {
            return fn(_.map(d,function(d) {    
                return fn(_.pluck(d.listDates,what))}))}

        var mindate=gmm(ngrams,
            "date",
            _.min
            );
        var maxdate=gmm(ngrams,
            "date",
            _.max
            );
        
        // fill in the empty dates - this might not be needed...
        ngrams=_.map(ngrams, function(d) {
            var counts={}
            _.each(d.listDates, function(d) {
                counts[d.date]=d.count });
            
            return {"name": d.name,
                     listDates: _.map(
                        _.range(mindate,maxdate+1,24*60*60*1000),
                        function(x) {
                            return {"name": d.name,
                                "date": x,
                                "count": counts[x] || 0
                            }})}});
         
         var partynames = _.pluck(ngrams,"name");

         var t=_.reduce(_.flatten(_.pluck(ngrams,"listDates")),
                function(x,y) {
                    x[y.date]=y.count + (x[y.date] || 0);
                    return x;} , {})

        total=_.sortBy(_.map(_.zip(_.keys(t),_.values(t)), function(d) {
            return {"date": d[0],
                "count": d[1],
                "name": "total"}}),
                    function(d) { return d.date });
        

        ngrams.push({"name":"total",
            "listDates": total});

        var maxcount=gmm(ngrams,"count",_.max);

        var xscale=d3.scale.linear()
            .domain([mindate,maxdate])
            .range([border,width-border]);
        
        var yscale=d3.scale.linear()
            .domain([0,maxcount])
            .range([height-border,border]);
        
        var path=d3.svg.line()
                .x(function(d) { return xscale(d.date)})
                .y(function(d) { return yscale(d.count)});
         
        var parties=svg.selectAll("g.party")
            .data(ngrams)
            .enter()
            .append("g")
            .attr("class","party")
            .attr("id",function(d) { return "group-"+d.name })
            .attr("style","visibility: hidden;");
        
        d3.select("#group-total")
            .attr("style","visibility: visible;");

        var paths=parties.append("path").attr("d",function(d) { return path(d.listDates)})
            .attr("stroke",function(d) { return colors(d.name) });

        
        var dots=parties.selectAll("circle")
                        .data(function(d) { return d.listDates })
                        .enter()
                        .append("circle")
                        .attr("cx",function(d) { return xscale(d.date) })
                        .attr("cy", height-border)
                        .attr("r", 5)
                        .attr("style", function(d) { return "fill: "+
                        colors(d.name); });
                        
        dots.transition().attr("cy", function(d) { return yscale(d.count) });
        
        setTimeout(function() { paths.attr("class","visible");},
            500);

        var selectbox = d3.select("#legendDiv")
        
        selectbox.selectAll("div").remove();

        var partyselect=selectbox.selectAll("div")
            .data(partynames)
            .enter()
            .append("div")
            
        partyselect.append("input")
            .attr("type","checkbox")
            .attr("id",function(d) { return "check-"+d })
            .on("click",function(d) {
                console.log(d);
                if ($("#check-"+d)[0].checked) {
                    d3.select("#group-"+d)
                      .attr("style","visibility: visible;");
                    }
                else {
                    d3.select("#group-"+d)
                      .attr("style","visibility: hidden;");
                    }
                });

         partyselect.append("span")
            .text(function(d) { return d; });

        };

    if (term) {
        d3.json("http://sejmotrendy.pl/service/api/ngram/"+encodeURI(term),
                graph 
                );
        }
    };

function setupGraph() {
    var svg=d3.select("#graph").append("svg")
              .attr("viewBox","0 0 "+width+" "+height)
              .attr("perserveAspectRatio","xMidYMid");
    }
