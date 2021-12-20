import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Nodul din graf, informatia principala a acestuia este mail-ul care trebuie
 * sa fie distinct intre oricare 2 noduri din graf. Se poate ca 2 noduri
 * sa aiba aceeasi persoana.
 * @author alex
 *
 */
class Node {
	private String email;
	private String person;

	/**
	 * Vecinii nodului
	 */
	private List<Node> neighbours;

	/**
	 * Starea de vizitat in parcurgere
	 */
	private boolean visited;

	public Node(String email, String person) {
		this.email = email;
		this.person = person;
		this.neighbours = new ArrayList<>();
		this.visited = false;
	}

	public String getEmail() {
		return email;
	}

	public String getPerson() {
		return person;
	}

	public List<Node> getNeighbours() {
		return neighbours;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public void addNeighbours(List<Node> neighbours) {
		this.neighbours.addAll(neighbours);
	}

	public void setNeighbours(List<Node> neighbours) {
		this.neighbours = neighbours;
	}

	/**
	 * Actualizeaza nodul la o alta persoana non-distincta din care
	 * face parte daca aceasta este mai mica lexicografic decat
	 * persoana non-distincta curenta
	 * @param person2		cealalta persoana non-distincta
	 */
	public void updatePerson(String person2) {
		if (person.compareTo(person2) > 0) {
			person = person2;
		}
	}
}

/**
 * Contine informatiile unei persoane distincte si anume numele si
 * mail-urile acesteia
 * @author alex
 *
 */
class PersonInfo implements Comparable<PersonInfo> {
	private String name;
	private List<String> emails;

	public PersonInfo(String name, List<String> emails) {
		this.name = name;
		this.emails = emails;
	}

	public String getName() {
		return name;
	}

	public List<String> getEmails() {
		return emails;
	}

	/**
	 * Sortreaza mail-urile lexicografic crescator
	 */
	public void sortEmails() {
		Collections.sort(this.emails);
	}

	/**
	* Pentru a sorta persoanele dupa numarul de mail-uri
	* Daca au acelasi numar de mail-uri se sorteaza crescator lexicografic
	*/
	@Override
	public int compareTo(PersonInfo o) {
		if (emails.size() == o.getEmails().size()) {
			return name.compareTo(o.getName());
		}

		return emails.size() - o.getEmails().size();
	}
}

public class Adrese {
	/**
	* Un map ce are drept key mail-ul si drept valoare nodul aferent
	*/
	Map<String, Node> nodeMap;

	/**
	* Lista de persoane distincte
	*/
	List<PersonInfo> result;

	public static void main(String[] args) throws IOException {
		Adrese adrese = new Adrese();
		adrese.run();
	}

	private void run() throws IOException {
		readInput();
		dfs();
		writeOutput();
	}

	private void readInput() throws IOException {
		FileReader inputFile = new FileReader("adrese.in");
		BufferedReader sc = new BufferedReader(inputFile);
		int numberOfPeople = Integer.parseInt(sc.readLine());
		nodeMap = new HashMap<>();

		/**
		* Parcurg fisierul, impartind liniile de persoane in 2, primul token
		* este numele persoanei si al doilea numarul de mail-uri
		* Apoi citesc fiecare linie de mail si daca aceasta este deja
		* in map-ul de mail-uri, atunci adaug in lista de noduri nodul
		* din map, daca nu adaug in lista un nod nou creat pentru mail-ul
		* respectiv.
		*/
		for (int i = 0; i < numberOfPeople; i++) {
			String[] line = sc.readLine().split(" ");
			String person = line[0];
			int numberOfEmails = Integer.parseInt(line[1]);

			List<Node> nodes = new ArrayList<>();
			for (int j = 0; j < numberOfEmails; j++) {
				String email = sc.readLine();
				Node node = nodeMap.get(email);
				if (node != null) {
					nodes.add(node);
					continue;
				}

				node = new Node(email, person);
				nodes.add(node);
			}

			/**
			* Pentru fiecare nod din lista de noduri, adaug toate nodurile(inclusiv pe sine
			* pentru a nu mai aplica remove) si actualizez persoana nodului(va afecta
			* doar punctele de articulatie). Apoi pun nodul respectiv in map-ul de
			* noduri(va adauga doar nodurile care nu se afla in map).
			*/
			for (Node node : nodes) {
				List<Node> neighs = new ArrayList<>(nodes);
				node.addNeighbours(neighs);
				node.updatePerson(person);
				nodeMap.put(node.getEmail(), node);
			}
		}

		sc.close();
	}

	/**
	* Parcurgere in adancime, nodurile se iau din map
	*/
	private void dfs() {
		result = new ArrayList<>();
		for (Node node : nodeMap.values()) {
			if (!node.isVisited()) {
				visitNode(node);
			}
		}
	}

	/**
	* Viziteaza un nod si implicit o componenta conexa. Tin o lista de mail-uri cu toate
	* mail-urile componentei conexe/persoanei distincte. De asemenea pentru fiecare nod scos
	* din stiva, daca persoana sa non-distincta este mai mica lexicografic, atunci actualizez
	* persoana si o pun pe aceasta ca fiind persoana distincta a CC-ului.
	* @param startNode		nodul de start al componentei conexe
	*/
	private void visitNode(Node startNode) {
		Stack<Node> stack = new Stack<>();
		stack.add(startNode);
		startNode.setVisited(true);
		String person = startNode.getPerson();
		List<String> emails = new ArrayList<>();
		while (!stack.isEmpty()) {
			Node node = stack.pop();
			emails.add(node.getEmail());
			if (person.compareTo(node.getPerson()) > 0) {
				person = node.getPerson();
			}

			// adaug in stiva toate nodurile vecine si le setez drept vizitate
			for (Node neigh : node.getNeighbours()) {
				if (!neigh.isVisited()) {
					stack.add(neigh);
					neigh.setVisited(true);
				}
			}
		}

		result.add(new PersonInfo(person, emails));
	}

	/**
	* Sortez persoanele distincte stocate in result si le afisez alaturi de mail-uri aferente.
	* @throws IOException	pentru writer
	*/
	private void writeOutput() throws IOException {
		File outputFile = new File("adrese.out");
		Writer writer = new FileWriter(outputFile, false);
		Collections.sort(result);
		int numberOfDistinctPeople = result.size();
		writer.write(numberOfDistinctPeople + "\n");
		for (PersonInfo person : result) {
			person.sortEmails();
			writer.write(person.getName() + " " + person.getEmails().size() + "\n");
			for (String email : person.getEmails()) {
				writer.write(email + "\n");
			}
		}

		writer.close();
	}
}
