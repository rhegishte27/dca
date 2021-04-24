import FileData from '../domain/entities/FileData';
import DirectoryRepository from '../domain/repositories/DirectoryRepository';

export default class DirectoryService {
    constructor(private directoryRepository: DirectoryRepository) {
    }

    async getDirectories(root: string): Promise<FileData[]> {
        return this.directoryRepository.getDirectories(root);
    }

    async createDirectory(currentPath: string, fileName: string): Promise<FileData> {
        const fileData: FileData = new FileData();
        fileData.path = currentPath;
        fileData.name = fileName;
        return this.directoryRepository.createDirectory(fileData);
    }

    async deleteFile(path: string): Promise<void> {
        return this.directoryRepository.deleteFile(path);
    }
}
