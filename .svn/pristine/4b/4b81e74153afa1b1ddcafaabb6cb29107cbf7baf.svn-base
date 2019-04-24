// Create the auto scroll region

(function($) {

	$.fn.autoVerticalScroll = function(options) {
		var opts = $.extend({}, $.fn.autoVerticalScroll.defaults, options);
		var myMain = $(this);
		var myContainer = $(this).children('div:first');
		var numBlocks = $(myContainer).children('div').length;

		if (numBlocks <= 1)
			return;		//	Not enough blocks to do anything
		if (opts.visualBlocks) {
			for (var i=opts.visualBlocks; i < numBlocks; i++)
				$($(myContainer).children('div')[i]).css("opacity", "0");
		}
		else {
			if ($(myContainer).outerHeight() <= $(this).outerHeight())
				return;		// have enough space to show all elements, no need to scroll
		}

		var intervalId = setTimeout(rotateBlocks, opts.interval);

		if (opts.onmouseover == 'stop') {
			//	Should stop sliding when mouseover
			$(myContainer).mouseover(function() {
				clearTimeout(intervalId);
			});
			$(myContainer).mouseout(function() {
				intervalId = setTimeout(rotateBlocks, opts.interval);
			});
		}

		function rotateBlocks() {

			if (opts.direction == 'up' || opts.direction == 'top') {
				//	Fadeout the 1st block
				$($(myContainer).children('div:first')).animate({ opacity: 0 }, opts.fadeOutSpeed, "linear", function() {

					//	After fadeout the 1st block, move up the 2nd block
					var firstChild = $(myContainer).children('div:first');
					var ognTopMargin = $(firstChild).css('margin-top');
					var speed = opts.easingSpeed * $(firstChild).outerHeight();

					$(firstChild).animate({ marginTop: -$(firstChild).outerHeight() }, speed, "linear", function() {
						//	After move up the 1st block, reset the the margin and append it to the back
						$(firstChild).css({
							"margin-top" : ognTopMargin
						});

						$(myContainer).append($(firstChild));
						if (opts.visualBlocks) {
							//$($(myContainer).children('div')[opts.visualBlocks - 1]).animate({ opacity: 1 }, opts.fadeInSpeed);
							$($(myContainer).children('div')[opts.visualBlocks - 1]).css("opacity", "1");
						}
						else {
							//$(myContainer).children('div:last').animate({ opacity: 1 }, opts.fadeInSpeed);
							$(myContainer).children('div:last').css("opacity", "1");
						}

						//	Set the time interval again
						intervalId = setTimeout(rotateBlocks, opts.interval);
					});					
				});
			}
			else {
				//	Remove the last block by make it invisible
				if (opts.visualBlocks) {
					//$($(myContainer).children('div')[opts.visualBlocks - 1]).animate({ opacity: 1 }, opts.fadeOutSpeed);
					$($(myContainer).children('div')[opts.visualBlocks - 1]).css("opacity", "0");
				}
				else {
					//$(myContainer).children('div:last').animate({ opacity: 1 }, opts.fadeOutSpeed);
					$(myContainer).children('div:last').css("opacity", "0");
				}

				//	After make the last block invisible, move down the 1st block to leave space for insertion
				var firstChild = $(myContainer).children('div:first');
				var lastChild = $(myContainer).children('div:last');
				var ognTopMargin = $(firstChild).css('margin-top');
				var speed = opts.easingSpeed * $(lastChild).outerHeight();

				$(myContainer).children('div:first').animate({ marginTop: $(lastChild).outerHeight() }, speed, "linear", function() {

					//	After move down the 1st block, prepend the block
					$(firstChild).css({
						"margin-top" : ognTopMargin
					});
					$(myContainer).prepend(lastChild);
					$($(myContainer).children('div:first')).animate({ opacity: 1 }, opts.fadeInSpeed, "linear");

					//	Set the time interval again
					intervalId = setTimeout(rotateBlocks, opts.interval);
				});
			}
		}
	}

	$.fn.autoVerticalScroll.defaults = {
		visualBlocks : 0,		// For show all blocks
		fadeOutSpeed : 500,
		fadeInSpeed : 500,
		easingSpeed : 20,
		interval: 5000,
		onmouseover	: 'continue',
		direction: 'up'
	};

})(jQuery);