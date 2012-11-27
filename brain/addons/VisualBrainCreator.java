package brain.addons;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowEvent;

import TroysCode.RenderableObject;
import TroysCode.Tools;
import TroysCode.hub;
import TroysCode.T.TScrollEvent;
import brain.BrainBlueprint;
import brain.NeuronBlueprint;

public class VisualBrainCreator extends RenderableObject
	{
		private static final long serialVersionUID = 1L;

		private int mouseX = 0;
		private int mouseY = 0;

		private boolean placeNode = false;
		private boolean placeInhibitingNode = false;
		private boolean removeNode = false;
		private boolean connectNode = false;
		private boolean moveNode = false;
		private boolean clearConnections = false;

		private NeuronBlueprint selectedNode = new NeuronBlueprint(BrainBlueprint.TYPE_NEURON, 0, 0);
		private NeuronBlueprint secondNode = new NeuronBlueprint(BrainBlueprint.TYPE_NEURON, 0, 0);

		private boolean showLabels = true;
		private boolean showThresholds = true;
		private boolean showHelp = true;

		public BrainBlueprint blueprints;

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
						blueprints.addNeuronNode(new NeuronBlueprint(BrainBlueprint.TYPE_NEURON, mouseX, mouseY), false);
						placeNode = false;
					}
				if (placeInhibitingNode)
					{
						blueprints.addNeuronNode(new NeuronBlueprint(BrainBlueprint.TYPE_NEURON, mouseX, mouseY), true);
						placeInhibitingNode = false;
					}
				if (removeNode)
					{
						blueprints.removeNeuronNode(selectedNode);
						selectedNode = new NeuronBlueprint(BrainBlueprint.TYPE_NEURON, 0, 0);

						removeNode = false;
					}
				if (connectNode)
					{
						blueprints.makeConnection(selectedNode, secondNode);

						connectNode = false;
					}
				else if (moveNode)
					{
						if (mouseX < 3 || mouseY < 3 || mouseX > 777 || mouseY > 556)
							{
								if (blueprints.neuronNodes.size() > 1 && blueprints.neuronNodes.contains(selectedNode))
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
						blueprints.clearConnections(selectedNode);

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
						g.drawString("Press 'n' to add a new normal Neuron.", 4, ++i * 15);
						g.drawString("Press 'i' to add an inhibiting Neuron.", 4, ++i * 15);
						g.drawString("Click on a Neuron and drag to move it around (purely cosmetic!).", 4, ++i * 15);
						g.drawString("Right click on another Neuron to connect it to the selected Neuron.", 4, ++i * 15);
						g.drawString("Press 'd' to delete the selected Neuron.", 4, ++i * 15);
						g.drawString("Press 'c' to clear the selected Neuron's Connections.", 4, ++i * 15);
						g.drawString("Press '+' to increase a Neuron's threshold.", 4, ++i * 15);
						g.drawString("Press '-' to decrease a Neuron's threshold.", 4, ++i * 15);
						g.drawString("Press '0' or '1' to reset a Neuron's threshold.", 4, ++i * 15);
						g.drawString("Press 't' to show/hide Neuron thresholds.", 4, ++i * 15);
						g.drawString("Press 'l' to show/hide Neuron labels.", 4, ++i * 15);
						g.drawString("Press 'm' to go to main menu.", 4, ++i * 15);
						g.drawString("Press 'h' to show/hide the help.", 4, ++i * 15);
						g.drawString("", 4, ++i * 15);
						g.drawString("", 4, ++i * 15);
						g.drawString("", 4, ++i * 15);
						g.drawString("The neurons threshold is how many times it must be triggered before", 4, ++i * 15);
						g.drawString("it 'fires / 'depolorises", 4, ++i * 15);
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
						for (NeuronBlueprint n : blueprints.sensorNodes)
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

								if (showLabels)
									{
										g.setColor(Color.WHITE);
										g.drawString(n.description, n.x + 7, n.y + 5);
									}
								if (showThresholds)
									{
										g.setColor(Color.WHITE);
										g.drawString("" + n.getThreshold(), n.x - 12, n.y - 7);
									}
							}
						i = 0;
						for (NeuronBlueprint n : blueprints.neuronNodes)
							{
								if (n.selected)
									{
										g.setColor(Color.WHITE);
										g.fillOval(n.x - 7, n.y - 7, 14, 14);
									}

								if (n.inhibitor)
									g.setColor(Color.DARK_GRAY);
								else
									g.setColor(Color.BLUE);
								g.fillOval(n.x - 6, n.y - 6, 12, 12);

								g.setColor(Color.WHITE);
								g.drawString("" + i++, n.x - 3, n.y + 5);
								if (showThresholds)
									g.drawString("" + n.getThreshold(), n.x - 12, n.y - 7);
							}
						i = 0;
						for (NeuronBlueprint n : blueprints.effectorNodes)
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
								if (showLabels)
									g.drawString(n.description, n.x + 7, n.y + 5);
								if (showThresholds)
									g.drawString("" + n.getThreshold(), n.x - 12, n.y - 7);
							}

						g.setColor(Color.GREEN);
						for (NeuronBlueprint node : blueprints.sensorNodes)
							for (NeuronBlueprint connection : node.connections)
								Tools.drawArrow(node.x, node.y, connection.x, connection.y, g, 15);
						for (NeuronBlueprint node : blueprints.neuronNodes)
							for (NeuronBlueprint connection : node.connections)
								Tools.drawArrow(node.x, node.y, connection.x, connection.y, g, 15);
						for (NeuronBlueprint node : blueprints.effectorNodes)
							for (NeuronBlueprint connection : node.connections)
								Tools.drawArrow(node.x, node.y, connection.x, connection.y, g, 15);
					}
			}

		public final void setBlueprintsToEdit(BrainBlueprint blueprints)
			{
				this.blueprints = blueprints;
			}

		@Override
		protected void mousePressed(MouseEvent event)
			{
				if (event.getButton() == MouseEvent.BUTTON1)
					{
						for (NeuronBlueprint n : blueprints.sensorNodes)
							if (Tools.getVectorLength(n.x, n.y, event.getX(), event.getY()) < 6)
								{
									n.selected = true;
									selectedNode = n;
								}
							else
								n.selected = false;
						for (NeuronBlueprint n : blueprints.neuronNodes)
							if (Tools.getVectorLength(n.x, n.y, event.getX(), event.getY()) < 6)
								{
									n.selected = true;
									selectedNode = n;
								}
							else
								n.selected = false;
						for (NeuronBlueprint n : blueprints.effectorNodes)
							if (Tools.getVectorLength(n.x, n.y, event.getX(), event.getY()) < 6)
								{
									n.selected = true;
									selectedNode = n;
								}
							else
								n.selected = false;

						if (!selectedNode.selected)
							{
								selectedNode = new NeuronBlueprint(BrainBlueprint.TYPE_NEURON, 0, 0);
							}
					}
				else if (event.getButton() == MouseEvent.BUTTON3)
					{
						for (NeuronBlueprint n : blueprints.sensorNodes)
							if (Tools.getVectorLength(n.x, n.y, event.getX(), event.getY()) < 6)
								{
									secondNode = n;
									connectNode = true;
								}
						for (NeuronBlueprint n : blueprints.neuronNodes)
							if (Tools.getVectorLength(n.x, n.y, event.getX(), event.getY()) < 6)
								{
									secondNode = n;
									connectNode = true;
								}
						for (NeuronBlueprint n : blueprints.effectorNodes)
							if (Tools.getVectorLength(n.x, n.y, event.getX(), event.getY()) < 6)
								{
									secondNode = n;
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
				else if (event.getKeyChar() == 'i')
					placeInhibitingNode = true;
				else if (event.getKeyChar() == 'd')
					removeNode = true;
				else if (event.getKeyChar() == 'c')
					clearConnections = true;
				else if (event.getKeyChar() == '+')
					selectedNode.setThreshold(selectedNode.getThreshold() + 1);
				else if (event.getKeyChar() == '-')
					selectedNode.setThreshold(selectedNode.getThreshold() - 1);
				else if (event.getKeyChar() == '0' || event.getKeyChar() == '1')
					selectedNode.setThreshold(1);
				else if (event.getKeyChar() == 't')
					showThresholds = !showThresholds;
				else if (event.getKeyChar() == 'l')
					showLabels = !showLabels;
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
	}
