package interfaces;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface interfacefile extends Remote {

    File[] showfiles(String path) throws RemoteException;

    int createFile(String path) throws RemoteException;

    int createFolder(String path) throws RemoteException;

    boolean renameElement(String elementName, String newElementName) throws RemoteException;

    boolean deleteElement(String path) throws RemoteException;

    boolean openElement(String path) throws RemoteException;

    String readFile(String path) throws RemoteException;

    boolean saveFile(String path, String content) throws RemoteException;

    boolean checkSubDirectory(String path) throws RemoteException;

    boolean isDirectory(String path) throws RemoteException;

}
