package ua.tankv2.action;

import ua.tankv2.Demo;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.event.*;

public class SetupUI extends JPanel {

    private JButton btnBT7;
    private JButton btnTiger;
    private String aggressor;
    private JFrame mFrame;

    public SetupUI() throws Exception  {

        mFrame = new JFrame("BATTLE FIELD, DAY 7");
        mFrame.setLocation(750, 150);
        mFrame.setMinimumSize(new Dimension(512,196));
        mFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mFrame.getContentPane().add(createBtn());


        mFrame.pack();
        mFrame.setVisible(true);

    }

    public JFrame getmFrame() {
        return mFrame;
    }

    public String getAggressor(){
        return aggressor;
    }


    private void otherColor(Color color, JButton btnSource, JButton btnDestination){
        btnSource.setBackground(getBackground());
        btnDestination.setBackground(color);
        repaint();
    }

    private JPanel createBtn(){

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        JLabel lName = new JLabel("Select aggressor");
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.ipady = 32;      //make this component tall
        panel.add(lName, c);


        btnBT7 = new MyButton("BT7","redtank.png",Color.GRAY);
        c = new GridBagConstraints();
        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.ipady = 40;      //make this component tall
        panel.add(btnBT7, c);

        btnBT7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                otherColor(Color.GRAY, btnTiger, btnBT7);
            }
        });

        btnTiger = new MyButton("Tiger","aitank.png", getBackground());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 1;
        c.ipady = 40;      //make this component tall
        panel.add(btnTiger, c);

        btnTiger.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                otherColor(Color.GRAY, btnBT7, btnTiger);
            }
        });

        JButton button = new MyButton("START","",getBackground());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 2;
        c.ipady = 40;      //make this component tall
        c.gridwidth = 2;
        panel.add(button, c);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aggressor = btnBT7.getBackground() == Color.GRAY ? "BT7":"Tiger";
                mFrame.setVisible(false);
            }
        });

        button = new MyButton("EXIT","",getBackground());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 3;
        c.ipady = 40;      //make this component tall
        c.gridwidth = 2;
        panel.add(button, c);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);

            }
        });


        JLabel label = new JLabel("");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 0;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.anchor = GridBagConstraints.FIRST_LINE_END; //bottom of space
        c.insets = new Insets(10,0,0,0);  //top padding
        c.gridx = 0;       //aligned with button 2
        c.gridwidth = 2;   //2 columns wide
        c.gridy = 2;       //third row
        panel.add(label, c);

        return panel;
    }

    private class MyButton extends JButton {
        Image image;
        public MyButton(String text, String nameImage, Color color){
            java.net.URL imageURL = Demo.class.getResource("images/" + nameImage);
            if (imageURL != null){
                image = new ImageIcon(imageURL).getImage();
            } else {
                System.out.println("not found image");
            }
            setText(text);
            setBackground(color);
            repaint();
        }

        public void paint( Graphics g ) {
            super.paint( g );
            g.drawImage(image, 0, 0, 64, 64, 0, 0, 32, 32, new ImageObserver() {
                @Override
                public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                    return false;
                }
            });
        }
    }


}
