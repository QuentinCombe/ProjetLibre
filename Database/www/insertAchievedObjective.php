<?php	
    try 
	{
        $pdo_options[PDO::ATTR_ERRMODE] = PDO::ERRMODE_EXCEPTION;
        $bdd = new PDO('mysql:host=localhost;dbname=projetlibre', 'root', '', $pdo_options);

		if (isset($_POST['idUser']) && isset($_POST['idObjective']))
		{	
			$myIdUser = (int) $_POST['idUser'];
			$myIdObjective = (int) $_POST['idObjective'];
			 
			$sql = 'INSERT INTO AchievedObjectives(idUser, idObjective) VALUES(?, ?)';
			
			$stmt = $bdd->prepare($sql);
			$stmt->bindParam(1, $myIdUser, PDO::PARAM_INT);
			$stmt->bindParam(2, $myIdObjective, PDO::PARAM_INT);
			$stmt->execute();
		}
    } 
	catch (Exception $e) 
	{
        die('Erreur : ' . $e->getMessage());
    }
?>

