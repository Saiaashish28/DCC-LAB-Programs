class NumberPrinter extends Thread {
    public void run() {
        for (int i = 1; i <=10 ; i++) { 
            System.out.println(i);
            try {
                Thread.sleep(1000); // Pausing for 1 second
            } catch (InterruptedException e) {
                 System.out.println(e);
            }
        }
    }
}
 
public class ProcSync {
    public static void main(String args[]) {
        NumberPrinter t1 = new NumberPrinter();
        // Creating the thread 
        t1.start(); 
        // // Starting the thread
    }
}
