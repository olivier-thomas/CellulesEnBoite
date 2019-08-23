package culturecellulaire;

import java.util.*;

import culturecellulaire.Milieu;

/*
 * Auteur : Olivier THOMAS
 */

public class Cellule {

	public enum Etat { // Sous-classe listant les differents etats genetiques et la position dans le cycle cellulaire possibles pour une
						// cellule
		NORMALE_EN_MITOSE, NORMALE_QUIESCENTE, MALIGNE, MORTE
	}

	// Attributs
	private static final int MAX_RAYON = 400;
	private int age = 0;
	private int rayon;
	private int mutation = 0;
	private boolean cancereuse = false;
	private Etat etat = Etat.NORMALE_EN_MITOSE; // Initialisation de l'etat genetique de la cellule
	private PlagedeCellules colonie;
	private CoordonneesPoint coordonnees;
	private Random alea = new Random();

	// Methodes
	public Cellule(PlagedeCellules colonie, int x, int y, Random alea) {
		this.colonie = colonie;
		this.coordonnees = new CoordonneesPoint(x, y);
		this.rayon = 1;
		this.alea = alea;
	}

	public Cellule(PlagedeCellules colonie, int x, int y, Random alea, int mutation) {
		this.colonie = colonie;
		this.coordonnees = new CoordonneesPoint(x, y);
		this.rayon = 1;
		this.alea = alea;
		this.mutation = mutation;
	}

	public Cellule(Random alea) {
		this.alea = alea;
		double angle = alea.nextDouble() * Math.PI * 2;
		int rayonPosition = alea.nextInt(MAX_RAYON);
		int x = (int) Math.floor(Math.cos(angle) * rayonPosition) + 500;
		int y = (int) Math.floor(Math.sin(angle) * rayonPosition) + 500;
		this.coordonnees = new CoordonneesPoint(x, y);
		this.rayon = 3;
	}

	public Cellule(PlagedeCellules colonie, int x, int y, Random alea, int mutation, Etat etat) {
		this.colonie = colonie;
		this.coordonnees = new CoordonneesPoint(x, y);
		this.rayon = 1;
		this.alea = alea;
		this.mutation = mutation;
		this.etat = etat;
	}

	public void setAlea(Random alea) {
		this.alea = alea;
	}

	public Random getAlea() {
		return alea;
	}

	public void setEtat(Etat etat) {
		this.etat = etat;
	}

	public Etat getEtat() {
		return etat;
	}

	public int getX() {
		return this.coordonnees.getX();
	}

	public int getY() {
		return this.coordonnees.getY();
	}

	public CoordonneesPoint getCoordonnees() {
		return this.coordonnees;
	}

	public void setRayon(int rayon) {
		this.rayon = rayon;
	}

	public int getRayon() {
		return this.rayon;
	}

	public PlagedeCellules getPlage() {
		return colonie;
	}

	public void setPlage(PlagedeCellules colonie) {
		this.colonie = colonie;
	}

	public void vivre(Milieu culture, Cellule cellule) {
		if (etat == Etat.NORMALE_EN_MITOSE || etat == Etat.MALIGNE) {
			possibleMutagenese(culture);
			etale(culture, this.coordonnees.getX() - 1, this.coordonnees.getY() - 1);
			etale(culture, this.coordonnees.getX() - 1, this.coordonnees.getY());
			etale(culture, this.coordonnees.getX() - 1, this.coordonnees.getY() + 1);
			etale(culture, this.coordonnees.getX(), this.coordonnees.getY() - 1);
			etale(culture, this.coordonnees.getX(), this.coordonnees.getY() + 1);
			etale(culture, this.coordonnees.getX() + 1, this.coordonnees.getY() - 1);
			etale(culture, this.coordonnees.getX() + 1, this.coordonnees.getY());
			etale(culture, this.coordonnees.getX() + 1, this.coordonnees.getY() + 1);

			if (etat == Etat.NORMALE_EN_MITOSE) {
				if (cancereuse == true) {
					this.etat = Etat.MALIGNE;
					setPlage(new PlagedeCellules(this.coordonnees));
					culture.addPlage(this.getPlage());
					culture.changeEtatCellule(this, false);
				} else if (this.age > 2) { // Les cellules saines trop agees (eloignees du front de proliferation) deviennent quiescentes et
											// cessent de se diviser.
					this.etat = Etat.NORMALE_QUIESCENTE;
					culture.changeEtatCellule(this, false);
				}
				this.age++; // La cellule vieillit d'un cycle cellulaire.
			}
		}
	}

	public void setCancereuse(boolean cancereuse) {
		this.cancereuse = cancereuse;
	}

	public boolean getCancereuse() {
		return this.cancereuse;
	}

