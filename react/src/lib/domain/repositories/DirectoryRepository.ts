import FileData from '../entities/FileData';

export default interface DirectoryRepository {
    getDirectories(root: string): Promise<FileData[]>;

    createDirectory(fileData: FileData): Promise<FileData>;

    deleteFile(path: string): Promise<void>;
}