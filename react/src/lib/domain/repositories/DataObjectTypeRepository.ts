import DataObjectType from '../entities/DataObjectType';

export default interface DataObjectTypeRepository {
    findAll(): Promise<DataObjectType[]>;
}