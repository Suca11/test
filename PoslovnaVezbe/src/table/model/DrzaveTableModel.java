package table.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.table.DefaultTableModel;

import util.ColumnList;
import util.SortUtils;
import database.DBConnection;

public class DrzaveTableModel extends DefaultTableModel {

	private String basicQuery = "SELECT dr_sifra, dr_naziv FROM drzava";
	private String orderBy = " ORDER BY dr_sifra";
	private String whereStmt = "";
	private static final int CUSTOM_ERROR_CODE = 50000;
	private static final String ERROR_RECORD_WAS_CHANGED = "Slog je promenjen od strane drugog korisnika. Molim vas, pogledajte njegovu trenutnu vrednost";
	private static final String ERROR_RECORD_WAS_DELETED = "Slog je obrisan od strane drugog korisnika";

	public DrzaveTableModel(Object[] colNames, int rowCount) {
		super(colNames, rowCount);
	}

	//Otvaranje upita
	public void open() throws SQLException {
		fillData(basicQuery + whereStmt + orderBy);
	}
	
	public void open2(String sifra, String naziv) throws SQLException{
		fillData(basicQuery + " WHERE DR_SIFRA = " + "'"+sifra+"'" + " OR DR_NAZIV = " + "'"+naziv+"'");
	}

	//Popunjavanje matrice podacima
	private void fillData(String sql) throws SQLException {
		setRowCount(0);
		Statement stmt = DBConnection.getConnection().createStatement();
		ResultSet rset = stmt.executeQuery(sql);

		while (rset.next()) {
			String sifra = rset.getString("DR_SIFRA");
			String naziv = rset.getString("DR_NAZIV");
			addRow(new String[]{sifra, naziv});
		}

		rset.close();
		stmt.close();
		fireTableDataChanged();
	}
	
	public void deleteRow(int index) throws SQLException {
	    checkRow(index);
		PreparedStatement stmt = DBConnection.getConnection().prepareStatement(
	        "DELETE FROM drzava WHERE dr_sifra=?");
	    String sifra = (String)getValueAt(index, 0);
	    stmt.setString(1, sifra);
	    //Brisanje iz baze 
	    int rowsAffected = stmt.executeUpdate();
	    stmt.close();
	    DBConnection.getConnection().commit();
	    if (rowsAffected > 0) {
	      // i brisanje iz TableModel-a
	      removeRow(index);
		  fireTableDataChanged();
	    }
	}
	
	private int sortedInsert(String sifra, String naziv) { 
		int left = 0;
		int right = getRowCount() - 1;   
		int mid = (left + right) / 2;
		while (left <= right ) {      
			mid = (left + right) / 2;
			String aSifra = (String)getValueAt(mid, 0);      
			if (SortUtils.getLatCyrCollator().compare(sifra, aSifra) > 0) 
				left = mid + 1;
			else if (SortUtils.getLatCyrCollator().compare(sifra, aSifra) < 0)
				right = mid - 1;
			else 
	        // ako su jednaki: to ne moze da se desi ako tabela ima primarni kljuc
				break;      
	    }
	    insertRow(left, new String[] {sifra, naziv});
	    return left;
	}
	
	public int insertRow(String sifra, String naziv) throws SQLException {
	    int retVal = 0;
	    PreparedStatement stmt = DBConnection.getConnection().prepareStatement(
	      "INSERT INTO drzava (dr_sifra, dr_naziv) VALUES (? ,?)");
	    stmt.setString(1, sifra);
	    stmt.setString(2, naziv);
	    int rowsAffected = stmt.executeUpdate();
	    stmt.close();
	    //Unos sloga u bazu
	    DBConnection.getConnection().commit();
	    if (rowsAffected > 0) {
	      // i unos u TableModel  
	    	retVal = sortedInsert(sifra, naziv);
	    	fireTableDataChanged();
	    }
	    return retVal;
	}
	
	public ColumnList odaberiDrzavu(){
		return null;
		
	}
		
	private void checkRow(int index) throws SQLException {

			DBConnection.getConnection().
				setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
			PreparedStatement selectStmt = DBConnection.getConnection().prepareStatement(basicQuery +
					" where DR_SIFRA =?");

			String sifra = (String)getValueAt(index, 0);
			selectStmt.setString(1, sifra);

			ResultSet rset = selectStmt.executeQuery();

			String sifraDr = "", naziv = "";
			Boolean postoji = false;
			String errorMsg = "";
			while (rset.next()) {
				sifraDr = rset.getString("DR_SIFRA").trim();
				naziv = rset.getString("DR_NAZIV");
				postoji = true;
			}
			if (!postoji) {
				removeRow(index);
				fireTableDataChanged();
				errorMsg = ERROR_RECORD_WAS_DELETED;
			}
			else if ((SortUtils.getLatCyrCollator().compare(sifraDr, ((String)getValueAt(index, 0)).trim()) != 0) ||
					(SortUtils.getLatCyrCollator().compare(naziv, (String)getValueAt(index, 1)) != 0))  {
				setValueAt(sifraDr, index, 0);
				setValueAt(naziv, index, 1);
				fireTableDataChanged();
				errorMsg = ERROR_RECORD_WAS_CHANGED;
			}
			rset.close();
			selectStmt.close();
			DBConnection.getConnection().setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			if (errorMsg != "") {
				DBConnection.getConnection().commit();
				throw new SQLException(errorMsg, "", CUSTOM_ERROR_CODE);
			}
		}
	
	public void editRow(String sifra, String naziv, int row) throws SQLException {
		checkRow(row);
		PreparedStatement stmt = DBConnection.getConnection().prepareStatement("UPDATE DRZAVA SET DR_NAZIV = ? WHERE DR_SIFRA = ?");
		stmt.setString(2, sifra);
		stmt.setString(1, naziv);
		stmt.executeUpdate();
		stmt.close();
		DBConnection.getConnection().commit();
		setValueAt(naziv, row, 1);
		fireTableDataChanged();
	}
}