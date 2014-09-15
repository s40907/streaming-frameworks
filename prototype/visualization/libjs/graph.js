var Graph = function (config, chartIdentity) {
 var logger = log4javascript.getLogger("UI.graph");
 var chart= '';
 var rect = '';
 var data = new Array();
 var width = 0;
 var height = 0;
 var widthScale = 0;
 var heightScale = 0;
 var color = 0;
 var yAxis = 0;
 var xAxis = 0;
 var currentMeasureDatum = new Date();
 var currentMessagesPerSecond = 0;

 this.start = function () {
  init();
  updateEnvironment();
  create();
 };

 this.update = function (newData, countPerSecond) {
  update(newData, countPerSecond);
 }

 function getCurrentDatum() {
  return new Date().getTime() / 1000;
 }

 function init () {
  currentMeasureDatum = getCurrentDatum();
  width = config.width - config.margin.left - config.margin.right;
  height = config.height - config.margin.top - config.margin.bottom;
  chart = d3.select(chartIdentity)
   .append('svg')
    .attr('width', width + config.margin.left + config.margin.right)
    .attr('height', height + config.margin.top + config.margin.bottom)
   .append('g')
    .attr('transform', 'translate(' + config.margin.left+ ', ' + config.margin.top + ')');
 }

 function updateEnvironment() {
  var maxDataX = d3.max(data, function (d) { return d.count; });
  color = d3.scale.linear()
   .domain([0, maxDataX])
   .range(config.colorRange);

  widthScale = d3.scale.linear()
   .domain([0, maxDataX])
   .range([0, width]);

  heightScale = d3.scale.ordinal()
   .domain(d3.range(data.length))
   .rangeBands([0, height], 0.1);

  xAxis = d3.svg.axis().scale(widthScale)
   .orient('bottom')
   .ticks(5);

  yAxis = d3.svg.axis().scale(heightScale)
   .orient('left')
   .tickFormat(function (d,i) { return data[i].word; });
}

function create() {
 var bars = '';
 bars = chart.selectAll('.bar')
  .data(data)
  .enter().append('g')
  .attr('class', 'bar')
  .attr("transform", function(d, i) { return 'translate(' + 0 + ',' + heightScale(i) + ')'; });

 bars.append('rect')
  .attr('fill', function(d) { return color(d.count); })
  .attr('width', function(d) { return widthScale(d.count); })
  .attr('height', heightScale.rangeBand());

 bars.append('text')
  .attr('x', function(d) { return widthScale(d.count); })
  .attr('y', 0 + heightScale.rangeBand() / 2)
  .attr('dx', -18)
  .attr('dy', '.35em')
  .attr('text-anchor', 'end')
  .text(function(d) { return d.count; });

 chart.append('g')
  .attr('class', 'x axis')
  .attr('transform', 'translate(0,' + height + ')')
  .call(xAxis);

 chart.append('g')
  .attr('class', 'y axis')
  .call(yAxis);

 chart.append('text')
  .attr('x', width / 2)
  .attr('y', height + config.margin.bottom)
  .style('text-anchor', 'middle')
  .style('fill', 'black')
  .style('font-family', 'arial')
  .style('font-size', '12px')
  .text('Count');

 chart.append('text')
  .attr('transform', 'rotate(-90)')
  .attr('y', 10 - config.margin.left)
  .attr('x', 0 - (height / 2))
  .attr('dy', '1em')
  .style('text-anchor', 'middle')
  .style('fill', 'black')
  .style('font-family', 'arial')
  .style('font-size', '12px')
  .text('Words');
 
 chart.append('text')
  .attr('x', 0 - config.margin.left)
  .attr('y', 10 - config.margin.top)
  .text('Messages per second: ');
 
 chart.append('text')
  .attr('class', 'mps')
  .attr('x', 80)
  .attr('y', 10 - config.margin.top)
  .text('0');
}

 function update(newData, countPerSecond) {
  data = newData;
  d3.selectAll(chartIdentity + ' > *').remove();
  init();
  updateEnvironment();
  create();
  
  chart.select('.mps')
   .transition()
    .text(countPerSecond);

  var rect = chart.selectAll(".bar rect").data(data);
  var text = chart.selectAll(".bar text").data(data);
    rect.transition()
      .attr("width", function(d) { return widthScale(d.count); })
      .attr("fill", function(d) { return color(d.count); });
    text.transition()
      .attr("x", function(d) { return widthScale(d.count); })
      .text(function(d) { return d.count; });
  chart.selectAll(".y.axis")
        .call(yAxis)
  chart.selectAll(".x.axis")
        .call(xAxis);
 }
}
