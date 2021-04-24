import PlatformType from '../../domain/entities/PlatformType';
import PlatformTypeRepository from '../../domain/repositories/PlatformTypeRepository';
import { ApiGateway } from '../config/apiGateway';

export default class PlatformTypeApiRepository implements PlatformTypeRepository {
    readonly URL: string = '/platformtypes';

    constructor(private api: ApiGateway) {
    }

    async findAll(): Promise<PlatformType[]> {
        return this.api.getArray(this.URL, { outType: PlatformType });
    }
}
