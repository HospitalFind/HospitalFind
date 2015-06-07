package br.com.hospitalfind.resource;

import br.com.hospitalfind.Model.PlaceDetail;
import br.com.manzini.services.PlacesService;

public class GooglePlacesResources {

	public static PlaceDetail[] buscar(String sLat, String sLng, String radius,
			String pg) {
		PlaceDetail[] tst = PlacesService.search(sLat, sLng, radius, pg);
		return tst;
	}
}