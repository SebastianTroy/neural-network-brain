package neuralWeb;

public interface Triggerable
	{
		/**
		 * Use this method to ensure that the object is definitely triggered.
		 */
		void trigger();

		/**
		 * <div>All {@link Brain} inputs require a weight of > 0, in order to
		 * ensure that they are triggered.</div>All {@link Brain} outputs fire
		 * with a weight of 1000.
		 * 
		 * @param weight
		 *            - <div>The weight used to calculate whether this object is
		 *            triggered. If this object is a Neuron within the
		 *            {@link Brain} then the weight must be greater than this
		 *            objects <code>threshold</code>.
		 */
		void trigger(int weight);
	}
