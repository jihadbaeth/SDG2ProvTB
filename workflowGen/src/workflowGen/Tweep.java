package workflowGen;

public class Tweep {
    private String username = "";
    private String name = "";
    private String bio = "";
    private double popularity = 0;
    private double legitimacy = 0;
    private double availability = 0;
    private Tweep[] followingList = null;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public double getPopularity() {
		return popularity;
	}
	public void setPopularity(double popularity) {
		this.popularity = popularity;
	}
	@Override
	public String toString() {
		return "Tweep [username=" + username + ", name=" + name + ", bio=" + bio + ", popularity=" + popularity +", legitimacy="+ legitimacy + ", availability=" + availability + "]";
	}
	public double getLegitimacy() {
		return legitimacy;
	}
	public void setLegitimacy(double legitimacy) {
		this.legitimacy = legitimacy;
	}
	public double getAvailability() {
		return availability;
	}
	public void setAvailability(double availability) {
		this.availability = availability;
	}
	public Tweep[] getFollowingList() {
		return followingList;
	}
	public void setFollowingList(Tweep[] followingList) {
		this.followingList = followingList;
	}


}
