package view;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.function.Supplier;

public class MapSelection {

	private ArrayList<String> maps = new ArrayList<>();
	private MapSelectionItem[] mapSelectionItems;

	public MapSelection() {
		getMaps();
		this.mapSelectionItems = createItems(this.maps);
	}

	public void draw(Graphics g, Supplier<Integer> getWidth, Supplier<Integer> getHeight) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth.get(), getWidth.get());

		if (mapSelectionItems == null) {
			System.out.println(1);
			return;
		}

		String title = "Select a Map";
		int x_location = (g.getFontMetrics().stringWidth(title));

		g.setColor(Color.YELLOW);
		g.drawString(title, (getWidth.get() / 2) - (x_location / 2), 150);

		for (MapSelectionItem item : mapSelectionItems) {
			g.setColor(Color.WHITE);

			int width = g.getFontMetrics().stringWidth(item.getName().split("[.]")[0]);
			int height = g.getFontMetrics().getHeight();

			item.setDimension(new Dimension(width, height));
			item.setLocation(new Point((1280 + width) / 2, item.getLocation().y));
			g.drawString(item.getName().split("[.]")[0], (getWidth.get() / 2) - width,
					item.getLocation().y);
		}
	}

	private void getMaps() {
		File folder = new File("./src/media/maps/");
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			if (file.isFile() && file.getName().endsWith(".png")) {
				maps.add(file.getName());
			}
		}
	}

	private MapSelectionItem[] createItems(ArrayList<String> maps) {
		if (maps == null)
			return null;

		int defaultGridSize = 100;
		MapSelectionItem[] items = new MapSelectionItem[maps.size()];
		for (int i = 0; i < items.length; i++) {
			Point location = new Point(0, (i + 1) * defaultGridSize + 200);
			items[i] = new MapSelectionItem(maps.get(i), location);
		}

		return items;
	}

	public String selectMap(Point mouseLocation) {
		for (MapSelectionItem item : mapSelectionItems) {
			Dimension dimension = item.getDimension();
			Point location = item.getLocation();

			boolean inX = location.x <= mouseLocation.x && location.x + dimension.width >= mouseLocation.x;
			boolean inY = location.y >= mouseLocation.y && location.y - dimension.height <= mouseLocation.y;

			if (inX && inY) {
				return item.getName();
			}
		}
		return null;
	}

	public String selectMap(int index) {
		if (index < mapSelectionItems.length && index > -1)
			return mapSelectionItems[index].getName();
		return null;
	}

	public int changeSelectedMap(int index, boolean up) {
		if (up) {
			if (index <= 0)
				return mapSelectionItems.length - 1;
			else
				return index - 1;
		} else {
			if (index >= mapSelectionItems.length - 1)
				return 0;
			else
				return index + 1;
		}
	}
}
