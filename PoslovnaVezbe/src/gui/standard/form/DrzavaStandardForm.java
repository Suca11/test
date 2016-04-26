package gui.standard.form;

import gui.main.form.MainFrame;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

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

import table.model.DrzaveTableModel;
import util.Column;
import util.ColumnList;
import net.miginfocom.swing.MigLayout;
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

public class DrzavaStandardForm extends JDialog{
	private static final long serialVersionUID = 1L;
	
	private JToolBar toolBar;
	private JButton btnAdd, btnCommit, btnDelete, btnFirst, btnLast, btnHelp, btnNext, btnNextForm,
	btnPickup, btnRefresh, btnRollback, btnSearch, btnPrevious;
	private JTextField tfSifra = new JTextField(5);
	private JTextField tfNaziv = new JTextField(20);
	private JTable tblGrid = new JTable(); 
	DrzaveTableModel tableModel;
	private int selectIndex;
	
	public static final int MODE_EDIT = 1;
	public static final int MODE_ADD = 2;
	public static final int MODE_SEARCH = 3;
	private int mode;
	
	private ColumnList cl;
	private ColumnList nextCl;

	public ColumnList getNextCl() {
		return nextCl;
	}

	public void setNextCl(ColumnList nextCl) {
		this.nextCl = nextCl;
	}

	public ColumnList getCl() {
		return cl;
	}

	public void setCl(ColumnList cl) {
		this.cl = cl;
	}

	public DrzavaStandardForm() throws SQLException{

		setLayout(new MigLayout("fill"));

		setSize(new Dimension(800, 600));
		setTitle("Države");
		setLocationRelativeTo(MainFrame.getInstance());
		setModal(true);
		
		initToolbar();
		initTable();
		initGui();
		
	}
	
	private void initTable() throws SQLException{
		JScrollPane scrollPane = new JScrollPane(tblGrid);
		add(scrollPane, "grow, wrap");
		// Kreiranje TableModel-a, parametri: header-i kolona i broj redova

		tableModel = new DrzaveTableModel(new String[] {"Šifra",
		"Naziv"}, 0);
		tblGrid.setModel(tableModel);
		tableModel.open();
		//Dozvoljeno selektovanje redova
		tblGrid.setRowSelectionAllowed(true);
		//Ali ne i selektovanje kolona
		tblGrid.setColumnSelectionAllowed(false);
		//Dozvoljeno selektovanje samo jednog reda u jedinici vremena
		tblGrid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
		
		
	}
	
	public DrzaveTableModel getTableModel() {
		return tableModel;
	}

	public void setTableModel(DrzaveTableModel tableModel) {
		this.tableModel = tableModel;
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
				
		add(toolBar, "dock north");
	}
	
	private void initGui(){
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new MigLayout("fillx"));
		JPanel dataPanel = new JPanel();
		dataPanel.setLayout(new MigLayout("gapx 15px"));

		JPanel buttonsPanel = new JPanel();
		btnCommit = new JButton(new CommitAction(this));
		btnRollback = new JButton(new RollbackAction(this));


		JLabel lblSifra = new JLabel ("Šifra države:");
		JLabel lblNaziv = new JLabel("Naziv države:");

		dataPanel.add(lblSifra);
		dataPanel.add(tfSifra,"wrap");
		dataPanel.add(lblNaziv);
		dataPanel.add(tfNaziv);
		bottomPanel.add(dataPanel);


		buttonsPanel.setLayout(new MigLayout("wrap"));
		buttonsPanel.add(btnCommit);
		buttonsPanel.add(btnRollback);
		bottomPanel.add(buttonsPanel,"dock east");

		add(bottomPanel, "grow, wrap");
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
	
	public void pretraga() throws SQLException{
		String sifra = tfSifra.getText();
		String naziv = tfNaziv.getText();
		tableModel.open2(sifra, naziv);
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
	    	DrzaveTableModel dtm = (DrzaveTableModel)tblGrid.getModel(); 
	      	dtm.deleteRow(index); 
	      	if (tableModel.getRowCount() > 0)
	      		tblGrid.setRowSelectionInterval(newIndex, newIndex);
	    } catch (SQLException ex) {
	    	JOptionPane.showMessageDialog(this, ex.getMessage(), "Greska", 
	    	JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	public void addRow() {       
		String sifra = tfSifra.getText().trim();
		String naziv = tfNaziv.getText().trim();
		try {
			DrzaveTableModel dtm = (DrzaveTableModel)tblGrid.getModel(); 
			int index = dtm.insertRow(sifra, naziv);
			tblGrid.setRowSelectionInterval(index, index);
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

	public void odaberiDrzavuZaNaselje(){
		DrzaveTableModel dtm = (DrzaveTableModel)tblGrid.getModel();
		int index = tblGrid.getSelectedRow();
		Column c1 = new Column("DR_SIFRA", dtm.getValueAt(index, 0));
		Column c2 = new Column("DR_NAZIV", dtm.getValueAt(index, 1));
		cl = new ColumnList();
		cl.add(c1);
		cl.add(c2);
	}
	
	public void otvoriNaseljenoMesto(){
		DrzaveTableModel dtm = (DrzaveTableModel)tblGrid.getModel();
		int index = tblGrid.getSelectedRow();
		Column c1 = new Column("drzava.dr_sifra", dtm.getValueAt(index, 0));
		Column c2 = new Column("DR_NAZIV", dtm.getValueAt(index, 1));
		nextCl = new ColumnList();
		nextCl.add(c1);
		nextCl.add(c2);
	}
	
	
	
	public void editRow(){
		String sifra = tfSifra.getText().trim();
		String naziv = tfNaziv.getText().trim();
		try {
			DrzaveTableModel dtm = (DrzaveTableModel)tblGrid.getModel();
			dtm.editRow(sifra, naziv, selectIndex);
			tblGrid.setRowSelectionInterval(selectIndex, selectIndex);
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(),"Greska", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void setMode(int m){
		if(m == MODE_ADD){
			tblGrid.getSelectionModel().clearSelection();
			mode = m;
			tfSifra.setEditable(true);
			tfSifra.setText("");
			tfNaziv.setText("");
			tfSifra.setRequestFocusEnabled(true);
		}else if(m == MODE_SEARCH){
			tblGrid.getSelectionModel().clearSelection();
			mode = m;
			tfSifra.setEditable(true);
			tfSifra.setText("");
			tfNaziv.setText("");
			tfSifra.setRequestFocusEnabled(true);
		}else if(m == MODE_EDIT){
			if(tblGrid.getSelectedRow() != -1){
				selectIndex = tblGrid.getSelectedRow();
			}
			mode = m;
			tfSifra.setText((String)tableModel.getValueAt(selectIndex, 0));
			tfSifra.setEditable(false);
			tfNaziv.setText((String)tableModel.getValueAt(selectIndex, 1));
		}
	}
	
	public int getMode() {
		return mode;
	}

	private void sync() {
		//setMode(MODE_EDIT);
		int index = tblGrid.getSelectedRow();
		if (index < 0) {
			tfSifra.setText("");
		    tfNaziv.setText("");
		    return;
		}
		String sifra = (String)tableModel.getValueAt(index, 0);
		String naziv = (String)tableModel.getValueAt(index, 1);
		tfSifra.setText(sifra);
		tfNaziv.setText(naziv);
	 }
}
