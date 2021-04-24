import LocationType from '../entities/LocationType';

export default interface LocationTypeRepository {
    findAll(): Promise<LocationType[]>;
}
