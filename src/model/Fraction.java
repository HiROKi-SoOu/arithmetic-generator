package model;

import utils.MathUtil;

import java.util.Objects;

public class Fraction {
    int numerator;
    int denominator;

    public Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public Fraction(int bound, boolean noZero) {
        this.numerator = noZero ? MathUtil.getRandomNum(1, bound) : MathUtil.getRandomNum(0, bound);
        this.denominator = MathUtil.getRandomNum(0,1) == 1 ? 1 : MathUtil.getRandomNum(1, bound); //生成整数和分数的概率各一半
        simplify();
    }

    public Fraction(String str) {
        String[] elems = str.split("/");;
        if (str.contains("'")) {
            String[] numeratorArr = elems[0].split("'");
            this.denominator = Integer.parseInt(elems[1]);
            this.numerator = Integer.parseInt(numeratorArr[0]) * denominator + Integer.parseInt(numeratorArr[1]);
        } else {
            if (elems.length == 2) {
                this.numerator = Integer.parseInt(elems[0]);
                this.denominator = Integer.parseInt(elems[1]);
            } else if (elems.length == 1) {
                this.numerator = Integer.parseInt(elems[0]);
                this.denominator = 1;
            }
        }
        simplify();
    }

    private void simplify() {
        if (numerator == 0) {
            denominator = 1;
        } else {
            int gcd = MathUtil.gcd(Math.abs(numerator), Math.abs(denominator));
            denominator = denominator / gcd;
            numerator = numerator / gcd;
            if (denominator<0) {
                numerator = -1 * numerator;
                denominator = -1 * numerator;
            }
        }
    }




    /**
     * @description 计算分数加法 a+b
     * @return model.Fraction
     */
    public static Fraction calAdd(Fraction a, Fraction b) {
        Fraction res;
        if (a.denominator == b.denominator) res = new Fraction(a.numerator + b.numerator, a.denominator);
        else {
            int lcm = MathUtil.lcm(a.denominator, b.denominator);
            res = new Fraction(a.numerator * (lcm/a.denominator) + b.numerator * (lcm/b.denominator), lcm);
        }
        res.simplify();
        return res;
    }

    /**
     * @description 计算分数减法 a-b
     * @return model.Fraction
     */
   public static Fraction calSub(Fraction a, Fraction b) {
       Fraction res;
       if (a.denominator == b.denominator) res = new Fraction(a.numerator - b.numerator, a.denominator);
       else {
           int lcm = MathUtil.lcm(a.denominator, b.denominator);
           res = new Fraction(a.numerator * (lcm/a.denominator) - b.numerator * (lcm/b.denominator), lcm);
       }
       res.simplify();
       return res;
   }

   /**
    * @description 计算分数乘法 a*b
    * @return model.Fraction
    */
   public static Fraction calMul(Fraction a, Fraction b) {
       Fraction res = new Fraction(a.numerator * b.numerator, a.denominator * b.denominator);
       res.simplify();
       return res;
   }

   /**
    * @description 计算分数除法 a÷b
    * @return model.Fraction
    */
    public static Fraction calDiv(Fraction a, Fraction b) {
        Fraction res = new Fraction(a.numerator * b.denominator, a.denominator * b.numerator);
        res.simplify();
        return res;
    }

    /**
     * @description toString重写，返回分数字符串
     * 例：整数5返回"5"，分数3分之2返回"2/3"
     * @return String
     * @author HiROKi
     * @date 2021/10/17
     */
    @Override
    public String toString() {
        if (denominator == 0) {
            System.out.println(); return "[ERROR]"; //改
        }
        else if (numerator == 0) return "0";
        else if (denominator == 1) return String.valueOf(numerator);
        else if (numerator < denominator) return numerator + "/" + denominator;
        else return numerator/denominator + "'" + numerator%denominator + "/" + denominator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fraction fraction = (Fraction) o;
        return numerator == fraction.numerator && denominator == fraction.denominator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }




}
