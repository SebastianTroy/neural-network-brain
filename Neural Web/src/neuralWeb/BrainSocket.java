package neuralWeb;

/**
 * Creates an object that implements {@link Triggerable}. Its purpose is to
 * allow brain Inputs to be linked to cluster inputs and brain outputs to
 * contain a direct link to an external {@link Triggerable} object. It also
 * means that technically brain outputs can be linked to brain inputs if it is
 * cast as a {@link Triggerable} object.
 * 
 * @author Sebastian Troy
 */
public class BrainSocket implements Triggerable
	{
		private Triggerable connectedEffector;

		public BrainSocket(Triggerable connectedEffector)
			{
				this.connectedEffector = connectedEffector;
			}

		@Override
		public void trigger(int connectionWeight)
			{
				connectedEffector.trigger(1000);
			}
	}