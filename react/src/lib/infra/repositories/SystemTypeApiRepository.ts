import SystemType from '../../domain/entities/SystemType';
import SystemTypeRepository from '../../domain/repositories/SystemTypeRepository';
import { ApiGateway } from '../config/apiGateway';

export default class SystemTypeApiRepository implements SystemTypeRepository {
    readonly URL: string = '/systemtypes';

    constructor(private api: ApiGateway) {
    }

    async findAll(): Promise<SystemType[]> {
        return this.api.getArray(this.URL, { outType: SystemType });
    }
}
