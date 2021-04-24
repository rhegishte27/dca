import PlatformType from '../domain/entities/PlatformType';
import PlatformTypeRepository from '../domain/repositories/PlatformTypeRepository';

export default class PlatformTypeService {
    constructor(private platformTypeRepository: PlatformTypeRepository) {
    }

    async findAll(): Promise<PlatformType[]> {
        return this.platformTypeRepository.findAll();
    }
}
