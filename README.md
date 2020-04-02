<h1>SheetSpread</h1>
<h3>Library for determining outliers in two dimensions.</h3>

<br>

<p>Initializes SheetSpread, passes in data path and determining outlier factor:</p>
<code>SheetSpread sheetSpread = new SheetSpread("datapath.txt", 1.5);</code>
<p>SheetSpread finds the average distance between points, and if a point is greater than the average distance times the determining outlier factor, the point is classified as an outlier.</p>

<br>

<p>Determines if an existing point is an outlier, passes line number to data point:</p>
<code>boolean isOutlier = sheetSpread.determineExisting(1);</code>

<br>

<p>Determines if a new point is an outlier, passes x and y coordinates</p>
<code>boolean isOutlier = sheetSpread.determineExperimental(1, 2);</code>
