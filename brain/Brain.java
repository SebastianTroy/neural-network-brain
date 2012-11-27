package brain;

import java.util.ArrayList;

import brain.addons.Node;
import brain.addons.VisualBrainCreator;

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
		 * These contain the information required to generate this brain, they
		 * are also needed in order to create another brain based on this one.
		 */
		public BrainBlueprints blueprints;
		/**
		 * This is the age of the {@link Brain} in seconds, it is used by the
		 * {@link Neuron}s to keep track of the time since they last 'fired' /
		 * 'depolorised'.
		 */
		protected double ageInSeconds = 0;

		/**
		 * NOTE:
		 * <p>
		 * there is no guaruntee that any {@link Sensor}s or {@link Effector}s
		 * will actually be connected to the {@link Brain}. You must connect
		 * them yourself in the {@link VisualBrainCreator} or hope that
		 * connections evolve naturally during your simulation)
		 * 
		 * @param neuronRechargeTime
		 *            - This dictates how long any individual {@link Neuron} has
		 *            to wait after 'firing' / 'depolorising' between before it
		 *            can 'fire' / 'depolorise' again.
		 * 
		 * @param sensors
		 *            - This array should contain all of the {@link Sensor}s
		 *            that you want to have available to the brain.
		 * 
		 * @param effectors
		 *            - Thiss array should contain all the {@link Effector}s
		 *            that you want available to the brain.
		 */
		public Brain(double neuronRechargeTime, Sensor[] sensors, Effector[] effectors)
			{
				this.neuronRechargeTime = neuronRechargeTime;
				blueprints = new BrainBlueprints(sensors, effectors);
			}

		/**
		 * NOTE:
		 * <p>
		 * You do NOT need to use exactly the same {@link Sensor}s and
		 * {@link Effector}s as were used in the parentBrain. This {@link Brain}
		 * will be as close a copyof the parentBrain as is possible. Any newly
		 * added {@link Sensor}s and {@link Effector}s will be added but not
		 * necissarily connected to the {@link Brain} and any missing will be
		 * removed.
		 * 
		 * @param parentBrain
		 *            - This is the {@link Brain} upon which this new
		 *            {@link Brain} will be based.
		 * 
		 * @param neuronRechargeTime
		 *            - This dictates how long any individual {@link Neuron} has
		 *            to wait after 'firing' / 'depolorising' between before it
		 *            can 'fire' / 'depolorise' again.
		 * 
		 * @param sensors
		 *            - This array should contain all of the {@link Sensor}s
		 *            that you want to have available to the brain.
		 * 
		 * @param effectors
		 *            - Thiss array should contain all the {@link Effector}s
		 *            that you want available to the brain.
		 */
		public Brain(Brain parentBrain, Sensor[] sensors, Effector[] effectors)
			{
				this.neuronRechargeTime = parentBrain.neuronRechargeTime;
				blueprints = new BrainBlueprints(parentBrain.blueprints, sensors, effectors);
			}

		public final void generateBrain()
			{
				Node[] nodes = new Node[blueprints.sensorNodes.size() + blueprints.neuronNodes.size() + blueprints.effectorNodes.size()];
				int nodeNum = 0;
				for (Node n : blueprints.neuronNodes)
					{
						nodes[nodeNum] = n;
						nodeNum++;
					}
				for (Node n : blueprints.sensorNodes)
					{
						nodes[nodeNum] = n;
						nodeNum++;
					}
				for (Node n : blueprints.effectorNodes)
					{
						nodes[nodeNum] = n;
						nodeNum++;
					}

				Neuron[] neurons = new Neuron[nodes.length];
				ArrayList<ArrayList<Triggerable>> connections = new ArrayList<ArrayList<Triggerable>>(neurons.length);
				for (int i = 0; i < neurons.length; i++)
					connections.add(new ArrayList<Triggerable>());

				for (int i = 0; i < nodes.length; i++)
					{
						nodes[i].neuron = new Neuron(this, nodes[i].inhibitor, 1, 2.5);
						neurons[i] = nodes[i].neuron;
					}

				for (int i = 0; i < nodes.length; i++)
					for (int j = 0; j < nodes[i].connections.size(); j++)
						switch (nodes[i].connections.get(j).type)
							{
							case BrainBlueprints.TYPE_NEURON:
								connections.get(i).add(nodes[i].connections.get(j).neuron);
								break;
							case BrainBlueprints.TYPE_EFFECTOR:
								boolean foundEffector = false;
								for (int effectorNum = 0; !foundEffector; effectorNum++)
									if (blueprints.effectorNodes.get(effectorNum) == nodes[i].connections.get(j))
										{
											connections.get(i).add(blueprints.getEffectors()[effectorNum]);
											foundEffector = true;
										}
								break;
							}

				for (int i = 0; i < blueprints.getSensors().length; i++)
					{
						blueprints.getSensors()[i].linkToNeuron(blueprints.sensorNodes.get(i).neuron);
					}

				createBrain(neurons, connections);
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
		public final void createBrain(Neuron[] neurons, ArrayList<ArrayList<Triggerable>> neuronConnections)
			{
				this.neurons = neurons;

				if (neuronConnections.size() == neurons.length)
					{
						for (int i = 0; i < neuronConnections.size(); i++)
							neurons[i].connectTo(neuronConnections.get(i));

						for (Neuron n : neurons)
							n.activate();

						if (neurons.length > 0)
							{
								neurons[0].trigger();
							}
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
