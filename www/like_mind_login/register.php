<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['login']) && isset($_POST['password'])) {
 
    // receiving the post params
    $login = $_POST['login'];
    $password = $_POST['password'];
 
    // check if user is already existed with the same login
    if ($db->isUserExisted($login)) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "User already existed with " . $login;
        echo json_encode($response);
    } else {
        // create a new user
        $user = $db->storeUser($login, $password);
        if ($user) {
            // user stored successfully
            $response["error"] = FALSE;
            $response["uid"] = $user["unique_id"];
            $response["user"]["login"] = $user["login"];
            $response["user"]["created_at"] = $user["created_at"];
            $response["user"]["updated_at"] = $user["updated_at"];
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (login or password) is missing!";
    echo json_encode($response);
}
?>