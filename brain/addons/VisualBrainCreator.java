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
		private ArrayList<Node> nodes = new ArrayList<Node>();

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
						nodes.add(new Node(TYPE_NEURON, mouseX, mouseY));

						for (int i = 0, num = 0; i < nodes.size(); i++)
							{
								if (nodes.get(i).type == TYPE_NEURON)
									{
										nodes.get(i).setID(num);
										num++;
									}
							}

						placeNode = false;
					}
				if (removeNode)
					{
						if (selectedNode.type == TYPE_NEURON && selectedNode.ID != 0)
							{
								for (Node n : nodes)
									{
										n.connections.remove(selectedNode);
									}
								if (selectedNode.type == TYPE_NEURON)
									nodes.remove(selectedNode);
								selectedNode = new Node(TYPE_NEURON, 0, 0);

								int numNeurons = 0;
								for (Node n : nodes)
									if (n.type == TYPE_NEURON)
										numNeurons++;

								if (numNeurons == 0)
									nodes.add(new Node(TYPE_NEURON, 400, 300));

								for (int i = 0, num = 0; i < nodes.size(); i++)
									{
										if (nodes.get(i).type == TYPE_NEURON)
											{
												nodes.get(i).setID(num);
												num++;
											}
									}
							}

						removeNode = false;
					}
				if (connectNode)
					{
						if (nodes.contains(selectedNode) && nodes.contains(secondNode) && selectedNode != secondNode && secondNode.type != TYPE_SENSOR
								&& !selectedNode.connections.contains(secondNode))
							{
								selectedNode.connections.add(secondNode);
							}

						connectNode = false;
					}
				else if (moveNode)
					{
						if (mouseX < 3 || mouseY < 3 || mouseX > 777 || mouseY > 556)
							{
								if (selectedNode.type == TYPE_NEURON && selectedNode.ID != 0)
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
						for (Node n : nodes)
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

						for (Node node : nodes)
							{
								if (node.selected)
									{
										g.setColor(Color.WHITE);
										g.fillOval(node.x - 7, node.y - 7, 14, 14);
									}
								switch (node.type)
									{
									case (TYPE_SENSOR):
										g.setColor(Color.YELLOW);
										g.fillOval(node.x - 6, node.y - 6, 12, 12);

										g.setColor(Color.BLACK);
										g.drawString("" + node.ID, node.x - 3, node.y + 5);

										break;
									case (TYPE_NEURON):
										g.setColor(Color.BLUE);
										g.fillOval(node.x - 6, node.y - 6, 12, 12);

										if (node.ID == 0)
											{
												g.setColor(Color.BLACK);
												g.drawString("" + node.ID, node.x - 3, node.y + 5);
											}
										break;
									case (TYPE_EFFECTOR):
										g.setColor(Color.RED);
										g.fillOval(node.x - 6, node.y - 6, 12, 12);

										g.setColor(Color.WHITE);
										g.drawString("" + node.ID, node.x - 3, node.y + 5);
										break;
									}
							}

						g.setColor(Color.GREEN);
						for (Node node : nodes)
							for (Node connection : node.connections)
								Tools.drawArrow(node.x, node.y, connection.x, connection.y, g, 15);
					}
			}

		public final void newBrain(Sensor[] sensors, Effector[] effectors)
			{
				nodes.clear();

				this.sensors = sensors;
				this.effectors = effectors;

				nodes.add(new Node(TYPE_NEURON, 400, 300));

				for (int s = 0, num = 0; s < sensors.length; s++, num++)
					{
						nodes.add(new Node(TYPE_SENSOR, 25 + (s * (750 / sensors.length)) + Tools.randInt(0, 7), Tools.randInt(18, 58)));
						nodes.get(s).setID(num);
					}
				for (int e = 0, num = 0; e < effectors.length; e++, num++)
					{
						nodes.add(new Node(TYPE_EFFECTOR, 25 + (e * (750 / effectors.length)) + Tools.randInt(0, 7), Tools.randInt(492, 532)));
						nodes.get(e + sensors.length).setID(num);
					}
				nodes.get(0).setID(0);
			}

		public final Brain generateBrain()
			{
				Brain brain = new Brain(1);

				Neuron[] neurons = new Neuron[nodes.size()];
				Triggerable[][] connections = new Triggerable[nodes.size()][];

				for (int i = 0; i < nodes.size(); i++)
					{
						neurons[i] = new Neuron(brain, 1, 1.5);

						connections[i] = new Triggerable[nodes.get(i).connections.size()];
						for (int j = 0; j < nodes.get(i).connections.size(); j++)
							{
								switch (nodes.get(i).connections.get(j).type)
									{
									case TYPE_NEURON:
										connections[i][j] = effectors[nodes.get(i).ID];
										break;
									case TYPE_EFFECTOR:
										connections[i][j] = effectors[nodes.get(i).ID];
										break;
									}
							}
					}

				for (int i = 0; i <= sensors.length; i++)
					{
						sensors[i].linkToNeuron(neurons[i + 1]);
					}

				brain.createBrain(neurons, connections);

				return brain;
			}

		@Override
		protected void mousePressed(MouseEvent event)
			{
				if (event.getButton() == MouseEvent.BUTTON1)
					{
						for (Node n : nodes)
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
						for (Node n : nodes)
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
				public int type, ID, x, y;
				public boolean selected = false;

				public ArrayList<Node> connections = new ArrayList<Node>();

				private Node(int type, int x, int y)
					{
						this.type = type;
						this.x = x;
						this.y = y;
					}

				private final void setID(int ID)
					{
						this.ID = ID;
					}
			}
	}
