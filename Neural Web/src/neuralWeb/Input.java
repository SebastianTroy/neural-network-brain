package neuralWeb;

/**
 * Please note that this class is an example and while extending it to create
 * your own Brain Input classes will work, they should simply implement
 * {@link Triggerable}, also they should hold an instance of a
 * {@link Triggerable} object, this is the connection that it has to the
 * {@link NeuralWeb} and can be obtained by calling
 * myNeuralWeb.getBrainInput(connectionIndex) where "connectionIndex" refers to
 * the connection into the brain you want to connect to.
 * 
 * @author Sebastian Troy
 */
public class Input implements Triggerable
	{
		private Triggerable connection;

		public Input()
			{}

		public Input(NeuralWeb neuralWeb, int connectionIndex)
			{
				connection = neuralWeb.getBrainInput(connectionIndex);
				if (connection == null)
					System.out.println("NULL CONNECTION AT INPUT: index = " + connectionIndex);
			}

		@Override
		public final void trigger()
			{
				connection.trigger();
			}

		@Override
		public final void trigger(int weight)
			{
				connection.trigger(weight);
			}

		public final void setConnection(NeuralWeb neuralWeb, int connectionIndex)
			{
				connection = neuralWeb.getBrainInput(connectionIndex);
				if (connection == null)
					System.out.println("NULL CONNECTION AT INPUT: index = " + connectionIndex);
			}
	}