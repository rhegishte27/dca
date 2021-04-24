import SystemType from '../domain/entities/SystemType';
import SystemTypeRepository from '../domain/repositories/SystemTypeRepository';

export default class SystemTypeService {
    constructor(private systemTypeRepository: SystemTypeRepository) {
    }

    async findAll(): Promise<SystemType[]> {
        return this.systemTypeRepository.findAll();
    }
}
