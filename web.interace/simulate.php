<html>
<body>
<a href="rule.php">back</a>
<?php
// process simulation in R
file_put_contents("upload/rules.R",$_POST["rulecode"]);
system('R CMD BATCH --no-save ignite.R upload/ignite.Rout');
?>
<h1>Simulation Result</h1>
<img src="upload/TotalValue.jpg"/>
</html>
</body>
