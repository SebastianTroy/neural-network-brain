package animalSimulation.animal.sensors;

import TroysCode.hub;
import animalSimulation.animal.Organism;
import brain.Sensor;

public class Whisker extends Sensor
	{
		Organism self;

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
				if (!hub.environment.isFree(x, y))
					sensorTriggered();
			}

	}
