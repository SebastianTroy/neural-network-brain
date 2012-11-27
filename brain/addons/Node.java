package brain.addons;

import java.util.ArrayList;

import brain.Neuron;

public class Node
{
	public long ID = 0;
	public int type, x, y;
	public boolean selected = false;
	public boolean inhibitor = false;
	public Neuron neuron;

	public String description = "";

	public ArrayList<Node> connections = new ArrayList<Node>();

	public Node(int type, int x, int y)
		{
			this.type = type;
			this.x = x;
			this.y = y;
		}
	
	public final void setID(long l)
		{
			ID = l;
		}
}
