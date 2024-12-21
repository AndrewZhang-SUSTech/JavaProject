package com.example.view.login;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.model.Level;
import com.example.model.Saving;
import com.example.view.FrameUtil;
import com.example.view.level.LevelFrame;

public class LoginFrame extends JFrame {
    private JTextField username;
    private JTextField password;
    private final JButton submitBtn;
    private final JButton resetBtn;
    private LevelFrame levelFrame;

    public LoginFrame(int width, int height) {
        this.setTitle("Login Frame");
        this.setLayout(null);
        this.setSize(width, height);
        JLabel userLabel = FrameUtil.createJLabel(this, new Point(50, 20), 70, 40, "username:");
        JLabel passLabel = FrameUtil.createJLabel(this, new Point(50, 80), 70, 40, "password:");
        username = FrameUtil.createJTextField(this, new Point(120, 20), 120, 40);
        password = FrameUtil.createJPassWordField(this, new Point(120, 80), 120, 40);

        submitBtn = FrameUtil.createButton(this, "Confirm", new Point(40, 140), 100, 40);
        resetBtn = FrameUtil.createButton(this, "Reset", new Point(160, 140), 100, 40);

        submitBtn.addActionListener(_ -> {
            System.out.println("Username = " + username.getText());
            System.out.println("Password = " + password.getText());

            Saving newSavingProfile = new Saving(Saving.getMD5Hash(username.getText()),
                    Saving.getMD5Hash(password.getText()));
            System.out.println(newSavingProfile.getUser() + '\n' + newSavingProfile.getPassword());

            StringBuilder json = new StringBuilder();
            // 读文件
            try {
                File file = new File("config.json");
                if (!file.exists()) {
                    if (file.createNewFile()) {
                        System.out.println("配置文件不存在，已重新创建");
                    } else {
                        System.out.println("创建失败");
                    }
                }
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        json.append(line);
                    }
                }
            } catch (IOException iOException) {
                System.err.println("写入错误");
            }

            java.util.List<Saving> savings = null;
            try {
                if (JSONObject.parseArray(json.toString(), Saving.class) != null) {
                    savings = JSONObject.parseArray(json.toString(), Saving.class);
                } else {
                    savings = new ArrayList<>();
                }
            } catch (JSONException exception) {
                // 重载配置文件
                int response = JOptionPane.showConfirmDialog(this, "配置文件损坏，是否重置配置文件", "确认", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    try {
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("config.json", false))) {
                            writer.write("");
                        }
                    } catch (IOException exception2) {
                        System.out.println("写入错误");
                    }

                }
            }
            boolean loginSucceed = false;
            boolean isSavingRegistered = false;
            if (savings == null) {
                savings = new ArrayList<>();
            }
            if (!savings.isEmpty()) {
                for (Saving i : savings) {
                    if (newSavingProfile.equals(i)) {
                        newSavingProfile = i;
                        isSavingRegistered = true;
                        loginSucceed = true;
                        break;
                    } else if (i.getUser().equals(username.getText())) {
                        JOptionPane.showMessageDialog(this, "密码错误", "警告", JOptionPane.WARNING_MESSAGE);
                        isSavingRegistered = true;
                    }
                }
            }

            int response = 100;
            if (!isSavingRegistered) {
                response = JOptionPane.showConfirmDialog(this, "未找到注册信息，是否立即用输入的信息注册", "确认",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    // write file
                    try {
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("config.json", false))) {
                            newSavingProfile.setLevels(Level.DEFAULT_LEVELS());
                            savings.add(newSavingProfile);
                            writer.write(JSON.toJSONString(savings, SerializerFeature.PrettyFormat));
                        }
                    } catch (IOException exception) {
                        System.out.println("写入错误");
                    }
                }
            }
            if (this.levelFrame != null && loginSucceed) {
                levelFrame.setSaving(newSavingProfile);
                this.levelFrame.setVisible(true);
                this.setVisible(false);
                username.setText("");
                password.setText("");
            }
            // check login info

        });

        resetBtn.addActionListener(_ -> {
            username.setText("");
            password.setText("");
        });

        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void setLevelFrame(LevelFrame levelFrame) {
        this.levelFrame = levelFrame;
    }
}
