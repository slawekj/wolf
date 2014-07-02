$(function () {
	function checkTime(i) {
		if (i<10) {i = "0" + i};  // add zero in front of numbers < 10
		return i;
	}

	function showTime() {
		var today=new Date();
		var h=today.getHours();
		var m=today.getMinutes();
		var s=today.getSeconds();
		m = checkTime(m);
		s = checkTime(s);
		document.getElementById("timer").innerHTML = h+":"+m+":"+s;
		setTimeout(function(){showTime()},500);
	}
	
	showTime()
});
