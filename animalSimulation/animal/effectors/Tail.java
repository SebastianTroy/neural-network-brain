package animalSimulation.animal.effectors;

import animalSimulation.animal.Organism;
import brain.Effector;

public class Tail extends Effector
	{
		private Organism organism;

		public Tail(Organism self)
			{
				this.organism = self;
			}

		@Override
		public void tick(double secondsPassed)
			{
				if (hasBeenTriggered())
					{
						switch (organism.facing)
							{
							case Organism.NORTH:
								organism.move(0,  -1);
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
			}

		@Override
		public String getDescription()
			{
				return "Tail";
			}
	}