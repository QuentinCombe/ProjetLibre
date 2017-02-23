<?php
    try {
        // connection to the database.
        $pdo_options[PDO::ATTR_ERRMODE] = PDO::ERRMODE_EXCEPTION;
        $bdd = new PDO('mysql:host=localhost;dbname=projetlibre', 'root', '', $pdo_options);

        // Execute SQL request on the database.
        $sql = 'SELECT idClue, description, image FROM clue WHERE idClue=1;';
        $response = $bdd->query($sql);
        $output = $response->fetch(PDO::FETCH_ASSOC);
    } catch (Exception $e) {
        die('Erreur : ' . $e->getMessage());
    }
	
    // Print JSON encode of the array.
    echo(json_encode($output));
?>

