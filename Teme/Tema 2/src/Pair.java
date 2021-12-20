/**
 * Clasa generala folosita pentru stocarea de perechi de orice fel.
 * @author alex
 *
 * @param <T>	tipul elementului din stanga
 * @param <E>	tipul elementului din dreapta
 */
public class Pair<T, E> {
	private T leftElem;
	private E rightElem;

	public Pair(T leftElem, E rightElem) {
		this.leftElem = leftElem;
		this.rightElem = rightElem;
	}

	public T getLeftElem() {
		return leftElem;
	}

	public void setLeftElem(T leftElem) {
		this.leftElem = leftElem;
	}

	public E getRightElem() {
		return rightElem;
	}

	public void setRightElem(E rightElem) {
		this.rightElem = rightElem;
	}

	// pentru problema podurilor am facut getere personalizate care sa
	// reprezinte pozitia, elementul din stanga fiind linia, iar
	// elementul din dreapta coloana
	public T getX() {
		return getLeftElem();
	}

	public E getY() {
		return getRightElem();
	}
}
