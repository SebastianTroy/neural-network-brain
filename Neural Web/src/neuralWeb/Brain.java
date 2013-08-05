package neuralWeb;

// TODO sort out this comment!
/**
 * This class is used to hold a network of {@link Neuron}s which are meant to
 * represent a non-linear and constantly non static neural network. This class
 * doesn't handle any of the logic needed to create a {@link Brain} from
 * scratch, or any logic needed to mutate the structure.
 * 
 * A brain is made up of {@link Cluster}s of Neurons, these are linked together.
 * The brain passes inputs to them for processing and converts them into
 * outputs.The last integer array contains the information needed to
 * 
 * @author Sebastian Troy
 */
public class Brain
	{
		int age = 0;

		private int[][] genes;

		private BrainSocket[] brainInputs, brainOutputs;
		private int numInputsConnected = 0, numOutputsConnected = 0;

		private Cluster[] clusters;

		/**
		 * Creates a new {@link Brain} which represents an exact copy of the
		 * {@link Brain} that the genes describe.
		 * 
		 * @param genes
		 *            - an array of integer arrays. The last array represents
		 *            the connections between {@link Cluster}s.
		 */
		public Brain(int[][] genes)
			{
				this.genes = genes;

				clusters = new Cluster[genes.length - 2];

				// Create each cluster, the first arrays are all genes for
				// clusters
				for (int i = 0; i < clusters.length; i++)
					{
						clusters[i] = new Cluster(this, genes[i]);
					}

				// link the clusters, the penultimate array links each cluster
				// together
				for (int i = 0; i < genes[genes.length - 2].length; i += 4)
					{
						int[] links = genes[genes.length - 2];

						// format is 4 number groups {[clusterNumber,
						// outputNumber]connects to [clusterNumber,
						// inputNumber]}
						clusters[links[i]].outputs[links[i + 1]].connectedNeuron = clusters[links[i + 2]].inputs[links[i + 3]];
					}

				// The last array links the brain inputs to cluster inputs and
				// cluster outputs to brain outputs.
				
				int numInputLinks = genes[genes.length - 1][0], numOutputLinks = genes[genes.length - 1][1];
				
				// Deal with inputs
				for (int i = 0; i < numInputLinks; i++)
					;
				
				// Deal with outputs
				for (int i = 0; i < numOutputLinks; i++)
					;
			}

		/**
		 * The brain has a set number of inputs. Each input can be triggered and
		 * is guaranteed to pass a message on to any connected
		 * {@link Triggerable} objects. Only one message is ever sent per
		 * calculation, no matter how many messages are sent.
		 * 
		 * @return A {@link Triggerable} object. null if there are no more free
		 *         input spaces.
		 */
		public Triggerable getFreeBrainInput()
			{
				BrainSocket input = null;

				if (numInputsConnected < brainInputs.length)
					{
						input = brainInputs[numInputsConnected];
						numInputsConnected++;
					}

				return input;
			}

		/**
		 * 
		 * @return
		 */
		public boolean addToFreeBrainOutput(Triggerable effector)
			{
				boolean connected = false;

				if (numOutputsConnected < brainOutputs.length)
					{
						brainOutputs[numOutputsConnected] = new BrainSocket(effector);
						numOutputsConnected++;
					}

				return connected;
			}

		/**
		 * The brain sends any unsent messages between Neurons, which are
		 * {@link Triggerable}. The brain then ages by 1.
		 */
		public void calculate()
			{
				for (Cluster c : clusters)
					c.calculate();

				age++;
			}
	}