package org.andy.linearj.FileSystem;

import java.nio.file.Path;

class File {
    private final String fileName;
    private final Path filePath;
    private final Path destinationPath;

    public File(String FILE_NAME, Path filePath, Path destinationPath ){
        this.fileName = FILE_NAME;
        this.filePath = filePath;
        this.destinationPath = destinationPath; //Optional Argument.
    }

    public String getFilename(){
        return this.fileName;
    }

    public Path getFilePath(){
        return this.filePath;
    }

    public Path getDestinationPath(){
        return this.destinationPath;
    }

    public boolean isValidFormat(final String FILE_NAME){
        if (this.fileName.contains(".spi") || this.fileName.contains(".spc") ){
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean isValidPath(final Path filePath){
        return true; //Placeholder
    }


}
