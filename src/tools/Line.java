package tools;

import java.awt.Point;

public class Line {
	private double rho;
	private double theta;
	private double xIntercept;
	private double slope;
	private double yIntercept;
	private Point[] points = new Point[2];
	
	public Line(double rho, double theta){
		this.rho = rho;
		this.theta = theta;
		this.init();
	}

	/**
	 * @return true if the line is vertical
	 */
	public boolean isVertical(){
		return (Math.sin(theta)==0);
	}
	
	
	/**
	 * Inits the value of verticalStep.
	 * Allocate rho*cos(theta) is the line is vertical, 0 otherwise.
	 */
	private void init(){
		if(this.isVertical()){
			this.xIntercept = (int)(rho*Math.cos(theta));
		}
		else{
			slope = -Math.cos(theta)/Math.sin(theta);
			yIntercept =  rho/Math.sin(theta);
		}
	}
	
	/**
	 * 
	 */
	public void setCorners(Point[] points){
		this.points = points;
	}
	
	public void addCorner(Point corner){
		if(points[0] == null){
			points[0] = corner;
		}
		else if(points[1] == null){
			points[1] = corner;
		}
		else
			throw new RuntimeException("Les coordonnées de la droite ont déja été fixées");
	}
	
	public Point[] getCorners(){
		return points;
	}
	
	public double getLength(){
		return Math.sqrt(Math.pow(points[0].getX() - points[1].getX(), 2) + Math.pow(points[0].getY() - points[1].getY(), 2));	
	}
	
	/**
	 * Returns the y value for the given x if the line is not vertical
	 * @throw ArithmeticException is the line is vertical
	 * @param x
	 */
	public int getY(int x){
		if (!this.isVertical()){
			return (int) (x*this.slope + this.yIntercept);
		}
		else{
			throw new ArithmeticException("Ligne verticale " + this);
		}
	}
	
	/**
	 * @return the value of the xIntercept attribrute
	 */
	public double getXIntercept(){
		return this.xIntercept;
	}
	
	/**
	 * @return the value of the slope attribrute
	 */
	public double getSlope() {
		return slope;
	}

	/**
	 * @return the value of the yIntercept attribrute
	 */
	public double getYIntercept() {
		return yIntercept;
	}


	/**
	 * @return the value of the rho attribute
	 */
	public double getRho() {
		return rho;
	}

	
	/**
	 * Set the value for the rho attribute and update the value of the verticalStep
	 * @param rho the new value of rho
	 */
	public void setRho(int rho) {
		this.rho = rho;
		this.init();
	}

	
	/**
	 * @return the value of the theta attribute in radian
	 */
	public double getTheta() {
		return theta;
	}
	
	
	/**
	 * @return the value of the theta attribute in degree
	 */
	public double getThetaDegree(){
		return Math.toDegrees(theta);
	}
	
	
	/**
	 * Set the value for the theta attribute and update verticalStep
	 * @param theta the new value of the theta attribute
	 */
	public void setTheta(double theta) {
		this.theta = theta;
		this.init();
	}

	
	/**
	 * @return the string corresponding to the current object
	 */
	public String toString(){
		return "(theta: "+ Math.toDegrees(this.theta) + ",rho: " + this.rho + ")";
	}

}
