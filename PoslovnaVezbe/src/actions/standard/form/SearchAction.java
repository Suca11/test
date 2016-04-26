package actions.standard.form;

import gui.standard.form.DrzavaStandardForm;
import gui.standard.form.NaseljenoMestoStandardForm;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

public class SearchAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private JDialog standardForm;

	public SearchAction(JDialog standardForm) {
		putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/img/search.gif")));
		putValue(SHORT_DESCRIPTION, "Pretraga");
		this.standardForm=standardForm;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(standardForm instanceof DrzavaStandardForm){
			((DrzavaStandardForm)standardForm).setMode(DrzavaStandardForm.MODE_SEARCH);
			((DrzavaStandardForm)standardForm).getTfSifra().grabFocus();
		}else if(standardForm instanceof NaseljenoMestoStandardForm){
			((NaseljenoMestoStandardForm)standardForm).setMode(NaseljenoMestoStandardForm.MODE_SEARCH);
			((NaseljenoMestoStandardForm)standardForm).getTfSifra().grabFocus();
		}
	}
}
