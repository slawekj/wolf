<script>
function addIf() {
	var rule = document.getElementById('ruletext').value;
	document.getElementById('ruletext').value = rule.concat('If');
	var rulecode = document.getElementById('rulecode').value;
	document.getElementById('rulecode').value = rulecode.concat('if (');

	document.getElementById('if').style.display='none';
        document.getElementById('columnleft').style.display='block';
        document.getElementById('columnright').style.display='none';
        document.getElementById('constantright').style.display='none';
        document.getElementById('eq').style.display='none';
        document.getElementById('lt').style.display='none';
        document.getElementById('gt').style.display='none';
	document.getElementById('and').style.display='none';
	document.getElementById('or').style.display='none';
	document.getElementById('then').style.display='none';
	document.getElementById('action').style.display='none';
	document.getElementById('done').style.display='none';
	document.getElementById('submit').style.display='none';
	return false;
}

function addAnd() {
	var rule = document.getElementById('ruletext').value;
	document.getElementById('ruletext').value = rule.concat(' and');
	var rulecode = document.getElementById('rulecode').value;
	document.getElementById('rulecode').value = rulecode.concat(' &&');

	document.getElementById('if').style.display='none';
        document.getElementById('columnleft').style.display='block';
        document.getElementById('columnright').style.display='none';
        document.getElementById('constantright').style.display='none';
        document.getElementById('eq').style.display='none';
        document.getElementById('lt').style.display='none';
        document.getElementById('gt').style.display='none';
	document.getElementById('and').style.display='none';
	document.getElementById('or').style.display='none';
	document.getElementById('then').style.display='none';
	document.getElementById('action').style.display='none';
	document.getElementById('done').style.display='none';
        document.getElementById('submit').style.display='none';
	return false;
}

function addOr() {
        var rule = document.getElementById('ruletext').value;
        document.getElementById('ruletext').value = rule.concat(' or');
        var rulecode = document.getElementById('rulecode').value;
        document.getElementById('rulecode').value = rulecode.concat(' ||');

	document.getElementById('if').style.display='none';
        document.getElementById('columnleft').style.display='block';
        document.getElementById('columnright').style.display='none';
        document.getElementById('constantright').style.display='none';
        document.getElementById('eq').style.display='none';
        document.getElementById('lt').style.display='none';
        document.getElementById('gt').style.display='none';
	document.getElementById('and').style.display='none';
	document.getElementById('or').style.display='none';
	document.getElementById('then').style.display='none';
	document.getElementById('action').style.display='none';
	document.getElementById('done').style.display='none';
        document.getElementById('submit').style.display='none';
	return false;
}

function addThen() {
        var rule = document.getElementById('ruletext').value;
        document.getElementById('ruletext').value = rule.concat(' then');
        var rulecode = document.getElementById('rulecode').value;
        document.getElementById('rulecode').value = rulecode.concat(' ) {\n');

	document.getElementById('if').style.display='none';
        document.getElementById('columnleft').style.display='none';
        document.getElementById('columnright').style.display='none';
        document.getElementById('constantright').style.display='none';
        document.getElementById('eq').style.display='none';
        document.getElementById('lt').style.display='none';
        document.getElementById('gt').style.display='none';
	document.getElementById('and').style.display='none';
	document.getElementById('or').style.display='none';
	document.getElementById('then').style.display='none';
	document.getElementById('action').style.display='block';
	document.getElementById('done').style.display='none';
        document.getElementById('submit').style.display='none';
	return false;
}

function addAction() {
        var rule = document.getElementById('ruletext').value;
        var rulecode = document.getElementById('rulecode').value;

	var combo = document.getElementById('actiontype');
	var typ = combo.options[combo.selectedIndex].value;
	var count = document.getElementById('actioncount').value;
	var nam = document.getElementById('actioncompany').value;
	if (typ == "buy") {
        	document.getElementById('ruletext').value = rule.concat(' buy ' + count + ' shares of ' + nam + '.\n');
        	document.getElementById('rulecode').value = rulecode.concat('\tbuy(\"' + nam + '\",' + count + ')\n}\n');
	} else {
        	document.getElementById('ruletext').value = rule.concat(' sell ' + count + ' shares of ' + nam + '.\n');
        	document.getElementById('rulecode').value = rulecode.concat('\tsell(\"' + nam + '\",' + count + ')\n}\n');
	}

	document.getElementById('if').style.display='block';
        document.getElementById('columnleft').style.display='none';
        document.getElementById('columnright').style.display='none';
        document.getElementById('constantright').style.display='none';
        document.getElementById('eq').style.display='none';
        document.getElementById('lt').style.display='none';
        document.getElementById('gt').style.display='none';
	document.getElementById('and').style.display='none';
	document.getElementById('or').style.display='none';
	document.getElementById('then').style.display='none';
	document.getElementById('action').style.display='none';
	document.getElementById('done').style.display='block';
        document.getElementById('submit').style.display='none';
	return false;
}

