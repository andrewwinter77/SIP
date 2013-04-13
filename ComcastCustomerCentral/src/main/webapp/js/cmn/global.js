/*
File: global.js

About: Version
	1.0

Project: Comcast Customer Central

Description:
	A common file that includes all globally shared functionality for CCC

Requires:
	- LABjs <http://labjs.com/>
	- jQuery

Requires:
	- <bootstrap.js>
	
*/

/*
Class: CCC
	Scoped to the CCC Global Namespace
*/
var CCC = window.CCC || {};

// When the DOM is ready.
(function () {
	
	// Storing a variable to reference
	var $self = CCC;
	
	/*
	Namespace: CCC.vars
		Shared global variables
	*/
	$self.vars = {
		
		/*
		variable: queue
			Contains the functions ready to be fired on DOM ready
		*/
		queue : []
	};
	
	/*
	Namespace: CCC.legacy
		A legacy namespace to be used if CCC has any legacy dependencies
	*/
	$self.legacy = {};
	
	/*
	Namespace: CCC.utils
		Shared global utilities
	*/
	$self.utils = {
		
		/*
		sub: ie6Check
		 	Adds an IE6 flag to jQuery
		*/
		ie6Check : function() {
			// Let's set a flag for IE 6
			$.extend($.browser, {
				ie6 : function () {
					return !!($.browser.msie && $.browser.version == 6);
				}()
			});
		}(),
		
		/*
		sub: queue
		 	A global initializer. Takes a function argument and queues it until <init> is fired
		
		Parameters:
			object - The function to initialize when the DOM is ready
			
		Example:
			>CCC.utils.queue(function() {
			>	// Add code here
			>});
		*/
		queue : function (object) {
			$self.vars.queue.push(object);
		},
		
		/*
		sub: init
		 	When fired, loops through $self.vars.queue and fires each queued function
		*/
		init : function() {
			var queue = $self.vars.queue;

			$.each(queue, function(i, object) {
				for (var key in object) {
					if (object.hasOwnProperty(key) && (typeof object[key] === "function")) {
						object[key]();
					}
				}
				
			});
		},
		
		beginTimer : function() {
			var timer = $(".smartzone-call-timer");
			timer.text("0:00");
			
			$self.vars.minutes = 0;
			$self.vars.seconds = 0;
			
			$self.vars.timer = timer;
			
			$self.vars.interval = window.setInterval($self.utils.tick, 1000);
			$self.utils.tick();
		},
		
		tick : function() {
			var timer = $self.vars.timer;
			$self.vars.minutes = $self.vars.minutes || 0;
			$self.vars.seconds = $self.vars.seconds || 0;
			
			if ($self.vars.seconds == 60) {
				$self.vars.seconds = 0;
				$self.vars.minutes++;
			}
			
			var seconds = $self.vars.seconds++;
			
			if (seconds < 10) {
				seconds = "0" + (seconds.toString());
			}
			
			timer.text($self.vars.minutes + ":" + (seconds));
		}
	};
	
	/*
	Namespace: CCC
		Under the CCC Local Namespace
	*/
	
	/*
	Function: global
	 	Takes care of a few global functionalities:
		- Fires CCC.unqueue
		- Overrides the default jQuery easing
		- Adds the IE BackgroundImageCache fix
	*/
	$self.global = function () {
		
		// Unqueue inline functions
		CCC.unqueue();
		
		if ($.browser.msie) {
			try {
				// Enable background image cache
			    document.execCommand("BackgroundImageCache", false, true);
			} catch (ex) {}
		}
	};
	
	/*
	Function: header
	 	Encapsulates functionality found in the #header space
	*/
	$self.header = function() {};
	
	$self.preventLinkDefaults = function() {
		var links = $("a");
		
		links.bind("click", function(e) {
			e.preventDefault();
		});
	};
	
	$self.preventFormDefaults = function() {
		var forms = $("form");
		
		forms.bind("submit", function(e) {
			e.preventDefault();
		});
	};
	
	$self.overlays = function() {
		var overlay = $("#overlay"),
		    wait = $("#wait-overlay"),
		    saved = $("#saved-overlay"),
		    overlayCloser = $(".close-overlay"),
		    overlayReset = $("#overlay input[type='reset']"),
		    overlaySubmit = $("#overlay input[type='submit']"),
		    overlayTrigger = $("#voice-settings a");
		
		overlayTrigger.bind("click", function(e) {
			var el = $(this),
			    id = el.parent().attr("id"),
			    target = overlay.find("." + id);
			
			overlay.children().hide();
			target.show();
			overlay.toggle();
		});
		
		overlayCloser.bind("click", function(e) {
			overlayReset.trigger("click");
			overlay.toggle(false);
		});
		
		overlayReset.bind("click", function(e) {
			if (overlayReset.parent().hasClass("saved")) {
				e.preventDefault();
			}
			
			$("#overlay form > fieldset > input").removeAttr("disabled");
			
			$(".edit-contact.cancel").trigger("click");
			$(".overlay-inner-tabs > ul > li:first-child > a").trigger("click");
			overlay.toggle();
		});
		
		var preventSubmit = false;
		overlaySubmit.bind("click", function() {
			if (preventSubmit) {
				return false;
			}
			
			wait.css({
				height: $(this).parents("#overlay > div").height() + 7,
				"margin-top" : parseInt($(this).parents("#overlay > div").css("margin-top"), 10) + 22
			});
			wait.show();
			
			$("#overlay form > fieldset > input").removeAttr("disabled");
			
			$(".edit-contact.cancel").trigger("click");
			
			$(this).parent().addClass("saved");
			
			window.setTimeout(function() {
				overlay.children().hide();
				saved.show();
			}, 1000);
			
			return true;
		});
		
		saved.bind("click", function() {
			saved.hide();
			
			$(".overlay-inner-tabs > ul > li:first-child > a").trigger("click");
			overlay.toggle();
		});
		
		// overlay.bind("click", function(e) {
		// 	if (e.target === this) {
		// 		overlay.toggle();
		// 	}
		// });
		
		$("#overlay label").bind("click", function(e) {
			e.stopPropagation();
		});
		
		$(".overlay-inner-tabs ol > li > a").bind("click", function() {
			$(this).parent().siblings().removeClass("active");
			$(this).parent().addClass("active");
		});
		
		$(".overlay-inner-tabs > ul > li > a").bind("click", function() {
			$(this).parent().siblings().removeClass("active");
			$(this).parent().addClass("active");
		});
		
		$(".overlay-inner-tabs ul.contacts > li > a").bind("click", function() {
			$(this).parent().siblings().removeClass("active");
			$(this).parent().addClass("active");
		});
		
		$(".overlay-inner-tabs ul.contacts .edit-contact").bind("click", function() {
			$("#overlay").toggleClass("editable");
		});
		
		$(".overlay-inner-tabs ul.contacts input, .overlay-inner-tabs ul.contacts select").each(function() {
			var el = $(this);
			
			var wrap = $('<span></span>').addClass("non-editable").addClass(el[0].type || el[0].nodeName.toLowerCase());
			wrap.append('<span class="placeholder">' + el.val() + '</span>');
			
			if (el.is(":checkbox") && el.is(":checked")) {
				wrap.addClass("checked");
			}
			
			wrap.insertAfter(el);
			wrap.append(el);
		});
		
		$(".overlay-inner-tabs ul.contacts select").bind("change", function() {
			$(this).parent().find("span").text($(this).val());
		});
		
		$(".call-screening :checkbox, .do-not-disturb :checkbox").bind("click", function() {
			if ($(this).is(":checked")) {
				$(this).closest("li").find("input").not($(this)).click();
			}
		});
		
		$(".overlay-inner-tabs ul.contacts").bind("click", function() {
			$(this).closest(".non-editable").toggleClass("checked");
		});
		
		$(".overlay-inner-tabs ul.contacts :radio").each(function() {
			var el = $(this),
			    parent = el.closest(".screening-options");
			
			el.bind("click", function() {
				if (el.hasClass("on")) {
					parent.removeClass("off").addClass("on");
				} else {
					parent.removeClass("on").addClass("off");
				}
			});
			
		});
		
		$("#overlay form").bind("submit", function() {
			if (preventSubmit) {
				return false;
			}
			
			var on = $("input.on"),
				action = "removeClass";
			
			if ($(this).closest(".call-forwarding").get(0)) {
				if (on.is(":checked")) {
					action = "addClass";
				}
				window.setTimeout(function() {
					$("#call-forwarding-status")[action]("on");
				}, 1000);
			}
			
			if ($(this).closest(".call-routing").get(0)) {
				window.setTimeout(function() {
					$("#call-routing-status").addClass("on");
				}, 1000);
			}
			
			if ($(this).closest(".call-screening").get(0)) {
				if (on.is(":checked")) {
					window.setTimeout(function() {
						$("#call-screening-status").addClass("on");
					}, 1000);
				} else {
					window.setTimeout(function() {
						$("#call-screening-status").removeClass("on");
					}, 1000);
				}
			}
			
			if ($(this).closest(".call-blocking").get(0)) {
				if (on.is(":checked")) {
					action = "addClass";
				}
				window.setTimeout(function() {
					$("#call-blocking-status")[action]("on");
				}, 1000);
			}
			
			if ($(this).closest(".do-not-disturb").get(0)) {
				if (on.is(":checked")) {
					window.setTimeout(function() {
						$("#do-not-disturb-status").addClass("on");
					}, 1000);
				} else {
					window.setTimeout(function() {
						$("#do-not-disturb-status").removeClass("on");
					}, 1000);
				}
			}
			
			return true;
		});
		
		var editButtons = $("#overlay .edit-contact");
		
		editButtons.bind("click", function() {
			if ($("#overlay").hasClass("editable")) {
				
				$("#overlay form > fieldset > input").attr("disabled", "disabled");
				
				editButtons.text("Ok");
				
				$('<a href="#" class="edit-contact cancel">Cancel</a>').bind("click", function(e) {
					e.preventDefault();
					$("#overlay").toggleClass("editable");
					editButtons.text("Edit");
					$(".edit-contact.cancel").remove();
					$("#overlay form > fieldset > input").attr("disabled", false);
				}).insertAfter(editButtons);
			} else {
				
				$("#overlay form > fieldset > input").removeAttr("disabled");
				
				editButtons.text("Edit");
				editButtons.next().remove();
			}
		});
		
		$("#overlay .ringer-options select").bind("change", function() {
			if ($(this).val() == "Custom") {
				$(this).closest(".ringer-options").find("p").show();
			}
		});
		
		$("#overlay .editable-phone")
			.find("input").bind("focus", function () {
				$(this).parent().addClass("focus");
				if ($(this).val() === "add number to list") {
					$(this).val("");
				}
			}).bind("blur", function () {
				$(this).parent().removeClass("focus");
				if ($(this).val() === "") {
					$(this).val("add number to list");
				}
			})
		.end()
			.find("a").bind("click", function () {
				if ($(this).siblings("input").val() !== "add number to list") {
					var clone = $(this).parent().clone(true);
					
					$(this).parent().removeClass("focus empty");
					$(this).text("×");
					
					clone.find("input").val("add number to list");
					$(this).parents("ul").append(clone);
				}
			});
			
		$("#overlay div.call-routing")
			.find(".edit-btn").click(function () {
				try { 
					if ($($(this).attr("href")).length) {
						$($(this).attr("href")).show();
					} else {
						return;
					}
				} catch(e) { return; }
				$("#routing-home").hide();
				$("#overlay div.call-routing").css("min-height", $("#overlay div.call-routing form").height());
				preventSubmit = true;
				overlaySubmit.bind("click", returnToRoutingHome);
			})
		.end()
			.find(".default-radio").change(function () {
				$(this).parents("td").find("input[type=checkbox]").attr("disabled", $(this).attr("checked"));
			})
		.end()
			.find(".custom-radio").change(function () {
				$(this).parents("td").find("input[type=checkbox]").attr("disabled", ! $(this).attr("checked"));
			});
			
		function returnToRoutingHome (e) {
			$("#routing-home").show();
			$("#overlay div.call-routing .edit-screen").hide();
			$("#overlay .edited, #overlay .not-edited").toggle();
			$("#overlay div.call-routing").css("min-height", $("#overlay div.call-routing form").height());
			preventSubmit = false;
			overlaySubmit.unbind("click", returnToRoutingHome);
		}
		
	};
	
	$self.smartzone = function() {
		var _timer;
		
		$(".tooltip-link").hover(function() {
			if (_timer) {
				window.clearTimeout(_timer);
			}
			$(".smartzone-tooltip").addClass($(this).attr("data-tip-class")).show();
		}, function() {
			_timer = window.setTimeout(function() {
				$(".smartzone-tooltip").removeClass("one two").hide();
			}, 1000);
		}).click(function () {
			showSelectScreen();
		});
		
		$(".smartzone-tooltip").hover(function() {
			if (_timer) {
				window.clearTimeout(_timer);
			}
		}, function() {
			_timer = window.setTimeout(function() {
				$(".smartzone-tooltip").removeClass("one two").hide();
			}, 1000);
		});
		
		$(".smartzone-tooltip").bind("click", function() {
			showSelectScreen();
		});
		
		$("#smartzone-call-btn").bind("click", function () {
			showCallingScreen();
		});
		
		$("#smartzone-cancel-btn").bind("click", function () {
			$("#smartzone-select-overlay").hide();
		});
		
		function showSelectScreen () {
			$(".smartzone-tooltip").hide();
			$("#smartzone-select-overlay").show();
		}
		
		function showCallingScreen () {
			$("#smartzone-overlay").show();
			$("#smartzone-select-overlay").hide();
			$(".smartzone-tooltip").hide();
			
			if ($self.vars.interval) {
				window.clearInterval($self.vars.interval);
			}
			
			$(".smartzone-call-timer").text("Pick up your Cell phone to complete dialing...");
			
			window.setTimeout($self.utils.beginTimer, 2000);
		}
		
		$("#smartzone-overlay").bind("click", function() {
			$(this).hide();
		});
	};
	
	/*
	Callback: init
		Sends local functions to a global queuer for initialization See: <CCC.utils.queue>
	*/
	$self.utils.queue($self);
	
}).call(CCC);