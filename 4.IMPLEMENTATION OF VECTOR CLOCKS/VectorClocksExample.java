import java.util.Arrays;

public class VectorClocksExample {

    public static void main(String[] args) {

        // Create and initialize vector clocks for 3 processes
        int numberOfProcesses = 3;

        VectorClock[] clocks = new VectorClock[numberOfProcesses];

        for (int i = 0; i < numberOfProcesses; i++) {
            clocks[i] = new VectorClock(numberOfProcesses, i);
        }

        // Simulate message sending and receiving
        sendMessage(clocks[0], clocks[1]); // Process 0 -> Process 1
        sendMessage(clocks[1], clocks[2]); // Process 1 -> Process 2
        sendMessage(clocks[2], clocks[0]); // Process 2 -> Process 0

        // Print final state of vector clocks
        System.out.println("Final Vector Clocks:");

        for (int i = 0; i < numberOfProcesses; i++) {
            System.out.println("Clock of Process " + i + ": " + clocks[i]);
        }
    }

    // Method to simulate sending and receiving a message
    private static void sendMessage(
            VectorClock sender,
            VectorClock receiver) {

        // Increment sender's clock before sending
        sender.increment();

        System.out.println("After sending message:");
        System.out.println("Sender Clock: " + sender);

        // Create a copy of sender's clock as the message timestamp
        VectorClock messageClock = new VectorClock(sender);

        // Increment receiver's clock before receiving
        receiver.increment();

        // Update receiver's clock using message clock
        receiver.receive(messageClock);

        System.out.println("After receiving message:");
        System.out.println("Receiver Clock: " + receiver);
        System.out.println();
    }

    // Vector Clock class
    static class VectorClock {

        private int[] clock;
        private int processId;

        // Constructor
        public VectorClock(int size, int processId) {
            this.clock = new int[size];
            this.processId = processId;
        }

        // Copy constructor
        public VectorClock(VectorClock other) {
            this.clock = Arrays.copyOf(
                    other.clock,
                    other.clock.length
            );

            this.processId = other.processId;
        }

        // Increment the process's own clock
        public void increment() {
            clock[processId]++;
        }

        // Receive and merge another vector clock
        public void receive(VectorClock messageClock) {

            for (int i = 0; i < clock.length; i++) {
                clock[i] = Math.max(
                        clock[i],
                        messageClock.clock[i]
                );
            }
        }

        // Display vector clock
        @Override
        public String toString() {
            return Arrays.toString(clock);
        }
    }
}