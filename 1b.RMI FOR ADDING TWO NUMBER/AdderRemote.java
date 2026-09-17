import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class AdderRemote extends UnicastRemoteObject implements Adder {

    public AdderRemote() throws RemoteException {
        super();
    }

    @Override
    public int adder(int x, int y) throws RemoteException {
        return x + y;
    }

    public static void main(String[] args) {
        try {
            // Start RMI Registry on port 3000
            LocateRegistry.createRegistry(3000);

            // Create remote object
            Adder stub = new AdderRemote();

            // Register the remote object
            Naming.rebind(
                "rmi://localhost:3000/Adderservice",
                stub
            );

            System.out.println("RMI Registry started on port 3000");
            System.out.println("Adder Server is ready...");

        } catch (Exception e) {
            System.err.println("Server exception: " + e);
            e.printStackTrace();
        }
    }
}