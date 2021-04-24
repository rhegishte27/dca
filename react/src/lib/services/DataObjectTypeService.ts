import DataObjectType from '../domain/entities/DataObjectType';
import DataObjectTypeRepository from '../domain/repositories/DataObjectTypeRepository';

export default class DataObjectTypeService {
    constructor(private repository: DataObjectTypeRepository) {
    }

    async findAll(): Promise<DataObjectType[]> {
        return this.repository.findAll();
    }
}