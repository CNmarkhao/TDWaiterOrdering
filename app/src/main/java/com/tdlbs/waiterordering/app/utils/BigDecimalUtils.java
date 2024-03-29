package com.tdlbs.waiterordering.app.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * ================================================
 * 创建时间：2018/10/18 14:30
 * 作者：markgu
 * 描述：展示 Model 的用法
 * Email：<a href="mailto:guhao561@gmail.com">Contact me</a>
 * ================================================
 */
public class BigDecimalUtils {
    // 默认精度10, 应该是2,4, 特别是做金额计算，到分和到毫
    public static final int DEFAULT_SCALE = 2;
    public static final int DEFAULT_DIV_SCALE = 2;

    // 默认的格式化字符样式 “#。00”  还可以是像“#。0000”
    public static final String DEFAULT_FORMAT_STR = "#.00";

    // 加法
    public static BigDecimal add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

    // 减法
    public static BigDecimal sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }

    // 乘法
    public static BigDecimal mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }

    // 除法,默认精度
    public static BigDecimal div(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, DEFAULT_DIV_SCALE, BigDecimal.ROUND_HALF_UP);
    }

    // 对一个double截取指定的长度,利用除以1实现
    public static BigDecimal round(double v1, int scale) {
        if (scale < 0) {
            new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal("1");
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
    }

    // 对一个double截取指定的长度,利用除以1实现
    public static BigDecimal round(double v1) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal("1");
        return b1.divide(b2, DEFAULT_SCALE, BigDecimal.ROUND_HALF_UP);
    }


    // 除法,自定义精度
    public static BigDecimal div(double v1, double v2, int scale) {
        if (scale < 0) {
            new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
    }


    // 比较2个double值，相等返回0，大于返回1，小于返回-1
    public static int compareTo(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.compareTo(b2);
    }

    // 判断2个double的值相等这里要改，相等返回true,否则返回false
    public static boolean valuesEquals(double v1, double v2) {
        boolean result;
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        int resultInt = b1.compareTo(b2);
        if (resultInt == 0) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    // 判断2个double的值,v1大于v2返回true,否则返回false
    public static boolean valuesGreater(double v1, double v2) {
        boolean result;
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        int resultInt = b1.compareTo(b2);
        if (resultInt > 0) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    // 判断2个double的值,v1小于v2返回true,否则返回false
    public static boolean valuesLess(double v1, double v2) {
        boolean result;
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        int resultInt = b1.compareTo(b2);
        if (resultInt < 0) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    // DecimalFormat格式化，使用默认的格式样式
    public static String format(Object object) {
        return new DecimalFormat(DEFAULT_FORMAT_STR).format(object);
    }

    // DecimalFormat格式化，使用传入的字符串格式样式
    public static String format(Object object, String formatStr) {
        return new DecimalFormat(formatStr).format(object);
    }


    public static void main(String[] args) {
        double a = 178.63;
        double b = 3.251;
        System.out.println(BigDecimalUtils.add(a, b));
        System.out.println(BigDecimalUtils.sub(a, b));
        System.out.println(BigDecimalUtils.mul(a, b));
        System.out.println(BigDecimalUtils.div(a, b));
        System.out.println(BigDecimalUtils.div(a, b, 5));
        // 不能以double来构建BigDecimal，只能由string来构建BigDecimal
        System.out.println(new BigDecimal(Double.valueOf(a)));
        System.out.println(new BigDecimal(Double.toString(a)));
        System.out.println(new BigDecimal(Double.toString(a)).toString());
        System.out.println(BigDecimalUtils.compareTo(a, b));
        System.out.println(BigDecimalUtils.compareTo(b, a));
        System.out.println(BigDecimalUtils.valuesEquals(0.002, 0.0020));
        System.out.println(BigDecimalUtils.valuesGreater(a, b));
        System.out.println(BigDecimalUtils.valuesLess(a, b));

        System.out.println(BigDecimalUtils.mul(0.03, 0.0002).stripTrailingZeros().toPlainString());
        System.out.println(BigDecimalUtils.mul(0.03, 0.0002).stripTrailingZeros());
        System.out.println(BigDecimalUtils.mul(0.03, 0.0002).stripTrailingZeros().toString());
        System.out.println("----------");

        System.out.println(new BigDecimal("10000000000").toString());
        System.out.println(new BigDecimal("100.000").toString());
        System.out.println(new BigDecimal("100.000").stripTrailingZeros().toString());
        System.out.println(new BigDecimal("100.000").stripTrailingZeros().toPlainString());

        System.out.println(new DecimalFormat("0.00").format(new BigDecimal("100.000")));
        System.out.println(new DecimalFormat("#.00").format(new BigDecimal("100.000")));

    }

}
