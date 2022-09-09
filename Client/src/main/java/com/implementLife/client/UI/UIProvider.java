package com.implementLife.client.UI;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.awt.*;

public final class UIProvider {
    public static void newWindow() {
        setFlatLookAndFeel();
        showUI();
    }

    private static void setFlatLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Throwable e) {
            showErrDialog(new RuntimeException("Failed to initialize LaF", e));
        }
    }

    private static void showUI() {
        JFrame frame = new JFrame();
        RoomMenu room = new RoomMenu();

        frame.setTitle("Implement Life Room Client");
        frame.setContentPane(room.getRoot());

        ImageIcon icon = new ImageIcon("resources/icons/ImplementLife/IL.png");
        if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            showErrDialog(new RuntimeException("Error load icon"));
        } else {
            frame.setIconImage(icon.getImage());
        }

        frame.setMinimumSize(new Dimension(600, 400));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        room.initAfterCreate();
    }

    public static void showErrDialog(Throwable throwable) {
        new ErrorDialog(throwable);
    }
}
