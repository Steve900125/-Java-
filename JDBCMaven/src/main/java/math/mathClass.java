/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math;

import java.math.BigDecimal;

/**
 *
 * @author USER
 */
public class mathClass {
    //預設除法運算精度
	private static final int DEF_DIV_SCALE = 10;
	//構造器私有化，讓這個類不能例項化
	//提供精確的加法運算
	public static double add(double v1, double v2)
	{
		BigDecimal b1 = BigDecimal.valueOf(v1);
		BigDecimal b2 = BigDecimal.valueOf(v2);
		return b1.add(b2).doubleValue();
	}
	//精確的減法運算
	public static double sub(double v1, double v2)
	{
		BigDecimal b1 = BigDecimal.valueOf(v1);
		BigDecimal b2 = BigDecimal.valueOf(v2);
		return b1.subtract(b2).doubleValue();
	}
	//精確的乘法運算
	public static double mul(double v1, double v2)
	{
		BigDecimal b1 = BigDecimal.valueOf(v1);
		BigDecimal b2 = BigDecimal.valueOf(v2);
		return b1.multiply(b2).doubleValue();
	}
}
