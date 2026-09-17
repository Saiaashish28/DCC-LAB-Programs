import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class HelloServer extends UnicastRemoteObject implements Hello {

    public HelloServer() throws RemoteException {
        super();
    }

    @Override
    public String sayHello() throws RemoteException {
        return "Hello, world!";
    }

    public static void main(String[] args) {
        try {
            // Create RMI Registry on port 5000
            LocateRegistry.createRegistry(5000);

            // Create server object
            HelloServer server = new HelloServer();

            // Bind the server
            Naming.rebind("rmi://localhost:5000/hello", server);

            System.out.println("Server ready...");
        } catch (Exception e) {
            System.out.println("Server exception: " + e);
            e.printStackTrace();
        }
    }
}