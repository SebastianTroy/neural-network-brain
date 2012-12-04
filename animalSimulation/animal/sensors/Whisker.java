package animalSimulation.animal.sensors;

import TroysCode.hub;
import animalSimulation.Environment;
import animalSimulation.animal.Organism;
import brain.Sensor;

public class Whisker extends Sensor
	{
		private Organism self;
		private double counter = 0;
		private double resetSpeed = 0.25;

		public Whisker(Organism self)
			{
				this.self = self;
			}

		@Override
		public void tick(double secondsPassed)
			{
				int x = self.x;
				int y = self.y;
				switch (self.facing)
					{
					case Organism.NORTH:
						y--;
						break;
					case Organism.SOUTH:
						y++;
						break;
					case Organism.EAST:
						x++;
						break;
					case Organism.WEST:
						x--;
						break;
					}
				if (counter < 0 && hub.environment.getEnvironmentAt(x, y) != Environment.EMPTY.getRGB())
					{
						counter = resetSpeed;
						sensorTriggered();
					}
				counter -= secondsPassed;
			}
		
		@Override
		public String getDescription()
			{
				return "Whisker";
			}
	}
