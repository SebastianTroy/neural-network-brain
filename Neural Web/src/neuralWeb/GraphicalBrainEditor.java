package neuralWeb;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import tCode.RenderableObject;

public class GraphicalBrainEditor extends RenderableObject
	{
		private int numInputs = 1, numOutputs = 2;

		private ArrayList<Cluster> clusters = new ArrayList<Cluster>();

		private Cluster selectedCluster = null;
		private Node selectedNode = null;
		private Connection selectedConnection = null;

		public GraphicalBrainEditor()
			{

			}

		public GraphicalBrainEditor(int[][] genes)
			{

			}

		@Override
		protected void initiate()
			{}

		@Override
		public void tick(double secondsPassed)
			{}

		@Override
		protected void render(Graphics2D g)
			{
				g.setColor(Color.WHITE);
				
				// Draw the brain outline
				g.setColor(Color.BLACK);
				
				
				// draw the brain innards
				for (Cluster c : clusters)
					c.render(g);
			}

		private class Cluster
			{
				private int x, y, numInpust = 0, numOutputs = 0;

				private ArrayList<Node> inputs = new ArrayList<Node>(), outputs = new ArrayList<Node>(), nodes = new ArrayList<Node>();

				private Cluster(int x, int y)
					{
						this.x = x;
						this.y = y;
					}
				
				private final void render(Graphics2D g)
					{
						
					}
			}

		private class Node
			{
				private int x, y, threshold, startingLevel;

				private ArrayList<Connection> incomingConnections = new ArrayList<Connection>(), outGoingConnections = new ArrayList<Connection>();

				private Node(int x, int y)
					{
						this.x = x;
						this.y = y;
					}
			}

		private class Connection
			{
				private Node source, destination;
				private int strength = 1000;

				private Connection(Node source, Node Connection)
					{
						this.source = source;
						this.destination = Connection;
					}
			}
	}