package animalSimulation.animal;

import TroysCode.Tools;
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

		public Brain brain;
		public Sensor[] sensors;
		public Effector[] effectors;

		public Organism(int x, int y)
			{
				this.x = x;
				this.y = y;

				sensors = new Sensor[1];
				sensors[0] = new Whisker(this);
				effectors = new Effector[3];
				effectors[0] = new LeftFin(this);
				effectors[1] = new RightFin(this);
				effectors[2] = new Tail(this);

				brain = new Brain(0, sensors, effectors);
				facing = Tools.randInt(NORTH, WEST);
			}

		public Organism(Organism o, int x, int y)
			{
				this.x = x;
				this.y = y;

				sensors = new Sensor[1];
				sensors[0] = new Whisker(this);
				sensors[0].setID(o.sensors[0]);
				
				effectors = new Effector[3];
				effectors[0] = new LeftFin(this);
				effectors[0].setID(o.effectors[0]);
				effectors[1] = new RightFin(this);
				effectors[1].setID(o.effectors[1]);
				effectors[2] = new Tail(this);
				effectors[2].setID(o.effectors[2]);

				this.brain = new Brain(o.brain, o.sensors, o.effectors);
				facing = Tools.randInt(NORTH, WEST);
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
