import java.sql.*;

public class TestDB {
    public static void main(String[] args) throws Exception {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:pizzadb", "sa", "");
        
        // Criar schema e dados
        String schema = "CREATE TABLE IF NOT EXISTS teste (id INT PRIMARY KEY, nome VARCHAR(255))";
        conn.createStatement().execute(schema);
        
        String insert = "INSERT INTO teste VALUES (1, 'Pizza')";
        conn.createStatement().execute(insert);
        
        String query = "SELECT COUNT(*) FROM teste";
        ResultSet rs = conn.createStatement().executeQuery(query);
        
        if (rs.next()) {
            System.out.println("✅ Banco de dados funcionando!");
            System.out.println("Total de registros: " + rs.getInt(1));
        }
        
        conn.close();
    }
}
