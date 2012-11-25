package animalSimulation.animal.effectors;

import animalSimulation.animal.Organism;
import brain.Effector;

public class LeftFin extends Effector
	{
		private Organism self;
		private double speed = 0.2;
		private double counter = 0;

		public LeftFin(Organism self)
			{
				this.self = self;
			}

		@Override
		public void tick(double secondsPassed)
			{
				if (counter < 0 && hasBeenTriggered())
					switch (self.facing)
						{
						default:
							counter = speed;
						case Organism.NORTH:
							self.facing = Organism.WEST;
							break;
						case Organism.SOUTH:
							self.facing = Organism.EAST;
							break;
						case Organism.EAST:
							self.facing = Organism.NORTH;
							break;
						case Organism.WEST:
							self.facing = Organism.SOUTH;
							break;
						}
				counter -= secondsPassed;
			}
		
		@Override
		public String getDescription()
			{
				return "Left Fin";
			}
	}
