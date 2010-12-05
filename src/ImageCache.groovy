import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

import javax.imageio.ImageIO;

class ImageCache {
	List<CachedImage> images = []

	void setFiles(List<File> files) {
		images.clear()
		files.each { File file ->
			images << new CachedImage(file: file)
		}
	}
	
	CachedImage getImage(File file) {
		loadNext(file)
		def cachedImage = images.find { it.file == file}
		return cachedImage
	}
	
	void loadNext(File file) {
		int index = images.findIndexOf { it.file == file} + 1
		if (index >= images.size()) return
		new Thread({
			images[index].load()
		} as Runnable).start()
	}
	
	void removeFromCache(File file) {
		def cachedImage = images.find { it.file == file}
		cachedImage.image = null
	}
	
	
}


class CachedImage {
	File file
	BufferedImage image
	
	void load() {
		image = ImageIO.read(file)
	}
	
	BufferedImage getImage() {
		if (!image) {
			load()
		}
		return image
	}
	
}