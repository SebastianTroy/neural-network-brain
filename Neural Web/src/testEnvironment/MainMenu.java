package testEnvironment;

import java.awt.Graphics2D;

import neuralWeb.Input;
import neuralWeb.NeuralWeb;
import neuralWeb.Triggerable;

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
		int[] genes = { 2, 2, 4, 0, 0, 1, 0, 1, 0, 1, 0, -1, 0, 1, 0, -2, 0, 1, 0, 0, -1, 1000, 1, -2, 1000 };

		NeuralWeb web;
		Triggerable in1, in2, out1, out2;

		@Override
		protected void initiate()
			{
				mainMenu = new TMenu(50, 0, Main.canvasWidth - 100, Main.canvasHeight, TMenu.VERTICAL);

				mainMenu.add(brainEditorButton);

				add(mainMenu);

				web = new NeuralWeb(genes);

				in1 = new Input(web, 0);
				in2 = new Input(web, 1);
				web.attachOutput(out1 = new Output(), 0);
				web.attachOutput(out2 = new Output(), 1);
			}

		@Override
		public void tick(double secondsPassed)
			{
				web.calculate();
			}

		@Override
		protected void render(Graphics2D g)
			{}

		@Override
		public void tActionEvent(TActionEvent e)
			{
				Object source = e.getSource();

				if (source == brainEditorButton)
					in1.trigger();
			}
	}