<?php
$url = 'http://localhost/like_mind_login/register.php';
$params = array(
    'login' => 'login1', 
    'password' => 'password1',
);
$result = file_get_contents($url, false, stream_context_create(array(
    'http' => array(
        'method'  => 'POST',
        'header'  => 'Content-type: application/x-www-form-urlencoded',
        'content' => http_build_query($params)
    )
)));

echo $result;
?>