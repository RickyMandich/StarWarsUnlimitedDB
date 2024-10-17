<!DOCTYPE html>
<html lang="it">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Carte</title>
    </head>
    <?php
        $conn = new mysqli("localhost","root","Minecraft35?", "starwarsunlimited", 3306);
        if ($conn->connect_error) {
            die("Connection failed: " . $conn->connect_error);
        }
        echo "Connected successfully";
        $resultSet = $conn->query("select * from carte");
        $rs = [];
        if( $resultSet->num_rows > 0) {
            $primo = true;
            while($row = $resultSet->fetch_assoc()){
                $line = [];
                if($primo){
                    $primo = false;
                    foreach($row as $key => $value){
                        array_push($line, $key);
                    }
                    $rs[0] = $line;
                    $line = [];
                }
                foreach($row as $key => $value) array_push($line, $value);
                array_push($rs, $line);
            }
        }
        $conn->close();
    ?>
    <body>
        <table border="">
            <thead>
                <tr>
                    <?php foreach($rs[0] as $column): ?>
                        <td>
                            <?php echo $column; ?>
                        </td>
                    <?php endforeach; ?>
                </tr>
            </thead>
            <tbody>
                <?php for($i = 1;$i < count($rs); $i++): ?>
                    <tr>
                        <?php foreach($rs[$i] as $value): ?>
                            <td>
                                <?php echo $value; ?>
                            </td>
                        <?php endforeach; ?>
                    </tr>
                <?php endfor; ?>
            </tbody>
        </table>
    </body>
</html>