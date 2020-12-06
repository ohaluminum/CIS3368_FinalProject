<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html>
    <head>
        <title>CIS3368 - Final Project - Login - Lejing Huang</title>

        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- CSS Template Source: https://www.w3schools.com/bootstrap/bootstrap_ref_css_tables.asp -->
        <!--===============================================================================================-->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <!--===============================================================================================-->
    </head>

    <body>
        <div class="container" style="margin:175px 720px; padding:100px;">

            <h2 style="font-size:40px">Log In Page</h2><br>

            <form method="post" action="/login">

                <!-- USERNAME -->
                <label for="usernameInput" style="font-size:16px">Username:</label><br>
                <input type="email" id="usernameInput" name="username" placeholder="Enter username" maxlength="254" style="width:250px" required><br><br>

                <!-- PASSWORD -->
                <label for="passwordInput" style="font-size:16px">Password:</label><br>
                <input type="password" id="passwordInput" name="password" placeholder="Enter password" maxlength="16" style="width:250px" required><br><br>

                <!-- SUBMIT -->
                <input type="submit" value="Login" style="font-size:16px">

            </form><br>

            <p style="color:CornflowerBlue">${loginNotification}</p><br>

            <a href="/sign">Create Your Account</a><br><br>

        </div>
    </body>
</html>
