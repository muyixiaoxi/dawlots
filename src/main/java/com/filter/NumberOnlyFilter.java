package com.filter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class NumberOnlyFilter extends DocumentFilter {

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {
        // 过滤掉非数字字符
        if (isDigit(string)) {
            super.insertString(fb, offset, string, attr);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs)
            throws BadLocationException {
        // 过滤掉非数字字符
        if (isDigit(string)) {
            super.replace(fb, offset, length, string, attrs);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private boolean isDigit(String text) {
        for (int i = 0 ; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        // 创建 JFrame 实例
        JFrame frame = new JFrame("Number Only Filter Example");

        // 设置窗口的大小和位置
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);

        // 创建一个面板和一个标签
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Enter a number:");

        // 创建一个只允许输入数字的文本框，并添加一个 DocumentFilter
        JTextField textField = new JTextField();

        ((AbstractDocument)textField.getDocument()).setDocumentFilter(new NumberOnlyFilter());

        // 添加一个 KeyListener 监听器，以便在用户按下 Enter 时打印出文本框的内容
        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String text = textField.getText();
                    System.out.println("The number entered is: " + text);
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        // 添加标签和文本框到面板中
        panel.add(label, BorderLayout.PAGE_START);
        panel.add(textField, BorderLayout.CENTER);

        // 将面板添加到窗口
        frame.getContentPane().add(panel);

        // 设置窗口退出时关闭应用程序
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 设置窗口可见
        frame.setVisible(true);
    }
}
