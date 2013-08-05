package testEnvironment;

import java.awt.Color;
import java.awt.Graphics2D;

import tCode.RenderableObject;

public class MainMenu extends RenderableObject
	{

		@Override
		protected void initiate()
			{
				// TODO Auto-generated method stub

			}

		@Override
		public void tick(double secondsPassed)
			{
				// TODO Auto-generated method stub

			}

		@Override
		protected void render(Graphics2D g)
			{
				g.setColor(Color.BLACK);
				g.drawString("This does nothing yet.", 50, 50);
			}

	}