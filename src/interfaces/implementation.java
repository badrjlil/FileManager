package interfaces;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class implementation extends UnicastRemoteObject implements interfacefile {

    public implementation() throws RemoteException {
        super();

    }

    @Override
    public File[] showfiles(String path) {
        File[] files = new File(path).listFiles(file -> file.isDirectory());
        return files;
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
    public File readFile(String path) throws RemoteException {
        File file = new File(path);
        return file;
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
}
