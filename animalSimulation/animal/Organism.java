package animalSimulation.animal;

import animalSimulation.animal.effectors.LeftFin;
import animalSimulation.animal.effectors.RightFin;
import animalSimulation.animal.effectors.Tail;
import animalSimulation.animal.sensors.Whisker;
import brain.Brain;
import brain.Effector;
import brain.Sensor;

public class Organism
	{
		public static final int NORTH = 0;
		public static final int SOUTH = 1;
		public static final int EAST = 2;
		public static final int WEST = 3;
		
		public int x, y, facing;
		
		private Brain brain;
		public Sensor[] sensors = {new Whisker(this)};
		public Effector[] effectors = {new LeftFin(this), new RightFin(this) ,new Tail(this)};
				
		//private
		
		public Organism()
			{
				x = y = 20;
				facing = NORTH;
			}
		
		public final void setBrain(Brain brain)
			{
				this.brain = brain;
			}
		
		public final void tick(double secondsPassed)
			{
				for (Sensor s : sensors)
					s.tick(secondsPassed);
				
				for (Effector e : effectors)
					e.tick(secondsPassed);
			}
	}
