package com.example.tankwar.view.panel;

import java.awt.Graphics;

import javax.swing.JPanel;


import com.example.tankwar.service.PaintService;
import org.apache.commons.logging.LogFactory;


public class GamePanel extends JPanel {


    private static final long serialVersionUID = 2933760710140135907L;
    private PaintService paintService;

    public GamePanel(PaintService paintService) {
        super();
        this.paintService = paintService;
    }

    /*
     * 重新paint
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        this.paintService.rePaintPanel(this, g);
        LogFactory.getLog(this.getClass()).debug("paint...");
    }
}
