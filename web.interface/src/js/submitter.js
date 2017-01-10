$(function() {
	function submitRule() {
		$.ajax({
			url: "http://54.183.118.189:5002/",
			type: "POST",
			ContentType: "application/json",
			data: {"data": JSON.stringify({
				"symbol":"NZDUSD",
				"modifier":"ASK",
				"comparator":">",
				"threshold":0.001,
				"url":"http://54.183.118.189:5002/"})
			},
			success: function(msg) { console.log("rule sent") }
		})

		setTimeout(submitRule, 500)
	}

	submitRule()

});
