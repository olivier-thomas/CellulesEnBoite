package culturecellulaire;

import java.util.*;

/*
 * Auteur : Olivier THOMAS
 */

public class PlagedeCellules {

	// Attributs
	private final CoordonneesPoint centre;
	private int rayon = 1;

	// Methodes
	public PlagedeCellules(CoordonneesPoint centre) {
		this.centre = centre;
	}

	public PlagedeCellules(CoordonneesPoint centre, int rayon) {
		this.centre = centre;
		this.rayon = rayon;
	}

	public CoordonneesPoint getCentre() {
		return this.centre;
	}

	public int getRayon() {
		return this.rayon;
	}

	public void incRayon() {
		this.rayon++;
	}

	public static void deposerCellule(Random alea, Milieu culture, int rayonMax, int rayon) {
		CoordonneesPoint centreDepot = CoordonneesPoint.getPointAlea(alea, rayonMax, culture.getCentre());
		PlagedeCellules colonie = new PlagedeCellules(centreDepot, rayon);
		culture.addPlage(colonie);
		int minX = centreDepot.getX() - rayon;
		int maxX = centreDepot.getX() + rayon;
		int minY = centreDepot.getY() - rayon;
		int maxY = centreDepot.getY() + rayon;

		for (int i = minX; i <= maxX; i++) {
			for (int j = minY; j <= maxY; j++) {
				double distanceAuCentreDepot = centreDepot.distance(i, j);
				if (distanceAuCentreDepot < rayon) {
					double distanceAuCentreBoite = culture.getCentre().distance(i, j);
					if (distanceAuCentreBoite < rayonMax) {
						culture.addCellule(new Cellule(colonie, i, j, alea), true);
					}

				}
			}
		}
	}
}
