package ua.tankv2.action;

import ua.tankv2.Demo;
import ua.tankv2.field.Blank;
import ua.tankv2.field.Water;
import ua.tankv2.field.Rock;
import ua.tankv2.managment.*;
import ua.tankv2.tanks.*;
import ua.tankv2.field.BattleField;
import ua.tankv2.managment.Action;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.util.*;
import java.util.List;


public class ActionField extends JPanel {

    private SetupUI setupUI;
    private BattleField battleField;
    private T34 defender;
    private BT7 aggressor;
    private Bullet bullet;
    private Tiger tiger;
    private ArrayList<Destroyable> listOfTank;
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JButton btnBT7;
    private JButton btnTiger;
    private  boolean key = false;

    public ActionField() throws Exception {

        battleField = new BattleField();

        aggressor   = new BT7(battleField,battleField.getAggressorLocation().get("x"),
                              battleField.getAggressorLocation().get("y"),Direction.DOWN);

        tiger  = new Tiger(battleField,battleField.getTigerLocation().get("x"),
                                       battleField.getTigerLocation().get("y"),
                                       Direction.DOWN, 2);

        //battleField.getArrayListAggressor().add(tiger);

        defender  = new T34(battleField);

        listOfTank = new ArrayList<Destroyable>() {
            {
                add(defender);
                add(aggressor);
                add(tiger);
            }
        };

        destroyObjUnderTank(listOfTank);
        battleField.setArrayListEagle();
        battleField.setArrayListOfTank(listOfTank);

        aggressor.setActionForDestroyObject(battleField.getArrayListEagle());
        //aggressor.setActionForDestroyObject(battleField.getArrayListAggressor());
        //defender.setActionForDestroyObject(battleField.getArrayListAggressor());
        
        bullet = new Bullet(-100, -100, Direction.NONE, null);

        mainFrame = new JFrame("BATTLE FIELD, DAY 7");
        mainFrame.setLocation(750, 150);
        mainFrame.setMinimumSize(new Dimension(battleField.getBfWidth() + 17, battleField.getBfHeight() + 35));
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainPanel = this;
        mainFrame.getContentPane().add(createBtn());

        mainFrame.pack();
        mainFrame.setVisible(true);


    }

    public SetupUI getSetupUI() {
        return setupUI;
    }

    public void setSetupUI(SetupUI setupUI) {
        this.setupUI = setupUI;
    }

