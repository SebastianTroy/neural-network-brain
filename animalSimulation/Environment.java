package animalSimulation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import TroysCode.RenderableObject;
import TroysCode.Tools;
import TroysCode.hub;
import TroysCode.T.TScrollEvent;
import animalSimulation.animal.Organism;

public class Environment extends RenderableObject
	{
		private static final long serialVersionUID = 1L;

		private int width = 780;
		private int height = 559;
		private BufferedImage environmentMap1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		private final Color EMPTY = new Color(255, 255, 255);
		private final Color BLOCKADE = new Color(0, 0, 0);
		private final Color FOOD = new Color(255, 0, 255);

		Organism organism = null;

		public Environment()
			{
				Graphics g = environmentMap1.getGraphics();
				g.setColor(EMPTY);
				g.fillRect(3, 3, width - 6, height - 6);
				g.setColor(BLOCKADE);
				g.fillRect(0, 0, 3, height);
				g.fillRect(3, 0, width - 3, 3);
				g.fillRect(width - 3, 0, 3, height);
				g.fillRect(0, 0, 3, height);

				for (int i = 0; i < 500; i++)
					g.fillOval(Tools.randInt(-10, width + 10), Tools.randInt(-10, height + 10), Tools.randInt(1, 15), Tools.randInt(1, 15));
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
				g.drawImage(environmentMap1, 0, 0, hub.renderer);

				g.setColor(Color.BLUE);
				g.fillOval(organism.x - 1, organism.y - 1, 3, 3);
			}

		public final boolean isFree(int x, int y)
			{
				if (environmentMap1.getRGB(x, y) == EMPTY.getRGB())
					return true;

				return false;
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
	}
