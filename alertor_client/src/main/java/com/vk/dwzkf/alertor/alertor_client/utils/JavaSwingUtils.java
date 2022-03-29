package com.vk.dwzkf.alertor.alertor_client.utils;

import javax.swing.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class JavaSwingUtils {
    public static <T> JScrollPane configureAutoscrollDown(JScrollPane scrollPane,
                                                          JList<T> chatList,
                                                          DefaultListModel<T> dlm,
                                                          int lastVisibleItemsThreshold) {
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            private int prevMaxSize = -1;
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (prevMaxSize == -1) {
                    prevMaxSize = e.getAdjustable().getMaximum();
                }
                if (prevMaxSize != e.getAdjustable().getMaximum()) {
                    if (chatList.getLastVisibleIndex() != -1) {
                        if (chatList.getLastVisibleIndex() == dlm.size() - lastVisibleItemsThreshold) {
                            e.getAdjustable().setValue(e.getAdjustable().getMaximum());
                        }
                    }
                }
                prevMaxSize = e.getAdjustable().getMaximum();
            }
        });
        return scrollPane;
    }
}
