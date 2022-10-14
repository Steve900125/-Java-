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
public class mathModel {
    public double xMin = 1;
    public double xRan = 1;
    public double yAns = 0;
    public int k = 1;
    
    public mathModel(double xMin , double xRan , int k ){
        this.xMin = xMin;
        this.xRan = xRan;
        this.k = k;
    }
    
    public double Paretodistribution(){
        if(xMin > xRan){
            return 0;
        }else{
//            yAns = (k * Math.pow(xMin, k))/ Math.pow(xRan, (k+1)) ;
            yAns = xMin / Math.pow(xRan, 1.0/k);
        }
        return yAns;
    }
    
}

