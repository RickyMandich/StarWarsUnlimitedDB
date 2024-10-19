<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="logIn.css">
        <title>Log In</title>
    </head>
    <?php
        session_start();
        if (isset($_SESSION["user"])):
    ?>
    <meta http-equiv="refresh" content="0; ./profilo">
    <?php else:
        $resultClass = "success";
        $resultText = "test";
        if(isset($_GET["userID"])):
        endif;
        ?> 
        <body>
            <div class="container">
                <div class="form-container">
                    <h1>Log in</h1>
                    <form action="logIn">
                        <div class="form-group">
                            <input type="text" name="userID" placeholder="Email/Username" required>
                        </div>
                        <div class="form-group">
                            <input type="password" name="password" placeholder="Password" required>
                        </div>
                        <div class="form-group">
                            <span id="result" class="<?php echo $resultClass?>">
                                <?php echo $resultText ?>
                            </span>
                        </div>
                        <button type="submit" class="submit-btn">Log in</button>
                    </form>
                    <div class="alternate-action">
                        <span>or </span>
                        <a href="signIn">Sign up</a>
                    </div>
                </div>
            </div>
        </body>
    <?php endif; ?>
</html>