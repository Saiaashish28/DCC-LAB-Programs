import java.rmi.Naming;

public class HelloClient {

    public static void main(String[] args) {
        try {
            Hello stub = (Hello) Naming.lookup(
                "rmi://localhost:5000/hello"
            );

            String response = stub.sayHello();

            System.out.println("Response: " + response);

        } catch (Exception e) {
            System.out.println("Client exception: " + e);
            e.printStackTrace();
        }
    }
}