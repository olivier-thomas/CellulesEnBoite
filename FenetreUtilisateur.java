package culturecellulaire;

import java.awt.*;
import javax.swing.*;

/*
 * Auteur : Olivier THOMAS
 */

public class FenetreUtilisateur extends JPanel {

	private static final long serialVersionUID = 1L;

	private final Parametres parametres = new Parametres();

	// Les valeurs possibles pour chaque parametre
	private final Integer[] CYCLES = { 0, 10, 20, 50, 100, 150, 200, 250, 300, 350, 400, 450, 500 };
	private final Integer[] NOMBRES = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
	private final Integer[] DUREES = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
	private final Integer[] INTERVALLES = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25 };
	private final Integer[] DEBUTS = { 0, 5, 10, 20, 50, 100, 150, 200 };

	// Les boites deroulantes affichant les valeurs
	private JComboBox<Integer> saisieDureeCulture = new JComboBox<>(CYCLES);
	private JComboBox<Integer> saisieNombre = new JComboBox<>(NOMBRES);
	private JComboBox<Integer> saisieDureePrise = new JComboBox<>(DUREES);
	private JComboBox<Integer> saisieIntervalle = new JComboBox<>(INTERVALLES);
	private JComboBox<Integer> saisieDebut = new JComboBox<>(DEBUTS);

	// Methodes
	public FenetreUtilisateur() {
		// Les champs des intitules
		JPanel reglageLabels = new JPanel(new GridLayout(5, 1, 5, 5));
		reglageLabels.add(new JLabel("Durée totale de la culture (de 0 à 500 cycles) :"));
		reglageLabels.add(new JLabel("Nombre de prise(s) (de 0 à 10) :"));
		reglageLabels.add(new JLabel("Durée de chaque prise (de 0 à 20 cycles) :"));
		reglageLabels.add(new JLabel("Intervalle entre chaque prise (de 0 à 25 cycles) :"));
		reglageLabels.add(new JLabel("Cycle à partir duquel commence le traitement (de 0 à 200) :"));

		// Les champs de saisie de donnees
		JPanel reglageSaisie = new JPanel(new GridLayout(5, 1, 5, 5));
		reglageSaisie.add(this.saisieDureeCulture);
		reglageSaisie.add(this.saisieNombre);
		reglageSaisie.add(this.saisieDureePrise);
		reglageSaisie.add(this.saisieIntervalle);
		reglageSaisie.add(this.saisieDebut);

		// ActionListeners des differents parametres
		this.saisieDureeCulture.addActionListener(new JComboBoxActionListenerAdapter<Integer>() {

			@Override
			public void actionPerformed(Integer value) {
				parametres.setDureeCulture(value.intValue());
			}
		});

		this.saisieNombre.addActionListener(new JComboBoxActionListenerAdapter<Integer>() {

			@Override
			public void actionPerformed(Integer value) {
				parametres.setNbPrises(value.intValue());
			}
		});
		this.saisieDureePrise.addActionListener(new JComboBoxActionListenerAdapter<Integer>() {

			@Override
			public void actionPerformed(Integer value) {
				parametres.setDureePrise(value.intValue());
			}
		});
		this.saisieIntervalle.addActionListener(new JComboBoxActionListenerAdapter<Integer>() {

			@Override
			public void actionPerformed(Integer value) {
				parametres.setIntervalle(value.intValue());
			}
		});

		this.saisieDebut.addActionListener(new JComboBoxActionListenerAdapter<Integer>() {

			@Override
			public void actionPerformed(Integer value) {
				parametres.setDebutPrises(value.intValue());

			}
		});

		// Organisation graphique du panneau d'interface utilisateur
		setLayout(new BorderLayout(5, 5));
		add(reglageLabels, BorderLayout.WEST);
		add(reglageSaisie, BorderLayout.CENTER);
	}

	public Parametres getParametres() { // Methode permettant l'acces aux parametres pour les autres classes
		return parametres;
	}
}