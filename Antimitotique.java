package culturecellulaire;

import java.util.*;

import culturecellulaire.Milieu;
import culturecellulaire.Cellule.Etat;

/*
 * Auteur : Olivier THOMAS
 */

public class Antimitotique {

	// Attributs
	private Random alea;

	// Methodes
	public Antimitotique(Random alea){
		this.alea = alea;
	}
	
	public void effetDrogue(Milieu culture, Cellule cellule) {
		/*
		 * La drogue antimitotique tue une partie des cellules en mitose et favorise la mutation des cellules survivantes.
		 */
		if ((cellule.getEtat() == Etat.MALIGNE || cellule.getEtat() == Etat.NORMALE_EN_MITOSE) && alea.nextInt(100) >= 10) {
			culture.tueCellule(cellule);
		} else if (cellule.getEtat() == Etat.NORMALE_QUIESCENTE || cellule.getEtat() == Etat.NORMALE_EN_MITOSE) {
			cellule.possibleMutagenese(culture);
		}
	}
}
