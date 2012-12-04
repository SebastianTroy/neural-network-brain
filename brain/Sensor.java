package brain;

import TroysCode.Tools;

/**
 * This class should be the base class for any sensor you wish to make, it
 * contains a {@link Neuron} from the brain and this is the {@link Neuron} that
 * it will trigger when it is itself triggered.
 * <p>
 * A sensor should be by design fairly simple, for more complex inputs into the
 * brain, try making an Organ, a class which has mutiple {@link Sensor}s within
 * it, and activates different {@link Sensor}s depending on the circumstances.
 * 
 * @author Sebastian Troy
 */
public abstract class Sensor
	{
		private long ID = Tools.randLong(0, Long.MAX_VALUE);

		public Sensor()
			{
			}

		/**
		 * The {@link Neuron} which will be triggered when this {@link Sensor}
		 * is triggered;
		 */
		private Neuron neuron;

		/**
		 * @param neuron
		 *            - The {@link Neuron} that will be triggered by this
		 *            {@link Sensor}
		 */
		public final void linkToNeuron(Neuron neuron)
			{
				this.neuron = neuron;
			}

		/**
		 * This method should be called whenever the {@link Sensor} is
		 * triggered, is in turn triggers this {@link Sensor}'s {@link Neuron}.
		 */
		protected final void sensorTriggered()
			{
				if (neuron != null)
					{
						neuron.trigger();
					}
			}

		public abstract void tick(double secondsPassed);

		public final long getID()
			{
				return ID;
			}

		public final void setID(Sensor parent)
			{
				ID = parent.ID;
			}

		/**
		 * This method should be overriden in any classes that extend the
		 * {@link Sensor} class.
		 * 
		 * @return - A string describing this {@link Sensor}.
		 */
		public String getDescription()
			{
				return "Sensor";
			}
	}
