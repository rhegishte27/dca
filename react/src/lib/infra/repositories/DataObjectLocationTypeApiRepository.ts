import DataObjectLocationType from '../../domain/entities/DataObjectLocationType';
import PlatformType from '../../domain/entities/PlatformType';
import DataObjectLocationTypeRepository from '../../domain/repositories/DataObjectLocationTypeRepository';
import { ApiGateway } from '../config/apiGateway';

export default class DataObjectLocationTypeApiRepository implements DataObjectLocationTypeRepository {
    readonly URL: string = '/dataobjectlocationtypes';

    constructor(private api: ApiGateway) {
    }

    async findAll(): Promise<DataObjectLocationType[]> {
        return this.api.getArray(this.URL, { outType: PlatformType });
    }
}