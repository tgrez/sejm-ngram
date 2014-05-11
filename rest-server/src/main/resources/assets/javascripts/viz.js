var width=773;
var height=547;
var selectbarheight=75;
var border=40;

function visualize(term) {
    var graph = function(data) {
        var svg=d3.select("#graph > svg");
        
        var colors=d3.scale.category10();
        
        var slugify=function(s) {
            return s.toLowerCase().replace(/[^a-z0-9-]/g,"-")
            }

        var colors=function() {
            var cs = [
                '#63A6B3',
                '#F7BB03',
                '#00BCD8',
                '#40152A'
                ]
            var cdict={};
            return function(d) {
                if (d == 'total')
                    { return '#f14949' }
                else {
                    var t=cdict[d]
                    if (!t) {
                        var i=_.keys(cdict).length+1;
                        t=cs[i%4];
                        cdict[d]=t;
                        }
                    return t;
                }}}()

        svg.selectAll("g.party").remove();

        // convert dates to timestamps 
        var ngrams=_.map(data.partiesNgrams,function(d) {
            return {"name": d.name,
                    listDates: _.sortBy(_.map(d.listDates,
                    function(d) {
                        return {
                            "count": d.count,
                            "date": new Date(d.date).getTime()
                        }}),function(d) {
                            return d.date })
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
//        ngrams=_.map(ngrams, function(d) {
  //          var counts={}
    //        _.each(d.listDates, function(d) {
    //            counts[d.date]=d.count });
            
    //        return {"name": d.name,
    //                 listDates: _.map(
    //                    _.range(mindate,maxdate+1,24*60*60*1000),
    //                    function(x) {
    //                        return {"name": d.name,
    //                            "date": x,
     //                           "count": counts[x] || 0
      //                      }})}});
         
         var partynames = _.pluck(ngrams,"name");

         var t=_.reduce(_.flatten(_.pluck(ngrams,"listDates")),
                function(x,y) {
                    x[y.date]=y.count + (x[y.date] || 0);
                    return x;} , {})

        total=_.sortBy(_.map(_.zip(_.keys(t),_.values(t)), function(d) {
            return {"date": parseInt(d[0]),
                "count": d[1],
                "name": "total"}}),
                    function(d) { return d.date });
        
        console.log(total);

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
                .y(function(d) { return yscale(d.count)})
                .interpolate("linear");
         
        var parties=svg.selectAll("g.party")
            .data(ngrams)
            .enter()
            .append("g")
            .attr("class","party")
            .attr("id",function(d) { return "group-"+slugify(d.name) })
            .attr("style","visibility: hidden;");
        
        d3.select("#group-total")
            .attr("style","visibility: visible;");

        var paths=parties.append("path").attr("d",function(d) { return path(d.listDates)})
            .attr("stroke",function(d) { return colors(d.name) });

        var tip=d3.tip()
            .attr("class","d3-tip")
            .offset([-10,0])
            .html(function(d){
                    var date=new Date(d.date);
                    return "<span>Count: "+ d.count+"</span><br/>" +
                    "<span>Month: "+ date.getFullYear()+"-"+(date.getMonth()+1) +"</span>" })

                    svg.call(tip);

                    var dots=parties.selectAll("circle")
                    .data(function(d) { return d.listDates })
                    .enter()
                    .append("circle")
                    .attr("cx",function(d) { return xscale(d.date) })
                    .attr("cy", height-border)
                    .attr("r", 4)
                    .attr("style", function(d) { return "fill: "+
                        colors(d.name); })
                    .on("mouseover",tip.show)
                    .on("mouseout",tip.hide);

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
                    .attr("id",function(d) { return "check-"+slugify(d) })
                    .on("click",function(d) {
                        if ($("#check-"+slugify(d))[0].checked) {
                        d3.select("#group-"+slugify(d))
                        .attr("style","visibility: visible;");
                        }
                    else {
                        d3.select("#group-"+slugify(d))
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

                    sb.selectAll("rect").remove();
                    sb.append("rect")
                    .attr("x",border)
                    .attr("y",0)
                    .attr("width",width-border*2)
                    .attr("class","frame")
                    .attr("height",selectbarheight);

                    var drawLabels= function(mind,maxd) {
                        svg.selectAll("text.label").remove();

                        svg.append("text")
                        .attr("x",border)
                        .attr("y",height-border+23)
                        .attr("class","label")
                        .attr("text-anchor","middle")
                        .text(function() {
                        var d=new Date(mind);
                        return d.getFullYear()+"-"+(d.getMonth() + 1)+"-"+d.getDate()
                        })
                    svg.append("text")
                    .attr("x",width-border)
                    .attr("y",height-border+23)
                    .attr("class","label")
                    .attr("text-anchor","middle")
                    .text(function() {
                        var d=new Date(maxd);
                        return d.getFullYear()+"-"+(d.getMonth() + 1 )+"-"+d.getDate()
                        })
                    }

                    var setscale=function() {
                        minx=d3.select("#coverleft").attr("width")*1+border;
                        maxx=d3.select("#coverright").attr("x")*1;
                        var nscale=d3.scale.linear()
                        .domain([sbrscale(minx),sbrscale(maxx)])
                        .range([border,width-border]);

                        dots.attr("cx",function(d) {
                        return nscale(d.date) });
                    var npath=d3.svg.line()
                    .x(function(d) { return nscale(d.date)})
                    .y(function(d) { return yscale(d.count)});

                    paths.attr("d",function(d) { return npath(d.listDates)});
                    drawLabels(sbrscale(minx),sbrscale(maxx));
                    }

                    var dragl=d3.behavior.drag()
                    .on("drag",function(d,i) {
                        d.x+=d3.event.dx;
                        if (d.x < border-2 ) {
                        d.x = border-2;
                        }
                    ox=d3.select("#dragr").attr("x")
                    if (d.x > ox ) {
                        d.x = parseInt(ox);
                        }
                    d3.select(this).attr("x",d.x);
                    d3.select("#coverleft")
                    .attr("width",d.x-border+2);
                    setscale();
                    })

                    var dragr=d3.behavior.drag()
                    .on("drag",function(d,i) {
                        d.x+=d3.event.dx;
                        if (d.x > width-border-2 ) {
                        d.x = width-border-2;
                        }
                    ox=d3.select("#dragl").attr("x")
                    if (d.x < ox ) {
                        d.x = parseInt(ox);
                        }
                    d3.select(this).attr("x",d.x);
                d3.select("#coverright")
                    .attr("width",width-border-d.x-2)
                    .attr("x",d.x+2);
                setscale();
                })

         
         
         sb.select("path").remove();
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
            .data([{"x": border-2, "y": selectbarheight/2-10}])
            .attr("x",border-2)
            .attr("y",selectbarheight/2-5)
            .attr("width",4)
            .attr("height",10)
            .attr("class","drag")
            .attr("id","dragl")
            .call(dragl);

        sb.append("rect")
            .data([{"x": width-border-2, "y": selectbarheight/2-10}])
            .attr("x",width-border-2)
            .attr("y",selectbarheight/2-5)
            .attr("width",4)
            .attr("height",10)
            .attr("id","dragr")
            .attr("class","drag")
            .call(dragr);
        var bboxes=[
            [0,0,border-5,height],
            [width-border+5,0,border-5,height]]

        svg.selectAll("rect.bbox").remove();
        
        svg.selectAll("rect")
            .data(bboxes)
            .enter()
            .append("rect")
            .attr("class","bbox")
            .attr("x",function(d) {return d[0]})
            .attr("y",function(d) {return d[1]})
            .attr("width",function(d) {return d[2]})
            .attr("height",function(d) {return d[3]})
            .attr("style","fill: #ffffff; stroke: none;");

                    drawAxis();

                    svg.selectAll("text.ylabel").remove();
        svg.append("text")
            .attr("class","ylabel")
            .attr("x",border-10)
            .attr("y",border+5)
            .attr("text-anchor","end")
            .text(maxcount);
        

        svg.append("text")
            .attr("x",border-10)
            .attr("y",height-border+5)
            .attr("text-anchor","end")
            .text(0);
        drawLabels(mindate,maxdate);
        
        };

    if (term) {
        d3.json("service/api/ngramfts/"+encodeURI(term),
                    graph
                    );
                    }
                    };

                    function drawAxis() {
                        var svg=d3.select("#graph > svg");

                        svg.select("g.axis").remove();

                        var axis=svg.append("g")
                        .attr("class","axis");

                        var lines=[[border-5,height-border, border-5, border],
                        [border,height-border+5, width-border, height-border+5],
                        [border-5, height-border, border-10, height-border],
                        [border-5, border, border-10, border],
                        [border,height-border+5, border, height-border+10],
                        [width-border,height-border+5, width-border, height-border+10]
                        ]

                        axis.selectAll("line")
                        .data(lines)
                        .enter()
                        .append("line")
                        .attr("x1",function(d) { return d[0] })
                    .attr("y1",function(d) { return d[1] })
        .attr("x2",function(d) { return d[2] })
        .attr("y2", function(d) { return d[3] });
    }

function setupGraph() {
    var svg=d3.select("#graph").append("svg")
              .attr("width",width)
              .attr("height",height);
    
    drawAxis();

    var gnum=10;

    var gw=(width-border*2)/gnum;
    var gh=(height-border*2)/gnum;

    _.each(_.range(0,gnum+1),function(i) {
        svg.append("line")
            .attr("class","grid")
            .attr("x2",border+i*gw)
            .attr("x1",border+i*gw)
            .attr("y2",border)
            .attr("y1",height-border);
        
        svg.append("line")
            .attr("class","grid")
            .attr("x1",border)
            .attr("x2",width-border)
            .attr("y1",border+i*gh)
            .attr("y2",border+i*gh);
        })

            
    var sb=d3.select("#selectbar").append("svg")
        .attr("width",width)
        .attr("height",selectbarheight);

    sb.append("rect")
            .attr("x",border)
            .attr("y",0)
            .attr("width",width-border*2)
            .attr("class","frame")
            .attr("height",selectbarheight);
     
    }
