package model;

import utils.MathUtil;

import java.util.Vector;

public class Infix {

    private int pos = 0; //exprStrings当前指针
    public String infixStr; //中缀表达式
    public Expression exprTree; //表达式二叉树
    public Vector<String> exprStrings; //元素数组，每个元素是数或者符号
    public Fraction value; //表达式结果的值

    /**
     * @description Infix构造方法，以中缀表达式字符串为参数实例化Infix对象，并给成员变量（计算并）赋值
     * @param infixStr: 中缀表达式字符串，例：(1 + 2) * (5 - 3)，空格将自动被移除
     */
    public Infix(String infixStr) {

        infixStr = infixStr.replace(" ","");
        this.exprStrings = new Vector<>();

        this.infixStr = infixStr + "+";
        for (int i=0, j = 0; i<this.infixStr.length(); i++) {
            char c = this.infixStr.charAt(i);
            if (c == '+' || c == '-' || c == '×' ||
                    c == '÷' || c == '(' || c == ')') {
                if (i!=0 && i!=j) {
                    this.exprStrings.add(infixStr.substring(j, i));
                }
                this.exprStrings.add(String.valueOf(c));
                j = i+1;
            }
        }

        this.infixStr = infixStr;
        this.exprStrings.remove(this.exprStrings.size()-1);

        exprTree = infix2Expr(this);

        this.value = Expression.calExprResult(exprTree);
    }

    /**
     * @description 递归中缀表达式字符串转表达式二叉树
     * @param infix: 构造过程中的infix，需要其中pos和infixStr成员变量
     * @return model.Expression
     */
    private static Expression infix2Expr(Infix infix) {
        Expression node = infix2ExprTerm(infix);
        while (infix.pos<infix.exprStrings.size() &&
                (infix.exprStrings.get(infix.pos).equals("+") ||
                        infix.exprStrings.get(infix.pos).equals("-"))) {
            int op = infix.exprStrings.get(infix.pos).equals("+") ?
                    Expression.ADD : Expression.SUB;
            infix.pos++;
            node = new Expression(op, node, infix2ExprTerm(infix));
        }
        return node;
    }

    private static Expression infix2ExprTerm(Infix infix) {
        Expression node = infix2ExprFactor(infix);
        while (infix.pos<infix.exprStrings.size() &&
                (infix.exprStrings.get(infix.pos).equals("×") ||
                        infix.exprStrings.get(infix.pos).equals("÷"))) {
            int op = infix.exprStrings.get(infix.pos).equals("×") ?
                    Expression.MUL : Expression.DIV;
            infix.pos++;
            node = new Expression(op, node, infix2ExprFactor(infix));
        }
        return node;
    }

    private static Expression infix2ExprFactor(Infix infix) {
        Expression node = null;
        if (!infix.exprStrings.get(infix.pos).equals("(")) {
            node = new Expression(Expression.NUM,
                    new Fraction(infix.exprStrings.get(infix.pos)));
            infix.pos++;
        } else if (infix.exprStrings.get(infix.pos).equals("(")) {
            infix.pos++;
            node = infix2Expr(infix);
            infix.pos++;
        }
        return node;
    }
}
