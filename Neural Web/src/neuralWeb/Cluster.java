package neuralWeb;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * This class represents a non-linear, mutable neural network. The idea is that
 * there is a 'Chip' like a computer chip, with a fixed set of inputs at the top
 * and a fixed set of outputs at the bottom. The neurons are between these
 * inputs and outputs and are connected. When a neuron fires it sends a signal
 * down all of its outgoing connections, the message it sends the next neuron is
 * the connections 'weight' this could be positive or negative but every
 * connection always sends the same message. When a neuron gets messages that
 * add up to or above a threshold it then fires messages down its own
 * connections. Connections can connect inputs to neurons, neurons to neurons,
 * neurons to outputs and even inputs directly to outputs. Neurons keep track of
 * when they should fire with a number that adds any inputs the neuron receives
 * together, when this number goes above the threshold the neuron will fire,
 * however the number slowly gets closer to zero each calculation so the bigger
 * the gap between messages the less likely it will fire.
 * 
 * @author Sebastian Troy
 */
public class Cluster
	{
		// The brain that this cluster is part of.
		private Brain brain;

		private int[] genes = null;

		private Map<Integer, Neuron> neurons;
		Neuron[] inputs;
		ClusterOutput[] outputs;

		private LinkedList<Message> messagesToSend = new LinkedList<Message>();
		private LinkedList<Message> messagesToSendNextCalculation = new LinkedList<Message>();

		/**
		 * Creates an instance of this class with no {@link Neuron}s or
		 * {@link Connection}s and sets the number of inputs and outputs
		 * 
		 * @param numInputs
		 *            - The number of inputs which feed into this
		 *            {@link Cluster}
		 * @param numOutputs
		 *            - The number of outputs which feed out of this
		 *            {@link Cluster}
		 */
		public Cluster(Brain brain, int numInputs, int numOutputs)
			{
				this.brain = brain;

				neurons = new HashMap<Integer, Neuron>(numInputs + numOutputs);
				inputs = new Neuron[numInputs];
				outputs = new ClusterOutput[numOutputs];

				setupInputsOutputs();

				writeGenes();
			}

		/**
		 * Creates an instance of this class containing all of the
		 * {@link Neuron}s and {@link Connection}s described in the genes.
		 * 
		 * @param genes
		 *            - An array of integers representing all of the information
		 *            required to make an instance of the stored cluster.
		 */
		public Cluster(Brain brain, int[] genes)
			{
				this.brain = brain;
				this.genes = genes;

				readGenes(genes);
			}

		/**
		 * Creates the inputs and outputs for the {@link Cluster}
		 */
		private final void setupInputsOutputs()
			{
				for (int i = 0; i < inputs.length; i++)
					inputs[i] = new Neuron(i, 0, 0, 1, 0);

				for (int i = 0; i < outputs.length; i++)
					outputs[i] = new ClusterOutput();
			}

		/**
		 * Any messages sent by {@link Neuron}s and inputs are sent,
		 * {@link neuron}s that receive messages are tested to see if they have
		 * been triggered, and a new message is created if they have, this
		 * message is stored until the nect call of this method.
		 */
		final void calculate()
			{
				// Send all messages.
				for (Message m : messagesToSend)
					m.recipient.trigger(m.weight);

				// Prepare the messages for next calculation
				messagesToSend = messagesToSendNextCalculation;
				messagesToSendNextCalculation = new LinkedList<Message>();
			}

		/**
		 * All the information required to make an identical copy of this
		 * {@link Cluster} is stored as a list of integers. First the number of
		 * inputs, then the number of outputs, then the number of neurons, then
		 * the information for each {@link Neuron} which is stored as an ID
		 * number, an x and y location (for graphical representation), its
		 * threshold and its start value. The rest is the {@link Connection}
		 * information, read in threes: the ID of the start, the ID of the end
		 * and the weight of the {@link Connection}.
		 * 
		 * @return - An array of integers that represent all the information
		 *         about this entire Cluster
		 */
		final int[] writeGenes()
			{
				if (genes == null)
					{
						int numConnections = 0;

						for (Neuron n : neurons.values())
							numConnections += n.connections.size();

						genes = new int[3 + (neurons.size() * 5) + numConnections * 3];

						genes[0] = inputs.length;
						genes[1] = outputs.length;
						genes[2] = neurons.size();

						int index = 3;
						for (Neuron n : neurons.values())
							if (n.ID >= inputs.length)
								{
									genes[index] = n.ID;
									genes[index + 1] = n.x;
									genes[index + 2] = n.y;
									genes[index + 3] = n.threshold;
									genes[index + 4] = n.startLevel;
									index += 5;
								}

						for (Neuron n : neurons.values())
							for (Connection connection : n.connections)
								{
									genes[index] = ((Neuron) (connection.sender)).ID;
									genes[index + 1] = ((Neuron) (connection.reciever)).ID;
									genes[index + 2] = index += 3;
								}
					}

				return genes;
			}

		/**
		 * Takes an array of integers that represents a {@link Connection} and
		 * turns it into an identical version of that {@link Connection}.
		 * 
		 * @param genes
		 *            - An array of integers that represents a cluster.
		 */
		private final void readGenes(int[] genes)
			{
				inputs = new Neuron[genes[0]];
				outputs = new ClusterOutput[genes[1]];

				setupInputsOutputs();

				int numGenes = genes[2];

				// Set the length of the map to save on wasted space
				neurons = new HashMap<Integer, Neuron>(inputs.length + outputs.length + numGenes);

				// Create each Neuron from the genes
				for (int i = 3; i < numGenes; i += 5)
					{
						neurons.put(genes[i], new Neuron(genes[i], genes[i + 1], genes[i + 2], genes[i + 3], genes[i + 4]));
					}

				int numConnections = (genes.length - 3 - (numGenes * 5)) / 3;
				for (int geneIndex = 3 + (numGenes * 5), connection = 0; connection < numConnections; geneIndex += 3, connection++)
					neurons.get(genes[geneIndex]).connections.add(new Connection(genes[geneIndex], genes[geneIndex + 1], genes[geneIndex + 2]));
			}

		/**
		 * Has a unique ID so that it can be differentiated from any other
		 * neuron. It contains data allowing it to be drawn to the screen, it
		 * also contains two variables which represent its threshold level and
		 * its current level. It also contains a list of {@link Connection}s it
		 * has with other neurons.
		 * <p>
		 * If its current level is => its threshold value the neuron will send a
		 * {@link Message} down each of its {@link Connection}s.
		 * 
		 * @author Sebastian Troy
		 */
		private class Neuron implements Triggerable
			{
				private int ID, x, y, threshold, startLevel, currentLevel, lastUpdated = 0, lastFired = -1;
				private LinkedList<Connection> connections;

				private Neuron(int uniqueID, int x, int y, int threshold, int startLevel)
					{
						ID = uniqueID;
						this.x = x;
						this.y = y;
						this.threshold = threshold;
						this.currentLevel = this.startLevel = startLevel;

						connections = new LinkedList<Connection>();
					}

				/**
				 * If a neuron is triggered, calculate its level. If the level
				 * is above this neuron's threshold
				 * <p>
				 * Then divide the Neuron's level by 2.
				 */
				@Override
				public final void trigger(int connectionWeight)
					{
						// Bring the neuron up to date
						while (lastUpdated < brain.age && currentLevel > 0)
							{
								currentLevel /= 2;
							}

						// Apply the new message to the neuron
						currentLevel += connectionWeight;

						// Check to see if neuron should fire and has not yet
						// fired this calculation
						if (currentLevel >= threshold && lastFired != brain.age)
							{
								// Make sure neuron fires only once each
								// calculation of the brain
								lastFired = brain.age;

								for (Connection connection : connections)
									messagesToSendNextCalculation.add(new Message(connection.reciever, connection.weight));
								currentLevel = 0;
							}

						// Update the time this neuron was last triggered
						lastUpdated = brain.age;
					}
			}

		/**
		 * This class acts like a {@link Neuron} that passes a message to
		 * outside of this cluster.
		 * 
		 * @author Sebastian Troy
		 */
		class ClusterOutput implements Triggerable
			{
				Triggerable connectedNeuron;

				@Override
				public void trigger(int connectionWeight)
					{
						connectedNeuron.trigger(connectionWeight);
					}
			}

		/**
		 * Stores three variables. The {@link Neuron} at the start of the
		 * connection (the sender), the {@link Neuron} at the end of the
		 * connection (the receiver) and the connections weight.
		 * <p>
		 * The weight of the connection is the variable passed to the
		 * {@link Neuron} receiving the {@link Message} from the sender.
		 * 
		 * @author Sebastian Troy
		 */
		private class Connection
			{
				private Triggerable sender, reciever;
				private int weight;

				private Connection(int senderID, int recieverID, int weight)
					{
						sender = neurons.get(senderID);
						if (recieverID < outputs.length)
							reciever = outputs[recieverID];
						else
							reciever = neurons.get(recieverID);
						this.weight = weight;
					}
			}

		/**
		 * Stores two variables, a {@link Neuron} that will receive the message
		 * and an integer representing the message.
		 * 
		 * @author Sebastian Troy
		 */
		private class Message
			{
				private Triggerable recipient;
				private int weight;

				private Message(Triggerable recipient, int weight)
					{
						this.recipient = recipient;
						this.weight = weight;
					}
			}
	}