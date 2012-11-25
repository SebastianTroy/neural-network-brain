package brain.addons;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import brain.Brain;
import brain.Effector;
import brain.Neuron;
import brain.Sensor;
import brain.Triggerable;

import TroysCode.RenderableObject;
import TroysCode.Tools;
import TroysCode.hub;
import TroysCode.T.TScrollEvent;

public class VisualBrainCreator extends RenderableObject
	{
		private static final long serialVersionUID = 1L;

		private final int TYPE_SENSOR = 0;
		private final int TYPE_NEURON = 1;
		private final int TYPE_EFFECTOR = 2;
		// private ArrayList<Node> nodes = new ArrayList<Node>();
		private ArrayList<Node> neuronNodes = new ArrayList<Node>();
		private ArrayList<Node> effectorNodes = new ArrayList<Node>();
		private ArrayList<Node> sensorNodes = new ArrayList<Node>();

		private int mouseX = 0;
		private int mouseY = 0;

		private boolean placeNode = false;
		private boolean removeNode = false;
		private boolean connectNode = false;
		private boolean moveNode = false;
		private boolean clearConnections = false;

		private Node selectedNode = new Node(TYPE_NEURON, 0, 0);
		private Node secondNode = new Node(TYPE_NEURON, 0, 0);

		private boolean showHelp = true;

		private Sensor[] sensors;
		private Effector[] effectors;

		@Override
		protected void initiate()
			{
			}

		@Override
		protected void refresh()
			{
			}

		@Override
		protected void tick(double secondsPassed)
			{
				if (placeNode)
					{
						neuronNodes.add(new Node(TYPE_NEURON, mouseX, mouseY));
						placeNode = false;
					}
				if (removeNode)
					{
						if (neuronNodes.contains(selectedNode) && neuronNodes.size() > 1)
							{
								for (Node n : sensorNodes)
									{
										n.connections.remove(selectedNode);
									}
								for (Node n : neuronNodes)
									{
										n.connections.remove(selectedNode);
									}
								for (Node n : effectorNodes)
									{
										n.connections.remove(selectedNode);
									}

								neuronNodes.remove(selectedNode);

								selectedNode = new Node(TYPE_NEURON, 0, 0);
							}

						removeNode = false;
					}
				if (connectNode)
					{
						if ((sensorNodes.contains(selectedNode) || neuronNodes.contains(selectedNode)
								&& (neuronNodes.contains(secondNode) || effectorNodes.contains(secondNode))))
							{
								selectedNode.connections.add(secondNode);
							}

						connectNode = false;
					}
				else if (moveNode)
					{
						if (mouseX < 3 || mouseY < 3 || mouseX > 777 || mouseY > 556)
							{
								if (neuronNodes.size() > 1 && neuronNodes.contains(selectedNode))
									removeNode = true;
								else
									return;
							}

						selectedNode.x = mouseX;
						selectedNode.y = mouseY;
						moveNode = false;
					}
				if (clearConnections)
					{
						for (Node n : sensorNodes)
							{
								n.connections.remove(selectedNode);
							}
						for (Node n : neuronNodes)
							{
								n.connections.remove(selectedNode);
							}
						for (Node n : effectorNodes)
							{
								n.connections.remove(selectedNode);
							}
						selectedNode.connections.clear();
						clearConnections = false;
					}
			}

		@Override
		protected void renderObject(Graphics g)
			{
				if (showHelp)
					{
						g.setColor(Color.GRAY);
						g.fillRect(0, 0, 800, 600);
						g.setColor(Color.BLACK);
						g.fillRect(3, 3, 777, 556);

						int i = 0;
						g.setColor(Color.WHITE);
						g.drawString("Click on a Neuron and drag to move it around (purely cosmetic!).", 4, ++i * 15);
						g.drawString("Right click on another Neuron to connect it to the selected Neuron.", 4, ++i * 15);
						g.drawString("Press 'd' to delete the selected Neuron.", 4, ++i * 15);
						g.drawString("Press 'c' to clear the selected Neuron's Connections.", 4, ++i * 15);
						g.drawString("Press 'm' to go to main menu.", 4, ++i * 15);
						g.drawString("Press 'h' to show/hide the help.", 4, ++i * 15);
					}
				else
					{
						g.setColor(Color.GRAY);
						g.fillRect(0, 0, 800, 600);
						g.setColor(Color.BLACK);
						g.fillRect(3, 3, 777, 556);

						g.setColor(Color.GREEN);
						g.drawString("Sensors Attached", 4, 14);
						g.setColor(Color.BLUE);
						g.drawString("Neurons", 4, 290);
						g.setColor(Color.RED);
						g.drawString("Effectors Attached", 4, 556);

						int i = 0;
						for (Node n : sensorNodes)
							{
								if (n.selected)
									{
										g.setColor(Color.WHITE);
										g.fillOval(n.x - 7, n.y - 7, 14, 14);
									}
								g.setColor(Color.YELLOW);
								g.fillOval(n.x - 6, n.y - 6, 12, 12);

								g.setColor(Color.BLACK);
								g.drawString("" + i++, n.x - 3, n.y + 5);
							}
						i = 0;
						for (Node n : neuronNodes)
							{
								if (n.selected)
									{
										g.setColor(Color.WHITE);
										g.fillOval(n.x - 7, n.y - 7, 14, 14);
									}

								g.setColor(Color.BLUE);
								g.fillOval(n.x - 6, n.y - 6, 12, 12);

								g.setColor(Color.WHITE);
								g.drawString("" + i++, n.x - 3, n.y + 5);
							}
						i = 0;
						for (Node n : effectorNodes)
							{
								if (n.selected)
									{
										g.setColor(Color.WHITE);
										g.fillOval(n.x - 7, n.y - 7, 14, 14);
									}
								g.setColor(Color.RED);
								g.fillOval(n.x - 6, n.y - 6, 12, 12);

								g.setColor(Color.WHITE);
								g.drawString("" + i++, n.x - 3, n.y + 5);
							}

						g.setColor(Color.GREEN);
						for (Node node : sensorNodes)
							for (Node connection : node.connections)
								Tools.drawArrow(node.x, node.y, connection.x, connection.y, g, 15);
						for (Node node : neuronNodes)
							for (Node connection : node.connections)
								Tools.drawArrow(node.x, node.y, connection.x, connection.y, g, 15);
						for (Node node : effectorNodes)
							for (Node connection : node.connections)
								Tools.drawArrow(node.x, node.y, connection.x, connection.y, g, 15);
					}
			}

		public final void newBrain(Sensor[] sensors, Effector[] effectors)
			{
				sensorNodes.clear();
				neuronNodes.clear();
				effectorNodes.clear();

				this.sensors = sensors;
				this.effectors = effectors;

				neuronNodes.add(new Node(TYPE_NEURON, 400, 300));

				for (int s = 0, num = 0; s < sensors.length; s++, num++)
					{
						sensorNodes.add(new Node(TYPE_SENSOR, 25 + (s * (750 / sensors.length)) + Tools.randInt(0, 7), Tools.randInt(18, 58)));
					}
				for (int e = 0, num = 0; e < effectors.length; e++, num++)
					{
						effectorNodes.add(new Node(TYPE_EFFECTOR, 25 + (e * (750 / effectors.length)) + Tools.randInt(0, 7), Tools.randInt(492, 532)));
					}
			}

		public final Brain generateBrain()
			{
				Node[] nodes = new Node[sensorNodes.size() + neuronNodes.size() + effectorNodes.size()];
				int nodeNum = 0;
				for (Node n : neuronNodes)
					{
						nodes[nodeNum] = n;
						nodeNum++;
					}
				for (Node n : sensorNodes)
					{
						nodes[nodeNum] = n;
						nodeNum++;
					}
				for (Node n : effectorNodes)
					{
						nodes[nodeNum] = n;
						nodeNum++;
					}

				Brain brain = new Brain(0);
				Neuron[] neurons = new Neuron[nodes.length];
				ArrayList<ArrayList<Triggerable>> connections = new ArrayList<ArrayList<Triggerable>>(neurons.length);
				for (int i = 0; i < neurons.length; i++)
					connections.add(new ArrayList<Triggerable>());

				for (int i = 0; i < nodes.length; i++)
					{
						nodes[i].neuron = new Neuron(brain, 1, 2.5);
						neurons[i] = nodes[i].neuron;
					}
				
				for (int i = 0; i < nodes.length; i++)
					for (int j = 0; j < nodes[i].connections.size(); j++)
						switch (nodes[i].connections.get(j).type)
							{
							case TYPE_NEURON:
								connections.get(i).add(nodes[i].connections.get(j).neuron);
								break;
							case TYPE_EFFECTOR:
								boolean foundEffector = false;
								for (int effectorNum = 0; !foundEffector; effectorNum++)
									if (effectorNodes.get(effectorNum) == nodes[i].connections.get(j))
										{
											connections.get(i).add(effectors[effectorNum]);
											foundEffector = true;
										}
								break;
							}

				for (int i = 0; i < sensors.length; i++)
					{
						sensors[i].linkToNeuron(sensorNodes.get(i).neuron);
					}

				brain.createBrain(neurons, connections);

				return brain;
			}

		@Override
		protected void mousePressed(MouseEvent event)
			{
				if (event.getButton() == MouseEvent.BUTTON1)
					{
						for (Node n : sensorNodes)
							if (Tools.getVectorLength(n.x, n.y, event.getX(), event.getY()) < 6)
								{
									n.selected = true;
									selectedNode = n;
								}
							else
								n.selected = false;
						for (Node n : neuronNodes)
							if (Tools.getVectorLength(n.x, n.y, event.getX(), event.getY()) < 6)
								{
									n.selected = true;
									selectedNode = n;
								}
							else
								n.selected = false;
						for (Node n : effectorNodes)
							if (Tools.getVectorLength(n.x, n.y, event.getX(), event.getY()) < 6)
								{
									n.selected = true;
									selectedNode = n;
								}
							else
								n.selected = false;

						if (!selectedNode.selected)
							{
								selectedNode = new Node(TYPE_NEURON, 0, 0);
							}
					}
				else if (event.getButton() == MouseEvent.BUTTON3)
					{
						for (Node n : sensorNodes)
							if (Tools.getVectorLength(n.x, n.y, event.getX(), event.getY()) < 6)
								{
									secondNode = n;
									if (selectedNode.type != TYPE_EFFECTOR && secondNode.type != TYPE_SENSOR)
										connectNode = true;
								}
						for (Node n : neuronNodes)
							if (Tools.getVectorLength(n.x, n.y, event.getX(), event.getY()) < 6)
								{
									secondNode = n;
									if (selectedNode.type != TYPE_EFFECTOR && secondNode.type != TYPE_SENSOR)
										connectNode = true;
								}
						for (Node n : effectorNodes)
							if (Tools.getVectorLength(n.x, n.y, event.getX(), event.getY()) < 6)
								{
									secondNode = n;
									if (selectedNode.type != TYPE_EFFECTOR && secondNode.type != TYPE_SENSOR)
										connectNode = true;
								}
					}
			}

		@Override
		protected void mouseReleased(MouseEvent event)
			{

			}

		@Override
		protected void mouseDragged(MouseEvent event)
			{
				mouseX = event.getX();
				mouseY = event.getY();

				moveNode = true;
			}

		@Override
		protected void mouseMoved(MouseEvent event)
			{
				mouseX = event.getX();
				mouseY = event.getY();
			}

		@Override
		protected void mouseWheelScrolled(MouseWheelEvent event)
			{

			}

		@Override
		protected void actionPerformed(ActionEvent event)
			{

			}

		@Override
		protected void keyPressed(KeyEvent event)
			{
				if (event.getKeyChar() == 'n')
					placeNode = true;
				else if (event.getKeyChar() == 'd')
					removeNode = true;
				else if (event.getKeyChar() == 'c')
					clearConnections = true;
				else if (event.getKeyChar() == 'h')
					showHelp = !showHelp;
				else if (event.getKeyChar() == 'm')
					changeRenderableObject(hub.menu);
			}

		@Override
		protected void keyReleased(KeyEvent event)
			{

			}

		@Override
		protected void keyTyped(KeyEvent event)
			{

			}

		@Override
		protected void mouseClicked(MouseEvent event)
			{

			}

		@Override
		protected void mouseEntered(MouseEvent event)
			{

			}

		@Override
		protected void mouseExited(MouseEvent event)
			{

			}

		@Override
		protected void programGainedFocus(WindowEvent event)
			{

			}

		@Override
		protected void programLostFocus(WindowEvent event)
			{

			}

		@Override
		protected void frameResized(ComponentEvent event)
			{

			}

		@Override
		public void tScrollBarScrolled(TScrollEvent event)
			{

			}

		private class Node
			{
				public int type, x, y;
				public boolean selected = false;
				public Neuron neuron;

				public ArrayList<Node> connections = new ArrayList<Node>();

				private Node(int type, int x, int y)
					{
						this.type = type;
						this.x = x;
						this.y = y;
					}
			}
	}
