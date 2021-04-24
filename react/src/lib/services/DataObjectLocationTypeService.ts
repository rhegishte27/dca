import DataObjectLocationType from '../domain/entities/DataObjectLocationType';
import DataObjectLocationTypeRepository from '../domain/repositories/DataObjectLocationTypeRepository';

export default class DataObjectLocationTypeService {
    constructor(private repository: DataObjectLocationTypeRepository) {
    }

    async findAll(): Promise<DataObjectLocationType[]> {
        return this.repository.findAll();
    }
}