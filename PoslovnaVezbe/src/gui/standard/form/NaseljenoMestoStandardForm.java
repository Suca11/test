package gui.standard.form;

import gui.main.form.MainFrame;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;
import table.model.DrzaveTableModel;
import table.model.NaseljenoMestoTableModel;
import util.ColumnList;
import util.Lookup;
import actions.standard.form.AddAction;
import actions.standard.form.CommitAction;
import actions.standard.form.DeleteAction;
import actions.standard.form.FirstAction;
import actions.standard.form.HelpAction;
import actions.standard.form.LastAction;
import actions.standard.form.NextAction;
import actions.standard.form.NextFormAction;
import actions.standard.form.PickupAction;
import actions.standard.form.PreviousAction;
import actions.standard.form.RefreshAction;
import actions.standard.form.RollbackAction;
import actions.standard.form.SearchAction;

public class NaseljenoMestoStandardForm extends JDialog{
	private static final long serialVersionUID = 1L;


	public static final int MODE_EDIT   = 1;
	public static final int MODE_ADD    = 2;
	public static final int MODE_SEARCH = 3;
	private int mode;
	int selectIndex;

	private JToolBar toolBar;
	private JButton btnAdd, btnCommit, btnDelete, btnFirst, btnLast, btnHelp, btnNext, btnNextForm,
	btnPickup, btnRefresh, btnRollback, btnSearch, btnPrevious;
	private JTable tblGrid = new JTable();
	private JTextField tfSifra = new JTextField(5);
	private JTextField tfNaziv = new JTextField(20);
	NaseljenoMestoTableModel tableModel;
	

	public NaseljenoMestoTableModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(NaseljenoMestoTableModel tableModel) {
		this.tableModel = tableModel;
	}

	public int getMode() {
		return mode;
	}

	private JTextField tfNazivDrzave = new JTextField(20);
	public JTextField getTfNazivDrzave() {
		return tfNazivDrzave;
	}

	public void setTfNazivDrzave(JTextField tfNazivDrzave) {
		this.tfNazivDrzave = tfNazivDrzave;
	}

	public JTextField getTfSifraDrzave() {
		return tfSifraDrzave;
	}

	public void setTfSifraDrzave(JTextField tfSifraDrzave) {
		this.tfSifraDrzave = tfSifraDrzave;
	}

	private JTextField tfSifraDrzave = new JTextField(5);

	private JButton btnZoom = new JButton("...");


	private ColumnList cl;
	
	public ColumnList getCl() {
		return cl;
	}

	public void setCl(ColumnList cl) {
		this.cl = cl;
	}

	public NaseljenoMestoStandardForm(ColumnList cl){

		setLayout(new MigLayout("fill"));
		
		setSize(new Dimension(800, 600));
		setTitle("Naseljena mesta");
		setLocationRelativeTo(MainFrame.getInstance());
		setModal(true);

		mode = MODE_EDIT;
		this.cl = cl;
		initToolbar();
		initTable();
		initGui();

	}
	
