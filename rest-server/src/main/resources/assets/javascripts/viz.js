var width=1000;
var height=1000;
var selectbarheight=200;
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

         sbxscale=d3.scale.linear()
            .domain([mindate,maxdate])
            .range([border,width-border]);

         sbrscale=d3.scale.linear()
            .domain([border,width-border])
            .range([mindate,maxdate]);

         sbyscale=d3.scale.linear()
            .domain([0,maxcount])
            .range([selectbarheight,0]);

         sbpath=d3.svg.line()
            .x(function(d) { return sbxscale(d.date); })
            .y(function(d) { return sbyscale(d.count); });

         sb=d3.select("#selectbar > svg");
        

        var setscale=function() {
            minx=d3.select("#coverleft").attr("width")*1+border;
            maxx=d3.select("#coverright").attr("x")*1;
            console.log(minx,maxx);
            var nscale=d3.scale.linear()
                .domain([sbrscale(minx),sbrscale(maxx)])
                .range([border,width-border]);

            dots.attr("cx",function(d) {
                return nscale(d.date) });
            var npath=d3.svg.line()
                    .x(function(d) { return nscale(d.date)})
                    .y(function(d) { return yscale(d.count)});

            paths.attr("d",function(d) { return npath(d.listDates)});
            }

        var dragl=d3.behavior.drag()
            .on("drag",function(d,i) {
                d.x+=d3.event.dx;
                if (d.x < border-5 ) {
                    d.x = border-5; 
                    }
                ox=d3.select("#dragr").attr("x")
                if (d.x > ox ) {
                    d.x = parseInt(ox);
                    }
                d3.select(this).attr("x",d.x); 
                d3.select("#coverleft")
                    .attr("width",d.x-border+5);
                setscale();
                })

        var dragr=d3.behavior.drag()
            .on("drag",function(d,i) {
                d.x+=d3.event.dx;
                if (d.x > width-border-5 ) {
                    d.x = width-border-5; 
                    }
                ox=d3.select("#dragl").attr("x")
                if (d.x < ox ) {
                    d.x = parseInt(ox);
                    }
                d3.select(this).attr("x",d.x); 
                d3.select("#coverright")
                    .attr("width",width-border-d.x-5)
                    .attr("x",d.x+5);
                setscale();
                })

         

         sb.append("path")
            .attr("d",sbpath(total));
         
         sb.selectAll("rect.cover")
            .data([{"width": 0, "x": border, "id": "coverleft"},
                {"width": 0, "x": width-border, "id": "coverright"}])
            .enter()
            .append("rect")
            .attr("class","cover")
            .attr("y",0)
            .attr("height",selectbarheight)
            .attr("x",function(d) {return d.x })
            .attr("width", function(d) { return d.width })
            .attr("id",function(d) { return d.id });
        
        sb.append("rect")
            .data([{"x": border-5, "y": selectbarheight/2-10}])
            .attr("x",border-5)
            .attr("y",selectbarheight/2-10)
            .attr("width",10)
            .attr("height",20)
            .attr("class","drag")
            .attr("id","dragl")
            .call(dragl);

        sb.append("rect")
            .data([{"x": width-border-5, "y": selectbarheight/2-10}])
            .attr("x",width-border-5)
            .attr("y",selectbarheight/2-10)
            .attr("width",10)
            .attr("height",20)
            .attr("id","dragr")
            .attr("class","drag")
            .call(dragr);

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

    var sb=d3.select("#selectbar").append("svg")
        .attr("viewBox", "0 0 "+width+" "+selectbarheight)
        .attr("perserverAspectRatio","xMidYMid");

    sb.append("rect")
            .attr("x",border)
            .attr("y",0)
            .attr("width",width-border*2)
            .attr("class","frame")
            .attr("height",selectbarheight);
     
    }
