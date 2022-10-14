/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math;
import java.util.Random;
/**
 *
 * @author USER
 */
public class ParetoRNG {
	private Random rng;
	private double xm; // min value (Xm)
	private double k; // coefficient
	private double maxValue;

	public ParetoRNG(Random rng, double k, double minValue, double maxValue) {
		this.rng = rng;
		this.xm = minValue;
		this.k = k;
		if (maxValue == -1) {
			this.maxValue = Double.POSITIVE_INFINITY;
		} else {
			this.maxValue = maxValue;
		}
	}
	public double getDouble() {
		if (xm == -1) {
			return Double.POSITIVE_INFINITY;
		}
		double x;
		do {
			x = xm * Math.pow((1 - rng.nextDouble()), (-1/k));
		} while (x > maxValue);
		return x;
	}		
}