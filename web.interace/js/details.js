$(function() {
	var containers = ['container-AUDUSD','container-EURUSD','container-GBPUSD','container-NZDUSD',
		'container-USDCAD','container-USDCHF','container-USDJPY', 'container-rule-listing', 'container-rule-creator'];
	var containers2 = ['container-AUDUSD','container-EURUSD','container-GBPUSD','container-NZDUSD',
		'container-USDCAD','container-USDCHF','container-USDJPY' ];


	function toggleVis(sym) {
		id  = 'container-'.concat(sym)
		if (document.getElementById(id).hasOwnProperty("detailed")) {
		// it was big, let's make all small
			for (var p_i=0; p_i < containers.length; ++p_i) {
				document.getElementById(containers[p_i]).style.visibility = "";
				delete document.getElementById(containers[p_i]).detailed; 
				document.getElementById(containers[p_i]).style.width = "420px";
				document.getElementById(containers[p_i]).style.height = "200px";
			}
			for (var p_i=0; p_i < containers2.length; ++p_i) {
				document.getElementById(containers[p_i].concat('_avg_s')).style.visibility = "hidden";
				document.getElementById(containers[p_i].concat('_avg_s')).style.width = "0px";
				document.getElementById(containers[p_i].concat('_avg_s')).style.height = "0px";
			}
		} else {
		// it was small, let's make it big
			for (var p_i=0; p_i < containers.length; ++p_i) {
				document.getElementById(containers[p_i]).style.visibility = "hidden";
				delete document.getElementById(containers[p_i]).detailed;

				document.getElementById(containers[p_i]).style.width = "0px";
				document.getElementById(containers[p_i]).style.height = "0px";
			}
			document.getElementById(id).style.visibility = "";
			document.getElementById(id).detailed = "true";	
			document.getElementById(id).style.width = "1250px";
			document.getElementById(id).style.height = "300px";

			document.getElementById(id.concat('_avg_s')).style.visibility  = "";
			document.getElementById(id.concat('_avg_s')).style.position  = "relative";
			document.getElementById(id.concat('_avg_s')).style.width  = "100%";
			document.getElementById(id.concat('_avg_s')).style.height = "300px";
		}
	}

	document.getElementById('container-AUDUSD').onclick = function() {toggleVis('AUDUSD')};
	document.getElementById('container-EURUSD').onclick = function() {toggleVis('EURUSD')};
	document.getElementById('container-GBPUSD').onclick = function() {toggleVis('GBPUSD')};
	document.getElementById('container-NZDUSD').onclick = function() {toggleVis('NZDUSD')};
	document.getElementById('container-USDCAD').onclick = function() {toggleVis('USDCAD')};
	document.getElementById('container-USDCHF').onclick = function() {toggleVis('USDCHF')};
	document.getElementById('container-USDJPY').onclick = function() {toggleVis('USDJPY')};
});

