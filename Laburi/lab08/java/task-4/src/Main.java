import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;
import java.util.ArrayList;

public class Main {
    static class Task {
        class Edge {
            int x, y;

            public Edge(int x, int y) {
                this.x = x;
                this.y = y;
            }

            public boolean equals(Object o) {
                if (o == this) {
                    return true;
                }
                if (!(o instanceof Edge)) {
                    return false;
                }
                Edge other = (Edge)(o);
                return (x == other.x && y == other.y);
            }
        }

        public static final String INPUT_FILE = "in";
        public static final String OUTPUT_FILE = "out";

        // numarul maxim de noduri
        public static final int NMAX = (int) 1e5 + 5; // 10^5 + 5 = 100.005

        // n = numar de noduri, m = numar de muchii/arce
        int n, m;

        // adj[node] = lista de adiacenta a nodului node
        // exemplu: daca adj[node] = {..., neigh, ...} => exista arcul (node, neigh)
        @SuppressWarnings("unchecked")
        ArrayList<Integer> adj[] = new ArrayList[NMAX];
        int [] parent;
        int [] found;
        int [] lowLink;
        int timestamp;
        Stack<Edge> edgeStack;
        ArrayList<ArrayList<Integer>> allBccs = new ArrayList<>();

        public void solve() {
            readInput();
            writeOutput(getResult());
        }

        private void readInput() {
            try {
                Scanner sc = new Scanner(new BufferedReader(new FileReader(INPUT_FILE)));
                n = sc.nextInt();
                m = sc.nextInt();

                for (int i = 1; i <= n; i++) {
                    adj[i] = new ArrayList<>();
                }
                for (int i = 1; i <= m; i++) {
                    int x, y;
                    x = sc.nextInt();
                    y = sc.nextInt();
                    // muchie (x, y)
                    adj[x].add(y);
                    adj[y].add(x);
                }
                sc.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        private void writeOutput(ArrayList<ArrayList<Integer>> all_bccs) {
            try {
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FILE)));
                pw.printf("%d\n", all_bccs.size());
                for (ArrayList<Integer> bcc : all_bccs) {
                    for (Integer node : bcc) {
                        pw.printf("%d ", node);
                    }
                    pw.printf("\n");
                }
                pw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private ArrayList<ArrayList<Integer>> getResult() {
            //
            // TODO: Gasiti componentele biconexe (BCC) ale grafului neorientat cu n noduri, stocat in adj.
            //
            // Rezultatul se va returna sub forma unui vector, fiecare element fiind un BCC (adica tot un vector).
            // * nodurile dintr-un BCC pot fi gasite in orice ordine
            // * BCC-urile din graf pot fi gasite in orice ordine
            //
            // Indicatie: Folositi algoritmul lui Tarjan pentru BCC.
            //

            allBccs = new ArrayList<>();
            parent = new int[n + 1];
            found = new int[n + 1];
            lowLink = new int[n + 1];
            edgeStack = new Stack<>();
            for (int node = 1; node <= n; node++) {
                parent[node] = -1;
                found[node] = Integer.MAX_VALUE;
                lowLink[node] = Integer.MAX_VALUE;
            }

            timestamp = 0;
            for (int node = 1; node <= n; node++) {
                if (parent[node] == -1) {
                    parent[node] = node;
                    DFS(node);
                }
            }
            return allBccs;
        }

        private void DFS(int node) {
            found[node] = ++timestamp;
            lowLink[node] = found[node];
            for (Integer neigh : adj[node]) {
                if (parent[neigh] != -1) {
                    if (neigh != parent[node]) {
                        lowLink[node] = min(lowLink[node], found[neigh]);
                    }

                    continue;
                }

                parent[neigh] = node;

                Edge originalEdge = new Edge(node, neigh);
                edgeStack.push(originalEdge);
                DFS(neigh);

                lowLink[node] = min(lowLink[node], lowLink[neigh]);
                if (lowLink[neigh] >= found[node]) {
                    ArrayList<Integer> bcc = new ArrayList<>();
                    Edge edge;
                    do {
                        edge = edgeStack.pop();
                        bcc.add(edge.y);
                    } while(!edge.equals(originalEdge));

                    bcc.add(edge.x);
                    allBccs.add(bcc);
                }
            }
        }

        private int min(int a, int b) {
            if (a > b) {
                return b;
            }

            return a;
        }
    }

    public static void main(String[] args) {
        new Task().solve();
    }
}
