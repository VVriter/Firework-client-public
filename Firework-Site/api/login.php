<?php
	require 'header.php';

	mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
	try {
		$db = new mysqli($host, $user, $pass, $dbname, $port);
		$db->set_charset($charset);
		$db->options(MYSQLI_OPT_INT_AND_FLOAT_NATIVE, 1);


		$stmt = $db->prepare("SELECT count(*) FROM `customers` WHERE `email` = ? AND `password` = ?");
		$stmt->bind_param("ss", $_GET["email"], $_GET["password"]);
		$stmt->execute();
		$result = $stmt->get_result()->fetch_assoc()["_count"];
		if($result == 1) {
			echo 1;
		} else {
			echo 0;
		}
	} catch (mysqli_sql_exception $e) {
		throw new mysqli_sql_exception($e->getMessage(), $e->getCode());
	}
	unset($host, $dbname, $user, $pass, $charset, $port); // we don't need them anymore
?>
