import LocationType from '../domain/entities/LocationType';
import LocationTypeRepository from '../domain/repositories/LocationTypeRepository';

export default class LocationTypeService {
    constructor(private locationTypeRepository: LocationTypeRepository) {
    }

    async findAll(): Promise<LocationType[]> {
        return this.locationTypeRepository.findAll();
    }
}
