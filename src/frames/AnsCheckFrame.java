/*
 * Created by JFormDesigner on Sun Oct 17 13:52:12 CST 2021
 */

package frames;

import model.Infix;
import net.miginfocom.swing.MigLayout;
import utils.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.stream.Collectors;

public class AnsCheckFrame extends JFrame {

    File exerciseFile;
    File ansFile;

    Vector<String> exprStrVec;
    Vector<String> ansVec;
    Vector<Infix> rightAnsVec = new Vector<>();
    Vector<Integer> rightVec = new Vector<>();
    Vector<Integer> wrongVec = new Vector<>();

    public AnsCheckFrame() {
        initComponents();
    }

    private void chooseFileButton1ActionPerformed() {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.showDialog(new JLabel(), "选择文件");
        exerciseFile = jfc.getSelectedFile();
        if (exerciseFile != null)
            filePath1.setText(exerciseFile.getAbsolutePath());
    }

    private void chooseFileButton2ActionPerformed() {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfc.showDialog(new JLabel(), "选择文件");
        ansFile = jfc.getSelectedFile();
        if (ansFile != null)
            filePath1.setText(ansFile.getAbsolutePath());
    }

    private void chooseFileButton3ActionPerformed() {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.showDialog(new JLabel(), "选择目录");
        File file = jfc.getSelectedFile();
        if (file != null) {
            filePath3.setText(file.getAbsolutePath());
        }
    }

    private void checkAnsButtonActionPerformed() {
        new Thread(this::checkAns).start();
    }

    private void checkAns() {
        if (filePath1.getText() != null && filePath1.getText().equals("Exercises.txt")) {
            exerciseFile = new File("Exercises.txt");
        }
        if (filePath2.getText() != null && filePath2.getText().equals("Answer.txt")) {
            ansFile = new File("Answer.txt");
        }

        String outPath = filePath3.getText().equals("") ? "Grade.txt" : filePath3.getText() + "\\Grade.txt";

        try {
            exprStrVec = FileUtil.file2StrVec(exerciseFile);
            ansVec = FileUtil.file2StrVec(ansFile);

            exprStrVec.forEach(s -> rightAnsVec.add(new Infix(s)));
            for (int i = 0; i < exprStrVec.size(); i++) {
                if (ansVec.get(i).equals(rightAnsVec.get(i).value.toString())) {
                    rightVec.add(i + 1);
                } else {
                    wrongVec.add(i + 1);
                }
            }

            FileUtil.clearFile(outPath);

            StringBuilder out = new StringBuilder("Correct: " + rightVec.size() + "(");
            out.append(rightVec.stream().map(String::valueOf).collect(Collectors.joining(", ")));
            out.append(")\n");

            FileUtil.appendStr2File(out.toString(), outPath);

            out = new StringBuilder("Wrong: " + wrongVec.size() + "(");
            out.append(wrongVec.stream().map(String::valueOf).collect(Collectors.joining(", ")));
            out.append(")\n");

            FileUtil.appendStr2File(out.toString(), outPath);

//            JOptionPane.showMessageDialog(null, "检查完毕！\n成绩已输出至 " + outPath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "输出文件异常：" + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException numberFormatException) {
            JOptionPane.showMessageDialog(null, "文件格式有误！");
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        label1 = new JLabel();
        filePath1 = new JTextField();
        chooseFileButton1 = new JButton();
        label2 = new JLabel();
        filePath2 = new JTextField();
        chooseFileButton2 = new JButton();
        label3 = new JLabel();
        filePath3 = new JTextField();
        chooseFileButton3 = new JButton();
        checkAnsButton = new JButton();

        //======== this ========
        setMinimumSize(new Dimension(410, 180));
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "fill,hidemode 3",
            // columns
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- label1 ----
        label1.setText("\u9009\u62e9\u9898\u76ee\u6587\u4ef6\uff08\u9ed8\u8ba4\u5728\u7a0b\u5e8f\u6839\u76ee\u5f55\u4e0b\uff09");
        contentPane.add(label1, "cell 0 0 2 1");

        //---- filePath1 ----
        filePath1.setEditable(false);
        contentPane.add(filePath1, "cell 0 1 2 1");

        //---- chooseFileButton1 ----
        chooseFileButton1.setText("\u9009\u62e9...");
        chooseFileButton1.addActionListener(e -> chooseFileButton1ActionPerformed());
        contentPane.add(chooseFileButton1, "cell 0 1 2 1,alignx trailing,growx 0");

        //---- label2 ----
        label2.setText("\u9009\u62e9\u7b54\u6848\u6587\u4ef6\uff08\u9ed8\u8ba4\u5728\u7a0b\u5e8f\u6839\u76ee\u5f55\u4e0b\uff09");
        contentPane.add(label2, "cell 0 2 2 1");

        //---- filePath2 ----
        filePath2.setEditable(false);
        contentPane.add(filePath2, "cell 0 3");

        //---- chooseFileButton2 ----
        chooseFileButton2.setText("\u9009\u62e9...");
        chooseFileButton2.addActionListener(e -> chooseFileButton2ActionPerformed());
        contentPane.add(chooseFileButton2, "cell 0 3 2 1,alignx trailing,growx 0");

        //---- label3 ----
        label3.setText("\u9009\u62e9\u7ed3\u679c\u6587\u4ef6\u8f93\u51fa\u4f4d\u7f6e\uff08\u9ed8\u8ba4\u5728\u7a0b\u5e8f\u6839\u76ee\u5f55\u4e0b\uff09");
        contentPane.add(label3, "cell 0 4");

        //---- filePath3 ----
        filePath3.setEditable(false);
        contentPane.add(filePath3, "cell 0 5");

        //---- chooseFileButton3 ----
        chooseFileButton3.setText("\u9009\u62e9...");
        chooseFileButton3.addActionListener(e -> chooseFileButton3ActionPerformed());
        contentPane.add(chooseFileButton3, "cell 0 5,alignx trailing,growx 0");

        //---- checkAnsButton ----
        checkAnsButton.setText("\u5f00\u59cb\u68c0\u67e5");
        checkAnsButton.addActionListener(e -> checkAnsButtonActionPerformed());
        contentPane.add(checkAnsButton, "cell 0 6");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        setVisible(true);

        filePath1.setText("Exercises.txt");
        filePath2.setText("Answer.txt");
        filePath3.setText("");
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label1;
    private JTextField filePath1;
    private JButton chooseFileButton1;
    private JLabel label2;
    private JTextField filePath2;
    private JButton chooseFileButton2;
    private JLabel label3;
    private JTextField filePath3;
    private JButton chooseFileButton3;
    private JButton checkAnsButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
