package culturecellulaire;

import java.awt.*;

/*
 * Auteur : Olivier THOMAS
 */

public class Pixel {

	// Enumerations
	public enum Occupation { // Sous-classe de Pixel recensant les differents etats d'occupation que peut prendre une unite de surface
								// (pixel)
		DISPONIBLE(Color.WHITE), EXTERIEUR(Color.GRAY), OCCUPE_CELLULE_MALIGNE(Color.RED), OCCUPE_CELLULE_QUIESCENTE(Color.PINK), OCCUPE_CELLULE_EN_MITOSE(
				Color.ORANGE);
		private final Color couleur;

		private Occupation(Color couleur) { // Lien entre un etat et la couleur prise par le pixel
			this.couleur = couleur;
		}

		public Color getCouleur() {
			return couleur;
		}
	}

	// Attributs
	private final CoordonneesPoint coordonnees; // Les coordonnees du pixel
	private Cellule contenu;  // La cellule occupant le pixel
	private Occupation occupation; // L'etat d'occupation du pixel et la couleur a afficher qui correspond.

	// Methodes
	public Pixel(CoordonneesPoint c, Occupation occupation) {
		this.coordonnees = c;
		this.occupation = occupation;
	}

	public Pixel(int i, int j, Occupation occupation) {
		this.coordonnees = new CoordonneesPoint(i, j);
		this.occupation = occupation;
	}

	public CoordonneesPoint getCoordonnees() {
		return coordonnees;
	}

	public void setOccupation(Occupation occupation) {
		this.occupation = occupation;
	}

	public Occupation getOccupation() {
		return occupation;
	}

	public void setContenu(Cellule cellule) {
		this.contenu = cellule;
	}

	public Cellule getContenu() {
		return contenu;
	}

}
