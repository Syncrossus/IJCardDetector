package tools;

public class Match implements Comparable<Match>{
	
	private double value;
	private String title;
	
	public Match(double value, String title){
		this.value = value;
		this.title = title;
		
	}
	
	public Match(){
		this(0., "");
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title){
		this.title = title;
	}

	@Override
	public int compareTo(Match other){
		return (int)(this.value - other.getValue());
	}
	
	public boolean equals(String title){
		return title.equals(title);
	}
}
