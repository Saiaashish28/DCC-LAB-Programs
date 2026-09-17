import java.util.*;

public class LamportLogicalClock {

    // Function to find the maximum timestamp between 2 events
    static int max1(int a, int b) {
        if (a > b)
            return a;
        else
            return b;
    }

    // Function to display the logical timestamp
    static void display(int e1, int e2, int p1[], int p2[]) {

        int i;

        System.out.print("\nThe time stamps of events in P1:\n");

        for (i = 0; i < e1; i++) {
            System.out.print(p1[i] + " ");
        }

        System.out.println("\nThe time stamps of events in P2:");

        for (i = 0; i < e2; i++) {
            System.out.print(p2[i] + " ");
        }

        System.out.println();
    }

    // Function to find the timestamp of events
    static void lamportLogicalClock(int e1, int e2, int m[][]) {

        int i, j, k;

        // Arrays for storing timestamps of P1 and P2
        int p1[] = new int[e1];
        int p2[] = new int[e2];

        // Initialize timestamps
        for (i = 0; i < e1; i++) {
            p1[i] = i + 1;
        }

        for (i = 0; i < e2; i++) {
            p2[i] = i + 1;
        }

        // Display the event matrix
        System.out.print("\t");

        for (i = 0; i < e2; i++) {
            System.out.print("e2" + (i + 1) + "\t");
        }

        for (i = 0; i < e1; i++) {

            System.out.print("\ne1" + (i + 1) + "\t");

            for (j = 0; j < e2; j++) {
                System.out.print(m[i][j] + "\t");
            }
        }

        // Calculate Lamport timestamps
        for (i = 0; i < e1; i++) {

            for (j = 0; j < e2; j++) {

                // Message is sent from P1 to P2
                if (m[i][j] == 1) {

                    p2[j] = max1(p2[j], p1[i] + 1);

                    // Update subsequent events in P2
                    for (k = j + 1; k < e2; k++) {
                        p2[k] = p2[k - 1] + 1;
                    }
                }

                // Message is received by P1 from P2
                if (m[i][j] == -1) {

                    p1[i] = max1(p1[i], p2[j] + 1);

                    // Update subsequent events in P1
                    for (k = i + 1; k < e1; k++) {
                        p1[k] = p1[k - 1] + 1;
                    }
                }
            }
        }

        // Display the final timestamps
        display(e1, e2, p1, p2);
    }

    public static void main(String args[]) {

        int e1 = 5;
        int e2 = 3;

        int m[][] = new int[5][3];

        /*
         * dep[i][j] = 1
         * Message is sent from P1 event i to P2 event j
         *
         * dep[i][j] = -1
         * Message is received by P1 event i from P2 event j
         *
         * dep[i][j] = 0
         * No message
         */

        m[0][0] = 0;
        m[0][1] = 0;
        m[0][2] = 0;

        m[1][0] = 0;
        m[1][1] = 0;
        m[1][2] = 1;

        m[2][0] = 0;
        m[2][1] = 0;
        m[2][2] = 0;

        m[3][0] = 0;
        m[3][1] = 0;
        m[3][2] = 0;

        m[4][0] = 0;
        m[4][1] = -1;
        m[4][2] = 0;

        // Function call
        lamportLogicalClock(e1, e2, m);
    }
}