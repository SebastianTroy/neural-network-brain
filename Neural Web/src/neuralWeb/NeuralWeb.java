package neuralWeb;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * This class is used to hold a network of {@link Neuron}s which are meant to
 * represent a non-linear and non static neural network.
 * 
 * A brain is made up of Neurons that are linked together by {@link Connection}
 * s. Each {@link Neuron} is designated a "cluster" number, what this means is
 * that when the brain is mutated, connections are more likely to be created or
 * destroyed within a cluster than between clusters.
 * 
 * @author Sebastian Troy
 */
public class NeuralWeb
	{
		// The location of important information within the genes.
		private final int NUM_INPUTS = 0, NUM_OUTPUTS = 1, NUM_NEURONS = 2, CLUSTER = 1, THRESHOLD = 2, START_LEVEL = 3, RECIEVER_ID = 1, CONNECTION_WEIGHT = 2;

		private int age = 0, numInputs, numOutputs;
		private int[] genes = null;

		private Map<Integer, Neuron> neurons;

		private LinkedList<Message> messagesToSend = new LinkedList<Message>();
		private LinkedList<Message> messagesToSendNextCalculation = new LinkedList<Message>();

		/**
		 * Creates an instance of this class with no {@link Neuron}s or
		 * {@link Connection}s and sets the number of inputs and outputs
		 * 
		 * @param genes
		 *            - A list of integers that describe exactly how this
		 *            {@link NeuralWeb} is structured.
		 */
		public NeuralWeb(int[] genes)
			{
				this.genes = genes;
				this.numInputs = genes[NUM_INPUTS];
				this.numOutputs = genes[NUM_OUTPUTS];
				neurons = new HashMap<Integer, Neuron>(NUM_NEURONS);

				createBrainFromGenes(genes);
			}

		/**
		 * This method can be used to get a {@link Triggerable} brain input. The
		 * first input is numbered 0.
		 * 
		 * @param inputNumber
		 *            - The index for the input that you would like to link to.
		 * @return - The {@link Triggerable} brain input at the specified index.
		 *         <code>null</code> if the inputNumber is negative or => the
		 *         number of inputs.
		 */
		public final Triggerable getInput(int inputNumber)
			{
				if (inputNumber < numInputs)
					return neurons.get(inputNumber);
				else
					return null;
			}

		public final void triggerInput(int inputNumber)
			{
				if (inputNumber < numInputs)
					neurons.get(inputNumber).trigger();

			}

		public final void attachOutput(Triggerable output, int brainOutputNumber)
			{
				if (brainOutputNumber < numOutputs)
					{
						/*
						 * "-brainOutputNumber" because output neurons have
						 * negative ID's, this is hidden from the end user. "-1"
						 * because the first output ID is -1, not 0.
						 */
						Neuron sender = neurons.get(-brainOutputNumber - 1);
						sender.connections.addLast(new Connection(sender, output));
					}
			}

		/**
		 * Any messages sent by {@link Neuron}s and inputs are sent,
		 * {@link neuron}s that receive messages are tested to see if they have
		 * been triggered, and a new message is created if they have, this
		 * message is stored until the next call of this method.
		 */
		public final void calculate()
			{
				// age by 1
				age++;

				// Send all messages.
				for (Message m : messagesToSend)
					m.recipient.trigger(m.weight);

				// Prepare the messages for next calculation
				messagesToSend = messagesToSendNextCalculation;
				messagesToSendNextCalculation = new LinkedList<Message>();
			}

		/**
		 * All the information required to make an identical copy of this
		 * {@link NeuralWeb} is stored as a list of integers. First the number
		 * of inputs, then the number of outputs, then the number of neurons,
		 * then the information for each {@link Neuron} which is stored as an ID
		 * number, a cluster number, its threshold and its start value. The rest
		 * is the {@link Connection} information, read in threes: the ID of the
		 * start, the ID of the end and the weight of the {@link Connection}.
		 * 
		 * @return - An array of integers that represent all the information
		 *         about this entire Brain
		 */
		final int[] getGenes()
			{
				int[] genesCopy = new int[genes.length];

				for (int i = 0; i < genes.length; i++)
					genesCopy[i] = genes[i];

				return genesCopy;
			}

		/**
		 * Takes an array of integers that represents a {@link Connection} and
		 * turns it into an identical version of that {@link Connection}.
		 * 
		 * @param genes
		 *            - An array of integers that represents a cluster.
		 */
		private final void createBrainFromGenes(int[] genes)
			{
				for (int neuronNum = 0, ID_index = 3; neuronNum < genes[NUM_NEURONS]; neuronNum++, ID_index += 4)
					neurons.put(genes[ID_index], new Neuron(genes[ID_index], genes[ID_index + CLUSTER], genes[ID_index + THRESHOLD], genes[ID_index + START_LEVEL]));

				for (int sender_ID_index = (genes[NUM_NEURONS] * 4) + 3; sender_ID_index < genes.length; sender_ID_index += 3)
					neurons.get(genes[sender_ID_index]).connections.addLast(new Connection(genes[sender_ID_index], genes[sender_ID_index + RECIEVER_ID], genes[sender_ID_index + CONNECTION_WEIGHT]));
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
				private int ID, cluster, threshold, startLevel, currentLevel, lastUpdated = 0, lastFired = -1;
				private LinkedList<Connection> connections;

				private Neuron(int uniqueID, int cluster, int threshold, int startLevel)
					{
						ID = uniqueID;
						this.cluster = cluster;
						this.threshold = threshold;
						this.currentLevel = this.startLevel = startLevel;

						connections = new LinkedList<Connection>();
					}

				/**
				 * Triggers this {@link Neuron} ensuring that it fires.
				 */
				@Override
				public final void trigger()
					{
						trigger(threshold);
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
						while (lastUpdated < age && currentLevel > 0)
							{
								currentLevel /= 2;
							}

						// Apply the new message to the neuron
						currentLevel += connectionWeight;

						// Check to see if neuron should fire and has not yet
						// fired this calculation
						if (currentLevel >= threshold && lastFired != age)
							{
								// Make sure neuron fires only once each
								// calculation of the brain
								lastFired = age;

								for (Connection connection : connections)
									messagesToSendNextCalculation.add(new Message(connection.recipient, connection.weight));
								currentLevel = 0;
							}

						// Update the time this neuron was last triggered
						lastUpdated = age;
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
				private Triggerable sender, recipient;
				private int weight;

				private Connection(int senderID, int recipientID, int weight)
					{
						sender = neurons.get(senderID);
						recipient = neurons.get(recipientID);
						this.weight = weight;
					}

				private Connection(Triggerable sender, Triggerable recipient)
					{
						this.sender = sender;
						this.recipient = recipient;
						weight = 0;
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