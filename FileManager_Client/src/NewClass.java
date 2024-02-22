import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NewClass {
    public static void processFolder(String path) throws Exception {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fileman", "root", "");
             PreparedStatement reqSelect = con.prepareStatement("SELECT * FROM dossier WHERE path_folder=?");
             PreparedStatement reqInsertDossier = con.prepareStatement("INSERT INTO dossier (path_folder) VALUES (?)");
             PreparedStatement reqSelectId = con.prepareStatement("SELECT id_folder AS id FROM dossier WHERE path_folder=?");
             PreparedStatement reqSelectFichier = con.prepareStatement("SELECT id_folder AS id FROM fichier WHERE id_folder=?");
             PreparedStatement reqInsertFichier = con.prepareStatement("INSERT INTO fichier (path_file, id_folder, id, `read`, `write`) VALUES (?, ?, ?, ?, ?)")) {

            reqSelect.setString(1, path);
            ResultSet rs = reqSelect.executeQuery();
            if (!rs.next()) {
                reqInsertDossier.setString(1, path);
                reqInsertDossier.executeUpdate();
            }

            reqSelectId.setString(1, path);
            rs = reqSelectId.executeQuery();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt("id");
            }

            File folder = new File(path);
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles();
                if (files != null) {
                    int half = files.length / 2;
                    for (int i = 0; i < files.length; i++) {
                        File f = files[i];
                        reqInsertFichier.setString(1, f.getAbsolutePath());
                        reqInsertFichier.setInt(2, id);
                        reqInsertFichier.setInt(3, 1); // Assuming id value
                      
                            reqInsertFichier.setBoolean(4, true); // Set "read" to false for first half
                            reqInsertFichier.setBoolean(5, true);  // Set "write" to true for first half
                        
                            reqInsertFichier.setBoolean(4, true);  // Set "read" to true for second half
                            reqInsertFichier.setBoolean(5, true); // Set "write" to false for second half
                        
                        reqInsertFichier.executeUpdate();
                        System.out.println(f.getPath() + " Readable: " + f.canRead() + " Writable: " + f.canWrite());
                    }
                } else {
                    System.out.println("No files in this folder");
                }
            } else {
                System.out.println("Folder doesn't exist");
            }
        }
    }

    public static void main(String[] args) throws Exception {
   String path = System.getProperty("user.dir") + "\\ServerStorage"; // Example path
System.out.println(path);
        processFolder(path);
    }
}
