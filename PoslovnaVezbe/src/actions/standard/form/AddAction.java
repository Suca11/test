package actions.standard.form;

import gui.standard.form.DrzavaStandardForm;
import gui.standard.form.NaseljenoMestoStandardForm;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;



public class AddAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	//kada se napravi genericka forma, staviti tu klasu umesto JDialog
	private JDialog standardForm;
	
	public AddAction(JDialog standardForm) {
		putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/img/add.gif")));
		putValue(SHORT_DESCRIPTION, "Dodavanje");
		this.standardForm=standardForm;
	}

	/*
	 * PROBA
	 * 
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//((DrzavaStandardForm)standardForm).getTfNaziv().setText("");
		//((DrzavaStandardForm)standardForm).getTfSifra().setText("");
		if(standardForm instanceof DrzavaStandardForm){
			((DrzavaStandardForm)standardForm).setMode(DrzavaStandardForm.MODE_ADD);
			((DrzavaStandardForm)standardForm).getTfSifra().grabFocus();
		}else if(standardForm instanceof NaseljenoMestoStandardForm){
			((NaseljenoMestoStandardForm) standardForm).setMode(NaseljenoMestoStandardForm.MODE_ADD);
			((NaseljenoMestoStandardForm) standardForm).getTfSifra().grabFocus();
		}
	}
}
