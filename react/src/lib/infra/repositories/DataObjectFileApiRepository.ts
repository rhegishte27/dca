import DataObjectFile from '../../domain/entities/DataObjectFile';
import DataObjectFileRepository from '../../domain/repositories/DataObjectFileRepository';
import { ApiGateway } from '../config/apiGateway';

export default class DataObjectFileApiRepository implements DataObjectFileRepository {
    readonly URL: string = '/dataobjectfiles';

    constructor(private api: ApiGateway) {
    }

    async findLatestFileByDataObjectId(dataObjectId: string): Promise<DataObjectFile> {
        return this.api.get(this.URL + '/' + dataObjectId, { outType: DataObjectFile });
    }
}