package testEnvironment;

import neuralWeb.Triggerable;

public class Output implements Triggerable
	{

	@Override
	public void trigger()
		{
			System.out.println("Triggered");
		}

	@Override
	public void trigger(int weight)
		{
			System.out.println("Triggered");
		}
	}