package animalSimulation.animal.sensors;

import TroysCode.hub;
import animalSimulation.Environment;
import animalSimulation.animal.Organism;
import brain.Sensor;

public class MonochromeEye
	{
		Organism organism;

		private Rod rod1;
		private Rod rod2;
		private Rod rod3;
		private Rod rod4;
		private Rod rod5;
		private Rod rod6;
		private Rod rod7;

		//TODO make monochrome from binary
		public MonochromeEye(Organism organism, double angleFacing)
			{
				this.organism = organism;

				rod1 = new Rod(angleFacing - 45);
				rod2 = new Rod(angleFacing - 27);
				rod3 = new Rod(angleFacing - 9);
				rod4 = new Rod(angleFacing); // extra actuity in the center of
												// the eye
				rod5 = new Rod(angleFacing + 9);
				rod6 = new Rod(angleFacing + 27);
				rod7 = new Rod(angleFacing + 45);
			}

		public final Sensor[] getSensors()
			{
				Sensor[] sensors = { rod1, rod2, rod3, rod4, rod5, rod6, rod7 };
				return sensors;
			}

		private class Rod extends Sensor
			{
				private double rodAngle, counter = 0;

				private Rod(double angle)
					{
						rodAngle = angle;
					}

				@Override
				public void tick(double secondsPassed)
					{
						if (counter < 0)
							{
								counter = 0.5;
								
								double angle = rodAngle;
								switch (organism.facing)
									{
									case Organism.SOUTH:
										angle += 180;
										break;
									case Organism.EAST:
										angle += 90;
										break;
									case Organism.WEST:
										angle -= 90;
										break;
									}

								double x = Math.sin(angle);
								double y = -Math.cos(angle);

								boolean objectInSight = false;
								for (double i = 0; i < 10; i++)
									{
										if (hub.environment.getEnvironmentAt((int) (x * i), (int) (y * i)) != Environment.EMPTY.getRGB())											{
												objectInSight = true;
												i = 10;
											}
									}

								if (objectInSight)
									sensorTriggered();
							}
						counter -= secondsPassed;
					}

				@Override
				public final String getDescription()
					{
						return "Rod: " + rodAngle;
					}
			}
	}
