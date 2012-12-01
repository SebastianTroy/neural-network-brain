package animalSimulation.animal.effectors;

import TroysCode.hub;
import animalSimulation.animal.Organism;
import brain.Effector;

public class Tail extends Effector
	{
		private Organism organism;
		private double speed = 0.25;
		private double counter = 0;

		public Tail(Organism self)
			{
				this.organism = self;
			}

		@Override
		public void tick(double secondsPassed)
			{
				if (hasBeenTriggered() && counter < 0)
					{
						counter = speed;

						switch (organism.facing)
							{
							case Organism.NORTH:
								organism.move(0, -1);
								break;
							case Organism.SOUTH:
								organism.move(0, 1);
								break;
							case Organism.EAST:
								organism.move(1, 0);
								break;
							case Organism.WEST:
								organism.move(-1, 0);
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