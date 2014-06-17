$(function () {
	//$.ajaxSetup({ cache:false });
	
	var timeout = 1000
	var initialValue = 0
	var prev_bid = 0
	var prev_ask = 0

	function queryData() {
		function onError() {
			timeout = 5000
			$.plot('#placeholder',[])
			$("#placeholder").append("<div style='position:absolute;left:" + 
				100 + "px;top:" + 100 + "px;color:#666;font-size:smaller'>Waiting for the data...</div>");
		}

		function onDataReceived(json) {
			timeout = 1000

			var end   = new Date().getTime()
			var start = end - 60 * 1000

			var plt_bid = [[start,prev_bid]]
			var plt_ask = [[start,prev_ask]]
			for (var i = 0; i < json.ticks.NZDUSD.length; ++i) {
				ct = new Date(json.ticks.NZDUSD[i][0]).getTime()
				if (ct > start) {
					plt_bid.push([ ct, prev_bid ])
					plt_ask.push([ ct, prev_ask ])
					prev_bid = json.ticks.NZDUSD[i][1]
					prev_ask = json.ticks.NZDUSD[i][2]
					plt_bid.push([ ct, prev_bid ])
					plt_ask.push([ ct, prev_ask ])
				}
			}
			plt_bid.push([end, prev_bid])
			plt_ask.push([end, prev_ask])
			//$.plot('#placeholder',[plt_bid, plt_ask],{ xaxis: {mode: "time", timezone: "browser"}})
			$.plot('#placeholder',
				[{label: "bid", data: plt_bid}, {label: "ask", data: plt_ask}],
				{ xaxis: {mode: "time", timezone: "browser"}})
		}

		$.ajax({
			cache: false,
			url: "http://localhost:5000",
			type: "GET",
			dataType: "json",
			success: onDataReceived,
			error: onError
		});

		setTimeout(queryData, timeout)
	}

	// Set up the con$.plot('#placeholder',[])
	$.plot('#placeholder',[])
	$("#placeholder").append("<div style='position:absolute;left:" + 
	100 + "px;top:" + 100 + "px;color:#666;font-size:smaller'>Waiting for the data...</div>");
	
	queryData()
});