    private void destroyObjUnderTank(List<Destroyable> listOfTank) {

        for (int i = 0; i < listOfTank.size(); i++) {
            Destroyable t = listOfTank.get(i);
            HashMap<String, Integer> hm = battleField.getQuadrant(t.getX(),t.getY());
            Destroyable obj = battleField.scanQuadrant(hm.get("y"), hm.get("x"));
            if (!(obj instanceof Blank) || !(obj.isDestroyed())) {
                obj.destroy();
            }
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int i = 0;
        Color cc;
        for (int v = 0; v < battleField.getDimentionY(); v++) {
            for (int h = 0; h < battleField.getDimentionX(); h++) {
                if (battleField.COLORED_MODE) {
                    if (i % 2 == 0) {
                        cc = new Color(252, 241, 177);
                    } else {
                        cc = new Color(233, 243, 255);
                    }
                } else {
                    cc = new Color(180, 180, 180);
                }
                i++;
                g.setColor(cc);
                g.fillRect(h * battleField.SIZE_QUADRANT, v * battleField.SIZE_QUADRANT,
                               battleField.SIZE_QUADRANT, battleField.SIZE_QUADRANT);
            }
        }

        battleField.draw(g);
        defender.draw(g);
        aggressor.draw(g);
        bullet.draw(g);
        tiger.draw(g);

    }

    public void runTheGame() throws Exception{

        System.out.println("1");
        if (key) {
            //showGame();
            while (true) {
                if (!aggressor.isDestroyed()) {
                    processAction(aggressor.setUp(), aggressor);
                    break;
                }
//            if (!defender.isDestroyed()) {
//                processAction(defender.setUp(), defender);
//            }
//
//            if ((aggressor.isDestroyed() && defender.isDestroyed()) || dieAllEagle()){
//                mainFrame.setVisible(false);
//                setUpUI.getmFrame().setVisible(true);
//            }

            }
        }
//        while (true) {
//            if (!aggressor.isDestroyed() ) {
//                processAction(aggressor.setUp(), aggressor);
//            }
////            if (!defender.isDestroyed()) {
////                processAction(defender.setUp(), defender);
////            }
////
////            if ((aggressor.isDestroyed() && defender.isDestroyed()) || dieAllEagle()){
////                mainFrame.setVisible(false);
////                setUpUI.getmFrame().setVisible(true);
////            }
//
//        }

//        setupUI.getmFrame().setVisible(true);
//        Thread.sleep(5000);
//        mainFrame.setVisible(false);
    }

    private boolean dieAllEagle(){

        int result = 0;
        List<Destroyable> lE = battleField.getArrayListEagle();
        for (Destroyable eagle: lE ) {
            if (eagle.isDestroyed()){
                result += 1;
            }
        }
        return lE.size() - 1 == result;
    }

    private void processAction(Action a, Tank tank) throws Exception{

        if (a == Action.MOVE) {
            processMove(tank);
        } else if (a == Action.FIRE) {
            processTurn();
            processFire(tank.fire());
        }

    }

    public void processTurn() throws Exception{
        repaint();
    }

    public void processMove(Tank tank) throws Exception{

        Direction direction = tank.getDirection();
        int step = 1;
        int covered = 0;

        if (battleField.checkLimits(tank.getX(), tank.getY(),direction)){
            return;
        }

        processTurn();

        while (covered < battleField.SIZE_QUADRANT) {

            if (direction == Direction.UP) {
                tank.updateY(-step);
            } else if (direction == Direction.DOWN) {
                tank.updateY(step);
            } else if (direction == Direction.LEFT) {
               tank.updateX(-step);
            } else if (direction == Direction.RIGHT) {
                tank.updateX(step);
            }

            covered += step;

            repaint();
            Thread.sleep(tank.getSpeed());
        }
    }

    public void processFire(Bullet bullet) throws Exception{

        this.bullet = bullet;
        int step = 1;
        int incToOut = battleField.OUT_FIELD;

        while ((bullet.getX() > (-1)*incToOut && bullet.getX() < battleField.getBfWidth() + incToOut)
                && (bullet.getY() > (-1) * incToOut && bullet.getY() < battleField.getBfHeight() + incToOut)) {

            if (bullet.getDirection() == Direction.UP) {
                bullet.updateY(-step) ;
            } else if (bullet.getDirection() == Direction.DOWN) {
                bullet.updateY(step) ;
            } else if (bullet.getDirection() == Direction.LEFT) {
                bullet.updateX(-step) ;
            } else if (bullet.getDirection() == Direction.RIGHT) {
                bullet.updateX(step);
            }

            if (processInterception()){
                bullet.destroy();
            }

            repaint();
            Thread.sleep(bullet.getSpeed());
            if (bullet.isDestroyed()){
                break;
            }

        }

    }


    private boolean processInterception() throws Exception {

        HashMap<String, Integer> coordinates = battleField.getQuadrant(bullet.getX(), bullet.getY());
        int y = coordinates.get("y");
        int x = coordinates.get("x");

        Destroyable obf;
        if (y >= 0 && y < battleField.getDimentionY() && x >= 0 && x < battleField.getDimentionX()) {
            obf = battleField.scanQuadrant(y,x);

            if (!obf.isDestroyed() && !(obf instanceof Blank) && !(obf instanceof Water) && !(obf instanceof Rock)) {
                battleField.destroyObject(y, x);
                return true;

            }

            if(obf instanceof Rock ) {
                return true;
            }

            for (Destroyable tank : listOfTank){
                if (!tank.isDestroyed() && checkInterception(tank, coordinates)){
                    tank.destroy();
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkInterception(Destroyable tank, HashMap<String,Integer> quadrant){

        HashMap<String,Integer> obj = battleField.getQuadrant(tank.getX(), tank.getY());
        int oy = obj.get("y");
        int ox = obj.get("x");

        int qy = quadrant.get("y");
        int qx = quadrant.get("x");

        if (oy >= 0 && oy < battleField.getDimentionY() && ox >= 0 && ox < battleField.getDimentionX()) {
            if (oy == qy && ox == qx && bullet.getTank() != tank ) {
                return true;
            }
        }
        return false;
    }

    private void showMenu(){

        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(createBtn());
        mainFrame.pack();
        mainFrame.repaint();

    }

    private void showGame(){

        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(mainPanel);

        mainFrame.pack();
        mainFrame.repaint();

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
                key = true;
                showGame();
                try {
                    runTheGame();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
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

    private void otherColor(Color color, JButton btnSource, JButton btnDestination){
        btnSource.setBackground(getBackground());
        btnDestination.setBackground(color);
        repaint();
    }


}
