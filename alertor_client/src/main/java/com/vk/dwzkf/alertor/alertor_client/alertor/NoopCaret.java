package com.vk.dwzkf.alertor.alertor_client.alertor;

import javax.swing.event.ChangeListener;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;
import java.awt.*;

public class NoopCaret implements Caret {
    @Override
    public void install(JTextComponent c) {

    }

    @Override
    public void deinstall(JTextComponent c) {

    }

    @Override
    public void paint(Graphics g) {

    }

    @Override
    public void addChangeListener(ChangeListener l) {

    }

    @Override
    public void removeChangeListener(ChangeListener l) {

    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void setVisible(boolean v) {

    }

    @Override
    public boolean isSelectionVisible() {
        return false;
    }

    @Override
    public void setSelectionVisible(boolean v) {

    }

    @Override
    public void setMagicCaretPosition(Point p) {

    }

    @Override
    public Point getMagicCaretPosition() {
        return null;
    }

    @Override
    public void setBlinkRate(int rate) {

    }

    @Override
    public int getBlinkRate() {
        return 0;
    }

    @Override
    public int getDot() {
        return 0;
    }

    @Override
    public int getMark() {
        return 0;
    }

    @Override
    public void setDot(int dot) {

    }

    @Override
    public void moveDot(int dot) {

    }
}
