package culturecellulaire;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

/*
 * Auteur : Olivier THOMAS
 */

public abstract class JComboBoxActionListenerAdapter<T> implements ActionListener {

	@Override // Commentaire validant l'ecrasement de la methode mère
	@SuppressWarnings("unchecked") // Commentaire faisant taire l'avertissement
	public void actionPerformed(ActionEvent e) { // Methode permettant a la ComboBox d'accepter un entier comme parametre
		if (e.getSource() instanceof JComboBox<?>) {
			JComboBox<T> box = (JComboBox<T>) e.getSource();
			actionPerformed((T)box.getSelectedItem());
		}
	}

	public abstract void actionPerformed(T value);

}
