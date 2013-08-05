package testEnvironment;

import tCode.RenderableObject;
import tCode.TCode;

public class Main extends TCode
	{
		RenderableObject mainMenu = new MainMenu();

		public Main(int width, int height, boolean framed, boolean resizable)
			{
				super(width, height, framed, resizable);
				
				TCode.programName = "Brain Demo";
				TCode.versionNumber = "Pre Alpha: 0.0";
				
				this.begin(mainMenu);
			}

		public static void main(String[] args)
			{
				new Main(800, 600, true, false);
			}
	}