	private void initGui(){
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new MigLayout("fillx"));
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new MigLayout());

		JPanel buttonsPanel = new JPanel();
		btnCommit = new JButton(new CommitAction(this));
		btnRollback = new JButton(new RollbackAction(this));


		JLabel lblSifra = new JLabel ("Šifra mesta:");
		JLabel lblNaziv = new JLabel("Naziv mesta:");
		JLabel lblSifraDrzave = new JLabel ("Šifra države:");

		dataPanel.add(lblSifra);
		dataPanel.add(tfSifra,"wrap, gapx 15px");
		dataPanel.add(lblNaziv);
		dataPanel.add(tfNaziv,"wrap,gapx 15px, span 3");
		dataPanel.add(lblSifraDrzave);
		dataPanel.add(tfSifraDrzave, "gapx 15px");
		dataPanel.add(btnZoom);

		dataPanel.add(tfNazivDrzave,"pushx");
		tfNazivDrzave.setEditable(false);
		bottomPanel.add(dataPanel);

		buttonsPanel.setLayout(new MigLayout("wrap"));
		buttonsPanel.add(btnCommit);
		buttonsPanel.add(btnRollback);
		bottomPanel.add(buttonsPanel,"dock east");

		add(bottomPanel, "grow, wrap");
	
	}
	
	private void initToolbar(){
		toolBar = new JToolBar();
		btnSearch = new JButton(new SearchAction(this));
		toolBar.add(btnSearch);


		btnRefresh = new JButton(new RefreshAction());
		toolBar.add(btnRefresh);

		btnPickup = new JButton(new PickupAction(this));
		toolBar.add(btnPickup);


		btnHelp = new JButton(new HelpAction());
		toolBar.add(btnHelp);

		toolBar.addSeparator();

		btnFirst = new JButton(new FirstAction(this));
		toolBar.add(btnFirst);

		btnPrevious = new JButton(new PreviousAction(this));
		toolBar.add(btnPrevious);

		btnNext = new JButton(new NextAction(this));
		toolBar.add(btnNext);

		btnLast = new JButton(new LastAction(this));
		toolBar.add(btnLast);

		toolBar.addSeparator();


		btnAdd = new JButton(new AddAction(this));
		toolBar.add(btnAdd);

		btnDelete = new JButton(new DeleteAction(this));
		toolBar.add(btnDelete);

		toolBar.addSeparator();

		btnNextForm = new JButton(new NextFormAction(this));
		toolBar.add(btnNextForm);
		
		btnPickup.setEnabled(false);
		add(toolBar, "dock north");
	}


	private void initTable(){
	
		JScrollPane scrollPane = new JScrollPane(tblGrid);      
		add(scrollPane, "grow, wrap");
		tableModel = new NaseljenoMestoTableModel(new String[] {"Šifra",   "Naziv", "Šifra države", "Naziv države"}, 0);
		tblGrid.setModel(tableModel);

		try {
			if(cl == null){
				tableModel.open();
			}else{
				tableModel.open2(cl.getWhereClause());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 

		//Dozvoljeno selektovanje redova
		tblGrid.setRowSelectionAllowed(true);
		//Ali ne i selektovanje kolona 
		tblGrid.setColumnSelectionAllowed(false);

		//Dozvoljeno selektovanje samo jednog reda u jedinici vremena 
		tblGrid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		tblGrid.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(tblGrid.getSelectedRow() != -1){
					setMode(MODE_EDIT);
				}
				if (e.getValueIsAdjusting())
					return;
				sync();
			}
		});
		
		tfSifraDrzave.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
			String sifraDrzave = tfSifraDrzave.getText().trim();
				try {
					tfNazivDrzave.setText(Lookup.getDrzava(sifraDrzave));
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		btnZoom.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				DrzavaStandardForm dsf = null;
				try {
					dsf = new DrzavaStandardForm();
					dsf.setVisible(true);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if(dsf.getCl() != null){
					tfSifraDrzave.setText((String) dsf.getCl().getValue("DR_SIFRA"));
					tfNazivDrzave.setText((String) dsf.getCl().getValue("DR_NAZIV"));
				}
			}
		});

	}
	
	public void goLast() {
	    int rowCount = tblGrid.getModel().getRowCount(); 
	    if (rowCount > 0)
	      tblGrid.setRowSelectionInterval(rowCount - 1, rowCount - 1);
	}
	
	public void goFirst(){
		tblGrid.setRowSelectionInterval(0, 0);
	}
	
	public void goNext(){
		int rowCount = tblGrid.getModel().getRowCount();
		int selektovan = tblGrid.getSelectedRow();
		if(selektovan + 1 < rowCount ){
			tblGrid.setRowSelectionInterval(selektovan + 1, selektovan + 1);
		} else {
			tblGrid.setRowSelectionInterval(0,0);
		}
	}
	
	public void goPrevious(){
		int rowCount = tblGrid.getModel().getRowCount();
		int seloktovan = tblGrid.getSelectedRow();
		
		if(seloktovan - 1 >= 0){
			tblGrid.setRowSelectionInterval(seloktovan-1, seloktovan-1);
		}else {
			tblGrid.setRowSelectionInterval(rowCount - 1,rowCount - 1);
		}
	}
	
	public void addRow() {       
		String sifra = tfSifra.getText().trim();
		String naziv = tfNaziv.getText().trim();
		String drSifra = tfSifraDrzave.getText().trim();
		String drNaziv = tfNazivDrzave.getText().trim();
		try {
			NaseljenoMestoTableModel dtm = (NaseljenoMestoTableModel)tblGrid.getModel(); 
			int index = dtm.insertRow(sifra, naziv, drSifra, drNaziv);
			tblGrid.setRowSelectionInterval(index, index);
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(),"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void editRow(){
		String sifra = tfSifra.getText().trim();
		String naziv = tfNaziv.getText().trim();
		try {
			NaseljenoMestoTableModel dtm = (NaseljenoMestoTableModel)tblGrid.getModel();
			dtm.editRow(sifra, naziv, selectIndex);
			tblGrid.setRowSelectionInterval(selectIndex, selectIndex);
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(),"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public JTextField getTfSifra() {
		return tfSifra;
	}

	public void setTfSifra(JTextField tfSifra) {
		this.tfSifra = tfSifra;
	}

	public JTextField getTfNaziv() {
		return tfNaziv;
	}

	public void setTfNaziv(JTextField tfNaziv) {
		this.tfNaziv = tfNaziv;
	}

	public void removeRow() {
	    int index = tblGrid.getSelectedRow(); 
	    if (index == -1) //Ako nema selektovanog reda (tabela prazna)
	    	return;        // izlazak 
	    //kada obrisemo tekuci red, selektovacemo sledeci (newindex):
	    int newIndex = index;  
	    //sem ako se obrise poslednji red, tada selektujemo prethodni
	    if (index == tableModel.getRowCount() - 1) 
	    	newIndex--; 
	    try {
	    	NaseljenoMestoTableModel dtm = (NaseljenoMestoTableModel)tblGrid.getModel(); 
	      	dtm.deleteRow(index); 
	      	if (tableModel.getRowCount() > 0)
	      		tblGrid.setRowSelectionInterval(newIndex, newIndex);
	    } catch (SQLException ex) {
	    	JOptionPane.showMessageDialog(this, ex.getMessage(), "Greska", 
	    	JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	public void pretraga() throws SQLException{
		String sifra = tfSifra.getText();
		String naziv = tfNaziv.getText();
		String drSifra = tfSifraDrzave.getText();
		tableModel.open3(sifra, naziv, drSifra);
	}
	
	public void setMode(int m){
		if(m == MODE_ADD){
			tblGrid.getSelectionModel().clearSelection();
			mode = m;
			tfSifra.setEditable(true);
			tfSifra.setText("");
			tfNaziv.setText("");
			tfNazivDrzave.setText("");
			tfSifraDrzave.setText("");
			tfSifraDrzave.setEditable(true);
			btnZoom.setEnabled(true);
			tfSifra.setRequestFocusEnabled(true);
		}else if(m == MODE_SEARCH){
			tblGrid.getSelectionModel().clearSelection();
			mode = m;
			tfSifra.setEditable(true);
			tfSifra.setText("");
			tfNaziv.setText("");
			tfNazivDrzave.setText("");
			tfSifraDrzave.setText("");
			btnZoom.setEnabled(true);
			tfSifraDrzave.setEditable(true);
			tfSifra.setRequestFocusEnabled(true);
		}else if(m == MODE_EDIT){
			if(tblGrid.getSelectedRow() != -1){
				selectIndex = tblGrid.getSelectedRow();
			}
			mode = m;
			tfSifra.setText((String)tableModel.getValueAt(selectIndex, 0));
			tfSifra.setEditable(false);
			tfNaziv.setText((String)tableModel.getValueAt(selectIndex, 1));
			tfSifraDrzave.setEditable(false);
			tfSifraDrzave.setText((String)tableModel.getValueAt(selectIndex, 2));
			tfNazivDrzave.setEditable(false);
			tfNazivDrzave.setText((String)tableModel.getValueAt(selectIndex, 3));
			btnZoom.setEnabled(false);
		}
	}

	private void sync() {
		int index = tblGrid.getSelectedRow();
		if (index < 0) {
			tfSifra.setText("");
			tfNaziv.setText("");
			return;
		}
		String sifra = (String)tblGrid.getModel().getValueAt(index, 0);
		String naziv = (String)tblGrid.getModel().getValueAt(index, 1);
		String sifraDrzave = (String)tblGrid.getModel().getValueAt(index, 2);
		String nazivDrzave = (String)tblGrid.getModel().getValueAt(index, 3);
		tfSifra.setText(sifra);
		tfNaziv.setText(naziv);
		tfSifraDrzave.setText(sifraDrzave);
		tfNazivDrzave.setText(nazivDrzave);
	}


}

