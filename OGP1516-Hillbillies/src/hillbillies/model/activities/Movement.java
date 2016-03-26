package hillbillies.model.activities;

import hillbillies.model.IActivity;

/**
 * the Id's of the activities are the following:
 * 0: noActivity
 * 1: attack
 * 2: defend
 * 3: movement
 * 4: working
 * 5: resting
 * 6: falling
 */

public class Movement implements IActivity {


    @Override
    public void advanceActivityTime(double dt) {

    }


    @Override
    public double returnSimpleTimeLeft() throws IllegalArgumentException {
        throw new IllegalArgumentException("Movement Has no simple TimeLeft attribute");
    }

    @Override
    public boolean canBeInterruptedBy(IActivity activity) {
        return true;
    }


    @Override
    public int getId() {
        return 3;
    }


}

