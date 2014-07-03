$(function () {
	var timeout = 500
	var prev_bid = {'AUDUSD':0,'EURUSD':0,'GBPUSD':0,'NZDUSD':0,'USDCAD':0,'USDCHF':0,'USDJPY':0}
	var prev_ask = {'AUDUSD':0,'EURUSD':0,'GBPUSD':0,'NZDUSD':0,'USDCAD':0,'USDCHF':0,'USDJPY':0}
	var pairs    = ['AUDUSD','EURUSD','GBPUSD','NZDUSD','USDCAD','USDCHF','USDJPY']

	function resetPlots() {
		for (p_i = 0; p_i < pairs.length; ++p_i) {
			var n = pairs[p_i]
			var p = '#placeholder-'.concat(n)
			$.plot(p,[])
			$(p).append("<div style='position:absolute;left:" + 
			10 + "px;top:" + 10 + "px;color:#666;font-size:smaller'>" + n + " waiting for the data...</div>");
		}
	}

	function queryData() {
		function onError() {
			resetPlots();
		}
function onDataReceived(json) {
			var end   = new Date().getTime() 
			var start = end - 60 * 1000
			for (p_i = 0 ; p_i < pairs.length; ++p_i) {
				p = pairs[p_i]
				var plt_bid = [[start,prev_bid[p]]]
				var plt_ask = [[start,prev_ask[p]]]
				for (var i = 0; i < json.ticks[p].length; ++i) {
					ct = new Date(json.ticks[p][i][0]).getTime()
					if (ct > start) {
						plt_bid.push([ ct, prev_bid[p] ])
						plt_ask.push([ ct, prev_ask[p] ])
						prev_bid[p] = json.ticks[p][i][1]
						prev_ask[p] = json.ticks[p][i][2]
						plt_bid.push([ ct, prev_bid[p] ])
						plt_ask.push([ ct, prev_ask[p] ])
					}
				}
				plt_bid.push([end, prev_bid[p]])
				plt_ask.push([end, prev_ask[p]])
				if (document.getElementById('container-'.concat(p)).style.width!="0px" &&
					document.getElementById('container-'.concat(p)).style.height!="0px") {
					$.plot('#placeholder-'.concat(p),
					[{label: "bid", data: plt_bid}, {label: "ask", data: plt_ask}],
					{ xaxis: {mode: "time", timezone: "browser"}, legend: {position: "sw"}})

					$('#placeholder-'.concat(p)).append("<div style='position:absolute;left:" +
					60 + "px;top:" + 10 + "px;color:#666;font-size:smaller'>" + p + "</div>");
				}

			}
		}

		$.ajax({
			cache: false,
			url: "http://54.183.118.189:5000",
			type: "GET",
			dataType: "json",
			success: onDataReceived,
			error: onError
		});

		setTimeout(queryData, timeout)
	}

	resetPlots()
	
	queryData()
});