	public void possibleMutagenese(Milieu culture) { // Mecanisme d'apparition de mutation et developpement du cancer
		if (this.etat == Etat.NORMALE_EN_MITOSE && alea.nextInt(1000) == 0) {
			this.mutation = mutation + 1;
			if (this.mutation >= 5 && this.cancereuse == false) {
				culture.tueCellule(this);
			} else if (alea.nextInt(100) == 0) {
				this.cancereuse = true;
			}
		} else if(this.etat == Etat.NORMALE_QUIESCENTE && alea.nextInt(100000) == 0) {
			this.mutation = mutation + 1;
			if (this.mutation >= 5 && this.cancereuse == false) {
				culture.tueCellule(this);
			} else if (alea.nextInt(100) == 0) {
				this.cancereuse = true;
			}
		}
	}

	public void repartMitose(Pixel[][] pixels) {

		Cellule gauche = pixels[this.coordonnees.getX() - 1][this.coordonnees.getY()].getContenu();
		Cellule droite = pixels[this.coordonnees.getX() + 1][this.coordonnees.getY()].getContenu();
		Cellule dessous = pixels[this.coordonnees.getX()][this.coordonnees.getY() - 1].getContenu();
		Cellule dessus = pixels[this.coordonnees.getX()][this.coordonnees.getY() + 1].getContenu();
		Cellule dessusdroite = pixels[this.coordonnees.getX() + 1][this.coordonnees.getY() + 1].getContenu();
		Cellule dessusgauche = pixels[this.coordonnees.getX() - 1][this.coordonnees.getY() + 1].getContenu();
		Cellule dessousgauche = pixels[this.coordonnees.getX() - 1][this.coordonnees.getY() - 1].getContenu();
		Cellule dessousdroite = pixels[this.coordonnees.getX() + 1][this.coordonnees.getY() - 1].getContenu();

		if (this.etat == Etat.NORMALE_QUIESCENTE
				&& (gauche == null || droite == null || dessous == null || dessus == null || dessusdroite == null || dessusgauche == null
						|| dessousgauche == null || dessousdroite == null || gauche.getEtat() == Etat.MORTE
						|| droite.getEtat() == Etat.MORTE || dessous.getEtat() == Etat.MORTE || dessus.getEtat() == Etat.MORTE
						|| dessusdroite.getEtat() == Etat.MORTE || dessusgauche.getEtat() == Etat.MORTE
						|| dessousgauche.getEtat() == Etat.MORTE || dessousdroite.getEtat() == Etat.MORTE)) {

			this.etat = Etat.NORMALE_EN_MITOSE;
			this.age = 0;
		}
	}

	private void etale(Milieu culture, int x, int y) {
		// On suspend cet etalement au cycle suivant.
		// Permet de maintenir l'aspect circulaire de la colonie.
		if (this.getPlage().getCentre().distance(x, y) > (getPlage().getRayon())) {
			return;
		}
		Pixel.Occupation etatPixel = culture.getOccupationA(x, y);
		switch (this.etat) {
		case MALIGNE:
			switch (etatPixel) {
			case EXTERIEUR:
			case OCCUPE_CELLULE_MALIGNE:
				return;
			case OCCUPE_CELLULE_QUIESCENTE:
			case OCCUPE_CELLULE_EN_MITOSE:
			case DISPONIBLE:
				culture.addCellule(new Cellule(this.getPlage(), x, y, this.alea, mutation, Etat.MALIGNE), true);
				return;
			}
			break;
		case NORMALE_EN_MITOSE:
			switch (etatPixel) {
			case EXTERIEUR:
			case OCCUPE_CELLULE_MALIGNE:
				return;
			case OCCUPE_CELLULE_QUIESCENTE:
			case OCCUPE_CELLULE_EN_MITOSE:
				if (this.cancereuse == true) {
					// Nouvelle cellule cancereuse
					PlagedeCellules nouvellePlage = new PlagedeCellules(new CoordonneesPoint(x, y));
					culture.addCellule(new Cellule(nouvellePlage, x, y, this.alea, mutation, Etat.MALIGNE), true);
					culture.addPlage(nouvellePlage);
				}
				return;
			case DISPONIBLE:
				if (this.cancereuse == true) {
					// Nouvelle cellule cancereuse
					PlagedeCellules nouvellePlage = new PlagedeCellules(new CoordonneesPoint(x, y));
					culture.addCellule(new Cellule(nouvellePlage, x, y, this.alea, mutation, Etat.MALIGNE), true);
					culture.addPlage(nouvellePlage);
				} else {
					// Nouvelle cellule saine
					culture.addCellule(new Cellule(this.getPlage(), x, y, this.alea, mutation), false);
				}
				return;
			}
			break;
		default:
			break;
		}
	}
}
