<?php
	require 'header.php';

	mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
	try {
		$db = new mysqli($host, $user, $pass, $dbname, $port);
		$db->set_charset($charset);
		$db->options(MYSQLI_OPT_INT_AND_FLOAT_NATIVE, 1);


		$stmt = $db->prepare("SELECT count(*) AS _count FROM `customers` WHERE `hwid`=?");
		$stmt->bind_param("s", $_GET["hwid"]);
		$stmt->execute();
		$result = $stmt->get_result()->fetch_assoc()["_count"];
		echo $result;
	} catch (mysqli_sql_exception $e) {
		throw new mysqli_sql_exception($e->getMessage(), $e->getCode());
	}
unset($host, $dbname, $user, $pass, $charset, $port); // we don't need them anymore
?>
