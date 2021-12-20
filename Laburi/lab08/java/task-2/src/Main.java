import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.TreeSet;
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
        // ca sa nu am puncte critice duplicate
        TreeSet<Integer> cvs;

        // parent[node] = parent of node in the DFS traversal
        int[] parent;

        // found[node] = the timestamp when node was found (when started to visit its subtree)
        // Note: The global timestamp is incremented everytime a node is found.
        int[] found;

        // the minimum accessible timestamp that node can see/access
        // low_link[node] = min { found[x] | x is node OR x in ancestors(node) OR x in descendants(node) };
        int[] lowLink;

        // global timestamp
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
                    // muchie (x, y)
                    adj[x].add(y);
                    adj[y].add(x);
                }
                sc.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void writeOutput(ArrayList<Integer>  all_cvs) {
            try {
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FILE)));
                for (Integer cv : all_cvs) {
                    pw.printf("%d ", cv);
                }
                pw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private ArrayList<Integer> getResult() {
            //
            // TODO: Gasiti toate nodurile critice ale grafului neorientat stocat cu liste de adiacenta in adj.
            // Rezultatul se va returna sub forma unui vector cu toate punctele critice (ordinea nu conteaza).
            //
            // Indicatie: Folositi algoritmul lui Tarjan pentru CV.
            //
            cvs = new TreeSet<>();
            parent = new int[n + 1];
            found = new int[n + 1];
            lowLink = new int[n + 1];
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

            return new ArrayList<Integer>(cvs);
        }

        private void DFS(int node) {
            found[node] = ++timestamp;
            lowLink[node] = found[node];
            int children = 0;
            for (Integer neigh : adj[node]) {
                if (parent[neigh] != -1) {
                    if (neigh != parent[node]) {
                        lowLink[node] = min(lowLink[node], found[neigh]);
                    }

                    continue;
                }

                parent[neigh] = node;
                children++;
                DFS(neigh);

                lowLink[node] = min(lowLink[node], lowLink[neigh]);
                if (parent[node] != node && lowLink[neigh] >= found[node]) {
                    cvs.add(node);
                }
            }

            if (parent[node] == node && children > 1) {
                cvs.add(node);
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
