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
     * @param maxOpNum: 运算符数量限制
     * @param bound:    表达式中最大允许出现数字
     * @return model.Expression
     * @description 随机生成表达式
     */
    public static Expression genExpr(int maxOpNum, int bound) {
        Expression expr = genSubExpr(MathUtil.getRandomNum(1, maxOpNum), bound, false);

        //必须先计算再生成表达式字符串，这是因为负数出现时，会在计算时修改表达式
        expr.value = calExprResult(expr);
        expr.str = getInfixString(expr, null);
        return expr;
    }

    /**
     * @param opNum:  剩余符号个数
     * @param bound:  表达式中最大允许出现数字
     * @param noZero: 要求值不可为零
     * @return model.Expression
     * @description 生成子表达式
     */
    private static Expression genSubExpr(int opNum, int bound, boolean noZero) {

        //初始化
        Expression expr = new Expression();

        if (opNum == 0) {
            expr.type = NUM;
            expr.value = new Fraction(bound, noZero);
        } else {
            expr.type = MathUtil.getRandomNum(1, 4); //加减乘除

            //随机给左右结点分配opNum，即剩余符号个数
            int leftOpNum = opNum == 1 ? 0 : MathUtil.getRandomNum(1, opNum - 1);
            int rightOpNum = opNum - leftOpNum - 1;

            if (expr.type == DIV) { // ÷
                expr.left = genSubExpr(leftOpNum, bound, noZero);
                expr.right = genSubExpr(rightOpNum, bound, true); //除号时规定分母不能为0
            } else {
                expr.left = genSubExpr(leftOpNum, bound, noZero);
                expr.right = genSubExpr(rightOpNum, bound, noZero);
            }
        }

        return expr;
    }

    /**
     * @param expr: 需要交换子树的结点
     * @description 交换左右子树
     */
    private static void swapLR(Expression expr) {
        Expression tmp = expr.left;
        expr.left = expr.right;
        expr.right = tmp;
    }

    /**
     * @param expr:   结点
     * @param parent: 父结点，若expr是根节点时应为null
     * @return String
     * @description 递归得到表达式的中缀表达式
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
        else if (parent.type == ADD || parent.type == SUB) {
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
            case ADD:
                op = " + ";
                break;
            case SUB:
                op = " - ";
                break;
            case MUL:
                op = " × ";
                break;
            case DIV:
                op = " ÷ ";
                break;
            default:
                op = "?";
                break;
        }
        stringBuilder.append(op);
        stringBuilder.append(getInfixString(expr.right, expr));
        if (tag) stringBuilder.append(')');
        return stringBuilder.toString();
    }

    /**
     * @param expr: 表达式
     * @return model.Fraction
     * @description 计算表达式结果，并会对出现负数的部分进行修正。
     */
    public static Fraction calExprResult(Expression expr) {

        if (expr == null) return null;

        Fraction res = null;
        Fraction left = null;
        Fraction right = null;
                
        if (expr.type == NUM) {
            res = expr.value;
        } else {
            // 计算出左右子表达式的值
            left = calExprResult(expr.left);
            right = calExprResult(expr.right);
            if (left == null || right == null) return null; //出现null说明出现了除以0的算式
        }
        
        switch (expr.type) {
            case NUM:
                break;
            case ADD: {
                // 保证只出现 少+多
                if (Fraction.calSub(left, right).getNumerator() > 0) swapLR(expr);
                res = Fraction.calAdd(left, right);
                break;
            }
            case SUB: {
                res = Fraction.calSub(left, right);
                if (res.getNumerator() < 0) { // 出现负号，交换左右子树并对res取负
                    swapLR(expr);
                    res.setDenominator(-res.getNumerator());
                }
                break;
            }
            case MUL: {
                // 保证只出现 少×多
                if (Fraction.calSub(left, right).getNumerator() > 0) swapLR(expr);
                res = Fraction.calMul(left, right);
                break;
            }
            case DIV:
                res = Fraction.calDiv(left, right);
                if (res.getDenominator() == 0) return null; // 保证最外层也不会分母为0
                break;
        }
        expr.value = res;
        return res;
    }
    
}
