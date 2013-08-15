package testEnvironment;

import tCode.RenderableObject;
import tCode.TCode;

public class Main extends TCode
	{
		RenderableObject mainMenu = new MainMenu();

		public Main(int width, int height, boolean framed, boolean resizable)
			{
				super(width, height, framed, resizable);
				
				TCode.programName = "Neural Web Demo";
				TCode.versionNumber = "Pre Alpha: 0.01";
				TCode.DEBUG = true;
				
				this.begin(mainMenu);
			}

		public static void main(String[] args)
			{
				new Main(900, 700, true, false);
			}
	}