package neuralWeb;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import tCode.RenderableObject;
import tComponents.components.TButton;
import tComponents.components.TLabel;
import tComponents.components.TMenu;
import tComponents.components.TRadioButton;
import tComponents.components.TTextField;
import tComponents.utils.RadioButtonsCollection;
import tComponents.utils.events.TActionEvent;
import testEnvironment.Main;
import tools.ColTools;
import tools.DrawTools;
import tools.NumTools;
import tools.RandTools;
import tools.WindowTools;

public class NeuralWebEditor extends RenderableObject
	{
		/*---------------------------------------------*/
		// Variables that deal with internal NeuralWeb structure
		/**/private ArrayList<Neuron> inputs = new ArrayList<Neuron>();
		/**/private ArrayList<Neuron> outputs = new ArrayList<Neuron>();
		/**/private ArrayList<Neuron> neurons = new ArrayList<Neuron>();
		/**/private final ArrayList<Cluster> clusters = new ArrayList<Cluster>();
		/**/
		/**/private Cluster inputCluster;
		/**/private Cluster outputCluster;
		/**/
		/**/private int numClusters = -1;
		/*---------------------------------------------*/
		// Variables that deal with the modification of the NeuralWeb
		/**/private Cluster selectedCluster = null;
		/**/private Neuron selectedNeuron = null;
		/**/private Connection selectedConnection = null;
		/**/private boolean structureBeingMoved = false;
		/*---------------------------------------------*/
		// Variables that deal with user interaction
		/**/private TMenu mouseInteractionMenu;
		/**/private final RadioButtonsCollection leftClickSelections = new RadioButtonsCollection();
		/**/private final RadioButtonsCollection rightClickSelections = new RadioButtonsCollection();
		/**/private final RadioButtonsCollection mouseDragSelections = new RadioButtonsCollection();
		/**/private final TRadioButton leftSelectButton = new TRadioButton("Select");
		/**/private final TRadioButton rightConnectButton = new TRadioButton("Connect");
		/**/private final TRadioButton rightDeleteButton = new TRadioButton("Delete");
		/**/private final TRadioButton dragMoveButton = new TRadioButton("Move");
		/**/private final TRadioButton dragDeleteButton = new TRadioButton("Delete");
		/**/
		/**/private TMenu keyInteractionMenu;
		/**/private final TTextField expandContractKey = new TTextField(0, 0, 18, 21, 1);
		/**/private final TTextField newNeuronKey = new TTextField(0, 0, 18, 21, 1);
		/**/private final TTextField newClusterKey = new TTextField(0, 0, 18, 21, 1);
		/**/private final TTextField deleteKey = new TTextField(0, 0, 18, 21, 1);
		/**/private final TButton newInputButton = new TButton("New Input");
		/**/private final TButton newOutputButton = new TButton("New Output");
		/**/
		/**/private int mouseX, mouseY;
		/*---------------------------------------------*/
		// Variables that deal with drawing the NeuralWeb
		/**/private boolean redraw = false;

		/*---------------------------------------------*/

		public NeuralWebEditor()
			{
				// Add output Cluster
				inputCluster = new Cluster(0, 30);
				inputCluster.colour = Color.WHITE;
				inputCluster.expanded = true;

				// Add input cluster
				outputCluster = new Cluster(0, Main.canvasHeight - 30);
				outputCluster.colour = Color.WHITE;
				outputCluster.expanded = true;

				// Add a starting cluster
				Cluster startingCluster = new Cluster(400, 250);

				// Add clusters to Editor
				clusters.add(inputCluster);
				clusters.add(outputCluster);
				clusters.add(startingCluster);

				// Select the starting cluster
				selectCluster(startingCluster);
			}

		public NeuralWebEditor(int[] genes){}
//			{
//				int totalNeurons = genes[0] + genes[1] + genes[2];
//				
//				HashMap<Integer, Cluster> mappedClusters = new HashMap<Integer, Cluster>();
//				for (int i = 3; i < 3 + (4 * totalNeurons);i+=4)
//					;
//
//				HashMap<Integer, Neuron> mappedNeurons = new HashMap<Integer, Neuron>(totalNeurons);
//
//				// extract all neurons from the genes
//				int geneIndex = 3;
//				for (int i = 0; i < totalNeurons; i++)
//					{
//						mappedNeurons.put(genes[geneIndex], new Neuron());
//						 = n.ID;
//						genes[geneIndex + 1] = n.cluster.clusterNum;
//						genes[geneIndex + 2] = n.threshold;
//						genes[geneIndex + 3] = n.startingLevel;
//
//						geneIndex += 4;
//					}
//
//				for (Neuron n : mappedNeurons.values())
//					for (Connection c : n.outgoingConnections)
//						{
//							genes[geneIndex] = c.source.ID;
//							genes[geneIndex + 1] = c.destination.ID;
//							genes[geneIndex + 2] = c.strength;
//
//							geneIndex += 3;
//						}
//
//				for (int i = 0; i < inputs.size(); i++)
//					{
//						inputs.get(i).ID = i;
//						mappedNeurons.put(i, inputs.get(i));
//					}
//
//				// output IDs start from -1 and decrease
//				for (int i = 0; i < outputs.size(); i++)
//					{
//						outputs.get(i).ID = -i - 1;
//						mappedNeurons.put(-i - 1, outputs.get(i));
//					}
//
//				// neuron IDs start after input IDs and increase
//				for (int i = 0; i < neurons.size(); i++)
//					{
//						neurons.get(i).ID = i + inputs.size();
//						mappedNeurons.put(i + inputs.size(), neurons.get(i));
//					}
//
//				// Calculate the number of connections
//				int numConnections = 0;
//				for (Neuron n : mappedNeurons.values())
//					numConnections += n.outgoingConnections.size();
//
//				// initiate the genes array to the exact length
//				int[] genes = new int[3 + (mappedNeurons.size() * 4) + (numConnections * 3)];
//
//				genes[0] = inputs.size();
//				genes[1] = outputs.size();
//				genes[2] = neurons.size();
//
//				int geneIndex = 3;
//				for (Neuron n : mappedNeurons.values())
//					{
//						genes[geneIndex] = n.ID;
//						genes[geneIndex + 1] = n.cluster.clusterNum;
//						genes[geneIndex + 2] = n.threshold;
//						genes[geneIndex + 3] = n.startingLevel;
//
//						geneIndex += 4;
//					}
//
//				for (Neuron n : mappedNeurons.values())
//					for (Connection c : n.outgoingConnections)
//						{
//							genes[geneIndex] = c.source.ID;
//							genes[geneIndex + 1] = c.destination.ID;
//							genes[geneIndex + 2] = c.strength;
//
//							geneIndex += 3;
//						}
//			}

		@Override
		protected void initiate()
			{
				mouseInteractionMenu = new TMenu(0, 0, Main.canvasWidth, 23, TMenu.HORIZONTAL);
				keyInteractionMenu = new TMenu(0, Main.canvasHeight - 23, Main.canvasWidth, 23, TMenu.HORIZONTAL);

				// Minimise wasted space in interactionMenus and align left
				mouseInteractionMenu.setBorderSize(1);
				mouseInteractionMenu.setTComponentSpacing(3);
				mouseInteractionMenu.setTComponentAlignment(TMenu.ALIGN_START);
				keyInteractionMenu.setBorderSize(1);
				keyInteractionMenu.setTComponentSpacing(3);
				keyInteractionMenu.setTComponentAlignment(TMenu.ALIGN_START);

				// Group RadioButtons in mouseInteraction menu
				leftClickSelections.add(leftSelectButton);
				rightClickSelections.add(rightConnectButton);
				rightClickSelections.add(rightDeleteButton);
				mouseDragSelections.add(dragMoveButton);
				mouseDragSelections.add(dragDeleteButton);

				// Add TComponents to mouseInteractionMenu
				mouseInteractionMenu.add(new TLabel("Left Click:"));
				mouseInteractionMenu.add(leftSelectButton);
				mouseInteractionMenu.add(new TLabel("Right Click:"));
				mouseInteractionMenu.add(rightConnectButton);
				mouseInteractionMenu.add(rightDeleteButton);
				mouseInteractionMenu.add(new TLabel("Mouse Drag:"));
				mouseInteractionMenu.add(dragMoveButton);
				mouseInteractionMenu.add(dragDeleteButton);

				// Add TComponents to keyInteractionMenu
				keyInteractionMenu.add(new TLabel("Show/Hide key"));
				keyInteractionMenu.add(expandContractKey, false);
				keyInteractionMenu.add(new TLabel("New Neuron key"));
				keyInteractionMenu.add(newNeuronKey, false);
				keyInteractionMenu.add(new TLabel("New Cluster key"));
				keyInteractionMenu.add(newClusterKey, false);
				keyInteractionMenu.add(new TLabel("Delete key"));
				keyInteractionMenu.add(deleteKey, false);
				keyInteractionMenu.add(newInputButton, false);
				keyInteractionMenu.add(newOutputButton, false);

				// Add interactionMenus to RenderableObject
				add(mouseInteractionMenu);
				add(keyInteractionMenu);

				// Set starting mouse interactions
				leftSelectButton.setChecked(true);
				rightConnectButton.setChecked(true);
				dragMoveButton.setChecked(true);

				// set starting key interactions
				expandContractKey.setText("s");
				newNeuronKey.setText("n");
				newClusterKey.setText("c");
				deleteKey.setText("d");

				organiseInputs();
				organiseOutputs();
			}

		@Override
		public void tick(double secondsPassed)
			{}

		@Override
		protected void render(Graphics2D g)
			{
				// Keep framerate in check
				if (redraw)
					{
						g.setColor(Color.BLACK);
						g.fillRect(0, 0, Main.canvasWidth, Main.canvasHeight);

						for (Cluster c : clusters)
							c.render(g);

						for (Neuron n : inputs)
							n.render(g);

						for (Neuron n : neurons)
							n.render(g);

						for (Neuron n : outputs)
							n.render(g);

						redraw = false;
					}
			}

		public final int[] getGenes()
			{
				HashMap<Integer, Neuron> mappedNeurons = new HashMap<Integer, Neuron>(inputs.size() + outputs.size() + neurons.size());

				// input IDs start from 0 and increase
				for (int i = 0; i < inputs.size(); i++)
					{
						inputs.get(i).ID = i;
						mappedNeurons.put(i, inputs.get(i));
					}

				// output IDs start from -1 and decrease
				for (int i = 0; i < outputs.size(); i++)
					{
						outputs.get(i).ID = -i - 1;
						mappedNeurons.put(-i - 1, outputs.get(i));
					}

				// neuron IDs start after input IDs and increase
				for (int i = 0; i < neurons.size(); i++)
					{
						neurons.get(i).ID = i + inputs.size();
						mappedNeurons.put(i + inputs.size(), neurons.get(i));
					}

				// Calculate the number of connections
				int numConnections = 0;
				for (Neuron n : mappedNeurons.values())
					numConnections += n.outgoingConnections.size();

				// initiate the genes array to the exact length
				int[] genes = new int[3 + (mappedNeurons.size() * 4) + (numConnections * 3)];

				genes[0] = inputs.size();
				genes[1] = outputs.size();
				genes[2] = neurons.size();

				int geneIndex = 3;
				for (Neuron n : mappedNeurons.values())
					{
						genes[geneIndex] = n.ID;
						genes[geneIndex + 1] = n.cluster.clusterNum;
						genes[geneIndex + 2] = n.threshold;
						genes[geneIndex + 3] = n.startingLevel;

						geneIndex += 4;
					}

				for (Neuron n : mappedNeurons.values())
					for (Connection c : n.outgoingConnections)
						{
							genes[geneIndex] = c.source.ID;
							genes[geneIndex + 1] = c.destination.ID;
							genes[geneIndex + 2] = c.strength;

							geneIndex += 3;
						}

				return genes;
			}

		private final void selectCluster(Cluster c)
			{
				selectedCluster = c;
				selectedNeuron = null;
				selectedConnection = null;

				structureBeingMoved = true;

				redraw = true;
			}

		private final void selectNeuron(Neuron n)
			{
				selectedCluster = null;
				selectedNeuron = n;
				selectedConnection = null;

				// Cannot move inputs or outputs
				if (n != null && n.cluster.clusterNum > 0)
					structureBeingMoved = true;

				redraw = true;
			}

		private final void selectConnection(Connection c)
			{
				selectedCluster = null;
				selectedNeuron = null;
				selectedConnection = c;

				redraw = true;
			}

		/**
		 * The aim of this method is to check and return a colour for a cluster
		 * to ensure that it is easily visible on a black background.
		 */
		private final Color getCheckedColor(Color colour)
			{
				int rgb[] = { colour.getRed(), colour.getGreen(), colour.getBlue() };

				// if too light - darken
				while (rgb[0] + rgb[1] + rgb[2] < 200)
					rgb[RandTools.getInt(0, 2)] += 5;
				// if too dark lighten
				while (rgb[0] + rgb[1] + rgb[2] > 615)
					rgb[RandTools.getInt(0, 2)] -= 5;

				colour = new Color(rgb[0], rgb[1], rgb[2]);

				return colour;
			}

		private final void organiseInputs()
			{
				int spacing = Main.canvasWidth / (inputs.size() + 1);
				for (int i = 0; i < inputs.size(); i++)
					inputs.get(i).x = spacing * (i + 1);
			}

		private final void organiseOutputs()
			{
				int spacing = Main.canvasWidth / (outputs.size() + 1);
				for (int i = 0; i < outputs.size(); i++)
					outputs.get(i).x = spacing * (i + 1);
			}

		@Override
		public void mousePressed(MouseEvent e)
			{
				int x = e.getX();
				int y = e.getY();

				switch (e.getButton())
					{
						case MouseEvent.BUTTON1:
							if (leftSelectButton.isChecked())
								{
									for (Cluster c : clusters)
										if (c.collides(x, y))
											{
												selectCluster(c);
												return;
											}

									for (Neuron n : neurons)
										{
											if (n.collides(x, y))
												{
													selectNeuron(n);
													return;
												}
											for (Connection c : n.outgoingConnections)
												if (c.collides(x, y))
													{
														selectConnection(c);
														return;
													}
										}

									for (Neuron n : inputs)
										{
											if (n.collides(x, y))
												{
													selectNeuron(n);
													return;
												}
											for (Connection c : n.outgoingConnections)
												if (c.collides(x, y))
													{
														selectConnection(c);
														return;
													}
										}

									for (Neuron n : outputs)
										{
											if (n.collides(x, y))
												{
													selectNeuron(n);
													return;
												}
											for (Connection c : n.outgoingConnections)
												if (c.collides(x, y))
													{
														selectConnection(c);
														return;
													}
										}
								}
							break;
						case MouseEvent.BUTTON3:
							if (rightConnectButton.isChecked())
								{
									for (Neuron n : neurons)
										{
											if (n.collides(x, y) && n != selectedNeuron)
												{
													new Connection(selectedNeuron, n);
													return;
												}
										}

									for (Neuron n : inputs)
										{
											if (n.collides(x, y) && n != selectedNeuron)
												{
													new Connection(selectedNeuron, n);
													return;
												}
										}

									for (Neuron n : outputs)
										{
											if (n.collides(x, y) && n != selectedNeuron)
												{
													new Connection(selectedNeuron, n);
													return;
												}
										}
								}
							else if (rightDeleteButton.isChecked())
								{
									for (Cluster c : clusters)
										if (c.collides(x, y))
											{
												c.delete();
												return;
											}

									for (Neuron n : neurons)
										{
											if (n.collides(x, y))
												{
													n.delete();
													return;
												}
											for (Connection c : n.outgoingConnections)
												if (c.collides(x, y))
													{
														c.delete();
														return;
													}
										}

									for (Neuron n : inputs)
										{
											if (n.collides(x, y))
												{
													n.delete();
													return;
												}
											for (Connection c : n.outgoingConnections)
												if (c.collides(x, y))
													{
														c.delete();
														return;
													}
										}

									for (Neuron n : outputs)
										{
											if (n.collides(x, y))
												{
													n.delete();
													return;
												}
											for (Connection c : n.outgoingConnections)
												if (c.collides(x, y))
													{
														c.delete();
														return;
													}
										}
								}
					}
			}

		@Override
		public void mouseReleased(MouseEvent e)
			{
				structureBeingMoved = false;
			}

		@Override
		public void mouseMoved(MouseEvent e)
			{
				mouseX = e.getX();
				mouseY = e.getY();
			}

		@Override
		public void mouseDragged(MouseEvent e)
			{
				mouseX = e.getX();
				mouseY = e.getY();

				if (dragMoveButton.isChecked())
					{
						if (structureBeingMoved)
							{
								int x = Math.min(Main.canvasWidth - 11, Math.max(10, e.getX()));
								int y = Math.min(Main.canvasHeight - 45, Math.max(45, e.getY()));

								if (selectedNeuron != null)
									{
										selectedNeuron.x = x;
										selectedNeuron.y = y;
									}
								else if (selectedCluster != null)
									{
										selectedCluster.x = x;
										selectedCluster.y = y;
									}
								redraw = true;
							}
					}
				else if (dragDeleteButton.isChecked())
					{
						for (int i = 0; i < neurons.size(); i++)
							if (neurons.get(i).collides(mouseX, mouseY))
								{
									neurons.get(i).delete();
									i--;
								}

						for (int i = 0; i < inputs.size(); i++)
							if (inputs.get(i).collides(mouseX, mouseY))
								{
									inputs.get(i).delete();
									i--;
								}

						for (int i = 0; i < outputs.size(); i++)
							if (outputs.get(i).collides(mouseX, mouseY))
								{
									outputs.get(i).delete();
									i--;
								}

						for (int i = 0; i < clusters.size(); i++)
							if (clusters.get(i).collides(mouseX, mouseY))
								{
									clusters.get(i).delete();
									i--;
								}
					}
			}

		@Override
		public void keyPressed(KeyEvent e)
			{
				// Do not edit neural web while user is selecting key bindings
				if (expandContractKey.isActive() || newNeuronKey.isActive() || newClusterKey.isActive() || deleteKey.isActive())
					return;

				try
					{
						if (e.getKeyChar() == expandContractKey.getText().charAt(0))
							{
								if (selectedCluster != null && selectedCluster.clusterNum != 0)
									selectedCluster.expanded = !selectedCluster.expanded;
								else if (selectedNeuron != null)
									selectedNeuron.cluster.expanded = !selectedNeuron.cluster.expanded;

								redraw = true;
							}
						else if (e.getKeyChar() == newNeuronKey.getText().charAt(0))
							{
								Cluster cluster;

								if (selectedCluster != null)
									cluster = selectedCluster;
								else if (selectedNeuron != null)
									cluster = selectedNeuron.cluster;
								else if (clusters.size() > 2)
									{
										cluster = clusters.get(2);
									}
								else
									{
										WindowTools.informationWindow("Error: You cannot add a neuron without a cluster, please select a Neuron or a Cluster.", "Error");
										return;
									}

								int x = Math.min(Main.canvasWidth - 11, Math.max(10, mouseX));
								int y = Math.min(Main.canvasHeight - 33, Math.max(33, mouseY));

								neurons.add(new Neuron(x, y, cluster));

								redraw = true;
							}
						else if (e.getKeyChar() == newClusterKey.getText().charAt(0))
							{
								int x = Math.min(Main.canvasWidth - 11, Math.max(10, mouseX));
								int y = Math.min(Main.canvasHeight - 33, Math.max(33, mouseY));

								clusters.add(new Cluster(x, y));

								redraw = true;
							}
						else if (e.getKeyChar() == deleteKey.getText().charAt(0))
							{
								if (selectedCluster != null)
									selectedCluster.delete();
								else if (selectedNeuron != null)
									selectedNeuron.delete();
								else if (selectedConnection != null)
									selectedConnection.delete();

								redraw = true;
							}
					}
				catch (StringIndexOutOfBoundsException sioobe)
					{
						WindowTools.informationWindow("Invalid keybinding, please select a character for each action.", "Whoops!");
					}
			}

		@Override
		public final void tActionEvent(TActionEvent e)
			{
				Object source = e.getSource();
				if (source == newInputButton)
					{
						inputs.add(new Neuron(0, 30, inputCluster));
						organiseInputs();
					}
				else if (source == newOutputButton)
					{
						outputs.add(new Neuron(0, Main.canvasHeight - 30, outputCluster));
						organiseOutputs();
					}
			}

		private class Cluster
			{
				private int x, y, clusterNum;
				private Color colour = getCheckedColor(ColTools.randColour());

				private boolean expanded = false;

				private Cluster(int x, int y)
					{
						this.x = x;
						this.y = y;
						clusterNum = numClusters;

						if (clusterNum > 0)
							neurons.add(new Neuron(x, y, this));

						numClusters++;
					}

				private final void render(Graphics2D g)
					{
						if (!expanded)
							{
								g.setColor(colour);
								g.drawOval(x - 8, y - 8, 16, 16);
								g.drawOval(x - 6, y - 6, 12, 12);
								g.fillOval(x - 4, y - 4, 8, 8);

								if (isSelected())
									{
										g.setColor(Color.WHITE);
										g.drawOval(x - 10, y - 10, 20, 20);
									}
							}
					}

				private boolean collides(int x, int y)
					{
						if (expanded || NumTools.distance(x, y, this.x, this.y) > 16)
							return false;

						return true;
					}

				private boolean isSelected()
					{
						if (selectedCluster == this)
							return true;
						else
							return false;
					}

				private void delete()
					{
						if (this.clusterNum <= 0)
							return;

						if (!WindowTools.confirmationWindow("Are you sure you want to delete this cluster?", "Delete confirmation"))
							return;

						if (selectedCluster == this)
							{
								selectCluster(null);
								structureBeingMoved = false;
							}

						for (int i = 0; i < neurons.size(); i++)
							if (neurons.get(i).cluster == this)
								{
									neurons.remove(i);
									i--;
								}

						clusters.remove(this);

						redraw = true;
					}
			}

		private class Neuron
			{
				private Cluster cluster;
				private int ID, x, y, threshold = 1000, startingLevel = 0;

				private ArrayList<Connection> incomingConnections = new ArrayList<Connection>(), outgoingConnections = new ArrayList<Connection>();

				private Neuron(int x, int y, Cluster cluster)
					{
						this.x = x;
						this.y = y;
						this.cluster = cluster;

						redraw = true;
					}

				private int getX()
					{
						if (cluster.expanded)
							return x;
						else
							return cluster.x;
					}

				private int getY()
					{
						if (cluster.expanded)
							return y;
						else
							return cluster.y;
					}

				private void render(Graphics2D g)
					{
						if (cluster.expanded)
							{
								g.setColor(cluster.colour);
								g.fillOval(getX() - 4, getY() - 4, 8, 8);

								if (isSelected())
									{
										g.setColor(Color.WHITE);
										g.drawOval(x - 5, y - 5, 10, 10);
									}
							}
						for (Connection c : outgoingConnections)
							c.render(g);
					}

				private boolean collides(int x, int y)
					{
						if (!cluster.expanded || NumTools.distance(x, y, this.x, this.y) > 8)
							return false;

						return true;
					}

				private boolean isSelected()
					{
						if (selectedNeuron == this)
							return true;
						else
							return false;
					}

				private void delete()
					{
						while (!incomingConnections.isEmpty())
							incomingConnections.get(0).delete();

						while (!outgoingConnections.isEmpty())
							outgoingConnections.get(0).delete();

						if (!neurons.remove(this))
							{
								if (!inputs.remove(this))
									{
										outputs.remove(this);
										organiseOutputs();
									}
								else
									organiseInputs();
							}

						if (selectedNeuron == this)
							{
								selectNeuron(null);
								// structureBeingMoved = false;
							}

						redraw = true;
					}
			}

		private class Connection
			{
				private Neuron source, destination;
				private int strength = 1000;

				private Connection(Neuron source, Neuron Connection)
					{
						this.source = source;
						this.destination = Connection;

						source.outgoingConnections.add(this);
						destination.incomingConnections.add(this);

						redraw = true;
					}

				private final void render(Graphics2D g)
					{
						/*
						 * If this connection is between two neurons within the
						 * same unexpanded cluster it is not drawn.
						 */
						if (source.cluster == destination.cluster && !source.cluster.expanded)
							return;

						g.setColor(source.cluster.colour);
						DrawTools.drawArrow(source.getX(), source.getY(), destination.getX(), destination.getY(), g, 2);
					}

				private boolean collides(int x, int y)
					{
						/*
						 * If this connection is between two neurons within the
						 * same unexpanded cluster.
						 */
						if (source.cluster == destination.cluster && !source.cluster.expanded)
							return false;

						int connectionX = (source.getX() + destination.getX()) / 2;
						int connectionY = (source.getX() + destination.getX()) / 2;

						if (NumTools.distance(x, y, connectionX, connectionY) > 6)
							return false;

						return true;
					}

				private boolean isSelected()
					{
						if (selectedConnection == this)
							return true;
						else
							return false;
					}

				private void delete()
					{
						source.outgoingConnections.remove(this);
						destination.incomingConnections.remove(this);

						if (selectedConnection == this)
							selectConnection(null);

						redraw = true;
					}
			}
	}