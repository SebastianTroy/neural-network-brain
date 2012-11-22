package brain;

/**
 * This class should be the base class for any Effector, that is any class which
 * does something when triggered by an output from the brain.
 * <p>
 * An example could be a "Muscle" that contracts each time it is triggered.
 * 
 * @author Sebastian Troy
 */
public abstract class Effector implements Triggerable
	{
		/**
		 * This keeps track of the number of times the {@link Effector} has been
		 * triggered.
		 */
		private int triggerNum = 0;

		/**
		 * This method is called by a {@link Neuron}, each time it is triggered
		 * the {@code triggerNum} increases by one.
		 */
		@Override
		public final void trigger()
			{
				triggerNum++;
			}

		/**
		 * If the {@code triggerNum} is greater than zero, i.e. the
		 * {@link Effector} has been triggered, this method returns true and
		 * reduces the triggerNumber by one, this means that the
		 * {@link Effector} should go off once for every time it is triggered;
		 * 
		 * @return
		 */
		protected final boolean hasBeenTriggered()
			{
				if (triggerNum > 0)
					{
						triggerNum--;
						return true;
					}
				return false;
			}
	}
