package hillbillies.model.activities;

import hillbillies.model.IActivity;
import hillbillies.model.Unit;

/**
 * The Id's of the activities are the following:
 * 0: noActivity
 * 1: attack
 * 2: defend
 * 3: movement
 * 4: working
 * 5: resting
 * 6: falling
 */

public class Rest implements IActivity {

	public Rest(Unit unit) {
		this.unit = unit;
	}

	public Unit unit;

	@Override
	public void advanceActivityTime(double dt) {
		if (unit.getCurrentHitPoints() < unit.getMaxPoints()) {
			int HitPointsToAdd = (int) Math.ceil((dt / 0.2) * (unit.getToughness() / 200));
			unit.setCurrentHitPoints(unit.getCurrentHitPoints() + HitPointsToAdd);
			//per 0.2s or continuously?	
		}
	}

	@Override
	public double returnSimpleTimeLeft() throws IllegalArgumentException {
		throw new IllegalArgumentException("A resting activity does not have a SimpleTimeLeft attribute.");
	}

	@Override
	public boolean canBeInterruptedBy(IActivity activity) {
		return true;
	}

	@Override
	public int getId() {
		return 5;
	}
	
}
