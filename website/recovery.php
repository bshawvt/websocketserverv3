<?PHP 

	require("../apnetwork/dbconfig.php");
	// credits to https://stackoverflow.com/a/46181
	// ported to php by me
	function validateEmail($email) {
		$re = '/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/';
		return preg_match($re, strtolower($email));
	}

	function myrandom_bytes($len) {
		$r = "";
		$i = 0;
		while($i < $len) {
			$r .= rand(0, 255);
			$i++;
		}
		return $r;
	}

	$to = $_POST["email"];
	$token = base64_encode(myrandom_bytes(64));


	if (!validateEmail($to)) {
		$status = "Invalid email address";
	}
	else {
		
		$sql = new mysqli($sqlAddress, $sqlUsername, $sqlPassword, $sqlDatabase, $sqlPort);

		if (!$sql->connect_errno) {	

			$e = $sql->prepare("SELECT * FROM useraccounts WHERE email = ?;");
			if ($e) {
				$e->bind_param("s", $to);

				if ($e->execute()) {
					$sqlResult = $e->fetch();
					if ($sqlResult!=NULL) {
						$e->close();

						$ps = $sql->prepare("INSERT INTO useraccountrecovery VALUES(NULL, ?, ?, 1, NOW());");
		
						if ($ps) {
							
							$ps->bind_param("ss", $token, $to);
							

							if ($ps->execute()) {
								
								if (strlen($to) > 0) {
									$subject = "Password reset";
									
									$message = "<h2>A password reset has been requested.</h2>To continue click ";
									$message .= "<a href='http://server.anotherprophecy.com/reset.form.php?email=" . $to ."&token=" . $token . "'>here</a>.<br/>";
									$message .= "If you did not request this email, please ignore it. This password reset will become invalid after 2 hours.";

									$headers = "From: no-reply@anotherprophecy.com\r\n";
									$headers .= "Reply-To: no-reply@anotherprophecy.com\r\n";
									$headers .= "MIME-Version: 1.0\r\n";
									$headers .= "Content-Type: text/html; charset=UTF-8\r\n";

									mail($to, $subject, $message, $headers);

									$status = "Your reset email has been sent.";
								}

							}

							$ps->close();
						}

					}
					else {
						$status = "The email you have provided is incorrect.";
					}
				}

			}
		}

		$sql->close();

	}

?>
<!DOCTYPE html>
<html>
	
	<head>
		
		<style>
			body {
				background-color: rgb(200, 200, 200);
				margin: 10% 25% 0 25%;
				color: black;
			}
			.hidden {
				display: none;
			}
			.container {
				background-color: #fff;
				border: 1px solid #000;
				padding: 5px;
			}
			.nav-top {
				background-color: #f0f0f0;
				padding: 5px;
				border: 1px solid #000;
				border-bottom: 0px;

			}
			.nav-bottom {
				background-color: #f0f0f0;
				padding: 5px;
				border: 1px solid #000;
				border-top: 0px;
			}
			.login {
				float: right;
			}
			.clearfix {
				clear: both;
			}
			a {
				color: black;
			}
			a:link {
				color: black;
			}
			a:visited {
				color: black;
			}
			a:hover {
				color: black;
			}
			a:active {
				color: black;
			}
			

		</style>

		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

		<script type="text/javascript" src="/3rd/sjcl.js"></script>

		<script type="text/javascript">

			jQuery(document.body).ready(function() {

				/*jQuery("#passwordreset").click(function() {

					var email = document.getElementById("esemail").value;
					var f = "<form id='hidden-form' class='hidden' action='recovery.php' method='POST'>";
					f += 	"<input type='text'  name='email' value='" + email + "'/>";
					f += 	"</form>";
					jQuery('body').append(f);
					jQuery("#hidden-form").submit();

				});*/
				//function redirect() {
				//	document.location = "/index/";
				//}
				//setTimeout(redirect, 5000);

			});
		</script>

	</head>

	<body>
		<div class="nav-top">
			<span class="title">Password reset requested</span>
			<div class="clearfix"></div>
		</div>
		<div class="container">
			<span><?PHP echo $status; ?></span>
		</div>
		<div class="nav-bottom"></div>

	</body>

</html>