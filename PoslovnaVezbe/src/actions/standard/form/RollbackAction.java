
package actions.standard.form;

import gui.standard.form.DrzavaStandardForm;
import gui.standard.form.NaseljenoMestoStandardForm;

import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

public class RollbackAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private JDialog standardForm;

	public RollbackAction(JDialog standardForm) {
		putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/img/remove.gif")));
		putValue(SHORT_DESCRIPTION, "Poništi");
		this.standardForm=standardForm;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(standardForm instanceof DrzavaStandardForm){
			try {
				if(((DrzavaStandardForm) standardForm).getMode() != DrzavaStandardForm.MODE_EDIT){
					((DrzavaStandardForm) standardForm).setMode(((DrzavaStandardForm) standardForm).getMode());
					((DrzavaStandardForm) standardForm).getTableModel().open();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if(standardForm instanceof NaseljenoMestoStandardForm){
			try {
				if(((NaseljenoMestoStandardForm) standardForm).getMode() != NaseljenoMestoStandardForm.MODE_EDIT){
					((NaseljenoMestoStandardForm) standardForm).setMode(((NaseljenoMestoStandardForm) standardForm).getMode());
					((NaseljenoMestoStandardForm) standardForm).getTableModel().open();
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
