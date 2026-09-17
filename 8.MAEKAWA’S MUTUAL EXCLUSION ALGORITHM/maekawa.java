import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

enum MessageType {
    REQUEST, REPLY, RELEASE
}

class Message {
    public MessageType type;
    public Process sender;

    public Message(MessageType type, Process sender) {
        this.type = type;
        this.sender = sender;
    }
}

class Process extends Thread {

    private final int id;
    private final Set<Process> quorum;

    private boolean inCriticalSection = false;

    private Queue<Message> messageQueue =
            new ConcurrentLinkedQueue<>();

    // Replies received from quorum members
    private Set<Process> granted = new HashSet<>();

    // Request waiting for this process's vote
    private Queue<Process> requestQueue =
            new LinkedList<>();

    // Process to which this process has currently given its vote
    private Process votedFor = null;

    public Process(int id, Set<Process> quorum) {
        this.id = id;
        this.quorum = quorum;
    }

    public synchronized void receiveMessage(Message msg) {
        messageQueue.add(msg);
        notifyAll();
    }

    public void run() {

        try {

            // Requesting critical section
            enterCriticalSection();

            // Simulate critical section work
            Thread.sleep(1000);

            // Exiting critical section
            exitCriticalSection();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void enterCriticalSection() throws InterruptedException {

        synchronized (this) {
            granted.clear();
        }

        // Send REQUEST to all members of quorum
        for (Process p : quorum) {
            p.receiveMessage(
                    new Message(MessageType.REQUEST, this)
            );
        }

        synchronized (this) {

            // Wait until replies from all quorum members are received
            while (granted.size() < quorum.size()) {
                wait();
            }

            inCriticalSection = true;

            System.out.println(
                    "Process " + id + " is in critical section"
            );
        }
    }

    private void exitCriticalSection() {

        synchronized (this) {

            inCriticalSection = false;

            // Send RELEASE to all quorum members
            for (Process p : quorum) {
                p.receiveMessage(
                        new Message(MessageType.RELEASE, this)
                );
            }

            granted.clear();
        }

        System.out.println(
                "Process " + id + " has exited critical section"
        );
    }

    public void processMessages() {

        synchronized (this) {

            while (!messageQueue.isEmpty()) {

                Message msg = messageQueue.poll();

                switch (msg.type) {

                    case REQUEST:

                        // If this process has not given its vote,
                        // give the vote to the requester.
                        if (votedFor == null) {

                            votedFor = msg.sender;

                            msg.sender.receiveMessage(
                                    new Message(
                                            MessageType.REPLY,
                                            this
                                    )
                            );

                        } else {

                            // Otherwise put the request in queue
                            if (!requestQueue.contains(msg.sender)) {
                                requestQueue.add(msg.sender);
                            }
                        }

                        break;

                    case REPLY:

                        // Record the reply received from quorum member
                        granted.add(msg.sender);

                        notifyAll();

                        break;

                    case RELEASE:

                        // The process that had the vote has released it
                        if (votedFor == msg.sender) {

                            votedFor = null;

                            // Give vote to next waiting requester
                            if (!requestQueue.isEmpty()) {

                                Process next = requestQueue.poll();

                                votedFor = next;

                                next.receiveMessage(
                                        new Message(
                                                MessageType.REPLY,
                                                this
                                        )
                                );
                            }
                        }

                        break;
                }
            }
        }
    }
}

public class maekawa {

    public static void main(String[] args) {

        Set<Process> quorum1 = new HashSet<>();
        Set<Process> quorum2 = new HashSet<>();
        Set<Process> quorum3 = new HashSet<>();

        Process p1 = new Process(1, quorum1);
        Process p2 = new Process(2, quorum2);
        Process p3 = new Process(3, quorum3);

        quorum1.addAll(Arrays.asList(p2, p3));
        quorum2.addAll(Arrays.asList(p1, p3));
        quorum3.addAll(Arrays.asList(p1, p2));

        p1.start();
        p2.start();
        p3.start();

        Thread messageProcessor = new Thread(() -> {

            while (p1.isAlive() || p2.isAlive() || p3.isAlive()) {

                p1.processMessages();
                p2.processMessages();
                p3.processMessages();

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        messageProcessor.start();

        try {
            p1.join();
            p2.join();
            p3.join();

            messageProcessor.interrupt();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}