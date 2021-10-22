package utils;

import java.util.Random;

public class MathUtil {

    /**
     * @description 得到范围内随机整数Integer
     * @param min: 起始数字（包括）
     * @param max: 边界数字（包括）
     * @return int
     * @author HiROKi
     * @date 2021/10/12
     */
    public static int getRandomNum (int min, int max) {
        return min == 0 ? new Random().nextInt(max + 1) : new Random().nextInt(min + max - 1) + min;
    }


    /**
     * @description 辗转相除法求x和y最大公约数
     * @return int
     */
    public static int gcd(int x, int y){
        if(y == 0)
            return x;
        else
            return gcd(y,x%y);
    }


    /**
     * @description 求x和y最小公倍数
     * @return int
     */
    public static int lcm(int x, int y) {
        return x*y / gcd(x,y);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            System.out.println(getRandomNum(0, 3));
        }
    }
}
