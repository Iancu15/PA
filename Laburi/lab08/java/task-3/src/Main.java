import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    static class Task {
        class Edge {
            int x, y;

            public Edge(int x, int y) {
                this.x = x;
                this.y = y;
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
        ArrayList<Edge> allCes;

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

        private void writeOutput(ArrayList<Edge>  all_ces) {
            try {
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FILE)));
                pw.printf("%d\n", all_ces.size());
                for (Edge ce : all_ces) {
                    pw.printf("%d %d\n", ce.x, ce.y);
                }
                pw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private ArrayList<Edge> getResult() {
            //
            // TODO: Gasiti toate muchiile critice ale grafului neorientat stocat cu liste de adiacenta in adj.
            // Rezultatul se va returna sub forma unui vector cu toate muchiile critice (ordinea nu conteaza).
            //
            // Indicatie: Folositi algoritmul lui Tarjan pentru CE.
            //

            allCes = new ArrayList<>();
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
            return allCes;
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
                DFS(neigh);

                lowLink[node] = min(lowLink[node], lowLink[neigh]);
                if (lowLink[neigh] > found[node]) {
                    allCes.add(new Edge(node, neigh));
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
