package animalSimulation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowEvent;

import TroysCode.RenderableObject;
import TroysCode.T.TScrollEvent;
import animalSimulation.animal.Organism;

public class Environment extends RenderableObject
	{
		private static final long serialVersionUID = 1L;

		private Tile[][] environmentMap = new Tile[800][600];

		Organism organism = null;

		public Environment()
			{
				for (int x = 0; x < 800; x++)
					for (int y = 0; y < 600; y++)
						{
							if (x < 3 || y < 3 || x > 777 || y > 556)
								environmentMap[x][y] = new Tile(false);
							else
								environmentMap[x][y] = new Tile(true);
						}
			}
		
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
				organism.tick(secondsPassed);
			}

		@Override
		protected void renderObject(Graphics g)
			{
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, 800, 600);
				g.setColor(Color.WHITE);
				g.fillRect(3, 3, 777, 556);

				g.setColor(Color.BLUE);
				g.fillOval(organism.x - 1, organism.y - 1, 3, 3);
			}

		public final boolean isFree(int x, int y)
			{
				boolean bool = environmentMap[x][y].isFree();
				return bool;
			}

		@Override
		protected void mousePressed(MouseEvent event)
			{

			}

		@Override
		protected void mouseReleased(MouseEvent event)
			{

			}

		@Override
		protected void mouseDragged(MouseEvent event)
			{

			}

		@Override
		protected void mouseMoved(MouseEvent event)
			{

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

		private class Tile
			{
				private Organism organism = null;
				private boolean isFree = true;

				private Tile(boolean free)
					{
						isFree = free;
					}

				private final boolean isFree()
					{
						return isFree;
					}

				private final void addOrganism(Organism organism)
					{
						if (isFree)
							{
								if (this.organism == null)
									this.organism = organism;

								isFree = false;
							}
					}

				private final void removeOrganism()
					{
						if (organism != null)
							{
								organism = null;
								isFree = true;
							}
					}
			}
	}
