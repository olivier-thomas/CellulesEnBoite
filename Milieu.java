package culturecellulaire;

import java.awt.*;

import javax.swing.*;

import culturecellulaire.Cellule;
import culturecellulaire.Cellule.Etat;
import culturecellulaire.Pixel.Occupation;

import java.util.*;
import java.util.List;

/*
 * Auteur : Olivier THOMAS
 */

public class Milieu {

	// Attributs
	private static final int BORDURE = 25; // Parametre chiffre de bordure
	private static final int DELAI = 50; // Parametre chiffre de delai
	private static final int DIMENSIONS_ZONE_BOITE = 500; // Parametre chiffre du cote de la partie carree (gauche) de la fenetre
	private static final int NB_DEPOTS_CELLULES = 100; // Nombre de cellules deposees dans la boite au debut de la culture
	private static final int RAYON_DEPOT_CELLULES = 3; // Rayon de depot de cellules lors de l'etalement
	private final int bordure; // Largeur de la bordure autour de la boite de Petri
	private final int dimension; // Cote de la fenetre carree
	private final CoordonneesPoint centre; // Le centre de la boite de Petri
	private static final int LARGEUR_LABEL_AXE = 50;// Largeur de la bande reservee pour l'axe et son label
	private static final int HAUTEUR_LABEL_AXE = 50; // Hauteur de la bande reservee pour l'axe horizontal et son label
	private static final int X_AXE_VERTICAL = DIMENSIONS_ZONE_BOITE + LARGEUR_LABEL_AXE; // Origine des abscisses sur l'axe graphique des
																							// courbes
	private static final int Y_AXE_HORIZONTAL = DIMENSIONS_ZONE_BOITE - HAUTEUR_LABEL_AXE; // Origine des ordonnees sur l'axe graphique des
																							// courbes

	// Jeu de couleurs pour les courbes de suivi de numération
	private static final Color COULEUR_TEXTE = Color.BLACK; // Couleur definie pour le texte des legendes des courbes
	private static final Color COULEUR_COURBE_CELLULES = Color.BLACK;
	private static final Color COULEUR_COURBE_CELLULES_SAINES = new Color(50, 150, 50);
	private static final Color COULEUR_COURBE_CELLULES_MALIGNES = Color.RED;
	private static final Color COULEUR_CADRILLAGE = Color.LIGHT_GRAY;
	private static final Color COULEUR_AXE = Color.DARK_GRAY;

	// Listes ordonnees, ensembles et tableaux
	private List<PlagedeCellules> colonies = new ArrayList<>(); // Liste ordonnee des colonies
	private List<Cellule> contenu = new ArrayList<>(); // Liste ordonnee de toutes les cellules dans la boite
	/**
	 * Liste ordonnee de pixels modifies durant le cycle en cours.<br>
	 * Permet d'optimiser le rafraichissement en ne re-dessinant que les points necessaires.
	 */
	private List<Pixel> pixelsModifies = new ArrayList<>(); // Liste ordonnee des pixels modifies au cours du cycle actuel,
	private Set<Cellule> cellulesTuees = new HashSet<>(); // Ensemble des unites tuees/ecrasees
	private Pixel[][] pixels; // Grille de points

	// Methodes
	public static void delay(int ms) throws InterruptedException {
		Thread.sleep(ms); // Fonction plus economique en ressource CPU que while(...)
	}

