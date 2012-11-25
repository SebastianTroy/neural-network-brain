package animalSimulation.animal.effectors;

import TroysCode.hub;
import animalSimulation.animal.Organism;
import brain.Effector;

public class Tail extends Effector
	{
		private Organism self;
		private double speed = 0.25;
		private double counter = 0;

		public Tail(Organism self)
			{
				this.self = self;
			}

		@Override
		public void tick(double secondsPassed)
			{
				if (counter < 0 && hasBeenTriggered())
					{
						counter = speed;

						switch (self.facing)
							{
							case Organism.NORTH:
								if (hub.environment.isFree(self.x, self.y - 1))
									self.y--;
								break;
							case Organism.SOUTH:
								if (hub.environment.isFree(self.x, self.y + 1))
									self.y++;
								break;
							case Organism.EAST:
								if (hub.environment.isFree(self.x + 1, self.y))
									self.x++;
								break;
							case Organism.WEST:
								if (hub.environment.isFree(self.x - 1, self.y))
									self.x--;
								break;
							}
					}
				counter -= secondsPassed;
			}
		
		@Override
		public String getDescription()
			{
				return "Tail";
			}
	}