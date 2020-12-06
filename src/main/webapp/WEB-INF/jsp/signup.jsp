<%@ page isELIgnored="false" %>

<!DOCTYPE html>
<html>
    <head>
        <title>CIS3368 - Final Project - Sign Up - Lejing Huang</title>

        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- CSS Template Source: https://www.w3schools.com/bootstrap/bootstrap_ref_css_tables.asp -->
        <!--===============================================================================================-->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
        <!--===============================================================================================-->
    </head>

    <body>
        <div class="container" style="margin:120px 720px; padding:100px;">

            <h2 style="font-size:40px">Sign Up Page</h2><br>

            <form method="post" action="/signup">

                <!-- USERNAME -->
                <label for="usernameInput" style="font-size:16px">Username:</label><br>
                <input type="email" id="usernameInput" name="username" placeholder="Enter email as username" maxlength="254" style="width:250px" required /><br><br>

                <!-- PASSWORD -->
                <label for="passwordInput1" style="font-size:16px">Password:</label><br>
                <input type="password" id="passwordInput1" name="password1" placeholder="Enter password" maxlength="16" style="width:250px" required /><br><br>

                <!-- PASSWORD -->
                <label for="passwordInput2" style="font-size:16px">Repeat Password:</label><br>
                <input type="password" id="passwordInput2" name="password2" placeholder="Enter password again" maxlength="16" style="width:250px" required /><br><br>

                <!-- Admin -->
                <label style="font-size:16px">Sign up as Admin:</label><br>
                <input type="radio" id="admin" name="isAdmin" value="true" checked = "checked" />
                <label for="admin">Yes</label>
                <input type="radio" id="notAdmin" name="isAdmin" value="false" />
                <label for="notAdmin">No</label><br><br>

                <!-- SUBMIT -->
                <input type="submit" value="Sign Up" style="font-size:16px">

            </form><br>

            <p style="color:CornflowerBlue">${signupNotification}</p><br>

            <a href="/">Login With Your Account</a><br><br>

        </div>
    </body>
</html>