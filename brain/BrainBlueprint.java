package brain;

import java.util.ArrayList;

import TroysCode.Tools;

public class BrainBlueprint
	{
		public static final int TYPE_SENSOR = 0;
		public static final int TYPE_NEURON = 1;
		public static final int TYPE_EFFECTOR = 2;

		public ArrayList<NeuronBlueprint> neuronNodes = new ArrayList<NeuronBlueprint>();
		public ArrayList<NeuronBlueprint> effectorNodes = new ArrayList<NeuronBlueprint>();
		public ArrayList<NeuronBlueprint> sensorNodes = new ArrayList<NeuronBlueprint>();

		private Sensor[] sensors;
		private Effector[] effectors;

		public BrainBlueprint(Sensor[] sensors, Effector[] effectors)
			{
				this.sensors = sensors;
				this.effectors = effectors;

				neuronNodes.add(new NeuronBlueprint(TYPE_NEURON, 400, 300));

				for (int s = 0; s < sensors.length; s++)
					{
						NeuronBlueprint newSensorNode = new NeuronBlueprint(TYPE_SENSOR, 25 + (s * (750 / sensors.length)) + Tools.randInt(0, 7), Tools.randInt(18, 58));
						newSensorNode.ID = sensors[s].getID();
						sensorNodes.add(newSensorNode);
					}
				for (int e = 0; e < effectors.length; e++)
					{
						NeuronBlueprint newEffectorNode = new NeuronBlueprint(TYPE_EFFECTOR, 25 + (e * (750 / effectors.length)) + Tools.randInt(0, 7), Tools.randInt(492, 532));
						newEffectorNode.ID = effectors[e].getID();
						effectorNodes.add(newEffectorNode);
					}

				for (int i = 0; i < sensors.length; i++)
					sensorNodes.get(i).description = sensors[i].getDescription();

				for (int i = 0; i < effectors.length; i++)
					effectorNodes.get(i).description = effectors[i].getDescription();
			}

		public BrainBlueprint(BrainBlueprint basePrints, Sensor[] sensors, Effector[] effectors)
			{
				this.sensors = sensors;
				this.effectors = effectors;
				
				// make a this a copy of the old blueprints
				basePrints.getNodesCopy(sensorNodes, neuronNodes, effectorNodes);
				
				// remove unused sensors and effectors
				for (Sensor s : basePrints.sensors)
					if (!containsSensor(sensors, s))
						for (int i = 0; i < sensorNodes.size(); i++)
							if (sensorNodes.get(i).ID == s.getID())
								{
									sensorNodes.remove(i);
									i = sensorNodes.size();
								}
				for (Effector e : basePrints.effectors)
					if (!containsEffector(effectors, e))
						for (int i = 0; i < sensorNodes.size(); i++)
							if (effectorNodes.get(i).ID == e.getID())
								{
									effectorNodes.remove(i);
									i = effectorNodes.size();
								}

				// add new sensors and effectors
				for (Sensor s : sensors)
					if (!containsSensor(basePrints.sensors, s))
						{
							NeuronBlueprint node = new NeuronBlueprint(TYPE_SENSOR, Tools.randInt(25, 750), Tools.randInt(18, 58));
							node.ID = s.getID();
							node.description = s.getDescription();
							node.inhibitor = false;
							sensorNodes.add(node);
						}
				for (Effector e : effectors)
					if (!containsEffector(basePrints.effectors, e))
						{
							NeuronBlueprint node = new NeuronBlueprint(TYPE_SENSOR, Tools.randInt(25, 750), Tools.randInt(492, 532));
							node.ID = e.getID();
							node.description = e.getDescription();
							effectorNodes.add(node);
						}

				for (int i = 0; i < sensors.length; i++)
					sensorNodes.get(i).description = sensors[i].getDescription();

				for (int i = 0; i < effectors.length; i++)
					effectorNodes.get(i).description = effectors[i].getDescription();
			}

		/**
		 * <strong>WARNING:
		 * <p>
		 * DO NOT EDIT THESE {@link Sensor}s</strong>
		 * 
		 * @return
		 */
		public final Sensor[] getSensors()
			{
				return sensors;
			}

		/**
		 * <strong>WARNING:
		 * <p>
		 * DO NOT EDIT THESE {@link Effector}s</strong>
		 * 
		 * @return
		 */
		public final Effector[] getEffectors()
			{
				return effectors;
			}

		private final boolean containsSensor(Sensor[] sensors, Sensor s)
			{
				for (Sensor s2 : sensors)
					if (s.getID() == s2.getID())
						return true;

				return false;
			}

		private final boolean containsEffector(Effector[] effectors, Effector e)
			{
				for (Effector e2 : effectors)
					if (e.getID() == e2.getID())
						return true;

				return false;
			}

		private final void getNodesCopy(ArrayList<NeuronBlueprint> newSensors, ArrayList<NeuronBlueprint> newNeurons, ArrayList<NeuronBlueprint> newEffectors)
			{
				for (NeuronBlueprint s : sensorNodes)
					{
						NeuronBlueprint node = new NeuronBlueprint(TYPE_SENSOR, s.x, s.y);
						node.ID = s.ID;
						node.description = s.description;
						node.inhibitor = s.inhibitor;
						node.setThreshold(s.getThreshold());
						newSensors.add(node);
					}
				for (NeuronBlueprint n : neuronNodes)
					{
						NeuronBlueprint node = new NeuronBlueprint(TYPE_NEURON, n.x, n.y);
						node.ID = n.ID;
						node.description = n.description;
						node.inhibitor = n.inhibitor;
						node.setThreshold(n.getThreshold());
						newNeurons.add(node);
					}
				for (NeuronBlueprint e : effectorNodes)
					{
						NeuronBlueprint node = new NeuronBlueprint(TYPE_EFFECTOR, e.x, e.y);
						node.ID = e.ID;
						node.description = e.description;
						node.inhibitor = e.inhibitor;
						node.setThreshold(e.getThreshold());
						newEffectors.add(node);
					}

				for (int i = 0; i < sensorNodes.size(); i++)
					for (int j = 0; j < sensorNodes.get(i).connections.size(); j++)
						{
							NeuronBlueprint connectedNode = null;
							switch (sensorNodes.get(i).connections.get(j).type)
								{
								case TYPE_SENSOR:
									for (int num = 0; num < sensorNodes.size(); num++)
										if (sensorNodes.get(i).connections.get(j) == sensorNodes.get(num))
											connectedNode = newSensors.get(num);
									break;
								case TYPE_NEURON:
									for (int num = 0; num < neuronNodes.size(); num++)
										if (sensorNodes.get(i).connections.get(j) == neuronNodes.get(num))
											connectedNode = newNeurons.get(num);
									break;
								case TYPE_EFFECTOR:
									for (int num = 0; num < effectorNodes.size(); num++)
										if (sensorNodes.get(i).connections.get(j) == effectorNodes.get(num))
											connectedNode = newEffectors.get(num);
									break;
								}
							if (connectedNode == null)
								System.out.println("NODE NOT FOUND, COULD NOT CONNECT");
							else
								newSensors.get(i).connections.add(connectedNode);
						}
				for (int i = 0; i < neuronNodes.size(); i++)
					for (int j = 0; j < neuronNodes.get(i).connections.size(); j++)
						{
							NeuronBlueprint connectedNode = null;
							switch (neuronNodes.get(i).connections.get(j).type)
								{
								case TYPE_SENSOR:
									for (int num = 0; num < sensorNodes.size(); num++)
										if (neuronNodes.get(i).connections.get(j) == sensorNodes.get(num))
											connectedNode = newSensors.get(num);
									break;
								case TYPE_NEURON:
									for (int num = 0; num < neuronNodes.size(); num++)
										if (neuronNodes.get(i).connections.get(j) == neuronNodes.get(num))
											connectedNode = newNeurons.get(num);
									break;
								case TYPE_EFFECTOR:
									for (int num = 0; num < effectorNodes.size(); num++)
										if (neuronNodes.get(i).connections.get(j) == effectorNodes.get(num))
											connectedNode = newEffectors.get(num);
									break;
								}
							if (connectedNode == null)
								System.out.println("NODE NOT FOUND, COULD NOT CONNECT");
							else
								newNeurons.get(i).connections.add(connectedNode);
						}
			}

		public final void addNeuronNode(NeuronBlueprint n, boolean inhibitor)
			{
				neuronNodes.add(n);
				n.inhibitor = inhibitor;
			}

		public final void removeNeuronNode(NeuronBlueprint node)
			{
				if (neuronNodes.contains(node) && neuronNodes.size() > 1)
					{
						for (NeuronBlueprint n : sensorNodes)
							{
								n.connections.remove(node);
							}
						for (NeuronBlueprint n : neuronNodes)
							{
								n.connections.remove(node);
							}
						for (NeuronBlueprint n : effectorNodes)
							{
								n.connections.remove(node);
							}

						neuronNodes.remove(node);
					}

			}

		public final void makeConnection(NeuronBlueprint n1, NeuronBlueprint n2)
			{
				if (n1 != n2 && n1.type != BrainBlueprint.TYPE_EFFECTOR && n2.type != BrainBlueprint.TYPE_SENSOR
						&& (sensorNodes.contains(n1) || neuronNodes.contains(n1) && (neuronNodes.contains(n2) || effectorNodes.contains(n2))))
					{
						n1.connections.add(n2);
					}
			}

		public final void clearConnections(NeuronBlueprint node)
			{
				for (NeuronBlueprint n : sensorNodes)
					{
						n.connections.remove(node);
					}
				for (NeuronBlueprint n : neuronNodes)
					{
						n.connections.remove(node);
					}
				for (NeuronBlueprint n : effectorNodes)
					{
						n.connections.remove(node);
					}
				node.connections.clear();
			}
	}