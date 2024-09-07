#### Codice per la connessione al database:
``` java
try (Connection conn = DriverManager.getConnection("jdbc:mysql://IP_SERVER:PORTA/DATABASE", "NOME_UTENTE", "PASSWORD")) {
    try (Statement stmt = conn.createStatement()) {
        try (ResultSet rs = stmt.executeQuery("`comando MySeQueL`)) {
            --- blocco di istruzioni
        }
    }
} catch (SQLException e) {
    throw new RuntimeException(e);
}
```