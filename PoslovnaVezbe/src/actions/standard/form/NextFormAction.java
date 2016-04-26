package actions.standard.form;

import gui.standard.form.DrzavaStandardForm;
import gui.standard.form.NaseljenoMestoStandardForm;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;


public class NextFormAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private JDialog standardForm;
	
	public NextFormAction(JDialog standardForm) {
		putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/img/nextform.gif")));
		putValue(SHORT_DESCRIPTION, "Sledeća forma");
		this.standardForm  = standardForm;
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(standardForm instanceof DrzavaStandardForm){
			((DrzavaStandardForm) standardForm).otvoriNaseljenoMesto();
			NaseljenoMestoStandardForm nsms = new NaseljenoMestoStandardForm(((DrzavaStandardForm) standardForm).getNextCl());
			nsms.getTfNazivDrzave().setEditable(false);
			nsms.getTfSifraDrzave().setEditable(false);
			nsms.getTfNazivDrzave().setText(((DrzavaStandardForm) standardForm).getNextCl().getColumn("DR_NAZIV").getValue().toString());
			nsms.getTfSifraDrzave().setText(((DrzavaStandardForm) standardForm).getNextCl().getColumn("drzava.dr_sifra").getValue().toString());
			nsms.setVisible(true);
		}
	}
}
