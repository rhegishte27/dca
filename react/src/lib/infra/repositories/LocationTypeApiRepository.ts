import LocationType from '../../domain/entities/LocationType';
import LocationTypeRepository from '../../domain/repositories/LocationTypeRepository';
import { ApiGateway } from '../config/apiGateway';

export default class LocationTypeApiRepository implements LocationTypeRepository {
    readonly URL: string = '/locationtypes';

    constructor(private api: ApiGateway) {
    }

    async findAll(): Promise<LocationType[]> {
        return this.api.getArray(this.URL, { outType: LocationType });
    }
}
