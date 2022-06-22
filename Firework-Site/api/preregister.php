<?php
	require 'header.php';

	mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
	try {
		$db = new mysqli($host, $user, $pass, $dbname, $port);
		$db->set_charset($charset);
		$db->options(MYSQLI_OPT_INT_AND_FLOAT_NATIVE, 1);


		$stmt = $db->prepare("INSERT INTO `verification_codes`(`email`, `code`) VALUES (?, ?)");
		$code = rand(1000000,8000000);
		$stmt->bind_param("si", $_GET["email"], $code);
		$stmt->execute();
		mysqli_commit($db);

		mail($_GET["email"], "Firework Verification Code", "Your code: " . $code);
	} catch (mysqli_sql_exception $e) {
		throw new mysqli_sql_exception($e->getMessage(), $e->getCode());
	}
	unset($host, $dbname, $user, $pass, $charset, $port); // we don't need them anymore
?>
