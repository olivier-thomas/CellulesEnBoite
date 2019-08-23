package culturecellulaire;

import java.util.*;

/*
 * Auteur : Olivier THOMAS
 */

public class Cycle {

	// Attributs
	private boolean droguepresente; // Declaration de la propriete absence/presence de drogue pour ce cycle
	private int nbtotalcellules = 0; // Initialisation du compteur du total de cellules pour ce cycle
	private int nbcellulesnormales = 0; // Initialisation du compteur de cellules normales pour ce cycle
	private int nbcellulesmalignes = 0; // Initialisation du compteur de cellules malignes pour ce cycle

	// Listes ordonnees
	private static List<Cellule> contenudescyclestermines = new ArrayList<>(); // Liste ordonnee de toutes les cellules dans la boite

	/**
	 * Liste ordonnee de pixels modifies durant le cycle en cours.<br>
	 * Permet d'optimiser le rafraichissement en ne re-dessinant que les points necessaires.
	 */

	// Methodes
	public Cycle(boolean droguepresente, int t, int n, int m) {
		this.droguepresente = droguepresente;
		this.nbtotalcellules = t;
		this.nbcellulesnormales = n;
		this.nbcellulesmalignes = m;
	}

	public static void setContenudescyclestermines(List<Cellule> contenudescyclestermines) {
		Cycle.contenudescyclestermines = contenudescyclestermines;
	}

	public static List<Cellule> getContenudescyclestermines() {
		return contenudescyclestermines;
	}

	public void setNbtotalcellules(int nbtotalcellules) {
		this.nbtotalcellules = nbtotalcellules;
	}

	public int getNbtotalcellules() {
		return nbtotalcellules;
	}

	public void setNbcellulesnormales(int nbcellulesnormales) {
		this.nbcellulesnormales = nbcellulesnormales;
	}

	public int getNbcellulesnormales() {
		return nbcellulesnormales;
	}

	public void setNbcellulesmalignes(int nbcellulesmalignes) {
		this.nbcellulesmalignes = nbcellulesmalignes;
	}

	public int getNbcellulesmalignes() {
		return nbcellulesmalignes;
	}

	public void setDroguepresente(boolean droguepresente) {
		this.droguepresente = droguepresente;
	}

	public boolean isDroguepresente() {
		return droguepresente;
	}

}
