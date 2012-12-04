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
import TroysCode.hub;
import TroysCode.T.TButton;
import TroysCode.T.TMenu;
import TroysCode.T.TScrollEvent;
import animalSimulation.animal.Organism;
import brain.Effector;
import brain.Sensor;
import brain.addons.TestEffector;
import brain.addons.TestSensor;

public class MainMenu extends RenderableObject
	{
		private static final long serialVersionUID = 1L;

		private final TMenu mainMenu = new TMenu(0, 0, 300, 560, TMenu.VERTICAL);
		private final TButton designBrainButton = new TButton(0, 0, "Design a Brain");
		private final TButton testBrainButton = new TButton(0, 0, "Test Brain");
		
		/* Create brain options */
		Sensor[] sensors = {new TestSensor()};
		Effector[] effectors = {new TestEffector()};

		@Override
		protected void initiate()
			{
				addTComponent(mainMenu);
				mainMenu.addTButton(designBrainButton, true);
				mainMenu.addTButton(testBrainButton, true);

			}

		@Override
		protected void refresh()
			{
			}

		@Override
		protected void tick(double secondsPassed)
			{

			}

		@Override
		protected void renderObject(Graphics g)
			{
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, 800, 600);

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
				if (event.getSource() == designBrainButton)
					{
						hub.environment.startingOrganism = new Organism(20, 20);
						hub.creator.setBlueprintsToEdit(hub.environment.startingOrganism.brain.blueprints);
						changeRenderableObject(hub.creator);
					}
				else if (event.getSource() == testBrainButton && hub.environment.startingOrganism != null)
					{
						hub.environment.startingOrganism.brain.generateBrain();
						hub.environment.prepareSimulation();
						changeRenderableObject(hub.environment);
					}
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
