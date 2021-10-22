package model;

import utils.MathUtil;

public class Expression {

    public static final int ADD = 1, SUB = 2, MUL = 3, DIV = 4, NUM = 0; //+, -, ×, ÷, /, number

    public int type;
    public Expression left;
    public Expression right;
    public Fraction value;
    public String str;

    public Expression() {
    }

    public Expression(int type, Fraction value) {
        this.type = type;
        this.value = value;
    }

    public Expression(int type, Expression left, Expression right) {
        this.type = type;
        this.left = left;
        this.right = right;
    }

    /**
     * @description 随机生成表达式
     * @param maxOpNum: 运算符数量限制
     * @param bound: 表达式中最大允许出现数字
     * @return model.Expression
     */
    public static Expression genExpr(int maxOpNum, int bound) {
        Expression expr = genSubExpr(MathUtil.getRandomNum(1, maxOpNum), bound, false);

        //必须先计算再生成表达式，因为为了保证没有负数出现会在计算时修改表达式
        expr.value = calExprResult(expr);
        expr.str = getInfixString(expr, null);
        return expr;
    }

    /**
     * @description 生成子表达式
     * @param opNum: 符号剩余配额
     * @param bound: 表达式中最大允许出现数字
     * @param noZero: 要求值不可为零
     * @return model.Expression
     */
    private static Expression genSubExpr(int opNum, int bound, boolean noZero) {

        //初始化
        Expression expr = new Expression();

        if (opNum == 0) {
            expr.type = NUM;
            expr.value = new Fraction(bound, noZero);
        } else  {
            expr.type = MathUtil.getRandomNum(1,4); //加减乘除
            int leftOpNum = opNum == 1 ? 0 : MathUtil.getRandomNum(1, opNum - 1);
            int rightOpNum = opNum - leftOpNum - 1;
            if (expr.type == SUB) { // -
                expr.left = genSubExpr(leftOpNum, bound, noZero);
                expr.right = genSubExpr(rightOpNum, bound, noZero);
            } else if (expr.type == DIV) { // ÷
                expr.left = genSubExpr(leftOpNum, bound, noZero);
                expr.right = genSubExpr(rightOpNum, bound, true);
            } else {
                expr.left = genSubExpr(leftOpNum, bound, noZero);
                expr.right = genSubExpr(rightOpNum, bound, noZero);
            }
        }

//        expr.value = calExprResult(expr);

        return expr;
    }


    /**
     * @description 交换左右子树
     * @param expr: 需要交换子树的结点
     */
    private static void swapLR(Expression expr) {
        Expression tmp = expr.left;
        expr.left = expr.right;
        expr.right = tmp;
    }


    /**
     * @description 递归得到表达式的中缀表达式
     * @param expr: 结点
     * @param parent: 父结点，若expr是根节点时应为null
     * @return String
     */
    public static String getInfixString(Expression expr, Expression parent) {

        if (expr == null) return "";

        if (expr.type == NUM) {
            return expr.value.toString();
        }

        StringBuilder stringBuilder = new StringBuilder();
        boolean tag = true;

        //括号判断
        if (parent == null) tag = false;
        else if (parent.type == ADD || parent.type == SUB ) {
            if (expr.type == MUL || expr.type == DIV)
                tag = false;
            else if (expr == parent.left && (expr.type == ADD || expr.type == SUB))
                tag = false;
        } else if (parent.type == MUL || parent.type == DIV) {
            if (expr == parent.left && (expr.type == MUL || expr.type == DIV))
                tag = false;
        }

        String op;
        if (tag) stringBuilder.append('(');
        stringBuilder.append(getInfixString(expr.left, expr));
        switch (expr.type) {
            case ADD : op = " + ";break;
            case SUB : op = " - ";break;
            case MUL : op = " × ";break;
            case DIV : op = " ÷ ";break;
            default: op = "?";break;
        }
        stringBuilder.append(op);
        stringBuilder.append(getInfixString(expr.right, expr));
        if (tag) stringBuilder.append(')');
        return stringBuilder.toString();
    }


    /**
     * @description 计算表达式结果
     * @param expr:
     * @return model.Fraction
     * @author HiROKi
     * @date 2021/10/17
     */
    public static Fraction calExprResult(Expression expr) {

        if (expr == null) return null;

        Fraction res;
        switch (expr.type) {
            case NUM : res = expr.value;break;
            case ADD : {
                Fraction left = calExprResult(expr.left);
                Fraction right = calExprResult(expr.right);
                if (Fraction.calSub(left, right).numerator > 0) swapLR(expr);
                res = Fraction.calAdd(left, right);
                break;
            }
            case SUB : {
                res = Fraction.calSub(calExprResult(expr.left), calExprResult(expr.right));
                if (res.numerator < 0) {
                    swapLR(expr);
                    res.numerator = -res.numerator;
                }
                break;
            }
            case MUL : {
                Fraction left = calExprResult(expr.left);
                Fraction right = calExprResult(expr.right);
                if (Fraction.calSub(left, right).numerator > 0) swapLR(expr);
                res = Fraction.calMul(left, right);
                break;
            }
            case DIV : res = Fraction.calDiv(calExprResult(expr.left), calExprResult(expr.right));break;
            default: res = new Fraction(0, 1);
        }
        expr.value = res;
        return res;
    }

}
