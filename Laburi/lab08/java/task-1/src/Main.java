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
        Stack<Integer> nodesStack;
        ArrayList<ArrayList<Integer>> allSccs;
        int timestamp;

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
                    adj[x].add(y); // arc (x, y)
                }
                sc.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void writeOutput(ArrayList<ArrayList<Integer>> all_sccs) {
            try {
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FILE)));
                pw.printf("%d\n", all_sccs.size());
                for (ArrayList<Integer> scc : all_sccs) {
                    for (Integer node : scc) {
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
            // TODO: Gasiti componentele tare conexe (CTC / SCC) ale grafului orientat cu n
            // noduri, stocat in adj.
            //
            // Rezultatul se va returna sub forma unui vector, fiecare element fiind un SCC
            // (adica tot un vector).
            // * nodurile dintr-un SCC pot fi gasite in orice ordine
            // * SCC-urile din graf pot fi gasite in orice ordine
            //
            // Indicatie: Folositi algoritmul lui Tarjan pentru SCC.
            //

            allSccs = new ArrayList<>();
            parent = new int[n + 1];
            found = new int[n + 1];
            lowLink = new int[n + 1];
            nodesStack = new Stack<>();
            for (int node = 1; node <= n; node++) {
                parent[node] = -1;
                found[node] = Integer.MAX_VALUE;
                lowLink[node] = Integer.MAX_VALUE;
            }

            timestamp = 0;
            for (int node = 1; node <= n; node++) {
                if (parent[node] == -1) {
                    parent[node] = node;
                    ArrayList<Integer> newScc = DFS(node);
                    if (newScc != null) {
                        allSccs.add(newScc);
                    }
                }
            }

            return allSccs;
        }

        private ArrayList<Integer> DFS(int node) {
            found[node] = ++timestamp;
            lowLink[node] = found[node];
            nodesStack.push(node);
            for (Integer neigh : adj[node]) {
                if (parent[neigh] != -1) {
                    if (nodesStack.contains(neigh)) {
                        lowLink[node] = min(lowLink[node], found[neigh]);
                    }

                    continue;
                }

                parent[neigh] = node;
                ArrayList<Integer> possibleScc = DFS(neigh);
                if (possibleScc != null) {
                    allSccs.add(possibleScc);
                }

                lowLink[node] = min(lowLink[node], lowLink[neigh]);
            }

            if (lowLink[node] == found[node]) {
                ArrayList<Integer> newScc = new ArrayList<>();
                int x;
                do {
                    x = nodesStack.pop();
                    newScc.add(x);
                } while(x != node);

                return newScc;
            }

            return null;
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
