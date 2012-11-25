package brain;

import java.util.ArrayList;

import TroysCode.Tools;

/**
 * This class is meant to mimic a very simple Neuron. It can be triggered by
 * another Neuron or a {@link Sensor}. When it has been triggered enough times
 * (its {@code triggerThreshold}) within a certain time limit (its
 * {@code triggerResetTime}) it will in turn trigger any Neurons or
 * {@link Effector}s it is connected to.
 * <p>
 * It implements Runnable and runs autonomously so long as it's parent brain is
 * alive.
 * 
 * @author Sebastian Troy
 */
public class Neuron implements Runnable, Triggerable
	{
		/** The brain that this {@link Neuron} is part of. */
		private Brain brain;
		/** connections may be other {@link Neuron}s or {@link Effector}s. */
		private Triggerable[] connections;

		/**
		 * The number of times a {@link Neuron} must be triggered, within the
		 * {@code triggerResetTime}, for it to 'fire' and trigger each of it's
		 * {@code connections}
		 */
		private int triggerThreshold;
		/** The number of times this {@link Neuron} has been triggered recently */
		private int triggerCount = 0;

		/**
		 * The length of time this {@link Neuron} will wait to be triggered
		 * again, before resetting the {@code triggerCount}.
		 */
		private double triggerResetTime;
		/**
		 * the age of the brain in seconds when this {@link Neuron} was last
		 * triggered
		 */
		private double timeOfLastTrigger = 0;
		/**
		 * the age of the brain in seconds when this {@link Neuron} last
		 * 'depolorised' or 'fired'
		 */
		private double timeOfLastDepolorisation = 0;

		/** This thread allows the neuron to run autonomously */
		private Thread thread = new Thread(this);

		/**
		 * @param brain
		 *            - The {@link Brain} this {@link Neuron} is part of
		 * @param triggerThreshold
		 *            - The number of times a {@link Neuron} must be triggered,
		 *            within the {@code triggerResetTime}, for it to 'fire' and
		 *            trigger each of it's {@code connections}
		 * @param triggerResetTime
		 *            - The length of time this {@link Neuron} will wait to
		 */
		public Neuron(Brain brain, int triggerThreshold, double triggerResetTime)
			{
				this.brain = brain;
				this.triggerThreshold = triggerThreshold > 1 ? triggerThreshold : 1;
				this.triggerResetTime = triggerResetTime > 0.01 ? triggerResetTime : 0.01;
			}

		/**
		 * This {@link Neuron} cannot be connected to itself at all, or
		 * connected to any other {@link Neuron} or {@link Effector} more than
		 * once.
		 * 
		 * @param connections
		 *            - The {@link Neuron}s and {@link Effector}s that we want
		 *            this {@link Neuron} to trigger when it 'fires' /
		 *            'depolorises'.
		 */
		public final void connectTo(ArrayList<Triggerable> newConnections)
			{
				ArrayList<Triggerable> tempNeuronArray = new ArrayList<Triggerable>(newConnections.size());

				for (Triggerable n : newConnections)
					if (n != this && !tempNeuronArray.contains(n))
						{
							tempNeuronArray.add(n);
						}

				connections = new Triggerable[tempNeuronArray.size()];
				tempNeuronArray.toArray(connections);
			}

		/**
		 * @return Returns the {@link Neuron}s and {@link Effector}s to which
		 *         this {@link Neuron} is connected;
		 */
		protected final Triggerable[] getConnections()
			{
				return connections;
			}

		/**
		 * Called by the {@link Brain} when it is ready to start.
		 */
		protected final void activate()
			{
				thread.start();
			}

		/**
		 * This method contains the logic that runs the {@link Neuron} for the
		 * duration of the life of the {@link Brain}.
		 */
		@Override
		public void run()
			{
				while (brain.alive)
					{
						if (brain.ageInSeconds - timeOfLastTrigger >= triggerResetTime)
							{
								triggerCount = 0;
							}

						if (triggerCount >= triggerThreshold)
							{
								for (Triggerable n : connections)
									n.trigger();

								timeOfLastDepolorisation = brain.ageInSeconds;
							}
						try
							{
								synchronized (thread)
									{
										thread.wait();
									}
							}
						catch (InterruptedException e)
							{
								Tools.errorWindow(e, "Neuron::run()");
							}
					}
			}

		/**
		 * Called when this {@link Neuron} is triggered by either another
		 * {@link Neuron} or a {@link Sensor}.
		 */
		@Override
		public final void trigger()
			{
				if (brain.ageInSeconds - timeOfLastDepolorisation >= brain.neuronRechargeTime)
					{
						triggerCount++;
						timeOfLastTrigger = brain.ageInSeconds;
						synchronized (thread)
							{
								thread.notify();
							}
					}
			}
	}