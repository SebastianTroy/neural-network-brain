package brain;

import TroysCode.Tools;

public class Signal implements Runnable
	{
		double signalDelay;
		Triggerable signalDestination;
		boolean inhibitSignal;

		Thread thread = new Thread(this);

		public Signal(double secondsDelay, Triggerable neuron, boolean inhibit)
			{
				signalDelay = secondsDelay;
				signalDestination = neuron;
				this.inhibitSignal = inhibit;

				thread.run();
			}

		@Override
		public void run()
			{
				try
					{
						synchronized (thread)
							{
								thread.wait((long) (1000 * signalDelay) + 1);
							}
					}
				catch (InterruptedException e)
					{
						Tools.errorWindow(e, "Signal::run()");
					}

				if (inhibitSignal)
					signalDestination.inhibit();
				else
					signalDestination.trigger();
			}
	}