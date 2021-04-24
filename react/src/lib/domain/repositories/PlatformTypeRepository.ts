import PlatformType from '../entities/PlatformType';

export default interface PlatformTypeRepository {
    findAll(): Promise<PlatformType[]>;
}
