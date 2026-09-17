import java.util.*;

class Process {

    private int id;
    private int[] state;
    private Queue<Message> pendingQueue;
    private boolean recording;

    // Constructor
    Process(int id, int numProcesses) {
        this.id = id;
        this.state = new int[numProcesses];
        this.pendingQueue = new LinkedList<>();
        this.recording = false;
    }

    // Receive a message
    public void receive(Message message) {

        if (recording) {
            state[message.sender] = message.value;

            System.out.println(
                "Process " + id +
                " received message from Process " +
                message.sender +
                " with value " +
                message.value
            );

        } else {
            pendingQueue.add(message);

            System.out.println(
                "Process " + id +
                " queued message from Process " +
                message.sender
            );
        }
    }

    // Start recording
    public void startRecording() {
        recording = true;

        System.out.println(
            "Process " + id + " started recording."
        );
    }

    // Stop recording
    public void stopRecording() {
        recording = false;

        processPendingMessages();

        System.out.println(
            "Process " + id + " stopped recording."
        );
    }

    // Process pending messages
    public void processPendingMessages() {

        while (!pendingQueue.isEmpty()) {

            Message message = pendingQueue.poll();

            state[message.sender] = message.value;

            System.out.println(
                "Process " + id +
                " processed pending message from Process " +
                message.sender
            );
        }
    }

    // Print process state
    public void printState() {

        System.out.println(
            "State of Process " + id +
            ": " + Arrays.toString(state)
        );
    }
}


// Message class
class Message {

    int sender;
    int value;

    Message(int sender, int value) {
        this.sender = sender;
        this.value = value;
    }
}


// Main class
public class LamportSnapshotAlgorithm {

    public static void main(String[] args) {

        int numProcesses = 3;

        Process[] processes = new Process[numProcesses];

        // Create processes
        for (int i = 0; i < numProcesses; i++) {
            processes[i] = new Process(i, numProcesses);
        }

        System.out.println("===== Lamport Snapshot Algorithm =====");
        System.out.println();

        // Process 0 initiates snapshot
        processes[0].startRecording();

        // Process 0 receives a message from Process 1
        processes[0].receive(new Message(1, 5));

        // Process 1 starts recording
        processes[1].startRecording();

        // Process 1 receives a message from Process 0
        processes[1].receive(new Message(0, 2));

        // Process 1 stops recording
        processes[1].stopRecording();

        // Process 0 stops recording
        processes[0].stopRecording();

        System.out.println();
        System.out.println("===== Final State =====");

        // Print state of all processes
        for (Process process : processes) {
            process.printState();
        }
    }
}