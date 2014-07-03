$(function() {
	function submitRule() {
		mod = document.getElementById('rule-creator-modifier').value
		sym = document.getElementById('rule-creator-symbol').value
		com = document.getElementById('rule-creator-comparator').value
		thr = document.getElementById('rule-creator-threshold').value
		url = document.getElementById('rule-creator-url').value

		$.ajax({
			url: "http://54.183.118.189:5002/",
			type: "POST",
			ContentType: "application/json",
			data: {"data": JSON.stringify({"symbol":sym,"modifier":mod,"comparator":com,"threshold":parseFloat(thr),"url":url})},
			success: function(msg) { alert(msg["status"]) }
		})
	}

	document.getElementById('rule-creator-submit').onclick = function() {submitRule()};
});
