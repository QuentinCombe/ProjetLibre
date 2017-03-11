<?php
	$output = null;
	
    try 
	{
        $pdo_options[PDO::ATTR_ERRMODE] = PDO::ERRMODE_EXCEPTION;
        $bdd = new PDO('mysql:host=localhost;dbname=projetlibre', 'root', '', $pdo_options);

		if (isset($_POST['mail']) && isset($_POST['password']))
		{	
			$myMail = (string) $_POST['mail'];
			$myPassword = (string) $_POST['password'];
			 
			$sql = 'SELECT * FROM user WHERE mail = ? and password = ?';
			
			$stmt = $bdd->prepare($sql);
			$stmt->bindParam(1, $myMail, PDO::PARAM_STR);
			$stmt->bindParam(2, $myPassword, PDO::PARAM_STR);
			$stmt->execute();
			
			$output = $stmt->fetch();
			
			$stmt->closeCursor();
		}
    } 
	catch (Exception $e) 
	{
        die('Erreur : ' . $e->getMessage());
    }
	
    echo(json_encode($output));
?>

