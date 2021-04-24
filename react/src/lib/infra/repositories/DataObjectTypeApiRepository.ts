import DataObjectType from '../../domain/entities/DataObjectType';
import PlatformType from '../../domain/entities/PlatformType';
import DataObjectTypeRepository from '../../domain/repositories/DataObjectTypeRepository';
import { ApiGateway } from '../config/apiGateway';

export default class DataObjectTypeApiRepository implements DataObjectTypeRepository {
    readonly URL: string = '/dataobjecttypes';

    constructor(private api: ApiGateway) {
    }

    async findAll(): Promise<DataObjectType[]> {
        return this.api.getArray(this.URL, { outType: PlatformType });
    }
}