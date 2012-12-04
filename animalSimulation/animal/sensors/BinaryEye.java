package animalSimulation.animal.sensors;

import TroysCode.hub;
import animalSimulation.Environment;
import animalSimulation.animal.Organism;
import brain.Sensor;

public class BinaryEye
	{
		Organism organism;

		private Rod rod1;
		private Rod rod2;
		private Rod rod3;
		private Rod rod4;
		private Rod rod5;
		private Rod rod6;
		private Rod rod7;

		public BinaryEye(Organism organism, double angleFacingDegreesZeroNorth)
			{
				this.organism = organism;
				angleFacingDegreesZeroNorth = Math.toRadians(angleFacingDegreesZeroNorth);

				rod1 = new Rod(angleFacingDegreesZeroNorth - 0.523598776);
				rod2 = new Rod(angleFacingDegreesZeroNorth - 0.34906585);
				rod3 = new Rod(angleFacingDegreesZeroNorth - 0.174532925);
				rod4 = new Rod(angleFacingDegreesZeroNorth);
				rod5 = new Rod(angleFacingDegreesZeroNorth + 0.174532925);
				rod6 = new Rod(angleFacingDegreesZeroNorth + 0.34906585);
				rod7 = new Rod(angleFacingDegreesZeroNorth + 0.523598776);
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
								counter = 0.20;

								double angle = rodAngle;
								switch (organism.facing)
									{
									case Organism.SOUTH:
										angle += 3.14159265;
										break;
									case Organism.EAST:
										angle += 1.57079633;
										break;
									case Organism.WEST:
										angle -= 1.57079633;
										break;
									}

								double x = Math.sin(angle);
								double y = -Math.cos(angle);

								boolean objectInSight = false;
								for (double i = 0; i < 10; i++)
									{
										if (Math.abs((int) (x * i) + (int) (y * i)) > 0)
											if (hub.environment.getEnvironmentAt(organism.x + (int) (x * i), organism.y + (int) (y * i)) != Environment.EMPTY
													.getRGB())
												{
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
						return "Rod: " + (int) (Math.toDegrees(rodAngle));
					}
			}
	}
