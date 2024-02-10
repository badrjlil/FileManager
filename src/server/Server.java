package server;

import interfaces.implementation;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {

    public static void main(String[] args) throws Exception {
        implementation fileOperations = new implementation();

        // Create the RMI registry on port 1099
        LocateRegistry.createRegistry(1099);

        // Bind the server object to the registry with a specific name
        Naming.rebind("rmi://localhost:1099/frameoperations", fileOperations);
        System.out.println("server pret");
    }

}
