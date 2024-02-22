package interfaces;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface interfacefile extends Remote {
    
    String getpaths()throws Exception;
    
    void init(String path)throws Exception;
    
    int loginuser(String username,String password) throws Exception;
    
    List<String> getusers(int iduser) throws Exception;
     
    void partagefile(String folderpath,String newpath,String username,int iduser) throws Exception;
    
    int createFile(String path,String newpath,int iduser) throws RemoteException;
    
    File[] showfiles(String path, int iduser) throws Exception;
    
    int createFolder(String news,String path,int iduser) throws RemoteException;

    boolean renameElement(String elementName, String newElementName, int iduser) throws RemoteException;

    boolean deleteElement(String path, int iduser, boolean statusChecked, int c) throws RemoteException;

    boolean openElement(String path,int iduser) throws RemoteException;

    String readFile(String path) throws RemoteException;

    boolean saveFile(String path, String content) throws RemoteException;

    boolean checkSubDirectory(String path) throws RemoteException;

    boolean isDirectory(String path) throws RemoteException;
    
    void ulpoad(String path, String fileName, byte[] pixels) throws RemoteException;
    
    void uploadFolder(String path, String fileName, byte[] pixels) throws RemoteException;
    
    byte[] download(String path) throws RemoteException;

}
