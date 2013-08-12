package testEnvironment;

import java.awt.Graphics2D;

import neuralWeb.NeuralWeb;

import tCode.RenderableObject;
import tComponents.components.TButton;
import tComponents.components.TMenu;
import tComponents.utils.events.TActionEvent;

public class MainMenu extends RenderableObject
	{
		private TMenu mainMenu;
		private final TButton brainEditorButton = new TButton("Gene Editor");

		/*
		 * { numInputs, numOutputs, totalNeuronNum, <neuronID, neuronCluster,
		 * neuronThreshold, neuronStartLevel>... , <connectionStartID,
		 * connectionEndID, connectionWeight>...}
		 */
		int[] genes = { 2, 2, 4, 0, 0, 1, 0, 1, 0, 1, 0, 2, 0, 1, 0, 3, 0, 1, 0, 0, 2, 1000, 1, 3, 1000 };

		@Override
		protected void initiate()
			{
				mainMenu = new TMenu(50, 0, Main.canvasWidth - 100, Main.canvasHeight, TMenu.VERTICAL);

				mainMenu.add(brainEditorButton);

				add(mainMenu);

				new NeuralWeb(genes);
			}

		@Override
		public void tick(double secondsPassed)
			{}

		@Override
		protected void render(Graphics2D g)
			{}

		@Override
		public void tActionEvent(TActionEvent e)
			{
				Object source = e.getSource();

				if (source == brainEditorButton)
					;
			}
	}