<?php
	$output = null;
	
    try 
	{
        $pdo_options[PDO::ATTR_ERRMODE] = PDO::ERRMODE_EXCEPTION;
        $bdd = new PDO('mysql:host=localhost;dbname=projetlibre', 'root', '', $pdo_options);

		if (isset($_POST['idUser']))
		{	
			$myId = (int) $_POST['idUser'];
			$sql = 'SELECT idObjective FROM AchievedObjectives WHERE idUser = ?';
			
			$stmt = $bdd->prepare($sql);
			$stmt->bindParam(1, $myId, PDO::PARAM_INT);
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

