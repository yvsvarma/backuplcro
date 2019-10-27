package utils;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
 
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class FileWatcher {
 
    public static boolean watch(String path,String extension) {
    	int counter=0;
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            Path dir = Paths.get(path);
            dir.register(watcher, ENTRY_CREATE);
              
            while (true) {
                WatchKey key;
                try {
                    key = watcher.take();
                } catch (InterruptedException ex) {
                    return false;
                }
                 
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                     
                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();
                     
                    if (kind == ENTRY_CREATE &&  fileName.toString().contains(extension)) {
                        BasicUtil.log("FileWatcher","File Have beeen created successfully : "+fileName.toString());
                        return true;
                    }else{
                    	BasicUtil.wait(1);
                    	if(counter>20)
                    		return false;
                    	else 
                    		counter++;
                    }
                }
                 
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
             
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return false;
    }
}
