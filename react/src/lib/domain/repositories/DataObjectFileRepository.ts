import DataObjectFile from '../entities/DataObjectFile';

export default interface DataObjectFileRepository {
    findLatestFileByDataObjectId(dataObjectId: string): Promise<DataObjectFile>;
}