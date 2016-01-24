package ua.tankv2;

import ua.tankv2.action.ActionField;
import ua.tankv2.action.SetupUI;

public class Demo {


    public static void main(String[] args)throws  Exception{

        SetupUI su = new SetupUI();
        rGame(su);

     }

    public static void rGame(SetupUI su) throws Exception{
        ActionField af;
        while (su.getmFrame().isVisible()) {
            Thread.sleep(1000);
        }

        af = new ActionField(su);
        af.setSetupUI(su);
        af.setNameAggressor(su.getAggressor());
        af.runTheGame();
        rGame(su);
    }
}
