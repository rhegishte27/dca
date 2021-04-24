import TypeProjectElement from '../domain/entities/TypeProjectElement';
import TypeProjectElementRepository from '../domain/repositories/TypeProjectElementRepository';

export default class TypeProjectElementService {
    constructor(private typeProjectElementRepository: TypeProjectElementRepository) {
    }

    async findAll(): Promise<TypeProjectElement[]> {
        return this.typeProjectElementRepository.findAll();
    }
}