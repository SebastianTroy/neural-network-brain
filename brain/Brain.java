package brain;

/**
 * This class is a holding class used to hold a network of {@link Neuron}s which
 * are meant to represent a highly simplified brain. This class doesn't handle
 * any of the logic needed to create a {@link Brain} from scratch, or any logic
 * needed to mutate the structure.
 * 
 * @author Sebastian Troy
 */
public class Brain
	{
		/**
		 * Once set to {@link false} this brain is no longer functional.
		 */
		protected boolean alive = true;
		/**
		 * This variable dictates how long any individual {@link Neuron} has to
		 * wait after 'firing' / 'depolorising' between before it can 'fire' /
		 * 'depolorise' again.
		 */
		protected double neuronRechargeTime;
		/**
		 * These are the {@link Neuron}s that the {@link Brain} is made up of.
		 */
		protected Neuron[] neurons;
		/**
		 * This is the age of the {@link Brain} in seconds, it is used by the
		 * {@link Neuron}s to keep track of the time since they last 'fired' /
		 * 'depolorised'.
		 */
		protected double ageInSeconds = 0;

		/**
		 * @param neuronRechargeTime
		 *            - This dictates how long any individual {@link Neuron} has
		 *            to wait after 'firing' / 'depolorising' between before it
		 *            can 'fire' / 'depolorise' again.
		 */
		public Brain(double neuronRechargeTime)
			{
				this.neuronRechargeTime = neuronRechargeTime;
		
			}

		/**
		 * @param brain
		 *            - The {@link Brain} which you want this to be a copy of.
		 */
		public Brain(Brain brain)
			{
				neuronRechargeTime = brain.neuronRechargeTime;
				neurons = new Neuron[brain.neurons.length];
				int i = 0;
				for (Neuron n : brain.neurons)
					{
						i++;
						neurons[i] = n;
					}
			}

		/**
		 * When called this method constructs the {@link Brain} and if it has at
		 * least one {@link Neuron} the first {@link Neuron} in the
		 * {@code neurons} array is triggered. This may or may not cause it to
		 * 'fire' or 'depolorise' but if it does it allows for {@link Brain} to
		 * begin functioning from a predictable starting point and allows for
		 * outputs to occour before any inputs from {@link Sensor}s occour.
		 * 
		 * @param neurons
		 *            - This array will become the array of {@link Neuron}s that
		 *            the brain is made up of.
		 * @param neuronConnections
		 *            - This array of arrays hold an array for each
		 *            {@link Neuron}, it denotes which other {@link Neuron}s and
		 *            {@link Effector}s it will connect to. All Connections are
		 *            one way, but this doesn't stop two {@link Neuron}s being
		 *            connected to each other, repeated back and forth
		 *            triggering should be limited by the
		 *            {@code neuronRechargeTime}
		 */
		public final void createBrain(Neuron[] neurons, Triggerable[][] neuronConnections)
			{
				this.neurons = neurons;

				if (neuronConnections.length == neurons.length)
					{
						for (int i = 0; i < neuronConnections.length; i++)
							neurons[i].connectTo(neuronConnections[i]);

						for (Neuron n : neurons)
							n.activate();

						if (neurons.length > 0)
							neurons[0].trigger();
					}
				else
					{
						alive = false;
						System.out.println("Brain::createBrain : neuron connections.length != brain.neurons.length");
					}
			}

		/**
		 * This method simply updates the age of the {@link Brain}.
		 * 
		 * @param secondsPassed
		 *            - The seconds passed since this method was last called.
		 */
		public final void tick(double secondsPassed)
			{
				this.ageInSeconds += secondsPassed;
			}

		/**
		 * Call this method to cease all brain activity, it cannot be undone!
		 */
		public final void kill()
			{
				alive = false;
			}
	}
