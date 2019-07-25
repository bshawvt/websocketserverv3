<?PHP
	/*
		this page is single operation only.
	*/

	require("../apnetwork/dbconfig.php");

	function myrandom_bytes($len) {
		$r = "";
		$i = 0;
		while($i < $len) {
			$r .= rand(0, 255);
			$i++;
		}
		return $r;
	}

	$sql = new mysqli($sqlAddress, $sqlUsername, $sqlPassword, $sqlDatabase, $sqlPort);

	$sqlResult 		= null;

	$status		 	= "You are not logged in.";
	$authenticated	= 0; // 0 = no auth, 1 = logged in

	// username and password should only ever be posted once
	$pUsername = $_POST["user"];
	$pPassword = $_POST["pass"];

	//echo $pUsername . "</br>" . $pPassword;



	session_start();


	// logout
	if ($_POST["end_session"] == 1) {
		session_destroy();
		unset($_SESSION["administration_id"]);
		unset($_POST["end_session"]);
		unset($_POST["user"]);
		unset($_POST["pass"]);

	}

	// login
	if (strlen($pUsername) > 3 && strlen($pPassword) > 9) {
		
		//session_destroy(); // the session must be recreated from here
		unset($_SESSION["administration_id"]);
		unset($_POST["user"]);
		unset($_POST["pass"]);

		if (!$sql->connect_errno) {	
			
			$ps = $sql->prepare("SELECT * FROM useraccounts WHERE username = ?");
		
			if ($ps) {
				
				$ps->bind_param("s", $pUsername);
				

				if ($ps->execute()) {
					
					$sqlResult = $ps->get_result();
					if ($sqlResult!=FALSE) {
					
						$fetch = $sqlResult->fetch_array();
						$tSalt = $fetch["salt"];
						//echo $tSalt;
						$tHash = $fetch["hash"];

						if (hash("sha256", $pPassword . $tSalt, true) === $tHash) {
							if ($fetch["permission"] == 1) { // check if user is admin
								$status = "Login successful.";
								$_SESSION['administration_id'] = $pUsername . "|" . myrandom_bytes(8);
							} else {
								$status = "Yo, dis area ain't for u!";
							}
						}
						else {
							$status = "Login failed.";
						}

					}

				} 
				else {
					$status = "prepared statement error: " . $sql->errno;
				}

				$ps->close();
			}
			else {
				$status = "prepared statement error: " . $sql->errno;
			}
		}

	}


	// tools go here. fuk, i hate php
	if (isset($_SESSION['administration_id'])) {
		$status = "Session is active";

		$userAccounts = null;

		if ($_POST["edit_useraccount"] == 1) {

			$editUsername = $_POST["edit_username"];
			$editEmail = $_POST["edit_email"];
			$editPermission = $_POST["edit_permission"];
			$editLocked = $_POST["edit_locked"];

			unset($_POST["edit_useraccount"]);

			$ps = $sql->prepare("UPDATE useraccounts SET username = ?, email = ?, permission = ?, locked = ? WHERE username = ?");
			if ($ps) {

				$ps->bind_param("ssiis", $editUsername, $editEmail, $editPermission, $editLocked, $editUsername);

				if ($ps->execute()) {
					$status = "Overwrote account data";
				}
			}

		}

		if (isset($_POST["useraccount"])) {

			$selectedUserAccount = $_POST["useraccount"];

		}

		$ps = $sql->prepare("SELECT id, lastLoginDate, username, email, dateOfCreation, permission, locked FROM useraccounts");
		/* 
			0: id
			1: lastLoginDate
			2: username
			3: email
			4: dateOfCreation
			5: permission
			6: locked
		*/

		if ($ps) {

			$ps->bind_param("s", $pUsername);

			if ($ps->execute()) {

				$sqlResult = $ps->get_result();

				if($sqlResult!=FALSE) {
					$userAccounts = $sqlResult->fetch_all();
				}
				//$tSalt = $userAccounts["id"];
			}

			$ps->close();
		}

	}

	// done..
	$sql->close();

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

				jQuery("#passwordreset").click(function() {

					var email = document.getElementById("esemail").value;
					var f = "<form id='hidden-form' class='hidden' action='/recovery/' method='POST'>";
					f += 	"<input type='text'  name='email' value='" + email + "'/>";
					f += 	"</form>";
					jQuery('body').append(f);
					jQuery("#hidden-form").submit();

				});

				jQuery("#login").click(function() {
					
					var user = document.getElementById("user").value;
					var pass = document.getElementById("pass").value;

					if (user.length == 0 || pass.length == 0) return;

					var crypt = new sjcl.hash.sha256();
					crypt.update(pass);
					pass = sjcl.codec.base64.fromBits(crypt.finalize());

					var f = "<form id='hidden-form' class='hidden' action='/admin/' method='POST'>";
					f += 	"<input type='text'  name='user' value='" + user + "'/>";
					f += 	"<input type='password'  name='pass' value='" + pass + "'/>";
					f += 	"</form>";
					jQuery('body').append(f);
					jQuery("#hidden-form").submit();

				});
				jQuery("#logout").click(function() {
					var f = "<form id='hidden-form' class='hidden' action='/admin/' method='POST'>";
					f += 	"<input type='text'  name='end_session' value='" + 1 + "'/>";
					f += 	"</form>";
					jQuery('body').append(f);
					jQuery("#hidden-form").submit();

				});

				jQuery("#useraccounts").change(function() {

					var user = document.getElementById("useraccounts").value;

					if (user.length == 0) return;

					var f = "<form id='hidden-form' class='hidden' action='/admin/' method='POST'>";
					f += 	"<input type='text'  name='useraccount' value='" + user + "'/>";
					f += 	"</form>";
					jQuery('body').append(f);
					jQuery("#hidden-form").submit();

				});

				jQuery("#editsave").click(function() {

					var user = document.getElementById("esusername").value;
					var email = document.getElementById("esemail").value;
					var permission = document.getElementById("espermission").value;
					var locked = (document.getElementById("eslocked").checked ? 1 : 0);

					var f = "<form id='hidden-form' class='hidden' action='/admin/' method='POST'>";
					f += 	"<input type='text'  name='edit_useraccount' value='" + 1 + "'/>";
					f += 	"<input type='text'  name='edit_username' value='" + user + "'/>";
					f += 	"<input type='text'  name='edit_email' value='" + email + "'/>";
					f += 	"<input type='text'  name='edit_permission' value='" + permission + "'/>";
					f += 	"<input type='text'  name='edit_locked' value='" + locked + "'/>";

					f += 	"<input type='text'  name='useraccount' value='" + user + "'/>"; // for keeping option selector place
					
					f += 	"</form>";
					jQuery('body').append(f);
					jQuery("#hidden-form").submit();

				});
			});

			var t = function() {
				console.log("test");
			}
		</script>

	</head>

	<body>
		<div class="nav-top">
			<span class="title"><?PHP echo $status; ?></span>

			<?PHP
				$loginHtml = "";
				
				$loginHtml .= '<div class="login">';
				if (!isset($_SESSION['administration_id'])) {

					//$loginHtml .= '<div class="login">';
					$loginHtml .= '<input id="user" type="text" placeholder="Username" />';
					$loginHtml .= '<input id="pass" type="password" placeholder="Password"/>';
					$loginHtml .= '<button id="login">Login</button>';
					//$loginHtml .= '</div>';

				} else {

					$loginHtml .= '<button id="logout">End session</button>';
				}

				$loginHtml .= '</div>';
				echo $loginHtml;
			?>
			<div class="clearfix"></div>
		</div>
		<div class="container">
			<?PHP

			$containerHtml = "";

			if (isset($_SESSION['administration_id'])) {


				//var_dump($userAccounts);

				$containerHtml .= '<ul>';
					$containerHtml .= '<li>';
						$containerHtml .= '<label>edit account: </label>';
						$containerHtml .= '<select id="useraccounts">';
							$containerHtml .= '<option value="">Select a user</option>';
						foreach($userAccounts as $acc) {
							if ($acc[2] === $selectedUserAccount) {
								$selected = $acc;
							}
							$containerHtml .= '<option ' . ($selected[2] == $acc[2] ? 'selected' : '') . ' value="' . $acc[2] . '">' . $acc[2] . '</option>';
						}
						$containerHtml .= '</select>';
						$containerHtml .= '<ul>';
							$containerHtml .= '<li><label>id: </label><input disabled value="' . $selected[0] . '"/></li>';
							$containerHtml .= '<li><label>lastLoginDate: </label><input disabled value="' . $selected[1] . '"/></li>';
							$containerHtml .= '<li><label>username: </label><input id="esusername" value="' . $selected[2] . '"/></li>';
							$containerHtml .= '<li><label>email: </label><input id="esemail" value="' . $selected[3] . '"/></li>';
							$containerHtml .= '<li><label>dateOfCreation: </label><input disabled value="' . $selected[4] . '"/></li>';
							$containerHtml .= '<li><label>permission: </label><input id="espermission" value="' . $selected[5] . '"/></li>';
							$containerHtml .= '<li><label>locked: </label><input id="eslocked" type="checkbox"' . ($selected[6]==true?'checked':'') . '/></li>';
							$containerHtml .= '<li><button id="passwordreset">reset password</button></li>';
						$containerHtml .= '</ul>';
						$containerHtml .= '<button id="editsave">Save Account</button>';
					$containerHtml .= '</li>';
					$containerHtml .= '<li>tools</li>';
				$containerHtml .= '</ul>';

				}

				else {
					$containerHtml .= '<p><h1>You must login</h1> to access this page.</p>';
				}

				echo $containerHtml;
			?>
		</div>
		<div class="nav-bottom"><?PHP echo $_SESSION['administration_id'];?></div>

	</body>

</html>