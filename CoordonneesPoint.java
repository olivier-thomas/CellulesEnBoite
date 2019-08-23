package culturecellulaire;

import java.util.Random;

/*
 * Auteur : Olivier THOMAS
 */

public class CoordonneesPoint {
	
	// Attributs
	private final int x;
	private final int y;

	// Methodes
	public CoordonneesPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public double distance(CoordonneesPoint autrePoint) { // Obtenir la distance entre ce point et un autre point connu.
		return distance(this.x, this.y, autrePoint.x, autrePoint.y);
	}

	public double distance(int x, int y) { // Obtenir la distance entre ce point et un autre point de coordonnees connues.
		return distance(this.x, this.y, x, y);
	}

	private double distance(int x1, int y1, int x2, int y2) { // Calculer une distance entre deux points dans un repere orthonormal.
		return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}

	public static CoordonneesPoint getPointAlea(Random alea, int rayonMax, CoordonneesPoint centre) {
		// Obtenir un point aleatoire (ses coordonnees en x et en y) au sein d'un disque de rayon et de centre donne.
		double angle = alea.nextDouble() * Math.PI * 2;
		int rayonPosition = alea.nextInt(rayonMax);
		int x = (int) Math.floor(Math.cos(angle) * rayonPosition) + centre.x; // Math.floor = fonction de troncature
		int y = (int) Math.floor(Math.sin(angle) * rayonPosition) + centre.y; // (int) ... = valeur forcee en entier

		return new CoordonneesPoint(x, y);
	}
}
