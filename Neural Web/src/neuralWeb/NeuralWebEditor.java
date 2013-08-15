package neuralWeb;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import tCode.RenderableObject;
import tComponents.components.TLabel;
import tComponents.components.TMenu;
import tComponents.components.TRadioButton;
import tComponents.utils.RadioButtonsCollection;
import testEnvironment.Main;
import tools.ColTools;
import tools.DrawTools;

public class NeuralWebEditor extends RenderableObject
	{
		/*---------------------------------------------*/
		// Variables that deal with inputs and outputs
		/**/private int numInputs = 1, numOutputs = 1;
		/**/
		/**/private Cluster inputoutputCluster = new Cluster();
		/**/
		/**/private ArrayList<Neuron> inputs = new ArrayList<Neuron>(), outputs = new ArrayList<Neuron>();
		/**/
		/*---------------------------------------------*/

		/*---------------------------------------------*/
		// Variables that deal with internal NeuralWeb structure
		/**/private final ArrayList<Neuron> neurons = new ArrayList<Neuron>();
		/**/private final ArrayList<Cluster> clusters = new ArrayList<Cluster>();
		/**/
		/**/private int numClusters = 1;
		/**/
		/*---------------------------------------------*/

		/*---------------------------------------------*/
		// Variables that deal with the modification of the NeuralWeb
		/**/private Cluster selectedCluster = null;
		/**/private Neuron selectedNode = null;
		/**/private Connection selectedConnection = null;
		/**/
		/**/
		/*---------------------------------------------*/

		/*---------------------------------------------*/
		// Variables that deal with user interaction
		/**/private final TMenu leftClickMenu = new TMenu(0, 0, Main.canvasWidth, 20, TMenu.HORIZONTAL);
		/**/private final TMenu rightClickMenu = new TMenu(0, 20, Main.canvasWidth, 20, TMenu.HORIZONTAL);
		/**/private final TMenu mouseDragMenu = new TMenu(0, 40, Main.canvasWidth, 20, TMenu.HORIZONTAL);
		/**/private final RadioButtonsCollection leftClickSelections = new RadioButtonsCollection();
		/**/private final RadioButtonsCollection rightClickSelections = new RadioButtonsCollection();
		/**/private final RadioButtonsCollection mouseDragSelections = new RadioButtonsCollection();
		/**/private final TRadioButton leftSelectButton = new TRadioButton("Select");
		/**/private final TRadioButton rightConnectButton = new TRadioButton("Connect");
		/**/private final TRadioButton rightDeleteButton = new TRadioButton("Delete");
		/**/private final TRadioButton dragMoveButton = new TRadioButton("Move");
		/**/private final TRadioButton dragDeleteButton = new TRadioButton("Delete");
		/**/
		/*---------------------------------------------*/

		/*---------------------------------------------*/
		// Variables that deal with drawing the NeuralWeb
		/**/private double frameRate = 1.0 / 60.0;
		/**/private double countDown = frameRate;
		/**/private boolean redraw = true;

		/*---------------------------------------------*/

		public NeuralWebEditor()
			{
				clusters.add(new Cluster(400, 250));

			}

		public NeuralWebEditor(int[][] genes)
			{

			}

		@Override
		protected void initiate()
			{
				// Minimise wasted space in radio button Menu's and align left
				leftClickMenu.setBorderSize(1);
				rightClickMenu.setBorderSize(1);
				mouseDragMenu.setBorderSize(1);
				leftClickMenu.setTComponentSpacing(3);
				rightClickMenu.setTComponentSpacing(3);
				mouseDragMenu.setTComponentSpacing(3);
				leftClickMenu.setTComponentAlignment(TMenu.ALIGN_START);
				rightClickMenu.setTComponentAlignment(TMenu.ALIGN_START);
				mouseDragMenu.setTComponentAlignment(TMenu.ALIGN_START);

				// Group RadioButtons
				leftClickSelections.add(leftSelectButton);
				rightClickSelections.add(rightConnectButton);
				rightClickSelections.add(rightDeleteButton);
				mouseDragSelections.add(dragMoveButton);
				mouseDragSelections.add(dragDeleteButton);

				// Add TButtons to TMenu's
				leftClickMenu.add(new TLabel("Left Click:"), false);
				leftClickMenu.add(leftSelectButton, false);
				rightClickMenu.add(new TLabel("Right Click:"), false);
				rightClickMenu.add(rightConnectButton, false);
				rightClickMenu.add(rightDeleteButton, false);
				mouseDragMenu.add(new TLabel("Mouse Drag:"), false);
				mouseDragMenu.add(dragMoveButton, false);
				mouseDragMenu.add(dragDeleteButton, false);

				// Add menu's to RenderableObject
				add(leftClickMenu);
				add(rightClickMenu);
				add(mouseDragMenu);

				// Set starting mouse interactions
				leftSelectButton.setChecked(true);
				rightConnectButton.setChecked(true);
				dragMoveButton.setChecked(true);
			}

		@Override
		public void tick(double secondsPassed)
			{
				countDown -= secondsPassed;
				if (countDown < 0)
					{
						countDown = frameRate;
						redraw = true;
					}
			}

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

						for (Neuron n : neurons)
							n.render(g);

						redraw = false;
					}
			}

		@Override
		public void mousePressed(MouseEvent e)
			{
				// if (actionSelectionButtons.)
			}

		private class Cluster
			{
				private int x, y, clusterNum;
				private Color colour = ColTools.randColour();

				private boolean expanded = false;

				private Cluster()
					{
						x = 0;
						y = 0;
						colour = Color.WHITE;
						expanded = true;
						clusterNum = 0;
					}

				private Cluster(int x, int y)
					{
						this.x = x;
						this.y = y;
						clusterNum = numClusters;
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
							}
					}
			}

		private class Neuron
			{
				private Cluster cluster;
				private int x, y, threshold, startingLevel;

				private ArrayList<Connection> incomingConnections = new ArrayList<Connection>(), outGoingConnections = new ArrayList<Connection>();

				private Neuron(int x, int y, Cluster cluster)
					{
						this.x = x;
						this.y = y;
						this.cluster = cluster;
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
							}

						for (Connection c : outGoingConnections)
							c.render(g);
					}

				private final boolean isSelected(int selectionX, int selectionY)
					{
						if (Math.abs(selectionX - getX()) > 10)
							return false;
						else if (Math.abs(selectionY - getY()) > 10)
							return false;

						return true;
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
					}

				private final void render(Graphics2D g)
					{
						DrawTools.drawArrow(source.getX(), source.getY(), destination.getX(), destination.getY(), g, 5);
					}

				private final boolean isSelected(int selectionX, int selectionY)
					{
						int connectionX = (source.getX() + destination.getX()) / 2;
						int connectionY = (source.getX() + destination.getX()) / 2;

						if (Math.abs(selectionX - connectionX) > 10)
							return false;
						else if (Math.abs(selectionY - connectionY) > 10)
							return false;

						return true;
					}
			}
	}