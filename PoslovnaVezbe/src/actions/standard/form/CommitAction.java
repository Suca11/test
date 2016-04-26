package actions.standard.form;


import gui.standard.form.DrzavaStandardForm;
import gui.standard.form.NaseljenoMestoStandardForm;

import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;



public class CommitAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	private JDialog standardForm;
	
	public CommitAction(JDialog standardForm) {
		putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/img/commit.gif")));
		putValue(SHORT_DESCRIPTION, "Commit");
		this.standardForm=standardForm;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(standardForm instanceof DrzavaStandardForm){
			int mode = ((DrzavaStandardForm)standardForm).getMode();
			
			if(mode == ((DrzavaStandardForm)standardForm).MODE_EDIT){
				((DrzavaStandardForm)standardForm).editRow();
			}else if(mode == ((DrzavaStandardForm)standardForm).MODE_ADD){
				((DrzavaStandardForm)standardForm).addRow();
				((DrzavaStandardForm)standardForm).getTfSifra().grabFocus();
			}else if(mode == ((DrzavaStandardForm)standardForm).MODE_SEARCH){
				try {
					((DrzavaStandardForm)standardForm).pretraga();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}else if(standardForm instanceof NaseljenoMestoStandardForm){
			int mode = ((NaseljenoMestoStandardForm)standardForm).getMode();
			
			if(mode == ((NaseljenoMestoStandardForm)standardForm).MODE_EDIT){
				((NaseljenoMestoStandardForm)standardForm).editRow();
			}else if(mode == ((NaseljenoMestoStandardForm)standardForm).MODE_ADD){
				((NaseljenoMestoStandardForm)standardForm).addRow();
				((NaseljenoMestoStandardForm)standardForm).getTfSifra().grabFocus();
			}else if(mode == ((NaseljenoMestoStandardForm)standardForm).MODE_SEARCH){
				try {
					((NaseljenoMestoStandardForm)standardForm).pretraga();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
}

