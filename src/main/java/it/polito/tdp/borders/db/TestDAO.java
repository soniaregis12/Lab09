package it.polito.tdp.borders.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Border;

public class TestDAO {

	public static void main(String[] args) {

		BordersDAO dao = new BordersDAO();
		
		Map<Integer, Country> mappa = new HashMap<Integer, Country>();

		System.out.println("Lista di tutte le nazioni:");
		dao.loadAllCountries(mappa, 1816);
		
		System.out.println("----------------");
		System.out.println("Confinanti con quello stato:");
		dao.getCountryPairs(mappa, new Country("ITA", 325, "Italy"), 1816);
	}
}
