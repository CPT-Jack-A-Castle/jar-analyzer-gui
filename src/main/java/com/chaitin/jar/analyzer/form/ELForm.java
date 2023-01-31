package com.chaitin.jar.analyzer.form;

import com.chaitin.jar.analyzer.core.ClassReference;
import com.chaitin.jar.analyzer.core.MethodReference;
import com.chaitin.jar.analyzer.model.ResObj;
import com.chaitin.jar.analyzer.spel.MethodEL;
import com.chaitin.jar.analyzer.spel.MethodELProcessor;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import jsyntaxpane.syntaxkits.JavaSyntaxKit;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ELForm {
    public JPanel elPanel;
    private JEditorPane elEditor;
    private JButton checkButton;
    private JButton searchButton;
    private JPanel opPanel;
    private JScrollPane editScroll;

    public ELForm(JarAnalyzerForm instance) {
        JavaSyntaxKit java = new JavaSyntaxKit();
        elEditor.setEditorKit(java);
        java.deinstallComponent(elEditor, "jsyntaxpane.components.LineNumbersRuler");

        checkButton.addActionListener(e -> {
            try {
                ExpressionParser parser = new SpelExpressionParser();
                String spel = elEditor.getText();
                parser.parseExpression(spel);
                JOptionPane.showMessageDialog(this.elEditor, "验证成功");
            } catch (Exception ignored) {
                JOptionPane.showMessageDialog(this.elEditor, "错误的表达式");
            }
        });

        searchButton.addActionListener(e -> {
            ExpressionParser parser = new SpelExpressionParser();
            String spel = elEditor.getText();

            Object value;
            try {
                MethodEL m = new MethodEL();
                Expression exp = parser.parseExpression(spel);
                StandardEvaluationContext ctx = new StandardEvaluationContext();
                ctx.setVariable("method", m);
                value = exp.getValue(ctx);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this.elEditor, "语法错误");
                return;
            }

            if (value instanceof MethodEL) {
                MethodEL condition = (MethodEL) value;

                DefaultListModel<ResObj> searchList = new DefaultListModel<>();

                for (Map.Entry<MethodReference.Handle, MethodReference> entry :
                        JarAnalyzerForm.methodMap.entrySet()) {
                    ClassReference.Handle ch = entry.getValue().getClassReference();
                    MethodReference mr = entry.getValue();

                    MethodELProcessor processor = new MethodELProcessor(ch, mr, searchList, condition);
                    processor.process();
                }

                if (searchList.size() == 0) {
                    JOptionPane.showMessageDialog(this.elEditor, "没有找到结果");
                } else {
                    JOptionPane.showMessageDialog(this.elEditor, "搜索成功");
                }

                instance.resultList.setModel(searchList);
            } else {
                JOptionPane.showMessageDialog(this.elEditor, "错误的表达式");
            }
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        elPanel = new JPanel();
        elPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        opPanel = new JPanel();
        opPanel.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        elPanel.add(opPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        checkButton = new JButton();
        checkButton.setText("验证表达式");
        opPanel.add(checkButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        searchButton = new JButton();
        searchButton.setText("使用该表达式搜索");
        opPanel.add(searchButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        editScroll = new JScrollPane();
        elPanel.add(editScroll, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(500, 300), null, null, 0, false));
        elEditor = new JEditorPane();
        editScroll.setViewportView(elEditor);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return elPanel;
    }

}
