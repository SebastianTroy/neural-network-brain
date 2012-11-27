package brain;

import java.util.ArrayList;


public class NeuronBlueprint
{
	public long ID = 0;
	public int type, x, y;
	private int threshold = 1;
	public boolean selected = false;
	public boolean inhibitor = false;
	public Neuron neuron;

	public String description = "";

	public ArrayList<NeuronBlueprint> connections = new ArrayList<NeuronBlueprint>();

	public NeuronBlueprint(int type, int x, int y)
		{
			this.type = type;
			this.x = x;
			this.y = y;
			threshold = 1;
		}
	
	public final void setID(long l)
		{
			ID = l;
		}
	
	public final void setThreshold(int newThreshold)
		{
			if (newThreshold < 1)
				newThreshold = 1;
			
			threshold = newThreshold;
		}
	
	public final int getThreshold()
		{
			return threshold;
		}
}
