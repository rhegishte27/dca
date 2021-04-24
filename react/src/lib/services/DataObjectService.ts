import DataObject from '../domain/entities/DataObject';
import DataObjectContainer from '../domain/entities/DataObjectContainer';
import DataObjectFile from '../domain/entities/DataObjectFile';
import DataObjectRepository from '../domain/repositories/DataObjectRepository';

export default class DataObjectService {
    constructor(private dataObjectRepository: DataObjectRepository) {
    }

    async create(container: DataObjectContainer): Promise<DataObject[]> {
        return this.dataObjectRepository.create(container);
    }

    async import(dataObjects: DataObject[]): Promise<DataObject[]> {
        return this.dataObjectRepository.import(dataObjects);
    }

    async validate(dataObjectContainer: DataObjectContainer): Promise<DataObjectFile[]> {
        return this.dataObjectRepository.validate(dataObjectContainer);
    }

    async update(dataObject: DataObject): Promise<void> {
        return this.dataObjectRepository.update(dataObject);
    }

    async delete(id: string): Promise<void> {
        return this.dataObjectRepository.delete(id);
    }

    async findAll(): Promise<DataObject[]> {
        return this.dataObjectRepository.findAll();
    }

    async findById(id: string): Promise<DataObject> {
        return this.dataObjectRepository.findById(id);
    }
}
