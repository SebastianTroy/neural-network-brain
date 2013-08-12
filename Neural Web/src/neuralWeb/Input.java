package neuralWeb;

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