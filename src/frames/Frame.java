package frames;/*
 * Created by JFormDesigner on Wed Oct 13 11:47:14 CST 2021
 */

import model.Expression;
import net.miginfocom.swing.MigLayout;
import utils.FileUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * @description Swing用户界面
 */
public class Frame extends JFrame {

    static HashSet<String> exprStrSet;
    static Vector<Expression> expressions;

    public Frame() {
        initComponents();
    }

    /**
     * @description 生成题目并写入文件
     * @param exprNum: 生成个数
     * @param bound: 题目中出现数字的范围
     */
    private void genStart(int exprNum, int bound) {

        try {
            // 初始化set、vector
            exprStrSet = new HashSet<>();
            expressions = new Vector<>();
            textArea.setText("");

            // 清除文件
            FileUtil.clearFile("Exercises.txt");
            FileUtil.clearFile("Answer.txt");

            // 循环生成
            for (int i = 0; i < exprNum; ) {
                Expression expr = Expression.genExpr(3, bound); //生成

                //若已出现过此题目或题目中会出现除以0，抛弃该题目
                if (exprStrSet.contains(expr.str) || expr.value == null) continue;
                expressions.add(expr);
                exprStrSet.add(expr.str);

                String str = i + 1 + ". " + expr.str;

                textArea.append(str + "\n"); //输出至textArea
                textArea.selectAll(); //保证textArea总在最下显示

                i++; // 计数
            }

            textArea.append("生成完毕，写入文件中...\n");
            textArea.selectAll();

            //用stream()将题目和答案组合成字符串并写入至文件
            FileUtil.appendStr2File(expressions.stream().map(expr -> expressions.indexOf(expr) + 1 + ". " + expr.str)
                            .collect(Collectors.joining("\n")),
                    "Exercises.txt");

            FileUtil.appendStr2File(expressions.stream().map(expr -> expressions.indexOf(expr) + 1 + ". " + expr.value)
                            .collect(Collectors.joining("\n")),
                    "Answer.txt");

            textArea.append("生成与写入完毕！\n");
            textArea.selectAll();

        } catch (IOException ioException1) {
            JOptionPane.showMessageDialog(null, "文件读写异常：\n" + ioException1.getMessage());
        }
    }

    private void ansCheckButtonActionPerformed() {
        new AnsCheckFrame();
    }

    private void genStartButtonActionPerformed() {
        if (!exprNumTextField.getText().equals("") &&
                !numBoundTextField.getText().equals("")) {
            new Thread(() -> genStart(Integer.parseInt(exprNumTextField.getText()),
                    Integer.parseInt(numBoundTextField.getText()))).start();
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel = new JPanel();
        exprNumLabel = new JLabel();
        exprNumTextField = new JTextField();
        numBoundLabel = new JLabel();
        numBoundTextField = new JTextField();
        scrollPane = new JScrollPane();
        textArea = new JTextArea();
        genStartButton = new JButton();
        ansCheckButton = new JButton();

        //======== this ========
        setTitle("\u56db\u5219\u8fd0\u7b97\u751f\u6210");
        setMinimumSize(new Dimension(450, 350));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
                "fill,hidemode 3",
                // columns
                "[fill]" +
                        "[fill]",
                // rows
                "[]" +
                        "[]" +
                        "[]"));

        //======== panel ========
        {
            panel.setBorder(new TitledBorder("\u751f\u6210\u9009\u9879"));
            panel.setLayout(new MigLayout(
                    "hidemode 3",
                    // columns
                    "[fill]" +
                            "[fill]",
                    // rows
                    "[]" +
                            "[]"));

            //---- exprNumLabel ----
            exprNumLabel.setText("\u751f\u6210\u9898\u76ee\u6570\u91cf");
            panel.add(exprNumLabel, "cell 0 0");
            panel.add(exprNumTextField, "cell 1 0,wmin 50,height 25::25");

            //---- numBoundLabel ----
            numBoundLabel.setText("\u9898\u76ee\u4e2d\u6570\u503c\u8303\u56f4");
            panel.add(numBoundLabel, "cell 0 1");
            panel.add(numBoundTextField, "cell 1 1,wmin 50,height 25::25");
        }
        contentPane.add(panel, "cell 0 0,aligny top,growy 0");

        //======== scrollPane ========
        {

            //---- textArea ----
            textArea.setEditable(false);
            scrollPane.setViewportView(textArea);
        }
        contentPane.add(scrollPane, "cell 1 0 1 3,dock center");

        //---- genStartButton ----
        genStartButton.setText("\u751f\u6210");
        genStartButton.addActionListener(e -> genStartButtonActionPerformed());
        contentPane.add(genStartButton, "cell 0 1");

        //---- ansCheckButton ----
        ansCheckButton.setText("\u68c0\u67e5\u7b54\u6848...");
        ansCheckButton.addActionListener(e -> ansCheckButtonActionPerformed());
        contentPane.add(ansCheckButton, "cell 0 2");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        this.setVisible(true);

        //限制数字输入
        PlainDocument doc = new PlainDocument();
        doc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int off, String str, AttributeSet attr)
                    throws BadLocationException {
                fb.insertString(off, str.replaceAll("\\D++", ""), attr);  // remove non-digits
            }

            @Override
            public void replace(FilterBypass fb, int off, int len, String str, AttributeSet attr)
                    throws BadLocationException {
                fb.replace(off, len, str.replaceAll("\\D++", ""), attr);  // remove non-digits
            }
        });
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel;
    private JLabel exprNumLabel;
    private JTextField exprNumTextField;
    private JLabel numBoundLabel;
    private JTextField numBoundTextField;
    private JScrollPane scrollPane;
    private JTextArea textArea;
    private JButton genStartButton;
    private JButton ansCheckButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
