import DataObjectFile from '../domain/entities/DataObjectFile';
import DataObjectFileRepository from '../domain/repositories/DataObjectFileRepository';

export default class DataObjectFileService {
    constructor(private repository: DataObjectFileRepository) {
    }

    async findLatestFileByDataObjectId(dataObjectId: string): Promise<DataObjectFile> {
        return this.repository.findLatestFileByDataObjectId(dataObjectId);
    }
}