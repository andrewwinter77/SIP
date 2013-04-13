/*
File: bootstrap.js

About: Version
	1.0

Project: Comcast Customer Central

Description:
	This should be included directly after the opening <body> tag.
	
	If a module required a <script> tag in the middle of the page
	markup, queue up the code in a function so that jQuery and other
	dependencies are available for the code runs.

Example:
	><script type="text/javascript">
	>	CCC.queue(function() {
	>		// do something with jQuery, etc
	>	});
	></script>
	> 
	>...
	> 
	>$(document).ready(function() {
	>	CCC.unqueue();
	>})
	
*/

/*
Class: CCC
	Scoped to the CCC Global Namespace
*/
var CCC = window.CCC || {};

(function(CCC) {
	
	document.body.className += " js-enabled";
	
	var queue = [];
	
	/*
	method: queue
	 	Adds functions to a queuer to be deployed once the DOM is ready
	
	Parameters:
		arguments - the function (or functions) to add to the queue
	*/
	CCC.queue = function() {
		for (var i = -1, func; func = arguments[++i];) {
			queue[queue.length] = func;
		}
	};
	
	/*
	method: unqueue
	 	Fires each queued function
	*/
	CCC.unqueue = function() {
		var func;
		while (queue != null && (func = queue.shift())) {
			func();
		}
		
		// Nullify
		for (var i = (queue != null) ? queue.length - 1 : -1; i >= 0; i--) {
			queue[i] = null;
		}
		
		queue = null;
	};
	
}(CCC));