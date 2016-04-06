package extraction;

public class Line {
	private int rho;
	private double theta;
	private int verticalStep;
	
	public Line(int rho, double theta){
		this.rho = rho;
		this.theta = theta;
		this.initVerticalStep();
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
	private void initVerticalStep(){
		if (this.isVertical()){
			this.verticalStep = (int)(rho*Math.cos(theta));
		}
		else{
			this.verticalStep = 0;
		}
	}
	
	/**
	 * Returns the y value for the given x if the line is not vertical
	 * @throw ArithmeticException is the line is vertical
	 * @param x
	 */
	public int getY(int x){
		if (!this.isVertical()){
			return (int) ((rho - x*Math.cos(theta))/Math.sin(theta));
		}
		else{
			throw new ArithmeticException("Ligne verticale " + this);
		}
	}
	
	/**
	 * @return the value of the verticalStep attribrutes
	 */
	public int getVerticalStep(){
		return this.verticalStep;
	}
	
	/**
	 * @return the value of the rho attribute
	 */
	public int getRho() {
		return rho;
	}

	/**
	 * Set the value for the rho attribute and update the value of the verticalStep
	 * @param rho the new value of rho
	 */
	public void setRho(int rho) {
		this.rho = rho;
		this.initVerticalStep();
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
		this.initVerticalStep();
	}

	/**
	 * @return the string corresponding to the current object
	 */
	public String toString(){
		return "(theta: "+ Math.toDegrees(this.theta) + ",rho: " + this.rho + ")";
	}

}
