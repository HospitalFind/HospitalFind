package br.com.hospitalfind.Model;

public class PlaceDetail {

	
	public String name;
	@Override
	public String toString() {
		return "Place [name=" + name + ", placeId=" + placeId + ", lat=" + lat
			+ ", lon=" + lon + ", ratings=" + ratings + ", token="+page+"]";
	}
	
	public String placeId;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public int getRatings() {
		return ratings;
	}
	public void setRatings(int ratings) {
		this.ratings = ratings;
	}
	
	public String page;
	public String lat;
	public String lon;
	public int ratings;
	
}
