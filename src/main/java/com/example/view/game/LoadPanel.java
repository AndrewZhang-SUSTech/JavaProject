package com.example.view.game;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.example.model.Level;

public class LoadPanel extends JFrame {
    public LoadPanel(GameFrame view) {
        Level level = view.getLevel();
        this.setVisible(true);
        this.setTitle("从外部载入游戏");
        this.setSize(400, 150);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        JTextField addressField = new JTextField();
        addressField.setColumns(50);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(5, 20, 5, 5);
        this.add(addressField, gbc);

        JButton fileButton = new JButton("地址");
        fileButton.setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(5, 20, 5, 20);
        this.add(fileButton, gbc);

        JButton saveButton = new JButton("save");
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        this.add(saveButton, gbc);

        fileButton.addActionListener(_ -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("JSON file", "json"));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                addressField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            } else {
                System.out.println("未选择文件");
            }
        });

        saveButton.addActionListener(_ -> {
            if (addressField.getText().isBlank()) {
                level.setSaveFile(null);
                this.dispose();

                System.out.println("save");
                view.getLevelFrame().reLoadPage(level.getId());
                return;
            }
            File loadFile = new File(addressField.getText());
            if (!loadFile.exists()) {
                JOptionPane.showMessageDialog(this, "文件不存在");
                this.setVisible(true);
            } else if (!addressField.getText().toLowerCase().endsWith(".json")) {
                JOptionPane.showMessageDialog(this, "文件格式错误，应为json格式");
                this.setVisible(true);
            } else {
                System.out.println(loadFile);
                level.setSaveFile(loadFile);
            }
            System.out.println("save");
            view.getLevelFrame().reLoadPage(level.getId());
            this.dispose();
        });
    }
}
