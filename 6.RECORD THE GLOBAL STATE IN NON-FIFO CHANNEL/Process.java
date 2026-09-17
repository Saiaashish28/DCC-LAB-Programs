import java.util.*;

class Process {

    private int processId;
    private int numProcesses;
    private int[] vectorClock;

    private Map<Integer, Queue<Message>> channels;
    private Map<String, String> localState;

    private Map<String, String> snapshot;
    private Set<Integer> receivedMarkers;

    public Process(int processId, int numProcesses) {
        this.processId = processId;
        this.numProcesses = numProcesses;
        this.vectorClock = new int[numProcesses];
        this.channels = new HashMap<>();
        this.localState = new HashMap<>();
        this.snapshot = new HashMap<>();
        this.receivedMarkers = new HashSet<>();

        // Initialize channels
        for (int i = 0; i < numProcesses; i++) {
            channels.put(i, new LinkedList<>());
        }
    }

    public void sendMessage(int destProcess, String message) {
        int[] timestamp = Arrays.copyOf(vectorClock, vectorClock.length);
        timestamp[processId]++;

        channels.get(destProcess).add(new Message(message, timestamp));
    }

    public void receiveMessages() {

        for (Map.Entry<Integer, Queue<Message>> entry : channels.entrySet()) {

            int srcProcess = entry.getKey();
            Queue<Message> channel = entry.getValue();

            for (Message msg : channel) {

                if (!msg.getMessage().equals("MARKER")) {

                    // Update local state with non-marker messages
                    vectorClock = Message.max(vectorClock, msg.getTimestamp());
                    localState.put(srcProcess + "-" + processId, msg.getMessage());

                } else {

                    // Handle marker messages
                    vectorClock = Message.max(vectorClock, msg.getTimestamp());
                    receivedMarkers.add(srcProcess);
                }
            }
        }
    }

    public void initiateSnapshot() {

        // Clear snapshot and receivedMarkers set
        snapshot.clear();
        receivedMarkers.clear();

        // Save local state
        snapshot.putAll(localState);

        // Send marker messages to all processes
        int[] markerTimestamp = Arrays.copyOf(vectorClock, vectorClock.length);
        markerTimestamp[processId]++;

        for (int destProcess = 0; destProcess < numProcesses; destProcess++) {
            channels.get(destProcess).add(
                new Message("MARKER", markerTimestamp)
            );
        }

        // Receive messages until a marker message is received from each process
        while (receivedMarkers.size() < numProcesses) {
            receiveMessages();
        }

        // Update the snapshot with the received non-marker messages
        for (Map.Entry<Integer, Queue<Message>> entry : channels.entrySet()) {

            int srcProcess = entry.getKey();
            Queue<Message> channel = entry.getValue();

            for (Message msg : channel) {

                if (!msg.getMessage().equals("MARKER")) {
                    snapshot.put(
                        srcProcess + "-" + processId,
                        msg.getMessage()
                    );
                }
            }
        }

        // Print the snapshot
        System.out.println(
            "Process " + processId + " Snapshot: " + snapshot
        );
    }

    public static void main(String[] args) {

        int numProcesses = 3;
        Process[] processes = new Process[numProcesses];

        for (int i = 0; i < numProcesses; i++) {
            processes[i] = new Process(i, numProcesses);
        }

        // Simulate some communication
        processes[0].sendMessage(1, "Hello");
        processes[2].sendMessage(0, "Hi");
        processes[1].sendMessage(2, "Hola");

        // Initiate snapshots
        for (Process process : processes) {
            process.initiateSnapshot();
        }
    }
}

class Message {

    private String message;
    private int[] timestamp;

    public Message(String message, int[] timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public int[] getTimestamp() {
        return timestamp;
    }

    public static int[] max(int[] arr1, int[] arr2) {

        int[] result = new int[arr1.length];

        for (int i = 0; i < arr1.length; i++) {
            result[i] = Math.max(arr1[i], arr2[i]);
        }

        return result;
    }
}