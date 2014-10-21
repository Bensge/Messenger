import java.awt.Color;


public class Settings implements SettingsListener{
	public static int UIChange = 0;
	public static int pathChange = 1;
	
	private boolean isDark = true;
	private String path;
	private GUI gui;
	
	public Settings(GUI gui){
		this.gui = gui;
	}
	
	@Override
	public void UIChanged() {
			isDark = !isDark;
			if(isDark){
				gui.setGUI(isDark);
				gui.messagesModel.getRenderer().changeBackground(true);
				gui.messagesModel.getRenderer().table.setBackground(gui.darkColor);
				gui.userModel.getRenderer().changeBackground(true);
			}
			else if(!isDark){
				gui.setGUI(isDark);
				gui.userModel.getRenderer().changeBackground(false);
				gui.messagesModel.getRenderer().table.setBackground(gui.brightColor);
				gui.messagesModel.getRenderer().changeBackground(false);
			}
			System.out.println("changed UI");
		}

	@Override
	public void pathChanged() {
		System.out.println("change path here");
		
	}
		
}
