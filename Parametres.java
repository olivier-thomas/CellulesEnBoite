package culturecellulaire;

/*
 * Auteur : Olivier THOMAS
 */

public class Parametres {

	// Attributs concernant la culture et les prises de drogues antimitotiques
	private int dureeculture;
	private int nombre;
	private int dureeprise;
	private int intervalle;
	private int debut;

	// Methodes
	public void setDureeCulture(int dureeculture) {
		this.dureeculture = dureeculture;
	}

	public int getDureeCulture() {
		return dureeculture;
	}
	
	public void setNbPrises(int nombre) {
		this.nombre = nombre;
	}

	public int getNbPrises() {
		return nombre;
	}

	public void setDureePrise(int dureeprise) {
		this.dureeprise = dureeprise;
	}

	public int getDureePrise() {
		return dureeprise;
	}

	public void setIntervalle(int intervalle) {
		this.intervalle = intervalle;
	}

	public int getIntervalle() {
		return intervalle;
	}

	public void setDebutPrises(int debut) {
		this.debut = debut;
	}
	
	public int getDebutPrises() {
		return debut;
	}

}
