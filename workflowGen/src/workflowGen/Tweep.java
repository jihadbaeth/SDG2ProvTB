package workflowGen;

public class Tweep {
	public String username ="";
	public String name="";
	public String bio="";
	public double credibility =0;
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
	public double getCredibility() {
		return credibility;
	}
	public void setCredibility(double credibility) {
		this.credibility = credibility;
	}
	@Override
	public String toString() {
		return "Tweep [username=" + username + ", name=" + name + ", bio=" + bio + ", credibility=" + credibility + "]";
	}



}