	public static void main(String[] args) throws InterruptedException {

		// Fenetre pour le parametrage par l'utilisateur
		FenetreUtilisateur fenetreUtilisateur = new FenetreUtilisateur();
		fenetreUtilisateur.setVisible(true);

		// Mise en place de la boite de dialogue OK/ANNULER
		int reponse = JOptionPane.showConfirmDialog(null, fenetreUtilisateur, "Réglages du traitement antimitotique",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (fenetreUtilisateur.getParametres().getDureeCulture() < fenetreUtilisateur.getParametres().getDebutPrises()
				+ (fenetreUtilisateur.getParametres().getDureePrise() * fenetreUtilisateur.getParametres().getNbPrises() + fenetreUtilisateur
						.getParametres().getIntervalle() * (fenetreUtilisateur.getParametres().getNbPrises() - 1))) {
			JOptionPane.showMessageDialog(null, "Erreur :\n"
					+ "La durée d'expérience sélectionée est trop courte pour pouvoir observer la totalité du traitement paramétré.");
		} else if (reponse == JOptionPane.OK_OPTION) { // Si confirmation le programme poursuit son execution.

			// Fenetre de modelisation graphique
			JFrame fenetre = new JFrame("Culture de cellules en boîte");
			fenetre.setMinimumSize(new Dimension(DIMENSIONS_ZONE_BOITE, DIMENSIONS_ZONE_BOITE));
			fenetre.setSize(DIMENSIONS_ZONE_BOITE + fenetreUtilisateur.getParametres().getDureeCulture() + 2 * LARGEUR_LABEL_AXE,
					DIMENSIONS_ZONE_BOITE);
			fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			fenetre.setVisible(true);
			Graphics g = fenetre.getGraphics();

			Random alea = new Random();

			Milieu culture = new Milieu(DIMENSIONS_ZONE_BOITE, BORDURE);
			culture.dessine(g);

			traceLegende(g, COULEUR_COURBE_CELLULES, "TOTAL DES CELLULES", 35);
			traceLegende(g, COULEUR_COURBE_CELLULES_SAINES, "CELLULES SAINES", 55);
			traceLegende(g, COULEUR_COURBE_CELLULES_MALIGNES, "CELLULES MALIGNES", 75);

			// Dessin de l'axe des ordonnees et de ses graduations en fonction de la duree de culture choisie
			for (int i = 0; i <= 7; i++) {
				g.setColor(COULEUR_AXE);
				traceAxeOrdonnees(g, 50 * i);
				if (i == 0) {
					g.drawLine(X_AXE_VERTICAL, Y_AXE_HORIZONTAL, X_AXE_VERTICAL + fenetreUtilisateur.getParametres().getDureeCulture()
							+ (int) Math.floor(LARGEUR_LABEL_AXE / 2), Y_AXE_HORIZONTAL);
				}
				if (i > 0) {
					g.setColor(COULEUR_CADRILLAGE);
					g.drawLine(X_AXE_VERTICAL, Y_AXE_HORIZONTAL - 50 * i, X_AXE_VERTICAL
							+ fenetreUtilisateur.getParametres().getDureeCulture() + (int) Math.floor(LARGEUR_LABEL_AXE / 2),
							Y_AXE_HORIZONTAL - 50 * i);
				}
			}

			// Dessin de l'axe des abscisses et de ses graduations en fonction de la duree de culture choisie
			for (int i = 0; i <= (int) Math.floor(fenetreUtilisateur.getParametres().getDureeCulture() / 50); i++) {
				if (i == 0) {
					g.setColor(COULEUR_AXE);
					g.drawLine(X_AXE_VERTICAL + 50 * i, Y_AXE_HORIZONTAL, X_AXE_VERTICAL + 50 * i, 100);
					traceAxeAbscisses(g, 50 * i);
				}
				if (i > 0) {
					g.setColor(COULEUR_CADRILLAGE);
					g.drawLine(X_AXE_VERTICAL + 50 * i, Y_AXE_HORIZONTAL, X_AXE_VERTICAL + 50 * i, 100);
					traceAxeAbscisses(g, 50 * i);
				}
			}

			delay(DELAI);

			Antimitotique drogue = new Antimitotique(alea);
			Cycle[] cycles = new Cycle[fenetreUtilisateur.getParametres().getDureeCulture()]; // Initialisation du tableau de cycles.

			// Creation des différentes instances de cycles
			for (int i = 0; i < fenetreUtilisateur.getParametres().getDureeCulture(); i++) {
				cycles[i] = new Cycle(false, 0, 0, 0); // La drogue antimitotique est absente par defaut.
				if (fenetreUtilisateur.getParametres().getNbPrises() > 0) {
					for(int n = 1; n <= fenetreUtilisateur.getParametres().getNbPrises(); n++){
						if(i >= fenetreUtilisateur.getParametres().getDebutPrises() + (fenetreUtilisateur.getParametres().getDureePrise() + fenetreUtilisateur.getParametres().getIntervalle()) * (n - 1) && i < fenetreUtilisateur.getParametres().getDebutPrises() + fenetreUtilisateur.getParametres().getDureePrise() * n + fenetreUtilisateur.getParametres().getIntervalle() * (n - 1)){
							cycles[i].setDroguepresente(true);
						}
					}
				}
			}

			/**
			 * La drogue antimitotique est presente si le nombre de prise est strictement superieur a zero au cours de la culture. En
			 * fonction du nombre de prises, de leur duree ainsi que de la duree de l'intervalle entre chaque prise, le nombre de cycles ou
			 * la drogue est presente varie.
			 */

			// Depot initial de Cellules
			for (int i = 0; i < NB_DEPOTS_CELLULES; i++) {
				PlagedeCellules.deposerCellule(alea, culture, culture.dimension / 2 - culture.bordure, RAYON_DEPOT_CELLULES);
			}

			for (int i = 0; i < fenetreUtilisateur.getParametres().getDureeCulture(); i++) {

				Cellule[] cellules = new Cellule[culture.contenu.size()];
				culture.contenu.toArray(cellules); // On travaille sur une copie ponctuelle du Set, pour eviter les erreurs de type
													// ConcurrentModificationException

				for (Cellule cellule : cellules) {

					if (cycles[i].isDroguepresente() == true) {
						drogue.effetDrogue(culture, cellule); // Faire agir la drogue antimitotique sur chacune des cellules présentes en
																// début de cycle.
					}
					cellule.repartMitose(culture.pixels);
					cellule.vivre(culture, cellule); // Faire vivre toutes les cellules survivantes connues du cycle.
				}

				culture.dessineModifications(g);

				for (PlagedeCellules colonie : culture.colonies) {
					colonie.incRayon();
				}
				culture.contenu.removeAll(culture.cellulesTuees);
				culture.cellulesTuees.clear();

				Cellule[] cellulesfin = new Cellule[culture.contenu.size()];
				culture.contenu.toArray(cellulesfin);

				for (Cellule cellule : cellulesfin) {
					cycles[i].setNbtotalcellules(cycles[i].getNbtotalcellules() + 1);
					if (cellule.getEtat() == Etat.NORMALE_EN_MITOSE || cellule.getEtat() == Etat.NORMALE_QUIESCENTE) {
						cycles[i].setNbcellulesnormales(cycles[i].getNbcellulesnormales() + 1);
					} else if (cellule.getEtat() == Etat.MALIGNE) {
						cycles[i].setNbcellulesmalignes(cycles[i].getNbcellulesmalignes() + 1);
					}
				}

				// Tracer les courbes d'evolution.
				if (i > 0) {
					traceCourbe(g, COULEUR_COURBE_CELLULES, i, cycles[i - 1].getNbtotalcellules(), cycles[i].getNbtotalcellules());
					traceCourbe(g, COULEUR_COURBE_CELLULES_SAINES, i, cycles[i - 1].getNbcellulesnormales(),
							cycles[i].getNbcellulesnormales());
					traceCourbe(g, COULEUR_COURBE_CELLULES_MALIGNES, i, cycles[i - 1].getNbcellulesmalignes(),
							cycles[i].getNbcellulesmalignes());
				}
			}

		} else if ((reponse == JOptionPane.CANCEL_OPTION)) { // Si annulation le programme se termine.
			return;
		}
	}

	public Milieu(int dimension, int bordure) {
		pixels = new Pixel[dimension][dimension];
		this.dimension = dimension;
		this.bordure = bordure;
		this.centre = new CoordonneesPoint(dimension / 2, dimension / 2);
		for (int i = 0; i < pixels.length; i++) {

			for (int j = 0; j < pixels.length; j++) {
				if (distanceAuCentre(i, j) < (dimension / 2) - bordure) {
					// Initialisation des points a l'interieur de la boite de Petri
					pixels[i][j] = new Pixel(i, j, Occupation.DISPONIBLE);
				} else {
					// Initialisation des points a l'exterieur de la boite de Petri et sur les bords de celle-ci
					pixels[i][j] = new Pixel(i, j, Occupation.EXTERIEUR);
				}
			}
		}
	}

	public void addCellule(Cellule cellule, boolean ecrase) {
		this.contenu.add(cellule);
		changeEtatCellule(cellule, ecrase);
	}

	public void addPlage(PlagedeCellules colonie) {
		this.colonies.add(colonie);
	}

	public void changeEtatCellule(Cellule cellule, boolean ecrase) {
		Pixel pixel = this.pixels[cellule.getX()][cellule.getY()];
		if (ecrase && pixel.getContenu() != null) {
			this.cellulesTuees.add(pixel.getContenu());
		}
		pixel.setContenu(cellule);
		switch (cellule.getEtat()) {
		case NORMALE_EN_MITOSE:
			pixel.setOccupation(Occupation.OCCUPE_CELLULE_EN_MITOSE);
			break;
		case NORMALE_QUIESCENTE:
			pixel.setOccupation(Occupation.OCCUPE_CELLULE_QUIESCENTE);
			break;
		case MALIGNE:
			pixel.setOccupation(Occupation.OCCUPE_CELLULE_MALIGNE);
			break;
		case MORTE:
			pixel.setOccupation(Occupation.DISPONIBLE);
			break;
		default:
			break;
		}
		this.pixelsModifies.add(pixel);
	}

	public void tueCellule(Cellule cellule) {
		Pixel pixel = this.pixels[cellule.getX()][cellule.getY()];
		cellule.setEtat(Etat.MORTE);
		pixel.setOccupation(Occupation.DISPONIBLE);
		this.cellulesTuees.add(pixel.getContenu());
		this.pixelsModifies.add(pixel);
	}

	public void dessine(Graphics g) {
		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < pixels.length; j++) {
				Pixel pixel = pixels[i][j];
				g.setColor(pixel.getOccupation().getCouleur());
				g.drawLine(i, j, i, j);
			}
		}
	}

	private double distanceAuCentre(int i, int j) {
		return this.centre.distance(i, j);
	}

	public CoordonneesPoint getCentre() {
		return centre;
	}

	public int getDimension() {
		return dimension;
	}

	public Pixel.Occupation getOccupationA(int x, int y) {
		return this.pixels[x][y].getOccupation();
	}

	public void dessineModifications(Graphics g) {
		for (Pixel pixel : this.pixelsModifies) {
			g.setColor(pixel.getOccupation().getCouleur());
			g.drawLine(pixel.getCoordonnees().getX(), pixel.getCoordonnees().getY(), pixel.getCoordonnees().getX(), pixel.getCoordonnees()
					.getY());
		}
		this.pixelsModifies.clear();
	}

	private static void traceCourbe(Graphics g, Color couleur, int cycle, int valeur, int ancienneValeur) {
		traceCourbe(g, couleur, X_AXE_VERTICAL + (cycle - 1), Y_AXE_HORIZONTAL - (int) Math.floor(valeur / 500), X_AXE_VERTICAL + (cycle),
				Y_AXE_HORIZONTAL - (int) Math.floor(ancienneValeur / 500));
	}

	public static void traceCourbe(Graphics g, Color couleur, int x1, int y1, int x2, int y2) {
		// Fonction pour tracer une courbe de numération, permet de tracer chacune des trois courbes souhaitees.
		g.setColor(couleur);
		g.drawLine(x1, y1, x2, y2);
	}

	private static void traceAxeOrdonnees(Graphics g, int y) {
		g.setColor(COULEUR_AXE);
		g.drawLine(X_AXE_VERTICAL - 5, Y_AXE_HORIZONTAL - y, X_AXE_VERTICAL, Y_AXE_HORIZONTAL - y);
		g.setColor(COULEUR_TEXTE);
		g.drawString(String.format("%5d", y * 500), DIMENSIONS_ZONE_BOITE, Y_AXE_HORIZONTAL - y + 5);
	}

	private static void traceAxeAbscisses(Graphics g, int x) {
		g.setColor(COULEUR_AXE);
		g.drawLine(X_AXE_VERTICAL + x, Y_AXE_HORIZONTAL + 5, X_AXE_VERTICAL + x, Y_AXE_HORIZONTAL);
		g.setColor(COULEUR_TEXTE);
		g.drawString(String.format("%5d", x), X_AXE_VERTICAL + x - (int) Math.floor(LARGEUR_LABEL_AXE / 2),
				Y_AXE_HORIZONTAL + (int) Math.floor(HAUTEUR_LABEL_AXE / 2));
	}

	private static void traceLegende(Graphics g, Color couleurCourbeCellules, String libelle, int y) {
		g.setColor(couleurCourbeCellules);
		g.drawLine(DIMENSIONS_ZONE_BOITE + 10, y, DIMENSIONS_ZONE_BOITE + 20, y);
		g.setColor(COULEUR_TEXTE);
		g.drawString(libelle, DIMENSIONS_ZONE_BOITE + 25, y + 5);
	}
}
