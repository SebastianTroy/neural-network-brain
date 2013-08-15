package neuralWeb;

/**
 * Please note that this class is an example and while extending it to create
 * your own Brain Output classes will work, they should simply implement
 * neuralWeb.Triggerable.
 * 
 * @author Sebastian Troy
 */
public class Output implements Triggerable
	{

		@Override
		public void trigger()
			{
				// This method isnt triggered by brain outputs, it should only
				// be used to ensure that brain inputs definitely trigger their connection in the brain.
			}

		@Override
		public void trigger(int weight)
			{
				// Do something here
			}
	}