<?PHP
	
	//taken from to https://stackoverflow.com/a/46181
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

?>
<!DOCTYPE html>
<html>
	
	<head>
		
		<style>
			body {
				background-color: rgb(200, 200, 200);
				margin: 100px 15% 0 15%;
				color: #000;
			}

			.ts-heavy {
				text-shadow: 1px 0px 1px #000, -1px 0px 1px #000, 0px 1px 1px #000, 0px -1px 1px #000, 0px 0px 2px #000, -1px -1px 1px #000, 1px 1px 1px #000, 1px -1px 1px #000, -1px 1px 1px #000;
			}
			.ts-medium {
				text-shadow: -1px 1px 1px #000;
			}
			.ts-light {
				text-shadow: 0px 1px 1px #000;
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
			#more {
				padding: 5px;
				font-size: 10px;
				font-weight: 1000;
			}
			.nav-bottom {
				background-color: #f0f0f0;
				padding: 5px;
				border: 1px solid #000;
				border-top: 0px;
			}
			.bound-1 {
				margin: 0 auto;
				padding: 5px;
				width: 225px;
				float: right;
				background-color: #f0f0f0;
				color: #000;
			}
			.prettify {
				margin: 5px;
				border: 1px solid #000;

			}
			.padding-1 {
				padding: 5px;
				margin: 5px;
			}
			.login {
				float: right;
			}
			.title-inner {
				width: auto;
				background-color: #a0a0f0;
				color: #fff;
				height: 25px;
				text-align: center;
				padding:5px;
				text-shadow: 0px 1px 1px #000;

			}
			.create {
				
			}
			.reset {
				
			}
			#status {
				text-shadow: 0px 1px 1px #000;
			}
			.status-online {
				text-align: center;
				color: #fff;
				background-color: #9ddf9d;
			}
			.status-offline {
				text-align: center;
				color: #fff;
				background-color: #df9d9d;
			}
			.status-error {
				text-align: center;
				color: #f00;
				background-color: #dfdf9d;
			}
			.clearfix {
				clear: both;
			}
			.display-right {
				float: right;
			}
			.display-left {
				float: left;
			}
			a {
				color: #e74444;
				text-decoration: none;
			}
			a:link {
				color: #e74444;
			}
			a:visited {
				color: #e74444;
			}
			a:hover {
				color: #ff3030;
			}
			a:active {
				color: #b06d6d;
			}
			

		</style>

		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>

		<script type="text/javascript" src="/src/3rd/sjcl.js"></script>

		<script type="text/javascript">

			// credits to https://stackoverflow.com/a/46181
			function validateEmail(email) {
				var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
				return re.test(String(email).toLowerCase());
			}

			jQuery(document.body).ready(function() {
				jQuery.post({
					url: "/serverstatus/",
					dataType: "json",
					success: function(e) {
						console.log(e);
						if (e.result == 0) {
							jQuery("#status").addClass("status-online");
						}
						else if (e.result == 1) {
							jQuery("#status").addClass("status-offline");
						}
						else {
							jQuery("#status").addClass("status-error");
						}

						jQuery("#status").html(e.message);
					}
				});
				jQuery("#more").click(function() {
					jQuery.post({
						url: "/documents/accountcreation.html",
						dataType: "html",
						success: function(e) {
							jQuery("#dump").html(e);
						}
					});
				});
				jQuery("#about").click(function() {
					jQuery.post({
						url: "/documents/about.html",
						dataType: "html",
						success: function(e) {
							jQuery("#dump").html(e);
						}
					});
				});
				jQuery("#new-submit").click(function() {

					var username = document.getElementById("username").value;
					var password = document.getElementById("password1").value;
					var password2 = document.getElementById("password2").value;

					if (password !== password2 || password.length < 9) {
						alert("Password requirements failed.");
						return;
					}
					var email = document.getElementById("email").value;
					if (!validateEmail(email)) {
						alert("Invalid email address.");
						return;
					}

					var crypt = new sjcl.hash.sha256();
					crypt.update(password);
					password = sjcl.codec.base64.fromBits(crypt.finalize());

					var f = "<form id='hidden-form' class='hidden' action='/create/' method='POST'>";
					f += 	"<input type='text'  name='username' value='" + username + "'/>";
					f += 	"<input type='password'  name='password' value='" + password + "'/>";
					f += 	"<input type='text'  name='email' value='" + email + "'/>";
					f += 	"</form>";
					jQuery('body').append(f);
					jQuery("#hidden-form").submit();

				});

			});
		</script>

	</head>

	<body>
		<div class="nav-top">
			<span class="title">Hello world</span>
			<span class="login"><a href="#" id="about" class="ts-light">About</a> | <a class="ts-light" href="/play/">Play</a></span>
			<div class="clearfix"></div>
		</div>
		<div class="container">
			<div class="bound-1">
				<div class="status prettify ">
					<div class="title-inner">Server status</div>
					<div class="padding-1" id="status">
						Loading...
					</div>
				</div>
				<div class="reset prettify">
					<div class="title-inner">Account recovery</div>
					<div class="padding-1">
						<form action="/recovery/" method="POST">
							<label>Recovery email:</label><br/>
							<input type="text" id="reset-email" name="email" placeholder="Email address"/><br/>
							<br/>
							<button id="reset-submit" type="submit">Send email</button>
						</form>
					</div>
				</div>
				<div class="create prettify">
					<div class="title-inner">Account Creation</div>
					<div class="padding-1">
						<label>Your username:</label><br/>
						<input type="text" id="username" placeholder="Username"/><br/>
						<br/>
						<label>Your password:</label><br/>
						<input type="password" id="password1" placeholder="Password"/><br/>
						<br/>
						<label>Confirm password:</label><br/>
						<input type="password" id="password2" placeholder="Confirm password"/><br/>
						<br/>
						<label>Your recovery email:</label><br/>
						<input type="text" id="email" placeholder="Email address"/><br/>
						<br/>
						<button id="new-submit">Create account</button>
						<span><a id="more" href="#">Read more...</a></span>
					</div>
				</div>
			</div>

			<div id="dump">
				<p>Please be gentle with my software, this is a pet project and I have no idea what I am doing.</p>
				<br/>
				<span>The servers are not always online, you can yell at me through twitter <a href="https://twitter.com/dying2death">@dying2death</a> if it's not.</span>
				<br/>
				<span>The status box is updated every 5 minutes.</span>
			</div>
			<div class="clearfix"></div>
		</div>
		<div class="nav-bottom"></div>

	</body>

</html>