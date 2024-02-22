package interfaces;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.swing.JOptionPane;

public class implementation extends UnicastRemoteObject implements interfacefile {

    private String defaultPath = System.getProperty("user.dir") + "\\ServerStorage";

    public implementation() throws RemoteException {
        super();

    }

    @Override
    public File[] showfiles(String path, int iduser) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fileman", "root", "");
        PreparedStatement req = con.prepareStatement("select id_folder from dossier where path_folder=?");
        req.setString(1, path);
        System.out.println("start");
        ResultSet rs = req.executeQuery();
        int id = 0;
        if (rs.next()) {
            id = rs.getInt("id_folder");
        }
        if (id == 0) {
            req = con.prepareStatement("insert into dossier(path_folder) values(?)");
            req.setString(1, path);
            req.executeUpdate();
            req = con.prepareStatement("select id_folder from dossier where path_folder=?");
            req.setString(1, path);
            rs = req.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id_folder");
            }
        }
    
        System.out.println("id : "+id);
        req = con.prepareStatement("select path_file from fichier where id_folder=? and id=? and `read` = 1", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        req.setInt(1, id);
        req.setInt(2, iduser);
        rs = req.executeQuery();

        // Count the number of files to initialize the array
        int count = 0;
        while (rs.next()) {
            count++;
            System.out.println(count);
        }
        rs.beforeFirst(); // Reset the cursor

        File[] files = new File[count]; // Initialize the array

        int i = 0;
        while (rs.next()) {
            System.out.println(rs.getString("path_file"));
            File f = new File(rs.getString("path_file"));
            files[i] = f;
            i++;
        }

        // Sort the files if needed
        sortByFolders(files);

        return files;
    }

    //  File[] files = new File(path).listFiles(/*file -> file.isDirectory()*/);
    private void sortByFolders(File[] files) {
        if (files != null) {
            java.util.Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File file1, File file2) {
                    if (file1.isDirectory() && !file2.isDirectory()) {
                        return -1;
                    } else if (!file1.isDirectory() && file2.isDirectory()) {
                        return 1;
                    } else {
                        return file1.getName().compareToIgnoreCase(file2.getName());
                    }
                }
            });
        }
    }

    @Override
    public int createFile(String path, String newpath, int iduser) throws RemoteException {
        try {
            File f = new File(newpath);
            if (!f.exists()) {
                System.out.println("path:"+path);
                System.out.println("filepath:"+newpath);
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fileman", "root", "");
                PreparedStatement req = con.prepareStatement("select id_folder from dossier where path_folder=?");
                req.setString(1, path);
                int id = 0;
                ResultSet rs = req.executeQuery();
                if (rs.next()) {
                    id = rs.getInt("id_folder");
                }
                System.out.println("id:"+id);
                if (id != 0) {
                    req = con.prepareStatement("INSERT INTO fichier values(?,?,?,?,?)");
                    req.setString(1, newpath);
                    req.setInt(2, id);
                    req.setInt(3, iduser);
                    req.setBoolean(4, true);
                    req.setBoolean(5, true);
                    req.executeUpdate();
                }
                if (f.createNewFile()) {

                    return 1;
                }
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 2;
    }

    @Override
    public int createFolder(String news,String path,int iduser) throws RemoteException {
        try {
            File f = new File(path);
            if (!f.exists()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fileman", "root", "");
                PreparedStatement  req = con.prepareStatement("INSERT INTO dossier(path_folder) values(?)");
                req.setString(1, path);
                req.executeUpdate();
            
              req = con.prepareStatement("select id_folder from dossier where path_folder=?");
                req.setString(1, news);
                int id = 0;
                ResultSet rs = req.executeQuery();
                if (rs.next()) {
                    id = rs.getInt("id_folder");
                }
                req = con.prepareStatement("INSERT INTO fichier values(?,?,?,?,?)");
                    req.setString(1, path);
                    req.setInt(2, id);
                    req.setInt(3, iduser);
                    req.setBoolean(4, true);
                    req.setBoolean(5, true);
                    req.executeUpdate();
                if (f.mkdir()) {
                    return 1;
                }
            } else {
                if (f.isDirectory()) {
                    return 3;
                } else {
                    return 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 2;
    }

    @Override
    public synchronized boolean renameElement(String elementName, String newElementName, int iduser) throws RemoteException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fileman", "root", "");
            PreparedStatement req = con.prepareStatement("select `write`,path_file from fichier where path_file=? and id=?");
            req.setString(1, elementName);
            req.setInt(2, iduser);
            ResultSet rs = req.executeQuery();
            int st = 0;
            if (rs.next()) {
                System.out.println(rs.getString("path_file"));
                st = rs.getInt("write");
                System.out.println(st + " haha");
            }
            if (st == 0) {
                System.out.println("lol");
                return false;
            }
            File ElementNameFile = new File(elementName);
            File newElementNameFile = new File(newElementName);
            boolean renamed = ElementNameFile.renameTo(newElementNameFile);
            if (renamed) {
                System.out.println("i'm being renamed");
                req = con.prepareStatement("update fichier set path_file=? where path_file=? and id=?");
                req.setString(1, newElementNameFile.getAbsolutePath());
                req.setString(2, ElementNameFile.getAbsolutePath());
                req.setInt(3, iduser);
                req.executeUpdate();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        File ElementNameFile = new File(elementName);
        File newElementNameFile = new File(newElementName);
        boolean renamed = ElementNameFile.renameTo(newElementNameFile);
        if (renamed) {
            return true;
        }*/
        return false;
    }

    @Override
    public synchronized boolean deleteElement(String path, int iduser, boolean statu, int c) throws RemoteException {
        /*File element = new File(path);
        if (element.isDirectory()) {
            File[] files = element.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        if (!deleteElement(file.getPath())) {
                            return false;
                        }
                    } else {
                        if (!file.delete()) {
                            return false;
                        }
                    }
                }
            }
        }
        return element.delete();*/

        try {
            if (statu == false) {
                System.out.println("badr");
                Class.forName("com.mysql.cj.jdbc.Driver");
                System.out.println("badr");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fileman", "root", "");
                PreparedStatement req = con.prepareStatement("select `write` from fichier where path_file=? and id=?");
                req.setString(1, path);
                req.setInt(2, iduser);
                ResultSet rs = req.executeQuery();
                int st = 0;
                if (rs.next()) {
                    st = rs.getInt("write");
                }
                if (st == 1) {
                    statu = true;
                }
            }
            if (statu == true) {
                File element = new File(path);
                if (element.isDirectory()) {
                    File[] files = element.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            if (file.isDirectory()) {
                                if (c == 0) {
                                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fileman", "root", "");
                                    PreparedStatement req = con.prepareStatement("Delete from dossier where path_folder= ?");
                                    req.setString(1, element.getAbsolutePath());
                                    req.executeUpdate();
                                    PreparedStatement reqSelectId = con.prepareStatement("SELECT id_folder AS id FROM dossier WHERE path_folder=?");
                                    reqSelectId.setString(1, element.getAbsolutePath());
                                    ResultSet rf = reqSelectId.executeQuery();
                                    int id = 0;
                                    if (rf.next()) {
                                        id = rf.getInt("id");
                                    }
                                    PreparedStatement reqs = con.prepareStatement("Delete from fichier where id_folder= ? and id=?");
                                    reqs.setInt(1, id);
                                    reqs.setInt(2, iduser);
                                    reqs.executeUpdate();
                                }
                                if (!deleteElement(file.getPath(), 1, true, 1)) {
                                    return false;
                                }
                            } else {
                                if (!file.delete()) {
                                    return false;
                                }
                            }
                        }
                    }
                }

                if (c == 0) {
                    System.out.println("hello");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fileman", "root", "");
                    PreparedStatement req = con.prepareStatement("Delete from fichier where path_file= ? and id= ?");
                    req.setString(1, element.getAbsolutePath());
                    req.setInt(2, iduser);
                    req.executeUpdate();
                }
                return element.delete();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean openElement(String path, int iduser) throws RemoteException {

        File folder = new File(path);
        if (folder.exists() && folder.isDirectory()) {
            System.out.println("exist");
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fileman", "root", ""); 
              PreparedStatement reqSelect = con.prepareStatement("SELECT * FROM dossier WHERE path_folder=?");
              PreparedStatement reqInsertDossier = con.prepareStatement("INSERT INTO dossier (path_folder) VALUES (?)");
             PreparedStatement reqSelectId = con.prepareStatement("SELECT id_folder AS id FROM dossier WHERE path_folder=?");
             PreparedStatement reqSelectFichier = con.prepareStatement("SELECT id_folder AS id FROM fichier WHERE id_folder=? AND path_file=?"); 
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

                File[] files = folder.listFiles();
                if (files != null) {
                    for (File f : files) {
                        reqSelectFichier.setInt(1, id);
                        reqSelectFichier.setString(2, f.getAbsolutePath());
                        ResultSet rsFichier = reqSelectFichier.executeQuery();
                        if (!rsFichier.next()) {
                            reqInsertFichier.setString(1, f.getAbsolutePath());
                            reqInsertFichier.setInt(2, id);
                            reqInsertFichier.setInt(3, iduser); // Assuming id value
                            reqInsertFichier.setBoolean(4, true); // Set "read" to true
                            reqInsertFichier.setBoolean(5, true); // Set "write" to true
                            reqInsertFichier.executeUpdate();
                            System.out.println(f.getPath() + " Readable: " + f.canRead() + " Writable: " + f.canWrite());
                        } else {
                            System.out.println("File " + f.getPath() + " already exists in the database");
                        }
                    }
                } else {
                    System.out.println("No files in this folder");
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Handle SQL Exception
            }
        }
        return new File(path).isDirectory();
    }

    @Override
    public String readFile(String path) throws RemoteException {
        File file = new File(path);
        String s = "";
        try {
            FileReader fileContent = new FileReader(file);
            int i = 0;
            while ((i = fileContent.read()) != -1) {
                s = s + (char) i;
            }
        } catch (Exception ex) {
            Logger.getLogger(implementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }

    @Override
    public boolean saveFile(String path, String content) throws RemoteException {
        try {
            FileWriter file = new FileWriter(path);
            file.write(content);
            file.close();
        } catch (IOException ex) {
            Logger.getLogger(implementation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean checkSubDirectory(String path) throws RemoteException {
        File file = new File(path);

        if (file.listFiles(subDirectory -> subDirectory.isDirectory()).length > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isDirectory(String path) throws RemoteException {
        File file = new File(path);
        if (file.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void ulpoad(String path, String fileName, byte[] pixels) throws RemoteException {
        //String destinationPath = System.getProperty("user.dir") + "\\ServerStorage" + path + "\\" + file.getName();
        //Path destination = Paths.get(destinationPath);
        File directory = new File(defaultPath + "\\" + path);
        try {
            // Copy the contents of the selected file to the destination path
            //Files.copy(file.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
            File outputFile = new File(directory, fileName);
            FileOutputStream out = new FileOutputStream(outputFile);
            byte[] pxl = pixels;
            out.write(pxl);
            out.close();
            System.out.println("File saved to: " + directory + "\\" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to save file: " + e.getMessage());
        }
    }

    @Override
    public void uploadFolder(String path, String fileName, byte[] pixels) throws RemoteException {
        try {
            File outputFile = new File(System.getProperty("java.io.tmpdir"), "tempFolderToUpload.zip");
            FileOutputStream out = new FileOutputStream(outputFile);
            byte[] pxl = pixels;
            out.write(pxl);
            out.close();
            Path extractPath = Paths.get(defaultPath + path);
            ZipInputStream zis = new ZipInputStream(new FileInputStream(outputFile));
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                Path newPath = extractPath.resolve(zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    Files.createDirectories(newPath);
                } else {
                    Files.createDirectories(newPath.getParent());
                    try (OutputStream os = Files.newOutputStream(newPath)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            os.write(buffer, 0, len);
                        }
                    }
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public byte[] download(String path) throws RemoteException {
        File file = new File(defaultPath + "\\" + path);
        //File fileToSend = null;
        byte[] pixels = null;
        if (file.isFile()) {
            try {
                FileInputStream in = new FileInputStream(file);
                pixels = new byte[in.available()];
                in.read(pixels);
            } catch (Exception ex) {
                Logger.getLogger(implementation.class.getName()).log(Level.SEVERE, null, ex);
            }
            return pixels;
        } else {
            Path selectedFolderPath = file.toPath();
            String folderName = file.getName();
            Path zipPath = Paths.get(System.getProperty("java.io.tmpdir") + "tempFolderToUpload.zip");
            try {
                ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()));
                Files.walkFileTree(selectedFolderPath, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        // Add the folder name as a prefix to the entry within the ZIP file
                        String entryName = selectedFolderPath.relativize(file).toString();
                        zos.putNextEntry(new ZipEntry(folderName + "/" + entryName));
                        Files.copy(file, zos);
                        zos.closeEntry();
                        return FileVisitResult.CONTINUE;
                    }
                });
                zos.close();
                FileInputStream in = new FileInputStream(new File(zipPath.toString()));
                pixels = new byte[in.available()];
                in.read(pixels);
                //fileToSend = new File(zipPath.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return pixels;
        }
    }

    @Override
    public int loginuser(String username, String password) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fileman", "root", "");
        PreparedStatement req = con.prepareStatement("SELECT * FROM utilisateur where username=? and password=?");
        System.out.println("infos : " + username + " " + password);
        req.setString(1, username);
        req.setString(2, password);
        ResultSet rs = req.executeQuery();
        int c = 0;
        int id = 0;
        if (rs.next()) {
            id = rs.getInt("id");
            c++;
        }
        if (c != 0) {
            return id;
        }
        return -1;

    }

    @Override
public void init(String path) throws Exception {
    defaultPath = path;
    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fileman", "root", "");
         PreparedStatement reqSelect = con.prepareStatement("SELECT * FROM dossier WHERE path_folder=?");
         PreparedStatement reqInsertDossier = con.prepareStatement("INSERT INTO dossier (path_folder) VALUES (?)");
         PreparedStatement reqSelectId = con.prepareStatement("SELECT id_folder AS id FROM dossier WHERE path_folder=?");
         PreparedStatement reqSelectFichier = con.prepareStatement("SELECT id_folder AS id FROM fichier WHERE id_folder=? AND path_file=?");
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
        System.out.println("path imp : " + folder.getAbsolutePath());
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    reqSelectFichier.setInt(1, id);
                    reqSelectFichier.setString(2, f.getAbsolutePath());
                    ResultSet rsFichier = reqSelectFichier.executeQuery();
                    if (!rsFichier.next()) {
                        reqInsertFichier.setString(1, f.getAbsolutePath());
                        reqInsertFichier.setInt(2, id);
                        reqInsertFichier.setInt(3, 1); // Assuming id value
                        reqInsertFichier.setBoolean(4, true); // Set "read" to false for first half
                        reqInsertFichier.setBoolean(5, true); // Set "write" to true for first half
                        reqInsertFichier.executeUpdate();
                        System.out.println(f.getPath() + " Readable: " + f.canRead() + " Writable: " + f.canWrite());
                    } else {
                        System.out.println("File " + f.getPath() + " already exists in the database");
                    }
                }
            } else {
                System.out.println("No files in this folder");
            }
        } else {
            System.out.println("Folder doesn't exist");
        }
    }
}

    @Override
    public String getpaths() throws Exception {
        return defaultPath;
    }

    @Override
    public List<String> getusers(int iduser) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        List<String> users = new ArrayList<String>();
        java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fileman", "root", "");
        PreparedStatement req = con.prepareStatement("select username from utilisateur where id != ?");
        req.setInt(1,iduser);
        ResultSet rs = req.executeQuery();
        while(rs.next()){
            users.add(rs.getString("username"));
        }
        return users;
    }

    @Override
    public void partagefile(String folderpath, String newpath,String username, int iduser) throws Exception {
           Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/fileman", "root", "");
                PreparedStatement req = con.prepareStatement("select id from utilisateur where username=?");
                req.setString(1,username);
                int id=0;
                ResultSet rs = req.executeQuery();
                if(rs.next()){
                    id = rs.getInt("id");
                }
                System.out.println(id);
                req = con.prepareStatement("select id_folder from dossier where path_folder=?");
                req.setString(1, folderpath);
                rs = req.executeQuery();
                int idv = 0;
                if(rs.next()){
                    idv = rs.getInt("id_folder");
                }
               PreparedStatement reqv = con.prepareStatement("insert into fichier values(?,?,?,?,?)");
               reqv.setString(1,newpath);
               reqv.setInt(2,idv);
               reqv.setInt(3,id);
               reqv.setBoolean(4, true); // Set "read" to false for first half
               reqv.setBoolean(5, true);
               reqv.executeUpdate();
               reqv = con.prepareStatement("select * from fichier where id_folder=?");
     
    }



 

}
