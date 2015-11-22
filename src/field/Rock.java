package ua.tankv2.field;

import ua.tankv2.tanks.Tiger;

public class Rock  extends ObjectBattleField {

    @Override
    public void destroy() throws Exception {

        if (getBullet().getTank() instanceof Tiger){
            super.destroy();
        }

    }
}