function addColumnLeft() {
        var rule = document.getElementById('ruletext').value;
        var rulecode = document.getElementById('rulecode').value;
	var combo  = document.getElementById('coltypeleft');
	var typ  = combo.options[combo.selectedIndex].value;
	var col  = document.getElementById('colnameleft').value;
        document.getElementById('ruletext').value = rule.concat(' ' + typ + ' of ' + col);
        document.getElementById('rulecode').value = rulecode.concat(' getMarket()[\"' + col + ':' + typ.toUpperCase() + '\"]');

	document.getElementById('if').style.display='none';
        document.getElementById('columnleft').style.display='none';
        document.getElementById('columnright').style.display='none';
        document.getElementById('constantright').style.display='none';
        document.getElementById('eq').style.display='block';
        document.getElementById('lt').style.display='block';
        document.getElementById('gt').style.display='block';
	document.getElementById('and').style.display='none';
	document.getElementById('or').style.display='none';
	document.getElementById('then').style.display='none';
	document.getElementById('action').style.display='none';
	document.getElementById('done').style.display='none';
        document.getElementById('submit').style.display='none';
	return false;
}

function addColumnRight() {
        var rule = document.getElementById('ruletext').value;
        var rulecode = document.getElementById('rulecode').value;
	var combo  = document.getElementById('coltyperight');
	var typ  = combo.options[combo.selectedIndex].value;
	var col  = document.getElementById('colnameright').value;
        document.getElementById('ruletext').value = rule.concat(' ' + typ + ' of ' + col);
        document.getElementById('rulecode').value = rulecode.concat(' getMarket()[\"' + col + ':' + typ.toUpperCase() + '\"]');

	document.getElementById('if').style.display='none';
        document.getElementById('columnleft').style.display='none';
        document.getElementById('columnright').style.display='none';
        document.getElementById('constantright').style.display='none';
        document.getElementById('eq').style.display='none';
        document.getElementById('lt').style.display='none';
        document.getElementById('gt').style.display='none';
	document.getElementById('and').style.display='block';
	document.getElementById('or').style.display='block';
	document.getElementById('then').style.display='block';
	document.getElementById('action').style.display='none';
	document.getElementById('done').style.display='none';
        document.getElementById('submit').style.display='none';
	return false;
}

function addConstantRight() {
        var rule = document.getElementById('ruletext').value;
        var rulecode = document.getElementById('rulecode').value;
	var col  = document.getElementById('constname').value;
        document.getElementById('ruletext').value = rule.concat(' ' + col);
        document.getElementById('rulecode').value = rulecode.concat(' ' + col);

	document.getElementById('if').style.display='none';
        document.getElementById('columnleft').style.display='none';
        document.getElementById('columnright').style.display='none';
        document.getElementById('constantright').style.display='none';
        document.getElementById('eq').style.display='none';
        document.getElementById('lt').style.display='none';
        document.getElementById('gt').style.display='none';
	document.getElementById('and').style.display='block';
	document.getElementById('or').style.display='block';
	document.getElementById('then').style.display='block';
	document.getElementById('action').style.display='none';
	document.getElementById('done').style.display='none';
        document.getElementById('submit').style.display='none';
	return false;
}


function addEq() {
        var rule = document.getElementById('ruletext').value;
        var rulecode = document.getElementById('rulecode').value;
        document.getElementById('ruletext').value = rule.concat(' is equal to');
        document.getElementById('rulecode').value = rulecode.concat(' ==');

	document.getElementById('if').style.display='none';
        document.getElementById('columnleft').style.display='none';
        document.getElementById('columnright').style.display='block';
        document.getElementById('constantright').style.display='block';
        document.getElementById('eq').style.display='none';
        document.getElementById('lt').style.display='none';
        document.getElementById('gt').style.display='none';
	document.getElementById('and').style.display='none';
	document.getElementById('or').style.display='none';
	document.getElementById('then').style.display='none';
	document.getElementById('action').style.display='none';
	document.getElementById('done').style.display='none';
        document.getElementById('submit').style.display='none';
	return false;
}

