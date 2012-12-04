package animalSimulation.animal;

import java.util.ArrayList;

import TroysCode.Tools;
import TroysCode.hub;
import animalSimulation.Environment;
import animalSimulation.animal.effectors.LeftFin;
import animalSimulation.animal.effectors.RightFin;
import animalSimulation.animal.effectors.Tail;
import animalSimulation.animal.sensors.BinaryEye;
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
				facing = Tools.randInt(NORTH, WEST);

				// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
				ArrayList<Sensor> newSensors = new ArrayList<Sensor>();

				newSensors.add(new Whisker(this));
				Sensor[] rods = new BinaryEye(this, 45).getSensors();
				for (Sensor s : rods)
					newSensors.add(s);
				rods = new BinaryEye(this, -45).getSensors();
				for (Sensor s : rods)
					newSensors.add(s);

				sensors = new Sensor[newSensors.size()];
				newSensors.toArray(sensors);

				// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
				ArrayList<Effector> newEffectors = new ArrayList<Effector>();

				newEffectors.add(new LeftFin(this));
				newEffectors.add(new RightFin(this));
				newEffectors.add(new Tail(this));

				effectors = new Effector[newEffectors.size()];
				newEffectors.toArray(effectors);

				// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
				brain = new Brain(0, sensors, effectors);
			}

		public Organism(Organism o, int x, int y)
			{
				this.x = x;
				this.y = y;
				facing = Tools.randInt(NORTH, WEST);

				// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
				ArrayList<Sensor> newSensors = new ArrayList<Sensor>();

				newSensors.add(new Whisker(this));
				Sensor[] rods = new BinaryEye(this, 45).getSensors();
				for (Sensor s : rods)
					newSensors.add(s);
				rods = new BinaryEye(this, -45).getSensors();
				for (Sensor s : rods)
					newSensors.add(s);

				sensors = new Sensor[newSensors.size()];
				newSensors.toArray(sensors);

				// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
				ArrayList<Effector> newEffectors = new ArrayList<Effector>();

				newEffectors.add(new LeftFin(this));
				newEffectors.add(new RightFin(this));
				newEffectors.add(new Tail(this));

				effectors = new Effector[newEffectors.size()];
				newEffectors.toArray(effectors);

				// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
				for (int i = 0; i < sensors.length && i < o.sensors.length; i++)
					sensors[i].setID(o.sensors[i]);
				for (int i = 0; i < effectors.length && i < o.effectors.length; i++)
					effectors[i].setID(o.effectors[i]);
				brain = new Brain(o.brain, sensors, effectors);
			}

		public final void setBrain(Brain brain)
			{
				this.brain = brain;
			}

		public final void tick(double secondsPassed)
			{
				brain.tick(secondsPassed);

				for (Sensor s : sensors)
					s.tick(secondsPassed);

				for (Effector e : effectors)
					e.tick(secondsPassed);
			}

		public final void move(int xMod, int yMod)
			{
				if (hub.environment.getEnvironmentAt(x + xMod, y + yMod) == Environment.EMPTY.getRGB())
					{
						hub.environment.moveOrganism(x, y, xMod, yMod);
						x += xMod;
						y += yMod;
					}
			}
	}
