package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends JFrame {

    private StringBuilder copiedStringBuilder = new StringBuilder();
    private Timer timer;
    private int iterations = 0;

    public Main() {
        setTitle("Clipboard Monitor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        JTextArea resultTextArea = new JTextArea(10, 30);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iterations = 0;
                startMonitoring();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopMonitoring();
                resultTextArea.setText(copiedStringBuilder.toString());
                // stop
            }
        });

        JPanel panel = new JPanel();
        panel.add(startButton);
        panel.add(stopButton);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(resultTextArea), BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);

        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkClipboard();
                if (iterations >= 25) {
                    stopMonitoring();
                    resultTextArea.setText(copiedStringBuilder.toString());
                }
            }
        });
    }

    private void startMonitoring() {
        timer.start();
    }

    private void stopMonitoring() {
        timer.stop();
    }

    private void checkClipboard() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
            try {
                String copiedText = (String) clipboard.getData(DataFlavor.stringFlavor);
                copiedStringBuilder.append(copiedText).append("\n");
                System.out.println(copiedText);
                iterations++;
                clipboard.setContents(new StringSelection(""), null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}