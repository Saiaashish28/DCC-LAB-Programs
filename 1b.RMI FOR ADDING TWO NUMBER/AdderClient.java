import java.rmi.Naming;

public class AdderClient {

    public static void main(String[] args) {
        try {
            Adder stub = (Adder) Naming.lookup(
                "rmi://localhost:3000/Adderservice"
            );

            int result = stub.adder(34, 4);

            System.out.println("Addition = " + result);

        } catch (Exception e) {
            System.out.println("Client exception: " + e);
            e.printStackTrace();
        }
    }
}