import java.util.*;

class SuzukiKasami {
    static int n = 4;
    // Request sequence number of each process
    static int[] RN = new int[n];
    // Token information
    static int[] LN = new int[n];
    // Queue of requesting processes
    static Queue<Integer> queue = new LinkedList<>();
    static int tokenHolder = 0;

    static void requestCriticalSection(int process) {
        RN[process]++;
        System.out.println("\nProcess " + process +
                " broadcasts REQUEST with sequence number " + RN[process]);
        // Add process to request queue
        if (!queue.contains(process)) {
            queue.add(process);
        }
        passToken();
    }

    static void passToken() {
        if (queue.isEmpty()) {
            return;
        }
        int nextProcess = queue.poll();
        System.out.println("Token passed from Process " +
                tokenHolder + " to Process " + nextProcess);
        tokenHolder = nextProcess;
        enterCriticalSection(nextProcess);
    }

    static void enterCriticalSection(int process) {
        System.out.println("Process " + process +
                " enters the Critical Section.");
        System.out.println("Process " + process +
                " performs the required operation.");
        // Update token sequence number
        LN[process] = RN[process];
        System.out.println("Process " + process +
                " exits the Critical Section.");
        passToken();
    }

    public static void main(String[] args) {
        System.out.println("Suzuki-Kasami Broadcast Algorithm");
        System.out.println("----------------------------------");
        System.out.println("Number of Processes: " + n);
        System.out.println("Initial Token Holder: Process 0");
        // Simulate requests from different processes
        requestCriticalSection(2);
        requestCriticalSection(1);
        requestCriticalSection(3);
    }
}