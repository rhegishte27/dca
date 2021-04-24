import FileData from '../../domain/entities/FileData';
import DirectoryRepository from '../../domain/repositories/DirectoryRepository';
import { ApiGateway } from '../config/apiGateway';

export default class DirectoryApiRepository implements DirectoryRepository {
    readonly URL: string = '/directories';

    constructor(private api: ApiGateway) {
    }

    async getDirectories(root: string): Promise<FileData[]> {
        return this.api.getArray(this.URL + '/' + root, {outType: FileData});
    }

    async createDirectory(fileData: FileData): Promise<FileData> {
        return this.api.post(this.URL, fileData, {inType: FileData, outType: FileData});
    }

    async deleteFile(path: string): Promise<void> {
        return this.api.delete(this.URL + '/' + path);
    }
}