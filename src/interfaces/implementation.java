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
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class implementation extends UnicastRemoteObject implements interfacefile {

    private final String defaultPath = System.getProperty("user.dir") + "\\ServerStorage";

    public implementation() throws RemoteException {
        super();

    }

    @Override
    public File[] showfiles(String path) {
        File[] files = new File(path).listFiles(/*file -> file.isDirectory()*/);
        sortByFolders(files);
        return files;
    }

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
    public int createFile(String path) throws RemoteException {
        try {
            File f = new File(path);
            if (!f.exists()) {
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
    public int createFolder(String path) throws RemoteException {
        try {
            File f = new File(path);
            if (!f.exists()) {
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
    public boolean renameElement(String elementName, String newElementName) throws RemoteException {
        File ElementNameFile = new File(elementName);
        File newElementNameFile = new File(newElementName);
        boolean renamed = ElementNameFile.renameTo(newElementNameFile);
        if (renamed) {
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteElement(String path) throws RemoteException {
        File element = new File(path);
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
        return element.delete();
    }

    @Override
    public boolean openElement(String path) throws RemoteException {
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
}