function addLt() {
        var rule = document.getElementById('ruletext').value;
        var rulecode = document.getElementById('rulecode').value;
        document.getElementById('ruletext').value = rule.concat(' is less than');
        document.getElementById('rulecode').value = rulecode.concat(' <');

	document.getElementById('if').style.display='none';
        document.getElementById('columnleft').style.display='none';
        document.getElementById('columnright').style.display='block';
        document.getElementById('constantright').style.display='block';
        document.getElementById('eq').style.display='none';
        document.getElementById('lt').style.display='none';
        document.getElementById('gt').style.display='none';
	document.getElementById('and').style.display='none';
	document.getElementById('or').style.display='none';
	document.getElementById('then').style.display='none';
	document.getElementById('action').style.display='none';
	document.getElementById('done').style.display='none';
        document.getElementById('submit').style.display='none';
	return false;
}

function addGt() {
        var rule = document.getElementById('ruletext').value;
        var rulecode = document.getElementById('rulecode').value;
        document.getElementById('ruletext').value = rule.concat(' is more than');
        document.getElementById('rulecode').value = rulecode.concat(' >');

	document.getElementById('if').style.display='none';
        document.getElementById('columnleft').style.display='none';
        document.getElementById('columnright').style.display='block';
        document.getElementById('constantright').style.display='block';
        document.getElementById('eq').style.display='none';
        document.getElementById('lt').style.display='none';
        document.getElementById('gt').style.display='none';
	document.getElementById('and').style.display='none';
	document.getElementById('or').style.display='none';
	document.getElementById('then').style.display='none';
	document.getElementById('action').style.display='none';
	document.getElementById('done').style.display='none';
        document.getElementById('submit').style.display='none';
	return false;
}

function done() {
	document.getElementById('if').style.display='none';
        document.getElementById('columnleft').style.display='none';
        document.getElementById('columnright').style.display='none';
        document.getElementById('constantright').style.display='none';
        document.getElementById('eq').style.display='none';
        document.getElementById('lt').style.display='none';
        document.getElementById('gt').style.display='none';
	document.getElementById('and').style.display='none';
	document.getElementById('or').style.display='none';
	document.getElementById('then').style.display='none';
	document.getElementById('action').style.display='none';
	document.getElementById('done').style.display='none';
        document.getElementById('submit').style.display='block';
	return false;
}

</script>

<html>
<body>
<h1>Welcome to Wolf!</h1>
<h2>Build your set of rules below.</h2>

<textarea readonly rows="10" cols="100" id="ruletext"></textarea>
<form action="simulate.php" method="post">
<table>

<td rowspan="10">
<div id="if" style="display:block;" onclick="return addIf();"><button>if</button></div>
<div id="and" style="display:none;" onclick="return addAnd();"><button>and</button></div>
<div id="or" style="display:none;" onclick="return addOr();"><button>or</button></div>
<div id="then" style="display:none;" onclick="return addThen();"><button>then</button></div>
</td>

<td rowspan="10">
<div id="columnleft" style="display:none;">
	<select id="coltypeleft">
		<option value="price">Price</option>
	</select>
	of
	<input id="colnameleft" value="Company"></input><button onclick="return addColumnLeft();">add</button>
</div>
</td>

<td rowspan="10">
<div id="eq" style="display:none;"><button onclick="return addEq();">is equal to</button></div>
<div id="lt" style="display:none;"><button onclick="return addLt();">is less than</button></div>
<div id="gt" style="display:none;"><button onclick="return addGt();">is more than</button></div>
</td>

<td rowspan="10">
<div id="columnright" style="display:none;">
	<select id="coltyperight">
		<option value="price">Price</option>
	</select>
	of
	<input id="colnameright" value="Company"></input><button onclick="return addColumnRight();">add</button>
</div>
<div id="constantright" style="display:none;"><input id="constname" value="constname"></input><button onclick="return addConstantRight();">add</button></div>
</td>

<td>
<div id="action" style="display:none;">
	<select id="actiontype">
		<option value="buy">Buy</option>
		<option value="sell">Sell</option>
	</select>
	<input id="actioncount"></input>
	shares of
	<input id="actioncompany"></input>
	<button onclick="return addAction();">add</button>
</div>
</td>

<td>
<div id="done" style="display:none;" onclick="return done();"><button>done</button></div>
<input id="submit" style="display:none;" type="submit" value="Submit">
</td>
</table>
<div style="display:none">
<textarea readonly id="rulecode" name="rulecode" rows="10" cols="100"></textarea>
</div>
</form>
</body>
</html>
