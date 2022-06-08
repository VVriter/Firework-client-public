<?php
	$host = '31.31.196.85';
	$port = 3306;
	$dbname = 'u0910511_firework_db';
	$user = 'u0910511_admin';
	$pass = 'fireworkmanager';
	$charset = 'utf8mb4';

	mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
	try {
		$db = new mysqli($host, $user, $pass, $dbname, $port);
		$db->set_charset($charset);
		$db->options(MYSQLI_OPT_INT_AND_FLOAT_NATIVE, 1);


		$stmt = $db->prepare("SELECT count(*) AS _count FROM `verification_codes` WHERE `code`= ?");
		$stmt->bind_param("i", $_GET["code"]);
		$stmt->execute();
		$result = $stmt->get_result()->fetch_assoc()["_count"];
		if($result == 1) {
			$stmt = $db->prepare("DELETE FROM `verification_codes` WHERE `code` = ?");
			$stmt->bind_param("i", $_GET["code"]);
			$stmt->execute();

			$stmt = $db->prepare("INSERT INTO `customers`(`email`, `password`) VALUES (?, ?)");
			$stmt->bind_param("ss", $_GET["email"], $_GET["password"]);
			$stmt->execute();

			mysqli_commit($db);
		}
	} catch (mysqli_sql_exception $e) {
		echo $e;
		throw new mysqli_sql_exception($e->getMessage(), $e->getCode());
	}
	unset($host, $dbname, $user, $pass, $charset, $port); // we don't need them anymore
?>